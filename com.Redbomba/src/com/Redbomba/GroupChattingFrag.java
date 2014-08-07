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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
public class GroupChattingFrag extends Fragment {

	private JSONArray ja;
	private ArrayList<ChattingCellView> list;

	private ScrollView svChatting;
	private LinearLayout llChatting;
	private EditText etChatting;
	private Button btnChatting;
	
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
		btnChatting = (Button)layout.findViewById(R.id.btnChatting);

		list = new ArrayList<ChattingCellView>();
		
		etChatting.addTextChangedListener(new TextWatcher() {

	          public void afterTextChanged(Editable s) {
	        	  setScrollBottom();
	          }

	          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	          public void onTextChanged(CharSequence s, int start, int before, int count) {}
	       });
		
		btnChatting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					
					intent5.putExtra("name", Settings.user_info.getString("username"));
					intent5.putExtra("con", etChatting.getText().toString());
					getActivity().sendBroadcast(intent5);
					etChatting.setText("");
					setScrollBottom();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		new ChattingListTask().execute((Void)null);
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
				
				llChatting.addView(new ChattingCellView(getActivity(), chatname, chatcon).getView());
				
				setScrollBottom();
			}
		};

		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_04));
	}

	class ChattingListTask extends AsyncTask<Void, Void, Boolean> {

		@Override 
		protected void onPreExecute() {
			super.onPreExecute(); 
			llChatting.removeAllViews();
		}

		protected Boolean doInBackground(Void... Void) {

			try {
				ja = Settings.GET("mode=getChatting&gid="+Settings.group_info.getString("gid"));
				for(int i=0; i<ja.length();i++){
					list.add(new ChattingCellView(getActivity(),ja.getJSONObject(i)));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		protected void onProgressUpdate(Void... Void) {

		}

		protected void onPostExecute(Boolean result) {
			if(result){
				for(int i=0; i<list.size();i++)
					llChatting.addView(list.get(i).getView());
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

}