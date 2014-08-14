package com.Redbomba;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberCellView extends View {
	private AQuery aq;
	
	private View feed;
	private Context con;

	private ImageView ivMemberIcon;
	private ImageView ivMemberOnOff;
	private TextView tvMemberName;
	
	private String user_icon = "";
	private String uid = "";
	private String username = "";

	public MemberCellView(Context context, JSONObject jo) {
		super(context);
		this.con = context;
		aq = new AQuery(con);
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_groupmember, null);
		
		ivMemberIcon = (ImageView)feed.findViewById(R.id.ivMemberIcon);
		ivMemberOnOff = (ImageView)feed.findViewById(R.id.ivMemberOnOff);
		tvMemberName = (TextView)feed.findViewById(R.id.tvMemberName);
		
		tvMemberName.setTypeface(Settings.setFont(context));
		
		try{
			user_icon = jo.getString("user_icon");
			uid = jo.getString("uid");
			username = jo.getString("username");
			
			if(!user_icon.equals("")){
				ImageOptions options = new ImageOptions();
				options.round = 100;
				aq.id(ivMemberIcon).image("http://redbomba.net/static/img/icon/usericon_"+user_icon+".jpg",options);
			}
			tvMemberName.setText(username);
		}catch (Exception e){ Log.i("error",""+e.getMessage()); }
		
	}
	
	public void setOnline(){
		ivMemberOnOff.setImageResource(R.drawable.sub_user_on);
	}
	
	public void setOffline(){
		ivMemberOnOff.setImageResource(R.drawable.sub_user_off);
	}
	
	public String getUid(){
		return uid;
	}
	
	public String getName(){
		return username;
	}
	
	public View getView(){
		return feed;
	}
}