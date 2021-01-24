package com.kraken.bedrock.assetloader;

import com.kraken.bedrock.debug.Console;
import com.kraken.bedrock.io.DirScanCallback;
import com.kraken.bedrock.io.FileUtil;
import com.kraken.bedrock.io.Path;
import com.kraken.bedrock.struct.Hashtable;
import java.io.IOException;
import javax.microedition.io.file.FileConnection;

/*
even if content is dynamically loaded in and out,
this is only meant for the right systems to know the content exists and make initial loading easier
*/
public class AssetLoader implements AssetLoaderEventStateListener{

	private Hashtable loaders;

	private int waitingAssetsLoaded = 0;
	private boolean hasScanFinished = false;

	public AssetLoader(){
		loaders = new Hashtable();
	}
	
	public void addAssetLoader(String name, AssetLoaderHandler loader){

		if(loaders == null)
			loaders = new Hashtable();

		loader.setLoadEventStateListener(this);

		loaders.put(name, loader);
	}

	public void loadAssets(String path) throws IOException{
		loadAssetFolder(path);
	}

	public void loadAssetFolder(String path) throws IOException{

		FileUtil.mapDir(path, new DirScanCallback(){

			public void handleFile(FileConnection entry){

				String loaderName = determineAssetLoaderName(entry);

				if(!loaders.containsKey(loaderName)){

					loaders.put(loaderName, null);
					Console.log("Loader under name: \"" +loaderName +"\" not set!");
				 }

				if(loaders.get(loaderName) == null)
					return;

				AssetLoadInfo asset = new AssetLoadInfo(entry.getPath());

				try{

					asset.size = entry.fileSize();
					((AssetLoaderHandler) loaders.get(loaderName)).queueAsset(asset);

					waitingAssetsLoaded++;
				} catch(IOException exc){
					Console.log("IO exception reading file size!");
				} catch(SecurityException exc){
					Console.log("Not allowed to read asset file due to security!");
				}
			}

			public void handleDir(FileConnection entry){}
		});
		
		hasScanFinished = true;
	}

	protected String determineAssetLoaderName(FileConnection file){

		Path path = new Path(file.getPath());
		String rootDir = path.getRoot().toString();
		
		return rootDir;
	}

	public void onAssetLoad(AssetLoaderHandler loader) {

		if(--waitingAssetsLoaded == 0 && hasScanFinished)
			onInitialLoadEnd();
	}

	public void onInitialLoadEnd(){}
	
	public void onProgress(){
		//TODO: put progress bar code here
	}
};