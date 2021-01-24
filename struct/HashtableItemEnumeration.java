package com.kraken.bedrock.struct;


public class HashtableItemEnumeration extends HashtableEnumeration{

	public HashtableItemEnumeration(Hashtable table){
		super(table);
	}

	public Object nextElement(){
		return ((HashtableEntry) nextElement()).value;
	}
};