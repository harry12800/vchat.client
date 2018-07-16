package cn.harry12800.vchat.frames.upgrade;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import cn.harry12800.j2se.tab.Tab;
import cn.harry12800.j2se.tab.TabRootPane;
import cn.harry12800.tools.Lists;


public class ContainerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ContainerPanel() {
//		setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(false);
		this.setLayout(new GridBagLayout());
//		UIManager.put("TabbedPane.contentOpaque", false);
//		initTabPage(new BusinessOrderPanel()
//		,new ConfigPanel()
//		//,new NavPanel()
//		);
	}
	 
	protected void initTabPage(JPanel... tab) {
//		JTabbedPane tabbedPane = new JTabbedPane();
//		tabbedPane.addTab(tab[i].getName(), icon, tab[i],
//				tab[i].getName());
//		tabbedPane.setFont(J2seFont.getDefinedFont(0, 14));
//		tabbedPane.setOpaque(false);
//		tabbedPane.setBackground(new Color(0,0,0,0));\
		TabRootPane tabRootPane = new TabRootPane();
		if (tab != null && tab.length > 0) {
			List<Tab> tabList = Lists.newArrayList();
			for (int i = 0; i < tab.length; i++) {
				Tab tabs = new Tab(tab[i].getName() );
				tabs.setContentPane(tab[i]);
				tabList.add(tabs);
			}
			tabRootPane.addTab(tabList.toArray(new Tab[0]));
		}
		add(tabRootPane,BorderLayout.CENTER);
	}

	/**
	 * 方法说明：获得图片 <br>
	 * 输入参数：String path 图片的路径 <br>
	 * 返回类型：ImageIcon 图片对象
	 */
	protected static ImageIcon createImageIcon(String path) {
		if (path != null) {
			return new ImageIcon(path);
		} else {
			System.out.println("Couldn't find file: " + path);
			return null;
		}
	}
}
