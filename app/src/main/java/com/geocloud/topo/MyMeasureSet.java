package com.geocloud.topo;

import java.util.ArrayList;

import android.util.Log;
import android.widget.Toast;

import com.geocloud.MyGLES20.MyParams;

public class MyMeasureSet {
	public int 							stasi;			//local index
	public int 							stasi_0=-1;
	
	public int 							stasi_id;			//global id index
	public int 							stasi_0_id=-1;
	
	public float 						stasi_0_angle=0;;
	public float 						YO;
	public float 						azimuth2stasi_0;		//azimuthio pros stasi midenismou, prokiptei apo coor kai ipologizetai mia fora
	public float 						azimuth2_0;
	public ArrayList<MyMeasurement> 	item = new ArrayList<MyMeasurement>();
	public ArrayList<MyMeasurement> 	itemStaseis = new ArrayList<MyMeasurement>();
	MyParams 							params;
	
	public String sxolia="";
	
	public int _t_tabId;
	public long _t_id;
	public long project;
	public Long global_id;
	public int date;
	public 	int			local_date;
	
	public  long _id;
	public MyMeasureSet(int stasi, MyParams params){
		this.stasi=stasi;
		this.params=params;
		//Log.i("---MyMeasureSet","kkkkkkkkkkk");
		_t_tabId=-1;
		_t_id=-1;
		project=-1;
		date=-1;
		local_date = (int) params.getTime();
	}
	
	
	
	public MyMeasureSet(){
		this.stasi=-1;
		this.stasi_0=-1;
		//this.params=params;
		//Log.i("---MyMeasureSet","kkkkkkkkkkk");
		_t_tabId=-1;
		_t_id=-1;
		project=-1;
		date=-1;
		local_date =-1;
	}
	
	
	public void setYO(float yo){
		YO=yo;
	}
	
	
	
	
	
