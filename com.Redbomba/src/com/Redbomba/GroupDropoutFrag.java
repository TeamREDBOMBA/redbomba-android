package com.Redbomba;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class GroupDropoutFrag extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View layout = inflater.inflate(R.layout.frag_group_dropout, container, false);
		((TextView)layout.findViewById(R.id.textView)).setText("Dropout");
		
		return layout;
	}
}