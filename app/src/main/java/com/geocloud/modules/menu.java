package com.geocloud.modules;

import java.io.BufferedReader;
import java.io.File;

//import com.example.test2.GPS_Location.CancelOnClickListener;
//import com.example.test2.menu.OkOnClickListener;




import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.db.DatabaseHelper;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.stasi_link_class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class menu extends ListActivity{

	DatabaseHelper db ;
	MyParams params;
	//String classes[] = {"GPS_Location","","MyGLSE20app","MySurfaceView"};//,"MainActivity"};
	//String labels[] = {"geoInfo","Clear cache","OpenGL ES20","MySurfaceView"};//,"MainActivity"};
	String classes[] = {"Projects","MyGLSE20app","SyncManager",""};//,"MainActivity"};
	String labels[] = {"Project Manager","Cloud survey","Sync Staseis","Sync Projects" , "Clear cache","Ζεύξη"};//,"MainActivity"};
	String baseDir;
	String tabId;
	String lastTabIdSent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		params = new MyParams();
		setListAdapter(new ArrayAdapter<String>(menu.this, android.R.layout.simple_expandable_list_item_1, labels));
		baseDir 		= Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache";  
		db = new DatabaseHelper(this.getBaseContext(),baseDir);
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.openDataBase();
		db.createTables();
		//tabId ="8";
		//db.delKeyValue("tabId");
		//db.ad                                                                                                                                                                                                                            dKeyValue("tabId","13");
		//db.addKeyValue("tabId","8");
		tabId = db.getKeyValue("tabId");
		Toast.makeText(menu.this,"tabId : " + tabId, Toast.LENGTH_SHORT).show();
		
		//db.admin_set_unique_period_local_date(tabId);
		
		/*
		db.execSQL("UPDATE staseis SET local_date = _id");
		db.execSQL("UPDATE staseis SET _t_tabId = 13");
		db.execSQL("UPDATE staseis SET local_date = _id");
		db.execSQL("UPDATE periodos SET local_date = _id");
		db.execSQL("UPDATE periodos SET _t_tabId = 13");
		
		
		db.execSQL("UPDATE metrisi SET local_date = _id");
		db.execSQL("UPDATE metrisi SET _t_tabId = 13");
		*/
		
		//db.updateTableColumn("periodos", "stasi", 10252, "stasi=" + "1001");
		//db.updateTableColumn("periodos", "stasi_0", 10252, "stasi_0=" + "1001");
		//db.updateTableColumn("metrisi", "stasi_index", 10252, "stasi_index=" + "1001");
		//db.updateTableColumn("staseis", "x", 475653.816, "_id=" + "10615");
		//db.updateTableColumn("staseis", "y", 4202882.687, "_id=" + "10615");
		
		
		
		db.logAllStaseis();
		db.logAllStasi_link();
		db.logAllPeriodos();
		//Log.i("menu start","tabId : " + tabId);
		lastTabIdSent="";
		
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		super.onListItemClick(l, v, position, id);
		if(tabId!=null){
		if(position==4){
			showDialog(1);
			
		}else if(position==2){
			//db.removeStaseisFromDb();
			
			syncStaseis runner = new syncStaseis(this.getBaseContext());
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);    
			
			
			//sync_t_Staseis runner2 = new sync_t_Staseis(this.getBaseContext());
			//runner2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);    
			
			
			
		}else if(position==5){
			Toast.makeText(menu.this,"device paired", Toast.LENGTH_SHORT).show();
			
			
		}else if(position==3){
			syncProjects runner = new syncProjects(this.getBaseContext());
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		}else if(position==0){
			try{
				Class ourClass = Class.forName("com.geocloud.app.modules.Projects");
				Intent ourIntent = new Intent(menu.this,ourClass);
				startActivity(ourIntent);
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}

		}else{
			db.close();
		
			String cheese  =  classes[position];
			try{
					Class ourClass = Class.forName("com.geocloud.app." + cheese);
					Intent ourIntent = new Intent(menu.this,ourClass);
					startActivity(ourIntent);
			}catch(ClassNotFoundException e){
					e.printStackTrace();
			}
		}
		}else{
			if(position==5){
				pairTablet runner = new pairTablet(this.getBaseContext());
				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);    
			}else{
			
			Toast.makeText(menu.this,"please pair your device", Toast.LENGTH_SHORT).show();
			}
			}
	}
	
	

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	//super.finish();
	
	
}








