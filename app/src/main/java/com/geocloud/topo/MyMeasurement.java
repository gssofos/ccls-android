package com.geocloud.topo;

import com.geocloud.MyGLES20.MyParams;

import android.util.Log;

public class MyMeasurement {
	public float sD;
	public float hD;
	public float hZ;
	public float vZ;
	public float ys;
	public long _id=0;
	public int type = -1;	//0:all, 1:dist only, 2:angle_only
	public int stasi_index = -1;	//if>0 tote stasi  (local index)
	public int stasi_index_id = -1;	//if>0 tote stasi  (global index)
	public int periodos = -1;		//xrisimopoieitai mono sto load db san voithitiki metavliti
	public 	int			local_date;
	public 	int			date;
	public long global_id=-1;
	
	public MyMeasureSet parent;
	
	
	public boolean 	odefsi_use;	//an einai skopeffsi pros stasi leo an ti xrisimopoio stin epilisi
	public int 		obtype;
	public String 	sxolia;
	
	
	public void setParent (MyMeasureSet parent){
		this.parent=parent;
	}
	public MyMeasurement(){
		obtype=0;
		sxolia="";
		ys=0;
		local_date=-1;
		global_id=-1;
	}
	
	public void setDist(float dist){
		this.sD=dist;
		if(this.type==-1){
			this.type =1;
		}else{
			this.type=0;
		}
	}
	
	
	public void setAngles(float hZ, float vZ){
		this.hZ=hZ;
		this.vZ=vZ;
		
		if(this.type==-1){
			this.type =2;
		}else{
			this.type=0;
		}
		
	}
	
	public boolean fromMsg(String msg){
		Log.i("MyMeasurement",msg);
		String[] data = msg.split(",");
		
		try{
		if(data[0].contains("Ea")){
			
				setAngles(Float.valueOf(data[6].trim()),Float.valueOf(data[5].trim()));
			
			setDist(Float.valueOf(data[4].trim()));
			this.hD = (float) (this.sD*Math.sin(this.vZ*Math.PI/200));
		}
		if(data[0].contains("Ee")){
			setAngles(Float.valueOf(data[5].trim()),Float.valueOf(data[4].trim()));
		}
		
		return true;
		}catch (Exception ex){
			return false;
		}
	}
	
	public void updateMeta(MyParams params){
		params.app.dbHelper.updatePointMeta(sxolia,obtype,_id);
	}
	
	public void db_set_odefsi_use(boolean value){
		parent.params.app.dbHelper.setOdefsiUse(value, _id);
	}
	
	public float hD(){
		return (float) (sD*0.9996*(Math.sin(vZ*Math.PI/200)));
	}
	
	
	public void log(){
		Log.i("MyMeasurement log",
				" id:" + this._id
				+ " d: " + this.date
				//+ " l_d:" + this.local_date
				//+ " hz:" + this.hZ
				//+ " vz:" + this.vZ
				//+ " sD:" + this.sD
				//+ " ys:" + this.ys
				+ " t:" + this.type
				+ " p:" + this.periodos
				+ " s:" + this.stasi_index_id
				
				
				);
		
		
		
		
		
		
		
	}
	
	
}
