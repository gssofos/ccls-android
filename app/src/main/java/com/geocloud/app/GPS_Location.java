package com.geocloud.app;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;








import com.example.test2.R;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;



// CLASS gia sxediasi se foto///////////////////////////////////////
///////////////////////////////////////////////////////////////////
class DrawOnTop extends ImageView { 	
	private Paint paint;
	public double x;
	public double y;
	public String filename;
	
    public DrawOnTop(Context context, AttributeSet attrs) { 
    	super(context, attrs);
    	init();
    }

    public void init(){
        paint = new Paint();
        paint.setTextSize(12);
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.FILL); 
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	float x0 = (float) x*super.getWidth();
    	float y0 = (float) (1-y)*super.getHeight();
    	paint.setStrokeWidth(3);
        canvas.drawLine(x0-15, y0+15, x0+15,y0-15, paint);
        canvas.drawLine(x0+15, y0+15, x0-15,y0-15, paint);
    }
}
//CLASS gia sxediasi se foto///////////////////////////////////////
///////////////////////////////////////////////////////////////////




//CLASS gia preview foto kai doudble click///////////////////////////////////////
///////////////////////////////////////////////////////////////////
class PhotoSurfaceView extends SurfaceView { 	
	public String path;
	public Camera cam;
	private Context cont;
	public GPS_Location act;
	 private static final int MAX_CLICK_DURATION = 250;
	    private long startClickTime;
	    String upLoadServerUri = null;
	    int serverResponseCode = 0;
    public PhotoSurfaceView(Context context, AttributeSet attrs) { 
    	super(context, attrs);
    	cont = context;
    	startClickTime = Calendar.getInstance().getTimeInMillis();
    	init();
    }


    ShutterCallback myShutterCallback = new ShutterCallback(){

    	 @Override
    	 public void onShutter() {
    	  // TODO Auto-generated method stub
    	 }};
    	 
    	 
    PictureCallback myPictureCallback_RAW = new PictureCallback(){

    	 @Override
    	 public void onPictureTaken(byte[] arg0, Camera arg1) {
    	  // TODO Auto-generated method stub
    	 
    	 }};
    	 
   PictureCallback myPictureCallback_JPG = new PictureCallback(){

    		 @Override
    		 public void onPictureTaken(byte[] arg0, Camera arg1) {
    		  // TODO Auto-generated method stub
    		  final Bitmap bitmapPicture
    		   = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
    		  
    		
    		    
    		    
    		  myThreadWithApp timer=new myThreadWithApp(act){
    				public void run(){
    				
    					try {
    					       FileOutputStream out = new FileOutputStream(path + "/000.jpg");
    					       bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 90, out);
    					       out.close();
    					       //UploadImage tt = 
    					    		   new UploadImage(bitmapPicture,cam,act.lon + "," + act.lat,act.userName);
    					} catch (Exception e) {
    						//Toast.makeText(cont,e.toString(), Toast.LENGTH_SHORT).show();
    						e.printStackTrace();
    					}
    				}
    			};
    			timer.start();
    		 }};
    		
    		 public String BitMapToString(Bitmap bitmap) {
    			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    			    byte[] b = baos.toByteArray();
    			    String strBitMap = Base64.encodeToString(b, Base64.DEFAULT);
    			    return strBitMap;
    			}
    		 
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		 long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
    	 startClickTime = Calendar.getInstance().getTimeInMillis();
    	 
    	 if(clickDuration < MAX_CLICK_DURATION) {
             //click event has occurred
    		 try{
    			
    			 cam.takePicture(myShutterCallback,  myPictureCallback_RAW, myPictureCallback_JPG);
    		// cam.takePicture(shutter, raw, jpeg);
    		 }catch(Exception e){
    			 Toast.makeText(cont,e.toString(), Toast.LENGTH_SHORT).show();
    	    		 
    		 }
         }else{
        	 
        	 cam.startPreview();
         }
		return super.onTouchEvent(event);
	}

	public void init(){
       upLoadServerUri = "http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/upload.php";
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    }
}


//CLASS gia preview foto kai doudble click///////////////////////////////////////
///////////////////////////////////////////////////////////////////









