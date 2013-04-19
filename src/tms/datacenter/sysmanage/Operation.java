package tms.datacenter.sysmanage;

public class Operation {
	
	public static final String SHOW_POS_NONE = "0";
	public static final String SHOW_POS_LIST = "1";
	public static final String SHOW_POS_ADD = "2";
	public static final String SHOW_POS_UPDATE = "3";
	
	public static final String NEED_CONFIRM_YES = "1";
	public static final String NEED_CONFIRM_NO = "0";
	
	public static final String SELECT_COUNT_NO = "0";
	public static final String SELECT_COUNT_ONLY_ONE = "1";
	public static final String SELECT_COUNT_LEAST_ONE = "2";
	
	private String name;
	private String cnname;
	private String specialparam;
	private String id;
	private int privilegevalue;
	private String showpos;
	private String needconfirm;
	private String selectcount;
	
	public String getNeedconfirm() {
		return needconfirm;
	}
	public void setNeedconfirm(String needconfirm) {
		this.needconfirm = needconfirm;
	}
	public String getSelectcount() {
		return selectcount;
	}
	public void setSelectcount(String selectcount) {
		this.selectcount = selectcount;
	}
	public String getShowpos() {
		return showpos;
	}
	public void setShowpos(String showpos) {
		this.showpos = showpos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCnname() {
		return cnname;
	}
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	public String getSpecialparam() {
		return specialparam;
	}
	public void setSpecialparam(String specialparam) {
		this.specialparam = specialparam;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPrivilegevalue() {
		return privilegevalue;
	}
	public void setPrivilegevalue(int privilegevalue) {
		this.privilegevalue = privilegevalue;
	}
}
