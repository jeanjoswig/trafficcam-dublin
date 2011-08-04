package org.gtug.trafficcam;

public class SynchronisedCounter
{
	    private int c = 0;
	    public synchronized void restore()
	    {
	    	c = 0;
	    }
	    
	    public synchronized void setTo(int setTo)
	    {
	    	c = setTo;
	    }
	    public synchronized void increment()
	    {
	        c++;
	    }

	    public synchronized void decrement()
	    {
	        c--;
	    }

	    public synchronized int value() {
	        return c;
	    }
}