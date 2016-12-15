package com.geocloud.app;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.microedition.khronos.opengles.GL10;

import tw.com.prolific.driver.pl2303.PL2303Driver;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.MyGLES20.D_Image_Grid;
import com.geocloud.MyGLES20.D_Square_Image;
import com.geocloud.MyGLES20.GL02_MyGLRenderer;
import com.geocloud.MyGLES20.GL01_MyGLSurfaceView;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.MyRelativeLayoutParams;
import com.geocloud.MyGLES20.MySerial;

import android.location.LocationManager;
import android.net.NetworkInfo;

import com.geocloud.db.DatabaseHelper;
import com.example.test2.R;
import com.geocloud.modules.tsComm;
import com.geocloud.topo.MyHouseCollection;
import com.geocloud.topo.MyKml;
import com.geocloud.topo.MyProject;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.MyStasiCollection;
import com.geocloud.topo.stasi_link_class;
import com.geocloud.window.MyWindow_edit;
import com.geocloud.window.MyWindow_edit_data;
import com.geocloud.window.MyWindow_edit_line_meta;
import com.geocloud.window.MyWindow_edit_lineadd;
import com.geocloud.window.MyWindow_edit_linemod;
import com.geocloud.window.MyWindow_edit_linemod_verify;
import com.geocloud.window.MyWindow_edit_point_meta;
import com.geocloud.window.MyWindow_house_meta;
import com.geocloud.window.MyWindow_msg;
import com.geocloud.window.MyWindow_set_Back_Orientation;
import com.geocloud.window.MyWindow_set_yo;
import com.geocloud.window.MyWindow_skop_stasi;
import com.geocloud.window.MyWindow_stasi;
import com.geocloud.window.MyWindow_tools;
import com.geocloud.wms.Ktima;
import com.geocloud.wms.Wms;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MyGLSE20app extends Activity 
{
	/** Hold a reference to our GLSurfaceView */
	//private GLSurfaceView mGLSurfaceView;
	private GL01_MyGLSurfaceView mGLSurfaceView;
	public 	GL02_MyGLRenderer 	mGLRenderer;
	private Display 		display ;
	private Ktima 			ktima;
	public Wms 			wms;
	private MyUtility 		myUtil;
	public DatabaseHelper 	dbHelper;
	
	public float  gssize = 2.0f;				//Size of tile in GL Units
	public String baseDir="";					//baseDir
	
	public MyParams			params;
	public D_Image_Grid 	grid;
	public TextView 		text1,text2,text3,text_debug,poly_add,poly_cancel,poly_undo,poly_end,poly_close;
	public ZoomControls 	zoom;
	public ImageView		stasi,pin,poly,tool_grid,edit_lines,edit_data,bt_button,house_img;
	public Spinner			spinner;
	public LinearLayout 	ll_main,ll_bar,ll_text,ll_tools,ll_poly,ll_poly_tools,ll_grid_tools,ll_footer;
	public RelativeLayout 	ll_wraper;
	public RelativeLayout 	ll_wraper_2;
	public MyWindow_stasi 		window_stasi;;
	public MyWindow_skop_stasi 		window_stasi_skop;;
	public MyWindow_tools 		window_tools;;
	public MyWindow_edit 		window_edit;;
	public MyWindow_set_Back_Orientation 		window_back_orientation;;
	private ListView 		lv_poly, lv_tools;
	
	
	//private CheckBox		allow_download;
	private CheckBox		show_map;
	
	
	private boolean downloading_flag = false;
	private int tmp_int;
	private Object[] tmp_ob;
	public MyGLSE20app 		myGLSE20app;		//myApp
	public Vibrator vib;
	
	public ArrayList<String> download_array;
	public float Lon0, Lat0;
	
	 PL2303Driver mSerial;
	 private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
	 private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
	 private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
	 private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
	 private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.DTRDSR;
	 private static final String ACTION_USB_PERMISSION = "com.geocloud.app.USB_PERMISSION";   

	 
	 public MyStasiCollection staseis;
	 public MyHouseCollection houses;
		public ArrayList<MyProject> myProjectCollection = new ArrayList<MyProject>();
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		 //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//this.getBaseContext().setKeepScreenOn(true);
		
		
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		//getWindow().getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		myGLSE20app 	= this;
		
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		//int densityDpi = (int)(metrics.density * 160f);
		int densityDpi = (int)(metrics.densityDpi/120/2);
		
		display 		= getWindowManager().getDefaultDisplay(); 
		ktima 			= new Ktima(this.getBaseContext());
		wms 			= new Wms(this.getBaseContext(), "ktimatologio", "ktima", "", 3, 3,512,512);
		baseDir 		= Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache";  
		mGLSurfaceView 	= new GL01_MyGLSurfaceView(this,display.getWidth(),display.getHeight()-100,this);
		
		
		
				
				
		params			= new MyParams(myGLSE20app);
		params.baseDir=baseDir;
		params.screen_scale_factor = (float) densityDpi;///120;
		params.wms=wms;
		params.setSurfaceView(mGLSurfaceView);
		
		vib 					= (Vibrator) 		getSystemService(Context.VIBRATOR_SERVICE);
		download_array = new ArrayList<String>();
		Lon0 = 23.709f;
		Lat0 = 37.912f;
		
		Lon0 = 22.805f;
		Lat0 = 37.57f;
		
		downloading_flag = false;
		
		staseis = new MyStasiCollection(myGLSE20app);
		params.staseis=staseis;
		
		houses = new MyHouseCollection(myGLSE20app);
		params.houses=houses;
		
		
		
		dbHelper = new DatabaseHelper(myGLSE20app,params);
		try {		dbHelper.createDataBase();		} catch (IOException e) {	e.printStackTrace();	}
		params.app.dbHelper.openDataBase();
    	params.app.dbHelper.loadProjectsFromDb();
		
    	params.tabId = dbHelper.getKeyValue("tabId");
    	params.dt = Long.valueOf(dbHelper.getKeyValue("dt"));
		
		//Lon0 = 22.86f;
		//Lat0 = 37.57f;
		
		// Check if the system supports OpenGL ES 2.0.
		final ActivityManager activityManager 		= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo 	= activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			mGLSurfaceView.setEGLContextClientVersion(2);
			
			//mGLSurfaceView.setEGLConfigChooser(false);
			//mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
			mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
			mGLSurfaceView.setRenderer(new GL02_MyGLRenderer(this));
			//mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		} else 	{
			// This is where you could create an OpenGL ES 1.x compatible
			// renderer if you wanted to support both ES 1 and ES 2.
			return;
		}
		
		
		setContentView(mGLSurfaceView);
		
		ll_main 		= new LinearLayout(this.getBaseContext());	ll_main.setOrientation(1);					//ll_main.setBackgroundColor(Color.argb(100,0, 0, 0));
		ll_footer 		= new LinearLayout(this.getBaseContext());	ll_footer.setOrientation(1);					ll_footer.setBackgroundColor(Color.argb(100,0, 0, 0));
		
		ll_wraper 	= new RelativeLayout(this.getBaseContext());	/*ll_footer.setOrientation(1);*/					//ll_wraper.setBackgroundColor(Color.argb(100,100, 0, 0));
		ll_wraper.setGravity(Gravity.BOTTOM);
		RelativeLayout.LayoutParams layout_params;
		layout_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 160);
		ll_wraper.addView(ll_footer, layout_params);
		
		
		
		
		//layout_params.leftMargin=100;
		//params.leftMargin = 80;
		//layout_params.topMargin=300;
		
		
		ll_wraper_2 	= new RelativeLayout(this.getBaseContext());	/*ll_footer.setOrientation(1);*/					//ll_wraper.setBackgroundColor(Color.argb(100,100, 0, 0));
		//ll_wraper.setGravity(Gravity.BOTTOM);
		//RelativeLayout.LayoutParams layout_params;
		//layout_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
		//ll_wraper.addView(ll_footer, layout_params);
		
		
		
		
		
		
		
		params.activeProject = new MyProject(params);
		
		window_stasi 	= new MyWindow_stasi(params);	
		ll_wraper_2.addView(window_stasi.layout, window_stasi.layout_params);
		params.window_stasi = window_stasi;
		
		params.window_msg 	= new MyWindow_msg(params);	
		ll_wraper_2.addView(params.window_msg.layout, params.window_msg.layout_params);
		
		params.window_edit 	= new MyWindow_edit(params);	
		ll_wraper_2.addView(params.window_edit.layout, params.window_edit.layout_params);
		
		params.window_edit_data 	= new MyWindow_edit_data(params);	
		ll_wraper_2.addView(params.window_edit_data.layout, params.window_edit_data.layout_params);
		
		params.window_edit_lineadd 	= new MyWindow_edit_lineadd(params);	
		ll_wraper_2.addView(params.window_edit_lineadd.layout, params.window_edit_lineadd.layout_params);
		
		params.window_edit_linemod 	= new MyWindow_edit_linemod(params);	
		ll_wraper_2.addView(params.window_edit_linemod.layout, params.window_edit_linemod.layout_params);
		
		params.window_edit_linemod_verify 	= new MyWindow_edit_linemod_verify(params);	
		ll_wraper_2.addView(params.window_edit_linemod_verify.layout, params.window_edit_linemod_verify.layout_params);
		
		params.window_edit_line_meta 	= new MyWindow_edit_line_meta(params);	
		ll_wraper_2.addView(params.window_edit_line_meta.layout, params.window_edit_line_meta.layout_params);
		
		params.window_edit_point_meta 	= new MyWindow_edit_point_meta(params);	
		ll_wraper_2.addView(params.window_edit_point_meta.layout, params.window_edit_point_meta.layout_params);
		
		params.window_house_meta 	= new MyWindow_house_meta(params);	
		ll_wraper_2.addView(params.window_house_meta.layout, params.window_house_meta.layout_params);
		
		params.window_yo 	= new MyWindow_set_yo(params);	
		ll_wraper_2.addView(params.window_yo.layout, params.window_yo.layout_params);
		
		
		window_stasi_skop 	= new MyWindow_skop_stasi(params);	
		ll_wraper_2.addView(window_stasi_skop.layout, window_stasi_skop.layout_params);
		params.window_stasi_skop = window_stasi_skop;
		
		
		window_back_orientation 	= new MyWindow_set_Back_Orientation(params);	
		ll_wraper_2.addView(window_back_orientation.layout, window_back_orientation.layout_params);
		params.window_back_orientation = window_back_orientation;
		
		
		
		
		
		window_tools 	= new MyWindow_tools(params);	
		ll_wraper_2.addView(window_tools.layout, window_tools.layout_params);
		params.window_tools = window_tools;
		
		
		
		
		
		ll_bar 		= new LinearLayout(this.getBaseContext());	ll_bar.setOrientation(0);					ll_bar.setBackgroundColor(Color.argb(160,0,0,0));
		ll_text 	= new LinearLayout(this.getBaseContext());	ll_text.setOrientation(1);					//ll_text.setBackgroundColor(Color.argb(100,0, 0, 0));
		//ll_footer_text_2 	= new LinearLayout(this.getBaseContext());	ll_footer_text_2.setOrientation(1);					//ll_text.setBackgroundColor(Color.argb(100,0, 0, 0));
		ll_poly 	= new LinearLayout(this.getBaseContext());	ll_poly.setOrientation(1);					//ll_poly.setBackgroundColor(Color.argb(100,0, 70, 0));
		ll_poly_tools 	= new LinearLayout(this.getBaseContext());	ll_poly_tools.setOrientation(1);		ll_poly_tools.setBackgroundColor(Color.argb(160,0, 0, 0));
		ll_grid_tools 	= new LinearLayout(this.getBaseContext());	ll_grid_tools.setOrientation(1);		ll_grid_tools.setBackgroundColor(Color.argb(160,0, 0, 0));
		ll_poly.setPadding(0, 15, 15, 0);
		ll_poly.setGravity(Gravity.RIGHT);
		ll_tools 	= new LinearLayout(this.getBaseContext());	ll_tools.setOrientation(0);					//ll_tools.setBackgroundColor(Color.argb(100,56, 0, 0));
		
		ll_tools.setGravity(Gravity.RIGHT);
		ll_tools.setPadding(30, 5, 30, 0);
		text1 = new TextView(this.getBaseContext());	text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		text2 = new TextView(this.getBaseContext());	text2.setText("");						//text2.setBackgroundColor(Color.argb(100,0,0, 0));
		text3 = new TextView(this.getBaseContext());	text3.setText("");						//text3.setBackgroundColor(Color.argb(100,0,0, 0));
		text_debug = new TextView(this.getBaseContext());	text_debug.setText("debug");						//text3.setBackgroundColor(Color.argb(100,0,0, 0));
		
		
		
		
		//layout_params.leftMargin=100;
		//params.leftMargin = 80;
		//layout_params.topMargin=300;
		
		
		lv_poly = new ListView(this.getBaseContext());
		String[] stringArray = new String[] { "Add", "Undo","End","Close","Clear" };
		
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, R.layout.list_item, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_list_item_2, stringArray);
		lv_poly.setAdapter(modeAdapter);
		
		lv_poly.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2==4) ll_poly_tools.removeView(lv_poly);
				//Log.i("clicked","clicked " + arg2);
				/*super.onListItemClick(l, v, position, id);
				if(position==1){
					showDialog(1);
					
				}else{
				
					String cheese  =  classes[position];
					try{
							Class ourClass = Class.forName("com.geocloud.app." + cheese);
							Intent ourIntent = new Intent(menu.this,ourClass);
							startActivity(ourIntent);
					}catch(ClassNotFoundException e){
							e.printStackTrace();
					}
				}*/
				
			}
		}
		);

        
		//setListAdapter(new ArrayAdapter<String>(this.getBaseContext(), lv_poly, {"111","22","#"}));
		//poly_add = new TextView(this.getBaseContext());	poly_add.setText("Add");						//text3.setBackgroundColor(Color.argb(100,0,0, 0));


		
		
		lv_tools = new ListView(this.getBaseContext());
		stringArray = new String[] { "Download", "Map","Vector","Close" };
		
		modeAdapter = new ArrayAdapter<String>(this, R.layout.list_item, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_list_item_2, stringArray);
		lv_tools.setAdapter(modeAdapter);
		
		lv_tools.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2==3) ll_grid_tools.removeView(lv_tools);
				//Log.i("clicked","clicked " + arg2);
				/*super.onListItemClick(l, v, position, id);
				if(position==1){
					showDialog(1);
					
				}else{
				
					String cheese  =  classes[position];
					try{
							Class ourClass = Class.forName("com.geocloud.app." + cheese);
							Intent ourIntent = new Intent(menu.this,ourClass);
							startActivity(ourIntent);
					}catch(ClassNotFoundException e){
							e.printStackTrace();
					}
				}*/
				
			}
		}
		);

		
		

		
		
		pin = new ImageView(this.getBaseContext());
		//pin.set
		//pin.setImageResource(getResources().getIdentifier("pin_80", "drawable-hdpi", getPackageName()));
		pin.setImageResource(R.drawable.pin_80);
		
		
		pin.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mGLRenderer.marker.add(mGLRenderer.curx, mGLRenderer.cury,1);
				vib.vibrate(100);
				//Log.i("wew","wewew");
			}
			
		});
		
		
		
		
		
		
		house_img = new ImageView(this.getBaseContext());
		house_img.setImageResource(R.drawable.house);
		
		
		house_img.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
					params.window_house_meta.show();
					vib.vibrate(100);
				
				}
			}
			
		);
		
		
		
		
		
		
		stasi = new ImageView(this.getBaseContext());
		stasi.setImageResource(R.drawable.stasi_80_off);
		
		
		stasi.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(params.activeProject._id>-1){
					
					if(!params.mode.edit_line){
					staseis.add(mGLRenderer.curx, mGLRenderer.cury);
					params.app.dbHelper.addStasiToDb(staseis.item.get(staseis.item.size()-1));
					params.activeProject.addStasiLink(staseis.item.get(staseis.item.size()-1)._id);
					
					staseis.item.get(staseis.item.size()-1).log();
					vib.vibrate(100);
					//Log.i("wew","wewew");
					}
				}
			}
			
		});
		
		
		
	
		
		
		
		poly = new ImageView(this.getBaseContext());
		//pin.set
		//pin.setImageResource(getResources().getIdentifier("pin_80", "drawable-hdpi", getPackageName()));
		poly.setImageResource(R.drawable.poly);
		
		
		poly.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mGLRenderer.marker.add(mGLRenderer.curx, mGLRenderer.cury);
				//vib.vibrate(100);
				//Log.i("wew","wewew");
				vib.vibrate(100);
				//params.window_tools.show();
				
				ll_poly_tools.addView(lv_poly);
				
			}
			
		});
		
		
		
		
		
		
		
		
		
		edit_lines = new ImageView(this.getBaseContext());
		edit_lines.setImageResource(R.drawable.edit_off_256);
		edit_lines.setOnClickListener(new OnClickListener(){
			@Override 
			public void onClick(View v) {
				vib.vibrate(100);
				params.mode.toggle_edit_line();
				params.surface.requestRender();
			}	
		});
		RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(130, 80);
		par.topMargin=params.imagePixelHeight-200;
		par.leftMargin=params.imagePixelWidth-150;
		ll_wraper_2.addView(edit_lines,par);
		
		
		
		
		edit_data = new ImageView(this.getBaseContext());
		edit_data.setImageResource(R.drawable.calc_off);
		edit_data.setOnClickListener(new OnClickListener(){
			@Override 
			public void onClick(View v) {
				vib.vibrate(100);
				params.mode.toggle_edit_data();
				params.surface.requestRender();
			}	
		});
		par = new RelativeLayout.LayoutParams(130, 100);
		par.topMargin=params.imagePixelHeight-210;
		par.leftMargin=params.imagePixelWidth-150-140-10;
		ll_wraper_2.addView(edit_data,par);
		
		
		
		
		
		bt_button = new ImageView(this.getBaseContext());
		bt_button.setImageResource(R.drawable.bt_off);
		bt_button.setOnClickListener(new OnClickListener(){
			@Override 
			public void onClick(View v) {
				vib.vibrate(100);
				params.tscomm.btToggle();
				//params.mode.toggle_edit_data();
			}	
		});
		par = new RelativeLayout.LayoutParams(130, 100);
		par.topMargin=params.imagePixelHeight-210;
		par.leftMargin=params.imagePixelWidth-150-140-120;
		ll_wraper_2.addView(bt_button,par);
		
		
		

		tool_grid = new ImageView(this.getBaseContext());
		//pin.set
		//pin.setImageResource(getResources().getIdentifier("pin_80", "drawable-hdpi", getPackageName()));
		tool_grid.setImageResource(R.drawable.grid_80);
		
		
		tool_grid.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mGLRenderer.marker.add(mGLRenderer.curx, mGLRenderer.cury);
				//vib.vibrate(100);
				//Log.i("wew","wewew");
				//vib.vibrate(100);
				//ll_grid_tools.addView(lv_tools);
				//openOptionsMenu();
				params.window_tools.toggle();
			}
			
		});
		
		
		
		spinner = new Spinner(this.getBaseContext());
		
		// Application of the Array to the Spinner
		//ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.marker));
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		spinner.setAdapter(spinnerArrayAdapter);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(249, 249, 249));   

            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		
		
		//pin.setBackgroundColor(Color.CYAN);
		zoom = new ZoomControls(this.getBaseContext());
		
		zoom.setOnZoomInClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				gssize =  gssize*((float) 1.2);
				grid.clear();
				grid.setVisibleGridCells();
			}
		});
		
		
		zoom.setOnZoomOutClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				gssize =  gssize/((float) 1.2);
				grid.clear();
				grid.setVisibleGridCells();
			}
		});
		
		ll_main.addView(ll_bar, ViewGroup.LayoutParams.FILL_PARENT, 100);
		//-ll_main.addView(ll_poly, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		ll_footer.addView(ll_text, 720, 160);
		ll_bar.addView(ll_tools, ViewGroup.LayoutParams.FILL_PARENT, 90);
		ll_text.addView(text_debug);
		ll_text.addView(text1);
		ll_text.addView(text2);
		ll_text.addView(text3);
		
		
		ll_tools.addView(house_img,130,80);
		
		ll_tools.addView(stasi,130,80);
		//ll_tools.addView(edit_lines,130,80);
		ll_tools.addView(tool_grid,120,80);
		
		
		
		this.addContentView(ll_main, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.addContentView(ll_wraper, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		this.addContentView(ll_wraper_2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		
		
	
		
		
		
		window_stasi.hide();
		window_tools.show();
		window_stasi_skop.hide();
		window_back_orientation.hide(false);
		
		
		
		
		
		UsbManager test = (UsbManager) getSystemService(Context.USB_SERVICE);
		mSerial = new PL2303Driver(test, this, ACTION_USB_PERMISSION,false); 
		params.tscomm = new tsComm(params,mSerial);
		

  
		  
	
	}
	

	
	 public void onStart() {
	    	params.debug("Enter onStart");
	    	super.onStart();
	    	//Log.d(TAG, "Leave onStart");
	    }
	    /*
	    public void onResume() {
	    	Log.d(TAG, "Enter onResume"); 
	        super.onResume();
	        String action =  getIntent().getAction();
	    	Log.d(TAG, "onResume:"+action);
	    	
	        //if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))        
	       	if(!mSerial.isConnected()) {
	             if (SHOW_DEBUG) {
	              	  Log.d(TAG, "New instance : " + mSerial);
	             }
	            
	    		 if( !mSerial.enumerate() ) {
	    			
	              	Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();     
	              	return;
	              } else {
	                 Log.d(TAG, "onResume:enumerate succeeded!");
	              }    		 
	        }//if isConnected  
			Toast.makeText(this, "attached", Toast.LENGTH_SHORT).show();
	       	
	        Log.d(TAG, "Leave onResume"); 
	    }        
	    
	*/
	public void add_point_point(){
		
	}
	
	
	
	
	

public void getImageToImageGrid(double f,double l,int z,final ArrayList<D_Square_Image> item, final float indX, final float indY, final boolean download){
	// Assign image
	// Load or Download Async
	// indX, indY			X,Y index of GL datum
	
	
	if(params.show_map){
	
	
	//final Object[] dat 		= ktima.getUrlFromLonLat((float) f,(float) l, mGLRenderer.zoom_level);
	final Object[] dat 		= wms.getUrlFromCoor((float) f,(float) l, mGLRenderer.zoom_level);
	Object[] tmp;
	tmp = (Object[]) dat[0];
	/*
	int i,j,counter=-1;
	
	for(i=0;i<=wms.cols-1;i++){
		for(j=0;j<=wms.rows-1;j++){
			counter+=1;
			tmp = (Object[]) dat[counter];
			Log.i("test",String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[1])));
			Log.i("test",String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[0])));
		}
		
	}*/
	//final String myurl 		= (String) dat[0];
	//final String filename 	= String.valueOf(String.format(baseDir + "/%s.jpg",(String) dat[1]));
	final String filename 	= String.valueOf(String.format(baseDir + "/%s.ttg",(String) tmp[1]));
  	//final String[] 	data 	= new String[2];
  					//data[0]	= myurl;
  					//data[1]	= filename;
	 Log.i("exists_?" ,filename );
  	if(new java.io.File(filename).exists() ){
  		//Log.i("exists_?" ,"22222" );
  		File file = new File(filename);
  		
  		//Log.i("exists_?" ,"44444" );
  		int imageIndexX = Integer.valueOf( String.valueOf(tmp[6]));		//IndexX tou Tile apo emena
    	int imageIndexY = Integer.valueOf( String.valueOf(tmp[7]));		//IndexY tou Tile apo emena
    	//Log.i("exists_?" ,imageIndexX + "," + imageIndexY );
    	//Log.i("exists_?" ,indX + "," + indY );
    	//if(!download_array.contains(filename)){
	 		if(file.length()>18000){
	 			//WMS_downloaded_load loader = new WMS_downloaded_load(item,mGLRenderer.zoom_level,indX,indY,imageIndexX,imageIndexY,gssize, filename);
	 			//loader.execute("");
	 			 //Log.i("exists_ok" ,String.valueOf(file.length()) );
	 			item.add(new D_Square_Image(filename,mGLRenderer));		//Add grid Tile
	 			item.get(item.size()-1).set_coor_data((float) indX, (float) indY,mGLRenderer.zoom_level,imageIndexX, imageIndexY,gssize);	//Set grid Tile
	 		}else{
	 			// Log.i("exists" ,String.valueOf(file.length()) );
	 		}
    	//}
  	}else{
  		// An den yparxei, Download me Async Task  (Mexri 5 ?)
  		//Collection<String> download_array2 = null ;
  		//download_array.add("dsdsd");
  		//String aa = String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[1]))+"-";
  		
  		if(params.allow_download){
  		
		  		try {
		  			//download_array.add(aa);
		  			//Log.i("deb_111444",download_array.contains(filename)+"");
		  			if(!download_array.contains(filename)){
		  				//Log.i("deb_111",filename);
		  				if(downloading_flag==false){
		  					download_array.add(filename);
		  	  		
		  					WmsDownloader runner = new WmsDownloader(item,dat,download,mGLRenderer.zoom_level);
		  					runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		  					downloading_flag=true;
		  				}else{
		  					
		  				Log.i("skip","skip");
		  				}
		  				/*
		  				KtimaDownloader runner = new KtimaDownloader(item,download,mGLRenderer.zoom_level);
		  				//runner.execute(filename,myurl,String.valueOf(indX),String.valueOf(indY),String.valueOf(dat[6]),String.valueOf(dat[7]));     
		  				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,filename,myurl,String.valueOf(indX),String.valueOf(indY),String.valueOf(dat[6]),String.valueOf(dat[7]));     
		  			*/
		  			}
		  			} catch (Exception e1) {
		  			// TODO Auto-generated catch block
		  			Log.i("deb_1",e1.toString());
		  			Log.i("deb_1",filename);
		  			
		  		}
  		}
  		
  		
  	}
	}
  	
   
    try{
    	// kt.execute(data); 
    }catch(Exception e){
    	
    }
    
}






private class WmsDownloader extends AsyncTask<String, Void, Long> {	
	ArrayList<D_Square_Image> item;
	String data;  
	Object[] ob;
	boolean download;
	int level;
	
	public WmsDownloader(ArrayList<D_Square_Image> item,Object[] ob,boolean download,int level){
		this.ob = ob;
		tmp_ob=ob;
		this.item = item;
		this.download=download;	
		this.level = level;   //level kata tin edoli
	}
	
	protected Long doInBackground(String... param) {
		runOnUiThread(new Runnable() {@Override  public void run() {	text_debug.setText(""); }});
		
		int i,j,x,y,counter=-1;
		Object[] tmp;
		//Bitmap[] source = new Bitmap[wms.cols*wms.rows];
		//int w = wms.tile_width*wms.cols;
		//int h = wms.tile_height*wms.rows;
		//Bitmap combined = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		tmp_int=0;
		for(j=wms.rows-1;j>=0;j--){
		for(i=0;i<=wms.cols-1;i++){
			try{
				
			
				counter+=1;
				//tmp_int+=1;
				//runOnUiThread(new Runnable() {		@Override         		public void run() {	text_debug.setText("downloading " + tmp_int + "/" + (wms.rows*wms.cols));     	}	});
				
				tmp = (Object[]) ob[counter];
				DownloadImageThread runner = new DownloadImageThread(String.valueOf((String) tmp[0]),baseDir + "/tmp/22" + counter + ".ttg");
  				runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
				/*
				downloadImage(String.valueOf((String) tmp[0]),baseDir + "/tmp/22" + counter + ".jpg");
				//ImageDownloader runner = new ImageDownloader(String.valueOf((String) tmp[0]),baseDir + "/tmp/22" + counter + ".jpg");
  				//runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				source[counter]=BitmapFactory.decodeFile(baseDir + "/tmp/22" + counter + ".jpg");
				
				runOnUiThread(new Runnable() {		@Override         		public void run() {	text_debug.setText("appending " + tmp_int + "/" + (wms.rows*wms.cols));     	}	});
				
				for(x=0;x<=wms.tile_width-1;x++){
					for(y=0;y<=wms.tile_height-1;y++){
						
						combined.setPixel(i*wms.tile_width+x, j*wms.tile_height+y, source[counter].getPixel(x, y));
							
					}
				}
				//runOnUiThread(new Runnable() {		@Override         		public void run() {	text_debug.setText(tmp_int + "/" + (wms.rows*wms.cols));     	}	});
				Log.i("test",String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[1])));*/
				//Log.i("test",String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[1])));
				//Log.i("test",String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[0])));
} catch (Exception e){
	Log.i("debug me",e.toString());
				
			}
			}
			
		}
		
		//FileOutputStream out;
		//tmp = (Object[]) ob[0];
		//create_dir(String.format(baseDir + "/%s.jpg",(String) tmp[1]));
		//out = new FileOutputStream(String.valueOf(String.format(baseDir + "/%s.jpg",(String) tmp[1])));
		//combined.compress(Bitmap.CompressFormat.JPEG,  90, out);
		
		
		Log.i("test","completed");
	        return (long) 5.0;
	    } 
	}








private class DownloadImageThread extends AsyncTask<String, Void, Long> {	
	String my_url;
	String my_file;
	
	public DownloadImageThread(String myurl,String filename){
		my_url = myurl;
		my_file = filename;
	}
	
	protected Long doInBackground(String... param) {
		 File file = new File(my_file);
		 URL url;
		 Log.i("url",my_url);
		try {
				url = new URL(my_url);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("GET");
	            con.setDoOutput(true);
	            con.connect();

	            InputStream 			is 		= con.getInputStream();
	            FileOutputStream 		fos 	= new FileOutputStream(file);
	            BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
	            byte[] 					data 	= new byte[1024];
	            int x = 0;
	            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
	            fos.flush();  bout.flush();  fos.close();   bout.close();  is.close();
	            tmp_int+=1;
	            runOnUiThread(new Runnable() {		@Override         		public void run() {	text_debug.setText(tmp_int + "/" + (wms.rows*wms.cols));     	}	});
				
	            
			} catch (MalformedURLException e) {			Log.i("download error" ,e.toString() );
			} catch (IOException e) {  					Log.i("download error" ,e.toString() );
			} catch(Exception e){
				Log.i("download error" ,e.toString() );
			}
		
		if(tmp_int==wms.rows*wms.cols)resample();
	        return (long) 5.0;
	    } 
	}




private void downloadImage(String myurl,String filename){
	
	 File file = new File(filename);
	 URL url;
	 Log.i("url",myurl);
	try {
			url = new URL(myurl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.connect();

            InputStream 			is 		= con.getInputStream();
            FileOutputStream 		fos 	= new FileOutputStream(file);
            BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
            byte[] 					data 	= new byte[1024];
            int x = 0;
            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
            fos.flush();  bout.flush();  fos.close();   bout.close();  is.close();
            
		} catch (MalformedURLException e) {			Log.i("download error" ,e.toString() );
		} catch (IOException e) {  					Log.i("download error" ,e.toString() );
		} catch(Exception e){
			Log.i("download error" ,e.toString() );
		}
	Log.i("url","downloaded");
	
}



private void resample(){
	Bitmap[] source = new Bitmap[wms.cols*wms.rows];
	int w = wms.tile_width*wms.cols;
	int h = wms.tile_height*wms.rows;
	Bitmap combined = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
	
	int i,j,x,y,counter=-1;
	Object[] tmp;
	//Bitmap[] source = new Bitmap[wms.cols*wms.rows];
	//int w = wms.tile_width*wms.cols;
	//int h = wms.tile_height*wms.rows;
	//Bitmap combined = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
	
	for(j=wms.rows-1;j>=0;j--){
	for(i=0;i<=wms.cols-1;i++){
		counter+=1;
		tmp = (Object[]) tmp_ob[counter];
		source[counter]=BitmapFactory.decodeFile(baseDir + "/tmp/22" + counter + ".ttg");
		
		//runOnUiThread(new Runnable() {		@Override         		public void run() {	text_debug.setText("appending " + tmp_int + "/" + (wms.rows*wms.cols));     	}	});
		
		for(x=0;x<=wms.tile_width-1;x++){
			for(y=0;y<=wms.tile_height-1;y++){
				
				combined.setPixel(i*wms.tile_width+x, j*wms.tile_height+y, source[counter].getPixel(x, y));
					
			}
		}
	}
	}
	
	FileOutputStream out;
			tmp = (Object[]) tmp_ob[0];
			create_dir(String.format(baseDir + "/%s.ttg",(String) tmp[1]));
			try {
				out = new FileOutputStream(String.valueOf(String.format(baseDir + "/%s.ttg",(String) tmp[1])));
				combined.compress(Bitmap.CompressFormat.JPEG,  90, out);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			float xy[] = mGLRenderer.grid.lf2xy(mGLSurfaceView.lastLonLat[0],mGLSurfaceView.lastLonLat[1]);
			mGLRenderer.set_ortho(xy[0], xy[1],true);
             
			downloading_flag=false;
	
}


private class ImageDownloader extends AsyncTask<String, Void, Long> {	
	//ArrayList<Boolean> completed;
	String myurl;  
	String filename;  
	
	
	public ImageDownloader(/*ArrayList<Boolean> completed,*/String url,String filename){
		//this.completed = completed;
		this.myurl = url;
		this.filename=filename;	
	}
	
	protected Long doInBackground(String... param) {
		try { 
    		//mGLSurfaceView.requestRender();			//Gia DIRTY MODE mallon (oxi auto update)
        	/*		
        		runOnUiThread(new Runnable() {		//giati allios de sxediazei sto UI
        		         @Override
        		            public void run() {
        		        	 	//text3.setText(filename); 
        		         	}
        		 });*/
			Log.i("test",myurl);
			Log.i("test",filename);
        		
        		 File file = new File(filename);
        		 URL url;
    			try {
    					url = new URL(myurl);
    					HttpURLConnection con = (HttpURLConnection)url.openConnection();
    		            con.setRequestMethod("GET");
    		            con.setDoOutput(true);
    		            con.connect();

    		            InputStream 			is 		= con.getInputStream();
    		            FileOutputStream 		fos 	= new FileOutputStream(file);
    		            BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
    		            byte[] 					data 	= new byte[1024];
    		            int x = 0;
    		            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
    		            fos.flush();  bout.flush();  fos.close();   bout.close();  is.close();
    		            
    				} catch (MalformedURLException e) {			Log.i("download error" ,e.toString() );
    				} catch (IOException e) {  					Log.i("download error" ,e.toString() );
    				} 
        		
    				
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
	        return (long) 5.0;
	    } 
	}




private class KtimaDownloader extends AsyncTask<String, Void, Long> {
	 	ArrayList<D_Square_Image> item;// = new ArrayList<D_Square_Image>();
		boolean download;
		int level;
		String myfilename;
		
		public KtimaDownloader(ArrayList<D_Square_Image> item,boolean download,int level){
			this.item = item;
			this.download=download;	
			this.level = level;   //level kata tin edoli
		}
		
		
	    protected Long doInBackground(String... param) {
	    	final String 	filename 	= param[0];
	    	String 			myurl 		= param[1];
	    	float 			indX 		= Float.valueOf		(param[2]);
	    	float 			indY 		= Float.valueOf		(param[3]);
	    	int 			imageIndexX = Integer.valueOf	(param[4]);
	    	int 			imageIndexY = Integer.valueOf	(param[5]);
	    	
	    	myfilename=filename;
	    	//myUtil.checkCreateFolder(baseDir + "/17");				//Kati xtipaei
	    	//myUtil.checkCreateFolder(baseDir + "/17/" + param[5]);
	    	String path;
	    	path = baseDir + "/" + mGLRenderer.zoom_level;				if(!new java.io.File(path).isDirectory()){File directory = new File(path);directory.mkdirs();	}
	    	path = baseDir + "/" + mGLRenderer.zoom_level + "/" + param[5];	if(!new java.io.File(path).isDirectory()){File directory = new File(path);directory.mkdirs();	}
	    	
	    	try { 
	    		//mGLSurfaceView.requestRender();			//Gia DIRTY MODE mallon (oxi auto update)
            	if(this.download && level==mGLRenderer.zoom_level){    //an edose edoli prin allagi level gia na min exo queue den proxorao
            			
            		runOnUiThread(new Runnable() {		//giati allios de sxediazei sto UI
            		         @Override
            		            public void run() {
            		        	 	//text3.setText(filename); 
            		         	}
            		 });
            		
            		 File file = new File(filename);
            		 URL url;
        			try {
        					url = new URL(myurl);
        					HttpURLConnection con = (HttpURLConnection)url.openConnection();
        		            con.setRequestMethod("GET");
        		            con.setDoOutput(true);
        		            con.connect();

        		            InputStream 			is 		= con.getInputStream();
        		            FileOutputStream 		fos 	= new FileOutputStream(file);
        		            BufferedOutputStream 	bout 	= new BufferedOutputStream(fos,1024);
        		            byte[] 					data 	= new byte[1024];
        		            int x = 0;
        		            while((x=is.read(data,0,1024))>=0){   bout.write(data,0,x);     }
        		            
        		            fos.flush();  bout.flush();  fos.close();   bout.close();  is.close();
        		            if(level==mGLRenderer.zoom_level){
        		            	item.add(new D_Square_Image(filename,mGLRenderer,level));																	//Add grid Tile
        		            	D_Square_Image tmp = item.get(item.size()-1);
        		           
        		            	tmp.set_coor_data((float) indX, (float) indY,mGLRenderer.zoom_level,imageIndexX, imageIndexY,gssize);	//Set grid Tile
        		            }
        				} catch (MalformedURLException e) {
        					Log.i("download error" ,e.toString() );
        				} catch (IOException e) {
        					Log.i("download error" ,e.toString() );
        				} 
            		}
        				
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
	        return (long) 5.0;
	    }
	    
	    protected void onProgressUpdate(Integer... progress) {
	        runOnUiThread(new Runnable() {
		         @Override
		             public void run() {
		        	//text3.setText(filename); 	
		         }
		        });
	    }
	    
	    protected void onPostExecute(Long result/*Void param*/) {
	    	download_array.remove(myfilename);
	       	runOnUiThread(new Runnable() {
		         @Override
		             public void run() {
		        	 int a = myGLSE20app.download_array_size();
		        	 if(a>0){
		        	 myGLSE20app.text2.setText( "downloading :  " + myGLSE20app.download_array_size());
	       	}else{
	       		myGLSE20app.text2.setText( " ");
		        	//text3.setText(""); 
		        	//text1.setText(grid.get_drawn_tiles());
		         }}
		        });		
	    }
	}














private class WMS_downloaded_load extends AsyncTask<String, Void, Long> {
	 	ArrayList<D_Square_Image> item;// = new ArrayList<D_Square_Image>();
		float indX;
		float indY;
		int imageIndexX;
		int imageIndexY;
		float gssize;
		String filename;
		public WMS_downloaded_load(ArrayList<D_Square_Image> item,int level,float indX,float indY, int imageIndexX,int imageIndexY ,float gssize, String filename ){
			this.item = item;
			this.filename=filename;
			this. indX=indX;
			this. indY=indY;
			this. imageIndexX=imageIndexX;
			this. imageIndexY=imageIndexY;
			this. gssize=gssize;
		}
	    protected  Long doInBackground(String... param) {
	    	return (long) 5.0;
	    }
	    protected void onPostExecute(Long result/*Void param*/) {
	    	item.add(new D_Square_Image(filename,mGLRenderer));		//Add grid Tile
 			item.get(item.size()-1).set_coor_data((float) indX, (float) indY,mGLRenderer.zoom_level,imageIndexX, imageIndexY,gssize);	//Set grid Tile
	    }
	}














	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		//params.debug("Enter onResume");
		super.onResume();
		try {
			mSerial.enumerate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		params.datacast.timerKick();
		
		params.tscomm.chkConnection();
		//params.tscomm.btConnect();
		/*
		 Intent intent = getIntent();
		    String action = intent.getAction();
		    params.debug("-" + action);
		    if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {

		        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
		        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);

		    } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
		    }*/
		    
		if(params.window_tools.data_cast.isChecked()) params.datacast.set(true);
		    mGLSurfaceView.onResume();
		    
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		mGLSurfaceView.onPause();
		this.dbHelper.close();
		if(params.tscomm.btConnected())	params.tscomm.btDisconnect();
		params.datacast.set(false);
		/*try {
			params.tscomm.btSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			//menu.add(0, 0, 0, "Download");
			//menu.add(1, 1, 1, "Show map");
		//baseDir 		= Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache";  
		
		
		
		Log.i("baseDir",Environment.getExternalStorageDirectory().getAbsolutePath() );
		File directory = new File(baseDir + "/vector");
		File[] files = directory.listFiles();
		int i =0;
		
		
		MenuItem r = menu.add(1, 1, 1, "none");
		r.setCheckable(true);;
		
		
	//	SubMenu menuItem  =  menu. addSubMenu(0, 0, 1, "Download");
		//MenuItem enable_web = menuItem.add(1, 1, 1, "Enable");
		//MenuItem disable_web = menuItem.add(1, 1, 1, "Disable");
		
		SubMenu menuItem2  =  menu. addSubMenu(0, 1, 2, "Tools");
		
		
		MenuItem tmp = menuItem2.add(1, 1, 1, "syncToCloud");
			
		tmp.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				params.activeProject.syncToCloud();
				return false;
			}
		}
		);
		
		
		tmp = menuItem2.add(1, 2, 1, "export CR1");
		tmp.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				params.activeProject.sendToMailCR1();
				return false;
			}
		}
		);
		
		
		
		tmp = menuItem2.add(1, 2, 1, "export Staseis");
		tmp.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				params.activeProject.sendToMailStaseis();
				return false;
			}
		}
		);
		
		
		
		/*
		for(i=1;i<=files.length;i++){
				tmp = menuItem2.add(1, 100+i, 1, files[i-1].getName());
				//String test = files[i-1].getName();
				
				tmp.setOnMenuItemClickListener(new OnMenuItemClickListener(){

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						int index = item.getItemId()-101;
						
						Log.i("aa java" , "taped" + index);
						//File directory = new File(baseDir + "/vector");
						//File[] files = directory.listFiles();
						//MyKml kml = new MyKml(Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache/vector/" + files[index].getparams);
						//MyKml kml = new MyKml( files[index].getAbsolutePath(),params);
						// TODO Auto-generated method stub
						return false;
					}
					
				});
			}*/
			
			//menu.findItem(0).
			
			//getMenuInflater().inflate(R.menu.glse20options, menu);
			return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.upres:
			//Toast.makeText(this, "You selected Gps Data log", Toast.LENGTH_SHORT).show();
			try{
	
			}catch(Exception e){
				//Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
				
			}
			break;
			
			
		case R.id.downres:
			showDialog(1);
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	   NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public int download_array_size(){
		return download_array.size();
	}
	
	
	
	public void create_dir(String dir){
		String[] separated = dir.split("/");
		Log.i("app create_dir",dir);
		int i;String tmp="";
		for(i=1;i<=separated.length-2;i++){
			tmp = tmp + "/" + separated[i];
			File f = new File(tmp);
			if(!f.exists()){
				f.mkdir();
			}
			//Log.i("create_dir",tmp);
			
		}
		
	}
	
	
	
	
	
	
	
	
	public void prolific_open() {
	   	 if(null==mSerial)
			   return;   	 
		   
	       if (mSerial.isConnected()) {
	           
	    	   params.debug( "openUsbSerial : isConnected ");
	          
	           String str = "9600";
	           int baudRate= Integer.parseInt(str);
			   switch (baudRate) {
	             	case 9600:
	             		mBaudrate = PL2303Driver.BaudRate.B9600;
	             		break;
	             	case 19200:
	             		mBaudrate =PL2303Driver.BaudRate.B19200;
	             		break;
	             	case 115200:
	             		mBaudrate =PL2303Driver.BaudRate.B115200;
	             		break;
	             	default:
	             		mBaudrate =PL2303Driver.BaudRate.B9600;
	             		break;
	           }   		            
			   params.debug( "baudRate:"+baudRate);
			  // if (!mSerial.InitByBaudRate(mBaudrate)) {
	           if (!mSerial.InitByBaudRate(mBaudrate,700)) {
	        	   if(!mSerial.PL2303Device_IsHasPermission()) {
	        		   params.debug( "cannot open, maybe no permission");		
					}
					
	               if(mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
	            	   params.debug("cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
	               }
	           } else {        	      
	                  //Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();        	   
	           }
	       }//isConnected
	       else{
	    	   
	    	   params.debug("mSerialNotConnected");
	       }
	      
	   }//openUsbSerial
	
	
	
	
	/*
	 
   public void prolific_read() throws InterruptedException {
	   //Thread.sleep(1220);
       int len=0;
       byte[] rbuf = new byte[4096];
       StringBuffer sbHex=new StringBuffer();
       
      // Log.d(TAG, "Enter readDataFromSerial");

		if(null==mSerial)
			return;        
       
       if(!mSerial.isConnected()) 
       	return;
       int counter = 0;
       while(len<5 && counter<20){
    	   counter+=1;
    	   len = mSerial.read(rbuf);
    	   Thread.sleep(100);
    	   if (len > 0) {  
    		   Thread.sleep(300);
               //if (SHOW_DEBUG) {
            	  // Log.d(TAG, "read len : " + len);
              // }                
               //rbuf[len] = 0;
               for (int j = 0; j < len; j++) {            	   
                 //String temp=Integer.toHexString(rbuf[j]&0x000000FF);
                 //Log.i(TAG, "str_rbuf["+j+"]="+temp);
                 //int decimal = Integer.parseInt(temp, 16);
                 //Log.i(TAG, "dec["+j+"]="+decimal);
                 //sbHex.append((char)decimal);
                 //sbHex.append(temp);
            	   sbHex.append((char) (rbuf[j]&0x000000FF));
               }        
               params.debug(sbHex.toString());
               Log.i("111","#" + sbHex.toString() + "#");
              
               
               
               
               //etRead.setText(sbHex.toString());    
               //Toast.makeText(this, "len="+len, Toast.LENGTH_SHORT).show();
        }
    	   
       }
       
      
      // Log.d(TAG, "Leave readDataFromSerial");	
   }//readDataFromSerial
   
   
   
   
   
   
   

	 
   public void prolific_read_with_callback(int callback_function_index) throws InterruptedException {
	   //Thread.sleep(1220);
       int len=0;
       byte[] rbuf = new byte[4096];
       StringBuffer sbHex=new StringBuffer();
     	if(null==mSerial)
			return;        
       
       if(!mSerial.isConnected()) 
       	return;
       int counter = 0;
       while(len<5 && counter<20){
    	   counter+=1;
    	   len = mSerial.read(rbuf);
    	   if (len > 0) {  
    		   Thread.sleep(100);
                for (int j = 0; j < len; j++) {            	   
                 sbHex.append((char) (rbuf[j]&0x000000FF));
               }          
        }
    	   
       }
       
       if(callback_function_index==1){params.window_back_orientation.setBSCallback(sbHex.toString());};
       if(callback_function_index==2){params.window_stasi_skop.meas_all_callBack(sbHex.toString(),false);};//to false de paizei kanena rolo. iparxei gia to check ton parametron tis function
        
       
   }//readDataFromSerial
   


   public void prolific_read_with_callback(int callback_function_index,boolean inn) throws InterruptedException {
	   //Thread.sleep(1220);
       int len=0;
       byte[] rbuf = new byte[4096];
       StringBuffer sbHex=new StringBuffer();
     	if(null==mSerial)
			return;        
       
       if(!mSerial.isConnected()) 
       	return;
       int counter = 0;
       while(len<5 && counter<20){
    	   counter+=1;
    	   len = mSerial.read(rbuf);
    	   if (len > 0) {  
    		   Thread.sleep(100);
                for (int j = 0; j < len; j++) {            	   
                 sbHex.append((char) (rbuf[j]&0x000000FF));
               }          
        }
    	   
       }
       
       if(callback_function_index==1){params.window_back_orientation.setBSCallback(sbHex.toString());};
       if(callback_function_index==2){params.window_stasi_skop.meas_all_callBack(sbHex.toString(),inn);};
        
       
   }//readDataFromSerial
   
   

	 
   public String prolific_read_to_str() throws InterruptedException {
	   //Thread.sleep(1220);
       int len=0;
       byte[] rbuf = new byte[4096];
       StringBuffer sbHex=new StringBuffer();
       
     
		if(null==mSerial)
			return "";        
       
       if(!mSerial.isConnected()) 
       	return "";
       int counter = 0;
       while(len<5 && counter<20){
    	   counter+=1;
    	   len = mSerial.read(rbuf);
    	   Thread.sleep(100);
    	   if (len > 0) {  
    		   for (int j = 0; j < len; j++) {            	   
               
            	   sbHex.append((char) (rbuf[j]&0x000000FF));
               }        
              // params.debug(sbHex.toString());
             //  Log.i("111","#" + sbHex.toString() + "#");
              
               
               
    			//return sbHex.toString();
               //etRead.setText(sbHex.toString());    
               //Toast.makeText(this, "len="+len, Toast.LENGTH_SHORT).show();
        }
    	   
       }
       
       	return sbHex.toString();
   }//readDataFromSerial
   
   
   */
   public void prolific_write(String msg) {
   	 
   //	Log.d(TAG, "Enter writeDataToSerial");
   	
		if(null==mSerial)
			return;
   	
   	if(!mSerial.isConnected()) 
   		return;
   	
       String strWrite = msg + Character.toString((char) 13)+Character.toString((char) 10);
       
		 int res = mSerial.write(strWrite.getBytes(), strWrite.length());
		if( res<0 ) {
		//	Log.d(TAG, "setup2: fail to controlTransfer: "+ res);
			return;
		} 
		
		//Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show(); 

		//Log.d(TAG, "Leave writeDataToSerial");
   }//writeDataToSerial
   
   
	
   
   
   static final int REQUEST_IMAGE_CAPTURE = 1;
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           String path  = params.window_house_meta.curPhotoPath;
          // Bitmap imageBitmap = (Bitmap) extras.get("data");
           Bitmap imageBitmap=null;
           File f= new File((String) path);
           BitmapFactory.Options options = new BitmapFactory.Options();
           options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                   try {
                	   imageBitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                   } catch (FileNotFoundException e) {
                       e.printStackTrace();
                   }

                  // int factor = 300/
          // Bitmap imageBitmap =BitmapFactory.decodeFile((extras.get(MediaStore.EXTRA_OUTPUT));
           params.window_house_meta.mImageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 300, 300, false));
           Log.i("height",imageBitmap.getWidth() + "x" + imageBitmap.getHeight());
       }
   }
   
   
}


