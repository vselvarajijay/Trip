package com.android.trip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Trip extends Activity implements GpsEventListener, OnTouchListener {
	
	public enum metric {km, miles, meter,feet}
	
	private TextView currentSpeed; 
	private TextView averageSpeed;
	private TextView totalDistance;
	private TextView totalTime;
	private TextView totalSatellites;
	
	
	private metric currentMetric;
	
	private GPSProvider gpsProvider;
	
	
	private float distance;
	private long time;
	private long lastStartTime;
	private int satellitesUsed;
	
	private Boolean paused;
	
	
	
	
	private Handler mHandler = new Handler();
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
		       final long start = lastStartTime;
		       long millis = SystemClock.uptimeMillis() - start;
		       int seconds = (int) (millis / 1000);
		       int minutes = (int) seconds / 60;
		       int hours = (int) minutes / 60;
		       
		       
		       seconds     = seconds % 60;
		       minutes = minutes % 60;

		       if (seconds < 10) {
		           totalTime.setText(hours + ":" + minutes + ":0" + seconds);
		       } else {
		           totalTime.setText(hours + ":" + minutes + ":" + seconds);            
		       }
		     
		       mHandler.postAtTime(this,
		    		   SystemClock.uptimeMillis() + 1000);
		       
		       
		   }
		};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        currentSpeed = (TextView)findViewById(R.id.speed);
        averageSpeed = (TextView)findViewById(R.id.averageSpeed);
        totalDistance = (TextView)findViewById(R.id.totalDistance);
        totalTime = (TextView)findViewById(R.id.totalTime);
        totalSatellites = (TextView)findViewById(R.id.totalSatellites);
        
        
        
        //keep the UI screen ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
              
        gpsProvider = new GPSProvider((LocationManager)getSystemService(Context.LOCATION_SERVICE));              
        gpsProvider.setEventListener(this);
        
                
        distance = 0;        
        satellitesUsed =0;        
        
        setMetric(metric.km);        
        setTotalDistance(0);
        setCurrentSpeed(0,1);
        setAverageSpeed(0);
        setTotalTime(0);
        
        paused = true;
        
                       
        View topLayout = this.findViewById(R.id.mainLayout);
        topLayout.setOnTouchListener(this);    
        
         
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.reset:     
        	resetTrip();
            return true;   
        case R.id.logs:        	        
            Intent myIntent = new Intent(this, Logs.class);
            startActivityForResult(myIntent, 0);           
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


	@Override
	public void distanceChanged(float distanceChange) {
		if(isPaused()) return;		
		distance += distanceChange;				
		setTotalDistance(distance);
			
	}

	
	private void setMetric(metric newMetric)
	{
		currentMetric = newMetric;
	}
	private void setTotalDistance(float distance)
	{
		Float distanceConverted = convertToMetric(distance);		
		totalDistance.setText(distanceConverted.toString());
	}
	private void setCurrentSpeed(float meters, float seconds)
	{
		float hours = (seconds/60f)/60f;
				
		Float speedConverted = convertToMetric(meters/hours);
		currentSpeed.setText(speedConverted.toString());
	}
	private void setAverageSpeed(float speed)
	{
		Float speedConverted = convertToMetric(speed);
		averageSpeed.setText(speedConverted.toString());
	}
	
	private void setTotalTime(float seconds)
	{		
		
		totalTime.setText(((Float)seconds).toString()); 
	}
	
	private float convertToMetric(float meter)
	{
		float returnVal = -1;
		switch(currentMetric){
		case km:
			returnVal = meter*(float).001;
			break;
		case miles:
			returnVal = meter*(float)0.000621371192;
			break;			
		case meter:
			returnVal = meter;
			break;		
		case feet:
			returnVal = meter *(float)3.2808399;
			break;
		}
		
		return returnVal;
	}


	@Override
	public void satelliteFix(int count) {
		
		satellitesUsed = count;
		totalSatellites.setText(((Integer)satellitesUsed).toString());		
	}
	
	private Boolean isPaused()
	{
		return paused;
	}
	
	
	private void startTrip()
	{	
		paused = false;
		notifyUser("starting");
		
		lastStartTime = SystemClock.uptimeMillis();
				
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
        	
	}
	private void pauseTrip()
	{
		paused = true;		
		
		mHandler.removeCallbacks(mUpdateTimeTask);
		
		notifyUser("paused");
	}
	
	 
	private void resetTrip()
	{
		 distance = 0;
	     time = 0;
	     satellitesUsed =0;
	        
	     setMetric(metric.km);        
	     setTotalDistance(0);
	     setCurrentSpeed(0,1);
	     setAverageSpeed(0);
	     setTotalTime(0);
	     
	     lastStartTime = 0;
	     paused = true;
	     
	     notifyUser("trip reset");
	}
	
	private void notifyUser(String msg)
	{
		LayoutInflater inflater =  getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup)findViewById(R.id.toast_layout_root));
		
		TextView text = (TextView)layout.findViewById(R.id.text);
		text.setText(msg);
		
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL,0,-125);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		
		switch(action)
		{
			case MotionEvent.ACTION_UP:				
			break;
			case MotionEvent.ACTION_DOWN:	
				if(!isPaused()) pauseTrip();
				else startTrip();
			break;				
		}
			 
		return true;
	}
	
    
    
}