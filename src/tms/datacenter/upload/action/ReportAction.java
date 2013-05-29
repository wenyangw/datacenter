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
				new JREmptyDataSource());//使用空参数，空的数据源
		
		//map = new HashMap<String, Object>();
		
		//map.put("title", "测试表头");
		
		JRViewer jrv=new JRViewer(getRootPath() + "/upload/test1.jrprint",false);

	       //C/S结构使用Jframe来装载JRViewer。B/S则使用Applet来装载

	       JFrame jf = new JFrame("报表预览");

	       jf.add(jrv);

	       jf.pack();

	       jf.setVisible(true);
		
		
		return null;
		
	}
	
	  
	
	public static String getRootPath(){      
	      
	       //因为类名为"SaveAction"，因此" SaveAction.class"一定能找到      
	      
	       String result = ReportAction.class.getResource("ReportAction.class").toString();      
	      
	       int index = result.indexOf("WEB-INF");    
	      
	       if(index == -1){      
	      
	           index = result.indexOf("bin");      
	      
	       }      
	      
	       result = result.substring(0,index);      
	      
	       if(result.startsWith("jar")){      
	      
	           // 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径       
	      
	           result = result.substring(10);      
	      
	       }else if(result.startsWith("file")){      
	      
	           // 当class文件在class文件中时，返回"file:/F:/ ..."样的路径       
	      
	           result = result.substring(6);      
	      
	       }      
	      
	       if(result.endsWith("/"))result = result.substring(0,result.length()-1);//不包含最后的"/"      
	      
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
		standardChartTheme.setLargeFont(new Font("楷体", Font.BOLD, 12));// 可以改变轴向的字体
		standardChartTheme.setRegularFont(new Font("宋体", Font.BOLD, 12));// 可以改变图例的字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 12));// 可以改变图标的标题字体
		ChartFactory.setChartTheme(standardChartTheme);// 设置主题
        
		CategoryDataset dataset = getDataSet2();  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                "水果产量图", // 图表标题  
                "水果", // 目录轴的显示标签  
                "产量", // 数值轴的显示标签  
                dataset, // 数据集  
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直  
                true,   // 是否显示图例(对于简单的柱状图必须是false)  
                true,   // 是否生成工具  
                true    // 是否生成URL链接  
                );
        
        return chart;  
    }  
  
    private CategoryDataset getDataSet2() {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        dataset.addValue(100, "北京", "苹果");  
        dataset.addValue(100, "上海", "苹果");  
        dataset.addValue(100, "广州", "苹果");  
        dataset.addValue(200, "北京", "梨子");  
        dataset.addValue(200, "上海", "梨子");  
        dataset.addValue(200, "广州", "梨子");  
        dataset.addValue(300, "北京", "葡萄");  
        dataset.addValue(300, "上海", "葡萄");  
        dataset.addValue(300, "广州", "葡萄");  
        dataset.addValue(400, "北京", "香蕉");  
        dataset.addValue(400, "上海", "香蕉");  
        dataset.addValue(400, "广州", "香蕉");  
        dataset.addValue(500, "北京", "荔枝");  
        dataset.addValue(500, "上海", "荔枝");  
        dataset.addValue(500, "广州", "荔枝");  
        return dataset;  
    } 
	
	
}
