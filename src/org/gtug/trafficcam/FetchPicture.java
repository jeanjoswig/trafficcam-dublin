/** Library to fetch current traffic pictures of Dublin.
 * 
 */
package org.gtug.trafficcam;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import android.graphics.drawable.Drawable;

/**
 * @author joswigjn
 * 
 * Utility class to retrieve traffic pictures from GAE 
 * 
 */

public class FetchPicture {	
	private ArrayList<Drawable> pics = new ArrayList<Drawable>();		
/*	private String base_address = "http://trafficcam-fetcher.savina.net/serve_single_pic/";	*/
	private String base_address = "http://trafficcam-fetcher.savina.net/serve_single_pic_GTUG_100/";

	FetchPicture() {
		super();
	}
	/*
	 * Retrieves a certain amount of pictures for a given list of cams.
	 * 
	 * Args:
	 * @param cams String containing cam name requested - this will be mapped via strings.xml
	 * @param num Integer amount of pictures to retrieve
	 * 
	 * Returns:
	 * - ArrayList<Drawable>: Array of Drawable entities containing loaded images
	 */
	ArrayList<Drawable> fetch_pics(String cam, int num) {
		try 
		{
			for (int i=0; i < num; i++) {
				URL im = new URL(base_address + cam + "/" + i);
				InputStream is = (InputStream)im.getContent();
				pics.add(Drawable.createFromStream(is, cam));
			}
		} catch (Throwable t) {
			android.util.Log.e("Problem", "Exception fetching data", t);	
		}
		Collections.reverse(pics);
		return pics;
	}
	Drawable fetch_pic(String cam, int choice)
	{
		Drawable recievedPic = null;
		try 
		{
				URL im = new URL(base_address + cam + "/" + choice);
				InputStream is = (InputStream)im.getContent();
				recievedPic = Drawable.createFromStream(is, cam);
		} 
		catch (Throwable t) 
		{
			android.util.Log.e("Problem", "Exception fetching data", t);	
		}
		return recievedPic;
	}
	URL makeURL(String cam, int choice)
	{
		URL im = null;
		try
		{
			im = new URL(base_address + cam + "/" + choice);
		} 
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return im;
	}
}


