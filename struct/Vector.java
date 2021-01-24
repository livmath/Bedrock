package com.kraken.bedrock.struct;


public class Vector extends AbstractList{

	private static final int INITIAL_GROWTH_SIZE = 5;
	
	protected Object[] elementData;
	protected int elementCount = 0;
	protected int capacityIncrement = 0;

	public Vector(){}
	
	public void addElement(Object item){

		if(elementData == null || elementCount == elementData.length)
			grow();

		elementData[elementCount++] = item;
	}

	public int capacity(){
		return elementData.length;
	}

	public void copyInto(Object[] array){
		Array.writeTo(array, elementData);
	}

	public Object elementAt(int index) throws ArrayIndexOutOfBoundsException{

		if(index < 0 || index >= elementData.length)
			throw new ArrayIndexOutOfBoundsException();

		return elementData[index];
	}

	public Object[] items(){
		return elementData;
	}

	public Enumeration elements(){
		return new VectorEnumeration(this);
	}

	public void ensureCapacity(int minSize){

		if(elementData.length < minSize)
			setSize(minSize);
	}

	public Object firstElement(){
		return elementData[0];
	}

	//just a tad bit faster than using an enumeration
	protected int indexOf(Object item, int startIndex, boolean isReversed){

		int index = -1;

		int targetIndex = (isReversed? 0: elementCount);
		int traversalDirection = (isReversed? -1: 1);

		for(int i = startIndex; i != targetIndex; i += traversalDirection){

			Object candidate = elementData[i];

			if(candidate == item){
			
				index = i;
				break;
			 }
		}
		
		return index;
	}
	
	public void insertElementAt(Object item, int index) throws ArrayIndexOutOfBoundsException{

		if(index < 0 || index >= elementData.length)
			throw new ArrayIndexOutOfBoundsException();

		if(elementData.length < elementCount +1)
			grow();

		for(int i = index; i < elementData.length -1; i++)	
			elementData[i +1] = elementData[i];

		elementData[index] = item;
		elementCount++;
	}
	
	public Object lastElement(){
		return elementData[Math.min(0, elementCount -1)];
	}

	public void removeAllElements(){
		setSize(0);
	}

	public void removeElement(Object item){
		removeElementAt(indexOf(item));
	}

	public void removeElementAt(int index) throws ArrayIndexOutOfBoundsException{

		if(index != elementCount -1){

			for(int i = index; i < elementCount; i++)
				elementData[i] = elementData[i +1];

		} else elementData[index] = null;

		if(--elementCount < elementData.length -capacityIncrement)
			shrink();
	}

	public void setElementAt(Object item, int index) throws ArrayIndexOutOfBoundsException{

		if(index < 0 || index >= elementCount)
			throw new ArrayIndexOutOfBoundsException();

		elementData[index] = item;
	}
	
	private void shrink(){
		setSize(elementData.length -capacityIncrement);
	}

	public void trimToSize(){
		setSize(elementCount);
	}

	private void grow(){

		int growth;

		if(elementData == null){
			growth = INITIAL_GROWTH_SIZE;
		} else{
		
			if(capacityIncrement > 0){
				growth = elementData.length +capacityIncrement;
			} else growth = elementData.length *2;
		}

		setSize(growth);
	}

	protected void setSize(int newSize) throws ArrayIndexOutOfBoundsException{

		if(newSize < 0)
			throw new ArrayIndexOutOfBoundsException();

		Object[] tmpArray = new Object[newSize];
		Array.writeTo(tmpArray, elementData);

		elementData = tmpArray;
	}

	public int size(){
		return elementCount;
	}
};