package cn.harry12800.vchat.app;

import java.util.Map;
import java.util.UUID;

import cn.harry12800.lnk.core.scanner.Scaner;
import cn.harry12800.lnk.core.util.JsonUtil;
import cn.harry12800.tools.Maps;
import cn.harry12800.vchat.utils.HttpUtil;

/**
 * Created by harry12800 on 17-5-28.
 */
public class App {

	static class Re1{
		public int pageIndex= 1;
		public int pageSize=10;
	}
	
	static class Re{
		public int queryType = 1;
		public String keyword ="ll";
		public Re1 page = new Re1();
	}
	public static void main1(String[] args) throws Exception {
		Map<String, String> headers = Maps.newHashMap();
		Re re = new Re();
		for(int i =0 ;i<100;i++) {
			re.keyword="ll"+UUID.randomUUID().toString().substring(1, 3);
			String json = JsonUtil.object2String(re);
			long nano = System.currentTimeMillis();
			String postJson = HttpUtil.postJson("http://mobile.fingerchat.cn:8686/xdata-proxy/v1/bd/read/fingerchat/userEmployee/queryByKeyword", headers, json);
			System.out.println(System.currentTimeMillis()-nano);
			System.out.println(postJson);
		}
	}
	public static void main(String[] args) throws Exception {
		Scaner.getInvoker();
		Launcher launcher = new Launcher();
		launcher.launch();
	}
}
