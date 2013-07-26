package tms.datacenter.upload.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.struts2.ServletActionContext;

import tms.datacenter.commontools.DateUtil;
import tms.datacenter.commontools.ExcelReader;
import tms.datacenter.commontools.StringToZn;
import tms.datacenter.commontools.TxtReader;
import tms.datacenter.dbmanage.ConnectionManage;
import tms.datacenter.dbmanage.Field;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.dbmanage.RecordCheck;
import tms.datacenter.dbmanage.TableConfig;
import tms.datacenter.dbmanage.TableDesc;
import tms.datacenter.dbmanage.TableManage;
import tms.datacenter.upload.ColumnMsg;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadMsg;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 文件上传
 * 
 * @author 王文阳
 * 2012.12.20
 *
 */
public class UploadAction extends ActionSupport{
	private String pkfield;
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	public static final String EXCEL2003 = "application/vnd.ms-excel";
	public static final String EXCEL2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String TXTFILE = "text/plain";
	
	@Override
	public String execute() throws Exception {
		int excelCols = 0;
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);	
		TableConfig tc = TableConfig.getInstance();
		TableDesc td = tc.getTable(pkfield);
		
		//List tableFields = new ArrayList();
		
//		if(td == null){
//			request.setAttribute("errorMsg", "上传配置文件错误，请联系管理员！");
//			return "error";
//		}else{
//			tableFields = td.getFieldList();
//		}
		//上传文件只能接收excel2003和2007格式以及txt文本文件
		if(!(uploadContentType.equals(EXCEL2003) || uploadContentType.equals(EXCEL2007) || uploadContentType.equals(TXTFILE))){
			request.setAttribute("errorMsg", "上传文件格式不对");
			return "error";
		}
		
		//读取上传配置文件，得到对应上传项目的字段数
		UploadConfig uc = UploadConfig.getInstance();
		UploadMsg um = uc.getUpload(pkfield);
		
		//将上传文件转换为List
		List list = null;
		
		//上传文件列数
		int cols = 0;
		
		if(uploadContentType.equals(TXTFILE)){
			//处理文本文件
			TxtReader txtReader = new TxtReader(upload);
			list = txtReader.getTxtContents(um.getTxtseparate());
			//取得文本文件分隔符
			
			cols = ((List)list.get(0)).size();
		}else{
			//处理excel文件
			ExcelReader er = new ExcelReader(upload, uploadContentType);
			
			//获取Excel文件行数
			list = er.getExcelContents();
			//不包括标题行，应大于等于1行
			if(list.size() < 2){
				ServletActionContext.getRequest().setAttribute("errorMsg", "上传文件无内容");
				return "error";
			}
			
//			for (Object o : (List)list.get(0)){
//				System.out.println("the list is " + o);
//				
//			}
			for(Iterator<String> itr = ((List)list.get(0)).iterator();itr.hasNext();)  
	        {
				String s = itr.next();
				if (s.trim().length() == 0){
					itr.remove();
				}
	        }
			
			//取到excel文件的列数
			cols = ((List)list.get(0)).size();
			//去掉标题行
			list.remove(0);
		}
		
		
		
		List uploadFields = um.getColumnList();
		//上传项目设置的字段数与上传文件实际的列数应保持一致
		if(uploadFields.size() != cols){
			ServletActionContext.getRequest().setAttribute("errorMsg", "上传文件与目标文件列数不符");
			return "error";
		}
		
		
		
		HttpSession session = request.getSession();
		//取得操作员信息
		Record user = (Record)session.getAttribute("dcuser");
		String userName = user.get("username");
		String org = user.get("organisation");
		String department = user.get("department");
		
		String logNo = user.get("loginname") + getTime();
		
		//获得当前时间
		String uploadTime = DateUtil.dateToStringWithTime(DateUtil.getCurrentDateTime());
		
