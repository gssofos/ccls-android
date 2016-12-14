package com.geocloud.MyGLES20;

import java.util.ArrayList;

import com.geocloud.app.MyGLSE20app;
import com.example.test2.R;
import com.geocloud.wms.Ktima;
import com.geocloud.wms.Wms;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;


public class D_Image_Grid{
	ArrayList<D_Square_Image> item = new ArrayList<D_Square_Image>();
	//private int cols,rows;
	private Ktima 		ktima;
			Context 	mActivityContext;
	private MyGLSE20app myGLSE20app;
	
	
	
	private int mMVPMatrixHandle;			/** Used for debug logs. */
	private int mMVMatrixHandle;			/** This will be used to pass in the modelview matrix. */
	private int mLightPosHandle;			/** This will be used to pass in the light position. */
	private int mTextureUniformHandle;		/** This will be used to pass in the texture. */
	private int mPositionHandle;			/** This will be used to pass in model position information. */
	private int mColorHandle;				/** This will be used to pass in model color information. */
	private int mNormalHandle;				/** This will be used to pass in model normal information. */
	private int mTextureCoordinateHandle;	/** This will be used to pass in model texture coordinate information. */
	private int mProgramHandle;				/** This is a handle to our cube shading program. */
	
	
	
	
	
	public float originLat;
	public float originLon;
	public 	float originBima;
		
	//private int originImageIndexX;
	//private int originImageIndexY;
	
	private float curImageX;		//H thesi sto render datum opou einai to kentro tou screen
	private float curImageY;
	
