package com.geocloud.app;

import java.io.File;

public class MyUtility {

	public void checkCreateFolder(String path){
		try {
			if(!new java.io.File(path).isDirectory()){
				File directory = new File(path); 
				directory.mkdirs();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
