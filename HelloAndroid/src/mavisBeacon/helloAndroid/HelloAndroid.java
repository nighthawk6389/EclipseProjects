package mavisBeacon.helloAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class HelloAndroid extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    
	private static final int MENU_ONE=Menu.FIRST;
	private static final int MENU_TWO=Menu.FIRST+1;
	private static final int MENU_THREE=Menu.FIRST+2;
	private static final int MENU_FOUR=Menu.FIRST+3;
	private static final int MENU_CHANGE=Menu.FIRST+4;
	private static final int MENU_DELETE=Menu.FIRST+5;
	private static final int MENU_SUB1=Menu.FIRST+6;
	private static final int MENU_SUB2=Menu.FIRST+7;

    private static final String[] COUNTRIES = new String[] {
        "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
        "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
        "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
        "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
        "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
        "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
        "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
        "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
        "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
        "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
        "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
        "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
        "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
        "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
        "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
        "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
        "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
        "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
        "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
        "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
        "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
        "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
        "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
        "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
        "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
        "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
        "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
        "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
        "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
        "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
        "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
        "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
        "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
        "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
        "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
        "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
        "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
        "Ukraine", "United Arab Emirates", "United Kingdom",
        "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
        "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
        "Yemen", "Yugoslavia", "Zambia"};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try{
        super.onCreate(savedInstanceState);
        
        
        /*
		JSONObject outerObject = new JSONObject("{ 'employees': { 'firstName':'John' , 'lastName':'Doe' } }");
        JSONObject employees = outerObject.getJSONObject("employees");
        String name = employees.getString("firstName");
        String output = outerObject.toString(4);
    	*/
	
        LinearLayout layout = new LinearLayout(this);
        
        Button b = new Button (this);
        b.setText("Manufacturers");
        b.setOnClickListener(new FetchURLListener(this,0,"","","","",""));
        layout.addView(b);
        
        b = new Button (this);
        b.setText("TabData");
        b.setOnClickListener(this);
        layout.addView(b);
        
        super.setContentView(layout);
        
		
		
        //this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,COUNTRIES));
        //this.getListView().setTextFilterEnabled(true);
        //this.registerForContextMenu(this.getListView());
    	}catch(Throwable t){
    		System.out.println(t);
    		Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
    	}
    }
    
    public void onClick(View view){ 
    	Intent i = new Intent("mavisBeacon.helloAndroid.TABS");
    	i.putExtra("manufacturer", "acura");
    	i.putExtra("year", "2011");
    	i.putExtra("model", "tsx");
    	i.putExtra("trim", "sedan");
    	this.startActivity(i);
    }
    
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ONE, 0, "Display Red");
        menu.add(0, MENU_TWO, 0, "Display Blue");
        menu.add(0, MENU_THREE, 0, "Display Green");
        menu.add(0, MENU_FOUR, 0, "Display Orange");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case MENU_ONE:this.setTitleColor(Color.RED);break;
    	case MENU_TWO:this.setTitleColor(Color.BLUE);break;
    	case MENU_THREE:this.setTitleColor(Color.GREEN);break;
    	case MENU_FOUR:this.setTitleColor(Color.MAGENTA);break;
    	default:System.out.println("No Good");return false;
    	}//end switch
    	System.out.println("Good Select");
        return true;
    }//end oOIS
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0,MENU_CHANGE,0,"CHANGE ME");
    	menu.add(0,MENU_DELETE,0,"DELETE ME");
    	SubMenu sub=menu.addSubMenu("SubMenu");
    	sub.add(0,MENU_SUB1,0,"Sub1");
    	sub.add(0,MENU_SUB2,0,"Sub2");
    	
    }//end oCCM

    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    		case MENU_CHANGE:change(item);break;
    		case MENU_DELETE:alert();break;
    		case MENU_SUB1:Toast.makeText(this, "Sub1", Toast.LENGTH_SHORT);
    		case MENU_SUB2:Toast.makeText(this, "Sub1", Toast.LENGTH_SHORT);
    	}//end switch
    	return true;
    }//end oCIS
    
    public void change(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//this.setSelection((int)info.id);
    }//end change
    
    public void alert(){
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setMessage("Are you sure you want to quit");
    	builder.setCancelable(false);
    	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
                
           }
       });
    	
    	builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
            }
        });

    	AlertDialog alert = builder.create();
    	alert.show();
    }//end alert
}//end HElloAndroid
