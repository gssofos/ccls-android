package com.geocloud.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.test2.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	int counter;
	Button add, sub;
	TextView display;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		counter = 0;
		add = (Button) findViewById(R.id.bAdd);
		sub = (Button) findViewById(R.id.bSub);
		display = (TextView) findViewById(R.id.tvDisplay);
		
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				counter+=1;
				display.setText("Your Total is " + counter);
				
			}
		});

		sub.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				counter--;
				display.setText("Your Total is " + counter);
				
			}
		});
		
		
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder( MainActivity.this);

		dlgAlert.setMessage("This is an alert with no consequence");
		dlgAlert.setTitle("App Title");
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
		
		
		
		
		
		
		/*
		
		try{
			URL yahoo = new URL ("http://www.yahoo.com/");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(yahoo.openStream()));
			
			String inputLine;
			String outLine;
			outLine="";
			
			
			while ((inputLine = in.readLine()) != null){
			   // System.out.println(inputLine);
			    outLine+=inputLine;
			}

			in.close();
			dlgAlert.setMessage(outLine);
			dlgAlert.create().show();
		}catch( MalformedURLException e){
			//e.printStackTrace();
			dlgAlert.setMessage(e.toString());
			dlgAlert.create().show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			dlgAlert.setMessage(e.toString());
			dlgAlert.create().show();
		}
		
		*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
