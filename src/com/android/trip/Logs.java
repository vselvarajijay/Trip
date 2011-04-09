package com.android.trip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Logs extends Activity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.logs);
	        	      
	        
	    }
	 
	 
	 public void goBack(View view) {
         Intent intent = new Intent();
         setResult(RESULT_OK, intent);

	     this.finish();
	 }
	 
	 

}
