package com.geocloud.topo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;

import android.util.Log;
import android.util.Xml;

public class MyKml {
	public String path = "";
	MyParams params;
	ArrayList<String> LineString = new ArrayList<String>();;
	ArrayList<kml_style> kmlst = new ArrayList<kml_style>();;
	
	
	
	public MyKml(String path,MyParams params){
		this.path = path;
		this.params = params;
		//Log.i("1",path);
		XmlPullParser parser = Xml.newPullParser();
		//Log.i("2",path);
		try {
			StringBuilder text = new StringBuilder();
			
			 File myfile = new File( path);
             //Log.i("eee",myfile..exists() + "-");
            // InputStream inputStream = params.app.getApplicationContext().openFileInput(myfile);
             BufferedReader br = new BufferedReader(new FileReader(myfile));
            // InputStream inputStream = params.app.getApplicationContext().openFileInput(myfile.getAbsolutePath());
            // InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
           
             ArrayList<String> data = new ArrayList<String>();;
         	
             String tmp = "";
             while((tmp= br.readLine()) != null){
            // Log.i("2",tmp);
            	 data.add(tmp);
            	 text.append(tmp);
             }
           
             
			//InputStream in_s = params.app.getApplicationContext().getAssets().open(path);
			//parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
          //  parser.setInput(br);
           // Log.i("3",path);
           // parseXML(parser);
            
            br.close();
            kmlRead(data);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			params.debug("1");;
			e.printStackTrace();
		}/* catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			params.debug("2");;
			e.printStackTrace();
		}*/
		
		params.surface.requestRender();
		
	}
	
	
	
	
	private void kmlRead(ArrayList<String> data){
		int length = data.size();
		int i;
		int polycount=0;
		String curStyle="";
		kml_style cks = null;
		
		params.renderer.kml_poly.clear();
		params.renderer.kml_points.clear();
		
		for(i=0;i<=length-1;i++){
			String tmp = data.get(i);
			
			
			
			if(tmp.contains("<Style ")){
				kml_style ks = new kml_style();
				ks.name=tmp.split("id=\"")[1].replace("\">", "");
				while(!tmp.contains("</Style>")){
					i+=1;
					tmp = data.get(i);
					if(tmp.contains("pink"))ks.setColor(255, 0, 255);
					if(tmp.contains("grn"))ks.setColor(0, 255, 0);
					if(tmp.contains("red"))ks.setColor(255, 0, 0);
					if(tmp.contains("GS"))ks.setColor(0f,0f, 255);
					if(tmp.contains("blu"))ks.setColor(255,255, 255);
					if(tmp.contains("ylw"))ks.setColor(255, 255, 0);
				}
				
				kmlst.add(ks);
				
			}
			
			
			
			
			if(tmp.contains("<styleUrl>")){
				curStyle = tmp.replace("<styleUrl>", "").replace("</styleUrl>", "").replace("#", "").trim();
				//Log.i("kmlstylename",curStyle);
				params.debug(curStyle);
				int u;
				for(u=0;u<=kmlst.size()-1;u++){
					//Log.i("+++++++++kmlstylename",kmlst.get(u).name);
					if(kmlst.get(u).name.contains(curStyle)){
						cks=kmlst.get(u);
						
					}
				}
				
				
			}
			
			
			if(tmp.contains("<Point>")){
				while(!tmp.contains("</Point>")){
					i+=1;
					tmp = data.get(i);
				
					if(tmp.contains("<coordinates>")){
						String coor = tmp.replace("<coordinates>", "").replace("</coordinates>", "");
						String[] xy = coor.split(",");
						if(cks!=null){
							params.renderer.kml_points.add(Double.valueOf(xy[0]), Double.valueOf(xy[1]),cks.r,cks.g,cks.b);
						}else{
							params.renderer.kml_points.add(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
						}
						
					}
					
					
					
				}
			}
			
			
			
			
			if(tmp.contains("<LineString>")){
				while(!tmp.contains("</LineString>")){
					i+=1;
					tmp = data.get(i);
			if(tmp.contains("<coordinates>")){
				String coor = tmp.replace("<coordinates>", "").replace("<Polygon>", "").replace("<outerBoundaryIs>", "").replace("<LinearRing>", "");
				while(!tmp.contains("</coordinates>")){
					i+=1;
					tmp = data.get(i);
					 coor = coor +  tmp.replace("</coordinates>", "");
				}
				
				coor.replace("</Polygon>","").replace("</LinearRing>","").replace("</outerBoundaryIs>","");
				 String[] points = coor.split(" ");
      		  polycount+=1;
      		 // params.debug("loading kml : " + polycount + " poly loading");
      		  MyPoly tmppoly = params.renderer.kml_poly.add(0.7f,0.8f,0.0f);
      			//params.debug(coor1[0]+"," +coor1[1]);
      			//Log.i(coor1[0]+"," +coor1[1],coor2[0]+"," +coor2[1]);
      			int j ;
      			for(j=0;j<=points.length-1;j++){
      				
      				if(points[j].length()>10){
      				String[] xy = points[j].split(",");
      				tmppoly.addVertice(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
      				}
      			}
      			
      			
      			//tmppoly.regen_coor();
      			
      			
				//Log.i("data",coor);
			}
				}
			}
		}
		
		params.renderer.kml_poly.regen_coor();
		
		
		params.app.Lon0=params.renderer.kml_poly.cx;
		params.app.Lat0=params.renderer.kml_poly.cy;
		
		params.renderer.originLon=params.renderer.kml_poly.cx;
		params.renderer.originLat=params.renderer.kml_poly.cy;
		
		
		params.renderer.set_OriginLonLat(params.renderer.kml_poly.cx,params.renderer.kml_poly.cy);
		
		double width = params.renderer.kml_poly.bbox.getWidth();
		double height = params.renderer.kml_poly.bbox.getHeight();
		
		for(i=30;i>=0;i--){
				float bim = params.wms.get_bima(i);
				
			if(bim>width && bim>height){
				params.renderer.zoom_level=i;
				i=-1;
			}
		}
		params.renderer.kml_poly.regen_coor();
		params.renderer.kml_points.regen_coor();
		
		params.renderer.set_OriginLonLat(params.renderer.kml_poly.cx,params.renderer.kml_poly.cy);
		
		
		params.renderer.grid.originBima=params.renderer.originBima;
		params.renderer.grid.originLon = params.renderer.originLon;
		params.renderer.grid.originLat = params.renderer.originLat;
		params.app.grid.clear();
		
		params.renderer.kml_poly.regen_coor();
		
		float xy[] = params.renderer.grid.lf2xy(params.renderer.kml_poly.cx,params.renderer.kml_poly.cy);
		
		// params.debug("(x,y) - " + xy[0] + "," + xy[1]);
		params.renderer.set_ortho(xy[0],  xy[1],false);
		params.debug(params.app.Lon0 + "," + params.app.Lat0);
		
	}
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
		
        int eventType = parser.getEventType();
        int polycount=0;
       // Product currentProduct = null;
       //String name;
        String coor="";
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            //Log.i("START_TAG",parser.getName());
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	//products = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                	name = parser.getName();
                	//Log.i("START_TAG",parser.getName());
                   if(name.contentEquals("coordinates")){
                	   try {
                		   coor = parser.nextText();
                		   String[] points = coor.split(" ");
                		   polycount+=1;
                		   params.debug("loading kml : " + polycount + " poly loading");
                		   MyPoly tmp = params.renderer.poly.add(0.0f,0.0f,0.0f);
                			//params.debug(coor1[0]+"," +coor1[1]);
                			//Log.i(coor1[0]+"," +coor1[1],coor2[0]+"," +coor2[1]);
                			int i ;
                			for(i=0;i<=points.length-1;i++){
                				
                				if(points[i].length()>10){
                				String[] xy = points[i].split(",");
                				//Log.i("qqqqq",points[i]);
                				tmp.addVertice(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
                				}
                			}
                			
                			
                			tmp.regen_coor();
                			params.renderer.poly.regen_coor();
                		   
                		 //  Log.i("START_TAGddd","-" + parser.nextText());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//Log.i("asas",coor);
						//e.printStackTrace();
					}
                   }
                	/* name = parser.getName();
                    if (name == "product"){
                      //  currentProduct = new Product();
                    } else if (currentProduct != null){
                        if (name == "productname"){
                          //  currentProduct.name = parser.nextText();
                        } else if (name == "productcolor"){
                        	//currentProduct.color = parser.nextText();
                        } else if (name == "productquantity"){
                          //  currentProduct.quantity= parser.nextText();
                        }  
                    }*/
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    //if (name.equalsIgnoreCase("product") && currentProduct != null){
                    	//products.add(currentProduct);
                   // } 
            }
            
            try {
				eventType = parser.next();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
           // eventType=parser.getFeature("LineString");
        }

        //printProducts(products);
	}
	
	
	
	
	
}


class kml_style{
	public String name="";
	public float r =0;
	public float g =0;
	public float b =0;
	
	public kml_style(){
		
	}
	
	public void setColor(float r, float g, float b){
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	
	
	
}
