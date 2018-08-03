package com.mavis.nycsubwayhelpernew;

import com.mavis.nycsubwayhelpernew.R;

import android.graphics.Color;

public class Constants {
	
	public static int [] station_ids = { 
		R.array.stations_array_1,
		R.array.stations_array_2,
		R.array.stations_array_3,
		R.array.stations_array_4,
		R.array.stations_array_5,
		R.array.stations_array_6,
		R.array.stations_array_GS
	};
	
	public static final String SETTINGS_FILE = "com.mavisbeacon.nycsubwayhelper.preferences";
	public static final String DEFAULT_TRAIN_KEY = "default_train";
	public static final String DEFAULT_STATION_KEY = "default_station";
	
	public static final int green 	= Color.parseColor("#59E817");
	public static final int	yellow 	= Color.parseColor("#FFA500");
	public static final int red 	= Color.parseColor("#E42217");
	public static final int slate_gray = Color.parseColor("#6D7B8D");
	public static final int city_gray = Color.parseColor("#4D5357");
	public static final int gray38 = Color.parseColor("#544E4F");


}
