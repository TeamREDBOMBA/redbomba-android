package com.Redbomba;

import java.net.MalformedURLException;
import java.net.URL;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Redbomba.LoginActivity.LoginTask;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	private SocketIO socket;

	public static final String BROADCAST_ACTION = "com.Redbomba.updateprogress";
	public static final String TAG = "ServiceMine"; 
	Intent intent;
	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	private int uid;
	private int gid;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		intent = new Intent(BROADCAST_ACTION);
		prefs_system = getSharedPreferences("system", 0);
		uid = prefs_system.getInt("uid", 0);

		Log.i(""+uid,""+uid);
		
		new SocketTask().execute(null, null, null);

	}

	class SocketTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... Void) {
			try{
				JSONObject jo = Settings.GET("mode=2&uid="+uid).getJSONObject(0);
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
			}
			return;
		}
	}

	private void startSocket(){
		try {

			socket = new SocketIO("http://14.63.186.76:8080");
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
					if (event.equals("html")){
						intent.putExtra("emit", "html");
						JSONObject jo;
						try {
							jo = new JSONObject(args[0].toString());
							if((jo.getString("name")).equals("#noti_value")){
								intent.putExtra("name", jo.getString("name"));
								intent.putExtra("value", jo.getInt("html"));
								sendBroadcast(intent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

	/*
	public void run() {  
		// TODO Auto-generated method stub  
		int notiNo = 0;
		while(true){  
			try{ 
				JSONArray ja = Settings.GET("mode=Notification&uid=31");
				for(int i=0; i<ja.length(); i++){
					notiNo = prefs_system.getInt("notiNo", 0);
					Log.i(""+notiNo,""+ja.getJSONObject(i).getInt("no"));
					if(notiNo < ja.getJSONObject(i).getInt("no")){
						String tablename = ja.getJSONObject(i).getString("tablename");
						if(tablename.equals("home_league")) tablename = "��ȸ�� �����߽��ϴ�.";
						else if(tablename.equals("home_smile")) tablename = "�������� �߰��Ǿ����ϴ�.";
						else if(tablename.equals("home_group")) tablename = "�׷쿡 �ʴ�Ǿ����ϴ�.";
						else if(tablename.equals("home_leagueround")) tablename = "��ȸ ������ ��ǥ�Ǿ����ϴ�.";
						else if(tablename.equals("home_leaguematch")) tablename = "��⿡ �������ּ���.";
						setNotification(Settings.stripHTML(ja.getJSONObject(i).getString("con")), tablename);
						editor_system.putInt("notiNo", ja.getJSONObject(i).getInt("no"));
						editor_system.commit();
					}
				}
				Thread.sleep(10000);
			}catch(Exception ex){  
				Log.e(TAG, ex.toString());  
			}  
		}  
	}  */

	@SuppressWarnings({ "deprecation" })
	private void setNotification(String msg, String title){
		NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.ic_launcher,title,System.currentTimeMillis());
		Intent intent = new Intent(NotificationService.this, LoginActivity.class);
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