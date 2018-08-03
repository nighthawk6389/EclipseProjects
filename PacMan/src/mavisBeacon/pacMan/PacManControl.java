package mavisBeacon.pacMan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PacManControl extends Activity { 
	
	PacManViewTest pcv;
    
	private void makeToast(String t){
		Toast.makeText(this, t, Toast.LENGTH_SHORT).show(); 
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        pcv=(PacManViewTest)findViewById(R.id.test);  
        Handler handler=new Handler();
        handler.post(pcv);
          
        
    }//end onCreate 
    
    @Override 
    protected void onPause() {
        super.onPause();
        // Pause the game along with the activity
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
    }

} 