package com.geocloud.wms;

import java.util.ArrayList;

import com.geocloud.MyGLES20.D_Square_Image;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Wms {
	public Proj proj;
	
	Context 	mActivityContext;
	String 		type;
	String 		name;
	int 		type_int;
	public int 		tile_width=0,tile_height=0;
	String 		url;
	public int 		cols,rows;		//megethos grid gia ena plakaki
	
	public Wms(Context mActivityContext,String name, String wms_type,String base_url, int col, int row,int width, int height){
		this.mActivityContext 	= 	mActivityContext;
		proj 					= 	new Proj();;
		type 					= 	wms_type;
		url						=	base_url;
		cols 					= 	col;
		rows					=	row;
		tile_width				=	width;
		tile_height				=	height;
		this.name				=	name;
		if(type=="ktima"){		type_int=2;		}
		
		if(type=="osm"){			type_int=0;	}
		
		
		if(type=="web_mercator"){	type_int=1;	}
	}
	
	
	
		public float[] getOriginFromCoor(float x, float y, int z){
				
				float bima = get_bima(z);
		       
		        
		        double y0 		= 	Math.floor(y/bima/rows)*bima*rows;
		        double x0 		= 	Math.floor(x/bima/cols)*bima*cols;
		        float out[] 	= 	new float[6];
		        
		        out[0]			=	(float) x0;					//thesi pou antistoixei sto origin tou plakakiou
		        out[1]			=	(float) y0;
		        
		        out[2]			=	(float) Math.floor(x/bima/cols);	//antistoixa index
		        out[3]			=	(float) Math.floor(y/bima/rows);
		        out[4] 			= 	bima*cols;
		        out[5] 			= 	bima*rows;
		        
		        return out;
		}


		
		public float get_bima(int level){
			
			int 		dz 		= level - 6;
			float 		bima 	= 0;
			int i;
			
			if(type_int==2){
				bima = 1;
			    if (dz > 0) {       
			    				for (i = 1;i<=dz;i++)		{    bima = bima / 2;  }
			    }else{
			            		for( i = 1 ;i<= -dz;i++)	{    bima = bima * 2;  }
			    }
			}
		    return bima;
		}
		
		
		

		public Object[] getUrlFromCoor(float x, float y, int z){
			
				float bima = get_bima(z);
				double y1 		= 	0;
			    double y2 		= 	0;
			    double x1 		= 	0;
			    double x2 		= 	0;  
			    int		i,j		=	0;
		       // Log.i("qq","q");
			    
			    y1 		= 	Math.floor(y/bima/rows)*bima*rows		-bima;
			    y2 		= 	y1 + bima								;
			    x1 		= 	Math.floor(x/bima/cols)*bima*cols		-bima;
			    x2 		= 	x1 + bima								;
			    
			    double x0 = x1;
			    
			    Object out_array[];
			    out_array = new Object[cols*rows];
			    int counter=0;
			    for(j=1;j<=rows;j++){
			    	y1 = y1+bima;
			    	y2 = y2+bima;
			    	x1=x0;
			    	x2=x0+bima;
			    	 for(i=1;i<=cols;i++){
			    		 x1 = x1+bima;
			    		 x2 = x2+bima;
						    Object out[] 	= 	{null,null,null,null,null,null,null,null};
					        
						    if(type_int==2){
						    	 out[0] 	= "http://gis.ktimanet.gr/wms/wmsopen/wmsserver.aspx?LAYERS=basic&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&BBOX=";
							     out[0] 	= out[0] + new Double(x1).toString() + ",";
							     out[0] 	= out[0] + new Double(y1).toString() + ",";
							     out[0] 	= out[0] + new Double(x2).toString() + ",";
							     out[0] 	= out[0] + new Double(y2).toString() + "&WIDTH=512&HEIGHT=512";
						    }
					       
					       
					        
					        
					        
					        //Log.i("qq","q");  
					        out[1] 	= name + "/" + (cols + "" + rows + "/") + (int)z + "/" + (int)Math.floor(x/bima/cols) + "/" + (int) Math.floor(y/bima/rows);
					        //Log.i("qq","q");  
					       // double 	egout[] 	= 	proj.fl2EGSA87(lat, lon);
					        //Log.i("qq","q");  
					       // double 	xeg			=	(double) Math.round(egout[0]*100)/100;
					       // double 	yeg			=	(double) Math.round(egout[1]*100)/100;
					       // double 	dx 			= 	get_ktima_dx(xeg,yeg);
					       // double 	dy 			= 	get_ktima_dy(xeg,yeg);
					    	//double 	ee[] 		= 	proj.Egsa2fl84(xeg-dx,yeg-dy);
					    	//double 	fktima		=	ee[0];
					    	//double 	lktima		=	ee[1];
					    	//Log.i("qq","q");
					      //  out[2]=(lktima-l1)/bima;
					       // out[3]=(fktima-f1)/bima;     
					        
					      //  out[4] = f1;
					       // out[5] = l1;
					        
					        out[6] = (int)Math.floor(x/bima/cols);
					        out[7] = (int)Math.floor(y/bima/rows);
					        
					        out_array[counter]=out;
					        counter+=1;
					        
			    	 }
			    }   
			return out_array;
		}
		
		
		
		



		
	
	
	
}
