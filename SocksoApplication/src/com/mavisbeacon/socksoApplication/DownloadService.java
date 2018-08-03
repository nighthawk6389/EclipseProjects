package com.mavisbeacon.socksoApplication;

import java.io.File;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


public class DownloadService extends Service {
	
	private NotificationManager mNM;
	private int start_id;
	private int NOTIFICATION = 2;
	private MediaPlayer player; 
	private String artist_name;
	private String song_name;

	private static final String TAG = "DownloadService";
	
	@Override
    public void onCreate() {
        Log.d("OnCreate", "WE ARE CREATED");
 
        
    }
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        
        if( !isGoodStorageState() ){
        	this.stopSelf();
        }
        
        File root = Environment.getRootDirectory();	
        File folder = new File(root.getAbsolutePath() + File.separator + Utils.SOCKSO_DOWNLOAD_FOLDER);
        if( !folder.isDirectory() ){
        	boolean isCreated = folder.mkdir();
        	Log.d(TAG, "Folder Create Status: " + isCreated );
        }
        Log.d(TAG, "Using folder: " + folder.getAbsolutePath());
        
        return START_STICKY;
        
	}
	
	private boolean isGoodStorageState(){
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		     mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}

	public class LocalBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
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
	
}