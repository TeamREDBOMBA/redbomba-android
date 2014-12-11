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
import org.json.JSONObject;

import net.Redbomba.R;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedCellView extends View {
	private AQuery aq = new AQuery(this);

	private View feed;
	private Context con;

	private ImageView iv_user_icon;
	private TextView tv_username;
	private TextView tv_content;
	private TextView tv_update;
	private TextView tv_replynum;
	private ImageView ivThumb;
	private TextView tvThumb;

	private String strId;
	private String strIcon;
	private String strUsername;
	private String strContent;
	private String strUpdate;
	private String strReplynum;
	private String strThumb;

	Settings settings;
	private JSONObject jo;
	private String[] str;

	public FeedCellView(Context context, String[] s, int i){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		str = s;
		getLayout(context, 0);
	}

	public FeedCellView(Context context, JSONObject j, int i){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		jo = j;
		getLayout(context, 1);
	}
	
	private void getLayout(Context context, int action){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_feed, null);
		settings = (Settings) context.getApplicationContext();

		iv_user_icon = (ImageView)feed.findViewById(R.id.iv_user_icon);
		tv_username = (TextView)feed.findViewById(R.id.tv_username);
		tv_content = (TextView)feed.findViewById(R.id.tv_content);
		tv_update = (TextView)feed.findViewById(R.id.tv_update);
		tv_replynum = (TextView)feed.findViewById(R.id.tv_replynum);
		ivThumb = (ImageView)feed.findViewById(R.id.ivThumb);
		tvThumb = (TextView)feed.findViewById(R.id.tvThumb);

		tv_username.setTypeface(Functions.setFont(con));
		tv_content.setTypeface(Functions.setFont(con));
		tv_update.setTypeface(Functions.setFont(con));
		tv_replynum.setTypeface(Functions.setFont(con));
		tvThumb.setTypeface(Functions.setFont(con));
		
		if(action == 0) byArray();
		else byJSON();
		
		if(!strIcon.equals("")){
			ImageOptions options = new ImageOptions();
			options.round = 10000;
			aq.id(iv_user_icon).image("http://redbomba.net"+strIcon,options);
		}

		tv_username.setText(strUsername);
		tv_content.setText(strContent);
		tv_update.setText(strUpdate);
		tv_replynum.setText(strReplynum+"개의 댓글");
		tvThumb.setText("좋아요 "+strThumb+"개");
		
		ivThumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				postFeedSmile(""+settings.user_id, strId);
			}
		});
	}

	private void byArray(){
		try{
			strId = str[0];
			strIcon = str[1];
			strUsername = str[2];
			strContent = str[3];
			strUpdate = str[4];
			strReplynum = str[5];
			strThumb = str[6];
		}catch (Exception e) {
			// TODO: handle exception
			Log.i("error", ""+e.getMessage());
		}
	}
	
	private void byJSON(){
		try{
			strIcon = jo.getString("icon");
			strUsername = jo.getString("name");
			strContent = jo.getString("con");
			strUpdate = jo.getString("date");
			strReplynum = jo.getString("reply_no");
			strThumb = jo.getString("smile_len");
		}catch (Exception e) {
			// TODO: handle exception
			Log.i("error", ""+e.getMessage());
		}
	}

	public View getView(){
		return feed;
	}

	public String postFeedSmile(String uid, String fid) {
		ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
		post.add(new BasicNameValuePair("uid", uid));
		post.add(new BasicNameValuePair("fid", fid));

		// 연결 HttpClient 객체 생성
		HttpClient client = new DefaultHttpClient();

		// 객체 연결 설정 부분, 연결 최대시간 등등
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);

		// Post객체 생성
		HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postFeedSmile");
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
