package com.geocloud.surfaceView;

import com.geocloud.app.MySurfaceView;
import com.geocloud.wms.Ktima;

import android.app.Application;
import android.content.Context;

public class MyApp_S extends Application {

		public String 			baseDir="";
		public Ktima 			ktima;
		public MySurfaceView 	head;
		public Context 			context;
		
	 public  MyApp_S(Context context, MySurfaceView head){
		 ktima 			= new Ktima(context);
		 this.context 	= context;
		 this.head 		= head;
	 }
	  
		
}