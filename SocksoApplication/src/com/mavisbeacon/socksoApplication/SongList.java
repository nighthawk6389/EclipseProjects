package com.mavisbeacon.socksoApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SongList extends Activity {
	
	private static String TAG = "SongList";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list); 
        
        LinearLayout ll = (LinearLayout)this.findViewById(R.id.ml_linear_layout1);
        
		Intent i 	= this.getIntent();
		int action 	= i.getIntExtra("action", Utils.GET_ALL_SONGS);
		int id 		= i.getIntExtra("id", -1);
        
		switch( action ){
			case Utils.GET_ALL_SONGS  : 
				getAllSongs(ll);
				break;
			case Utils.GET_ARTIST_SONGS :
				getArtistSongs(ll, id);
				break; 
		}
		
    }
	
	private void getAllSongs(LinearLayout ll){
        JSONArray songs = Utils.getAllSongs(); 
        for( int i = 0; i < songs.length(); i++){
        	final JSONObject song = songs.optJSONObject(i);
        	final JSONObject artistInfo = song.optJSONObject("artist");
        	final String songName = song.optString("name", "null");
        	TextView tv = ViewHelper.createMusicListSong(this,songName);
        	//tv.setOnTouchListener(new TouchListener());
        	tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Log.d(TAG, "Clicked");
					Intent i = new Intent(SongList.this,Player.class);
					i.setAction(Utils.PLAY_NEW_SONG);
					i.putExtra("id", song.optInt("id", -1));
					i.putExtra("songName", songName);
					i.putExtra("artistName", artistInfo.optString("name", null));
					SongList.this.startActivity(i);
				}	
        	});
        	ll.addView(tv);
        	View sep = ViewHelper.createBreakLine(this);
        	ll.addView(sep);
        };
	}
	
	private void getArtistSongs(LinearLayout ll, int id){
        JSONArray songs = Utils.getArtistSongs(id);
        for( int i = 0; i < songs.length(); i++){
        	final JSONObject song = songs.optJSONObject(i);
        	final JSONObject artistInfo = song.optJSONObject("artist");
        	final String songName = song.optString("name", "null");
        	TextView tv = ViewHelper.createMusicListSong(this,songName);
        	//tv.setOnTouchListener(new TouchListener());
        	tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Log.d(TAG, "Clicked");
					Intent i = new Intent(SongList.this,Player.class);
					i.setAction(Utils.PLAY_NEW_SONG);
					i.putExtra("id", song.optInt("id", -1));
					i.putExtra("songName", songName);
					i.putExtra("artistName", artistInfo.optString("name", null));
					SongList.this.startActivity(i);
				}	
        	});
        	ll.addView(tv);
        	View sep = ViewHelper.createBreakLine(this);
        	ll.addView(sep);
        };
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
		Log.d(TAG, "OnCreateContextMenu");
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    Log.d(TAG, ""+item.getItemId());
	    switch (item.getItemId()) {
	        case R.id.download_menu_item:
	        	Intent intent = new Intent(this,DownloadService.class); 
				intent.setAction( Utils.DOWNLOAD_SONG );
				//intent.putExtra("song_name", songName);
				//intent.putExtra("artist_name", artistName);
				this.startService(intent);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	
	 @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
		 	MenuInflater inflater = getMenuInflater();
		 	inflater.inflate(R.menu.options_menu, menu);
		 	return true;
	    }
	 
	 @Override  
	    public boolean onOptionsItemSelected(MenuItem item) {  
	        if(item.getItemId() == R.id.now_playing){
	        	Log.d(TAG, "Item Selected: " + item.getItemId());
	        	Intent i = new Intent(SongList.this,Player.class);
				i.putExtra("action", Utils.BRING_TO_FRONT);
				i.putExtra("id", -1);
	        }  
	        return true;  
	    }  

}
