package mavisBeacon.Camera2;

import mavisBeacon.Camera2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launch extends Activity {
    /** Called when the activity is first created. */
    
	private OnClickListener mStart = new OnClickListener() {
	    public void onClick(View v) {
	      startCamera();
	    }
	};
	
	private OnClickListener mEnd = new OnClickListener() {
	    public void onClick(View v) {
	      endCamera();
	    }
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button b=(Button)findViewById(R.id.start);
        b.setOnClickListener(mStart);
        Button b=(Button)findViewById(R.id.end);
        b.setOnClickListener(mEnd);
    }
}