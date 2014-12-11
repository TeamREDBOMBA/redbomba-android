package net.Redbomba.Main;

import net.Redbomba.Main.Detail.GlobalCardActivity;
import net.Redbomba.Main.Detail.WriteFeedActivity;
import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

import org.json.JSONArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.Redbomba.R;

public class MainGlobalFrag extends Fragment {

	View layout;
	LinearLayout llGlobalList;
	ProgressBar pb;
	private JSONArray ja;
	
	BroadcastReceiver broadcastReceiver;
	Settings settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.frag_main_global, container, false);
		llGlobalList = (LinearLayout)layout.findViewById(R.id.llGlobalList);
		
		settings = (Settings) getActivity().getApplicationContext();

		pb = new ProgressBar(getActivity());
		
		llGlobalList.removeAllViews();
		llGlobalList.addView(pb);
		new GlobalListTask().execute(null, null, null);
		setBroadcast();

		return layout;
	}
	
	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("BROADCAST_ACTION_SET_GLOBAL_CARD","LOAD");

				llGlobalList.removeAllViews();
				llGlobalList.addView(pb);
				new GlobalListTask().execute(null, null, null);
			}
		};

		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(WriteFeedActivity.BROADCAST_ACTION_SET_GLOBAL_CARD));
	}

	class GlobalListTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			llGlobalList = (LinearLayout)layout.findViewById(R.id.llGlobalList);
			ja = Functions.GET("mode=getGlobalList");
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				try{
					for(int i=0;i<ja.length();i++){
						GlobalCellView gcv  = new GlobalCellView(getActivity(),ja.getJSONObject(i), i);
						View v = gcv.getView();
						v.setTag("global_"+i);
						v.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Vibrator Vibe = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
								Vibe.vibrate(20);

								if(v.getTag()!=null){
									String click_head = v.getTag().toString();
									if(click_head.startsWith("global_")){
										try {
											int no = Integer.parseInt(v.getTag().toString().replaceAll("global_", ""));
											Intent gin = new Intent(getActivity(),GlobalCardActivity.class);
											gin.putExtra("id", ja.getJSONObject(no).getString("id"));
											gin.putExtra("src", ja.getJSONObject(no).getString("img"));
											gin.putExtra("title", ja.getJSONObject(no).getString("title"));
											gin.putExtra("tag", "리그오브레전드");
											gin.putExtra("con", ja.getJSONObject(no).getString("txt"));
											gin.putExtra("comment_no", ja.getJSONObject(no).getString("comment_no"));
											startActivity(gin);
											getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
										} catch (Exception e) { }
									}
								}
							}
						});
						llGlobalList.addView(gcv.getView());
					}
					llGlobalList.removeView(pb);
				}catch(Exception e){ Log.i("error", e.getMessage()); }
			}

			return;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}
}