package com.Redbomba.Group;

import org.json.JSONException;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;

public class GroupActivity extends FragmentActivity implements OnPageChangeListener {

	GroupFragAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	int Number = 0;
	
	Settings settings;

	private static final String[] CONTENT = new String[] { "그룹홈", "채팅"};
	private static final int[] ICONS = new int[] {
		R.drawable.tab_home_selector,
		R.drawable.tab_chat_selector,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		settings = (Settings) getApplicationContext();

		mAdapter = new GroupFragAdapter(getSupportFragmentManager());

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(1);

		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setOnPageChangeListener(this);

		try {
			setTitle(settings.group_info.getString("name"));
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

	public class GroupFragAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
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
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
}

