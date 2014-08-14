package com.Redbomba;

import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class GroupActivity extends FragmentActivity implements OnClickListener {

	TextView tvGroupTitle;
	ImageButton btnSubBack;

	ViewPager Tab;
	FragmentTabHost mTabHost;
	
	private String[] tabmenu = {"그룹페이지","채팅","탈퇴"};
	private Class[] tabclass = {GroupInfoFrag.class,GroupChattingFrag.class,GroupDropoutFrag.class};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		
		tvGroupTitle = (TextView)findViewById(R.id.tvGroupTitle);
		btnSubBack = (ImageButton)findViewById(R.id.btnSubBack);
		btnSubBack.setOnClickListener((OnClickListener)this);
		
		tvGroupTitle.setTypeface(Settings.setFont(this));
		try {
			tvGroupTitle.setText(Settings.group_info.getString("name"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setFragmentView();
		
		try{
			Bundle in = getIntent().getExtras();
			mTabHost.setCurrentTab(in.getInt("tab", 0));
		}catch (Exception e){ }

	}

	private void setFragmentView(){
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		for(int i=0; i<3; i++){
			mTabHost.addTab(mTabHost.newTabSpec(tabmenu[i]).setIndicator(tabmenu[i]), tabclass[i], null);
		}
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
}