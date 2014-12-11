package net.Redbomba.Main;

import net.Redbomba.Settings.Functions;
import net.Redbomba.Settings.Settings;

import net.Redbomba.R;
import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotiCellView extends View {
	private AQuery aq;
	
	private View feed;
	private Context con;

	private ImageView imgIcon;
	private TextView tvCon;
	private TextView tvDate;
	
	Settings settings;

	public NotiCellView(Context context, String strCon, String strDate, String strImg) {
		super(context);
		this.con = context;
		aq = new AQuery(con);
		// TODO Auto-generated method stub
		settings = (Settings) context.getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		feed = inflater.inflate(R.layout.cell_notification, null);
		
		String urlIcon = "http://redbomba.net"+strImg;
		
		imgIcon = (ImageView)feed.findViewById(R.id.imgIcon);
		tvCon = (TextView)feed.findViewById(R.id.tvCon);
		tvDate = (TextView)feed.findViewById(R.id.tvDate);
		
		tvCon.setTypeface(Functions.setFont(context));
		tvDate.setTypeface(Functions.setFont(context));
		
		tvCon.setText(Functions.stripHTML(strCon));
		tvDate.setText(strDate);
		aq.id(imgIcon).image(urlIcon);
	}
	
	public View getView(){
		return feed;
	}
}