package com.kraken.bedrock.io.net;

import com.kraken.bedrock.struct.Enumeration;
import com.kraken.bedrock.struct.Hashtable;


public abstract class HttpMessage{
	
	public float httpVersion = 1.1f;

	private final Hashtable headers = new Hashtable();
	private String content = "";
	
	public void setHeader(String field, String value){
		headers.put(field, value);
	}
	
	protected abstract void formHeader(StringBuffer buffer);

	public void setContent(String content){
		this.content = content;
	}
	
	public String toString(){
	
		StringBuffer buffer = new StringBuffer();

		formHeader(buffer);
		
		headers.put("Content-Type", "text/plain; charset=utf-8");
		headers.put("Content-Length", content.length());
		
		for(Enumeration e = headers.keys(); e.hasMoreElements();){
		
			String field = (String) e.nextElement();
			String value = (String) headers.get(field);

			buffer.append(field).append(": ").append(value);
		}

		buffer.append("\r\n");
		buffer.append(content);
		
		return buffer.toString();
	}
};