	private int cols = 1;		//	number of columns to render from origin
	private int rows = 1;		//	number of rows to render from origin
	private Wms wms;
	//private Proj datum;
	public void clear(){
		item.clear();
	}
	public D_Image_Grid(MyGLSE20app myGLSE20app,Context mActivityContext,float orLon, float orLat/*, int orImX, int orImY*/, float orStep){
		originLat	=	orLat;
		originLon	=	orLon;
		//originImageIndexX=orImX;
		//originImageIndexY=orImY;
		originBima 	= 	orStep;
		this.mActivityContext 	= 	mActivityContext;
		this.myGLSE20app		=	(MyGLSE20app) myGLSE20app;
		ktima 					= 	new Ktima(this.mActivityContext);
		wms						=	this.myGLSE20app.wms;
		
		final String 	vertexShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
        final String 	fragmentShader 			= MyGLRawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
        final int 		vertexShaderHandle 		= MyGLShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int 		fragmentShaderHandle 	= MyGLShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		
		mProgramHandle = MyGLShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});	
		
	}
	
	
	public String get_drawn_tiles(){
		int count=0;
		int i;
		for(i=0;i<=this.item.size()-1;i++){
			if(this.item.get(i).textureSet){
				count+=1;
			}
		}
		return "Loaded Images : " + count + "\n" + "Level : " + myGLSE20app.mGLRenderer.zoom_level;
	}
	
	
	public void setVisibleGridCells(){
		boolean download = myGLSE20app.isOnline();
		float i , j;
		//Log.i( " : " +curImageX,curImageY + "");
		//myGLSE20app.mGLRenderer.offsetx = curImageX*(myGLSE20app.mGLRenderer.scale-1);
		//myGLSE20app.mGLRenderer.offsety = curImageY*(myGLSE20app.mGLRenderer.scale-1);
		
		//Prota sxediazo to kentriko
		float x0 = (float) Math.round(curImageX);
		float y0 = (float) Math.round(curImageY);
		//Log.i("orig",curImageX + "," + curImageY);
		//Log.i("orig",originLon+x0*originBima + "," + originLat+y0*originBima);
		if(!itemExists(x0,(float) Math.round(curImageY))){
			myGLSE20app.getImageToImageGrid(originLon+x0*originBima,originLat+y0*originBima, myGLSE20app.mGLRenderer.zoom_level,item,x0,y0,download);
		}
		
		
		
		for(i=(float) Math.round(curImageX)-cols;i<=Math.round(curImageX)+cols;i++){
			for(j=(float) Math.round(curImageY)-rows;j<=Math.round(curImageY)+rows;j++){
				if(i!=x0 || j!=y0){
					
						if(!itemExists(i,j)){
							myGLSE20app.getImageToImageGrid(originLon+i*originBima,originLat+j*originBima, myGLSE20app.mGLRenderer.zoom_level,item,i,j,download);
						}
					
				}
				
			}
		}
		//myGLSE20app.text1.setText(get_drawn_tiles());
	}
	
	
	public void removeOffGridCells(){	
		int i;
		int size = item.size();
		Log.i("size of loaded image set" , size + "");
		for(i=size-1;i>=0;i--){
			//Log.i("remove_image",item.get(i).gridCol + "-" + item.get(i).gridRow);
			if(		item.get(i).gridCol<(float) Math.round(curImageX)-cols
					|| item.get(i).gridCol>(float) Math.round(curImageX)+cols
					|| item.get(i).gridRow<(float) Math.round(curImageY)-rows
					|| item.get(i).gridRow>(float) Math.round(curImageY)+rows
					
					){
				item.remove(i);
				//Log.i("remove_image",item.get(i).gridCol + "-" + item.get(i).gridRow);
				
			}
		}
	}
	
	
	public float[] xy2lf(float x, float y){
		//x,y idio me eye form
		float out[] = new float[2];
		
		out[0] = (x/myGLSE20app.gssize/2/ myGLSE20app.mGLRenderer.scale+0.5f)*originBima/*myGLSE20app.grid.rows*/ + originLon;
		out[1] = (y/myGLSE20app.gssize/2/ myGLSE20app.mGLRenderer.scale+0.5f)*originBima/*myGLSE20app.grid.cols*/ + originLat;
        return out;
	}
	
	
	
	
	public float[] lf2xy(double l, double f){
		//x,y idio me eye form
		float out[] = new float[2] ;
		
		out[0]=(float) (((l- originLon)/originBima -0.5d)*myGLSE20app.gssize*2* myGLSE20app.mGLRenderer.scale);
		out[1]=(float) (((f- originLat)/originBima -0.5d)*myGLSE20app.gssize*2* myGLSE20app.mGLRenderer.scale);
		//out[1] = (y/myGLSE20app.gssize/2+0.5f)*originBima + originLat;
        return out;
	}
	
	public float[] lf2xy_noscale(double l, double f){
		//x,y idio me eye form
		//me scale factor = 1
		//stis geometrikes odotites px points poy thelo gia zoom =1 kai to scale einai sto scale matrix
		float out[] = new float[2] ;
		
		out[0]=(float) (((l- originLon)/originBima -0.5d)*myGLSE20app.gssize*2);
		out[1]=(float) (((f- originLat)/originBima -0.5d)*myGLSE20app.gssize*2);
		//out[1] = (y/myGLSE20app.gssize/2+0.5f)*originBima + originLat;
        return out;
	}
	
	public double[] lf2xy_noscale_double(double l, double f){
		//x,y idio me eye form
		//me scale factor = 1
		//stis geometrikes odotites px points poy thelo gia zoom =1 kai to scale einai sto scale matrix
		double out[] = new double[2] ;
		
		out[0]= (((l- originLon)/originBima -0.5d)*myGLSE20app.gssize*2);
		out[1]= (((f- originLat)/originBima -0.5d)*myGLSE20app.gssize*2);
		//out[1] = (y/myGLSE20app.gssize/2+0.5f)*originBima + originLat;
        return out;
	}
	
	
	public float[] lf2xy_noscale(double l, double f, double sf){
		//x,y idio me eye form
		//me scale factor = 1
		//stis geometrikes odotites px points poy thelo gia zoom =1 kai to scale einai sto scale matrix
		float out[] = new float[2] ;
		
		out[0]=(float) (((l- originLon)/originBima -0.5d)*myGLSE20app.gssize*2*sf);
		out[1]=(float) (((f- originLat)/originBima -0.5d)*myGLSE20app.gssize*2*sf);
		//out[1] = (y/myGLSE20app.gssize/2+0.5f)*originBima + originLat;
        return out;
	}
	
	
	public String xy2string(float x, float y){
		float lf[] = xy2lf(x,y);
		//return x+"," + y + "  -  " + lf[0] + " , " + lf[1];
		return  lf[0] + " , " + lf[1];
	}
	
	public void setCurXY(float x, float y){
		curImageX=x;
		curImageY=y;
		//Log.i(""+x,""+y);
		setVisibleGridCells();
	}
	
	private boolean itemExists(float indX, float indY){
		boolean found = false;
		int i;
		for(i=0;i<=item.size()-1;i++){
			if(item.get(i).gridCol==indX){
				if(item.get(i).gridRow==indY){
					found=true;
				}
			}
		}
		return found;
	}
	
	public void add(float l, float f,float indX, float indY){
		Object[] res = ktima.getUrlFromLonLat(l, f,  myGLSE20app.mGLRenderer.zoom_level);
		String filename = (String) res[1] ;
		filename = "/geoCloudCache/" + filename + ".jpg";
		//ktima.getImageToImageGrid(l,f, myGLSE20app.mGLRenderer.zoom_level,item,indX,indY);
	}
	
	
	
	public void draw(		float[] mLightPosInEyeSpace,
							float[] mViewMatrix,
							float[] mProjectionMatrix,
							float scale
			){
		
			 // Set our per-vertex lighting program.
	        GLES20.glUseProgram(mProgramHandle);
	        
	        // Set program handles for cube drawing.
	        mMVPMatrixHandle 			= GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	        mMVMatrixHandle 			= GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	        mLightPosHandle 			= GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
	        mTextureUniformHandle 		= GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
	        mPositionHandle 			= GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	        mColorHandle 				= GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
	        mNormalHandle 				= GLES20.glGetAttribLocation(mProgramHandle, "a_Normal"); 
	        mTextureCoordinateHandle 	= GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        
        
		//Log.i("32322","323223");
			int i;
			boolean left = false;
			
			for(i=0;i<=item.size()-1;i++){
											
											try {
												if(!item.get(i).textureSet) {left=true;;item.get(i).set_mTextureDataHandle(mActivityContext);}else{
												
													item.get(i).draw(mLightPosInEyeSpace,
																	mProjectionMatrix,
																	mViewMatrix,
																	mMVPMatrixHandle,
																	mMVMatrixHandle,
																	mLightPosHandle,
																	mTextureUniformHandle,
																	mPositionHandle,
																	mColorHandle,
																	mNormalHandle,
																	mTextureCoordinateHandle,
																	scale);
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												Log.i("debud draw Grid", item.get(i).filename);
											}
			}
			if(left==true){
				//long millis = System.currentTimeMillis() % 1000;
				//removeOffGridCells();
				//long millis2 = System.currentTimeMillis() % 1000;
				//Log.i("time",String.valueOf(millis2-millis)+"");
			}
			
	
	
	}
	
	
	
	
}
