package com.geocloud.app;

import com.example.test2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle testName) {
		// TODO Auto-generated method stub
		super.onCreate(testName);
		
		setContentView(R.layout.splash);
		
		Thread timer=new Thread(){
			public void run(){
				try{
					sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint = new Intent("com.example.test2.MENU");
					
					
					
					startActivity(openStartingPoint);
				}
			}
		
		};
		timer.start();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	

}
