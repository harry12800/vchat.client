package cn.harry12800.vchat.adapter.message;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.harry12800.j2se.style.layout.VerticalFlowLayout;
import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.SizeAutoAdjustTextArea;
import cn.harry12800.vchat.components.message.MessagePopupMenu;
import cn.harry12800.vchat.components.message.RCLeftImageMessageBubble;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.utils.FontUtil;

/**
 * Created by harry12800 on 17-6-2.
 */
@SuppressWarnings("serial")
public class MessageLeftTextViewHolder extends BaseMessageViewHolder {
	public JLabel sender = new JLabel();
	// public JLabel avatar = new JLabel();
	// public JLabel size = new JLabel();
	// public RCLeftTextMessageBubble text = new RCLeftTextMessageBubble();

	public SizeAutoAdjustTextArea text;
	public RCLeftImageMessageBubble messageBubble = new RCLeftImageMessageBubble();

	private JPanel timePanel = new JPanel();
	private JPanel messageAvatarPanel = new JPanel();
	@SuppressWarnings("unused")
	private MessagePopupMenu popupMenu = new MessagePopupMenu();

	public MessageLeftTextViewHolder() {
		initComponents();
		initView();
	}

	private void initComponents() {
		int maxWidth = (int) (MainFrame.getContext().currentWindowWidth * 0.5);
		text = new SizeAutoAdjustTextArea(maxWidth);
		text.setParseUrl(true);

		time.setForeground(Colors.FONT_GRAY);
		time.setFont(FontUtil.getDefaultFont(12));

		sender.setFont(FontUtil.getDefaultFont(12));
		sender.setForeground(Colors.FONT_GRAY);

		messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);
		timePanel.setBackground(Colors.WINDOW_BACKGROUND);
	}

	private void initView() {
		setLayout(new BorderLayout());
		timePanel.add(time);

		messageBubble.add(text);

		JPanel senderMessagePanel = new JPanel();
		senderMessagePanel.setBackground(Colors.WINDOW_BACKGROUND);
		senderMessagePanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
		senderMessagePanel.add(sender);
		senderMessagePanel.add(messageBubble);

		messageAvatarPanel.setLayout(new GridBagLayout());
		messageAvatarPanel.add(avatar, new GBC(1, 0).setWeight(1, 1).setAnchor(GBC.NORTH).setInsets(4, 5, 0, 0));
		messageAvatarPanel.add(senderMessagePanel,
				new GBC(2, 0).setWeight(1000, 1).setAnchor(GBC.WEST).setInsets(0, 5, 5, 0));

		add(timePanel, BorderLayout.NORTH);
		add(messageAvatarPanel, BorderLayout.CENTER);
	}
}
