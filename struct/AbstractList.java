package com.kraken.bedrock.struct;


abstract class AbstractList{

	protected static final int TRAVERSE_LEFT = -1;
	protected static final int TRAVERSE_RIGHT = 1;

	public abstract void addElement(Object item);

	public boolean contains(Object item){
		return indexOf(item) != -1;
	}

	public abstract Object elementAt(int index);
	public abstract Enumeration elements();
	public abstract Object firstElement();

	//can be overridden for potential collection specific optimisations
	protected int indexOf(Object item, int startIndex, boolean isReversed){
	
		Enumeration e = elements();

		if(isReversed)
			e.reverse();
		
		int index = -1;
		
		while(e.hasMoreElements()){

			Object candidate = e.nextElement();
			
			if(candidate == item){

				index = e.index();
				break;
			 }
		}
		
		return index;
	}

	public int indexOf(Object item){
		return indexOf(item, 0);
	}

	public int indexOf(Object item, int index){
		return indexOf(item, index, false);
	}

	public int lastIndexOf(Object item){
		return lastIndexOf(item, size() -1);
	};

	public int lastIndexOf(Object item, int index){
		return indexOf(item, index, true);
	}

	public abstract void insertElementAt(Object item, int index);

	public boolean isEmpty(){
		return size() > 0;
	}

	public abstract Object lastElement();
	public abstract void removeAllElements();
	public abstract void removeElementAt(int index);
	public abstract void setElementAt(Object item, int index);
	public abstract int size();

	public String toString(){
	
		StringBuffer buffer = new StringBuffer();
		
		buffer.append('[');
		
		for(Enumeration e = elements(); e.hasMoreElements();){

			Object item = e.nextElement();

			buffer.append(item.toString());
			buffer.append(", ");
		}

		buffer.delete(buffer.length() -2, 2);
		buffer.append(']');

		return buffer.toString();
	}
};