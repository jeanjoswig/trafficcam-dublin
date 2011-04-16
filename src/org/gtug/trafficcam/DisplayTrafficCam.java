package org.gtug.trafficcam;

public class DisplayTrafficCam extends Activity {

	@override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		String selectedCamera;
		getIntent().getStringExtra(selectedCamera);
		
	}

}
