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

public class GlobalCardActivity extends Activity implements OnClickListener {
	private AQuery aq = new AQuery(this);
	
	private ImageView ivSrc;
	private TextView tvTitle;
	private TextView tvTag;
	private TextView tvCon;
	private TextView tvComment;
	
	private LinearLayout llFeedBtn;
	private ImageButton ibClose;
	
	private Bundle extra;
	private String extra_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_global_card);
		
		extra = getIntent().getExtras();
		
		ivSrc = (ImageView)findViewById(R.id.ivSrc);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvTag = (TextView)findViewById(R.id.tvTag);
		tvCon = (TextView)findViewById(R.id.tvCon);
		tvComment = (TextView)findViewById(R.id.tvComment);
		
		llFeedBtn = (LinearLayout)findViewById(R.id.llFeedBtn);
		ibClose = (ImageButton)findViewById(R.id.ibClose);
		
		ibClose.setOnClickListener((OnClickListener) this);
		llFeedBtn.setOnClickListener((OnClickListener) this);
		
		tvTitle.setTypeface(Settings.setFont(this));
		tvTag.setTypeface(Settings.setFont(this));
		tvComment.setTypeface(Settings.setFont(this));
		
		aq.id(ivSrc).image("http://redbomba.net"+extra.getString("src"), true, true, 0, 0, null, AQuery.FADE_IN);
		tvTitle.setText(extra.getString("title"));
		tvTag.setText(extra.getString("tag"));
		tvComment.setText(extra.getString("comment_no","0")+"개의 코멘트 보기");
		extra_id = extra.getString("id");
		
		String markdownString = extra.getString("con");
		tvCon.setText(markdownString);
		tvCon.setMovementMethod(LinkMovementMethod.getInstance());
		
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
		case R.id.ibClose:
			finish();
			break;
		case R.id.llFeedBtn:
			Intent gin = new Intent(this,FeedActivity.class);
			gin.putExtra("id", extra_id);
			gin.putExtra("type", "g");
			startActivity(gin);
			overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
			break;
		}
		
	}
}
