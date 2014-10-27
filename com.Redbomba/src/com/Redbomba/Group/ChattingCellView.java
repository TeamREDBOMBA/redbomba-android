package com.Redbomba.Group;

import org.json.JSONException;
import org.json.JSONObject;

import com.Redbomba.R;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChattingCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;

	private ImageView ivChatIcon;
	TextView tvChatName;
	TextView tvChatCon;
	
	Settings settings;

	public ChattingCellView(Context context, JSONObject jo, int dir) {
		super(context);
		
		settings = (Settings) context.getApplicationContext();
		
		try {
			setChattingText(context, jo.getString("username"), jo.getString("con"), jo.getString("usericon"), dir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ChattingCellView(Context context, String name, String con, String icon, int dir) {
		super(context);
		setChattingText(context, name, con, icon, dir);
	}
	
	private void setChattingText(Context context, String name, String con, String icon, int dir){
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(dir == Gravity.RIGHT) feed = inflater.inflate(R.layout.cell_chatting_right, null);
		else feed = inflater.inflate(R.layout.cell_chatting_left, null);

		ivChatIcon = (ImageView)feed.findViewById(R.id.ivChatIcon);
		tvChatName = (TextView)feed.findViewById(R.id.tvChatName);
		tvChatCon = (TextView)feed.findViewById(R.id.tvChatCon);
		
		tvChatName.setTypeface(Functions.setFont(context));
		tvChatCon.setTypeface(Functions.setFont(context));
		
		ImageOptions options = new ImageOptions();
		options.round = 10000;
		
		aq.id(tvChatName).text(name);
		aq.id(tvChatCon).text(con);
		aq.id(ivChatIcon).image("http://redbomba.net"+icon,options);
	}
	
	public View getView(){
		return feed;
	}
}