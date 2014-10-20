package com.Redbomba.Group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Redbomba.R;
import com.Redbomba.R.id;
import com.Redbomba.R.layout;

public class GroupDropoutFrag extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View layout = inflater.inflate(R.layout.frag_group_dropout, container, false);
		((TextView)layout.findViewById(R.id.textView)).setText("Dropout");
		
		return layout;
	}
}