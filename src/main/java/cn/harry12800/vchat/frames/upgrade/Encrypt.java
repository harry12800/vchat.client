package cn.harry12800.vchat.frames.upgrade;

import java.io.File;
import java.util.Random;

import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.MachineUtils;

public class Encrypt {
	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encrypted(String content){
		char[] charArray = content.toCharArray();
		StringBuffer sbBuffer = new StringBuffer();
		Random random = new Random(System.currentTimeMillis());
		for (char c : charArray) {
			int i = (c+0);
			int x = 16;
			while(x--!=0){
				int nextInt = random.nextInt(10);
				int t = i&1;
				i= i>>1;
				t=t^1;
				sbBuffer.append(t);
				if(nextInt==0)sbBuffer.append("\r\n");
			}
		}
		return sbBuffer.toString();
	}
	public static void main(String[] args) {
		String wirteFile = Encrypt.encrypted(FileUtils.getStringByFileName("online.xml"));
		FileUtils.writeContent(MachineUtils.getProjectPath()+File.separator+"config.dba", wirteFile);
		String stringByFilePath = FileUtils.getStringByFilePath(MachineUtils.getProjectPath()+File.separator+"config.dba");
//		String stringByFilePath = FileUtils.getStringByFilePath(MachineUtils.getProjectPath()+File.separator+"text.txt");
		String name = Encrypt.decode(stringByFilePath);
		System.out.println(name);
	}
	/**
	 * 解密
	 * @param content
	 * @return
	 */
	public static String decode(String content) {
		StringBuffer sbBuffer = new StringBuffer();
		char[] charArray = content.toCharArray();
		int x =0;
		int t=0;
		for (int c : charArray) {
			c-='0';
			if(!(c==0||c==1))continue;
			c=c^1;
			t= t|(c<<x);
			x++;
			if(x==16){
				x=0;
				sbBuffer.append((char) t);
				t= 0;
			}
		}
		return sbBuffer.toString();
	}
}
