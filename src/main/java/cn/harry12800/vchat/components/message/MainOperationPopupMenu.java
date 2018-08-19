package cn.harry12800.vchat.components.message;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.tools.MachineUtils;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.app.config.Contants;
import cn.harry12800.vchat.components.RCMainOperationMenuItemUI;
import cn.harry12800.vchat.frames.CreateGroupDialog;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.frames.SystemConfigDialog;

/**
 * Created by harry12800 on 2017/6/5.
 */
@SuppressWarnings("serial")
public class MainOperationPopupMenu extends JPopupMenu {
	public MainOperationPopupMenu() {
		initMenuItem();
	}

	private void initMenuItem() {
		JMenuItem item1 = new JMenuItem("创建群聊");
		JMenuItem item2 = new JMenuItem("设置");

		item1.setUI(new RCMainOperationMenuItemUI());
		item1.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCreateGroupDialog();
			}
		});
		ImageIcon icon1 = new ImageIcon(getClass().getResource("/image/chat.png"));
		icon1.setImage(icon1.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		item1.setIcon(icon1);
		item1.setIconTextGap(5);

		item2.setUI(new RCMainOperationMenuItemUI());
		item2.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("系统设置");
				SystemConfigDialog dialog = new SystemConfigDialog(MainFrame.getContext(), true);
				dialog.setVisible(true);
			}
		});
		ImageIcon icon2 = new ImageIcon(getClass().getResource("/image/setting.png"));
		icon2.setImage(icon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		item2.setIcon(icon2);
		item2.setIconTextGap(5);

		JMenuItem item3 = new JMenuItem("重启");
		JMenuItem item4 = new JMenuItem("访问主页");
		item3.setUI(new RCMainOperationMenuItemUI());
		item3.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MachineUtils.reStart();
				System.exit(1);
			}
		});
		ImageIcon icon3 = new ImageIcon(getClass().getResource("/image/setting.png"));
		icon3.setImage(icon3.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		item3.setIcon(icon3);
		item3.setIconTextGap(5);
		item4.setUI(new RCMainOperationMenuItemUI());
		item4.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					URI uri = new URI(Contants.getPath("?userId=" + Launcher.currentUser.getUserId()));
					java.awt.Desktop.getDesktop().browse(uri);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		ImageIcon icon4 = new ImageIcon(getClass().getResource("/image/setting.png"));
		icon4.setImage(icon4.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		item4.setIcon(icon4);
		item4.setIconTextGap(5);
		this.add(item1);
		this.add(item2);
		this.add(item3);
		this.add(item4);
		setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
		setBackground(Colors.FONT_WHITE);
	}

	/**
	 * 弹出创建群聊窗口
	 */
	private void showCreateGroupDialog() {
		CreateGroupDialog dialog = new CreateGroupDialog(null, true);
		dialog.setVisible(true);

		/*
		 * ShadowBorderDialog shadowBorderDialog = new
		 * ShadowBorderDialog(MainFrame.getContext(), true, dialog);
		 * shadowBorderDialog.setVisible(true);
		 */
	}
}
