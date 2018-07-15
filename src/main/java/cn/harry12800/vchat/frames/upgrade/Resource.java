package cn.harry12800.vchat.frames.upgrade;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement
public class Resource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String realname;
	@XmlAttribute
	private String url;
	@XmlAttribute
	private String path;
	@XmlAttribute
	private String md5;
	
	/**
	 * 获取md5
	 *	@return the md5
	 */
	public String getMd5() {
		return md5;
	}
	/**
	 * 设置md5
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	/**
	 * 获取name
	 *	@return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取realname
	 *	@return the realname
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 设置realname
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * 获取url
	 *	@return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置url
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取path
	 *	@return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置path
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Resource [name=" + name + ", realname=" + realname + ", url="
				+ url + ", path=" + path + ", md5=" + md5 + "]";
	}
	
}
