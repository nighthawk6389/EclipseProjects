package mavisBeacon.motw;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class Videos extends MediaTab {
	
	private String titleName = "Videos";
	private String categoryName = "videos";

	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	super.updateView(false);
    	    	
	}

	@Override
	protected View getActivityStructureView(Map<String,List> holder){
		LinearLayout ll = this.createMediaLinearLayout();
   
       	String date = this.getDateFromHolder(holder);
    	TextView title = this.createCategoryTitle(titleName + " from " + date);
    	TableLayout table = this.createMediaTable(holder, categoryName);
    	
    	ll.addView(title);
       	ll.addView(this.createBreakLine());
    	ll.addView(table);
    	
    	//ScrollView sv = new ScrollView(this);
    	//sv.addView(ll);
    	
    	return ll;
	}
	
}
