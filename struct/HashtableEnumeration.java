package com.kraken.bedrock.struct;


public class HashtableEnumeration extends VectorEnumeration{

	private int nonNullValuesReturned = 0;
	
	public HashtableEnumeration(Hashtable table){
		super(table);
	}

	public boolean hasMoreElements(){
		return (nonNullValuesReturned < vector.elementCount);
	}

	public Object nextElement(){

		Object element;
		
		do{
			element = super.nextElement();
		} while(element == null);

		nonNullValuesReturned++;

		return element;
	}
};