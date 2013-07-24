<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript">
if (window.Event)
  document.captureEvents(Event.MOUSEUP);
  
function nocontextmenu()
{
 event.cancelBubble = true
 event.returnValue = false;
  
 return false;
}
  
function norightclick(e)
{
 if (window.Event)
 {
  if (e.which == 2 || e.which == 3)
   return false;
 }
 else
  if (event.button == 2 || event.button == 3)
  {
   event.cancelBubble = true
   event.returnValue = false;
   return false;
  }
  
}
document.oncontextmenu = nocontextmenu;  // for IE5+
document.onmousedown = norightclick;  // for all others

</script>
</head>
<body>
<div style="margin-top:-75px;margin-left:auto;margin-right:auto">
<object classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" id="Pdf1" width="730" height="606">
<param name="toolbar" value="false">
<param name="_Version" value="65539">
<param name="_ExtentX" value="19315">
<param name="_ExtentY" value="16034">
<param name="_StockProps" value="0">
<param name="SRC" value="4.pdf">

</object>
</div>

</body>
</html>