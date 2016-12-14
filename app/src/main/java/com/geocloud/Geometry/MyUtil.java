package com.geocloud.Geometry;

import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.geocloud.MyGLES20.MyParams;

public class MyUtil {
	MyParams params;
	
	public MyUtil(MyParams params){
		this.params = params;
		
	}
	public double[] intersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		double[] out = new double[2];
		
		double dx1 	= 	x2-x1;
		double dy1 	= 	y2-y1;
		double dx2	=	x4-x3;
		double dy2 	= 	y4-y3;
		
		
		
		
		
		return out;
	}
	
	
	
	
	public boolean intersects(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		boolean out = false;
		
		
		if(
					max(x1,x2)<min(x3,x4)
				|| 	min(x1,x2)>max(x3,x4)
				||	min(y1,y2)>max(y3,y4)
				||	max(y1,y2)<min(y3,y4)
				
				
				){
			
			
				return false;
			
			
		}else{
			
			
			
			double dx1 	= 	x2-x1;
			double dy1 	= 	y2-y1;
			double dx2	=	x4-x3;
			double dy2 	= 	y4-y3;
			
			if(dx1==0){
				if(dx2==0){
					return false;
				}else{
					//double a = get_a	(x3,y3,x4,y4);
					//double b = get_b	(x3,y3,a);	
					if(max(x3,x4)>x1 && min(x3,x4)<x1){
						double a = get_a	(x3,y3,x4,y4);
						double b = get_b	(x3,y3,a);
						double yt = a*x1+b;
						if(isBetween(yt,y1,y2)){
							return true;
						}else{
							return false;
						}
						
					}else{
						return false;
					}
				}
			}else if(dx2==0){
				
				
			}else{
				double a1 = get_a	(x1,y1,x2,y2);
				double a2 = get_a	(x3,y3,x4,y4);
				
				if(a1==a2){
					return false;
				}else{
					
				}
			}
			
			
			
		}
		
		
		return out;
	}
	
	
	public double get_a(double x1, double y1, double x2, double y2){
		double dx 	= 	x2-x1;
		double dy 	= 	y2-y1;
		
		double out=0;
		double logos = dy/dy;
		
		
		
		
		return Math.atan(logos);
	}
	
	
	
	
	
public double getAzimuth_by_fl_inn(double l1, double f1, double l2, double f2){
		double dx,dy;
		//MyPoint p1 = vertices.get(0);
		//MyPoint p2 = vertices.get(1);
		
		
		double[] egsa1 = params.wms.proj.fl2EGSA87(f1, l1);
		double[] egsa2 = params.wms.proj.fl2EGSA87(f2, l2);
		
		
		 dx = (egsa2[0]-egsa1[0]);
		 dy =  (egsa2[1]-egsa1[1]);
		if(dy==0){
			if(dx>=0){
				return 100d;
			}else{
				return 300d;
			}
		}else{
			 double logos = dx/dy;
			
			 double atan = Math.abs(Math.atan(logos));
			 
			if(dx>0){
				if(dy>0){
					atan=atan;
				}else{
					atan = (float) (Math.PI-atan);
				}
			}else{
				if(dy>0){
					atan = (float) (2*Math.PI-atan);
				}else{
					atan = (float) (Math.PI+atan);
				}
			}
			
			//azimuth2stasi_0 = (float) (atan*200/Math.PI);
			//azimuth2_0 = azimuth2stasi_0-angle;
			
			return atan*200/Math.PI;
		}
	}






public double getAzimuth_by_dxdy_inn(double dx, double dy){
	//azimouthio dinontas dx, dy input 
//	Log.i("dx = " + dx , " dy = " + dy);
	if(dy==0){
		if(dx>=0){
			return 100d;
		}else{
			return 300d;
		}
	}else{
		 double logos = dx/dy;
		
		 double atan = Math.abs(Math.atan(logos));
		 
		if(dx>0){
			if(dy>0){
				atan=atan;
			}else{
				atan = (float) (Math.PI-atan);
			}
		}else{
			if(dy>0){
				atan = (float) (2*Math.PI-atan);
			}else{
				atan = (float) (Math.PI+atan);
			}
		}
		
		//azimuth2stasi_0 = (float) (atan*200/Math.PI);
		//azimuth2_0 = azimuth2stasi_0-angle;
		
		return atan*200/Math.PI;
	}
}


	
	
	public boolean isBetween(double x, double x1, double x2){
		if(x>min(x1,x2) && x<max(x1,x2)){
			return true;
		}else{
			return false;
		}
	}
	
	public double get_b(double x1, double y1, double a){
		
		return (y1-a*x1);
	}
	
	
	
	public double max(double x1, double x2){
		if(x1>x2) {
			return x1;
		}else{
			return x2;
		}
	}
	
	
	public double min(double x1, double x2){
		if(x1>x2) {
			return x2;
		}else{
			return x1;
		}
	}
	
	
	

	public TextView addTextView(RelativeLayout parent, int left, int top, int width, int height,String name, int color ,float textSize){
		TextView tv = new TextView(params.app.getBaseContext());
		RelativeLayout.LayoutParams lay ;
		
		
		tv.setText(name);
		tv.setTextColor(color);
		tv.setTextSize(textSize);
		lay = new RelativeLayout.LayoutParams(width, height);
		lay.leftMargin=left;
		lay.topMargin=top;
		parent.addView(tv,lay);
		
		return tv;
	}
	
	
	 
}
