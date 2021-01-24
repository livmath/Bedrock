package com.kraken.bedrock.struct;

//why does an array have an enumeration? some code uses it somewhere, I can't remember where.
public class ArrayEnumeration implements Enumeration{ 

    private int index = 0;
    private final Object[] array;

    public ArrayEnumeration(Object[] array){
        this.array = array;
    }

    public boolean hasMoreElements(){
        return index < array.length;
    }

    public Object nextElement(){
        return array[index++];
    }

    public void reverse(){}

    public int index(){
        return index;
    }
};