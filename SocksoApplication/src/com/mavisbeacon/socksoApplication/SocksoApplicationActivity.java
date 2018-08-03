package com.mavisbeacon.socksoApplication;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;


public class SocksoApplicationActivity extends TabActivity{
    /** Called when the activity is first created. */
	
	TabHost tabHost;
	private static int TAB_HEIGHT = 50;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);  
        
        Resources res = getResources(); 	// Resource object to get Drawables
        tabHost = getTabHost();  			// The activity TabHost
        TabHost.TabSpec spec;  				// Resusable TabSpec for each tab
        Intent intent;  					// Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ArtistList.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("artists").setIndicator("Artists")
                      .setContent(intent); 
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SongList.class);
        spec = tabHost.newTabSpec("songs").setIndicator("Songs")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Do the same for the other tabs
        intent = new Intent().setClass(this,PlaylistList.class);
        spec = tabHost.newTabSpec("playlists").setIndicator("Playlist")
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        int children = tabHost.getTabWidget().getChildCount();
        for(int i = 0; i < children ; i++){
	        tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = TAB_HEIGHT;
        }
        
        tabHost.getTabWidget().setStripEnabled(false);
         
    }
	
}