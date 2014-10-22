package com.Redbomba.Main.Detail;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedActivity extends Activity implements OnClickListener {
	private AQuery aq = new AQuery(this);
	
	private LinearLayout llWriteFeedBtn;
	private ImageButton ibClose;
	
	private Bundle extra;
	private String extra_id = "";
	private String extra_type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		
		extra = getIntent().getExtras();
		
		llWriteFeedBtn = (LinearLayout)findViewById(R.id.llWriteFeedBtn);
		ibClose = (ImageButton)findViewById(R.id.ibClose);
		
		llWriteFeedBtn.setOnClickListener((OnClickListener) this);
		ibClose.setOnClickListener((OnClickListener)this);
		
		extra_id = extra.getString("id");
		extra_type = extra.getString("type");
		
	}
	
	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);
		
		switch(v.getId()){
		case R.id.llWriteFeedBtn:
			Intent gin = new Intent(this,WriteFeedActivity.class);
			gin.putExtra("id", extra_id);
			gin.putExtra("type", extra_type);
			startActivity(gin);
			overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
			break;
		case R.id.ibClose:
			finish();
			break;
		}
	}
}
