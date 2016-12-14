package com.geocloud.Geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyGLRawResourceReader;
import com.geocloud.MyGLES20.MyGLShaderHelper;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.Vector_Poly_Layer;
import com.geocloud.app.MyGLSE20app;
import com.geocloud.db.LineMeta;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class MyPoly extends MyGeometry{
	private ArrayList<MyPoint> vertices = new ArrayList<MyPoint>();
	
	MyGLSE20app myGLSE20app;
	Context mActivityContext;
	private int vertices_number=0;
	
	
	//private int mMVPMatrixHandle;			/** Used for debug logs. */
	//private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */
	//private int mPositionHandle;			/** This will be used to pass in model position information. */
	//private int mColorHandle;				/** This will be used to pass in model color information. */

	private float[] mModelMatrix = new float[16];	
	private float[] mMVPMatrix = new float[16];
	
	//float size = 0.5f;
	private  		float[] 	mPositionData = new float[300*3];
	private  		float[] 	mPositionData2;
	private	float[] mColorData;/* = {
			 1, 0, 0, 1, 
				0, 1, 0, 1, 
				0, 0, 1, 1 
			};*/

	
	private int mProgramHandle;
	private  FloatBuffer mPositions;
	private  FloatBuffer mColors;
	private final int mBytesPerFloat = 4;	
	private final int mPositionDataSize = 3;	
	private final int mColorDataSize = 4;	
	
	public MyBbox bbox;
	
	public MyParams params;
	public double cx=0;
	public double cy=0;
	public int flag = -1;
	public float r,g,b;
	
	public float tempr,tempg,tempb;
	
	public long _id;
	
	
	
	
	public LineMeta meta;
	
	
	
	
	
	
	
	
	
	
	
	
	public MyPoly(MyGLSE20app myGLSE20app,Context mActivityContext,MyParams params, float r, float g, float b)																	{	
		meta = new LineMeta();
		
		this.type=2;
		this.r=r;this.g=g;this.b=b;
		this.tempr=r;this.tempg=g;this.tempb=b;
		
		
		
		
		
		this.bbox=new MyBbox();
		this.params = params;
		this.mActivityContext=mActivityContext;
		this.myGLSE20app=myGLSE20app;
		/*final String vertexShader 		=  MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoly_vertex_shader);
        final String fragmentShader 	= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.mypoly_fragment_shader);
        final int vertexShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});			
        */
		_id=-1;
	
		// mColorHandle 				= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
	       
	
	
	}
	
	
	public MyPoly(double x1, double y1, double x2, double y2)																	{	
		//kathara gia temporary, geometrikes praxeis
		this.bbox=new MyBbox();
		_id=-1;
	
	}
	
	
	public void setFlag(int flag){
		this.flag=flag;
		
	}
	
	public void setColor(float r, float g, float b){
		this.r=r;this.g=g;this.b=b;
		this.tempr=r;this.tempg=g;this.tempb=b;
		
	}
	
	public String kmlCoor(){
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		return p1.x+"," + p1.y +",0 " + p2.x+","+p2.y + ",0";
	}
	
	
	
	public String dxfCoor(){
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		double[] eg1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] eg2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		return (float) Math.round(eg1[0]*100)/100+";" + (float) Math.round(eg1[1]*100)/100 +";" +  (float) Math.round(eg2[0]*100)/100 + ";"+  (float) Math.round(eg2[1]*100)/100 + "";
	}
	
	
	
	public void setTempColor(float r, float g, float b){
		this.tempr=r;this.tempg=g;this.tempb=b;
	}
	
	public void restoreColor(){
		this.tempr=this.r;this.tempg=this.g;this.tempb=this.b;
	}
	
	
	
	public double getDist(){
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		
		double[] egsa1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] egsa2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		double dx,dy;
		
		 dx = (egsa2[0]-egsa1[0]);
		 dy =  (egsa2[1]-egsa1[1]);
		 
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	public double getAzimuth(){
		
		double dx,dy;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		
		double[] egsa1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] egsa2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		
		
		 dx = (egsa2[0]-egsa1[0]);
		 dy =  (egsa2[1]-egsa1[1]);
		if(dy==0){
			if(dx>=0){
				return 100d;
			}else{
				return 300d;
			}
		}else{
			 double logos = dx/dy;
			
			 double atan = Math.abs(Math.atan(logos));
			 
			if(dx>0){
				if(dy>0){
					atan=atan;
				}else{
					atan = (float) (Math.PI-atan);
				}
			}else{
				if(dy>0){
					atan = (float) (2*Math.PI-atan);
				}else{
					atan = (float) (Math.PI+atan);
				}
			}
			
			//azimuth2stasi_0 = (float) (atan*200/Math.PI);
			//azimuth2_0 = azimuth2stasi_0-angle;
			
			return atan*200/Math.PI;
		}
	}
	
	