//CLASSES gia custom THREAD///////////////////////////////////////
///////////////////////////////////////////////////////////////////
 class myThread extends Thread{

	 double x,y;
	    public myThread(double x, double y) {
	        this.x=x;
	        this.y=y;
	    }
	    @Override
	    public void run() {
	        // your stuff
	    }
}
 
 
 

 class myThreadWithApp extends Thread{

	 GPS_Location act;
	    public myThreadWithApp(GPS_Location act) {
	        this.act=act;
	    }
	    @Override
	    public void run() {
	        // your stuff
	    }
}
 
 

 class myThreadWithCont extends Thread{

	 Context mycont;
	    public myThreadWithCont(Context cont) {
	        this.mycont=cont;
	    }
	    @Override
	    public void run() {
	        // your stuff
	    }
}

//CLASSES gia custom THREAD///////////////////////////////////////
///////////////////////////////////////////////////////////////////
 

 
 
 
 
 
 
 
 
 
 

public class GPS_Location extends Activity implements LocationListener, SurfaceHolder.Callback {
	boolean debug = false;
	
	EditText useret;	//user tEditText apo dialog
	EditText passet;	// pass omoios
	String baseDir;
	Timer gpsEmuTimer;  //Gps EMu timer;
	Timer partnerTimer;  //Gps EMu timer;
	LocationManager locationManager ;
	LinearLayout map_layout;
	LinearLayout photo_layout;
	TextView infoDisplay;
	TextView debugDisplay;
	TextView webLogInfoDisplay;
	SurfaceHolder photoSurfaceHolder;
	PhotoSurfaceView photoSurfaceView;
	Camera cam;
	ToggleButton ktima_button;
	ToggleButton web_log_button;
	public DrawOnTop ktima_image;
	Spinner datum_list;
	ToggleButton snap_photo_button;
	MyApp appState;
	int web_counter=0;
	double lon,lat;
	double xeg,yeg;
	boolean hatt_got = false;
	hatt_object current_hatt;
	public String current_5000_id;
	String MyIMEI;
	String MyMac;
	String userName;
	String passCode;
	public hatt_object[] hatt_array = 	 new hatt_object[390];
	Datum egsa = new Datum();;
	Vibrator v;
	TelephonyManager telephonyManager ;
	double lastemuf=38.13;
	double lastemul=23.64;
	int streamParent=0; //0 = GPS, 1 Partner
	Time today;
	int yearDay;
	String partnerName="";
	String partnerPass="";
	
	
	private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){
	    	public void onAutoFocus(boolean autoFocusSuccess, Camera arg1) { 
	 }};

	    
	    
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appState = ((MyApp)getApplicationContext());
    
    Thread hatt_timer=new Thread(){
		public void run(){	try{read_hatt();	}catch(Exception e){}finally{	}	}
	};hatt_timer.start();
	
	
    
    setContentView(R.layout.activity_gps_location);
    
    infoDisplay 		= (TextView) 		findViewById(R.id.infoDisplay);
    debugDisplay 		= (TextView) 		findViewById(R.id.debugDisplay);
    webLogInfoDisplay 	= (TextView) 		findViewById(R.id.webLogInfoDisplay);
   
    photoSurfaceView 	= (PhotoSurfaceView) 		findViewById(R.id.photoSurfaceView);
    photoSurfaceView.act = GPS_Location.this;
    // photoSurfaceView.setZOrderOnTop(true);    // necessary
    photoSurfaceHolder 	= (SurfaceHolder) photoSurfaceView.getHolder();
    photoSurfaceHolder.addCallback(GPS_Location.this);
    photoSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
   // photoSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    
    ktima_image 		= (DrawOnTop) 		findViewById(R.id.ktima_image);
    map_layout 			= (LinearLayout) 	findViewById(R.id.map_layout);
    photo_layout 		= (LinearLayout) 	findViewById(R.id.photo_layout);
	ktima_button 		= (ToggleButton) 	findViewById(R.id.ktima_button);
	web_log_button 		= (ToggleButton) 	findViewById(R.id.web_log_button);
	snap_photo_button 	= (ToggleButton) 	findViewById(R.id.snap_photo_button);

    datum_list 			= (Spinner) 		findViewById(R.id.datumList);
    v 					= (Vibrator) 		getSystemService(Context.VIBRATOR_SERVICE);
   
    Display display = getWindowManager().getDefaultDisplay();
	//final int width = (display.getWidth());
	current_5000_id="";
	
    today = new Time(Time.getCurrentTimezone());
	today.setToNow();
	yearDay = today.yearDay;
    
	appState.setKtimaView(ktima_image);
	appState.setGPS_Location(GPS_Location.this);
	
	
	final SharedPreferences myPrefs = getSharedPreferences("gims", MODE_PRIVATE);	final SharedPreferences.Editor prefsEditor; prefsEditor = myPrefs.edit(); 
	userName = myPrefs.getString("userName", "");
		if(userName.length()<2){
			try {
				//appState.alert("fgfg");
				URL url = new URL( "http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/get_free_id.php");
				BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));
				String inputLine;inputLine = in.readLine();	in.close();
				prefsEditor.putString("userName", inputLine);  
				prefsEditor.commit();
				userName=inputLine;
				passCode = "" + get_pass_from_int(Integer.parseInt(userName));
			} catch (Exception e) {	
				
				userName="";
			} 
		}else{
			passCode = "" + get_pass_from_int(Integer.parseInt(userName));
		}
		webLogInfoDisplay.setText("   user : " + userName + "\n" + "   pass : " + passCode);
		
	
	
	final int mywidth = (display.getWidth());
	map_layout.getLayoutParams().height = 2;
	photo_layout.getLayoutParams().height = 2;
	
    /*
	cam = Camera.open();
	photoSurfaceView.cam=cam;
	Camera.Parameters parameters = cam.getParameters();
	//Camera.Parameters parameters = camera.getParameters();
    //parameters.setPictureFormat(PixelFormat.JPEG); 
    // parameters.set("orientation", "portrait");
    //parameters.setRotation(90);
	//parameters.set("orientation", "portrait"); 
	//parameters.set("rotation", 90);
	cam.setDisplayOrientation(90);
	//cam.set
	parameters.setPreviewSize(1280, 720);
	parameters.setPictureSize(1280,720);
	//if (parameters.getFocusMode().equals(parameters.FOCUS_MODE_AUTO) ||
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//{
	    cam.autoFocus(myAutoFocusCallback);
	//parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
	//parameters.setPreviewFrameRate(20);
	cam.setParameters(parameters);
	//cam.autoFocus(null);
	//cam.release();
	*/
	
	web_log_button.setOnCheckedChangeListener(  new OnCheckedChangeListener() {
	    
		public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
				
            if(isChecked){
            	if(userName==""){
        			try {
        				URL url = new URL( "http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/get_free_id.php");
        				BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));
        				String inputLine;inputLine = in.readLine();	in.close();
        				prefsEditor.putString("userName", inputLine);  
        				prefsEditor.commit();
        				userName=inputLine;
        				//webLogInfoDisplay.setText("   user : " + userName + "\n" + "   pass : " + passCode);

        				
        				if(debug){
    	            		try{gpsEmuTimer.cancel();}catch(Exception e){	}
    	            		startGPSEmu();
    	            	}else{
    	            		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPS_Location.this);
    	            	}
        				
        				
        			} catch (Exception e) {	
        				web_log_button.setChecked(false);
        			} 
        		
	            	
            	}else{
            		if(debug){
	            		try{gpsEmuTimer.cancel();}catch(Exception e){	}
	            		startGPSEmu();
	            	}else{
	            		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPS_Location.this);
	            	}
        		}
            	
            }else{
            	if(streamParent==1){
            		if(debug){gpsEmuTimer.cancel();
            		
            		}else{
            			locationManager.removeUpdates( (LocationListener) GPS_Location.this);
            		}
            		
            	}
            }
        }
	});
	
	
	
	ktima_button.setOnCheckedChangeListener(  new OnCheckedChangeListener() {
	    
		public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
				
            if(isChecked){
            	snap_photo_button.setChecked(false);
            	Display display = getWindowManager().getDefaultDisplay();
            	
            	final int width = (display.getWidth());
            	map_layout.getLayoutParams().height = width;
            }else{
            	map_layout.getLayoutParams().height = 2;
            }
        }
	});
	
	
	snap_photo_button.setOnCheckedChangeListener(  new OnCheckedChangeListener() {
	    
		public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
				
            if(isChecked){
            	
            	URL url;
				try {
					url = new URL( "http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/get_photo_enabled.php?user=" + userName);
					//Toast.makeText(GPS_Location.this,url.toString(), Toast.LENGTH_SHORT).show();
					BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));
					String inputLine;inputLine = in.readLine();	in.close();
					//Toast.makeText(GPS_Location.this,inputLine, Toast.LENGTH_SHORT).show();
					if(Integer.parseInt(inputLine.split("#")[0])==1)	{
						
						
						try {
		            		  ktima_button.setChecked(false);
		            		 // photoSurfaceView.getLayoutParams().height = mywidth*4/3;
		            		  photo_layout.getLayoutParams().height = mywidth*4/3;
		        	        	cam.setPreviewDisplay(photoSurfaceHolder);
		      	        	cam.startPreview();
		      	        	//Camera.Parameters parameters = cam.getParameters();
		      	   
		      	    	} catch (IOException e1) {
		      	    		
		      	    	}
						
					}else{
						Toast.makeText(GPS_Location.this, inputLine.split("#")[1], Toast.LENGTH_SHORT).show();
						snap_photo_button.setChecked(false);
					}
					
				} catch (Exception e) {
					Toast.makeText(GPS_Location.this, "Photo mode error", Toast.LENGTH_SHORT).show();
					//Log.v("ss",e.toString());
					snap_photo_button.setChecked(false);
				}
				
            	
            	
            	  
            }else{
            	//photoSurfaceView.getLayoutParams().height = 2;
            	photo_layout.getLayoutParams().height = 2;
            	cam.stopPreview();
            	
            	
            	
   	        	
            }
        }
	});
	
	

	

  
   
   if(Environment.getExternalStorageDirectory().exists()){
	   baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();   
   }else{
	   baseDir = "/mnt";
   }
   //debugDisplay.setText(baseDir);
  	//appState.baseDir=baseDir;
   //Emu///////////
  	boolean success = (new File(baseDir + "/geoCloudCache")).mkdir(); 
  	//boolean success = (new File(baseDir + "/myfoldertest")).mkdir(); 
  	 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
  	
  	 streamParent=0;
  	//debugDisplay.setText("-" + streamParent);
  	if(debug){
  		startGPSEmu();
  	}else{
  		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
  	}
   ////////////////
   
  	
  	photoSurfaceView.path = baseDir + "/geoCloudCache";
  	photoSurfaceView.act = GPS_Location.this;
     
     
     
}





