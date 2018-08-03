package com.mavisbeacon.socksoApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class Utils {
	
	private static String host 		= "192.168.1.101";
    private static int port 		= 4444;
    private static String artists	= "/api/artists/";
    private static String allSongs	= "/api/tracks/";
    
    public static final int GET_ALL_ARTISTS 	= 100;
    public static final int GET_ARTIST_SONGS 	= 101;
    public static final int GET_ALL_SONGS 		= 102;
    public static final int BRING_TO_FRONT		= 103;
    
    public static final String PLAY_NEW_SONG	= "com.mavisbeacon.sockoApplication.PLAY_NEW_SONG";
    public static final String RESUME_PLAYING	= "com.mavisbeacon.sockoApplication.RESUME_PLAYING";
    public static final String STOP_PLAYING		= "com.mavisbeacon.sockoApplication.STOP_PLAYING";
    public static final String CONTINUE_PLAYING	= "com.mavisbeacon.sockoApplication.CONTINUE_PLAYING";
    public static final String DOWNLOAD_SONG	= "com.mavisbeacon.sockoApplication.DOWNLOAD_SONG";
    
    public static final String SOCKSO_DOWNLOAD_FOLDER = "socksoDownloads";
    
    private static final int CONNECT_TIMEOUT = 10000;
    
	
	public static BufferedReader getURLStream(String host, int port, String rest){
		
        URL url = null;
        URLConnection urlConnection = null;
        try { 
            url = new URL("http" , host , port, rest);
            urlConnection = url.openConnection();
            urlConnection.setConnectTimeout( CONNECT_TIMEOUT );
            Log.d("URL", url.toString());
        } catch (MalformedURLException ex) {
            Log.d("getUrlStream","MaformedURLException: " + ex.getMessage());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        BufferedReader stream = null;
		try {
			stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			Log.d("getUrlStream" , e1.toString());
		}
		return stream;
	}
	
	public static JSONArray getAllArtists(){
        		
        BufferedReader stream = Utils.getURLStream(host, port, artists);
                
        return getJSONArrayFromStream(stream);
	}
	
	public static JSONArray getArtistSongs(int songID){
		
        BufferedReader stream = Utils.getURLStream(host, port, artists+songID+"/tracks");
                
        return getJSONArrayFromStream(stream);
	}
	
public static JSONArray getAllSongs(){
		
        BufferedReader stream = Utils.getURLStream(host, port, allSongs);
                
        return getJSONArrayFromStream(stream);
	}
	
	public static JSONArray getJSONArrayFromStream(BufferedReader stream){
        List<String> list = new ArrayList<String>();
        String line;
		try {
			while( (line = stream.readLine()) != null ){
				list.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d("JSON", list.toString());
		
		JSONArray json = null;
		try {
			json = new JSONArray( list.get(0) );//list.get(0) );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
