package com.mavisbeacon.socksoApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewHelper {
	
	public static final int MUSIC_ITEM_SIZE = 15;
	public static final int ITEM_PADDING = 30;
	public static final int ITEM_MORE_PADDING = 15;
	public static final int BOTTOM_PADDING = 15;

	
	public static View createMusicListArtist(Context context, String artistName, Integer track_count){
		
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		//Artist Name
		TextView name = createMusicListItem(context, artistName);
		
		//Track count
		TextView count = new TextView(context);
		count.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		count.setPadding(ITEM_PADDING + ITEM_MORE_PADDING, 0, 0, BOTTOM_PADDING);
		//tv.setTextColor(Color.BLACK);
		count.setTextSize(MUSIC_ITEM_SIZE);
		count.setText(track_count.toString() + " Songs");
		count.setHorizontallyScrolling(false);
		
		ll.addView(name);
		ll.addView(count);
		
		return ll;
	}
	
	public static TextView createMusicListSong(Activity context, String text){
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tv.setPadding(ITEM_PADDING, ITEM_PADDING, 0, ITEM_PADDING);
		//tv.setTextColor(Color.BLACK);
		tv.setTextSize(MUSIC_ITEM_SIZE);
		tv.setText(text);
		tv.setHorizontallyScrolling(false);
		//tv.setLongClickable(true);
		context.registerForContextMenu(tv);
		
		return tv;
	}
	
	public static TextView createMusicListItem(Context context, String text){
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tv.setPadding(ITEM_PADDING, ITEM_PADDING, 0, 0);
		//tv.setTextColor(Color.BLACK);
		tv.setTextSize(MUSIC_ITEM_SIZE);
		tv.setText(text);
		tv.setHorizontallyScrolling(false);
		
		return tv;
	}
	
	public static View createBreakLine(Context context){
		View line = new View(context);
    	line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
    	line.setBackgroundColor(Color.rgb(51, 51, 51));
    	return line;
	}
	
	public static ImageView createPlayerImage(Context context , int player_image){
		ImageView iv = new ImageView(context);
		
		switch( player_image ){
		case Player.PLAY_IMAGE : iv.setImageResource(R.drawable.player_play); break;
		case Player.PAUSE_IMAGE : iv.setImageResource(R.drawable.player_pause); break;
		case Player.STOP_IMAGE : iv.setImageResource(R.drawable.player_stop); break;
		}
		
		
		return iv;
	}

}