@Override
protected Dialog onCreateDialog(int id) {
 // switch (id) {
   // case DIALOG_ALERT:
    // create out AlterDialog
	LayoutInflater inflator = getLayoutInflater();
	final View layout =  getLayoutInflater().inflate(R.layout.activity_gps_location_bk, (ViewGroup) findViewById(R.layout.activity_gps_location));
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Set Partner data");
    builder.setCancelable(true);
    builder.setPositiveButton("OK", new OkOnClickListener());
    builder.setNegativeButton("Cancel", new CancelOnClickListener());
    builder.setView(layout);
    useret = (EditText) layout.findViewById(R.id.userEditText);
    passet = (EditText) layout.findViewById(R.id.passEditText);
    
    AlertDialog dialog = builder.create();
   // dialog.setContentView(R.layout.activity_gps_location_bk);
    dialog.show();
  // }
  return super.onCreateDialog(id);
}


private final class OkOnClickListener implements
	DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
		try{
			//EditText text1 = ((EditText) 	findViewById(R.id.userEditText));;
			//EditText text2 = ((EditText) 	findViewById(R.id.passEditText));;
			GPS_Location.this.partnerName= useret.getText().toString();
			GPS_Location.this.partnerPass= passet.getText().toString();
			//GPS_Location.this.partnerPass= text2.getText().toString();
			
			//Toast.makeText(GPS_Location.this, "You selected Partner Data log", Toast.LENGTH_SHORT).show();
			
			streamParent=1;
			try{
				partnerTimer.cancel();
			}catch(Exception e){
				
			}
			
			
			
			startPartnerTimer();
			
			if(!web_log_button.isChecked()){	
				if(debug){
					gpsEmuTimer.cancel();
				}else{
					locationManager.removeUpdates( (LocationListener) GPS_Location.this);
				}
				
			}
		}catch(Exception e){
			Toast.makeText(GPS_Location.this, e.toString(), Toast.LENGTH_SHORT).show();
			
		}
		
		
		
	}
} 



