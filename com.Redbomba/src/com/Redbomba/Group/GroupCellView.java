package com.Redbomba.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Redbomba.R;
import com.Redbomba.R.id;
import com.Redbomba.R.layout;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.NotificationService;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.readystatesoftware.viewbadger.BadgeView;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;
	private Context con;
	
	private ImageView ivStatImg;
	private ImageView ivGroupIcon;
	private TextView tvGroupName;
	private TextView tvGameName;
	private TextView tvGroupInitial;
	private LinearLayout llGroupMem;
	public LinearLayout llbtnChatting;
	
	private String strGroupIcon = "";
	private String strGroupName = "";
	private String strGameName = "";
	private String strGroupInitial = "";
	
	Settings settings;
	BroadcastReceiver broadcastReceiver;
	static int newChat = 0;
	static BadgeView badge;
	
	public GroupCellView(Context context, JSONObject jo){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		settings = (Settings) context.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_grouplist, null);
		
		ivStatImg = (ImageView)feed.findViewById(R.id.ivStatImg);
		ivGroupIcon = (ImageView)feed.findViewById(R.id.ivGroupIcon);
		tvGroupName = (TextView)feed.findViewById(R.id.tvGroupName);
		tvGameName = (TextView)feed.findViewById(R.id.tvGameName);
		tvGroupInitial = (TextView)feed.findViewById(R.id.tvGroupInitial);
		llbtnChatting = (LinearLayout)feed.findViewById(R.id.llbtnChatting);
		
		tvGroupName.setTypeface(Functions.setFont(con));
		tvGameName.setTypeface(Functions.setFont(con));
		tvGroupInitial.setTypeface(Functions.setFont(con));
		
		try{
			strGroupIcon = jo.getString("icon");
			strGroupName = jo.getString("name");
			strGameName = jo.getString("game");
			strGroupInitial = jo.getString("nick");
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		int stat_no = (int)(Math.random()*641)+1;
		aq.id(ivStatImg).image("http://re01-xv2938.ktics.co.kr/stat_lol_"+stat_no+".jpg", true, true, 0, 0, null, AQuery.FADE_IN);
		aq.id(ivGroupIcon).image("http://redbomba.net/media/group_icon/"+strGroupIcon);
		
		tvGroupName.setText(strGroupName);
		tvGameName.setText(strGameName);
		tvGroupInitial.setText(strGroupInitial);
		
		setGroupMember(jo);
		setBroadcast();
		
	}
	
	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_4","LOAD");
				newChat++;
				if(badge==null) badge = new BadgeView(con, llbtnChatting);
				badge.setText(""+newChat);
				badge.show();
			}
		};

		con.registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_04));
	}
	
	public View getView(){
		return feed;
	}
	
	private void setGroupMember(JSONObject jo){
		llGroupMem = (LinearLayout)feed.findViewById(R.id.llGroupMem);
		
		try {
			JSONArray ja = jo.getJSONArray("memlist");
			for(int i=0;i<ja.length();i++){
				llGroupMem.addView(getGroupMember(ja.getJSONObject(i).getString("user_icon")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ImageView getGroupMember(String user_icon){
		ImageView memberIcon = new ImageView(con);
		memberIcon.setAdjustViewBounds(true);
		memberIcon.setLayoutParams(new LinearLayout.LayoutParams(115, 115));
		memberIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
		memberIcon.setPadding(10, 10, 0, 10);
		
		String urlIcon = "http://redbomba.net"+user_icon;
		
		ImageOptions options = new ImageOptions();
		options.round = 10000;
		
		aq.id(memberIcon).image(urlIcon,options);
		
		return memberIcon;
	}

}
