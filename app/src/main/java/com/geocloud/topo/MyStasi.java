package com.geocloud.topo;

import java.util.ArrayList;

import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.app.MyGLSE20app;

public class MyStasi {
	ArrayList<MyMeasurement> item = new ArrayList<MyMeasurement>();
	ArrayList<pairAzimuth> 		azimuth = new ArrayList<pairAzimuth>();
	
	public MyPoint point;
		public String  name = "";
		float 	YO	=	0.0f;
		// index sto array tou layer
		int MyMarkerIndex = 0;
		int MyTextIndex = 0;
		public int markerIndex=-1;
		public int stasiPointIndex=-1;
		MyParams param;

		public ArrayList<stasi_link_class> 		stigmiotipokarastasisstaseonkatatinepilisi = new ArrayList<stasi_link_class>();
		public long prevstasikatatinepilisi;
		
		public 	boolean 	fixed;	
		//public 	boolean 	locked;	//		einai aftes pou erxontai apo online db kai einai ousiastika kleidomenes
		public 	float 		shor,sx,sy,sh;
		public 	String 		sxolia;
		public 	int			type;
		public 	long 		_id;
		public 	String		user,foto;
		public 	double		x,y,f,l,h,h_ort;
		public 	int			date;
		public 	int			local_date;
		public int 			_t_id;
		public int			_t_project;
		public int 			_t_tabId;
		public boolean 			_t_base;
		public long      global_id;
		public boolean deleted=false;
		public boolean toCast=false;
		/*public MyStasi(MyGLSE20app myGLSE20app,){
			param = myGLSE20app.params;
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			
			fixed	=	false;
			shor	=	-1;		sx	=	-1;		sy	=	-1;		sh	=	-1;
			sxolia	=	"";
			type	=	0;
			_id		=	0;
			user	=	"";
			foto	=	"";
			date	=	-1;
			
		}*/
		
		/*
		public MyStasi(String name, float x, float y,MyGLSE20app myGLSE20app){
			this.name = name;
			this.point = new MyPoint(x,y);
			markerIndex = myGLSE20app.mGLRenderer.marker.add(x,y,2,3,4);
			param = myGLSE20app.params;
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			
			fixed	=	false;
			shor	=	-1;		sx	=	-1;		sy	=	-1;		sh	=	-1;
			sxolia	=	"";
			type	=	0;
			_id		=	0;
			user	=	"";
			foto	=	"";
			date	=	-1;
			
		}*/
		
		
		public void paint(float r, float g, float b){
			param.renderer.stasiPoints.item.get(this.stasiPointIndex).r=r;
			param.renderer.stasiPoints.item.get(this.stasiPointIndex).g=g;
			param.renderer.stasiPoints.item.get(this.stasiPointIndex).b=b;
		}
		
		
		public void log(){
			Log.i("MyStasi log -  _id : " + _id  + "(" + date + ")",_id + " , " + name + " , " + _t_project + " , " + _t_tabId + " , " + _t_base + " , " + local_date + " , " + global_id + "," + toCast);
		}
		public void logcoor(){
			Log.i("MyStasi log -  _id : " + _id  + "(" + date + ")",_id + " , " + name + " , x:" + x + " , y:" + y + " , f:" + f + " , l:" + l );
		}
		
		
		
		
		public String name(){
			try{
				if(name.contains("added")){
					return "ST" + _id;
				}else{
					return name;
				}
			}catch(Exception ex){
				return "ST" + _id;
			}
			
		}
		public String toCSV(){
			String out = "";
			
			out = out + _id;
			out = out + ";" + this.name;
			out = out + ";" + this.sxolia;
			out = out + ";" + this.type;
			out = out + ";" + this.x;
			out = out + ";" + this.y;
			out = out + ";" + this.l;
			out = out + ";" + this.f;
			out = out + ";" + this.h;
			out = out + ";" + this.h_ort;
			out = out + ";" + this.sx;
			out = out + ";" + this.sy;
			out = out + ";" + this.sh;
			out = out + ";" + this.shor;
			out = out + ";" + this._t_id;
			out = out + ";" + this._t_project;
			out = out + ";" + this._t_tabId;
			out = out + ";" + this._t_base;
			out = out + ";" + this.date;
			out = out + ";" + this.global_id;
			out = out + ";" + this.foto;
			out = out + ";" + this.fixed;
			out = out + ";" + this.date;														
			out = out + ";" + this.local_date;														
			out = out + ";" + this.toCast;														
			
			return out;
			
		}
		
