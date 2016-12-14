package com.geocloud.MyGLES20;

import tw.com.prolific.driver.pl2303.PL2303Driver;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

public class MySerial {
	MyParams params;
	PL2303Driver mSerial;
	String TAG = "PL2303HXD_APLog";
	 //BaudRate.B4800, DataBits.D8, StopBits.S1, Parity.NONE, FlowControl.RTSCTS
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
    private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
    private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
    private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
    private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.DTRDSR;
  
    private static final String ACTION_USB_PERMISSION = "com.geocloud.MyGLES20.USB_PERMISSION";   
    public MySerial(MyParams params){
    	this.params=params;
    	params.debug("Serial startup");;
    	mSerial = new PL2303Driver((UsbManager) params.app.getSystemService(params.app.getBaseContext().USB_SERVICE),
    			 params.app, ACTION_USB_PERMISSION,false);
    	
    	//if(mSerial.PL2303Device_IsSupportChip()) params.debug("PL2303Device_IsSupportChip");
    	//if(mSerial.PL2303Device_IsHasPermission()) params.debug("PL2303Device_IsSupportChip");
    	params.debug("s:" + mSerial.PL2303Device_IsSupportChip() + " , " + "p:" + mSerial.PL2303Device_IsHasPermission());
    	 if (!mSerial.PL2303USBFeatureSupported()) {
    		 params.debug("No Support USB host API");;
  			//Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT)
  					//.show();

  			//Log.d(TAG, "No Support USB host API");

  			mSerial = null;

  		}
    	 
    	
    }
	public void openUsbSerial() {
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
	
	
	
	
	
	 
    private void readDataFromSerial() {

        int len;
        byte[] rbuf = new byte[4096];
        StringBuffer sbHex=new StringBuffer();
        
        Log.d(TAG, "Enter readDataFromSerial");

		if(null==mSerial)
			return;        
        
        if(!mSerial.isConnected()) 
        	return;
        
        len = mSerial.read(rbuf);
        if(len<0) {
        	Log.d(TAG, "Fail to bulkTransfer(read data)");
        	return;
        }

        if (len > 0) {        	
               //if (SHOW_DEBUG) {
            	   Log.d(TAG, "read len : " + len);
              // }                
               //rbuf[len] = 0;
               for (int j = 0; j < len; j++) {            	   
                 //String temp=Integer.toHexString(rbuf[j]&0x000000FF);
                 //Log.i(TAG, "str_rbuf["+j+"]="+temp);
                 //int decimal = Integer.parseInt(temp, 16);
                 //Log.i(TAG, "dec["+j+"]="+decimal);
                 //sbHex.append((char)decimal);
                 //sbHex.append(temp);
            	   sbHex.append((char) (rbuf[j]&0x000000FF));
               }        
               Log.i("read serial",sbHex.toString());
               //etRead.setText(sbHex.toString());    
               //Toast.makeText(this, "len="+len, Toast.LENGTH_SHORT).show();
        }
        else {     	
        	// if (SHOW_DEBUG) {
               Log.d(TAG, "read len : 0 ");
           //  }
        	// etRead.setText("empty");
        	 return;
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Leave readDataFromSerial");	
    }//readDataFromSerial
    
    public void writeDataToSerial(String msg) {
    	 
    	Log.d(TAG, "Enter writeDataToSerial");
    	
		if(null==mSerial)
			return;
    	
    	if(!mSerial.isConnected()) 
    		return;
    	
        String strWrite = msg + Character.toString((char) 13)+Character.toString((char) 10);
        /*
        //strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
       // strWrite = changeLinefeedcode(strWrite);
         strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
         if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
		if( res<0 ) {
			Log.d(TAG, "setup: fail to controlTransfer: "+ res);
			return;
		} 
		
		Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();  
		*/
        // test data: 600 byte
		//strWrite="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		// if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write 2(" + strWrite.length() + ") : " + strWrite);
       // }
		 int res = mSerial.write(strWrite.getBytes(), strWrite.length());
		if( res<0 ) {
			Log.d(TAG, "setup2: fail to controlTransfer: "+ res);
			return;
		} 
		
		//Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show(); 

		Log.d(TAG, "Leave writeDataToSerial");
    }//writeDataToSerial
    
    
    
}
