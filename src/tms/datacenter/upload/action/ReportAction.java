package tms.datacenter.upload.action;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.JFrame;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JRViewer;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;
import tms.datacenter.upload.ReportBean;


public class ReportAction  extends PrivilegeParentAction {
	
	public String list() throws Exception {
		HttpServletRequest request = this.getRequest();

		Hashtable parames = new Hashtable();
		parames.put("methodName", "list");

		ContentControl cc = new ContentControl();
		String condition = cc.getControlSQL(this.getLoginUser(), "dc_role");
		
//		UploadConfig uc = UploadConfig.getInstance();
//		ArrayList uploads = uc.getUploadslist();
		//Record user = this.getLoginUser();
		//String loginName = user.get("loginname");
		//UploadPrivilege up = new UploadPrivilege();
		//ArrayList uploads = up.getUserUploads(loginName);
		//request.setAttribute("records", records);
		//request.setAttribute("records", uploads);
		//request.setAttribute("pager", pager.getPage());
		//addFiledShowName();
		//request.setAttribute("fieldslabels", fieldslabels);

		
		String moduleid=StringToZn.toZn(request.getParameter("moduleid"));
		if(moduleid == null)
			moduleid = "";
		request.setAttribute("moduleid", moduleid);
		
		return "list";
	}
	
	public String print() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "2013��ͼ������ͳ��");
		
		List<ReportBean> lists = new ArrayList<ReportBean>();
		lists.add(new ReportBean("���", 100.0, 200.0, 300.0, 400.0, 500.0));
		lists.add(new ReportBean("����", 100.0, 200.0, 300.0, 400.0, 500.0));
		lists.add(new ReportBean("����", 100.0, 200.0, 300.0, 400.0, 500.0));
		
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lists);
		
//		HttpServletRequest request = this.getRequest();
//		String basePath = request.getContextPath();
//		System.out.println(request.getRealPath(basePath));
//		System.out.println("basePath = " + basePath);
		JasperFillManager.fillReportToFile(getRootPath() + "/upload/test1.jasper", map,
				ds);//ʹ�ÿղ������յ�����Դ
		
		
		
		//map = new HashMap<String, Object>();
		
		//map.put("title", "���Ա�ͷ");
		
		JRViewer jrv=new JRViewer(getRootPath() + "/upload/test1.jrprint",false);

	       //C/S�ṹʹ��Jframe��װ��JRViewer��B/S��ʹ��Applet��װ��

	       JFrame jf = new JFrame("����Ԥ��");

	       jf.add(jrv);

	       jf.pack();

	       jf.setVisible(true);
		
		
		return "list";
		
	}
	
	  
	
	public static String getRootPath(){      
	      
	       //��Ϊ����Ϊ"SaveAction"�����" SaveAction.class"һ�����ҵ�      
	      
	       String result = ReportAction.class.getResource("ReportAction.class").toString();      
	      
	       int index = result.indexOf("WEB-INF");    
	      
	       if(index == -1){      
	      
	           index = result.indexOf("bin");      
	      
	       }      
	      
	       result = result.substring(0,index);      
	      
	       if(result.startsWith("jar")){      
	      
	           // ��class�ļ���jar�ļ���ʱ������"jar:file:/F:/ ..."����·��       
	      
	           result = result.substring(10);      
	      
	       }else if(result.startsWith("file")){      
	      
	           // ��class�ļ���class�ļ���ʱ������"file:/F:/ ..."����·��       
	      
	           result = result.substring(6);      
	      
	       }      
	      
	       if(result.endsWith("/"))result = result.substring(0,result.length()-1);//����������"/"      
	      
	       return result;      
	      
	 }
	
	public String chart() throws Exception{
		return "chart";
	}
			
}
