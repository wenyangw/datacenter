package tms.datacenter.upload.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * �ļ��ϴ�
 * 
 * @author ������
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
		System.out.println("��ʼ��" + new Date());
		int excelCols = 0;
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		//ȡ�ò���Ա��Ϣ
		Record user = (Record)session.getAttribute("dcuser");
		String userName = user.get("username");
		String org = user.get("organisation");
		String department = user.get("department");
		
		
		
		//��õ�ǰʱ��
		String uploadTime = DateUtil.dateToStringWithTime(DateUtil.getCurrentDateTime());
		String logNo = user.get("loginname") + getTime();
		String saveFileName = pkfield + "_" + logNo + "_" + uploadFileName;
		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);	
		
		System.out.println("��ʼ�����ļ���" + new Date());
		uploadFile(saveFileName);
		
		
		TableConfig tc = TableConfig.getInstance();
		TableDesc td = tc.getTable(pkfield);
		
		
		//�ϴ��ļ�ֻ�ܽ���excel2003��2007��ʽ�Լ�txt�ı��ļ�
		if(!(uploadContentType.equals(EXCEL2003) || uploadContentType.equals(EXCEL2007) || uploadContentType.equals(TXTFILE))){
			request.setAttribute("errorMsg", "�ϴ��ļ���ʽ����");
			delFile(saveFileName);
			return "error";
		}
		
		//��ȡ�ϴ������ļ����õ���Ӧ�ϴ���Ŀ���ֶ���
		UploadConfig uc = UploadConfig.getInstance();
		UploadMsg um = uc.getUpload(pkfield);
		
		
		//���ϴ��ļ�ת��ΪList
		List list = null;
		//�ϴ��ļ�����
		int cols = 0;
		
		System.out.println("��ʼ�����ļ���" + new Date());
		if(uploadContentType.equals(TXTFILE)){
			//�����ı��ļ�
			TxtReader txtReader = new TxtReader(upload);
			list = txtReader.getTxtContents(um.getTxtseparate());
			//ȡ���ı��ļ��ָ���
			
			cols = ((List)list.get(0)).size();
		}else{
			//����excel�ļ�
			ExcelReader er = new ExcelReader(upload, uploadContentType);
			
			//��ȡExcel�ļ�����
			list = er.getExcelContents();
			//�����������У�Ӧ���ڵ���1��
			if(list.size() < 2){
				ServletActionContext.getRequest().setAttribute("errorMsg", "�ϴ��ļ�������");
				delFile(saveFileName);
				return "error";
			}
			
			for(Iterator<String> itr = ((List)list.get(0)).iterator();itr.hasNext();)  
	        {
				String s = itr.next();
				if (s.trim().length() == 0){
					itr.remove();
				}
	        }
			
			//ȡ��excel�ļ�������
			cols = ((List)list.get(0)).size();
			//System.out.println("cols:" + cols);
			//ȥ��������
			list.remove(0);
		}
		
		
		System.out.println("��ʼУ���ļ���" + new Date());
		List uploadFields = um.getColumnList();
		//�ϴ���Ŀ���õ��ֶ������ϴ��ļ�ʵ�ʵ�����Ӧ����һ��
		if(uploadFields.size() != cols){
			ServletActionContext.getRequest().setAttribute("errorMsg", "�ϴ��ļ���Ŀ���ļ���������");
			delFile(saveFileName);
			return "error";
		}
		
		
		
		
		System.out.println("��ʼ����ң����䣺" + new Date());
		//���ϴ�����ת��Ϊ�洢Record��List
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
			//��ÿ����¼�м�������ˡ���λ������
			r.set("username", userName , Field.FIELD_TYPE_TEXT, false);
			r.set("organization", org, Field.FIELD_TYPE_TEXT, false);
			r.set("department", department, Field.FIELD_TYPE_TEXT, false);
			r.set("logno", logNo, Field.FIELD_TYPE_TEXT, false);
			r.set("updatetime", uploadTime, Field.FIELD_TYPE_DATE, false);
			
			//��ÿ��Record����У��
			error = RecordCheck.checkRecord(pkfield, r, false,true);
			if(error != null && error.trim().length() > 0){
				request.setAttribute("errorMsg", "�ڵ�" + (i+1) + "����������ʧ�ܣ�\n" + error + "\n����ϸ�˶ԣ�");
				delFile(saveFileName);
				return "error";
			}
			
			records.add(r);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("��ʼ�����ϴ���־��" + new Date());
		//�����ϴ���־��¼
		Record log = new Record();
