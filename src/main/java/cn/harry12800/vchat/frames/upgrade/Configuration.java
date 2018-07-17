package cn.harry12800.vchat.frames.upgrade;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import cn.harry12800.tools.Lists;
import cn.harry12800.tools.XmlUtils;


@XmlAccessorType(XmlAccessType.FIELD)  
//XML文件中的根标识  
@XmlRootElement(name="configuration")
public class Configuration {

	/**
	 * 版本格式：主版本号.次版本号.修订号，版本号递增规则如下：

主版本号：当你做了不兼容的 API 修改，
次版本号：当你做了向下兼容的功能性新增，
修订号：当你做了向下兼容的问题修正。
先行版本号及版本编译信息可以加到“主版本号.次版本号.修订号”的后面，作为延伸。
	 */
	public static String selfversion = "";
	static String spareServerUrl = "http://192.168.0.119/pharm";
	static {
		InputStream resourceAsStream = Configuration.class.getClassLoader()
				.getResourceAsStream("online.xml");
		try {
			// selfversion = XmlUtils.getNodeValueByName(resourceAsStream,
			// "version");
			selfversion = XmlUtils.getAttrValueByName(resourceAsStream,
					"configuration", "version");
			parse(selfversion);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String prop = Config.getProp(Configuration.class, "serverUrl");
	}
	@XmlAttribute
	public String author="";
	@XmlAttribute
	public String version;
	@XmlElementWrapper(name="resources")
	@XmlElement(name="resource",type = Resource.class)
	public List<Resource> resources;
	@XmlElementWrapper(name="description")
	@XmlElement(name="properties",type = Properties.class)
	public List<Properties> description =Lists.newArrayList();

	/**
	 * 获取resources
	 *	@return the resources
	 */
//	@XmlElementWrapper(name="resources") 
//	@XmlElement(name="resource",type= Resource.class)
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * 设置resources
	 * @param resources the resources to set
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	/**
	 * 获取description
	 *	@return the description
	 */
	public List<Properties> getDescription() {
		return description;
	}

	/**
	 * 设置description
	 * @param description the description to set
	 */
	public void setDescription(List<Properties> description) {
		this.description = description;
	}

	/**
	 * <P>
	 * <li>-2不符合版本号
	 * <li>-1 版本回退
	 * <li>0 版本正常。
	 * <li>1 版本不兼容。
	 * <li>2 当做了向下兼容的功能性新增
	 * <li>3 当做了向下兼容的问题修正
	 * @return
	 */
	public static int verifyVersion(String version) {
		if (isVersionFormat(version)) {
			String[] split = version.split("[.]");
			if (!split[0].equals(compatible)) {
				int compareTo = split[0].compareTo(compatible);
				return compareTo > 0 ? 1 : -1;
			}
			if (!split[1].equals(problem)) {
				int compareTo = split[1].compareTo(problem);
				return compareTo > 0 ? 2 : -1;
			}
			if (!split[2].equals(problemFix)) {
				int compareTo = split[2].compareTo(problemFix);
				return compareTo > 0 ? 3 : -1;
			}
			return 0;
		}
		return -2;
	}

	public static void main(String[] args) {
	 
		String convertToXml = XmlUtils.convertToXml(new Configuration());
		System.out.println(convertToXml);
	}

	private static void parse(String selfversion) {
		if (isVersionFormat(selfversion)) {
			String[] split = selfversion.split("[.]");
			compatible = split[0];
			problem = split[1];
			problemFix = split[2];
		}else{
			System.out.println("项目中版本配置错误。");
		}
	}

	private static boolean isVersionFormat(String version) {
		if (version == null)
			return false;
		else {
			String[] split = version.split("[.]");
			if (split.length < 3) {
				return false;
			} else {
				for (String versionItem : split) {
					if (!isNumeric(versionItem))
						return false;
					if ("".equals(versionItem)) {
						return false;
					}
				}
				return true;
			}
		}
	}

	private static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 兼用
	 */
	private static String compatible;
	/**
	 * 问题新增
	 */
	private static String problem;
	/**
	 * 问题修复
	 */
	private static String problemFix;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Configuration [resources=" + resources + ", description="
				+ description + "]";
	}
	
}
