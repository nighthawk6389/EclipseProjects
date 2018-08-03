package com.mavisbeacon.socksoApplication;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Player extends Activity implements OnPreparedListener,OnCompletionListener, OnBufferingUpdateListener {
	
	private static String 	TAG 			= "Player";
	private static String 	STREAM_ADDR		= "http://192.168.1.101:4444/stream/";
	private static String 	SONG 			= "23";
	private Button startStream, stopStream; 
	private MediaPlayer player;
	private PlayerService service;
	private PlayerServiceConnector con = new PlayerServiceConnector(this);
	private SeekBar seek;
	private SeekBarUpdater seekUpdater;
	
	public static final int PLAY_IMAGE = 1001;
	public static final int PAUSE_IMAGE = 1002;
	public static final int STOP_IMAGE = 1003;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.player); 
	        
	        Log.d(TAG,"In Player");
	        Intent i 		= this.getIntent();
	        String action	= i.getAction();
			int id 			= i.getIntExtra("id", -1);
			String songName = i.getStringExtra("songName");
			String artistName = i.getStringExtra("artistName");
			
			String songAddress = STREAM_ADDR + id;
			
			TextView songView = (TextView)findViewById(R.id.textView1);
			songView.setText(songName);
			TextView artistView = (TextView)findViewById(R.id.textView2);
			artistView.setText(artistName);
	        
	        startStream = (Button) findViewById(R.id.start_streaming);
	        //startStream.setBackgroundResource(R.drawable.player_play);
	        startStream.setText("Start");
	        startStream.setEnabled(false);
	        startStream.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startStream.setEnabled(false);
					stopStream.setEnabled(true);
					startStream();
				}
	        });
	        
	        stopStream = (Button) findViewById(R.id.stop_streaming);
	        //stopStream.setBackgroundResource(R.drawable.player_pause);
	        stopStream.setText("Stop");
	        stopStream.setEnabled(true);
	        stopStream.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startStream.setEnabled(true);
					stopStream.setEnabled(false);
					stopStream();
				}
	        });
	        
			Intent intent1 = new Intent(this,PlayerService.class); 
			intent1.setAction( action );
			intent1.putExtra("data_source", songAddress);
			intent1.putExtra("song_name", songName);
			intent1.putExtra("artist_name", artistName);
			this.startService(intent1);
			
			Intent intent2 = new Intent(this,PlayerService.class);
			con.doBindService(intent2);	
	 }
	
	public void onNewIntent(){
		Log.d("OnNewIntent","Used same Player");
	}
	
    public void startStream(){
    	Log.d(TAG + " = startStream()", " In start stream");
    	service.play();
    }
    
    public void stopStream(){
    	Log.d(TAG + " = stopStream()", " In stop stream");
    	service.stop();
    }

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
		service.setMediaPlayerCompletionListener(this);
		service.setMediaPlayerBufferingUpdateListener(this); 
		
		seek = (SeekBar)findViewById(R.id.seekBar1); 
		seek.setOnSeekBarChangeListener(new SeekBarListener(player)); 
		seek.setMax( (int)(player.getDuration() / 1000) );
		seekUpdater = new SeekBarUpdater(player,seek);
		seekUpdater.execute();
		
		startStream();
		
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG,"OnComplete");
		//startStream.setEnabled(true);
		//stopStream.setEnabled(false);
		
	} 

 	@Override
	public void onBufferingUpdate(MediaPlayer player, int percent) {
		// TODO Auto-generated method stub
		int secondary = (int) ( (percent/100.0)*(player.getDuration()/1000) ) ;
		seek.setSecondaryProgress(secondary);
	}
	
	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	    if( seekUpdater != null)
	    	seekUpdater.stopUpdating();
	    con.doUnbindService();
	}


	public void serviceConnected(PlayerService p) {
		// TODO Auto-generated method stub
		service = p;
		player = p.getPlayer();

		if( player.isPlaying() ){
			this.onPrepared(player);
			return;
		}
		service.setMediaPlayerOnPreparedListener(this);
		player.prepareAsync(); // prepare async to not block main thread
		
		
	}

}
