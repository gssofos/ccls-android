

	package com.geocloud.MyGLES20;



import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.wms.Ktima;
import com.geocloud.wms.Wms;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;




	/**
	 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
	 * renderers -- the static class GLES20 is used instead.
	 */
	public class GL02_MyGLRenderer implements GLSurfaceView.Renderer 
	{	
		
		//private static final 	String 		TAG = "LessonFourRenderer";				/** Used for debug logs. */
		private final 			Context 	mActivityContext;
		public 					Ktima 		ktima; 			
		private final 			MyGLSE20app myGLSE20app;
		private 				MyParams	params;
		public 					int 		zoom_level	=	15;
		public 					int 		max_zoom_level	=	17;
		
		public float curx, cury;
		/**
		 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
		 * it positions things relative to our eye.
		 */
		private float[] mViewMatrix = new float[16];

		/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
		private float[] mProjectionMatrix = new float[16];
		
		
		public float scale = 1.0f;
		float offsetx = 0.0f;		//offsetx logo scale
		float offsety = 0.0f;
	    
	    
		/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
		//private float[] mMVPMatrix = new float[16];
		
		
		
		/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
		 *  we multiply this by our transformation matrices. */
		//private final float[] mLightPosInModelSpace = new float[] {10.0f, 10.0f,10.0f, 1.0f};
		
		/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
		private final float[] mLightPosInEyeSpace = new float[4];
		
		
		
		
		
		private float eyeX = 0.0f;		private float eyeY = 0.0f;		private float eyeZ = 100.0f;		// Position the eye in front of the origin.
		private float lookX = 0.0f;		private float lookY = 0.0f;		private float lookZ = 0.0f;	// We are looking toward the distance
		private float upX = 0.0f;		private float upY = 100.0f;		private float upZ = 100.0f;		// Set our up vector. This is where our head would be pointing were we holding the camera.

		
		public float originLat;			// Lat sto 0,0 eye
		public float originLon;			// Lon sto 0,0 eye
		public float originBima;		// df,dl ana tile
			
		private int originImageIndexX;	// IndexX of image tile index
		private int originImageIndexY;	// IndexY of image tile index
			
		public D_Image_Grid 		grid;
		public Vector_Cross 		cross;
		public Vector_Point_Layer 	points;
		public Vector_Point_Layer 	stasiPoints;
		public Vector_Point_Layer 	pointHighlight;
		public Vector_Poly_Layer 	poly;
		public Vector_Poly_Layer 	kml_poly;
		public Vector_Point_Layer 	kml_points;
		public Vector_Poly_Layer 	odefsi_poly;
		public Vector_Poly_Layer 	my_poly;
		public Vector_Marker_Layer 	marker;
		public Vector_Text_Layer 	text_layer;
		
		
		//public ArrayList<MyMeasureSet> 		msets = new ArrayList<MyMeasureSet>();
		
		
		private Wms wms;
		
		/**
		 * Initialize the model data.
		 */
		public GL02_MyGLRenderer(final Context activityContext)
		{	
			myGLSE20app				=	(MyGLSE20app) activityContext;
			params					=	myGLSE20app.params;
			mActivityContext		= 	activityContext;	
			wms						=	myGLSE20app.wms;
			
			scale = params.screen_scale_factor;
		}
		
		protected String getVertexShader()
		{
			return MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
		}
		
		protected String getFragmentShader()
		{
			return MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
		}
		
		public void set_OriginLonLat(float lon, float lat){
			//float[] 	res 				= 	ktima.getOriginFromLonLat(lon, lat, zoom_level);
			float[] 	res 				= 	wms.getOriginFromCoor(lon, lat, zoom_level);
						originLat			=	res[1];
						originLon			=	res[0];
						originBima 			= 	(float) res[4];
						originImageIndexX 	= 	(int) res[2];
						originImageIndexY 	= 	(int) res[3];
						Log.i("originLat",originLat+"");
						Log.i("originLon",originLon+"");
						Log.i("originBima",originBima+"");
						Log.i("originImageIndexX",originLat+"");
						Log.i("originImageIndexY",originImageIndexY+"");
						
		}
		
		public void set_ortho(float x, float y,boolean cellCreate){		// set eye position klp
			set_eye		(x,y,-0.5f);
			set_look	(x,y,-5.0f);
			set_up		(0.0f,1.0f,0.0f);
			Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
			if(cellCreate==true) {try{grid.setCurXY(x/(myGLSE20app.gssize*2)/scale,y/(myGLSE20app.gssize*2)/scale);}catch(Exception ex){Log.i("GLRenderer","setCurXY error");};};
		}
		
		public float[] get_ortho(){
			float[] out =new float[] {eyeX, eyeY};;
			return out; 
		}
		
		private void set_eye(float x, float y , float z){
			eyeX = x;	eyeY = y;	eyeZ = z;	// Position the eye in front of the origin.
		}
		private void set_look(float x, float y , float z){
			lookX = x;	lookY = y;	lookZ = z;	// Position the eye in front of the origin.
		}
		private void set_up(float x, float y , float z){
			upX = x;	upY = y;	upZ = z;	// Position the eye in front of the origin.
		}
		
		
		
		@Override
		public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
		{
			
			ktima 				= new Ktima(mActivityContext);
			
			GLES20.glClearColor(255f, 255f, 255f, 255f);			// Set the background clear color to black.	
			GLES20.glEnable(GLES20.GL_CULL_FACE);					// Use culling to remove back faces.
			GLES20.glLineWidth(3.0f);
			 
			
			//GLES20.glEnable(GLES20.GL_DEPTH_TEST);					// Enable depth testing
			//GLES20.glDepthFunc( GLES20.GL_LEQUAL );
			//GLES20.glDepthMask( true );
			
			set_OriginLonLat(myGLSE20app.Lon0,myGLSE20app.Lat0);
			//set_OriginLonLat(23.709f,38.012f);
			set_ortho(0.0f,0.0f,false);
				        
	        grid 	= 	new D_Image_Grid(myGLSE20app,mActivityContext,originLon,originLat/*,originImageIndexX,originImageIndexY*/,originBima);
	    	
	        grid.add(originLon, originLat,0.0f,0.0f);
	    	myGLSE20app.grid=grid;
	    	
	    	cross	=	new Vector_Cross(mActivityContext);
	    	
	    	pointHighlight	=	new Vector_Point_Layer(myGLSE20app,mActivityContext,"MyPointHighlightLayer",11f);
	    	pointHighlight.add(myGLSE20app.Lon0+0.0002, myGLSE20app.Lat0+0.0003,150f,120f,0f);
	    	pointHighlight.add(myGLSE20app.Lon0-0.0002, myGLSE20app.Lat0-0.0003,0f,255f,122f);
	    	
	    	
	    	points	=	new Vector_Point_Layer(myGLSE20app,mActivityContext,"MyTestPointLayer",5f);
	    	kml_points	=	new Vector_Point_Layer(myGLSE20app,mActivityContext,"kml_points",9f);
	    	stasiPoints	=	new Vector_Point_Layer(myGLSE20app,mActivityContext,"MyTestPointLayer",13f);
	    	//points.add(23.7092d, 37.912846d);
	    	//points.add(myGLSE20app.Lon0+0.0002, myGLSE20app.Lat0+0.0003);
	    	//points.add(myGLSE20app.Lon0-0.0002, myGLSE20app.Lat0-0.0003);
	    	
	    	poly	=	new Vector_Poly_Layer(myGLSE20app,mActivityContext,"MyTestPolyLayer",params);
	    	kml_poly	=	new Vector_Poly_Layer(myGLSE20app,mActivityContext,"MyKmlPolyLayer",params);
	    	odefsi_poly	=	new Vector_Poly_Layer(myGLSE20app,mActivityContext,"MyOdefsiPolyLayer",params);
	    	my_poly	=	new Vector_Poly_Layer(myGLSE20app,mActivityContext,"MyMetriseisPOlyLayer",params);
	    	
	    	/*MyPoly tmp = poly.add();
	    	tmp.addVertice(22.804527f, 37.567802f);
	    	tmp.addVertice(22.804504f, 37.567867f);
	    	tmp.addVertice(22.804632f, 37.568703f);
	    	tmp.addVertice(22.804667f, 37.56877f);
	    	poly.regen_coor();
	    	
	    	
	    	tmp = poly.add();
	    	tmp.addVertice(22.804527f, 37.567802f);
	    	
	    	tmp.addVertice(22.804667f, 37.56877f);
	    	poly.regen_coor();
	    	
	    	*/
	    	
	    	//points.add(0.2093d, 0.2d);
	    	//points.add(-0.1093d, 0.5d);
	    	
	    	
	    	
	    	marker	=	new Vector_Marker_Layer(myGLSE20app,mActivityContext,"MyTestPointLayer");
	    	//marker.add(myGLSE20app.Lon0+0.0002, myGLSE20app.Lat0+0.0003,1);
	    	//marker.add(22.807655, 37.56739,1);
	    	
	    	text_layer	=	new Vector_Text_Layer(myGLSE20app,mActivityContext,"MyTestTextLayer");
	    	text_layer.add(22.807655, 37.56739,"ST 1");
	    	text_layer.add(22.807655, 37.56759,"ST 2");
	    	text_layer.add(22.807655, 37.56749,"ST 3");
	    	
	    	
	    	//float xy[] = grid.lf2xy(myGLSE20app.Lon0,myGLSE20app.Lat0);
	   		//set_ortho(xy[0], xy[1],true);
	    	
	    	params.app.dbHelper.openDataBase();
	    	params.app.dbHelper.loadStaseisFromDb	(true);
	    	/*
	    	try {
				params.app.prolific_read_with_callback(100,false);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	*/
		}	
			
		@Override
		public void onSurfaceChanged(GL10 glUnused, int width, int height) 
		{
			// Set the OpenGL viewport to the same size as the surface.
			GLES20.glViewport(0, 0, width, height);

			// Create a new perspective projection matrix. The height will stay the same
			// while the width will vary as per aspect ratio.
			final float ratio = (float) width / height;
			final float left = -ratio;
			final float right = ratio;
			final float bottom = -1.0f;
			final float top = 1.0f;
			final float near = 1.0f;
			final float far = 10.0f;
			
			//Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);		//PROJ
			Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);			//ORTHO
		}	

		@Override
		public void onDrawFrame(GL10 glUnused) 
		{
			GLES20.glFlush();
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
			   
			
		    grid.	draw	(mLightPosInEyeSpace,mViewMatrix, mProjectionMatrix, scale);
		    if(!params.mode.edit_data)  points.	draw	(mViewMatrix, mProjectionMatrix, scale);
		   
		      marker.	draw	(mViewMatrix, mProjectionMatrix, scale);
			   stasiPoints.	draw	(mViewMatrix, mProjectionMatrix, scale);
			   pointHighlight.	draw	(mViewMatrix, mProjectionMatrix, scale);
				
		   // text_layer.	draw	(mViewMatrix, mProjectionMatrix, scale);
		   
		    poly.	draw	(mViewMatrix, mProjectionMatrix, scale);
		    kml_poly.	draw	(mViewMatrix, mProjectionMatrix, scale);
		    kml_points.	draw	(mViewMatrix, mProjectionMatrix, scale);
		    odefsi_poly.	draw	(mViewMatrix, mProjectionMatrix, scale);
		    if(!params.mode.edit_data) my_poly.	draw	(mViewMatrix, mProjectionMatrix, scale);
		       
			    cross.	draw	(mViewMatrix, mProjectionMatrix);
		    
		        
		}				
		
		
		
		
		
	}
