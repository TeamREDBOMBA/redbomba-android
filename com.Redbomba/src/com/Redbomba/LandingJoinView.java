package com.Redbomba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class LandingJoinView extends View{
	
	private View feed;
	public Button btnBack_v2;

	public LandingJoinView(Context context){
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_landing_join, null);
		
		btnBack_v2 = (Button)feed.findViewById(R.id.btnBack_v2);

		btnBack_v2.setTypeface(Settings.setFont(context));

		feed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
	}
	
	public View getView(){
		return feed;
	}
}
