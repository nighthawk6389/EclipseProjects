package mavisBeacon.motw;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MediaOfTheWeekTabs extends TabActivity {
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        Resources res = getResources(); 	// Resource object to get Drawables
        TabHost tabHost = getTabHost();  	// The activity TabHost
        TabHost.TabSpec spec;  				// Resusable TabSpec for each tab
        Intent intent;  					// Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, RecentActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("recent").setIndicator("Recent",
                          res.getDrawable(R.drawable.ic_tab_artists_grey))
                      .setContent(intent); 
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this,RecentActivity.class);
        spec = tabHost.newTabSpec("articles").setIndicator("Articles",
                          res.getDrawable(R.drawable.ic_tab_artists_grey))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, RecentActivity.class);
        spec = tabHost.newTabSpec("videos").setIndicator("Videos",
                          res.getDrawable(R.drawable.ic_tab_artists_grey))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, RecentActivity.class);
        spec = tabHost.newTabSpec("tags").setIndicator("Tags",
                          res.getDrawable(R.drawable.ic_tab_artists_grey))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);
    }

}
