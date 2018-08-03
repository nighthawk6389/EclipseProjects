package com.mavisbeacon.socksoApplication;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchListener implements OnTouchListener{

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch( event.getAction() ){
			
		case MotionEvent.ACTION_DOWN 	: v.setPressed(true); 	break;
		case MotionEvent.ACTION_UP		: v.setPressed(false);	break;
		case MotionEvent.ACTION_CANCEL	: v.setPressed(false);	break;
		case MotionEvent.ACTION_MOVE	: v.setPressed(false);	break;
		
		}
		if( event.getAction() == MotionEvent.ACTION_UP)
			v.performClick();
		
		return true;
	}

}
