package com.kraken.bedrock.assetloader;


public interface AssetLoaderEventStateListener{

	public void onAssetLoad(AssetLoaderHandler loader);
	public void onInitialLoadEnd();
	public void onProgress();
};