package com.geocloud.MyGLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;

public class Vector_Marker_Layer {
	public ArrayList<MyPoint> 		item = new ArrayList<MyPoint>();
	public ArrayList<MyMarkerIcon> icon = new ArrayList<MyMarkerIcon>();
	
	public String name;
	private int count=0;
	
	Context 	mActivityContext;
	MyGLSE20app myGLSE20app;
	private int mMVPMatrixHandle;			/** Used for debug logs. */
	private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */

	private int mPositionHandle;			/** This will be used to pass in model position information. */
	private int mColorHandle;				/** This will be used to pass in model color information. */


	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mModelMatrix = new float[16];	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	float size = 0.5f;
	
			
	/** This is a handle to our light point program. */
	private int mProgramHandle;
	

	
	
	
	
	
	
	
	public Vector_Marker_Layer(MyGLSE20app myGLSE20app,Context mActivityContext,String name){
		float ssf = myGLSE20app.params.screen_scale_factor;
		myGLSE20app.params.debug(ssf+"#");
		icon.add(new MyMarkerIcon("pin",R.drawable.pin_90,mActivityContext,true,1.0f,ssf));
		
		icon.add(new MyMarkerIcon("stasi",R.drawable.stasi_80,mActivityContext,false,1.2f,ssf));
		if(!myGLSE20app.params.stasiIconShow) icon.get(icon.size()-1).drawMarker=false;
		
		icon.add(new MyMarkerIcon("stasi_hl",R.drawable.stasi_hl_80,mActivityContext,false,1.2f,ssf));
		icon.add(new MyMarkerIcon("stasi_hl_orange",R.drawable.stasi_hl_orange_80,mActivityContext,false,1.2f,ssf));
		icon.add(new MyMarkerIcon("trig",R.drawable.trig,mActivityContext,false,1.2f,ssf));
		icon.add(new MyMarkerIcon("house",R.drawable.house,mActivityContext,false,1.2f,ssf));
		this.mActivityContext=mActivityContext;
		this.myGLSE20app=myGLSE20app;
		this.name=name;
		this.count=0;
		
		
		final String 	vertexShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
        final String 	fragmentShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
        final int vertexShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		
		
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});
		
		/*
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
        */
        
		
	}
	
	public void clear(){
		this.count=0;
		item.clear();
	}
	public void setMarkerIconIndex(int index){
		
	}
	
	
	public int add(double x, double y,int icon_index,int icon_hg_index,int icon_hg_index_2){
		MyPoint tmp = new MyPoint(x,y);
		tmp.setIconIndex(icon_index);
		tmp.addIconHlIndex(icon_hg_index);
		tmp.addIconHlIndex(icon_hg_index_2);
		tmp.setIconHlArrayIndex(1);
		item.add(tmp); 
		this.count=this.count+1;
		
		return this.count-1;
		//regen_coor();
		
		
	}
	
	
	
	
	
	
	
	public int add(double x, double y,int icon_index,int icon_hg_index){
		MyPoint tmp = new MyPoint(x,y);
		tmp.setIconIndex(icon_index);
		tmp.addIconHlIndex(icon_hg_index);
		item.add(tmp); 
		
		this.count=this.count+1;
		
		return this.count-1;
		//regen_coor();
		
		
	}
	
	public void add(double x, double y,int icon_index){
		MyPoint tmp = new MyPoint(x,y);
		tmp.setIconIndex(icon_index);
		item.add(tmp); 
		
		this.count=this.count+1;
		
		//regen_coor();
		
		
	}
	
	
	public void regen_coor(){
		/*
		int i =0;
		MyPoint tmp;
		for(i=0;i<=item.size()-1;i++){
			tmp = item.get(i);
			float xy[] = myGLSE20app.grid.lf2xy_noscale((float)tmp.x,(float)tmp.y);
			mPositionData[(i)*3]= 		xy[0];
			mPositionData[(i)*3+1]= 	xy[1];
			mPositionData[(i)*3+2]=(float) 	tmp.z;
		}
		
		//Log.i("re","ee");
		// Initialize the buffers.
		mPositions 	= ByteBuffer.allocateDirect(this.count*3 * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		//double mPositionData2 = Arrays.copyOfRange(mPositionData, 0, this.count*3-1);
		mPositions.put(mPositionData,0,this.count*3 ).position(0);	
		*/
	}
	
	
	
	public void highlight(int index, boolean value,int highlightIndex){
		MyPoint tmp = item.get(index);
		tmp.setIconHlArrayIndex(highlightIndex);
		tmp.highlighted=value;
	}
	
	public void highlight(int index, boolean value){
		MyPoint tmp = item.get(index);
		tmp.highlighted=value;
	}
	
	
	

	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale){
		
		int i =0;
		MyPoint tmp;
		
		
		for(i=0;i<=item.size()-1;i++){
			
			tmp = item.get(i);
			if(!tmp.deleted){
			//Log.i(icon.size()+"",(tmp.icon_index-1)+"");
			float xy[] = myGLSE20app.grid.lf2xy_noscale((float)tmp.x,(float)tmp.y);
			
			if(tmp.highlighted){
				MyMarkerIcon marker = icon.get(tmp.getIconHlIndex()-1);
				marker.draw(mViewMatrix, mProjectionMatrix, scale, mProgramHandle,xy[0],xy[1]);
			}
			
			MyMarkerIcon marker = icon.get(tmp.icon_index-1);
			marker.draw(mViewMatrix, mProjectionMatrix, scale, mProgramHandle,xy[0],xy[1]);
			
			
		}
		}
		
		
        
	}
	
}





