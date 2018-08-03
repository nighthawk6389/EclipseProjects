package mavisBeacon.motw;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Archive extends MediaTab {

	private String titleName = "Archive";
	private String categoryName = "archive";
	
	private boolean inArchiveMode = false;

	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	super.updateView(false);
    	    	
	}
	
	@Override
	public void onBackPressed(){
		if(inArchiveMode){
			inArchiveMode = false;
			super.updateView(false);
		}
		else{
			super.onBackPressed();
		}
		
	}
	
	@Override
	protected View runUpdateForNewest() {
		
		LinearLayout ll = this.createMediaLinearLayout();
    	
       	List<String> holder = GetMediaHelper.getIndexList();
       	if( holder == null ){
       		return null;
       	}
       	Iterator itList = holder.iterator();
       	
    	TextView title = this.createCategoryTitle(titleName);
    	
    	ll.addView(title);
       	ll.addView(this.createBreakLine());
    	
    	while(itList.hasNext()){
    		String nameOfFile = (String)itList.next();
           	String week = GetMediaHelper.getDateFromFileTitle(nameOfFile);
    		Button b = new Button(this);//this.createArchiveWeekButton("Week of " + week, nameOfFile);
    		b.setText("Week of " + week);
    		b.setTextColor(Color.BLUE);
    		b.setTag(nameOfFile);
    		b.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					View view = Archive.this.getArchiveView((String)arg0.getTag());
					Log.d("Archive onclick",arg0.getTag().toString());
					if(view == null){
						Archive.this.runOnUiThread(new Runnable() {
						    public void run() {
						        Toast.makeText(Archive.this, "An error occurred while contacting the server", Toast.LENGTH_LONG).show();
						    }
						});
						return;
					}
					inArchiveMode = true;
					Archive.super.setContentView(view); 
				}
    		});
    		ll.addView(b);
    	}
       	
    	ScrollView sv = new ScrollView(this);
    	sv.setLayoutParams(new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1));
    	sv.setBackgroundColor(Color.WHITE);
    	sv.addView(ll);
    	
    	return sv;
	}
	
	/**
	 * UNIMPLEMENTED FOR THIS CLASS. Uses runUpdateForNewest
	 */
	@Override
	protected View runUpdateForIndex(int index){
		return this.runUpdateForNewest();
	}
	
	protected View getArchiveView(String fileName){
		
    	LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(LinearLayout.VERTICAL);
    	ll.setBackgroundColor(Color.WHITE);
    	ll.setGravity(Gravity.FILL);
    	ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1));
    	
       	Map<String,List> holder = GetMediaHelper.getMediaList("media/"+fileName);
       	if( holder == null ){
       		return null;
       	}
       	
       	String date = this.getDateFromHolder(holder);
    	TextView title = this.createCategoryTitle(titleName + " from " + date);
    	TextView winnersTitle = this.createCategoryTitle("Winners");
    	TextView videosTitle = this.createCategoryTitle("Videos");
    	TextView articlesTitle = this.createCategoryTitle("Articles");
       	TableLayout winnersTable = this.createMediaTable(holder, "winners");
    	TableLayout videosTable = this.createMediaTable(holder, "videos");
    	TableLayout articlesTable = this.createMediaTable(holder, "articles");
    	
    	ll.addView(title);
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
    	
    	ScrollView sv = new ScrollView(this);
    	sv.addView(ll);
    	
    	return sv;
	}

	/**
	 * 
	 */
	@Override
	protected View getActivityStructureView(Map<String, List> holder) {
		// TODO Auto-generated method stub
		return null;
	}

}
