package com.vikash.jsontoexcel;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class App 
{
	private static String [] columns= {"Servers","Path","Size"};
	private static final String  ROOT_DIR=System.getProperty("user.dir");
	public static final String JSON_FILE=ROOT_DIR+"/server.json";
	private static Logger logger = Logger.getLogger(App.class);  
	

	public static void main( String[] args ) {
		try {
			Utility.getInstance().writeToExcel(JSON_FILE,columns);
		} catch (IOException | InterruptedException e) {
			logger.log(Level.WARN, "Interrupted!", e);
		    Thread.currentThread().interrupt();
		}
		 
		
		
	}
	 
}



