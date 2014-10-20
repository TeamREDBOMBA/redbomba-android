package com.Redbomba.Main;

import org.json.JSONObject;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivateCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;
	private Context con;
	
	private ImageView ivIcon;
	private TextView tvDate;
	private TextView tvTitle;
	private TextView tvCon;
	
	private String strIcon = "";
	private String strDate = "";
	private String strTitle = "";
	private String strCon = "";
	
	public PrivateCellView(Context context, JSONObject jo){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_privatelist, null);
		
		ivIcon = (ImageView)feed.findViewById(R.id.ivIcon);
		tvDate = (TextView)feed.findViewById(R.id.tvDate);
		tvTitle = (TextView)feed.findViewById(R.id.tvTitle);
		tvCon = (TextView)feed.findViewById(R.id.tvCon);
		
		tvDate.setTypeface(Settings.setFont(con));
		tvTitle.setTypeface(Settings.setFont(con));
		tvCon.setTypeface(Settings.setFont(con));
		
		try{
			strIcon = jo.getString("icon");
			strDate = jo.getString("date_updated");
			strTitle = jo.getString("name");
			strCon = jo.getString("con");
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		aq.id(ivIcon).image("http://redbomba.net"+strIcon, true, true, 0, 0, null, AQuery.FADE_IN);
		
		tvDate.setText(strDate);
		tvTitle.setText(strTitle);
		tvCon.setText(strCon);	
	}
	
	public View getView(){
		return feed;
	}

}