private final class CancelOnClickListener implements
	DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
		GPS_Location.this.partnerName="";
		GPS_Location.this.partnerPass="";
		
		//GPS_Location.this.finish();
	}
} 

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	hatt_got=false;
	switch (item.getItemId()) {
	case R.id.action_settings:
		Toast.makeText(this, "You selected Gps Data log", Toast.LENGTH_SHORT).show();
		try{
			
				streamParent=0;
				if(debug){
					try{gpsEmuTimer.cancel();}catch(Exception e){	}
					startGPSEmu();
				}else{
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				}
				
				if(!web_log_button.isChecked()){
					partnerTimer.cancel();
				}
		}catch(Exception e){
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			
		}
		break;
		
		
	case R.id.action_settings2:
		showDialog(1);
		
		break;
	default:
		break;
	}
	return super.onOptionsItemSelected(item);
}


public void startGPSEmu() {
	    final Handler handler = new Handler();
	    gpsEmuTimer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	lastemul = lastemul+ (double) 0.0001;
	                    	if(streamParent==0){
	                    		onLocationChangedFunction(lastemuf,lastemul,false,false);
	                    	}else{
	                    		onLocationChangedFunction(lastemuf,lastemul,true,false);
	                    	}
	                    	//onLocationChangedEmu(lastemuf,lastemul);
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                    }
	                }
	            });
	        }
	    };
	    gpsEmuTimer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
	   
	}