		public MyStasi(String name, double x, double y,MyGLSE20app myGLSE20app){
			// apo dimiourgia stasis
			this.azimuth.clear();
			this.name = name;
			this.toCast=true;
			this.point = new MyPoint(x,y);
			stasiPointIndex=myGLSE20app.mGLRenderer.stasiPoints.add(x, y, 0f, 0f,1f);
			markerIndex = myGLSE20app.mGLRenderer.marker.add(x,y,2,3,4);
			param = myGLSE20app.params;
			
			//if(!param.stasiIconShow)	myGLSE20app.mGLRenderer.marker.setLastMarkerInvisible();
			
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			
			
			double[] fl = new double [2];
			fl[0]=y;fl[1]=x;
			this.setCoorFl(fl);
			
			
			fixed	=	false;
			//locked	=	false;
			shor	=	-1;		sx	=	-1;		sy	=	-1;		sh	=	-1;
			sxolia	=	"";
			type	=	0;
			_id		=	0;
			user	=	"";
			foto	=	"";
			date	=		(int) param.getTime();
			this.local_date = (int) param.getTime();
			_t_id	=	-1;
			_t_project	=	(int) param.activeProject._id;
			_t_tabId	=	Integer.valueOf(param.tabId);
			_t_base=false;
			global_id=-1;
			
		}
		
		
		public MyStasi(){
			this.azimuth.clear();
			this.name = "";
			this.point = new MyPoint(0,0);
			
			//myGLSE20app.mGLRenderer.text_layer.add(x, y,"ST 1");
			
			fixed	=	false;
			//locked	=	false;
			shor	=	-1;		sx	=	-1;		sy	=	-1;		sh	=	-1;
			sxolia	=	"";
			type	=	0;
			_id		=	0;
			user	=	"";
			foto	=	"";
			date	=	-1;
			_t_id	=	-1;
			_t_project	=	-1;
			_t_tabId	=	-1;
			_t_base=false;
			global_id=-1;
			this.local_date = -1;
			
			
			
		}
		
		
		
		
		public MyStasi(long _id,String name,int type, double x, double y,float h,String sxolia, int date,int project,int local_date,int tabId){
			this.azimuth.clear();
			this.point = new MyPoint(0,0);
			this.name = name;
			this._id=_id;
			this.sxolia = sxolia;
			this.date=date;
			this.h=h;
			this.x=x;
			this.y=y;
			
			_t_project	=	project;
			_t_tabId	=	tabId;
			this.local_date = local_date;
			Log.i("333","333");
			double[] fl = new double [2];
			fl[0]=y;fl[1]=x;
			this.setCoorFl(fl);
		}
		
		
		public MyStasi(long _id,String name,int type, double x, double y,float h,String sxolia, int date,int project,int local_date,int tabId,double f, double l, MyParams param){
			this.param=param;
			this.point = new MyPoint(0,0);
			this.azimuth.clear();
			this.name = name;
			this._id=_id;
			this.sxolia = sxolia;
			this.date=date;
			this.h=h;
			this.x=x;
			this.y=y;
			this.f=f;
			this.l=l;
			
			_t_project	=	project;
			_t_tabId	=	tabId;
			this.local_date = local_date;
			double[] fl = new double [2];
			fl[0]=l;fl[1]=0;
			this.point.x=l;
			this.point.y=f;
			
		}
		
		
		