@Override
protected Dialog onCreateDialog(int id) {
 // switch (id) {
   // case DIALOG_ALERT:
    // create out AlterDialog
	//LayoutInflater inflator = getLayoutInflater();
	//final View layout =  getLayoutInflater().inflate(R.layout.activity_gps_location_bk, (ViewGroup) findViewById(R.layout.activity_gps_location));
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
	 String baseDir;
		if(Environment.getExternalStorageDirectory().exists()){
				  baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();   
			   }else{
				    baseDir = "/mnt";
			   }
			  	int ss = (new File(baseDir + "/geoCloudCache")).listFiles().length; 
			  	
			  	
			  	
    builder.setMessage("Delete cache ? (" + ss + " files)");
    builder.setCancelable(true);
    builder.setPositiveButton("OK", new OkOnClickListener());
    builder.setNegativeButton("Cancel", null);
    //builder.setView(layout);
    //useret = (EditText) layout.findViewById(R.id.userEditText);
    //passet = (EditText) layout.findViewById(R.id.passEditText);
    
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
				
			 String baseDir;
			if(Environment.getExternalStorageDirectory().exists()){
					  baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();   
				   }else{
					    baseDir = "/mnt";
				   }
			//Log.i("ff",baseDir + "/geoCloudCache");
			//File f = new File(baseDir + "/geoCloudCache");
			
			//if(!f.exists()){
				//f.mkdir();
			//}
			
				  	boolean success = (new File(baseDir + "/geoCloudCache/tmp")).mkdir(); 
				  	
					  	
					  	deleteDirectory(new File(baseDir + "/geoCloudCache/tmp"));
					  	
					  	success = (new File(baseDir + "/geoCloudCache/tmp")).mkdir(); 
					  	//Toast.makeText(this, "cache deleted", Toast.LENGTH_SHORT).show();
				  	
			//EditText text1 = ((EditText) 	findViewById(R.id.userEditText));;
			//EditText text2 = ((EditText) 	findViewById(R.id.passEditText));;
			//GPS_Location.this.partnerName= useret.getText().toString();
			//GPS_Location.this.partnerPass= passet.getText().toString();
			//GPS_Location.this.partnerPass= text2.getText().toString();
			
			//Toast.makeText(GPS_Location.this, "You selected Partner Data log", Toast.LENGTH_SHORT).show();
			
			
			
			/*
			myThreadWithApp timer=new myThreadWithApp(GPS_Location.this){
				public void run(){
					try{
						//this.act.appState.alert("rerE");
						//sleep(3000);
						this.act.startPartnerTimer();
					}catch(Exception e){
						Toast.makeText(this.act, e.toString(), Toast.LENGTH_SHORT).show();
						
					}finally{
						
					}
				}
			
			};
			timer.start();*/
			
			//startPartnerTimer();
			Toast.makeText(menu.this,"ddddddd", Toast.LENGTH_SHORT).show();
			
		}catch(Exception e){
			Toast.makeText(menu.this, e.toString(), Toast.LENGTH_SHORT).show();
			
		}
		
		
		
	}
} 






public static void deleteDirectory( File dir )
{

    if ( dir.isDirectory() )
    {
        String [] children = dir.list();
        for ( int i = 0 ; i < children.length ; i ++ )
        {
         File child =    new File( dir , children[i] );
         if(child.isDirectory()){
             deleteDirectory( child );
             child.delete();
         }else{
             child.delete();

         }
        }
        dir.delete();
    }
}








