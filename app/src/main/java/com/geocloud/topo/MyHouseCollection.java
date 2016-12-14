package com.geocloud.topo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.MySerial;
import com.geocloud.app.MyGLSE20app;

public class MyHouseCollection {
	public ArrayList<MyHouse> item = new ArrayList<MyHouse>();
	MyGLSE20app myGLSE20app;
	MyParams	params;
	public MyHouseCollection( Context activityContext)
		{	
			myGLSE20app				=	(MyGLSE20app) activityContext;
			this.params 			=	myGLSE20app.params;
		}
	
	
	
	
	public void add(float x, float y){
		item.add(new MyHouse(x,y,myGLSE20app));
		//Log.i("asasas","assaas");
	}
	/*
	public void add(MyHouse house){
		item.add(house);
		//Log.i("asasas","assaas");
	}*/
	
	
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
	public MyHouse get_house_by_id(long _id){
		int i;
		MyHouse out = new MyHouse( _id, _id, myGLSE20app);
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i)._id==_id) {	
					out = item.get(i);
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	
	public double[] getCoorOfIndex(int index){
		double[] out = new double[2];
		MyHouse tmp = item.get(index);
		out[0]=tmp.point.x;
		out[1]=tmp.point.y;
		return out;
	}
	public void highlightStasi(int index,boolean value,int highlightIndex){
		if(index<item.size() && index>-1){
			MyHouse tmp = item.get(index);
			tmp.highlight(value,highlightIndex);
		}
	}
	
	
	public void highlightStasi(int index,boolean value){
		if(index<item.size() && index>-1){
			MyHouse tmp = item.get(index);
			tmp.highlight(value);
		}
	}
	
	
	
	
	public int get_house_index_selected(double x, double y,int dpixel){
		//Log.i("pix" , "-" + dpixel);
		
		int out = -1;
		
		
		int i =0;
		double dx,dy,min=99999999999999999999999d,dist;
		int min_dist_index=-1;
		for(i=0;i<=item.size()-1;i++){
			MyHouse tmp = item.get(i);
			dx = tmp.point.x-x;
			dy = tmp.point.y-y;
			dist = dx*dx+dy*dy;
			if(dist<min) {min = dist;min_dist_index=i;};
		}
		
		float dd = (float) (Math.sqrt(min)/params.pixel2world);
			
		
		
		
			if(dd<params.tapSelectPixelWindow){
				MyHouse tmp;
				out = min_dist_index;
			}else{
				if(dpixel<20){
					//MyHouse tmp = item.get(params.window_stasi.curOpenStasiIndex);
					//tmp.highlight(false);
					//params.window_stasi.hide();
				}
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
	
	
	
	

}
