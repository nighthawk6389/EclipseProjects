package com.mavisbeacon.socksoApplication;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MusicIntentReciever extends android.content.BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
			
			Intent i = new Intent(context,PlayerService.class);
			i.setAction(Utils.STOP_PLAYING);
			context.startService(i);
		}
		
	}
	

}
