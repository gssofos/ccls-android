package com.geocloud.topo;

import java.util.ArrayList;

import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.app.MyGLSE20app;

public class MyHouse {
	ArrayList<MyMeasurement> item = new ArrayList<MyMeasurement>();
		
	public MyPoint point;
		// index sto array tou layer
		int MyMarkerIndex = 0;
		int MyTextIndex = 0;
		int markerIndex=-1;
		MyParams param;
		public long _id;
		
		public 	String 		sxolia;
		public 	int			type;
		public 	int 		floors;
		public 	String		path;
		public 	double		x,y,f,l,h,h_ort;
		public 	int			local_date;
		
		
		
	
		
		public MyHouse( double x, double y,MyGLSE20app myGLSE20app){
			Log.i("MyHouse xy", x + "," + y);
			this.point = new MyPoint(x,y);
			markerIndex = myGLSE20app.mGLRenderer.marker.add(x,y,6,3,4);
			param = myGLSE20app.params;
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			_id=0;
			path	=	"";
			sxolia	=	"";
			type	=	0;
			floors		=	0;
			local_date = -1;
		}
		
		
		
		
		public MyHouse( String sxolia,String path,int floors, int type,double x, double y,MyGLSE20app myGLSE20app){
			this.point = new MyPoint(x,y);
			markerIndex = myGLSE20app.mGLRenderer.marker.add(x,y,6,3,4);
			param = myGLSE20app.params;
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			_id=0;
			this.path	=	path;
			this.sxolia	=	sxolia;
			this.type	=	type;
			this.floors		=	floors;
			local_date = -1;
		}
		
		
		public MyHouse(){
			this.point = new MyPoint(0,0);
			
			sxolia	=	"";
			type	=	0;
			floors		=	0;
			path	=	"";
			local_date = -1;
			
		}
		
		
		
		
		public MyHouse(long _id,String sxolia,int type,int floors, double x, double y,String path){
			this._id=_id;
			this.sxolia = sxolia;
			this.path = path;
			this.floors=floors;
			this.x=x;
			this.y=y;
			local_date = -1;
		}
		
		
		public void setCoorFl(double[] fl){
			this.point.x=fl[1];
			this.point.y=fl[0];
			this.f=fl[0];
			this.l=fl[1];
			
			
			param.renderer.marker.item.get(markerIndex).x=fl[1];
			param.renderer.marker.item.get(markerIndex).y=fl[0];
			
			//ekkremmei egsa;
			
		}
		
		
		public void addToDb(){
			
			param.app.dbHelper.addHouseToDb(this);
		}
		
		
		
		public void log(){
			Log.i("MyHouse log -  _id : " + _id  + "",_id + " , " + this.type + " , " + this.floors 
					+ " , " + this.path 
					+ " , " + this.f 
					+ " , " + this.l 
					+ " , " + this.sxolia 
					
					
					
					+ " , " );
		}
		
		
		public void remove(){
			param.renderer.marker.highlight(markerIndex, false);
			param.renderer.marker.item.remove(markerIndex);
			param.staseis.remove_by_id(this._id);
			param.window_stasi.hide();
			param.mode.set_stasi_explore();
		}
		
		public void highlight(boolean value,int highlightIndex){
			param.renderer.marker.highlight(markerIndex, value,highlightIndex);
		}
		public void highlight(boolean value){
			param.debug(markerIndex + " - " + value);
			param.renderer.marker.highlight(markerIndex, value);
		}
		
		public void db_update_coor(){
			//param.app.dbHelper.updateStasiCoor(this);
		}
		
		public void db_update_global_id(){
			//param.app.dbHelper.updateStasiGlobalId(this);
		}
		
		public void db_update_timeStamp(){
			//param.app.dbHelper.updateStasiTimeStamp(this);
		}
		
		public void db_updateStasiflxysxolianame(){
			//param.app.dbHelper.updateStasiflxysxolianame(this);
		}
		
	
		
}
