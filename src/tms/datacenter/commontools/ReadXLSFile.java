package tms.datacenter.commontools;


import java.io.File;     
import java.io.FileInputStream;     
import jxl.Cell;     
import jxl.Sheet;     
import jxl.Workbook;

/**  
*  读取XLS文件
*  
* @author 王文阳
* 2012.12.19 
*/

public class ReadXLSFile {
	public String getContent(File file) throws Exception {     
		// TODO
		// 1、读取指定的工作表
		// 2、分离标题行与内容行
		// 3、将标题行、内容行分别与设定的XML进行校验，确保数据上传的合理性
	    FileInputStream fileInputStream = new FileInputStream(file);
	    
	    StringBuilder stringBuilder = new StringBuilder();     
	    Workbook workbook = Workbook.getWorkbook(fileInputStream);     
	    
	    //也可以调用getsheet方法获取指定的工作表  
	    Sheet[] sheets = workbook.getSheets();
	    for (int i = 0; i < sheets.length; i++) {     
	        Sheet sheet = workbook.getSheet(i);     
	        for (int j = 0; j < sheet.getRows(); j++) {     
	           
	        	Cell[] cells = sheet.getRow(j);
	           
	        	for(int k = 0;k < cells.length; k++){     
	        		stringBuilder.append(cells[k].getContents() + "\t");
	        	}
	        	stringBuilder.append("\n");
	        }     
	    }
	    
	    fileInputStream.close();
	    
	    return stringBuilder.toString();     
	}
	
	public static void main(String[] args) throws Exception{
		File file = new File("D:\\book1.xls");
		ReadXLSFile read = new ReadXLSFile();
		System.out.println(read.getContent(file));
	}
}
