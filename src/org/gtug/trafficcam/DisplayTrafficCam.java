package org.gtug.trafficcam;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DisplayTrafficCam extends Activity {

	@override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String selectedCamera;
		getIntent().getStringExtra(selectedCamera);
		
	}

}
