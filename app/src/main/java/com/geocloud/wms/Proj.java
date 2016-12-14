package com.geocloud.wms;

import com.geocloud.app.MyApp;

public class Proj {
	MyApp appState;
	
	public double wgs84_a = 6378137;
			
	
	public double wgs84_f = 1 / 298.257223563;
	public double wgs84_e = Math.sqrt(2*wgs84_f-wgs84_f*wgs84_f);
	public double wgs84_b = 6356752.314;
	

	public double eg_a = 6378137;
	public double eg_f =  1 / 298.257223563;					//1 / 298.257222101;
	public double eg_e = Math.sqrt(2*wgs84_f-wgs84_f*wgs84_f);	//Math.sqrt(0.00669438);
	public double eg_b = 6356752.314;

	public double pi = Math.PI;

	public  double rad(double x) {return x*Math.PI/180;}
	

	public  double[] flh2xyz(double f,double l,double h){

		double  a	=	eg_a ;
		double 	e	=	eg_e;
	
		double n = a / (Math.sqrt(1 - (e * e * Math.sin(f * pi / 180) * Math.sin(f * pi / 180))));

		double x = (n + h) * Math.cos(f * pi / 180) * Math.cos(l * pi / 180);
		double y = (n + h) * Math.cos(f * pi / 180) * Math.sin(l * pi / 180);
		double z = (n * (1 - e * e) + h) * Math.sin(f * pi / 180);


		double out[] = {0,0,0};// = new Array();
		out[0]=x;
		out[1]=y;
		out[2]=z;

		return out;

}
	
	
	

	public  double[] xyz2flh(double x,double y,double z){

	
	//if ( Ell == undefined ) { Ell = "WGS84";}
		double a=eg_a;
		double e=eg_e;
		double ff=eg_f;
	//if(Ell=="WGS84"){a=eg_a;e=eg_e;ff=eg_f;}
	//if(Ell=="ED50"){a=ED_a;e=ED_e;ff=ED_f;}

		double l = Math.atan(y / x) ;
	
	
		double ge=e*e/(1-e*e);
		double gb=a*(1-ff);
		double gp=Math.sqrt(x*x+y*y);
		double gq=Math.atan((z*a)/(gp*gb));
		double f = Math.atan(    (z + ge*gb*Math.sin(gq)*Math.sin(gq)*Math.sin(gq)) / (gp- e*e*a*Math.cos(gq)*Math.cos(gq)*Math.cos(gq)  )     );


		double h = (gp / Math.cos(f))-a/Math.sqrt(1 - ( e * e * Math.sin(f) * Math.sin(f) ) );


		double out[]={0,0,0};
		out[0]=f*180/pi;
		out[1]=l* 180 / pi;
		out[2]=h;

		return out;

}


	

