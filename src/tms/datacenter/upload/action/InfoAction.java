package tms.datacenter.upload.action;

import tms.datacenter.sysmanage.action.PrivilegeParentAction;

public class InfoAction extends PrivilegeParentAction{
	public String recent(){
		
		return "recent";
	}
	
	public String remind(){
		return "remind";
	}
}
