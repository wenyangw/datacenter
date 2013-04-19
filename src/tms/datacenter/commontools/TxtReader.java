package tms.datacenter.commontools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class TxtReader {
	private File file;
	
	public TxtReader(File file){
		this.file = file;
	}
	
	public List getLines(){
		List lines = new ArrayList();
		String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
		try{
			if (file.isFile() && file.exists()) {   
				InputStreamReader read = new InputStreamReader(   
	    			new FileInputStream(file), encoding);   
				BufferedReader bufferedReader = new BufferedReader(read);   
				String lineTxt = null;   
				while ((lineTxt = bufferedReader.readLine()) != null) {   
					//System.out.println(lineTXT.indexOf("\t"));
					lines.add(lineTxt);
				}   
				read.close();   
			}else{   
				return null;   
			}   
		} catch (Exception e) {      
			e.printStackTrace();
			return null;
		}
		return lines;
	}
	
	public List getTxtContents(String separate){
		List lines = getLines();
		List contents = new ArrayList();
		
		if(lines == null){
			return null;
		}
		for(Object o : lines){
			String strLine = (String)o;
			String[] words = strLine.split(separate);
			if(words == null){
				return null;
			}
			List line = new ArrayList();
			for(String word : words){
				line.add(word.trim());
			}
			contents.add(line);
		}
		
		return contents;
	}
	
	public static void main(String[] args){
		File file = new File("d:/cc.txt");
		
		TxtReader reader = new TxtReader(file);
		List contents = reader.getTxtContents("\t");
		if(contents == null){
			System.out.println("文件读取错误！");
			return;
		}
		for(Object o : contents){
			List line = (List)o;
			for(Object oo : line){
				String word = (String)oo;
				System.out.print(word + "\t");
			}
			System.out.println("");
		}
	}
}