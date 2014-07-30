package com.Redbomba;

import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service implements Runnable {

	public static final String TAG = "ServiceMine"; 
	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		prefs_system = getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();

		Thread myThread = new Thread(this);  
		myThread.start();  
	}

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
						if(tablename.equals("home_league")) tablename = "대회에 참가했습니다.";
						else if(tablename.equals("home_smile")) tablename = "스마일이 추가되었습니다.";
						else if(tablename.equals("home_group")) tablename = "그룹에 초대되었습니다.";
						else if(tablename.equals("home_leagueround")) tablename = "대회 일정이 발표되었습니다.";
						else if(tablename.equals("home_leaguematch")) tablename = "경기에 입장해주세요.";
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
	}  

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