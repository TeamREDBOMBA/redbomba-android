package com.Redbomba.Landing;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LandingJoinView extends View {
	
	private View feed;
	public Button btnBack_v2;
	public EditText etEmail;
	public EditText etPW;
	public EditText etPW_;
	public LinearLayout ll_lj_loin;

	public LandingJoinView(Context context){
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_landing_join, null);
		
		btnBack_v2 = (Button)feed.findViewById(R.id.btnBack_v2);
		etEmail = (EditText)feed.findViewById(R.id.etEmail);
		etPW = (EditText)feed.findViewById(R.id.etPW);
		etPW_ = (EditText)feed.findViewById(R.id.etPW_);
		ll_lj_loin = (LinearLayout)feed.findViewById(R.id.ll_lj_loin);

		btnBack_v2.setTypeface(Settings.setFont(context));

		feed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v2.setOnClickListener((OnClickListener)context);
		ll_lj_loin.setOnClickListener((OnClickListener)context);
	}
	
	public View getView(){
		return feed;
	}
}
