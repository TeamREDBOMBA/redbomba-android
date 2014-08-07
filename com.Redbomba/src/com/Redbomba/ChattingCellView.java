package com.Redbomba;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ChattingCellView extends View {
	
	private View feed;
	private Context con;

	private TextView tvChatName;
	private TextView tvChatCon;

	public ChattingCellView(Context context, JSONObject jo) {
		super(context);
		
		try {
			setChattingText(context, jo.getString("username"), jo.getString("con"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ChattingCellView(Context context, String name, String con) {
		super(context);
		setChattingText(context, name, con);
	}
	
	private void setChattingText(Context context, String name, String con){
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_chatting, null);

		tvChatName = (TextView)feed.findViewById(R.id.tvChatName);
		tvChatCon = (TextView)feed.findViewById(R.id.tvChatCon);
		
		try {
			if(name.equals(Settings.user_info.getString("username"))){
				tvChatName.setTypeface(null, Typeface.BOLD);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tvChatName.setText(name);
		tvChatCon.setText(con);
	}
	
	public View getView(){
		return feed;
	}
}