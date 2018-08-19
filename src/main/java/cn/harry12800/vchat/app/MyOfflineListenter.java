package cn.harry12800.vchat.app;

import cn.harry12800.lnk.client.OfflineListenter;

public class MyOfflineListenter extends OfflineListenter {

	@Override
	public void exe() {
		System.out.println("请求重连！");
		Launcher.client.init(this);
	}

}