public void startPartnerTimer() {
	    final Handler handler = new Handler();
	    partnerTimer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    		                    	
	                    	try {
	        					URL url = new URL( "http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/get_record.php?user=" + GPS_Location.this.partnerName + "&pass=" + GPS_Location.this.partnerPass + "&me=" + GPS_Location.this.userName);
	        				
	        					BufferedReader in = new BufferedReader(
	        				            new InputStreamReader(
	        				            url.openStream()));

	        				String inputLine;
	        				inputLine = in.readLine();//
	        				in.close();
	        				//Toast.makeText(GPS_Location.this, inputLine.substring(0,3), Toast.LENGTH_SHORT).show();
		        			
		        				if(Integer.parseInt(inputLine.substring(0,3))==127){
		        					Toast.makeText(GPS_Location.this,inputLine.split("#")[1],  (int) 10000 ).show();
		        							        					
		        					
		        					if(debug){
		        						gpsEmuTimer.cancel();
		        					}else{
		        						locationManager.removeUpdates( (LocationListener) GPS_Location.this);
		        					}
		        					
		        					try{
		        						partnerTimer.cancel();
		        					}catch(Exception e){
		        						
		        					}
		        					
		        					
		        					//Thread.sleep(10000);
		        					GPS_Location.this.onStop();
		        					GPS_Location.this.finish();
		        					
		        					
		        					
		        					Class ourClass = Class.forName("com.example.test2.menu");
		        					Intent ourIntent = new Intent(GPS_Location.this,ourClass);
		        					startActivity(ourIntent);
		        					
		        				}else{
		        					String coorstr = inputLine.substring(6,inputLine.length()-1).replace(" ", "#");
		        					String[] coor = coorstr.split("#");
		        					onLocationChangedFunction(Double.parseDouble(coor[1]),Double.parseDouble(coor[0]),false,true);
		        					//Toast.makeText(GPS_Location.this,coor[0].toString(), Toast.LENGTH_SHORT).show();
		        				}
		        				
	        				} catch (MalformedURLException e) {
	        					// TODO Auto-generated catch block
	        					//e.printStackTrace();
	        					//this.activity.infoDisplay.setText(filename);;
	        					Toast.makeText(GPS_Location.this, e.toString(), Toast.LENGTH_SHORT).show();
	        				} catch (IOException e) {
	        					// TODO Auto-generated catch block
	        					//e.printStackTrace();
	        					//this.activity.infoDisplay.setText(filename);;
	        					Toast.makeText(GPS_Location.this, e.toString(), Toast.LENGTH_SHORT).show();
	        				} 
	            		
	                    	
	                    	
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                    }
	                }
	            });
	        }
	    };
	    partnerTimer.schedule(doAsynchronousTask, 1000, 10000); //execute in every 50000 ms
	   
	}


public void setImage(String filename,double drawx, double drawy){
	//debugDisplay.setText(filename);
	File imagefile = new File(filename);
	FileInputStream fis = null;
	try {
		fis = new FileInputStream(imagefile);
    	    } catch (FileNotFoundException e) {
    	    	debugDisplay.setText(e.toString());
	}
	Bitmap myBitmap;
	myBitmap = BitmapFactory.decodeStream(fis);
    ktima_image.x=(double) drawx;
    ktima_image.y=(double) drawy;
    ktima_image.filename=filename;
    ktima_image.setImageBitmap(myBitmap);
}

@Override
public void onLocationChanged(Location location) {
    // TODO Auto-generated method stub


    double latitude = (double) (location.getLatitude());
    double longitude = (double) (location.getLongitude());
    if(streamParent==0){
    	onLocationChangedFunction(latitude,longitude,false,false);
    }else{
    	onLocationChangedFunction(latitude,longitude,true,true);
    }
    
    
    
    
}




