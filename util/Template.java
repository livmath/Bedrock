package com.kraken.bedrock.util;

import com.kraken.bedrock.struct.Enumeration;
import com.kraken.bedrock.struct.Hashtable;


public class Template{

	protected String str;
	protected String templateBlank;
	
	public Template(String str){
	
		this.str = str;
		templateBlank = new String(str);
	}
	
	public void set(String id, String value){
		str = StringUtil.replace(str, "{#" +id +"}", value);
	}

	public void fill(Hashtable fields){

		for(Enumeration e = fields.keys(); e.hasMoreElements();){

			String key = (String) e.nextElement();
			set(key, (String) fields.get(key));
		}
	}

	public void clean(){
		str = StringUtil.replace(str, "/{#*}/g", "");
	}
	
	public String toString(){

		clean();
		return str;
	}
};