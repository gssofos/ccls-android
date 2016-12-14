package com.geocloud.surfaceView;

import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;



public class TileGrid {
	ArrayList<Tile> item = new ArrayList<Tile>();
	MyApp_S app;
	String baseDir;
	
	public TileGrid(MyApp_S app){
		this.app			=	app;
		baseDir 			= 	Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache" ;  
		
	}
	
	
	public void add(float l, float f, int level){
		final Object[] res 	= 	app.ktima.getUrlFromLonLat(l,f,level);
		String 	filename	=	String.valueOf(String.format(baseDir + "/%s.jpg",(String) res[1]));
		String 	url			=	(String) res[0];
		float 	l1		=	Float.valueOf(String.valueOf( res[4]));
		float 	f1		=	Float.valueOf(String.valueOf( res[5]));
		int 	indX		=	Integer.valueOf(String.valueOf( res[6]));
		int 	indY		=	Integer.valueOf(String.valueOf( res[7]));
		item.add(new Tile(l, f, level,filename,url,indX,indY,l1,f1));
	}
	
	
	public boolean itemExists(float l, float f){
		int i =0;
		boolean found = false;
		Tile tmp;
		for(i=0;i<=item.size()-1;i++){
			tmp = item.get(i);
			if(tmp.l1==l){
				if(tmp.f1==f){
					found=true;
				}	
			}
			
		}
		return found;
	}
	
	
	
}
