/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kraken.bedrock.dataformat;

import com.kraken.bedrock.bdf.BasicDataFormatDocument;
import com.kraken.bedrock.btf.BinaryTagDocument;
import com.kraken.bedrock.io.Path;

import java.io.IOException;
import java.io.OutputStream;


public abstract class Document extends Node{

	public Document(String id) {
		super(id);
	}

	public Document() {
		super();
	}

	public static Document parse(String path) throws IOException, UnsupportedFormatException{

		switch(getDataFormat(path)){

			case DataFormat.BINARY_TAG:
				return BinaryTagDocument.parse(path);

			case DataFormat.BASIC_DATA_FORMAT:
				return BasicDataFormatDocument.parse(path);

			default:
				throw new UnsupportedFormatException();
		}
	}

	public static int getDataFormat(String url) throws UnsupportedFormatException{

		Path path = new Path(url);
		String ext = path.getExtension();

		if(ext.equals("bdf")){
			return DataFormat.BASIC_DATA_FORMAT;
		} else if(ext.equals("btf")){
			return DataFormat.BINARY_TAG;
		} else throw new UnsupportedFormatException();
	}

	public abstract void writeTo(OutputStream stream) throws IOException;
	public abstract void writeTo(String path) throws IOException;
	public abstract byte[] compile() throws IOException;
};