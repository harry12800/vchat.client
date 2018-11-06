package cn.harry12800.vchat.app.websocket;

import java.util.Timer;
import java.util.TimerTask;

import cn.harry12800.lnk.core.util.JsonUtil;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.app.config.Contants;
import cn.harry12800.vchat.utils.HttpUtil;

public class PullWebInfo {

	public static void pull() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				String path2 = Contants.getPath(Contants.userDiaryCatalogUrl + "?username=" + Launcher.currentUser.getUsername());
				String post = HttpUtil.postJson(path2, null, JsonUtil.object2String(a));
				System.out.println(post);
				= JsonUtil.string2Json(post, Result.class);
			}
		}, 5000, 1000);
	}
}
