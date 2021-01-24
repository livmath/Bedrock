package com.kraken.bedrock.struct;


public class LinkedListEnumeration implements Enumeration{

	private int index = 0;
	private final LinkedListNode root;
	private LinkedListNode node;
	private boolean isReversed = false;

	public LinkedListEnumeration(LinkedList list){
		node = this.root = list.nodeAt(0);
	}

	public boolean hasMoreElements(){
		return !(node == null || (node == root && index != 0));
	}

	public Object nextElement(){

		Object item = node.data;
		node = (isReversed? node.prev: node.next);

		index += (isReversed? -1: 1);

		return item;
	}

	public void reverse(){
		isReversed = !isReversed;
	}

	public int index(){
		return index;
	}
};