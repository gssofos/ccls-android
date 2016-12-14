package com.geocloud.web;


import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyStasi;

public class DataCast {
	MyParams params;
	TimerTask task;
	public  Timer timer ;
	 
	public DataCast(final MyParams params){
		this.params=params;
		

		task = new TimerTask() {
            public void run() {
                //Log.i("DataCast","DataCast");
                params.datacast.cast_staseis();
            }
        };
        
        //timerKick();
      
	}
	
	
	public void timerKick(){
		try{
			timer.cancel();
			timer=null;
			task.cancel();
			timer = new Timer();
			timer.schedule(task, 0, 10000);
		}catch(Exception ex){
		}
		
		
	}
	
	
	public void set(boolean state){
		try{
			timer.cancel();
			timer=null;
			task.cancel();
			
		}catch(Exception ex){
		}
		 
		if(state) {
			

			task = new TimerTask() {
	            public void run() {
	                //Log.i("DataCast","DataCast");
	                params.datacast.cast_staseis();
	            }
	        };
	        
	        
			timer = new Timer();
			
			timer.schedule(task, 0, 10000);
		}
	}
	/*
	public String intToByteArray(int value) {
	    byte[] bytes = new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	    
	    String s = new String(bytes); // possibly with a charset  
		//char[] chars = s.toCharArray();  
		
		return s;
		
		
	}
	
	
	public String num2byte(int inn){
		return ByteBuffer.allocate(4).putInt(inn).asCharBuffer().array().toString();
	}
	
	
	public String num2byte(long inn){
		return ByteBuffer.allocate(8).putLong(inn).asCharBuffer().array().toString();
	}
	*/
	
	public void cast_staseis(){		
		String out = params.activeProject._id + "#" + params.tabId + "#"+ params.activeProject.max_stasi_global_id + "#";//params.staseis.item.size() + "#";
		
			int i,k,a;
			for(i=0;i<=params.staseis.item.size()-1;i++){
				MyStasi st = params.staseis.item.get(i);
				
				
				
				
				if(st.toCast){
					if(!st.fixed){
						st.log();

						if(params.activeProject.getStasiLinkIsBaseBy_id( st._id ) ){
							 a =1;
						}else{
							 a=0;
						}
						
						String mytbId="-1";
						if(st._t_tabId>0){
							mytbId=String.valueOf(st._t_tabId);
						}else{
							mytbId=params.tabId;
						}
						
						
						out = out+ st._id 
								+ "|" 	+  Double.toHexString(st.f )
								+ "|" 	+  Double.toHexString(st.l )
								+ "|" 	+  Double.toHexString(st.x )
								+ "|" 	+  Double.toHexString(st.y )
								+ "|" 	+  st.sxolia 
								+ "|" 	+  (int) (st.date-1261440000)
								+ "|" 	+  a
								+ "|" 	+  st.name 
								+ "|" 	+  mytbId 
								+ "|"   + st._t_id
								+ "|"   + (int) (st.local_date-1261440000) + "#" ;
														
						//out = out +tmp;
						
					}
				}
				
				
				
			}
			params.logWithTime(out);
			params.web.httpCallPost(params.c_url + "_geo/php/client/_castStaseis.php",out,500,5000);;
			//Log.i("DataCast   cast_staseis  ",out + " ---- " + params.dt);
		    //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
			//params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_syncStaseisToCloud.php",out,100,5000);;
	}
	
	
	
	
}
