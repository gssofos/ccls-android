package com.geocloud.app;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.hardware.Camera;

 
public class UploadImage  {
    InputStream inputStream;
       
    public  UploadImage(Bitmap bitmap,Camera cam,String coorstring, String userName) {
            //super.onCreate();
            //setContentView(R.layout.main);
 
           // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           // String stream = "";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            
            String image_str = Base64.encodeBytes(byte_arr);
            final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
 
            nameValuePairs.add(new BasicNameValuePair("image",image_str));
            
            Calendar c = Calendar.getInstance(); 
            Date dat = c.getTime();
            String datestring = "";
            datestring+=dat.getDay() + "-";
            datestring+=dat.getMonth() + "_";
            datestring+=dat.getHours() + ":";
            datestring+=dat.getMinutes() + ":";
            datestring+=dat.getSeconds() ;
            nameValuePairs.add(new BasicNameValuePair("mydate",datestring + "cc" + coorstring));
            nameValuePairs.add(new BasicNameValuePair("mycoor",coorstring.replace(",","_")));
             nameValuePairs.add(new BasicNameValuePair("user",userName));
             // Log.v("48", datestring);
             Thread t = new Thread(new Runnable() {
             
           
            public void run() {
                  try{
                         HttpClient httpclient = new DefaultHttpClient();
                         HttpPost httppost = new HttpPost("http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/upload.php");
                         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                         HttpResponse response = httpclient.execute(httppost);
                         String the_string_response = convertResponseToString(response);
                        
                        // Log.v("54", the_string_response);
                         
                     }catch(Exception e){
                    	// Log.v("57", e.toString());
                     }  
            }
        });
             cam.startPreview();
         t.start();
        }
 
        public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
 
             String res = "";
             StringBuffer buffer = new StringBuffer();
             inputStream = response.getEntity().getContent();
             int contentLength = (int) response.getEntity().getContentLength(); //getting content length..
             //Log.v("74", "contentLength : " + contentLength);  
             //Log.v("74", "contentLength : " + inputStream.read());  
             if (contentLength < 0){
             }
             else{
                    byte[] data = new byte[512];
                    int len = 0;
                    try
                    {
                        while (-1 != (len = inputStream.read(data)) )
                        {
                            buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer..
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        inputStream.close(); // closing the stream..
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    res = buffer.toString();     // converting stringbuffer to string..
 
                   // Log.v("102",  "Result : " + res);
                   //.v("102",  "Result : " + EntityUtils.toString(response.getEntity()));
                  //  System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
             }
             
             
             return res;
        }
}