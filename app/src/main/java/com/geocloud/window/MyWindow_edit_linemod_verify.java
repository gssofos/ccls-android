package com.geocloud.window;

import java.io.File;

import com.example.test2.R;
import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
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

public class MyWindow_edit_linemod_verify {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private ListView lv_poly;
	//public EditText editText1;
	private boolean open = false;
	private double tmpInn;   //parametros pou mpainei apo to show gia na xero poso extend kano
	
	
	
	
	
	//public boolean add_mode = false;
	//public boolean add_mode_p1_set = false;
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_edit_linemod_verify(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(300, 190);
		this.layout_params.topMargin=-1200;
		
		RelativeLayout.LayoutParams tmp;
		
		
		
		
		lv_poly = new ListView(params.app.getBaseContext());
		lv_poly.setBackgroundColor(Color.GRAY);
		String[] stringArray = new String[] { "Accept","Cancel" };
		
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, stringArray);
		//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android. R.layout.simple_list_item_2, stringArray);
		lv_poly.setAdapter(modeAdapter);
		
		lv_poly.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//arg1.setBackgroundColor();
				//arg1.setSelected(true);;
				//arg0.getChildAt(0).setSelected(true);
				
				//lv_poly.notifyAll();
				MyParams tmp = get_params();
				
				if(arg2==0){
					
					
					
	       			tmp.event.clearSelectePoints();
	       			
	       			
					if(tmp.mode.edit_line_mode==1){
							tmp.renderer.my_poly.remove_last();
							tmp.event.selectedLineObject.extend(tmpInn);
							MyPoint p1 = tmp.event.selectedLineObject.getVertice(0);
			       			MyPoint p2 = tmp.event.selectedLineObject.getVertice(1);
							double[] mc = new double[4];mc[0]=p1.x;mc[1]=p1.y;mc[2]=p2.x;mc[3]=p2.y;
			       			tmp.app.dbHelper.replaceLineToDb(tmp.event.selectedLineObject ,0, 0, 0, 0, mc,tmp.event.selectedLineObject._id);
			       			
			       			
					}
					if(tmp.mode.edit_line_mode==3){
							tmp.renderer.my_poly.lastPoly.setColor(tmp.event.selectedLineObject.r, tmp.event.selectedLineObject.g,tmp.event.selectedLineObject.b);
						//tmp.event.selectedLineObject.offset(tmpInn);
							MyPoint p1 = tmp.renderer.my_poly.lastPoly.getVertice(0);
			       			MyPoint p2 = tmp.renderer.my_poly.lastPoly.getVertice(1);
							double[] mc = new double[4];mc[0]=p1.x;mc[1]=p1.y;mc[2]=p2.x;mc[3]=p2.y;
			       			tmp.app.dbHelper.addLineToDb(tmp.renderer.my_poly.lastPoly ,0, 0, 0, 0, mc);
			       			
			       			
					}
					
					if(tmp.mode.edit_line_mode==4){
						tmp.renderer.my_poly.lastPoly.setColor(0f, 0f,0f);
						tmp.renderer.my_poly.lastPoly.flag=0;
					//tmp.event.selectedLineObject.offset(tmpInn);
						MyPoint p1 = tmp.renderer.my_poly.lastPoly.getVertice(0);
		       			MyPoint p2 = tmp.renderer.my_poly.lastPoly.getVertice(1);
						double[] mc = new double[4];mc[0]=p1.x;mc[1]=p1.y;mc[2]=p2.x;mc[3]=p2.y;
		       			tmp.app.dbHelper.addLineToDb(tmp.renderer.my_poly.lastPoly ,0, 0, 0, 0, mc);
		       			
		       			
				}
					
					
					tmp.renderer.my_poly.reset_color();
	       			tmp.renderer.my_poly.regen_coor(tmp.renderer.my_poly.item.indexOf(tmp.event.selectedLineObject));
	       			tmp.mode.edit_line_mode=0;
					hide();
										
								
									}
					if(arg2==1){
						tmp.event.clearSelectePoints();
						
						tmp.renderer.my_poly.remove_last();
						tmp.event.selectedLineObject.restoreColor();
						tmp.renderer.my_poly.regen_coor_silent();
						tmp.mode.edit_line_mode=0;
						hide();
						
					}
					
				
				
			}
		}
		);
		
		this.layout.addView(lv_poly);
				
		
		open=false;
	}
	
	
	
	private String get_base_dir(){
		return params.app.baseDir;
	}
	
	private MyParams get_params(){
		return params;
	}
	
	
	public void show(int x, int y, double inn){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		
		tmpInn=inn;
		
		tmp.leftMargin=x;
		tmp.topMargin=y;
		layout.setLayoutParams(tmp);
		
		
		open=true;
	}
	
	public void show(int x, int y){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		
		tmp.leftMargin=x;
		tmp.topMargin=y;
		layout.setLayoutParams(tmp);
		
		
		open=true;
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
		
		open=false;
		//params.window_tools.hide();
	}
	
	


}
