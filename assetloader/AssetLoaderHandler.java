package com.kraken.bedrock.assetloader;

import com.kraken.bedrock.debug.Console;
import com.kraken.bedrock.io.FileUtil;
import com.kraken.bedrock.struct.Stack;
import java.io.IOException;

public abstract class AssetLoaderHandler{

	private AssetLoaderEventStateListener eventListener;
	private final Stack loadQueue = new Stack();

	private final int MAX_ASSET_LOAD_ATTEMPTS = 2;

	public void queueAsset(AssetLoadInfo asset){
		loadQueue.push(asset);
	}

	protected void handleNextAsset(){

		if(loadQueue.isEmpty())
			return;

		AssetLoadInfo asset = (AssetLoadInfo) loadQueue.peek();

		try{

			loadAsset(FileUtil.getByteContents(asset.path));
			loadQueue.pop();

			eventListener.onAssetLoad(this);
		} catch(IOException exc){

			Console.log("Error during reading asset file, attempt ["
					+asset.loadAttempts + "/" +MAX_ASSET_LOAD_ATTEMPTS +"]");

			if(asset.loadAttempts++ > MAX_ASSET_LOAD_ATTEMPTS){

				Console.log("Asset failed to load!");
				loadQueue.pop();
			 };
		} catch(OutOfMemoryError exc){

			Console.log("Asset too large, out of memory!");
			loadQueue.pop();
		};
	}

	protected abstract void loadAsset(byte[] rawAssetBytes);

	public void setLoadEventStateListener(AssetLoaderEventStateListener listener){
		eventListener = listener;
	}
};