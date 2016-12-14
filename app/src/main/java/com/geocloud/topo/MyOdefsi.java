package com.geocloud.topo;

import java.util.ArrayList;

import android.util.Log;

import com.geocloud.Geometry.MyPoint;
import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;

public class MyOdefsi {
	MyParams 	params;
	distSet 	distset;
	azimuthSet 	azimuthset;
	
	public MyOdefsi(MyParams params){
		this.params		=	params;
		this.distset	=	new distSet();
		this.azimuthset	=	new azimuthSet();
		
	}
	
	
	
	
	
	
	
	
	public void set_dist_set(){
		distset		.item.clear();
		int i,j;
		//kataxoro pairs
				for(i=0;i<=params.msets.size()-1;i++){
					MyMeasureSet tmp2 = params.msets.get(i);	
					//tmp2.recaclulateAzimuth();
					for(j=0;j<=tmp2.itemStaseis.size()-1;j++){
						MyMeasurement tmp = tmp2.itemStaseis.get(j);
						//params.logvWithTime(tmp.stasi_index_id + "--" + tmp.odefsi_use);
						if(tmp.odefsi_use){
							//params.logvWithTime(tmp.stasi_index_id + "--+");
						if(tmp.type==0 || tmp.type==1) 
									distset.add		(tmp2.stasi, tmp.stasi_index, tmp.hD(),tmp._id);  //local index
						}
					}
				}
				//ipologizo dist mo
				distset.compute_stats();
				
				//distset.log2(params);;
				//distset.log(params);			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void compute_stasi(long stasi_id, long prev_stasi_id){
		if(
					params.activeProject.getStasiLinkIsBaseBy_id(stasi_id) || 
					params.activeProject.getStasiLinkIsComputedBy_id(stasi_id)	){
			
						
			params.activeProject.clonestasilinkto(params.staseis.get_stasi_by_id(stasi_id).stigmiotipokarastasisstaseonkatatinepilisi);
			params.staseis.get_stasi_by_id(stasi_id).prevstasikatatinepilisi=prev_stasi_id;
			
			params.activeProject.setStasiLinkIsSolvedBy_id(stasi_id,true);
			
			
			int i,j,k;
			MyMeasureSet tmp;
			MyMeasurement tmp3 ;
			
			this.azimuthset.clearPairsWithStasi1LocalIndex(params.staseis.stasi_id_to_index(stasi_id));
						
			
			for(i=0;i<=params.msets.size()-1;i++){				//gia kathe periodo tis stasis
				tmp = params.msets.get(i);			//pairno stasi
				//params.logWithTime("per to compute : "  + tmp._id);
				
				long i_Stasi_id = params.staseis.item.get(tmp.stasi)._id;			//stasi_id tis periodou
				if(i_Stasi_id== stasi_id){											//an stasi id antistoixei sti periodo
					//tmp.recaclulateAzimuth();										//ipologise azimouthia periodou (azimuth to 0
					
					if(params.activeProject.getStasiLinkIsBaseBy_id(stasi_id)){		//An einai base i stasi ipologise azimuthuia mono apo stasi enarxis
						tmp.getAzimuthTo0FromStasiIndex(tmp.stasi_0_id,true);
					}else{															//An den einai base i stasi ipologise azimuthuia apo ola
						tmp.getAzimuthTo0();
					}
					
					
					
					
					for(j=0;j<=tmp.itemStaseis.size()-1;j++){
						tmp3 = tmp.itemStaseis.get(j);
						if(tmp3.odefsi_use){
						if(tmp3.type==0 || tmp3.type==2) 
										azimuthset.add	(tmp.stasi, tmp3.stasi_index, tmp.azimuth2_0+tmp3.hZ);  //local index
					}
					}
					
				}
			}
			
			
			
			
			
			for(i=0;i<=azimuthset.item.size()-1;i++){
				int st1,st2;
				st1 = azimuthset.item.get(i).st1;
				st2 = azimuthset.item.get(i).st2;
				
				long i_Stasi_id = params.staseis.item.get(st1)._id;
				if(i_Stasi_id== stasi_id){
					
					azimuthset.item.get(i).compute_stats();
					int tmp6 = distset.pairIndex(st1,st2);
					if(tmp6>-1){
						azimuthset.item.get(i).mo_dist=distset.item.get(tmp6).mo_dist;
						azimuthset.item.get(i).s0_dist=distset.item.get(tmp6).s0;
						azimuthset.item.get(i).min_dist=distset.item.get(tmp6).min;
						azimuthset.item.get(i).max_dist=distset.item.get(tmp6).max;
					}else{
						azimuthset.item.get(i).mo_dist=-1;
						azimuthset.item.get(i).s0_dist=-1;
						azimuthset.item.get(i).min_dist=-1;
						azimuthset.item.get(i).max_dist=-1;
					}
					
					
					/*
					params.odefsi_solution_csv_string+=
							"mo_az_di;" 
							+ ";" + params.staseis.item.get(st1).name() 
							+ ";" + params.staseis.item.get(st2).name() 
							//+ ";" +  params.staseis.item.get(st1).YO 
							+ ";" +  azimuthset.item.get(i).mo_azimuth
							+ ";" +  azimuthset.item.get(i).mo_dist
							+ "#@";
					for(j=0;j<=azimuthset.item.get(i).item.size()-1;j++){
						params.odefsi_solution_csv_string+=
								"az i;" 
								+ ";" //+ params.staseis.item.get(st1).name() 
								+ ";"// + params.staseis.item.get(st2).name() 
								//+ ";" +  params.staseis.item.get(st1).YO 
								+ ";" +  azimuthset.item.get(i).item.get(j)
								+ ";" //+  azimuthset.item.get(i).mo_dist
								+ "#@";
					}
					
					*/
					
				}
					
					
					
					
					
				}
			
		
			
			//params.logWithTime("stasi to compute : "  + params.staseis.get_stasi_by_id(stasi_id).name());
			
			
					
					
			MyMeasurement 	target;		
					
			for(i=0;i<=params.msets.size()-1;i++){
				tmp = params.msets.get(i);
				long i_Stasi_id = params.staseis.item.get(tmp.stasi)._id;
				if(i_Stasi_id== stasi_id){
					
					
					
					for(k=0;k<=tmp.itemStaseis.size()-1;k++){
						target 			= tmp.itemStaseis.get(k);
						long 			targetStasi_id 	= params.staseis.item.get(target.stasi_index)._id;
						
						if(targetStasi_id!=prev_stasi_id){
							if(!params.activeProject.getStasiLinkIsComputedBy_id(targetStasi_id))  //to index sto measure einai local
							{
								//Log.i("stasi computing : " ,stasi_id + "--> " + targetStasi_id);
								//an oxi computed
								int st1 = tmp.stasi;				//local index
								int st2 = target.stasi_index;		//local index
								//Log.i("stasi computing : " ,st1 + "--> " + st2);
										
								int azimuthPairIndex = azimuthset.pairIndex(st1, st2);
								//Log.i("azimuthPairIndex : " ,""+azimuthPairIndex);
								
								if(azimuthPairIndex>-1){
										
										pairAzimuth azs = 			azimuthset.item.get(azimuthPairIndex);
										//Log.i("azimuthPairIndex : " ,""+azs.item.get(0));
										double azimuth 	= 	azs.mo_azimuth;		
										double dist 	=  	azs.mo_dist;
										
										if(dist>0){
											params.logvWithTime(azimuth + "");
										//azimuthset.item.get(tmp5).mo_dist;
												
												
												int 	markerIndex 	= params.staseis.item.			get(st1).markerIndex;
												double 	x0 				= params.renderer.marker.item.	get(markerIndex).x;
												double 	y0 				= params.renderer.marker.item.	get(markerIndex).y;
												
												double[] 	egsa={0,0};
												
												if( params.staseis.item.			get(st1).x>0){
													x0 =  params.staseis.item.			get(st1).x;
													y0 =  params.staseis.item.			get(st1).y;
													egsa[0]=x0;
													egsa[1]=y0;
													
												}else{
													egsa 	= params.wms.proj.fl2EGSA87(y0, x0);
											
												}
												
												double 		x 		= egsa[0]+ dist*Math.sin((azimuth)*Math.PI/200);
												double 		y 		= egsa[1]+ dist*Math.cos((azimuth)*Math.PI/200);
												
												double[] fl 	= params.wms.proj.Egsa2fl84(x, y);
												markerIndex 	= params.staseis.item.get(st2).markerIndex;
												MyPoint tmp7 	= params.renderer.marker.item.get(markerIndex);
												//MyPoint tmp7egsa 	= params.renderer.marker.itemegsa.get(markerIndex);
												tmp7.x=fl[1];
												tmp7.y=fl[0];
												//tmp7egsa.x=x;
												//tmp7egsa.y=y;
												double[] xy={x,y};
												/*
												params.odefsi_solution_csv_string+=
														"compute coor;" 
														+ ";" + params.staseis.item.get(st1).name() 
														+ ";" + params.staseis.item.get(st2).name() 
														//+ ";" +  params.staseis.item.get(st1).YO 
														+ ";" + azimuth
														+ ";" + dist
														+ ";" +  egsa[0]
														+ ";" + egsa[1]
														+ ";" + x
														+ ";" + y
														+ "#@";
												*/
												
												params.staseis.item.get(st2).	setCoorFl(fl,xy);
												//params.staseis.item.get(st2).	setCoorEgsa(xy);
												params.activeProject.			setStasiLinkIsComputedBy_id(targetStasi_id,true);
												
												if(!params.activeProject.getStasiLinkIsSolvedBy_id(targetStasi_id)){
													compute_stasi(targetStasi_id,stasi_id);
												}
												
												
												
								}	
										
								}
							}
					}
						}
					
					
					
				}
			}
			
			
			
		}
	}
	
	
	
	
	
	
	public void redraw(boolean tax){
		
		params.odefsi_solution_csv_string="" + "#@";
		
		int i,j;
		params.logWithTime("redraw entry");
		if(tax) params.renderer.points.item.clear();
		
		params.activeProject.setAllStasiLinkIsComputedToFalse();
		params.activeProject.setAllStasiLinkIsSolvedToFalse();
		azimuthset	.item.clear();
		//params.logWithTime("redraw initialized variables");
		set_dist_set();
		params.logWithTime("distset computed");
		
		//Sarono kathe stasi pou anoikei sto project kai arxizo na lino apo baseis
		for(i=0;i<=params.activeProject.st.size()-1;i++){
			if(params.activeProject.st.get(i).isbase){
				//Log.i("base found", "" + params.activeProject.st.get(i)._id);
				//params.logWithTime("base found   " + params.activeProject.st.get(i)._id);
				
				//this.compute_stasi(params.activeProject.st.get(i)._id,-1);
				this.compute_stasi(params.activeProject.st.get(i)._id,-1);
				
				/*params.odefsi_solution_csv_string+= "#@#@#@#@";*/
			}
			
		}
		
		
		this.azimuthset.assign_azimuth_to_staseis(params);
		this.azimuthset.assign_colors_to_staseis(params);
		this.distset.color_odefsi(params);
		params.logWithTime("odefsi computed");
		
		params.renderer.marker.regen_coor();
		params.renderer.odefsi_poly.regen_coor();
		
		params.logWithTime("coor regenerated");
		
		if(tax) this.draw_tax();
		
		params.surface.requestRender();
		
		
	}
	
	
	
	
	
	
	
	

