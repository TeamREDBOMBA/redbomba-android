package com.Redbomba.Landing;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.Redbomba.R;
import com.Redbomba.Main.MainActivity;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.Settings;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LandingActivity extends FragmentActivity implements OnClickListener{

	private HorizontalScrollView hscv;
	private LinearLayout ll_hscv_con;

	private LinearLayout ll_hscv_v1;
	private Button btnTryJoin;
	private LinearLayout ll_v1_login;
	
	private LinearLayout ll_hscv_v2;
	
	private LandingLoginView llv;
	private LandingJoinView ljv;

	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	private Handler mHandler;
	private boolean mFlag = false;
	private Display display;
	
	Settings settings;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		settings = (Settings) getApplicationContext();

		display = getWindowManager().getDefaultDisplay(); 

		hscv = (HorizontalScrollView)findViewById(R.id.hscv);
		hscv.setOnTouchListener( new OnTouchListener(){ 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return true;
			}
		});
		
		ll_hscv_con = (LinearLayout)findViewById(R.id.ll_hscv_con);

		prefs_system = getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();
		
		setView1();
		ljv = new LandingJoinView(this);
		llv = new LandingLoginView(this);
		
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
		btnTryJoin = (Button)findViewById(R.id.btnTryJoin);
		ll_v1_login = (LinearLayout)findViewById(R.id.ll_v1_login);
		
		ll_hscv_v2 = (LinearLayout)findViewById(R.id.ll_hscv_v2);

		btnTryJoin.setTypeface(Functions.setFont(this));

		ll_hscv_v1.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),ViewGroup.LayoutParams.FILL_PARENT));
		btnTryJoin.setOnClickListener(this);
		ll_v1_login.setOnClickListener(this);
		
		ll_hscv_v2.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(),ViewGroup.LayoutParams.FILL_PARENT));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Vibrator Vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Vibe.vibrate(20);
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		switch(v.getId()){
		case R.id.btnTryJoin:
			ll_hscv_v2.removeAllViews();
			ll_hscv_v2.addView(ljv.getView());
			hscv.smoothScrollTo(display.getWidth(), 0);
			break;
		case R.id.btnBack_v2:
			hscv.smoothScrollTo(0, 0);
			break;
		case R.id.ll_v1_login:
			ll_hscv_v2.removeAllViews();
			ll_hscv_v2.addView(llv.getView());
			hscv.smoothScrollTo(display.getWidth(), 0);
			break;
		case R.id.btnBack_v3:
			hscv.smoothScrollTo(0, 0);
			break;
		case R.id.btnLogin :
			new LoginTask().execute(null, null, null);
			break;
		case R.id.ll_ll_join:
			returnView(ljv.getView());
			break;
		case R.id.ll_lj_loin:
			returnView(llv.getView());
			break;
		}
	}
	
	private void returnView(final View v){
		hscv.smoothScrollTo(0, 0);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		        runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	ll_hscv_v2.removeAllViews();
		            	ll_hscv_v2.addView(v);
		    			hscv.smoothScrollTo(display.getWidth(), 0);
		            }
		        });
		    }
		}, 250);
	}

	class LoginTask extends AsyncTask<Void, Void, Boolean> {
		protected Boolean doInBackground(Void... Void) {
			String email = llv.getID();
			String password = llv.getPW();
			int uid=0;
			try {
				uid = Functions.GET("mode=1&email="+email+"&password="+password).getJSONObject(0).getInt("uid");
				editor_system.putInt("uid", uid);
				editor_system.commit();
				settings.user_id = uid;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				startActivity(new Intent(LandingActivity.this, MainActivity.class));
				LandingActivity.this.finish();
			}else{
				new AlertDialog.Builder(LandingActivity.this).setTitle("로그인 실패")
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
