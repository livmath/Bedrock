package com.kraken.bedrock.debug;

import com.kraken.bedrock.struct.Hashtable;
import com.kraken.bedrock.util.StringUtil;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;


//Console class that can also send over the network. Data that fails to write is dropped.
public class Console implements ANSIEscapeCodes{

	private static final String WARNING_PREFIX = ConsoleColor.YELLOW_BACKGROUND +"[WARNING] ";
	private static final String INFO_PREFIX = ConsoleColor.WHITE_BACKGROUND +"[INFO] ";
	private static final String ERROR_PREFIX = ConsoleColor.RED_BACKGROUND +ConsoleColor.WHITE_BOLD +"[ERROR] ";
	private static final String GROUPING_PREFIX = ConsoleColor.RED_BACKGROUND +" " +ConsoleColor.RESET +" ";
	
	private static SocketConnection socket;
	private static DataOutputStream stream;
	
	private static int indentation = 0;
	private static int indentationSpaces = 4;
	public static boolean isTabbed = true;
	
	private static final Hashtable counters = new Hashtable();

	public static void group(){
		group("");
	}
	
	public static void group(String label){

		indentation++;
		
		log(label);
		log(StringUtil.createRepeatedString('=', label.length()));
	}
	
	public static void setGroupingSpaces(int spaces){
		indentationSpaces = spaces;
	}
	
	public static void groupEnd(){
		
		if(indentation > 0)
			indentation--;
	}
	
	public static void groupEndAll(){
		indentation = 0;
	}
	
	public static void count(){
		count("default");
	}
	
	public static void count(String label){

		int value = counters.getInt(label);
		log(label +": " +value);

		counters.put(label, ++value);
	}
	
	public static void countReset(){
		countReset("default");
	}
	
	public static void countReset(String label){
		counters.put(label, 0);
	}
	
	public static void warn(String str){
		log(WARNING_PREFIX +str +ConsoleColor.RESET);
	}

	public static void info(String str){
		log(INFO_PREFIX +str +ConsoleColor.RESET);
	}

	public static void error(String str){
		log(ERROR_PREFIX +str +ConsoleColor.RESET);
	}

	public static void log(String str){

		if(indentation > 0){

			int indentSpaces = isTabbed? indentation -1: (indentation -1) *indentationSpaces;

			str = GROUPING_PREFIX +str;
			str = StringUtil.leftPad(str, indentSpaces, isTabbed);
		}

		System.out.println(str);

		try{
			stream.write(str.getBytes());
		} catch(IOException ignored){
		} catch(NullPointerException ignored){}
	}

	public static void connect(String host, int port) throws IOException{

		socket = (SocketConnection) Connector.open("socket://" +host +":" +port);
		stream = socket.openDataOutputStream();
	}

	public static void disconnect(){

		try{
			stream.close();
		} catch(IOException ignored){}
	}
};