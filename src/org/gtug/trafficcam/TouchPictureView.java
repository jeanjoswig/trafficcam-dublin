package org.gtug.trafficcam;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/*Code taken from example code in Hello, Android Third Edition, Chapter 11.
 */

public class TouchPictureView extends Activity implements OnTouchListener {
	   private static final String TAG = "TouchPictureView";


	   // These matrices will be used to move and zoom image
	   Matrix matrix = new Matrix();
	   Matrix savedMatrix = new Matrix();
	   
	   // We can be in one of these 3 states
	   static final int NONE = 0;
	   static final int DRAG = 1;
	   static final int ZOOM = 2;
	   int mode = NONE;

	   // Remember some things for zooming
	   PointF start = new PointF();
	   PointF mid = new PointF();
	   float oldDist = 1f;
	   

	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.viewimage);
		   String selectedCamera = this.getIntent().getExtras().getString("selectedCamera");
  		  ArrayList<Drawable> pics = new ArrayList<Drawable>();
	      pics = (new FetchPicture()).fetch_pics(selectedCamera, 1); /*use FetchPicture to get the image for that camera*/
	      ImageView view = (ImageView) findViewById(R.id.imageholder);
	      view.setImageDrawable(pics.get(0));
	      view.setOnTouchListener(this);
	      Display display = getWindowManager().getDefaultDisplay(); 
	      float displayHeight = (float) display.getHeight(); //800 on the N1
	      float displayWidth = (float) display.getWidth(); //400 on the N1
	      float viewHeight = (float) pics.get(0).getMinimumHeight();
	      float viewWidth =(float) pics.get(0).getMinimumWidth();
	      float heightRatio = displayHeight/viewHeight;
	      float widthRatio = displayWidth/viewWidth;
	      savedMatrix.set(matrix);
	      if (heightRatio <= widthRatio)
	      {
	    	  matrix.postScale(heightRatio, heightRatio);
	      }
	      else if (widthRatio < heightRatio)
	      {
	    	  matrix.postScale(widthRatio, widthRatio);
	      }
	      view.setImageMatrix(matrix);
	      }
	   /*
	    * This code uses compares the ratio of the images 
	    * dimensions and scales it to whichever value will 
	    * keep it from over running the screen.
	    * This does not work if the phone is held in portrait.
	    */

	   @Override
	   public boolean onTouch(View v, MotionEvent event) 
	   {
	      ImageView view = (ImageView) v;

	      /* Dump touch event to log
	      dumpEvent(event);*/

	      // Handle touch events here...
	      switch (event.getAction() & MotionEvent.ACTION_MASK) 
	      {
	      case MotionEvent.ACTION_DOWN:
	         savedMatrix.set(matrix);
	         start.set(event.getX(), event.getY());
	         Log.d(TAG, "mode=DRAG");
	         mode = DRAG;
	         break;
	      case MotionEvent.ACTION_POINTER_DOWN:
	         oldDist = spacing(event);
	         Log.d(TAG, "oldDist=" + oldDist);
	         if (oldDist > 10f)
	         {
	            savedMatrix.set(matrix);
	            midPoint(mid, event);
	            mode = ZOOM;
	            Log.d(TAG, "mode=ZOOM");
	         }//This code ignores input which appears to be from two fingers at approximately the same location.
	         break;
	      case MotionEvent.ACTION_UP:
	      case MotionEvent.ACTION_POINTER_UP:
	         mode = NONE;
	         Log.d(TAG, "mode=NONE");
	         break;
	      case MotionEvent.ACTION_MOVE:
	         if (mode == DRAG) 
	         {
	            // ...
	            matrix.set(savedMatrix);
	            matrix.postTranslate(event.getX() - start.x,
	                  event.getY() - start.y);
	         }
	         else if (mode == ZOOM) 
	         {
	            float newDist = spacing(event);
	            Log.d(TAG, "newDist=" + newDist);
	            if (newDist > 10f) 
	            {
	               matrix.set(savedMatrix);
	               float scale = newDist / oldDist;
	               matrix.postScale(scale, scale, mid.x, mid.y);
	            }
	         }
	         break;
	      }

	      view.setImageMatrix(matrix);
	      return true; // indicate event was handled
	   }
	   /** Determine the space between the first two fingers */
	   private float spacing(MotionEvent event)
	   {
	      float x = event.getX(0) - event.getX(1);
	      float y = event.getY(0) - event.getY(1);
	      return FloatMath.sqrt(x * x + y * y);
	   }

	   /** Calculate the mid point of the first two fingers */
	   private void midPoint(PointF point, MotionEvent event) 
	   {
	      float x = event.getX(0) + event.getX(1);
	      float y = event.getY(0) + event.getY(1);
	      point.set(x / 2, y / 2);
	   }
}
