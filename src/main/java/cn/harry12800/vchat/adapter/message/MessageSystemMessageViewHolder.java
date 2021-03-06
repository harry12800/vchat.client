package cn.harry12800.vchat.adapter.message;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cn.harry12800.j2se.style.layout.VerticalFlowLayout;
import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.j2se.utils.FontUtil;

/**
 * Created by harry12800 on 17-6-2.
 */
@SuppressWarnings("serial")
public class MessageSystemMessageViewHolder extends BaseMessageViewHolder {
	// public JLabel size = new JLabel();
	public JLabel text = new JLabel();
	private JPanel timePanel = new JPanel();
	private JPanel textPanel;

	public MessageSystemMessageViewHolder() {
		avatar = null;
		initComponents();
		initView();
	}

	private void initComponents() {
		setBackground(Colors.WINDOW_BACKGROUND);
		timePanel.setBackground(Colors.WINDOW_BACKGROUND);

		time.setForeground(Colors.FONT_GRAY);
		time.setFont(FontUtil.getDefaultFont(12));
		text.setHorizontalTextPosition(SwingConstants.CENTER);
		text.setFont(FontUtil.getDefaultFont(12));
		textPanel = new JPanel() {
			@Override
			public Insets getInsets() {
				return new Insets(-3, 0, -3, 0);
			}

			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(new Color(195, 195, 195));
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

				g2d.setColor(new Color(248, 248, 248));
				FontMetrics fm = getFontMetrics(getFont());
				int x = (getWidth() - fm.stringWidth(text.getText())) / 2;
				g2d.drawString(text.getText(), x, fm.getHeight() - 1);
				g2d.dispose();
			}
		};
		textPanel.setFont(FontUtil.getDefaultFont(12));
	}

	private void initView() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 0, true, false));
		timePanel.add(time);
		textPanel.add(text);
		contentPanel.add(timePanel);
		contentPanel.add(textPanel);

		add(contentPanel);
	}
}
