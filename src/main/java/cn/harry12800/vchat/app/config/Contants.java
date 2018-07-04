package cn.harry12800.vchat.app.config;

import cn.harry12800.vchat.app.Launcher;

public class Contants {
	public final static String webHost = Launcher.client.getWebHost();
	public final static String userDiaryCatalogUrl = webHost + "/v1/diaryCatalog/getAllByUserId";
	public final static String userDiaryUrl = webHost + "/v1/diary/getAllByUserId";
	public final static String userDiarySaveUrl = webHost + "/v1/diary/saveOrUpdate";
	public final static String diaryCatalogAddUrl = webHost + "/v1/diaryCatalog/add";
}
