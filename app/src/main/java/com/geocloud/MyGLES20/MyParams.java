package com.geocloud.MyGLES20;

import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geocloud.Geometry.MyBbox;
import com.geocloud.Geometry.MyEventManager;
import com.geocloud.Geometry.MyUtil;
import com.geocloud.app.MyGLSE20app;
import com.geocloud.modules.tsComm;
import com.geocloud.topo.MyHouseCollection;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyMeasureSet_collection;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyMode;
import com.geocloud.topo.MyOdefsi;
import com.geocloud.topo.MyProject;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.MyStasiCollection;
import com.geocloud.web.DataCast;
import com.geocloud.web.MyWeb;
import com.geocloud.window.MyWindow_edit;
import com.geocloud.window.MyWindow_edit_data;
import com.geocloud.window.MyWindow_edit_line_meta;
import com.geocloud.window.MyWindow_edit_lineadd;
import com.geocloud.window.MyWindow_edit_linemod;
import com.geocloud.window.MyWindow_edit_linemod_verify;
import com.geocloud.window.MyWindow_edit_point_meta;
import com.geocloud.window.MyWindow_house_meta;
import com.geocloud.window.MyWindow_msg;
import com.geocloud.window.MyWindow_set_Back_Orientation;
import com.geocloud.window.MyWindow_set_yo;
import com.geocloud.window.MyWindow_skop_stasi;
import com.geocloud.window.MyWindow_stasi;
import com.geocloud.window.MyWindow_tools;
import com.geocloud.wms.Wms;

public class MyParams {
	public MyBbox 					bbox;
	public MyGlobalPoint			center;		//kentro
	public MyGlobalPoint			tap;		//tap point
	
	public MyGLSE20app 				app;
	public GL01_MyGLSurfaceView 	surface;
	public GL02_MyGLRenderer 		renderer;
	public Wms 		wms;
	public float					screenRatio;
	
	
	public MyMode			mode;
	public MyOdefsi odefsi;
	public MyProject activeProject;			
	
	public float					pixel2world;
	public int 					imagePixelWidth;
	public int 					imagePixelHeight;
	public int					tapSelectPixelWindow;
	
	public MyWindow_stasi 					window_stasi;;
	public MyWindow_tools 					window_tools;;
	public MyWindow_skop_stasi				window_stasi_skop;
	public MyWindow_set_Back_Orientation  	window_back_orientation;
	public MyWindow_msg  					window_msg;
	public MyWindow_edit  					window_edit;
	public MyWindow_edit_data  					window_edit_data;
	public MyWindow_edit_lineadd  					window_edit_lineadd;
	public MyWindow_edit_linemod  					window_edit_linemod;
	public MyWindow_edit_linemod_verify  					window_edit_linemod_verify;
	public MyWindow_edit_line_meta  					window_edit_line_meta;
	public MyWindow_edit_point_meta  					window_edit_point_meta;
	public MyWindow_house_meta  					window_house_meta;
	public MyWindow_set_yo 					window_yo;
	public MyStasiCollection				staseis;
	public MyHouseCollection				houses;
	public MyMeasureSet_collection msets;
	
	public String tabId;
	public MyUtil util;
	
	public int selectedStasi;
	
	public MyEventManager		event;
	public MyWeb				web;
	public tsComm				tscomm;
	//public int skopefsiStasiIndex=-1;
	public MySerial serial;
	public String baseDir;
	public boolean readCommCancelFlag = false;
	public boolean stasiIconShow;
	public boolean allow_download;	//true = it can download
	public boolean show_map;	//true = it can download
	public float screen_scale_factor	=	1.0f;
	public long dt=-1;
	public DataCast datacast;
	public String odefsi_solution_csv_string;
	
	
	public String c_url = "http://192.168.1.104/";
	
	public MyParams(){
	}
	
	
	public MyParams(MyGLSE20app 	app){
		this.app 		= 	app;
		bbox 			= 	new MyBbox();
		center			=	new MyGlobalPoint();
		tap				=	new MyGlobalPoint();
		allow_download	=	false;
		show_map		=	false;
		odefsi 			= 	new MyOdefsi(this);
		mode			=	new MyMode(this);
		event			=	new MyEventManager(this);
		web 			= 	new MyWeb(this);
		msets 			= 	new MyMeasureSet_collection(this);
		selectedStasi=-1;
		util = new MyUtil(this);
		datacast = new DataCast(this);
		tabId=null;
		stasiIconShow=false;
		odefsi_solution_csv_string="";
		
	}
	
	
	
	
	public long getTime(){
		long current_time_sec = System.currentTimeMillis()/1000;
		
		return current_time_sec + dt;
	}
	
	public void setRenderer(GL02_MyGLRenderer 		mGLRenderer){
		this.renderer = mGLRenderer;
	}
	
	public void setSurfaceView(GL01_MyGLSurfaceView 	mGLSurfaceView){
		this.surface = mGLSurfaceView;
	}
	
	public void setScreenRatio(float ratio){
		this.screenRatio = ratio;
	}
	
	
	public void setImagePixelSize(int width, int height){
		imagePixelWidth=width;
		imagePixelHeight=height;
		tapSelectPixelWindow=(int) Math.round(0.10*height*screen_scale_factor/2);;
	}
	
	public void debug(String msg){
		final String ms = msg;
		this.app.runOnUiThread(new Runnable() {@Override  public void run() {	app.text_debug.setText(ms); }});
		
	}
	
	public double[] getCenterCoor(){
		double[] out = center.world;
		return out;
	}
	
