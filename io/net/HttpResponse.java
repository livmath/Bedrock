package com.kraken.bedrock.io.net;

public class HttpResponse extends HttpMessage{

	public int responseCode = 0;
	public String responseMessage = "";
	
	protected void formHeader(StringBuffer buffer){

		buffer.append(String.valueOf(httpVersion)).append(" ");

		buffer.append(String.valueOf(responseCode));
		buffer.append(responseMessage).append("\r\n");
	}
};