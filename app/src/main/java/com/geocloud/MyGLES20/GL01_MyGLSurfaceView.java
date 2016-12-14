package com.geocloud.MyGLES20;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.app.MyGLSE20app;
import com.geocloud.topo.MyStasi;
import com.geocloud.wms.Ktima;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class GL01_MyGLSurfaceView extends GLSurfaceView {

    private GL02_MyGLRenderer 	mRenderer;
    private int 			mWidth;
    private int 			mHeight;
    MyGLSE20app 			myGLSE20app;
    private MyParams		params;
    public GL01_MyGLSurfaceView(Context context,int width, int height,MyGLSE20app myGLSE20app) {
        super(context);
        mHeight 			= height;
        mWidth 				= width;
        this.myGLSE20app	= myGLSE20app;
        
        setPreserveEGLContextOnPause(true);
        
        Log.i("width",width + "");
        //this.setKeepScreenOn(true);
       // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    @Override
	public void setRenderer(Renderer renderer) {
		// TODO Auto-generated method stub
    	mRenderer						=	(GL02_MyGLRenderer) renderer;
    	myGLSE20app.mGLRenderer			=	mRenderer;
    	myGLSE20app.params.setRenderer		(mRenderer);
    	params							=	myGLSE20app.params;
    	params.setImagePixelSize(mWidth, mHeight);
    	params.setScreenRatio(TOUCH_SCALE_FACTOR);
		super.setRenderer(renderer);
		
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		
	}

    
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
    	Log.i("surface","resumed");
		super.onResume();
	}


	private final 	float 	TOUCH_SCALE_FACTOR = 10.0f / 16;
    private 		float 	mPreviousX,mPreviousY;						//x,y eye tou render sto touchdown
    private 		float 	mStartX,mStartY;							//x,y se pixel sto touchdown
    private 		double 	mStartX1,mStartY1;							//x,y se pixel sto touchdown tou defterou
    private 		float 	mStartX2,mStartY2;							//x,y se pixel sto touchdown tou defterou
    private			float	mDist;
   private 		float	lastmovex,lastmovey;
   // private 		float	lastmovex2,lastmovey2;
     private 		boolean press2 = false;
     private 		float 	mPreviousScale;
     private 		float 	startScale;   //mono gia to event (mplokaro sto scale to click event)
      
     float nx;
     float ny;
     public float lastLonLat[] = new float[2];
     
     public double tmpx,tmpy,tmpx2,tmpy2,tmpa0;
     
    // private 		float 	mLastScale;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
       
    	
    	
    	float x = e.getX();			//x se pixel		
        float y = e.getY();			//y se pixel
     
        
       
        
        //params.debug(params.mode.edit_line_mode + "");
        
        if(params.mode.mode==1 && params.window_yo.isOpen()){
        	/*SET YO
        	 */
        	
        	 
        	 switch (e.getActionMasked( )) {
	      		case MotionEvent.ACTION_UP: 
	      			int tx=(int) e.getX();;
	       			int ty=(int) e.getY();;
	       			//params.debug(((float)(-ty+mStartY1)/params.imagePixelHeight)+"");
	       			params.window_yo.set_yo((float) ((float)(-ty+mStartY1)/params.imagePixelHeight/10),true);
	      			break;
	     		case MotionEvent.ACTION_DOWN: 
	     			mStartX1=(int) e.getX();;
	       			mStartY1=(int) e.getY();;
	     			break;  
	         	case MotionEvent.ACTION_MOVE:
	         		int tx2=(int) e.getX();;
	       			int ty2=(int) e.getY();;
	       			//params.debug(((float)(-ty+mStartY1)/params.imagePixelHeight)+"");
	       			params.debug(((float)(-ty2+mStartY1)/params.imagePixelHeight)+"");
	       			params.window_yo.set_yo((float) ( (float)(-ty2+mStartY1)/params.imagePixelHeight/10),false);
	       			
	         		break;
	            default:
	            	
        	 }
        } else if(params.mode.edit_data && params.mode.edit_data_move_graphical){
        	/*SET YO
        	 */
        	
        	MyStasi st ;
   			MyStasi st0 ;
			
			
        	 switch (e.getActionMasked( )) {
	      		case MotionEvent.ACTION_UP: 
	      			params.renderer.points.clear();
	      			params.odefsi.draw_tax();
	      			
	      			
	      			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
	       			st.db_update_coor();
	       			st0.db_update_coor();
	       			//st
	      			break;
	     		case MotionEvent.ACTION_DOWN:
	     			mStartX1=(int) e.getX();;
	       			mStartY1=(int) e.getY();;
	       			
	       			
	       			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
	       			
	       			tmpx = st.l;
	       			tmpy = st.f;
	       			
	       			tmpx2 = st0.l;
	       			tmpy2 = st0.f;
	       			
	       			
	     			break;  
	         	case MotionEvent.ACTION_MOVE:
	         		int tx2=(int) e.getX();;
	       			int ty2=(int) e.getY();;
	       			
	       			double dx = (tx2-mStartX1)*params.pixel2world;
	       			double dy = -(ty2-mStartY1)*params.pixel2world;
	       			
	       			double[] fl = new double[2];
	       			fl[0] = tmpy + dy;
	       			fl[1] = tmpx + dx;
	       			
	       			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
					
					
					double[] c0 = new double[2];
					c0[1]=tmpx2+dx;
					c0[0]=tmpy2+dy;
					
					st0.setCoorFl(c0);
					
					
	       			
	       			st.setCoorFl(fl);
	       			//params.staseis.item.get((int) params.window_edit_data.curStasi).l=tmpx + dx;
	       			//params.staseis.item.get((int) params.window_edit_data.curStasi).f=tmpy + dy;
	       			
	       			
	       			params.odefsi.redraw(false);
	       			
	       			params.debug(fl[1]+"," + fl[0]);
	       			
	         		break;
	            default:
	            	
        	 }
        }else if(params.mode.edit_data && params.mode.edit_data_rotate_graphical){
        	/*SET YO
        	 */
        	
        	MyStasi st ;
   			MyStasi st0 ;
			
   			
   			
			
        	 switch (e.getActionMasked( )) {
	      		case MotionEvent.ACTION_UP: 
	      			params.renderer.points.clear();
	      			params.odefsi.draw_tax();
	      		
	      			
	      			
	      			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
	       			st.db_update_coor();
	       			st0.db_update_coor();
	       			
	       			
	      			break;
	     		case MotionEvent.ACTION_DOWN:
	     			mStartX1=(int) e.getX();;
	       			mStartY1=(int) e.getY();;
	       			
	       			
	       			float pos2[] = mRenderer.get_ortho();		//x,y eye tou render sto touchdown
				 	mPreviousX = pos2[0];///mRenderer.scale;
				 	mPreviousY = pos2[1];///mRenderer.scale;
				 	
	       			
				
				 	
				 	float nx2 = (float) ((float)  mPreviousX+(mStartX1-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR);		//render system
			        float ny2 = (float) ((float)  mPreviousY-(mStartY1-mHeight/2)/mHeight*2);
			       // float center[] =mRenderer.grid.xy2lf(nx2,ny2);
			       // mRenderer.set_ortho(nx2, ny2,true);
			        
			       // float tapx = (float)  mPreviousX+(mStartX-x+x-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR;		//render system
			       // float tapy = (float)  mPreviousY-(mStartY-y+y-mHeight/2)/mHeight*2;
			        //float tap[] =mRenderer.grid.xy2lf(tapx,tapy);
			        
			        
			       // params.center.setRender(nx2, ny2);
			       // params.center.setPixel(mWidth/2, mHeight/2);
			       // params.center.setWorld(center[0], center[1]);   
			        
			        
			        
	       			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
	       			
	       			tmpx = st.l;
	       			tmpy = st.f;
	       			
	       			tmpx2 = st0.l;
	       			tmpy2 = st0.f;
	       			
	       			float[] lf = params.renderer.grid.xy2lf(nx2,ny2);
	       			
	       			//Log.i(tmpx + "," + tmpy,lf[0] + "," + lf[1]);
	       			double dl = lf[0]-tmpx;
	       			double df =  lf[1]-tmpy;
	       			
	       			tmpa0 = params.util.getAzimuth_by_dxdy_inn(dl,df);
	       			//Log.i("start a0",tmpa0 + "");
	     			break;  
	         	case MotionEvent.ACTION_MOVE:
	         		float tx2=(int) e.getX();;
	       			float ty2=(int) e.getY();;
	       			
	       			//double dx = -(tx2-mStartX1)*params.pixel2world;
	       			//double dy = -(ty2-mStartY1)*params.pixel2world;
	       			
	       			//double[] fl = new double[2];
	       			//fl[0] = tmpy + dy;
	       			//fl[1] = tmpx + dx;
	       			
	       			st =params.staseis.item.get((int) params.window_edit_data.curStasi);
	       			st0 =params.msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
					
					
	       			
	       			float nx22 = (float) ((float)  mPreviousX+(tx2-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR);		//render system
			        float ny22 = (float) ((float)  mPreviousY-(ty2-mHeight/2)/mHeight*2);
			       
			        
			        
			        float[] lf2 = params.renderer.grid.xy2lf(nx22,ny22);
	       			
	       			//Log.i(nx22 + "," + ny22,tx2 + "," + ty2);
	       			double dl2 = lf2[0]-tmpx;
	       			double df2 =  lf2[1]-tmpy;
	       			
	       			double tmpa02 = params.util.getAzimuth_by_dxdy_inn(dl2,df2);
					
	       			
	       			double[] egst0 = params.wms.proj.fl2EGSA87(tmpy2, tmpx2) ;
	       			double[] egst = params.wms.proj.fl2EGSA87(tmpy, tmpx) ;
	       			
	       			double dx = egst[0]-egst0[0];
	       			double dy = egst[1]-egst0[1];
	       			double a0 = params.util.getAzimuth_by_dxdy_inn(-dx, -dy);
	       			double dist = Math.sqrt(dx*dx+dy*dy);
	       			
	       			
	       			double x0 = egst[0] + dist*Math.sin((a0+(tmpa02-tmpa0))*Math.PI/200);
	       			double y0 = egst[1] + dist*Math.cos((a0+(tmpa02-tmpa0))*Math.PI/200);
	       			double[] fl0 = params.wms.proj.Egsa2fl84(x0, y0);
	       			st0.setCoorFl(fl0);
	       			//Log.i("-> "+tmpa0 +  "-> " + tmpa02 , " = " + (tmpa02-tmpa0));
					//double[] c0 = new double[2];
					//c0[1]=tmpx2+dx;
					//c0[0]=tmpy2+dy;
					
					//st0.setCoorFl(c0);
					
					
	       			
	       			//st.setCoorFl(fl);
	       			//params.staseis.item.get((int) params.window_edit_data.curStasi).l=tmpx + dx;
	       			//params.staseis.item.get((int) params.window_edit_data.curStasi).f=tmpy + dy;
	       			
	       			
	       			params.odefsi.redraw(false);
	       			
	       			//params.debug(fl[1]+"," + fl[0]);
	       			
	         		break;
	            default:
	            	
        	 }
        }else if(params.mode.edit_data && 8==4){
        	/*SET YO
        	 */
        	
        	 
        	 switch (e.getActionMasked( )) {
	      		case MotionEvent.ACTION_UP: 
	      			break;
	     		case MotionEvent.ACTION_DOWN: 
	     			break;  
	         	case MotionEvent.ACTION_MOVE:
	         		break;
	            default:
	            	
        	 }
        }else if(params.mode.edit_line && params.mode.edit_line_mode==1){
        	/*EXTEND
        	 * 
        	 */
        	
        	int px=(int) x;;
			int py=(int) y;;
			
			
			if(params.window_edit_linemod_verify.isOpen()){
				params.window_edit_linemod_verify.show(px+40,py-250);
			}else{
				
			
        	switch (e.getActionMasked( )) {
      		case MotionEvent.ACTION_UP: 
      			//params.renderer.my_poly.remove_last();
      			//params.renderer.my_poly.regen_coor_silent();
      			
      			
      			double tx=(int) e.getX();;
      			double ty=(int) e.getY();;
       			double di =(double)  Math.round((-ty+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(di+"");
       			
       			params.window_edit_linemod_verify.show(px+40,py-250,di);
      			
       			
       			//params.event.selectedLineObject.extend(di);
       			//params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.indexOf(params.event.selectedLineObject));
         		break;
     		case MotionEvent.ACTION_DOWN: 
     			
     			
     			MyPoly tmp = params.renderer.my_poly.add(255f, 0f, 255f, 0);
     			MyPoint po1 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po2 = params.event.selectedLineObject.getVertice(1);
     			tmp.addVertice(po1.x, po1.y);
				tmp.addVertice(po2.x, po2.y);
				params.renderer.my_poly.regen_coor();
     			
     			//params.renderer.my_poly.add(tmp);
     			//params.renderer.my_poly.regen_coor();
     			//params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.indexOf(params.event.selectedLineObject));
     			//params.renderer.my_poly.regen_coor();
         		
     			
     			mStartX1=(int) e.getX();;
       			mStartY1=(int) e.getY();;
     			break;  
         	case MotionEvent.ACTION_MOVE:
         		//params.renderer.my_poly.regen_coor_silent();
         		double tx2=(int) e.getX();;
         		double ty2=(int) e.getY();;
       			
       			
         		params.renderer.my_poly.remove_last();
         		MyPoly tmp0 = params.renderer.my_poly.add(255f, 0f, 0f, 0);
     			MyPoint po01 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po02 = params.event.selectedLineObject.getVertice(1);
     			tmp0.addVertice(po01.x, po01.y);
				tmp0.addVertice(po02.x, po02.y);
				tmp0.extend((double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100);
				params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.size()-1);
     			
     			
         		
       			double dii = (double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(dii+"");
       			break;
            default:
        	
        	}
			}
        	
        }else if(params.mode.edit_line && params.mode.edit_line_mode==3){
        	/*OFFSET
        	 * 
        	 */
        	
        	int px=(int) x;;
			int py=(int) y;;
			
			
			if(params.window_edit_linemod_verify.isOpen()){
				params.window_edit_linemod_verify.show(px+40,py-250);
			}else{
				
			
        	switch (e.getActionMasked( )) {
      		case MotionEvent.ACTION_UP: 
      			int tx=(int) e.getX();;
       			int ty=(int) e.getY();;
       			double di =(double)  Math.round((-ty+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(di+"");
       			
       			params.window_edit_linemod_verify.show(px+40,py-250,di);
      			
       			
       			//params.event.selectedLineObject.extend(di);
       			//params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.indexOf(params.event.selectedLineObject));
         		break;
     		case MotionEvent.ACTION_DOWN: 
     			
     			
     			MyPoly tmp = params.renderer.my_poly.add(255f, 0f, 255f, 0);
     			MyPoint po1 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po2 = params.event.selectedLineObject.getVertice(1);
     			tmp.addVertice(po1.x, po1.y);
				tmp.addVertice(po2.x, po2.y);
				params.renderer.my_poly.regen_coor();
     			
     			mStartX1=(int) e.getX();;
       			mStartY1=(int) e.getY();;
     			break;  
         	case MotionEvent.ACTION_MOVE:
         		//params.renderer.my_poly.regen_coor_silent();
         		double tx2=(int) e.getX();;
         		double ty2=(int) e.getY();
       			
       			
         		params.renderer.my_poly.remove_last();
         		MyPoly tmp0 = params.renderer.my_poly.add(255f, 0f, 0f, 0);
     			MyPoint po01 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po02 = params.event.selectedLineObject.getVertice(1);
     			tmp0.addVertice(po01.x, po01.y);
				tmp0.addVertice(po02.x, po02.y);
				tmp0.offset((double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100);
				params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.size()-1);
     			
     			
         		
       			double dii = (double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(dii+"");
       			break;
            default:
        	
        	}
			}
        	
        }else if(params.mode.edit_line && params.mode.edit_line_mode==2){
        	/*OFFSET
        	 * 
        	 */
        	
        	int px=(int) x;;
			int py=(int) y;;
			
			
			if(params.window_edit_linemod_verify.isOpen()){
				params.window_edit_linemod_verify.show(px+40,py-250);
			}else{
				
			
        	switch (e.getActionMasked( )) {
      		case MotionEvent.ACTION_UP: 
      			//int tx=(int) e.getX();;
       			//int ty=(int) e.getY();;
       			//double di =(double)  Math.round((-ty+mStartY1)/params.imagePixelHeight*100)/100;
       			//params.debug(di+"");
       			
       			//params.window_edit_linemod_verify.show(px+40,py-250,di);
      			params.window_edit_linemod_verify.show(px+40,py-250);
       			
       			break;
     		case MotionEvent.ACTION_DOWN: 
     			
     			
     			//MyPoly tmp = params.renderer.my_poly.add(255f, 0f, 255f, 0);
     			//MyPoint po1 = params.event.selectedLineObject.getVertice(0);
     			//MyPoint po2 = params.event.selectedLineObject.getVertice(1);
     			//tmp.addVertice(po1.x, po1.y);
				//tmp.addVertice(po2.x, po2.y);
				//params.renderer.my_poly.regen_coor();
     			
     			mStartX1=(int) e.getX();;
       			mStartY1=(int) e.getY();;
     			break;  
         	case MotionEvent.ACTION_MOVE:
         		//params.renderer.my_poly.regen_coor_silent();
         		/*int tx2=(int) e.getX();;
       			int ty2=(int) e.getY();
       			
       			
         		params.renderer.my_poly.remove_last();
         		MyPoly tmp0 = params.renderer.my_poly.add(255f, 0f, 0f, 0);
     			MyPoint po01 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po02 = params.event.selectedLineObject.getVertice(1);
     			tmp0.addVertice(po01.x, po01.y);
				tmp0.addVertice(po02.x, po02.y);
				tmp0.offset((double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100);
				params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.size()-1);
     			
     			
         		
       			double dii = (double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(dii+"");*/
       			break;
            default:
        	
        	}
			}
        	
        }else if(params.mode.edit_line && params.mode.edit_line_mode==5){
        	/*META
        	 * 
        	 */
        	int px=(int) x;		int py=(int) y;
		
			if(params.window_edit_linemod_verify.isOpen()){
				params.window_edit_linemod_verify.show(px+40,py-250);
			}else{
	        	switch (e.getActionMasked( )) {
		      		case MotionEvent.ACTION_UP: 
		      			params.window_edit_line_meta.hide();
		      			params.mode.edit_line_mode=0;
		       			break;
		     		case MotionEvent.ACTION_DOWN: 
		     			break;  
		         	case MotionEvent.ACTION_MOVE:
		       			break;
		            default:
	        	}
			}
        	
        }else if(params.mode.edit_line && params.mode.edit_line_mode==4){
        	/*PER
        	 * 
        	 */
        	
        	int px=(int) x;;
			int py=(int) y;;
			
			
			if(params.window_edit_linemod_verify.isOpen() && 8==9){
				params.window_edit_linemod_verify.show(px+40,py-250);
			}else{
				
			
        	switch (e.getActionMasked( )) {
      		case MotionEvent.ACTION_UP: 
      			//int tx=(int) e.getX();;
       			//int ty=(int) e.getY();;
       			//double di =(double)  Math.round((-ty+mStartY1)/params.imagePixelHeight*100)/100;
       			//params.debug(di+"");
      			
       			//params.window_edit_linemod_verify.show(px+40,py-250,di);
      			params.window_edit_linemod_verify.show(px+40,py-250);
       			
             	float pos2[] = mRenderer.get_ortho();		//x,y eye tou render sto touchdown
             	mPreviousX = pos2[0];///mRenderer.scale;
             	mPreviousY = pos2[1];///mRenderer.scale;
      			float nx2 = (float)  mPreviousX+(x-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR;		//render system
                float ny2 = (float)  mPreviousY-(y-mHeight/2)/mHeight*2;
               
                
      			float tap[] =mRenderer.grid.xy2lf(nx2,ny2);
      			params.renderer.my_poly.removeByFlag(2);
      			if(params.event.clickPoint(tap[0], tap[1], 0,0)){
      				params.debug(/*nx2 + " : " +*/ tap[0] + ","+tap[1]);;
      				double  x0 = params.event.selectedx;
      				double  y0 = params.event.selectedy;
      				
      				MyPoint po = params.event.selectedLineObject.projectionOf(x0, y0);
      				//MyPoint po2 = params.event.selectedLineObject.projectionOf(x0, y0);
      				
      				MyPoly tmp = params.renderer.my_poly.add(255f, 0f, 255f, 2);
         			//MyPoint po1 = params.event.selectedLineObject.getVertice(0);
         			//MyPoint po2 = params.event.selectedLineObject.getVertice(1);
         			tmp.addVertice(x0, y0);
    				tmp.addVertice(po.x, po.y);
    				params.renderer.my_poly.regen_coor();
      				
      				
      			}
      			
      			
      			 //params.debug(/*nx2 + " : " +*/ tap[0] + ","+tap[1]);;
       			
                
       			break;
     		case MotionEvent.ACTION_DOWN: 
     			
     			
     			//MyPoly tmp = params.renderer.my_poly.add(255f, 0f, 255f, 0);
     			//MyPoint po1 = params.event.selectedLineObject.getVertice(0);
     			//MyPoint po2 = params.event.selectedLineObject.getVertice(1);
     			//tmp.addVertice(po1.x, po1.y);
				//tmp.addVertice(po2.x, po2.y);
				//params.renderer.my_poly.regen_coor();
     			
     			//mStartX1=(int) e.getX();;
       			//mStartY1=(int) e.getY();;
       			
       			
       			//float tapx = (float)  mPreviousX+(mStartX-x+x-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR;		//render system
               // float tapy = (float)  mPreviousY-(mStartY-y+y-mHeight/2)/mHeight*2;
                //params.event.clickPoint(mStartX1,mStartY1,)
       			
       			
     			break;  
         	case MotionEvent.ACTION_MOVE:
         		//params.renderer.my_poly.regen_coor_silent();
         		/*int tx2=(int) e.getX();;
       			int ty2=(int) e.getY();
       			
       			
         		params.renderer.my_poly.remove_last();
         		MyPoly tmp0 = params.renderer.my_poly.add(255f, 0f, 0f, 0);
     			MyPoint po01 = params.event.selectedLineObject.getVertice(0);
     			MyPoint po02 = params.event.selectedLineObject.getVertice(1);
     			tmp0.addVertice(po01.x, po01.y);
				tmp0.addVertice(po02.x, po02.y);
				tmp0.offset((double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100);
				params.renderer.my_poly.regen_coor(params.renderer.my_poly.item.size()-1);
     			
     			
         		
       			double dii = (double) Math.round((-ty2+mStartY1)/params.imagePixelHeight*100)/100;
       			params.debug(dii+"");*/
       			break;
            default:
        	
        	}
			}
        	
        }else{
        	
       
        try {
			switch (e.getActionMasked( )) {
				case MotionEvent.ACTION_POINTER_UP: 
					if(e.getActionIndex()<2){
					press2=false;
					
					if(e.getActionIndex()==1){
					mStartX=x;
			     	mStartY=y;
					}else{
						
						mStartX=(int) e.getX(e.getPointerId(1));;
			         	mStartY=(int) e.getY(e.getPointerId(1));;
					}
			     	
			     	float pos[] = mRenderer.get_ortho();		//x,y eye tou render sto touchdown
			     	mPreviousX = pos[0];///mRenderer.scale;
			     	mPreviousY = pos[1];///mRenderer.scale;
					}
			     	//myGLSE20app.text2.setText(" : " + e.getActionIndex());
					//mLastScale
					break;
				case MotionEvent.ACTION_POINTER_DOWN: 
					if(e.getActionIndex()<2){
					if(e.getActionIndex()==1){
			 			mStartX1=(int) e.getX(e.getPointerId(1));;
			   			mStartY1=(int) e.getY(e.getPointerId(1));;
			   			mStartX2=lastmovex;
			   			mStartY2=lastmovey;
					}else{
						mStartX2=x;;
			   			mStartY2=y;;
			   			mStartX1=lastmovex;
			   			mStartY1=lastmovey;
					}
					
					mDist = (float) Math.sqrt((mStartX2-mStartX1)*(mStartX2-mStartX1)+(mStartY2-mStartY1)*(mStartY2-mStartY1));
					mPreviousScale=myGLSE20app.mGLRenderer.scale;
					//myGLSE20app.text2.setText("2 down"  + " - " + mDist + "," );
					press2=true;
					}
					break;
      
			case MotionEvent.ACTION_MOVE:
					try {
						//new Thread();	
						Thread.sleep(6);	//Gia na perioriso ta events/sec  oste na min exo spasimata (me dokimi to 6)
					} catch (InterruptedException e1) {
					
					}
					
						
			        
					if(press2){
						//myGLSE20app.text1.setText(" : " + x + "," + y);
						
						int tx=(int) e.getX(e.getPointerId(1));;
			   			int ty=(int) e.getY(e.getPointerId(1));;
			   			//lastmovex2=tx;
			        	//lastmovey2=ty;
			        	
			   			float tDist = (float) Math.sqrt((tx-x)*(tx-x)+(ty-y)*(ty-y));
			   			myGLSE20app.mGLRenderer.scale= mPreviousScale*tDist/mDist ;
			   			myGLSE20app.text1.setText("Local Scale" + " : " + (myGLSE20app.mGLRenderer.scale/params.screen_scale_factor));
			   			mRenderer.set_ortho(nx*mRenderer.scale, ny*mRenderer.scale,false);
			   			
			   			//mRenderer.kml_poly.regen_coor();
			   			
			   		//  myGLSE20app.text2.setText(nx*mRenderer.scale + " : " +ny*mRenderer.scale);
			   			//mLastScale= tDist/mDist*mPreviousScale;
			   			//myGLSE20app.text1.setText("");
			   		// mRenderer.grid.setVisibleGridCells();
			   			
					}else{
						lastmovex=x;
			        	lastmovey=y;
						float 	ratio = TOUCH_SCALE_FACTOR;		//ratio = mHeight/mWidth/2.0f;
						nx = (float)  mPreviousX+(mStartX-x)/mWidth*2*ratio;		//cur x,y se render datum
			            ny = (float)  mPreviousY-(mStartY-y)/mHeight*2;
			           // myGLSE20app.text_debug.setText(x + " : " +y);
			            lastLonLat=myGLSE20app.myGLSE20app.grid.xy2lf(nx,ny);
			            mRenderer.curx = lastLonLat[0];
			            mRenderer.cury = lastLonLat[1];
			            myGLSE20app.text1.setText(mRenderer.zoom_level + " (" + String.valueOf(mRenderer.scale/params.screen_scale_factor).substring(0, 3) + ") : " + myGLSE20app.grid.xy2string(nx,ny));
			            
			            
			            
			            //float xy[] = mRenderer.grid.lf2xy(lastLonLat[0],lastLonLat[1]);
			           // myGLSE20app.text1.setText(xy[0] + "," + xy[1] + " : " + nx + "," +ny);
			            mRenderer.set_ortho(nx, ny,false); 
			            
			            /*
			            float xy[] = mRenderer.grid.lf2xy(lastLonLat[0],lastLonLat[1]);
			            float nx2 = (float)  mPreviousX+(mStartX-xy[0])/mWidth*2*TOUCH_SCALE_FACTOR;
			             float ny2 = (float)  mPreviousY-(mStartY-xy[1])/mHeight*2;
			             mRenderer.set_ortho(xy[0], xy[1],true);  
			            mRenderer.grid.setVisibleGridCells(); //////////////////////////////
			            */
			            
			            
			            
			            
			            nx=nx/mRenderer.scale;
			            ny=ny/mRenderer.scale;
			            
					}
			     break;
			 case MotionEvent.ACTION_DOWN:
				 //params.app.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				 startScale=mRenderer.scale;
				 mStartX=x;
			 	mStartY=y;
			 	
			 	
			 	float pos2[] = mRenderer.get_ortho();		//x,y eye tou render sto touchdown
			 	mPreviousX = pos2[0];///mRenderer.scale;
			 	mPreviousY = pos2[1];///mRenderer.scale;
			 	break;
			 case MotionEvent.ACTION_UP:
			 	
				 
				 int px=(int) x;;
					int py=(int) y;;
					
					
				 boolean max_level_reached = mRenderer.zoom_level>=mRenderer.max_zoom_level;
				 float lf[] = mRenderer.grid.xy2lf(x, y);
				 
				 if(mRenderer.scale<0.5*params.screen_scale_factor ){
					 	mRenderer.zoom_level-=(Math.floor(1.0/mRenderer.scale*params.screen_scale_factor)-1);
			   			mRenderer.scale=mRenderer.scale*((float)Math.pow(2,((float) (Math.floor(1.0/mRenderer.scale*params.screen_scale_factor)-1))));
			   		
			   		mRenderer.set_OriginLonLat(myGLSE20app.Lon0,myGLSE20app.Lat0);
			   		//mRenderer.originBima= mRenderer.ktima.get_bima(mRenderer.zoom_level);
			   		mRenderer.grid.originBima=mRenderer.originBima;
			   		mRenderer.grid.originLon = mRenderer.originLon;
			   		mRenderer.grid.originLat = mRenderer.originLat;
			   		mRenderer.grid.clear();
			   		
			   		mRenderer.points.regen_coor();
			   		mRenderer.kml_points.regen_coor();
			   		mRenderer.stasiPoints.regen_coor();
			   		
			   			mRenderer.pointHighlight.regen_coor();
			   	
			   		
			   		mRenderer.poly.regen_coor();
			   		mRenderer.kml_poly.regen_coor();
			   		mRenderer.odefsi_poly.regen_coor();

			  		mRenderer.my_poly.regen_coor();
			  		
			   		float xy[] = mRenderer.grid.lf2xy(lastLonLat[0],lastLonLat[1]);
			   		
			   		float nx2 = (float)  mPreviousX+(mStartX-x)/mWidth*2*TOUCH_SCALE_FACTOR;
			          float ny2 = (float)  mPreviousY-(mStartY-y)/mHeight*2;
			          mRenderer.set_ortho(xy[0], xy[1],true);
			          mRenderer.grid.removeOffGridCells();		//afairo ta tiles pou einai makria mou
			          											//to kno sto telos giati den xero an ta i teliki thesi t periexei
			          
			          
			         
				 }
				 else if(mRenderer.scale>2*params.screen_scale_factor  && !max_level_reached ){
					 float dz = 0;
					 if((mRenderer.zoom_level+Math.floor(mRenderer.scale/params.screen_scale_factor/2))>mRenderer.max_zoom_level){
						 dz = mRenderer.max_zoom_level-mRenderer.zoom_level;
					 }else{
						 dz=(float) Math.floor(mRenderer.scale/params.screen_scale_factor/2);
					 }
					 mRenderer.zoom_level+=dz;
			  		mRenderer.scale= mRenderer.scale/((float)Math.pow(2, dz));
			  		params.debug(myGLSE20app.Lon0+ "," + myGLSE20app.Lat0);
			  		mRenderer.set_OriginLonLat(myGLSE20app.Lon0,myGLSE20app.Lat0);
			  		//mRenderer.originBima= mRenderer.ktima.get_bima(mRenderer.zoom_level);
			  		mRenderer.grid.originBima=mRenderer.originBima;
			  		mRenderer.grid.originLon = mRenderer.originLon;
			  		mRenderer.grid.originLat = mRenderer.originLat;
			  		mRenderer.grid.clear();
			  		
			  		mRenderer.stasiPoints.regen_coor();
			  		mRenderer.points.regen_coor();
			  		mRenderer.kml_points.regen_coor();
			  		mRenderer.pointHighlight.regen_coor();
			  		mRenderer.poly.regen_coor();
			  		mRenderer.kml_poly.regen_coor();
			  		mRenderer.odefsi_poly.regen_coor();
			  		mRenderer.my_poly.regen_coor();
			  		
			  		float xy[] = mRenderer.grid.lf2xy(lastLonLat[0],lastLonLat[1]);
			  		
			  		float nx2 = (float)  mPreviousX+(mStartX-xy[0])/mWidth*2*TOUCH_SCALE_FACTOR;
			         float ny2 = (float)  mPreviousY-(mStartY-xy[1])/mHeight*2;
			         mRenderer.set_ortho(xy[0], xy[1],true);
			         mRenderer.grid.removeOffGridCells();		//afairo ta tiles pou einai makria mou
			         											//to kno sto telos giati den xero an ta i teliki thesi t periexei
			         
			 	 }else{
			 		
			 		 
			        
			        
			        float nx2 = (float)  mPreviousX+(mStartX-x)/mWidth*2*TOUCH_SCALE_FACTOR;		//render system
			        float ny2 = (float)  mPreviousY-(mStartY-y)/mHeight*2;
			        float center[] =mRenderer.grid.xy2lf(nx2,ny2);
			        mRenderer.set_ortho(nx2, ny2,true);
			        
			        float tapx = (float)  mPreviousX+(mStartX-x+x-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR;		//render system
			        float tapy = (float)  mPreviousY-(mStartY-y+y-mHeight/2)/mHeight*2;
			        float tap[] =mRenderer.grid.xy2lf(tapx,tapy);
			        
			        
			        params.center.setRender(nx2, ny2);
			        params.center.setPixel(mWidth/2, mHeight/2);
			        params.center.setWorld(center[0], center[1]);   
			        //params.center.log("SurfaceView - ACTION_UP");
			        
			        params.tap.setRender(tapx, tapy);
			        params.tap.setPixel(x, y);
			        params.tap.setWorld(tap[0], tap[1]);   
			        //params.tap.log("SurfaceView - ACTION_UP");
			        
			        
			        float 	ratio = TOUCH_SCALE_FACTOR;	
			        float ll[] = mRenderer.grid.xy2lf( mPreviousX+(mStartX-x-mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR,mPreviousY-(mStartY-y-mHeight/2)/mHeight*2);
			        float ur[] = mRenderer.grid.xy2lf( mPreviousX+(mStartX-x+mWidth/2)/mWidth*2*TOUCH_SCALE_FACTOR,mPreviousY-(mStartY-y+mHeight/2)/mHeight*2);
			        params.bbox.clear();
			        params.bbox.add(ll[0], ll[1]);
			        params.bbox.add(ur[0], ur[1]);
			        
			        params.pixel2world=-(-ll[1]+ur[1])/mHeight;
			        //params.bbox.log("SurfaceView - ACTION_UP");
			       
			        
			        mRenderer.grid.removeOffGridCells();		//afairo ta tiles pou einai makria mou
			        											//to kno sto telos giati den xero an ta i teliki thesi t periexei
			        //this.myGLSE20app.staseis.get_stasi_index_selected(mRenderer.curx,mRenderer.cury);
			       
			        
			        if(mRenderer.scale==startScale){
			        			params.event.triggerClick(params.tap.world[0],params.tap.world[1],(int) Math.sqrt((mStartX-x)*(mStartX-x)+(mStartY-y)*(mStartY-y)),px,py);
			        	//if(!params.mode.edit_line) this.myGLSE20app.staseis.get_stasi_index_selected(params.tap.world[0],params.tap.world[1],(int) Math.sqrt((mStartX-x)*(mStartX-x)+(mStartY-y)*(mStartY-y)));
			        			//params.event.clickPoint(params.tap.world[0],params.tap.world[1],(int) Math.sqrt((mStartX-x)*(mStartX-x)+(mStartY-y)*(mStartY-y)));
			        			//params.event.clickLine(params.tap.world[0],params.tap.world[1],(int) Math.sqrt((mStartX-x)*(mStartX-x)+(mStartY-y)*(mStartY-y)));
			                    
			        }
			        
			        
			        
			        }
			 	break;
			 
			 
			
			 
			 default:
			
    }
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  

        }
        //invalidate();
        
        
       this.requestRender();
        
        return true;
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
  

}
