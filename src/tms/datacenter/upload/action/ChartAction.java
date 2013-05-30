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
	public JFreeChart getChart() {
		return chart;
	}

		
	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

	public String execute() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		chart = createBarChart();
		return SUCCESS;
	}
	
	
	public JFreeChart createBarChart() throws Exception{
		StandardChartTheme standardChartTheme = new StandardChartTheme("name");
		standardChartTheme.setLargeFont(new Font("楷体", Font.BOLD, 12));// 可以改变轴向的字体
		standardChartTheme.setRegularFont(new Font("宋体", Font.BOLD, 12));// 可以改变图例的字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 12));// 可以改变图标的标题字体
		ChartFactory.setChartTheme(standardChartTheme);// 设置主题
        
		CategoryDataset dataset = getDataSet2();  
        JFreeChart chart = ChartFactory.createBarChart3D(  
                "2013年图书销售统计", // 图表标题  
                "", // 目录轴的显示标签  
                "销售额(万元)", // 数值轴的显示标签  
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
        dataset.addValue(100, "社科", "1月");  
        dataset.addValue(100, "音像", "1月");  
        dataset.addValue(100, "文艺", "1月");  
        dataset.addValue(200, "社科", "2月");  
        dataset.addValue(200, "音像", "2月");  
        dataset.addValue(200, "文艺", "2月");  
        dataset.addValue(300, "社科", "3月");  
        dataset.addValue(300, "音像", "3月");  
        dataset.addValue(300, "文艺", "3月");  
        dataset.addValue(400, "社科", "4月");  
        dataset.addValue(400, "音像", "4月");  
        dataset.addValue(400, "文艺", "4月");  
        dataset.addValue(500, "社科", "5月");  
        dataset.addValue(500, "音像", "5月");  
        dataset.addValue(500, "文艺", "5月");  
        return dataset;  
    }  
	
}
