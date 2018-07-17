package cn.harry12800.vchat.adapter.message;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.VerticalFlowLayout;
import cn.harry12800.vchat.components.message.MessageImageLabel;
import cn.harry12800.vchat.components.message.RCLeftImageMessageBubble;
import cn.harry12800.vchat.utils.FontUtil;

/**
 * Created by harry12800 on 17-6-2.
 */
@SuppressWarnings("serial")
public class MessageLeftImageViewHolder extends BaseMessageViewHolder {
	public JLabel sender = new JLabel();
	// public JLabel avatar = new JLabel();
	// public JLabel size = new JLabel();
	public MessageImageLabel image = new MessageImageLabel();
	public RCLeftImageMessageBubble imageBubble = new RCLeftImageMessageBubble();
	private JPanel timePanel = new JPanel();
	private JPanel messageAvatarPanel = new JPanel();

	public MessageLeftImageViewHolder() {
		initComponents();
		initView();
	}

	private void initComponents() {
		timePanel.setBackground(Colors.WINDOW_BACKGROUND);
		messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);

		imageBubble.add(image);

		time.setForeground(Colors.FONT_GRAY);
		time.setFont(FontUtil.getDefaultFont(12));

		sender.setFont(FontUtil.getDefaultFont(12));
		sender.setForeground(Colors.FONT_GRAY);
		// sender.setVisible(false);
	}

	private void initView() {
		setLayout(new BorderLayout());
		timePanel.add(time);

		JPanel senderMessagePanel = new JPanel();
		senderMessagePanel.setBackground(Colors.WINDOW_BACKGROUND);
		senderMessagePanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
		senderMessagePanel.add(sender);
		senderMessagePanel.add(imageBubble);

		messageAvatarPanel.setLayout(new GridBagLayout());
		messageAvatarPanel.add(avatar, new GBC(1, 0).setWeight(1, 1).setAnchor(GBC.NORTH).setInsets(4, 5, 0, 0));
		messageAvatarPanel.add(senderMessagePanel,
				new GBC(2, 0).setWeight(1000, 1).setAnchor(GBC.WEST).setInsets(0, 5, 5, 0));

		add(timePanel, BorderLayout.NORTH);
		add(messageAvatarPanel, BorderLayout.CENTER);
	}
}
