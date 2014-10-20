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

public class GlobalCellView extends View {
	private AQuery aq = new AQuery(this);
	
	private View feed;
	private Context con;
	
	private ImageView ivSrc;
	private TextView tvTitle;
	private TextView tvTag;
	private TextView tvCommentNo;
	private TextView tvDate;
	
	private String strSrc = "";
	private String strTitle = "";
	private String strCommentNo = "";
	private String strDate = "";
	
	public GlobalCellView(Context context, JSONObject jo){
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_globallist, null);
		
		ivSrc = (ImageView)feed.findViewById(R.id.ivSrc);
		tvTitle = (TextView)feed.findViewById(R.id.tvTitle);
		tvTag = (TextView)feed.findViewById(R.id.tvTag);
		tvCommentNo = (TextView)feed.findViewById(R.id.tvCommentNo);
		tvDate = (TextView)feed.findViewById(R.id.tvDate);
		
		tvTitle.setTypeface(Settings.setFont(con));
		tvTag.setTypeface(Settings.setFont(con));
		tvCommentNo.setTypeface(Settings.setFont(con));
		tvDate.setTypeface(Settings.setFont(con));
		
		try{
			strSrc = jo.getString("img");
			strTitle = jo.getString("title");
			strCommentNo = jo.getString("comment_no");
			strDate = jo.getString("date_updated");
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		aq.id(ivSrc).image("http://redbomba.net"+strSrc, true, true, 0, 0, null, AQuery.FADE_IN);
		
		tvTitle.setText(strTitle);
		tvCommentNo.setText(strCommentNo);
		tvDate.setText(strDate);	
	}
	
	public View getView(){
		return feed;
	}

}
