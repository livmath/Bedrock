package com.kraken.bedrock.io;

import com.kraken.bedrock.struct.Enumeration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;


public class FileUtil{

	public static FileConnection open(String path) throws IOException{
		return (FileConnection) Connector.open("file://" +path);
	}
	
	public static void setContents(String path, String contents) throws IOException{
	
		FileConnection file = open(path);

		if(!file.exists())
			file.create();

		DataOutputStream stream = file.openDataOutputStream();
		stream.writeUTF(contents);
		
		stream.close();
	}
	
	public static String getContents(String path) throws IOException{
	
		FileConnection file = open(path);
		
		DataInputStream stream = file.openDataInputStream();
		String contents = stream.readUTF();
		
		stream.close();
		
		return contents;
	}
	
	public static void rename(String path, String newName) throws IOException{
	
		FileConnection file = open(path);
		file.rename(newName);

		file.close();
	}

	public static void delete(String path) throws IOException{
	
		FileConnection file = open(path);
		file.delete();

		file.close();
	}
	
	public static void mapDir(String path, DirScanCallback callback) throws IOException{

		String basePath = path;
		
		FileConnection dir = open(path);
		FileConnection entry = dir;

		Enumeration paths = (Enumeration) dir.list();

		while(paths.hasMoreElements()){
		
			String entryPath = basePath +((String) paths.nextElement());
			entry.setFileConnection(entryPath);

			if(entry.isDirectory()){

				callback.handleDir(entry);
				mapDir(entryPath, callback);
			} else{
				callback.handleFile(entry);
			}
		}

		dir.close();
	}

	public static byte[] getByteContents(String path) throws IOException{

		FileConnection file = open(path);
		byte[] bytes = new byte[(int) file.fileSize()];

		if(!file.exists())
			file.create();

		DataInputStream stream = file.openDataInputStream();
		stream.readFully(bytes);

		stream.close();
		
		return bytes;
	}
	
	public static void move(String path, String newPath) throws IOException{
	}
};