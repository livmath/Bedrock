class Chunk{

	public static readonly int SIZE = 4096;
	public static readonly int MAX_DEPTH = 5;

	public int x;
	public int y;
	public ChunkPage page;

	public byte[] terrain = new byte[SIZE *SIZE];

	public Chunk(){}

	public Chunk(int x, int y){

		this.x = x;
		this.y = y;
	}

	public void SetTile(int id, int x, int y, int z){
		terrain[GetTileIndex(x, y, z)] = (byte)id;
	}

	public int GetTile(int x, int y, int z){
		return terrain[GetTileIndex(x, y, z)];
	}

	public int GetTileIndex(int x, int y, int z){
		return x +y *SIZE +(SIZE *SIZE *z);
	}
}