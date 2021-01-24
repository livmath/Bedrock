package com.kraken.bedrock.struct;


public class HashtableKeyEnumeration extends HashtableEnumeration{

	public HashtableKeyEnumeration(Hashtable table){
		super(table);
	}
	
	public Object nextElement(){
		return ((HashtableEntry) super.nextElement()).key;
	}
};