package com.mavisbeacon.socksoApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ArtistList extends Activity {
	
	private static String TAG = "ArtistList";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list); 
        
        LinearLayout ll = (LinearLayout)this.findViewById(R.id.ml_linear_layout1);
        
		getAllArtists(ll);
		
    }
	
	private void getAllArtists(LinearLayout ll){
        JSONArray artists = Utils.getAllArtists();
        
        for( int i = 0; i < artists.length(); i++){
        	final JSONObject artist = artists.optJSONObject(i);
        	final String artistName = artist.optString("name", "null");
        	final Integer trackCount = artist.optInt("track_count",-1);
        	View tv = (LinearLayout)ViewHelper.createMusicListArtist(this,artistName, trackCount);
        	tv.setOnTouchListener(new TouchListener());
        	tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent i = new Intent(ArtistList.this,SongList.class);
					i.putExtra("action", Utils.GET_ARTIST_SONGS);
					i.putExtra("id", artist.optInt("id", -1));
					ArtistList.this.startActivity(i);
				}	
        	});
        	ll.addView(tv);
        	View sep = ViewHelper.createBreakLine(this);
        	ll.addView(sep);
        	
        };
	}
	
}