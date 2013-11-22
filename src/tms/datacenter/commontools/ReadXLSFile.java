package tms.datacenter.commontools;


import java.io.File;     
import java.io.FileInputStream;     
import jxl.Cell;     
import jxl.Sheet;     
import jxl.Workbook;

/**  
*  ��ȡXLS�ļ�
*  
* @author ������
* 2012.12.19 
*/

public class ReadXLSFile {
	public String getContent(File file) throws Exception {     
		// TODO
		// 1����ȡָ���Ĺ�����
		// 2�������������������
		// 3���������С������зֱ����趨��XML����У�飬ȷ�������ϴ��ĺ�����
	    FileInputStream fileInputStream = new FileInputStream(file);
	    
	    StringBuilder stringBuilder = new StringBuilder();     
	    Workbook workbook = Workbook.getWorkbook(fileInputStream);     
	    
	    //Ҳ���Ե���getsheet������ȡָ���Ĺ�����  
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
