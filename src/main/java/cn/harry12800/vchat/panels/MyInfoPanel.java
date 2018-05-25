package cn.harry12800.vchat.panels;

import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.message.MainOperationPopupMenu;
import cn.harry12800.vchat.db.service.CurrentUserService;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.frames.SystemConfigDialog;
import cn.harry12800.vchat.listener.AbstractMouseListener;
import cn.harry12800.vchat.utils.AvatarUtil;
import cn.harry12800.vchat.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by harry12800 on 17-5-29.
 */
public class MyInfoPanel extends ParentAvailablePanel {
	private static MyInfoPanel context;

	private JLabel avatar;
	private JLabel username;
	private JLabel menuIcon;
	private CurrentUserService currentUserService = Launcher.currentUserService;

	MainOperationPopupMenu mainOperationPopupMenu;
	private String currentUsername;

	public MyInfoPanel(JPanel parent) {
		super(parent);
		context = this;

		initComponents();
		setListeners();
		initView();
	}

	private void initComponents() {

		//GImage.setBorder(new SubtleSquareBorder(true));
		//        CurrentUser test = currentUserService.findById("Ni7bJcX3W8yExKSa3");
		//        System.out.println(test);
		;
		currentUsername = currentUserService.findAll().get(0).getUsername();
		//        currentUsername  = "song";
		avatar = new JLabel();
		avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

		avatar.setPreferredSize(new Dimension(50, 50));
		avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));

		username = new JLabel();
		username.setText(currentUsername);
		username.setFont(FontUtil.getDefaultFont(16));
		username.setForeground(Colors.FONT_WHITE);

		menuIcon = new JLabel();
		menuIcon.setIcon(new ImageIcon(getClass().getResource("/image/options.png")));
		menuIcon.setForeground(Colors.FONT_WHITE);
		menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

		mainOperationPopupMenu = new MainOperationPopupMenu();
	}

	private void setListeners() {
		menuIcon.addMouseListener(new AbstractMouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Component component = e.getComponent();
					mainOperationPopupMenu.show(component, -112, 50);
					super.mouseClicked(e);
				}

			}
		});

		avatar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					SystemConfigDialog dialog = new SystemConfigDialog(MainFrame.getContext(), true);
					dialog.setVisible(true);
					super.mouseClicked(e);
				}
			}
		});
	}

	private void initView() {
		this.setBackground(Colors.DARK);
		this.setLayout(new GridBagLayout());

		add(avatar, new GBC(0, 0).setFill(GBC.NONE).setWeight(2, 1));
		add(username, new GBC(1, 0).setFill(GBC.BOTH).setWeight(7, 1));
		add(menuIcon, new GBC(2, 0).setFill(GBC.BOTH).setWeight(1, 1));
	}

	public void reloadAvatar() {
		currentUsername = currentUserService.findAll().get(0).getUsername();
		//Image image = AvatarUtil.createOrLoadUserAvatar(currentUsername);
		//avatar.setImage(image);
		avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

		avatar.revalidate();
		avatar.repaint();
	}

	public static MyInfoPanel getContext() {
		return context;
	}
}
