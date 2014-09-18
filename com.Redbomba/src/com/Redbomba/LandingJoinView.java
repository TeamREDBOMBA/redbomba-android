package com.Redbomba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LandingJoinView extends View implements OnFocusChangeListener{
	
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
		etEmail.setOnFocusChangeListener((OnFocusChangeListener)this);
		etPW.setOnFocusChangeListener((OnFocusChangeListener)this);
		etPW_.setOnFocusChangeListener((OnFocusChangeListener)this);
		ll_lj_loin.setOnClickListener((OnClickListener)context);
	}
	
	public View getView(){
		return feed;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.etEmail :
			if(hasFocus) { 
				if(etEmail.getText().toString().equals("이메일")) etEmail.setText("");
			} else{
				if(etEmail.getText().toString().equals("")) etEmail.setText("이메일");
			}
			break;
		case R.id.etPW :
			if(hasFocus) { 
				if(etPW.getText().toString().equals("********")) etPW.setText("");
			}else{
				if(etPW.getText().toString().equals("")) etPW.setText("********");
			}
			break;
		case R.id.etPW_ :
			if(hasFocus) { 
				if(etPW_.getText().toString().equals("********")) etPW_.setText("");
			}else{
				if(etPW_.getText().toString().equals("")) etPW_.setText("********");
			}
			break;
		}
	}
}
