package com.kraken.bedrock.btf;

import com.kraken.bedrock.dataformat.NodeDataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class BinaryTagOutputStream extends NodeDataOutputStream{

	public BinaryTagOutputStream(OutputStream out) {
		super(out);
	}

	public BinaryTagOutputStream(String path) throws IOException {
		super(path);
	}

	public void writeObjectStart() throws IOException{
		writeObjectStart(null);
	}

	public void writeObjectStart(String name) throws IOException{
		writeTag(BinaryTag.Compound, name);
	}

	public void writeObjectEnd() throws IOException{
		writeByte(BinaryTag.CompoundEnd);
	}

	public void writeListStart() throws IOException{
		writeListStart(null);
	}

	public void writeListStart(String name) throws IOException{
		writeTag(BinaryTag.ItemList, name);
	}

	public void writeListEnd() throws IOException{
		writeByte(BinaryTag.ListEnd);
	}

	public void writeArray(String name, byte[] array) throws IOException{

		writeTag(BinaryTag.Array, name);
		write(array);
	}

	public void writeArray(byte[] array) throws IOException{
		writeArray(null, array);
	}

	public void writeByteProperty(String name, byte value) throws IOException{

		writeTag(BinaryTag.Byte, name);
		writeByte(value);
	}

	public void writeByteProperty(byte value) throws IOException{
		writeByteProperty(null, value);
	}

	public void writeShortProperty(String name, short value) throws IOException{

		writeTag(BinaryTag.Short, name);
		writeShort(value);
	}

	public void writeShortProperty(short value) throws IOException{
		writeShortProperty(null, value);
	}

	public void writeIntProperty(String name, int value) throws IOException{

		writeTag(BinaryTag.Int, name);
		writeInt(value);
	}

	public void writeIntProperty(int value) throws IOException{
		writeIntProperty(null, value);
	}

	public void writeLongProperty(String name, long value) throws IOException{

		writeTag(BinaryTag.Long, name);
		writeLong(value);
	}

	public void writeLongProperty(long value) throws IOException{
		writeLongProperty(null, value);
	}

	public void writeFloatProperty(String name, float value) throws IOException{

		writeTag(BinaryTag.Float, name);
		writeFloat(value);
	}

	public void writeFloatProperty(float value) throws IOException{
		writeFloatProperty(null, value);
	}

	public void writeDoubleProperty(String name, double value) throws IOException{

		writeTag(BinaryTag.Double, name);
		writeDouble(value);
	}

	public void writeDoubleProperty(double value) throws IOException{
		writeDoubleProperty(null, value);
	}

	public void writeBoolProperty(String name, boolean value) throws IOException{

		writeTag(BinaryTag.Bool, name);
		writeByte((byte) (value? 1: 0));
	}

	public void writeBoolProperty(boolean value) throws IOException{
		writeBoolProperty(null, value);
	}

	public void writeStringProperty(String name, String value) throws IOException{

		writeTag(BinaryTag.String, name);
		writeUTF(value);
	}

	public void writeStringProperty(String value) throws IOException{
		writeStringProperty(null, value);
	}

	public void writeTag(byte tag, String name) throws IOException{

		writeByte(tag);

		if(name != null)
			writeUTF(name);
	}
};