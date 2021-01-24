package com.kraken.bedrock.struct;


class VectorEnumeration implements Enumeration{

	private int traversalDirection = 1;
	
	private int index = 0;
	protected final Vector vector;
	
	public VectorEnumeration(Vector vector){
		this.vector = vector;
	}

	public boolean hasMoreElements(){

		return ((traversalDirection == 1 && index < vector.size() -1)
				|| (traversalDirection == -1 && index > 0));
	}

	public Object nextElement(){
		return vector.elementAt(index += traversalDirection);
	}

	public void reverse(){
		
		if(traversalDirection == 1 && index == 0){
		
			index = vector.size() -1;
			traversalDirection = -1;
		} else traversalDirection = 1;
	}

	public int index(){
		return index;
	}
};