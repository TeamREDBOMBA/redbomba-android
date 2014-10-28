package com.Redbomba.Main.Detail;

import org.json.JSONArray;

import com.Redbomba.R;
import com.Redbomba.Settings.Functions;
import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FeedActivity extends Activity implements OnClickListener {
	private AQuery aq = new AQuery(this);

	private LinearLayout llFeedList;
	private LinearLayout llWriteFeedBtn;
	private ImageButton ibClose;

	private Bundle extra;
	private String extra_id = "";
	private String extra_type = "";

	private JSONArray ja;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		llFeedList = (LinearLayout)findViewById(R.id.llFeedList);

		llWriteFeedBtn = (LinearLayout)findViewById(R.id.llWriteFeedBtn);
		ibClose = (ImageButton)findViewById(R.id.ibClose);

		llWriteFeedBtn.setOnClickListener((OnClickListener) this);
		ibClose.setOnClickListener((OnClickListener)this);

	}

	class FeedListTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			ja = Functions.GET("mode=getFeed&fid="+extra_id+"&type="+extra_type);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				try{
					for(int i=0;i<ja.length();i++){
						FeedCellView fcv  = new FeedCellView(getApplication(),ja.getJSONObject(i), i);
						View v = fcv.getView();
						v.setTag("feed_"+i);
						v.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
								Vibe.vibrate(20);
								if(v.getTag()!=null){
									String click_head = v.getTag().toString();
									if(click_head.startsWith("feed_")){
										try {
											int no = Integer.parseInt(v.getTag().toString().replaceAll("feed_", ""));
											Intent gin = new Intent(FeedActivity.this,FeedDetailActivity.class);
											gin.putExtra("id", ja.getJSONObject(no).getString("id"));
											gin.putExtra("icon", ja.getJSONObject(no).getString("icon"));
											gin.putExtra("name", ja.getJSONObject(no).getString("name"));
											gin.putExtra("con", ja.getJSONObject(no).getString("con"));
											gin.putExtra("date", ja.getJSONObject(no).getString("date"));
											gin.putExtra("reply_no", ja.getJSONObject(no).getString("reply_no"));
											startActivity(gin);
											overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
										} catch (Exception e) { }
									}
								}
							}
						});
						llFeedList.addView(v);
					}
				}catch(Exception e){
					Log.i("error", ""+e.getMessage());
				}
			}

			return;
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		extra = getIntent().getExtras();
		extra_id = extra.getString("id");
		extra_type = extra.getString("type");
		if(llFeedList.getChildCount()!=0) llFeedList.removeAllViews();
		new FeedListTask().execute(null, null, null);
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