class MyMarkerIcon{
	String 		name;
	int 		drawable;
	public int 	mTextureDataHandle=-1;
	float size=0.02f;
	public boolean drawMarker;
	
	
	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mModelMatrix = new float[16];	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	
	private final int mBytesPerFloat = 4;
	
	
	
	
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;	
	
	/** Size of the normal data in elements. */
	private final int mNormalDataSize = 3;
	
	/** Size of the texture coordinate data in elements. */
	private final int mTextureCoordinateDataSize = 2;
	
	
	
	
	
	
	
	/** Store our model data in a float buffer. */
	private  FloatBuffer markerPositions;
	private  FloatBuffer markerColors;
	private  FloatBuffer markerTextureCoordinates;
	
	private int mMVPMatrixHandle;			/** Used for debug logs. */
	private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */
	private int mPositionHandle;			/** This will be used to pass in model position information. */
	private int mColorHandle;				/** This will be used to pass in model color information. */
	private int mTextureCoordinateHandle;				/** This will be used to pass in model color information. */
	private float relative_scale=1.0f;
	float screen_scale_factor=1.0f;
	
	
	private boolean bottom=false;
	
	final float[] markerPositionData =
		{
				// In OpenGL counter-clockwise winding is default. This means that when we look at a triangle, 
				// if the points are counter-clockwise we are looking at the "front". If not we are looking at
				// the back. OpenGL has an optimization where all back-facing triangles are culled, since they
				// usually represent the backside of an object and aren't visible anyways.
				
				// Front face
				-size, size, 0.0f,				
				-size, -size, 0.0f,
				size, size, 0.0f, 
				-size, -size, 0.0f, 				
				size, -size, 0.0f,
				size, size, 0.0f
				
		
		};	
	
	
	final float[] markerTextureCoordinateData =
		{												
				// Front face
				0.0f, 0.0f, 				
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
		};
	
	// R, G, B, A
				final float[] markerColorData =
				{				
						// Front face (white)
						1.0f, 1.0f, 1.0f, 1.0f,				
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f,				
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f
					
				};
				
				
	
	
	public MyMarkerIcon(String name, int drawable,Context mActivityContext, boolean bottom,float relative_scale,float screen_scale_factor){
		
		drawMarker=true;
		
		// Initialize the buffers.
		markerPositions = ByteBuffer.allocateDirect(markerPositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		markerPositions.put(markerPositionData).position(0);		
				
		markerColors = ByteBuffer.allocateDirect(markerColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		markerColors.put(markerColorData).position(0);			
					
		markerTextureCoordinates = ByteBuffer.allocateDirect(markerTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		markerTextureCoordinates.put(markerTextureCoordinateData).position(0);
		
		mTextureDataHandle = MyGLTextureHelper.loadTexture(mActivityContext, drawable);
		this.bottom=bottom;
		//this.screen_scale_factor = screen_scale_factor;
		//this.relative_scale = relative_scale;
	}
	
	
	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale,int mProgramHandle,float x, float y){
		if(this.drawMarker){
		float scale0 = scale;
		scale=1.0f;
		
		GLES20.glUseProgram(mProgramHandle);
		
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
		  
		        
		// Set program handles for cube drawing.
        mMVPMatrixHandle 			= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle 			= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
        mPositionHandle 			= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle 				= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
        mTextureCoordinateHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
       
        
        
       
		// Pass in the position information
        markerPositions.position(0);		
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, markerPositions);             
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        markerTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,0, markerTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
        
        GLES20.glEnableVertexAttribArray(mPositionHandle);
       // GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        //Log.i(x + "," + y, "," + scale);
        Matrix.setIdentityM			(mModelMatrix, 0);
		Matrix.scaleM				(mModelMatrix, 0, scale*relative_scale*screen_scale_factor,  scale*relative_scale*screen_scale_factor,1.0f);
		if(bottom) Matrix.translateM			(mModelMatrix, 0, 0.f,scale0*size, -2.0f);      
		Matrix.translateM			(mModelMatrix, 0, scale0*x,scale0*y, -2.0f);      
        Matrix.multiplyMM			(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        GLES20.glUniformMatrix4fv	(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                  
        Matrix.multiplyMM			(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv	(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays			(GLES20.GL_TRIANGLES, 0, 6);            
	}
	}
	
}


