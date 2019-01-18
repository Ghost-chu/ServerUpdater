package com.mcsunnyside.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class Config {
	public static String readVer() throws IOException {
		Properties pro = new Properties();
		File file = new File("updater.properties");
		if(!file.exists())
			file.createNewFile();
		FileInputStream in = new FileInputStream("updater.properties");
		pro.load(in);
		in.close();
		return pro.getProperty("version","0");
	}
	public static void writeVer(String version) throws IOException {
		Properties pro = new Properties();
		FileInputStream in = new FileInputStream("updater.properties");
		pro.load(in);
		in.close();
		pro.setProperty("version",version);
		FileOutputStream oFile = new FileOutputStream("updater.properties");
		pro.store(oFile, null);
		oFile.close();
	}
}
