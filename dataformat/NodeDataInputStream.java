package com.kraken.bedrock.dataformat;

import com.kraken.bedrock.io.FileUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class NodeDataInputStream extends DataInputStream{

	public NodeDataInputStream(InputStream in){
		super(in);
	}

	public NodeDataInputStream(String path) throws IOException{
		super(FileUtil.open(path).openDataInputStream());
	}

	public char readUTF8Char() throws IOException{
		return (char) this.readByte();
	}

	public String readUTF8String() throws IOException{

		StringBuffer str = new StringBuffer();

		char chr;
		while((chr = readUTF8Char()) != '\0')
			str.append(chr);

		return str.toString();
	}

	public abstract void readNextTag(Node node) throws IOException, EndOfTreeException;

	public boolean findTag(int tag, Node node) throws IOException{

		try{

			do{
				readNextTag(node);
			} while(node.tag != tag);
		} catch(EndOfTreeException exc){}

		return (node.tag == tag);
	}

	public boolean findProperty(Node node, String name) throws IOException{

		try{

			do{
				readNextTag(node);
			} while(!node.id.equals(name));
		} catch(EndOfTreeException exc){}

		return node.id.equals(name);
	}
};