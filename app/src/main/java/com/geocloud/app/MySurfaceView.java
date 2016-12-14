package com.geocloud.app;

import com.geocloud.surfaceView.MyApp_S;
import com.geocloud.surfaceView.MySurfaceViewSurface;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MySurfaceView extends Activity{
	
	public MyApp_S app;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = new MyApp_S(this.getBaseContext(),this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new MySurfaceViewSurface(this,app));
    }
	
}