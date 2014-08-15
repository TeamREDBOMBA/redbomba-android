package com.Redbomba;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;
import net.simonvt.menudrawer.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private AQuery aq = new AQuery(this);

	MenuDrawer mLeft, mRight;
	ImageButton leftbutton, rightbutton;
	TextView tvNotiLength;

	LinearLayout llGroupList;

	LinearLayout llgamelink;
	LinearLayout llgroup;
	ImageButton btnSetting, btnLeftBack;
	ImageView imgUsericon, imgGroupimg;
	TextView tvUsername, tvGamelink, tvGroupname;

	TextView tvLength;
	LinearLayout llTable;

	private JSONArray ja;
	private Handler mHandler;
	private boolean mFlag = false;
	private BroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		try {
			Settings.user_info = Settings.GET("mode=2&uid="+Settings.user_id).getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		leftbutton = (ImageButton) findViewById(R.id.leftbutton);
		rightbutton = (ImageButton) findViewById(R.id.rightbutton);
		tvNotiLength = (TextView) findViewById(R.id.tvNotiLength);

		leftbutton.setOnClickListener((OnClickListener) this);
		rightbutton.setOnClickListener((OnClickListener) this);

		setLeftMenu();
		setRightMenu();
		new GroupListTask().execute(null, null, null);

		setHandler();
		setBroadcast();

		ComponentName componentName = new ComponentName("com.Redbomba", "com.Redbomba.NotificationService");
		Intent service_intent = new Intent();
		service_intent.setComponent(componentName);
		startService(service_intent);

		loadNoti();
	}

	class GroupListTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			llGroupList = (LinearLayout)findViewById(R.id.llGroupList);
			ja = Settings.GET("mode=getGroupList&uid="+Settings.user_id);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				Log.i("ja_size", ""+ja.length());
				try{
					for(int i=0;i<ja.length();i++){
						GroupCellView gcv  = new GroupCellView(MainActivity.this,ja.getJSONObject(i));
						View v = gcv.getView();
						v.setTag("group_"+i);
						v.setOnClickListener((OnClickListener) MainActivity.this);
						gcv.llbtnChatting.setTag("group_"+i);
						gcv.llbtnChatting.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								int no = Integer.parseInt(v.getTag().toString().replaceAll("group_", ""));
								Intent gin = new Intent(MainActivity.this,GroupActivity.class);
								gin.putExtra("tab", 1);
								startActivity(gin);
								try {
									Settings.group_info = ja.getJSONObject(no);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
							}
						});
						llGroupList.addView(gcv.getView());
					}
				}catch(Exception e){ Log.i("error", e.getMessage()); }
			}

			return;
		}
	}

	private void setLeftMenu(){
		Log.i("LEFT","CALLED");
		String urlUsericon = "";
		String urlGroupimg = "";
		String username = "";
		String user_icon = "";
		String gamelink = "";
		String groupname = "";
		String groupimg = "";
		try{
			username = Settings.user_info.getString("username");
			user_icon = Settings.user_info.getString("user_icon");
			gamelink = Settings.user_info.getString("gamelink");
			groupname = Settings.user_info.getString("groupname");
			groupimg = Settings.user_info.getString("groupimg");
			urlUsericon = "http://redbomba.net/static/img/icon/usericon_"+user_icon+".jpg";
			urlGroupimg = "http://redbomba.net/media/group_icon/"+groupimg;
		}catch (Exception e) {
			// TODO: handle exception
		}

		mLeft = MenuDrawer.attach(this, Type.OVERLAY, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		if(width>=640)
			mLeft.setMenuSize(600);
		else if(width<640)
			mLeft.setMenuSize(400);
		mLeft.setContentView(R.layout.activity_main);
		mLeft.setMenuView(R.layout.sliding_left_menu);
		mLeft.setDropShadowEnabled(false);

		llgamelink = (LinearLayout)findViewById(R.id.llgamelink);
		llgroup = (LinearLayout)findViewById(R.id.llgroup);
		btnSetting = (ImageButton) findViewById(R.id.btnSetting);
		btnLeftBack = (ImageButton) findViewById(R.id.btnLeftBack);
		imgUsericon = (ImageView) findViewById(R.id.imgUsericon);
		imgGroupimg = (ImageView) findViewById(R.id.imgGroupimg);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvGamelink = (TextView) findViewById(R.id.tvGamelink);
		tvGroupname = (TextView) findViewById(R.id.tvGroupname);

		tvUsername.setTypeface(Settings.setFont(this));
		tvGamelink.setTypeface(Settings.setFont(this));
		tvGroupname.setTypeface(Settings.setFont(this));

		btnSetting.setOnClickListener((OnClickListener) this);
		btnLeftBack.setOnClickListener((OnClickListener) this);

		if(!user_icon.equals("")){
			aq.id(imgUsericon).image(urlUsericon);
			aq.id(imgGroupimg).image(urlGroupimg);
		}
		tvUsername.setText(username);
		if(gamelink.equals("null")){
			llgamelink.removeAllViews();
			TextView tv = new TextView(this);
			tv.setText("연결된 게임이 없습니다.");
			tv.setTextColor(Color.WHITE);
			tv.setPadding(20, 0, 0, 0);
			llgamelink.addView(tv);
		}else{
			tvGamelink.setText(gamelink);
		}
		if(groupname.equals("0")){
			llgroup.removeAllViews();
			TextView tv = new TextView(this);
			tv.setText("소속된 그룹이 없습니다.");
			tv.setTextColor(Color.WHITE);
			llgroup.addView(tv);
		}else{
			if(groupname.length() > 13) groupname = groupname.substring(0, 13)+"...";
			tvGroupname.setText(groupname);
		}
	}

	private void setRightMenu(){
		Log.i("RIGHT","CALLED");

		mRight = MenuDrawer.attach(this, Type.OVERLAY, Position.RIGHT,
				MenuDrawer.MENU_DRAG_WINDOW);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		if(width>=640)
			mRight.setMenuSize(600);
		else if(width<640)
			mRight.setMenuSize(400);
		mRight.setContentView(R.layout.activity_main);
		mRight.setMenuView(R.layout.sliding_right_menu);
		mRight.setDropShadowEnabled(false);

		tvLength = (TextView)findViewById(R.id.tvLength);
		llTable = (LinearLayout)findViewById(R.id.llTable);
	}

	private void loadNoti(){
		llTable.removeAllViews();
		JSONArray ja = null;
		int i=0;
		try{
			ja = Settings.GET("mode=Notification&uid="+Settings.user_id);
			int ja_len = ja.length();
			for(i=0;i<ja_len;i++){
				Log.i("con",ja.getJSONObject(i).getString("con"));
				Log.i("imgurl",ja.getJSONObject(i).getString("imgurl"));
				Log.i("date_updated",""+ja.getJSONObject(i).getInt("date_updated"));
				Log.i("now",""+ja.getJSONObject(i).getInt("now"));
				Log.i("time",""+ja.getJSONObject(i).getString("time"));
				int no = ja.getJSONObject(i).getInt("no");
				String strCon = ja.getJSONObject(i).getString("con");
				String strDate = ja.getJSONObject(i).getString("time");
				String strImg = ja.getJSONObject(i).getString("imgurl");
				NotiCellView ncv  = new NotiCellView(this,strCon,strDate,strImg);
				View v = ncv.getView();
				v.setTag("noti_"+no);
				v.setOnClickListener((OnClickListener) this);
				llTable.addView(ncv.getView());
			}
			tvLength.setText(ja_len+"");
			if(ja_len!=0){
				tvNotiLength.setText(ja.length()+"");
				tvNotiLength.setVisibility(View.VISIBLE);
			}else{
				tvNotiLength.setText(0);
				tvNotiLength.setVisibility(View.GONE);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);

		switch(v.getId()){
		case R.id.leftbutton:
			mLeft.openMenu();
			break;
		case R.id.rightbutton:
			mRight.openMenu();
			break;
		case R.id.btnLeftBack:
			mLeft.closeMenu();
			break;
		}

		if(v.getTag()!=null){
			String click_head = v.getTag().toString();
			if(click_head.startsWith("noti_")){
				try{
					int no = Integer.parseInt(v.getTag().toString().replaceAll("noti_", ""));
					JSONObject jo = Settings.GET("mode=NotificationDel&no="+no).getJSONObject(0);
					if(jo.getInt("result")==1){
						loadNoti();
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				Log.i(""+v.getTag(),""+v.getTag());
			}else if(click_head.startsWith("group_")){
				try {
					int no = Integer.parseInt(v.getTag().toString().replaceAll("group_", ""));
					Intent gin = new Intent(this,GroupActivity.class);
					startActivity(gin);
					Settings.group_info = ja.getJSONObject(no);
					overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
				} catch (Exception e) { }
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!mFlag) {
				Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle extra = intent.getExtras();

				if(extra.getString("emit","").equals("html")){

					if(extra.getString("name","").equals("#noti_value")){
						tvNotiLength.setText(""+extra.getInt("value", 0));
						tvLength.setText(""+extra.getInt("value", 0));
						if(!tvNotiLength.equals("")) tvNotiLength.setVisibility(View.VISIBLE);
						else tvNotiLength.setVisibility(View.GONE);
					}

				}
			}
		};

		registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_01));
	}

	public void setHandler(){
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0) {
					mFlag = false;
				}
			}
		};
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
