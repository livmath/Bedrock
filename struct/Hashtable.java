package com.kraken.bedrock.struct;

//TODO: improve this to be actually nice

import com.kraken.bedrock.debug.Console;

public class Hashtable extends Vector{

	private static final int DEFAULT_SIZE = 64;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private final float loadFactor;
	
	public Hashtable(){

		setSize(DEFAULT_SIZE);
		loadFactor = DEFAULT_LOAD_FACTOR;
	}

	public Hashtable(int size, float loadFactor){

		setSize(size);
		this.loadFactor = loadFactor;
	}

	public void insertElementAt(Object item, int index){

		elementData[index] = item;
		elementCount++;
	}

	protected final void setSize(int size){
		
		super.setSize(size);

		//values will be overwritten so make copy
		HashtableEntry[] entries = new HashtableEntry[elementData.length];
		Array.writeTo(entries, elementData);
		
		//re-index everything
//		Console.log(String.valueOf(entries.length));
		
		elementCount = 0;
		
		for(int i = 0; i < entries.length; i++){

			HashtableEntry entry = entries[i];

			if(entry != null){

				Console.log("Reinserting \"" +entry.key +"\"");
				put(entry.key, entry.value);
			}
		}
	}

	public int index(Object key){
		return (Math.abs(key.hashCode()) %elementData.length);
	}

	public int index(int key){
		return index(new Integer(key));
	}
	
	public Object get(Object key){
		Console.log("Index: " +String.valueOf(index(key)));
		return ((HashtableEntry) elementAt(index(key))).value;
	}

	public Object get(int key){
		Console.log("Index: " +String.valueOf(index(key)));
		return ((HashtableEntry) elementAt(index(key))).value;
	}

	public int getInt(Object key){
		return ((Integer) get(key)).intValue();
	}
	
	public int getInt(int key){
		return ((Integer) get(key)).intValue();
	}

	public Enumeration elements(){
		return new HashtableItemEnumeration(this);
	}

	public Enumeration keys(){
		return new HashtableKeyEnumeration(this);
	}

	public float load(){
		return (elementCount *100 /capacity());
	}

	public void put(Object key, Object item){

		Console.log("Load: " +String.valueOf(load()));
		
		if(load() >= loadFactor *100)
			setSize(elementData.length *2);

		Console.log("Index: " +String.valueOf(index(key)));

		insertElementAt(new HashtableEntry(key, item), index(key));
	}

	public void put(int key, Object item){
		put(new Integer(key), item);
	}
	
	public void put(int key, int item){
		put(new Integer(key), new Integer(item));
	}

	public void put(Object key, int item){
		put(key, new Integer(item));
	}
	
	public boolean containsKey(String key){
		return (get(key) != null);
	}
};