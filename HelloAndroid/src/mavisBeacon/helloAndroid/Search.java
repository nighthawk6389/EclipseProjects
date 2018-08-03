package mavisBeacon.helloAndroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Search extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent i = this.getIntent();
		int action = i.getIntExtra("action", 0);
		String manufacturer = i.getStringExtra("manufacturer");
		String year = i.getStringExtra("year");
		String model = i.getStringExtra("model");
		String trim = i.getStringExtra("trim");
		String transmission = i.getStringExtra("transmission");
		
		FetchURLListener l = new FetchURLListener(this,action,manufacturer,year,model,trim,transmission);
		
		View view = null;
		if(action == FetchURLListener.MANUFACTURERS){
			view = l.getManufacturers();
		}
		else if (action == FetchURLListener.YEARS){
			view = l.getYears();
		}
		else if (action == FetchURLListener.MODELS){
			view = l.getModels();
		}
		else if (action == FetchURLListener.TRIMS){
			view = l.getTrims();
		}
		
		super.setContentView(view);


	}
	
}