private class pairTablet extends AsyncTask<String, Void, Long> {

		Context con;
		
		long timeNow = System.currentTimeMillis();
		String tn = Long.valueOf(timeNow).toString().substring(7);
		
		String myurl = "";
		
		public pairTablet(Context  con){
			this.con=con;
			myurl = params.c_url + "_geo/php/client/_pair_tablet.php?tl=" + lastTabIdSent + "&tn=" + tn;
			lastTabIdSent=tn;
		}
		
	    protected Long doInBackground(String... param) {
	    	//Log.i("aaaaa",tn);
	    	try { 
            		 URL url;
            		 
        			try {
        					url = new URL(myurl);
        					DefaultHttpClient httpclient = new DefaultHttpClient();
        				    HttpGet httpget = new HttpGet(myurl);
        				    HttpResponse response = httpclient.execute(httpget); 
        				    
        				    BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    String line = in.readLine();
        				   // while ((line = in.readLine()) != null) {   
        				    	//Log.i("response",line);
    				            
        				   // }
        				   final String[] piso =  line.split("#");
        				   Log.i("response",line);
        				     runOnUiThread(new Runnable() {
     					        public void run() {		//game connect
     					        	//Toast.makeText(menu.this,"Sync Completed", Toast.LENGTH_SHORT).show();
     					        	if(piso[0].contains("tabIdSet")){
     					        		menu.this.db.delKeyValue("tabId");
     					        		menu.this.db.addKeyValue("tabId", piso[1]);
     					        		Toast.makeText(menu.this, "device logged "  , Toast.LENGTH_LONG).show();
     					        		tabId = db.getKeyValue("tabId");
     					        	}else{
     					        		Toast.makeText(menu.this, "enter : " + tn , Toast.LENGTH_LONG).show();
          	         				   
     					        	}
     					        	
     					        }
     					    });
        				     
        				    
        				     
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            	Log.i("download error" ,e.toString() );
            }
	        return (long) 5.0;
	    }
	    
	   
	    }
	    
	 








private class syncStaseis extends AsyncTask<String, Void, Long> {

		Context con;
		String myurl = params.c_url + "_geo/php/client/_get_staseis.php";
		
		public syncStaseis(Context  con){
			this.con=con;
		}
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
            		 URL url;
            		 
        			try {
        					url = new URL(myurl);
        					DefaultHttpClient httpclient = new DefaultHttpClient();
        				    HttpGet httpget = new HttpGet(myurl);
        				    HttpResponse response = httpclient.execute(httpget);
        				    BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    StringBuffer sb = new StringBuffer("");
        				    String line = "";
        				    String NL = System.getProperty("line.separator");
        				    while ((line = in.readLine()) != null) {                    
        				            sb.append(line + NL);
        				     }
        				     in.close();
        				     String result = sb.toString();
        				     
        				      // Log.i("------ Load staseis",result);
        				     String[] st_dat = result.split("#");
        				     int i;
        				     for(i=0;i<=st_dat.length-1;i++){
        				    	// Log.i("sync staseis : " + i,st_dat[i]);
        				    	 String[] d = st_dat[i].split(";");
        				    	 MyStasi st = new MyStasi(
        				    			 				Long.valueOf(d[0]),
        				    			 				d[1],
        				    			 				Integer.valueOf(d[2]),
        				    			 				Double.valueOf(d[3]),
        				    			 				Double.valueOf(d[4]),
        				    			 				Float.valueOf(d[5]),
        				    			 				d[6],
        				    			 				Integer.valueOf(d[7]),
        				    			 				-1,
        				    			 				-1,
        				    			 				-1
        				    			 );
        				    	 
        				    	// stasi_link_class stl = new stasi_link_class(i)
        				    	 db.addStasiToDb(st,true,1);
        				    	 
        				     }
        				     
        				    // runOnUiThread(new Runnable() {
     					        //public void run() {		//game connect
     					        	//Toast.makeText(menu.this,"Sync Completed", Toast.LENGTH_SHORT).show();
     					       // }
     					   // });
        				     
        				     
        				     
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
	        return (long) 5.0;
	    }
	    
	   
	    }
	    
	 








private class syncPeriodoi extends AsyncTask<String, Void, Long> {

