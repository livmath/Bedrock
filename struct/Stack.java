package com.kraken.bedrock.struct;

import java.util.EmptyStackException;


public class Stack extends Vector{

	public void push(Object item){
		addElement(item);
	}

	public Object pop() throws EmptyStackException{

		if(isEmpty())
			throw new EmptyStackException();

		Object item = elementAt(elementCount -1);
		removeElementAt(elementCount -1);

		return item;
	}

	public Object peek() throws EmptyStackException{

		if(isEmpty())
			throw new EmptyStackException();

		return elementAt(elementCount -1);
	}

	public boolean empty(){
		return isEmpty();
	}

	public int search(Object item){
		return elementCount -indexOf(item);
	}
};