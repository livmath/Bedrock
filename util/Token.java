package com.kraken.bedrock.util;

import java.util.Random;


public class Token{

	public static String random(){

		Random random = new Random();

		int token = ((random.nextInt() *0xffff) +(random.nextInt() *0xffff)
		+(random.nextInt() *0xffff) +(random.nextInt() *0x0fff |0x4000)
		+(random.nextInt() *0x3fff |0x8000) |(4 << (random.nextInt() *0xffff))
		|(2 << (random.nextInt() *0xffff)) |(random.nextInt() *0xffff));
		
		return String.valueOf(token);
	};
}
