package com.mavisbeacon.helicoptercontrol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class Control extends Activity {

	ControlView view;
	
    private PowerManager mPowerManager; 
    private WakeLock mWakeLock;
    
	SensorManager mSensorManager;
	Sensor mSensor;
	
	  
	float axisX, axisY, axisZ;
	
	boolean runThreads = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_control);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
         

        // Create a bright wake lock
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, getClass()
                .getName());
        
        
        /* Kickoff the Server, it will
         * be 'listening' for one client packet */
        //new Thread(new Server()).start();  
               
        // Kickoff the Client
        new Thread(new Client()).start();
		
		view = new ControlView(this);
		view.setBackgroundColor(Color.WHITE);
		
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        mWakeLock.acquire();

        // Start the simulation
        view.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */

        // Stop the simulation
        view.stop();

        // and release our wake-lock
        mWakeLock.release();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
	
	
	class ControlView extends View implements SensorEventListener{
		  private Paint paint = new Paint();
		  
		  int width, height;
		
		  public ControlView(Context context) {
		      super(context);
		
		      DisplayMetrics metrics = new DisplayMetrics();
		      Activity thisActivity = (Activity) context;
		      thisActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		      
		      paint.setColor(Color.BLACK);
		
		      this.width 	= metrics.widthPixels;
		      this.height 	= metrics.heightPixels;
		      
				VerticalSeekBar seekbar = (VerticalSeekBar) ((Activity)context).findViewById(R.id.seekBar1);
				if( seekbar == null){
					Log.d("Null","NULL NULL");
					return;
				}
				else{
					Log.d("Not Null", "NOT NULL");
				}
				
				
				seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
				{
					
				    public void onStartTrackingTouch(SeekBar seekBar){
				        // TODO Auto-generated method stub
				    }
					
				    public void onStopTrackingTouch(SeekBar seekBar){
				        // TODO Auto-generated method stub
				    }
				
					@Override
					public void onProgressChanged(SeekBar arg0,
							int progress, boolean arg2) {
						// TODO Auto-generated method stub
						Log.d("Seek","SeekBar value is "+progress);
						axisX = progress;
							
					}
				});
				
		  }
		
		  protected void onDraw(Canvas canvas) {
			  canvas.drawLine(0,height/2,width,height/2,paint);
			  
			  canvas.drawText(axisX+"",10,10,paint);
			  canvas.drawText(axisY+"",10,100,paint);
			  canvas.drawText(axisZ+"",10,200,paint);
			  
			  invalidate();
		  }
		  
		  public void start(){
			  mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
			  runThreads = true;
		  }
		  
		  public void stop(){
			  mSensorManager.unregisterListener(this);
			  runThreads = false;
		  }

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
			//axisX = event.values[0];
		    axisY = event.values[1];
		    //axisZ = event.values[2];
			
		}
	}
	
	class Server{
	 
	        public static final String SERVERIP = "192.168.1.199"; // 'Within' the emulator!
	        public static final int SERVERPORT = 9999;
	  
	}
	 
	class Client implements Runnable{
	        public void run() {
                try {
                	
                	while(runThreads){
	                    /* GIve the Server some time for startup */
	                    try {
	                    	Thread.sleep(150);
	                    } catch (InterruptedException e) { Log.d("ERROR",e.toString() );}  
	                	
	                    // Retrieve the ServerName
	                    InetAddress serverAddr = InetAddress.getByName(Server.SERVERIP);
	                   
	                    //Log.d("UDP", "C: Connecting...");
	                    /* Create new UDP-Socket */
	                    DatagramSocket socket = new DatagramSocket();
	                   
	                    /* Prepare some data to be sent. */
	                    byte[] buf = ((int)(axisX) + " " + (int)(axisY*10) + " \0").getBytes();
	                   
	                    /* Create UDP-packet with
	                     * data & destination(url+port) */
	                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, Server.SERVERPORT);
	                    //Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
	                   
	                    /* Send out the packet */
	                    socket.send(packet);
	                    //Log.d("UDP", "C: Sent.");
	                   // Log.d("UDP", "C: Done.");
                	}
                } catch (Exception e) {
                    Log.e("UDP", "C: Error", e);
                }
	        }
	}
	
	

}
