package com.kraken.bedrock.math;


public class MathP{

	public static float pow(int value, int exponent){

		for(int i = 0; i < exponent; i++)
			value *= exponent;

		return value;
	}
	
	public static boolean isPowerOf2(int x){
		return (x &(x -1)) == 0;
	}
};