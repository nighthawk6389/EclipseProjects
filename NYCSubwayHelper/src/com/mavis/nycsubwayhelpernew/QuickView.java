package com.mavis.nycsubwayhelpernew;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.transit.realtime.NYCTSubway.NyctTripDescriptor.Direction;
import com.mavis.nycsubwayhelpernew.R;
import com.mavis.nycsubwayhelpernew.Utilities.TrainComing;

public class QuickView extends Activity {
	
	private final static int TOP_TEXTVIEW_PADDING_TOP = 10;
	private final static int TOP_TEXTVIEW_PADDING_LEFT = 20;
	private final static int BOTTOM_TEXTVIEW_PADDING_LEFT = TOP_TEXTVIEW_PADDING_LEFT + 20;
	private final static int BOTTOM_TEXTVIEW_PADDING_BOTTOM = 10;
	
	private static int THREAD_DIALOG_ERROR_SLEEP_TIME = 15000;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_view);
		
		Utilities.perform_setup(this);
		
		//populateRecentItems();
		populateStarredItems();
		
	}
	
	/*
	private void populateRecentItems() {
		LinearLayout recentView = (LinearLayout)this.findViewById(R.id.ll_listRecent);
		Set<String> recent = Utilities.starredAndRecentMap.get(Utilities.RECENT_KEY);
		populateItems(recentView, recent);
	}
	*/
	
	private void populateStarredItems() {
		LinearLayout starredView = (LinearLayout)this.findViewById(R.id.ll_listStarred);
		Set<String> starred = Utilities.starredAndRecentMap.get(Utilities.STARRED_KEY);
		populateItems(starredView, starred);
	}
	
	private void populateItems(LinearLayout parent_view, Set<String> set){
		
		if( set == null)
			return;
		
		for(final String s: set){
			LinearLayout ll = createQuickViewLineLayout();
			ll.addView( createQuickViewLineTop(s) );
			ll.addView( createQuickViewLineBottom(s) );
			ll.addView( createHorizLine() );
			
			ll.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent i = new Intent(QuickView.this,NYCSubwayHelper.class);
					String array [] = Utilities.getArrayOfStarredAndRecentMapString(s);
					i.putExtra("train", array[0]);
					i.putExtra("station", Utilities.getStationStringFromID(array[1]));
					SingleStationChooser.load_from_intent = true;
					SingleStationChooser.train_from_intent = array[0];
					SingleStationChooser.station_from_intent = Utilities.getStationStringFromID(array[1]);
					NYCSubwayHelper.tabHost.setCurrentTab(1);
					
				}
			});
			
			parent_view.addView(ll);
		}
	}
	
	

	private LinearLayout createQuickViewLineLayout() {
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
		return ll;
		
	}
	
	private View createQuickViewLineTop(String trainStationInfo) {
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
		
		TextView tv = new TextView(this);
		tv.setTextSize(15);
		tv.setTextColor(Color.WHITE);
		tv.setPadding(TOP_TEXTVIEW_PADDING_LEFT, TOP_TEXTVIEW_PADDING_TOP, 0, 0);
		
		String array[] = Utilities.getArrayOfStarredAndRecentMapString(trainStationInfo);
		String train = array[0];
		String stationName = Utilities.getStationStringFromID(array[1]);
		tv.setText(train + " train - " + stationName);
		
		TextView tv2 = new TextView(this);
		tv2.setTextSize(16);
		tv2.setTextColor(Constants.slate_gray);
		tv2.setText("\t>");
		
		ll.addView(tv);
		ll.addView(tv2);
		
		return ll;
	}

	private View createQuickViewLineBottom(String trainStationInfo) {
		
		String array[] = Utilities.getArrayOfStarredAndRecentMapString(trainStationInfo);
		
		int id = (int)(Math.random()*1000);
		
		callToPopulateTrainTimes(this, array[0],array[1], id);
		
		View north = createQuickViewBottomItem(null, Direction.NORTH);
		View south = createQuickViewBottomItem(null, Direction.SOUTH);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setId(R.id.ll_bottom + id);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
		ll.setPadding(0, 0, 0, BOTTOM_TEXTVIEW_PADDING_BOTTOM);
		ll.addView(north);
		ll.addView(south);
		
		return ll;
		
	}
	
	private void callToPopulateTrainTimes(final Context c, final String train, final String stationID, final int id){
		
		AsyncTask<Void,Void,Map<Direction,Set<TrainComing>>> task = new AsyncTask<Void,Void,Map<Direction,Set<TrainComing>>>(){

			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(QuickView.this, "", "Loading...", true);
			}
			
			@Override
			protected Map<Direction,Set<TrainComing>> doInBackground(Void... arg0) {
				
				new Thread(){
					@Override
					public void run(){
						try {
							Thread.sleep(THREAD_DIALOG_ERROR_SLEEP_TIME);
						} catch (InterruptedException e) {
						
						}
						if( dialog.isShowing() ){
							dialog.dismiss();
							Log.e("Dialog Thread", "Canceled after 5 seconds");
							QuickView.this.runOnUiThread(new Runnable() {
							    public void run() {
							        Toast.makeText(QuickView.this, "An error occurred while contacting the server", Toast.LENGTH_LONG).show();
							    }
							});
						}
					}
				}.start();
				
				Map<Direction,Set<TrainComing>> map = 
						Utilities.getTimesForTrainAtStation(c, train, stationID);
				return map;

			}
			
			@Override
			protected void onPostExecute(Map<Direction,Set<TrainComing>> result) {
				if(dialog != null){
					dialog.dismiss();
				}
				actualPopulateQuickViewBottomLine(result, id);
		         
			}
		};
		
		task.execute();	
	}
	
	private void actualPopulateQuickViewBottomLine(Map<Direction, Set<TrainComing>> result, int id){
		
		LinearLayout ll = (LinearLayout)this.findViewById(R.id.ll_bottom + id);
		ll.removeAllViews();
		
		View north = createQuickViewBottomItem(result, Direction.NORTH);
		View south = createQuickViewBottomItem(result, Direction.SOUTH);
		
		ll.addView(north);
		ll.addView(south);
		
	}
	
	private View createQuickViewBottomItem(Map<Direction,Set<TrainComing>> map, Direction dir){
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
		
		TextView tv = createQuickViewBottomTextView();
		TextView tvMin = createQuickViewBottomMinutesTextView();
		ll.addView(tv);
		ll.addView(tvMin);
		
		if(map == null)
			return ll;
		
		
		Set<TrainComing> set = map.get(dir);
		if( set == null )
			return ll;
			
		TrainComing first = set.iterator().next();
		long now = System.currentTimeMillis() / 1000;
		long minutes = (first.time - now) / 60;
		
		if( minutes < -1)
			return ll;
		
		if( dir.equals(Direction.SOUTH)){
			tv.setText("Next Downtown:\t");
			tvMin.setText(minutes + "m");
		}
		else{
			tv.setText("Next Uptown:\t\t");
			tvMin.setText(minutes + "m");
		}
		

		return ll;
	}

	private TextView createQuickViewBottomTextView() {
		TextView tv = new TextView(this);
		tv.setTextSize(12);
		tv.setTextColor(Constants.yellow);
		tv.setPadding(BOTTOM_TEXTVIEW_PADDING_LEFT, 0, 0, 0);
		return tv;
	}
	
	private TextView createQuickViewBottomMinutesTextView() {
		TextView tv = new TextView(this);
		tv.setTextSize(16);
		tv.setTextColor(Constants.yellow);
		return tv;
	}
	
	private View createHorizLine(){
		
		View v = new View(this);
		v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1,1));
		v.setBackgroundColor(Constants.slate_gray);
		v.setPadding(2, 20, 2, 20);
		return v;
	}
	
	private void clearRecent(){
		Utilities.clearStarredAndRecentMap(this, Utilities.RECENT_KEY);
		finish();
		Intent i = new Intent(this,NYCSubwayHelper.class);
		startActivity(i);
	}
	
	private void clearStarred(){
		Utilities.clearStarredAndRecentMap(this, Utilities.STARRED_KEY);
		finish();
		Intent i = new Intent(this,NYCSubwayHelper.class);
		startActivity(i);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    
	    	//Change defaults
	       // case R.id.clear_recent:
	       //     clearRecent();
	       //     return true;
	        case R.id.clear_starred:
	            clearStarred();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_view, menu);
		return true;
	}

}
