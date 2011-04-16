package org.gtug.trafficcam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class TrafficCamIntent extends Activity {
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Spinner s = (Spinner) findViewById(R.id.Spinner01);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.cameras_array, R.layout.myspinneritem);
		s.setAdapter(adapter);
		}
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView parent,
	        View view, int pos, long id) {
	    	String selectedCamera = R.array.cameras_array[pos];
	        	Intent intent = new Intent(TrafficCamIntent.this, DisplayTrafficCam.class);
	        	intent.putExtra(selectedCamera);
	        	startActivity(intent);
	        }
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
}