	public void redrawfromstasi(boolean tax, MyStasi st){
		
		
		
		int i,j;
		params.logWithTime("redrawfromstasi entry");
		if(tax) params.renderer.points.item.clear();
		
		params.activeProject.clonestasilinkfrom(st.stigmiotipokarastasisstaseonkatatinepilisi);
		//params.activeProject.st=(ArrayList<stasi_link_class>) st.stigmiotipokarastasisstaseonkatatinepilisi.clone();
		//params.logWithTime("redraw initialized variables");
		set_dist_set();
		params.logWithTime("distset computed");
		
			this.compute_stasi(st._id,st.prevstasikatatinepilisi);
			
		
		this.azimuthset.assign_azimuth_to_staseis(params);
		this.azimuthset.assign_colors_to_staseis(params);
		this.distset.color_odefsi(params);
		params.logWithTime("odefsi computed");
		
		params.renderer.marker.regen_coor();
		params.renderer.odefsi_poly.regen_coor();
		
		params.logWithTime("coor regenerated");
		
		if(tax) this.draw_tax();
		
		params.surface.requestRender();
		
		
	}
	
	
	
	
	
	
	public void draw_tax(){
		params.logWithTime("draw_tax entry");
		params.renderer.points.item.clear();
		
		
		int i,j;
		for(i=0;i<=params.msets.size()-1;i++){
			MyMeasureSet tmp2 = params.msets.get(i);
			String stasi_name = params.staseis.get_stasi_by_id(params.msets.get(i).stasi_id).name();
			double azimuth2_0 = tmp2.azimuth2_0;
			double yo = tmp2.YO;
			double x0 = params.renderer.marker.item.get(params.msets.get(i).stasi).x;
			double y0 = params.renderer.marker.item.get(params.msets.get(i).stasi).y;
			double[] egsa = params.wms.proj.fl2EGSA87(y0, x0);
			if(Math.abs(params.staseis.get_stasi_by_id(params.msets.get(i)._id).x-x0)<1){  //gia na xero pos exo arxikopoisei xy
				egsa[0] = params.staseis.get_stasi_by_id(params.msets.get(i)._id).x;
				egsa[1]=params.staseis.get_stasi_by_id(params.msets.get(i)._id).y;
			}
			
			/*params.odefsi_solution_csv_string+=
					";" 
					+ ";" +stasi_name
					+ ";" + yo
					//+ ";" +  params.staseis.item.get(st1).YO 
					+ ";" 
					+ ";"
					+ ";"// +  egsa[0]
					+ ";"// + egsa[1]
					+ ";" + Math.round(egsa[0]*1000.0)/1000.0
					+ ";" + Math.round(egsa[1]*1000.0)/1000.0
					+ "#@";
				*/
			
			

			for(j=0;j<=tmp2.itemStaseis.size()-1;j++){
				MyMeasurement tmp = tmp2.itemStaseis.get(j);
				double x = egsa[0]+ tmp.hD()*Math.sin((azimuth2_0+tmp.hZ)*Math.PI/200);
				double y = egsa[1]+ tmp.hD()*Math.cos((azimuth2_0+tmp.hZ)*Math.PI/200);
				String dh;
				dh="";
				if(tmp.ys>-1){dh = Math.round((tmp.sD*Math.cos(tmp.vZ*Math.PI/200)+yo-tmp.ys)*1000.00)/1000.00+"";}
				/*params.odefsi_solution_csv_string+=
						";" 
						+ ";" + stasi_name
						+ ";" + params.staseis.get_stasi_by_id(tmp.stasi_index_id).name()
						+ ";" + (azimuth2_0+tmp.hZ)
						+ ";" + tmp.hD()
						+ ";"// +  egsa[0]
						+ ";"// + egsa[1]
						+ ";" + Math.round(x*100.0)/100.0
						+ ";" + Math.round(y*100.0)/100.0
						+ ";" + tmp.obtype
						+ ";" + tmp.sxolia
						+ ";" + tmp.hZ
						+ ";" + tmp.vZ
						+ ";" + Math.round(tmp.sD*100.0)/100.0
						+ ";" + Math.round(tmp.hD*100.0)/100.0
						+ ";" + Math.round(tmp.ys*100.0)/100.0
						+ ";" + dh
						+ "#@";*/
			}
			
			
			
					
			for(j=0;j<=tmp2.item.size()-1;j++){
				MyMeasurement tmp = tmp2.item.get(j);
				
				double x = egsa[0]+ tmp.hD()*Math.sin((azimuth2_0+tmp.hZ)*Math.PI/200);
				double y = egsa[1]+ tmp.hD()*Math.cos((azimuth2_0+tmp.hZ)*Math.PI/200);
				
				double[] coor = params.wms.proj.Egsa2fl84(x, y);
				
				params.renderer.points.add(coor[1], coor[0]);
				//Log.i("point ob typr","t : " + tmp.obtype);
				params.renderer.points.item.get(params.renderer.points.item.size()-1).setMetrisi(tmp);
				
				//params.web.httpCall("http://eds.culture.gr/_geo/php/_add_point.php?x=" + coor[1] + "&y=" + coor[0] + "&p=" + params.activeProject._id);
				String dh;
				dh="";
				if(tmp.ys>-1){
					dh = Math.round((tmp.sD*Math.cos(tmp.vZ*Math.PI/200)+yo-tmp.ys)*1000.00)/1000.00+"";
				
				}
				/*
				params.odefsi_solution_csv_string+=
						";" 
						+ ";" + stasi_name
						+ ";" + tmp._id
						//+ ";" +  params.staseis.item.get(st1).YO 
						+ ";" + (azimuth2_0+tmp.hZ)
						+ ";" + tmp.hD()
						+ ";"// +  egsa[0]
						+ ";"// + egsa[1]
						+ ";" + Math.round(x*100.0)/100.0
						+ ";" + Math.round(y*100.0)/100.0
						+ ";" + tmp.obtype
						+ ";" + tmp.sxolia
						//+ ";" +tmp.type
						+ ";" + tmp.hZ
						+ ";" + tmp.vZ
						+ ";" + Math.round(tmp.sD*100.0)/100.0
						+ ";" + Math.round(tmp.hD*100.0)/100.0
						+ ";" + Math.round(tmp.ys*100.0)/100.0
						+ ";" + dh
						+ "#@";*/
			}
			
		
			params.surface.requestRender();
			
		}
		
		params.logWithTime("draw_tax cpmpleted");
	}
	
	
	
}
	
	



