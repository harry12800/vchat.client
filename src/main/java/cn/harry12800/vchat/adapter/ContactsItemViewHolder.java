package cn.harry12800.vchat.adapter;

import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.RCBorder;
import cn.harry12800.vchat.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by harry12800 on 17-5-30.
 */
@SuppressWarnings("serial")
public class ContactsItemViewHolder extends ViewHolder {
	public JLabel avatar = new JLabel();
	public JLabel roomName = new JLabel();

	public ContactsItemViewHolder() {
		initComponents();
		initView();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(100, 50));
		setBackground(Colors.DARK);
		setBorder(new RCBorder(RCBorder.BOTTOM));
		setOpaque(true);
		setForeground(Colors.FONT_WHITE);

		roomName.setFont(FontUtil.getDefaultFont(13));
		roomName.setForeground(Colors.FONT_WHITE);
	}

	private void initView() {
		setLayout(new GridBagLayout());
		add(avatar, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
		add(roomName, new GBC(1, 0).setWeight(10, 1).setFill(GBC.BOTH));
	}
}
