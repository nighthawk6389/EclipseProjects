package mavisBeacon.motw;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class SmartScrollView extends ScrollView{
	
	private boolean currentlyAtBottom = false;
	MediaTab mediaTab;
		
	public SmartScrollView(Context context, MediaTab mediaTab) {
		super(context);
		this.mediaTab = mediaTab; 
	} 

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
       
			View view = (View) getChildAt(getChildCount()-1);
	        int diff = (view.getBottom()-(getHeight()+getScrollY()));// Calculate the scrolldiff
	        if( diff == 0 ){  // if diff is zero, then the bottom has been reached
	        	if(!currentlyAtBottom){
	        		Log.d("SMART_SCROLL", "MyScrollView: Bottom has been reached" );
	        		mediaTab.reachedBottomOfScrollPane();
	        	}
	            currentlyAtBottom = true;
	        }
	        else
	        	currentlyAtBottom = false;
        
	        super.onScrollChanged(l, t, oldl, oldt);
	}

}
