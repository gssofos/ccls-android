package com.geocloud.modules;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraService extends Service
{
      //Camera variables
      //a surface holder
      private SurfaceHolder sHolder; 
      //a variable to control the camera
      private Camera mCamera;
      //the camera parameters
      private Parameters parameters;
      /** Called when the activity is first created. */
    @Override
    public void onCreate()
    {
        super.onCreate();
         
    }
    @Override
    public void onStart(Intent intent, int startId) {
      // TODO Auto-generated method stub
      super.onStart(intent, startId);
     Log.i("SdsdsdS","sdsdsd");
       mCamera = Camera.open();
       SurfaceView sv = new SurfaceView(getApplicationContext());
   

       try {
                  mCamera.setPreviewDisplay(sv.getHolder());
                  parameters = mCamera.getParameters();
                   
                   //set camera parameters
                 mCamera.setParameters(parameters);
                 mCamera.startPreview();
                 mCamera.takePicture(null, null, mCall);
            
            } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
            }
       
       
       //Get a surface
         sHolder = sv.getHolder();
        //tells Android that this surface will have its data constantly replaced
         sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

     
     
    Camera.PictureCallback mCall = new Camera.PictureCallback()
    {
  
       public void onPictureTaken(byte[] data, Camera camera)
       {
             //decode the data obtained by the camera into a Bitmap
     
             FileOutputStream outStream = null;
                  try{
                      outStream = new FileOutputStream("/sdcard/Image.jpg");
                      outStream.write(data);
                      outStream.close();
                      
                      Log.d("CAMERA","done");
                  } catch (FileNotFoundException e){
                      Log.d("CAMERA", e.getMessage());
                  } catch (IOException e){
                      Log.d("CAMERA", e.getMessage());
                  }
     
       }
    };


      @Override
      public IBinder onBind(Intent intent) {
            // TODO Auto-generated method stub
            return null;
      }
}