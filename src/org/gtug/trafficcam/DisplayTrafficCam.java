package org.gtug.trafficcam;

import android.app.Activity;
import android.os.Bundle;

public class DisplayTrafficCam extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String selectedCamera = null;
		getIntent().getStringExtra(selectedCamera);
		
	}

}
