package org.gtug.trafficcam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
	    //String[] cameras_array = res.getStringArray(R.array.cameras_array);
	    
	    Spinner s = (Spinner) findViewById(R.id.Spinner01);
	    MyOnItemSelectedListener l = new MyOnItemSelectedListener(); 
	    s.setOnItemSelectedListener(l);
	    
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.cameras_array,  android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
	    s.setAdapter(adapter);
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	Boolean boolFlag = false;
	    	Resources res = getResources();
	    	ArrayList<Drawable> pics = new ArrayList();
	    	String[] camera_filenames = res.getStringArray(R.array.camera_filenames);

	    	String selectedCamera = camera_filenames[pos];
	        	pics = (new FetchPicture()).fetch_pics(selectedCamera, 1);
	        	ImageView iv = (ImageView) findViewById(R.id.imageholder);
	        	iv.setImageDrawable(pics.get(0));
	        	//setContentView(R.id.imageholder);
	        	
//	             private void startGame(int i) {
//	             	Log.d(TAG, "clicked on" + i);
//	             	Intent intent = new Intent(Sudoku.this, Game.class);
//	             	intent.putExtra(Game.KEY_DIFFICULTY, i);
//	             	startActivity(intent);
	             }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}