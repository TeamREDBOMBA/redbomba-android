package com.Redbomba.Group;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.Redbomba.R;
import com.Redbomba.Settings.Functions;
import com.Redbomba.Settings.NotificationService;
import com.Redbomba.Settings.Settings;

public class GroupChattingFrag extends Fragment {

	private JSONArray ja;

	private gsScrollview svChatting;
	private LinearLayout llChatting;
	private EditText etChatting;
	private ImageButton btnChatting;

	static int chat_len = 10;

	public static final String BROADCAST_ACTION_05 = "com.Redbomba.setChatting";
	private Intent intent5;

	BroadcastReceiver broadcastReceiver;
	BroadcastReceiver llbr;
	
	Settings settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		settings = (Settings) getActivity().getApplicationContext();
		
		View layout = inflater.inflate(R.layout.frag_group_chatting, container, false);
		Functions.setBadge(getActivity(),0);
		
		chat_len = 10;

		intent5 = new Intent(BROADCAST_ACTION_05);

		svChatting = (gsScrollview)layout.findViewById(R.id.svChatting);
		llChatting = (LinearLayout)layout.findViewById(R.id.llChatting);
		etChatting = (EditText)layout.findViewById(R.id.etChatting);
		btnChatting = (ImageButton)layout.findViewById(R.id.btnChatting);

		etChatting.setTypeface(Functions.setFont(getActivity()));

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
						intent5.putExtra("name", settings.user_info.getString("username"));
						intent5.putExtra("con", etChatting.getText().toString());
						intent5.putExtra("icon", settings.user_info.getString("user_icon"));
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

		new ChattingListTask().execute(true,null,null);
		setBroadcast();
		setListLoadBroadcast();

		return layout;
	}

	public void setBroadcast(){
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("setBroadcast_4","LOAD");

				Bundle extra = intent.getExtras();

				String chatname = extra.getString("name");
				String chatcon = extra.getString("con");
				String chaticon = extra.getString("icon","1");

				int dir = Gravity.LEFT;
				try {
					if(chatname.equals(settings.user_info.getString("username"))) dir = Gravity.RIGHT;
					else dir = Gravity.LEFT;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				llChatting.addView(new ChattingCellView(getActivity(), chatname, chatcon, chaticon,dir).getView());
				settings.NotiCount = 0;
				Functions.setBadge(getActivity(),settings.NotiCount);

				setScrollBottom();
			}
		};

		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.BROADCAST_ACTION_04));
	}

	public void setListLoadBroadcast(){
		llbr = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				new ChattingListTask().execute(false,null,null);
			}
		};

		getActivity().registerReceiver(llbr, new IntentFilter(gsScrollview.BROADCAST_ACTION_06));
	}

	class ChattingListTask extends AsyncTask<Boolean, Void, ArrayList<ChattingCellView>> {

		private LinearLayout ll_pb;
		private Boolean scrollBottom = true;

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

		protected ArrayList<ChattingCellView> doInBackground(Boolean... para) {
			scrollBottom = para[0];
			ArrayList<ChattingCellView> list = new ArrayList<ChattingCellView>();
			try {
				ja = Functions.GET("mode=getChatting&gid="+settings.group_info.getString("gid")+"&len="+chat_len);
				for(int i=0; i<ja.length();i++){
					int dir = Gravity.LEFT;
					if(ja.getJSONObject(i).getString("username").equals(settings.user_info.getString("username"))) dir = Gravity.RIGHT;
					else dir = Gravity.LEFT;
					list.add(new ChattingCellView(getActivity(),ja.getJSONObject(i),dir));
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
			if(scrollBottom) setScrollBottom();
			else setScrollPrevEle();
			settings.NotiCount = 0;
			Functions.setBadge(getActivity(),settings.NotiCount);
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

	private void setScrollPrevEle(){
		svChatting.post(new Runnable() {
			@Override
			public void run() {
				View v_10 = llChatting.getChildAt(10);
				svChatting.setScrollY(v_10.getMeasuredHeight( )*10);
			}
		});
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
		getActivity().unregisterReceiver(llbr);
	}
}

class gsScrollview extends ScrollView {

	public static final String BROADCAST_ACTION_06 = "com.Redbomba.loadChattingList";
	private Intent intent6;

	private Context con=null;

	Handler m_hd = null ;
	Rect m_rect ;

	public gsScrollview(Context context, AttributeSet attrs)  {
		super(context, attrs);
		con = context;
		intent6 = new Intent(BROADCAST_ACTION_06);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		checkIsLocatedAHeader( ) ;
	}

	private void checkIsLocatedAHeader() {
		if( m_rect == null ) {
			m_rect = new Rect( ) ;
			getLocalVisibleRect( m_rect );
			return ;
		}
		int oldTop = m_rect.top;

		getLocalVisibleRect( m_rect );

		ViewGroup v = (ViewGroup) getChildAt(0);

		int v_len = v.getChildCount();
		if(v_len>=GroupChattingFrag.chat_len){
			if (oldTop != m_rect.top && m_rect.top == 0 )  {
				GroupChattingFrag.chat_len += 10;
				con.sendBroadcast(intent6);
				if( m_hd != null ) {
					m_hd.sendEmptyMessage( 1 ) ;
				}
			}
		}
	}

	public void setHandler( Handler hd ){
		m_hd = hd ; 
	}
}
