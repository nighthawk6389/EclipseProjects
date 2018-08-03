package com.mavis.nycsubwayhelpernew;

import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mavis.nycsubwayhelpernew.R;
import com.mavis.nycsubwayhelpernew.Utilities.Status;

public class StatusActivity extends Activity {
	
	private TextView status_view, title;
	private LinearLayout tab_linearLayout;
	private String lineSelected = "1";
	private Set<Status> status_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		status_view = (TextView)this.findViewById(R.id.textview_status);
		title = (TextView)this.findViewById(R.id.textview_status_title);
		tab_linearLayout = (LinearLayout)this.findViewById(R.id.tab_linearlayout);
		
		callToSubwayStatus();
	}
	
	private void callToSubwayStatus(){
		AsyncTask<Void,Void,Set<Status>> task = new AsyncTask<Void,Void,Set<Status>>(){

			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(StatusActivity.this, "", "Loading...", true);
			}
			
			@Override
			protected Set<Utilities.Status> doInBackground(Void... arg0) {
				
				Set<Utilities.Status> set = Utilities.getSubwayStatus(StatusActivity.this);
				return set;

			}
			
			@Override
			protected void onPostExecute(Set<Utilities.Status> set) {
				if(dialog != null){
					dialog.dismiss();
				}
		        
				populateSubwayStatus(set);
			}
		};
		
		task.execute();
	}

	protected void populateSubwayStatus(Set<Utilities.Status> set) {
		
		if( set == null || set.size() == 0){
			//Log.d("Status", "Set was null");
			return;
		}
		
		//Log.d("Status",set.toString());
		
		status_info = set;
		
		int index = 0;
		for( Status s : set){
			String linesAffectedClean = s.linesAffected.toString().replaceAll("["+Pattern.quote("[")+Pattern.quote("]")+"]", "");
			Button b = new Button(this);
			b.setPadding(10, 10, 10, 10);
			b.setBackgroundResource(R.drawable.btn_default_normal_holo_dark);
			b.setTextColor(Color.YELLOW);
			if( s.status.contains("DELAYS"))
				b.setTextColor(Constants.red);
			if( s.status.contains("GOOD SERVICE") )
				b.setTextColor(Color.WHITE);
			b.setText(linesAffectedClean);
			b.setTag(Integer.valueOf(index++));
			b.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					int num = (Integer)v.getTag();
					for( Status s : status_info){
						if( num-- == 0){
							status_view.setText(Html.fromHtml(s.text));
							title.setText(s.linesAffected.toString().replaceAll("["+Pattern.quote("[")+Pattern.quote("]")+"]", "") + " - " + s.status);
							title.setTextColor(Color.YELLOW);
							if( s.status.contains("DELAYS"))
								title.setTextColor(Constants.red);
							if( s.status.contains("GOOD SERVICE") )
								title.setTextColor(Color.WHITE);
						}
					}
				}
			});
			
			
			tab_linearLayout.addView(b);
			
			if( s.linesAffected.contains(lineSelected) ){
				status_view.setText(Html.fromHtml(s.text));
				title.setText(linesAffectedClean + " - " + s.status);
				title.setTextColor(Color.YELLOW);
				if( s.status.contains("DELAYS"))
					title.setTextColor(Constants.red);
				if( s.status.contains("GOOD SERVICE") )
					title.setTextColor(Color.WHITE);
			}
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_status, menu);
		return true;
	}

}
