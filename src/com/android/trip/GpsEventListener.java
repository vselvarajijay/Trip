package com.android.trip;

import java.util.EventListener;

public interface GpsEventListener extends EventListener{

	public void distanceChanged(float distanceChange);
	public void satelliteFix(int count);
	
}