		Context con;
		String myurl = params.c_url + "_geo/php/client/_get_periodoi.php";
		
		public syncPeriodoi(Context  con){
			this.con=con;
		}
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
            		 URL url;
            		 
        			try {
        					url = new URL(myurl);
        					DefaultHttpClient httpclient = new DefaultHttpClient();
        				    HttpGet httpget = new HttpGet(myurl);
        				    HttpResponse response = httpclient.execute(httpget);
        				    BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    StringBuffer sb = new StringBuffer("");
        				    String line = "";
        				    String NL = System.getProperty("line.separator");
        				    while ((line = in.readLine()) != null) {                    
        				            sb.append(line + NL);
        				     }
        				     in.close();
        				     String result = sb.toString();
        				     
        				      // Log.i("------ Load staseis",result);
        				     String[] st_dat = result.split("#");
        				     int i;
        				     for(i=0;i<=st_dat.length-1;i++){
        				    	// Log.i("sync staseis : " + i,st_dat[i]);
        				    	 String[] d = st_dat[i].split(";");
        				    	 MyStasi st = new MyStasi(
        				    			 				Long.valueOf(d[0]),
        				    			 				d[1],
        				    			 				Integer.valueOf(d[2]),
        				    			 				Double.valueOf(d[3]),
        				    			 				Double.valueOf(d[4]),
        				    			 				Float.valueOf(d[5]),
        				    			 				d[6],
        				    			 				Integer.valueOf(d[7]),
        				    			 				-1,
        				    			 				-1,
        				    			 				-1
        				    			 );
        				    	 
        				    	// stasi_link_class stl = new stasi_link_class(i)
        				    	 db.addStasiToDb(st,true,1);
        				    	 
        				     }
        				     
        				    // runOnUiThread(new Runnable() {
     					        //public void run() {		//game connect
     					        	//Toast.makeText(menu.this,"Sync Completed", Toast.LENGTH_SHORT).show();
     					       // }
     					   // });
        				     
        				     
        				     
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
	        return (long) 5.0;
	    }
	    
	   
	    }
	    
	 






private class sync_t_Staseis extends AsyncTask<String, Void, Long> {

		Context con;
		String myurl = params.c_url + "_geo/php/client/_get_t_staseis.php?p=" + db.getCSVProjects();;
		
		public sync_t_Staseis(Context  con){
			this.con=con;
		}
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
            		 URL url;
            		
