package com.Redbomba;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
public class GroupChattingFrag extends Fragment {

	private JSONArray ja;

	private ScrollView svChatting;
	private LinearLayout llChatting;
	private EditText etChatting;
	private ImageButton btnChatting;

	public static final String BROADCAST_ACTION_05 = "com.Redbomba.setChatting";
	private Intent intent5;

	BroadcastReceiver broadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.frag_group_chatting, container, false);

		intent5 = new Intent(BROADCAST_ACTION_05);

		svChatting = (ScrollView)layout.findViewById(R.id.svChatting);
		llChatting = (LinearLayout)layout.findViewById(R.id.llChatting);
		etChatting = (EditText)layout.findViewById(R.id.etChatting);
		btnChatting = (ImageButton)layout.findViewById(R.id.btnChatting);
		
		etChatting.setTypeface(Settings.setFont(getActivity()));

		etChatting.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(etChatting.getText().length() > 0){
						btnChatting.setImageResource(R.drawable.group_chat_send);
					}else{
						btnChatting.setImageResource(R.drawable.group_chat_cancel);
					}
				}else {
					btnChatting.setImageResource(R.drawable.group_chat_send);
				}
			}
		});

		etChatting.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				if(etChatting.getText().length() > 0){
					etChatting.setBackgroundResource(R.drawable.group_chat_et);
					
				}else{
					etChatting.setBackgroundResource(R.drawable.group_chat_et_m);
				}
				setScrollBottom();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		btnChatting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etChatting.getText().length() > 0){
					try {
						intent5.putExtra("name", Settings.user_info.getString("username"));
						intent5.putExtra("con", etChatting.getText().toString());
						intent5.putExtra("icon", Settings.user_info.getString("user_icon"));
						getActivity().sendBroadcast(intent5);
						etChatting.setText("");
						setScrollBottom();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		new ChattingListTask().execute(null,null,null);
		setBroadcast();

		return layout;
	}

	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				Bundle extra = intent.getExtras();

				String chatname = extra.getString("name");
				String chatcon = extra.getString("con");
				String chaticon = extra.getString("icon","1");

				llChatting.addView(new ChattingCellView(getActivity(), chatname, chatcon, chaticon).getView());

				setScrollBottom();
			}
		};

		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_04));
	}

	class ChattingListTask extends AsyncTask<Void, Void, ArrayList<ChattingCellView>> {
		
		private LinearLayout ll_pb;

		@Override 
		protected void onPreExecute() {
			super.onPreExecute(); 
			llChatting.removeAllViews();
			ll_pb = new LinearLayout(getActivity());
			ll_pb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			ll_pb.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			ll_pb.addView(new ProgressBar(getActivity()));
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					llChatting.addView(ll_pb);
				}
			});
		}

		protected ArrayList<ChattingCellView> doInBackground(Void... Void) {
			ArrayList<ChattingCellView> list = new ArrayList<ChattingCellView>();
			try {
				ja = Settings.GET("mode=getChatting&gid="+Settings.group_info.getString("gid"));
				for(int i=0; i<ja.length();i++){
					list.add(new ChattingCellView(getActivity(),ja.getJSONObject(i)));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return list;
		}

		protected void onPostExecute(ArrayList<ChattingCellView> para) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					llChatting.removeView(ll_pb);
				}
			});
			
			for(int i=0; i<para.size(); i++){
				llChatting.addView(para.get(i).getView());
			}
			setScrollBottom();
			return;
		}

		protected void onCancelled(){
			getActivity().finish();
		}
	}

	private void setScrollBottom(){
		svChatting.post(new Runnable() {
			@Override
			public void run() {
				svChatting.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}
}