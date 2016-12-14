package com.geocloud.surfaceView;


import java.io.File;
import java.util.ArrayList;

import com.geocloud.MyGLES20.D_Square_Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class Tile {
	
	private 	String 		filename;
	private 	String 		url;
	private 	int 		level;
	private 	float		f;
	private 	float		l;
	public 	float		f1;
	public 	float		l1;
	public		int			indX;
	public		int 		indY;
	public 		Bitmap 		bmp;
	public		boolean		loaded	=	false;
	
	public Tile(float l, float f, int level,String filename, String url, int indX, int indY,float l1, float f1){
		this.l			=	l;
		this.f			=	f;
		this.l1			=	l1;
		this.f1			=	f1;
		this.level 		=	level;
			this.filename 	=	filename;
			this.url		=	url;
			this.indX 		=	indX;
			this.indY 		=	indY;
			
			if(new java.io.File(filename).exists() ){
				BitmapFactory.Options options 	= 	new BitmapFactory.Options();
				options.inPreferredConfig 		= 	Bitmap.Config.ARGB_8888;
				this.bmp 						= 	BitmapFactory.decodeFile(filename, options);
				this.loaded						=	true;
			}else{
				
				
			}
	}
	
	
	
	
	
	
	
	
	
	
			
			
		
		/**
		 * @author Prabu
		 * Private class which runs the long operation. ( Sleeping for some time )
		 */
		private class KtimaDownloader extends AsyncTask<String, Void, Long> {
			 	ArrayList<D_Square_Image> item;// = new ArrayList<D_Square_Image>();
				boolean download;
				
				public KtimaDownloader(ArrayList<D_Square_Image> item,boolean download){
					this.item = item;
					this.download=download;
					
				}
				
				
			    protected Long doInBackground(String... param) {
			    	String filename = param[0];
			    	String myurl = param[1];
			    	float indX = Float.valueOf( param[2]);
			    	float indY = Float.valueOf( param[3]);
			    	int imageIndexX = Integer.valueOf( param[4]);
			    	int imageIndexY = Integer.valueOf( param[5]);
			    	try {
		            	
			    		//mGLSurfaceView.requestRender();
		            	if(new java.io.File(filename).exists() ){
		            		//Log.i("exists" ,filename );
		            		 File file = new File(filename);
		            		
		            		if(file.length()>18000){
		            		// item.add(new D_Square_Image(filename));
		            		// item.get(item.size()-1).set_coor_data((float) indX, (float) indY,17,imageIndexX, imageIndexY,gssize);
		            		}else{
		            			 Log.i("exists" ,String.valueOf(file.length()) );
		            		}
		            		
		            		//item.get(item.size()-1).set_filename(filename);
		            		//item.get(item.size()-1).set_mTextureDataHandle(getBaseContext());
		            	}else{
		            		if(this.download){
		            			/*
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
		        		            
		        		            
		        		            item.add(new D_Square_Image(filename));
		                    		item.get(item.size()-1).set_coor_data((float) indX, (float) indY,17,imageIndexX, imageIndexY,gssize);
		        				
		        				} catch (MalformedURLException e) {
		        					// TODO Auto-generated catch block
		        					//e.printStackTrace();
		        					//this.activity.infoDisplay.setText(filename);;
		        					Log.i("download error" ,e.toString() );
		        				} catch (IOException e) {
		        					// TODO Auto-generated catch block
		        					//e.printStackTrace();
		        					//this.activity.infoDisplay.setText(filename);;
		        					Log.i("download error" ,e.toString() );
		        				} 
		        				//item.add(new D_Square_Image());
		        				//item.get(item.size()-1).set_coor_data((float) 0, (float) 0,17,(Integer) dat[6], (Integer) dat[7]);
		                		//item.get(item.size()-1).set_filename(filename);
		        				Log.i("download" ,filename );*/
		        				
		            		}
		        				
		        				
		            	}
		            } catch (Exception e) {
		                // TODO Auto-generated catch block
		            }
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
