package com.kraken.bedrock.btf;

public class BinaryTag{

	/*
	A tag's ID is defined by a unique value as the high nibble,
	and the size of the data type it represents for the low nibble.

	More complex data types such as lists read tags and so don't have a size.
	For arrays the value is a 4-byte integer representing the length.
	*/

	public static final byte Compound = (0x00 |0x00);
	public static final byte CompoundEnd = (0x10 |0x00);
	public static final byte Array = (0x20 |0x04);
	public static final byte ItemList = (0x30 |0x00);
	public static final byte ListItem = (0x40 |0x00);
	public static final byte ListEnd = (0x50 |0x00);

	public static final byte Byte = (0x60 |0x01);
	public static final byte Char = (0x70 |0x01);
	public static final byte Short = (byte) (0x80 | 0x02);
	public static final byte Int = (byte) (0x90 |0x04);
	public static final byte Float = (byte) (0xA0 |0x04);
	public static final byte Double = (byte) (0xB0 |0x08);
	public static final byte String = (byte) (0xC0 |0x00);
	public static final byte Bool = (byte) (0xD0 |0x01);
	public static final byte Long = (byte) (0xE0 |0x08);

	public static final byte Function = (byte) (0xF0 |0x09);

	public static int getUniqueValue(byte tag){
		return tag &0xF0;
	}

	public static int getValueSize(byte tag){
		return tag &0x0F;
	}
};