		public void setCoorFl(double[] fl,double[] xy){
			this.point.x=fl[1];
			this.point.y=fl[0];
			this.f=fl[0];
			this.l=fl[1];
			//double[] egsa = param.wms.proj.fl2EGSA87(this.f, this.l);
			this.x=xy[0];
			this.y=xy[1];
			param.renderer.stasiPoints.item.get(markerIndex).x=fl[1];
			param.renderer.stasiPoints.item.get(markerIndex).y=fl[0];
			param.renderer.marker.item.get(markerIndex).x=fl[1];
			param.renderer.marker.item.get(markerIndex).y=fl[0];
			
			//ekkremmei egsa;
			
		}
		
		
		
		
		
		public void setCoorFl(double[] fl){
			this.point.x=fl[1];
			this.point.y=fl[0];
			this.f=fl[0];
			this.l=fl[1];
			double[] egsa = param.wms.proj.fl2EGSA87(this.f, this.l);
			this.x=egsa[0];
			this.y=egsa[1];
			param.renderer.stasiPoints.item.get(markerIndex).x=fl[1];
			param.renderer.stasiPoints.item.get(markerIndex).y=fl[0];
			param.renderer.marker.item.get(markerIndex).x=fl[1];
			param.renderer.marker.item.get(markerIndex).y=fl[0];
			
			//ekkremmei egsa;
			
		}
		
		
		
		public void setCoorFl(double f, double l){
			this.point.x=l;
			this.point.y=f;
			this.f=f;
			this.l=l;
			double[] egsa = param.wms.proj.fl2EGSA87(this.f, this.l);
			this.x=egsa[0];
			this.y=egsa[1];
			param.renderer.stasiPoints.item.get(markerIndex).x=l;
			param.renderer.stasiPoints.item.get(markerIndex).y=f;
			param.renderer.marker.item.get(markerIndex).x=l;
			param.renderer.marker.item.get(markerIndex).y=f;
			
			//ekkremmei egsa;
			
		}
		
		public void delete(){
			Log.i("MyStasi delete()",this.name());
			param.renderer.marker.highlight(markerIndex, false);
			//param.renderer.marker.item.remove(markerIndex);
			//param.staseis.remove_by_id(this._id);
			//this.markerIndex
			this.deleted=true;
			param.renderer.marker.item.get(markerIndex).deleted=true;
			param.window_stasi.hide();
			param.mode.set_stasi_explore();
		}
		
		
		public boolean variationErrorOfAzimuthPairTo(int target_stasi_local_index){
			return getAzimuthPairTo(target_stasi_local_index).variationError();
		}
		
		
		
		private pairAzimuth getAzimuthPairTo(int target_stasi_local_index){
			pairAzimuth out = null;
			pairAzimuth pa;
			int i;
			for(i=0;i<=this.azimuth.size()-1;i++){
				
				pa = this.azimuth.get(i);
				/*Log.i(".........." + this.name(),
							param.staseis.item.get(target_stasi_local_index).name() + "  (" + target_stasi_local_index 
							+ ") : " 
							+ param.staseis.item.get(pa.st1).name()  + "  (" + pa.st1 + ") "
							+ "  =>  " 
							+  param.staseis.item.get(pa.st2).name()  + "  (" + pa.st2 + ") ");*/
				if(pa.st2==target_stasi_local_index) {
					out = pa;
					//Log.i("found","found");
				}
			}
			return out;
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
			param.app.dbHelper.updateStasiCoor(this);
		}
		
		public void db_update_global_id(){
			param.app.dbHelper.updateStasiGlobalId(this);
		}
		
		public void db_update_timeStamp(){
			param.app.dbHelper.updateStasiTimeStamp(this);
		}
		
		public void db_updateStasiflxysxolianame(){
			param.app.dbHelper.updateStasiflxysxolianame(this);
		}
		
		public int db_get_date(){
			int out =0;
			
			return out;
		}
	
		
		public boolean isEmpty(){
			boolean out = true;
			if(param.msets.stasi_id_is_has_period(_id)) out = false;
			if(param.msets.stasi_id_is_refered_by_other_periods(_id)) out=false;
			return out;
		}
		
}
