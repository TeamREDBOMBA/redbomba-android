package com.Redbomba;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity implements OnClickListener, OnFocusChangeListener{

	private EditText etEmail;
	private EditText etPW;
	private Button btnLogin;
	private Button btnJoin;

	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	private Handler mHandler;
	private boolean mFlag = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		prefs_system = getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();

		if(prefs_system.getInt("uid", 0) != 0){
			Settings.user_id = prefs_system.getInt("uid", 0);
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		}

		etEmail = (EditText)findViewById(R.id.etEmail);
		etPW = (EditText)findViewById(R.id.etPW);
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnJoin = (Button)findViewById(R.id.btnJoin);
		
		etEmail.setTypeface(Settings.setFont(this));
		etPW.setTypeface(Settings.setFont(this));
		btnLogin.setTypeface(Settings.setFont(this));
		btnJoin.setTypeface(Settings.setFont(this));

		etEmail.setOnFocusChangeListener((OnFocusChangeListener)this);
		etPW.setOnFocusChangeListener((OnFocusChangeListener)this);
		btnLogin.setOnClickListener((OnClickListener) this);
		btnJoin.setOnClickListener((OnClickListener) this);
		
		setVideoBG();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0) {
					mFlag = false;
				}
			}
		};

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);
		switch(v.getId()){
		case R.id.btnLogin :
			new LoginTask().execute(null, null, null);
			break;
		case R.id.btnJoin :
			break;
		}
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

	class LoginTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... Void) {
			String email = etEmail.getText().toString();
			String password = etPW.getText().toString();
			int uid=0;
			try {
				uid = Settings.GET("mode=1&email="+email+"&password="+password).getJSONObject(0).getInt("uid");
				Log.i("Login Result",uid+"");
				editor_system.putInt("uid", uid);
				editor_system.commit();
				Settings.user_id = uid;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				LoginActivity.this.finish();
			}else{
				new AlertDialog.Builder(LoginActivity.this).setTitle("로그인 실패")
				.setMessage("아이디와 비밀번호를 다시 확인 후 시도해주세요.")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
			}
			return;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!mFlag) {
				Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setVideoBG(){
		TextureVideoView cropTextureView = (TextureVideoView) findViewById(R.id.cropTextureView);

		cropTextureView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);

		try {
			AssetFileDescriptor afd = getAssets().openFd("bg.mp4");
			cropTextureView.setDataSource(afd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cropTextureView.play();
		cropTextureView.setLooping(true);
	}
}
