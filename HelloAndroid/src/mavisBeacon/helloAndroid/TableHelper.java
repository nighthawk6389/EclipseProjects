package mavisBeacon.helloAndroid;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public abstract class TableHelper extends Activity {

	protected View inflateTableFromListOfCategories(JSONObject jsonData, List<String> list){
		
		super.setContentView(R.layout.model_details);
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		ScrollView sv = (ScrollView)this.findViewById(R.id.scroll1);
		
		LinearLayout ll = (LinearLayout)this.findViewById(R.id.linear_layout1);
		try{
			
			TableLayout tableLeft = (TableLayout)this.findViewById(R.id.tableLayout1);
			TableLayout tableRight = (TableLayout)this.findViewById(R.id.tableLayout2);
			
			Iterator<String> it = list.iterator();
			int i = 0;
			while(it.hasNext()){
				String dataCategory = it.next();
				 
				tableLeft = (TableLayout)inflater.inflate(R.layout.table_row, tableLeft);
				tableRight = (TableLayout)inflater.inflate(R.layout.table_row, tableRight);
				
				//inflater.inflate(R.layout.text_view, (TableRow)tableLeft.getChildAt(i) );
				//inflater.inflate(R.layout.text_view, (TableRow)tableRight.getChildAt(i) );
				
				TextView text1 = (TextView)((TableRow)tableLeft.getChildAt(i)).getChildAt(0);
				TextView text2 = (TextView)((TableRow)tableRight.getChildAt(i)).getChildAt(0);
				
				String columnName = null;
				String data = null;
				try {
					data = jsonData.getString(dataCategory);
					columnName = jsonData.getString("display_" + dataCategory);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d("TableHelper", e.toString());
				}
				text1.setText(columnName+":");
				text2.setText(data);
			
				i++;
			}
			
		} catch(Exception ex){
			Log.d("TableHelper", ex.toString());
			return null;
		}
		
		//ScrollView scroll = new ScrollView(this);
		//scroll.addView(ll);
		
		//return scroll;
		return sv;
	}
	
	protected View createTableFromListOfCategories(JSONObject jsonData, List<String> list){
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setBackgroundColor(Color.WHITE);
		
		TableLayout layout1 = new TableLayout(this);
		TableLayout layout2 = new TableLayout(this);
		
		TableLayout.LayoutParams  params= new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.weight=1;
		params.setMargins(8, 8, 8, 8);
		
		layout1.setLayoutParams(params);
		layout1.setBackgroundColor(Color.BLACK);
		
		layout2.setLayoutParams(params);
		layout2.setBackgroundColor(Color.BLACK);
		
		Iterator<String> it = list.iterator();
		while(it.hasNext()){
			String dataCategory = it.next();
		
			TableRow row1 = new TableRow(this);
			TableRow row2 = new TableRow(this);
			
			TableLayout.LayoutParams trParams1 = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			trParams1.setMargins(2, 2, 2, 2);
			trParams1.gravity = Gravity.CENTER_HORIZONTAL;
			TableLayout.LayoutParams trParams2 = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			trParams2.setMargins(2, 2, 2, 2);
			trParams2.gravity = Gravity.CENTER_HORIZONTAL;
			
			row1.setLayoutParams(trParams1);
			row1.setBackgroundColor(Color.LTGRAY);
			row2.setLayoutParams(trParams2);
			row2.setBackgroundColor(Color.LTGRAY);
			
			TextView text =  new TextView(this);
			TextView text2 = new TextView(this);
			
			TableRow.LayoutParams tvParams1 = new TableRow.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			tvParams1.gravity = Gravity.CENTER_HORIZONTAL;
			text.setLayoutParams(tvParams1);
			text.setTextColor(Color.BLACK);
			//text.setGravity(Gravity.CENTER);
			text.setTextSize(12);
			text.setBackgroundColor(Color.RED);
			
			TableRow.LayoutParams tvParams2 = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			tvParams2.gravity = Gravity.CENTER;
			text2.setLayoutParams(tvParams2);
			text2.setTextColor(Color.BLACK);
			text2.setGravity(Gravity.CENTER);
			text2.setTextSize(12);
			
			String columnName = null;
			String data = null;
			try {
				data = jsonData.getString(dataCategory);
				columnName = jsonData.getString("display_" + dataCategory);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("TableHelper", e.toString());
			}
			text.setText(columnName+":");
			text2.setText(data);
			
			row1.addView(text);
			row2.addView(text2);
			
			layout1.addView(row1);
			layout2.addView(row2);
		
		}
		
		ll.addView(layout1);
		ll.addView(layout2);
		
		ScrollView scroll = new ScrollView(this);
		scroll.addView(ll);
		
		return scroll;
	}
	
	protected JSONObject getDataOfModel(String manufacturer, String year,
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
