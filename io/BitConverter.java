package com.kraken.bedrock.io;


public class BitConverter{

	public static short readShort(byte[] buffer, int offset){
		return (short) ((buffer[offset] << 8) |buffer[offset +1]);
	}

	public static int readInt(byte[] buffer, int offset){
		return (int) readShort(buffer, offset);
	}

	public static String readString(byte[] buffer, int offset){

		StringBuffer str = new StringBuffer();

		char chr;
		while((chr = (char) buffer[offset++]) != '\0')
			str.append(chr);

		return str.toString();
	}
};