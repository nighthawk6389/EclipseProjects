package com.mavis.nycsubwayhelpernew;
 
import com.mavis.nycsubwayhelpernew.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AlarmActivity extends Activity {
	
	private Spinner train_spinner, station_spinner;
	private TimePicker timepicker;
	private RadioButton sunday, monday, tuesday, wednesday, thursday, friday, saturday;
	private CheckBox repeating;
	private EditText minToWalk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		timepicker 	= (TimePicker)	this.findViewById(R.id.timepicker_alarm);
		repeating 	= (CheckBox)	this.findViewById(R.id.checkBox_repeatingalarm);
		minToWalk 	= (EditText)	this.findViewById(R.id.editText_minToStation);
		sunday 		= (RadioButton)	this.findViewById(R.id.radio_sunday);
		monday 		= (RadioButton)	this.findViewById(R.id.radio_monday);
		tuesday 	= (RadioButton)	this.findViewById(R.id.radio_tuesday);
		wednesday 	= (RadioButton)	this.findViewById(R.id.radio_wednesday);
		thursday 	= (RadioButton)	this.findViewById(R.id.radio_thursday);
		friday 		= (RadioButton)	this.findViewById(R.id.radio_friday);
		saturday 	= (RadioButton)	this.findViewById(R.id.radio_saturday);
		
		train_spinner = (Spinner) findViewById(R.id.spinner_train3); 
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.trains_array, R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		train_spinner.setAdapter(adapter);
		
		station_spinner = (Spinner) findViewById(R.id.spinner_station3);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		        R.array.stations_array_1,R.layout.spinner_layout);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
		// Apply the adapter to the spinner
		station_spinner.setAdapter(adapter2);
		
		
		/*Event Listener*/
		train_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("Train Item Selected", arg2 + "" );
				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(AlarmActivity.this,
				        Constants.station_ids[arg2], R.layout.spinner_layout);
				adapter2.setDropDownViewResource(R.layout.dropdown_spinner_layout);
				station_spinner.setAdapter(adapter2);
				station_spinner.setSelection(0);
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void setAlarmButtonClicked(View view){
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm, menu);
		return true;
	}
	

}
