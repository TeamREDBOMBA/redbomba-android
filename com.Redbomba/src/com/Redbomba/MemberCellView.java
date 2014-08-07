package com.Redbomba;

import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberCellView extends View {
	
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
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_groupmember, null);
		
		ivMemberIcon = (ImageView)feed.findViewById(R.id.ivMemberIcon);
		ivMemberOnOff = (ImageView)feed.findViewById(R.id.ivMemberOnOff);
		tvMemberName = (TextView)feed.findViewById(R.id.tvMemberName);
		
		try{
			user_icon = jo.getString("user_icon");
			uid = jo.getString("uid");
			username = jo.getString("username");
			
			if(!user_icon.equals("")){
				Bitmap bm = Settings.getRemoteImage(new URL("http://redbomba.net/static/img/icon/usericon_"+user_icon+".jpg"));
				RoundImage roundedImage = new RoundImage(bm);
				ivMemberIcon.setImageDrawable(roundedImage);
			}
			tvMemberName.setText(username);
		}catch (Exception e){ }
		
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