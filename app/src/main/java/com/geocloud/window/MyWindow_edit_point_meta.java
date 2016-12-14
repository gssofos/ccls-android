package com.geocloud.window;

import java.io.File;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyKml;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyStasi;

import android.R.color;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
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

public class MyWindow_edit_point_meta {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private int width,height;
	private boolean open = false;
	
	public Spinner spinnerType;
	public TextView t1;
	public EditText et1;
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_edit_point_meta(MyParams params){
		this.params = 	params;
		this.layout = 	new RelativeLayout(params.app.getBaseContext());	
		this.layout.setBackgroundColor(Color.argb(255, 44,44,44));
		this.height = 	290;
		this.width	=	320;
		
		this.layout_params 			= 	new RelativeLayout.LayoutParams(width, height);
		this.layout_params.topMargin=	-1200;
		
		RelativeLayout.LayoutParams tmp;
		
		
		

		
		
		
		
		spinnerType = new Spinner(params.app.getBaseContext());
		String[] stringArray = new String[] { "none","?????","???","???�","??????","????","??????" };;
		//ArrayAdapter<String> modeAdapterProject = new ArrayAdapter<String>(params.app, R.layout.simpe_selectable_list_item, stringArray);
		//modeAdapterProject.setDropDownViewResource(R.layout.simpe_selectable_list_item); // The drop down view
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
				if(get_params().event.pointSelected){
					get_params().event.selectedPointObject.metrisi.obtype=position;
					get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
				}else{
					get_params().debug("no selected point");
				}
				/*afto giati iparxei to select trigger sto show tis klasis*/
				//if(get_params().event.lineSelected) {
							//get_params().event.selectedLineObject.meta.type = position;
							//get_params().event.selectedLineObject.updateMeta();
							if(position==0){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 255f);
			            	}
							if(position==1){
							//	get_params().event.selectedLineObject.setColor(0f, 255f, 255f);
			            	}
							if(position==2){
							//	get_params().event.selectedLineObject.setColor(255f, 255f, 0f);
			            	}
							
							
							//get_params().renderer.my_poly.reset_color();
				//}
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
            
		tmp = new RelativeLayout.LayoutParams(width-40,80);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(spinnerType,tmp);
		

		
		
		
		
		
		
		/*
		
		
		t1 = new TextView(params.app.getBaseContext());	t1.setText("??????");
		t1.setTextColor(Color.argb(210,255, 255,255));
		t1.setTextSize(2, 14f);
		tmp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
		tmp.leftMargin=40;
		tmp.topMargin=120;
		this.layout.addView(t1,tmp);
		
		*/
		
		et1 = new EditText(params.app.getBaseContext());	//et1.setText("??????");
		//et1.setText("sdsdsdsd");
		et1.setBackgroundColor(Color.argb(210,255, 255,255));
		et1.setGravity(Gravity.TOP);
		et1.setTextColor(Color.argb(210,0, 0, 0));
		et1.setTextSize(2, 14f);
		tmp = new RelativeLayout.LayoutParams(width-40, 150);
		tmp.leftMargin=20;
		tmp.topMargin=110;
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
	
	
	
	
	private MyParams get_params(){
		return params;
	}
	
	
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		//tmp.leftMargin=(params.imagePixelWidth-width);
		//tmp.topMargin=(550);
		tmp.leftMargin=420;
		tmp.topMargin=(120);
		layout.setLayoutParams(tmp);
		open=true;
		
		
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
				get_params().event.selectedPointObject.metrisi.sxolia=et1.getText().toString();
				get_params().event.selectedPointObject.metrisi.updateMeta(get_params());
				Log.i("saved",get_params().event.selectedPointObject.metrisi.sxolia);
			
		//}
		}
		open=false;
		//params.window_tools.hide();
	}
	
	


}
