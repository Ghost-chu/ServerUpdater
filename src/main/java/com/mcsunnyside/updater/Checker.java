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
		update(json);
		
	}
	public void update(String jsonString) throws IOException {
		String lastet = (String) JSON.parseObject(jsonString).getJSONObject("latest").get("snapshot");
		if(!Config.readVer().equals(lastet)) {
			System.out.println("Local version is "+Config.readVer());
			System.out.println("Remote version is "+lastet);
			System.out.println("Updating the server jar file...");
			JSONObject serverJSONObject= JSON.parseObject(jsonString);
			System.out.println("Update Source: Mojang API");
			String serverJSON = Network.GET((String) serverJSONObject.getJSONArray("versions").getJSONObject(0).get("url"));
			System.out.println("Reading download url from json file...");
			String serverJARurl=  (String) JSON.parseObject(serverJSON).getJSONObject("downloads").getJSONObject("server").get("url");
			System.out.println("ServerJar url:"+serverJARurl);
			System.out.println("Downloading server...");
			
			saveBinaryFile(new URL(serverJARurl));
			System.out.println("Download server completed...");
			Config.writeVer(lastet);
			}else {
				System.out.println("Nothing to update.");
			}
			System.out.println("Finish");
		}

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