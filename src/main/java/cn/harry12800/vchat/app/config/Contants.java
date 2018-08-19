package cn.harry12800.vchat.app.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import cn.harry12800.j2se.utils.Config;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.MachineUtils;
import cn.harry12800.tools.StringUtils;

public class Contants {
	public final static String userDiaryCatalogUrl = "/v1/diaryCatalog/getAllByUserId";
	public final static String userDiaryUrl = "/v1/diary/getAllByUserId";
	public final static String userDiarySaveUrl = "/v1/diary/saveOrUpdate";
	public final static String diaryCatalogAddUrl = "/v1/diaryCatalog/add";
	public final static String diaryDelUrl = "/v1/diary/del";
	public final static String diaryCatalogDelUrl = "/v1/diaryCatalog/del";

	public static String getPath(String path) {
		return config() + path;
	}

	private static String config() {
		String prop = Config.getProp("webHost");
		if (!StringUtils.isEmpty(prop))
			return prop;
		Properties p = new Properties();
		boolean byClass = MachineUtils.getByClass(Contants.class);
		String homePath = "";
		if (byClass) {
			String clazz = System.getProperty("sun.java.command");
			System.err.println("sun.java.command: " + clazz);
			File file = new File(clazz);
			// MachineUtils.printSystemProperties();
			if (file.exists()) {
				File file2 = new File(file.getAbsolutePath());
				File parentFile = file2.getParentFile();
				homePath = parentFile.getAbsolutePath();
			} else {
				homePath = System.getProperty("user.dir");
			}
		} else {
			homePath = System.getProperty("user.dir");
		}
		try (InputStream stream = Contants.class.getResourceAsStream("/client.properties");) {
			String fileName = homePath + File.separator + "client.properties";
			if (!new File(fileName).exists())
				FileUtils.inputStream2File(fileName, stream);
			p.load(new FileInputStream(new File(fileName)));
			return p.getProperty("webHost");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
