package cn.harry12800.vchat.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by harry12800 on 17-6-4.
 */
@SuppressWarnings("serial")
public class RCProgressBar extends JProgressBar {
	public RCProgressBar() {
		setForeground(Colors.PROGRESS_BAR_START);

		setBorder(new LineBorder(Colors.PROGRESS_BAR_END));
	}

	@Override
	protected void paintBorder(Graphics g) {
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), 6);
	}
}
