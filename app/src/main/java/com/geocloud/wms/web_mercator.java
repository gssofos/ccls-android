package com.geocloud.wms;

public class web_mercator {
	String base_url;
	String name;
	public web_mercator(String name,String base_url){
		this.name		=name;
		this.base_url 	= base_url;
	}
	
	public float[]  fl2web(float Lon, float Lat){
		float[] out = new float[2];
		out[0]= Lon * 20037508.34f / 180;
		double y = Math.log(Math.tan((90 + Lat) * Math.PI / 360)) / (Math.PI / 180) * 20037508.34 / 180;
		out[1]=(float) y; 
		return out;
	}
	
	
	public float[]  web2fl(float x, float y){
		float[] out 	= new float[2];
				out[0] 	= (x / 20037508.34f) * 180;
		double 	lat 	= (y / 20037508.34f) * 180;
				lat 	= 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
				out[1]	=(float)	lat;
		return out;
	}
	
	
}
