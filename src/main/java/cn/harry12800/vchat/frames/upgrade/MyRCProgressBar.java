package cn.harry12800.vchat.frames.upgrade;

import cn.harry12800.vchat.components.RCProgressBar;
import cn.harry12800.vchat.frames.components.GradientProgressBarUI;

public class MyRCProgressBar extends RCProgressBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyRCProgressBar() {
		setMaximum(100);
		setMinimum(0);
		setName("更新中");
		setUI(new GradientProgressBarUI());
		// TODO Auto-generated constructor stub
	}

	public void setDesc(String string) {
		setName(string);
	}

	public void setVal(int pros) {
		setValue(pros);
	}

}
