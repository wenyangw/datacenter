package tms.datacenter.commontools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取XLS文件
 * 
 * @author 王文阳 2012.12.19
 */

public class ExcelReader {
	private File file;
	private String fileType;
	
	public static final String EXCEL2003 = "application/vnd.ms-excel";
	public static final String EXCEL2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	public ExcelReader(File file, String fileType) {
		this.file = file;
		this.fileType = fileType;
	}
	
//	public void setFileType(String fileName){
//		int index = fileName.indexOf('.');
//		this.fileType = fileName.substring(index + 1);
//	}
	
	public String getFileName(File file){
		return file.getName();
	}
	
	public boolean isExcel(){
		if(getFileName(file).endsWith(".xls") || getFileName(file).endsWith(".xlsx")){
			return true;
		}
		return false;
	}
	
	public Workbook getWorkbook() throws Exception{
		Workbook wb = null;
		
		//InputStream inputStream = new FileInputStream(file);
		if(fileType.equals(EXCEL2003)){
			wb = new HSSFWorkbook(); 
		}else{
			wb = new XSSFWorkbook();
		}
		wb = WorkbookFactory.create(file);

		return wb;
	}
	
	public Sheet getSheet() throws Exception{
		return getWorkbook().getSheetAt(0);
	}
    
    public String getContent() throws Exception {
		// TODO
		// 1、读取指定的工作表
		// 2、分离标题行与内容行
		// 3、将标题行、内容行分别与设定的XML进行校验，确保数据上传的合理性
		//FileInputStream fileInputStream = new FileInputStream(file);

		StringBuilder stringBuilder = new StringBuilder();

		for (Row row : getSheet()) {
			for (Cell cell : row) {
				// CellReference cellRef = new CellReference(row.getRowNum(),
				// cell.getColumnIndex());
				// System.out.print(cellRef.formatAsString());
				String str = "";

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					String s = cell.getRichStringCellValue().getString();
					if (s.trim().length() <= 0){
						s = " ";
					}
					str += s;
					//str += cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (org.apache.poi.ss.usermodel.DateUtil
							.isCellDateFormatted(cell)) {
						str += cell.getDateCellValue();

					} else {
						str += cell.getNumericCellValue();
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					str += cell.getBooleanCellValue();
					break;
				case Cell.CELL_TYPE_FORMULA:
					str += cell.getCellFormula();
					break;
				default:
					System.out.println();
				}
				stringBuilder.append(str + "\t");
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
    }
		
		public List getExcelContents() throws Exception {
			// TODO
			// 1、读取指定的工作表
			// 2、分离标题行与内容行
			// 3、将标题行、内容行分别与设定的XML进行校验，确保数据上传的合理性
			//FileInputStream fileInputStream = new FileInputStream(file);
			List lists = new ArrayList();

			for (Row row : getSheet()) {
				List list = new ArrayList();
				for (Cell cell : row) {
					// CellReference cellRef = new CellReference(row.getRowNum(),
					// cell.getColumnIndex());
					// System.out.print(cellRef.formatAsString());
					

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						list.add(cell.getRichStringCellValue().getString());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						
						if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
							list.add(DateUtil.dateToString(cell.getDateCellValue(), DateUtil.ISO_EXPANDED_DATE_FORMAT));
						} else {
							//System.out.println(cell.getNumericCellValue());
							//DecimalFormat formatter = new DecimalFormat("########");
							double d = cell.getNumericCellValue();
						    String str = "";
							if (d - (int) d < Double.MIN_VALUE) { 
						    // 是否为int型
						    	str = Integer.toString((int) d);
						    }else { 
						    	System.out.println("double.....");
						    	// 是否为double型
						    	str = Double.toString(cell.getNumericCellValue());
						    }
							list.add(str);
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						list.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						list.add(cell.getCellFormula());
						break;
					default:
						list.add("");
					}
				}
				lists.add(list);
			}
		return lists;
		// return stringBuilder.toString();
	}
	
	
	
	public static void main(String[] args) throws Exception {
		//File file = new File("D:\\book1.xlsx");
		String fileName = "D:\\book1.xls";
		ExcelReader read = new ExcelReader(new File(fileName), EXCEL2003);
		if(read.isExcel()){
			//read = new ExcelReader(fileName);
			List lists = read.getExcelContents();
			for(Object o : lists){
				List list = (List)o;
				for(Object oo : list){
					System.out.print(String.valueOf(oo) + "\t");
				}
				System.out.print("\n");
			}
		}else{
			System.out.println("文件格式不对！");
		}
		
	}

}
