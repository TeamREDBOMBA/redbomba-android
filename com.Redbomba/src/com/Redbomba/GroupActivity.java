package com.Redbomba;

import org.json.JSONException;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.Redbomba.R;

public class GroupActivity extends FragmentActivity implements OnClickListener {

	TextView tvGroupTitle;
	ImageButton btnSubBack;

	GroupFragAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	int Number = 0;

	private static final String[] CONTENT = new String[] { "그룹정보", "채팅", "탈퇴"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);

		tvGroupTitle = (TextView)findViewById(R.id.tvGroupTitle);
		btnSubBack = (ImageButton)findViewById(R.id.btnSubBack);
		btnSubBack.setOnClickListener((OnClickListener)this);

		mAdapter = new GroupFragAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);

		tvGroupTitle.setTypeface(Settings.setFont(this));
		try {
			tvGroupTitle.setText(Settings.group_info.getString("name"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try{
			Bundle in = getIntent().getExtras();
			indicator.setCurrentItem(in.getInt("tab"));
		}catch(Exception e){ }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);

		switch(v.getId()){
		case R.id.btnSubBack:
			finish();
			break;
		}

	}

	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	class GroupFragAdapter extends FragmentPagerAdapter {
		public GroupFragAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position){
			case 0:
				return new GroupInfoFrag();
			case 1:
				return new GroupChattingFrag();
			case 2:
				return new GroupDropoutFrag();
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}

