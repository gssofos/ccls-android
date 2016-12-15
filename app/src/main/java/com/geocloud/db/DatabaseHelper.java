package com.geocloud.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;



















import com.geocloud.Geometry.MyPoly;
import com.geocloud.MyGLES20.MyParams;
import com.geocloud.topo.MyHouse;
import com.geocloud.topo.MyMeasureSet;
import com.geocloud.topo.MyMeasurement;
import com.geocloud.topo.MyProject;
import com.geocloud.topo.MyStasi;
import com.geocloud.topo.stasi_link_class;
import com.geocloud.wms.Proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.geocloud/databases/";
 
    private static String DB_NAME = "geoclouddb";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
    private MyParams params;
    
    boolean flat_mode;
    String baseDir;
    
    
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context, MyParams params) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.params = params;
        flat_mode = false;
        this.baseDir = params.app.baseDir;
    }	
    
    
    
    public boolean isOpen(){
    	return myDataBase.isOpen(); 
    }
    public DatabaseHelper(Context context, String baseDir) {
    	 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        //this.params = params;
        flat_mode = true;
        this.baseDir=baseDir;
    }	
    
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    
   
    
    
    public void createDataBase() throws IOException{
    	Log.i("DatabaseHelper - checkDataBase  dbExist ","pre");
    	boolean dbExist = checkDataBase();
 
    	Log.i("DatabaseHelper - checkDataBase  dbExist ",""+dbExist);
    	
    	if(dbExist){
    		//do nothing - database already exist
    		//copyDataBase();
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
    	
    	
    	
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
    	boolean out = false;
    	String myPath = baseDir + "/"  + DB_NAME;
    	//final String filename 	= String.valueOf(String.format(baseDir + "/%s.ttg",(String) tmp[1]));
      	//final String[] 	data 	= new String[2];
      					//data[0]	= myurl;
      					//data[1]	= filename;
    	 Log.i("exists_? " +  myPath,""+new java.io.File(myPath).exists());
      	if(new java.io.File(myPath).exists() ){
      			out=true;
      	}else{
    	/*try{
    		
    		//String myPath = params.app.baseDir + "/"  + DB_NAME;
    		
    		Log.i("DatabaseHelper - checkDataBase  checkDB ",""+myPath);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    		Log.i("DatabaseHelper - checkDataBase  checkDB ",""+checkDB);
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
    		Log.i("DatabaseHelper - checkDataBase  error ",""+e.toString());
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}*/}
      	return out;
    	//return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	//Log.i("-","1");
    	// Path to the just created empty db
    	//String outFileName = DB_PATH + DB_NAME;
    	//String outFileName = params.app.baseDir + "/" + DB_NAME;
    	String outFileName = baseDir + "/" + DB_NAME;
    	Log.i("DatabaseHelper - copy database ",outFileName);
    	
    	//Open the empty db as the output stream
    	OutputStream myOutput;
		try {
			Log.i("DatabaseHelper - copy database ","1");
			myOutput = new FileOutputStream(outFileName);
			Log.i("DatabaseHelper - copy database ","2");
    	
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	long datalength = 0;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    		datalength+=length;
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    	
    	Log.i("DatabaseHelper - copy database ","completed " +  datalength + " bytes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("DatabaseHelper - copyDataBase  error ",""+e.toString());
			e.printStackTrace();
		}
    	//Log.i("-","2");
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
    	// String myPath = params.app.baseDir + "/" + DB_NAME;
    	 String myPath = baseDir + "/" + DB_NAME;
    	 Log.i("DatabaseHelper",myPath);
    	 
     	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
     	/*
     	myDataBase.delete("staseis", "project=3", null); 
     	myDataBase.delete("periodos", "project=3", null); 
     	myDataBase.delete("line", "project=3", null); 
     myDataBase.delete("metrisi", null, null); 
     	myDataBase.delete("con_project_stasi", "project=3", null); 
     	 */
     	//myDataBase.execSQL("ALTER TABLE metrisi	ADD odefsi_use INTEGER DEFAULT 0");
     	//myDataBase.execSQL("update metrisi	SET stasi_index=3 WHERE stasi_index=1029");
     	//myDataBase.execSQL("update periodos 	SET stasi=3 WHERE stasi=1029");
     	//myDataBase.execSQL("update periodos 	SET stasi_0=3 WHERE stasi_0=1029");
     	
     	//ContentValues values = new ContentValues(); 
		//values.put("odefsi_use", 1); 
     	//myDataBase.update("metrisi", values, "stasi_index>-1", null);
    
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
	
	
	
	

	  

	  public void loadStaseisFromDb(boolean fixed) {
	   // List<Category> categories = new ArrayList<Category>();
	    String[] columns = {
	    						"_id","name","type","fixed","shor","sxolia","user",
	    						"x","y","f","l",
	    						"date","foto","h","h_ort",
	    						"sx","sy","sh","global_id","_t_id","_t_tabId","_t_project","_t_base","local_date","tocast"};
	   
	    //Log.i("",myDataBase.toString());
	  //  Log.i("DB Load staseis",params.activeProject._id + " p id, fixed " + fixed);
	    Cursor cursor;
	    if(fixed){
	    	cursor = myDataBase.query( "staseis",  columns, "fixed=1", null, null, null, null);
	    }else{
	    	//Log.i("DB Load staseis",params.activeProject._id + " p id, fixed " + fixed);
	    	cursor = myDataBase.query( "staseis",  columns, "fixed=0 AND project=" + params.activeProject._id  , null, null, null, null);
	    }
	    
	    //Log.i("DB Load staseis","num : " + cursor.getCount());
	    
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	MyStasi stasi = cursorToStasi(cursor,fixed);
	    	stasi.log();
	    	//categories.add(category);
	    	params.staseis.item.add(stasi);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    //return categories;
	  }
	  
	  
	  
	  
	  
	  
	 
	  


	  public void loadHousesFromDb() {
	   // List<Category> categories = new ArrayList<Category>();
	    String[] columns = {
	    						"_id","sxolia","path","type","floors","f","l","x","y"
	    						};
	   
	    //Log.i("",myDataBase.toString());
	   // Log.i("DB Load staseis",params.activeProject._id + " p id, fixed ");
	    Cursor cursor;
	   
	    	cursor = myDataBase.query( "houses",  columns, "project=" + params.activeProject._id, null, null, null, null);
	    
	    Log.i("DB Load houses","num : " + cursor.getCount());
	    
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	MyHouse house = cursorToHouse(cursor);
	    	house.log();
	    	//categories.add(category);
	    	params.houses.item.add(house);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    //return categories;
	  }
	  
	  
	  
	  
	  
	 
	  
	  
	  
	  
	  
	  
	  

	  

	  public void logAllStaseis() {
	   String[] columns = {
	    						"_id","name","type","fixed","shor","sxolia","user",
	    						"x","y","f","l",
	    						"date","foto","h","h_ort",
	    						"sx","sy","sh","global_id","_t_id","_t_tabId","_t_project","_t_base","local_date","tocast"};
	   
	    Cursor cursor = myDataBase.query( "staseis",  columns, "1", null, null, null, null);
	    
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	MyStasi stasi = cursorToStasi(cursor);
	    	stasi.log();
	    	cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	  }
	  
	  
	  public String getAllStaseisToCSV(long project) {
		  String out = "";
		  String[] columns = {
		    						"_id","name","type","fixed","shor","sxolia","user",
		    						"x","y","f","l",
		    						"date","foto","h","h_ort",
		    						"sx","sy","sh","global_id","_t_id","_t_tabId","_t_project","_t_base","local_date","tocast"};
		   
		    Cursor cursor = myDataBase.query( "staseis",  columns, "project="+project, null, null, null, null);
		    
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	MyStasi stasi = cursorToStasi(cursor);
		    	out = out + stasi.toCSV() + "\n";
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    
		    
		    return out;
		  }
		  
	  
	  
	  
	  
	  
	  public void logAllStasi_link() {
		   String[] columns = {
		    						"_id","project","stasi"};
		   
		    Cursor cursor = myDataBase.query( "con_project_stasi",  columns, "1", null, null, null, null);
		    
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	Log.i("logAllStasi_link", cursor.getLong(0) + " , " + cursor.getLong(1) + " , " + cursor.getLong(2));
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		  }
	  
	  


	  

	  
	  public void logAllPeriodos() {
		   String[] columns = {
		    						"_id","stasi","stasi_0","project","yo","_t_tabId","_t_id","date","local_date","global_id"};
		   
		    Cursor cursor = myDataBase.query( "periodos",  columns, "1", null, null, null, null);
		    
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	Log.i("logAllPeriodos", 
		    									  		cursor.getLong(0) 
		    							+ " , " 		+ cursor.getLong(1) 
		    							+ " , " 		+ cursor.getLong(2) 
		    							+ " ,p : " 		+ cursor.getLong(3) 
		    							+ " ,yo : " 	+ cursor.getFloat(4) 
		    							+ " ,tab : " 	+ cursor.getLong(5) 
		    							+ " ,t_id : " 	+ cursor.getLong(6)
		    							+ " ,date: " 	+ cursor.getLong(7)
		    							+ " ,l_date: " + cursor.getLong(8)
		    							+ " ,g_id: " + cursor.getLong(9)
		    							);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		  }
	  
	  
	  
	  
	  
	  
	  public String getKeyValue(String key){
		  String 	out 	= null;
		  String[] 	columns = {"value"};
		  Cursor cursor = myDataBase.query( "params",  columns, "key='" + key + "'", null, null, null, null);
		  
		  if(cursor.getCount()>0){
			  cursor.moveToFirst();
			  out = cursor.getString(0);
			  cursor.close();
		  }
		  
		  return out;
	  }
	  
	  
	  
	  
	  public long getLongValue(String table,String column, String where){
		 // Log.i("sdsds","sdsdsD");
		  long 	out 	= -9999;		  String[] 	columns = {column};
		 // Log.i("sdsds","sdsdsD");
		  Cursor cursor = myDataBase.query( table,  columns, where, null, null, null, null);
		 // Log.i("sdsds","sdsdsD");
		  if(cursor.getCount()>0){  cursor.moveToFirst();	  out = cursor.getLong(0);	  cursor.close();  }
		  return out;	  }
	  
	  
	  
	  public void addKeyValue(String key,String value){
		  
		  ContentValues values = new ContentValues(); 
		  values.put("key",key); 
		  values.put("value",value); 
		   
		 long cursor = myDataBase.insert(  "params",  null, values);
		  
	  }
	  
	  
 public void delKeyValue(String key){  
	long cursor = myDataBase.delete("params", "key='" + key + "'",null);
 }
 
 
	  
	  public void loadProjectsFromDb() {
		   // List<Category> categories = new ArrayList<Category>();
		    String[] columns = {
		    						"_id","name","sxolia","user",
		    						"start_stasi","stasi_0","date",
		    						"staseis_last_sync_date",
		    						"periodos_last_sync_date",
		    						"metrisi_last_sync_date"
		    						 
		    								 
		    						  
		    					};
		    
		    
		    Cursor cursor = myDataBase.query( "project",  columns, null, null, null, null, null);
		    Log.i("Load projects","intro");
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	MyProject project = cursorToProject(cursor);
		    	//categories.add(category);
		    	//params.staseis.item.add(stasi);
		    	//Log.i("--","dbpn : " + counter);
		    	params.app.myProjectCollection.add(project);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    //return categories;
		  }
	  
	  

	  public String  getCSVProjects() {
		   // List<Category> categories = new ArrayList<Category>();
		    String[] columns = {
		    						"_id"
		    					};
		    
		    String out="";
		    Cursor cursor = myDataBase.query( "project",  columns, null, null, null, null, null);
		   // Log.i("Load projects","intro");
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	out = out + cursor.getString(0) + ",";
		    	//counter++;
		    	//MyProject project = cursorToProject(cursor);
		    	//categories.add(category);
		    	//params.staseis.item.add(stasi);
		    	//Log.i("--","dbpn : " + counter);
		    	//params.app.myProjectCollection.add(project);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    //return categories;
		    return out;
		  }
	  
	  
	  
	  public void loadPeriodoiFromDb() {
		    String[] columns = {
		    						"_id","stasi","stasi_0","stasi_0_angle",
		    						"yo","_t_id","_t_tabId","project","date","global_id","local_date"
		    					};
		    Cursor cursor = myDataBase.query( "periodos",  columns,  "project=" + params.activeProject._id, null, null, null, null);
		   // Log.i("Load periodoi","intro");
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	MyMeasureSet periodos = cursorToPeriodos(cursor);
		    	Log.i("--periodos load ","dbpn : " + counter);
		    	params.msets.add(periodos);
		    	cursor.moveToNext();
		    }
		    cursor.close();
		  }
	  
	  
	  
	  
	  
	  public int admin_local_date_count( int local_date){
		  String[] columns = {
					"_id","local_date"
				};
		  Cursor cursor = myDataBase.query( "periodos",  columns,  "local_date=" + local_date, null, null, null, null);
		   return cursor.getCount();
	  }
	  
	  
	  
	  
	  
	  
	  public void admin_set_unique_period_local_date(String tabId) {
		  //frontizei monadika local date
		  String[] columns = {
		    						"_id","local_date"
		    					};
		    Cursor cursor = myDataBase.query( "periodos",  columns,  "_t_tabId=" + tabId, null, null, null, null);
		   // Log.i("Load periodoi","intro");
		    cursor.moveToFirst();
		     while (!cursor.isAfterLast()) {
		    	long id = cursor.getLong(0);
		    	long ld = cursor.getLong(1);
		    	
		    	if(admin_local_date_count((int) ld)>1){
		    		
		    		while (admin_local_date_count((int) ld)>0){
		    			ld=ld+1;
		    		}
		    		updateTableColumn("periodos", "local_date", ld, "_id=" + id);
		    	}
		    	
		    	cursor.moveToNext();
		    }
		    cursor.close();
		  }
	  
	  
	  
	  
	  
	  public void loadStasiLinkFromDb() {
		    String[] columns = {
		    						"stasi","isbase"
		    					};
		    Cursor cursor = myDataBase.query( "con_project_stasi",  columns,  "project=" + params.activeProject._id, null, null, null, null);
		    Log.i("Load con_project_stasi","intro");
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	stasi_link_class st = cursorToStasiLink(cursor);
		    	Log.i("--con_project_stasi ","dbpn : "  + st._id + " - " + st.isbase);
		    	params.activeProject.addStasiLink(st,false);
		    	cursor.moveToNext();
		    }
		    cursor.close();
		  }
	  
	  

	  public void clearProject(int _id) {
		   
			  String sql 	= "DELETE FROM staseis WHERE project="  + _id + " AND type=0 AND fixed=0";
			  myDataBase.execSQL(sql);
			 
			  
			  String[] columns = {
						"_id"
					};
			  
			  Cursor cursor = myDataBase.query("periodos", columns, "project=" + _id, null, null, null, null);
			  cursor.moveToFirst();
			    while (!cursor.isAfterLast()) {
			    	 sql 			= "DELETE FROM metrisi WHERE periodos="  + cursor.getInt(0);
					  myDataBase.execSQL(sql);
			    	
			    	cursor.moveToNext();
			    }
			    cursor.close();
			    
			  sql 			= "DELETE FROM con_project_stasi WHERE project="  + _id;
			  myDataBase.execSQL(sql);
				 
			  sql 			= "DELETE FROM periodos WHERE project="  + _id;
			  myDataBase.execSQL(sql);
				 
			 
				 
			  sql 			= "DELETE FROM line WHERE project="  + _id;
			  myDataBase.execSQL(sql);
				 
			  sql 			= "DELETE FROM houses WHERE project="  + _id;
			  myDataBase.execSQL(sql);
				 
			  
		  
		  
		  
		  
		 }
	  
	  
	  


	  public void clearAll() {
		   
		  String sql 	= "DELETE FROM staseis WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  sql 	= "DELETE FROM metrisi WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  sql 	= "DELETE FROM periodos WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  sql 	= "DELETE FROM con_project_stasi WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  sql 	= "DELETE FROM line WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  sql 	= "DELETE FROM houses WHERE 1";
		  myDataBase.execSQL(sql);
		 
		  
		  
		  
		 }
	  
	  
	  
	  public void loadMetriseisFromDb() {
		   // String[] columns = {
		    			//			"_id","stasi_index","tostasi","periodos",
		    			//			"type","hZ","sD"
		    					//};
		    //
		    ArrayList<Long> 		periodoi = new ArrayList<Long>();
		    		
		    String[] columns = {
					"_id"
				};
		    Cursor cursor = myDataBase.query( "periodos",  columns,  "project=" + params.activeProject._id, null, null, null, null);
			   
		    //Cursor cursor =  myDataBase.rawQuery("SELECT project FROM periodos WHERE project ="  + params.activeProject._id,null);
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	long tmp = cursor.getLong(0);;
		    	Log.i("--periodos of metrisi load ","_id : " + tmp);
		    	periodoi.add(tmp);
		    	
		    	String query =  "SELECT "
    					+ "_id,stasi_index,tostasi,periodos,type,hZ,sD,vZ,obtype,sxolia,odefsi_use,ys,global_id,local_date,date "
    					+ "FROM metrisi WHERE "
    					+ "periodos=" + tmp +  " ";//AND type=0";
    					
		    	Cursor cursor_2 = myDataBase.rawQuery(query		,null);
		    	cursor_2.moveToFirst();
		    	while (!cursor_2.isAfterLast()) {
			    	counter++;
			    	MyMeasurement metrisi = cursorToMetrisi(cursor_2);
			    	metrisi.setParent(params.msets.get(metrisi.periodos));
			    	Log.i("--metrisi load ","dbpn : " + metrisi.stasi_index);
			    	if(metrisi.stasi_index>=0){
			    		params.msets.get(metrisi.periodos).itemStaseis.add(metrisi);
			    	}else{
			    		params.msets.get(metrisi.periodos).item.add(metrisi);
			    	}
			    	metrisi.log();
			    	cursor_2.moveToNext();
			    }
		    	cursor_2.close();
		    	
		    	cursor.moveToNext();
		    }
		    cursor.close();
		    
		    
		    
		    
		    
		   /* Cursor cursor =  myDataBase.rawQuery("SELECT "
					+ "metrisi._id,metrisi.stasi_index,metrisi.tostasi,metrisi.periodos,metrisi.type,metrisi.hZ,metrisi.sD "
					+ "FROM periodos,metrisi WHERE "
					+ "metrisi.periodos=periodos._id AND periodos.project ="  + params.activeProject._id
					,null);*/
	    
		    /*
		    cursor =  myDataBase.rawQuery("SELECT "
		    					+ "_id,stasi_index,tostasi,periodos,type,hZ,sD "
		    					+ "FROM metrisi WHERE "
		    					+ "periodos=periodos._id AND periodos.project ="  + params.activeProject._id
		    					,null);*/
				    
		    
		    //Cursor cursor = myDataBase.query( "metrisi",  columns,  "project=" + params.activeProject._id, null, null, null, null);
		    /*Log.i("Load metriseis","intro");
		    cursor.moveToFirst();
		    counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	MyMeasurement metrisi = cursorToMetrisi(cursor);
		    	Log.i("--metrisi load ","dbpn : " + counter);
		    	if(metrisi.stasi_index>=0){
		    		params.renderer.msets.get(metrisi.periodos).itemStaseis.add(metrisi);
		    	}else{
		    		params.renderer.msets.get(metrisi.periodos).item.add(metrisi);
		    	}
		    	
		    	cursor.moveToNext();
		    }
		    cursor.close();*/
		  }
	  
	  
	  
	  
	  public void loadLinesFromDb() {
		  params.renderer.my_poly.clear();  
		  String[] columns = {
		    						"coor","_id","type","sxolia"
		    					};
		    Cursor cursor = myDataBase.query( "line",  columns,  "project=" + params.activeProject._id, null, null, null, null);
		    Log.i("Load lines","intro");
		    cursor.moveToFirst();
		    int counter=0;
		    while (!cursor.isAfterLast()) {
		    	counter++;
		    	 
		    	double[] line = cursorToLine(cursor);
		    	long id = cursor.getLong(1);
		    	//Log.i("--line load ","dbpn : " + line[0] + "," + line[1] + "  -  " + line[2] + "," + line[3]);
		    	
		    	MyPoly myline = params.renderer.my_poly.add(0f, 0f, 0f);
				myline.addVertice(line[0],line[1]);
				myline.addVertice(line[2], line[3]);
				
				myline.meta.type =  (int) cursor.getLong(2);
				
				if(myline.meta.type==0) myline.setColor(0f, 0f, 0f);
				if(myline.meta.type==1) myline.setColor(0f, 255f, 255f);
				if(myline.meta.type==2) myline.setColor(255f, 255f, 0f);
				myline.meta.sxolia =   cursor.getString(3);
				 
				 
				myline._id=id;
		    	
		    	//params.renderer.msets.add(periodos);
		    	cursor.moveToNext();
		    }
		    cursor.close();
		    
		    params.renderer.my_poly.regen_coor();
		    
		  }
	  
	  
	  
	  
	  public void addStasiToDb(MyStasi stasi) {
		   // List<Category> categories = new ArrayList<Category>();
		    String[] columns = {
		    						"MAX(_id)"
		    					};
		    
		    //Log.i("",myDataBase.toString());
		    Cursor cursor = myDataBase.query( "staseis",  columns, "_id<1000", null, null, null, null);
		  //  myDataBase .rawQuery("SELECT MAX(price) FROM spendings", null).
		    cursor.moveToFirst();
		   // int counter=0;
		    long maxval=0;
		    while (!cursor.isAfterLast()) {
		    	maxval=cursor.getLong(0);
		    	cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		   
		    double[] egsa = params.wms.proj.fl2EGSA87(stasi.point.y, stasi.point.x);
			  
		    
		    ContentValues values = new ContentValues(); 
		    values.put("_id", maxval+1); 
		    values.put("project", params.activeProject._id); 
		    values.put("type", 0); 
		    values.put("fixed", 0); 
		    values.put("name", "added"); 
		    values.put("date", stasi.date); 
		    values.put("local_date", stasi.local_date); 
		    values.put("x", egsa[0]); 
		    values.put("y", egsa[1]); 
		    values.put("f", stasi.point.y); 
		    values.put("l", stasi.point.x); 
		    if(stasi.toCast){
			    values.put("tocast", 1); 
			    }else{
			    	values.put("tocast", 1); 
			    }    
		    
		    
		    
		    Log.i("Stasi add", "_id : " + (maxval+1));
		    Log.i("Stasi add", "project : " +params.activeProject._id);
		    Log.i("Stasi add", "x : " + egsa[0]);
		    Log.i("Stasi add", "y : " + egsa[1]);
		    Log.i("Stasi add", "f : " + stasi.point.y);
		    Log.i("Stasi add", "l : " +stasi.point.x);
		    
		    
		    
		    myDataBase.insert("staseis", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    stasi._id=maxval+1;
		    
		    //return categories;
		  }
	  
	  
	  
	  

	  
	  
	  public void addStasiToDbFromMenuSync(MyStasi stasi) {
		   // List<Category> categories = new ArrayList<Category>();
		   
		  
		  String[] columns0 = {
					"count(_id)"
				};
		  
		  Cursor cursor0 = myDataBase.query( "staseis",  columns0, "global_id=" + stasi._id, null, null, null, null);
		  cursor0.moveToFirst();
		  int count = cursor0.getInt(0);
		  cursor0.close();
		  if(count==0){
		  
			  
			  String[] columns1 = {
						"MAX(_id)"
					};
			  
			  Cursor cursor1 = myDataBase.query( "staseis",  columns0, null, null, null, null, null);
			  cursor1.moveToFirst();
			  int newid = cursor1.getInt(0)+1;
			  cursor1.close();
			  
			  
			  
		   Proj proj = new Proj();
		    double[] fl =proj.Egsa2fl84(stasi.x, stasi.y);
			  
		    
		    ContentValues values = new ContentValues(); 
		    values.put("_id",newid); 
		    values.put("global_id",stasi._id); 
			    values.put("project", 0); 
		    values.put("type", stasi.type); 
		    values.put("fixed", 1); 
		    values.put("name", stasi.name); 
		    values.put("x", stasi.x); 
		    values.put("y", stasi.y); 
		    values.put("f", fl[0]); 
		    values.put("l", fl[1]); 
		    
		    values.put("_t_base", stasi._t_base); 
		    values.put("_t_id", stasi._t_id); 
		    values.put("project", stasi._t_project); 
		    values.put("_t_project", stasi._t_project); 
		    values.put("_t_tabId", stasi._t_tabId); 
		    values.put("date", stasi.date); 
		    values.put("local_date", stasi.local_date); 
		    
		    
		    
		    
		    Log.i("Stasi add", "_id : " +stasi._id);
		    Log.i("Stasi add", "name : " +stasi.name);
		    Log.i("Stasi add", "project : " +0);
		    Log.i("Stasi add", "x : " + stasi.x);
		    Log.i("Stasi add", "y : " + stasi.y);
		    Log.i("Stasi add", "f : " + fl[0]);
		    Log.i("Stasi add", "l : " +fl[1]);
		    Log.i("Stasi add", "_t_base : " +stasi._t_base);
		    Log.i("Stasi add", "_t_id : " +stasi._t_id);
		    Log.i("Stasi add", "project : " +stasi._t_project);
		    Log.i("Stasi add", "_t_project : " +stasi._t_project);
			    Log.i("Stasi add", "_t_tabId : " + stasi._t_tabId);
		    Log.i("Stasi add", "date : " + stasi.date);
		    
		    
		    
		    myDataBase.insert("staseis", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    //stasi._id=maxval+1;
		  }else{
			  
			  Log.i("Stasi found", "_id : " +stasi._id);
		  }
		    //return categories;
		  }
	  
	  
	  
	  

	  
	  
	  
	  
	  
	  public void addStasiToDb(MyStasi stasi,boolean fromSync, int fixed) {
		   // List<Category> categories = new ArrayList<Category>();
		   
		  
		  String[] columns0 = {
					"count(_id)"
				};
		  
		  Cursor cursor0 = myDataBase.query( "staseis",  columns0, "_id=" + stasi._id, null, null, null, null);
		  cursor0.moveToFirst();
		  int count = cursor0.getInt(0);
		  cursor0.close();
		  if(count==0){
		  
			  
		   Proj proj = new Proj();
		    double[] fl =proj.Egsa2fl84(stasi.x, stasi.y);
			  
		    
		    ContentValues values = new ContentValues(); 
		    values.put("_id",stasi._id); 
		    values.put("project", stasi._t_project); 
		    values.put("type", stasi.type); 
		    values.put("fixed", fixed); 
		    values.put("name", stasi.name); 
		    values.put("x", stasi.x); 
		    values.put("y", stasi.y); 
		    values.put("f", fl[0]); 
		    values.put("l", fl[1]); 
		    
		    values.put("_t_base", stasi._t_base); 
		    values.put("_t_id", stasi._t_id); 
		    values.put("project", stasi._t_project); 
		    values.put("_t_project", stasi._t_project); 
		    values.put("_t_tabId", stasi._t_tabId); 
		    values.put("date", stasi.date); 
		    values.put("local_date", stasi.local_date); 
		    if(stasi.toCast){
		    values.put("tocast", 1); 
		    }else{
		    	values.put("tocast", 1); 
		    }
		    
		    
		    
		    
		    Log.i("Stasi add", "_id : " +stasi._id);
		    Log.i("Stasi add", "name : " +stasi.name);
		    Log.i("Stasi add", "project : " +0);
		    Log.i("Stasi add", "x : " + stasi.x);
		    Log.i("Stasi add", "y : " + stasi.y);
		    Log.i("Stasi add", "f : " + fl[0]);
		    Log.i("Stasi add", "l : " +fl[1]);
		    Log.i("Stasi add", "_t_base : " +stasi._t_base);
		    Log.i("Stasi add", "_t_id : " +stasi._t_id);
		    Log.i("Stasi add", "project : " +stasi._t_project);
		    Log.i("Stasi add", "_t_project : " +stasi._t_project);
			    Log.i("Stasi add", "_t_tabId : " + stasi._t_tabId);
		    Log.i("Stasi add", "date : " + stasi.date);
		    
		    
		    
		    myDataBase.insert("staseis", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    //stasi._id=maxval+1;
		  }else{
			  
			  Log.i("Stasi found", "_id : " +stasi._id);
		  }
		    //return categories;
		  }
	  
	  
	  
	  
	  
	  
	  public int addStasiFromSync2ToDb(MyStasi stasi, int fixed) {
		   // List<Category> categories = new ArrayList<Category>();
		   
		  
		  String[] columns = {
					"MAX(_id)"
				};
		  
		  Cursor cursor1 = myDataBase.query( "staseis",  columns, null, null, null, null, null);
		  cursor1.moveToFirst();
		  int newid = cursor1.getInt(0)+1;
		  
			  
		   Proj proj = new Proj();
		    double[] fl =proj.Egsa2fl84(stasi.x, stasi.y);
			  
		    
		    ContentValues values = new ContentValues(); 
		    values.put("_id",newid); 
		    values.put("project", stasi._t_project); 
		    values.put("type", stasi.type); 
		    values.put("fixed", fixed); 
		    values.put("name", stasi.name); 
		    values.put("x", stasi.x); 
		    values.put("y", stasi.y); 
		    values.put("f", fl[0]); 
		    values.put("l", fl[1]); 
		    
		    values.put("_t_base", stasi._t_base); 
		    values.put("_t_id", stasi._t_id); 
		    values.put("project", stasi._t_project); 
		    values.put("_t_project", stasi._t_project); 
		    values.put("_t_tabId", stasi._t_tabId); 
		    values.put("date", stasi.date); 
		    values.put("local_date", stasi.local_date); 
		    values.put("global_id", stasi.global_id); 
		    values.put("tocast", 0); 
		    
		    
		    
		    
		    Log.i("Stasi add", "_id : " +stasi._id);
		    Log.i("Stasi add", "name : " +stasi.name);
		    Log.i("Stasi add", "project : " +0);
		    Log.i("Stasi add", "x : " + stasi.x);
		    Log.i("Stasi add", "y : " + stasi.y);
		    Log.i("Stasi add", "f : " + fl[0]);
		    Log.i("Stasi add", "l : " +fl[1]);
		    Log.i("Stasi add", "_t_base : " +stasi._t_base);
		    Log.i("Stasi add", "_t_id : " +stasi._t_id);
		    Log.i("Stasi add", "project : " +stasi._t_project);
		    Log.i("Stasi add", "_t_project : " +stasi._t_project);
			    Log.i("Stasi add", "_t_tabId : " + stasi._t_tabId);
		    Log.i("Stasi add", "date : " + stasi.date);
		    
		    
		    
		    myDataBase.insert("staseis", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    //stasi._id=maxval+1;
		    return newid;
		  }
	 
	  

	  
	  
	  
	  
	  public void addHouseToDb(MyHouse house) {
		   // List<Category> categories = new ArrayList<Category>();
		   
		  String[] columns = {
					"MAX(_id)"
				};
				
				//Log.i("",myDataBase.toString());
				Cursor cursor = myDataBase.query( "houses",  columns, null, null, null, null, null);
				//  myDataBase .rawQuery("SELECT MAX(price) FROM spendings", null).
				cursor.moveToFirst();
				// int counter=0;
				long maxval=0;
				while (!cursor.isAfterLast()) {
				//counter++;
				//MyProject project = cursorToProject(cursor);
				//categories.add(category);
				//params.staseis.item.add(stasi);
				maxval=cursor.getLong(0);
				///Log.i("--","dbpn : " + maxval);
				//Log.i("--","dbpn : " + counter);
				//params.app.myProjectCollection.add(project);
				cursor.moveToNext();
				}
				// make sure to close the cursor
				cursor.close();
					
				
				house.log();
				
				
		   Proj proj = new Proj();
		    double[] xy =proj.fl2EGSA87(house.point.y, house.point.x);
			  
		   
		    ContentValues values = new ContentValues(); 
		    values.put("_id",(maxval+1)); 
		    values.put("project",params.activeProject._id); 
		    values.put("type", house.type); 
		    values.put("floors", house.floors); 
			    values.put("path", house.path); 
		    values.put("sxolia", house.sxolia); 
		    values.put("x", xy[0]); 
		    values.put("y", xy[1]); 
		    values.put("f", house.f); 
		    values.put("l", house.l); 
		    
		    values.put("project",params.activeProject._id); 
		    
		     
		    myDataBase.insert("houses", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    //stasi._id=maxval+1;
		  
		    //return categories;
		  }
	  
	  
	  
	  
	  
	  public void updateStasiToDbProjectTable(stasi_link_class st) {
		   // List<Category> categories = new ArrayList<Category>();
		    
		  

		  String[] columns0 = {
					"count(_id)"
				};
		  
		  Cursor cursor0 = myDataBase.query( "con_project_stasi",  columns0, "stasi=" + st._id + " AND project=" + params.activeProject._id, null, null, null, null);
		  cursor0.moveToFirst();
		  int count = cursor0.getInt(0);
		  cursor0.close();
		  if(count==0){
			  String[] columns = {
						"MAX(_id)"
					};
			  Cursor cursor = myDataBase.query( "con_project_stasi",  columns, null, null, null, null, null);
			  cursor.moveToFirst();

			  	long maxval=0;
			    while (!cursor.isAfterLast()) {
			    	maxval=cursor.getLong(0);
			    	Log.i("--","dbpn : " + maxval);
			    	cursor.moveToNext();
			    }
			    cursor.close();
			    
			    
			    
			    ContentValues values = new ContentValues(); 
			    values.put("_id", maxval+1); 
			    values.put("project", params.activeProject._id); 
			    values.put("stasi", st._id); 
			    if(st.isbase){ values.put("isbase", 1); }else{values.put("isbase", 0);};
			    myDataBase.insert("con_project_stasi", null, values);
			    
		  }else{
			  	ContentValues values = new ContentValues(); 
			    values.put("stasi", st._id); 
			    if(st.isbase){ values.put("isbase", 1); }else{values.put("isbase", 0);};
			    myDataBase.update("con_project_stasi", values, "stasi=" + st._id + " AND project=" + params.activeProject._id, null);
			  
		  }
		  
		  
		  
		    //return categories;
		  }
	  
	  
	  
	  

	  public void updateStasiToDbProjectTableGivingProject(stasi_link_class st,int project) {
		   // List<Category> categories = new ArrayList<Category>();
		    
		  

		  String[] columns0 = {
					"count(_id)"
				};
		  
		  Cursor cursor0 = myDataBase.query( "con_project_stasi",  columns0, "stasi=" + st._id + " AND project=" + project, null, null, null, null);
		  cursor0.moveToFirst();
		  int count = cursor0.getInt(0);
		  cursor0.close();
		  if(count==0){
			  String[] columns = {
						"MAX(_id)"
					};
			  Cursor cursor = myDataBase.query( "con_project_stasi",  columns, null, null, null, null, null);
			  cursor.moveToFirst();

			  	long maxval=0;
			    while (!cursor.isAfterLast()) {
			    	maxval=cursor.getLong(0);
			    	Log.i("--","dbpn : " + maxval);
			    	cursor.moveToNext();
			    }
			    cursor.close();
			    
			    
			    
			    ContentValues values = new ContentValues(); 
			    values.put("_id", maxval+1); 
			    values.put("project", project); 
			    values.put("stasi", st._id); 
			    if(st.isbase){ values.put("isbase", 1); }else{values.put("isbase", 0);};
			    myDataBase.insert("con_project_stasi", null, values);
			    
		  }else{
			  	ContentValues values = new ContentValues(); 
			    values.put("stasi", st._id); 
			    if(st.isbase){ values.put("isbase", 1); }else{values.put("isbase", 0);};
			    myDataBase.update("con_project_stasi", values, "stasi=" + st._id + " AND project=" + project, null);
			  
		  }
		  
		  
		  
		    //return categories;
		  }
	  
	  
	  
	  
	  
	  public void addProjectToDb(Long _id, String name) {
		   // List<Category> categories = new ArrayList<Category>();
		   
		  
		  String[] columns0 = {
					"count(_id)"
				};
		  
		  Cursor cursor0 = myDataBase.query( "project",  columns0, "_id=" + _id, null, null, null, null);
		  cursor0.moveToFirst();
		  int count = cursor0.getInt(0);
		  
		  Log.i("sdsd","d : " + count);
		  cursor0.close();
		  if(count==0){
		  
		    
		    ContentValues values = new ContentValues(); 
		    values.put("_id",_id); 
		    values.put("name", name); 
		   
		    
		    
		    Log.i("Stasi add", "_id : " +_id);
		    Log.i("Stasi add", "name : " +name);
		   
		    
		    
		    
		    myDataBase.insert("project", null, values);

		   //myDataBase.insert(table, nullColumnHack, values)
		   // myDataBase .rawQuery(query, null);
		    
		    //stasi._id=maxval+1;
		  }else{
			  
			  Log.i("Project found", "_id : " +_id + "   name : " + name);
		  }
		    //return categories;
		  }
	  
	  
	  
	  
	  
	  public void addPeriodoToDb(MyMeasureSet periodos) {
		    String[] columns = {
		    						"MAX(_id)"
		    					};
		    
		    Cursor cursor = myDataBase.query( "periodos",  columns, null, null, null, null, null);
		    cursor.moveToFirst();
		    long maxval=0;
		    while (!cursor.isAfterLast()) {
		    	maxval=cursor.getLong(0);
		    	Log.i("--","dbpn : " + maxval);
		    	cursor.moveToNext();
		    }
		    cursor.close();

		    
		   
		     
		    ContentValues values = new ContentValues(); 
		    values.put("_id", maxval+1); 
		    values.put("project", params.activeProject._id); 
		    
		    //oi deiktes stasi kai stasi _0 anaferontai sto index tis stasis sto array
		    // ego apothikevo to _i kai otan fortono tha k;ano anagogi se afto
		    
		    values.put("stasi"	,params.staseis.item.get(periodos.stasi)._id); 
		    values.put("global_id"	,params.staseis.item.get(periodos.stasi).global_id); 
		    values.put("_t_tabId"	,periodos._t_tabId); 
		    values.put("local_date", params.staseis.item.get(periodos.stasi_0).local_date); 
		    values.put("stasi_0", params.staseis.item.get(periodos.stasi_0)._id); 
			    values.put("stasi_0_angle", periodos.stasi_0_angle); 
			values.put("yo", periodos.YO); 
		    
		
		    myDataBase.insert("periodos", null, values);
		    periodos._id= maxval+1;
		   
		    
		    Log.i("Periodos add", "_id : " + (maxval+1));
		    Log.i("Periodos add", "project : " +params.activeProject._id);
		    Log.i("Periodos add", "stasi : " + params.staseis.item.get(periodos.stasi)._id);
		    Log.i("Periodos add", "stasi_0 : " + params.staseis.item.get(periodos.stasi_0)._id);
		    Log.i("Periodos add", "stasi_0_angle : " + periodos.stasi_0_angle);
		    Log.i("Periodos add", "YO : " +periodos.YO);
		    
		    
		    
		   // stasi._id=maxval+1;
		   
		   
		  }
	  
	  
	  
	  
	  
	  
	  
	  

	  public void addPeriodoToDbFromSync(MyMeasureSet periodos) {
		     
		    ContentValues values = new ContentValues(); 
		    values.put("_id"			, periodos._id); 
		    values.put("stasi"			, periodos.stasi_id); 
		    values.put("stasi_0"		, periodos.stasi_0_id); 
		    values.put("stasi_0_angle"	, periodos.stasi_0_angle); 
		    values.put("yo"				, periodos.YO); 
		    values.put("date"			, periodos.date); 
		    values.put("project"		, periodos.project); 
		    values.put("sxolia"			, periodos.sxolia); 
		    values.put("_t_id"			, periodos._t_id); 
		    values.put("_t_tabId"		, periodos._t_tabId); 
		    values.put("global_id"		, periodos.global_id); 
		    values.put("local_date"		, periodos.local_date); 

 
		    
		    
		
		    myDataBase.insert("periodos", null, values);
		   
		  }
	  
	  
	  
	  

	  public int addPeriodoToDbFromSync2(MyMeasureSet periodos) {
		     
		  
		  String[] columns = {
					"MAX(_id)"
				};
		  
		  Cursor cursor1 = myDataBase.query( "periodos",  columns, null, null, null, null, null);
		  cursor1.moveToFirst();
		  int newid = cursor1.getInt(0)+1;
		  
			  
		  
		  
		    ContentValues values = new ContentValues(); 
		    values.put("_id"			, newid); 
		    values.put("stasi"			, periodos.stasi_id); 
		    values.put("stasi_0"		, periodos.stasi_0_id); 
		    values.put("stasi_0_angle"	, periodos.stasi_0_angle); 
		    values.put("yo"				, periodos.YO); 
		    values.put("date"			, periodos.date); 
		    values.put("project"		, periodos.project); 
		    values.put("sxolia"			, periodos.sxolia); 
		    values.put("_t_id"			, periodos._t_id); 
		    values.put("_t_tabId"		, periodos._t_tabId); 
		    values.put("global_id"		, periodos.global_id); 
		    values.put("local_date"		, periodos.local_date); 
		    values.put("toCast"		, 0); 

 
		    
		    
		
		    myDataBase.insert("periodos", null, values);
		    
		    
		    return newid;
		   
		  }
	  
	  
	  

	  
	  public long addMeasurementToDb(MyMeasurement metrisi, long periodos_id, int tostasi) {
		    String[] columns = {
		    						"MAX(_id)"
		    					};
		    
		    Cursor cursor = myDataBase.query( "metrisi",  columns, null, null, null, null, null);
		    cursor.moveToFirst();
		    long maxval=0;
		    while (!cursor.isAfterLast()) {
		    	maxval=cursor.getLong(0);
		    	//Log.i("Metrisi max index","dbpn : " + maxval);
		    	cursor.moveToNext();
		    }
		    cursor.close();

		    
		   
		     
		    ContentValues values = new ContentValues(); 
		    values.put("_id", maxval+1); 
		    values.put("type", metrisi.type); 
		    values.put("hZ", metrisi.hZ); 
		    values.put("sD", metrisi.sD); 
		    values.put("vZ", metrisi.vZ); 
		    values.put("periodos", periodos_id); 
		    values.put("ys", metrisi.ys); 
		    values.put("tostasi", tostasi); 
		    values.put("global_id", metrisi.global_id); 
		    values.put("local_date", metrisi.local_date); 
		    if(metrisi.stasi_index>=0){
		    	values.put("stasi_index",params.staseis.item.get(metrisi.stasi_index)._id); 
		    	 Log.i("Metrisi add", "stasi_index : " +params.staseis.item.get(metrisi.stasi_index)._id);
		    }else{
		    	values.put("stasi_index",-1);
		    	 Log.i("Metrisi add", "stasi_index : " +(-1));
		    }
		    
		    if(metrisi.odefsi_use){
		    	 values.put("odefsi_use", 1); 
		    }else{
		    	values.put("odefsi_use", 0);
		    }
		    
		    
		
		    myDataBase.insert("metrisi", null, values);
		   // metrisi._id= maxval+1;
		    metrisi._id=(maxval+1);
		    
		    params.debug("id : " + metrisi._id);
		   // Log.i("Metrisi add", "_id : " + (maxval+1));
		   // Log.i("Metrisi add", "type : " +metrisi.type);
		   // Log.i("Metrisi add", "hZ : " + metrisi.hZ);
		  //  Log.i("Metrisi add", "vZ : " + metrisi.vZ);
		  //  Log.i("Metrisi add", "sD : " + metrisi.sD);
		   // Log.i("Metrisi add", "periodos : " + periodos_id);
		  //  Log.i("Metrisi add", "tostasi : " +tostasi);
		   
		    
		    
		    return metrisi._id;
		   // stasi._id=maxval+1;
		   
		   
		  }
	  
	  
	  

	  
	  public void addMeasurementToDbFromSync2(MyMeasurement metrisi) {
		  String[] columns = {
					"MAX(_id)"
				};
		  
		  Cursor cursor1 = myDataBase.query( "metrisi",  columns, null, null, null, null, null);
		  cursor1.moveToFirst();
		  int newid = cursor1.getInt(0)+1;
		    
		   
		     
		    ContentValues values = new ContentValues(); 
		    values.put("_id", newid); 
		    values.put("global_id", metrisi.global_id); 
		    values.put("type", metrisi.type); 
		    values.put("hZ", metrisi.hZ); 
		    values.put("sD", metrisi.sD); 
		    values.put("vZ", metrisi.vZ); 
		    values.put("periodos", metrisi.periodos); 
		    values.put("ys", metrisi.ys); 
		    //values.put("tostasi", tostasi); 
		    values.put("global_id", metrisi.global_id); 
		    values.put("local_date", metrisi.local_date); 
		    values.put("date", metrisi.date); 
		    values.put("stasi_index",metrisi.stasi_index_id); 
		    	
		    if(metrisi.odefsi_use){
		    	 values.put("odefsi_use", 1); 
		    }else{
		    	values.put("odefsi_use", 0);
		    }
		    
		    
		
		    myDataBase.insert("metrisi", null, values);
		   // metrisi._id= maxval+1;
		    //metrisi._id=(maxval+1);
		    
		   // params.debug("id : " + metrisi._id);
		   // Log.i("Metrisi add", "_id : " + (maxval+1));
		   // Log.i("Metrisi add", "type : " +metrisi.type);
		   // Log.i("Metrisi add", "hZ : " + metrisi.hZ);
		  //  Log.i("Metrisi add", "vZ : " + metrisi.vZ);
		  //  Log.i("Metrisi add", "sD : " + metrisi.sD);
		   // Log.i("Metrisi add", "periodos : " + periodos_id);
		  //  Log.i("Metrisi add", "tostasi : " +tostasi);
		   
		    
		    
		    
		   // stasi._id=maxval+1;
		   
		   
		  }
	  
	  
	  public void addLineToDb(MyPoly poly ,long p1, long m1, long p2, long m2, double[] coor) {
		    String[] columns = {
		    						"MAX(_id)"
		    					};
		    
		    Cursor cursor = myDataBase.query( "line",  columns, null, null, null, null, null);
		    cursor.moveToFirst();
		    long maxval=0;
		    while (!cursor.isAfterLast()) {
		    	maxval=cursor.getLong(0);
		    	Log.i("Line max index","dbpn : " + maxval);
		    	cursor.moveToNext();
		    }
		    cursor.close();

		    
		    String sql                      =   "INSERT INTO line (_id,p1,m1,p2,m2,coor,project,type,sxolia) VALUES(?,?,?,?,?,?,?,?,?)";
		    SQLiteStatement insertStmt      =   myDataBase.compileStatement(sql);
		    insertStmt.clearBindings();
		    insertStmt.bindLong(1, maxval+1);
		    insertStmt.bindLong(2, p1);
		    insertStmt.bindLong(3, m1);
		    insertStmt.bindLong(4, p2);
		    insertStmt.bindLong(5, m2);
		    
		    ByteBuffer buffer = ByteBuffer.allocate(32);
		    buffer.putDouble(coor[0]);
		    buffer.putDouble(coor[1]);
		    buffer.putDouble(coor[2]);
		    buffer.putDouble(coor[3]);
			insertStmt.bindBlob(6, buffer.array());
			insertStmt.bindLong(7, params.activeProject._id);
			
			insertStmt.bindLong(8, poly.meta.type);
			insertStmt.bindString(9, poly.meta.sxolia);
		    
			
		    insertStmt.executeInsert();
		    
		    poly._id=maxval+1;
		    
		    
		    /*
		    ContentValues values = new ContentValues(); 
		    values.put("_id", maxval+1); 
		    values.put("p1", p1); 
		    values.put("m1", m1); 
		    values.put("p2", m2); 
		    values.put("coor", coor); 
			    values.put("periodos", periodos_id); 
		    values.put("tostasi", tostasi); 
		    if(metrisi.stasi_index>=0){
		    	values.put("stasi_index",params.staseis.item.get(metrisi.stasi_index)._id); 
		    	 Log.i("Metrisi add", "stasi_index : " +params.staseis.item.get(metrisi.stasi_index)._id);
		    }else{
		    	values.put("stasi_index",-1);
		    	 Log.i("Metrisi add", "stasi_index : " +(-1));
		    }
		    
		    */
		
		    //myDataBase.insert("metrisi", null, values);
		   // metrisi._id= maxval+1;
		   
		    
		    Log.i("Metrisi add", "_id : " + (maxval+1));
		    Log.i("Metrisi add", "p1 : " +p1);
		    Log.i("Metrisi add", "m1 : " + m1);
		    Log.i("Metrisi add", "p2 : " + p2);
		    Log.i("Metrisi add", "m2 : " + m2);
		    Log.i("Metrisi add", "coor : " + coor[0] + "," + coor[1] +  "  -  " + coor[2] + "," + coor[3]);
		   
		    
		    
		    
		   // stasi._id=maxval+1;
		   
		   
		  }
	  
	  
	  

	  
	  


	  
	  public void replaceLineToDb(MyPoly poly ,long p1, long m1, long p2, long m2, double[] coor,long _id) {
		    
		  
		  myDataBase.delete("line", "_id=" + _id, null);
		  String[] columns = {
		    						"MAX(_id)"
		    					};
		    
		   
		    String sql                      =   "INSERT INTO line (_id,p1,m1,p2,m2,coor,project,type,sxolia) VALUES(?,?,?,?,?,?,?,?,?)";
		    SQLiteStatement insertStmt      =   myDataBase.compileStatement(sql);
		    insertStmt.clearBindings();
		    insertStmt.bindLong(1, _id);
		    insertStmt.bindLong(2, p1);
		    insertStmt.bindLong(3, m1);
		    insertStmt.bindLong(4, p2);
		    insertStmt.bindLong(5, m2);
		    
		    ByteBuffer buffer = ByteBuffer.allocate(32);
		    buffer.putDouble(coor[0]);
		    buffer.putDouble(coor[1]);
		    buffer.putDouble(coor[2]);
		    buffer.putDouble(coor[3]);
			insertStmt.bindBlob(6, buffer.array());
			insertStmt.bindLong(7, params.activeProject._id);
			insertStmt.bindLong(8, poly.meta.type);
			insertStmt.bindString(9, poly.meta.sxolia);
		    
			
		    insertStmt.executeInsert();
		    
		   
		    
		    
		    /*
		    ContentValues values = new ContentValues(); 
		    values.put("_id", maxval+1); 
		    values.put("p1", p1); 
		    values.put("m1", m1); 
		    values.put("p2", m2); 
		    values.put("coor", coor); 
			    values.put("periodos", periodos_id); 
		    values.put("tostasi", tostasi); 
		    if(metrisi.stasi_index>=0){
		    	values.put("stasi_index",params.staseis.item.get(metrisi.stasi_index)._id); 
		    	 Log.i("Metrisi add", "stasi_index : " +params.staseis.item.get(metrisi.stasi_index)._id);
		    }else{
		    	values.put("stasi_index",-1);
		    	 Log.i("Metrisi add", "stasi_index : " +(-1));
		    }
		    
		    */
		
		    //myDataBase.insert("metrisi", null, values);
		   // metrisi._id= maxval+1;
		   
		    
		    Log.i("Metrisi add", "_id : " + _id);
		    Log.i("Metrisi add", "p1 : " +p1);
		    Log.i("Metrisi add", "m1 : " + m1);
		    Log.i("Metrisi add", "p2 : " + p2);
		    Log.i("Metrisi add", "m2 : " + m2);
		    Log.i("Metrisi add", "coor : " + coor[0] + "," + coor[1] +  "  -  " + coor[2] + "," + coor[3]);
		   
		    
		    
		    
		   // stasi._id=maxval+1;
		   
		   
		  }
	  
	  
	  


	  public void createTables() {
		  String CREATE_PARAMS_TABLE = "CREATE TABLE IF NOT EXISTS params ("
	                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "key" + " TEXT, " + "value" + " TEXT  )";
		  myDataBase.execSQL(CREATE_PARAMS_TABLE);
	        
		  
		  String sql = "SELECT * FROM staseis LIMIT 0,1";
		  Cursor tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("global_id")==-1){
			  sql = "ALTER TABLE staseis  ADD global_id NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  sql = "SELECT * FROM staseis LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_tabId")==-1){
			  sql = "ALTER TABLE staseis  ADD _t_tabId NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  

		  sql = "SELECT * FROM staseis LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_project")==-1){
			  sql = "ALTER TABLE staseis  ADD _t_project NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  
		  sql = "SELECT * FROM staseis LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_id")==-1){
			  sql = "ALTER TABLE staseis  ADD _t_id NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  
		  sql = "SELECT * FROM staseis LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_base")==-1){
			  sql = "ALTER TABLE staseis  ADD _t_base NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  
		  
		  sql = "SELECT * FROM periodos LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_tabId")==-1){
			  sql = "ALTER TABLE periodos  ADD _t_tabId NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  sql = "SELECT * FROM periodos LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("_t_id")==-1){
			  sql = "ALTER TABLE periodos  ADD _t_id NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  
		  
		  sql = "SELECT * FROM metrisi LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("odefsi_use")==-1){
			  sql = "ALTER TABLE metrisi  ADD odefsi_use NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  sql = "SELECT * FROM metrisi LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("net_id")==-1){
			  sql = "ALTER TABLE metrisi  ADD net_id NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  
		  
		  
		  
		  
		  
		  
		  String CREATE_HOUSES_TABLE = "CREATE TABLE IF NOT EXISTS houses ("
	                + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "type" + " NUMERIC, " + "project" + " NUMERIC, " + "floors" + " NUMERIC, " + "sxolia" + " TEXT, " + "path" + " TEXT  )";
		  myDataBase.execSQL(CREATE_HOUSES_TABLE);
	       
		  
		  /*
		   * 
		  sql = "SELECT * FROM houses LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("project")==-1){
			  sql = "ALTER TABLE houses  ADD project NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		 
		  
		  sql = "SELECT * FROM houses LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("f")==-1){
			  sql = "ALTER TABLE houses  ADD f NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  sql = "SELECT * FROM houses LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("l")==-1){
			  sql = "ALTER TABLE houses  ADD l NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		  sql = "SELECT * FROM houses LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("x")==-1){
			  sql = "ALTER TABLE houses  ADD x NUMERIC";
			  myDataBase.execSQL(sql);
		  }
		 
		  sql = "SELECT * FROM houses LIMIT 0,1";
		  tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex("y")==-1){
			  sql = "ALTER TABLE houses  ADD y NUMERIC";
			  myDataBase.execSQL(sql);
		  }*/
		  
		  
		  check_add_field("houses"		,"project");
		  check_add_field("houses"		,"f");
		  check_add_field("houses"		,"l");
		  check_add_field("houses"		,"x");
		  check_add_field("houses"		,"y");
		  
		  
		  check_add_field("houses"		,"local_date");
		  check_add_field("staseis"		,"local_date");
		  check_add_field("periodos"	,"local_date");
		  check_add_field("metrisi"		,"local_date");
		  
		 
		  
		  check_add_field("houses"		,"tocast");
		  check_add_field("staseis"		,"tocast");
		  check_add_field("periodos"	,"tocast");
		  check_add_field("metrisi"		,"tocast");
		  
		  
		  
		  
		  check_add_field("project"		,"staseis_last_sync_date");
		  check_add_field("project"		,"periodos_last_sync_date");
		  check_add_field("project"		,"metrisi_last_sync_date");
		 
				 
		  check_add_field("staseis"		,"global_id");
		  check_add_field("periodos"	,"global_id");
		  check_add_field("metrisi"		,"global_id");
		 
		  check_add_field("metrisi"		,"_t_tabId");
		  check_add_field("metrisi"		,"date");
		 
		  
		  check_add_field("line"		,"type");
		  check_add_field("line"		,"sxolia");
						  
		  
		 
	  }
	  
	  
	  public void execSQL(String sql){
		  myDataBase.execSQL(sql);
	       
	  }
	  
	  public void check_add_field(String table, String field){
		  String sql = "SELECT * FROM " + table + " LIMIT 0,1";
		  Cursor tmp =  myDataBase.rawQuery(sql, null);
		  if(tmp.getColumnIndex(field)==-1){
			  sql = "ALTER TABLE " + table + "  ADD " + field + " NUMERIC";
			  myDataBase.execSQL(sql);
			  Log.i("DB", "field '" + field + "' added to table '" + table + "'");
		  }
	  }
	  
	  public void removeLineFromDb( long _id) {
		  myDataBase.delete("line", "_id=" + _id, null);  
	  }
	  
	  

	  public void removeStaseisFromDb( ) {
		  myDataBase.delete("staseis", null, null);  
	  }
	   
	  
	  
	  public void updateField(String table, String column, long value, long _id) {
		   // List<Category> categories = new ArrayList<Category>();
		 
		  ContentValues values = new ContentValues(); 
		  values.put("start_stasi", value); 
		  myDataBase.update(table, values, "_id="+_id, null);
		   
		  }
	  
	  

	  public void updateField(String table, String column, long value, String where) {
		  // not used akoma
		   // List<Category> categories = new ArrayList<Category>();
		 
		  ContentValues values = new ContentValues(); 
		  values.put("start_stasi", value); 
		  myDataBase.update(table, values, where, null);
		   
		  }
	  
	  
	  

	  public void updateLineMeta(LineMeta meta, long _id){
		  ContentValues values = new ContentValues(); 
		  values.put("type", meta.type); 
		  values.put("sxolia", meta.sxolia); 
		  myDataBase.update("line", values, "_id=" + _id, null);
	  }
	  
	  
	  public void updatePointMeta(String sxolia, int type, long _id){
		  Log.i("update type", _id + " --> " + type);
		  ContentValues values = new ContentValues(); 
		  values.put("obtype", type); 
		  values.put("sxolia", sxolia); 
		  myDataBase.update("metrisi", values, "_id=" + _id, null);
	  }
	  
	  
	  
	  public void updateStasiCoor(MyStasi stasi){
		  
		  double[] xy =params.wms.proj.fl2EGSA87(stasi.f, stasi.l);
			
		  
		  Log.i("update stasi", stasi._id + " -- ");
		  ContentValues values = new ContentValues(); 
		  values.put("f", stasi.f); 
		  values.put("l", stasi.l); 
		  values.put("x", xy[0]); 
		  values.put("y", xy[1]); 
		 int res =  myDataBase.update("staseis", values, "_id=" + stasi._id, null);
		 Log.i("update stasi","f: " + stasi.f + "   l : " + stasi.l);
		 Log.i("update stasi", stasi._id + " -- " + res + " rows affected");
			 
		 
	  }
	  
	  
	  
  public void updateStasiGlobalId(MyStasi stasi){
		  Log.i("db - updateStasiGlobalId", stasi.global_id + " -- ");
		  ContentValues values = new ContentValues(); 
		  values.put("global_id", stasi.global_id); 
		  int res =  myDataBase.update("staseis", values, "_id=" + stasi._id, null);
		
	  }
  

  public void updateStasiTimeStamp(MyStasi stasi){
		  Log.i("db - updateStasiTimeStamp", stasi.date + " -- ");
		  ContentValues values = new ContentValues(); 
		  values.put("date", stasi.date); 
		  int res =  myDataBase.update("staseis", values, "_id=" + stasi._id, null);
		
	  }
  
  
  

  public void updateTableColumn(String table,String column, Integer value,String where){
	  ContentValues values = new ContentValues(); 
	  values.put(column, value); 
	  int res =  myDataBase.update(table, values, where, null);
	  Log.i("Db Update field",table + " , " + column + " , " + value + " , " + where + " , " + value + " : " + res);

  }
  public void updateTableColumn(String table,String column, String value,String where){
	  ContentValues values = new ContentValues(); 
	  values.put(column, value); 
	  int res =  myDataBase.update(table, values, where, null);
	  Log.i("Db Update field",table + " , " + column + " , " + value + " , " + where + " , " + value + " : " + res);

  }

  public void updateTableColumn(String table,String column, long value,String where){
	  ContentValues values = new ContentValues(); 
	  values.put(column, value); 
	  int res =  myDataBase.update(table, values, where, null);
	  Log.i("Db Update field",table + " , " + column + " , " + value + " , " + where + " , " + value + " : " + res);
  }
  
  public void updateTableColumn(String table,String column, double value,String where){
	  ContentValues values = new ContentValues(); 
	  values.put(column, value); 
	  int res =  myDataBase.update(table, values, where, null);
	  Log.i("Db Update field",table + " , " + column + " , " + value + " , " + where + " , " + value + " : " + res);
  }
  
  public void updateStasiflxysxolianame(MyStasi stasi){
		  Log.i("db - updateStasiflxysxolianame", stasi.date + " -- ");
		  ContentValues values = new ContentValues(); 
		  values.put("f", stasi.f); 
		  values.put("l", stasi.f); 
		  values.put("x", stasi.f); 
		  values.put("y", stasi.f); 
		  values.put("sxolia", stasi.f); 
		  values.put("name", stasi.f); 
		  
		  int res =  myDataBase.update("staseis", values, "_id=" + stasi._id, null);
		
	  }
  
  
  public void updateStasiDataFromAddToBase(MyStasi stasi){
	  Log.i("db - updateStasiDataFromAddToBase", stasi.date + " -- ");
	  ContentValues values = new ContentValues(); 
	  values.put("_id"			, stasi._id); 
	  values.put("_t_id"		, stasi._t_id); 
	  values.put("_t_project"	, stasi._t_project); 
	  values.put("_t_tabId"		, stasi._t_tabId); 
	  values.put("global_id"	, stasi.global_id); 
	  values.put("date"	, stasi.date); 
		  
	  if(stasi._t_base){
		  values.put("_t_base"		, 1); 
		  Log.i("Db updateStasiDataFromAddToBase", "_t_base  : " + 1);
	  }else{
		  values.put("_t_base"		, 0); 
		  Log.i("Db updateStasiDataFromAddToBase", "_t_base  : " + 0);
	  }
	  
	  
	  int res =  myDataBase.update("staseis", values, "_id=" + stasi._t_id, null);
	  Log.i("Db updateStasiDataFromAddToBase", " : " + res);

  }

  
	  
	  public void setOdefsiUse(boolean value, long _id){
		  ContentValues values = new ContentValues(); 
		  values.put("odefsi_use", value); 
		  myDataBase.update("metrisi", values, "_id=" + _id, null);
	  }
	  
	  

	  
	  public boolean stasiExists(MyStasi stasi){
		  
		  boolean 	out 	= false;
		 // String[] 	columns = {"_id","project","_t_tabId"};
		  Cursor cursor = myDataBase.rawQuery("SELECT _id FROM staseis WHERE "
							  + " _t_project=" + stasi._t_project
							  + " AND  _t_id=" + stasi._t_id
							  + " AND  _t_tabId=" + stasi._t_tabId
							 , null);
		  
		  if(cursor.getCount()>0){
			  out=true;
		  }
		 return out;
	  }
	  
	  
	  

	  private MyStasi cursorToStasi(Cursor cursor, boolean fixed) {
		  double x = cursor.getDouble(7);
		  double y = cursor.getDouble(8);
		  Log.i("x : " + x , "y : " + y);
		  double[] fl = params.wms.proj.Egsa2fl84(x, y);
		  
		 MyStasi stasi 	= 	new MyStasi(cursor.getString(1),fl[1],fl[0],params.app);
		 stasi._id		=	cursor.getLong(0);
		 stasi.name		=	cursor.getString(1);
		 stasi.type		=	(int) cursor.getLong(2);
		 
		 double tmp 	= 	cursor.getDouble(3);; if(tmp==0d){ stasi.fixed=true;}else{stasi.fixed=false;};
		 stasi.shor		=	(float) cursor.getDouble(4);
		 stasi.sxolia	=	cursor.getString(5);
		 stasi.user		=	cursor.getString(6);
		 stasi.x		=	cursor.getDouble(7);
		 stasi.y		=	cursor.getDouble(8);
		 stasi.f		=	cursor.getDouble(9);
		 stasi.l		=	cursor.getDouble(10);
		 stasi.date=(int) cursor.getLong(11);
		 stasi.foto		=	cursor.getString(12);
		 stasi.h		=	cursor.getDouble(13);
		 stasi.h_ort	=	cursor.getDouble(14);
		 stasi.sx		=	(float) cursor.getDouble(15);
		 stasi.sy		=	(float) cursor.getDouble(16);
		 stasi.sh		=	(float) cursor.getDouble(17);
		 stasi.global_id		=	 cursor.getLong(18);
		 stasi._t_id		=	 (int) cursor.getLong(19);
		 stasi._t_tabId		=	 (int) cursor.getLong(20);
		 stasi._t_project		=	 (int) cursor.getLong(21);
		 stasi.fixed		=	fixed;
		 
		 

		 stasi.local_date		=	 (int) cursor.getLong(23);
		 
		 if( cursor.getLong(22)==1){
				stasi._t_base		=	true;
			}else{
				stasi._t_base		=	false;
			}
		 if( cursor.getLong(24)==1){
				stasi.toCast		=	true;
			}else{
				stasi.toCast		=	false;
			}
		 
		 
		 
		 return stasi;
	  }
	  
	  
	  
	  
	  
	  
	  

	  private MyStasi cursorToStasi(Cursor cursor) {
		  double x = cursor.getDouble(7);
		  double y = cursor.getDouble(8);
		  double[] fl = new Proj().Egsa2fl84(x, y);
		  
		 MyStasi stasi 	= 	new MyStasi();
		 stasi._id		=	cursor.getLong(0);
		 stasi.name		=	cursor.getString(1);
		 stasi.type		=	(int) cursor.getLong(2);
		 
		 double tmp 	= 	cursor.getDouble(3);; if(tmp==0d){ stasi.fixed=true;}else{stasi.fixed=false;};
		 stasi.shor		=	(float) cursor.getDouble(4);
		 stasi.sxolia	=	cursor.getString(5);
		 stasi.user		=	cursor.getString(6);
		 stasi.x		=	cursor.getDouble(7);
		 stasi.y		=	cursor.getDouble(8);
		 stasi.f		=	cursor.getDouble(9);
		 stasi.l		=	cursor.getDouble(10);
		 stasi.date=(int) cursor.getLong(11);
		 stasi.foto		=	cursor.getString(12);
		 stasi.h		=	cursor.getDouble(13);
		 stasi.h_ort	=	cursor.getDouble(14);
		 stasi.sx		=	(float) cursor.getDouble(15);
		 stasi.sy		=	(float) cursor.getDouble(16);
		 stasi.sh		=	(float) cursor.getDouble(17);
		 stasi.global_id		=	 cursor.getLong(18);

		 stasi._t_id		=	 (int) cursor.getLong(19);
		 stasi._t_tabId		=	 (int) cursor.getLong(20);
		 stasi._t_project		=	 (int) cursor.getLong(21);
		 stasi.local_date		=	 (int) cursor.getLong(23);
		 
		
		 if( cursor.getLong(24)==1){
				stasi.toCast		=	true;
			}else{
				stasi.toCast		=	false;
			}
			 
		 
		if( cursor.getLong(3)==1){
			stasi.fixed		=	true;
		}else{
			stasi.fixed		=	false;
		}
		
		if( cursor.getLong(22)==1){
			stasi._t_base		=	true;
		}else{
			stasi._t_base		=	false;
		}
		
		
		 
		 return stasi;
	  }
	  
	  
	  
	  

	  private MyHouse cursorToHouse(Cursor cursor) {
		 //"_id","sxolia","path","type","floors","f","l","x","y"
			
		  
		  double x = cursor.getDouble(7);
		  double y = cursor.getDouble(8);
		  Log.i("xy",x+"," + y);
		  double[] fl = new Proj().Egsa2fl84(x, y);
		  
		 MyHouse house 	= 	new MyHouse(fl[1],fl[0],params.app);
		 house._id		=	cursor.getLong(0);
		 house.sxolia		=	cursor.getString(1);
		 house.path		=	cursor.getString(2);
		 
		 house.x		=	cursor.getDouble(7);
		 house.y		=	cursor.getDouble(8);
		 house.f		=	cursor.getDouble(5);
		 house.l		=	cursor.getDouble(6);
		 house.type=(int) cursor.getLong(3);
		 house.floors		=	(int) cursor.getLong(4);
		 
		
		
		 
		 return house;
	  }
	   
	  


	  private MyProject cursorToProject(Cursor cursor) {
		 MyProject project 	= 	new MyProject(params);
		 project._id		=	cursor.getLong(0);
		 project.name		=	cursor.getString(1);
		 project.sxolia		=	cursor.getString(2);
			 
		 project.user			=	cursor.getString(3);
		 project.start_stasi	=	cursor.getLong(4);
		 project.stasi_0		=	cursor.getLong(5);
		 project.date		=	cursor.getLong(6);
		 
		 project.staseis_last_sync_date=cursor.getLong(7);
		 project.periodos_last_sync_date=cursor.getLong(8);
		 project.metrisi_last_sync_date=cursor.getLong(9);
			 
		
		 return project;
	  }
	  
	  
	  
	  

	  private MyMeasureSet cursorToPeriodos(Cursor cursor) {
		 
		  
		  MyMeasureSet periodos 	= 	new MyMeasureSet(0, params);
		  periodos._id		=	cursor.getLong(0);
		 
		  periodos.stasi 			= params.staseis.stasi_id_to_index(cursor.getLong(1));
		  periodos.stasi_0 			= params.staseis.stasi_id_to_index(cursor.getLong(2));
		  periodos.stasi_id 			= (int) cursor.getLong(1);
		  periodos.stasi_0_id 			= (int) cursor.getLong(2);
		  periodos.stasi_0_angle	= cursor.getFloat(3);
		  periodos.YO				= cursor.getFloat(4);
		  periodos._t_id				= cursor.getLong(5);
		  periodos._t_tabId				= (int) cursor.getLong(6);
		  periodos.project				= cursor.getLong(7);
		  periodos.date				= 	(int) cursor.getLong(8);
		  periodos.global_id				= 	 cursor.getLong(9);
		  periodos.local_date				= 	(int) cursor.getLong(10);
		  


		 return periodos;
	  }
	  
	  
	  


	  private stasi_link_class cursorToStasiLink(Cursor cursor) {
		 
		  
		  stasi_link_class st 	= 	new stasi_link_class(0);
		  st._id		=	cursor.getLong(0);
		  if(cursor.getInt(1)==1) st.isbase=true;
		  if(cursor.getInt(1)==0) st.isbase=false;
			 
		 

		 return st;
	  }
	  
	  
		  

	  private MyMeasurement cursorToMetrisi(Cursor cursor) {
		 
		  //metrisi._id,metrisi.stasi_index,metrisi.tostasi,metrisi.periodos,metrisi.type,metrisi.hZ,metrisi.sD "
		//  "_id,stasi_index,tostasi,periodos,type,hZ,sD,vZ,obtype,sxolia,odefsi_use,ys,global_id,local_date "		
		  MyMeasurement metrisi 	= 	new MyMeasurement();
		  metrisi._id		=	cursor.getLong(0);
		  metrisi.stasi_index 		= params.staseis.stasi_id_to_index(cursor.getLong(1));
		  metrisi.stasi_index_id 		= (int) cursor.getLong(1);
			 // metrisi.tostasi = cursor.getLong(2);
		  //Log.i(" periodos id to index",cursor.getLong(3) + "-->" + measureSet_id_to_index(cursor.getLong(3)));
		  metrisi.periodos			=	(int) measureSet_id_to_index(cursor.getLong(3));
		  metrisi.type				=	cursor.getInt(4);
		  metrisi.hZ				=	(float) cursor.getFloat(5);
		  metrisi.sD				=	(float) cursor.getFloat(6);
		  metrisi.vZ				=	(float) cursor.getFloat(7);
		  metrisi.hD				=	(float) (metrisi.sD*Math.sin(metrisi.vZ*Math.PI/200));
		  metrisi.obtype				=	(int) cursor.getLong(8);
		  metrisi.sxolia				=	 cursor.getString(9);
		  
		  metrisi.odefsi_use				=	 false;
		  
		  metrisi.ys=cursor.getFloat(11);
		  
		  metrisi.global_id=cursor.getLong(12);
		  
		  metrisi.local_date=(int) cursor.getLong(13);
		  metrisi.date=(int) cursor.getLong(14);
			  
		  
			  if( metrisi.stasi_index>-1){
			  int ou = cursor.getInt(10);;
			  if(ou==1){
				  metrisi.odefsi_use=true;
			  }
		  }
				  
		//  Log.i("db load metrisi type","t : " +metrisi.obtype );
		  
		 return metrisi;
	  }
	  
	  
	  


	  private double[] cursorToLine(Cursor cursor) {
		 
		  double[] out = new double[4];
		 
		  byte[] mc = cursor.getBlob(0);
		  ByteBuffer bytebuf = ByteBuffer.wrap(mc);
		  
		  out[0] = bytebuf.getDouble();
		  out[1] = bytebuf.getDouble();
		  out[2] = bytebuf.getDouble();
		  out[3] = bytebuf.getDouble();
			

		 return out;
	  }
	  
	  
	  
	  
	  
	  private long measureSet_id_to_index(long id){
		  int i ;
		  long out = -1;
		  for(i=0;i<=params.msets.size()-1;i++){
			  if(params.msets.get(i)._id==id){
				  out=i;
				  i=params.msets.size();
			  }
		  }
		  return out;
	  }
	  
	  
	  
	  
	  
	  public void insert_metrisi_if_not_exists(String data_str_from_sync){
		  //PARADOXI
		  // i metrisi pou erxetai anoikei se allo tablet ara tha exei net_id an einai idi kataxorimeni
		  
		  
		  
		  String[] columns = {	"MAX(_id)"};
		  Cursor cursor = myDataBase.query( "metrisi",  columns, null, null, null, null, null);
		  cursor.moveToFirst();
		  long maxval=cursor.getLong(0);
		  cursor.close();


		  
		  String[] data = data_str_from_sync.split(";");
		  
		  long per 			= Long.valueOf(data[0]);
		  long net_id 		= Long.valueOf(data[1]);
		  long stasi		= Long.valueOf(data[2]);
		  float hZ 			= Float.valueOf(data[3]);
		  float vZ 			= Float.valueOf(data[4]);
		  float sD 			= Float.valueOf(data[5]);
		  int type 			= Integer.valueOf(data[6]);
		  float ys 			= Float.valueOf(data[7]);
		  int obtype 		= Integer.valueOf(data[8]);
		  String sxolia 	= data[9];
		  double x 			= Double.valueOf(data[10]);
		  double y 			= Double.valueOf(data[11]);
		  int odefsi_use 	= Integer.valueOf(data[12]);
		  
		  String[] columns2 = {	"count(_id)","_id"};
		  Cursor cursor2 = myDataBase.query( "metrisi",  columns2, "periodos=" + per + " AND net_id=" + net_id, null, null, null, null);
		  cursor2.moveToFirst();
		  long count=cursor2.getLong(0);
		 
			 

		 
		  
		  if(count==0){
			  String query = " INSERT INTO metrisi (_id,periodos,net_id,hZ,vZ,sD,stasi_index,odefsi_use,type,ys,obtype,sxolia) "
			  			+	" VALUES (" + (maxval+1)  + "," + per + "," + net_id + "," + hZ + "," + vZ + "," + sD + "," + stasi + "," + odefsi_use + "," + type + "," + ys + "," + obtype + ",'" + sxolia + "'" + ")"
			  			;
			  Log.i("DatabaseHelper insert_metrisi_if_not_exists",query);
			  myDataBase.execSQL(query);
		  }else{
			  long existing_id=cursor2.getLong(1);
			  myDataBase.execSQL("DELETE FROM metrisi WHERE _id=" + existing_id);
			  
			  String query = " INSERT INTO metrisi (_id,periodos,net_id,hZ,vZ,sD,stasi_index,odefsi_use,type,ys,obtype,sxolia) "
			  			+	" VALUES (" + existing_id  + "," + per + "," + net_id + "," + hZ + "," + vZ + "," + sD + "," + stasi + "," + odefsi_use + "," + type + "," + ys + "," + obtype + ",'" + sxolia + "'" + ")"
			  			;
			  Log.i("DatabaseHelper insert_metrisi_if_not_exists",query);
			  myDataBase.execSQL(query);
			  
			  Log.i("DatabaseHelper insert_metrisi_if_not_exists","record found and ovewrited");
		  }
		  cursor2.close();
		 /* String query = "IF NOT EXISTS "
				  			+	" (SELECT * FROM metrisi WHERE periodos=" + per + " AND net_id=" + net_id + ")"
				  			+	" BEGIN "
				  			+ 	" INSERT INTO metrisi (_id,periodos,net_id,hZ,vZ,sD,stasi_index,odefsi_use) "
				  			+	" VALUES (" + (maxval+1)  + "," + per + "," + net_id + "," + hZ + "," + vZ + "," + sD + "," + stasi + "," + odefsi_use + ")"
				  			+ " END ";
  */
		  
		
		  
		  //long per 		= Long.getLong(data[0]);
			  
		  
		  
		  
		  
	  }
	  
	  
	  
}