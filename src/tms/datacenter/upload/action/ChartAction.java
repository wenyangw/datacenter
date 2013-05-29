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
