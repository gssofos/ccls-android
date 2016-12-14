package com.geocloud.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.MyGLES20.D_Square_Image;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.stasi_link_class;
	
public class MyWeb {
	MyParams params;
	
		public MyWeb(MyParams params){
				this.params=params;
		}
		
		
		
		
		
		
		
		
		
		
	
		public void httpCall(String url){
			httpCall_Class runner = new httpCall_Class(url);
			//params.debug(url);;
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);     
		}
		
		
		
		
		

		
		public void httpCallPost(String url, String data){
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", data));
			
			httpCallPost_Class runner = new httpCallPost_Class(url,nameValuePairs,0,2000);
			//params.debug(url);;
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);     
		}
		
		
		public void httpCallPost(String url, String data,int callback_function_index,int timeout){
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("data", data));
			
			httpCallPost_Class runner = new httpCallPost_Class(url,nameValuePairs,callback_function_index,timeout);
			//params.debug(url);;
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);     
		}
		
		
		


private class httpCall_Class extends AsyncTask<String, Void, Long> {


		String myurl;
		
		public httpCall_Class(String myurl){
			
			this.myurl = myurl;
			
		}
		
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
	    		//mGLSurfaceView.requestRender();			//Gia DIRTY MODE mallon (oxi auto update)
            			


            		
            		// File file = new File(filename);
            		 URL url;
            		 Log.i("url",myurl);
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
        				     Log.v("My Response :: ", result);
        				       // params.debug(result);
        				        /*   
        					HttpURLConnection con = (HttpURLConnection)url.openConnection();
        		            con.setRequestMethod("GET");
        		            con.setDoOutput(true);
        		            con.connect();

        		           
        		            InputStream 			is 		= con.getInputStream();
        		            //FileOutputStream 		fos 	= new FileOutputStream(file);
        		            //BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
        		           // BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
        		            byte[] 					data 	= new byte[1024];
        		            int x = 0;
        		            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
        		            
        		            //fos.flush();  
        		            bout.flush();  
        		            //fos.close();   
        		            bout.close();  
        		            is.close();
        		          */
        		            
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
	    
	    //protected void onPostExecute(Long result/*Void param*/) {


	   // }







private class httpCallPost_Class extends AsyncTask<String, Void, Long> {


		String myurl;
		List<NameValuePair> nameValuePairs;
		int callback_function_index;
		int timeout =2000;
		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	      //  nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
	       
	        
	        
