package com.Redbomba;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.util.Log;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupCellView extends View {
	
	private View feed;
	private Context con;
	
	private ImageView ivGroupIcon;
	private TextView tvGroupName;
	private TextView tvGameName;
	private TextView tvGroupInitial;
	private LinearLayout llGroupMem;
	LinearLayout llbtnChatting;
	
	private URL urlGroupIcon = null;
	private String strGroupIcon = "";
	private String strGroupName = "";
	private String strGameName = "";
	private String strGroupInitial = "";
	
	public GroupCellView(Context context, JSONObject jo){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_group, null);
		
		setGroupInfo(jo);
		setGroupMember(jo);
		
	}
	
	public View getView(){
		return feed;
	}
	
	private void setGroupInfo(JSONObject jo){
		ivGroupIcon = (ImageView)feed.findViewById(R.id.ivGroupIcon);
		tvGroupName = (TextView)feed.findViewById(R.id.tvGroupName);
		tvGameName = (TextView)feed.findViewById(R.id.tvGameName);
		tvGroupInitial = (TextView)feed.findViewById(R.id.tvGroupInitial);
		llbtnChatting = (LinearLayout)feed.findViewById(R.id.llbtnChatting);
		
		try{
			strGroupIcon = jo.getString("icon");
			strGroupName = jo.getString("name");
			strGameName = jo.getString("game");
			strGroupInitial = jo.getString("nick");
			urlGroupIcon = new URL("http://redbomba.net/media/group_icon/"+strGroupIcon);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!strGroupIcon.equals("")){
			ivGroupIcon.setImageBitmap(Settings.getRemoteImage(urlGroupIcon));
		}
		tvGroupName.setText(strGroupName);
		tvGameName.setText(strGameName);
		tvGroupInitial.setText(strGroupInitial);
	}
	
	private void setGroupMember(JSONObject jo){
		llGroupMem = (LinearLayout)feed.findViewById(R.id.llGroupMem);
		
		try {
			JSONArray ja = jo.getJSONArray("memlist");
			for(int i=0;i<ja.length();i++){
				llGroupMem.addView(getGroupMember(ja.getJSONObject(i).getInt("user_icon")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ImageView getGroupMember(int user_icon){
		ImageView memberIcon = new ImageView(con);
		memberIcon.setAdjustViewBounds(true);
		memberIcon.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		memberIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
		memberIcon.setPadding(10, 10, 0, 10);
		
		URL urlIcon = null;
		try {
			urlIcon = new URL("http://redbomba.net/static/img/icon/usericon_"+user_icon+".jpg");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bm = Settings.getRemoteImage(urlIcon);
		RoundImage roundedImage = new RoundImage(bm);
		memberIcon.setImageDrawable(roundedImage);
		
		return memberIcon;
	}

}
