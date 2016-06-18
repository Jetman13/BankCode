package com.jetman.bankcode.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
	
	public static String getHtml(String urlStr, Object... obj) {
		String encoding = "utf-8";
		if (obj != null && obj.length > 0) {
			encoding = (String) obj[0];
		}
		URL url;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			System.out.println("request interface>>>>>>" + url);
//			trustAllHttpsCertificates();  
//			HttpsURLConnection.setDefaultHostnameVerifier(hv);  
			URLConnection URLconnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			httpConnection.setReadTimeout(60000);

			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream urlStream = httpConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, encoding));
				String sCurrentLine = "";
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} else {
				throw new RuntimeException("get URLConection fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("get URLConection fail", e);
		}
	//	System.out.println("result  interface<<<<<<" + sb.toString());
		return sb.toString();
	}

}
