package com.geocloud.MyGLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;

public class Vector_Point_Layer {
	public ArrayList<MyPoint> item = new ArrayList<MyPoint>();
	public String name;
	private int count=0;
	public boolean drawLayer=true;
	Context 	mActivityContext;
	MyGLSE20app myGLSE20app;
	private int mMVPMatrixHandle;			/** Used for debug logs. */
	private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */

	private int mPositionHandle;			/** This will be used to pass in model position information. */
	private int mColorHandle;				/** This will be used to pass in model color information. */
	
	private float pointSize;

	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mModelMatrix = new float[16];	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	float size = 0.5f;
	/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
	 *  we multiply this by our transformation matrices. */
	private  		float[] 	mPositionData = new float[300*3];
	private  		float[] 	mPositionData2;
	private	 		float[]  	mColorData  = new float[300*4] ;
	
				
				// X, Y, Z
				// The normal is used in light calculations and is a vector which points
				// orthogonal to the plane of the surface. For a cube model, the normals
				// should be orthogonal to the points of each face.
				//final float[] mNormalData;
				
				// S, T (or X, Y)
				// Texture coordinate data.
				// Because images have a Y axis pointing downward (values increase as you move down the image) while
				// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
				// What's more is that the texture coordinates are the same for every face.
				//final float[] mTextureCoordinateData;
				
				
	
	
		
	/** This is a handle to our light point program. */
	private int mProgramHandle;
	

	
	/** Store our model data in a float buffer. */
	private  FloatBuffer mPositions;
	private  FloatBuffer mColors;
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;	
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;	
	
	
	
	public void clear(){
		this.count=0;
		item.clear();
		regen_coor();
	}
	
	public Vector_Point_Layer(MyGLSE20app myGLSE20app,Context mActivityContext,String name, float pointSize){
		this.mActivityContext=mActivityContext;
		this.myGLSE20app=myGLSE20app;
		this.name=name;
		this.count=0;
		
		this.pointSize=pointSize;
		final String vertexShader 		=  MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoint_vertex_shader);
        final String fragmentShader 	= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoint_fragment_shader);
        final int vertexShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
        
        
		
	}
	
	public int add(double x, double y, int icon_index, int icon_hl_index){
		MyPoint tmp = new MyPoint(x,y);
		tmp.setIconIndex(icon_index);
		
		item.add(tmp); 
		this.count=this.count+1;
		regen_coor();
		
		return item.size()-1;
	}
	
	
	public int add(double x, double y){
		item.add(new MyPoint(x,y)); 
		this.count=this.count+1;
		regen_coor();
		
		return item.size()-1;
	}
	
	
	

	public int add(double x, double y, float r, float g, float b){
		item.add(new MyPoint(x,y,r,g,b)); 
		this.count=this.count+1;
		regen_coor();
		
		return item.size()-1;
	}
	
	public int add(double x, double y, float r, float g, float b, int flag){
		item.add(new MyPoint(x,y,r,g,b,flag)); 
		this.count=this.count+1;
		regen_coor();
		
		return item.size()-1;
		
		
	}
	
	public void removeByFlag(int flag){
		int i =0;
		MyPoint tmp;
		for(i=item.size()-1;i>=0;i--){
			tmp = item.get(i);
			if(tmp.flag==flag){
				item.remove(i);
			}
			
		}
	}
	
	
	public void regen_coor(){
		int i =0;
		MyPoint tmp;
		mPositionData = new float[item.size()*3];
		mColorData = new float[item.size()*4];
		
		for(i=0;i<=item.size()-1;i++){
			tmp = item.get(i);
			double xy[] = myGLSE20app.grid.lf2xy_noscale_double(tmp.x,tmp.y);
			mPositionData[(i)*3]= 		(float) xy[0];
			mPositionData[(i)*3+1]= 	(float) xy[1];
			mPositionData[(i)*3+2]=(float) 	tmp.z;
			
			
			mColorData[i*4]=tmp.r;
			mColorData[i*4+1]=tmp.g;
			mColorData[i*4+2]=tmp.b;
			mColorData[i*4+3]=1f;
			
		}
		
		//Log.i("re","ee");
		// Initialize the buffers.
		mPositions 	= ByteBuffer.allocateDirect(item.size()*3 * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		//double mPositionData2 = Arrays.copyOfRange(mPositionData, 0, this.count*3-1);
		mPositions.put(mPositionData,0,item.size()*3 ).position(0);	
		//mPositions.put(mPositionData).position(0);	
		//mColors 	= ByteBuffer.allocateDirect(mColorData.length * mBytesPerFloat)      .order(ByteOrder.nativeOrder()).asFloatBuffer();	mCrossColors.put(mColorData).position(0);
				
		
		mColors 	= ByteBuffer.allocateDirect(item.size()*4* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mColors.put(mColorData,0,4*item.size() ).position(0);	
		
		
		
		//mCrossTextureDataHandle = MyGLTextureHelper.loadTexture(mActivityContext, R.drawable.football);
		/*Matrix.setIdentityM	(mModelMatrix, 0);
		Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
		*/
		
	}
	
	
	
	
	

	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale){
		if(drawLayer && this.item.size()>0){
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.scaleM(mModelMatrix, 0, scale,  scale,1.0f);
		//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol/scale, 2*gridSize*gridRow/scale, -3.0f);   //pazzle event
		Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
		//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol + mGLRenderer.offsetx, 2*gridSize*gridRow + mGLRenderer.offsety, -3.0f);
		
		
		GLES20.glUseProgram(mProgramHandle);
		 
		// Set program handles for cube drawing.
		
		 int pointSizeHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "pointSize");
		 GLES20.glUniform1f(pointSizeHandle, this.pointSize); 
		 
        mMVPMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
        mPositionHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle 		= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
       
        
        
		// Pass in the position information
        mPositions.position(0);		
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositions);            
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        
        mColors.position(0);	
		GLES20.glEnableVertexAttribArray(mColorHandle);     
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mColors);            
		   
		
		
		/*
        // Pass in the color information
        mCrossColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mCrossColors);        
        GLES20.glEnableVertexAttribArray(mColorHandle);
*/
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, this.count);  
		}
        
        
	}
	
}
