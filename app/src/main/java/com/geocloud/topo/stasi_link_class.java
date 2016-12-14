package com.geocloud.topo;

public class stasi_link_class {
	public long _id;
	public boolean computed;	//an exei ipologistei apo alli stasi
	public boolean solved;	//an exei lithei apo afti ti stasi i geometria
	public boolean isbase;		// as einai bae
	
	public stasi_link_class(long _id){
		this._id		=	_id;
		this.computed	=	false;
		this.isbase 	= 	false;
		this.solved		=	false;
	}
	
	public stasi_link_class clone(){
		stasi_link_class out = new stasi_link_class(this._id);
		out.computed=this.computed;
		out.solved=this.solved;
		out.isbase=this.isbase;
		return out;
		
		
		
	}
}
