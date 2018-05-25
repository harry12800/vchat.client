package cn.harry12800.vchat.panels;

import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.utils.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by harry12800 on 2017/6/15.
 */
@SuppressWarnings("serial")
public class TipPanel extends ParentAvailablePanel {
	private JLabel imageLabel;

	public TipPanel(JPanel parent) {
		super(parent);
		initComponents();
		initView();
	}

	private void initComponents() {
		imageLabel = new JLabel();
		imageLabel.setIcon(IconUtil.getIcon(this, "/image/bg.png", 140, 140));
	}

	private void initView() {
		setLayout(new GridBagLayout());
		add(imageLabel, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(0, 0, 50, 0));
	}

}
