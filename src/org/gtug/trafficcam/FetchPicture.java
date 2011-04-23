/** Library to fetch current traffic pictures of Dublin.
 * 
 */
package org.gtug.trafficcam;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;


/**
 * @author joswigjn
 * 
 * Utility class to retrieve traffic pictures.
 * 
 */

public class FetchPicture {	
	private HttpClient client;
	private ArrayList<String> dirs = new ArrayList<String>();
	private ArrayList<Drawable> pics = new ArrayList<Drawable>();		
	private String base_address = "http://traffic.savina.net/webcam/";
	
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
		final 
		HttpGet getMethod = new HttpGet(base_address);		
		
		try 
		{
			client = new DefaultHttpClient();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(getMethod, responseHandler);
			dirs = parse_dirs(responseBody);			
			
			for (int i=0; i < num; i++) {
				URL im = new URL(base_address + dirs.get(i) + cam);
				InputStream is = (InputStream)im.getContent();
				pics.add(Drawable.createFromStream(is, cam));
								
			}
		} catch (Throwable t) {
			android.util.Log.e("Problem", "Exception fetching data", t);	
			dirs.add("Error");
		}
		Collections.reverse(pics);
		return pics;
	}
	
	/*
	 * Parse directory names from Raphael's server and return list sorted by date.
	 * 
	 * Recent date is top.
	 * 
	 * Expects:
	 * @param body String containing html directory listing
	 * @return ArrayList<String> with sorted directory names
	 */
	ArrayList<String> parse_dirs(String body) {
		ArrayList<String> st = new ArrayList<String>(); 
		Pattern p = Pattern.compile("[0-9]+/");
		Matcher m = p.matcher(body);		
		while(m.find()) {
			st.add(m.group());
		}		
		Collections.reverse(st);
		return st;
	}
}
