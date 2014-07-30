package com.Redbomba;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

public class Settings {
	
	public static int user_id = 0;
	
	public static JSONArray GET(String var){
		String jsonStr = null;
		jsonStr = downloadHtml("http://redbomba.net/mobile/?"+var);
		jsonStr = jsonStr.trim();
		try{
			JSONArray ja = new JSONArray(jsonStr);
			return ja;
		}catch(Exception e){
			Log.i("JSON ERROR",""+e.getMessage());
		}
		return null;
	}

	static String downloadHtml(String addr){
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StringBuilder html = new StringBuilder();
		try{
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(conn!=null){
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
					while(true){
						String line = br.readLine();
						Log.i("httptest",line);
						if(line==null) break;
						html.append(line+"\n");
					}
					br.close();
				}
				conn.disconnect();
			}
		}catch(Exception ex){}
		return html.toString();
	}
	
	public static Bitmap getRemoteImage(final URL aURL) { 
		Bitmap bm = null;
		try { 
			final URLConnection conn = aURL.openConnection(); 
			conn.connect(); 
			final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream()); 
			bm = BitmapFactory.decodeStream(bis); 
			bis.close(); 

		} catch (IOException e) { 
			Log.d("DEBUGTAG", "Oh noooz an error..."); 
		} 
		return bm; 
	}
	
	public static String stripHTML(String htmlStr) {
        Pattern p = Pattern.compile("<(?:.|\\s)*?>");
        Matcher m = p.matcher(htmlStr);
        return m.replaceAll("");
    }
}