package com.mcsunnyside.updater;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;




public class Checker {
	public Checker() {
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() throws IOException {
		System.out.println("Checking the server update...");
		String json = Network.GET("https://launchermeta.mojang.com/mc/game/version_manifest.json");
		
		System.out.print(json);
		update(json);
		
	}
	public void update(String jsonString) throws IOException {
		String lastet = (String) JSON.parseObject(jsonString).getJSONObject("latest").get("snapshot");
		if(Config.readVer()!=lastet) {
			System.out.println("Local version is "+Config.readVer());
			System.out.println("Remote version is "+lastet);
			System.out.println("Updating the server jar file...");
			JSONObject serverJSONObject= JSON.parseObject(jsonString);
			String gameJSONurl = (String) serverJSONObject.getJSONArray("versions").getJSONObject(0).get("url");
			String snapshot = (String) serverJSONObject.getJSONArray("versions").getJSONObject(0).get("id");
			String serverJSON = Network.GET(gameJSONurl);
			System.out.println("Download JSON completed.");
			System.out.println(serverJSON);
			JSONObject jsonObject = JSON.parseObject(serverJSON);
			JSONObject jsonDownloadObject=  jsonObject.getJSONObject("downloads");
			JSONObject jsonServerDownloadObject = jsonDownloadObject.getJSONObject("server");
			String serverJARurl = (String) jsonServerDownloadObject.get("url");
			System.out.println(serverJARurl);
			saveBinaryFile(new URL(serverJARurl));
			Config.writeVer(lastet);
//			int i = 0;
//			while (array.size()>i) {
//				if(array.getJSONObject(i).get("id").toString()==lastet) {
//					File file = new File("server.jar");
//					if(file.exists())
//						file.delete();
//					System.out.println("Downloading the server jar file...");
//					saveBinaryFile(new URL((String)array.getJSONObject(i).get("url")));
//					System.out.println("Download completed.");
//					Config.writeVer(lastet);
//					break;
//				}else {
//					i++;
//				}
				
			}
			System.out.println("Finish");
		}
//		String lastet = jsonObject.getJSONObject("lastet").getString("snapshot");
//		if(Config.readVer()!=lastet) {
//			int i = 0;
//			while (jsonObject.getJSONArray("versions").getJSONObject(i)!=null){
//				String version = jsonObject.getJSONArray("versions").getJSONObject(i).getString("id");
//				if(version==lastet) {
//					System.out.println("Downloading from" +jsonObject.getJSONArray("versions").getJSONObject(i).getString("url"));
//					saveBinaryFile(new URL(jsonObject.getJSONArray("versions").getJSONObject(i).getString("url")));
//					break;
//				}
//					
//				}
//				
//			}

public void saveBinaryFile(URL u) throws IOException {
		URLConnection uc = u.openConnection();
		String contentType = uc.getContentType();
		int contentLength = uc.getContentLength();
		if (contentType.startsWith("text/") || contentLength == -1) {
			throw new IOException("This is not a binary file.");
		}
		try (InputStream raw = uc.getInputStream()) {
			InputStream in = new BufferedInputStream(raw);
			byte[] data = new byte[contentLength];
			int offset = 0;
			while (offset < contentLength) {
				int bytesRead = in.read(data, offset, data.length - offset);
				if (bytesRead == -1) {
					break;
				}
				offset += bytesRead;
			}
			if (offset != contentLength) {
				throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes.");
			}
			String fileName = "server.jar";
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
			try (FileOutputStream fout = new FileOutputStream(fileName)) {
				fout.write(data);
				fout.flush();
			}
		}
	
}
}