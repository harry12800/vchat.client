package cn.harry12800.vchat.frames.upgrade;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement
public class Properties {
	@XmlAttribute
	private String value;

	/**
	 * 获取value
	 *	@return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置value
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
