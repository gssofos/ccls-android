package com.geocloud.window;

import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyStasi;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyWindow_msg {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private TextView textView1;
	private TextView textViewClose;
	//public EditText editText1;
	private boolean open = false;
	
	public int curOpenStasiIndex;
	public int curSkopefsiIndex;
	
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_msg(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(255,255,255, 255));
		this.layout_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
		this.layout_params.leftMargin=120;
		this.layout_params.topMargin=-9910;
		
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 50);
		
	
		
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("");
		textView1.setTextColor(Color.argb(210,0, 0, 0));
		textView1.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textView1,tmp);
		
		textView1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//highlightStasi(curOpenStasiIndex, false);
				//hide();
				try {
					//meas_all();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		);


		
		
		
		textViewClose = new TextView(params.app.getBaseContext());	
		textViewClose.setText("Cancel"/* [x]"*/);
		textViewClose.setTextColor(Color.argb(190,150, 0, 0));
		textViewClose.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(220, 80);
		tmp.leftMargin=params.imagePixelWidth-150-90;
		tmp.topMargin=10;
		
		this.layout.addView(textViewClose,tmp);
		
		
		
		textViewClose.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.i("aa",curOpenStasiIndex + "-" + curSkopefsiIndex);
				//if(curOpenStasiIndex>curSkopefsiIndex){
					//highlightStasi(curOpenStasiIndex, false);
					//highlightStasi(curSkopefsiIndex, false);
				//}else{
					//highlightStasi(curSkopefsiIndex, false);
					//highlightStasi(curOpenStasiIndex, false);
					
				
				//}
				//
				
				MyParams tmp = 		getParams();
				tmp.debug("textViewClose");;
				tmp.mode.set_stasi_explore();
				hide();
				tmp.staseis.item.get(tmp.window_stasi.curOpenStasiIndex).highlight(false);
				tmp.window_stasi.hide();
			}
		}
		);
		
		
		
		
		open=false;
	}
	
	public void show(String msg){
		
		
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=0;
		tmp.topMargin=0;
		layout.setLayoutParams(tmp);
		open=true;


		textView1.setText(msg);
		
		
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=50;
		tmp.topMargin=-220;
		layout.setLayoutParams(tmp);
		open=false;
		
	}
	public MyParams getParams(){
		return params;
	}
	
	
	
}
