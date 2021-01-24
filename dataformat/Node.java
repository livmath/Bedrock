package com.kraken.bedrock.dataformat;

import com.kraken.bedrock.io.BitConverter;
import com.kraken.bedrock.struct.Enumeration;
import com.kraken.bedrock.struct.Vector;

public class Node{

	public String id;
	public byte[] value;
	public byte tag;

	public Node parent = null;
	public final Vector children = new Vector();

	public Node(){}

	public Node(String id){
		this.id = id;
	}

	public int getByte(){
		return value[0];
	}
	
	public short getShort(){
		return BitConverter.readShort(value, 0);
	}
	
	public int getInt(){
		return BitConverter.readInt(value, 0);
	}
	
	public String getString(){
		return BitConverter.readString(value, 0);
	}

	public void appendChild(Node node){

		children.addElement(node);
		node.parent = this;
	}

	public Node getNodeById(String id){ //TODO: implement cheaper search

		if(this.id.equals(id))
			return this;

		Node find = null;

		for(Enumeration e = children.elements(); e.hasMoreElements();){

			Node child = (Node) e.nextElement();
			Node pos = child.getNodeById(id);

			if(pos != null){

				find = pos;
				break;
			 };
		};

		return find;
	}

	public Node getProperty(String id){
		return getNodeById(id);
	}

	public Node tryGetProperty(String id) throws MissingPropertyException{

		Node node = getNodeById(id);
		if(node == null)
			throw new MissingPropertyException();

		return node;
	}

	public void removeChild(Node node){
		children.removeElement(node);
	}
};