		//将上传内容转换为存储Record的List
		List<Record> records = new ArrayList<Record>();
		Record r = null;
		List rows = null;
		String str = "";
		String fieldName = "";
		String fieldType = "";
		String fieldValue = "";
		String error = "";
		try{
		for (int i = 0; i < list.size(); i++) {
			rows = (List)list.get(i);
			if (rows.size() == 1){
				break;
			}
			str = "";
			for(Object ss : rows){
				str += ss.toString();
			}
			if (str.trim().length() == 0){
				break;
			}
			r = new Record();
			//for(int j = 0; j < rows.size(); j++){
			for(int j = 0; j < cols; j++){
				fieldName = ((ColumnMsg)uploadFields.get(j)).getFieldname();
				fieldType = ((Field)td.getField(fieldName)).getFieldType();
				fieldValue = "";
				if(rows.size() > j){
					fieldValue = rows.get(j).toString();
				}
				boolean isPk = ((Field)td.getField(fieldName)).isIspk();
				
				r.set(fieldName, fieldValue, fieldType, isPk);
			}
			//在每条记录中加入操作人、单位、部门
			r.set("username", userName , Field.FIELD_TYPE_TEXT, false);
			r.set("organization", org, Field.FIELD_TYPE_TEXT, false);
			r.set("department", department, Field.FIELD_TYPE_TEXT, false);
			r.set("logno", logNo, Field.FIELD_TYPE_TEXT, false);
			r.set("updatetime", uploadTime, Field.FIELD_TYPE_DATE, false);
			
			//对每个Record进行校验
			error = RecordCheck.checkRecord(pkfield, r, false,true);
			if(error != null && error.trim().length() > 0){
				request.setAttribute("errorMsg", "在第" + (i+1) + "行生成数据失败，\n" + error + "\n请仔细核对！");
				return "error";
			}
			
			records.add(r);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//生成上传日志记录
		Record log = new Record();
//		log.set("logid", "1", Field.FIELD_TYPE_INT, true);
		log.set("uploadname", pkfield, Field.FIELD_TYPE_TEXT, false);
		log.set("username", userName, Field.FIELD_TYPE_TEXT, false);
		log.set("uploadtime", uploadTime, Field.FIELD_TYPE_DATE, false);
		log.set("organization", org, Field.FIELD_TYPE_TEXT, false);
		log.set("department", department, Field.FIELD_TYPE_TEXT, false);
		log.set("logno", logNo, Field.FIELD_TYPE_TEXT, false);
		log.set("locked", "1", Field.FIELD_TYPE_TEXT, false);
		
		//上传日志Record校验
		error = RecordCheck.checkRecord("dc_uploadlog", log, false, true);
		if(error != null && error.trim().length() > 0){
			request.setAttribute("errorMsg", error + "\n上传日志生成失败，请重试！");
			return "error";
		}
		
		//进行数据库的操作
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		TableManage tm = new TableManage();
		try {
			conn.setAutoCommit(false);
			//添加上传内容到数据库
			for(Record r2 : records){
				int res = tm.insertRecord(conn, pkfield, r2);
				if (res <= 0) {
					conn.rollback();
					request.setAttribute("errorMsg", "上传失败，请重试！");
					return "error";
				}
			}
			//添加上传日志到数据库
			int resLog = tm.insertRecord(conn, "dc_uploadlog", log);
			if (resLog <= 0) {
				conn.rollback();
				request.setAttribute("errorMsg", "上传日志写入失败，请重试！");
				return "error";
			}
			
			conn.commit();
			request.setAttribute("returnAction", request.getContextPath()
					+ "/upload/uploadManageAction");
			Hashtable params = new Hashtable();
			params.put("methodName", "list");
			params.put("moduleid", moduleid);
			request.setAttribute("params", params);
			list = null;
			return "success";
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}

		return "error";
	}
	
	public static String getTime(){
		SimpleDateFormat format = new SimpleDateFormat("_yyyyMMdd_HHmmss");
		Date date = new Date();
		return format.format(date);
	}

	public String getPkfield() {
		return pkfield;
	}

	public void setpkfield(String pkfield) {
		this.pkfield = pkfield;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
}
