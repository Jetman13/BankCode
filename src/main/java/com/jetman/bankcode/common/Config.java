package com.jetman.bankcode.common;

public class Config {
	public static String STRO_PATH = "D:\\unicodeNew.csv";
	
	static{
		if (java.io.File.separator.equals("/")) {
			STRO_PATH = "/tmp/unicodeNew.csv";
		}
	}

}