public double get_a(){
		
		double dx,dy;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		
		double[] egsa1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] egsa2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		
		
		 dx = (egsa2[0]-egsa1[0]);
		 dy =  (egsa2[1]-egsa1[1]);
		 
		if(dx==0){
			return (Double) null;
		}else{
			 double logos = dy/dx;		
			return logos;
		}
	}

	public void extend(double dist){
		
		double dist0 = getDist();
		//double az = getAzimuth();
		
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		 
		double[] egsa1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] egsa2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		double dx,dy;
		dx = (egsa2[0]-egsa1[0]);
		 dy =  (egsa2[1]-egsa1[1]);
		 
		if(dist>0){
			egsa2[0] = egsa1[0] + (dist0+dist)/dist0*dx;
			egsa2[1] = egsa1[1] + (dist0+dist)/dist0*dy;
		}else{
			egsa1[0] = egsa2[0] - (dist0-dist)/dist0*dx;
			egsa1[1] = egsa2[1] - (dist0-dist)/dist0*dy;
		}
		
		double[] fl1 = params.wms.proj.Egsa2fl84(egsa1[0], egsa1[1]);
		double[] fl2 = params.wms.proj.Egsa2fl84(egsa2[0], egsa2[1]);
		
		vertices.get(0).x=fl1[1];
		vertices.get(0).y=fl1[0];
		vertices.get(1).x=fl2[1];
		vertices.get(1).y=fl2[0];
		
		
		bbox.add(fl1[1], fl1[0]);
		bbox.add(fl2[1], fl2[0]);
		}
	
	
	
public void offset(double dist){
		
		double dist0 = getDist();
		double az = getAzimuth();
		//double az = getAzimuth();
		
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		 
		double[] egsa1 = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double[] egsa2 = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		double dx,dy;
		dx = (egsa2[0]-egsa1[0]);
		dy =  (egsa2[1]-egsa1[1]);
		 
		 double xt1 = 0, yt1 = 0,xt2 = 0, yt2 = 0;
		if(dx==0){
			
		}else{
			xt1 = egsa1[0]+dist*Math.sin((az+100)*Math.PI/200);
			yt1 = egsa1[1]+dist*Math.cos((az+100)*Math.PI/200);
			
			xt2 = egsa2[0]+dist*Math.sin((az+100)*Math.PI/200);
			yt2 = egsa2[1]+dist*Math.cos((az+100)*Math.PI/200);
			
			
		}
		
		
		
		double[] fl1 = params.wms.proj.Egsa2fl84(xt1, yt1);
		double[] fl2 = params.wms.proj.Egsa2fl84(xt2, yt2);
		
		vertices.get(0).x=fl1[1];
		vertices.get(0).y=fl1[0];
		vertices.get(1).x=fl2[1];
		vertices.get(1).y=fl2[0];
		
		
		//edo kanonika to bbox prepei n ipologistei olo apo tin arxi
		bbox.add(fl1[1], fl1[0]);
		bbox.add(fl2[1], fl2[0]);
		
		
	}
	

