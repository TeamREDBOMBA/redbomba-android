package com.Redbomba;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.ViewGroup;

public class Settings {

	private static Typeface mTypeface = null;

	public static int user_id = 0;
	public static JSONObject user_info = null;
	public static JSONObject group_info = null;

	public static Typeface setFont(Context con){
		if(mTypeface == null){
			if (Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.FROYO) {
				mTypeface = Typeface.createFromAsset(con.getAssets(), "font.ttf");
			}else{
				mTypeface = Typeface.SANS_SERIF;
			}
		}
		
		return mTypeface;
	}

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

	public static String stripHTML(String htmlStr) {
		Pattern p = Pattern.compile("<(?:.|\\s)*?>");
		Matcher m = p.matcher(htmlStr);
		return m.replaceAll("");
	}
}