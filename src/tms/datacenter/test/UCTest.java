package tms.datacenter.test;

import java.util.List;

import tms.datacenter.upload.ColumnMsg;
import tms.datacenter.upload.UploadConfig;
import tms.datacenter.upload.UploadMsg;

public class UCTest {
	public static void main(String[] args) {
//		UCTest uct = new UCTest();
		UploadConfig uc = UploadConfig.getInstance();
		List list = uc.getUpload("test1").getColumnList();
		for(int i = 0; i < list.size(); i++){
			String str = ((ColumnMsg)list.get(i)).getFieldname();
			System.out.println(str);
		}
	}
}
