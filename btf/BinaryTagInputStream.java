package com.kraken.bedrock.btf;


import com.kraken.bedrock.dataformat.EndOfTreeException;
import com.kraken.bedrock.dataformat.Node;
import com.kraken.bedrock.dataformat.NodeDataInputStream;
import com.kraken.bedrock.io.FileUtil;

import java.io.EOFException;
import java.io.IOException;

import java.io.DataInputStream;

public class BinaryTagInputStream extends NodeDataInputStream{

	public BinaryTagInputStream(String path) throws IOException{
		super(path);
	}

	public void readNextTag(Node node) throws IOException{

		node.tag = readByte();

		switch(node.tag){
	
			case BinaryTag.Compound:
			case BinaryTag.CompoundEnd:
			case BinaryTag.ItemList:
			case BinaryTag.ListEnd:
				return;
	
			case BinaryTag.ListItem:
				readNextTag(node);
				return;
	
			case BinaryTag.String:
				node.value = readUTF8String().getBytes();
				break;

			case BinaryTag.Array:
	
				int arrayLength = readInt();
				node.value = new byte[arrayLength];

				readFully(node.value);
				break;
	
			case BinaryTag.Byte:
			case BinaryTag.Bool:
			case BinaryTag.Char:
			case BinaryTag.Short:
			case BinaryTag.Int:
			case BinaryTag.Long:
			case BinaryTag.Float:
			case BinaryTag.Double:
				readFully(node.value, 0, BinaryTag.getValueSize(node.tag));
				break;
	
			default:
				node.tag = 0;
				break;
		};
	
		node.id = readUTF8String();
	}
};