class distSet{
	
	ArrayList<pairDist> 		item = new ArrayList<pairDist>();
	
	public void add(int st1,int st2, double dist,long mes_id){
		int i;
		int found = -1;
		for(i=0;i<=item.size()-1;i++){
			int tmp = pairIndex(st1,st2);
			if(tmp==-1){
				
			}else{
				item.get(tmp).addDist(dist,mes_id);
				found=i;
				i=999999;
			}
		}
		
		if(found==-1){
			item.add(new pairDist(st1,st2,dist,mes_id));
			//Log.i("aaa","added");
		}else{
			
			//Log.i("aaa","not added");
		}
		
	}
	
	
	public int pairIndex(int st1, int st2){
		int i;
		int out = -1;
		
		for(i=0;i<=item.size()-1;i++){
			if(item.get(i).is(st1, st2)){
				out=i;
				i=999999;
			}
		}
		return out;
	}
	
	public void compute_stats(){
		int i;
		for(i=0;i<=item.size()-1;i++){
			item.get(i).compute_stats();
		}
	}
	
	public void log(MyParams params){
		int i;
		
		
		for(i=0;i<=item.size()-1;i++){
			pairDist pd = item.get(i);
			
					Log.i("MyOdefsi : "  + params.staseis.item.get(pd.st1)._id+ "-" +  params.staseis.item.get(pd.st2)._id ,"" + pd.mo_dist + " , " + pd.s0);
			//if(item.get(i).is(st1, st2)){
				//out=i;
				//i=999999;
			//}
		//}
	}
	}
	
