package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * read data from file conveniently
 * firstly, call initBufferedReader() with filename parameter
 * secondly, call readLine()
 * lastly, call close()
 * @author zhuhan
 */

public class FReader{
	private String filePath = "";
	private BufferedReader bR= null;
	
	public FReader(){
	}
	
	public void initBufferedReader(String fPath){
		this.filePath = fPath;
		initBufferedReader();
	}
	
	public void initBufferedReader(){
		try{
			File f = new File(filePath);
			bR = new BufferedReader(new FileReader(f));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String readLine(){
		try{
			return bR.readLine();
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void close(){
		if(bR != null){
			try{
				bR.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}