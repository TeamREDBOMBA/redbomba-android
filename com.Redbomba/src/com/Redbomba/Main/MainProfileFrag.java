package com.Redbomba.Main;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.Redbomba.R;
import com.Redbomba.Landing.LandingActivity;
import com.Redbomba.Settings.Settings;

public class MainProfileFrag extends Fragment {
	
	private SharedPreferences prefs_system;
	private SharedPreferences.Editor editor_system;
	private String user_profile;
	private String user_games;
	
	public static final String BROADCAST_ACTION_06 = "com.Redbomba.Logout";
	private Intent intent6;

	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
		View rootView = lf.inflate(R.layout.frag_main_profile, vg, false);
		Button logout = (Button) rootView.findViewById(R.id.logout);
		prefs_system = this.getActivity().getSharedPreferences("system", 0);
		editor_system = prefs_system.edit();
		
		intent6 = new Intent(BROADCAST_ACTION_06);

		final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					getActivity().sendBroadcast(intent6);
					editor_system.clear();
					Settings.user_id = 0;
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

		return rootView;
	}
}