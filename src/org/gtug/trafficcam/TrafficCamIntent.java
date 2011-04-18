package org.gtug.trafficcam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class TrafficCamIntent extends Activity {
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    Spinner s = (Spinner) findViewById(R.id.Spinner01);
	    MyOnItemSelectedListener l = new MyOnItemSelectedListener(); 
	    s.setOnItemSelectedListener(l);
	    
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.cameras_array,  android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
	    s.setAdapter(adapter);
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener
	{
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	    		{
	    		Resources res = getResources();
	    		ArrayList<Drawable> pics = new ArrayList();
	    		String[] camera_filenames = res.getStringArray(R.array.camera_filenames); /*load filenames from R*/
	    		String selectedCamera = camera_filenames[pos]; /*set filename to the camera selected in the spinner*/
	    		pics = (new FetchPicture()).fetch_pics(selectedCamera, 1); /*use FetchPicture to get the image for that camera*/
	    		ImageView iv = (ImageView) findViewById(R.id.imageholder); 
	    		iv.setImageDrawable(pics.get(0));/*load downloaded image into the imageholder*/
	             }

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			// TODO Auto-generated method stub		
		}
	}
}