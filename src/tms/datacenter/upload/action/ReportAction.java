package tms.datacenter.upload.action;

import java.awt.Font;
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
import net.sf.jasperreports.view.JRViewer;

import tms.datacenter.commontools.StringToZn;
import tms.datacenter.dbmanage.Record;
import tms.datacenter.sysmanage.ContentControl;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;


public class ReportAction  extends PrivilegeParentAction {
	
	
	
	
	private List<Record> lists;
	private Map<String, Object> map;
	private JFreeChart chart;
	private String fileName;
	public JFreeChart getChart() {
		return chart;
	}

		
	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
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
//		HttpServletRequest request = this.getRequest();
//		String basePath = request.getContextPath();
//		System.out.println(request.getRealPath(basePath));
//		System.out.println("basePath = " + basePath);
		JasperFillManager.fillReportToFile(getRootPath() + "/upload/test1.jasper", null,
				new JREmptyDataSource());//ʹ�ÿղ������յ�����Դ
		
		//map = new HashMap<String, Object>();
		
		//map.put("title", "���Ա�ͷ");
		
		JRViewer jrv=new JRViewer(getRootPath() + "/upload/test1.jrprint",false);

	       //C/S�ṹʹ��Jframe��װ��JRViewer��B/S��ʹ��Applet��װ��

	       JFrame jf = new JFrame("����Ԥ��");

	       jf.add(jrv);

	       jf.pack();

	       jf.setVisible(true);
		
		
		return null;
		
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
		HttpServletRequest request = ServletActionContext.getRequest();
		chart = createBarChart();
		fileName = ServletUtilities.saveChartAsPNG(chart, 400, 600, request.getSession());
		
		return "chart";
	}
	
	
	public JFreeChart createBarChart() throws Exception{
		StandardChartTheme standardChartTheme = new StandardChartTheme("name");
		standardChartTheme.setLargeFont(new Font("����", Font.BOLD, 12));// ���Ըı����������
		standardChartTheme.setRegularFont(new Font("����", Font.BOLD, 12));// ���Ըı�ͼ��������
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 12));// ���Ըı�ͼ��ı�������
		ChartFactory.setChartTheme(standardChartTheme);// ��������
        
		CategoryDataset dataset = getDataSet2();  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                "ˮ������ͼ", // ͼ�����  
                "ˮ��", // Ŀ¼�����ʾ��ǩ  
                "����", // ��ֵ�����ʾ��ǩ  
                dataset, // ���ݼ�  
                PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ  
                true,   // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)  
                true,   // �Ƿ����ɹ���  
                true    // �Ƿ�����URL����  
                );
        
        return chart;  
    }  
  
    private CategoryDataset getDataSet2() {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        dataset.addValue(100, "����", "ƻ��");  
        dataset.addValue(100, "�Ϻ�", "ƻ��");  
        dataset.addValue(100, "����", "ƻ��");  
        dataset.addValue(200, "����", "����");  
        dataset.addValue(200, "�Ϻ�", "����");  
        dataset.addValue(200, "����", "����");  
        dataset.addValue(300, "����", "����");  
        dataset.addValue(300, "�Ϻ�", "����");  
        dataset.addValue(300, "����", "����");  
        dataset.addValue(400, "����", "�㽶");  
        dataset.addValue(400, "�Ϻ�", "�㽶");  
        dataset.addValue(400, "����", "�㽶");  
        dataset.addValue(500, "����", "��֦");  
        dataset.addValue(500, "�Ϻ�", "��֦");  
        dataset.addValue(500, "����", "��֦");  
        return dataset;  
    } 
	
	
}
