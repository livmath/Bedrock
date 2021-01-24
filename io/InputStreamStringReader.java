package com.kraken.bedrock.io;

import com.kraken.bedrock.debug.Console;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class InputStreamStringReader extends InputStreamReader{

	public InputStreamStringReader(InputStream in){
		super(in);
	}

	/* Attempts to read a line of text, returning null if an IOException occurred,
	 * or there's no more lines to read.
	 * Works with both \r\n ahd \n systems.
	 */
	public String readLine(){

		try{

			StringBuffer buffer = new StringBuffer();

			char chr;
			int value = 0;

			while(true){

				if((value = read()) == -1)
					break;

				chr = (char) value;

				if(chr == '\n' || (chr == '\r' && read() == '\n'))
					break;

				buffer.append(chr);
			}

			return (value == -1 && buffer.length() == 0? null: buffer.toString());
		} catch(IOException exc){
			return null;
		}
	}

	/* Tries to skip up to @count lines, stopping if it encounters end of file
	 * or an IOException.
	 */
	public void skipLines(int count){

		for(int i = 0; i < count; i++){

			if(readLine() == null)
				break;
		}
	}
};