	public void setAzimuthFromComputed(){
		
		//de xrisimopoieitai
		int i;
		for(i=0;i<=this.itemStaseis.size()-1;i++){
			MyMeasurement tmp = this.itemStaseis.get(i);
			if(tmp.odefsi_use){
				
			}
		}
	}
	
	
	public double getAzimuthTo0(){
		double out = 0;
		int n=0;
		
		ArrayList<Long> staseis_id_list = new ArrayList<Long>();
		int i;
		
		
		
		
		
		for(i=0;i<=this.itemStaseis.size()-1;i++){
			MyMeasurement mes = this.itemStaseis.get(i);
			if(mes.odefsi_use){
				if(!staseis_id_list.contains(mes.stasi_index_id)){
					staseis_id_list.add((long) mes.stasi_index_id);
				}
			}
		}
		
		
		for(i=0;i<=staseis_id_list.size()-1;i++){
			double tmp = getAzimuthTo0FromStasiIndex(staseis_id_list.get(i),false);
			
			if(tmp>-1){
				//Log.i("MyMeasureSet getAzimuthTo0 added","(" +params.staseis.get_stasi_by_id(staseis_id_list.get(i)).name() + "#" + staseis_id_list.get(i) + ")  " +  tmp + "#");
				
				out = out + tmp;
				n+=1;
			}
		}
		
		if(n>0){
			out = out/n;
		}
		
		
		
		
		
		if(n==0){out=-1;};
		this.azimuth2_0=(float) out;
		
		Log.i("MyMeasureSet getAzimuthTo0 final",this.azimuth2_0 + "#");
		return out;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public double getAzimuthTo0FromStasiIndex(long target_stasi_id, boolean overide_computed_demand){
		
		//overide_computed_demand einai gia tis periptoseis pou den einai aparaitito na exei ipologistei i stasi gia na proxoris (px enarxi)
		double out = 0;
		int i;
		
		float sum=0;
		float n=0;
		params.logvWithTime(target_stasi_id);
		params.logvWithTime(this.stasi_id);
		MyStasi st 	= params.staseis.	get_stasi_by_id(this.stasi_id);
		MyStasi st0 = params.staseis.	get_stasi_by_id(target_stasi_id);
		double 	az0 = params.util.		getAzimuth_by_dxdy_inn(st0.x-st.x, st0.y-st.y);
		st.logcoor();
		st0.logcoor();
		Log.i("MyMeasureSet getAzimuthTo0", "dx:" + (st0.x-st.x) +    "  dy:" + (st0.y-st.y) +  "    az0 : " + az0);
		params.odefsi_solution_csv_string+=
				";" 
				+ ";" + st.name() 
				+ ";" + st0.name() 
				//+ ";" +  params.staseis.item.get(st1).YO 
				+ ";;;;;" +  (st0.x)
				+ ";" +  (st0.y)
				+ ";" +  (st.x)
				+ ";" +  (st.y)
				+ ";" +  (st0.x-st.x)
				+ ";" +  (st0.y-st.y)
				+ ";" +  az0
				+ "#@";
		/*
		if(
				(params.activeProject.getStasiLinkIsComputedBy_id(st0._id) || params.activeProject.getStasiLinkIsSolvedBy_id(st0._id) || overide_computed_demand) 
				&& target_stasi_id==this.stasi_0_id 
			){
			sum = this.stasi_0_angle;
			n=1;
		}
		*/
		
		
		if(
				params.activeProject.getStasiLinkIsComputedBy_id(st0._id) 	|| 
				params.activeProject.getStasiLinkIsSolvedBy_id(st0._id)  	|| 
				overide_computed_demand 
			){
			
				for(i=0;i<=this.itemStaseis.size()-1;i++){
					MyMeasurement res= this.itemStaseis.get(i)	;
					if(res.odefsi_use){
						if(res.stasi_index_id==target_stasi_id){
							if(res.type==0 || res.type==2){
							//Log.i("MyMeasureSet getAzimuthTo0","hz:" + res.hZ);
							
							n+=1;
							sum = sum+res.hZ;
							}
						}
					}
				}
			
			
				if(n>0){
					params.logvWithTime(az0+"");
					params.logvWithTime(sum/n+"");
					out = az0-sum/n;
					if(out<0) out=out+400;
					if(out>=400) out=out-400;
					
				}
		}
		
		this.azimuth2_0 = (float) out;
		//Log.i("MyMeasureSet getAzimuthTo0",this.azimuth2_0 + "#");
		if(n==0){out=-1;};
		return out;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setAzimuth(float angle, int stasi_0){
		this.stasi_0=stasi_0;
		this.stasi_0_id=(int) params.staseis.item.get(stasi_0)._id;
		this.stasi_0_angle=angle;
		
		MyStasi st = params.staseis.item.get(stasi); 
		MyStasi st0 = params.staseis.item.get(stasi_0); 
		//stasi_0=stasiIndex;	
		double[] egsa = params.wms.proj.fl2EGSA87(st.point.y, st.point.x);
		double[] egsa_0 = params.wms.proj.fl2EGSA87(st0.point.y, st0.point.x);
		
		
		
		//Log.i("xy"," : " + egsa[0] + "," + egsa[1]);
		//Log.i("xy"," : " + egsa_0[0] + "," + egsa_0[1]);
		
		//float dx = (float) (st0.point.x-st.point.x);
		//float dy = (float) (st0.point.y-st.point.y);
		
		float dx = (float) (egsa_0[0]-egsa[0]);
		float dy = (float) (egsa_0[1]-egsa[1]);
		
		float logos = dx/dy;
		
		float atan = (float) Math.abs(Math.atan(logos));
		if(dx>0){
			if(dy>0){
				atan=atan;
			}else{
				atan = (float) (Math.PI-atan);
			}
		}else{
			if(dy>0){
				atan = (float) (2*Math.PI-atan);
			}else{
				atan = (float) (Math.PI+atan);
			}
		}
		
		azimuth2stasi_0 = (float) (atan*200/Math.PI);
		azimuth2_0 = azimuth2stasi_0-angle;
		if(azimuth2_0<0) azimuth2_0+=400;
		//Log.i("azimuthio"," : " + azimuth2stasi_0);
		//Log.i("azimuth2_0"," : " + azimuth2_0);
		
		//Log.i("MyMeasureSet setAzimuth",this.azimuth2_0 + "#");
		
		
	}
	
		
	
	
	
	

	
	public void setAzimuth(){
		
		MyStasi st = params.staseis.item.get(stasi); 
		MyStasi st0 = params.staseis.item.get(stasi_0); 
		double[] egsa = params.wms.proj.fl2EGSA87(st.point.y, st.point.x);
		double[] egsa_0 = params.wms.proj.fl2EGSA87(st0.point.y, st0.point.x);
		
		
		float dx = (float) (egsa_0[0]-egsa[0]);
		float dy = (float) (egsa_0[1]-egsa[1]);
		
		float logos = dx/dy;
		
		float atan = (float) Math.abs(Math.atan(logos));
		if(dx>0){
			if(dy>0){
				atan=atan;
			}else{
				atan = (float) (Math.PI-atan);
			}
		}else{
			if(dy>0){
				atan = (float) (2*Math.PI-atan);
			}else{
				atan = (float) (Math.PI+atan);
			}
		}
		
		azimuth2stasi_0 = (float) (atan*200/Math.PI);
		azimuth2_0 = azimuth2stasi_0-this.stasi_0_angle;
		if(azimuth2_0<0) azimuth2_0+=400;
	}
	
		
	
	
	
	
	public double[] addMeasurementFormString(String data,boolean draw){
		MyMeasurement tmp = new MyMeasurement();
		tmp.fromMsg(data);
		tmp.setParent(this);
		tmp.local_date=(int) params.getTime();
		item.add(tmp);
		MyStasi st = params.staseis.item.get(stasi);
		//Log.i("x : " + st.x, "y : " + st.y);
		
		double[] egsa = params.wms.proj.fl2EGSA87(st.point.y, st.point.x);
		double hD = tmp.sD*Math.sin(tmp.vZ*Math.PI/200);
		
		
		//Log.i("azimuth2_0 : " + azimuth2_0, "hZ : " + tmp.hZ + " # " + (azimuth2_0+tmp.hZ));
		
		
		double x = egsa[0]+ hD*Math.sin((azimuth2_0+tmp.hZ)*Math.PI/200);
		double y = egsa[1]+ hD*Math.cos((azimuth2_0+tmp.hZ)*Math.PI/200);
		
		double[] coor = params.wms.proj.Egsa2fl84(x, y);
		if(draw){
			params.web.httpCall(
								"http://eds.culture.gr/_geo/php/_add_point.php?x=" + coor[1] 
										+ "&y=" 	+ coor[0] 
										+ "&p=" 	+ params.activeProject._id
										+ "&hZ=" 	+ tmp.hZ
										+ "&sD=" 	+ tmp.sD
										+ "&vZ=" 	+ tmp.vZ
										+ "&per=" 	+ this._id
										+ "&st=" 	+ params.staseis.item.get(this.stasi)._id								
					);
			/*Log.i("web","http://eds.culture.gr/_geo/php/_add_point.php?x=" + coor[1] 
										+ "&y=" 	+ coor[0] 
										+ "&p=" 	+ params.activeProject._id
										+ "&hZ=" 	+ tmp.hZ
										+ "&sD=" 	+ tmp.sD
										+ "&vZ=" 	+ tmp.vZ
										+ "&per=" 	+ this._id
										+ "&st=" 	+ params.staseis.item.get(this.stasi)._id	);
			Log.i("coor",coor[0] + "," + coor[1]);*/
			params.renderer.points.add(coor[1], coor[0]);
			params.renderer.points.item.get(params.renderer.points.item.size()-1).setMetrisi(tmp);
			params.renderer.points.regen_coor();
		}
		//Log.i("sD : " + tmp.sD, "hZ : " + tmp.hZ);
		//Log.i("hD : " + hD, "vZ : " +tmp.vZ);
		//Log.i("x : " + x, "y : " + y);
		//Log.i("x : " + coor[0], "y : " + coor[1]);
		//params.debug("sdsdsdsd");
		
		return coor;
		
	}
	
	
	

	
	
	
	
	public MyMeasurement addAngleMeasurementFromString(String data,int stasiIndex){
		MyMeasurement tmp = new MyMeasurement();
		Toast.makeText( params.app, data,Toast.LENGTH_LONG).show();
		tmp.fromMsg(data);
		tmp.odefsi_use=false;
		tmp.setParent(this);
		tmp.odefsi_use=false;
		
		tmp.local_date=(int) params.getTime();
		//tmp._id	=	params.app.dbHelper.addMeasurementToDb(tmp, this._id, stasiIndex) ;
		
		tmp.stasi_index=stasiIndex;
		
		
		if(stasiIndex>-1){
			tmp.stasi_index_id = (int) params.staseis.item.get(stasiIndex)._id;
			tmp.odefsi_use=true;
			itemStaseis.add(tmp);
		}else{
			item.add(tmp);
		}
		
		
		
		return tmp;
		
		//Log.i("MyMeasureSet  addAngleMeasurementFromString", "t:" + tmp.type + ",  hZ:" + tmp.hZ + "  ,vZ:" + tmp.vZ + "  ,:" + tmp.sD);
		
		
	}
	
	
	
	
	
	
	
	public double[] addMeasurementFormString(String data,boolean draw,int stasiIndex){
		
		double[] coor ={0}; //an den allaxei exei piasei lathos
		
		params.debug(data);
		Toast.makeText( params.app, data,Toast.LENGTH_LONG).show();
		MyMeasurement tmp = new MyMeasurement();
		if(tmp.fromMsg(data)){
			tmp.stasi_index=stasiIndex;
			tmp.setParent(this);
			tmp.odefsi_use=true;
			tmp.local_date=(int) params.getTime();
			itemStaseis.add(tmp);
			MyStasi st = params.staseis.item.get(stasi);
			
			double[] egsa = params.wms.proj.fl2EGSA87(st.point.y, st.point.x);
			double hD = tmp.hD();//sD*Math.sin(tmp.vZ*Math.PI/200);
			
			double x = egsa[0]+ hD*Math.sin((azimuth2_0+tmp.hZ)*Math.PI/200);
			double y = egsa[1]+ hD*Math.cos((azimuth2_0+tmp.hZ)*Math.PI/200);
			
			 coor = params.wms.proj.Egsa2fl84(x, y);
			if(draw){
				//params.web.httpCall("http://eds.culture.gr/_geo/php/_add_point.php?x=" + coor[1] + "&y=" + coor[0] + "&p=" + params.activeProject._id);
				params.web.httpCall(
						"http://eds.culture.gr/_geo/php/_add_point.php?x=" + coor[1] 
								+ "&y=" 	+ coor[0] 
								+ "&p=" 	+ params.activeProject._id
								+ "&hZ=" 	+ tmp.hZ
								+ "&sD=" 	+ tmp.sD
								+ "&vZ=" 	+ tmp.vZ
								+ "&per=" 	+ this._id
								+ "&st=" 	+ params.staseis.item.get(this.stasi)._id								
			);
			params.renderer.points.add(coor[1], coor[0]);
			params.renderer.points.item.get(params.renderer.points.item.size()-1).setMetrisi(tmp);
			
			params.renderer.points.regen_coor();
			}
			//Log.i("sD : " + tmp.sD, "hZ : " + tmp.hZ);
			//Log.i("hD : " + hD, "vZ : " +tmp.vZ);
			//Log.i("x : " + x, "y : " + y);
			//Log.i("x : " + coor[0], "y : " + coor[1]);
			//params.debug("sdsdsdsd");
		}else{
			
		}
		
		return coor;
		
	}
	
	
	
	public void addMeasurementFormString(String data,int stasiIndex){
		
		if(stasi_0<0){
			MyStasi st = params.staseis.item.get(stasi); 
			MyStasi st0 = params.staseis.item.get(stasiIndex); 
			stasi_0=stasiIndex;	
			
			float dx = (float) (st0.point.x-st.point.x);
			float dy = (float) (st0.point.y-st.point.y);
			
			float logos = dx/dy;
			
			float atan = (float) Math.abs(Math.atan(logos));
			if(dx>0){
				if(dy>0){
					atan=atan;
				}else{
					atan = (float) (Math.PI-atan);
				}
			}else{
				if(dy>0){
					atan = (float) (2*Math.PI-atan);
				}else{
					atan = (float) (Math.PI+atan);
				}
			}
			
			azimuth2stasi_0 = (float) (atan*200/Math.PI);
			
			//Log.i("azimuthio"," : " + azimuth2stasi_0);
		}
		
		MyMeasurement tmp = new MyMeasurement();
		tmp.fromMsg(data);
		tmp.stasi_index=stasiIndex;
		tmp.local_date=(int) params.getTime();
		itemStaseis.add(tmp);
		
		if(stasi_0==stasiIndex){
			recaclulateAzimuth();
		}
		
		
		
	}
	
	
	public void recaclulateAzimuth(){
		setAzimuth(this.stasi_0_angle,this.stasi_0);
		params.debug("recalculate azimuth");;
		int i;
		MyMeasurement tmp;
		float sum=stasi_0_angle;
		int counter=1;
		for(i=0;i<=itemStaseis.size()-1;i++){
			tmp= itemStaseis.get(i);
			if(tmp.stasi_index==stasi_0 && tmp.odefsi_use){
					counter+=1;
					sum+=tmp.hZ;
				//Log.i("rec","#" + tmp.hZ);
			}
			//MyStasi st = params.staseis.item.get(itemStaseis.get(i).stasi_index);
			//Log.i("rec",st)

		}
		azimuth2_0=(float) (400-sum/counter+azimuth2stasi_0);
		if(azimuth2_0>400)azimuth2_0-=400;
		if(azimuth2_0<0)azimuth2_0+=400;
		
		
		params.debug(counter + "mesurements   "+"   #" + sum/counter);
		//Log.i("azimuthio"," : " + azimuth2stasi_0);
		//Log.i("azimuthio"," : " + azimuth2_0);
	}
	
	
	
	
	
	
	
	


	public void update_stasi_index_id( int old_stasi_index_id,long new_stasi_index_id){
		int i;
		for(i=0;i<=this.item.size()-1;i++){
			if(this.item.get(i).stasi_index_id==old_stasi_index_id){
				this.item.get(i).stasi_index_id=(int) new_stasi_index_id;
			}
		}
	}
	
	
	
	
}











