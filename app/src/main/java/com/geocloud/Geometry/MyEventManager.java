package com.geocloud.Geometry;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Toast;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyStasi;

public class MyEventManager {
	
	MyParams params;
	public boolean pointSelected;
	public boolean point2Selected;
	public double selectedx, selectedy;
	public double selected2x, selected2y;
	
	public boolean lineSelected;
	public MyPoly  selectedLineObject;
	public MyPoint  selectedPointObject;
	
	public MyEventManager(MyParams params){
		this.params = params;
		pointSelected=false;
		lineSelected=false;
		selectedLineObject=null;
		
	}
	
	
	
	public void triggerClick(double x, double y,int dpixel,int px, int py){
		
		if(params.mode.edit_line){
			params.window_edit_point_meta.hide();
			
			double[] center = params.getCenterCoor();
			if(params.window_edit.add_mode){
				params.window_edit_lineadd.hide();
				//params.window_edit_lineadd.show(px,py);
				
				if(params.window_edit.add_mode_p1_set){
					if(params.event.clickPoint(x,y,dpixel,1)) params.window_edit_lineadd.show(px+40,py-250);
					//params.debug(" : "  + params.renderer.my_poly.lastPoly.flag);
					//Log.i("asasas","vanvas" + params.renderer.my_poly.lastPoly.flag);
					if(params.renderer.my_poly.item.size()>0) {
								if(params.renderer.my_poly.lastPoly.flag==1) 
										params.renderer.my_poly.remove_last();
					}
					
					MyPoly tmp = params.renderer.my_poly.add( 0f, 0f, 0f,1);
					tmp.addVertice(selectedx, selectedy);
					tmp.addVertice(selected2x, selected2y);
					params.renderer.my_poly.regen_coor();
					
				}else{
					if(params.event.clickPoint(x,y,dpixel,0)) params.window_edit_lineadd.show(px+40,py-250);
				
				}
				
			}else{
				params.window_edit_linemod.hide();
				
				if(params.event.clickLine(x,y,dpixel)){
					if(px+100+300>params.imagePixelWidth){
						params.window_edit_linemod.show(px-100-300,py-250);
					}else{
						params.window_edit_linemod.show(px+100,py-250);
					}
					
				}else{
					//params.window_edit_linemod.hide();
				};
			}
			//params.event.clickPoint(center[0],center[1],0);
		}else{
			if(dpixel<20){
			if(params.mode.mode==0 && params.selectedStasi>=0) {
					params.staseis.item.get(params.selectedStasi).highlight(false);params.selectedStasi=-1;
					//Toast.makeText(params.app, "aaaa", Toast.LENGTH_SHORT);
			};
			
			if(params.mode.edit_data && params.selectedStasi>=0) {
					//if(dpixel<20) 
						params.staseis.item.get(params.selectedStasi).highlight(false);params.selectedStasi=-1;
			};
			
				int stasi_index = params.staseis.get_stasi_index_selected(x,y,dpixel);
				//params.debug(stasi_index + "");
				//params.app.vib.vibrate(100);;
				
				
				
				
				if(stasi_index==-1){
					
					
					
					if(params.mode.edit_data){
						params.window_edit_point_meta.hide();
						
						double eg[] = params.wms.proj.fl2EGSA87(y, x);
						//Log.i("xy",x + " , " + y + "     " + eg[0] + " , " + eg[1]);
						if(params.mode.edit_data){
							//params.staseis.getnearestPerStasiPair(eg[0],eg[1]);
						
						}
						
						params.window_edit_data.set_data(-1);
						
					}else{
						//an den exo epistrepsei stasi selected
						if(params.event.clickPoint(x,y,dpixel)){
							params.window_edit_point_meta.show();
						}else{
							params.window_edit_point_meta.hide();
						}
					}
					
					
					
				}else{
					
					MyStasi tmp = params.staseis.item.get(stasi_index);
					tmp.highlight(true,0);
					params.selectedStasi=stasi_index;
					
					
					if(params.mode.edit_data){
						params.window_stasi.hide();
						params.window_edit_data.set_data(stasi_index);
					}else{
						params.window_stasi.curOpenStasiIndex=stasi_index;
						params.window_stasi.show();
						
						
						String name = "";
						//if(params.staseis.item.get(stasi_index).name.contains("added")){
							//name = "ST" + (stasi_index+1);
						//}else{
							name = params.staseis.item.get(stasi_index).name();
						//}
						params.window_stasi.textViewName.setText(name);
						params.window_stasi.textViewX.setText((float)Math.round(tmp.point.x*1000)/1000 + " ");
						params.window_stasi.textViewY.setText((float)Math.round(tmp.point.y*1000)/1000 + " ");
					}
					params.window_edit_point_meta.hide();
					
				}
			
			
			}
			
		}
			
		//params.surface.requestRender();
        
		
	}

