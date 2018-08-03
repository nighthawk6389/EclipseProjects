package com.mavisbeacon.socksoApplication;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PlayerService extends Service {
	
	private NotificationManager mNM;
	private int start_id;
	private int NOTIFICATION = 2;
	private MediaPlayer player; 
	private String artist_name;
	private String song_name;

	@Override
    public void onCreate() {
        Log.d("OnCreate", "WE ARE CREATED");
    }
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        start_id = startId;
        
        //Stop playing
        if( intent.getAction().equalsIgnoreCase(Utils.STOP_PLAYING)){
        	stop();
        }
        
        //New Song
        if( intent.getAction().equalsIgnoreCase(Utils.RESUME_PLAYING) ){
        	play();
        }
        
        //New Song
        if( intent.getAction().equalsIgnoreCase(Utils.PLAY_NEW_SONG) ){
        	String data_source 	= intent.getStringExtra("data_source");
        	artist_name 		= intent.getStringExtra("artist_name");
        	song_name			= intent.getStringExtra("song_name");
        	setupNewMedia( data_source );
        }
        
        if( intent.getAction().equalsIgnoreCase(Utils.CONTINUE_PLAYING) ){
        	//Do nothing
        }
        
        return START_STICKY;
    }
	
	public void setupNewMedia(String dataSource){
		Log.d("SetupMedia","Setting up new Media");
        if( player != null){
        	player.release();
        	player = null;
        }
        
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource( dataSource );
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void play(){
		if( player != null && !player.isPlaying()){
			showNotification();
			player.start();
		}
	}
	
	public void stop(){
		if( player != null && player.isPlaying()){
			hideNotification();
			player.pause();
		}
	}
	
	
	
	public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }
	
	 // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {

    	Log.d("PlayerService","Notification");
    	
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification();
        notification.icon = R.drawable.play_icon;
        notification.tickerText = artist_name;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        
  

        // The PendingIntent to launch our activity if the user selects this notification
        Intent i = new Intent(this,Player.class);
        i.setAction(Utils.CONTINUE_PLAYING);
        i.putExtra("artistName", artist_name);
        i.putExtra("songName", song_name);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, song_name,
                       artist_name, contentIntent);
        
        startForeground(NOTIFICATION, notification);

    }
    
    public void hideNotification(){
    	this.stopForeground(true);
    }
	
	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}
	
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        if( player != null)
        	player.release();
        // Tell the user we stopped.
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }

	public void setMediaPlayerCompletionListener(Player player2) {
		// TODO Auto-generated method stub
		player.setOnCompletionListener(player2);
	}

	public void setMediaPlayerBufferingUpdateListener(Player player2) {
		// TODO Auto-generated method stub
		player.setOnBufferingUpdateListener(player2);
		
	}
	public void setMediaPlayerOnPreparedListener(Player player2) {
		// TODO Auto-generated method stub
        player.setOnPreparedListener(player2);
		
	}

}
