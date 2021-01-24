package com.kraken.bedrock.io.net;

import com.kraken.bedrock.util.Base64;
import com.kraken.bedrock.util.SHA1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class WebSocket{
	
	public static void handshake(String host, HttpRequest request, DataOutputStream socket) throws IOException{

		String securityKey = request.getHeader("Sec-WebSocket-Key");

		byte[] keyHash = new SHA1().getDigestOfString((securityKey +"258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()).getBytes();

		//hex pack the hash string
		for(int i = 0; i < keyHash.length; i += 2){ //may have to take encoding into account
			keyHash[i] = (byte) (keyHash[i] << 4 |keyHash[i +1]);
		}

		String securityAccept = Base64.encode(keyHash, 0, keyHash.length /2);

		HttpResponse response = new HttpResponse();
		
		response.responseCode = 101;
		response.responseMessage = "Switching Protocols";

		response.setHeader("Upgrade", "websocket");
		response.setHeader("Connection", "Upgrade");
		response.setHeader("WebSocket-Origin", host);
		response.setHeader("Sec-WebSocket-Accept", securityAccept);

		socket.write(response.toString().getBytes());
	}

	public static void read(DataInputStream socket, byte[] packet) throws IOException{

		byte b1 = socket.readByte();

		boolean isMasked = ((b1 &128) == 128);
		long length = b1 &127;

		if(length == 126){
			length = socket.readShort();
		} else if(length == 127){
			length = socket.readLong();
		}

		socket.readFully(packet, 0, (int) length);

		if(length > Integer.MAX_VALUE)
			socket.readFully(packet, Integer.MAX_VALUE, (int) (length >> 32));

		if(isMasked){

			int masks = socket.readInt(); //4 bytes of masks

			for(int i = 0; i < length; i++)
				packet[i] = (byte) (packet[i] ^(masks &(0xFF000000 >>> (i %4))));
		}
	}
	
	public static void send(byte[] packet, long length, DataOutputStream socket) throws IOException{
		
		/*
		 *	For whoever is reading this, so you don't have to read the docs:
		 *
		 *  %x0 denotes a continuation frame
		 *  %x1 denotes a text frame
		 *  %x2 denotes a binary frame
		 *  %x3-7 are reserved for further non-control frames
		 *  %x8 denotes a connection close
		 *  %x9 denotes a ping
		 *  %xA denotes a pong
		 *  %xB-F are reserved for further control frames
		 */

		byte b1 = (byte) 0x80 |(0x02 &0x0F); //frame type
		socket.writeByte(b1);

		length = Math.min(packet.length, length);
		
		if(length <= 125){
			socket.writeByte((int) length);
		} else if(length <= 65536){

			socket.writeByte(126);
			socket.writeShort((int) length);
		} else{

			socket.writeByte(127);
			socket.writeLong(length);
		}

		socket.write(packet, 0, (int) length);

		if(length > Integer.MAX_VALUE)
			socket.write(packet, Integer.MAX_VALUE, (int) (length >> 32));
	}
};