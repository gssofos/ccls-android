package com.geocloud.window;


import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.stasi_link_class;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyWindow_edit_data {
	public RelativeLayout layout;
	public RelativeLayout layout_sv;
	public RelativeLayout.LayoutParams layout_params; 
	MyParams params;
	
	
	public long curStasi;
	public TextView textViewName;
	public TextView redrawTax;
	public TextView recalcOdefsi;
	private ScrollView sv;
	
	//public TextView textViewHeight;
	//public TextView textViewX;
	//public TextView textViewY;
	//public EditText editText1;
	private boolean open = false;
	
	
	
	public int curOpenStasiIndex;
	
	
	public boolean isOpen(){
		return open;
	}
	public MyWindow_edit_data(MyParams params){
		this.params = params;
		this.layout = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		this.layout.setBackgroundColor(Color.argb(180,0,0, 0));
		
		this.layout_params = new RelativeLayout.LayoutParams(500,params.imagePixelHeight-350);
		this.layout_params.leftMargin=-1111;
		
		RelativeLayout.LayoutParams tmp;
		
		
		
		textViewName = new TextView(params.app.getBaseContext());	
		textViewName.setText("ST : ");
		textViewName.setTextColor(Color.argb(255,255, 255, 255));
		tmp = new RelativeLayout.LayoutParams(300, 50);
		tmp.leftMargin=20;
		tmp.topMargin=20;
		this.layout.addView(textViewName,tmp);
		
		
		sv = new ScrollView(params.app.getBaseContext());
		sv.setBackgroundColor(Color.TRANSPARENT);
		tmp = new RelativeLayout.LayoutParams(450, params.imagePixelHeight-350-250);
		tmp.leftMargin=20;
		tmp.topMargin=120;
		this.layout.addView(sv,tmp);
		
		
		
		this.layout_sv = new RelativeLayout(params.app.getBaseContext());	/*this.layout.setOrientation(1);*/					
		tmp = new RelativeLayout.LayoutParams(450, params.imagePixelHeight-350-120);
		tmp.leftMargin=0;
		tmp.topMargin=0;
		sv.addView(this.layout_sv,tmp);
		
		
		
		redrawTax = new TextView(params.app.getBaseContext());	
		redrawTax.setText("Taximetrika");
		redrawTax.setTextColor(Color.argb(255,255, 255, 255));
		redrawTax.setTextSize(22);
		tmp = new RelativeLayout.LayoutParams(300, 60);
		tmp.leftMargin=25;
		tmp.topMargin=params.imagePixelHeight-450;
		this.layout.addView(redrawTax,tmp);
		
		
		redrawTax.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				get_params().mode.toggle_edit_data();
				get_params().odefsi.draw_tax();
				
				
			}
			
		});
		
		
		
		
		recalcOdefsi = new TextView(params.app.getBaseContext());	
		recalcOdefsi.setText("Odefsi");
		recalcOdefsi.setTextColor(Color.argb(255,255, 255, 255));
		recalcOdefsi.setTextSize(22);
		tmp = new RelativeLayout.LayoutParams(300, 60);
		tmp.leftMargin=300;
		tmp.topMargin=params.imagePixelHeight-450;
		this.layout.addView(recalcOdefsi,tmp);
		
		
		recalcOdefsi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//get_params().mode.toggle_edit_data();
				get_params().renderer.my_poly.clear();
				get_params().odefsi.redraw(false);
				
				
			}
			
		});
		
		
		
		
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
		
		tmp.leftMargin=0;
		tmp.topMargin=120;
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
	
	public void toggle(){
		if(open) {hide();}else{show();};
		//params.window_tools.hide();
	}
	
	public void highlightStasi(int index,boolean value){
		params.staseis.highlightStasi(index, value);
	}
	
	public void set_data(int index){
		// index -> local array index
		get_params().mode.edit_data_move_graphical=false;
		get_params().mode.edit_data_rotate_graphical = false;
		
		this.curStasi = index;
		RelativeLayout.LayoutParams lay ;
		TextView tmp;
		CheckBox cb;
		
		layout_sv.removeAllViews();
		textViewName.setText("Project :   " + params.activeProject.name);
		
		if(index>=0){
		
			
					MyStasi st = params.staseis.item.get(index);
					String name = "";
					if(st.name.contains("added")){
						name = "S" + st._id;
					}else{
						name = st.name + " (" + st._id + ")";
					}
					textViewName.setText("ST :   " + st.name() + "  (" + st._id + ")");
		
		
		
					MyStasi st0 = get_params().msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
			
					
				if(params.activeProject.getStasiLinkIsBaseBy_id(st._id) && !st.fixed && st0!=null && !st0.fixed) {// an einai gia move/rotate
					
						cb = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
						lay = new RelativeLayout.LayoutParams(130, 50);
						lay.leftMargin=240;		lay.topMargin=10;
						layout_sv.addView(cb,lay);
						cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				
							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								//set_allow_download(isChecked);
								// TODO Auto-generated method stub
								get_params().mode.edit_data_move_graphical=isChecked;
								get_params().mode.edit_data_rotate_graphical=false;
							}
							
						});
			
						tmp = params.util.addTextView(layout_sv, 
									320, 10, 
									300, 50, 
									"Move", 
									Color.argb(255,255, 255, 255),
									16f);	
				 
				
				
				
						cb = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
						lay = new RelativeLayout.LayoutParams(130, 50);
						lay.leftMargin=240;		lay.topMargin=60;
						layout_sv.addView(cb,lay);
						cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				
							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								//set_allow_download(isChecked);
								// TODO Auto-generated method stub
								get_params().mode.edit_data_move_graphical=false;
								get_params().mode.edit_data_rotate_graphical=isChecked;
							}
							
						});
						
	
						tmp = params.util.addTextView(layout_sv, 
									320, 60, 
									300, 50, 
									"Rot", 
									Color.argb(255,255, 255, 255),
									16f);	
	
				}
		 
		

				
				
				
				cb = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
				lay = new RelativeLayout.LayoutParams(130, 50);
				lay.leftMargin=10;			lay.topMargin=0;			layout_sv.addView(cb,lay);
				
				cb.setId(index);
				
					cb.setChecked(params.activeProject.getStasiLinkIsBaseBy_id(st._id));
					
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							//set_allow_download(isChecked);
							// TODO Auto-generated method stub
							get_params().activeProject.updateStasiLinkisbase(params.staseis.item.get((int) get_params().window_edit_data.curStasi)._id,isChecked);
							
						}
						
					});
					
					
					
				 tmp = params.util.addTextView(layout_sv, 
						90, 0, 
						300, 50, 
						"Base", 
						Color.argb(255,255, 255, 255),
						16f);	
				 
		
		 tmp = params.util.addTextView(layout_sv, 
					90, 60, 
					350, 90, 
					(double) Math.round(st.x*100)/100+ " \n" + (double)  Math.round(st.y*100)/100 + " \n"  + st.date, 
					Color.argb(255,255, 255, 255),
					13f);	

		
		 if(st.isEmpty()){
		 
			 tmp = params.util.addTextView(layout_sv, 
						90, 260, 
						350, 50, 
						"Remove Stasi", 
						Color.argb(255,255, 255, 255),
						16f);
			 
			 tmp.setId((int) st._id);
			 tmp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					final int index = v.getId();
					
					
					final AlertDialog.Builder b = new AlertDialog.Builder(get_params().app);
					b.setIcon(android.R.drawable.ic_dialog_alert);
					b.setTitle("System");
					b.setMessage("delete Stasi " + params.staseis.get_stasi_by_id(index).name()  +  " ?");
					b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	 // get_params().activeProject.syncToCloud();
					    	  params.window_edit_data.set_data(-1);
					    	  params.staseis.get_stasi_by_id(index).delete();
					      } });
					
					
					b.setNegativeButton("No",  new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
						        // Log.i("alert","no pressed");
						      } });
					//b.setPositiveButton("Yes", null);
					//b.setNegativeButton("No", null);
					b.show();
					
					
					//Log.i("Delete stasi","koukou");
					
					
				}});
		 }
		 
		 /*
		 if(params.activeProject.getStasiLinkIsBaseBy_id(st._id) && !st.fixed && st0!=null && !st0.fixed) {
			// if(params.activeProject.getStasiLinkIsBaseBy_id(st._id) && !st.fixed && st0!=null && !st0.fixed) {
					 tmp = params.util.addTextView(layout_sv, 
					270, 60, 
					420, 60, 
					"MOVE", 
					Color.argb(255,255, 255, 255),
					21f);	

			tmp.setId(index);
			//Log.i("f,l","" + get_params().msets.stasi0_local_index_of_fist_period_of_stasi_by_id(st._id));
			
			tmp.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int gid = v.getId();
					Log.i("stasi index",""+gid);
					
					MyStasi st = get_params().staseis.item.get(gid);
					MyStasi st0 = get_params().msets.MyStasi_of_fist_period_of_stasi_by_id(st._id);
						
					double[] center = get_params().getCenterCoor();
					
					Log.i("f,l",""+(double) Math.round(st.f*100000000)/100000000 + " \n" + (double)  Math.round(st.l*100000000)/100000000);
					Log.i("f,l","" + (double)  Math.round(center[1]*100000000)/100000000+ " \n" + (double)  Math.round(center[0]*100000000)/100000000);
					Log.i("f,l","" + get_params().msets.MyStasi_of_fist_period_of_stasi_by_id(st._id)._id);
					//get_params().debug("" +get_params().staseis.item.get(get_params().msets.stasi0_local_index_of_fist_period_of_stasi_by_id(st._id))._id);
					
					
					
					double[] c0 = new double[2];
					c0[1]=st0.l+center[0]-st.l;
					c0[0]=st0.f+center[1]-st.f;
					
					st0.setCoorFl(c0);
					
					
					double[] c = new double[2];
					c[0]=center[1];
					c[1]=center[0];
					st.setCoorFl(c);
					
					
					
					
					//int p = (int) Math.floor(gid/10000);int m = gid-p*10000;
					//Log.i("p:" + p,"m:" + m);
					//MyMeasurement tmp = get_params().msets.get(p).itemStaseis.get(m);
					//if(tmp.odefsi_use){			tmp.odefsi_use=false;}else{tmp.odefsi_use=true;					}
					//get_params().window_edit_data.set_data(get_params().selectedStasi);
					get_params().odefsi.redraw();
					get_params().renderer.points.clear();
					get_params().odefsi.draw_tax();
				}
			}
			);
			
			
			
			 
			
		}
			
			*/
		
		
		int i,k;
		int counter = 0;
		int counterPer = 0;
		for (i=params.msets.size()-1;i>=0;i--){
			MyMeasureSet ms =params.msets.get(i) ;
			if(ms.stasi==params.selectedStasi){
				counterPer ++;
				
				
				tmp = params.util.addTextView(layout_sv, 
						20, (counterPer)*90 + counter*70+100, 
						350, 50, 
						"YO : " + ms.YO, 
						Color.argb(255,255, 255, 255),
						12f);	

				tmp = params.util.addTextView(layout_sv, 
						180, (counterPer)*90 + counter*70+100, 
						330, 50, 
						"" + ms.stasi_0_angle + "  (" + params.staseis.item.get(ms.stasi_0).name() + ")", 
						Color.argb(255,255, 255, 255),
						12f);	

				
				for (k=0;k<=ms.itemStaseis.size()-1;k++){
					counter ++;
					MyMeasurement mm = ms.itemStaseis.get(k);
					MyStasi targetst = params.staseis.item.get(mm.stasi_index);
					int color;
					int color_name;
					if(mm.odefsi_use){
						
						if(st.variationErrorOfAzimuthPairTo(mm.stasi_index)){
							color_name = Color.argb(255,255, 0, 0);
						}else{
							color_name = Color.argb(255,255, 255, 255);
						}
						color = Color.argb(255,255, 255, 255);
					}else{
							color_name = Color.argb(255,0, 0, 0);
							color = Color.argb(255,0, 0, 0);
						
					}
					
					tmp = params.util.addTextView(layout_sv, 
														40, (counterPer)*90 + counter*70+100, 
														300, 50, 
														"" + targetst.name() ,//+" - " +  mm.global_id, 
														color_name,
														12f);	
					
					tmp.setId(i*10000+k);
					tmp.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int gid = v.getId();int p = (int) Math.floor(gid/10000);int m = gid-p*10000;
							//Log.i("p:" + p,"m:" + m);
							MyMeasurement tmp = get_params().msets.get(p).itemStaseis.get(m);
							if(tmp.odefsi_use){			tmp.odefsi_use=false;}else{tmp.odefsi_use=true;					}
							get_params().window_edit_data.set_data(get_params().selectedStasi);
							get_params().renderer.points.clear();
							//get_params().odefsi.redraw(false);
							get_params().odefsi.redrawfromstasi(false, get_params().staseis.get_stasi_by_id(get_params().msets.get(p).stasi_id));
							get_params().window_edit_data.set_data((int) get_params().window_edit_data.curStasi);
							//get_params().odefsi.draw_tax();
						}
					}
					);
					
					tmp = params.util.addTextView(layout_sv, 
							190, (counterPer)*90 + counter*70+100, 
							400, 50, 
							" : " + (float) Math.round(mm.hD()*1000)/1000 + "    "   + mm.hZ, 
							color,
							12f);	
					
					tmp.setId(i*10000+k);
					tmp.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int gid = v.getId();int p = (int) Math.floor(gid/10000);int m = gid-p*10000;
							//Log.i("p:" + p,"m:" + m);
							
							MyMeasurement tmp = get_params().msets.get(p).itemStaseis.get(m);
							if(tmp.odefsi_use){		tmp.db_set_odefsi_use(false);	tmp.odefsi_use=false;}else{tmp.odefsi_use=true;	tmp.db_set_odefsi_use(true);				}
							get_params().window_edit_data.set_data(get_params().selectedStasi);
							get_params().renderer.points.clear();
							get_params().odefsi.redrawfromstasi(false, get_params().staseis.get_stasi_by_id(get_params().msets.get(p).stasi_id));
							
							//get_params().odefsi.redraw(false);
							get_params().window_edit_data.set_data((int) get_params().window_edit_data.curStasi);
							
							//get_params().renderer.points.clear();
							//get_params().odefsi.draw_tax();
						}
					}
					);
					
					
				}
				
			}
				
			}
		
		
		
		
		
		
		
		}else{
			
			
			
			
			
			
			
			
			int i,counter=0;
			MyParams params =  get_params();
			
			for(i=0;i<=params.activeProject.st.size()-1;i++){
				stasi_link_class st = params.activeProject.st.get(i);
				counter++;
				
				
				cb = new CheckBox(params.app.getBaseContext());	//text1.setText("");		//text1.setBackgroundColor(Color.argb(100,0, 0, 0));
				lay = new RelativeLayout.LayoutParams(130, 50);
				lay.leftMargin=40;
				lay.topMargin=(counter)*90+110-3;
				layout_sv.addView(cb,lay);
				
				
				 tmp = params.util.addTextView(layout_sv, 
						110, (counter)*90+110, 
						300, 50, 
						"" + params.staseis.item.get(params.staseis.stasi_id_to_index(st._id)).name(), 
						Color.argb(255,255, 255, 255),
						16f);	
				
				 tmp = params.util.addTextView(layout_sv, 
							300, (counter)*90+110, 
							300, 50, 
							"p: " + params.msets.stasi_id_count_of_periods(st._id), 
							Color.argb(255,255, 255, 255),
							16f);	
				 
				 
				 
				 
			}
			
			
		}
		
		
		
	}
	
	
	


}