public void onLocationChangedFunction(double latitude, double longitude,boolean weblogonly,boolean noweblog){


		
		lon = longitude;
	    lat = latitude;
	    if(!weblogonly){
			    double out[] = egsa.fl2EGSA87(latitude, longitude);
			    xeg=(double) Math.round(out[0]*100)/100;
			    yeg=(double) Math.round(out[1]*100)/100;
			    
				
			    
			    if(hatt_got==false){
			    	current_hatt = fltohatt_object(lat,lon);
			    	Thread get_5000=new myThread(xeg,yeg){
						public void run(){
							try{
								appState.setg_5000(read_g5000(this.x,this.y));
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								//Intent openStartingPoint = new Intent("com.example.test2.MENU");
								//startActivity(openStartingPoint);
							}
						}
					
					};
					get_5000.start();
					hatt_got=true;
			    }
			    
			    if(current_5000_id==""){
			    	current_5000_id= appState.getg_5000();
			    }
			    
			    if (datum_list.getSelectedItemPosition()==0){  		infoDisplay.setText(" lat : " + lat + "\n" + " lon : " + lon + "\n"  ); }
			    
				if (datum_list.getSelectedItemPosition()==1){		infoDisplay.setText(" x : " + (double) Math.round(xeg*100)/100 + "\n" + " y : " + (double) Math.round(yeg*100)/100  + "\n");		}
				
				if (datum_list.getSelectedItemPosition()==2){	 	double ht[] = current_hatt.to_egsa(xeg, yeg);			String tex =   current_hatt.name + " ( " + current_5000_id + " )\n" + " x : " + (double) Math.round(ht[0]*100)/100 + "\n"  + " y : " + (double) Math.round(ht[1]*100)/100;
																	infoDisplay.setText(tex);		}
			  
				if(this.ktima_button.isChecked()){
					  	Object[] url = egsa.fl2string(lat, lon, (int) 17);
					  	String[] data = {"" + lat,"" + lon,(String) url[0],(String) url[1],""+ url[2],""+ url[3],baseDir}; 
					    KtimaDownload kt = new KtimaDownload(GPS_Location.this,appState);
					    try{
					    	 kt.execute(data); 
					    }catch(Exception e){
					    	infoDisplay.setText(e.toString());
					    }
				   }
			    	
	    }
	    
	    
	    if(!noweblog){
	    if(web_log_button.isChecked()){
	        web_counter+=1;
	        if(web_counter>10){
	        	 web_counter=0;
	        try{
	        	//Toast.makeText(this, "add_record.php?user=" + userName + "&pass=" + passCode + "&pos='POINT(" + longitude + "_" + latitude + ")'", Toast.LENGTH_LONG).show();
				
		    		URL yahoo = new URL ("http://www.geosolution.gr/geomap/gims/__gims_m/multitrack/php/add_record.php?user=" + userName + "&pass=" + passCode + "&pos='POINT(" + longitude + "_" + latitude + ")'");
		    		v.vibrate(100);
		    		BufferedReader in = new BufferedReader(
		    				new InputStreamReader(yahoo.openStream()));
		    		
		    		String inputLine;//,outLine;
		    		//outLine="";
		    		//while ((inputLine = in.readLine()) != null){ 		    outLine+=inputLine;  		}
		    		inputLine = in.readLine();
		    		in.close();
		    		
		    		
		    		
		    		if(Integer.parseInt(inputLine.substring(0,3))==127){
    					Toast.makeText(GPS_Location.this,inputLine.split("#")[1],  (int) 10000 ).show();				
    					web_log_button.setChecked(false);
    				}
		    		
		    		
		    		
	    		}catch( MalformedURLException e)	{
	    		}catch (IOException e) 				{
	    		}
	       }
	    }
	    }
	    


	    
	
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
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	//super.finish();
	//locationManager.removeUpdates( (LocationListener) this);
	
}


@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	super.finish();
	if(debug){
		gpsEmuTimer.cancel();
	}else{
		locationManager.removeUpdates( (LocationListener) this);
	}
	
	try{
		partnerTimer.cancel();
	}catch(Exception e){
		
	}
	
	try{
		cam.release();
	}catch(Exception e){
		
	}
}






