package com.Redbomba.Group;

import org.json.JSONException;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.Redbomba.R;
import com.Redbomba.R.anim;
import com.Redbomba.R.id;
import com.Redbomba.R.layout;
import com.Redbomba.Settings.Settings;

public class GroupActivity extends FragmentActivity {

	GroupFragAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	int Number = 0;

	private static final String[] CONTENT = new String[] { "그룹정보", "채팅", "탈퇴"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);

		mAdapter = new GroupFragAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);

		try {
			setTitle(Settings.group_info.getString("name"));
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	public class GroupFragAdapter extends FragmentPagerAdapter {
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

