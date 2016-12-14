package com.geocloud.MyGLES20;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;

public class Vector_Text_Layer {
	//ArrayList<MyPoint> 		item = new ArrayList<MyPoint>();
	ArrayList<MyText> text_array = new ArrayList<MyText>();
	
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
	

	
	
	
	
	
	
	
	public Vector_Text_Layer(MyGLSE20app myGLSE20app,Context mActivityContext,String name){
		
		
		
		
		//text_array.add(new MyText("pin",R.drawable.pin_90,mActivityContext,true,1.0f));
		//text_array.add(new MyMarkerIcon("stasi",R.drawable.stasi_80,mActivityContext,false,1.2f));
		this.mActivityContext=mActivityContext;
		this.myGLSE20app=myGLSE20app;
		this.name=name;
		this.count=0;
		
		
		final String 	textvertexShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.text_per_pixel_vertex_shader);
        final String 	textfragmentShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.text_per_pixel_fragment_shader);
        final int textvertexShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, textvertexShader);		
		final int textfragmentShaderHandle = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, textfragmentShader);		
		
		
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(textvertexShaderHandle, textfragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});
		
		/*
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
        */
        
		
	}
	
	public void add(double x, double y,String mytext){
		//MyPoint tmp = new MyPoint(x,y);
		//tmp.setIconIndex(icon_index);
		//Log.i("asasas","1212121");
		text_array.add(new MyText(mytext,mActivityContext,new MyPoint(x,y),8.0f,this.myGLSE20app));
		//item.add(tmp); 
		this.count=this.count+1;
		
		//regen_coor();
		
		//return this.count;
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
	
	
	
	
	

	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale){
		
		int i =0;
		MyText tmp;
		
		
		for(i=0;i<=text_array.size()-1;i++){
			tmp = text_array.get(i);
			
			MyText text = text_array.get(i);
			float xy[] = myGLSE20app.grid.lf2xy_noscale((float)tmp.point.x,(float)tmp.point.y);
			//Log.i(text.name,tmp.point.x + "," + tmp.point.y + "," + scale);
			text.draw(mViewMatrix, mProjectionMatrix, scale, mProgramHandle,xy[0],xy[1]);
		}
		
		
        
	}
	
}





class MyText{
	String 		name;
	int 		drawable;
	private int 	mTextureDataHandle=-1;
	float size=0.02f;
	MyPoint point;
	

	// Create an empty, mutable bitmap
	//private Bitmap bitmap;
	// get a canvas to paint over the bitmap
	//private Canvas canvas;
	
	
	
	
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
	//private boolean bottom=false;
	
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
				
				
	
	
	public MyText(String name, Context mActivityContext,MyPoint point,float relative_scale,MyGLSE20app myGLSE20app){
		
		this.name = name;
		this.point = point;
		this.relative_scale=relative_scale;
		/*
		// Create an empty, mutable bitmap
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);
		
	
		
		// Draw the text
		Paint textPaint = new Paint();
		textPaint.setTextSize(32);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
		// draw the text centered
		canvas.drawText(name, 16,112, textPaint);
		
		*/
		/*
		
		FileOutputStream out;
		myGLSE20app.create_dir(String.format(myGLSE20app.baseDir + "/tmp/%s.jpg",name));
		try {
			out = new FileOutputStream(String.format(myGLSE20app.baseDir + "/tmp/%s.jpg",name));
			bitmap.compress(Bitmap.CompressFormat.JPEG,  90, out);
		} catch (FileNotFoundException e) {
			
		}
		bitmap.recycle();
		*/
		
		// Drawable d = Drawable.createFromPath((String.format(myGLSE20app.baseDir + "/%s.jpg",name)));
		
		// Initialize the buffers.
		markerPositions = ByteBuffer.allocateDirect(markerPositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		markerPositions.put(markerPositionData).position(0);		
				
		markerColors = ByteBuffer.allocateDirect(markerColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();							
		markerColors.put(markerColorData).position(0);			
					
		markerTextureCoordinates = ByteBuffer.allocateDirect(markerTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		markerTextureCoordinates.put(markerTextureCoordinateData).position(0);
		
		//mTextureDataHandle = MyGLTextureHelper.loadTextureFromFile(mActivityContext, String.format(myGLSE20app.baseDir + "/tmp/%s.jpg",name));
		mTextureDataHandle = MyGLTextureHelper.loadTextureFromString(mActivityContext, name);
		
		
		//bitmap.recycle();	
		
		
		//this.bottom=bottom;
		//this.relative_scale = relative_scale;
	}
	
	
	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale,int mProgramHandle,float x, float y){
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
        // Pass in the color information
      /*  markerColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, markerColors);        
        GLES20.glEnableVertexAttribArray(mColorHandle);*/
        // Pass in the texture coordinate information
        markerTextureCoordinates.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,0, markerTextureCoordinates);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
        
        GLES20.glEnableVertexAttribArray(mPositionHandle);
       // GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
       // Log.i(x + "," + y, "," + scale);
		
        Matrix.setIdentityM			(mModelMatrix, 0);
		Matrix.scaleM				(mModelMatrix, 0, scale*relative_scale,  scale*relative_scale,1.0f);
		//if(bottom) Matrix.translateM			(mModelMatrix, 0, 0.f,size, -2.0f);      
		Matrix.translateM			(mModelMatrix, 0, x,y, -2.0f);      
        Matrix.multiplyMM			(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        GLES20.glUniformMatrix4fv	(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                  
        Matrix.multiplyMM			(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv	(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays			(GLES20.GL_TRIANGLES, 0, 6);            
	}
	
}


