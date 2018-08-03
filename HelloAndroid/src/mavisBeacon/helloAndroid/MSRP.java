package mavisBeacon.helloAndroid;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MSRP extends TableHelper {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	Intent i = this.getIntent();
	String manufacturer = i.getStringExtra("manufacturer");
    String year = i.getStringExtra("year");
    String model = i.getStringExtra("model");
    String trim = i.getStringExtra("trim");
    String transmission = i.getStringExtra("transmission");
    JSONObject jsonData = this.getDataOfModel(manufacturer, year, model, trim, transmission);
    	if(jsonData == null){
    			Toast.makeText(this, "Error retrieving model data", Toast.LENGTH_LONG).show();
    			return;
    	}	
	
	ArrayList<String> list = new ArrayList<String>();
	list.add("msrp");
	list.add("destination_fee");
	list.add("gas_guzzler_tax");
	list.add("tax_credit");
		
	View scroll = this.inflateTableFromListOfCategories(jsonData, list);
	if(scroll == null)
		return;
	
	super.setContentView(scroll);
	}

}
