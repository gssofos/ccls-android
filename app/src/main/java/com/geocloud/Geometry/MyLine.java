package com.geocloud.Geometry;

import android.util.Log;

public class MyLine extends MyGeometry{
	private MyPoint start,end;
	
	public MyLine()																	{	this.type=1;}
	public MyLine(double x1, double y1, double z1,double x2, double y2, double z2)	{	this.type=1;	setCoor(x1, y1, z1,x2, y2, z2);	}
	public MyLine(double x1, double y1,double x2, double y2)						{	this.type=1;	setCoor(x1, y1, x2, y2);		}
	
	
	public void setCoor(double x1, double y1, double z1,double x2, double y2, double z2)
	{	start = new MyPoint(x1,y1,z1);		end = new MyPoint(x2,y2,z2);	}	

	public void setCoor(double x1, double y1,double x2, double y2)
	{	start = new MyPoint(x1,y1);		end = new MyPoint(x2,y2);	}	


	public String toString(){
		return start.toString() + " - " + end.toString();
	}
	
	public void log(){
		Log.i("Line Log",toString());
	}
}
