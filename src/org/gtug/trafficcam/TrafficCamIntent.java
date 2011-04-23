package org.gtug.trafficcam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TrafficCamIntent extends Activity 
{
	ArrayList<Drawable> pics = new ArrayList<Drawable>();
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				Context c = getBaseContext();
				Resources res = getResources();
				
				String[] camera_filenames = res.getStringArray(R.array.camera_filenames); /*load filenames from R*/
				final String selectedCamera = camera_filenames[pos]; /*set filename to the camera selected in the spinner*/
				pics = (new FetchPicture()).fetch_pics(selectedCamera, 10); /*use FetchPicture to get the image for that camera*/
				// Reference the Gallery view
		        Gallery g = (Gallery) findViewById(R.id.gallery);
		        // Set the adapter to our custom adapter (below)
		        g.setAdapter(new ImageAdapter(c, pics));
		        // Set a item click listener, and just Toast the clicked position
		        g.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            	String a = pics.get(position).toString();
		                Toast.makeText(TrafficCamIntent.this, "" + a, Toast.LENGTH_SHORT).show();
		            }
		        });
		        g.setOnLongClickListener(new View.OnLongClickListener()
				{
		        	@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		        
		        
		        // We also want to show context menu for longpressed items in the gallery
		        registerForContextMenu(g);
				/*ImageView iv = (ImageView) findViewById(R.id.imageholder);
				iv.setImageDrawable(pics.get(0));//load downloaded image into the imageholder
				final Bundle b=new Bundle();
				b.putString("selectedCamera", selectedCamera);
				iv.setOnLongClickListener(new View.OnLongClickListener()
			{
			
				@Override
				public boolean onLongClick(View view)
				{
					Intent intent = new Intent(TrafficCamIntent.this, TouchPictureView.class);
					intent.putExtras(b);
					startActivity(intent);
					return true;
				}
			});*/
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.info:
            //info();
            return true;
        case R.id.preferences:
            //showHelp();
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
        // See res/values/attrs.xml for the <declare-styleable> that defines
        // Gallery1.
        TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.Gallery1_android_galleryItemBackground, 0);
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