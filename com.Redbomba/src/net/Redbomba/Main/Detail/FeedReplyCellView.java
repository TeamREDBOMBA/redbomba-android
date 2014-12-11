package net.Redbomba.Main.Detail;

import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

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

public class FeedReplyCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;
	private Context con;
	
	private ImageView iv_user_icon;
	private TextView tv_username;
	private TextView tv_content;
	private TextView tv_update;
	
	private String strIcon;
	private String strUsername;
	private String strContent;
	private String strUpdate;
	
	Settings settings;
	
	public FeedReplyCellView(Context context, JSONObject jo, int i){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_feed_reply, null);
		settings = (Settings) context.getApplicationContext();
		
		iv_user_icon = (ImageView)feed.findViewById(R.id.iv_user_icon);
		tv_username = (TextView)feed.findViewById(R.id.tv_username);
		tv_content = (TextView)feed.findViewById(R.id.tv_content);
		tv_update = (TextView)feed.findViewById(R.id.tv_update);
		
		tv_username.setTypeface(Functions.setFont(con));
		tv_content.setTypeface(Functions.setFont(con));
		tv_update.setTypeface(Functions.setFont(con));
		
		try{
			strIcon = jo.getString("icon");
			strUsername = jo.getString("name");
			strContent = jo.getString("con");
			strUpdate = jo.getString("date");
			
			if(!strIcon.equals("")){
				ImageOptions options = new ImageOptions();
				options.round = 10000;
				aq.id(iv_user_icon).image("http://redbomba.net"+strIcon,options);
			}
			
			tv_username.setText(strUsername);
			tv_content.setText(strContent);
			tv_update.setText(strUpdate);
		}catch (Exception e) {
			// TODO: handle exception
			Log.i("error", ""+e.getMessage());
		}
		
	}
	
	public View getView(){
		return feed;
	}

}
