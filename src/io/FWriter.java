package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * write data to file conveniently
 * firstly, call initWriterU8() with filename parameter, encoding utf-8
 * secondly, call write() or writeLine() to write data
 * lastly, call close()
 * @author zhuhan
 */

public class FWriter{
	private String filePath = "";
	private OutputStreamWriter osw = null;
	private FileOutputStream fos = null;
	
	public FWriter(){
	}
	
	public FWriter(String fP){
		this.filePath = fP;
	}
	
	public void initWriterU8(String fP){
		this.filePath = fP;
		initWriterU8();
	}
	
	public void initWriterU8(){
		try{
			fos = new FileOutputStream(new File(filePath));
			osw = new OutputStreamWriter(fos, "UTF-8");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void write(String str){
		try{
			osw.write(str);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void writeLine(String str){
		try{
			osw.write(str);
			osw.write("\r\n");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		try{
			if(osw != null){
				osw.close();
			}
			if(fos != null){
				fos.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}