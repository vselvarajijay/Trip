package com.android.trip;

import java.util.EventObject;



//Declare the event. It must extend EventObject.
public class GpsEvent extends EventObject {
	
	public enum Event{DistanceChanged}
	
    public GpsEvent(Object source) {
        super(source);
    }
}
