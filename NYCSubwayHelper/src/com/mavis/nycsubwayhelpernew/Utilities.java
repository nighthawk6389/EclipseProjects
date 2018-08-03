package com.mavis.nycsubwayhelpernew;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.ExtensionRegistry;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.EntitySelector;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.NYCTSubway;
import com.google.transit.realtime.NYCTSubway.NyctTripDescriptor;
import com.google.transit.realtime.NYCTSubway.NyctTripDescriptor.Direction;
import com.mavis.nycsubwayhelpernew.R;

public class Utilities {
	
	private static ExtensionRegistry extensionRegistry;
	private static FeedMessage lastFeedMessage;
	private static long lastCallForNewMessage;
	
	public static Map<String,String> stationIDMap;
	private static final String stationIDMapFileName = "stationToIDMap.mapObj";
	
	public static Map<String,Set<String>> trainLineMap;
	private static final String trainLineMapFileName = "trainLineStops.mapObj";
	
	public static Map<String,Set<String>> starredAndRecentMap;
	private static final String starredAndRecentMapFileName = "starredAndRecentMap.mapObj";
	static final String RECENT_KEY = "RECENT";
	static final String STARRED_KEY = "STARRED";
	private static final int MAX_STARRED_AND_RECENT_SIZE = 10;
	
	private static final String fmURL = "http://datamine.mta.info/files/";
	private static String fmUrlKey;
	private static final String fmUrlKeyURL = "http://sidebump.com/subway/info.txt";
	private static final String statusURL = "http://www.mta.info/status/serviceStatus.txt";
	
	private static final int MIN_TIME_FEED_MESSAGE = 15; //Min time between calls for new message (seconds)
	
	private static XmlPullParserFactory factory = null;
	
	
	private static boolean setup_already_performed = false;
	public static void perform_setup(Context context){
		
		if( setup_already_performed)
			return;
		
		int i = 0;
		i++;
		
		/*Create extensionregistry for NYCT extensions for Proto functionality*/
		extensionRegistry = ExtensionRegistry.newInstance();
        NYCTSubway.registerAllExtensions(extensionRegistry);
        
		// Read in static object file containing mapping of ID to String name
		stationIDMap = Utilities.readInMap(context, Utilities.stationIDMapFileName, true);
		if( stationIDMap == null){
			Log.d("Error", "Null stationIDMap");
			Toast.makeText(context, "StationIDMap was NULL", Toast.LENGTH_LONG).show();
			
		}
		
		// Read in static object file containing the stops for each line
		trainLineMap = Utilities.readInMap(context,Utilities.trainLineMapFileName, true);
		if( trainLineMap == null){
			Log.d("Error", "Null trainMap");
			Toast.makeText(context, "TrainLineMap was NULL", Toast.LENGTH_LONG).show();
			
		}
		
		// Read in static object file containing the starred stations and most recent
		starredAndRecentMap = Utilities.readInMap(context,Utilities.starredAndRecentMapFileName,false);
		if( starredAndRecentMap == null){
			starredAndRecentMap = new LinkedHashMap<String,Set<String>>();
			starredAndRecentMap.put(Utilities.STARRED_KEY, new LinkedHashSet<String>());
			starredAndRecentMap.put(Utilities.RECENT_KEY, new LinkedHashSet<String>());
			//Log.d("Warn", "Null starredAndRecentMap");
			//Toast.makeText(context, "StarredAndRecentMap was NULL", Toast.LENGTH_LONG).show();
			
		}
		/*else{
			starredAndRecentMap = new LinkedHashMap<String,Set<String>>();
			Utilities.saveStarredAndRecentMap(context);
		}
		*/
        
		
		setup_already_performed = true;
		
	}
	
	public static Map readInMap(Context context, String name, boolean useAssets){
		try {	
			InputStream is;
			if( useAssets) // trainLineMap, stationIDMap
				 is = context.getAssets().open(name);
			else //StarredAndRecentMap
				is = context.openFileInput(name);
			
			ObjectInputStream read = new ObjectInputStream(is);
			
			Map map = (Map)read.readObject();
			return map;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR",e.toString());
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR",e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR",e.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR",e.toString());
		}
		return null;
	}
	
