package com.geocloud.MyGLES20;




	import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

	import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.geocloud.Geometry.MyBbox;
import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;

	public class Vector_Poly_Layer {
		public ArrayList<MyPoly> item = new ArrayList<MyPoly>();
		public MyPoly lastPoly = null;
		//public MyPoly lastitem ;
		
		public String name;
		private int count=0;
		private int vertices=0;
		
		public float cx=0;
		public float cy=0;
		
		
		public boolean drawLayer=true;
		public MyBbox bbox;
		
		ArrayList<Integer> verticesArray = new ArrayList<Integer>();
		Context 	mActivityContext;
		MyGLSE20app myGLSE20app;
		MyParams params;
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
		/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
		 *  we multiply this by our transformation matrices. */
		private  		float[] 	mPositionData = new float[300*3];
		ArrayList<float[]> mPositionDataArray = new ArrayList<float[]>();
		
		private  		float[] 	mPositionData2;
		private  		float[] 	mColorData;
		private  		float[] 	mPackData;
				
		
		
			
		/** This is a handle to our light point program. */
		private int mProgramHandle;
		

		
		/** Store our model data in a float buffer. */
		private  FloatBuffer mPositions;
		ArrayList<FloatBuffer> mPositionsArray = new ArrayList<FloatBuffer>();
		
		
		private  FloatBuffer mColors;
		/** How many bytes per float. */
		private final int mBytesPerFloat = 4;	
		
		/** Size of the position data in elements. */
		private final int mPositionDataSize = 3;	
		
		/** Size of the color data in elements. */
		private final int mColorDataSize = 4;	
		
		
		
		//private  FloatBuffer mPacks;	
		//private final int mPackDataSize = 7;	
		
		
		
		
		
		public Vector_Poly_Layer(MyGLSE20app myGLSE20app,Context mActivityContext,String name,MyParams params){
			this.mActivityContext=mActivityContext;
			this.myGLSE20app=myGLSE20app;
			this.params=params;
			this.name=name;
			this.count=0;
			this.bbox=new MyBbox();
			final String vertexShader 		=  MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoly_vertex_shader);
	        final String fragmentShader 	= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoly_fragment_shader);
	        final int vertexShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
			final int fragmentShaderHandle = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
			
			mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
					new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
	        
	       
			
		}
		
		public MyPoly add(float r, float g, float b){
			item.add(new MyPoly(this.myGLSE20app,this.mActivityContext,params,r,g,b)); 
			 lastPoly = item.get(item.size()-1);
			this.count=this.count+1;
			
			//lastitem = item.get(this.count-1);
			
			return item.get(item.size()-1);
			//regen_coor();
			
			
		}
		
		
		
		
		public MyPoly add(MyPoly inn){
			item.add(inn); 
			lastPoly = item.get(item.size()-1);
			this.count=this.count+1;
			
			//lastitem = item.get(this.count-1);
			
			return item.get(item.size()-1);
			//regen_coor();
			
			
		}
		
		
		
		public MyPoly add(float r, float g, float b, int flag){
			item.add(new MyPoly(this.myGLSE20app,this.mActivityContext,params,r,g,b)); 
			this.count=this.count+1;
			 lastPoly = item.get(item.size()-1);
			 lastPoly.setFlag(flag);
			//lastitem = item.get(this.count-1);
			
			return item.get(item.size()-1);
			//regen_coor();
			
			
		}
		
		
		public void clear(){
			item.clear();
			regen_coor();
			this.count=0;
			
		
		}
		
		
		public void regen_coor(int itemIndex){
			int i =0,j=0;			MyPoly 	tmp;			double [] tmp2;
			float sumx=0;			float sumy=0;
			bbox = new MyBbox();
			
			
			
			int counter=0;
			for(i=0;i<=item.size()-1;i++){	
				
				counter+=item.get(i).size();		
			}
			
			//mPositionData 	= new float[ (counter*3)];			mColorData 	= new float[ (counter*4)];
			int counter2 =0;			int counter3 = 0;
			for(i=0;i<=item.size()-1;i++){
				tmp =  item.get(i);
				if(i==0){		bbox.add(tmp.bbox.left,tmp.bbox.top);	
				}else{				bbox.addX(tmp.bbox.left);					bbox.addY(tmp.bbox.bottom);				}
				 				
				bbox.addX(tmp.bbox.right);			bbox.addY(tmp.bbox.top);
				
				tmp2 =  item.get(i).getCoorArray();
				
				for(j=0;j<=tmp2.length-1;j=j+3){
					sumx+=tmp2[j];
					sumy+=tmp2[j+1];
					
					if(i==itemIndex){
						float xy[] = myGLSE20app.grid.lf2xy_noscale(tmp2[j],tmp2[j+1]);
						mPositionData[ counter2]=xy[0];;
						mPositionData[ counter2+1]=xy[1];;
						mPositionData[ counter2+2]=(float) tmp2[j+2];;
						
						mColorData[counter3*4]=tmp.tempr;
						mColorData[counter3*4+1]=tmp.tempg;
						mColorData[counter3*4+2]=tmp.tempb;
						mColorData[counter3*4+3]=1f;
					}
					counter2+=3;		counter3+=1;															
				}
				
			}
			
			cx=sumx/counter;		cy=sumy/counter;
			this.vertices = counter;
			
			// Initialize the buffers.
			mPositions 	= ByteBuffer.allocateDirect(counter*3* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mPositions.put(mPositionData,0,3*counter ).position(0);	
			mColors 	= ByteBuffer.allocateDirect(counter*4* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mColors.put(mColorData,0,4*counter ).position(0);	
			
		}
		
		
		
		
		public void regen_coor(){
			int i =0,j=0;			MyPoly 	tmp;			double [] tmp2;
			float sumx=0;			float sumy=0;
			bbox = new MyBbox();
			
			
			int counter=0;
			for(i=0;i<=item.size()-1;i++){
				counter+=item.get(i).size();
			}
			mPositionData 	= new float[ (counter*3)];
			mColorData 	= new float[ (counter*4)];
			//mPackData 	= new float[ (counter*7)];
			int counter2 =0;
			int counter3 = 0;
			for(i=0;i<=item.size()-1;i++){
				tmp =  item.get(i);
				if(i==0){
					bbox.add(tmp.bbox.left,tmp.bbox.top);
					
				}else{
					bbox.addX(tmp.bbox.left);
					bbox.addY(tmp.bbox.bottom);
				}
				 
				
				bbox.addX(tmp.bbox.right);
				bbox.addY(tmp.bbox.top);
				
				
				tmp2 =  item.get(i).getCoorArray();
				
				for(j=0;j<=tmp2.length-1;j=j+3){
					sumx+=tmp2[j];
					sumy+=tmp2[j+1];
					
					float xy[] = myGLSE20app.grid.lf2xy_noscale(tmp2[j],tmp2[j+1]);
					mPositionData[ counter2]=xy[0];;
					mPositionData[ counter2+1]=xy[1];;
					mPositionData[ counter2+2]=(float) tmp2[j+2];;
					
					mColorData[counter3*4]=tmp.tempr;
					mColorData[counter3*4+1]=tmp.tempg;
					mColorData[counter3*4+2]=tmp.tempb;
					mColorData[counter3*4+3]=1f;
					
					/*
					mPackData[counter3*7+0]=xy[0];
					mPackData[counter3*7+1]=xy[1];
					mPackData[counter3*7+2]=(float) tmp2[j+2];;
					mPackData[counter3*7+3]=tmp.r;
					mPackData[counter3*7+4]=tmp.g;
					mPackData[counter3*7+5]=tmp.b;
					mPackData[counter3*7+6]=1.f;
					
					
					*/
					counter2+=3;
					counter3+=1;
					
					
					
				}
				
			}
			
			cx=sumx/counter;
			cy=sumy/counter;
			
			
			this.vertices = counter;
			
			// Initialize the buffers.
			mPositions 	= ByteBuffer.allocateDirect(counter*3* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mPositions.put(mPositionData,0,3*counter ).position(0);	
			
			mColors 	= ByteBuffer.allocateDirect(counter*4* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mColors.put(mColorData,0,4*counter ).position(0);	
			
			//mPacks 	= ByteBuffer.allocateDirect(counter*7* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			//mPacks.put(mPackData,0,7*counter ).position(0);	
		}
		
		
		
		
		public void regen_coor_silent(){
			//regen_coor xoris epanadimiourgia pinaka
			int i =0,j=0;			MyPoly 	tmp;			double [] tmp2;
			float sumx=0;			float sumy=0;
			bbox = new MyBbox();
			
			
			int counter=0;
			for(i=0;i<=item.size()-1;i++){
				counter+=item.get(i).size();
			}
			//mPositionData 	= new float[ (counter*3)];
			//mColorData 	= new float[ (counter*4)];
			//mPackData 	= new float[ (counter*7)];
			int counter2 =0;
			int counter3 = 0;
			for(i=0;i<=item.size()-1;i++){
				tmp =  item.get(i);
				if(i==0){
					bbox.add(tmp.bbox.left,tmp.bbox.top);
					
				}else{
					bbox.addX(tmp.bbox.left);
					bbox.addY(tmp.bbox.bottom);
				}
				 
				
				bbox.addX(tmp.bbox.right);
				bbox.addY(tmp.bbox.top);
				
				
				tmp2 =  item.get(i).getCoorArray();
				
				for(j=0;j<=tmp2.length-1;j=j+3){
					sumx+=tmp2[j];
					sumy+=tmp2[j+1];
					
					float xy[] = myGLSE20app.grid.lf2xy_noscale(tmp2[j],tmp2[j+1]);
					mPositionData[ counter2]=xy[0];;
					mPositionData[ counter2+1]=xy[1];;
					mPositionData[ counter2+2]=(float) tmp2[j+2];;
					
					mColorData[counter3*4]=tmp.tempr;
					mColorData[counter3*4+1]=tmp.tempg;
					mColorData[counter3*4+2]=tmp.tempb;
					mColorData[counter3*4+3]=1f;
					
					/*
					mPackData[counter3*7+0]=xy[0];
					mPackData[counter3*7+1]=xy[1];
					mPackData[counter3*7+2]=(float) tmp2[j+2];;
					mPackData[counter3*7+3]=tmp.r;
					mPackData[counter3*7+4]=tmp.g;
					mPackData[counter3*7+5]=tmp.b;
					mPackData[counter3*7+6]=1.f;
					
					
					*/
					counter2+=3;
					counter3+=1;
					
					
					
				}
				
			}
			
			cx=sumx/counter;
			cy=sumy/counter;
			
			
			this.vertices = counter;
			
			// Initialize the buffers.
			mPositions 	= ByteBuffer.allocateDirect(counter*3* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mPositions.put(mPositionData,0,3*counter ).position(0);	
			
			mColors 	= ByteBuffer.allocateDirect(counter*4* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mColors.put(mColorData,0,4*counter ).position(0);	
			
			//mPacks 	= ByteBuffer.allocateDirect(counter*7* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			//mPacks.put(mPackData,0,7*counter ).position(0);	
		}
		
		
		
		
		public void reset_color(){
			int i =0,j=0;			MyPoly 	tmp;			double [] tmp2;
			int counter3 = 0;
			
			int counter=0;
			
			for(i=0;i<=item.size()-1;i++){
				counter+=item.get(i).size();
			}
		//	mColorData 	= new float[ (counter*4)];
			
			for(i=0;i<=item.size()-1;i++){
				tmp2 =  item.get(i).getCoorArray();
				tmp =  item.get(i);	
				tmp.restoreColor();
				for(j=0;j<=tmp2.length-1;j=j+3){
					mColorData[counter3*4]=tmp.tempr;
					mColorData[counter3*4+1]=tmp.tempg;
					mColorData[counter3*4+2]=tmp.tempb;
					mColorData[counter3*4+3]=1f;
					counter3+=1;
				}
				
			}
					
			mColors 	= ByteBuffer.allocateDirect(counter*4* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			mColors.put(mColorData,0,4*counter ).position(0);	
		}
		

		public void regen_coor_old(){
			//mPositionDataArray.clear();
			//mPositionsArray.clear();
			//verticesArray.clear();
			int i =0,j=0;
			MyPoly 	tmp;
			double [] tmp2;
			//float[] mPositionDatatmp;
			
			//int counter=0,countertmp=0;;
			//vertices=0;
			float sumx=0;
			float sumy=0;
			bbox = new MyBbox();
			
			
			
			for(i=0;i<=item.size()-1;i++){
				
				//tmp 			= item.get(i).getCoorArray();
				tmp =  item.get(i);
				tmp.regen_coor();
				sumx+=tmp.cx;
				sumy+=tmp.cy;
				if(i==0){
					bbox.add(tmp.bbox.left,tmp.bbox.top);
					
				}else{
					bbox.addX(tmp.bbox.left);
					bbox.addY(tmp.bbox.bottom);
				}
				 
				
				bbox.addX(tmp.bbox.right);
				bbox.addY(tmp.bbox.top);
				
				//mPositionDatatmp 	= new float[tmp.length*3];
				//countertmp=0;
				//for(j=0;j<=tmp.length-2;j=j+3){
					//float xy[] = myGLSE20app.grid.lf2xy_noscale((float)tmp[j],(float)tmp[j+1]);
					
					//Log.i(i + " ->",xy[0] + "," + xy[1]);
					/*
					mPositionDatatmp[countertmp]= 			xy[0];
					mPositionDatatmp[countertmp+1]= 		xy[1];
					mPositionDatatmp[countertmp+2]= 		tmp[j+2];
					mPositionDataArray.add(mPositionDatatmp);
					countertmp = countertmp+3;
					*/
					//mPositionData[counter]= 		xy[0];
					//mPositionData[counter+1]= 		xy[1];
					//mPositionData[counter+2]= 		tmp[j+2];
					//counter = counter+3;
					
					//vertices+=1;
				//}
				//Log.i(i + " ->",mPositionDatatmp[countertmp] + "," + mPositionDatatmp[countertmp]);
				
				/*
				verticesArray.add(tmp.length-1);
				
				mPositionsArray.add(
						(FloatBuffer) ByteBuffer.allocateDirect(countertmp* mBytesPerFloat)
									.order(ByteOrder.nativeOrder())
									.asFloatBuffer()
									.put(mPositionDataArray.get(i),0,countertmp )
									.position(0)	
						);*/
			}
			
			cx = sumx/item.size();
			cy = sumy/item.size();
			
			
			
			
			
			
			
			
			
			// Initialize the buffers.
		//	mPositions 	= ByteBuffer.allocateDirect(this.count*3*vertices* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
			//double mPositionData2 = Arrays.copyOfRange(mPositionData, 0, this.count*3-1);
			//mPositions.put(mPositionData,0,this.count*3*vertices ).position(0);	
			//mPositions.put(mPositionData).position(0);	
			//mColors 	= ByteBuffer.allocateDirect(mColorData.length * mBytesPerFloat)      .order(ByteOrder.nativeOrder()).asFloatBuffer();	mCrossColors.put(mColorData).position(0);
								
			//mCrossTextureDataHandle = MyGLTextureHelper.loadTexture(mActivityContext, R.drawable.football);
			/*Matrix.setIdentityM	(mModelMatrix, 0);
			Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
			*/
			
			//bbox.log("Poly Regen)");
			
			
			
		}
		
		
		

		public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale){

			if(drawLayer){
			GLES20.glFlush();
			GLES20.glUseProgram(mProgramHandle);
			mMVPMatrixHandle 	= GLES20.glGetUniformLocation	(mProgramHandle, "u_MVPMatrix");
	        mMVMatrixHandle 	= GLES20.glGetUniformLocation	(mProgramHandle, "u_MVMatrix"); 
	        mPositionHandle 	= GLES20.glGetAttribLocation	(mProgramHandle, "a_Position");
	        mColorHandle 		= GLES20.glGetAttribLocation	(mProgramHandle, "a_Color");
		       
	        
	        
	        try {
				Matrix.setIdentityM(mModelMatrix, 0);
				Matrix.scaleM(mModelMatrix, 0, scale,  scale,1.0f);
				Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
				
				
				
				// Pass in the position information
				mPositions.position(0);		
				GLES20.glEnableVertexAttribArray(mPositionHandle);     
				GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositions);            
				   
				
				mColors.position(0);	
				GLES20.glEnableVertexAttribArray(mColorHandle);     
				GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mColors);            
				   
				
				
				
				
				//mPacks.position(0);	
				//GLES20.glEnableVertexAttribArray(mPositionHandle);     
				//GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,7*4, mPacks);            
				   
				//mPacks.position(3);	
				//GLES20.glEnableVertexAttribArray(mColorHandle);     
				//GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,7, mPacks);
				
				
				
				
				Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
				GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
				Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
				GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
				//GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, this.vertices);
				
				
				int i;
				int tmp;
				int sum=0;
				for(i=0;i<=item.size()-1;i++){
					tmp = item.get(i).size();
					
					GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, sum, tmp);
					sum+= tmp;
				}
				
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	        
	        // Draw the cube.
	        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices);             
	        //GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices);      */  
			}
	        
	        
		}
		
		
		
		
		
		
		

		public void drawold(float[] mViewMatrix,float[] mProjectionMatrix,float scale){
			//Log.i("draw", "");
			 //Log.i("draw","renderer : " + item.size());
			MyPoly tmp;
			int i;
			
			//Log.i("re1",mPositionData[0] + "," +mPositionData[1] +","+mPositionData[2]);
			//Log.i("re",mPositionData[1] + "");
			//Log.i("re",mPositionData[2] + "");
			
			
			
			
			//Matrix.setIdentityM(mModelMatrix, 0);
			//Matrix.scaleM(mModelMatrix, 0, scale,  scale,1.0f);
			//Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
			GLES20.glUseProgram(mProgramHandle);
			mMVPMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	        mMVMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	        mPositionHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	        mColorHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
		       
	        
	        
	        for(i=0;i<=item.size()-1;i++){
				item.get(i).draw(mViewMatrix, mProjectionMatrix, scale,mMVPMatrixHandle,mMVMatrixHandle,mPositionHandle,mColorHandle);
				//tmp.draw(mViewMatrix, mProjectionMatrix, scale);
			}
	        
	        /*
	        
			// Pass in the position information
	        mPositions.position(0);		
			GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositions);            
	        GLES20.glEnableVertexAttribArray(mPositionHandle);        
	        
	       Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
	       GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	        
	        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	        // (which now contains model * view * projection).
	        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

	        // Pass in the combined matrix.
	        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	        
	        // Pass in the light position in eye space.        
	        //GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
	        
	        // Draw the cube.
	        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices);             
	        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices);          
	        */
	        
	       // for(i=0;i<=item.size()-1;i++){
				//item.get(i).draw(mViewMatrix, mProjectionMatrix, scale);
				//tmp.draw(mViewMatrix, mProjectionMatrix, scale);
		//	}
	        
	        /*
	        for(i=0;i<=item.size()-1;i++){
	        		mPositionsArray.get(i).position(0);		
	 				GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositionsArray.get(i));            
	 				GLES20.glEnableVertexAttribArray(mPositionHandle);  
	 				GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, verticesArray.get(i));
			}
	        */
	        
	        
	        
	        
	        /*
	    	
			Matrix.setIdentityM(mModelMatrix, 0);
			Matrix.scaleM(mModelMatrix, 0, scale,  scale,1.0f);
			//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol/scale, 2*gridSize*gridRow/scale, -3.0f);   //pazzle event
			Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
			//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol + mGLRenderer.offsetx, 2*gridSize*gridRow + mGLRenderer.offsety, -3.0f);
			
			
			GLES20.glUseProgram(mProgramHandle);
			// Set program handles for cube drawing.
	        mMVPMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	        mMVMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	        mPositionHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	       // mColorHandle 		= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
	       
	        
	        
			// Pass in the position information
	        mPositions.position(0);		
			GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositions);            
	        GLES20.glEnableVertexAttribArray(mPositionHandle);        
	        
	        // Pass in the color information
	        //mCrossColors.position(0);
	        //GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mCrossColors);        
	       // GLES20.glEnableVertexAttribArray(mColorHandle);
	
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
	        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices);             
	        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices);      */  
	        
	        
		}
		
		
		public void remove_last(){
				//Log.i("size",item.size()+"");
			if(item.size()>0){
			item.remove(item.size()-1);
			}
			this.lastPoly=item.get(item.size()-1);
			this.count = item.size();
			//regen_coor();
			//Log.i("size",item.size()+"");
			regen_coor_silent();
		}
		
		
		public void remove_last(int flag){
			//Log.i("size",item.size()+"");
		if(item.size()>0){
			if(item.get(item.size()-1).flag==flag) item.remove(item.size()-1);
		}
		this.lastPoly=item.get(item.size()-1);
		this.count = item.size();
		//regen_coor();
		//Log.i("size",item.size()+"");
		regen_coor_silent();
	}
		
		

		public void removeByFlag(int flag){
			int i =0;
			MyPoly tmp;
			for(i=item.size()-1;i>=0;i--){
				tmp = item.get(i);
				if(tmp.flag==flag){
					item.remove(i);
				}
				
			}
		}
		

		public MyPoly clickEvent(double x, double y, int dpixel){
			MyPoly out = null;;
			int i;
			double min_dist,tmp_dist;min_dist=999999999999999999999d;
			//MyPoly min_dist_poly = null;
			long min_dist_index=-1;
			this.reset_color();
			double offset = params.tapSelectPixelWindow*params.pixel2world;
			for(i=0;i<=item.size()-1;i++){
				MyPoly tmp= item.get(i);
				boolean res = tmp.pointInBboxWithOffset(x, y,offset);
				if(res){
					tmp_dist = tmp.pointToLinePerDist(x, y);
					if(min_dist>tmp_dist){
						min_dist=tmp_dist;
						min_dist_index=i;
						//min_dist_poly=item.get(i);
					}
					
					
					
					
					
				}
			}
			if(min_dist_index>-1){
				item.get((int) min_dist_index).setTempColor(255f, 0f, 0f);
				this.regen_coor((int) min_dist_index);
				out = item.get((int) min_dist_index);
				params.debug(min_dist+"");;
			}
			
			
			return out;
		}
		
		
		
	}
