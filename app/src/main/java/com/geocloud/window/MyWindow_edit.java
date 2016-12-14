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

public class MyWindow_edit {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private ListView lv_poly;
	//public EditText editText1;
	private boolean open = false;
	
	
	
	
	
	
	public boolean add_mode = false;
	public boolean add_mode_p1_set = false;
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_edit(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(600, 190);
		this.layout_params.topMargin=-1200;
		
		RelativeLayout.LayoutParams tmp;
		
		
		
		
		lv_poly = new ListView(params.app.getBaseContext());
		
		String[] stringArray = new String[] { "Delete Last", "Add"};
		
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
				
					/*if(arg2==0){
										
								if(tmp.event.lineSelected){
									
									tmp.web.httpCall(
											"http://eds.culture.gr/_geo/php/_remove_line.php?id=" +tmp.event.selectedLineObject._id 
													+ "&p=" 	+ tmp.activeProject._id);
									
									
									
									tmp.app.dbHelper.removeLineFromDb(tmp.event.selectedLineObject._id);
									tmp.renderer.my_poly.item.remove(tmp.event.selectedLineObject);
									tmp.renderer.my_poly.regen_coor();
								}
									}*/
					if(arg2==1){
						
						setAddMode(!add_mode);
						
						tmp.event.clearSelectePoints();
						
						//tt.setTextColor(Color.RED);
						//lv_poly.setSelected(true);
					}
					//if(arg2==2){
					/*TextView tt4 = (TextView) lv_poly.getChildAt(3);
					if(add_mode && !add_mode_p1_set){
						add_mode_p1_set=true;
						tt4.setText("Next point");;
					}else	if(add_mode && add_mode_p1_set){
						
						
						tmp.renderer.my_poly.lastPoly.setFlag(0);
						add_mode_p1_set=true;
						
						
						
						
						
						
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
						tt4.setText("Next point");;
					}*/
				//}
				//if(arg2==2){
				//	hide();
					//get_params().mode.set_edit_line(false);
				//}
				if(arg2==0){
					tmp.app.dbHelper.removeLineFromDb(tmp.renderer.my_poly.lastPoly._id);
					
					tmp.renderer.my_poly.remove_last();
				}
				// TODO Auto-generated method stub
				//if(arg2==4) ll_poly_tools.removeView(lv_poly);
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
		
		this.layout.addView(lv_poly);
				
		
		open=false;
	}
	
	
	
	private String get_base_dir(){
		return params.app.baseDir;
	}
	
	private MyParams get_params(){
		return params;
	}
	
	
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		
		
		tmp.leftMargin=params.imagePixelWidth-20-250-50;
		tmp.topMargin=120;
		layout.setLayoutParams(tmp);
		
		setAddMode(false);
		open=true;
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
		setAddMode(false);
		open=false;
		get_params().mode.set_edit_line(false);
		get_params().mode.edit_line_mode=0;
		//params.window_tools.hide();
	}
	
	public void toggle(){
		if(open) {hide();}else{show();};
		//params.window_tools.hide();
	}
	
	
	
	public void setAddMode(boolean value){
		add_mode=value;
		add_mode_p1_set=false;
		TextView tt = (TextView) lv_poly.getChildAt(1);
		//TextView tt2 = (TextView) lv_poly.getChildAt(0);
		TextView tt3 = (TextView) lv_poly.getChildAt(0);
		//TextView tt4 = (TextView) lv_poly.getChildAt(3);
		
		
		try {
			if(value){
				
				tt.setBackgroundColor(Color.argb(254, 239, 62, 54));
				//tt4.setTextColor(Color.argb(255, 255, 255, 255));
				//tt2.setTextColor(Color.argb(255, 111, 111, 111));
				tt3.setTextColor(Color.argb(255, 111, 111, 111));
				//tt4.setText("Set Start");;
			}else{
				tt.setBackgroundColor(Color.TRANSPARENT);
				//tt2.setTextColor(Color.argb(255, 255, 255, 255));
				tt3.setTextColor(Color.argb(255, 255, 255, 255));
				//tt4.setTextColor(Color.argb(255, 111, 111, 111));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
