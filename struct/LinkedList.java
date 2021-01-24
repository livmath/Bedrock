package com.kraken.bedrock.struct;


public class LinkedList extends AbstractList{

	private LinkedListNode root;

	public void addElement(Object item){

		LinkedListNode node = new LinkedListNode();
		node.data = item;
		
		if(root != null){
			insertNodeAt(node, -1);
		} else root = node;
	}

	public Object elementAt(int index){
		return nodeAt(index).data;
	}

	public LinkedListNode nodeAt(int index){
	
		LinkedListNode node = root;
		
		if(index > 0){

			for(int i = 0; i < index; i++)
				node = node.next;
		} else{
		
			for(int i = 0; i > index; i--)
				node = node.prev;
		}
		
		return node;
	}

	public Enumeration elements(){
		return new LinkedListEnumeration(this);
	}

	public Object firstElement(){
		return root.data;
	}

	private void insertNodeAt(LinkedListNode node, int index){
	
		LinkedListNode old = nodeAt(index);
		
		node.prev = old.prev;
		node.prev.next = node;

		node.next = old;
		old.prev = node;
	}
	
	public void insertElementAt(Object item, int index){

		LinkedListNode node = new LinkedListNode();
		node.data = item;
		
		insertNodeAt(node, index);
	}

	public Object lastElement(){
		return root.prev.data;
	}

	public void removeAllElements(){
		root = null;
	}

	public void removeElementAt(int index){

		LinkedListNode node = nodeAt(index);
		
		node.prev.next = node.next;
		node.next.prev = node.prev;
	}

	public void setElementAt(Object item, int index){
		nodeAt(index).data = item;
	}

	public int size(){

		LinkedListNode node = root;
		int size = 0;
		
		do{

			size++;
			node = node.next;
		} while(node != root);
		
		return size;
	}
};