package com.geocloud.window;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.modules.CameraService;
import com.geocloud.topo.MyHouse;
import com.geocloud.topo.MyKml;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyStasi;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyWindow_house_meta {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private int width,height;
	private boolean open = false;
	
	public Spinner spinnerType;
	public Spinner spinnerNumber;
	public TextView t1;
	public EditText et1;
	
	public ImageView mImageView  ;
	
	public Camera camera;
	public SurfaceView surfaceView;
	public SurfaceHolder surfaceHolder;
	public PictureCallback rawCallback;
	public ShutterCallback shutterCallback;
	public PictureCallback jpegCallback;
    private final String tag = "VideoServer";

    public Button save,cancel;
    
    public String curPhotoPath;
    
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_house_meta(MyParams params){
		this.params = 	params;
		this.layout = 	new RelativeLayout(params.app.getBaseContext());	
		this.layout.setBackgroundColor(Color.argb(255, 0,113,188));
		this.height = 	1100;
		this.width	=	800;
		
		this.layout_params 			= 	new RelativeLayout.LayoutParams(width, height);
		this.layout_params.topMargin=	-1200;
		
		RelativeLayout.LayoutParams tmp;
		
		
		save = new Button(params.app.getBaseContext());
		save.setText("??????.");
		
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				get_params().app.dbHelper.openDataBase();
				get_params().houses.add(get_params().renderer.curx, get_params().renderer.cury);
				MyHouse tmp = get_params().houses.item.get(get_params().houses.item.size()-1);
				tmp.type = spinnerType.getSelectedItemPosition();
				tmp.floors = spinnerNumber.getSelectedItemPosition();
				tmp.path=curPhotoPath;
				tmp.sxolia=et1.getText().toString();
				tmp.local_date = (int) get_params().getTime();
				tmp.addToDb();
				hide();
				
			}}
		);
		 tmp = new RelativeLayout.LayoutParams((width-40)/2-20, 150);
			tmp.leftMargin=(width-40)/2+20;
			tmp.topMargin=900;
			this.layout.addView(save,tmp);
			
			
			
			
			
			
			
			cancel = new Button(params.app.getBaseContext());
			cancel.setText("�����");
			
			cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hide();
					
				}}
			);
			 tmp = new RelativeLayout.LayoutParams((width-40)/2-20, 150);
				tmp.leftMargin=20;
				tmp.topMargin=900;
				this.layout.addView(cancel,tmp);
			
		
		mImageView = new ImageView(params.app.getBaseContext());
		 	//surfaceView = new SurfaceView(params.app.getBaseContext());
		 	
			

		 	
		 	/*surfaceView.setBackgroundColor(Color.WHITE);
	        surfaceHolder = surfaceView.getHolder();
	        surfaceHolder.addCallback(this);
	        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        rawCallback = new PictureCallback() {
	            public void onPictureTaken(byte[] data, Camera camera) {
	                Log.d("Log", "onPictureTaken - raw");
	            }
	        };
	        */
	        tmp = new RelativeLayout.LayoutParams(width-40, 400);
			tmp.leftMargin=20;
			tmp.topMargin=450;
			this.layout.addView(mImageView,tmp);
		

		
		
		
		
		spinnerType = new Spinner(params.app.getBaseContext());
		String[] stringArray = new String[] { "","�����","���������","������" };;
		ArrayAdapter<String> modeAdapterProject = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		modeAdapterProject.setDropDownViewResource(R.layout.list_item); // The drop down view
		//ArrayAdapter<String> modeAdapterProject = new ArrayAdapter<String>(params.app, android.R.layout.simple_spinner_item, stringArray);
		//modeAdapterProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		spinnerType.setAdapter(modeAdapterProject);
		spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               
				((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(255, 255, 255)); 
				// ( (TextView) selectedItemView).setTextColor(Color.rgb(249, 249, 249));
				//if(get_params().event.pointSelected){
					//get_params().event.selectedPointObject.metrisi.obtype=position;
					//get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
				//}else{
					//get_params().debug("no selected point");
			//	}
				/*afto giati iparxei to select trigger sto show tis klasis*/
				//if(get_params().event.lineSelected) {
							//get_params().event.selectedLineObject.meta.type = position;
							//get_params().event.selectedLineObject.updateMeta();
							//if(position==0){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 255f);
			            	//}
							//if(position==1){
							//	get_params().event.selectedLineObject.setColor(0f, 255f, 255f);
			            	//}
							//if(position==2){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 0f);
			            	//}
							
							
							//get_params().renderer.my_poly.reset_color();
				//}
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		//tmp = new RelativeLayout.LayoutParams(width-40,80);
		tmp = new RelativeLayout.LayoutParams(450,100);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(spinnerType,tmp);
		

		
		
		
		
		
		
		spinnerNumber = new Spinner(params.app.getBaseContext());
		stringArray = new String[] { "1","2","3","4","5" };;
		modeAdapterProject = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		modeAdapterProject.setDropDownViewResource(R.layout.list_item); // The drop down view
		//ArrayAdapter<String> modeAdapterProject = new ArrayAdapter<String>(params.app, android.R.layout.simple_spinner_item, stringArray);
		//modeAdapterProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		spinnerNumber.setAdapter(modeAdapterProject);
		spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               
				((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(255, 255, 255)); 
				// ( (TextView) selectedItemView).setTextColor(Color.rgb(249, 249, 249));
				//if(get_params().event.pointSelected){
					//get_params().event.selectedPointObject.metrisi.obtype=position;
					//get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
				//}else{
					//get_params().debug("no selected point");
			//	}
				/*afto giati iparxei to select trigger sto show tis klasis*/
				//if(get_params().event.lineSelected) {
							//get_params().event.selectedLineObject.meta.type = position;
							//get_params().event.selectedLineObject.updateMeta();
							//if(position==0){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 255f);
			            	//}
							//if(position==1){
							//	get_params().event.selectedLineObject.setColor(0f, 255f, 255f);
			            	//}
							//if(position==2){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 0f);
			            	//}
							
							
							//get_params().renderer.my_poly.reset_color();
				//}
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		//tmp = new RelativeLayout.LayoutParams(width-40,80);
		tmp = new RelativeLayout.LayoutParams(200,100);
		tmp.leftMargin=600;
		tmp.topMargin=10;
		this.layout.addView(spinnerNumber,tmp);
		

		
		
		
		
		
		
		/*
		
		
		t1 = new TextView(params.app.getBaseContext());	t1.setText("������");
		t1.setTextColor(Color.argb(210,255, 255,255));
		t1.setTextSize(2, 14f);
		tmp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
		tmp.leftMargin=40;
		tmp.topMargin=120;
		this.layout.addView(t1,tmp);
		
		*/
		
		et1 = new EditText(params.app.getBaseContext());	//et1.setText("������");
		//et1.setText("sdsdsdsd");
		et1.setBackgroundColor(Color.argb(255,255, 255,255));
		et1.setGravity(Gravity.TOP);
		et1.setTextColor(Color.argb(210,0, 0, 0));
		et1.setTextSize(2, 14f);
		tmp = new RelativeLayout.LayoutParams(width-40, 250);
		tmp.leftMargin=20;
		tmp.topMargin=130;
		this.layout.addView(et1,tmp);
		/*
		et1.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					
				}else{
					//if(get_params().event.pointSelected){
						get_params().event.selectedPointObject.metrisi.sxolia=et1.getText().toString();
						get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
						Log.i("saved",get_params().event.selectedPointObject.metrisi.sxolia);
				//}
				}
				
			}
			
		});
		*/
		
		open=false;
	}
	
	
	
	
	
	private void start_camera()
    {
        try{
            camera = Camera.open();
        }catch(RuntimeException e){
            Log.e(tag, "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        param.setPreviewSize(176, 144);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            //camera.takePicture(shutter, raw, jpeg)
        } catch (Exception e) {
            Log.e(tag, "init_camera: " + e);
            return;
        }
    }

    private void stop_camera()
    {
        camera.stopPreview();
        camera.release();
    }

    
    
	
	private MyParams get_params(){
		return params;
	}
	
	
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		//tmp.leftMargin=(params.imagePixelWidth-width);
		//tmp.topMargin=(550);
		tmp.leftMargin=(params.imagePixelWidth-width)/2;
		tmp.topMargin=(120);
		layout.setLayoutParams(tmp);
		
		
		params.window_tools.hide();

		
		spinnerType.setSelection(0);

		spinnerNumber.setSelection(0);
		et1.setText("");
		
		
		dispatchTakePictureIntent();
		
		
		open=true;
		
		//start_camera();
		/*pige sto line select*/
		
		//spinnerType.setSelection(
		//get_params().event.selectedLineObject.meta.type);
		
		//et1.setText(get_params().event.selectedPointObject.metrisi.sxolia);;
		
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
		if(open){
		//Log.i("saved",get_params().event.selectedPointObject.metrisi.sxolia);
	
		//}else{
			//get_params().debug("no selected point");
		//}
		
		//if(get_params().event.pointSelected) {
				//get_params().event.selectedPointObject.metrisi.sxolia=et1.getText().toString();
				//get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
				//Log.i("saved",get_params().event.selectedPointObject.metrisi.sxolia);
			
		//}
		}
		open=false;
		//params.window_tools.hide();
	}

	
	
	static final int REQUEST_IMAGE_CAPTURE = 1;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
            //    Uri.fromFile(photoFile));
	   
	    File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.i("klklk",ex.toString());
        }
        if (photoFile != null) {
        	
        	curPhotoPath =  Uri.fromFile(photoFile).getPath();
        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
	    	takePictureIntent.putExtra("path",
                    Uri.fromFile(photoFile));
	    	
	    if (takePictureIntent.resolveActivity(params.app.getPackageManager()) != null) {
	    	
    	params.app.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	   
	    	
	    
	    
	    }}
	}
	
	
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	     String imageFileName = "" + System.currentTimeMillis()/1000;
	    Log.i("sdsds",imageFileName);
	     File storageDir = Environment.getExternalStoragePublicDirectory(
	    		  params.baseDir + "/photo/p" + params.activeProject._id + "/" );
	     Log.i("sdsds",storageDir.getAbsolutePath());
	    
	     File image = new File(params.baseDir + "/photo/p" + params.activeProject._id + "/",  imageFileName + ".jpg");
	    // File image = File.createTempFile(
	     //   imageFileName,  /* prefix */
	      //  ".jpg",         /* suffix */
	       // storageDir      /* directory */
	   // );
	    Log.i("sdsds",image.getAbsolutePath());
		  
	    // Save a file: path for use with ACTION_VIEW intents
	   // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	


}
