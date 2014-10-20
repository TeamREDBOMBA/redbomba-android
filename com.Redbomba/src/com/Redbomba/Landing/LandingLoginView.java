package com.Redbomba.Landing;

import com.Redbomba.R;
import com.Redbomba.Settings.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LandingLoginView extends View implements OnFocusChangeListener{
	
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
		etEmail.setOnFocusChangeListener((OnFocusChangeListener)this);
		etPW.setOnFocusChangeListener((OnFocusChangeListener)this);
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
		}
	}
}
