package com.geocloud.Geometry;

import android.util.Log;

public class MyBbox {
	public double left;
	public double top;
	public double right;
	public double bottom;
	private boolean initialized=false;
	
	public MyBbox(){
		
	}
	
	public void add(double x, double y){
		if(!this.initialized){
			this.left=x;
			this.right=x;
			this.top=y;
			this.bottom=y;

			this.initialized=true;
		}else{
			if(x>this.right) this.right=x;
			if(x<this.left) this.left=x;
			if(y>this.top) this.top=y;
			if(y<this.bottom) this.bottom=y;
		}
	}
	
	
	public void addX(double x){
		
			if(x>this.right) this.right=x;
			if(x<this.left) this.left=x;
		
	}
	
	public void addY(double y){
		
		if(y>this.top) this.top=y;
		if(y<this.bottom) this.bottom=y;
	
}
	
	
	public double getWidth(){
		return right-left;
	}
	
	
	public double getHeight(){
		return top-bottom;
	}
	
	
	public void clear(){
		 initialized=false;
	}
	
	public boolean containsPoint(double x, double y){
		boolean out = false;
		if(x>left){
			if(x<right){
				if(y>bottom){
					if(y<top){
						out=true;
					}
				}
			}
		}
		
		return out;
	}
	
	
	
	public boolean containsPointInOffset(double x, double y, double offset){
		//eistrefei true an to simeio einai mesa sto dievrimeno kata offset bbox
		boolean out = false;
		
		
		if((x+offset)>left){
			if((x-offset)<right){
				if((y+offset)>bottom){
					if((y-offset)<top){
						out=true;
					}
				}
			}
		}
		
		return out;
	}
	
	
	
	
	public void log(String inn){
		Log.i("MyBbox ll : " + inn,"[" + (float)Math.round(left*100000)/100000 + "," +  (float)Math.round(bottom*100000)/100000 );
		Log.i("MyBbox ur : " + inn, " " + (float)Math.round(right*100000)/100000 + "," +  (float)Math.round(top*100000)/100000 + "]");
	}
	
}
