package com.Redbomba.Main.Detail;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.Redbomba.R;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.Settings;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

public class FeedDetailActivity extends Activity {
	
	private LinearLayout ll_feed_contents;
	private LinearLayout llReplyList;
	
	private EditText etCon;
	private ImageButton btnWrite;

    Settings settings;
    private Bundle extra;
	private String extra_id = "";
	private String[] extra_con = new String[7];
	
	private JSONArray ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        
        settings = (Settings) getApplicationContext();
        getExtra();

        ll_feed_contents = (LinearLayout)findViewById(R.id.ll_feed_contents);
        llReplyList = (LinearLayout)findViewById(R.id.llReplyList);
        
        etCon = (EditText)findViewById(R.id.etCon);
        btnWrite = (ImageButton)findViewById(R.id.btnWrite);
        
        btnWrite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeComment(String.valueOf(settings.user_id),extra_id,etCon.getText().toString());
				etCon.setText("");
				onResume();
			}
		});

    }
    
    @Override
	public void onResume(){
		super.onResume();
		getExtra();
		if(ll_feed_contents.getChildCount()!=0) ll_feed_contents.removeAllViews();
		if(llReplyList.getChildCount()!=0) llReplyList.removeAllViews();
		new FeedReplyTask().execute(null, null, null);
	}
    
    class FeedReplyTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			ja = Functions.GET("mode=getFeedComments&fid="+extra_id);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				try{
					for(int i=0;i<ja.length();i++){
						FeedReplyCellView frcv  = new FeedReplyCellView(getApplication(),ja.getJSONObject(i), i);
						View v = frcv.getView();
						llReplyList.addView(v);
					}
					extra_con[5] = ja.length()+"";
					FeedCellView fcv  = new FeedCellView(getApplication(),extra_con, 0);
					ll_feed_contents.addView(fcv.getView());
				}catch(Exception e){
					Log.i("error", ""+e.getMessage());
				}
			}

			return;
		}
	}
    
    private void getExtra(){
    	extra = getIntent().getExtras();
 		extra_id = extra.getString("id");
 		extra_con[0] = extra.getString("id");
 		extra_con[1] = extra.getString("icon");
 		extra_con[2] = extra.getString("name");
 		extra_con[3] = extra.getString("con");
 		extra_con[4] = extra.getString("date");
 		extra_con[5] = extra.getString("reply_no");
 		extra_con[6] = extra.getString("smile_len");
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void finish(){
		super.finish();
		overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
	}


    public String writeComment(String uid, String fid, String txt) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("fid", fid));
        post.add(new BasicNameValuePair("txt", txt));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postFeedReply");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
