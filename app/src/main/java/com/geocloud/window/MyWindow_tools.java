package com.geocloud.window;

import java.io.File;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyKml;
import com.geocloud.topo.MyStasi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyWindow_tools {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private TextView textView1;
	public TextView textViewName;
	private TextView textView2;
	public TextView textViewHeight;
	private TextView textView3;
	public TextView textViewX;
	private TextView textView4;
	private TextView textView5;
	private TextView textView6;
	public TextView textViewY;
	private TextView textViewClose;
	//public EditText editText1;
	private boolean open = false;
	
	
	
	private CheckBox		allow_download;
	private CheckBox		show_map;
	public CheckBox		data_cast;
	private Spinner spinner;
	private Spinner spinnerProject;
	private Spinner spinnerTools;
	public int curOpenStasiIndex;
	public int curSkopefsiIndex;
	
	
	
	private CheckBox		drawLine;
	private CheckBox		drawKml;
	private CheckBox		drawTax;
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_tools(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(350, 830);
		
		
		RelativeLayout.LayoutParams tmp;
		
		
		/*
		
		MyRelativeLayoutOutlined tmplayout = new MyRelativeLayoutOutlined(params.app.getBaseContext());//this.layout.setOrientation(1);			
		//tmplayout.setBackgroundColor(Color.argb(180,255,0, 0));
		tmp = new RelativeLayout.LayoutParams(210, 60);
		
		tmp.leftMargin=25;
		tmp.topMargin=160;
		this.layout.addView(tmplayout,tmp);
		
		
		tmplayout.setGravity(Gravity.CENTER);
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("vector");
		tmplayout.addView(textView3);
		
		
		tmplayout.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==event.ACTION_DOWN){
				//v.setBackgroundColor(Color.argb(180,100,120, 0));
				
				}
				//if(event.getAction()==event.ACTION_UP){
					//v.setBackgroundColor(Color.argb(0,100,120, 0));
					//}
				//	
				
				return false;
			}
			
			
			
		});
		
		*/
		
		
		
spinner = new Spinner(params.app.getBaseContext());
		
		// Application of the Array to the Spinner
		//ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, colors);
		//ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(params.app,   android.R.layout.simple_spinner_item, params.app.getBaseContext().getResources().getStringArray(R.array.marker));
		//spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		

		File myfile = new File( get_base_dir() + "/vector");
		File[] files = myfile.listFiles();
		
		int i;int counter=1;
		for (i=0;i<=files.length-1;i++){
			if(files[i].getPath().contains(".kml")){
				counter+=1;
			}
		}
		String[] stringArray = new String[counter+1];
		stringArray[0]="none";
		for (i=0;i<=files.length-1;i++){
			if(files[i].getPath().contains(".kml")){
				stringArray[i+1]= files[i].getName().replace(".kml", "");
			}
		}
		
//String[] stringArray = new String[] { "none", "nafplio_1","argolida","eds","nomoi_okxe","koutsoumpos"};
		
		ArrayAdapter modeAdapter = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		modeAdapter.setDropDownViewResource(R.layout.list_item); // The drop down view
		//ArrayAdapter modeAdapter = new ArrayAdapter<String>(params.app, android.R.layout.simple_spinner_item, stringArray);
		//modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_list_item_2, stringArray);
		//lv_tools.setAdapter(modeAdapter);
		
		
		spinner.setAdapter(modeAdapter);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(249, 249, 249));   
                // Log.i("sel", ((TextView)parentView.getChildAt(0)).getText().toString());
                 
                 String fname = ((TextView)parentView.getChildAt(0)).getText().toString();
               // ( (TextView) selectedItemView).setTextColor(Color.rgb(249, 249, 249));
                 File myfile = new File( get_base_dir() + "/vector/" + fname + ".kml");
                // Log.i("eee",myfile.exists() + "-");
					//File[] files = directory.listFiles();
					//MyKml kml = new MyKml(Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache/vector/" + files[index].getparams);
				if(myfile.exists()){	
                 MyKml kml = new MyKml( get_base_dir() + "/vector/" + fname + ".kml" ,get_params());
				}else{
					clearKml();
				}
					
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		tmp = new RelativeLayout.LayoutParams(280,120);
		tmp.leftMargin=20;
		tmp.topMargin=150;
		this.layout.addView(spinner,tmp);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
spinnerProject = new Spinner(params.app.getBaseContext());
		
		Log.i("--","-"+params.app.myProjectCollection.size());
		stringArray = new String[params.app.myProjectCollection.size()+1];
		stringArray[0]="none";
		for (i=0;i<=params.app.myProjectCollection.size()-1;i++){
			Log.i("--","-"+params.app.myProjectCollection.get(i).name);
				stringArray[i+1]= params.app.myProjectCollection.get(i).name;
		
		}
		
//String[] stringArray = new String[] { "none", "nafplio_1","argolida","eds","nomoi_okxe","koutsoumpos"};
		
		ArrayAdapter modeAdapterProject = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		modeAdapterProject.setDropDownViewResource(R.layout.list_item); // The drop down view
		//ArrayAdapter modeAdapterProject = new ArrayAdapter<String>(params.app, android.R.layout.simple_spinner_item, stringArray);
		//modeAdapterProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_list_item_2, stringArray);
		//lv_tools.setAdapter(modeAdapter);
		
		
		spinnerProject.setAdapter(modeAdapterProject);
		
		
		
		
		spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            
			@Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               
				int i;
				//for(i=0;i<=parentView.getChildCount()-1;i++){
					//((TextView)parentView.getChildAt(i)).setTextColor(Color.rgb(255, 255, 255));   
				//}
				
				((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(249, 249, 249)); 
            	setActiveProject(position);
            	if(position>0){
            		get_params().msets.clear();
            		get_params().app.dbHelper.loadStasiLinkFromDb();
            		
            		get_params().activeProject.updateStasiIcon();
            		
            		
	            	get_params().app.dbHelper.loadPeriodoiFromDb	();
	            	get_params().app.dbHelper.loadMetriseisFromDb	();
	            	get_params().app.dbHelper.loadLinesFromDb		();
	            	//get_params().logData();
	            	//get_params().odefsi.set_dist_set();
	            	get_params().odefsi.redraw(true);
	            	//get_params().odefsi.draw_tax();
	            	
	            	get_params().surface.requestRender();
	            	//get_params().activeProject.sendToMail();
	            	//get_params().activeProject.sendToMailCR1();
            	}
            	//Log.i("project index","ii : " + position);
            	
            	// Log.i("sel", ((TextView)parentView.getChildAt(0)).getText().toString());
                /* 
                 String fname = ((TextView)parentView.getChildAt(0)).getText().toString();
                 
                 File myfile = new File( get_base_dir() + "/vector/" + fname + ".kml");
                // Log.i("eee",myfile.exists() + "-");
					//File[] files = directory.listFiles();
					//MyKml kml = new MyKml(Environment.getExternalStorageDirectory().getAbsolutePath() + "/geoCloudCache/vector/" + files[index].getparams);
				if(myfile.exists()){	
                 MyKml kml = new MyKml( get_base_dir() + "/vector/" + fname + ".kml" ,get_params());
				}else{
					clearKml();
				}*/
					
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		tmp = new RelativeLayout.LayoutParams(280,120);
		tmp.leftMargin=20;
		tmp.topMargin=280;
		this.layout.addView(spinnerProject,tmp);
		
		
		
		
		
		
		
		
		
		allow_download = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=10;
		this.layout.addView(allow_download,tmp);
		
		allow_download.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				set_allow_download(isChecked);
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
		//TextView textView1 = new TextView(params.app.getBaseContext());	textView1.setText("Download");
		//this.layout.addView(textView1, new MyRelativeLayoutParams(150,40,80,20));
		
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("Download");
		//textView1.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=110;
		tmp.topMargin=10;
		this.layout.addView(textView1,tmp);
		
		
		
		
		
		

		show_map = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		show_map.setChecked(false);
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=90;
		this.layout.addView(show_map,tmp);
		//set_show_map(false);
		show_map.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				//set_allow_download(isChecked);
				// TODO Auto-generated method stub
				set_show_map(isChecked);
				get_params().surface.requestRender();
				
			}
			
		});
		
		
		
		//TextView textView1 = new TextView(params.app.getBaseContext());	textView1.setText("Download");
		//this.layout.addView(textView1, new MyRelativeLayoutParams(150,40,80,20));
		
		textView2 = new TextView(params.app.getBaseContext());	textView2.setText("Show Map");
		//textView1.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=110;
		tmp.topMargin=95;
		this.layout.addView(textView2,tmp);
		
		
		
		
		
		
		
		
		
		

		
		data_cast = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=545;
		this.layout.addView(data_cast,tmp);
		
		data_cast.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				//set_allow_download(isChecked);
				// TODO Auto-generated method stub
				get_params().datacast.set(isChecked);
				
			}
			
		});
		
		
		
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("Data Cast");
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=110;
		tmp.topMargin=550;
		this.layout.addView(textView3,tmp);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		drawLine = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		drawLine.setChecked(true);
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=615;
		this.layout.addView(drawLine,tmp);
		
		drawLine.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
						get_params().renderer	.my_poly.drawLayer = isChecked;
						get_params().surface	.requestRender();
			}
			
		});
		
		
	
		textView4 = params.util.addTextView(this.layout, 
										110, 620, 
										300, 50, 
										"draw lines", 
										Color.argb(255,255, 255, 255),
										15f);	
	
		
		
		
		
		
		
		
		
		

		
		drawKml = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		drawKml.setChecked(true);
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=685;
		this.layout.addView(drawKml,tmp);
		
		drawKml.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				get_params().renderer	.kml_poly.drawLayer = isChecked;
				get_params().renderer	.kml_points.drawLayer = isChecked;
				get_params().surface	.requestRender();
			}
			
		});
		
		
	
		textView5 = params.util.addTextView(this.layout, 
										110, 690, 
										300, 50, 
										"draw Kml", 
										Color.argb(255,255, 255, 255),
										15f);	
	
		
		
		
		
		
		
		
		
	
		
		

		
		drawTax = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
		drawTax.setChecked(true);
		tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=10;
		tmp.topMargin=755;
		this.layout.addView(drawTax,tmp);
		
		drawTax.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
						get_params().renderer	.points.drawLayer = isChecked;
						get_params().surface	.requestRender();
			}
			
		});
		
		
	
		textView6 = params.util.addTextView(this.layout, 
										110, 760, 
										300, 50, 
										"draw Tax", 
										Color.argb(255,255, 255, 255),
										15f);	
	
		
		
		
		
		
		
		
		/*
		textView4 = new TextView(params.app.getBaseContext());	textView4.setText("draw lines");
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=110;
		tmp.topMargin=615;
		this.layout.addView(textView4,tmp);
		*/
		
		
		/*
		
		textViewClose = new TextView(params.app.getBaseContext());	
		textViewClose.setText("EXIT");
		textViewClose.setTextColor(Color.argb(190,150, 0, 0));
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 50);
		tmp.leftMargin=120;
		tmp.topMargin=10;
		this.layout.addView(textViewClose,tmp);
		
		
		
		textViewClose.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("aa",curOpenStasiIndex + "-" + curSkopefsiIndex);
				if(curOpenStasiIndex>curSkopefsiIndex){
					highlightStasi(curOpenStasiIndex, false);
					highlightStasi(curSkopefsiIndex, false);
				}else{
					highlightStasi(curSkopefsiIndex, false);
					highlightStasi(curOpenStasiIndex, false);
					
				
				}
				
			
				hide();
			}
		}
		);
		
		*/
		
		
		
		
		
		
		
		
		
		/*
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("???????  ");
		textView1.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textView1,tmp);*/
		/*
		textViewName = new TextView(params.app.getBaseContext());	textViewName.setText("");
		textViewName.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textViewName,tmp);
		
		
		
		
		
		
		textView2 = new TextView(params.app.getBaseContext());	textView2.setText("??  ");
		textView2.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=50;
		this.layout.addView(textView2,tmp);
		
		textViewHeight = new TextView(params.app.getBaseContext());	textViewHeight.setText("");
		textViewHeight.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=120;
		tmp.topMargin=50;
		this.layout.addView(textViewHeight,tmp);
		
		
		
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("X  ");
		textView3.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=80;
		this.layout.addView(textView3,tmp);
		
		textViewX = new TextView(params.app.getBaseContext());	textViewX.setText("");
		textViewX.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=120;
		tmp.topMargin=80;
		this.layout.addView(textViewX,tmp);
		
		
		
		textView4 = new TextView(params.app.getBaseContext());	textView4.setText("Y  ");
		textView4.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=110;
		this.layout.addView(textView4,tmp);
		
		textViewY = new TextView(params.app.getBaseContext());	textViewY.setText("");
		textViewY.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=120;
		tmp.topMargin=110;
		this.layout.addView(textViewY,tmp);
		
		*/
		
		/*
		
		editText1 = new EditText(params.app.getBaseContext());	editText1.setText("ST 1");editText1.setTextColor(Color.argb(150,100, 0, 0));
		tmp = new RelativeLayout.LayoutParams(120, 100);
		tmp.leftMargin=250;
		tmp.topMargin=50;
		//editText1.setTextSize(2.4f);
		this.layout.addView(editText1,tmp);
		this.layout.setLeft(10);*/
		
		
		
		
		
		
		
	
			
		spinnerTools = new Spinner(params.app.getBaseContext());
		String[] stringArray3 = {"","export CR1","export odefsi","syncToCloud","export Staseis","export kml","export dxf","sync Staseis","sync Period","sync Measurements","clear Project","clear All"};
		
		ArrayAdapter modeAdapter3 = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray3);
		modeAdapter3.setDropDownViewResource(R.layout.list_item); // The drop down view
			
		spinnerTools.setAdapter(modeAdapter3);
		
		spinnerTools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(249, 249, 249));   
               
				if(position==1){
					
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("export CR1 measurement file ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().activeProject.sendToMailCR1();
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
					
					
					
				}
				
				if(position==2){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("export odefsi file ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().activeProject.sendToMailOdefsi();
					      } });
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					b.show();
				}
				
				
				if(position==3){
					
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("synchronize to Cloud ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().activeProject.syncToCloud();
					    	 
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
					
								}
				
				
				if(position==4){
					
					
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("export local Staseis file ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().activeProject.sendToMailStaseis();
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
					
					
					
					
				}
				if(position==5){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("Export kml ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					         //Log.i("alert","yes pressed");
					    	  get_params().activeProject.sendToMailKML();
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
					    	 
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				if(position==6){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("Export dxf ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					         //Log.i("alert","yes pressed");
					    	  get_params().activeProject.sendToMailDXF();
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				if(position==7){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("synchronize Staseis ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	 // get_params().activeProject.clear();
					    	 // get_params().activeProject.syncStaseisToCloud(false);
					    	  get_params().activeProject.syncStaseis();
					    	  
					    	 // Intent intent = get_params().app.getIntent();
					    	//  get_params().app.finish();
					    	//  get_params().app.startActivity(intent);
					    	  //Log.i("alert","yes pressed");
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				
				
				

				if(position==8){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("synchronize Periods ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  //get_params().activeProject.syncPeriodoiToCloud();
					    	  get_params().activeProject.syncPeriodoi();
						    	// Intent intent = get_params().app.getIntent();
					    	 // get_params().app.finish();
					    	 // get_params().app.startActivity(intent);
					    	  //Log.i("alert","yes pressed");
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				
				if(position==9){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("synchronize Measurements ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  //get_params().activeProject.syncPeriodoiToCloud();
					    	  get_params().activeProject.syncMetriseis();
						    	// Intent intent = get_params().app.getIntent();
					    	 // get_params().app.finish();
					    	 // get_params().app.startActivity(intent);
					    	  //Log.i("alert","yes pressed");
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				if(position==10){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("Clear Project ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().activeProject.clear();
					    	  Intent intent = get_params().app.getIntent();
					    	  get_params().app.finish();
					    	  get_params().app.startActivity(intent);
					    	  //Log.i("alert","yes pressed");
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
				if(position==11){
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("Clear All ?");
					b.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  get_params().app.dbHelper.clearAll();
					    	  Intent intent = get_params().app.getIntent();
					    	  get_params().app.finish();
					    	  get_params().app.startActivity(intent);
					    	  //Log.i("alert","yes pressed");
					      } });
					
					
					b.setNegativeButton(android.R.string.no,  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
	
				}
				
				
            }

            
            
            
            
            
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		tmp = new RelativeLayout.LayoutParams(280,120);
		tmp.leftMargin=20;
		tmp.topMargin=400;
		this.layout.addView(spinnerTools,tmp);
		
		
		
		
		
		
		
		open=false;
	}
	
	private void clearKml(){
		try{
			params.renderer.kml_poly.clear();
		}catch(Exception ex){
			
		}
		
		
	}
	private void set_allow_download(boolean value){
		params.allow_download=value;
	}
	
	
	private void setActiveProject(int index){
		if(index>0) params.app.myProjectCollection.get(index-1).resetActive();
	}
	
	private String get_base_dir(){
		return params.app.baseDir;
	}
	
	public MyParams get_params(){
		return params;
	}
	
	
	private void set_show_map(boolean value){
		params.show_map=value;
		if(value){
			
		}else{
			params.app.grid.clear();
		}
	}
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		
		tmp.leftMargin=params.imagePixelWidth-20-250-50;
		tmp.topMargin=120;
		layout.setLayoutParams(tmp);
		open=true;
		
            	//params.app.openOptionsMenu();
        
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
		open=false;
		//params.window_tools.hide();
	}
	
	public void toggle(){
		if(open) {hide();}else{show();};
		//params.window_tools.hide();
	}
	
	public void highlightStasi(int index,boolean value){
		params.staseis.highlightStasi(index, value);
	}
	
	
	
public  void meas_all() throws InterruptedException{
		
		
		
		params.app.vib.vibrate(100);
		String out = "Ee 0100,0,   1.600, 0, 91.2875, 26.4356,E115";
		Log.i("-" + params.window_stasi.curSkopefsiIndex , out);
		params.msets.get(params.msets.size()-1).addMeasurementFormString(out,params.window_stasi.curSkopefsiIndex);
		
		/*
		params.renderer.poly.add();
		params.renderer.poly.lastitem.addVertice((float) coor1[0], (float) coor1[0]);
		params.renderer.poly.lastitem.addVertice((float) coor2[0], (float) coor2[0]);
		params.renderer.poly.regen_coor();
    	
    	*/
    	
    	
	}


}
