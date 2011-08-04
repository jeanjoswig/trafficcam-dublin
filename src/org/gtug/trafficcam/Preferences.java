/**
 * 
 */
package org.gtug.trafficcam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author Rory
 *
 */
public class Preferences extends Activity implements SeekBar.OnSeekBarChangeListener
{
	public static final String PREFS_NAME = "MyPrefsFile";
	SeekBar fetchSeekBar;
	
	private int fetchNumber;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		TextView fetchCurrent = (TextView)findViewById(R.id.fetchCurrent);
		fetchCurrent.setText("Selected: " + 1);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		/*Pull the settings for the Seekbar and load them into it.*/
	    fetchNumber = settings.getInt("fetchNumber", 2);
        fetchSeekBar = (SeekBar)findViewById(R.id.fetchSeek);
        fetchSeekBar.setOnSeekBarChangeListener(this);
        fetchSeekBar.setProgress(fetchNumber-1);
        /*Pull the setting of the CheckBox from SharedPreferences and load them into it.*/
        Boolean singleFile = settings.getBoolean("singleFile", false);
    	final CheckBox checkBox = (CheckBox) findViewById(R.id.singleFileBox);
        checkBox.setChecked(singleFile);
        /*Listen for checkBox being clicked.*/
        checkBox.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean("singleFile", checkBox.isChecked());
			    editor.commit();
			}
        });
	}

	
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
	{
		TextView fetchCurrent;
		fetchCurrent = (TextView)findViewById(R.id.fetchCurrent);		
			fetchNumber = progress;
		//Make sure that fetchNumer isn't set to zero
		//Display the current selection.
		fetchCurrent.setText("Selected: " + (progress+1));
		if (progress == 0)
		{
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
		    editor.putInt("fetchNumber", progress+1);
		    editor.commit();
		}
	}

	
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// This function has to be implemented, but we don't need it for anything.
		
	}

	
	public void onStopTrackingTouch(SeekBar seekBar)
	{
	      
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
	    editor.putInt("fetchNumber", fetchNumber+1);
	    editor.commit();
	}
	
}