		public httpCallPost_Class(String myurl,List<NameValuePair> nameValuePairs,int callback_function_index, int timeout){
			
			this.myurl = myurl;
			this.nameValuePairs=nameValuePairs;
			this.callback_function_index = callback_function_index;
			this.timeout=timeout;
		}
		
		
	    protected Long doInBackground(String... param) {
	    	
	    	try { 
	    		//mGLSurfaceView.requestRender();			//Gia DIRTY MODE mallon (oxi auto update)
            			


            		
            		// File file = new File(filename);
            		// URL url;
            		// Log.i("url",myurl);
        			try {
        				//	url = new URL(myurl);
        				HttpParams httpParameters = new BasicHttpParams();
        				HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        				HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        				
        				
        				HttpClient client = new DefaultHttpClient(httpParameters);
        				client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        				client.getParams().setParameter("http.socket.timeout", timeout);
        				client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
        				httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
        				HttpPost request = new HttpPost(myurl);
        				request.getParams().setParameter("http.socket.timeout", timeout);
        				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);

        					//HttpClient httpclient = new DefaultHttpClient();
        				   // HttpPost httppost = new HttpPost(myurl);
        				request.setEntity(formEntity);
        					 // Execute HTTP Post Request
        			        HttpResponse response = client.execute(request);
        				   
        			      
        			        
        					//DefaultHttpClient httpclient = new DefaultHttpClient();
        				   // HttpGet httpget = new HttpGet(myurl);
        				   // HttpResponse response = httpclient.execute(httpget);
        				   BufferedReader    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        				    StringBuffer sb = new StringBuffer("");
        				    String line = "";
        				    String NL = System.getProperty("line.separator");
        				    while ((line = in.readLine()) != null) {                    
        				            sb.append(line + NL);
        				    }
        				    in.close();
        				   String result = sb.toString();
        				    
        				   if(callback_function_index==0)		   		Log.v("My Response :: ", result);
        				   if(callback_function_index==100) 			callback_1_MyProject_syncStaseisToCloud(result,true);
        				   if(callback_function_index==101) 			callback_1_MyProject_syncStaseisToCloud(result,false);
        				   if(callback_function_index==2) 				callback_2_MyProject_syncPeriodoiToCloud(result);
        				   
        				   if(callback_function_index==200) 			callback_200_MyProject_syncPeriodoi(result);
        				   if(callback_function_index==300) 			callback_3_MyProject_syncStaseis(result);
        				   if(callback_function_index==400)		   		callback_400_MyProject_syncMetriseis(result);
        				   if(callback_function_index==500)		   		callback_500_MyProject_castStaseis(result);
          				  
        				   
        				   
        				      // params.debug(result);
        				        /*   
        					HttpURLConnection con = (HttpURLConnection)url.openConnection();
        		            con.setRequestMethod("GET");
        		            con.setDoOutput(true);
        		            con.connect();

        		           
        		            InputStream 			is 		= con.getInputStream();
        		            //FileOutputStream 		fos 	= new FileOutputStream(file);
        		            //BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
        		           // BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
        		            byte[] 					data 	= new byte[1024];
        		            int x = 0;
        		            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
        		            
        		            //fos.flush();  
        		            bout.flush();  
        		            //fos.close();   
        		            bout.close();  
        		            is.close();
        		          */
        		            
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






	private void callback_1_MyProject_syncStaseisToCloud(String data, boolean all){
		//Idio array kai seira me to MyProject_syncStaseisToCloud
		

		String[] dat0 = data.split("@@");


		if(dat0[0].contains("900100") && dat0[3].contains("900200")){
		
			String[] dat 	= dat0[1].split("#");
			long	 t 		= Long.valueOf(dat[0]);
			int i,k,count=0;
			Log.i("callback_1_MyProject_syncStaseisToCloud  st count",params.staseis.item.size() + "");
			for(i=0;i<=params.staseis.item.size()-1;i++){
				MyStasi st = params.staseis.item.get(i);
				if(!st.fixed){
					count++;
					String[] dt = dat[count].split(",");
					Log.i("callback_1_MyProject_syncStaseisToCloud  "," ##" + dat[count]);
					
					
					
					if(Long.valueOf(dt[0])==0){
					//if(st.global_id<1){
						if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
							st._t_base=true;
						}else{
							st._t_base=false;
						}
					
						st.global_id	=	Long.valueOf(dt[1]);
						st.date			=	(int) t;
						st._t_id		=	(int) st._id;
						st._t_tabId		=	Integer.valueOf(params.tabId);
						st._t_project	=	(int) params.activeProject._id;
						
						Log.i("callback_1_MyProject_syncStaseisToCloud  added", "st.global_id : " + st.global_id + "   -   "  +  "st._t_project : " + st._t_project    +  "st._t_id : " + st._t_id  +  "st._t_base : " + st._t_base);
						
						
						
						//sto telos allazo to id gia na to xrisimopoio
						st._id			=	Long.valueOf(dt[1]);
						//params.app.dbHelper.updateTableColumn("staseis", "_id", st._id, "_id=" + st._t_id);
						params.app.dbHelper.updateStasiDataFromAddToBase(st);
						params.app.dbHelper.updateTableColumn("con_project_stasi", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi_0", st._id, "stasi_0=" + st._t_id);
						params.app.dbHelper.updateTableColumn("metrisi", "stasi_index", st._id, "stasi_index=" + st._t_id);
						
						
						params.msets.update_stasi_index_id(  st._t_id,st._id);
						params.msets.update_stasi_0_index_id(  st._t_id,st._id);
						params.msets.update_stasi_index_id_of_metriseis(st._t_id,st._id);
						
						
					}
				
					if(Long.valueOf(dt[0])==1){
						Log.i("callback_1_MyProject_syncStaseisToCloud  fixed", "stasi same version : " + dt[1]);
					}

					if(Long.valueOf(dt[0])==2){
						Log.i("callback_1_MyProject_syncStaseisToCloud ", "newer version on tablet : " + dt[1]);
						//st.date=(int) t;
						//st.db_update_timeStamp();
					}
				
					if(Long.valueOf(dt[0])==3){
						//Log.i("callback_1_MyProject_syncStaseisToCloud ", "updating data : " + dat[count]);
						t = Long.valueOf(dt[2]);
						
						Log.i("callback_1_MyProject_syncStaseisToCloud updte data : ",t + " , " + dt[7] + " , " + dt[8]);
						params.app.dbHelper.updateTableColumn("staseis", "date", t, "_id=" + st._id);
						params.app.dbHelper.updateTableColumn("staseis", "sxolia", dt[7], "_id=" + st._id);
						params.app.dbHelper.updateTableColumn("staseis", "name", dt[8], "_id=" + st._id);
						
						params.app.dbHelper.updateTableColumn("staseis", "f", Double.valueOf(dt[3]), "_id=" + st._id);
						params.app.dbHelper.updateTableColumn("staseis", "l", Double.valueOf(dt[4]), "_id=" + st._id);
						params.app.dbHelper.updateTableColumn("staseis", "x", Double.valueOf(dt[5]), "_id=" + st._id);
						params.app.dbHelper.updateTableColumn("staseis", "y", Double.valueOf(dt[6]), "_id=" + st._id);
						
						st.name =  dt[8];
						st.sxolia =  dt[7];
						st.date =  (int) t;
					}
				}
			}
		
		
		
		
			
			
			
			if(dat0[2].length()>10){
		
					String[] new_staseis = dat0[2].split("#");
		
					for(i=0;i<=new_staseis.length-1;i++){
			
			
						String[] d = new_staseis[i].split(";");
						MyStasi st = new MyStasi(
	    			 				Long.valueOf(d[0]),
	    			 				d[1],
	    			 				Integer.valueOf(d[2]),
	    			 				Double.valueOf(d[3]),
	    			 				Double.valueOf(d[4]),
	    			 				Float.valueOf(d[5]),
	    			 				d[6],
	    			 				Integer.valueOf(d[7]),
	    			 				(int) params.activeProject._id,
	    			 				(int) params.getTime(),
	    			 				Integer.valueOf(params.tabId)
	    			 				
								);
				    	st._t_base=false;
				    	if(Integer.valueOf(d[8])==1) st._t_base=true;
				    	st._t_id=Integer.valueOf(d[9]);
				    	st._t_tabId=Integer.valueOf(d[10]);
				    	st._t_project=Integer.valueOf(d[11]);
				    	st.name=d[1];
				    	st.log();
	    	
				    	stasi_link_class stl = new stasi_link_class(st._id);
				    	stl.isbase=st._t_base;
	    	
	    	
			    		 //Log.i("sync staseis : " + i,"Not Exist");
			    		params.app.dbHelper.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
			    		params.app.dbHelper.addStasiToDb(st,true,0); 	 	
					}
			}
			
			if(all){
				params.activeProject.syncPeriodoiToCloud();
			}else{
				Intent intent = params.app.getIntent();
				params.app.finish();
				params.app.startActivity(intent);
			}
			
		
			//Intent intent = params.app.getIntent();
			//params.app.finish();
			//params.app.startActivity(intent);
		
		}
	}

	
	
	
	
	
	
	private void callback_2_MyProject_syncPeriodoiToCloud(String data){
		//Idio array kai seira me to MyProject_syncPeriodoiToCloud
		Log.i("callback_2_MyProject_syncPeriodoiToCloud  ",data);
		String[] dat0 = data.split("@@");
		Log.i("callback_2_MyProject_syncPeriodoiToCloud  debug",dat0[5]);
		if(dat0[0].contains("900100") && dat0[4].contains("900200")){
			
			
			String[] dtmet =dat0[3].split("#");
			for(int y=0;y<dtmet.length-1;y++){
				
				Log.i("callback_2_MyProject_syncPeriodoiToCloud metrisi data ",dtmet[y]);
				params.app.dbHelper.insert_metrisi_if_not_exists(dtmet[y]);
			}
			
			
		String[] dat = dat0[1].split("#");
		long t = Long.valueOf(dat[0]);
		
		int i,k,count=0;
		Log.i("callback_2_MyProject_syncPeriodoiToCloud  per count",params.msets.item.size() + "");
		
		for(i=0;i<=params.msets.item.size()-1;i++){
			MyMeasureSet per = params.msets.item.get(i);
			Log.i("callback_2_MyProject_syncPeriodoiToCloud  ",dat[i+1]);
				count++;
				String[] dt = dat[count].split(",");
				if(Long.valueOf(dt[0])==0){
					//if(st.global_id<1){
					Log.i("callback_2_MyProject_syncPeriodoiToCloud  ","Added to net");
					per._t_id=	per._id;
					per._id=	Long.valueOf(dt[1]);
					per.date=	(int) t;
					
					
					params.app.dbHelper.updateTableColumn("periodos", "date", t, "_id=" +per._t_id);
					params.app.dbHelper.updateTableColumn("periodos", "_t_id", per._t_id, "_id=" +per._t_id);
					params.app.dbHelper.updateTableColumn("periodos", "_id", per._id, "_id=" +per._t_id);
					params.app.dbHelper.updateTableColumn("metrisi", "periodos", per._id, "periodos=" +per._t_id);
					
					
					/*
					if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
						st._t_base=true;
					}else{
						st._t_base=false;
					}
					
					
						st.global_id	=	Long.valueOf(dt[1]);
						st.date			=	(int) t;
						st._t_id		=	(int) st._id;
						st._t_tabId		=	Integer.valueOf(params.tabId);
						st._t_project	=	(int) params.activeProject._id;
						
						Log.i("callback_1_MyProject_syncStaseisToCloud  added", "st.global_id : " + st.global_id + "   -   "  +  "st._t_project : " + st._t_project    +  "st._t_id : " + st._t_id  +  "st._t_base : " + st._t_base);
						
						
							
						//sto telos allazo to id gia na to xrisimopoio
						st._id			=	Long.valueOf(dt[1]);
						//params.app.dbHelper.updateTableColumn("staseis", "_id", st._id, "_id=" + st._t_id);
						params.app.dbHelper.updateStasiDataFromAddToBase(st);
						params.app.dbHelper.updateTableColumn("con_project_stasi", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi_0", st._id, "stasi_0=" + st._t_id);
						params.app.dbHelper.updateTableColumn("metrisi", "stasi_index", st._id, "stasi_index=" + st._t_id);
						
						*/
				}
				
				if(Long.valueOf(dt[0])==1){
					Log.i("callback_2_MyProject_syncPeriodoiToCloud  fixed", "per same version : " + dt[1]);
				}

				if(Long.valueOf(dt[0])==2){
					Log.i("callback_2_MyProject_syncPeriodoiToCloud ", "newer version on tablet : " + dt[1]);
					//st.date=(int) t;
					//st.db_update_timeStamp();
				}
				
				if(Long.valueOf(dt[0])==3){
					//Log.i("callback_1_MyProject_syncStaseisToCloud ", "updating data : " + dat[count]);
					t = Long.valueOf(dt[2]);
					/*
					Log.i("callback_2_MyProject_syncPeriodoiToCloud updte data : ",t + " , " + dt[7] + " , " + dt[8]);
					params.app.dbHelper.updateTableColumn("staseis", "date", t, "_id=" + st._id);
					params.app.dbHelper.updateTableColumn("staseis", "sxolia", dt[7], "_id=" + st._id);
					params.app.dbHelper.updateTableColumn("staseis", "name", dt[8], "_id=" + st._id);
					
					st.name =  dt[8];
					st.sxolia =  dt[7];
					st.date =  (int) t;
					
					
					*/
				
					
				
				}

				
			
		}
		
		
/*
		if(dat0[2].length()>10){
		String[] new_periodoi = dat0[2].split("#");
		for(i=0;i<=new_periodoi.length-1;i++){
			Log.i("callback_2_MyProject_syncPeriodoiToCloud--",new_periodoi[i]);
			
			String[] d = new_periodoi[i].split(";");
	    	 MyMeasureSet per = new MyMeasureSet( );
	    	 
	    	 per._id			=	Long.valueOf(d[0]);
	    	 per.stasi_id		=	Integer.valueOf(d[1]);
	    	 per.stasi_0_id		=	Integer.valueOf(d[2]);
	    	 per.stasi_0_angle	=	Float.valueOf(d[3]);
	    	 per.project		=	Integer.valueOf(d[4]);
	    	 per.YO				=	Float.valueOf(d[5]);
	    	 per.sxolia			=	d[6];
	    	 per.date			=	Integer.valueOf(d[7]);
	    	 per._t_id			=	Integer.valueOf(d[8]);
	    	 per._t_tabId		=	Integer.valueOf(d[9]);
	    	 
	    	 
	    	 params.app.dbHelper.addPeriodoToDbFromSync(per);
	    	 
	    	 
	    	// stasi_link_class stl = new stasi_link_class(i)
	    	 //db.addStasiToDb(st,true,1);
	    	 
	    	 
	    	 
			
		}
		}*/
		Intent intent = params.app.getIntent();
		params.app.finish();
		params.app.startActivity(intent);
		
		}
	}
	
	
	
	
	
	
	

	private void callback_3_MyProject_syncStaseis(String data){
		//Idio array kai seira me to MyProject_syncStaseisToCloud
		
		 	params.logWithTime(data);
		    

				String[] dat0 = data.split("@@");


				if(dat0[0].contains("900100") && dat0[3].contains("900200")){
				
					String[] dat 	= dat0[1].split("#");
					long	 t 		= Long.valueOf(dat[0]);
					int i,k,count=0;
					Log.i("callback_3_MyProject_syncStaseis  st count",params.staseis.item.size() + "");
					for(i=0;i<=params.staseis.item.size()-1;i++){
						
						MyStasi st = params.staseis.item.get(i);
						if(!st.fixed){
							//if(st._t_tabId==Integer.valueOf(params.tabId)){
								
							count++;
							String[] dt = dat[count].split(",");
							Log.i("callback_3_MyProject_syncStaseisToCloud  "," ##" + dat[count]);
							
							
							
							if(Long.valueOf(dt[0])==0){
							//if(st.global_id<1){
								if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
									st._t_base=true;
								}else{
									st._t_base=false;
								}
							
								st.global_id	=	Long.valueOf(dt[1]);
								st.date			=	(int) t;
								st.toCast			=	false;
								//st._t_id		=	(int) st._id;					//to deprecate
								st._t_tabId		=	Integer.valueOf(params.tabId);
								//st._t_project	=	(int) params.activeProject._id;
								
								Log.i("callback_3_MyProject_syncStaseis  added", "st.global_id : " + st.global_id + "   -   "  +  "st._t_project : " + st._t_project    +  "st._t_id : " + st._t_id  +  "st._t_base : " + st._t_base);
								
								params.app.dbHelper.updateTableColumn("staseis", "global_id", st.global_id, "_id=" + st._id);
								params.app.dbHelper.updateTableColumn("staseis", "tocast", 0, "_id=" + st._id);
								params.app.dbHelper.updateTableColumn("staseis", "date", t, "_id=" + st._id);
								
								//sto telos allazo to id gia na to xrisimopoio
								/*
								
								st._id			=	Long.valueOf(dt[1]);
								params.app.dbHelper.updateStasiDataFromAddToBase(st);
								params.app.dbHelper.updateTableColumn("con_project_stasi", "stasi", st._id, "stasi=" + st._t_id);
								params.app.dbHelper.updateTableColumn("periodos", "stasi", st._id, "stasi=" + st._t_id);
								params.app.dbHelper.updateTableColumn("periodos", "stasi_0", st._id, "stasi_0=" + st._t_id);
								params.app.dbHelper.updateTableColumn("metrisi", "stasi_index", st._id, "stasi_index=" + st._t_id);
								
								
								params.msets.update_stasi_index_id(  st._t_id,st._id);
								params.msets.update_stasi_0_index_id(  st._t_id,st._id);
								params.msets.update_stasi_index_id_of_metriseis(st._t_id,st._id);
								*/
								
							}
							
							if(Long.valueOf(dt[0])==1){
								Log.i("callback_3_MyProject_syncStaseis  fixed", "stasi same version : " + dt[1]);
							}

							if(Long.valueOf(dt[0])==2){
								Log.i("callback_3_MyProject_syncStaseis ", "newer version on tablet : " + dt[1]);
								//st.date=(int) t;
								//st.db_update_timeStamp();
							}
						
							if(Long.valueOf(dt[0])==3){
								//Log.i("callback_1_MyProject_syncStaseisToCloud ", "updating data : " + dat[count]);
								t = Long.valueOf(dt[2]);
								
								Log.i("callback_3_MyProject_syncStaseis update data : ",t + " , " + dt[7] + " , " + dt[8]);
								params.app.dbHelper.updateTableColumn("staseis", "date", t, "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "sxolia", dt[7], "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "name", dt[8], "global_id=" + st.global_id);
								
								params.app.dbHelper.updateTableColumn("staseis", "f", Double.valueOf(dt[3]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "l", Double.valueOf(dt[4]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "x", Double.valueOf(dt[5]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "y", Double.valueOf(dt[6]), "global_id=" + st.global_id);
								//params.app.dbHelper.updateTableColumn("staseis", "_id", Long.valueOf(dt[6]), "_id=" + st._id);
								
								st.name =  dt[8];
								st.sxolia =  dt[7];
								st.date =  (int) t;
							}
						//}
					}
				
					}
				
					
					
					
					
					
					//pernao nees

					
					if(dat0[2].length()>10){
				
							String[] new_staseis = dat0[2].split("#");
				
							for(i=0;i<=new_staseis.length-1;i++){
					
								params.logWithTime(new_staseis[i]);
					
								String[] d = new_staseis[i].split(";");
								/*
								params.logWithTime(-1 + "");
								params.logWithTime(d[1] + "");
								params.logWithTime(Integer.valueOf(d[2]) + "");
								params.logWithTime(Double.valueOf(d[3]) + "");
								params.logWithTime(Double.valueOf(d[4]) + "");
								params.logWithTime(Float.valueOf(d[5]) + "");
								params.logWithTime(d[6] + "");
								params.logWithTime(Integer.valueOf(d[7]) + "");
								*/
								
								MyStasi st = new MyStasi(
			    			 				-1,
			    			 				d[1],
			    			 				Integer.valueOf(d[2]),
			    			 				Double.valueOf(d[3]),
			    			 				Double.valueOf(d[4]),
			    			 				Float.valueOf(d[5]),
			    			 				d[6],
			    			 				Integer.valueOf(d[7]),
			    			 				(int) params.activeProject._id,
			    			 				(int) params.getTime(),
			    			 				Integer.valueOf(params.tabId),
			    			 				Double.valueOf(d[13]),
			    			 				Double.valueOf(d[14]),
			    			 				params
										);
								st._t_base=false;
						    	
						    	if(Integer.valueOf(d[8])==1) st._t_base=true;
						    	st._t_id=Integer.valueOf(d[9]);
						    	st._t_tabId=Integer.valueOf(d[10]);
						    	st._t_project=Integer.valueOf(d[11]);
						    	st.name=d[1];
						    	st.local_date=Integer.valueOf(d[12]);
						    	st.global_id=Integer.valueOf(d[0]);
						    	st.toCast=false;
						    	
						    	st.log();
						    	
			    	
			    	
					    		 //Log.i("sync staseis : " + i,"Not Exist");
					    		//params.app.dbHelper.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
					    		int nid = params.app.dbHelper.addStasiFromSync2ToDb(st,0); 
					    		
					    		stasi_link_class stl = new stasi_link_class(nid);
						    	stl.isbase=st._t_base;
						    	params.app.dbHelper.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
						    	
							}
					}
					
					
					params.activeProject.getStasiMaxGlobalId();
					Intent intent = params.app.getIntent();
					params.app.finish();
					params.app.startActivity(intent);
				
				}
	}

	
	
	
	
	
	private void callback_500_MyProject_castStaseis(String data){
		 	params.logvWithTime(data);
		    

				String[] dat0 = data.split("@@");


				if(dat0[0].contains("900100") && dat0[3].contains("900200")){
				
					String[] dat 	= dat0[1].split("#");
					long	 t 		= Long.valueOf(dat[0]);
					int i,k;
					for(i=1;i<=dat.length-1;i++){
						
						//if(!st.fixed){
							//if(st._t_tabId==Integer.valueOf(params.tabId)){
								
							String[] dt = dat[i].split(",");
							Log.i("callback_500_MyProject_castStaseis  "," ##" + dat[i]);
							
							
							
							if(Long.valueOf(dt[0])==0){
							/*	if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
									st._t_base=true;
								}else{
									st._t_base=false;
								}
							*/
								MyStasi st = params.staseis.get_stasi_by_id(Long.valueOf(dt[2]));
									st.log();	
								st.global_id	=	Long.valueOf(dt[1]);
								st.date			=	(int) t;
								st.toCast			=	false;
								
								Log.i("callback_500_MyProject_castStaseis  added", "st.global_id : " + st.global_id + "   -   "  +  "st._t_project : " + st._t_project    +  "st._t_id : " + st._t_id  +  "st._t_base : " + st._t_base);
								
								params.app.dbHelper.updateTableColumn("staseis", "global_id", st.global_id, "_id=" + st._id);
								params.app.dbHelper.updateTableColumn("staseis", "tocast", 0, "_id=" + st._id);
								params.app.dbHelper.updateTableColumn("staseis", "date", t, "_id=" + st._id);
								
								
								
							}
							
							if(Long.valueOf(dt[0])==1){
								Log.i("callback_500_MyProject_castStaseis  fixed", "stasi same version : " + dt[1]);
							}

							if(Long.valueOf(dt[0])==2){
								Log.i("callback_500_MyProject_castStaseis ", "newer version on tablet : " + dt[1]);
								//st.date=(int) t;
								//st.db_update_timeStamp();
							}
						
							if(Long.valueOf(dt[0])==3){
								t = Long.valueOf(dt[2]);
								
								Log.i("callback_500_MyProject_castStaseis update data : ",t + " , " + dt[7] + " , " + dt[8]);
								/*params.app.dbHelper.updateTableColumn("staseis", "date", t, "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "sxolia", dt[7], "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "name", dt[8], "global_id=" + st.global_id);
								
								params.app.dbHelper.updateTableColumn("staseis", "f", Double.valueOf(dt[3]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "l", Double.valueOf(dt[4]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "x", Double.valueOf(dt[5]), "global_id=" + st.global_id);
								params.app.dbHelper.updateTableColumn("staseis", "y", Double.valueOf(dt[6]), "global_id=" + st.global_id);
								
								st.name =  dt[8];
								st.sxolia =  dt[7];
								st.date =  (int) t;
								*/
							}
						
					//}
				
					}
				
					
					if(dat.length>0) params.renderer.stasiPoints.regen_coor();
					
					
					
					//pernao nees
					params.logWithTime("--------" + dat0[2]);
					
					if(dat0[2].length()>10){
						

						
						String[] new_staseis = dat0[2].split("#");
			
						for(i=0;i<=new_staseis.length-1;i++){
				
							params.logWithTime(new_staseis[i]);
				
							String[] d = new_staseis[i].split(";");
							/*
							params.logWithTime(-1 + "");
							params.logWithTime(d[1] + "");
							params.logWithTime(Integer.valueOf(d[2]) + "");
							params.logWithTime(Double.valueOf(d[3]) + "");
							params.logWithTime(Double.valueOf(d[4]) + "");
							params.logWithTime(Float.valueOf(d[5]) + "");
							params.logWithTime(d[6] + "");
							params.logWithTime(Integer.valueOf(d[7]) + "");
							*/
							
							MyStasi st = new MyStasi(
		    			 				-1,
		    			 				d[1],
		    			 				Integer.valueOf(d[2]),
		    			 				Double.valueOf(d[3]),
		    			 				Double.valueOf(d[4]),
		    			 				Float.valueOf(d[5]),
		    			 				d[6],
		    			 				Integer.valueOf(d[7]),
		    			 				(int) params.activeProject._id,
		    			 				(int) params.getTime(),
		    			 				Integer.valueOf(params.tabId),
		    			 				Double.valueOf(d[13]),
		    			 				Double.valueOf(d[14]),
		    			 				params
									);
							st._t_base=false;
					    	
					    	if(Integer.valueOf(d[8])==1) st._t_base=true;
					    	st._t_id=Integer.valueOf(d[9]);
					    	st._t_tabId=Integer.valueOf(d[10]);
					    	st._t_project=Integer.valueOf(d[11]);
					    	st.name=d[1];
					    	st.local_date=Integer.valueOf(d[12]);
					    	st.global_id=Integer.valueOf(d[0]);
					    	st.toCast=false;
					    	
					    	
					    	st.stasiPointIndex=params.renderer.stasiPoints.add(st.l, st.f, 0f, 0f,1f);
					    	st.markerIndex = params.renderer.marker.add(st.l, st.f,2,3,4);
							
							
					    	st.log();
					    	
		    	
					    	params.staseis.item.add(st);
				    		//int nid = params.app.dbHelper.addStasiFromSync2ToDb(st,0); 
				    		
				    		//stasi_link_class stl = new stasi_link_class(nid);
					    	//stl.isbase=st._t_base;
					    	//params.app.dbHelper.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
					    	
						}
						
						params.renderer.stasiPoints.regen_coor();
					}
					
					
					params.activeProject.getStasiMaxGlobalId();
					params.surface.requestRender();
				}
	}

	
	
	
	
	
	

	
	
	private void callback_200_MyProject_syncPeriodoi(String data){
		//Idio array kai seira me to MyProject_syncPeriodoiToCloud
		
		params.app.dbHelper.openDataBase();
		
		Log.i("callback_200_MyProject_syncPeriodoi  ",data);
		String[] dat0 = data.split("@@");
		Log.i("callback_200_MyProject_syncPeriodoi  debug",dat0[5]);
		if(dat0[0].contains("900100") && dat0[4].contains("900200")){
			
			
			//String[] dtmet =dat0[3].split("#");
			//for(int y=0;y<dtmet.length-1;y++){
				
				//Log.i("callback_200_MyProject_syncPeriodoi metrisi data ",dtmet[y]);
				//params.app.dbHelper.insert_metrisi_if_not_exists(dtmet[y]);
		//	}
			
			
		String[] dat = dat0[1].split("#");
		long t = Long.valueOf(dat[0]);
		
		int i,k,count=0;
		Log.i("callback_200_MyProject_syncPeriodoi  per count",params.msets.item.size() + "");
		
		for(i=0;i<=params.msets.item.size()-1;i++){
			MyMeasureSet per = params.msets.item.get(i);
			Log.i("callback_200_MyProject_syncPeriodoi  ",dat[i+1]);
				count++;
				String[] dt = dat[count].split(",");
				
				
				if(Long.valueOf(dt[0])==0){
					//if(st.global_id<1){
					Log.i("callback_200_MyProject_syncPeriodoi  ","Added to net");
					//per._t_id=	per._id;
					per.global_id=	Long.valueOf(dt[1]);
					per.date=	(int) t;
					//per.project	=	(int) params.activeProject._id;
					params.app.dbHelper.updateTableColumn("periodos", "global_id", per.global_id, "_id=" + per._id);
					params.app.dbHelper.updateTableColumn("periodos", "date", t, "_id=" + per._id);
					
					//params.app.dbHelper.updateTableColumn("periodos", "date", t, "_id=" +per._t_id);
					//params.app.dbHelper.updateTableColumn("periodos", "_t_id", per._t_id, "_id=" +per._t_id);
					//params.app.dbHelper.updateTableColumn("periodos", "_id", per._id, "_id=" +per._t_id);
					//params.app.dbHelper.updateTableColumn("metrisi", "periodos", per._id, "periodos=" +per._t_id);
					
					
					/*
					if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
						st._t_base=true;
					}else{
						st._t_base=false;
					}
					
					
						st.global_id	=	Long.valueOf(dt[1]);
						st.date			=	(int) t;
						st._t_id		=	(int) st._id;
						st._t_tabId		=	Integer.valueOf(params.tabId);
						st._t_project	=	(int) params.activeProject._id;
						
						Log.i("callback_1_MyProject_syncStaseisToCloud  added", "st.global_id : " + st.global_id + "   -   "  +  "st._t_project : " + st._t_project    +  "st._t_id : " + st._t_id  +  "st._t_base : " + st._t_base);
						
						
							
						//sto telos allazo to id gia na to xrisimopoio
						st._id			=	Long.valueOf(dt[1]);
						//params.app.dbHelper.updateTableColumn("staseis", "_id", st._id, "_id=" + st._t_id);
						params.app.dbHelper.updateStasiDataFromAddToBase(st);
						params.app.dbHelper.updateTableColumn("con_project_stasi", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi", st._id, "stasi=" + st._t_id);
						params.app.dbHelper.updateTableColumn("periodos", "stasi_0", st._id, "stasi_0=" + st._t_id);
						params.app.dbHelper.updateTableColumn("metrisi", "stasi_index", st._id, "stasi_index=" + st._t_id);
						
						*/
				}
				
				if(Long.valueOf(dt[0])==1){
					Log.i("callback_200_MyProject_syncPeriodoi  fixed", "per same version : " + dt[1]);
				}

				if(Long.valueOf(dt[0])==2){
					Log.i("callback_2_MyProject_syncPeriodoiToCloud ", "newer version on tablet : " + dt[1]);
					//st.date=(int) t;callback_200_MyProject_syncPeriodoi
					//st.db_update_timeStamp();
				}
				
				if(Long.valueOf(dt[0])==3){
					//Log.i("callback_1_MyProject_syncStaseisToCloud ", "updating data : " + dat[count]);
					t = Long.valueOf(dt[2]);
					//per.date		=	(int) t;
					//per.global_id	=	Long.valueOf(dt[1]);
					
					
					
					
					/*
					Log.i("callback_2_MyProject_syncPeriodoiToCloud updte data : ",t + " , " + dt[7] + " , " + dt[8]);
					params.app.dbHelper.updateTableColumn("staseis", "date", t, "_id=" + st._id);
					params.app.dbHelper.updateTableColumn("staseis", "sxolia", dt[7], "_id=" + st._id);
					params.app.dbHelper.updateTableColumn("staseis", "name", dt[8], "_id=" + st._id);
					
					st.name =  dt[8];
					st.sxolia =  dt[7];
					st.date =  (int) t;
					
					
					*/
				
					
				
				}

				
			
		}
		
		
		if(dat0[2].length()>10){
		String[] new_periodoi = dat0[2].split("#");
		for(i=0;i<=new_periodoi.length-1;i++){
			Log.i("callback_2_MyProject_syncPeriodoi",new_periodoi[i]);
			
			String[] d = new_periodoi[i].split(";");
	    	 MyMeasureSet per = new MyMeasureSet( );
	    	 Log.i("callback_2_MyProject_syncPeriodoi",new_periodoi[i]);
	    	 per._id			=	-1;
	    	 per.stasi_id		=	(int) params.staseis.get_stasi_id_of_stasi_having_global_id(Integer.valueOf(d[1]));
	    	 per.stasi_0_id		=	(int) params.staseis.get_stasi_id_of_stasi_having_global_id(Integer.valueOf(d[2]));
	    	 per.stasi_0_angle	=	Float.valueOf(d[3]);
	    	 per.project		=	Integer.valueOf(d[4]);
	    	 per.YO				=	Float.valueOf(d[5]);
	    	 per.sxolia			=	d[6];
	    	 per.date			=	Integer.valueOf(d[7]);
	    	 per._t_id			=	Integer.valueOf(d[8]);
	    	 per._t_tabId		=	Integer.valueOf(d[9]);
	    	 per.global_id		=	Long.valueOf(d[0]);
	    	 per.local_date		=	Integer.valueOf(d[10]);
	    	 
	    	 
	    	 Log.i("callback_2_MyProject_syncPeriodoi",new_periodoi[i]);
	    	 
	    	 params.app.dbHelper.addPeriodoToDbFromSync2(per);
	    	 
	    	 
	    	// stasi_link_class stl = new stasi_link_class(i)
	    	 //db.addStasiToDb(st,true,1);
	    	 
	    	 
	    	 
			
		}
		}
		
		Intent intent = params.app.getIntent();
		params.app.finish();
		params.app.startActivity(intent);
		
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	
	
	private void callback_400_MyProject_syncMetriseis(String data){
		//Idio array kai seira me to MyProject_syncPeriodoiToCloud
		
		if(!params.app.dbHelper.isOpen()) params.app.dbHelper.openDataBase();
		
		//Log.i("callback_400_MyProject_syncMetriseis  ",data);
		String[] dat0 = data.split("@@");
		//Log.i("callback_400_MyProject_syncMetriseis  debug",dat0[5]);
		if(dat0[0].contains("900100") && dat0[4].contains("900200")){
			
			
			
		String[] dat = dat0[1].split("#");
		long t = Long.valueOf(dat[0]);
		
		String[] period_data = dat[1].split("@");
		
		//params.logWithTime(dat[1]);
		//params.logWithTime(period_data[0]);
		//params.logWithTime(period_data[1]);
		//params.logWithTime(period_data[2]);
		
		
		int period_global_id 	= Integer.valueOf(period_data[0]);
		int period_tab 			= Integer.valueOf(period_data[1]);
		int period_local_date 	= Integer.valueOf(period_data[2]);
		
		MyMeasureSet per = params.msets.get_period_having_global_id(period_global_id);
		
		int i,k,count=0;
		Log.i("callback_400_MyProject_syncMetriseis  per count",params.msets.item.size() + "");
		
		
		
		for(i=2;i<=dat.length-1;i++){
			MyMeasurement mes = per.itemStaseis.get(count);
			//Log.i("callback_400_MyProject_syncMetriseis  ",dat[i]);
				count++;
				String[] dt = dat[i].split(",");
				
				
				if(Long.valueOf(dt[0])==0){
					Log.i("callback_400_MyProject_syncMetriseis  ","Added to net");
					mes.global_id=	Long.valueOf(dt[1]);
					mes.date=	(int) t;
					params.app.dbHelper.updateTableColumn("metrisi", "global_id", mes.global_id, "_id=" + mes._id);
					params.app.dbHelper.updateTableColumn("metrisi", "date", t, "_id=" + mes._id);
					
					
				}
				
				if(Long.valueOf(dt[0])==1){
					//Log.i("callback_400_MyProject_syncMetriseis  ", "per same version : " + dat[i]);
				}

				if(Long.valueOf(dt[0])==2){
					Log.i("callback_400_MyProject_syncMetriseis ", "newer version on tablet : " + dat[i]);
					
				}
				
				if(Long.valueOf(dt[0])==3){
					t = Long.valueOf(dt[2]);
					Log.i("callback_400_MyProject_syncMetriseis ", "newer version on db : " + dat[i]);
			
				}

				
			
		}
		
			
			
			
			

			
			
			if(dat0[2].length()>10){
		
					String[] new_metriseis = dat0[2].split("#");
					int periodos_id = (int) params.msets.get_period_id_of_period_having_global_id(Integer.valueOf(new_metriseis[0]));
					params.logWithTime(periodos_id);
					params.logWithTime(dat0[2]);
					for(i=1;i<=new_metriseis.length-1;i++){
			
						//params.logWithTime(new_metriseis[i]);
						String[] d = new_metriseis[i].split(";");
						
						
						
						
						MyMeasurement mes = new MyMeasurement();
						mes.periodos=periodos_id;
						mes.global_id=Integer.valueOf(d[0]);
						mes.stasi_index_id = (int) params.staseis.get_stasi_id_of_stasi_having_global_id(Integer.valueOf(d[1]));
						mes.type = Integer.valueOf(d[2]);
						mes.hZ=Float.valueOf(d[3]);
						mes.vZ=Float.valueOf(d[4]);
						mes.sD=Float.valueOf(d[5]);
						mes.ys=Float.valueOf(d[6]);
						
						mes.obtype = Integer.valueOf(d[7]);
						mes.sxolia = d[8];
						mes.date = Integer.valueOf(d[11]);
						
						mes.odefsi_use = false;
						if(Integer.valueOf(d[12])==1) mes.odefsi_use=true;
						
						mes.local_date=Integer.valueOf(d[14]);
						
						
						params.app.dbHelper.addMeasurementToDbFromSync2(mes);
						
						mes.log();
						
						
	    	/*
			    		 //Log.i("sync staseis : " + i,"Not Exist");
			    		params.app.dbHelper.updateStasiToDbProjectTableGivingProject(stl, st._t_project);
			    		params.app.dbHelper.addStasiToDb(st,true,0); 	 
			    		*/	
					}
			}
			
			
		

		
		}
	}
	
	
	
	
	
	
	
	

	}





















