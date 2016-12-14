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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyWindow_set_yo {
	public RelativeLayout layout;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	private TextView textView1;
	private TextView textViewClose;
	private Button okView;
	private Button cancelView;
	//public EditText editText1;
	private boolean open = false;
	
	public int curOpenStasiIndex;
	public int curSkopefsiIndex;
	
	public float yo = 1.60f;
	public float angle;
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_set_yo(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					this.layout.setBackgroundColor(Color.argb(255,255,255, 255));
		this.layout.setBackgroundColor(Color.argb(180,0, 0, 0));
		this.layout_params = new RelativeLayout.LayoutParams(240, 290);
		this.layout_params.leftMargin=120;
		this.layout_params.topMargin=-9910;
		
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(130, 60);
		
	
		
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("YO : 1.60");
		textView1.setTextColor(Color.argb(255,255,255, 255));
		textView1.setTextSize(2, 15f);
		tmp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
		tmp.leftMargin=20;
		tmp.topMargin=10;
		this.layout.addView(textView1,tmp);
		
		textView1.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
			}
		}
		);

		
		
		okView = new Button(params.app.getBaseContext());	okView.setText("Accept");
		okView.setTextColor(Color.argb(210,0, 0, 0));
		okView.setTextSize(2, 15f);
		okView.setBackgroundColor(Color.argb(255,253,237,64));
		tmp = new RelativeLayout.LayoutParams(200, 90);
		tmp.leftMargin=15;
		tmp.topMargin=70;
		this.layout.addView(okView,tmp);
		
		okView.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				hide();
				MyParams tmp = getParams();
				tmp.window_back_orientation.hide(false);
				tmp.window_msg.hide();
				tmp.mode.set_measuring(yo,angle,getParams().window_back_orientation.curOpenStasiIndex);
				tmp.app.dbHelper.addPeriodoToDb(tmp.msets.get(tmp.msets.size()-1));
				tmp.window_stasi.just_show();
				tmp.window_stasi_skop.show();
				
			}
		}
		);

		
		cancelView = new Button(params.app.getBaseContext());	cancelView.setText("Cancel");
		cancelView.setTextColor(Color.argb(210,0, 0, 0));
		cancelView.setTextSize(2, 15f);
		cancelView.setBackgroundColor(Color.argb(255,253,237,64));
		tmp = new RelativeLayout.LayoutParams(200, 90);
		tmp.leftMargin=15;
		tmp.topMargin=180;
		this.layout.addView(cancelView,tmp);
		
		cancelView.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				hide();
				getParams().window_back_orientation.hide(true);
			}
		}
		);

			
		open=false;
	}
	
	public void show(float angle){
		
		this.angle=angle;
		params.debug(angle + "");
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=0;
		tmp.topMargin=110;
		layout.setLayoutParams(tmp);
		
			if(!open) yo = 1.60f;  // an einai idi anoixto mi xalas yo

		
			open=true;
		
	}
	
	public void hide(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=50;
		tmp.topMargin=-420;
		layout.setLayoutParams(tmp);
		open=false;
		
	}
	public MyParams getParams(){
		return params;
	}
	
	
	public void set_yo(float yo_add, boolean update){
		params.debug((yo_add+yo) + ","+ yo_add +"," + yo + "");
		textView1.setText("YO : " +(float)  Math.round((yo_add+yo)*1000)/1000);
		if(update) yo = (float)  Math.round((yo_add+yo)*1000)/1000;
	}
	
	
}
