package com.geocloud.app;

import android.app.AlertDialog;


class bound_object{
	public double[] x;
	public double[] y;
	public String string;
	public int len=0;
	public double left,top,right,bottom;
	
	
	public  bound_object(/*AlertDialog.Builder dlgAlert,*/String data){
		left=999999999999999999.0;
		bottom=999999999999999999.0;
		top=-999999999999999999.0;
		right=-999999999999999999.0;
		
		
		String[] out = data.trim().split("#");//space delimiter
		len = out.length;
		x = new double[len];
		y = new double[len];
		String coor[];
		int i;
		for(i=0;i<=len-1;i++){
			coor = out[i].split(",");
			try{
				x[i]= Double.parseDouble(coor[0]);
				y[i]= Double.parseDouble(coor[1]);
			}catch(Exception e){
				//dlgAlert.setMessage(coor[0] + "\n" +coor[1] + "\n" + e.toString());
				//dlgAlert.create().show();
				//e.printStackTrace();
			}
			
			
			
			if(top<y[i]) top=y[i];
			if(bottom>y[i]) bottom=y[i];
			if(left>x[i]) left=x[i];
			if(right<x[i]) right=x[i];
			
		}
	}
	
	public boolean hasPointInExtent(double x,double y){
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
	
	
	
public boolean hasPointInPoly(double x,double y){
		
		return false;
	}

}


public class hatt_object {
	public double a[]={0,0,0,0,0,0};
	public double b[]={0,0,0,0,0,0};
	public double f0;
	public double l0;
	public bound_object bound;// arrays = new double[5][];
	public String name ;
	public String name_EN ;
	public String bounds_str ;
	public String id;
	public hatt_object(/*AlertDialog.Builder dlgAlert,*/String myid, String myname,double myf0,double myl0,double a0, double a1, double a2, double a3, double a4, double a5, double b0, double b1, double b2, double b3, double b4, double b5, String myname_EN,String mybounds ){
		
		
		 //dlgAlert.setMessage(myname);
		// dlgAlert.create().show();
		 		
		name_EN = myname_EN;
		bounds_str=mybounds;
		bound = new bound_object(/*dlgAlert, */bounds_str);
		id=myid;					
		name=myname;
		f0=myf0;
		l0=myl0;
		
		a[0]=a0;
		a[1]=a1;
		a[2]=a2;
		a[3]=a3;
		a[4]=a4;
		a[5]=a5;
		
		b[0]=b0;
		b[1]=b1;
		b[2]=b2;
		b[3]=b3;
		b[4]=b4;
		b[5]=b5;

	}
	
	public double[] to_egsa(double x, double y){
		double out[]={0,0};
		out[0]=a[0]+a[1]*x+a[2]*y+a[3]*x*x+a[4]*y*y+a[5]*x*y;
		out[1]=b[0]+b[1]*x+b[2]*y+b[3]*x*x+b[4]*y*y+b[5]*x*y;	
		return out;
	}
	
	
	public String toString(){
		String out="";
		out = out + name + "\n";
		out = out + (double) Math.round(bound.x[0]/100)*100 + "," +  (double) Math.round(bound.y[0]/100)*100 + "\n";
		out = out + (double) Math.round(bound.left/100)*100 + "," +  (double) Math.round(bound.bottom/100)*100 + "\n";
		out = out + (double) Math.round(bound.right/100)*100 + "," +  (double) Math.round(bound.top/100)*100 + "\n";
		
		
		return out;
		
	}
	
	
}
