package tms.datacenter.commontools;

import java.util.*;

public class Pager {
	private int currentPage;// 当前页数

	private int size = 10;// 每页显示多少记录

	private int totalCounts;// 总记录数

	private int totalpages;// 总页数

	private int firstPage;

	private int lastPage;

	private int prePage;

	private int NextPage;

	private int startposition;// 开始记录位置

	private String url = "";

	private Hashtable params;
	
	private String listMethodName="list";
	
	public String getListMethodName() {
		return listMethodName;
	}

	public void setListMethodName(String listMethodName) {
		this.listMethodName = listMethodName;
	}

	public int getStartposition() {
		return (getCurrentPage() - 1) * size;
	}

	public Pager(int currentPage, int totalCounts, String url, Hashtable params) {

		this.currentPage = currentPage;
		this.totalCounts = totalCounts;
		this.url = url;
		this.params = params;
	}

	public void setStartposition(int startposition) {
		this.startposition = startposition;
	}

	public int getFirstPage() {
		return 1;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return getTotalpages();
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getPrePage() {
		if (getCurrentPage() - 1 <= 0)
			return 1;
		return getCurrentPage() - 1;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		if (getCurrentPage() + 1 > getTotalpages())
			return getTotalpages();
		return getCurrentPage() + 1;
	}

	public void setNextPage(int nextPage) {
		NextPage = nextPage;
	}

	public int getCurrentPage() {
		if(currentPage == 0)
			 return 1;
		else if(currentPage > this.getTotalpages())
			return getTotalpages();
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	public int getTotalpages() {
		if(totalCounts != 0)
		if (totalCounts % size == 0)
			return totalCounts / size;
		return totalCounts / size + 1;

	}

	public void setTotalpages(int totalpages) {
		this.totalpages = totalpages;
	}

	public Hashtable getParams() {
		return params;
	}

	public void setParams(Hashtable params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public String getPage() {
//		StringBuffer page_str = new StringBuffer();
//		if (url != null && url.trim().length() > 0) {
//			//page_str.append("<form name=\"pagerform\" action=\""+getUrl()+getParamsStr()+"\" method=\"post\">");
//			if (getCurrentPage() != 1) {
//				page_str.append("<a href=\"" + getUrl() + getParamsStr()
//						+ "\">首页</a>");
//				page_str.append("&nbsp;<a href=\"" + getUrl() + getParamsStr()
//						+ "&page=" + this.getPrePage() + "\">上页</a>");
//			} else {
//				page_str.append("首页");
//				page_str.append("&nbsp;上页");
//			}
//			page_str
//					.append("&nbsp;" + getCurrentPage() + "/" + getTotalpages());
//			if (getCurrentPage() != this.getLastPage()) {
//				page_str.append("&nbsp;<a href=\"" + getUrl() + getParamsStr()
//						+ "&page=" + this.getNextPage() + "\">下页</a>");
//				page_str.append("&nbsp;<a href=\"" + getUrl() + getParamsStr()
//						+ "&page=" + this.getLastPage() + "\">尾页</a>");
//			} else {
//				page_str.append("&nbsp;下页");
//				page_str.append("&nbsp;尾页");
//
//			}
//			page_str.append("&nbsp;<input type=\"button\" name=\"jump\" onclick=\"this.form.methodName.value='list';this.form.submit();\" value=\"GO\">");
//			page_str.append("&nbsp;<input type=\"text\" style=\"width:30px;\" name=\"page\" value=\""+this.getCurrentPage()+"\">");
//			//page_str.append("&nbsp;页</form>");
//			page_str.append("&nbsp;页");
//		}
//		return page_str.toString();
//	}
	public String getPage() {
		StringBuffer page_str = new StringBuffer();
		if (url != null && url.trim().length() > 0) {
			//page_str.append("<form name=\"pagerform\" action=\""+getUrl()+getParamsStr()+"\" method=\"post\">");
			String script_first = "document.forms[0].methodName.value='"+this.getListMethodName()+"';document.forms[0].page.value=1;document.forms[0].submit();";
			String script_pre = "document.forms[0].methodName.value='"+this.getListMethodName()+"';document.forms[0].page.value="+this.getPrePage()+";document.forms[0].submit();";
			if (getCurrentPage() != 1) {
				page_str.append("<span style=\"cursor:pointer\" onclick=\""+script_first+"\"><font color=\"#005590\">首页</font></span>");
				page_str.append("&nbsp;<span style=\"cursor:pointer\" onclick=\""+script_pre+"\"><font color=\"#005590\">上页</font></span>");
			} else {
				page_str.append("首页");
				page_str.append("&nbsp;上页");
			}
			String script_last = "document.forms[0].methodName.value='"+this.getListMethodName()+"';document.forms[0].page.value="+this.getLastPage()+";document.forms[0].submit();";
			String script_next = "document.forms[0].methodName.value='"+this.getListMethodName()+"';document.forms[0].page.value="+this.getNextPage()+";document.forms[0].submit();";
			page_str
					.append("&nbsp;" + getCurrentPage() + "/" + getTotalpages());
			if (getCurrentPage() != this.getLastPage()) {
				page_str.append("&nbsp;<span style=\"cursor:pointer\" onclick=\""+script_next+"\"><font color=\"#005590\">下页</font></span>");
				page_str.append("&nbsp;<span style=\"cursor:pointer\" onclick=\""+script_last+"\"><font color=\"#005590\">尾页</font></span>");
			} else {
				page_str.append("&nbsp;下页");
				page_str.append("&nbsp;尾页");

			}
			page_str.append("&nbsp;<input type=\"button\" name=\"jump\" onclick=\"this.form.methodName.value='"+this.getListMethodName()+"';this.form.submit();\" value=\"GO\">");
			page_str.append("&nbsp;<input type=\"text\" style=\"width:30px;\" id=\"pageidid\" name=\"page\" value=\""+this.getCurrentPage()+"\">");
			//page_str.append("&nbsp;页</form>");
			page_str.append("&nbsp;页");
		}
		return page_str.toString();
	}
	private String getParamsStr() {
		String condition = "";
		if (params != null && params.size() > 0) {
			Set keys = params.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key != null && key.trim().length() > 0) {
					String value = (String) params.get(key);
					if (value != null && value.trim().length() > 0) {
						condition += "&" + key + "=" + value;
					}
				}
			}
			if (condition.startsWith("&"))
				condition = condition.replaceFirst("&", "?");
		}
		return condition;
	}
	public static void main(String[] args)
	{
		Hashtable h = new Hashtable();
		h.put("a", "1");
		h.put("b", "2");
		h.put("c", "3");
		Pager pager = new Pager(1, 56, "",h);
		System.out.println(pager.getPage());
	}

}
