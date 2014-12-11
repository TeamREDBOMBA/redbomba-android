package net.Redbomba.Landing;

import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

import net.Redbomba.R;

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
	
	Settings settings;

	public LandingLoginView(Context context){
		super(context);
		settings = (Settings) context.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_landing_login, null);

		btnBack_v3 = (Button)feed.findViewById(R.id.btnBack_v3);
		etEmail = (EditText)feed.findViewById(R.id.etEmail);
		etPW = (EditText)feed.findViewById(R.id.etPW);
		btnLogin = (Button)feed.findViewById(R.id.btnLogin);
		ll_ll_join = (LinearLayout)feed.findViewById(R.id.ll_ll_join);

		btnBack_v3.setTypeface(Functions.setFont(context));
		etEmail.setTypeface(Functions.setFont(context));
		etPW.setTypeface(Functions.setFont(context));
		btnLogin.setTypeface(Functions.setFont(context));

		feed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v3.setOnClickListener((OnClickListener)context);
		btnLogin.setOnClickListener((OnClickListener)context);
		ll_ll_join.setOnClickListener((OnClickListener)context);
	}
	
	public View getView(){
		init();
		return feed;
	}
	
	public void init(){
		etEmail.setText("");
		etPW.setText("");
	}
	
	public String getID(){
		return etEmail.getText().toString();
	}
	
	public String getPW(){
		return etPW.getText().toString();
	}
}
