package com.Redbomba.Main;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;
import net.simonvt.menudrawer.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Redbomba.R;
import com.Redbomba.Settings.CustomTabIndicator;
import com.Redbomba.Settings.NotificationService;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnPageChangeListener{
	private AQuery aq = new AQuery(this);

	static TextView tvMainTitle;

	MenuDrawer mRight;

	TextView tvLength;
	LinearLayout llTable;

	MainFragAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	int Number = 0;

	private static final String[] CONTENT = new String[] { "글로벌아티클", "활동스트림", "그룹", "프로필"};
	private static final int[] ICONS = new int[] {
		R.drawable.ic_tab_global,
		R.drawable.ic_tab_private,
		R.drawable.ic_tab_group,
		R.drawable.ic_tab_profile,
	};

	private Handler mHandler;
	private boolean mFlag = false;
	private BroadcastReceiver broadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setRightMenu();
		setPager();
		onPageSelected(0);

		setHandler();
		setBroadcast();

		ComponentName componentName = new ComponentName("com.Redbomba", "com.Redbomba.Settings.NotificationService");
		Intent service_intent = new Intent();
		service_intent.setComponent(componentName);
		startService(service_intent);

		loadNoti();
	}

	private void setPager(){
		mAdapter = new MainFragAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.mainPager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(3);

		CustomTabIndicator indicator = (CustomTabIndicator)findViewById(R.id.mainIndicator);
		indicator.setViewPager(mPager);
		indicator.setOnPageChangeListener(this);

		try{
			Bundle in = getIntent().getExtras();
			indicator.setCurrentItem(in.getInt("tab"));
		}catch(Exception e){ }
	}

	private void setRightMenu(){
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
				int no = ja.getJSONObject(i).getInt("no");
				String strCon = ja.getJSONObject(i).getString("con");
				String strDate = ja.getJSONObject(i).getString("time");
				String strImg = ja.getJSONObject(i).getString("img");
				NotiCellView ncv  = new NotiCellView(this,strCon,strDate,strImg);
				View v = ncv.getView();
				v.setTag("noti_"+no);
				llTable.addView(ncv.getView());
			}
			tvLength.setText(ja_len+"");

		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_notification:
			mRight.openMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
						//tvNotiLength.setText(""+extra.getInt("value", 0));
						tvLength.setText(""+extra.getInt("value", 0));

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

	class MainFragAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		public MainFragAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
			case 0:
				return new MainGlobalFrag();
			case 1:
				return new MainPrivateFrag();
			case 2:
				return new MainGroupFrag();
			case 3:
				return new MainProfileFrag();
			}
			return null;
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return ICONS[index];
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int i) {
		// TODO Auto-generated method stub
		setTitle(CONTENT[i]);
	}

}
