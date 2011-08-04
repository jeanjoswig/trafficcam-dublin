package org.gtug.trafficcam;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class SynchronisedPics
	{
	private ArrayList<Drawable> pics = new ArrayList<Drawable>();

		    public synchronized void add(Drawable newDrawable)
		    {
		    	pics.add(newDrawable);
		    }

		    public synchronized ArrayList<Drawable> output()
		    {
		        return pics;
		    }
		    public synchronized void restore()
		    {
		    	pics.clear();
		    }
		
	}