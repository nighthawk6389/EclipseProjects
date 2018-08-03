package com.mavis.nycsubwayhelpernew;

import com.mavis.nycsubwayhelpernew.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetDefaults extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_defaults);
		
		Spinner train_spinner = (Spinner) findViewById(R.id.spinner_train2);  
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.trains_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		train_spinner.setAdapter(adapter);
		train_spinner.setSelection(0);
		
		Spinner station_spinner = (Spinner) findViewById(R.id.spinner_station2);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		        R.array.stations_array_1,R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		station_spinner.setAdapter(adapter2);
		station_spinner.setSelection(0); 
		
		
		/*Event Listener*/
		train_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Spinner station_spinner = (Spinner) findViewById(R.id.spinner_station2);
				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(SetDefaults.this,
				        Constants.station_ids[arg2], R.layout.spinner_layout);
				adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
				station_spinner.setAdapter(adapter2);
				station_spinner.setSelection(0);
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-genverated method stub
				
			}
		});
	}

	public void saveButtonClicked(View view) {
		Spinner train_spinner = (Spinner) findViewById(R.id.spinner_train2); 
		Spinner station_spinner = (Spinner) findViewById(R.id.spinner_station2);
	
	   SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	   SharedPreferences.Editor editor = settings.edit();
	   editor.putInt(Constants.DEFAULT_TRAIN_KEY, train_spinner.getSelectedItemPosition());
	   editor.putInt(Constants.DEFAULT_STATION_KEY, station_spinner.getSelectedItemPosition());
	   editor.commit();
	   
	   displayMainScreen();
	}

	public void cancelButtonClicked(View view) {
	   
	   displayMainScreen();
	}
	
	
	private void displayMainScreen() {
		// TODO Auto-generated method stub
		//Intent intent = new Intent(this, SingleStationChooser.class);
		//startActivity(intent);
		this.finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_set_defaults, menu);
		return true;
	}

}
