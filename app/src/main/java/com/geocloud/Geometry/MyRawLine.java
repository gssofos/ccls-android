package com.geocloud.Geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyGLRawResourceReader;
import com.geocloud.MyGLES20.MyGLShaderHelper;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.Vector_Poly_Layer;
import com.geocloud.app.MyGLSE20app;
import com.geocloud.db.LineMeta;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class MyRawLine extends MyGeometry{
	private ArrayList<MyPoint> vertices = new ArrayList<MyPoint>();
	
	MyGLSE20app myGLSE20app;
	Context mActivityContext;
	
	public MyBbox bbox;
	
	public MyParams params;
	public double cx=0;
	public double cy=0;
	public int flag = -1;
	public float r,g,b;
	
	public float tempr,tempg,tempb;
	
	public long _id;
	
	
	
	public MyRawLine(MyParams params, double x1, double y1, double x2, double y2)																	{	
		//kathara gia temporary, geometrikes praxeis
		this.params=params;
		this.bbox=new MyBbox();
		_id=-1;
		vertices.add(new MyPoint(x1,y1));
		vertices.add(new MyPoint(x2,y2));
		
		bbox.add(x1, y1);
		bbox.add(x2, y2);
	}
	
	
	public void setFlag(int flag){
		this.flag=flag;
		
	}
	
	public void setColor(float r, float g, float b){
		this.r=r;this.g=g;this.b=b;
		this.tempr=r;this.tempg=g;this.tempb=b;
		
	}
	
	public String kmlCoor(){
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		return p1.x+"," + p1.y +",0 " + p2.x+","+p2.y + ",0";
	}
	
	
	
	public void setTempColor(float r, float g, float b){
		this.tempr=r;this.tempg=g;this.tempb=b;
	}
	
	public void restoreColor(){
		this.tempr=this.r;this.tempg=this.g;this.tempb=this.b;
	}
	
	
	
	public double getDist(){
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		
		double dx,dy;
		
		 dx = (p2.x-p1.x);
		 dy =  (p2.y-p1.y);
		 
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	
	public double getAzimuth(){
		double dx,dy;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
	
		 dx = (p2.x-p1.x);
		 dy =  (p2.y-p1.y);
		 
		return params.util.getAzimuth_by_dxdy_inn(dx, dy);
	}
	
	
public double get_a(){
		
		double dx,dy;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		
		dx = (p2.x-p1.x);
		dy =  (p2.y-p1.y);
		 
		if(dx==0){
			return (Double) null;
		}else{
			 double logos = dy/dx;		
			return logos;
		}
	}

	
	

	public MyPoint getVertice(long index){
		return vertices.get((int) index);
	}
	
	
	
	
	public double pointToLinePerDist(double x,double y){
		double out=-1d;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		double dx = (p2.x-p1.x);
		double dy =  (p2.y-p1.y);
		
		double a =0,b=0;
		double at =0,bt=0;
		double xt,yt;
		
		if(dx==0){
			out = Math.abs(x-p1.x);
		}else if(dy==0){
			out = Math.abs(y-p1.y);
		}else{
			a=dy/dx;
			at=-1/a;
			b=p1.y-a*p1.x;
			bt=y-at*x;
			xt=(b-bt)/(at-a);
			yt=xt*at+bt;
			
			dx=xt-x;
			dy=yt-y;
			out = Math.sqrt(dy*dy+dx*dx);
		}
		return out;
	}
	
	
	
	
	
	
	public boolean pointInBbox(double x,double y){
		boolean out =false;
		out = bbox.containsPoint(x, y);
		//if(out) 	bbox.log("point : " + x + "," + y + "  in bbox : ");
		return out;
	}
	
	
	public boolean pointInBboxWithOffset(double x,double y,double offset){
		boolean out =false;
	
		out = bbox.containsPointInOffset(x, y,offset);
		//if(out) 	bbox.log("point : " + x + "," + y + "  in bbox : ");
		return out;
	}
	
	public String toString(){
		return "";//start.toString() + " - " + end.toString();
	}
	
	public void log(){
		Log.i("Poly Log",toString());
	}
	
	
}
