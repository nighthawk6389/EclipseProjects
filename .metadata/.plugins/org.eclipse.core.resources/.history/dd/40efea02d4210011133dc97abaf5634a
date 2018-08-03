package mavisBeacon.motw;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Articles extends MediaTab {
	
	private String titleName = "Articles";
	private String categoryName = "articles";

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
