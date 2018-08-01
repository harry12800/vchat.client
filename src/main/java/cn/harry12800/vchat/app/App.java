package cn.harry12800.vchat.app;

import cn.harry12800.lnk.core.scanner.Scaner;

/**
 * Created by harry12800 on 17-5-28.
 */
public class App {
	public static void main(String[] args) throws Exception {
		Scaner.getInvoker();
		Launcher launcher = new Launcher();
		launcher.launch();
	}
}
