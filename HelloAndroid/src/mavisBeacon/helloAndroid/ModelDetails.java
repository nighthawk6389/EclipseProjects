package mavisBeacon.helloAndroid;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ModelDetails extends TableHelper {
 
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
	list.add("car_manufacturer");
	list.add("model_year");
	list.add("model");
	list.add("trim");
	list.add("msrp");
	list.add("class");
	list.add("type");
	list.add("bed_size");
	list.add("rear_wheels");
	list.add("doors");
	list.add("engine");
	list.add("electric_motor");
	list.add("hp");
	list.add("torque");
	list.add("gas_diesel");
	list.add("transmission");
	list.add("drivetrain");
	list.add("mpg_city");
	list.add("mpg_highway");
	list.add("mpg_combined");
	list.add("range_mi");
	list.add("fuel_requirement");
	list.add("tank_capacity");
	list.add("empg_city");
	list.add("empg_highway");
	list.add("electric_range");
		
	View scroll = this.inflateTableFromListOfCategories(jsonData, list);
	if(scroll == null)
		return;
	
	super.setContentView(scroll);
	}

}