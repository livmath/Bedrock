package com.kraken.bedrock.math;


public class Vector3 extends Vector2{

	public float z = 0;

	public Vector3(){}

	public Vector3(float x, float y, float z){

		super(x, y);
		this.z = z;
	}
	
	public void set(Vector3 vector){
	
		x = vector.x;
		y = vector.y;
		z = vector.z;
	}
	
	public String toString(){
		return "(" +x +", " +y +", " +z +")";
	}
};