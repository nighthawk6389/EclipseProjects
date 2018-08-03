package com.mavisbeacon.socksoApplication;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SeekBar;

public class SeekBarUpdater extends AsyncTask<Void,Void,Void>{

	private final MediaPlayer player;
	private final SeekBar seek;
	private boolean isRunning = true;
	
	private static final int THREAD_SLEEP = 1000;
	
	public SeekBarUpdater(MediaPlayer player, SeekBar seek){
		this.player = player;
		this.seek = seek;
	}

	
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		new Thread(){
			@Override
			public void run(){
				while( isRunning ){
					try {
						Thread.sleep(THREAD_SLEEP);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SeekBarUpdater.this.publishProgress();
				} 
			}
		}.start();
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void...voids){
		if( isRunning && player.isPlaying() ){
			int currentPos = (int)(player.getCurrentPosition() / 1000);
			seek.setProgress(currentPos);
		}
	}
	
	public void stopUpdating(){
		isRunning = false;
	}

}