	public  double[] fl2EGSA87(double f,double l){
	
		double par = Math.sqrt(1 - (eg_e * eg_e * Math.sin(f*pi/180) * Math.sin(f*pi/180)));
		double n = eg_a / (Math.sqrt(1 - (eg_e * eg_e * Math.sin(f*pi/180) * Math.sin(f*pi/180))));

		double h=0;
		double W_x = (n + h) * Math.cos(f * pi / 180) * Math.cos(l * pi / 180);
		double W_y = (n + h) * Math.cos(f * pi / 180) * Math.sin(l * pi / 180);
		double W_z = (n * (1 - eg_e * eg_e) + h) * Math.sin(f * pi / 180);

		double dx=199.723;
		double dy=-74.03;
		double dz=-246.018;

		double E_x = W_x+dx;
		double E_y = W_y+dy;
		double E_z = W_z+dz;



		double E_mik = Math.atan(E_y / E_x) * 180 / pi;

		double F0 = Math.atan(E_z / ((1 - eg_e * eg_e) * Math.sqrt(E_x * E_x + E_y * E_y)));
		double f1 = Math.atan((E_z + eg_e * eg_e * (eg_a / par) * Math.sin(F0)) / (Math.sqrt(E_x * E_x + E_y * E_y)));

	while(Math.abs(F0 - f1) > 0.000000001 * pi / 180) 
		{
			F0 = f1;
			f1 = Math.atan((E_z + eg_e * eg_e * (eg_a / par) * Math.sin(F0)) / (Math.sqrt(E_x * E_x +E_y * E_y)));
		}

	double E_plat = f1 * 180 / pi;


	l=E_mik*pi/180;
	f=E_plat*pi/180; 

	n = eg_a / (Math.sqrt(1 - (eg_e * eg_e * Math.sin(f) * Math.sin(f))));


	double M0 = 1 + 3 * eg_e * eg_e / 4 + 45 * eg_e * eg_e * eg_e * eg_e / 64 + 175 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 256 + 11025 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 16384;
	double m2 = 3 * eg_e * eg_e / 8 + 15 * eg_e * eg_e * eg_e * eg_e / 32 + 525 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 1024 + 2205 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 4096;
	double M4 = 15 * eg_e * eg_e * eg_e * eg_e / 256 + 105 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 1024 + 2205 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 8820;
	double M6 = 35 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 3072 + 315 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 12288;
	double M8 = 315 * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e * eg_e / 130784;

	double m = eg_a * (1 - eg_e * eg_e) * (M0 * f - m2 * Math.sin(2 * f) + M4 * Math.sin(4 * f) - M6 * Math.sin(6 * f) + M8 * Math.sin(8 * f));


	double k0 = 0.9996;
	double L0 = 24 * pi / 180;

	double et = (Math.sqrt((eg_a * eg_a - eg_b * eg_b) / (eg_b * eg_b)));


	double y = k0 * m + k0 * n * Math.cos(f) * Math.sin(f) * (l - L0) * (l - L0) / 2;
		y = y + k0 * n * Math.sin(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * (5 - Math.tan(f) * Math.tan(f) + 9 * et * Math.cos(f) * Math.cos(f)) * (l - L0) * (l - L0) * (l - L0) * (l - L0) / 24;
		y = y + k0 * Math.sin(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * (61 - 58 * Math.tan(f) * Math.tan(f) + Math.tan(f) * Math.tan(f) * Math.tan(f) * Math.tan(f)) * (l - L0) * (l - L0) * (l - L0) * (l - L0) * (l - L0) * (l - L0) / 720;




		double x = 500000 + k0 * n * Math.cos(f) * (l - L0);
		x = x + k0 * n * Math.cos(f) * Math.cos(f) * Math.cos(f) * (1 - Math.tan(f) * Math.tan(f) + et * et * Math.cos(f) * Math.cos(f)) * (l - L0) * (l - L0) * (l - L0) / 6;
		x = x + k0 * n * Math.cos(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * Math.cos(f) * (-18 * Math.tan(f) * Math.tan(f) + Math.tan(f) * Math.tan(f) * Math.tan(f) * Math.tan(f) + 14 * et * et * Math.cos(f) * Math.cos(f) - 58 * et * et * Math.sin(f) * Math.sin(f)) * (l - L0) * (l - L0) * (l - L0) * (l - L0) * (l - L0) / 120;

	//alert(x);
		double out[]={0,0,0};
		out[0]=x;
		out[1]=y;
		return out;

}


	

	public  double[] Egsa2fl84(double x, double y) {


		double out[]={0,0};

	double k0 = 0.9996;


	double F0;
	double dy;
	double df;
	double m;

	double DX;
    DX = x - 500000;

    double L0;
    L0 = 24;

	F0 = 10;
	dy = 10;
	df = 10;

	while (Math.abs(dy) > 0.0000005){

		double e = eg_e;
		double a = eg_a;

		double M0 = 1 + 3 * Math.pow(e , 2) / 4 + 45 * Math.pow(e , 4) / 64 + 175 * Math.pow(e , 6) / 256 + 11025 * Math.pow(e , 8) / 16384;
		double m2 = 3 * Math.pow(e , 2) / 8 + 15 * Math.pow(e , 4) / 32 + 525 * Math.pow(e , 6) / 1024 + 2205 * Math.pow(e , 8) / 4096;
		double M4 = 15 * Math.pow(e , 4) / 256 + 105 * Math.pow(e , 6) / 1024 + 2205 * Math.pow(e, 8) / 8820;
		double M6 = 35 * Math.pow(e , 6) / 3072 + 315 * Math.pow(e, 8) / 12288;
		double M8 = 315 * Math.pow(e ,8) / 130784;

		m = a * (1 - Math.pow(e , 2)) * (M0 * F0 * pi / 180 - m2 * Math.sin(2 * F0 * pi / 180) + M4 * Math.sin(4 * F0 * pi / 180) - M6 * Math.sin(6 * F0 * pi / 180) + M8 * Math.sin(8 * F0 * pi / 180));


    if (y / k0 - m > 0){ 
		F0 = F0 + df;}
        
		if (y / k0 - m < 0){
			df = df / 2;
            F0 = F0 - df;
        }

	dy = y / k0 - m;
}

	//F0=F0;

	double ee;
    ee = eg_e;
    double et;
    et=Math.sqrt(ee *ee / (1 - ee*ee));
    double aa;
    aa = eg_a;
    //double bb;
   // bb = eg_b;
    

    double N0;
	N0 = aa / Math.sqrt(1 - ee * ee * Math.sin(F0 * pi / 180) * Math.sin(F0 * pi / 180));

	double p0;
	p0 =  aa * (1 - ee * ee) / Math.sqrt(Math.pow((1 - ee * ee * Math.sin(F0* pi / 180) * Math.sin(F0* pi / 180)) , 3));

	double t0;
	t0 = Math.tan(F0 * pi / 180);

   

	double  nn0;
	nn0 = Math.sqrt(et * et * Math.cos(F0 * pi / 180) * Math.cos(F0 * pi / 180));

	double f;
	double l;



	f = F0 - ((t0 / (2 * k0 * k0 * N0 * p0))) * DX  * DX  * 180 / pi;
	f = f + ((t0 / (24 * k0 * k0 * k0 * k0 * p0 * N0 * N0 * N0)) * (5 + 3 * t0 * t0 + nn0 * nn0 - 4 * nn0 * nn0 * nn0 * nn0 - 9 * t0 * t0 * nn0 * nn0)) * DX * DX * DX * DX * 180 / pi;
	f = f - ((t0 / (720 * Math.pow(k0, 6) * p0 * Math.pow(N0, 5))) * (61 + 90 * Math.pow(t0, 2) + 45 * Math.pow(t0 , 4))) * Math.pow(DX , 6) * 180 / pi;

	l = L0 + (1 / (k0 * N0 * Math.cos(F0 * pi / 180))) * DX * 180 / pi;
	l = l - (1 / (6 * Math.pow(k0, 3) * Math.pow(N0 ,3) * Math.cos(F0 * pi / 180))) * (1 + 2 * Math.pow(t0 , 2) + Math.pow(nn0 , 2)) * Math.pow(DX , 3) * 180 / pi;
	l = l + (1 / (120 * Math.pow(k0 , 5) * Math.pow(N0, 5) * Math.cos(F0 * pi / 180))) * (5 + 6 * Math.pow(nn0 ,2) + 28 * Math.pow(t0 , 2) + 8 * Math.pow(t0, 2) * Math.pow(nn0, 2) + 24 * Math.pow(t0 , 4) - 3 * Math.pow(nn0 , 4) - 4 * Math.pow(nn0 , 6) + 4 * Math.pow(t0 , 2) * Math.pow(nn0 , 4) + 24 * Math.pow(t0, 2) * Math.pow(nn0, 6)) * Math.pow(DX , 5) * 180 / pi;
	//l = l - (1 / (5040 *  Math.pow(k0 ,7) * Math.pow(N0, 7) * Math.cos(F0 * pi / 180))) * (61 + 66 *  Math.pow(t0 ,2) + 1320 *  Math.pow(t0 , 4) + 720 *  Math.pow(t0 , 6)) *  Math.pow(DX, 7) * 180 / pi;


	double par = Math.sqrt(1 - (eg_e * eg_e * Math.sin(f*pi/180) * Math.sin(f*pi/180)));
	double n = eg_a / (Math.sqrt(1 - (eg_e * eg_e * Math.sin(f*pi/180) * Math.sin(f*pi/180))));

	double h=0;
	double E_x = (n + h) * Math.cos(f * pi / 180) * Math.cos(l * pi / 180);
	double E_y = (n + h) * Math.cos(f * pi / 180) * Math.sin(l * pi / 180);
	double E_z = (n * (1 - eg_e * eg_e) + h) * Math.sin(f * pi / 180);

	double dx=199.723;
	dy=-74.03;
	double dz=-246.018;

	double W_x = E_x-dx;
	double W_y = E_y-dy;
	double W_z = E_z-dz;

	double E_mik = Math.atan(W_y / W_x) * 180 / pi;

	F0 = Math.atan(E_z / ((1 - eg_e * eg_e) * Math.sqrt(W_x * W_x + W_y * W_y)));
	
	double f1 = Math.atan((W_z + eg_e * eg_e * (eg_a / par) * Math.sin(F0)) / (Math.sqrt(W_x * W_x + W_y * W_y)));

	while(Math.abs(F0 - f1) > 0.000000001 * pi / 180) 
		{
			F0 = f1;
			f1 = Math.atan((W_z + eg_e * eg_e * (eg_a / par) * Math.sin(F0)) / (Math.sqrt(W_x * W_x +W_y * W_y)));
		}

	double E_plat = f1 * 180 / pi;


	l=E_mik;
	f=E_plat; 

	out[0]=f;
	out[1]=l;

	return out;
            
}
}
