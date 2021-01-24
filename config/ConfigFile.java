package com.kraken.bedrock.config;

import com.kraken.bedrock.debug.Console;
import com.kraken.bedrock.io.FileUtil;
import com.kraken.bedrock.io.InputStreamStringReader;
import com.kraken.bedrock.struct.Enumeration;
import com.kraken.bedrock.struct.Hashtable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

//config file that tries to support most basic formats
public final class ConfigFile{

	//have this for other things using config file
	public static final char ACCESSOR_DELIMITER = '.';

	private String path;
	private int sectionCount;
	private final Hashtable properties = new Hashtable();

	public ConfigFile(String path){

		this.path = path;

		try{
			loadProperties(FileUtil.open(path).openDataInputStream());
		} catch(IOException ignored){}
	}

	public ConfigFile(InputStream stream){

		try{

			if(stream != null){
				loadProperties(stream);
			} else Console.log("Stream is null!");
		} catch(IOException ignored){}
	}
	
	public void loadProperties(InputStream inputStream) throws IOException{

		Console.log("Reading properties!");
		
		InputStreamStringReader stream = new InputStreamStringReader(inputStream);

		String currentSection = "";
		boolean hasLoggedError = false;

		String line;
		while((line = stream.readLine()) != null){

			char[] chars = (line +"\n").toCharArray();

			String token = "";
			boolean isReadingSectionHeader = false;

			String fieldName = null	;
			
			for(int i = 0; i < chars.length; i++){

				char chr = chars[i];
				
				switch(chr){

					case '[':

						token = "";
						isReadingSectionHeader = true;
						break;

					case ']':

						if(token.charAt(0) == '.'){ //subsection of current if nothing comes before
							currentSection += token;
						} else currentSection = token;

						currentSection = currentSection.toLowerCase();
						Console.log("Section is: " +currentSection);

						isReadingSectionHeader = false;
						
						i = chars.length; //anything on the same line is rubbish
						break;

					case '=':
					case ':':
	
						if(isReadingSectionHeader && !hasLoggedError){

							Console.log("Assignment in section definition header, ignoring rest of line.");
							chars[i +1] = ']'; //acts as a jump to the right case

							hasLoggedError = true;
							
							break;
						}

						fieldName = token;
						token = "";
						break;

					//ignore special characters
					case ' ':
					case '\'':
					case '"':
					case '!':
					case '$':
					case '%':
					case '^':
					case '&':
					case '*':
					case '(':
					case ')':

						if(isReadingSectionHeader){

							Console.log("Use of illegal chars, they will be ignored.");
							chars[i++] = ']';
						} else token += chr;
						break;

					case ';':
					case '#':
					case '\n':

						if(isReadingSectionHeader){ //hasn't finished
							chars[i--] = ']'; //go back one, replacing '\n' with bracket
						} else if(fieldName != null){ //finish reading value

							setProperty(currentSection, fieldName, token);
							fieldName = null;
							
							i = chars.length; //just make sure to end
						} else i = chars.length; //comment, ignore rest of line
						break;
						
					default:
						token += chr;
						break;
				}
			}
		}

		if(hasLoggedError)
			Console.log("Encountered errors. File should be cleaned.");

		stream.close();
	}
	
	public void move(String newPath){

		try{

			FileUtil.move(path, newPath);
			path = newPath;
		} catch(IOException ignored){}
	}

	public int sectionCount(){
		return sectionCount;
	}

	public boolean hasSection(String section){
	
		boolean isFound = false;
		
		for(Enumeration e = properties.keys(); e.hasMoreElements();){

			String key = (String) e.nextElement();
			
			if(key.indexOf(section) == 0){
			
				isFound = true;
				break;
			 }
		}
		
		return isFound;
	}
	
	public void save(){


	}
	
	public Hashtable getSectionProperties(String section){

		Hashtable sectionProperties = new Hashtable();

		for(Enumeration e = properties.keys(); e.hasMoreElements();){

			String key = (String) e.nextElement();

			if(key.indexOf(section) == 0) //contains section name at start
				sectionProperties.put(key, properties.get(key));
		}

		return sectionProperties;
	}
	
	public String getProperty(String section, String field){
		return getProperty(section +':' +field);
	}
	
	public String getProperty(String id){

		id = id.toLowerCase().replace(':', '.');
		return (String) properties.get(id);
	}

	public String getNonNullProperty(String id){

		String value = getProperty(id);
		return (value == null? "": value);
	}
	
	public int getPropertyInt(String section, String field){
		return getPropertyInt(section +':' +field);
	}

	public int getPropertyInt(String id){

		int value;
		
		try{
			value = Integer.parseInt(getProperty(id));
		} catch(NumberFormatException exc){
			value = -1;
		}

		return value;
	}

	public void setProperty(String section, String field, String value){
		setProperty(section +':' +field, value);
	}

	public void setProperty(String id, String value){

		id = id.toLowerCase().replace(':', '.').trim();
		value = value.trim();

		Console.log("Set property: \"" +id +"\"");
		properties.put(id, value);
	}
};