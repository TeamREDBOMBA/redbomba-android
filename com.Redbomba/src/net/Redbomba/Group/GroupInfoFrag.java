package net.Redbomba.Group;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.NotificationService;
import net.Redbomba.Settings.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.Redbomba.R;
import net.Redbomba.R.id;
import net.Redbomba.R.layout;

public class GroupInfoFrag extends Fragment {
	private AQuery aq = new AQuery(getActivity());

	public static final String BROADCAST_ACTION_02 = "com.Redbomba.getMemOnOff";
	BroadcastReceiver broadcastReceiver;
	Intent intent;

	private JSONObject jo = null;

	private View layout;

	private ImageView ivGroupStatImg;
	private TextView tvGroupName;
	private ImageView ivGroupIcon;
	private TextView tvGameName;

	private LinearLayout llGroupMemList0;
	private LinearLayout llGroupMemList1;

	private MemberCellView[] mvc;
	private ArrayList<String> isOnline= new ArrayList<String>();
	
	Settings settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		intent = new Intent(BROADCAST_ACTION_02);
		
		settings = (Settings) getActivity().getApplicationContext();

		this.jo = settings.group_info;

		layout = inflater.inflate(R.layout.frag_group_info, container, false);

		ivGroupStatImg = (ImageView)layout.findViewById(R.id.ivGroupStatImg);
		tvGroupName = (TextView)layout.findViewById(R.id.tvGroupName);
		ivGroupIcon = (ImageView)layout.findViewById(R.id.ivGroupIcon);
		tvGameName = (TextView)layout.findViewById(R.id.tvGameName);

		llGroupMemList0 = (LinearLayout)layout.findViewById(R.id.llGroupMemList0);
		llGroupMemList1 = (LinearLayout)layout.findViewById(R.id.llGroupMemList1);
		
		tvGroupName.setTypeface(Functions.setFont(getActivity()));
		tvGameName.setTypeface(Functions.setFont(getActivity()));

		try {
			tvGroupName.setText(jo.getString("name"));
			tvGameName.setText(jo.getString("game"));
			
			int stat_no = (int)(Math.random()*641)+1;
			aq.id(ivGroupStatImg).image("http://re01-xv2938.ktics.co.kr/stat_lol_"+stat_no+".jpg", true, true, 0, 0, null, AQuery.FADE_IN);
			aq.id(ivGroupIcon).image("http://redbomba.net/media/group_icon/"+jo.getString("icon"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new MemListTask().execute((Void)null);
		setBroadcast();

		return layout;
	}

	class MemListTask extends AsyncTask<Void, Void, Boolean> {

		@Override 
		protected void onPreExecute() {
			super.onPreExecute(); 
			llGroupMemList0.removeAllViews();
			llGroupMemList1.removeAllViews();
		}

		protected Boolean doInBackground(Void... Void) {
			try{
				JSONArray memlist = jo.getJSONArray("memlist");
				mvc = new MemberCellView[memlist.length()];
				for(int i=0;i<memlist.length();i++){
					mvc[i]  = new MemberCellView(getActivity(),memlist.getJSONObject(i));
				}
			}catch (Exception e){ }

			return true;
		}

		protected void onProgressUpdate(Void... Void) {

		}

		protected void onPostExecute(Boolean result) {
			if(result){
				String[] member_list = new String[mvc.length];
				for(int i=0;i<mvc.length;i++){
					if(i%2 == 0) llGroupMemList0.addView(mvc[i].getView());
					if(i%2 == 1) llGroupMemList1.addView(mvc[i].getView());
					member_list[i] = mvc[i].getUid();
				}
				intent.putExtra("member_list", member_list);
				getActivity().sendBroadcast(intent);
			}
			return;
		}

		protected void onCancelled(){
			getActivity().finish();
		}
	}

	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_3","LOAD");
				Bundle extra = intent.getExtras();
				String Member = extra.getString("Member");

				try{
					if(extra.getString("emit").equals("isOnline")) isOnline.add(Member);
					else if(extra.getString("emit").equals("isOffline")) isOnline.remove(Member);
				}catch (Exception e) { }
				
				Set setItems = new LinkedHashSet(isOnline);
				isOnline.clear();
				isOnline.addAll(setItems);
				
				int oo = isOnline.size();
				for(int i=0;i<oo;i++)
					Log.i("isOnline_"+i, isOnline.get(i));

				for(int i=0;i<mvc.length;i++)
					if(isOnline.indexOf(mvc[i].getUid()) >= 0){
						mvc[i].setOnline();
					}else{
						mvc[i].setOffline();
					}
			}
		};

		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_03));
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}
}