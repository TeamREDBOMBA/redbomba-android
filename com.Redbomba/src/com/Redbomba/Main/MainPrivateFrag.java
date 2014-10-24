package com.Redbomba.Main;

import org.json.JSONArray;

import android.content.Context;
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

import com.Redbomba.R;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.Settings;

public class MainPrivateFrag extends Fragment {
	
	View layout;
	LinearLayout llPrivateList;
	private JSONArray ja;
	
	Settings settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.frag_main_private, container, false);
		
		settings = (Settings) getActivity().getApplicationContext();

		new PrivateListTask().execute(null, null, null);
		
		return layout;
	}
	
	class PrivateListTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			llPrivateList = (LinearLayout)layout.findViewById(R.id.llPrivateList);
			ja = Functions.GET("mode=getPrivateList&uid="+settings.user_id);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				try{
					for(int i=0;i<ja.length();i++){
						PrivateCellView pcv  = new PrivateCellView(getActivity(),ja.getJSONObject(i));
						View v = pcv.getView();
						v.setTag("private_"+i);
						v.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Vibrator Vibe = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
								Vibe.vibrate(20);
							}
						});
						llPrivateList.addView(pcv.getView());
					}
				}catch(Exception e){ Log.i("error", ""+e.getMessage()); }
			}

			return;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}