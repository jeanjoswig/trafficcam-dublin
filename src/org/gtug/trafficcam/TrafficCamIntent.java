package org.gtug.trafficcam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;



public class TrafficCamIntent extends Activity 
{

	
	 
	ArrayList<Drawable> pics = new ArrayList<Drawable>();
    /** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		// Request progress bar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		setProgressBarIndeterminateVisibility(true);
		Spinner s = (Spinner) findViewById(R.id.Spinner01);
		MyOnItemSelectedListener l = new MyOnItemSelectedListener();
		s.setOnItemSelectedListener(l);
	    
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cameras_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		s.setAdapter(adapter);
		}

	public class MyOnItemSelectedListener implements OnItemSelectedListener
		{
		ArrayList<Drawable> pics = new ArrayList<Drawable>();
		public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id)
			{
			setProgressBarIndeterminateVisibility(true);
			
			new Thread(new Runnable() 
			{
			    public void run() 
			    {
			    final Context c = getBaseContext();
				Resources res = getResources();

				String[] camera_filenames = res.getStringArray(R.array.camera_filenames); /*load filenames from R*/
				final String selectedCamera = camera_filenames[pos]; /*set filename to the camera selected in the spinner*/
				pics = (new FetchPicture()).fetch_pics(selectedCamera, 10); /*use FetchPicture to get the image for that camera*/
				// Reference the Gallery view
		        final Gallery g = (Gallery) findViewById(R.id.gallery);
		        // Set the adapter to our custom adapter (below)
		        g.post(new Runnable()
		        {
		        	public void run()
		        	{
		        g.setAdapter(new ImageAdapter(c, pics));
		        // Set a item click listener, and just Toast the clicked position
		        g.setOnItemClickListener(new OnItemClickListener() 
		        {
		            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
		            {
		            	String a = pics.get(position).toString();
		                Toast.makeText(TrafficCamIntent.this, "" + a, Toast.LENGTH_SHORT).show();
		            }
		        });
		        // We also want to show context menu for longpressed items in the gallery
		        registerForContextMenu(g);
		        setProgressBarIndeterminateVisibility(false);
			    }
			});
			    }
			}
			).start();
			}
			
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
			{
				menu.add(R.string.context_menu_1);
			}
		public boolean onContextItemSelected(MenuItem item)
			{
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Toast.makeText(TrafficCamIntent.this, "Longpress: " + info.position, Toast.LENGTH_SHORT).show();
				return true;
				/*ImageView iv = (ImageView) findViewById(R.id.imageholder);
				iv.setImageDrawable(pics.get(0));//load downloaded image into the imageholder
				final Bundle b=new Bundle();
				b.putString("selectedCamera", selectedCamera);
				iv.setOnLongClickListener(new View.OnLongClickListener()*/
			}
		

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
			{
			// The spinner defaults to its first item so this code can't be reached.
			}
		}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trafficcam_menu, menu);
        return true;
    }

    //Menu with just 2 entries: info and preferences
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) 
        {
        case R.id.info:
        	// Should display text from strings.xml (dialog?)
            //info();
        	Intent i = new Intent(this, About.class);
        	startActivity(i);
            return true;
        case R.id.preferences:
            // Preferences screen to be defined
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) 
    {	
        return true;
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
}