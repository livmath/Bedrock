package com.kraken.bedrock.struct;


public class Array{ //bunch of utility methods for arrays

	public static void writeTo(Object[] target, Object[] source){

		if(target == null || source == null)
			return;

		for(int i = 0; i < Math.min(target.length, source.length); i++)
			target[i] = source[i];
	}

	public static void nullFill(Object[] array){

		if(array == null)
			return;
		
		for(int i = 0; i < array.length; i++)
			array[i] = null;
	}
	
	public static void put(Object[] array, Object item){

		if(array == null || item == null)
			return;
		
		for(int i = 0; i < array.length; i++){
		
			if(array[i] == null){

				array[i] = item;
				break;
			}
		}
	}
	
	public static int indexOf(Object[] array, Object item){
		
		int index = -1;
		
		if(array == null || item == null)
			return index;
		
		for(int i = 0; i < array.length; i++){

			if(array[i] == item){

				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public static void splice(Object[] array, int start, int end){
	
		if(array == null)
			return;
		
		for(int i = start; i < start +end; i++){
			
			
		}
	}
};