	public void log2(MyParams params){
		int i;
		pairDist p;
		
		MyStasi st1,st2;
		for(i=0;i<item.size()-1;i++){
			p=item.get(i);
			st1 = params.staseis.item.get(p.st1);
			st2 = params.staseis.item.get(p.st2);
			st1.log();st2.log();params.logvWithTime("---");
		}
	}
	public void color_odefsi(MyParams params){
		params.renderer.odefsi_poly.clear();
		int i;
		pairDist p;
		MyPoly poly;
		MyStasi st1,st2;
		for(i=0;i<=item.size()-1;i++){
			p=item.get(i);
			if((p.max-p.min)>0.05){
				poly = params.renderer.odefsi_poly.add(1f, 0f, 0f);
			}else{
				poly = params.renderer.odefsi_poly.add(0.0f,0.9f, 0.0f);
			}
			
			st1 = params.staseis.item.get(p.st1);
			st2 = params.staseis.item.get(p.st2);
			poly.addVertice(st1.l, st1.f);
			poly.addVertice(st2.l,st2.f);
			
			
		}
		
		
		
		
		
		
	}
}




class pairDist{
	ArrayList<Double> 			item = new ArrayList<Double>();
	ArrayList<Long> 			mes_id = new ArrayList<Long>();
	public int st1;
	public int st2;
	public double mo_dist=0;
	public double s0;
	public double min = -1;
	public double max=-1;
	
