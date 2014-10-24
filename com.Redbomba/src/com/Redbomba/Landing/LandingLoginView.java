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

public class LandingLoginView extends View {
	
	private View feed;
	private EditText etEmail;
	private EditText etPW;
	public Button btnBack_v3;
	public Button btnLogin;
	public LinearLayout ll_ll_join;

	public LandingLoginView(Context context){
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_landing_login, null);

		btnBack_v3 = (Button)feed.findViewById(R.id.btnBack_v3);
		etEmail = (EditText)feed.findViewById(R.id.etEmail);
		etPW = (EditText)feed.findViewById(R.id.etPW);
		btnLogin = (Button)feed.findViewById(R.id.btnLogin);
		ll_ll_join = (LinearLayout)feed.findViewById(R.id.ll_ll_join);

		btnBack_v3.setTypeface(Settings.setFont(context));
		etEmail.setTypeface(Settings.setFont(context));
		etPW.setTypeface(Settings.setFont(context));
		btnLogin.setTypeface(Settings.setFont(context));

		feed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v3.setOnClickListener((OnClickListener)context);
		btnLogin.setOnClickListener((OnClickListener)context);
		ll_ll_join.setOnClickListener((OnClickListener)context);
	}
	
	public View getView(){
		return feed;
	}
	
	public String getID(){
		return etEmail.getText().toString();
	}
	
	public String getPW(){
		return etPW.getText().toString();
	}
}
