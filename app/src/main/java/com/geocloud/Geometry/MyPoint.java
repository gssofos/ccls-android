package com.geocloud.Geometry;

import java.util.ArrayList;

import com.geocloud.topo.MyMeasurement;

import android.util.Log;

public class MyPoint extends MyGeometry {
	public double x,y,z;
	public float r=0,g=0,b=0,psize=5;
	public int icon_index = 0;
	public int flag = -1;
	
	public MyMeasurement metrisi = null;
	//public int[] icon_hl_index = new int[0];
	ArrayList<Integer> 		icon_hl_index = new ArrayList<Integer>();		//krataei ta index ton icon pou orizontai sto marker layer;
	public MyPoint()								{	this.type=0; }
	public MyPoint(double x, double y, double z)	{	this.type=0;	setCoor(x,y,z);	}
	public MyPoint(double x, double y)				
	{	this.type=0;	setCoor(x,y,0);	}
	public MyPoint(double x, double y,float r, float g, float b)				
	{	this.type=0;	setCoor(x,y,0);
		this.r=r;this.g=g;this.b=b;
	}

	public MyPoint(double x, double y,float r, float g, float b,int flag)				
	{	this.type=0;	setCoor(x,y,0);
		this.r=r;this.g=g;this.b=b;this.flag=flag;
	}


	public boolean highlighted=false;
	public int icon_cur_hl_index=-1;		//krataei to index sto array gia to highlight icon
	
	public boolean deleted=false;
	
	public void setIconIndex(int icon_index){
		this.icon_index=icon_index;	
	}
	
	public void addIconHlIndex(int icon_hl_index){
		this.icon_hl_index.add(icon_hl_index);//=icon_hl_index;	
		if(this.icon_hl_index.size()==1) icon_cur_hl_index=0;
	}
	
	public int getIconHlIndex(){
		return this.icon_hl_index.get(icon_cur_hl_index);//=icon_hl_index;	
	}
	
	public void setIconHlArrayIndex(int index){
		icon_cur_hl_index=index;
	}
	
	public void setCoor(double x,double y, double z){
		//Log.i("xyaaaaaaaaa",x +"," + y);
		
		this.x=x;	this.y=y;	this.z=z;	
	}
	
	public String toString(){
		return this.x + "," + this.y + "," + this.z;
	}
	
	public void log(){
		Log.i("Point Log",toString());
	}
	
	public void setMetrisi(MyMeasurement metrisi){
		this.metrisi = metrisi;
	}
}
