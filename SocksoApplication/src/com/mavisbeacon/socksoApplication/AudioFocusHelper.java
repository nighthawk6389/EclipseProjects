package com.mavisbeacon.socksoApplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

public class AudioFocusHelper implements OnAudioFocusChangeListener {

	AudioManager mAudioManager;
	Context ctx;
	PlayerService service;
	
	public AudioFocusHelper(Context ctx, PlayerService service){
		this.ctx = ctx;
		this.service = service;
		mAudioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
            mAudioManager.abandonAudioFocus(this);
    }

	
	@Override
	public void onAudioFocusChange(int change) {
		// TODO Auto-generated method stub
		switch( change ){
		
			case AudioManager.AUDIOFOCUS_GAIN : service.play(); break;
			case AudioManager.AUDIOFOCUS_LOSS : service.stop(); break;
		
		}

	}

}
