package tms.datacenter.upload.action;


import java.awt.Font;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.opensymphony.xwork2.ActionSupport;

import tms.datacenter.sysmanage.action.PrivilegeParentAction;


public class ChartAction extends ActionSupport{
		
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


	public String execute() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		chart = createBarChart();
		fileName = ServletUtilities.saveChartAsPNG(chart, 400, 600, request.getSession());
		return SUCCESS;
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
