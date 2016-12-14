package com.geocloud.window;

import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyMeasurement;
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
import android.widget.Toast;

public class MyWindow_set_Back_Orientation {
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
	public TextView textViewY;
	private TextView textViewClose;
	//public EditText editText1;
	private boolean open = false;
	
	public int curOpenStasiIndex;
	public int curSkopefsiIndex;
	
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_set_Back_Orientation(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(255,255,255, 255));
		this.layout.setBackgroundColor(Color.argb(255,253,237,64));
		this.layout_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);
		this.layout_params.leftMargin=120;
		this.layout_params.topMargin=10;
		
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 50);
		
		
		/*
		textViewClose = new TextView(params.app.getBaseContext());	
		textViewClose.setText("EXIT");
		textViewClose.setTextColor(Color.argb(190,255, 255, 255));
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 50);
		//tmp.leftMargin=120;
		//tmp.topMargin=10;
		this.layout.addView(textViewClose,tmp);
		
		
		
		textViewClose.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				highlightStasi(curOpenStasiIndex, false);
				hide();
			}
		}
		);
		
		*/
		
		
		
		
		
		
		
		
		
		
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("Set Back Point");
		textView1.setTextColor(Color.argb(210,0, 0, 0));
		textView1.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
		tmp.leftMargin=20;
		tmp.topMargin=20;
		this.layout.addView(textView1,tmp);
		
		textView1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//highlightStasi(curOpenStasiIndex, false);
				//hide();
				
				
				try {
					//meas_all();
					meas_angle();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		);


		
		
		
		
		textView4 = new TextView(params.app.getBaseContext());	textView4.setText("Cancel");
		textView4.setTextColor(Color.argb(210,0, 0, 0));
		textView4.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(150, 80);
		tmp.leftMargin=20;
		tmp.topMargin=-100;
		this.layout.addView(textView4,tmp);
		
		textView4.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//highlightStasi(curOpenStasiIndex, false);
				//hide();
				
					//getParams().readCommCancelFlag=true;
					setCancel(false);
			}
		}
		);
		
		
		
		/*
		textView2 = new TextView(params.app.getBaseContext());	textView2.setText("???????");
		textView2.setTextColor(Color.argb(210,0, 0, 0));
		textView2.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(300, 80);
		tmp.leftMargin=120;
		tmp.topMargin=20;
		this.layout.addView(textView2,tmp);
		
		
		
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("?????");
		textView3.setTextColor(Color.argb(210,0, 0, 0));
		textView3.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(300, 80);
		tmp.leftMargin=330;
		tmp.topMargin=20;
		this.layout.addView(textView3,tmp);
		
textView3.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//highlightStasi(curOpenStasiIndex, false);
				//hide();
				try {
					meas_angle();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		);*/
		
		/*
		textViewName = new TextView(params.app.getBaseContext());	textViewName.setText("");
		textViewName.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textViewName,tmp);
		
		
		
		
		
		
		textView2 = new TextView(params.app.getBaseContext());	textView2.setText("��  ");
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
		open=false;
	}
	
	public void show(int stasiIndex){
		if(params.staseis.item.size()>0){
			MyStasi tmp2 = params.staseis.item.get(this.curOpenStasiIndex);
			tmp2.highlight(false);
			//params.renderer.odefsi_poly.clear();
			}
		
		
		
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=0;
		tmp.topMargin=0;
		layout.setLayoutParams(tmp);
		open=true;
		this.curOpenStasiIndex=stasiIndex;
		
		MyStasi tmp2 = params.staseis.item.get(this.curOpenStasiIndex);
		textView1.setText("SET  \"" + tmp2.name + "\"  ");
		
		
		MyStasi tmp3 = params.staseis.item.get(params.window_stasi.curOpenStasiIndex);
		
		
		MyPoly mp = params.renderer.odefsi_poly.add(1f,0f,0f);
		mp.addVertice(tmp3.point.x, tmp3.point.y);
		mp.addVertice(tmp2.point.x, tmp2.point.y);
		params.renderer.odefsi_poly.regen_coor();
		
		
	}
	
	public void hide(boolean resetPoly){
		params.debug("sss");
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=50;
		tmp.topMargin=-220;
		layout.setLayoutParams(tmp);
		open=false;
		
		if(params.staseis.item.size()>0){
		MyStasi tmp2 = params.staseis.item.get(this.curOpenStasiIndex);
		tmp2.highlight(false);
		
		if(resetPoly){
			params.renderer.odefsi_poly.clear();
		}
		}
		
	}
	
	public void highlightStasi(int index,boolean value){
		params.staseis.highlightStasi(index, value);
		
		
	}
	
	public  void meyas_all() throws InterruptedException{
		
		
		//params.app.prolific_write("Ea");
		//params.app.vib.vibrate(100);
		//params.app.prolific_read();
		
		/*
		double[] coor1 = params.staseis.getCoorOfIndex(params.window_stasi.curOpenStasiIndex);
		double[] coor2 = params.staseis.getCoorOfIndex(params.window_stasi.curSkopefsiIndex);
	
		MyPoly tmp = params.renderer.poly.add();
		//params.debug(coor1[0]+"," +coor1[1]);
		Log.i(coor1[0]+"," +coor1[1],coor2[0]+"," +coor2[1]);
		
		tmp.addVertice((float) coor1[0], (float) coor1[1]);
		tmp.addVertice((float) coor2[0], (float) coor2[1]);
		tmp.regen_coor();
		params.renderer.poly.regen_coor();
		
		
		*/
		
		
		//params.app.vib.vibrate(100);
		//String out = "Ea 0100,0,  1.600, 0, 0.303,91.2875,26.4358";
		
		//Log.i("-" + params.window_stasi.curSkopefsiIndex , out);
		//params.renderer.msets.get(params.renderer.msets.size()-1).addMeasurementFormString(out,params.window_stasi.curSkopefsiIndex);
		
		/*
		params.renderer.poly.add();
		params.renderer.poly.lastitem.addVertice((float) coor1[0], (float) coor1[0]);
		params.renderer.poly.lastitem.addVertice((float) coor2[0], (float) coor2[0]);
		params.renderer.poly.regen_coor();
    	
    	*/
    	
    	
	}
	public  void meas_angle() throws InterruptedException{
		//params.app.prolific_write("Ee");
		//params.app.vib.vibrate(100);
		
		
		//String out = "Ee 0100,0,   1.600, 0, 91.2875, 26.4356,E115";
		
		//String out = "Ea 0100,0,  1.600, 0, 0.303,91.2875,26.4358";
		//Log.i("-" + params.window_stasi.curSkopefsiIndex , out);
		
		
		//params.app.prolific_write("Ee");
		//params.app.prolific_read_with_callback(1);
		
		
		//if(params.tscomm.writeCommand("Ee")){
		
			if(params.tscomm.writeCommand("angle")){
				params.debug("1111");;	
				setCancel(true);
				//params.debug("22");;
					params.app.vib.vibrate(100);
					params.tscomm.readCommandWithCallback(1,false);
		}else{
			Toast.makeText(params.app, "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		
		
		//params.renderer.msets.get(params.renderer.msets.size()-1).addMeasurementFormString(out,params.window_stasi.curSkopefsiIndex);
		
		//params.app.prolific_read();
	}
	
	public void setBSCallback(String data){
		if(data!=""){
			Log.i(" data",data);
		MyMeasurement tmp = new MyMeasurement();
		tmp.fromMsg(data);
		params.debug(data);;
		params.window_yo.show(tmp.hZ);
		}else{
			Log.i("setBSCallback","null data");
		}
		
		setCancel(false);
	}
	
	
	
	
	
	
	

	
	private MyParams getParams(){
		return params;
	}
	
	

	private void  setCancel(boolean value){
		
		if(value){
			RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) textView1.getLayoutParams();
			tmp.topMargin=-100;
			textView1.setLayoutParams(tmp);
		
			
			tmp = (android.widget.RelativeLayout.LayoutParams) textView4.getLayoutParams();
			tmp.topMargin=20;
			textView4.setLayoutParams(tmp);
			
			params.readCommCancelFlag=false;
		}else{
			RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) textView1.getLayoutParams();
			tmp.topMargin=20;
			textView1.setLayoutParams(tmp);
		
			
			
			tmp = (android.widget.RelativeLayout.LayoutParams) textView4.getLayoutParams();
			tmp.topMargin=-100;
			textView4.setLayoutParams(tmp);
			
			
			params.readCommCancelFlag=true;
		}
		
		
	}
	
}
