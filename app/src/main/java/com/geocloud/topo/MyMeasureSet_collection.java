package com.geocloud.topo;

import java.util.ArrayList;

import com.geocloud.MyGLES20.MyParams;
import com.geocloud.app.MyGLSE20app;

public class MyMeasureSet_collection {
	public ArrayList<MyMeasureSet> item = new ArrayList<MyMeasureSet>();
	MyParams	params;
	public MyMeasureSet_collection(MyParams params){
		this.params = params;
	}
	
	
	public void add(MyMeasureSet set){
		this.item.add(set);
	}
	
	public MyMeasureSet get(int index){
		return this.item.get(index);
	}
	
	public int size(){
		return this.item.size();
	}
	
	public void clear(){
		this.item.clear();
	}
	
	
	

	
	public void update_stasi_index_id_of_metriseis( int old_stasi_index_id,long new_stasi_index_id){
		int i;
		for(i=0;i<=this.item.size()-1;i++){
			
				this.item.get(i).update_stasi_index_id(old_stasi_index_id, new_stasi_index_id);;
			
		}
	}
	
	
	
	
	public void update_stasi_index_id( int old_stasi_index_id,long new_stasi_index_id){
		int i;
		for(i=0;i<=this.item.size()-1;i++){
			if(this.item.get(i).stasi_id==old_stasi_index_id){
				this.item.get(i).stasi_id=(int) new_stasi_index_id;
			}
		}
	}
	
	
	

	public void update_stasi_0_index_id( int old_stasi_0_index_id,long new_stasi_0_index_id){
		int i;
		for(i=0;i<=this.item.size()-1;i++){
			if(this.item.get(i).stasi_0_id==old_stasi_0_index_id){
				this.item.get(i).stasi_0_id=(int) new_stasi_0_index_id;
			}
		}
	}
	
	
	
	
	
	
	public boolean stasi_id_is_has_period(long _id){
		int i;
		boolean found=false;
		for(i=0;i<=this.item.size()-1;i++){
			if(params.staseis.item.get(this.item.get(i).stasi)._id==_id){
				//if(this.item.get(i)._id==_id){
						found = true;
				i=this.item.size();
			}
		}
		return found;
	}
	
	
	

	public boolean stasi_id_is_refered_by_other_periods(long _id){
		int i;
		boolean found=false;
		//for(i=0;i<=this.item.size()-1;i++){
			//if(params.staseis.item.get(this.item.get(i).stasi)._id==_id){
				//if(this.item.get(i)._id==_id){
				//		found = true;
				//i=this.item.size();
			//}
		//}
		return found;
	}
	
	
	
	

	
	public long get_period_id_of_period_having_global_id(long global_id){
		int i;
		long out = -1;
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i).global_id==global_id) {	
					out = item.get(i)._id;
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	
	public MyMeasureSet get_period_having_global_id(long global_id){
		int i;
		MyMeasureSet out = null;
		//Log.i("stasi collection coor trigger",x + "," +y);
		for(i=0;i<=item.size()-1;i++){
			if( item.get(i).global_id==global_id) {	
					out = item.get(i);
					i=item.size();
			}
			
		}
		
		return out;
	}
	
	public int stasi_id_count_of_periods(long _id){
		int i;
		int count=0;
		for(i=0;i<=this.item.size()-1;i++){
			if(params.staseis.item.get(this.item.get(i).stasi)._id==_id){
				count++;
			}
		}
		return count;
	}
	
	public MyStasi MyStasi_of_fist_period_of_stasi_by_id(long _id){
		//_id einai to _id tis stasis
		//dinei to local index tis stasis0 tis protis periodou gia ti stasi me _id==_id
		//ousiastika thelo gia mia stasi tin stsi protou midensmou kai ti xrisimopoio gia to move
		
		MyStasi out = null;
		int i;
		for(i=0;i<=item.size()-1;i++){
			if(params.staseis.item.get(item.get(i).stasi)._id==_id){
				out = params.staseis.item.get(item.get(i).stasi_0);
			}
		}
		
		return out;
		
		
	}
}
