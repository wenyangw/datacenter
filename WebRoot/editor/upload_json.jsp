<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.io.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.json.simple.*,tms.datacenter.index.Notice" %>
<%@ page import="org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper" %>
<%

/**
 * KindEditor JSP
 * 
 * 本JSP程序是演示程序，建议不要直接在实际项目中使用。
 * 如果您确定直接使用本程序，使用之前请仔细确认相关安全设置。
 * 
 */

//文件保存目录路径
String savePath = pageContext.getServletContext().getRealPath("/") + "uploadpic/";
System.out.println(savePath);

//文件保存目录URL
String saveUrl  = request.getContextPath() + "/uploadpic/";

//定义允许上传的文件扩展名
HashMap extMap = new HashMap();
extMap.put("image", "gif,jpg,jpeg,png,bmp");
extMap.put("flash", "swf,flv");
extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

//最大文件大小
long maxSize = 1000000;

response.setContentType("text/html; charset=UTF-8");

if(!ServletFileUpload.isMultipartContent(request)){
	out.println(Notice.getError("请选择文件。"));
	return;
}
//检查目录
File uploadDir = new File(savePath);
if(!uploadDir.isDirectory()){
	out.println(Notice.getError("上传目录不存在。"));
	return;
}
//检查目录写权限
if(!uploadDir.canWrite()){
	out.println(Notice.getError("上传目录没有写权限。"));
	return;
}

String dirName = request.getParameter("dir");
if (dirName == null) {
	dirName = "image";
}
if(!extMap.containsKey(dirName)){
	out.println(Notice.getError("目录名不正确。"));
	return;
}
//创建文件夹
savePath += dirName + "/";
saveUrl += dirName + "/";
File saveDirFile = new File(savePath);
if (!saveDirFile.exists()) {
	saveDirFile.mkdirs();
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
String ymd = sdf.format(new Date());
savePath += ymd + "/";
saveUrl += ymd + "/";
File dirFile = new File(savePath);
if (!dirFile.exists()) {
	dirFile.mkdirs();
}

FileItemFactory factory = new DiskFileItemFactory();
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setHeaderEncoding("UTF-8");
List items = upload.parseRequest(request);
Iterator itr = items.iterator();

MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
String[] filesnames = wrapper.getFileNames("imgFile");
File[] files =  wrapper.getFiles("imgFile");
ArrayList al = null;
for(int i = 0; i < files.length; i++) {
	File file = files[i];
	String fileName = filesnames[i];
	long fileSize = file.length();
	
		//检查文件大小
		if(fileSize > maxSize){
			out.println(Notice.getError("上传文件大小超过限制。"));
			return;
		}
		//检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String[] exts = ((String)extMap.get(dirName)).split(",");
		al = new ArrayList();
		for(int j = 0; j < exts.length; j++){
			al.add(exts[j]);
		}
		if(!al.contains(fileExt)){
			out.println(Notice.getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
			return;
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		byte[] buffer = new byte[1024];
		//获取文件输出流
		FileOutputStream fos = new FileOutputStream(savePath +"/" + newFileName);
		//获取内存中当前文件输入流
		InputStream in = new FileInputStream(file);
		try {
		        int num = 0;
		        while ((num = in.read(buffer)) > 0) {
		                fos.write(buffer, 0, num);
		        }
		} catch (Exception e) {
		        e.printStackTrace(System.err);
		} finally {
		        in.close();
		        fos.close();
		}

		JSONObject obj = new JSONObject();
		obj.put("error", new Integer(0));
		obj.put("url", saveUrl + newFileName);
		out.println(obj.toJSONString());
	
}
%>