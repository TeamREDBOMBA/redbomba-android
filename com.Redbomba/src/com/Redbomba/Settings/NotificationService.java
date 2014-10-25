package com.Redbomba.Settings;

import java.net.MalformedURLException;
import java.util.List;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Redbomba.R;
import com.Redbomba.Group.GroupChattingFrag;
import com.Redbomba.Group.GroupInfoFrag;
import com.Redbomba.Landing.LandingActivity;
import com.Redbomba.Main.MainProfileFrag;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

public class NotificationService extends Service {

	private SocketIO socket;

	public static final String BROADCAST_ACTION_01 = "com.Redbomba.setNotification";
	public static final String BROADCAST_ACTION_03 = "com.Redbomba.setMemOnOff";
	public static final String BROADCAST_ACTION_04 = "com.Redbomba.getChatting";
	
	public static final String TAG = "ServiceMine"; 
	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	private int uid;
	private int gid;
	
	private Intent intent1;
	private Intent intent3;
	private Intent intent4;
	
	Settings settings;

	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		settings = (Settings) getApplicationContext();

		intent1 = new Intent(BROADCAST_ACTION_01);
		intent3 = new Intent(BROADCAST_ACTION_03);
		intent4 = new Intent(BROADCAST_ACTION_04);
		
		prefs_system = getSharedPreferences("system", 0);
		uid = prefs_system.getInt("uid", 0);

		new SocketTask().execute(null, null, null);

	}

	class SocketTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... Void) {
			try{
				JSONObject jo = Functions.GET("mode=getUserInfo&uid="+uid).getJSONObject(0);
				gid = jo.getInt("gid");
			}catch (Exception e) {
				// TODO: handle exception
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				startSocket();
				setBroadcast_2();
				setBroadcast_5();
				setBroadcast_6();
			}
			return;
		}
	}

	public void setBroadcast_2(){
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_2","LOAD");
				Bundle extra = intent.getExtras();
				String[] str_mem_list = extra.getStringArray("member_list");
				for(int i=0; i<str_mem_list.length;i++)
					socket.emit("isOnlineOne", str_mem_list[i]);
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(GroupInfoFrag.BROADCAST_ACTION_02));
	}
	
	public void setBroadcast_5(){
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_5","LOAD");
				Bundle extra = intent.getExtras();
				try {
					String usericon = extra.getString("icon");
					String username = extra.getString("name");
					String contents = extra.getString("con");
					
					JSONObject jo_info = new JSONObject();
					jo_info.put("name", username);
					jo_info.put("con", contents);
					jo_info.put("icon", usericon);
					
					socket.emit("chatGroup", jo_info);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(GroupChattingFrag.BROADCAST_ACTION_05));
	}
	
	public void setBroadcast_6(){
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_6","LOAD");
				socket.disconnect();
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(MainProfileFrag.BROADCAST_ACTION_06));
	}

	private void startSocket(){
		try {

			socket = new SocketIO("http://redbomba.net:3000");
			socket.connect(new IOCallback() {

				@Override
				public void onDisconnect() {
					// TODO Auto-generated method stub
					System.out.println("Connection terminated.");
				}

				@Override
				public void onConnect() {
					// TODO Auto-generated method stub
					System.out.println("Connection established");
					Log.i("OnConnect", "@@@@@@@@@@@@@@@@@@@@@@ "+uid);
					socket.emit("joinGroup", gid);
					socket.emit("addUser", uid);
					socket.emit("loadNotification", uid);
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {
					// TODO Auto-generated method stub
					System.out.println("Server said: " + data);
				}

				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					// TODO Auto-generated method stub
					try {
						System.out.println("Server said:" + json.toString(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					// TODO Auto-generated method stub
					System.out.println("Server triggered event '" + event + "'");
					try {

						JSONObject jo = new JSONObject(args[0].toString());

						if (event.equals("html")){
							intent1.putExtra("emit", "html");
							if((jo.getString("name")).equals("#noti_value")){
								intent1.putExtra("name", jo.getString("name"));
								intent1.putExtra("value", jo.getInt("html"));
								sendBroadcast(intent1);
							}
						}else if(event.equals("isOnline")){
							Log.i("isOnline!!!!!!!!!!!!!!",""+jo.getString("id"));
							intent3.putExtra("emit", "isOnline");
							intent3.putExtra("Member",jo.getString("id"));
							sendBroadcast(intent3);
						}else if(event.equals("isOffline")){
							Log.i("isOffline!!!!!!!!!!!!!",""+jo.getString("id"));
							intent3.putExtra("emit", "isOffline");
							intent3.putExtra("Member",jo.getString("id"));
							sendBroadcast(intent3);
						}else if(event.equals("setChat")){
							Log.i("setChat!!!!!!!!!!!!!",""+jo.getString("name"));
							intent4.putExtra("emit", "setChat");
							intent4.putExtra("name",jo.getString("name"));
							intent4.putExtra("con",jo.getString("con"));
							intent4.putExtra("icon",jo.getString("icon"));
							Functions.setBadge(getApplication(),settings.NotiCount++);
							sendBroadcast(intent4);
						}


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onError(SocketIOException socketIOException) {
					// TODO Auto-generated method stub
					System.out.println("an Error occured");
					socketIOException.printStackTrace();
				}

			});

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setNotification(String msg, String title){
		NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.ic_launcher,title,System.currentTimeMillis());
		Intent intent = new Intent(NotificationService.this, LandingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", msg);
		PendingIntent pending = PendingIntent.getActivity(NotificationService.this, 0, intent, 0);
		noti.icon = R.drawable.ic_launcher;
		noti.setLatestEventInfo(NotificationService.this, title, msg, pending);
		noti.defaults |= Notification.DEFAULT_ALL;
		noti.flags |= Notification.FLAG_INSISTENT;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notiMgr.notify(0, noti);
	}

	static String replaceAll(String strOriginal, String from, String to){
		int startPos;
		String strReplacedString = strOriginal;
		while(strReplacedString.indexOf(from) != -1)
		{
			startPos = strReplacedString.indexOf(from);
			strReplacedString = strReplacedString.substring(0, startPos) + to + strReplacedString.substring(startPos + from.length());
		}
		return strReplacedString;
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}