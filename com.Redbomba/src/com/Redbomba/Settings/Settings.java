package com.Redbomba.Settings;

import org.json.JSONObject;

import android.app.Application;

public class Settings extends Application{
	public int NotiCount = 0;

	public int user_id = 0;
	public JSONObject user_info = null;
	public JSONObject group_info = null;
}