public MyPoint projectionOf(double x, double y){
	MyPoint out = null;
	
	
	MyPoint p1 = vertices.get(0);
	MyPoint p2 = vertices.get(1);
	
	 
	double[] egsa 	= params.wms.proj.fl2EGSA87(y, x);
	double[] egsa1 	= params.wms.proj.fl2EGSA87(p1.y, p1.x);
	double[] egsa2 	= params.wms.proj.fl2EGSA87(p2.y, p2.x);
	double dx,dy;
	dx = (egsa2[0]-egsa1[0]);
	dy =  (egsa2[1]-egsa1[1]);
	
	if(dx==0){
		double xt = egsa1[0];
		double yt = egsa[1];
		double[] fl 	= params.wms.proj.Egsa2fl84(xt, yt);
		out = new MyPoint(fl[1],fl[0]);
	}else if(dy==0){
		double xt = egsa[0];
		double yt = egsa1[1];
		double[] fl 	= params.wms.proj.Egsa2fl84(xt, yt);
		out = new MyPoint(fl[1],fl[0]);
	}else{
		double a = get_a();
		double b = (params.util.get_b(egsa1[0], egsa1[1], a));
		
		double at = -1/a;
		double bt = (params.util.get_b(egsa[0], egsa[1], at));
		
		double xt = (bt-b)/(a-at);
		double yt = at*xt+bt;
		double[] fl 	= params.wms.proj.Egsa2fl84(xt, yt);
		out = new MyPoint(fl[1],fl[0]);
	}
	
	return out;
}

	public void splitToNodes(Vector_Poly_Layer lay){
		
		int i =0;
		for(i=0;i<=lay.item.size()-1;i++){
			MyPoly tmp = lay.item.get(i);
			
		}
		
		
	}


	

	public MyPoint getVertice(long index){
		return vertices.get((int) index);
	}
	
	
	//public void setVertice(long index, MyPoint po){
		//vertices.set((int) index, po);
	//}
	
	public MyPoly clone(){
		MyPoly out = null;
		out = new MyPoly(myGLSE20app,mActivityContext,params,255f,0f,255f);
		
		//out = params.renderer.my_poly.add(255,0,255);
		//out.addVertice(vertices.get(0).x, vertices.get(0).y);
		//out.addVertice(vertices.get(1).x, vertices.get(1).y);	
		return out;	
	}
	public void regen_coor(){
		int j=0;

		//float [] tmp;
		MyPoint tmp;
		double [] tmp2;
		int counter=0;
		vertices_number=0;
		tmp2 = getCoorArray();
		mPositionData2 = new float[tmp2.length*3];
		mColorData = new float[tmp2.length*4];
		//mColorData = new float[4];
		//mColorData[0]=1.0f;mColorData[1]=0.0f;mColorData[2]=1.0f;mColorData[3]=1.0f;
		
		double sumx=0,sumy=0;
		//String tmpout = "";
		for(j=0;j<=tmp2.length-1;j=j+3){
			sumx+=tmp2[j];
			sumy+=tmp2[j+1];
			//Log.i("xy",(float)tmp2[j] +"," + (float)tmp2[j+1]);
			//Log.i("xy",tmp2[j] +"," + tmp2[j+1]);
			float xy[] = myGLSE20app.grid.lf2xy_noscale(tmp2[j],tmp2[j+1]);
			//double xy[] = myGLSE20app.grid.lf2xy_noscale_double(tmp2[j],tmp2[j+1]);
			
			mPositionData2[counter]= 		(float) ( xy[0]);
			mPositionData2[counter+1]= 		(float) ( xy[1]);
			mPositionData2[counter+2]= 		(float) tmp2[j+2];
			
			
			mColorData[vertices_number*4]= 	tempr;
			mColorData[vertices_number*4+1]= 	tempg;
			mColorData[vertices_number*4+2]= 	tempb;
			mColorData[vertices_number*4+3]= 	1f;
			
			
			counter = counter+3;
			vertices_number+=1;
		}
		
		cx=sumx/vertices_number;
		cy=sumy/vertices_number;
		
		/*
		for(j=0;j<=vertices.size()-1;j=j+3){
				tmp = vertices.get(j);
				float xy[] = myGLSE20app.grid.lf2xy_noscale((float)tmp.x,(float)tmp.y);
				
				mPositionData[counter]= 		xy[0];
				mPositionData[counter+1]= 		xy[1];
				mPositionData[counter+2]= 		0;
				counter = counter+3;
				vertices_number+=1;
			}*/
		
		//Log.i("draw",tmpout);
		// Initialize the buffers.
		mPositions 	= ByteBuffer.allocateDirect(3*vertices_number* mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPositions.put(mPositionData2,0,3*vertices_number ).position(0);	
		
		mColors 	= ByteBuffer.allocateDirect(4* mBytesPerFloat*vertices_number).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mColors.put(mColorData,0,4*vertices_number ).position(0);	
		
		
	}
	

	public void draw(float[] mViewMatrix,float[] mProjectionMatrix,float scale,
			
			int mMVPMatrixHandle,
			int mMVMatrixHandle,
			int mPositionHandle,
			int mColorHandle
			
			){
		//regen_coor();
		//Log.i("re2",mPositionData[0] + "," +mPositionData[1] +","+mPositionData[2]);
		GLES20.glFlush();
		try {
			Matrix.setIdentityM(mModelMatrix, 0);
			Matrix.scaleM(mModelMatrix, 0, scale,  scale,1.0f);
			//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol/scale, 2*gridSize*gridRow/scale, -3.0f);   //pazzle event
			Matrix.translateM	(mModelMatrix, 0, 0.0f,0.0f, -2.0f);
			//Matrix.translateM(mModelMatrix, 0, 2*gridSize*gridCol + mGLRenderer.offsetx, 2*gridSize*gridRow + mGLRenderer.offsety, -3.0f);
			
			//GLES20.glUseProgram(mProgramHandle);
			// Set program handles for cube drawing.
      // mMVPMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
      // mMVMatrixHandle 	= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
			//mPositionHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
      
			
			
			mColors.position(0);	
			//mColorHandle 		= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
			GLES20.glEnableVertexAttribArray(mColorHandle); 
			GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mColors);            
			//GLES20.glEnableVertexAttribArray(mColorHandle); 
			
			
			
			
			
			
			
			// Pass in the position information
			mPositions.position(0);		
			GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mPositions);            
			GLES20.glEnableVertexAttribArray(mPositionHandle);        
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
			//GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices);             
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices_number);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}             
        
        
	}
	
	
	/*
	
	
	public void draw_2(float[] mViewMatrix,float[] mProjectionMatrix,float scale){
		regen_coor();
		//Log.i("re2",mPositionData[0] + "," +mPositionData[1] +","+mPositionData[2]);
		
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
       // mCrossColors.position(0);
       // GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,0, mCrossColors);        
      //  GLES20.glEnableVertexAttribArray(mColorHandle);

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
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertices_number);             
        
        
	}
	*/
	
	
	public int size(){		return vertices.size();	}
	
	public double length(){
		return 1.0;
	}
	
	public void addVertice(double x, double y)			{vertices_number+=1;		vertices.add(new MyPoint(x,y));		bbox.add(x, y);;/*regen_coor();*/}
	public void addVertice(float x, float y)			{vertices_number+=1;		vertices.add(new MyPoint(x,y));		bbox.add(x, y);	/*regen_coor();*/}
	public void addVertice(double x, double y, double z){vertices_number+=1;		vertices.add(new MyPoint(x,y,z));		bbox.add(x, y);/*regen_coor();*/}
	
	
	
	public double[] getCoorArray(){
		double[] out = new double[this.size()*3];
		int i =0;
		MyPoint tmp;
		for(i=0;i<=this.size()-1;i++){
			tmp = this.vertices.get(i);
			//Log.i("xy3",tmp.x +"," + tmp.y);
			//Log.i("xy3f",tmp.x +"," +  tmp.x+  " ---" + 0.1234567891f);
			
			out[i*3]= tmp.x;
			out[i*3+1]= tmp.y;
			out[i*3+2]= tmp.z;
		}
		return out;
	}

	
	
