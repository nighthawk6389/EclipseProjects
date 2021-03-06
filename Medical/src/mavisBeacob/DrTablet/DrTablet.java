package mavisBeacob.DrTablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;


public class DrTablet extends Activity{
	
	private OnClickListener mNext = new OnClickListener() {
	    public void onClick(View v) {
	      if(checkForm())
	    	  displayPageTwo();
	      else
	    	  displayWarning();
	    }
	};
	private OnClickListener mNext2 = new OnClickListener() {
	    public void onClick(View v) {
	    	if(v.getId()==R.id.no)
	    		displayPageThree();
	    	else
	    		displayPageThree();
	    }
	};
	private OnClickListener mNext3 = new OnClickListener() {
	    public void onClick(View v) {
	    	displayDiagnosis();
	    }
	};

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_danger);
        Button b=(Button)findViewById(R.id.next);
        b.setOnClickListener(mNext);
    }//end onCreate
    
	private void displayPageTwo() {
		this.setContentView(R.layout.main_symptoms);
		Button b=(Button)findViewById(R.id.yes);
        b.setOnClickListener(mNext2);
        b=(Button)findViewById(R.id.no);
        b.setOnClickListener(mNext2);
	}//end displayPage
	
	private void displayPageThree() {
		setContentView(R.layout.breathing);
		Button b=(Button)findViewById(R.id.next);
        b.setOnClickListener(mNext3);
	}

	private void displayDiagnosis() {
		setContentView(R.layout.diagnosis);
	}
	
	private void displayWarning(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.warning)
		       .setCancelable(false)
		       .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                displayPageTwo();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();

	}//end displaywarning

	private boolean checkForm() {
		CheckBox drink=(CheckBox)this.findViewById(R.id.drink);
		CheckBox vomit=(CheckBox)this.findViewById(R.id.drink);
		CheckBox convulse=(CheckBox)this.findViewById(R.id.drink);
		CheckBox unconscious=(CheckBox)this.findViewById(R.id.unconscious);
		CheckBox convulsing=(CheckBox)this.findViewById(R.id.convulsing);
		if(drink.isChecked())
			return false;
		if(vomit.isChecked())
			return false;
		if(convulse.isChecked())
			return false;
		if(unconscious.isChecked())
			return false;
		if(convulsing.isChecked())
			return false;
		
		return true;

	}//end checkForm

	
}//end DrTablet  