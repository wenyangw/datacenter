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
				new JREmptyDataSource());//ʹ�ÿղ������յ�����Դ
		
		//map = new HashMap<String, Object>();
		
		//map.put("title", "���Ա�ͷ");
		
		JRViewer jrv=new JRViewer("static.jrprint",false);

	       //C/S�ṹʹ��Jframe��װ��JRViewer��B/S��ʹ��Applet��װ��

	       JFrame jf = new JFrame("����Ԥ��");

	       jf.add(jrv);

	       jf.pack();

	       jf.setVisible(true);
		
		
		return null;
		
	}
}
