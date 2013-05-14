package tms.datacenter.index.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.struts2.ServletActionContext;

import tms.datacenter.commontools.DateUtil;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class UploadPicAction extends ActionSupport {

    // 封装上传文件域的属性
    private File image;
    // 封装上传文件类型的属性
    private String imageContentType;
    // 封装上传文件名的属性
    private String imageFileName;
    // 接受依赖注入的属性
    private String savePath;

    @Override
    public String execute() {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            // 建立文件输出流
        	String datetime = DateUtil.dateToString(new java.util.Date(),"yyyyMMddHHmmssSSS");
        	if(getImageFileName() == null || getImageFileName().trim().length() <= 0){
        		return "uploadsucc";
        	}
        	int dot = getImageFileName().lastIndexOf(".");
        	if(dot<=0)
        		return "uploadsucc";
        	String filetype = getImageFileName().substring(dot);
            System.out.println(getSavePath());
            fos = new FileOutputStream(getSavePath() + "\\" + datetime+filetype);
            // 建立文件上传流
            fis = new FileInputStream(getImage());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            ServletActionContext.getRequest ().setAttribute("filepath", savePath+"/"+datetime+filetype);
        } catch (Exception e) {
            System.out.println("文件上传失败");
            e.printStackTrace();
        } finally {
            close(fos, fis);
        }
        return "uploadres";
    }

    /**
     * 返回上传文件的保存位置
     * 
     * @return
     */
    public String getSavePath() throws Exception{
        return ServletActionContext.getServletContext().getRealPath(savePath); 
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    private void close(FileOutputStream fos, FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("FileInputStream关闭失败");
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                System.out.println("FileOutputStream关闭失败");
                e.printStackTrace();
            }
        }
    }
public static void main(String[] args){
	String s = "123123.jpg";
	int dot = s.lastIndexOf(".");
	String filetype = s.substring(dot);
	System.out.println(filetype);
}
}