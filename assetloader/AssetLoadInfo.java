package com.kraken.bedrock.assetloader;

import com.kraken.bedrock.io.Path;


public class AssetLoadInfo extends Path{

	public long size;
	public int loadAttempts = 0;

	public AssetLoadInfo(String path){
		super(path);
	}
};
