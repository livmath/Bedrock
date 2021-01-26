using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

class ChunkFile: DataStream{

	private static readonly long MAX_PAGE_SIZE = 8192 *3;
	private static readonly long PAGE_GROWTH_ROOM = 512;
	private static readonly int MIN_PAGE_SIZE = 64;
	private static readonly int MAX_FILE_SIZE = (int) MAX_PAGE_SIZE;

	private static readonly string PAGES_PATH_EXT = "-pages.btf";

	private readonly string pagesPath;

	private readonly Hashtable usedPages = new Hashtable();
	private readonly List<ChunkPage> freePages = new List<ChunkPage>();

	// recycled tree for writing chunks
	private readonly Document chunkPageTree;
	private readonly Node terrainNode;

	private readonly string savePath;

	public ChunkFile(string path): base(new FileStream(path, FileMode.OpenOrCreate, FileAccess.ReadWrite)){

		savePath = path;

		pagesPath = path.Substring(0, path.LastIndexOf(".")) +PAGES_PATH_EXT;
		LoadPages();

		chunkPageTree = new BinaryTagDocument();
		chunkPageTree.tag = BinaryTag.Compound;

		terrainNode = new Node("terrain");
		terrainNode.tag = BinaryTag.Array;

		chunkPageTree.AppendChild(terrainNode);
	}

	public override void Close(){

		try{

			//SaveDefragmented()
			Save();
			base.Close();
		} catch(IOException ignored){}
	}

	public Chunk GetChunk(int x, int y){

		string chunkId = ChunkId(x, y);
		ChunkPage page = (ChunkPage) usedPages[chunkId];

		Chunk chunk = null;

		if(page.Equals(null)){ // chunk doesn't exist at all

			Debug.Log("Page is null!");
			page = AllocateFreePage(); // this page could be freed later on, just need an entry

			if(page.Equals(null)){

				Debug.Log("Page is again null!");
				return null;
			}

			chunk = new Chunk(x, y);

			page.chunk = chunk;
			chunk.page = page;

			usedPages.Add(chunkId, chunk.page);
		} else chunk = GetChunkFromPage(page);

		chunk.x = x;
		chunk.y = y;

		return chunk;
	}

	/**
	 * Be noted that this method does not set the x and y of the chunk.
	 * Anything that needs the coordinates should already have them.
	 */
	protected Chunk GetChunkFromPage(ChunkPage page){

		Chunk chunk = null;

		if(page.chunk == null){ // uuhh, we actually have to do some work. pray the disk isn't bogged up.


			Debug.Log("Reading chunk data!");

			try{

				Seek(page.offset);

				Debug.Log("Offset: " +page.offset);
				
				chunk = new Chunk();

				byte[] data = new byte[page.usedSize];
				Debug.Log("Size: " +page.usedSize);

				// read data

				BinaryTagStream chunkData = new BinaryTagStream(new MemoryStream(data));
				chunk.terrain = chunkData.FindProperty("terrain").value;

				chunkData.Close();

				page.chunk = chunk;
				chunk.page = page;
			} catch(IOException ignored){}
		} else chunk = page.chunk; // yay, it's already been loaded :)

		return chunk;
	}

	private byte[] EncodeChunk(Chunk chunk)
	{

		terrainNode.value = chunk.terrain;
		return chunkPageTree.Compile();
	}

	// room for optimisation where if it hasn't been loaded into memory send raw disk read
	public byte[] GetChunkEncoded(int x, int y)
	{
		return EncodeChunk(GetChunk(x, y));
	}

	/**
	 *	This is fairly more expensive than a regular save,
	 *	so only use this when it won't be needed for some time.
	 *	All chunk data is encoded & rewritten.
	 */
	private void SaveDefragmented(){

		string tmpPath = savePath +".tmp";
		FileStream stream = new FileStream(tmpPath, FileMode.OpenOrCreate, FileAccess.Read);

		long offset = 0;

		for(IDictionaryEnumerator e = usedPages.GetEnumerator(); e.MoveNext();){

			ChunkPage page = (ChunkPage) e.Current;
			Chunk chunk = GetChunkFromPage(page);

			page.offset = offset;

			terrainNode.value = chunk.terrain;
			byte[] encodedData = chunkPageTree.Compile();

			page.size = encodedData.Length +PAGE_GROWTH_ROOM;
			page.usedSize = encodedData.Length;
			offset += page.size;

			stream.Write(encodedData, 0, encodedData.Length);

			// pad rest of page with zeros
			byte[] pad = new byte[(int)page.size - page.usedSize];
			stream.Write(pad, 0, pad.Length);
		}

		stream.Close();

		freePages.Clear();

		ChunkPage freePage = new ChunkPage();
		freePage.offset = offset;
		Debug.Log("New single free page offset is: " +offset);
		freePage.size = MAX_FILE_SIZE -offset;

		freePages.Add(freePage);

		// save only headers
		Save(true);
		base.Close();

		File.Delete(savePath);
		File.Move(tmpPath, savePath);
		//File.Delete(tmpPath); // doesn't seem to get removed after rename, look into it
	}

