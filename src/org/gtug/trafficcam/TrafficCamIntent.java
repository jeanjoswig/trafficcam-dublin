package org.gtug.trafficcam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



public class TrafficCamIntent extends Activity 
{
	public static final String PREFS_NAME = "MyPrefsFile";
	private ArrayList<Drawable> pics = new ArrayList<Drawable>();
	private int fetchNumber = 1;
	private int spinnerPos;
	private Gallery g; 

    /** Called when the activity is first created. **/
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		// Request progress bar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		this.drawInterface();
		super.onCreate(savedInstanceState);
		}
	@Override
	protected void onResume()
		{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		if (fetchNumber != settings.getInt("fetchNumber", 2))
		{
		this.drawInterface();
		}
		super.onResume();
		}
	
	protected void drawInterface()
		{
		// Request progress spinner
		setProgressBarIndeterminateVisibility(true);
		//Define Camera Name Spinner
		Spinner s = (Spinner)findViewById(R.id.Spinner01);
		//Restore last selection
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		spinnerPos = settings.getInt("spinnerPos", 0);

		/** Can't figure out why this isn't working, it should. Might not work with xml defined spinners. **/
		//Set listener for Spinner
		MyOnItemSelectedListener l = new MyOnItemSelectedListener();
		s.setOnItemSelectedListener(l);
	    
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cameras_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		s.setAdapter(adapter);
		s.setSelection(spinnerPos);
		s.invalidate();
		}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener
		{
		public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id)
			{

			setProgressBarIndeterminateVisibility(true);
			
			new Thread(new Runnable() 
			{
			    public void run() 
			    {
			    final Context c = getBaseContext();
				Resources res = getResources();
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				//Save Spinner Position to Shared Preferences
				SharedPreferences.Editor editor = settings.edit();
			    editor.putInt("spinnerPos", pos);
			    editor.commit();
			      
				String[] camera_filenames = res.getStringArray(R.array.camera_filenames); /*load filenames from R*/
				final String selectedCamera = camera_filenames[pos]; /*set filename to the camera selected in the spinner*/
				// Restore number of pictures to fetch preference
				fetchNumber = settings.getInt("fetchNumber", 2);
				pics = (new FetchPicture()).fetch_pics(selectedCamera, fetchNumber); /*use FetchPicture to get the image for that camera*/
				// Reference the Gallery view
		        g = (Gallery) findViewById(R.id.gallery);
		        // Set the adapter to our custom adapter (below)
		        g.post(new Runnable()
		        {
		        	public void run()
		        	{
		        	g.setAdapter(new ImageAdapter(c, pics));
			        //Set the view to show the most recent picture, which is farthest to the right.
		        	int right = g.getCount() -1;
			        g.setSelection(right);
			        // We also want to show context menu for longpressed items in the gallery
			        setProgressBarIndeterminateVisibility(false);
			        registerForContextMenu(g);
				    }
			});
			    }
			}
			).start();
			
			}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
			{
			// The spinner defaults to its first item so this code can't be reached.
			}
		}
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.traffficcam_context,menu);
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
	@Override
    public boolean onContextItemSelected(MenuItem item)
	{
		final int galleryPos = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
		Resources res = getResources();
		
		String[] cameras_array = res.getStringArray(R.array.cameras_array);
		String cameraName = cameras_array[spinnerPos];
		
		//AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		/*int context1 = R.string.context_menu_1;*/
        switch (item.getItemId()) 
        {
        case R.id.view:
        	//This picture Toast will be taken out later.
        	
        	LayoutInflater inflater = getLayoutInflater();
        	View layout = inflater.inflate(R.layout.imagetoast,
        	                               (ViewGroup) findViewById(R.id.toast_layout_root));
        	ImageView image = (ImageView) layout.findViewById(R.id.toastImage);
        	
        	Drawable myDrawable = pics.get(galleryPos);
        	image.setImageDrawable(myDrawable);
        	
        	Toast toast = new Toast(getApplicationContext());
        	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        	toast.setDuration(Toast.LENGTH_LONG);
        	toast.setView(layout);
        	toast.show();
        	/*
        	Intent viewPic = new Intent(this, TouchPictureView.class);
        	startActivity(viewPic);
        		//http://mobile.tutsplus.com/tutorials/android/android-sdk-sending-pictures-the-easy-way/
        		 * http://stackoverflow.com/questions/1362723/how-can-i-get-a-dialog-style-activity-window-to-fill-the-screen
        	*/
        	
            return true;
        case R.id.save:
            Time cDT = new Time();
            cDT.setToNow();
            
            long milliTime = cDT.toMillis(false);
            milliTime = milliTime + (galleryPos*15)*60*1000;
            cDT.set(milliTime);
            String pictureFileName = cameraName + cDT.format("%Y%m%d_%H%M");
            
            if (ExternalStorageTest() == true)
            {
            	Boolean picSaved = createExternalStoragePublicPicture(pictureFileName, pics.get(galleryPos));
            	if (picSaved == true)
            	{
            		Toast.makeText(TrafficCamIntent.this, "Image saved as: " + pictureFileName + ".png", Toast.LENGTH_LONG).show();	
            	}
            	else
            	{
            		Toast.makeText(TrafficCamIntent.this, "Image Saving Failed.", Toast.LENGTH_LONG).show();
            	}
            }
            else
            {
            	Toast.makeText(TrafficCamIntent.this, "External Storage Unavailable.", Toast.LENGTH_LONG).show();
            }
        	/*Boolean storageAvailable = ExternalStorageTest();
        	
        	Boolean pictureSaved = createExternalStoragePublicPicture(item.getItemId());
        	Toast.makeText(TrafficCamIntent.this, "External Storage Available: " + storageAvailable + "\n Picture Saved: " + pictureSaved, Toast.LENGTH_SHORT).show();*/
            // Save file to folder so Android Gallery will index it.
            return true;
        case R.id.send:
        	/*String deleteResult;
            // Launch the share resource system for this file which will allow it to be sent
        	Intent sharePic = new Intent();
        	sharePic.setAction(Intent.ACTION_SEND);
        	
        	File downloadedPic =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"q.jpeg");
        	Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        	try 
        		{
				FileOutputStream out = new FileOutputStream(bitmap);
				this.compress(Bitmap.CompressFormat.PNG, 90, out);
        		} 
        	catch (FileNotFoundException e)
        		{
				// TODO Auto-generated catch block
				e.printStackTrace();
        		}

            sharePic.setType("image/png");
            sharePic.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(downloadedPic));
        	
        	if ( hasExternalStoragePublicPicture() == true)
        	{
        		deleteExternalStoragePublicPicture();
        		deleteResult = "Deleted.";
        	}
        	else
        	{
        		deleteResult = "Not Found.";
        	}
        	Toast.makeText(TrafficCamIntent.this, "Result: Image " + deleteResult, Toast.LENGTH_SHORT).show();*/
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trafficcam_menu, menu);
        return true;
    	}
    //Menu with just 2 entries: info and preferences
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) 
        {
        case R.id.info:
        	Intent about = new Intent(this, About.class);
        	startActivity(about);
            return true;
        case R.id.preferences:
            // Preferences screen to be defined
        	Intent prefs = new Intent(this, Preferences.class);
        	startActivity(prefs);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    
	public class ImageAdapter extends BaseAdapter
	{
		int mGalleryItemBackground;
    
		public ImageAdapter(Context c, ArrayList<Drawable> loadedPics)
		{
			mContext = c;
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
			pics = loadedPics;
		}
    
		public int getCount() 
		{
			return pics.size();
		}

		public Object getItem(int position)
		{
			return pics.get(position);
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			ImageView i = new ImageView(mContext);

			i.setImageDrawable(pics.get(position));
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new Gallery.LayoutParams((int)Math.rint(1.75*pics.get(0).getMinimumWidth()),(int)Math.rint(1.75*pics.get(0).getMinimumHeight())));
        
			// The preferred Gallery item background
			i.setBackgroundResource(mGalleryItemBackground);
        
        	return i;
		}
		private Context mContext;
		private ArrayList<Drawable> pics;
	}


