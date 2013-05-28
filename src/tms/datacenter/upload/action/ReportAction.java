package tms.datacenter.upload.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JRViewer;

import tms.datacenter.dbmanage.Record;

import com.opensymphony.xwork2.ActionSupport;

public class ReportAction extends ActionSupport {
	private List<Record> lists;
	private Map<String, Object> map;
	
	public String print() throws Exception {
		
		JasperFillManager.fillReportToFile("xsmx.jasper", null,
				new JREmptyDataSource());//使用空参数，空的数据源
		
		//map = new HashMap<String, Object>();
		
		//map.put("title", "测试表头");
		
		JRViewer jrv=new JRViewer("static.jrprint",false);

	       //C/S结构使用Jframe来装载JRViewer。B/S则使用Applet来装载

	       JFrame jf = new JFrame("报表预览");

	       jf.add(jrv);

	       jf.pack();

	       jf.setVisible(true);
		
		
		return null;
		
	}
}
