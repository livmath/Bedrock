package com.kraken.bedrock.btf;

import com.kraken.bedrock.dataformat.Document;
import com.kraken.bedrock.dataformat.Node;
import com.kraken.bedrock.struct.Enumeration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryTagDocument extends Document{

	public static Document parse(String path) throws IOException{

		BinaryTagInputStream stream = new BinaryTagInputStream(path);
		return parse(stream);
	}

	public static Document parse(BinaryTagInputStream stream) throws IOException{
		
		BinaryTagDocument document = new BinaryTagDocument();

		Node root = document;
		Node parent = root;

		stream.readNextTag(root);

		if(root.tag != BinaryTag.Compound){

			System.out.println("[WARNING]: Missing root compound tag, attempting to read anyway!");
			root.tag = BinaryTag.Compound;
		 }

		boolean isReading = true;

		while(isReading){

			Node node = new Node();
			stream.readNextTag(node);

			node.appendChild(node);

			switch(node.tag){

				case BinaryTag.Compound:
				case BinaryTag.ItemList:
					parent = node;
					break;

				case BinaryTag.CompoundEnd:
				case BinaryTag.ListEnd:

					if((parent = parent.parent) == null)
						isReading = false;
					break;
			}
		}

		stream.close();

		return document;
	}

	public void writeTo(BinaryTagOutputStream stream) throws IOException{

		stream.writeTag(tag, id);
		stream.write(value);

		for(Enumeration e = children.elements(); e.hasMoreElements();){

			Node child = (Node) e.nextElement();
		}
	}

	public void writeTo(OutputStream stream) throws IOException{
		writeTo(new BinaryTagOutputStream(stream));
	}

	public void writeTo(String path) throws IOException{

		BinaryTagOutputStream stream = new BinaryTagOutputStream(path);
		writeTo(stream);

		stream.close();
	}

	public byte[] compile() throws IOException{

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BinaryTagOutputStream stream = new BinaryTagOutputStream(buffer);

		writeTo(stream);
		stream.close();

		return buffer.toByteArray();
	}
};