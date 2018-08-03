package mavisBeacon.helloAndroid;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TabData extends Activity {
	
	private static int[] TABLE_ROW_MARGINS = {2,2,2,2};
	private static int TABLE_ROW_BG = Color.BLACK;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Get data of model in JSON format
		Intent i = this.getIntent();
		
		String manufacturer = i.getStringExtra("manufacturer");
		String year = i.getStringExtra("year");
		String model = i.getStringExtra("model");
		String trim = i.getStringExtra("trim");
		String transmission = i.getStringExtra("transmission");
		
		JSONObject jsonObj = this.getDataOfModel(manufacturer,year,model,trim,transmission);
		if(jsonObj == null){
			Toast.makeText(this, "Error retrieving model data", Toast.LENGTH_LONG).show();
			return;
		}
		
		//Create table
		TableLayout layout = new TableLayout(this);
		layout.setPadding(10, 5, 10, 5);
		
		//Create LayoutParams for all rows
		TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
		tableRowParams.setMargins(TABLE_ROW_MARGINS[0], TABLE_ROW_MARGINS[1], TABLE_ROW_MARGINS[2], TABLE_ROW_MARGINS[3]);
		
		//Get the names of the columns (e.g. manufacturer, trim, etc)
		List<String> columns = URLHelper.parseURLDataNewlines("util/getColumns.php");
		Iterator<String> it = columns.iterator();
		while(it.hasNext()){
			String columnName = it.next();
			if(columnName.equals(""))
				continue; //Return from empty string
			
			//Create row to hold the name of the column and data
			TableRow trName = new TableRow(this);
			trName.setBackgroundColor(Color.BLACK);
			trName.setLayoutParams(tableRowParams);
			
			//Text View to hold name
			TextView textName = new TextView(this);
			String column = URLHelper.capitalizeAndReplaceUnderscores(columnName);
			textName.setText(column);
			textName.setTextSize(8);
			
			
			//TextView to hold data
			TextView textData = new TextView(this);
			try{
				textData.setText(jsonObj.getString(columnName));
			} catch(Exception e){
			}
			textData.setTextSize(8);
			
			//Add both TextView to TableRow
			trName.addView(textName);
			trName.addView(textData);
			//Add TableRow to TableLayout
			layout.addView(trName);	
			
		}
		
		ScrollView sv = new ScrollView(this);
		sv.addView(layout);
		super.setContentView(sv);
		
	
	}
	
	private JSONObject getDataOfModel(String manufacturer, String year,
			String model, String trim, String transmission) {
		
			String url = "main/getModelData.php?manufacturer="+ manufacturer +"&year=" + year +
							"&model="+ model + "&trim=" + trim + "&transmission=" + transmission;
			List<String> list = URLHelper.parseURLDataNewlines(url);
			
			JSONObject jsonObj = null;
			try {
				jsonObj = new JSONObject(list.get(0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		return jsonObj;
	}

}