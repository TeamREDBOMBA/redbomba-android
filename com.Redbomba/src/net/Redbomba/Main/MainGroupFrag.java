package net.Redbomba.Main;

import net.Redbomba.Group.GroupActivity;
import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.Redbomba.R;

public class MainGroupFrag extends Fragment {

	View layout;
	LinearLayout llGroupList;
	private JSONArray ja;
	
	Settings settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		layout = inflater.inflate(R.layout.frag_main_group, container, false);
		
		settings = (Settings) getActivity().getApplicationContext();

		new GroupListTask().execute(null, null, null);

		return layout;
	}

	class GroupListTask extends AsyncTask<Void, Void, Boolean> {

		protected Boolean doInBackground(Void... Void) {
			llGroupList = (LinearLayout)layout.findViewById(R.id.llGroupList);
			ja = Functions.GET("mode=getGroupList&uid="+settings.user_id);
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				try{
					for(int i=0;i<ja.length();i++){
						GroupCellView gcv  = new GroupCellView(getActivity(),ja.getJSONObject(i));
						View v = gcv.getView();
						v.setTag("group_"+i);
						v.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Vibrator Vibe = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
								Vibe.vibrate(20);

								if(v.getTag()!=null){
									String click_head = v.getTag().toString();
									if(click_head.startsWith("group_")){
										try {
											int no = Integer.parseInt(v.getTag().toString().replaceAll("group_", ""));
											Intent gin = new Intent(getActivity(),GroupActivity.class);
											startActivity(gin);
											settings.group_info = ja.getJSONObject(no);
											getActivity().overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
										} catch (Exception e) { }
									}
								}
							}
						});
						gcv.llbtnChatting.setTag("group_"+i);
						gcv.llbtnChatting.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								int no = Integer.parseInt(v.getTag().toString().replaceAll("group_", ""));
								Intent gin = new Intent(getActivity(),GroupActivity.class);
								gin.putExtra("tab", 1);
								startActivity(gin);
								try {
									settings.group_info = ja.getJSONObject(no);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								getActivity().overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
							}
						});
						llGroupList.addView(gcv.getView());
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