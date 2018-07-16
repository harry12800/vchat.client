package cn.harry12800.vchat.frames.upgrade;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import cn.harry12800.j2se.component.Progressar;
import cn.harry12800.j2se.component.panel.TitlePanel;
import cn.harry12800.j2se.component.panel.TitlePanel.Builder;
import cn.harry12800.j2se.component.panel.TitlePanel.TitleHeight;
import cn.harry12800.j2se.style.J2seColor;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.tools.Lists;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.RCProgressBar;
import cn.harry12800.vchat.model.diary.MyScrollBarUI;

public class DownloadPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel label= new JLabel("正在更新……");
	/**
	 * 获取label
	 *	@return the label
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * 设置label
	 * @param label the label to set
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}

	public DownloadPanel(JFrame mainFrame,List<Resource> resources, Map<Resource, MyRCProgressBar> maps) {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(1, 1, 1, 1));
//		setBorder(
//				BorderFactory.createCompoundBorder(
//						ShadowBorder.newBuilder().shadowSize(3).center().build(), 
//				BorderFactory.createLineBorder(Color.GRAY))
//				);
		setOpaque(false);
		Builder builder = TitlePanel.createBuilder(mainFrame);
		builder.hasTitle=true;
		builder.titleHeight= TitleHeight.middle;
		add(new TitlePanel(builder), BorderLayout.NORTH);
		ContainerPanel containerPanel = new ContainerPanel();
		int i=0;
		containerPanel.setLayout(new BoxLayout(containerPanel,BoxLayout.Y_AXIS));
		for (Resource resource : resources) {
			JLabel jLabel = new JLabel(resource.getRealname());
			jLabel.setForeground(Colors.FONT_WHITE);
			containerPanel.add(jLabel);
			MyRCProgressBar myRCProgressBar = maps.get(resource);
//			myRCProgressBar.setPreferredSize(new Dimension(400, 10));
			containerPanel.add(myRCProgressBar );
		}
		JScrollPane scroll = new JScrollPane();
		scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
		scroll.setViewportView(containerPanel);
		scroll.getViewport().setOpaque(false);
		// jScrollPane.getViewport().setBackground( UI.backColor);
		// jScrollPane.setBackground(UI.backColor);
		scroll.getVerticalScrollBar().setBackground(UI.backColor);
		// jScrollPane.getVerticalScrollBar().setVisible(false);
		scroll.getVerticalScrollBar().setUI(new MyScrollBarUI());
		// 屏蔽横向滚动条
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBackground(Colors.DARK);
		add(scroll,BorderLayout.CENTER);
		add(label,BorderLayout.SOUTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(J2seColor.getBorderColor());
		GradientPaint p1;
		p1 = new GradientPaint(0, 0,J2seColor.getBackgroundColor(), 0, h - 200,
				J2seColor.getBackgroundColor());
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, 0, 0);
		g2d.clip(r2d);
		g2d.setPaint(p1);
		g2d.fillRect(0, 0, w-1,h-1);
		/*
		 * int x = 0, y = 0; test.jpg是测试图片，与Demo.java放在同一目录下
		 */

//		g2d.drawImage(ImageUtils.getByName("desk.jpg"), 3, 3, w-6, h-6, null);
		/*
		 * g.drawImage(image, x, y, getSize().width, getSize().height, this);
		 * while (true) { g2d.drawImage(image, 0, 0, w, h, null); if (x >
		 * getSize().width && y > getSize().height) break; //
		 * 这段代码是为了保证在窗口大于图片时，图片仍能覆盖整个窗口 if (x > getSize().width) { x = 0;
		 * image.getw //y += ic.getIconHeight(); } else ;//x +=
		 * ic.getIconWidth(); } g2d.fillRect(0, 0, w, h); g2d.setClip(clip);
		 */
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(J2seColor.getBorderColor());
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, 0, 0);
		g2d.setStroke(new BasicStroke(1));
		/*
		 * g2d.setPaint(p2); g2d.drawRoundRect(0, 0, w - 1, h - 1, 0, 0);
		 */
	}
}