//		log.set("logid", "1", Field.FIELD_TYPE_INT, true);
		log.set("uploadname", pkfield, Field.FIELD_TYPE_TEXT, false);
		log.set("username", userName, Field.FIELD_TYPE_TEXT, false);
		log.set("uploadtime", uploadTime, Field.FIELD_TYPE_DATE, false);
		log.set("organization", org, Field.FIELD_TYPE_TEXT, false);
		log.set("department", department, Field.FIELD_TYPE_TEXT, false);
		log.set("logno", logNo, Field.FIELD_TYPE_TEXT, false);
		log.set("locked", "1", Field.FIELD_TYPE_TEXT, false);
		log.set("filename", saveFileName, Field.FIELD_TYPE_TEXT, false);
		
		//�ϴ���־RecordУ��
		error = RecordCheck.checkRecord("dc_uploadlog", log, false, true);
		if(error != null && error.trim().length() > 0){
			request.setAttribute("errorMsg", error + "\n�ϴ���־����ʧ�ܣ������ԣ�");
			delFile(saveFileName);
			return "error";
		}
		
		System.out.println("��ʼ����д�룺" + new Date());
		//�������ݿ�Ĳ���
		ConnectionManage cm = ConnectionManage.getInstance();
		Connection conn = cm.getConnection("datacenter");
		TableManage tm = new TableManage();
		try {
			conn.setAutoCommit(false);
			//����ϴ����ݵ����ݿ�
			for(Record r2 : records){
				int resUpdate = tm.updateRecord(conn, pkfield, r2);
				int res = 0;
				if(resUpdate <= 0){
					//System.out.println("Insert++++++");
					res = tm.insertRecord(conn, pkfield, r2);
				}
				if (res < 0) {
					conn.rollback();
					request.setAttribute("errorMsg", "�ϴ�ʧ�ܣ������ԣ�");
					delFile(saveFileName);
					return "error";
				}
			}
			System.out.println("��ʼд���ϴ���־��" + new Date());
			//����ϴ���־�����ݿ�
			int resLog = tm.insertRecord(conn, "dc_uploadlog", log);
			if (resLog <= 0) {
				conn.rollback();
				request.setAttribute("errorMsg", "�ϴ���־д��ʧ�ܣ������ԣ�");
				delFile(saveFileName);
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
			System.out.println("������" + new Date());
			return "success";
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			cm.freeConnection("datacenter", conn);
		}

		return "error";
	}
	/**
	 * �ļ��ϴ�
	 * @param logNo
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void uploadFile(String fileName) throws FileNotFoundException,
			IOException {
		if(upload != null){
			
				InputStream is = new FileInputStream(upload);
				
				if (is != null){
					String savePath = getUploadPath();
					
					File fileDest = new File(savePath, fileName);
					
					OutputStream os = new FileOutputStream(fileDest);
					
					byte[] buffer = new byte[512];
					int length = 0;
					while(-1 != (length = is.read(buffer))){
						os.write(buffer, 0, length);
					}
					os.close();
				}
				is.close();
				
		}
	}
	private String getUploadPath() {
		//�����ϴ��ļ���Ŀ¼
		String root = ServletActionContext.getServletContext().getRealPath("/uploadfiles");
		//�����ϴ��ļ���Ŀ¼���꣨��λ�����£���λ��
		String savePath = root + DateUtil.getCurrentDateString("/yyyyMM");
		//���洢Ŀ¼�Ƿ����
		File childPath=new File(savePath);
		if(!childPath.exists()){
			childPath.mkdirs();
		}
		return savePath;
	}
	/**
	 * ɾ���ļ�
	 * @param fileName
	 * @return
	 */
	public boolean delFile(String fileName){
		String filePath = getUploadPath();
        File file=new File(filePath + "/" + fileName);  
        if(file.exists()){  
            return file.delete();  
        }  
        return false;  
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