	public void SaveChunk(Chunk chunk){

		if(chunk == null){

			Debug.Log("Garbage log value");
			return;
		}

		byte[] encodedData = EncodeChunk(chunk);

		Debug.Log("Need " +encodedData.Length +" bytes of space to save!");

		if(chunk.page.size < encodedData.Length){

			ChunkPage newPage = AllocateFreePage(encodedData.Length);

			if(newPage == null){

				Debug.Log("Page is null!");
				return;
			}

			chunk.page.usedSize = 0;
			freePages.Add(chunk.page);
			usedPages.Add(ChunkId(chunk.x, chunk.y), chunk.page);
		}

		Debug.Log("Saving chunk at offset " +chunk.page.offset +"!");

		Seek(chunk.page.offset);
		Write(encodedData);

		chunk.page.usedSize = encodedData.Length;
	}

	private string ChunkId(int x, int y){
		return x +"-" +y;
	}

	private void LoadPages(){

		usedPages.Clear();
		freePages.Clear();

		try{

			FileStream fileStream = new FileStream(pagesPath, FileMode.OpenOrCreate);
			BinaryTagStream stream = new BinaryTagStream(fileStream);

			Document document = BinaryTagDocument.Parse(stream);
			Node node = document.GetProperty("pages");

			if(node != null){

				Debug.Log("Found node");

				for(IEnumerator e = node.children.GetEnumerator(); e.MoveNext();){

					node = (Node) e.Current;
					ChunkPage page = new ChunkPage();

					page.offset = node.TryGetProperty("offset").GetInt();

					page.size = node.TryGetProperty("size").GetInt();
					Debug.Log("Page size: " +page.size);
					page.usedSize = node.TryGetProperty("used_size").GetInt();

					Node xProperty = node.GetProperty("x");

					if(xProperty != null){

						int x = xProperty.GetInt();
						int y = node.TryGetProperty("y").GetInt();

						string chunkId = ChunkId(x, y);

						usedPages.Add(chunkId, page);
					} else freePages.Add(page);
				}
			}
			
			stream.Close();
		} catch(Exception exc) when(exc is IOException || exc is MissingPropertyException){}

		//Console.log(String.valueOf(freePages.size()));
		
		if(usedPages.Count == 0 && freePages.Count == 0){

			Debug.Log("Creating new empty page!");

			ChunkPage page = new ChunkPage();
			page.size = MAX_PAGE_SIZE;

			freePages.Add(page);
		}
	}
	
	public void Save(bool isPagesOnly){

		FileStream file = new FileStream(pagesPath, FileMode.OpenOrCreate, FileAccess.Write);
		BinaryTagStream stream = new BinaryTagStream(file);

		// may end up storing some other data for root id, could just be other fields though. idk
		stream.WriteObjectStart(BinaryTagDocument.VERSION_STR);
		stream.WriteListStart("pages");

		/*
		 *	Go over the used pages first, so that they can allocate pages
		 *	before we start saving them.
		 */
		for(IDictionaryEnumerator e = usedPages.GetEnumerator(); e.MoveNext();){

			ChunkPage page = (ChunkPage) e.Current;
			Chunk chunk = page.chunk;
			
			if(!isPagesOnly)
				SaveChunk(page.chunk);

			// page may have changed
			page = chunk.page;

			stream.WriteObjectStart();

			stream.WriteIntProperty("offset", (int) page.offset);
			stream.WriteIntProperty("size", (int) page.size);
			stream.WriteIntProperty("used_size", page.usedSize);

			stream.WriteIntProperty("x", page.chunk.x);
			stream.WriteIntProperty("y", page.chunk.y);

			stream.WriteObjectEnd();
		}
		
		Debug.Log("Saving " +freePages.Count +" free pages!");

		foreach(ChunkPage page in freePages){

			Debug.Log("Free page saving!");
			
			stream.WriteObjectStart();

			stream.WriteIntProperty("offset", (int) page.offset);
			stream.WriteIntProperty("size", (int) page.size);
			stream.WriteIntProperty("used_size", page.usedSize);

			stream.WriteObjectEnd();
		}

		stream.WriteListEnd();
		stream.WriteObjectEnd();
		
		stream.Close();
	}

	public void Save()
	{
		Save(false);
	}

	private ChunkPage AllocateFreePage(int size){

		Debug.Log("Finding page!");
		
		ChunkPage bestPage = null;

		foreach(ChunkPage page in freePages){

			if(page.size >= size){

				bestPage = page;
				freePages.Remove(page);
				Debug.Log("Removing page!");
				break;
			}
		}

		if(bestPage != null){

			long leftover = bestPage.size -size -PAGE_GROWTH_ROOM;

			Debug.Log("Leftover bytes: " +leftover);

			if(leftover >= MIN_PAGE_SIZE){

				bestPage.size -= leftover;

				ChunkPage page = new ChunkPage();

				page.offset = bestPage.offset +bestPage.size;
				page.size = leftover;				
				freePages.Add(page);
			}
		}

		return bestPage;
	}

	private ChunkPage AllocateFreePage()
	{
		return AllocateFreePage(MIN_PAGE_SIZE);
	}
}