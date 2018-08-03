package mavisBeacon.motw;

import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecentActivity extends MediaTab {

	private String titleName = "Weekly";
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	super.updateView(false);
    	    	
	}
	
	@Override
	protected View getActivityStructureView(Map<String,List> holder){
    	LinearLayout ll = this.createMediaLinearLayout();
    	
       	String date = this.getDateFromHolder(holder);
    	TextView title = this.createCategoryTitle(titleName + " from " + date);
    	TextView weeklyMessage = this.createWeeklyMessage(holder);
    	TextView winnersTitle = this.createCategoryTitle("Winners");
    	TextView videosTitle = this.createCategoryTitle("Videos");
    	TextView articlesTitle = this.createCategoryTitle("Articles");
       	TableLayout winnersTable = this.createMediaTable(holder, "winners");
    	TableLayout videosTable = this.createMediaTable(holder, "videos"); 
    	TableLayout articlesTable = this.createMediaTable(holder, "articles");
    	
    	ll.addView(title);
    	if(weeklyMessage != null)
    		ll.addView(weeklyMessage);
       	ll.addView(this.createBreakLine());
       	ll.addView(winnersTitle);
       	ll.addView(this.createBreakLine());
    	ll.addView(winnersTable);
    	ll.addView(videosTitle);
       	ll.addView(this.createBreakLine());
    	ll.addView(videosTable);
    	ll.addView(articlesTitle);
       	ll.addView(this.createBreakLine());
    	ll.addView(articlesTable);
    	
    	//SmartScrollView sv = new SmartScrollView(this,this);
    	//sv.addView(ll);
    	
    	return ll;
	}
	
}
