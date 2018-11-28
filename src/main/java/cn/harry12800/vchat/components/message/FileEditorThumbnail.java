package cn.harry12800.vchat.components.message;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.j2se.utils.FontUtil;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.helper.AttachmentIconHelper;

/**
 * 文件在输入框中的缩略图，当文件直接被粘贴到输入框时，该文件将会以缩略图的形式显示在输入框中 Created by harry12800 on
 * 29/06/2017.
 */
@SuppressWarnings("serial")
public class FileEditorThumbnail extends JPanel {
	private JLabel icon;
	private JLabel text;

	private String path;
	private AttachmentIconHelper attachmentIconHelper = new AttachmentIconHelper();

	public FileEditorThumbnail(String path) {
		this.path = path;

		initComponents();
		initView();
	}

	private void initComponents() {
		setPreferredSize(new Dimension(100, 70));
		setMaximumSize(new Dimension(100, 70));
		setBackground(Colors.FONT_WHITE);
		setBorder(new LineBorder(Colors.LIGHT_GRAY));

		icon = new JLabel();
		icon.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon imageIcon = attachmentIconHelper.getImageIcon(path, 35, 35);
		icon.setIcon(imageIcon);

		text = new JLabel();
		text.setFont(FontUtil.getDefaultFont(12));
		text.setText(path.substring(path.lastIndexOf(File.separator) + 1));
		text.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void initView() {
		setLayout(new GridBagLayout());
		add(icon, new GBC(0, 0).setFill(GBC.BOTH).setInsets(5).setWeight(1, 1));
		add(text, new GBC(0, 1).setFill(GBC.BOTH).setInsets(5).setWeight(1, 1));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
