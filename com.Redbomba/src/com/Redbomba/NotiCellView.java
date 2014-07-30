package com.Redbomba;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotiCellView extends View {
	
	private View feed;
	private Context con;

	private ImageView imgIcon;
	private TextView tvCon;
	private TextView tvDate;

	public NotiCellView(Context context, String strCon, String strDate, String strImg) {
		super(context);
		this.con = context;
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_notification, null);
		
		URL urlIcon = null;
		try {
			urlIcon = new URL("http://redbomba.net"+strImg);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imgIcon = (ImageView)feed.findViewById(R.id.imgIcon);
		tvCon = (TextView)feed.findViewById(R.id.tvCon);
		tvDate = (TextView)feed.findViewById(R.id.tvDate);
		
		tvCon.setText(Settings.stripHTML(strCon));
		tvDate.setText(strDate);
		imgIcon.setImageBitmap(Settings.getRemoteImage(urlIcon));
	}
	
	public View getView(){
		return feed;
	}
}