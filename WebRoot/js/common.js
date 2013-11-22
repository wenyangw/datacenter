function checkAll(checkAllControl,checkBoxName){
	var checkboxes = document.getElementsByName(checkBoxName);
	if(checkboxes){
		var c = null;
		for(i=0;i < checkboxes.length; i++){
			c = checkboxes[i];
			if(c.type=="checkbox" && !c.disabled)
				c.checked = checkAllControl.checked;
		}
	}
}
function isCheckedOne(checkBoxName){
	var checkboxes = document.getElementsByName(checkBoxName);
	var checkcount = 0;
	if(checkboxes){
		var c = null;
		for(i=0;i < checkboxes.length; i++){
			c = checkboxes[i];
			if(c.checked)
				checkcount++;
			if(checkcount > 1)
			{
				return false;
			}
		}
	}
	if(checkcount <= 0){
		return false;
	}
	return true;
}
function isCheckedLeastOne(checkBoxName){
	var checkboxes = document.getElementsByName(checkBoxName);
	if(checkboxes){
		var c = null;
		for(i=0;i < checkboxes.length; i++){
			c = checkboxes[i];
			if(c.checked)
				return true;
		}
	}
	return false;
}
function commonOperateSimple(method){
	var methodcontrol = document.getElementById("methodNameId");
	methodcontrol.value=method;
	methodcontrol.form.submit();
}
function commonOperate(method,specialparam,cnname,needconfirm,selectcount){
	if(selectcount==1){
		if(!isCheckedOne("pkfield"))
		{
			alert("请选择1条记录进行操作！");
			return false;
		}
	}else if(selectcount == 2){
		if(!isCheckedLeastOne("pkfield"))
		{
			alert("请至少选择1条记录进行操作！");
			return false;
		}
	}
	if('1'==needconfirm)
	{
		if(!window.confirm("您确定要执行"+cnname+"操作吗？")){
			return false;
		}
	}
	var methodcontrol = document.getElementById("methodNameId");
	var special = document.getElementById("specialparamId");
	if(methodcontrol)
		methodcontrol.value=method;
	if(special)
		special.value=specialparam;
	methodcontrol.form.submit();
}
function generateSql(){
	var fObj = document.getElementById("fnameid");
	var oObj = document.getElementById("onameid");
	var vObj = document.getElementById("vnameid");
	var fieldname = fObj.options[fObj.options.selectedIndex].value;
	var operate = oObj.options[oObj.options.selectedIndex].value;
	var cvalue = vObj.value;
	if(fieldname==""){
		alert("请选择条件的列！");
		return;
	}
	if(operate==""){
		alert("请选择条件操作符！");
		return;
	}
	if(cvalue==""){
		alert("请填写具体条件值！");
		return;
	}
	var condition = document.getElementById("conditionsqlid");
	var realop = "";
	var operate2 = operate*1;
	switch(operate2){
		case 1:
			realop = "= "+cvalue;
			break;
		case 2:
			realop = "> "+cvalue;
			break;
		case 3:
			realop = "< "+cvalue;
			break;
		case 4:
			realop = ">= "+cvalue;
			break;
		case 5:
			realop = "<= "+cvalue;
			break;
		case 6:
			realop = cvalue;
			break;
		case 7:
			realop = cvalue;
			break;
		case 8:
			realop = cvalue;
			break;
		case 9:
			realop = "in ("+cvalue+")";
			break;
		case 10:
			realop = "<> "+cvalue;
			break;
	}
	if(condition.value != '')
		condition.value += " and "+ fieldname+" "+realop;
	else
		condition.value = fieldname+" "+realop;
}
function reinitIframe(frameid){
	alert("1");
	var iframe = document.getElementById(frameid);
	try{
		alert(iframe.contentWindow.document);
		alert("2");
	var bHeight = iframe.contentWindow.document.body.scrollHeight;
	var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
	alert(dHeight);
	var height = Math.max(bHeight, dHeight);
	alert(height);
	iframe.height =  height;
	}catch (ex){}
}
function changeShow(id){
	
	var total = document.getElementById("indexccount");
	var countc = 0;
	
	if(total)
		countc = total.value;
	
	for(i = 1; i <= countc; i++){
		var cbar = document.getElementById("c"+i);
		if(cbar)
			cbar.className = "indexbar0";
		var ccont = document.getElementById("cc"+i);
		if(ccont)
			ccont.className = "chidden";
	}
	document.getElementById("c100").className="indexbar0";
	document.getElementById("c101").className="indexbar0";
	document.getElementById("cc100").className="chidden";
	document.getElementById("cc101").className="chidden";
	document.getElementById("c"+id).className="indexbar1";
	document.getElementById("cc"+id).className="cshow";
}