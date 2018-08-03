package com.mavis.nycsubwayhelpernew;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StationToTrainsChooser extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_to_trains_chooser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_station_to_trains_chooser,
				menu);
		return true;
	}

}
