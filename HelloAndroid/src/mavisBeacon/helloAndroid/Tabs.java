package mavisBeacon.helloAndroid;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Tabs extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        Resources res = getResources(); 	// Resource object to get Drawables
        TabHost tabHost = getTabHost();  	// The activity TabHost
        TabHost.TabSpec spec;  				// Resusable TabSpec for each tab
        Intent details;  					// Reusable Intent for each tab

		
		Intent i = this.getIntent();
		String manufacturer = i.getStringExtra("manufacturer");
	    String year = i.getStringExtra("year");
	    String model = i.getStringExtra("model");
	    String trim = i.getStringExtra("trim");
	    String transmission = i.getStringExtra("transmission");
	    
	    details = new Intent().setClass(this, ModelDetails.class);
	    details.putExtra("manufacturer", manufacturer);
    	details.putExtra("year", year);
    	details.putExtra("model", model);
    	details.putExtra("trim", trim);
    	details.putExtra("transmission", transmission);
        spec = tabHost.newTabSpec("details").setIndicator("Details")
                      .setContent(details);
        tabHost.addTab(spec);
        
        details = new Intent().setClass(this, MSRP.class);
        details.putExtra("manufacturer", manufacturer);
    	details.putExtra("year", year);
    	details.putExtra("model", model);
    	details.putExtra("trim", trim);
    	details.putExtra("transmission", transmission);
        spec = tabHost.newTabSpec("msrp").setIndicator("MSRP")
                      .setContent(details);
        tabHost.addTab(spec);
        
        details = new Intent().setClass(this, EstimatePayment.class);
        details.putExtra("manufacturer", manufacturer);
    	details.putExtra("year", year);
    	details.putExtra("model", model);
    	details.putExtra("trim", trim);
    	details.putExtra("transmission", transmission);
        spec = tabHost.newTabSpec("estimate_payment").setIndicator("Estimate Payment")
                      .setContent(details);
        tabHost.addTab(spec);
        
        details = new Intent().setClass(this, Reviews.class);
        details.putExtra("manufacturer", manufacturer);
    	details.putExtra("year", year);
    	details.putExtra("model", model);
    	details.putExtra("trim", trim);
    	details.putExtra("transmission", transmission);
        spec = tabHost.newTabSpec("reviews").setIndicator("Reviews")
                      .setContent(details);
        tabHost.addTab(spec);
		
        
	}

}
