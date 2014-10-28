package com.Redbomba.Landing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.Redbomba.R;
import com.Redbomba.Landing.LandingActivity.LoginTask;
import com.Redbomba.Main.MainActivity;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class LandingJoinView extends View implements OnClickListener {

	private Context con;
	private View feed;
	public Button btnBack_v2;
	private HorizontalScrollView hsv_join;

	private LinearLayout ll_join_v1;
	private EditText etEmail;
	private EditText etPW;
	private EditText etPW_;
	private Button btnNext;
	private int bg_etEmail = 0;
	private int bg_etPW = 0;
	private int bg_etPW_ = 0;

	private LinearLayout ll_join_v2;
	private EditText etNick;
	private Button btnJoin;
	public int bg_etNick = 0;
	
	public LinearLayout ll_v2_loading;
	public LinearLayout ll_lj_loin;

	Settings settings;
	int display_width;

	public LandingJoinView(Context context, int width){
		super(context);
		settings = (Settings) context.getApplicationContext();
		
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
		
		display_width = (int) (width-px);
		con = context;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_landing_join, null);
		btnBack_v2 = (Button)feed.findViewById(R.id.btnBack_v2);
		ll_lj_loin = (LinearLayout)feed.findViewById(R.id.ll_lj_loin);
		hsv_join = (HorizontalScrollView)feed.findViewById(R.id.hsv_join);
		ll_v2_loading = (LinearLayout)feed.findViewById(R.id.ll_v2_loading);

		btnBack_v2.setTypeface(Functions.setFont(con));

		feed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v2.setOnClickListener((OnClickListener)con);
		ll_lj_loin.setOnClickListener((OnClickListener)con);

		setV1();
		setV2();
	}

	private void setV1(){
		ll_join_v1 = (LinearLayout)feed.findViewById(R.id.ll_join_v1);
		etEmail = (EditText)feed.findViewById(R.id.etEmail);
		etPW = (EditText)feed.findViewById(R.id.etPW);
		etPW_ = (EditText)feed.findViewById(R.id.etPW_);
		btnNext = (Button)feed.findViewById(R.id.btnNext);

		ll_join_v1.setLayoutParams(new LinearLayout.LayoutParams(display_width,ViewGroup.LayoutParams.WRAP_CONTENT));

		btnNext.setOnClickListener((OnClickListener) this);
		etEmail.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				new ChkEmailTask().execute(etEmail.getText().toString(), null, null);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		etPW.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(etPW.getText().toString().equals("")){
					bg_etPW = R.drawable.et_landing_border_radius;
				}else if(etPW.getText().toString().length() < 8){
					bg_etPW = R.drawable.et_landing_border_radius_error;
				}else{
					bg_etPW = R.drawable.et_landing_border_radius_success;
				}
				etPW.setBackgroundResource(bg_etPW);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		etPW_.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if(etPW_.getText().toString().equals("")){
					bg_etPW_ = R.drawable.et_landing_border_radius;
				}else if(!etPW_.getText().toString().equals(etPW.getText().toString())){
					bg_etPW_ = R.drawable.et_landing_border_radius_error;
				}else{
					bg_etPW_ = R.drawable.et_landing_border_radius_success;
				}
				etPW_.setBackgroundResource(bg_etPW_);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void setV2(){
		ll_join_v2 = (LinearLayout)feed.findViewById(R.id.ll_join_v2);
		etNick = (EditText)feed.findViewById(R.id.etNick);
		btnJoin = (Button)feed.findViewById(R.id.btnJoin);

		ll_join_v2.setLayoutParams(new LinearLayout.LayoutParams(display_width,ViewGroup.LayoutParams.WRAP_CONTENT));

		btnJoin.setOnClickListener((OnClickListener) con);
		
		etNick.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				new ChkNickTask().execute(etNick.getText().toString(), null, null);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});
	}

	public View getView(){
		init();
		return feed;
	}

	public void init(){
		etEmail.setText("");
		etPW.setText("");
		etPW_.setText("");
		etNick.setText("");
		hsv_join.smoothScrollTo(0, 0);
	}
	
	public String getID(){
		return etEmail.getText().toString();
	}
	
	public String getPW(){
		return etPW.getText().toString();
	}
	
	public String getNICK(){
		return etNick.getText().toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)con.getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);

		switch(v.getId()){
		case R.id.btnNext :
			if(bg_etEmail == R.drawable.et_landing_border_radius_success
			&& bg_etPW == R.drawable.et_landing_border_radius_success
			&& bg_etPW_ == R.drawable.et_landing_border_radius_success)
				hsv_join.smoothScrollTo(display_width, 0);
			break;
		}
	}
	
	class ChkNickTask extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... nick) {
			if(etNick.getText().toString().equals("")){
				bg_etNick = R.drawable.et_landing_border_radius;
			}else{
				String ret = Functions.downloadHtml("mode=chkNick&nick="+nick[0]);
				Log.i("ret!!!!!!", ""+ret);
				if (ret.equals("")) return true;
				else return false;
			}
			Log.i("TEXT!!!!!!", ""+etNick.getText().toString());
			return false;
		}

		protected void onPostExecute(Boolean result) {
			if(etNick.getText().toString().equals("")){
				bg_etNick = R.drawable.et_landing_border_radius;
			}else if(!result){
				bg_etNick = R.drawable.et_landing_border_radius_error;
			}else{
				bg_etNick = R.drawable.et_landing_border_radius_success;
			}
			etNick.setBackgroundResource(bg_etNick);
			return;
		}
	}

	class ChkEmailTask extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... email) {
			if(etEmail.getText().toString().equals("")){
				bg_etEmail = R.drawable.et_landing_border_radius;
			}else if(!isEmailValid(etEmail.getText().toString())){
				bg_etEmail = R.drawable.et_landing_border_radius_error;
			}else{
				String ret = Functions.downloadHtml("mode=chkEmail&email="+email[0]);
				Log.i("ret!!!!!!", ""+ret);
				if (ret.equals("")) return true;
				else return false;
			}
			Log.i("TEXT!!!!!!", ""+etEmail.getText().toString());
			return false;
		}

		protected void onPostExecute(Boolean result) {
			if(etEmail.getText().toString().equals("")){
				bg_etEmail = R.drawable.et_landing_border_radius;
			}else if(!result){
				bg_etEmail = R.drawable.et_landing_border_radius_error;
			}else{
				bg_etEmail = R.drawable.et_landing_border_radius_success;
			}
			etEmail.setBackgroundResource(bg_etEmail);
			return;
		}
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

}
