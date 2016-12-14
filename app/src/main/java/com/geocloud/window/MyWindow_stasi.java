package com.geocloud.window;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.MyGLES20.MyRelativeLayoutOutlined;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyStasi;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyWindow_stasi {
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
	private TextView textViewBasePoint;
	private TextView textViewRemoveBasePoint;
	private TextView textViewRemoveStasi;
	private TextView textViewOrientationPoint;
	//public EditText editText1;
	private boolean open = false;
	
	public int curOpenStasiIndex;
	public int curSkopefsiIndex;
	
	
	
	//public long stasi_id ;
	
	
	public MyMeasureSet mset;
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_stasi(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		//this.layout.setBackgroundColor(Color.argb(220,255,255, 255));
		this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(320, 200);
		
		textViewClose = new TextView(params.app.getBaseContext());	
		textViewClose.setText("X"/* [x]"*/);
		textViewClose.setTextColor(Color.argb(255,255, 255, 255));
		textViewClose.setTextSize(2, 20f);
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 80);
		tmp.leftMargin=250;
		tmp.topMargin=0;
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
				
				MyParams tmp = 		getParams();
				tmp.mode.set_stasi_explore();
				hide();
				getParams().window_stasi_skop.hide();
			}
		}
		);
		
		
		
		
		
		
		
		
		
		
		
		textViewBasePoint = new TextView(params.app.getBaseContext());	
		textViewBasePoint.setText("Set Base"/* [x]"*/);
		textViewBasePoint.setTextColor(Color.argb(255,255, 255, 255));
		textViewBasePoint.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(280, 100);
		tmp.leftMargin=20;
		tmp.topMargin=110;
		this.layout.addView(textViewBasePoint,tmp);
		
		
		textViewBasePoint.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("aa",curOpenStasiIndex + "-" + curSkopefsiIndex);
				/*if(curOpenStasiIndex>curSkopefsiIndex){
					highlightStasi(curOpenStasiIndex, false);
					highlightStasi(curSkopefsiIndex, false);
				}else{
					highlightStasi(curSkopefsiIndex, false);
					highlightStasi(curOpenStasiIndex, false);	
				}
				*/
				MyParams tmp = 		getParams();
				//tmp.activeProject.start_stasi=tmp.staseis.item.get(curOpenStasiIndex)._id;
				
				//tmp.app.dbHelper.updateField("project","start_stasi",tmp.activeProject.start_stasi,tmp.activeProject._id);tmp.activeProject.resetActive();
				
				
				tmp.activeProject.updateStasiLinkisbase(tmp.staseis.item.get(curOpenStasiIndex)._id,true);
				tmp.activeProject.updateStasiIcon();
				highlightStasi(curOpenStasiIndex, false);
				hide();
				tmp.window_stasi_skop.hide();
				
				
			}
		}
		);
		
		
		
		
		
		textViewRemoveBasePoint = new TextView(params.app.getBaseContext());	
		textViewRemoveBasePoint.setText("Unset Base"/* [x]"*/);
		textViewRemoveBasePoint.setTextColor(Color.argb(255,255, 255, 255));
		textViewRemoveBasePoint.setTextSize(2, 18f);
		tmp = new RelativeLayout.LayoutParams(280, 80);
		tmp.leftMargin=20;
		tmp.topMargin=130;
		this.layout.addView(textViewRemoveBasePoint,tmp);
		
		
		textViewRemoveBasePoint.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("aa",curOpenStasiIndex + "-" + curSkopefsiIndex);
				/*if(curOpenStasiIndex>curSkopefsiIndex){
					highlightStasi(curOpenStasiIndex, false);
					highlightStasi(curSkopefsiIndex, false);
				}else{
					highlightStasi(curSkopefsiIndex, false);
					highlightStasi(curOpenStasiIndex, false);	
				}
				*/
				MyParams tmp = 		getParams();
				//tmp.activeProject.start_stasi=tmp.staseis.item.get(curOpenStasiIndex)._id;
				
				//tmp.app.dbHelper.updateField("project","start_stasi",tmp.activeProject.start_stasi,tmp.activeProject._id);tmp.activeProject.resetActive();
				
				
				tmp.activeProject.updateStasiLinkisbase(tmp.staseis.item.get(curOpenStasiIndex)._id,false);
				tmp.activeProject.updateStasiIcon();
				highlightStasi(curOpenStasiIndex, false);
				hide();
				tmp.window_stasi_skop.hide();
				
				
			}
		}
		);
		
		
		
		
		
		
		
		
		
		
		textViewRemoveStasi = new TextView(params.app.getBaseContext());	
		textViewRemoveStasi.setText("Remove St *"/* [x]"*/);
		textViewRemoveStasi.setTextColor(Color.argb(255,255, 255, 255));
		textViewRemoveStasi.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(280, 50);
		tmp.leftMargin=-9920;
		tmp.topMargin=130;
		this.layout.addView(textViewRemoveStasi,tmp);
		
		
		textViewRemoveStasi.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//getParams().staseis.item.get(curOpenStasiIndex).remove();
				
				
				
			}
		}
		);
		
		
		
		
		
		
		

		
		
		textViewOrientationPoint = new TextView(params.app.getBaseContext());	
		textViewOrientationPoint.setText("Set Back"/* [x]"*/);
		textViewOrientationPoint.setTextColor(Color.argb(255,255, 255, 255));
		textViewOrientationPoint.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(280, 100);
		tmp.leftMargin=20;
		tmp.topMargin=130;
		this.layout.addView(textViewOrientationPoint,tmp);
		
		
		textViewOrientationPoint.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("aa",curOpenStasiIndex + "-" + curSkopefsiIndex);
				/*if(curOpenStasiIndex>curSkopefsiIndex){
					highlightStasi(curOpenStasiIndex, false);
					highlightStasi(curSkopefsiIndex, false);
				}else{
					highlightStasi(curSkopefsiIndex, false);
					highlightStasi(curOpenStasiIndex, false);	
				}
				*/
				MyParams tmp = 		getParams();
				tmp.mode.set_back_define();
				tmp.window_msg.show("Select Back Sight");
				just_hide();
				tmp.window_stasi_skop.hide();
				//tmp.activeProject.stasi_0=tmp.staseis.item.get(curOpenStasiIndex)._id;
				//tmp.activeProject.resetActive();
				//hide();
			}
		}
		);
		
		
		
		
		
		
		
		
		/*
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("?????  ");
		textView1.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textView1,tmp);*/
		
		textViewName = new TextView(params.app.getBaseContext());	textViewName.setText("");
		textViewName.setTextColor(Color.argb(255,255, 255, 255));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textViewName,tmp);
		
		
		
		
		/*
		
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
		
		*/
		
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("X  ");
		textView3.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=-9950;
		this.layout.addView(textView3,tmp);
		
		textViewX = new TextView(params.app.getBaseContext());	textViewX.setText("");
		textViewX.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=120;
		tmp.topMargin=-9950;
		this.layout.addView(textViewX,tmp);
		
		
		
		textView4 = new TextView(params.app.getBaseContext());	textView4.setText("Y  ");
		textView4.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=-9980;
		this.layout.addView(textView4,tmp);
		
		textViewY = new TextView(params.app.getBaseContext());	textViewY.setText("");
		textViewY.setTextColor(Color.argb(190,0, 0, 0));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=120;
		tmp.topMargin=-9980;
		this.layout.addView(textViewY,tmp);
		
		
		
		/*
		
		editText1 = new EditText(params.app.getBaseContext());	editText1.setText("ST 1");editText1.setTextColor(Color.argb(150,100, 0, 0));
		tmp = new RelativeLayout.LayoutParams(120, 100);
		tmp.leftMargin=250;
		tmp.topMargin=50;
		//editText1.setTextSize(2.4f);
		this.layout.addView(editText1,tmp);
		this.layout.setLeft(10);*/
		
		
		
		
		

		
		MyRelativeLayoutOutlined tmplayout = new MyRelativeLayoutOutlined(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		tmplayout.setBackgroundColor(Color.argb(190,0,0, 0));
		RelativeLayout.LayoutParams tmpl = new RelativeLayout.LayoutParams(200, 80);
		
		tmpl.leftMargin=10;
		tmpl.topMargin=180+100;
		this.layout.addView(tmplayout,tmpl);
		
		
		tmplayout.setGravity(Gravity.CENTER);
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("Hit point");
		tmplayout.addView(textView3);
		
		
		tmplayout.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==event.ACTION_DOWN){
				//v.setBackgroundColor(Color.argb(180,100,120, 0));
					//try {
					//	meas_all();
					//} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						//debug(e.toString());
					//}
				}
				//if(event.getAction()==event.ACTION_UP){
					//v.setBackgroundColor(Color.argb(0,100,120, 0));
					//}
				//	
				
				return false;
			}
			
			
			
		});
		
		///this.layout.addView(tmplayout,tmpl);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		open=false;
	}
	
	public MyParams getParams(){
		return params;
	}
	public void debug(String msg){
		params.debug(msg);;
	}
	
	public void small_size(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.height=130;
		layout.setLayoutParams(tmp);
	
	}
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=20;
		tmp.topMargin=120;
		
		
		if(params.mode.mode==2){
			// An eimai se fasi skopefsis - metrisis
			tmp.height=130;					
			layout.setLayoutParams(tmp);
			open=true;
			
		}else{
			//allios 
			tmp.height=280;
			
				
				
				RelativeLayout.LayoutParams textViewOrientationPointLayoutParams;
				RelativeLayout.LayoutParams textViewRemoveBasePointLayoutParams;
				RelativeLayout.LayoutParams textViewRemoveStasiLayoutParams;
				RelativeLayout.LayoutParams textViewBasePointLayoutParams;
				
				
				textViewBasePointLayoutParams 									= (android.widget.RelativeLayout.LayoutParams) this.textViewBasePoint.getLayoutParams();
				textViewOrientationPointLayoutParams 	= (android.widget.RelativeLayout.LayoutParams) this.textViewOrientationPoint.getLayoutParams();
				textViewRemoveBasePointLayoutParams 	= (android.widget.RelativeLayout.LayoutParams) this.textViewRemoveBasePoint.getLayoutParams();
				textViewRemoveStasiLayoutParams 	= (android.widget.RelativeLayout.LayoutParams) this.textViewRemoveStasi.getLayoutParams();
				
				//Log.i("params.activeProject.start_stasi"," : " + params.activeProject.start_stasi);
				//Log.i("params.activeProject.stasi_0"," : " + params.activeProject.stasi_0);
				//Log.i("curOpenStasiIndex"," : " + curOpenStasiIndex);
				
				
				if(	
						params.activeProject.getStasiLinkIsBaseBy_id(params.staseis.item.get(curOpenStasiIndex)._id)
						|| params.activeProject.getStasiLinkIsComputedBy_id(params.staseis.item.get(curOpenStasiIndex)._id)
						){
					//An einai base to trexon
					textViewBasePointLayoutParams.topMargin=-120;		//kripse Set Base
					boolean has_period = params.msets.stasi_id_is_has_period(params.staseis.item.get(curOpenStasiIndex)._id);
					
					
					if(has_period){
						/* An exei metrithei periodos apo ti stasi*/
						textViewRemoveBasePointLayoutParams.topMargin=-120;		//Kripse remove base
						textViewRemoveStasiLayoutParams.topMargin=-120;	
						//tmp.height=180;
					}else{
						textViewRemoveBasePointLayoutParams.topMargin=190;		//Deixe remove base
						textViewRemoveStasiLayoutParams.topMargin=-120;	
						//tmp.height=250;
					}
					textViewOrientationPointLayoutParams.topMargin=120;			//Deixe Set Orientation
							
				}else{
					textViewBasePointLayoutParams.topMargin=110;
					if(params.staseis.item.get(curOpenStasiIndex).fixed){
						textViewRemoveStasiLayoutParams.topMargin=-120;	
						tmp.height=180;
					}else{
						textViewRemoveStasiLayoutParams.topMargin=180;	
						//tmp.height=180;
					}
					
					textViewOrientationPointLayoutParams.topMargin=-120;
					textViewRemoveBasePointLayoutParams.topMargin=-120;	
					
					
					
					
				}
				this.textViewBasePoint.setLayoutParams(textViewBasePointLayoutParams);
				
				if(params.msets.size()>0){
					//Log.i("---","ppp");
					//Log.i("---",":" + params.staseis.item.get(params.msets.get(params.msets.size()-1).stasi)._id);
					//Log.i("---",":" + params.staseis.item.get(this.curOpenStasiIndex)._id);
				if(params.staseis.item.get(params.msets.get(params.msets.size()-1).stasi)._id ==params.staseis.item.get(this.curOpenStasiIndex)._id){
					params.window_stasi_skop.show();
					params.mode.mode=2;
				}
				}
				
				
				layout.setLayoutParams(tmp);
				open=true;
				
		
		}
		//mset=new MyMeasureSet();
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
		open=false;
		params.window_stasi_skop.hide();
		params.app.vib.vibrate(100);
	}
	
	
	public void just_show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=20;
		tmp.topMargin=120;
		
		if(params.mode.mode==2){
			tmp.height=130;
		}
		
		layout.setLayoutParams(tmp);
	}
	
	
	
	

	public void just_hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=-9999250;
		tmp.topMargin=50;
		layout.setLayoutParams(tmp);
	}
	
	
	
	public void highlightStasi(int index,boolean value){
		params.staseis.highlightStasi(index, value);
	}
	
	/*
	public  void meas_all() throws InterruptedException{
		
		params.debug("meas_all");
		params.app.prolific_write("Ea");
		params.debug("after_write");
		params.app.vib.vibrate(100);
		//String out = "Ea 0100,0,  1.600, 0, 20.303,91.2875,26.4358";
		String msg = params.app.prolific_read_to_str();
		params.debug("after_read");
		params.debug(msg);
		//Log.i("-" + params.window_stasi.curSkopefsiIndex , out);
		params.msets.get(params.msets.size()-1).addMeasurementFormString(msg,true);
    	
	}*/
	
	
	
}
