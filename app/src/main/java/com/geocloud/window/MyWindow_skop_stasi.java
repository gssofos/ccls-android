package com.geocloud.window;

import java.io.File;
import java.util.Locale;

import com.example.test2.R;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyKml;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyStasi;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyWindow_skop_stasi {
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
	
	
	public float curYS;
	private int width,height;
	
	private Spinner ys;
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_skop_stasi(MyParams params){
		this.params = params;
		
		
		this.height = 	100;
		this.width	=	params.imagePixelWidth-280;
		
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		this.layout.setBackgroundColor(Color.argb(255,255,255, 255));
		//this.layout_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, this.height);
		this.layout_params = new RelativeLayout.LayoutParams(this.width, this.height);
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
		
		
		
		
		
		textView1 = new TextView(params.app.getBaseContext());	textView1.setText("[ @ ]");
		textView1.setTextColor(Color.argb(210,0, 0, 0));
		textView1.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(200, 80);
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
					getParams().window_stasi.small_size();
					meas_all(false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		);


		
		textView2 = new TextView(params.app.getBaseContext());	textView2.setText("[ -- ]");
		textView2.setTextColor(Color.argb(210,0, 0, 0));
		textView2.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(300, 80);
		tmp.leftMargin=160+30;
		tmp.topMargin=10;
		this.layout.addView(textView2,tmp);
		
	textView2.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//highlightStasi(curOpenStasiIndex, false);
					//hide();
					try {
						getParams().window_stasi.small_size();
						meas_all(true);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			);

		
		
		textView3 = new TextView(params.app.getBaseContext());	textView3.setText("[  >  ]");
		textView3.setTextColor(Color.argb(210,0, 0, 0));
		textView3.setTextSize(2, 20f);
		tmp = new RelativeLayout.LayoutParams(300, 80);
		tmp.leftMargin=370;
		tmp.topMargin=10;
		this.layout.addView(textView3,tmp);
		
textView3.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				
				
				try {
					getParams().window_stasi.small_size();
					meas_angle();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				// TODO Auto-generated method stub
				//highlightStasi(curOpenStasiIndex, false);
				//hide();
				//try {
					//meas_angle();
				//} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
			}
		}
		);
		
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








		ys = new Spinner(params.app.getBaseContext());
	
		String yss = params.app.dbHelper.getKeyValue("ys");
		if(yss.indexOf(",")>0){
			yss="*.**," + yss;
		}else{
			yss="*.**,0.00";;
		}
		
		String[] stringArray = yss.split(",");
		int i;
		for(i=1;i<=stringArray.length-1;i++){
			stringArray[i]= String.format(Locale.ENGLISH,"%.2f",(Float.valueOf(stringArray[i])/100));;
		}
		ArrayAdapter ysmodeAdapter = new ArrayAdapter<String>(params.app, R.layout.list_item, stringArray);
		ysmodeAdapter.setDropDownViewResource(R.layout.list_item); // The drop down view


		ys.setAdapter(ysmodeAdapter);
		
		ys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // ((TextView) parentView.getChildAt(0)).setTextColor(Color.rgb(255, 255, 255));  
		       //  parentView.setBackgroundColor(Color.GREEN);
		    	
		    	int myColor = Color.BLACK;
		    	((TextView) parentView.getChildAt(0)).setTextColor(myColor);
		    	
		    	if(position==0){
		    		getParams().window_stasi_skop.curYS=-999f;
		    		Toast.makeText(getParams().app.getApplicationContext(), " YO set to : " +getParams().window_stasi_skop.curYS + " m", Toast.LENGTH_LONG).show();;;
				       
		    	}else{
		    		getParams().window_stasi_skop.curYS=Float.valueOf(((TextView) parentView.getChildAt(0)).getText().toString());
		    		Toast.makeText(getParams().app.getApplicationContext(), " YS set to : " +((TextView) parentView.getChildAt(0)).getText().toString() + " m", Toast.LENGTH_LONG).show();;;
				       
		    	}
		    	
		    	 // Log.i("sel", ((TextView)parentView.getChildAt(0)).getText().toString());
		         /*
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
				}*/
					
		    }
		
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		    
		
		ys.setSelection(0);
		tmp = new RelativeLayout.LayoutParams(200,100);
		tmp.leftMargin=this.width-205;
		tmp.topMargin=10;
		this.layout.addView(ys,tmp);
		







		open=false;
	}
	
	public void show(){
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=0;
		tmp.topMargin=0;
		layout.setLayoutParams(tmp);
		open=true;
	}
	
	public void hide(){
		params.debug("sss");
		RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		tmp.leftMargin=50;
		tmp.topMargin=-220;
		layout.setLayoutParams(tmp);
		open=false;
	}
	
	public void highlightStasi(int index,boolean value){
		params.staseis.highlightStasi(index, value);
		
		
	}
	
	
	private MyParams getParams(){
		return params;
	}
	public  void meas_all(boolean line) throws InterruptedException{
		
		//params.debug("meas_all");
		//params.app.prolific_write("Ea");
		//params.tscomm.prolific_write("Ea")
		//if(params.tscomm.writeCommand("Ea")){
			if(params.tscomm.writeCommand("slope")){
					
			
			setCancel(true);
			
			
			params.app.vib.vibrate(100);
			//params.tscomm.prolific_read_with_callback(2,line);
			params.tscomm.readCommandWithCallback(2,line);
		}else{
			Toast.makeText(params.app, "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		
		
		//String msg = params.app.prolific_read_to_str();
		//params.renderer.msets.get(params.renderer.msets.size()-1).addMeasurementFormString(msg);	
	}
	
	
	
	private void  setCancel(boolean value){
		
		if(value){
			RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) textView1.getLayoutParams();
			tmp.topMargin=-100;
			textView1.setLayoutParams(tmp);
		
			tmp = (android.widget.RelativeLayout.LayoutParams) textView2.getLayoutParams();
			tmp.topMargin=-100;
			textView2.setLayoutParams(tmp);
		
			tmp = (android.widget.RelativeLayout.LayoutParams) textView3.getLayoutParams();
			tmp.topMargin=-100;
			textView3.setLayoutParams(tmp);
			
			tmp = (android.widget.RelativeLayout.LayoutParams) textView4.getLayoutParams();
			tmp.topMargin=20;
			textView4.setLayoutParams(tmp);
			
			params.readCommCancelFlag=false;
			
			//Log.i("setCancel" , "" + value);
			
		}else{
			RelativeLayout.LayoutParams tmp = (android.widget.RelativeLayout.LayoutParams) textView1.getLayoutParams();
			tmp.topMargin=20;
			textView1.setLayoutParams(tmp);
		
			tmp = (android.widget.RelativeLayout.LayoutParams) textView2.getLayoutParams();
			tmp.topMargin=20;
			textView2.setLayoutParams(tmp);
		
			tmp = (android.widget.RelativeLayout.LayoutParams) textView3.getLayoutParams();
			tmp.topMargin=20;
			textView3.setLayoutParams(tmp);
			
			tmp = (android.widget.RelativeLayout.LayoutParams) textView4.getLayoutParams();
			tmp.topMargin=-100;
			textView4.setLayoutParams(tmp);
			
			
			params.readCommCancelFlag=true;
			
			//Log.i("setCancel" , "" + value);
		}
		
		
	}
	
	public void meas_all_callBack(String data,boolean line){
			
		Log.i("meas_all_callBack Data","---");
		//Log.i("Data",data);
		if(data!=""){
			Log.i("meas_all_callBack Data","null");
		if(params.window_stasi.curSkopefsiIndex==-1){
				MyMeasureSet periodos = params.msets.get(params.msets.size()-1);
				
				
				//sxediazo se highlight to telefatio point gia na xero pou einai
				double[] coor = periodos.addMeasurementFormString(data,true);	
				
				if(coor.length>1){/*params.renderer.pointHighlight.item.clear();
				params.renderer.pointHighlight.add(coor[1], coor[0],255f,0f,0f);
				params.renderer.pointHighlight.regen_coor();
				*/
				//MyMeasurement last = periodos.item.get(periodos.item.size()-1);
				MyMeasurement mes = periodos.item.get(periodos.item.size()-1);
				mes.ys=params.window_stasi_skop.curYS;
				params.app.dbHelper.addMeasurementToDb(
						mes
						,periodos._id,0);
				
				
				if(line && params.event.pointSelected){
					MyPoly myline = params.renderer.my_poly.add(0f, 0f, 0f);
					myline.addVertice(params.event.selectedx, params.event.selectedy);
					myline.addVertice(coor[1], coor[0]);
					params.renderer.my_poly.regen_coor();
					double[] mc = new double[4];mc[0]=params.event.selectedx;mc[1]=params.event.selectedy;mc[2]=coor[1];mc[3]=coor[0];
					params.app.dbHelper.addLineToDb(myline,0, 0, 0, 0, mc);
					
					
					
					params.web.httpCall(
							"http://eds.culture.gr/_geo/php/_add_line.php?x1=" +params.event.selectedx 
									+ "&y1=" 	+ params.event.selectedy 
									+ "&x2=" 	+ coor[1] 
									+ "&y2=" 	+ coor[0] 
									+ "&ys=" 	+ mes.ys 
									+ "&id=" 	+ myline._id 
									+ "&p=" 	+ params.activeProject._id
													
				);
					
					
					
		
					
				}
				
				params.event.setSelectedPoint(coor[1], coor[0]);
				params.surface.requestRender();
				}else{
					params.tscomm.btRead_trash();
					ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
					toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200); 
				}
			}else{
				
				MyMeasureSet periodos = params.msets.get(params.msets.size()-1);
				
				double coor[] = periodos.addMeasurementFormString(data,false,params.window_stasi.curSkopefsiIndex);
				if(coor.length>1){
				MyStasi tmp = params.staseis.item.get(params.window_stasi.curSkopefsiIndex);
				params.renderer.marker.regen_coor();
				tmp.setCoorFl(coor);
				
				MyMeasurement mes = periodos.itemStaseis.get(periodos.itemStaseis.size()-1);
				mes.ys=params.window_stasi_skop.curYS;
				
				params.app.dbHelper.addMeasurementToDb(
															mes
															,periodos._id,1);
				
				//params.logData();
				params.odefsi.redrawfromstasi(false, params.staseis.get_stasi_by_id(periodos.stasi_id));
				
				//params.odefsi.redraw(false);
				params.surface.requestRender();
				}else{
					params.tscomm.btRead_trash();
					
					ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
					toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200); 
				}
				
			}
		}else{
			Log.i("meas_all_callBack null data","null data");
			params.debug("null data");;
			params.tscomm.btRead_trash();
			
		}
		//Log.i("ddfdf","dfdfd");
		params.window_stasi_skop.setCancel(false);
		}
	
	
	
	public  void meas_angle() throws InterruptedException{
		if(params.tscomm.writeCommand("angle")){
					
			
			setCancel(true);
			
			
			params.app.vib.vibrate(100);
			params.tscomm.readCommandWithCallback(3,false);
			//params.tscomm.readCommandWithCallback(2,line);
		}else{
			Toast.makeText(params.app, "No Connection", Toast.LENGTH_SHORT).show();
		}
		
		
			/*
			
		params.app.vib.vibrate(100);
		String out = "Ee 0100,0,   1.600, 0, 91.2875, 26.4356,E115";
		
		//String out = "Ea 0100,0,  1.600, 0, 0.303,91.2875,26.4358";
		Log.i("-" + params.window_stasi.curSkopefsiIndex , out);
		
		params.msets.get(params.msets.size()-1).addMeasurementFormString(out,params.window_stasi.curSkopefsiIndex);
		
		//params.app.prolific_read();*/
	}
	
	
	
	
	
	
	
	
	
	
	

	public void meas_angle_callBack(String data,boolean trash){
			
		Log.i("meas_angle_callBack  Data",data);
		
		if(data!=""){
					Log.i("meas_angle_callBack  Data","null data");
					if(params.window_stasi.curSkopefsiIndex==-1){
						//MyMeasureSet periodos = params.msets.get(params.msets.size()-1);
						MyMeasureSet 	periodos 	= params.msets.get(params.msets.size()-1);
						MyMeasurement 	mes 		= periodos.addAngleMeasurementFromString(data,params.window_stasi.curSkopefsiIndex);	
						
						mes._id =params.app.dbHelper.addMeasurementToDb(
								mes
								,periodos._id,0);
						
							
						params.surface.requestRender();
					
					}else{

						MyMeasureSet 	periodos 	= params.msets.get(params.msets.size()-1);
						MyMeasurement 	mes 		= periodos.addAngleMeasurementFromString(data,params.window_stasi.curSkopefsiIndex);	
						
						mes._id =params.app.dbHelper.addMeasurementToDb(
								mes
								,periodos._id,1);
						
						params.odefsi.redrawfromstasi(false, params.staseis.get_stasi_by_id(periodos.stasi_id));
						
						
						//params.odefsi.redraw(false);
						params.surface.requestRender();
						
					}
		}else{
					Log.i("null data","null data");
					params.debug("null data");;
		}
		
		
		params.window_stasi_skop.setCancel(false);
	}
	
	
	
}
