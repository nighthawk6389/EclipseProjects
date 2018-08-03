package com.mavis.nycsubwayhelpernew;

import com.mavis.nycsubwayhelpernew.R;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class NYCSubwayHelper extends TabActivity {
	
	private static final int TAB_HEIGHT = 50;
	static TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs_home);
		
        Resources res = getResources(); 	// Resource object to get Drawables
        tabHost = getTabHost();			  	// The activity TabHost
        TabHost.TabSpec spec;  				// Resusable TabSpec for each tab
        Intent intent;  					// Reusable Intent for each tab
        
        
        intent = new Intent().setClass(this, QuickView.class);
        spec = tabHost.newTabSpec("quickview").setIndicator("QuickView")
                      .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SingleStationChooser.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("countdown").setIndicator("Countdown")
                      .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this,StatusActivity.class);
        spec = tabHost.newTabSpec("status").setIndicator("Status")
                      .setContent(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(spec);

        
        int children = tabHost.getTabWidget().getChildCount();
        //tabHost.getTabWidget().getLayoutParams().height = TAB_HEIGHT;
        for(int i = 0; i < children ; i++){
        	View child = tabHost.getTabWidget().getChildAt(i);
        	TextView tv = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        child.getLayoutParams().height = TAB_HEIGHT;
	        //child.setPadding(0, 10, 0, 20);
	        if( tv != null ){
	        	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	        		tv.setTextColor(getResources().getColorStateList(R.color.tab_selector));
	        	} else{
	        		tv.setTextColor(getResources().getColorStateList(R.color.tab_selector_old));
	        	}	
	    
	        }
        }
        
        tabHost.getTabWidget().setStripEnabled(false);
        

        tabHost.setCurrentTab(1);
		
	}
	
	public static View prepareTabView(Context context, String text) {
	    View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_layout, null);
	    TextView tv = (TextView) view.findViewById(R.id.customTabTextView);
	    tv.setText(text);
	    return view;
	}
	
	private void changeDefaults(){
		Intent i = new Intent(this,SetDefaults.class);
		startActivity(i);
		//loadSettings()
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
		getMenuInflater().inflate(R.menu.activity_tabs_home, menu);
		return true;
	}

}
