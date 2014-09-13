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
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity implements OnClickListener, OnFocusChangeListener{

	private HorizontalScrollView hscv;

	private LinearLayout ll_hscv_v1;
	private Button btnJoin;
	private LinearLayout ll_v1_login;

	private LinearLayout ll_hscv_v2;
	private Button btnBack_v2;

	private LinearLayout ll_hscv_v3;
	private Button btnBack_v3;

	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	private Handler mHandler;
	private boolean mFlag = false;
	private Display display;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		display = getWindowManager().getDefaultDisplay(); 

		hscv = (HorizontalScrollView)findViewById(R.id.hscv);
		hscv.setOnTouchListener( new OnTouchListener(){ 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		prefs_system = getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();

		if(prefs_system.getInt("uid", 0) != 0){
			Settings.user_id = prefs_system.getInt("uid", 0);
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		}

		setView1();
		setView2();
		setView3();

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

	private void setView1(){
		ll_hscv_v1 = (LinearLayout)findViewById(R.id.ll_hscv_v1);
		btnJoin = (Button)findViewById(R.id.btnJoin);
		ll_v1_login = (LinearLayout)findViewById(R.id.ll_v1_login);

		ll_hscv_v1.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),ViewGroup.LayoutParams.FILL_PARENT));
		btnJoin.setOnClickListener(this);
		ll_v1_login.setOnClickListener(this);
	}

	private void setView2(){
		ll_hscv_v2 = (LinearLayout)findViewById(R.id.ll_hscv_v2);
		btnBack_v2 = (Button)findViewById(R.id.btnBack_v2);

		ll_hscv_v2.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v2.setOnClickListener(this);
	}

	private void setView3(){
		ll_hscv_v3 = (LinearLayout)findViewById(R.id.ll_hscv_v3);
		btnBack_v3 = (Button)findViewById(R.id.btnBack_v3);

		ll_hscv_v3.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),ViewGroup.LayoutParams.FILL_PARENT));
		btnBack_v3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);
		switch(v.getId()){
		case R.id.btnJoin:
			ll_hscv_v3.setVisibility(View.GONE);
			hscv.smoothScrollTo(display.getWidth(), 0);
			break;
		case R.id.btnBack_v2:
			ll_hscv_v2.setVisibility(View.VISIBLE);
			ll_hscv_v3.setVisibility(View.VISIBLE);
			hscv.smoothScrollTo(0, 0);
			break;
		case R.id.btnBack_v3:
			ll_hscv_v2.setVisibility(View.VISIBLE);
			ll_hscv_v3.setVisibility(View.VISIBLE);
			hscv.smoothScrollTo(0, 0);
			break;
		case R.id.ll_v1_login:
			ll_hscv_v2.setVisibility(View.GONE);
			hscv.smoothScrollTo(display.getWidth(), 0);
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch(v.getId()){

		}
	}

	class LoginTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... Void) {
			String email = "";
			String password = "";
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
				if(hscv.getScrollX() == 0){
					mFlag = true;
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
					mHandler.sendEmptyMessageDelayed(0, 2000);
				}else{
					ll_hscv_v2.setVisibility(View.VISIBLE);
					ll_hscv_v3.setVisibility(View.VISIBLE);
					hscv.smoothScrollTo(0, 0);
					mFlag = false;
				}
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
	}
}