/*
	
	public double[] getCoorArrayForLineDraw(){
		double[] out = new double[this.size()*3];
		int i =0;
		MyPoint tmp;
		
		//double prevx, prevy, prevz;
		
		tmp = this.vertices.get(0);		out[0]= tmp.x;		out[1]= tmp.y;		out[2]= tmp.z;
		
		
		for(i=1;i<=this.size()-2;i++){
			tmp = this.vertices.get(i);
			//Log.i("xy3",tmp.x +"," + tmp.y);
			//Log.i("xy3f",tmp.x +"," +  tmp.x+  " ---" + 0.1234567891f);
			
			out[i*3]= tmp.x;
			out[i*3+1]= tmp.y;
			out[i*3+2]= tmp.z;
			
			out[i*3+3]= tmp.x;
			out[i*3+1+3]= tmp.y;
			out[i*3+2+3]= tmp.z;
			
		}
		
		tmp = this.vertices.get(this.size()-1);		out[0]= tmp.x;		out[1]= tmp.y;		out[2]= tmp.z;
		
		return out;
	}*/
	
	
	
	public double pointToLinePerDist(double x,double y){
		double out=-1d;
		MyPoint p1 = vertices.get(0);
		MyPoint p2 = vertices.get(1);
		
		double xy0[] = params.wms.proj.fl2EGSA87(y, x);
		double xy1[] = params.wms.proj.fl2EGSA87(p1.y, p1.x);
		double xy2[] = params.wms.proj.fl2EGSA87(p2.y, p2.x);
		
		//Log.i("xy" , xy0[0] + "," + xy0[1]);
		//Log.i("xy" , xy1[0] + "," + xy1[1]);
		//Log.i("xy" , xy2[0] + "," + xy2[1]);
		
		
		double dx = xy2[0]-xy1[0];
		double dy = xy2[1]-xy1[1];
		double a =0,b=0;
		double at =0,bt=0;
		double xt,yt;
		
		if(dx==0){
			out = Math.abs(xy0[0]-xy1[0]);
		}else if(dy==0){
			out = Math.abs(xy0[1]-xy1[1]);
		}else{
			a=dy/dx;
			at=-1/a;
			b=xy1[1]-a*xy1[0];
			bt=xy0[1]-at*xy0[0];
			xt=(b-bt)/(at-a);
			yt=xt*at+bt;
			
			dx=xt-xy0[0];
			dy=yt-xy0[1];
			Log.i("xtyt" ,xt + "," +yt);
			out = Math.sqrt(dy*dy+dx*dx);
		}
		
		//Log.i("dd" ,dx + "," +dy);
		
		//Log.i("out" , ": " + out);
		//boolean out =false;
		//out = bbox.containsPoint(x, y);
		//if(out) 	bbox.log("point : " + x + "," + y + "  in bbox : ");
		return out;
	}
	
	
	
	
	
	
	public boolean pointInBbox(double x,double y){
		boolean out =false;
		out = bbox.containsPoint(x, y);
		//if(out) 	bbox.log("point : " + x + "," + y + "  in bbox : ");
		return out;
	}
	
	
	public boolean pointInBboxWithOffset(double x,double y,double offset){
		boolean out =false;
	
		out = bbox.containsPointInOffset(x, y,offset);
		//if(out) 	bbox.log("point : " + x + "," + y + "  in bbox : ");
		return out;
	}
	
	public String toString(){
		return "";//start.toString() + " - " + end.toString();
	}
	
	public void log(){
		Log.i("Poly Log",toString());
	}
	
	
	
	public void updateMeta(){
		params.app.dbHelper.updateLineMeta(meta,_id);
	}
}
