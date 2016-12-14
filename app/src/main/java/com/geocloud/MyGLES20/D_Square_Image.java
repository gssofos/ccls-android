package com.geocloud.MyGLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class D_Square_Image {

	
	
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];
	public String filename;
	public boolean textureSet = false;
	
	public int imageIndexX;
	public int imageIndexY;
	public int gridCol;
	public int gridRow;
	public float gridSize;
	private GL02_MyGLRenderer mGLRenderer;
	public int level;
	
	/** Store our model data in a float buffer. */
	private  FloatBuffer mSquarePositions;
	private  FloatBuffer mSquareColors;
	private  FloatBuffer mSquareNormals;
	private  FloatBuffer mSquareTextureCoordinates;
	
	
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;	
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;	
	
	/** Size of the normal data in elements. */
	private final int mNormalDataSize = 3;
	
	/** Size of the texture coordinate data in elements. */
	private final int mTextureCoordinateDataSize = 2;
	
	
	private int mTextureDataHandle=-1;
	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	public  D_Square_Image(String filename, GL02_MyGLRenderer mGLRenderer){	
		this.filename=filename;	
		this.mGLRenderer=mGLRenderer;
		this.level = mGLRenderer.zoom_level;
	}
	
	public  D_Square_Image(String filename, GL02_MyGLRenderer mGLRenderer,int level){	
		this.filename=filename;	
		this.mGLRenderer=mGLRenderer;
		this.level = level;
	}
	
	public void set_coor_data(float x, float y, int level, int imIndX, int imIndY, float size){
		final float[] squarePositionData =
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
			
			// R, G, B, A
			final float[] squareColorData =
			{				
					// Front face (white)
					1.0f, 1.0f, 1.0f, 1.0f,				
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,				
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f
				
			};
			
			// X, Y, Z
			// The normal is used in light calculations and is a vector which points
			// orthogonal to the plane of the surface. For a cube model, the normals
			// should be orthogonal to the points of each face.
			final float[] squareNormalData =
			{												
					// Front face
					0.0f, 0.0f, 1.0f,				
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,				
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f
			};
			
			// S, T (or X, Y)
			// Texture coordinate data.
			// Because images have a Y axis pointing downward (values increase as you move down the image) while
			// OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
			// What's more is that the texture coordinates are the same for every face.
			final float[] squareTextureCoordinateData =
			{												
					// Front face
					0.0f, 0.0f, 				
					0.0f, 1.0f,
					1.0f, 0.0f,
					0.0f, 1.0f,
					1.0f, 1.0f,
					1.0f, 0.0f
			};
			
			// Initialize the buffers.
			mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat)
	        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
			mSquarePositions.put(squarePositionData).position(0);		
			//Log.i("deb","111111111");
			mSquareColors = ByteBuffer.allocateDirect(squareColorData.length * mBytesPerFloat)
	        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
			mSquareColors.put(squareColorData).position(0);
			
			mSquareNormals = ByteBuffer.allocateDirect(squareNormalData.length * mBytesPerFloat)
	        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
			mSquareNormals.put(squareNormalData).position(0);
			
			mSquareTextureCoordinates = ByteBuffer.allocateDirect(squareTextureCoordinateData.length * mBytesPerFloat)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mSquareTextureCoordinates.put(squareTextureCoordinateData).position(0);
			
			
			 Matrix.setIdentityM(mModelMatrix, 0);
			
			 gridCol=(int) x;
			 gridRow=(int) y;
			 gridSize=size;
			 imageIndexX = imIndX;
			 imageIndexY = imIndY;
			 
			 
			
				
			 //Matrix.translateM(mModelMatrix, 0, 0.0f, -2.0f, -7.0f);
			// Matrix.translateM(mModelMatrix, 0, 2*size*x, 2*size*y, -5.0f);
			// Matrix.translateM(mModelMatrix, 0, 2*size*x, 2*size*y, -3.0f);
				// Log.i("sqyare translate" , 2*size*x + "," +  2*size*y);
		 		
	
	}
	
	
	
	public void set_filename(String filename){
		this.filename = filename;
		Log.i("texture0squareClass","-" + this.filename + "-");
	}
	
	public void set_mTextureDataHandle(Context mActivityContext){
		//Log.i("eererr",filename);
		mTextureDataHandle = MyGLTextureHelper.loadTextureFromFile(mActivityContext, filename);
		//Log.i("eeredddddddrr",filename);
		this.textureSet=true;
	}
	
	
	public void set_data(float x, float y,String filename,Context mActivityContext,int level, int imageIndexX, int imageIndexY,float size){
		//String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();  
    	//filename = baseDir + filename;
    	set_coor_data( x,  y,level,imageIndexX,imageIndexY,size);
    	set_filename( filename);
    	set_mTextureDataHandle(mActivityContext);
	}
	
	
	
	
	public void draw(		float[] mLightPosInEyeSpace,
			float[]  mProjectionMatrix,
			float[] mViewMatrix,
							int mMVPMatrixHandle,
							int mMVMatrixHandle,
							int mLightPosHandle,
							int mTextureUniformHandle,
							int mPositionHandle,
							int mColorHandle,
							int mNormalHandle,
							int mTextureCoordinateHandle,
							float scale
							){
		//Log.i("draw",String.valueOf(this.textureSet));
					if(this.textureSet && mTextureDataHandle!=-1){
						
						
						//Matrix.setIdentityM(mModelMatrix, 0);
						//Matrix.multiplyMM(mModelMatrix, 0, mScaleMatrix, 0, mModelMatrix, 0);
						//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol, 2*gridSize*gridRow, -3.0f);
						 
						 	Matrix.setIdentityM(mModelMatrix, 0);
							Matrix.scaleM(mModelMatrix, 0, scale, scale,1.0f);
							//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol/scale, 2*gridSize*gridRow/scale, -3.0f);   //pazzle event
							Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol + mGLRenderer.offsetx, 2*gridSize*gridRow + mGLRenderer.offsety, -3.0f);
							 
						 
						//Log.i("draw","dd");
					GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			        // Bind the texture to this unit.
			        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
			        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
			        GLES20.glUniform1i(mTextureUniformHandle, 0);    
        
        
					// Pass in the position information
					mSquarePositions.position(0);		
					GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
			        		0, mSquarePositions);        
			                
			        GLES20.glEnableVertexAttribArray(mPositionHandle);        
			        // Pass in the color information
			       /* mSquareColors.position(0);
			        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
			        		0, mSquareColors);        
			        
			        GLES20.glEnableVertexAttribArray(mColorHandle);*/
			         // Pass in the normal information
			        /*
			        mSquareNormals.position(0);
			        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
			        		0, mSquareNormals);
			        
			        GLES20.glEnableVertexAttribArray(mNormalHandle);*/
			        // Pass in the texture coordinate information
			        mSquareTextureCoordinates.position(0);
			        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 
			        		0, mSquareTextureCoordinates);
			        
			        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
			       // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
			        // (which currently contains model * view).
			        //Matrix.multiplyMM(mMVPMatrix, 0, mScaleMatrix, 0, mModelMatrix, 0);
			       // Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mMVPMatrix, 0);   
			        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
				        
			        // Pass in the modelview matrix.
			        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
			        
			        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
			        // (which now contains model * view * projection).
			        //Matrix.multiplyMM(mMVPMatrix, 0, mScaleMatrix, 0, mMVPMatrix, 0);
			        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
			        
			        // Pass in the combined matrix.
			        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
			        
			        // Pass in the light position in eye space.        
			       //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
			        
			        // Draw the cube.
			        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);             
		
					}
		
		
		
	}
	
	
	
}
