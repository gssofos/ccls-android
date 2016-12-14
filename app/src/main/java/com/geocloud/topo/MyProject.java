package com.geocloud.topo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;

public class MyProject {
	private MyParams	params;
	public String 	name;
	public long 	_id;
	public long  	date;
	public String 	sxolia;
	public String 	user;
	public long 	start_stasi;
	public long 	stasi_0;
	

	public long max_stasi_global_id=-1; 
	  
	  
	public long  	staseis_last_sync_date;
	public long  	periodos_last_sync_date;
	public long  	metrisi_last_sync_date;
	
	
	
	public ArrayList<stasi_link_class> 		st = new ArrayList<stasi_link_class>();
	
	public MyProject(MyParams params){
		this.params=params;
		this.start_stasi=-1;
		this.stasi_0=-1;
		this._id=-1;
	}
	
	
	public void clonestasilinkto(ArrayList<stasi_link_class> clonetoobject){
		int i ;
		clonetoobject.clear();
		for(i=0;i<=st.size()-1;i++){
			clonetoobject.add(st.get(i).clone());
		}
	}
	
	

	public void clonestasilinkfrom(ArrayList<stasi_link_class> clonefromobject){
		int i ;
		st.clear();
		for(i=0;i<=clonefromobject.size()-1;i++){
			st.add(clonefromobject.get(i).clone());
		}
	}
	
	
	
	
	public void addStasiLink(long stasi_id){
		boolean found = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			if(st.get(i)._id==stasi_id) found=true;
		}
		if(!found){
			this.st.add(new stasi_link_class(stasi_id));
			params.app.dbHelper.updateStasiToDbProjectTable(params.activeProject.st.get(params.activeProject.st.size()-1));
		}
	}
	
	public void addStasiLink(stasi_link_class st, boolean updatedb){
		
		this.st.add(st);
		if(updatedb) params.app.dbHelper.updateStasiToDbProjectTable(params.activeProject.st.get(params.activeProject.st.size()-1));
	
}

	public void updateStasiLinkisbase(long stasi_id, boolean isbase){
		
		boolean found = false;
		int i;int foundindex=-1;
		for(i=0;i<=st.size()-1;i++){
			if(st.get(i)._id==stasi_id) {
						found=true;
						foundindex=i;
						i=st.size();
			}
			}
		
		if(!found){
			this.st.add(new stasi_link_class(stasi_id));
			st.get(st.size()-1).isbase=isbase;
			params.app.dbHelper.updateStasiToDbProjectTable(st.get(st.size()-1));
		}else{
			st.get(foundindex).isbase=isbase;
			params.app.dbHelper.updateStasiToDbProjectTable(st.get(foundindex));
		}
		
		
		
}

	
	public boolean getStasiLinkIsBaseBy_id(long stasi_id){
		boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			//Log.i("xxxxxxxxxxxxxx","-" + st.get(i).isbase);
			if(st.get(i)._id==stasi_id) {
						out=st.get(i).isbase;
						
						i=st.size();
			}
			}
		return out;
	}
	
	
	
	

	public void setStasiLinkIsComputedBy_id(long stasi_id,boolean value){
		//boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			//Log.i("xxxxxxxxxxxxxx","-" + st.get(i).isbase);
			if(st.get(i)._id==stasi_id) {
						st.get(i).computed=value;
						
						i=st.size();
			}
			}
		//return out;
	}
	
	
	
	


	public void setStasiLinkIsSolvedBy_id(long stasi_id,boolean value){
		//boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			//Log.i("xxxxxxxxxxxxxx","-" + st.get(i).isbase);
			if(st.get(i)._id==stasi_id) {
						st.get(i).solved=value;
						
						i=st.size();
			}
			}
		//return out;
	}
	
	
	
	
	
	

	
	public void setAllStasiLinkIsComputedToFalse(){
		//boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			
						st.get(i).computed=false;
						
						
			}
		//return out;
	}
	
	
	

	
	public void setAllStasiLinkIsSolvedToFalse(){
		//boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			
						st.get(i).solved=false;
						
						
			}
		//return out;
	}
	
	
	
	
	public boolean getStasiLinkIsComputedBy_id(long stasi_id){
		boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			//Log.i("getStasiLinkIsComputedBy_id","# " + st.get(i).computed);
			if(st.get(i)._id==stasi_id) {
						out=st.get(i).computed;
						
						i=st.size();
			}
			}
		return out;
	}
	
	

	public boolean getStasiLinkIsSolvedBy_id(long stasi_id){
		boolean out = false;
		int i;
		for(i=0;i<=st.size()-1;i++){
			//Log.i("getStasiLinkIsComputedBy_id","# " + st.get(i).computed);
			if(st.get(i)._id==stasi_id) {
						out=st.get(i).solved;
						
						i=st.size();
			}
			}
		return out;
	}
	
	

	public void getStasiMaxGlobalId(){
			int i =0;
		this.max_stasi_global_id=-1;
		
		
		
		
		for(i=params.staseis.item.size()-1;i>=0;i--){
			if(params.staseis.item.get(i).global_id>this.max_stasi_global_id){
				this.max_stasi_global_id=params.staseis.item.get(i).global_id;
			}
			
		}
	}
	
	
	
	
	
	
	public void resetActive(){
		//params.logWithTime(this.name);;
	//params.logWithTime(this._id);;
		params.activeProject=this;
		st.clear();
		
		params.app.create_dir(params.baseDir+ "/photo/p" + params.activeProject._id + "/");
		params.app.dbHelper.loadHousesFromDb();
		
		int i =0;
		
		
		
		
		
		for(i=params.staseis.item.size()-1;i>=0;i--){
			if(params.staseis.item.get(i).fixed==false){
				//params.renderer.marker.item.remove(params.staseis.item.get(i).markerIndex);
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).deleted=true;
				params.staseis.item.remove(i);
			}
		}
		
		
		//for(i=params.staseis.item.size()-1;i>=0;i--){
			//if(params.staseis.item.get(i).fixed==false){
				//params.renderer.marker.item.remove(params.staseis.item.get(i).markerIndex);
				//params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).deleted=true;
				//params.staseis.item.remove(i);
			//}
		//}
		//params.staseis.item.clear();
		//params.renderer.marker.clear();
		//params.renderer.points.clear();
		//params.renderer.poly.clear();
		//params.msets.clear();
		
		//params.app.dbHelper.loadStaseisFromDb(true);
		params.app.dbHelper.loadStaseisFromDb(false);
		params.mode.set_stasi_explore();
		params.mode.set_edit_line(false);
		
		this.getStasiMaxGlobalId();
		///params.mode.toggle_stasi();
		
	/*
		for(i=0;i<=params.staseis.item.size()-1;i++){
			
			if(getStasiLinkIsBaseBy_id(params.staseis.item.get(i)._id)){
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=5;
			}else{
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=2;
			}
			//if(params.staseis.item.get(i)._id==this.start_stasi){
			//	params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=5;
			//}else{
			//	params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=2;
			//}
		}
		*/
		
		
	}
	
	
	
	public void updateStasiIcon(){
		
	int i;
		for(i=0;i<=params.staseis.item.size()-1;i++){
			
			if(getStasiLinkIsBaseBy_id(params.staseis.item.get(i)._id)){
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=5;
			}else{
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=2;
			}
			/*if(params.staseis.item.get(i)._id==this.start_stasi){
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=5;
			}else{
				params.renderer.marker.item.get(params.staseis.item.get(i).markerIndex).icon_index=2;
			}*/
		}
		
		
		
	}
	
	
	
	public void sendToMailCR1(){
		String NL = "#@";
		//String out = "Project " + this.name + NL + NL;
		String out ="";
		MyMeasurement tmp;
		
		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			out = out + "STN     " + params.staseis.item.get(params.msets.get(i).stasi).name() + "," +  params.msets.get(i).YO + NL;
			
			/*out = out
			+ "SS      "+  params.staseis.item.get(params.msets.get(i).stasi_0).name() + ",0.0," + NL
			+ "HV      " + params.msets.get(i).stasi_0_angle
			+ ",100.0000"
			+ NL;*/
			
			//out = out + "SS      S" + params.staseis.item.get(params.msets.get(i).stasi_0)._id + ",0.00" +  params.msets.get(i).stasi_0_angle + NL;
			for(k=0;k<=params.msets.get(i).itemStaseis.size()-1;k++){
				tmp = params.msets.get(i).itemStaseis.get(k);
				if(tmp.type==2){
					if(tmp.odefsi_use){
						out = out
								+ "SS      "+  params.staseis.item.get(tmp.stasi_index).name() + "," + tmp.ys + "," + NL
								
								+ "HV      " + tmp.hZ
								+ "," +  tmp.vZ 
								+ NL;
					}
				}else{
					if(tmp.odefsi_use){
						out = out
								+ "SS      "+  params.staseis.item.get(tmp.stasi_index).name() + "," + tmp.ys + "," + NL
								
								+ "SD      " + tmp.hZ
								+ "," +  tmp.vZ 
								+ "," +  tmp.sD 
								+ NL;
					}
				}
				
				
			}
			
			String pro = "";
			
			for(k=0;k<=params.msets.get(i).item.size()-1;k++){
				tmp = params.msets.get(i).item.get(k);
				
				if(tmp.obtype==1){
					pro="f";
				}else{
					pro="";
				}
				
				if(tmp.type==2){
					out = out
							
							+ "SS      "+ pro + tmp._id + "," + tmp.ys + "," + NL
							
							+ "HV      " + tmp.hZ
							+ "," +  tmp.vZ 
							+ NL;
				}else{
					out = out
							
							+ "SS      "+ pro + tmp._id + "," + tmp.ys + "," + NL
							
							+ "SD      " + tmp.hZ
							+ "," +  tmp.vZ 
							+ "," +  tmp.sD 
							+ NL;
				}
				
						
					
			}
			
			
			out = out + NL;
			
		}
		
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost(params.c_url + "_geo/php/client/_export_file.php",out);;
		
		params.debug("send to mail");
	}
	
	
	
	

	public void sendToMailOdefsi(){
		params.web.httpCallPost(params.c_url + "_geo/php/client/_export_odefsi.php",params.odefsi_solution_csv_string);;
		params.debug("send to mail odefsi");
	}
	
	

	public void sendToMailKML(){
		String NL = "#@";
		//String out = "Project " + this.name + NL + NL;
		String out ="";
		
		out = out
				+	"<?xml version='1.0' encoding='UTF-8'?>" + NL
				+ 	"<kml xmlns='http://www.opengis.net/kml/2.2' xmlns:gx='http://www.google.com/kml/ext/2.2' xmlns:kml='http://www.opengis.net/kml/2.2' xmlns:atom='http://www.w3.org/2005/Atom'>" + NL
				+	"<Document>" + NL
				+	"<name>geoCloud.kml</name>" + NL
				+	"<Style id='Style_1'>" + NL
				+	"<LineStyle>" + NL
				+	"<color>ffffaaff</color>" + NL
				+	"<width>2.5</width>" + NL
				+	"</LineStyle>" + NL
				+	"</Style>" + NL
				+	"<Placemark>" + NL
				+	"<name>geoCloud</name>" + NL
				+	"<styleUrl>#Style_1</styleUrl>" + NL
				+	"<MultiGeometry>" + NL
				
				;
				
		int i,k;
		for(i=0;i<=params.renderer.my_poly.item.size()-1;i++){
			MyPoly tmp = params.renderer.my_poly.item.get(i);
			
			out = out
					+	"<LineString>" + NL
					+ 	"<tessellate>1</tessellate>" + NL
					+	"<coordinates>" + NL
					+	tmp.kmlCoor() + NL
					+	"</coordinates>" + NL
					+	"</LineString>" + NL
					;
			
			
		}
		
		
		

		out = out
				+	"</MultiGeometry>" + NL
				+ 	"</Placemark>" + NL
				+	"</Document>" + NL
				+	"</kml>"
				;
		
		
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost(params.c_url + "_geo/php/client/_export_kml_file.php",out);;
		
		params.debug("send to mail");
	}
	
	
	
	
	
	
	


	public void sendToMailDXF(){
		String NL = "#@";
		//String out = "Project " + this.name + NL + NL;
		String out ="";
		
		
				
		int i,k;
		for(i=0;i<=params.renderer.my_poly.item.size()-1;i++){
			MyPoly tmp = params.renderer.my_poly.item.get(i);
			
			out = out
					+	tmp.dxfCoor() + NL
					
					;
			
			
		}
		
		
		
		Log.i("lklklkl",out);
		
		
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost(params.c_url + "_geo/php/client/geo_export/_export_dxf.php",out,0,15000);;
		
		params.debug("send to mail");
	}
	
	
	
	
	
	
	
	



	public void clear(){
		params.app.dbHelper.clearProject((int) _id);
	}
	
	
	
	
	
	
	
	
	public void sendToMailStaseis(){
		String NL = "#@";
		//String out = "Project " + this.name + NL + NL;
		String out ="";
		out = params.app.dbHelper.getAllStaseisToCSV(_id);
		out.replace("\n", NL);
		params.web.httpCallPost(params.c_url + "_geo/php/client/_export_csv.php",out);;
		params.debug("send to mail");
	}
	
	
	
	
	public void sendToMail(){
		String NL = "#@";
		String out = "Project " + this.name + NL + NL;


		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			out = out + "stasi : (" + params.staseis.item.get(params.msets.get(i).stasi)._id +") " + params.staseis.item.get(params.msets.get(i).stasi).name + ", yo : " +  params.msets.get(i).YO + NL;
			out = out + "midenismos : " + params.msets.get(i).stasi_0 + ", angle : " +  params.msets.get(i).stasi_0_angle + NL;
			for(k=0;k<=params.msets.get(i).itemStaseis.size()-1;k++){
				out = out
						+ "hZ : " + params.msets.get(i).itemStaseis.get(k).hZ
						+ ", vZ : " +  params.msets.get(i).itemStaseis.get(k).vZ 
						+ ", sD : " +  params.msets.get(i).itemStaseis.get(k).sD 
						+ ", st : " +  params.staseis.item.get(params.msets.get(i).itemStaseis.get(k).stasi_index )._id 
						
						+ NL;
				
			}
			
			for(k=0;k<=params.msets.get(i).item.size()-1;k++){
				out = out
						+ "hZ : " + params.msets.get(i).item.get(k).hZ
						+ ", vZ : " +  params.msets.get(i).item.get(k).vZ 
						+ ", sD : " +  params.msets.get(i).item.get(k).sD 
						//+ ", st : " + params.staseis.item.get(params.msets.get(i).item.get(k).stasi_index )._id 
						
						+ NL;
				
			}
			
			
			out = out + NL;
			
		}
		
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost(params.c_url + "_geo/php/client/_export_file.php",out);;
		
		params.debug("send to mail");
	}
	
	
	
	
	
	

	public void syncStaseis(){
		
		//Idio array kai seira me to web callback_1_MyProject_syncStaseisToCloud
			
			
		String NL = "#";
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#";

		int i,k;
		for(i=0;i<=params.staseis.item.size()-1;i++){
			int a;
			MyStasi st = params.staseis.item.get(i);
			
			if(this.getStasiLinkIsBaseBy_id( st._id ) ){
				 a =1;
			}else{
				 a=0;
			}
			
			if(!st.fixed){
				//if(st._t_tabId==Integer.valueOf(params.tabId)){
						String mytbId="-1";
						if(st._t_tabId>0){
							mytbId=String.valueOf(st._t_tabId);
						}else{
							mytbId=params.tabId;
						}
						String tmp =  st._id 
								+ "," 	+  st.f 
								+ "," 	+  st.l 
								+ "," 	+  st.x 
								+ "," 	+  st.y 
								+ "," 	+  st.sxolia 
								+ "," 	+  st.date 
								+ "," 	+  a
								+ "," 	+  st.name 
								+ "," 	+  mytbId 
								+ ","   + st._t_id
								+ ","   + st.local_date
								+ NL;
						out = out +tmp;
				//}
				
			}
			
		}
		params.logWithTime(out);
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost(params.c_url + "_geo/php/client/_syncStaseis.php",out,300,5000);;
		
	}
	
	
	
	
	

	public void syncStaseisToCloud(boolean all){
		
		//Idio array kai seira me to web callback_1_MyProject_syncStaseisToCloud
			
			
		String NL = "#";
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#";

		int i,k;
		for(i=0;i<=params.staseis.item.size()-1;i++){
			int a;
			MyStasi st = params.staseis.item.get(i);
			
			if(this.getStasiLinkIsBaseBy_id( st._id ) ){
				 a =1;
			}else{
				a=0;
			}
			
			if(!st.fixed){
				String mytbId="-1";
				if(st._t_tabId>0){
					mytbId=String.valueOf(st._t_tabId);
				}else{
					mytbId=params.tabId;
				}
				String tmp =  st._id 
						+ "," 	+  st.f 
						+ "," 	+  st.l 
						+ "," 	+  st.x 
						+ "," 	+  st.y 
						+ "," 	+  st.sxolia 
						+ "," 	+  st.date 
						+ "," 	+  a
						+ "," 	+  st.name 
						+ "," 	+  mytbId 
						+ ","   + st._t_id
						+ NL;
				
				out = out +tmp;
				
			}
			
		}
		Log.i("syncStaseisToCloud",out);
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		if(all){
		params.web.httpCallPost(params.c_url + "_geo/php/client/_syncStaseisToCloud.php",out,100,5000);;
		}
		else{
			params.web.httpCallPost(params.c_url + "_geo/php/client/_syncStaseisToCloud.php",out,101,5000);;
			
			//params.debug("send to mail");
		}
	}
	
	
	


	


	public void syncPeriodoi(){

		String NL = "#"; 
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#";
		Log.i("syncPeriodoi","syncPeriodoi");
		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			MyMeasureSet per = params.msets.item.get(i);
			String mytbId="-1";
			if(per._t_tabId>0){
				mytbId=String.valueOf(per._t_tabId);
			}else{
				mytbId=params.tabId;
			}
			
			int a;
			
			
			
			out = out 
								+ per._id 
							+ "," 	+  params.staseis.get_stasi_by_id(per.stasi_id).global_id
							+ "," 	+   params.staseis.get_stasi_by_id(per.stasi_0_id ).global_id
							+ "," 	+  per.stasi_0_angle
							+ "," 	+  per.YO 
							+ "," 	+  mytbId 
							+ "," 	+  per._t_id 
							+ "," 	+  per.date 
							+ "," 	+  per.itemStaseis.size() 
							+ "," 	+  per.item.size()  
							+ "," 	+  per.local_date
							
							+ "," 	;
			
			//Log.i("-------------------",per.itemStaseis.size()+" :: " + per.stasi_id);
			int ki;
			int ou = 0;
			/*
			for(ki=0;ki<=per.itemStaseis.size()-1;ki++){
				MyMeasurement me = per.itemStaseis.get(ki);
				ou = 0;
				//Log.i("-------------++++++", me.hZ+"");
				if( me.odefsi_use){ ou=1;};
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "1"		//prokeitai gia skopefsi odefsis
						+ "$";
			}
			*/
			out = out +",";
			
			/*
			for(k=0;k<=per.item.size()-1;k++){
				MyMeasurement me = per.item.get(k);
				ou = 0;
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "0"
						+ "$";
			}
			
			*/
			
			out = out +NL;
			
			
		}
		Log.i("syncPeriodoi data",out);
		  //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		//params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_syncPeriodoiToCloud.php",out,2,30000);;
		params.web.httpCallPost(params.c_url + "_geo/php/client/_syncPeriodoi.php",out,200,60000);;
		//params.debug("send to mail");
	}
	
	
	
	
	

	
	

	public void syncMetriseis(long periodos_id){

		String NL = "#"; 
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#" ;
		Log.i("syncMetriseis","syncMetriseis");
		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			
			MyMeasureSet per = params.msets.item.get(i);
			if(per._id==periodos_id){
					
				String mytbId="-1";
					if(per._t_tabId>0){		mytbId=String.valueOf(per._t_tabId);	}else{	mytbId=params.tabId;	}
					
					out = out 
										+  per.global_id
									+ "," 	+  mytbId 
									+ "," 	+  per.local_date
									+ "," 	;
					//Log.i("-------------++++++", out);
					int ki;
					int ou = 0;
					
					if(per._t_tabId==Integer.valueOf(params.tabId)){
								for(ki=0;ki<=per.itemStaseis.size()-1;ki++){
									MyMeasurement me = per.itemStaseis.get(ki);
									ou = 0;
									//Log.i("-------------++++++", me.hZ+"");
									if( me.odefsi_use){ ou=1;};
									out = out 
											+ me._id
											+ "|" + params.staseis.get_stasi_by_id(me.stasi_index_id).global_id
											+ "|" + me.type
											+ "|" + me.hZ
											+ "|" + me.vZ
											+ "|" + me.sD
											+ "|" + me.ys
											+ "|" + me.obtype
											+ "|" + me.sxolia
											
											
											+ "|" + ou
											+ "|" + "1"		//prokeitai gia skopefsi odefsis
											+ "|" + me.local_date
											+ "|" + me.date
											
											+ "$";
								}
								
								out = out +",";
								
								
								for(k=0;k<=per.item.size()-1;k++){
									MyMeasurement me = per.item.get(k);
									ou = 0;
									out = out 
											+ me._id
											+ "|" + params.staseis.get_stasi_by_id(me.stasi_index_id).global_id
											+ "|" + me.type
											+ "|" + me.hZ
											+ "|" + me.vZ
											+ "|" + me.sD
											+ "|" + me.ys
											+ "|" + me.obtype
											+ "|" + me.sxolia
											
											
											+ "|" + ou
											+ "|" + "0"
											+ "|" + me.local_date
											+ "|" + me.date
											+ "$";
								}
								
								
								
								out = out +NL;
					}else{
						
						for(ki=0;ki<=per.itemStaseis.size()-1;ki++){
							MyMeasurement me = per.itemStaseis.get(ki);
							ou = 0;
							Log.i("-------------++++++", per._t_tabId +"..." + Integer.valueOf(params.tabId)+"");
							if( me.odefsi_use){ ou=1;};
							out = out 
									+ params.staseis.get_stasi_by_id(me.stasi_index_id).global_id
									+ "|" + me.local_date
									+ "|" + me.date
									
									+ "$";
						}
						
						out = out +",";
						
						
						for(k=0;k<=per.item.size()-1;k++){
							MyMeasurement me = per.item.get(k);
							ou = 0;
							out = out 
									+ params.staseis.get_stasi_by_id(me.stasi_index_id).global_id
									+ "|" + me.local_date
									+ "|" + me.date
									+ "$";
						}
						
						
						
						out = out +NL;
						
						
						
						
						
						
						
						
						
						
						
					}
			
		}
		}
		//Log.i("syncMetriseis data",out);
		//  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		//params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_syncPeriodoiToCloud.php",out,2,30000);;
		params.web.httpCallPost(params.c_url + "_geo/php/client/_syncMetriseis.php",out,400,60000);;
		//params.debug("send to mail");
	}
	
	
	
	

	public void syncMetriseis(){

		String NL = "#"; 
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#";
		Log.i("syncMetriseis","syncMetriseis");
		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			MyMeasureSet per = params.msets.item.get(i);
			syncMetriseis(per._id);
			
			
			
			/*
			
			String mytbId="-1";
			if(per._t_tabId>0){
				mytbId=String.valueOf(per._t_tabId);
			}else{
				mytbId=params.tabId;
			}
			
			
			out = out 
								+  per.global_id
							+ "," 	+  mytbId 
							+ "," 	+  per.local_date
							+ "," 	;
			
			int ki;
			int ou = 0;
			
			for(ki=0;ki<=per.itemStaseis.size()-1;ki++){
				MyMeasurement me = per.itemStaseis.get(ki);
				ou = 0;
				//Log.i("-------------++++++", me.hZ+"");
				if( me.odefsi_use){ ou=1;};
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "1"		//prokeitai gia skopefsi odefsis
						+ "$";
			}
			
			out = out +",";
			
			
			for(k=0;k<=per.item.size()-1;k++){
				MyMeasurement me = per.item.get(k);
				ou = 0;
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "0"
						+ "$";
			}
			
			
			
			out = out +NL;
			
			*/
		}
		Log.i("syncMetriseis data",out);
		  //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		//params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_syncPeriodoiToCloud.php",out,2,30000);;
		//params.web.httpCallPost(params.c_url + "_geo/php/client/_syncMetriseis.php",out,400,60000);;
		//params.debug("send to mail");
	}
	
	
	
	
	
	public void syncPeriodoiToCloud(){

		String NL = "#"; 
		//String out = "Project " + this.name + NL + NL;
		String out =_id + "#" + params.tabId + "#";
		Log.i("syncPeriodoiToCloud","syncPeriodoiToCloud");
		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			MyMeasureSet per = params.msets.item.get(i);
			String mytbId="-1";
			if(per._t_tabId>0){
				mytbId=String.valueOf(per._t_tabId);
			}else{
				mytbId=params.tabId;
			}
			
			int a;
			
			
			
			out = out 
								+ per._id 
							+ "," 	+  per.stasi_id
							+ "," 	+  per.stasi_0_id 
							+ "," 	+  per.stasi_0_angle
							+ "," 	+  per.YO 
							+ "," 	+  mytbId 
							+ "," 	+  per._t_id 
							+ "," 	+  per.date 
							+ "," 	+  per.itemStaseis.size() 
							+ "," 	+  per.item.size()  
							
							+ "," 	;
			
			Log.i("-------------------",per.itemStaseis.size()+" :: " + per.stasi_id);
			int ki;
			int ou = 0;
			/*
			for(ki=0;ki<=per.itemStaseis.size()-1;ki++){
				MyMeasurement me = per.itemStaseis.get(ki);
				ou = 0;
				//Log.i("-------------++++++", me.hZ+"");
				if( me.odefsi_use){ ou=1;};
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "1"		//prokeitai gia skopefsi odefsis
						+ "$";
			}
			*/
			out = out +",";
			
			/*
			for(k=0;k<=per.item.size()-1;k++){
				MyMeasurement me = per.item.get(k);
				ou = 0;
				out = out 
						+ me._id
						+ "|" + me.stasi_index_id
						+ "|" + me.type
						+ "|" + me.hZ
						+ "|" + me.vZ
						+ "|" + me.sD
						+ "|" + me.ys
						+ "|" + me.obtype
						+ "|" + me.sxolia
						
						
						+ "|" + ou
						+ "|" + "0"
						+ "$";
			}
			
			*/
			
			out = out +NL;
			
			
		}
		Log.i("syncPeriodoiToCloud data",out);
		  //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		//params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_syncPeriodoiToCloud.php",out,2,30000);;
		params.web.httpCallPost(params.c_url + "_geo/php/client/_syncPeriodoiToCloud.php",out,2,60000);;
		//params.debug("send to mail");
	}
	
	
	
	
	public void syncToCloud(){
		syncStaseisToCloud(true);
		
		
		
		/*
		String NL = "#";
		//String out = "Project " + this.name + NL + NL;
		String out ="";

		int i,k;
		for(i=0;i<=params.msets.size()-1;i++){
			out = out + "STN     S" + params.staseis.item.get(params.msets.get(i).stasi)._id + "," +  params.msets.get(i).YO + NL;
			
			out = out
			+ "SS      S"+  params.staseis.item.get(params.msets.get(i).stasi_0)._id + ",0.0," + NL
			+ "HV      " + params.msets.get(i).stasi_0_angle
			+ ",100.0000"
			+ NL;
			
			//out = out + "SS      S" + params.staseis.item.get(params.msets.get(i).stasi_0)._id + ",0.00" +  params.msets.get(i).stasi_0_angle + NL;
			for(k=0;k<=params.msets.get(i).itemStaseis.size()-1;k++){
				out = out
						+ "SS      S"+  params.staseis.item.get(params.msets.get(i).itemStaseis.get(k).stasi_index)._id + ",0.0," + NL
						
						+ "SD      " + params.msets.get(i).itemStaseis.get(k).hZ
						+ "," +  params.msets.get(i).itemStaseis.get(k).vZ 
						+ "," +  params.msets.get(i).itemStaseis.get(k).sD 
						+ NL;
				
			}
			
			String pro = "";
			
			for(k=0;k<=params.msets.get(i).item.size()-1;k++){
				if(params.msets.get(i).item.get(k).obtype==5){
					pro="f";
				}else{
					pro="";
				}
				out = out
						
						+ "SS      "+ pro + params.msets.get(i).item.get(k)._id + ",0.0," + NL
						
						+ "SD      " + params.msets.get(i).item.get(k).hZ
						+ "," +  params.msets.get(i).item.get(k).vZ 
						+ "," +  params.msets.get(i).item.get(k).sD 
						+ NL;
						
					
			}
			
			
			out = out + NL;
			
		}
		
	      //  nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
		params.web.httpCallPost("http://eds.culture.gr/_geo/php/client/_export_file.php",out);;
		
		params.debug("send to mail");*/
	}
	
	
	
	
	
}