	public void logWithTime(String msg){
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		long t = getTime();
		if(msg.length()>100){
			Log.i("Params" ,t + " : "  + ste[1+2].getMethodName() );
			Log.i("Params" ,t + " : " +  msg);
		}else{
			Log.i("Params" ,ste[1+2].getMethodName() + " ==>  "  +  getTime() + " : " +  msg);
		}
		
	}
	
	
	
	public void logWithTime(long msg){
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		long t = getTime();
		
			Log.i("Params" ,ste[1+2].getMethodName() + " ==>  "  +  getTime() + " : " +  msg);
		
		
	}
	
	
	
	public void logvWithTime(String msg){
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		long t = getTime();
		if(msg.length()>100){
			Log.v("Params" ,t + " : "  + ste[1+2].getMethodName() );
			Log.v("Params" ,t + " : " +  msg);
		}else{
			Log.v("Params" ,ste[1+2].getMethodName() + " ==>  "  +  getTime() + " : " +  msg);
		}
		
	}
	
	
	
	public void logvWithTime(long msg){
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		long t = getTime();
		
			Log.v("Params" ,ste[1+2].getMethodName() + " ==>  "  +  getTime() + " : " +  msg);
		
		
	}
	
	public void logData(){
		
		Log.i("Log Data", "  ");
		Log.i("Project ", "_id : " + this.activeProject._id);
		Log.i("Project ", "name : " + this.activeProject.name);
		Log.i("Log Data", "  ");
		
		int i,j;
		for(i=0;i<=this.staseis.item.size()-1;i++){
			MyStasi tmp = this.staseis.item.get(i);
			Log.i("Stasi " + i, "_id : " + tmp._id);
			Log.i("Stasi " + i, "f : " + tmp.f);
			Log.i("Stasi " + i, "l : " + tmp.l);
			Log.i("Stasi " + i, "x : " + tmp.x);
			Log.i("Stasi " + i, "y : " + tmp.y);
			Log.i("Stasi " + i, "type : " + tmp.type);
			Log.i("Log Data", "  ");
		}
		
		Log.i("Log Data", "  ");
		for(i=0;i<=this.msets.size()-1;i++){
			MyMeasureSet tmp = this.msets.get(i);
			Log.i("Periodos " + i, "_id : " + tmp._id);
			Log.i("Periodos " + i, "YO : " + tmp.YO);
			Log.i("Periodos " + i, "stasi : " + tmp.stasi);
			Log.i("Periodos " + i, "stasi_0 : " + tmp.stasi_0);
			Log.i("Periodos " + i, "stasi_0_angle : " + tmp.stasi_0_angle);
			Log.i("Log Data", "  ");
			
			for(j=0;j<=tmp.itemStaseis.size()-1;j++){
				MyMeasurement tmp2 = tmp.itemStaseis.get(j);
				Log.i("Metrisi " + j, 		" p: " + tmp2.periodos 
											+ " t: " + tmp2.type
											+ " st: " + tmp2.stasi_index
											+ " hZ: " + tmp2.hZ
											+ " vZ : " + tmp2.vZ	
											+ " sD : " + tmp2.sD
											+ " hD : " + tmp2.hD
						);
				
				//Log.i("Metrisi " + j, "type : " + tmp2.type);
				
				//Log.i("Metrisi " + j, "type : " + tmp2.type);
				//Log.i("Metrisi " + j, "stasi_index : " + tmp2.stasi_index);
				//Log.i("Metrisi " + j, "hZ : " + tmp2.hZ);
				//Log.i("Metrisi " + j, "sD : " + tmp2.sD);
				//Log.i("Log Data", "  ");
				
			}
			
			
			for(j=0;j<=tmp.item.size()-1;j++){
				MyMeasurement tmp2 = tmp.item.get(j);
				Log.i("Metrisi " + j, 		" p: " + tmp2.periodos 
											+ " t: " + tmp2.type
											+ " st: " + tmp2.stasi_index
											+ " hZ: " + tmp2.hZ
											+ " sD : " + tmp2.sD	
						);
				
				//Log.i("Metrisi " + j, "type : " + tmp2.type);
				
				//Log.i("Metrisi " + j, "type : " + tmp2.type);
				//Log.i("Metrisi " + j, "stasi_index : " + tmp2.stasi_index);
				//Log.i("Metrisi " + j, "hZ : " + tmp2.hZ);
				//Log.i("Metrisi " + j, "sD : " + tmp2.sD);
				//Log.i("Log Data", "  ");
				
			}
			
			
		}
		
		
		
	}
	
	
	
}



class MyGlobalPoint{
	public double[] world = new double[2]; 
	public float[] 	pixel = new float[2]; 
	public float[] 	render = new float[2]; 
	
	public MyGlobalPoint(){
		
	}
	
	public void setWorld(double x, double y){
		world[0]=x;
		world[1]=y;
	}
	
	public void setPixel(float x, float y){
		pixel[0]=x;
		pixel[1]=y;
	}
	
	public void setRender(float x, float y){
		render[0]=x;
		render[1]=y;
	}
	
	

	public void log(String inn){
		Log.i("WorldPoint pixel : " + inn,"[" + (float)Math.round(pixel[0]*100000)/100000 + "," +  (float)Math.round(pixel[1]*100000)/100000  + "]");
		Log.i("WorldPoint render : " + inn,"[" + (float)Math.round(render[0]*100000)/100000 + "," +  (float)Math.round(render[1]*100000)/100000  + "]");
		Log.i("WorldPoint world : " + inn,"[" + (float)Math.round(world[0]*100000)/100000 + "," +  (float)Math.round(world[1]*100000)/100000  + "]");
		
		
		
	}
	
	
	
	
	
	
	
	
	
}