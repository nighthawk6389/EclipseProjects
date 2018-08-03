package com.mavisbeacon.socksoApplication;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarListener implements OnSeekBarChangeListener {
	
	private MediaPlayer player;
	
	private static final String TAG = "SeekBarListener";
	
	public SeekBarListener(MediaPlayer player){
		this.player = player;
	}

	@Override
	public void onProgressChanged(SeekBar seek, int progress, boolean user) {
		// TODO Auto-generated method stub
		
		int secondary = seek.getSecondaryProgress();
		if( player.isPlaying() && user && secondary > progress){
			player.seekTo( (int)(progress*1000) );
			
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

}
