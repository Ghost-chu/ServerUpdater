package com.mcsunnyside.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.net.URL;
import java.net.URLConnection;

public class Network {
	public static String GET(String link) throws IOException {
		   URL url = new URL(link);
           //使用方法打开链接,并使用connection接受返回值
           URLConnection connection = url.openConnection();
           //获取connection输入流,并用is接受返回值
           InputStream is = connection.getInputStream();
           //将字节流向字符流的转换
           InputStreamReader isr = new InputStreamReader(is);
           BufferedReader br = new BufferedReader(isr);

           String line;
           /**
            * StringBuilder和StringBuffer不同的地方在于 StringBuffer是线程安全的
            * 单线程、不需要线程安全的情况下，处于性能的考虑，优先选择StringBuilder
            */
           StringBuilder builder = new StringBuilder();
           while ((line = br.readLine())!= null) {
               builder.append(line);
           }

           br.close();
           isr.close();
           is.close();

           return builder.toString();

	}
}
