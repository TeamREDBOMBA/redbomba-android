package net.Redbomba.Main;

import net.Redbomba.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class TnCActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tnc);
		setTitle("이용약관 및 기타");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
