package com.geocloud.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.widget.ImageView;

public class MyApp extends Application {

	private String g_5000="";
	public String baseDir="";
	  private ImageView ktima_view;
	  public GPS_Location GPS_Location;
	  private AlertDialog.Builder dlgAlert;
	  public String getg_5000(){
	    return g_5000;
	  }
	  public void setg_5000(String s){
		  g_5000 = s;
	  }
	  
	  
	  public void setKtimaView(ImageView ktima_view){
		   this.ktima_view=ktima_view;
		  }
	  public void setGPS_Location(GPS_Location myGPS_Location){
		   dlgAlert  = new AlertDialog.Builder( myGPS_Location);
		   dlgAlert.setMessage("-");
			dlgAlert.setTitle("App Title");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
	
		   this.GPS_Location=myGPS_Location;
		  }
	  
	  public void alert(String msg){
		 // dlgAlert  = new AlertDialog.Builder( MyApp.this);
		  // dlgAlert.setMessage("-");
			//dlgAlert.setTitle("App Title");
			//dlgAlert.setPositiveButton("OK", null);
			//dlgAlert.setCancelable(true);
		  dlgAlert.setMessage( msg);
			dlgAlert.create().show();
	  }
		
}