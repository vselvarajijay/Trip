package com.android.trip;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSProvider implements LocationListener, GpsStatus.Listener {

	private EventListener listener;
	
	private LocationManager locationManager;
	
	private Location previousLocation;
	private Location currentLocation;
	
	private Iterable<GpsSatellite> visibleSatellites;
	
	
	public GPSProvider(LocationManager locationManager)
	{
		
		this.locationManager = locationManager;
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
		locationManager.addGpsStatusListener(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		previousLocation = currentLocation;
		currentLocation = location;		
		
		if(previousLocation!=null)
			((GpsEventListener)listener).distanceChanged(currentLocation.distanceTo(previousLocation));		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		int count = 0;
		switch(event)
		{
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					((GpsEventListener)listener).satelliteFix(-1);
				break;		
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					GpsStatus status = locationManager.getGpsStatus(null);
					visibleSatellites = status.getSatellites();					
					for(GpsSatellite sat : visibleSatellites)
					{						
						count++;
					}
					((GpsEventListener)listener).satelliteFix(count);
				break;
		}		
	}
	
	public void setEventListener(GpsEventListener listener)
	{
		this.listener = listener;
	}
	
}