	public pairDist(int st1, int st2, double dist,long mes_id){
		this.st1=st1;
		this.st2=st2;
		addDist(dist,mes_id);
	}
	
	public void addDist(double dist,long mes_id){
		item.add(dist);
		this.mes_id.add(mes_id);
	}
	
	public boolean is(int a, int b){
		boolean out = false;
		if(st1==a){
			if(st2==b) out=true;
		}
		
		if(st2==a){
			if(st1==b) out=true;
		}
		
		return out;
	}
	
	public void compute_stats(){
		double sum=0;
		s0=0;
		int counter = 0;
		int j;
		
		this.min = item.get(0);
		this.max = item.get(0);
		
			for(j=0;j<=item.size()-1;j++){
				double tmp3 = item.get(j);
				if(min>tmp3) min=tmp3;
				if(max<tmp3) max=tmp3;
				sum+=tmp3;
				counter+=1;
			}
			
			mo_dist = sum/counter;
			
			counter=0;
			for(j=0;j<=item.size()-1;j++){
				s0+=(mo_dist- item.get(j))*(mo_dist- item.get(j));
				counter+=1;
			}
			if(counter==1){
				s0 = 0d;
			}else{
				s0 = Math.sqrt(s0/(counter-1));
			}
			
			
			
	}
	
	
	
}


















