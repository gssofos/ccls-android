package com.geocloud.topo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.geocloud.Geometry.MyRawLine;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.MySerial;
import com.geocloud.app.MyGLSE20app;

public class MyStasiCollection {
	public ArrayList<MyStasi> item = new ArrayList<MyStasi>();
	MyGLSE20app myGLSE20app;
	MyParams	params;
	public MyStasiCollection( Context activityContext)
		{	
			myGLSE20app				=	(MyGLSE20app) activityContext;
			this.params 			=	myGLSE20app.params;
		}
	
	
	public void add(String name, float x, float y){
		item.add(new MyStasi(name,x,y,myGLSE20app));
	}
	
	public void add(float x, float y){
		item.add(new MyStasi("added",x,y,myGLSE20app));
		//Log.i("asasas","assaas");
	}
	
	
	
	
	
	public void remove_by_id(long _id){
		int i;
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i)._id==_id) {	
					item.remove(i);
					i=item.size();
			}
			
		}
	}
	
	
/*
	public void remove_not_fixed(){
		int i;
		for(i=item.size()-1;i>=0;i--){
			if( !item.get(i).fixed) {	
				params.renderer.marker.item.get(item.get(i).markerIndex).deleted=true;
				item.remove(i);
			}
		}
	}
	
	*/
	public MyStasi get_stasi_by_id(long _id){
		int i;
		MyStasi out = new MyStasi(null, _id, _id, myGLSE20app);
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i)._id==_id) {	
					out = item.get(i);
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	
	public MyStasi get_stasi_by_local_date( long local_date){
		int i;
		MyStasi out = new MyStasi(null, 0, 0, myGLSE20app);
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i).local_date==local_date && item.get(i)._t_tabId==Integer.valueOf(params.tabId)) {	
					out = item.get(i);
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	
	public long get_stasi_id_of_stasi_having_global_id(long global_id){
		int i;
		long out = -1;
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i).global_id==global_id) {	
					out = item.get(i)._id;
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	
	
	
	public double[] getCoorOfIndex(int index){
		double[] out = new double[2];
		MyStasi tmp = item.get(index);
		out[0]=tmp.point.x;
		out[1]=tmp.point.y;
		return out;
	}
	public void highlightStasi(int index,boolean value,int highlightIndex){
		if(index<item.size() && index>-1){
			MyStasi tmp = item.get(index);
			tmp.highlight(value,highlightIndex);
		}
	}
	
	
	public void highlightStasi(int index,boolean value){
		if(index<item.size() && index>-1){
			MyStasi tmp = item.get(index);
			tmp.highlight(value);
		}
	}
	
	
	
	
	public int get_stasi_index_selected(double x, double y,int dpixel){
		//Log.i("pix" , "-" + dpixel);
		
		int out = -1;
		
		
		int i =0;
		double dx,dy,min=99999999999999999999999d,dist;
		int min_dist_index=-1;
		for(i=0;i<=item.size()-1;i++){
			MyStasi tmp = item.get(i);
			
			if(!tmp.deleted){
				dx = tmp.point.x-x;
				dy = tmp.point.y-y;
				dist = dx*dx+dy*dy;
				if(dist<min) {min = dist;min_dist_index=i;};
			}
			
		}
		
		float dd = (float) (Math.sqrt(min)/params.pixel2world);
			
		
		
		if(params.mode.mode==0 || params.mode.edit_data){
			if(dd<params.tapSelectPixelWindow){
				MyStasi tmp;
				//Log.i("gg" , ""+params.window_stasi.curOpenStasiIndex);
				tmp = item.get(params.window_stasi.curOpenStasiIndex);
				tmp.highlight(false);
				
				/*tmp = item.get(min_dist_index);
				
				params.window_stasi.curOpenStasiIndex=min_dist_index;
				params.window_stasi.show();
				tmp.highlight(true,0);
				params.window_stasi.textViewName.setText("ST" + (min_dist_index+1));
				params.window_stasi.textViewX.setText((float)Math.round(tmp.point.x*1000)/1000 + " ");
				params.window_stasi.textViewY.setText((float)Math.round(tmp.point.y*1000)/1000 + " ");
				params.app.vib.vibrate(100);*/
				//params.app.prolific_open();
				
				out = min_dist_index;
			}else{
				if(dpixel<20){
					MyStasi tmp = item.get(params.window_stasi.curOpenStasiIndex);
					tmp.highlight(false);
					params.window_stasi.hide();
				}
			}
		}
		
		
		
		
		if(params.mode.mode==1){
			if(dd<params.tapSelectPixelWindow){
				
				
				if(min_dist_index!=params.window_stasi.curOpenStasiIndex){
					MyStasi tmp;
					//params.debug(min_dist_index + "");
					tmp = item.get(min_dist_index);
					
					params.window_back_orientation.show(min_dist_index);
					tmp.highlight(true,0);
					
					//params.window_stasi_skop.layout.setBackgroundColor(Color.argb(111,187,204,255));
				}else{
					params.window_back_orientation.hide(true);
					//params.window_stasi_skop.layout.setBackgroundColor(Color.argb(255,255,255,255));
			}
				
				
				
			}else{
				//params.window_stasi_skop.layout.setBackgroundColor(Color.argb(255,255,255,255));
				
				//Log.i("colll" , "=" + "4");
				if(dpixel<20){
					params.window_back_orientation.hide(true);
				}
			}	
				//se kathe periptosi prepei to selected index na einai highlighted
				MyStasi tmp;
				tmp = item.get(params.window_stasi.curOpenStasiIndex);
				tmp.highlight(true);
	
		}
		
		
		
		
		if(params.mode.mode==2){
			if(dd<params.tapSelectPixelWindow){
				params.window_stasi_skop.layout.setBackgroundColor(Color.argb(255,255,255,255));
				if(params.window_stasi.curSkopefsiIndex>-1){	MyStasi tmp = item.get(params.window_stasi.curSkopefsiIndex);tmp.highlight(false);params.window_stasi.curSkopefsiIndex=-1;			}
				
				
				if(params.window_stasi.curOpenStasiIndex==min_dist_index){
					//params.debug(params.window_stasi.curOpenStasiIndex +"-" + params.window_stasi.curSkopefsiIndex);;
				}else{
					MyStasi tmp = item.get(min_dist_index);
					tmp.highlight(true,1);
					params.window_stasi_skop.layout.setBackgroundColor(Color.argb(255,253,237,64));
					params.window_stasi.curSkopefsiIndex=min_dist_index;
					params.app.vib.vibrate(100);
				}
				
				
			}else{
				params.window_stasi_skop.layout.setBackgroundColor(Color.argb(255,255,255,255));
				if(params.window_stasi.curSkopefsiIndex>-1){
					MyStasi tmp = item.get(params.window_stasi.curSkopefsiIndex);	
					tmp.highlight(false);	params.window_stasi.curSkopefsiIndex=-1;	
					
				}
				
			}
			
			//se kathe periptosi prepei to selected index na einai highlighted
			MyStasi tmp;
			tmp = item.get(params.window_stasi.curOpenStasiIndex);
			tmp.highlight(true);
			
			
			
			
		}
		
		


		return out;
	}
	
	public int stasi_id_to_index(long id){
		//eisago id tis stasis (apo db) kai moy dinei ti thesi sto array
		int out = -1;
		int i=0;
		for(i=0;i<=this.item.size()-1;i++){
			if(this.item.get(i)._id==id){
				
				out = i;
				i=this.item.size();
			}
		}
		
		return out;
	}
	
	
	public int[] getnearestPerStasiPair(double x, double y){
		double min_dist=99999999;
		int min_dist_st1=-1;
		int min_dist_st2=-1;
		int min_dist_pair_index=-1;
		int[] out = new int[2];
		int i,j;
		MyMeasureSet ms;
		MyMeasurement m;
		
		
		for(i=0;i<=params.odefsi.distset.item.size()-1;i++){
			int st1 = params.odefsi.distset.item.get(i).st1;
			int st2 = params.odefsi.distset.item.get(i).st2;
			double tmp = getPerDistFromSt1St2((int) this.item.get(st1)._id,(int) this.item.get(st2)._id,x,y);
			if(tmp>0){
				if(tmp<min_dist){
					min_dist=tmp;
					min_dist_st1=st1;
					min_dist_st2=st2;
					min_dist_pair_index=i;
					
				}
			}
		}
		/*
		for(i=0;i<=params.msets.item.size()-1;i++){
			ms = params.msets.item.get(i);
			int st1_id = ms.stasi_id;
			for(j=0;j<=ms.itemStaseis.size()-1;j++){
				m = ms.itemStaseis.get(j);
				if(m.odefsi_use){
					double tmp = getPerDistFromSt1St2(st1_id,m.stasi_index_id,x,y);
					//Log.i("getnearestPerStasiPair",st1_id + " - " + m.stasi_index_id + "   =>  " + tmp);
					if(tmp>0){
						if(tmp<min_dist){
							min_dist=tmp;
							min_dist_st1=st1_id;
							min_dist_st2=m.stasi_index_id;
							
						}
					}
				}
			}
		}*/
		params.debug("#" + (float) Math.round(params.odefsi.distset.item.get(min_dist_pair_index).mo_dist*1000)/1000 + "#" );
		Log.i("getnearestPerStasiPair",this.item.get(min_dist_st1)._id + " - " + this.item.get(min_dist_st2)._id + "   =>  " + min_dist);
		
		return out;
	}
	
	public double getPerDistFromSt1St2(int id1,int id2, double x, double y){
		MyStasi st1 = this.get_stasi_by_id(id1);
		MyStasi st2 = this.get_stasi_by_id(id2);
		
		double offset = params.tapSelectPixelWindow*params.pixel2world;
		
		MyRawLine rl = new MyRawLine(params,st1.x,st1.y,st2.x,st2.y);
		if(rl.pointInBboxWithOffset(x, y, offset)){
			return rl.pointToLinePerDist(x, y);
		}else{
			return -1d;
		}
	}
	
	
	
	

}
