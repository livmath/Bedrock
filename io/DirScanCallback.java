package com.kraken.bedrock.io;

import javax.microedition.io.file.FileConnection;


public interface DirScanCallback{

	public void handleFile(FileConnection entry);
	public void handleDir(FileConnection entry);
};