package com.geocloud.topo;

import com.example.test2.R;
import com.geocloud.MyGLES20.MyParams;

public class MyMode {
	public int mode=0;
	public boolean edit_line = false;
	public boolean edit_data = false;
	public boolean edit_data_move_graphical = false;		//gia  move graphical
	public boolean edit_data_rotate_graphical = false;		//gia  rotate graphical
	
	public int edit_line_mode=0;		//0:tipota, 1:extend, 2:trim, 3:offset, 4:per, 5:meta
	MyParams params;
	
	public MyMode(MyParams params){
		this.params=params;
	}
	public void set_stasi_explore(){
		//anoigo sto touch staseis kai blepo. mporo na epilexo energeies gia stasi i na bgo
		this.mode=0;
		toggle_stasi();
	}
	
	public void set_back_define(){
		//exo anoixei stasi kai perimeno na touch se stasi gia orismo 0 (goniaka mallon)
		this.mode=1;
		toggle_stasi();
	}
	
	
	
	
	
	public void set_measuring(float yo, float angle, int stasi_0){
		this.mode=2;			//exo parei yo kai stasi miden kai arxizo metrisi
		
		MyMeasureSet tmp 	= new MyMeasureSet(params.window_stasi.curOpenStasiIndex, params);
		tmp.stasi_id 		= (int) params.staseis.item.get(params.window_stasi.curOpenStasiIndex)._id;
		tmp.setYO(yo);
		tmp.setAzimuth(angle, stasi_0);
		tmp._t_tabId=Integer.valueOf(params.tabId);
		params.msets.add(tmp);
		toggle_stasi();
		edit_data_move_graphical = false;
		edit_data_rotate_graphical = false;
	}
	
	public void set_edit_line(boolean value){
		if(value){
			edit_line=true;
			params.app.edit_lines.setImageResource(R.drawable.edit_yellow_256);
			params.window_tools.hide();
			params.window_edit.show();
			params.window_stasi.hide();
			params.window_edit_data.hide();
			set_edit_data(false);
			
		}else{
			edit_line=false;
			params.app.edit_lines.setImageResource(R.drawable.edit_off_256);
			params.window_edit_linemod.hide();
			
		}
		edit_data_move_graphical = false;
		edit_data_rotate_graphical = false;
		//toggle_stasi();
	}
	
	
	public void toggle_edit_line(){
		//kano edit se grammes
		if(edit_line){
			this.mode=0;
			set_edit_line(false);
			params.window_edit.hide();
		}else{
			set_edit_line(true);
			
		}
		
		//toggle_stasi();
	}
	
	
	
	
	public void set_edit_data(boolean value){
		if(value){
			edit_data=true;
			this.mode=3;
			params.app.edit_data.setImageResource(R.drawable.calc_on);
			set_edit_line(false);
			params.window_edit_data.show();
			params.window_stasi.hide();
			params.window_edit.hide();
			params.window_tools.hide();
		}else{
			edit_data=false;
			params.window_edit_data.hide();
			params.app.edit_data.setImageResource(R.drawable.calc_off);
		}
		edit_data_move_graphical = false;
		edit_data_rotate_graphical = false;
		//toggle_stasi();
	}
	
	
	
	public void toggle_edit_data(){
		//kano edit se grammes
		if(edit_data){
			this.mode=0;
			set_edit_data(false);
		}else{
			set_edit_data(true);
			//params.window_tools.hide();params.window_edit.show();
		}
		
		toggle_stasi();
	}
	
	
	
	
	public void toggle_stasi(){
		if(edit_line){
			params.app.stasi.setImageResource(R.drawable.stasi_80_off);
		}else{
			if(mode==0){
				params.app.stasi.setImageResource(R.drawable.stasi_80);
			}else{
				params.app.stasi.setImageResource(R.drawable.stasi_80_off);
			}
		}
	}
	
	
}
