package com.kraken.bedrock.dataformat;

import com.kraken.bedrock.io.FileUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class NodeDataOutputStream extends DataOutputStream{

    public NodeDataOutputStream(OutputStream out) {
        super(out);
    }

    public NodeDataOutputStream(String path) throws IOException{
        super(FileUtil.open(path).openDataOutputStream());
    }

    public abstract void writeObjectStart() throws IOException;
    public abstract void writeObjectStart(String name) throws IOException;
    public abstract void writeObjectEnd() throws IOException;

    public abstract void writeListStart() throws IOException;
    public abstract void writeListStart(String name) throws IOException;
    public abstract void writeListEnd() throws IOException;

    public abstract void writeArray(String name, byte[] array) throws IOException;
    public abstract void writeArray(byte[] array) throws IOException;

    public abstract void writeByteProperty(String name, byte value) throws IOException;
    public abstract void writeByteProperty(byte value) throws IOException;

    public abstract void writeShortProperty(String name, short value) throws IOException;
    public abstract void writeShortProperty(short value) throws IOException;

    public abstract void writeIntProperty(String name, int value) throws IOException;
    public abstract void writeIntProperty(int value) throws IOException;

    public abstract void writeLongProperty(String name, long value) throws IOException;
    public abstract void writeLongProperty(long value) throws IOException;

    public abstract void writeFloatProperty(String name, float value) throws IOException;
    public abstract void writeFloatProperty(float value) throws IOException;

    public abstract void writeDoubleProperty(String name, double value) throws IOException;
    public abstract void writeDoubleProperty(double value) throws IOException;

    public abstract void writeBoolProperty(String name, boolean value) throws IOException;
    public abstract void writeBoolProperty(boolean value) throws IOException;

    public abstract void writeStringProperty(String name, String value) throws IOException;
    public abstract void writeStringProperty(String value) throws IOException;
};