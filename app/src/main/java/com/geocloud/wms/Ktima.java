package com.geocloud.wms;

import java.util.ArrayList;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.geocloud.MyGLES20.D_Square_Image;

public class Ktima {
	
	Proj proj;
	Context mActivityContext;
	
	public Ktima(Context mActivityContext){
		this.mActivityContext = mActivityContext;
		proj = new Proj();;
	}
	
	
	
	public Object[] getUrlFromLonLat(float lon, float lat, int z){
		
			float bima = get_bima(z);
	            
	       // Log.i("qq","q");
	        double f1 		= 	Math.floor(lat/bima)*bima;
	        double f2 		= 	f1 + bima;
	        double l1 		= 	Math.floor(lon/bima)*bima;
	        double l2 		= 	l1 + bima;
	        Object out[] 	= 	{null,null,null,null,null,null,null,null};
	        
	        out[0] 	= "http://gis.ktimanet.gr/wms/wmsopen/wmsserver.aspx?LAYERS=basic&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&BBOX=";
	        out[0] 	= out[0] + new Double(l1).toString() + ",";
	        out[0] 	= out[0] + new Double(f1).toString() + ",";
	        out[0] 	= out[0] + new Double(l2).toString() + ",";
	        out[0] 	= out[0] + new Double(f2).toString() + "&WIDTH=512&HEIGHT=512";
	        //Log.i("qq","q");  
	       // out[1] 	= (int)z + "_" + (int)Math.floor(lat/bima) + "_" + (int) Math.floor(lon/bima);
	        out[1] 	= (int)z + "/" + (int)Math.floor(lat/bima) + "/" + (int) Math.floor(lon/bima);
	        //Log.i("qq","q");  
	        double 	egout[] 	= 	proj.fl2EGSA87(lat, lon);
	        //Log.i("qq","q");  
	        double 	xeg			=	(double) Math.round(egout[0]*100)/100;
	        double 	yeg			=	(double) Math.round(egout[1]*100)/100;
	        double 	dx 			= 	get_ktima_dx(xeg,yeg);
	        double 	dy 			= 	get_ktima_dy(xeg,yeg);
	    	double 	ee[] 		= 	proj.Egsa2fl84(xeg-dx,yeg-dy);
	    	double 	fktima		=	ee[0];
	    	double 	lktima		=	ee[1];
	    	//Log.i("qq","q");
	        out[2]=(lktima-l1)/bima;
	        out[3]=(fktima-f1)/bima;     
	        
	        out[4] = f1;
	        out[5] = l1;
	        
	        out[6] = (int)Math.floor(lon/bima);
	        out[7] = (int)Math.floor(lat/bima);
	        
	        
		return out;
	   
	}
	
	
	public float[] getOriginFromLonLat(float lon, float lat, int z){
		
		float bima = get_bima(z);
       
        
        double f1 		= 	Math.floor(lat/bima)*bima;
        double l1 		= 	Math.floor(lon/bima)*bima;
        float out[] 	= 	new float[5];
        
        out[0]=(float) l1;
        out[1]=(float) f1;
        out[2]=(float) Math.floor(lon/bima);
        out[3]=(float) Math.floor(lat/bima);
        out[4] = bima;
	return out;
   
}
	
	
	
public float get_bima(int level){

	int 		dz = level - 6;
	float 		bima;
	int i;
	
	bima = 1;
    if (dz > 0) {       
    				for (i = 1;i<=dz;i++)		{    bima = bima / 2;  }
    }else{
            		for( i = 1 ;i<= -dz;i++)	{    bima = bima * 2;  }
    }
    
    return bima;

}

	
public void getImageToImageGrid(double f,double l,int z,final ArrayList<D_Square_Image> item, final float indX, final float indY){
	
	
	
	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();  
	final Object[] dat = getUrlFromLonLat((float) f,(float) l, (int) 17);
	
	Log.i("deb","e1.toString()");
  	final String myurl 		= (String) dat[0];
  	final String filename 	= String.valueOf(String.format(baseDir + "/geoCloudCache/%s.jpg",(String) dat[1]));
    
  	final String[] data = new String[2];
  	data[0]=myurl;
  	data[1]=filename;
  	try {
  		//KtimaDownloader runner = new KtimaDownloader(item);
  		//Intent intent 				= new Intent(mActivityContext.getApplicationContext(), KtimaDownloaderService.class);
	   	// intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
  		//mActivityContext.getApplicationContext().startService(intent);
	     
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		Log.i("deb",e1.toString());
	}
   // String sleepTime = "100";
   // runner.execute(sleepTime);
  	
    
    /*
  	final Handler handler = new Handler(Looper.getMainLooper());
  	final Timer ktimaDownloadTimer = new Timer();
  	TimerTask doAsynchronousTask = new TimerTask() {       
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {       
                    try {
                    	
                    	
                    	if(new java.io.File(filename).exists() ){
                    		item.add(new D_Square_Image());
                    		item.get(item.size()-1).set_coor_data((float) indX, (float) indY,17,(Integer) dat[6], (Integer) dat[7]);
                    		item.get(item.size()-1).set_filename(filename);
                    	}else{
                    		 File file = new File(filename);
                    		 URL url;
                				try {
                					url = new URL(myurl);
                				
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
                				item.add(new D_Square_Image());
                				item.get(item.size()-1).set_coor_data((float) 0, (float) 0,17,(Integer) dat[6], (Integer) dat[7]);
                        		item.get(item.size()-1).set_filename(filename);
                    	}
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                    ktimaDownloadTimer.cancel();
                }
            });
        }
    };
    ktimaDownloadTimer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
  	*/
  	//KtimaDownloader kt = new KtimaDownloader(mActivityContext);
   // Log.i("121","5");
    try{
    	// kt.execute(data); 
    }catch(Exception e){
    	
    }
    
}




	 
	 public double get_ktima_dx(double x, double y) {
	 
	 
	    double dx;
	    
	    
	    double a, b, c, d, e, f ;
	    a = -49.53;
	    b = 1.343 / Math.pow(10,5);
	    c = -1.627 / Math.pow(10,7);
	    d = -1.981 / Math.pow(10,12);
	    e = 1.908 / Math.pow(10,12);
	    f = 4.529 / Math.pow(10,12);
	    
	 
	    dx = a + b * x + c * y + d * x * x + e * y * y + f * x * y;
	    return  dx;
	 
	 }
	 
	 
	 
	 
	 public double get_ktima_dy(double x, double y) {
	 
	 
	    double dy;
	    
	    
	    double a, b, c, d, e, f;
	    a = -37.578;
	    b = -4.651 / Math.pow(10,6);
	    c = 2.508 / Math.pow(10,5);
	    d = 1.07 / Math.pow(10,12);
	    e = -3.409 / Math.pow(10,12);
	    f = -3.49 / Math.pow(10,12);
	    
	 
	    dy = a + b * x + c * y + d * x * x + e * y * y + f * x * y;
	    return dy;
	 
	 }
	 

	 
	
	 
	 
	 
	 
	 /**
	  * @author Prabu
	  * Private class which runs the long operation. ( Sleeping for some time )
	  */
	 private class KtimaDownloader extends AsyncTask<String, Void, Long> {
		 	ArrayList<D_Square_Image> item = new ArrayList<D_Square_Image>();
			
			
			public KtimaDownloader(ArrayList<D_Square_Image> item){
				this.item = item;
				
			}
			
			
		    protected Long doInBackground(String... param) {
		    	/*double f = Double.parseDouble(param[0]);
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
		        //Do some work*/
		        return (long) 5.0;
		    }
		    
		    protected void onProgressUpdate(Integer... progress) {
		        //setProgressPercent(progress[0]);
		    }
		    
		    protected void onPostExecute(Long result/*Void param*/) {
		       //this.activity.setImage(this.myfilename,this.drawx,this.drawy);
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
	 
	 
	
	
}





