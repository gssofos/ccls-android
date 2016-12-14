package com.geocloud.MyGLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.test2.R;

public class Vector_Cross {

	

	Context 	mActivityContext;
	private int mMVPMatrixHandle;			/** Used for debug logs. */
	private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */
	//private int mLightPosHandle;			/** This will be used to pass in the light position. */
	//private int mTextureUniformHandle;		/** This will be used to pass in the texture. */
	private int mPositionHandle;			/** This will be used to pass in model position information. */
	private int mColorHandle;				/** This will be used to pass in model color information. */
	//private int mNormalHandle;				/** This will be used to pass in model normal information. */
	//private int mTextureCoordinateHandle;	/** This will be used to pass in model texture coordinate information. */
	
	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mCrossModelMatrix = new float[16];	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	
	
	/**
	
	 * 
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	
	private float[] MymViewMatrix = new float[16];

	
	
	float size = 0.05f;
	/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
	 *  we multiply this by our transformation matrices. */
	private final float[] mCrossPositionData = new float[] {
			-size, 0.0f, 0.0f,				
			size, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 
			0.0f, -size, 0.0f, 				
			0.0f, size, 0.0f,
			0.0f, 0.0f, 0.0f
																	
																};
	//private final short[]  indices = new short[] {0, 1, 2, 3}; // The order of vertexrendering. - See more at: http://androidblog.reindustries.com/a-real-opengl-es-2-0-2d-tutorial-part-2/#sthash.XGfDopEU.dpuf
	// R, G, B, A
	private		final float[]  mCrossColorData =
				{				
						// Front face (white)
						0.0f, 0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 0.0f, 1.0f	,
						0.0f, 0.0f, 0.0f, 1.0f,				
						0.0f, 0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 0.0f, 1.0f
				};
	
	
	
/*	// R, G, B, A
				final float[] mCrossData =
				{				
						// Front face (white)
						1.0f, 1.0f, 1.0f, 1.0f,				
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f,				
						1.0f, 1.0f, 1.0f, 1.0f,
						1.0f, 1.0f, 1.0f, 1.0f
					
				};*/
				
				// X, Y, Z
				// The normal is used in light calculations and is a vector which points
				// orthogonal to the plane of the surface. For a cube model, the normals
				// should be orthogonal to the points of each face.
				final float[] mCrossNormalData =
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
				final float[] mCrossTextureCoordinateData =
				{												
						// Front face
						0.0f, 0.0f, 				
						0.0f, 1.0f,
						1.0f, 0.0f,
						0.0f, 1.0f,
						1.0f, 1.0f,
						1.0f, 0.0f
				};
				
				
	
	/** Used to hold the current position of the light in world space (after transformation via model matrix). */
	//private final float[] mCrossPosInWorldSpace = new float[4];
	
	/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
	//private final float[] mCrossPosInEyeSpace = new float[4];
	
		
	/** This is a handle to our light point program. */
	private int mProgramHandle2;
	

	
	/** Store our model data in a float buffer. */
	private  FloatBuffer mCrossPositions;
	private  FloatBuffer mCrossColors;
	private  FloatBuffer mCrossNormals;
	private  FloatBuffer mCrossTextureCoordinates;
	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;	
	
	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;	
	
	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;	
	
	/** Size of the normal data in elements. */
	//private final int mNormalDataSize = 3;
	
	/** Size of the texture coordinate data in elements. */
	//private final int mTextureCoordinateDataSize = 2;
	//private int mCrossTextureDataHandle=-1;
	
	
	public Vector_Cross(Context mActivityContext){
		 
		this.mActivityContext=mActivityContext;
		final String vertexShader2 		=  MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader2);
	        final String fragmentShader2 	= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader2);
	        final int vertexShaderHandle2 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader2);		
			final int fragmentShaderHandle2 = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader2);		
			
			mProgramHandle2 = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle2, fragmentShaderHandle2, 
					new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
	        
	        
			// Initialize the buffers.
						mCrossPositions = ByteBuffer.allocateDirect(mCrossPositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();mCrossPositions.put(mCrossPositionData).position(0);	
						mCrossColors = ByteBuffer.allocateDirect(mCrossColorData.length * mBytesPerFloat)      .order(ByteOrder.nativeOrder()).asFloatBuffer();	mCrossColors.put(mCrossColorData).position(0);
						mCrossNormals = ByteBuffer.allocateDirect(mCrossNormalData.length * mBytesPerFloat)    .order(ByteOrder.nativeOrder()).asFloatBuffer();		mCrossNormals.put(mCrossNormalData).position(0);
						mCrossTextureCoordinates = ByteBuffer.allocateDirect(mCrossTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();mCrossTextureCoordinates.put(mCrossTextureCoordinateData).position(0);
						
						//mCrossTextureDataHandle = MyGLTextureHelper.loadTexture(mActivityContext, R.drawable.football);
						Matrix.setIdentityM(mCrossModelMatrix, 0);
						Matrix.translateM(mCrossModelMatrix, 0, 0.0f,0.0f, -2.0f);
	}
	
	
	public void draw(float[] mViewMatrix,float[] mProjectionMatrix){
		GLES20.glUseProgram(mProgramHandle2);
		// Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle2, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle2, "u_MVMatrix"); 
        //mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle2, "u_LightPos");
        //mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle2, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle2, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgramHandle2, "a_Color");
        //mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle2, "a_Normal"); 
        //mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle2, "a_TexCoordinate");
        
        
        
        
        
       // GLES20.glActiveTexture(GLES20.GL_TEXTURE0);	       
       //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mCrossTextureDataHandle);								// Bind the texture to this unit.
       // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.	//GLES20.glUniform1i(mTextureUniformHandle, 0); 
           


		// Pass in the position information
        mCrossPositions.position(0);		
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mCrossPositions);            
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        // Pass in the color information
        mCrossColors.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mCrossColors);        
        GLES20.glEnableVertexAttribArray(mColorHandle);
        
        // Pass in the normal information
        // mCrossNormals.position(0);
        // GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, mCrossNormals);
        // GLES20.glEnableVertexAttribArray(mNormalHandle);
        
        // Pass in the texture coordinate information
        // mCrossTextureCoordinates.position(0);
        // GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mCrossTextureCoordinates); 
        // GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
       
        
        Matrix.setLookAtM(MymViewMatrix, 0, 0.0f,0.0f, -0.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, MymViewMatrix, 0, mCrossModelMatrix, 0);   
       // Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mCrossModelMatrix, 0);   
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
        GLES20.glLineWidth(1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 6);             
        
        
	}
	
	
}
