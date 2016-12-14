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

public class MyWindow_edit_lineadd {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private ListView lv_poly;
	//public EditText editText1;
	private boolean open = false;
	
	
	
	
	
	
	//public boolean add_mode = false;
	//public boolean add_mode_p1_set = false;
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_edit_lineadd(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(300, 190);
		this.layout_params.topMargin=-1200;
		
		RelativeLayout.LayoutParams tmp;
		
		
		
		
		lv_poly = new ListView(params.app.getBaseContext());
		lv_poly.setBackgroundColor(Color.GRAY);
		String[] stringArray = new String[] { "Accept","Close" };
		
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
				tmp.event.clearSelectePoints();
				if(arg2==0){
				if(get_params().window_edit.add_mode && !get_params().window_edit.add_mode_p1_set){
					get_params().window_edit.add_mode_p1_set=true;
					//tt4.setText("Next point");;
				}else	if(get_params().window_edit.add_mode && get_params().window_edit.add_mode_p1_set){
					
					
					tmp.renderer.my_poly.lastPoly.setFlag(0);
					get_params().window_edit.add_mode_p1_set=true;
					
					
					
					Log.i("asasas","itemclick");
					
					
					double[] mc = new double[4];mc[0]=tmp.event.selectedx;mc[1]=tmp.event.selectedy;mc[2]=tmp.event.selected2x;mc[3]=tmp.event.selected2y;
					tmp.app.dbHelper.addLineToDb(tmp.renderer.my_poly.lastPoly,0, 0, 0, 0, mc);
					
					
					tmp.web.httpCall(
							"http://eds.culture.gr/_geo/php/_add_line.php?x1=" +tmp.event.selectedx 
									+ "&y1=" 	+ tmp.event.selectedy 
									+ "&x2=" 	+ tmp.event.selected2x
									+ "&y2=" 	+ tmp.event.selected2y 
									+ "&id=" 	+ tmp.renderer.my_poly.lastPoly._id 
									+ "&p=" 	+ tmp.activeProject._id);
					
					
					
					
					
					
					tmp.event.selectedx=tmp.event.selected2x;
					tmp.event.selectedy=tmp.event.selected2y;
					//tmp.renderer.pointHighlight.removeByFlag(1);
					//tt4.setText("Next point");;
				}
				
					hide();
										
								
									}
					if(arg2==1){
					tmp.renderer.my_poly.remove_last();
					Log.i("asasas","item2click");
						get_params().window_edit.setAddMode(!get_params().window_edit.add_mode);
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