	private static InputStream getConnectionStream(final Context c, String address){
		
		if( !checkNetworkConnection(c) ){
			((Activity)c).runOnUiThread(new Runnable(){
				@Override
				public void run() {
					Toast.makeText(c, "No network connection", Toast.LENGTH_LONG).show();
				}
			});
			return null;
		}
		
		try {
				URL url = new URL(address);
				return url.openStream();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR", e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ERROR", e.toString());
		}
		return null;

	}
	
	private static void getKeyForFMURL(Context context){
        try { 
        	InputStream is = Utilities.getConnectionStream(context, Utilities.fmUrlKeyURL);
        	if( is == null){
        		Log.d("StreamFailed", "couldnt get key");
        		return;
        	}
			fmUrlKey = new BufferedReader(new InputStreamReader(is)).readLine();
			//Log.d("FM URL KEY", fmUrlKey);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("FMURLKEY FAILURE!", e.toString());
		}
	}
	
	private static FeedMessage getFeedMessage(final Context c){
		
		if( fmUrlKey == null){
			getKeyForFMURL(c);
		}
		
		FeedMessage fm = Utilities.lastFeedMessage;
		if( (System.currentTimeMillis() - Utilities.lastCallForNewMessage) / 1000 > MIN_TIME_FEED_MESSAGE){
			Log.d("FeedMessage","NEW CALL");
			InputStream stream = getConnectionStream(c, fmURL + fmUrlKey + "/gtfs");
			try {
				Utilities.lastFeedMessage = fm = FeedMessage.parseFrom(stream,extensionRegistry);
			} catch (IOException e) {
				Log.d("ERROR IN GETFEEDMESSAGE", e.toString());
			}
			Utilities.lastCallForNewMessage = System.currentTimeMillis();
		}
		return fm;
	}
	
	public static Set<Utilities.Status> getSubwayStatus(final Context c){
		InputStream stream = getConnectionStream(c, statusURL);
		XmlPullParser parser = getXmlParser();
		
		if(stream == null)
			return null;
		
		Set<Status> set = new LinkedHashSet<Status>();
		try {
			parser.setInput(new InputStreamReader(stream));
			parser.nextTag();

			while( (parser.nextTag()) == XmlPullParser.START_TAG ){
				
				if( parser.getName().equals("line") ){
					parser.nextTag(); //name
					String lines = parser.nextText();
					parser.nextTag(); //Status
					String status = parser.nextText();
					parser.nextTag(); //text
					String text = parser.nextText();
					parser.nextTag(); // date;
					String date = parser.nextText();
					parser.nextTag(); // time
					String time = parser.nextText();
					parser.nextTag(); // </line>
					set.add(new Status(lines,status,text,date,time));
					
				}
				else if( parser.getName().equals("subway") ){
					
				}
				else if( parser.getName().equals("bus") ){
					Log.d("PARSER", "Bus found, breaking");
					break;
				}
				
				else if( parser.getName().equals("responsecode") ){
					parser.nextText();
				}
				else if( parser.getName().equals("timestamp") ){
					parser.nextText();
				}
				
			}
			
			return set;
			
		} catch (XmlPullParserException e) {
			Log.d("Error in subwayStatus", e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Error in subwayStatus", e.toString());
		}
		
		return null;
	}
	
	private static XmlPullParser getXmlParser(){
		try {
			if( factory == null){
				factory = XmlPullParserFactory.newInstance();
			}
			return factory.newPullParser();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			Log.d("Error in getSubwayStatus", e.toString());
		}
		return null;
	}
	
	
	/***
	 * 
	 * @param train
	 * @param stationID		ID w/o direction (ex. 111 and NOT 111N or 111S)
	 * @return 				Map by Direction of all the times listed for that train at that station
	 */
	public static Map<Direction,Set<TrainComing>> getTimesForTrainAtStation(Context c, String train, String stationID){
				
		FeedMessage fm = Utilities.getFeedMessage(c); 
		if( fm == null )
			return null;
		
		//Get all delayed trains
		//HACK: Assumes alert message is the last one
		Set<String> delayedTrains = new HashSet<String>();
		FeedEntity feAlert = fm.getEntity(fm.getEntityCount() - 1);
		if( feAlert.hasAlert() ){
			Alert alert = feAlert.getAlert();
			for( EntitySelector es : alert.getInformedEntityList()){
				TripDescriptor td = es.getTrip();
				NyctTripDescriptor ntd = td.getExtension(NYCTSubway.nyctTripDescriptor);
				delayedTrains.add(ntd.getTrainId());
			}
		}
		
		Map<Direction,Set<TrainComing>> mapOfTimes = new HashMap<Direction,Set<TrainComing>>();
		for(FeedEntity feItem : fm.getEntityList() ){
            TripUpdate tuItem = feItem.getTripUpdate();
            NyctTripDescriptor ntdItem = tuItem.getTrip().getExtension(NYCTSubway.nyctTripDescriptor); 
            
            if( !tuItem.getTrip().getRouteId().equals(train)){ 
                continue;
            }
            
            if( !ntdItem.getIsAssigned()){
                continue;
            }
            
             boolean delayed = false;
             if( delayedTrains.contains(ntdItem.getTrainId()) ){
            	 Log.d("DELAYED",ntdItem.toString());
            	 delayed = true;
             }
            
            for( StopTimeUpdate stu : tuItem.getStopTimeUpdateList()){
                String stopId = stu.getStopId();
                if( stopId.equalsIgnoreCase(stationID+"N")){
                    long time = stu.getArrival().getTime();
                    Set<TrainComing> set = mapOfTimes.get(Direction.NORTH);
                    if( set == null ){
                    	set = new TreeSet<TrainComing>();
                    	mapOfTimes.put(Direction.NORTH, set);
                    } 
                    set.add(new Utilities.TrainComing(time, Direction.NORTH, 
                    			tuItem.getStopTimeUpdate(0).getStopId(),feItem.getVehicle().getCurrentStatus().toString(),
                    			feItem.getVehicle().getTimestamp(), delayed));
                }
                if( stopId.equalsIgnoreCase(stationID+"S")){
                    long time = stu.getArrival().getTime();
                    Set<TrainComing> set = mapOfTimes.get(Direction.SOUTH);
                    if( set == null ){
                    	set = new TreeSet<TrainComing>();
                    	mapOfTimes.put(Direction.SOUTH, set);
                    }
                    set.add(new Utilities.TrainComing(time, Direction.SOUTH, 
                    			tuItem.getStopTimeUpdate(0).getStopId(), feItem.getVehicle().getCurrentStatus().toString(),
                    			feItem.getVehicle().getTimestamp(), delayed));
                
                }
            }
		}
		
		recentTrainStationDirection(c, train, stationID, true, true);
        
        return mapOfTimes;
	}
	
	public static void populateTrainTimesForSide(Context c, LinearLayout layout, Set<TrainComing> set){
		
		/* No trains */
		if( set == null){
			Log.d("NULL WARNING", "Set was NULL");
			return;
		}
		
		
		int count = 0;
		for(TrainComing tc : set){
			if( count++ > 2)
				break;
			
			LinearLayout singleLine = createTrainTimeLine(c);
			TextView hourMinute = createHourMinuteTextView(c);
			TextView currStation = createCurrStationTextView(c);
			
			long now = System.currentTimeMillis() / 1000;
			long minutes = (tc.time - now) / 60;
			long secs = (tc.time - now) % 60;
			
			//Dont display ridiculous numbers
			if( minutes < -1)
				continue;
			
			Date date = new Date(tc.time*1000);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			String extraSpaceHour = (cal.get(Calendar.HOUR) > 9 ? "" : "  ");
			String extraZero = (cal.get(Calendar.MINUTE) > 9 ? "" : "0");
			String extraSpaceMin = (minutes > 9 ? "" : "  ");
			String s1 = cal.get(Calendar.HOUR) + ":" + extraZero + cal.get(Calendar.MINUTE) + extraSpaceHour;
			String s2 = "Arrives in " + minutes + "m" + extraSpaceMin;//, " + secs + " s";
			if( tc.delayed ){
				Log.d("DELAYED",tc.currStation+"");
				s1 = "DELAYED";
				s2 = "DELAYED";
			}
			String s3 = parseStatus(tc.status)+" " + getStationStringFromID(tc.currStation);
			
			hourMinute.setText(s1);
			TextView arrival = createArrivalTextView(c,(int)minutes);
			arrival.setText(s2);
			currStation.setText(s3);
			singleLine.addView(hourMinute);
			singleLine.addView(arrival);
			singleLine.addView(currStation);
			
			layout.addView(singleLine);
		}
		
	}
	

	public static void recentTrainStationDirection(Context c, String train, String station, boolean nValid, boolean sValid) {
		addTrainStationDirectionToMap(c, train, station, nValid, sValid, Utilities.RECENT_KEY);
	}
	
	public static void starTrainStationDirection(Context c, String train, String station, boolean nValid, boolean sValid) {
		addTrainStationDirectionToMap(c, train, station, nValid, sValid, Utilities.STARRED_KEY);
	}
	
	public static void addTrainStationDirectionToMap(Context c, String train, 
					String station, boolean nValid, boolean sValid, String key) {
		Set<String> starredOrRecent = starredAndRecentMap.get(key);
		if( starredOrRecent == null){
			starredOrRecent = new LinkedHashSet<String>();
			starredAndRecentMap.put(key, starredOrRecent);
		}
		starredOrRecent.add(getStarredAndRecentString(train,station, nValid, sValid));
		while( starredOrRecent.size() > MAX_STARRED_AND_RECENT_SIZE){
			Iterator<String> it = starredOrRecent.iterator();
			it.next();
			it.remove();
		}
		
		saveStarredAndRecentMap(c);
	}
	
	private static void saveStarredAndRecentMap(Context context) {
		try {
			OutputStream os = context.openFileOutput(Utilities.starredAndRecentMapFileName, Context.MODE_PRIVATE);
		    ObjectOutputStream save = new ObjectOutputStream(os);
		    save.writeObject(starredAndRecentMap);
		    save.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("ERROR IS SAVING STARREDANDRECENTMAP", e.toString());
		}
		
	}
	
	public static void clearStarredAndRecentMap(Context c, String key) {
		// TODO Auto-generated method stub
		Set<String> set = new LinkedHashSet<String>();
		Utilities.starredAndRecentMap.put(key, set);
		saveStarredAndRecentMap(c);
		
	}

	private static String getStarredAndRecentString(String train,String station, boolean nValid, boolean sValid){
		return train + "_" + station + "_" + nValid + "_" + sValid;
	}
	
	public static void checkIfStarred(Button button, String train, String station) {
		// TODO Auto-generated method stub
		button.setText(R.string.star_button_default);
		button.setTextColor(Color.WHITE);
		button.setClickable(true);
		button.setEnabled(true);
		for(String s : starredAndRecentMap.get(Utilities.STARRED_KEY)){
			String [] array = Utilities.getArrayOfStarredAndRecentMapString(s);
			if( array[0].equalsIgnoreCase(train) && array[1].equalsIgnoreCase(station)){
				button.setText(R.string.star_button_starred);
				button.setTextColor(Color.YELLOW);
				button.setClickable(false);
				button.setEnabled(false);
			}
		}
		
	}
	
	public static String[] getArrayOfStarredAndRecentMapString(
			String trainStationInfo) {
		// TODO Auto-generated method stub
		return trainStationInfo.split("_");
	}
	
	
	private static String parseStatus(String status){
		
		if( status.contains("IN_TRANSIT_TO")){
			return "In transit to";
		} else if( status.contains("STOPPED")){
			return "Stopped at";
		} else if( status.contains("INCOMING")){
			return "Incoming to";
		} else{
			return status;
		}
	}

	
	
	/**************************
	/* METHODS TO CREATE VIEW *
	/***********************
	 */
	
	private static LinearLayout createTrainTimeLine(Context c) {
		// TODO Auto-generated method stub
		
		LinearLayout ll = new LinearLayout(c);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1));
		
		return ll;
	}
	
	private static TextView createHourMinuteTextView(Context c) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(c);
		tv.setGravity(Gravity.LEFT);
		//tv.setHeight(40);
		tv.setTextSize(18);
		tv.setTextColor(Constants.slate_gray);
		
		return tv;
	}
	
	private static TextView createArrivalTextView(Context c, int length) {
		// TODO Auto-generated method stub
		int color = Constants.yellow;
		if( length > 5){
			color = Constants.yellow;
		}
		if( length > 10){
			color = Constants.yellow;
		}
		
		TextView tv = new TextView(c);
		tv.setPadding(30, 0, 0, 0);
		tv.setGravity(Gravity.LEFT);
		//tv.setHeight(40);
		tv.setTextColor(color);
		tv.setTextSize(16);
		
		return tv;
	}
	
	private static TextView createCurrStationTextView(Context c) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(c);
		tv.setPadding(20, 0, 0, 0);
		tv.setGravity(Gravity.LEFT);
		//tv.setHeight(50);
		tv.setMinHeight(50);
		tv.setTextColor(Color.WHITE);
		tv.setTextSize(12);
		
		return tv;
	}

	public static String getStationStringFromID(String id){

		String s = stationIDMap.get(id);	
		if( s == null)
			s = "";
		return s;
		
	}
	
	/**
	 * 
	 * @param stationString
	 * @param train
	 * @return		RETURNS ID without direction (shortens 111N to 111)
	 */
	public static String getIDFromStationString(String stationString, String train){

		Set<String> possibles = new HashSet<String>();
		for(String key : stationIDMap.keySet()){
			String s = stationIDMap.get(key);
			if( s.equalsIgnoreCase(stationString)){
				possibles.add(key);
			}
		}
		
		Set<String> stopsForLine = trainLineMap.get(train);
		if( stopsForLine == null){
			Log.d("NULL ERROR", "StopsForLine was null Train: " + train + " Map: "+ trainLineMap.toString());
			return "";
		}
		for(String s : stopsForLine){
			for(String d : possibles){
				if( s.equalsIgnoreCase(d))
					return s.substring(0, 3);
			}
		}
		Log.d("GetIdFromStationString", "RETURNING EMPTY S:" + stopsForLine.toString() + " D: " + possibles.toString());
		return "";
		
	}
	
	public static boolean checkNetworkConnection(Context c){
		ConnectivityManager connMgr = (ConnectivityManager) 
		        c.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		    return false;
	}
	
	static class TrainComing implements Comparable{
		public long time;
		public Direction dir;
		public boolean delayed;
		
		/*These three are only applicable to the current station*/
		public String currStation;
		public String status;
		public long timestamp_moved;
		
		
		public TrainComing(long time, Direction dir, String currStation, 
					String status, long timestamp, boolean delayed){
			this.time = time;
			this.dir = dir;
			this.currStation = currStation;
			this.status = status;
			this.timestamp_moved = timestamp;
			this.delayed = delayed;
		}

		@Override
		public String toString() {
			return "TrainComing [time=" + time + ", dir=" + dir
					+ ", currStation=" + currStation + "]";
		}

		@Override
		public int compareTo(Object arg) {
			// TODO Auto-generated method stub
			if( !(arg instanceof TrainComing))
				return 0;
			TrainComing tc = (TrainComing) arg;
			
			return (int)(this.time - tc.time);
		}
	}
	
	static class Status{
		Set<String> linesAffected = new HashSet<String>();
		String status;
		String text;
		String date;
		String time;
		public Status(String linesAffected, String status, String text,
				String date, String time) {
			super();
			this.linesAffected = getSetFromString(linesAffected);
			this.status = status;
			this.text = text;
			this.date = date;
			this.time = time;
		}
		
		private Set<String> getSetFromString(String lines){
			Set<String> set = new LinkedHashSet<String>();
			char [] array = lines.toCharArray();
			for( int i = 0; i < array.length; i++){
				set.add(array[i]+"");
			}
			return set;
		}

		@Override
		public String toString() {
			return "Status [linesAffected=" + linesAffected + ", status="
					+ status + ", text=" + text + ", date=" + date + ", time="
					+ time + "]";
		}
		
		
	}

}
