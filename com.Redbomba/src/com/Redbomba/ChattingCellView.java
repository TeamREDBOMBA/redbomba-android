package com.Redbomba;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChattingCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;
	private Context con;

	private ImageView ivChatIcon;
	TextView tvChatName;
	TextView tvChatCon;

	public ChattingCellView(Context context, JSONObject jo) {
		super(context);
		
		try {
			setChattingText(context, jo.getString("username"), jo.getString("con"), jo.getString("usericon"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ChattingCellView(Context context, String name, String con, String icon) {
		super(context);
		setChattingText(context, name, con, icon);
	}
	
	private void setChattingText(Context context, String name, String con, String icon){
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_chatting, null);

		ivChatIcon = (ImageView)feed.findViewById(R.id.ivChatIcon);
		tvChatName = (TextView)feed.findViewById(R.id.tvChatName);
		tvChatCon = (TextView)feed.findViewById(R.id.tvChatCon);
		
		tvChatName.setTypeface(Settings.setFont(context));
		tvChatCon.setTypeface(Settings.setFont(context));
		
		ImageOptions options = new ImageOptions();
		options.round = 20;
		
		aq.id(tvChatName).text(name);
		aq.id(tvChatCon).text(con);
		aq.id(ivChatIcon).image("http://redbomba.net/static/img/icon/usericon_"+icon+".jpg",options);
	}
	
	public View getView(){
		return feed;
	}
}