public void read_hatt(){
		int counter2 = 0;
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder( GPS_Location.this);
		dlgAlert.setMessage("-");
		dlgAlert.setTitle("App Title");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
	 try {
		 		InputStream is = GPS_Location.this.getResources().openRawResource(R.raw.hcoi);
		 		BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		 		String line;   
		 		int counter;
		 		counter=0;
		 		double a0,a1,a2,a3,a4,a5,b0,b1,b2 , b3, b4,b5,f0,l0 ;
		 		String id, bounds,name_EN;
		        //while ((line = br.readLine()) != null) {
		 		int i;
		        	for(i=1;i<=390;i++){
		        			counter+=1;
		        			line = br.readLine();
		        			String[] out = line.split(";");
		        			 
		        			 id = out[0];
		        			 a0 = Double.parseDouble(out[1]);
		        			 a1 = Double.parseDouble(out[2]);
		        			 a2 = Double.parseDouble(out[3]);
		        			 a3 = Double.parseDouble(out[4]);
		        			 a4 = Double.parseDouble(out[5]);
		        			 a5 = Double.parseDouble(out[6]);
		        			
		        			 b0 = Double.parseDouble(out[7]);
		        			 b1 = Double.parseDouble(out[8]);
		        			 b2 = Double.parseDouble(out[9]);
		        			 b3 = Double.parseDouble(out[10]);
		        			 b4 = Double.parseDouble(out[11]);
		        			 b5 = Double.parseDouble(out[12]);

		        			 f0 = Double.parseDouble(out[14]);
		        			 l0 = Double.parseDouble(out[15]);
		        			 
		        			 name_EN = out[17];
		        			 bounds = out[18];
		        			
		        			hatt_array[counter-1] = new hatt_object(/*dlgAlert,*/
		        											id,out[13],f0,l0,
		        											a0,a1,a2,a3,a4,a5,
		        											b0,b1,b2,b3,b4,b5,
		        											name_EN,bounds
		        			);
		        	
		        	
		                    } 
	 } catch (Exception e) {

			dlgAlert.setMessage( e.toString());
			dlgAlert.create().show();
			counter2+=1;
       }

}




public String read_g5000(double xx, double yy){
		String out_id = "";
	 try {
		
		 		InputStream is = GPS_Location.this.getResources().openRawResource(R.raw.g5000);
		 		BufferedReader br = new BufferedReader(new InputStreamReader(is));  
		 		String line;   
		 		int counter;
		 		counter=0;
		 		String id, bounds;
		 		 String[] out;
		 		 String[] out2;
		 		double left,top,bottom,right;
		 		int len;
		 		double[] x,y;
		       int i;
		        	for(i=1;i<=13055;i++){
		        			counter+=1;
		        			line = br.readLine();
		        			out = line.split(";");
		        			 
		        			 id = out[0];
		        			 bounds = out[1];
		        			 out2 = bounds.trim().split("#");//space delimiter
		        			
		        			 
		        			 left=999999999999999999.0;
		        			bottom=999999999999999999.0;
		        				top=-999999999999999999.0;
		        				right=-999999999999999999.0;
		        				
		        				
		        				
		        				len = out2.length;
		        				x = new double[len];
		        				y = new double[len];
		        				String coor[];
		        				int ii;
		        				for(ii=0;ii<=len-1;ii++){
		        					coor = out2[ii].split(",");
		        					try{
		        						x[ii]= Double.parseDouble(coor[0]);
		        						y[ii]= Double.parseDouble(coor[1]);
		        					}catch(Exception e){
		        						//dlgAlert.setMessage(coor[0] + "\n" +coor[1] + "\n" + e.toString());
		        						//dlgAlert.create().show();
		        						//e.printStackTrace();
		        					}
		        					
		        					
		        					
		        					if(top<y[ii]) top=y[ii];
		        					if(bottom>y[ii]) bottom=y[ii];
		        					if(left>x[ii]) left=x[ii];
		        					if(right<x[ii]) right=x[ii];
		        					
		        				}
		        				 
		        				if(xx>left){
		        					if(xx<right){
		        						if(yy>bottom){
		        							if(yy<top){
		        								out_id=id;
		        								//debugDisplay.setText(id + "#"+ i  + "\n" + xx + "\n" + yy + "\n" + left + " \n" + bottom + "\n"  + out[1]);
		        								i=13056;
		        							}
		        						}
		        					}
		        				}
		        	
		                    } 
	 } catch (Exception e) {
		 debugDisplay.setText(e.toString());
       }finally{
    	  
       }
	 return out_id;
}




	public hatt_object fltohatt_object(double ff, double ll){
		
		
		double eg[] = egsa.fl2EGSA87(ff, ll);
	    double x=(double) Math.round(eg[0]*100)/100;
	    double y=(double) Math.round(eg[1]*100)/100;
	    
			
		int i=0;
		int index=0;
		for(i=0;i<=390-1;i++){
			
			//if(Math.abs(hatt_array[i].f0-f)<=0.15 && Math.abs(hatt_array[i].l0-l)<=0.15){
			if(hatt_array[i].bound.hasPointInExtent(x, y)==true){
					
				//msg.setText(" x : " + "\n" + " y : " );
				//infoDisplay.setText(hatt_array[i].name);
				//dlgAlert.setMessage("-" +hatt_array[i].name);
				//dlgAlert.create().show();		
				index=i;
			}
		
		}
		
		return  hatt_array[index] ;
	}
	
	
	

	public String getMacAddress(Context context) {
	    WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    String macAddress = wimanager.getConnectionInfo().getMacAddress();
	    if (macAddress == null) {
	        macAddress = "Device don't have mac address or wi-fi is disabled";
	    }
	    return macAddress;
	}
	
	
