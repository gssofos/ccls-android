package com.geocloud.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import tw.com.prolific.driver.pl2303.PL2303Driver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyParams;

public class tsComm {
//total station communicator
	
		MyParams params;
	
		PL2303Driver mSerial;
		private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
		
		
		private static final int REQUEST_ENABLE_BT = 1;
		  private BluetoothAdapter btAdapter = null;
		  public BluetoothSocket btSocket = null;
		  public  BluetoothDevice btDevice=null;
		 // private OutputStream outStream = null;
		   
		  // Well known SPP UUID
		  private static final UUID SPP_UUID =
		      UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
		  
		  
		
		
		  
		  
		public boolean chkConnection(){
			boolean out = false;
			if(btConnected()) out = true;
			
			
			return out;
		}
		
		
		
		public boolean btConnected(){
			boolean out = false;
			if(btDevice!=null){
				if(btSocket!=null){
					if(btSocket.isConnected()) out=true;
				}
			}
			
			
			if(out){
				params.app.bt_button.setImageResource(R.drawable.bt_on);
			}else{
				params.app.bt_button.setImageResource(R.drawable.bt_off);
			}
			
			return out;
		}
		
		
		
		
		public boolean prolificConnected(){
			return false;
		}
		
		
		
		public void btToggle(){
			
			
			if(!btConnected()){
				if(btConnect()){
					params.debug("0");
					params.app.bt_button.setImageResource(R.drawable.bt_on);
					//btClearBuffer();
				}else{
					params.debug("1");
					params.app.bt_button.setImageResource(R.drawable.bt_off);
				}
			}else{
				params.debug("2");
				btDisconnect();
				params.app.bt_button.setImageResource(R.drawable.bt_off);
			}
		}
		
		
		public boolean btConnect(){
			boolean out = false;
			
			btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter.isDiscovering()) {
				btAdapter.cancelDiscovery();
			}
			
			
			Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
			int i;
			Object[] bta = bt.toArray();
			btDevice=null;
			for(i=0;i<=bta.length-1;i++		){
				if(((BluetoothDevice) bta[i]).getName().contains("HC-06")){
					 btDevice = (BluetoothDevice) bta[i];
				}
				
				//Log.i("tsComm" ,((BluetoothDevice) bta[i]).getBondState() + " : "  + ((BluetoothDevice) bta[i]).getName() );
				
			}
			 try {
				if(btDevice!=null){
					  btSocket = btDevice.createRfcommSocketToServiceRecord(SPP_UUID);
						if(!btSocket.isConnected()){
					    	   btSocket.connect();
					    	   out = true;
					      }else{
					    	  out=true;
					      }
					      Log.i("tsComm" ,btDevice.getName());
				 }
			     
				
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out=false;
			}
			//while (bt.iterator().hasNext()){
				//BluetoothDevice btd = bt.iterator().next();
				//bt.toArray()
			//	Log.i("tsComm" ,btd.getName() );
			//}
			return out;
			 
			 
		}
		
		public void btDisconnect(){
			btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter.isDiscovering()) {
				btAdapter.cancelDiscovery();
			}
			
			
			
			try {
				btSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	public tsComm(MyParams params,PL2303Driver mSerial){
		this.params=params;
		this.mSerial = mSerial;
		
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}
		
		
		
		//btConnect();
		
		
		
	}
	
	
	
	
	
	
	

	
	public void prolific_open() {
	   	 if(null==mSerial)
			   return;   	  
	       if (mSerial.isConnected()) {
	           
	    	   params.debug( "openUsbSerial : isConnected ");
	          
	           String str = "9600";
	           int baudRate= Integer.parseInt(str);
			   switch (baudRate) {
	             	case 9600:
	             		mBaudrate = PL2303Driver.BaudRate.B9600;
	             		break;
	             	case 19200:
	             		mBaudrate =PL2303Driver.BaudRate.B19200;
	             		break;
	             	case 115200:
	             		mBaudrate =PL2303Driver.BaudRate.B115200;
	             		break;
	             	default:
	             		mBaudrate =PL2303Driver.BaudRate.B9600;
	             		break;
	           }   		            
			   params.debug( "baudRate:"+baudRate);
			  // if (!mSerial.InitByBaudRate(mBaudrate)) {
	           if (!mSerial.InitByBaudRate(mBaudrate,700)) {
	        	   if(!mSerial.PL2303Device_IsHasPermission()) {
	        		   params.debug( "cannot open, maybe no permission");		
					}
					
	               if(mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
	            	   params.debug("cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
	               }
	           } else {        	      
	                  //Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();        	   
	           }
	       }//isConnected
	       else{
	    	   
	    	   params.debug("mSerialNotConnected");
	       }
	      
	   }//openUsbSerial
	
	
	

	   public void prolific_write(String msg) {
		  	if(null==mSerial)
					return;
		   	if(!mSerial.isConnected()) 
		   		return;
	   	
		   	String strWrite = msg ;
	       	int res = mSerial.write(strWrite.getBytes(), strWrite.length());
			if( res<0 ) {
				return;
			} 
	   }
	   
	   
	   
	   
	   public void btWrite(String msg){
		   
		  try {
			OutputStream os =  btSocket.getOutputStream();
			String fullmsg = msg;
			os.write(fullmsg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	   }
	   
	   
	   
	   
	   public void btRead_trash(){
		   try {
				InputStream os =  btSocket.getInputStream();
				byte[] rbuf = new byte[4096];
			    Thread.sleep(200);	 
			    os.read(rbuf);
			    	
			    Thread.sleep(200);	 
			    os.read(rbuf);
			    	
			 	   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e){
				//Log.i("btRead_with_callback exception",e.toString());
			}
		   
	   }
	   
	   public void btRead_with_callback(int callback_function_index,final boolean inn){
		   //Log.i("btRead_with_callback",callback_function_index + "" + params.readCommCancelFlag);
		   
		   try {
			 //  Log.i("btRead_with_callback",callback_function_index + "" + params.readCommCancelFlag);
			  
			InputStream os =  btSocket.getInputStream();
			//Log.i("btRead_with_callback",callback_function_index + "" + params.readCommCancelFlag);
			   
			int len=0;
		       byte[] rbuf = new byte[4096];
		       StringBuffer sbHex=new StringBuffer();
		       
		       
		       
		       int counter = 0;
		      // Log.i("btRead_with_callback",len + "," + counter +"," + !params.readCommCancelFlag);
			   
		       
		       //params.debug("start reading 5" + params.readCommCancelFlag);;
		       while(/*len<5*/  sbHex.toString().split(",").length<5 && /*counter<40 &&*/ !params.readCommCancelFlag){
		    	   counter+=1;
		    	   //Log.i("btRead_with_callback",len + "," + counter +"," + !params.readCommCancelFlag);
					 
		    	   len = os.read(rbuf);
		    	   //Log.i("btRead_with_callback",len + "," + counter +"," + !params.readCommCancelFlag);
					  
		    	   if (len > 0) {  
		    		   Thread.sleep(200);
		                for (int j = 0; j < len; j++) {            	   
		                 sbHex.append((char) (rbuf[j]&0x000000FF));
		               }  
		               
		        }
		    	  // Log.i("btRead_with_callback",counter + "" + params.readCommCancelFlag);
		       }
		      
		      /* len=0;
		       Thread.sleep(200);
		       len = os.read(rbuf);
		       if (len > 0) {  
	    		   //Thread.sleep(200);
	                for (int j = 0; j < len; j++) {            	   
	                 sbHex.append((char) (rbuf[j]&0x000000FF));
	               }  
	               
	        }*/
		       
		    	   final String data = sbHex.toString();
		    	   
		    	   
		    	   // params.debug("start reading 6");;
			       if(callback_function_index==1){
			    	   params.app.runOnUiThread(new Runnable() { public void run() { 
			    		   String dataout = data;
			    		   if(params.readCommCancelFlag || data.length()<5) dataout="";
				    	   	params.window_back_orientation.setBSCallback(dataout);
				    	 } });
			    	 
			    	   	};
			       if(callback_function_index==2){
			    	   params.app.runOnUiThread(new Runnable() { public void run() { 
			    		   String dataout = data;
			    		   if(params.readCommCancelFlag || data.length()<5 ) dataout="";
			    	   params.window_stasi_skop.meas_all_callBack(dataout,inn);
			    	   } });
			    	   };
			    	   
			    	   
			    	   
			    	   if(callback_function_index==3){
				    	   params.app.runOnUiThread(new Runnable() { public void run() { 
				    		   String dataout = data;
				    		   if(params.readCommCancelFlag || data.length()<5 ) dataout="";
				    		   	params.window_stasi_skop.meas_angle_callBack(dataout,inn);
				    	   } });
				    	   };
		    	   
		    	   
		    	   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			//Log.i("btRead_with_callback exception",e.toString());
		}
		
	   }
	   
	   
	   
	   
	   
	   

	   public void btClearBuffer(){
		   try {
			InputStream os =  btSocket.getInputStream();
			int len=0;
		       byte[] rbuf = new byte[4096];
		       StringBuffer sbHex=new StringBuffer();
		       len = os.read(rbuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("ex",e.toString());
			e.printStackTrace();
		} 
	   }
	   
	   
	   
	   
	   
	   
	   public boolean check_connection(){
		   if(null==mSerial)					return false;
	   		if(!mSerial.isConnected())   		return false;
	   		return true;
	   }

	   
		
		
		
		
		
		
		public void readCommandWithCallback(int callback_function_index,boolean inn){
			params.debug("start reading");;
			readCommand_Class runner = new readCommand_Class(callback_function_index,inn);
			runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); 
		}
		
		
		
		public boolean writeCommand(String command){
			String mycommand = "" ;
			
			if(check_connection() || this.chkConnection()){
			if(command=="angle"){
				params.debug("angle");;
				mycommand=get_angle_string("kolida");
				writeCommand_Class runner = new writeCommand_Class(mycommand);
				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
				
				
				mycommand=get_angle_string("topon");
				writeCommand_Class runner2 = new writeCommand_Class(mycommand);
				runner2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
				
				
			}else if(command=="slope"){
				
				params.debug("slope");;
				mycommand=get_slope_string("kolida");
				writeCommand_Class runner = new writeCommand_Class(mycommand);
				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
				
				
				mycommand=get_slope_string("topon");
				writeCommand_Class runner2 = new writeCommand_Class(mycommand);
				runner2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
				
					
			}else{
				mycommand=command + Character.toString((char) 13)+Character.toString((char) 10);
				writeCommand_Class runner = new writeCommand_Class(mycommand);
				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
				
			
			}
				
				 //params.app.prolific_write(command);
					//writeCommand_Class runner = new writeCommand_Class(mycommand);
					//runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mycommand); 
			}
			
			return (check_connection() || this.chkConnection());
		}
		
		
		
	   public void prolific_read_with_callback(int callback_function_index,final boolean inn) throws InterruptedException {
		   //Thread.sleep(1220);
	       int len=0;
	       byte[] rbuf = new byte[4096];
	       StringBuffer sbHex=new StringBuffer();
	      // params.debug("start reading 3");;
	       if(null==mSerial)
				return;        
	       //params.debug("start reading 4");;
	       if(!mSerial.isConnected()) 
	       	return;
	       int counter = 0;
	      
	       
	       //params.debug("start reading 5" + params.readCommCancelFlag);;
	       while(len<5 && counter<40 && !params.readCommCancelFlag){
	    	   counter+=1;
	    	   len = mSerial.read(rbuf);
	    	   if (len > 0) {  
	    		   Thread.sleep(200);
	                for (int j = 0; j < len; j++) {            	   
	                 sbHex.append((char) (rbuf[j]&0x000000FF));
	               }  
	               
	        }
	    	  // Log.i("lll",counter + "");
	       }
	      
	    	   final String data = sbHex.toString();
	      
	      
	      // params.debug("start reading 6");;
	       if(callback_function_index==1){
	    	   params.app.runOnUiThread(new Runnable() { public void run() { 
	    		   String dataout = data;
	    		   if(params.readCommCancelFlag || data.length()<5) dataout="";
		    	   	params.window_back_orientation.setBSCallback(dataout);
		    	 } });
	    	 
	    	   	};
	       if(callback_function_index==2){
	    	   params.app.runOnUiThread(new Runnable() { public void run() { 
	    		   String dataout = data;
	    		   if(params.readCommCancelFlag || data.length()<5 ) dataout="";
	    	   params.window_stasi_skop.meas_all_callBack(dataout,inn);
	    	   } });
	    	   };
	        
	       
	   }//readDataFromSerial
	   
	  
	   
		







	private class writeCommand_Class extends AsyncTask<String, Void, Long> {
	   			String mycommand;
	   		
		   		public writeCommand_Class(String mycommand){	
		   			this.mycommand = mycommand;	
		   		}
		   	    protected Long doInBackground(String... param) {	
		   	    	if(chkConnection()){
		   	    		//btClearBuffer();
		   	    		
						btWrite(mycommand);
					}else{
						params.app.prolific_open();
						 
						 //gia na mi pairnei piso data apo proigoumeni klisi
						 byte[] rbuf = new byte[4096];
						 mSerial.read(rbuf);
						 
						 
						prolific_write(mycommand);
					}
		   	    		
		   	    	return (long) 5.0;
		   	    }
	   	    }
	   	    
	   	

	   
	   


	   private class readCommand_Class extends AsyncTask<String, Void, Long> {
		   		int callback_function_index;
		   		boolean inn;			//flag gia ti callback function
		   		public readCommand_Class(int callback_function_index,boolean inn){	
		   			this.callback_function_index = callback_function_index;	
		   			this.inn=inn;
		   			
		   		}
		   	    protected Long doInBackground(String... param) {	
		   	    	params.debug("start reading 2");;
					
		   	    	try{
		   	    		if(chkConnection()){
		   	    			params.debug("bt start reading");;
		   	    			btRead_with_callback(callback_function_index,inn);
		   	    			params.debug("bt started reading");;
		   	    		}else{
		   	    			prolific_read_with_callback(callback_function_index,inn);
		   	    		}
		   	    		
		   	    	}catch (Exception ex){
		   	    		Log.i("readCommand_Class",ex.toString());
		   	    	}
		   	    	return (long) 5.0;
		   	    }
	   	    }
	   
	   


	   
	   
	   private String get_slope_string(String type){
		   String out = "";
			
		   if(type=="topcon"){
			   
			   // set MODE
				out =			Character.toString((char) 90) 
							+ 	Character.toString((char) 51) 
							+ 	Character.toString((char) 52) 
							+ 	Character.toString((char) 48) 
							+ 	Character.toString((char) 57) 
							+ 	Character.toString((char) 51) 
							+ 	Character.toString((char) 3) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
				
				
				
				//ask data
				out +=			Character.toString((char) 67) 
							+ 	Character.toString((char) 48) 
							+ 	Character.toString((char) 54) 
							+ 	Character.toString((char) 55) 
							+ 	Character.toString((char) 3) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
		   }
		   
		   
		   if(type=="kolida"){
				out =			Character.toString((char) 69) 
							+ 	Character.toString((char) 97) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
		   }
		   
		   
		   
		   
		   return out;
		   
	   }
	   
	   
	   
	   
	   
	   
	   private String get_angle_string(String type){
		   String out = "";
			
		   if(type=="topcon"){
			   
			   // set MODE
				out =			Character.toString((char) 90) 
							+ 	Character.toString((char) 49) 
							+ 	Character.toString((char) 48) 
							+ 	Character.toString((char) 48) 
							+ 	Character.toString((char) 57) 
							+ 	Character.toString((char) 49) 
							+ 	Character.toString((char) 3) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
				
				
				
				//ask data
				out +=			Character.toString((char) 67) 
							+ 	Character.toString((char) 48) 
							+ 	Character.toString((char) 54) 
							+ 	Character.toString((char) 55) 
							+ 	Character.toString((char) 3) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
		   }
		   
		   
		   if(type=="kolida"){
				out =			Character.toString((char) 69) 
							+ 	Character.toString((char) 101) 
							+ 	Character.toString((char) 13)
							+	Character.toString((char) 10);
		   }
		   
		   
		   
		   
		   return out;
		   
	   }
	   
	   
	
}
