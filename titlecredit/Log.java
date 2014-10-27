package titlecredit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Log {

	String _vamixDir;

	// For creating log
	Boolean _isTitle;
	String _outputPath;
	String _inputPath;
	String _text;
	String _size;
	String _font;
	String _colour;
	String _position;
	String _duration;
	
	
	// For checking log
	Boolean _checkTitle;
	String _checkOutputPath;
	

	/**
	 * Constructor for adding an entry to log
	 * @param isTitle
	 * @param outputPath
	 * @param inputPath
	 * @param text
	 * @param size
	 * @param font
	 * @param colour
	 * @param position
	 * @param duration
	 */
	public Log(Boolean isTitle, String outputPath, String inputPath,
			String text, String size, String font, String colour,
			String position, String duration) {
		String line;

		String homeDir = System.getProperty("user.home");
		_vamixDir = homeDir + "/.VAMIX";

		_isTitle = isTitle;
		_outputPath = outputPath;
		_inputPath = inputPath;
		_text = text;
		_size = size;
		_font = font;
		_colour = colour;
		_position = position;
		_duration = duration;

		try {
			new FileOutputStream(_vamixDir + "/log.txt", true).close();
			FileWriter logFile = new FileWriter(_vamixDir + "/log.txt", true);
			if(_isTitle){
				line = "Title\t";
			} else{
				line = "Credit\t";
			}
			line += _outputPath + "\t" + _inputPath
					+ "\t" + _text + "\t" + _size + "\t" + _font + "\t"
					+ _colour + "\t" + _position + "\t" + _duration + "\t\n";
			logFile.write(line);
			logFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Constructor for checking for an entry in log
	 * @param isTitle
	 * @param outputPath : the path of the input video
	 * 
	 */
	public Log(Boolean isTitle,String outputPath){
		_checkTitle = isTitle;
		_checkOutputPath = outputPath;
	}
	
	public String checkLog(){
		String homeDir = System.getProperty("user.home");
		_vamixDir = homeDir + "/.VAMIX";
		
		String checkLine;
		if(_checkTitle){
			checkLine = "Title\t";
		} else {
			checkLine = "Credit\t";
		}
		checkLine = checkLine + _checkOutputPath + "\t";
		
		System.out.println(checkLine);
		
		String entry = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(_vamixDir + "/log.txt")));
			String line;
			Boolean isFound = false;
			while (((line = br.readLine()) != null)) {
				System.out.println(line);
			   if(line.startsWith(checkLine)){
				   System.out.println("file found");
				   entry = line;
			   }
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entry;
	}
	
	
}