        			try {
        					url = new URL(myurl);
        					DefaultHttpClient httpclient = new DefaultHttpClient();
        				    HttpGet httpget = new HttpGet(myurl);
        				    HttpResponse response = httpclient.execute(httpget);
        				    BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    StringBuffer sb = new StringBuffer("");
        				    String line = "";
        				    String NL = System.getProperty("line.separator");
        				    while ((line = in.readLine()) != null) {                    
        				            sb.append(line + NL);
        				     }
        				     in.close();
        				     String result = sb.toString();
        				     
        				      // Log.i("------ Load _t_staseis",result);
        				     String[] st_dat = result.split("#");
        				    // Log.i("------ Load _t_staseis",st_dat[0] + "");
        				     int i;
        				     for(i=0;i<=st_dat.length-1;i++){
        				    	 //Log.i("sync staseis : " + i,st_dat[i]);
        				    	String[] d = st_dat[i].split(";");
        				    	 MyStasi st = new MyStasi(
        				    			 				Long.valueOf(d[0]),
        				    			 				d[1],
        				    			 				Integer.valueOf(d[2]),
        				    			 				Double.valueOf(d[3]),
        				    			 				Double.valueOf(d[4]),
        				    			 				Float.valueOf(d[5]),
        				    			 				d[6],
        				    			 				Integer.valueOf(d[7]),
        				    			 				Integer.valueOf(d[11]),
        				    			 				-1,
        				    			 				-1
        				    			 );
        				    	if(Integer.valueOf(d[8])==1) st._t_base=true;
        				    	st._t_id=Integer.valueOf(d[9]);
        				    	st._t_tabId=Integer.valueOf(d[10]);
        				    	st._t_project=Integer.valueOf(d[11]);
        				    	st.name=d[1];
        				    	stasi_link_class stl = new stasi_link_class(st._id);
        				    	stl.isbase=st._t_base;
        				    	
        				    	if(db.stasiExists(st)){
        				    		//Log.i("sync staseis : " + i,"Exists");
        				    		long t = db.getLongValue("staseis", "date", 
        				    					" _t_project=" + st._t_project
        									  + " AND  _t_id=" + st._t_id
        									  + " AND  _t_tabId=" + st._t_tabId
        				    				);
        				    		Log.i("sync staseis : " + i,t + " - " + Integer.valueOf(d[7]));
        				    	}else{
        				    		 //Log.i("sync staseis : " + i,"Not Exist");
        				    		db.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
           				    	 	db.addStasiToDb(st,true,0);
        				    	}
        				    	
        				    	 
        				     }
        				     
        				    // runOnUiThread(new Runnable() {
     					        //public void run() {		//game connect
     					        	//Toast.makeText(menu.this,"Sync Completed", Toast.LENGTH_SHORT).show();
     					       // }
     					   // });
        				     
        				     
        				     
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
	        return (long) 5.0;
	    }
	    
	   
	    }
	    
	 




private class syncProjects extends AsyncTask<String, Void, Long> {

		Context con;
		String myurl = params.c_url + "_geo/php/client/_get_projects.php?tb=" + tabId;
		
		public syncProjects(Context  con){
			this.con=con;
		}
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
            		 URL url;
            		 
        			try {
        				
        					long current_time_sec = System.currentTimeMillis()/1000;
        					url = new URL(myurl);
        					DefaultHttpClient httpclient = new DefaultHttpClient();
        				    HttpGet httpget = new HttpGet(myurl);
        				    HttpResponse response = httpclient.execute(httpget);
        				    BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    StringBuffer sb = new StringBuffer("");
        				    String line = "";
        				    String NL = System.getProperty("line.separator");
        				    while ((line = in.readLine()) != null) {                    
        				            sb.append(line + NL);
        				     }
        				     in.close();
        				     String result = sb.toString();
        				     
        				    
        				     
        				     String[] dd   = result.split("@");
        				     String[] st_dat = dd[0].split("#");
        				     String ys = dd[1];
        				     long dt = Long.valueOf(dd[2])-current_time_sec;
        				     db.delKeyValue("dt");
        				     db.addKeyValue("dt",String.valueOf(dt));
        				     
        				     
        				    // Log.i("syncProjects ", "result : " + result);
        				     Log.i("t sec ",  dt+"" );
        				     Log.i("syncProjects ",  ys);
        				     db.delKeyValue("ys");
        				     db.addKeyValue("ys",ys);
        				     int i;
        				     for(i=0;i<=st_dat.length-1;i++){
        				    	 Log.i("sync staseis : " + i,st_dat[i]);
        				    	 String[] d = st_dat[i].split(";");
        				    
        				    	 db.addProjectToDb(Long.valueOf(d[0]),d[1]);
        				    	 
        				     }
        				    // runOnUiThread(new Runnable() {
     					        //public void run() {		//game connect
     					        	//Toast.makeText(menu.this,"Sync Completed", Toast.LENGTH_SHORT).show();
     					       // }
     					   // });
        				     
        				     
        				     
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            	Log.i("download error" ,e.toString() );
            }
	        return (long) 5.0;
	    }
	    
	   
	    }


	}











	