public String fltohatt(double ff, double ll){
		
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder( GPS_Location.this);

		 /*dlgAlert.setMessage("-" +f + "\n" + l);
		 			dlgAlert.setTitle("App Title");
		 				dlgAlert.setPositiveButton("OK", null);
		 					dlgAlert.setCancelable(true);
			
			dlgAlert.create().show();
			
			dlgAlert.setMessage("-" + Math.abs(hatt_array[0].f0-f) + "\n " +Math.abs(hatt_array[0].l0-l));
			dlgAlert.create().show();*/
		double eg[] = egsa.fl2EGSA87(ff, ll);
	    double x=(double) Math.round(eg[0]*100)/100;
	    double y=(double) Math.round(eg[1]*100)/100;
	    
			
		int i=0;
		int index=0;
		for(i=0;i<=390-1;i++){
			
			//if(Math.abs(hatt_array[i].f0-f)<=0.15 && Math.abs(hatt_array[i].l0-l)<=0.15){
			if(hatt_array[i].bound.hasPointInExtent(x, y)==true){
					
				//msg.setText(" x : " + "\n" + " y : " );
				//infoDisplay.setText(hatt_array[i].name);
				//dlgAlert.setMessage("-" +hatt_array[i].name);
				//dlgAlert.create().show();		
				index=i;
			}
		
		}
	
		
	    double ht[] = hatt_array[index].to_egsa(x, y);
	    //return hatt_array[index].toString()  ;
		
		return  hatt_array[index].name + "\n" + " x : " + (double) Math.round(ht[0]*100)/100 + " (" + (double) Math.round(x*100)/100 + ")\n"  + " y : " + (double) Math.round(ht[1]*100)/100  + " (" + (double) Math.round(y*100)/100 + ")" ;
	}

@Override
public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceCreated(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceDestroyed(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	
}



public int get_pass_from_int(int user){
	
	
	long dd =  Math.round(user*132*13)*yearDay;
	int digits = ("" + dd).length();
	dd=Math.round(dd/Math.pow(10,digits-4));
	
	
		return (int) dd;

}


/**
 * Get the optimal preview size for the given screen size.
 * @param sizes
 * @param screenWidth
 * @param screenHeight
 * @return
 */
public static Size getOptimalPreviewSize(List<Size> sizes, int screenWidth, int screenHeight) {
  double aspectRatio = ((double)screenWidth)/screenHeight;
  Size optimalSize = null;
  for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
    Size currSize =  iterator.next();
    double curAspectRatio = ((double)currSize.width)/currSize.height;
    //do the aspect ratios equal?
    if ( Math.abs( aspectRatio - curAspectRatio ) < 0.01 ) {
      //they do
      if(optimalSize!=null) {
        //is the current size smaller than the one before
        if(optimalSize.height>currSize.height && optimalSize.width>currSize.width) {
          optimalSize = currSize;
        }
      } else {
        optimalSize = currSize;
      }
    }
  }
  if(optimalSize == null) {
    //did not find a size with the correct aspect ratio.. let's choose the smallest instead
    for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
      Size currSize =  iterator.next();
      if(optimalSize!=null) {
        //is the current size smaller than the one before
        if(optimalSize.height>currSize.height && optimalSize.width>currSize.width) {
          optimalSize = currSize;
        } else {
          optimalSize = currSize;
        }
      }else {
        optimalSize = currSize;
      }
      
    }
  }
  return optimalSize;
}





}





