package org.gtug.trafficcam;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;



public class TrafficCamIntent extends Activity {
    /** Called when the activity is first created. */
/*	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Spinner s = (Spinner) findViewById(R.id.Spinner01);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.cameras_array, R.layout.myspinneritem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		}*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    Resources res = getResources();
	    String[] cameras_array = res.getStringArray(R.array.cameras_array);
	    String[] locations_array, names_array, temp_array;
	    String delimiter = "|";
	    for (int i = 0; i<cameras_array.length; i++)
	    	{
	    	temp_array = cameras_array[i].split(delimiter);
	    	}
	    
	    Spinner s = (Spinner) findViewById(R.id.Spinner01);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, &temp_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(adapter);
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	Boolean boolFlag = false;
	    	Resources res = getResources();
	    	String[] cameras_array = res.getStringArray(R.array.cameras_array);

	    	String selectedCamera = cameras_array[pos];
	        	Intent intent = new Intent(TrafficCamIntent.this, DisplayTrafficCam.class);
	        	intent.putExtra(selectedCamera, boolFlag);
	        	startActivity(intent);
	        }



		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	    }

	}
