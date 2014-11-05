package com.Redbomba.Main;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Redbomba.R;
import com.Redbomba.Landing.LandingActivity;
import com.Redbomba.Main.Detail.GlobalCardActivity;
import com.Redbomba.Settings.Settings;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

public class MainProfileFrag extends Fragment {
	private AQuery aq = new AQuery(getActivity());

	private ImageView ivIcon;
	private TextView tvName;
	private TextView tvEmail;

	private ImageView ivLinkBg;
	private ImageView ivLinkIcon;
	private TextView tvLinkNon;
	private TextView tvLinkName;

	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;

	public static final String BROADCAST_ACTION_06 = "com.Redbomba.Logout";
	private Intent intent6;

	Settings settings;

	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
		View rootView = lf.inflate(R.layout.frag_main_profile, vg, false);
		Button logout = (Button) rootView.findViewById(R.id.logout);
		prefs_system = this.getActivity().getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();

		settings = (Settings) getActivity().getApplicationContext();

		intent6 = new Intent(BROADCAST_ACTION_06);

		ivIcon = (ImageView)rootView.findViewById(R.id.ivIcon);		
		tvName = (TextView)rootView.findViewById(R.id.tvName);	
		tvEmail = (TextView)rootView.findViewById(R.id.tvEmail);	

		ivLinkBg = (ImageView)rootView.findViewById(R.id.ivLinkBg);	
		ivLinkIcon = (ImageView)rootView.findViewById(R.id.ivLinkIcon);	
		tvLinkNon = (TextView)rootView.findViewById(R.id.tvLinkNon);	
		tvLinkName = (TextView)rootView.findViewById(R.id.tvLinkName);

		ImageOptions options = new ImageOptions();
		options.round = 10000;

		TextView btn_terms_and_conditions = (TextView)rootView.findViewById(R.id.btn_terms_and_conditions);
		btn_terms_and_conditions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(),TnCActivity.class);
				startActivity(in);
			}
		});

		final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					getActivity().sendBroadcast(intent6);
					editor_system.clear();
					settings.user_id = 0;
					editor_system.commit();
					Intent intent = new Intent(getActivity(), LandingActivity.class);
					getActivity().finish();
					startActivity(intent);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					break;
				}
			}
		};

		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("로그아웃 하시겠습니까?").setPositiveButton("예", dialogClickListener)
				.setNegativeButton("아니오", dialogClickListener).show();
			}
		});
		
		try {
			aq.id(tvName).text(settings.user_info.getString("username"));
			aq.id(tvEmail).text(settings.user_info.getString("emil"));
			aq.id(ivIcon).image("http://redbomba.net"+settings.user_info.getString("user_icon"),options);
			getGameLink(settings.user_info.getString("gamelink"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rootView;
	}

	private void getGameLink(String str) throws JSONException{
		if(!str.equals("")){
			ImageOptions options = new ImageOptions();
			options.round = 10000;
			aq.id(ivLinkBg).image("http://redbomba.net/static/img/main_link_game1_bg.png");
			aq.id(ivLinkIcon).image("http://redbomba.net/static/img/game_leagueoflegends.jpg",options);
			aq.id(tvLinkName).text(str);
		}else{
			tvLinkNon.setVisibility(View.VISIBLE);
		}
	}
}