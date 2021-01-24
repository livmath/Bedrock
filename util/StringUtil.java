package com.kraken.bedrock.util;

import com.kraken.bedrock.struct.Array;
import com.kraken.bedrock.struct.Stack;


public class StringUtil{

	/* Originally wanted this purely for aesthetics, of-course, although damn it's actually
	 * difficult implementing this in resource constrained devices.
	 * Simple way is indexing into a dictionary, although undefined words are missed.
	 */
	public static String toCamelCase(String str){

		str = str.toLowerCase();
		str = Character.toUpperCase(str.charAt(0)) +str.substring(1);

		return str;
	}

	public static String[] split(String str, char delimiter){
		return split(str, new String(new char[]{delimiter}));
	}

	public static String[] split(String str, String delimiter){

		int index = 0;
		int lastIndex = 0;
		
		Stack parts = new Stack();

		do{

			try{

				index = str.indexOf(delimiter, lastIndex);
				parts.push(str.substring(lastIndex, index));

				lastIndex = index;
			} catch(IndexOutOfBoundsException exc){
				break;
			}
		} while(index != -1);

		if(parts.isEmpty())
			parts.push(str);

		return (String[]) parts.items();
	}

	public static String replace(String str, String pattern, String value){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public static String leftPad(String str, int pad){
		return leftPad(str, pad, true);
	}

	public static String leftPad(String str, int pad, boolean isTabbed){

		char appendChar = isTabbed? '	':' ';
		StringBuffer buffer = new StringBuffer(str);

		for(int i = 0; i < pad; i++)
			buffer.insert(0, appendChar);

		return buffer.toString();
	}
	
	public static String createRepeatedString(char chr, int count){
	
		char[] chars = new char[count];
		
		for(int i = 0; i < count; i++)
			chars[i] = chr;
		
		return new String(chars);
	}
};