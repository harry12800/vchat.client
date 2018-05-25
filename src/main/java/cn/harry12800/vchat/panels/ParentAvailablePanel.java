package cn.harry12800.vchat.panels;

import javax.swing.*;

/**
 * Created by harry12800 on 17-5-30.
 */
public class ParentAvailablePanel extends JPanel {
	private JPanel parent;

	public ParentAvailablePanel(JPanel parent) {
		this.parent = parent;
	}

	public JPanel getParentPanel() {
		return parent;
	}
}
