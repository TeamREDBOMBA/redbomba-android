package net.Redbomba.Main.Detail;

import java.io.IOException;
import java.util.ArrayList;

import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

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
import org.json.JSONException;

import net.Redbomba.R;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class WriteFeedActivity extends Activity {
	private AQuery aq = new AQuery(this);
	
	private ImageView ivIcon;
	private TextView tvName;
	private EditText etCon;
	
	private Bundle extra;
	private String uid="";
	
	public static final String BROADCAST_ACTION_SET_GLOBAL_CARD = "com.Redbomba.setGlobalCard";
	private Intent intent_set_global_card;
	
	Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_feed);
		
		settings = (Settings) getApplicationContext();
		
		intent_set_global_card = new Intent(BROADCAST_ACTION_SET_GLOBAL_CARD);
		
		extra = getIntent().getExtras();
		
		ivIcon = (ImageView)findViewById(R.id.ivIcon);
		tvName = (TextView)findViewById(R.id.tvName);
		etCon = (EditText)findViewById(R.id.etCon);
		
		tvName.setTypeface(Functions.setFont(this));
		etCon.setTypeface(Functions.setFont(this));
		
		ImageOptions options = new ImageOptions();
		options.round = 10000;
		
		try {
			tvName.setText(settings.user_info.getString("username"));
			aq.id(ivIcon).image("http://redbomba.net"+settings.user_info.getString("user_icon"),options);
			uid = settings.user_info.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.write_feed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
			return true;
		case R.id.menu_write_feed:
			if(!etCon.getText().toString().equals("")){
				writeFeed(uid,extra.getString("id"),extra.getString("type"),etCon.getText().toString());
				sendBroadcast(intent_set_global_card);
				this.finish();
			}
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
	
	public String writeFeed(String uid, String uto, String utotype, String txt) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("uto", uto));
        post.add(new BasicNameValuePair("utotype", utotype));
        post.add(new BasicNameValuePair("feedtype", "1"));
        post.add(new BasicNameValuePair("txt", txt));
        post.add(new BasicNameValuePair("tag", null));
        post.add(new BasicNameValuePair("img", null));
        post.add(new BasicNameValuePair("vid", null));
        post.add(new BasicNameValuePair("log", null));
        post.add(new BasicNameValuePair("hyp", null));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postLeagueFeed");

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
