package com.geocloud.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class KtimaDownload extends AsyncTask<String, Void, Long> {
	MyApp appState;
	GPS_Location activity;
	String myfilename;
	double drawx, drawy;
	double l;
	
	public KtimaDownload(GPS_Location loc, MyApp appState){
		
		this.appState = appState;
		this.activity = loc;
		
		
	}
	
	
    protected Long doInBackground(String... param) {
    	double f = Double.parseDouble(param[0]);
    	double l = Double.parseDouble(param[1]);
    	this.drawx=Double.parseDouble(param[4]);
    	this.drawy=Double.parseDouble(param[5]);
    	//this.appState.alert("3434");;
    	String myurl = param[2];
    	//String filename = param[3];
    	//String filename = String.valueOf(String.format("/sdcard/myfoldertest/%s.jpg",(String) param[3]));
    	String filename = String.valueOf(String.format(param[6] + "/geoCloudCache/%s.jpg",(String) param[3]));
         this.myfilename = filename;
    	//this.activity.infoDisplay.setText(filename);; 
		
       
    	
        //new java.io.File(filename).delete();
    	if(new java.io.File(filename).exists() ){
    		//this.activity.infoDisplay.setText(filename + "\n" + "exists");; 
    		
    	}else{
    		//this.activity.infoDisplay.setText(filename + "\n" + "not exists");; 
    		
    		
    		 File file = new File(filename);

		      URL url;
				try {
					url = new URL( myurl);
				
					HttpURLConnection con = (HttpURLConnection)url.openConnection();

		            con.setRequestMethod("GET");
		            con.setDoOutput(true);
		            con.connect();

		            InputStream is = con.getInputStream();
		            FileOutputStream fos = new FileOutputStream(file);
		            BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		            byte[] data =new byte[1024];
		            int x = 0;

		            while((x=is.read(data,0,1024))>=0){
		                bout.write(data,0,x);
		            }
		            fos.flush();
		            bout.flush();
		            fos.close();
		            bout.close();
		            is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//this.activity.infoDisplay.setText(filename);;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//this.activity.infoDisplay.setText(filename);;
				} 
    		
    	}
    	//this.appState.alert("3434");;
    	//this.activity.infoDisplay.setText(filename);;
        //Do some work
        return (long) 5.0;
    }
    
    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }
    
    protected void onPostExecute(Long result/*Void param*/) {
       this.activity.setImage(this.myfilename,this.drawx,this.drawy);
    	//Print Toast or open dialog
    	//File imagefile = new File(this.myfilename);
    	//FileInputStream fis = null;
    	//try {
    		//fis = new FileInputStream(file);
    		//fis = new FileInputStream(imagefile);
        	   // } catch (FileNotFoundException e) {
    	    	//this.msg.setText(e.toString());
    	//}
    	//Bitmap myBitmap;
    	//myBitmap = BitmapFactory.decodeStream(fis);
    	
        //infoDisplay.setText("Downloading Completed");
        //this.activity.ktima_image.x=(double) this.drawx;
        //this.activity.ktima_image.y=(double)this.drawy;
        //this.activity.ktima_image.filename=this.myfilename;
        //sleep(3000);
        
        //this.activity.ktima_image.setImageBitmap(myBitmap);
        
    		
    }
}