class azimuthSet{
	
	ArrayList<pairAzimuth> 		item = new ArrayList<pairAzimuth>();
	
	
	public void clearPairsWithStasi1LocalIndex(long st1){
		int i;
		
		for(i=item.size()-1;i>=0;i--){
			if(item.get(i).st1==st1){
				item.remove(i);
			}
		}
		
	}
	public void add(int st1,int st2, double azimuth){
		
		if(azimuth>400) azimuth-=400;
		if(azimuth<0) azimuth+=400;
		
		int i;
		int found = -1;
		for(i=0;i<=item.size()-1;i++){
			int tmp = pairIndex(st1,st2);
			if(tmp==-1){
				
			}else{
				item.get(tmp).addAzimuth(azimuth);
				found=i;
				i=999999;
			}
		}
		
		if(found==-1){
			item.add(new pairAzimuth(st1,st2,azimuth));
		}
		
	}
	
	
	public int pairIndex(int st1, int st2){
		int i;
		int out = -1;
		
		for(i=0;i<=item.size()-1;i++){
			if(item.get(i).is(st1, st2)){
				out=i;
				i=999999;
			}
		}
		return out;
	}
	
	
	
	
	public void assign_colors_to_staseis(MyParams params){
		//Log.i("assign_colors_to_staseis",this.item.size() + "");
		int i ;
		pairAzimuth ap;
		
		//ola mple
		for(i=0;i<=params.staseis.item.size()-1;i++){
				params.staseis.item.get(i).paint(0f, 0f, 255f);
		}
		
		//an vrethei variation erro kane kokkin
		//den to kano se ena vima giati krataei tin teleytai timi pou briskei kata ti sarosi
		for(i=0;i<=this.item.size()-1;i++){
			ap = this.item.get(i);
			if(ap.variationError()){
				params.staseis.item.get(ap.st1).paint(255f, 0f, 0f);
			}
		}
		params.renderer.stasiPoints.regen_coor();
		
	}
	
	
	
	
	public void assign_azimuth_to_staseis(MyParams params){
		int i ;
		MyStasi st;
		for(i=0;i<=params.staseis.item.size()-1;i++){
			params.staseis.item.get(i).azimuth.clear();
		}
		
		for(i=0;i<=this.item.size()-1;i++){
			params.staseis.item.get(this.item.get(i).st1).azimuth.add(this.item.get(i));
		}
	}
	
}




class pairAzimuth{
	ArrayList<Double> 		item = new ArrayList<Double>();
	public int st1;
	public int st2;
	public double mo_dist =0;
	public double s0_dist;
	public double min_dist = -1;
	public double max_dist=-1;
	
	
	public double mo_azimuth=0;
	public double s0_azimuth;
	public double min_azimuth = -1;
	public double max_azimuth=-1;
	
	
	public pairAzimuth(int st1, int st2, double dist){
		this.st1=st1;
		this.st2=st2;
		addAzimuth(dist);
	}
	
	public void addAzimuth(double dist){
		item.add(dist);
	}
	
	public boolean is(int a, int b){
		boolean out = false;
		if(st1==a){
			if(st2==b) out=true;
		}
		
		return out;
	}
	
	
	public boolean variationError(){
		if((this.max_azimuth-this.min_azimuth)>=0.0030){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	public void compute_stats(){
		double sum=0;
		s0_azimuth=0;
		int counter = 0;
		int j;
		
		this.min_azimuth = item.get(0);
		this.max_azimuth = item.get(0);
		
			for(j=0;j<=item.size()-1;j++){
				double tmp3 = item.get(j);
				if(min_azimuth>tmp3) min_azimuth=tmp3;
				if(max_azimuth<tmp3) max_azimuth=tmp3;
				sum+=tmp3;
				counter+=1;
			}
			
			mo_azimuth = sum/counter;
			
			counter=0;
			for(j=0;j<=item.size()-1;j++){
				s0_azimuth+=(mo_azimuth- item.get(j))*(mo_azimuth- item.get(j));
				counter+=1;
			}
			if(counter==1){
				s0_azimuth = 0d;
			}else{
				s0_azimuth = Math.sqrt(s0_azimuth/(counter-1));
			}
			
			
			
	}
	
	
	
	
}