	public void setSelectedPoint(double x, double y){
		pointSelected=true;
		selectedx=x;
		selectedy=y;
		selectedy=y;
		selectedy=y;
		params.renderer.pointHighlight.removeByFlag(0);
		params.renderer.pointHighlight.add(x,y,255,0,0,0);
		params.renderer.pointHighlight.regen_coor();
	}
	

	public void setSelectedPoint(double x, double y,int flag){
		
		params.renderer.pointHighlight.removeByFlag(flag);
		if(flag==0){
			pointSelected=true;
			selectedx=x;
			selectedy=y;
			selectedy=y;
			selectedy=y;
			params.renderer.pointHighlight.add(x,y,255,0,0,flag);
		}else{
			point2Selected=true;
			selected2x=x;
			selected2y=y;
			selected2y=y;
			selected2y=y;
			params.renderer.pointHighlight.add(x,y,0,255,0,flag);
		}
		
		params.renderer.pointHighlight.regen_coor();
	}
	
	public void setSelectedPoint2(double x, double y){
		pointSelected=true;
		selectedx=x;
		selectedy=y;
		selectedy=y;
		selectedy=y;
		params.renderer.pointHighlight.removeByFlag(2);
		params.renderer.pointHighlight.add(x,y,255,0,0,2);
		params.renderer.pointHighlight.regen_coor();
	}
	
	
	public boolean  clickPoint(double x, double y,int dpixel){
		
		
		boolean out=false;
		if(dpixel<20 && params.renderer.points.drawLayer){
				//dpixel metaboli se pixel gia na xero an tha kano render
				int i;				int min_dist_index=-1;			double dx,dy,min=99999999999999999999999d,dist;			ArrayList<MyPoint> item = params.renderer.points.item;
				
				for(i=0;i<=item.size()-1;i++){
					MyPoint tmp = item.get(i);
					dx = tmp.x-x;		dy = tmp.y-y;			dist = dx*dx+dy*dy;
					if(dist<min) {min = dist;min_dist_index=i;};
				}
				
				float dd = (float) (Math.sqrt(min)/params.pixel2world);
											
					if(dd<params.tapSelectPixelWindow){
						MyPoint tmp = item.get(min_dist_index);
						this.selectedPointObject=tmp;
						params.window_edit_point_meta.spinnerType.setSelection(tmp.metrisi.obtype);
						params.window_edit_point_meta.et1.setText(tmp.metrisi.sxolia);
						setSelectedPoint(tmp.x,tmp.y);		
						out = true;
					}else{
						pointSelected=false;
						params.window_edit_point_meta.spinnerType.setSelection(0);
						params.renderer.pointHighlight.item.clear();
					}		
			}
		
		return out;
	}
	
	public void clearSelectePoints(){
		pointSelected=false;
		point2Selected=false;
		params.window_edit_point_meta.spinnerType.setSelection(0);
		params.renderer.pointHighlight.item.clear();
	}
	public boolean clickPoint(double x, double y,int dpixel,int flag){
		boolean out=false;
		if(dpixel<20 && params.renderer.points.drawLayer){
				//dpixel metaboli se pixel gia na xero an tha kano render
				int i;				int min_dist_index=-1;			double dx,dy,min=99999999999999999999999d,dist;			ArrayList<MyPoint> item = params.renderer.points.item;
				
				for(i=0;i<=item.size()-1;i++){
					MyPoint tmp = item.get(i);
					dx = tmp.x-x;		dy = tmp.y-y;			dist = dx*dx+dy*dy;
					if(dist<min) {min = dist;min_dist_index=i;};
				}
				
				float dd = (float) (Math.sqrt(min)/params.pixel2world);
											
					if(dd<params.tapSelectPixelWindow){
						MyPoint tmp = item.get(min_dist_index);
						setSelectedPoint(tmp.x,tmp.y,flag);	
						out = true;
											
					}else{
						if(flag==0){
							pointSelected=false;
						}else{
							point2Selected=false;
						}
						params.renderer.pointHighlight.removeByFlag(flag);
						
					}		
			}
		
		return out;
	}
	
	
	
	public boolean clickLine(double x, double y,int dpixel){
		boolean out = false;
		if(dpixel<20){
				//dpixel metaboli se pixel gia na xero an tha kano render
			MyPoly clicked = 	params.renderer.my_poly.clickEvent(x, y, dpixel);
			if(clicked !=null){
				lineSelected=true;
				//Log.i("meta" , "" + clicked.meta.type);
				
				out=true;
			}else{
				lineSelected=false;
			}
			selectedLineObject=clicked;
			
			//params.renderer.my_poly.item.re
		}
			return out;
	}
	


}
