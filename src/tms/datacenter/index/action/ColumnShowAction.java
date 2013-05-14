package tms.datacenter.index.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tms.datacenter.dbmanage.Record;
import tms.datacenter.index.Column;
import tms.datacenter.index.PicAD;
import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class ColumnShowAction  extends PrivilegeParentAction {
	public String show(){
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		
		PicAD ad = new PicAD();
		ArrayList al = ad.getAllRecords("datacenter", "","order by showorder,updatetime desc");
		request.setAttribute("ads", al);
		return "columnshow";
	}
}