public boolean ExternalStorageTest ()
{
	boolean mExternalStorageWriteable = false;
	String state = Environment.getExternalStorageState();

	if (Environment.MEDIA_MOUNTED.equals(state))
	{
	    // We can read and write the media
	    mExternalStorageWriteable = true;
	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
	{
	    // We can only read the media
	    mExternalStorageWriteable = false;
	} else
	{
	    // Something else is wrong. It may be one of many other states, but all we need
	    //  to know is we can neither read nor write
	    mExternalStorageWriteable = false;
	}
	return mExternalStorageWriteable;
}
Boolean createExternalStoragePublicPicture(String pictureFileName, Drawable selectedPicture)
{
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    
    File file = new File(path, pictureFileName + ".png");
    Bitmap myBitmap = ((BitmapDrawable) selectedPicture).getBitmap();

    try
    {
        // Make sure the Pictures directory exists.
        path.mkdirs();

        // Very simple code to copy a picture from the application's
        // resource into the external file.  Note that this code does
        // no error checking, and assumes the picture is small (does not
        // try to copy it in chunks).  Note that if external storage is
        // not currently mounted this will silently fail.
        OutputStream os = new FileOutputStream(file);
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        os.close();
        
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this,
                new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener()
        {
            public void onScanCompleted(String path, Uri uri)
            {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
    }
    catch (IOException e) 
    {
        // Unable to create file, likely because external storage is
        // not currently mounted.
        Log.w("ExternalStorage", "Error writing " + file, e);
        return false;
    }
    return true;
}

void deleteExternalStoragePublicPicture()
{
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File file = new File(path, "DemoPicture.jpg");
    file.delete();
}

boolean hasExternalStoragePublicPicture()
{
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File file = new File(path, "DemoPicture.jpg");
    return file.exists();
}
}