package com.Redbomba.Main;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotiCellView extends View {
	private AQuery aq;
	
	private View feed;
	private Context con;

	private ImageView imgIcon;
	private TextView tvCon;
	private TextView tvDate;

	public NotiCellView(Context context, String strCon, String strDate, String strImg) {
		super(context);
		this.con = context;
		aq = new AQuery(con);
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_notification, null);
		
		String urlIcon = "http://redbomba.net"+strImg;
		
		imgIcon = (ImageView)feed.findViewById(R.id.imgIcon);
		tvCon = (TextView)feed.findViewById(R.id.tvCon);
		tvDate = (TextView)feed.findViewById(R.id.tvDate);
		
		tvCon.setTypeface(Settings.setFont(context));
		tvDate.setTypeface(Settings.setFont(context));
		
		tvCon.setText(Settings.stripHTML(strCon));
		tvDate.setText(strDate);
		aq.id(imgIcon).image(urlIcon);
	}
	
	public View getView(){
		return feed;
	}
}