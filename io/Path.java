package com.kraken.bedrock.io;


public class Path{

	public final String path;

	public Path(String path){
		this.path = path;
	}
	
	public String getName(){

		int lastSeperator = path.lastIndexOf('/');
		if(lastSeperator == -1)
			lastSeperator = path.lastIndexOf('\\');

		if(lastSeperator == -1)
			lastSeperator = 0;

		int extensionStart = path.lastIndexOf('.');
		if(extensionStart == -1)
			extensionStart = path.length();

		return path.substring(lastSeperator, extensionStart);
	}
	
	public String getExtension(){
		return path.substring(path.lastIndexOf('.'));
	}

	public Object getRoot(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
};