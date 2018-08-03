package com.mavis.nycsubwayhelpernew;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.transit.realtime.NYCTSubway.NyctTripDescriptor.Direction;
import com.mavis.nycsubwayhelpernew.R;
import com.mavis.nycsubwayhelpernew.Utilities.TrainComing;

public class SingleStationChooser extends Activity {
	
	private int default_train_index, default_station_index, station_from_intent_index, train_from_intent_index;
	private String default_train, default_station;
	static String curr_train, curr_station, station_from_intent, train_from_intent;
	private Spinner train_spinner, station_spinner;
	private Button star;
	static boolean load_first_time = true, load_from_intent = false;
	
	private static int THREAD_DIALOG_ERROR_SLEEP_TIME = 15000;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_single_station_chooser);
		
		Log.d("SSC started", station_from_intent + " " + train_from_intent);
		
		load_first_time = true;
		
		/*
		Intent i = this.getIntent();
		if( i != null){
			if( train_from_intent != null && station_from_intent != null){
				train_from_intent = i.getStringExtra("train");
				station_from_intent = i.getStringExtra("station");
				load_first_time = false;
				load_from_intent = true;
			}
		}
		*/
		
		if( !checkIfDefaultsSet() ){
			changeDefaults();
		}
		 
		Utilities.perform_setup(this);   
		
		train_spinner = (Spinner) findViewById(R.id.spinner_train); 
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.trains_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		train_spinner.setAdapter(adapter);
		
		station_spinner = (Spinner) findViewById(R.id.spinner_station);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		        R.array.stations_array_1,R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		station_spinner.setAdapter(adapter2);
		
		star = (Button) findViewById(R.id.button_star);
		
		
		/*Event Listener*/
		train_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("Train Item Selected", arg2 + "" );
				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(SingleStationChooser.this,
				        Constants.station_ids[arg2], R.layout.spinner_layout);
				adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
				station_spinner.setAdapter(adapter2);
				curr_train = train_spinner.getSelectedItem().toString().trim();
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*Event Listener*/
		station_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
				if( load_from_intent ){
					Log.d("Intent Loaded", station_from_intent_index + "");
					station_spinner.setSelection(station_from_intent_index);
					load_from_intent = false;
					load_first_time = false;
					return;
				}
				
			
				if( load_first_time && curr_train.equalsIgnoreCase(default_train) && arg2 != default_station_index){
					Log.d("First Time Loaded", arg2 + "");
					station_spinner.setSelection(default_station_index);
					load_first_time = false;
					return;
				}
				
				
				Log.d("Station Item Selected", arg2 + "" );
				// Get info from gfst-mta, parse it and display it
				String train = train_spinner.getSelectedItem().toString();
				String station = Utilities.getIDFromStationString(station_spinner.getSelectedItem().toString(), train);
				callToPopulateTrainTimes(train,station);
				Utilities.checkIfStarred(star,train,station);
				curr_station = station_spinner.getSelectedItem().toString();
				
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		star.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String train = train_spinner.getSelectedItem().toString();
				String station = Utilities.getIDFromStationString(station_spinner.getSelectedItem().toString(), train);
				Utilities.starTrainStationDirection(SingleStationChooser.this, train, station, true, true);	
				Utilities.checkIfStarred(star,train,station);
			}
		});
		
		
		//Get default values and load them
		loadSettings();
		if( train_from_intent == null ){
			train_spinner.setSelection(default_train_index);
		}
		else{
			train_from_intent_index = ((ArrayAdapter<CharSequence>)(train_spinner.getAdapter())).getPosition(train_from_intent);
			train_spinner.setSelection(train_from_intent_index);
			Log.d("TrainIndex", train_from_intent_index+"");
		}
		
		if( station_from_intent == null){
			station_spinner.setSelection(default_station_index);
			Log.d("SETTINGS","1");
		}
		else {
			String array [] = getResources().getStringArray(Constants.station_ids[train_from_intent_index]);
			int index = 0;
			for(String s : array){
				if( s.equalsIgnoreCase(station_from_intent)){
					station_from_intent_index = index;
					break;
				}
				index++;
			}
			Log.d("SETTINGS","2 - " + station_from_intent_index);
			station_spinner.setSelection(station_from_intent_index);
			Log.d("StationIndex", station_from_intent + " " + station_from_intent_index);
		}
		
		
	}

	/***
	 * 
	 * @param train
	 * @param station		Without Direction "111", Dont send "111N"
	 */
	private void callToPopulateTrainTimes(final String train, final String station) {
		// TODO Auto-generated method stub
		
		AsyncTask<Void,Void,Map<Direction,Set<TrainComing>>> task = new AsyncTask<Void,Void,Map<Direction,Set<TrainComing>>>(){

			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(SingleStationChooser.this, "", "Loading...", true);
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
							SingleStationChooser.this.runOnUiThread(new Runnable() {
							    public void run() {
							        Toast.makeText(SingleStationChooser.this, "An error occurred while contacting the server", Toast.LENGTH_LONG).show();
							    }
							});
						}
					}
				}.start();
				
				Map<Direction,Set<TrainComing>> map = 
						Utilities.getTimesForTrainAtStation(SingleStationChooser.this, train, station);
				return map;

			}
			
			@Override
			protected void onPostExecute(Map<Direction,Set<TrainComing>> result) {
				if(dialog != null){
					dialog.dismiss();
				}
				actualPopulateTrainTimes(result);
		         
			}
		};
		
		task.execute();
		
	}
	
	private void actualPopulateTrainTimes(Map<Direction,Set<TrainComing>> map){
		
		if( map == null){
			//Toast.makeText(this, "Map was null", Toast.LENGTH_LONG).show();
			return;
		}
		
		LinearLayout north = (LinearLayout)this.findViewById(R.id.linearLayout_north);
		LinearLayout south = (LinearLayout)this.findViewById(R.id.linearLayout_south);
		north.removeAllViews();
		south.removeAllViews();
	
		Utilities.populateTrainTimesForSide(this, north, map.get(Direction.NORTH));
		Utilities.populateTrainTimesForSide(this, south, map.get(Direction.SOUTH));
		
	}
	
	private void loadSettings(){
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		int trainSetting = settings.getInt(Constants.DEFAULT_TRAIN_KEY, -1);
		int stationSetting = settings.getInt(Constants.DEFAULT_STATION_KEY, -1);
		if( trainSetting == -1 || stationSetting == -1){
			Log.d("NO SETTINGS", trainSetting + " " + stationSetting);
			return;
		}
		
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(SingleStationChooser.this,
		        Constants.station_ids[trainSetting], R.layout.spinner_layout);
		adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		station_spinner.setAdapter(adapter2);
		
		default_train = train_spinner.getItemAtPosition(trainSetting).toString().trim();
		default_station = station_spinner.getItemAtPosition(stationSetting).toString().trim();
		default_train_index = trainSetting;
		default_station_index = stationSetting;
		
	}
	
	private boolean checkIfDefaultsSet() {
		// TODO Auto-generated method stub
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		int train = settings.getInt(Constants.DEFAULT_TRAIN_KEY, -1);
		int station = settings.getInt(Constants.DEFAULT_STATION_KEY, -1);
		if( train == -1 || station == -1)
			return false;
		return true;
	}
	
	public void refreshClicked(View view){
		String train = train_spinner.getSelectedItem().toString();
		String station = Utilities.getIDFromStationString(station_spinner.getSelectedItem().toString(), train);
		callToPopulateTrainTimes(train,station);
	}
	
	private void changeDefaults(){
		Intent i = new Intent(this,SetDefaults.class);
		startActivity(i);
		loadSettings();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    
	    	//Change defaults
	        case R.id.item1:
	            changeDefaults();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_single_station_chooser, menu);
		return true;
	}



}
