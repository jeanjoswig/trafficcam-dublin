/**
 * 
 */
package org.gtug.trafficcam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import org.gtug.trafficcam.R;

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
	    fetchNumber = settings.getInt("fetchNumber", 2);
        fetchSeekBar = (SeekBar)findViewById(R.id.fetchSeek);
        fetchSeekBar.setOnSeekBarChangeListener(this);
        fetchSeekBar.setProgress(fetchNumber-1);
	}

	
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
	{
		TextView fetchCurrent;
		fetchCurrent = (TextView)findViewById(R.id.fetchCurrent);		
			fetchNumber = progress;
		//Make sure that fetchNumer isn't set to zero
		//Display the current selection.
		fetchCurrent.setText("Selected: " + (progress+1));
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
