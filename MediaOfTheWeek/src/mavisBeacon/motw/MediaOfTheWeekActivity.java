package mavisBeacon.motw;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;

public class MediaOfTheWeekActivity extends TabActivity {
    
    TabHost tabHost;
    private static int TAB_HEIGHT = 50;
    
	/** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); 	// Resource object to get Drawables
        tabHost = getTabHost();  			// The activity TabHost
        TabHost.TabSpec spec;  				// Resusable TabSpec for each tab
        Intent intent;  					// Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, RecentActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("recent").setIndicator("Weekly")
                      .setContent(intent); 
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, Winners.class);
        spec = tabHost.newTabSpec("winners").setIndicator("Winners")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Do the same for the other tabs
        intent = new Intent().setClass(this,Articles.class);
        spec = tabHost.newTabSpec("articles").setIndicator("Articles")
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Videos.class);
        spec = tabHost.newTabSpec("videos").setIndicator("Videos")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, Archive.class);
        spec = tabHost.newTabSpec("index").setIndicator("Archive")
                     .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        int children = tabHost.getTabWidget().getChildCount();
        for(int i = 0; i < children ; i++){
	        tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = TAB_HEIGHT;
        }
        
        tabHost.getTabWidget().setStripEnabled(false);
        
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String tabTag = tabHost.getCurrentTabTag();
				Activity activity = MediaOfTheWeekActivity.this.getLocalActivityManager().getActivity(tabTag);
				((MediaTab)activity).updateView(false);
			}
        });
        
        Button feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("message/rfc822");//setType("text/plain");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.FEEBACK_SUBJECT);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{Constants.FEEDBACK_EMAIL});
				startActivity(Intent.createChooser(emailIntent, "Email:"));
			}
        });
    }
}