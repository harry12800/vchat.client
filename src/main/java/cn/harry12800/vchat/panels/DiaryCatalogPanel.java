package cn.harry12800.vchat.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import cn.harry12800.j2se.dialog.InputMessageDialog;
import cn.harry12800.j2se.dialog.InputMessageDialog.Callback;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.j2se.utils.Clip;
import cn.harry12800.lnk.core.util.JsonUtil;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.StringUtils;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.model.diary.AricleNode;
import cn.harry12800.vchat.model.diary.Aritcle;
import cn.harry12800.vchat.model.diary.CategoryNode;
import cn.harry12800.vchat.model.diary.MyJTreeTransferHandler;
import cn.harry12800.vchat.model.diary.MyScrollBarUI;
import cn.harry12800.vchat.model.diary.MyTreeUI;
import cn.harry12800.vchat.model.diary.TreeNodeRenderer;

/**
 * 日志的目录面板
 * @author Yuexin
 * 
 */
public class DiaryCatalogPanel extends JScrollPane {

	/**
	 * 
	 */
	public static DiaryCatalogPanel context;
	public String dirPath = "C:/Users/ZR0014/Desktop/a/data/diary";
	private static final long serialVersionUID = 1L;
	FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				char charAt = file.getName().charAt(0);
				boolean mark = true;
				if (charAt <= 'z' && charAt >= 'a') {
					mark = false;
				}
				return mark;
			}
			return file.getName().endsWith(".properties");
		}
	};
	JTree catalogTree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode currNode;

	public void selectDiary(File f) {
		if (f.isFile()) {
			Aritcle a;
			try {
				a = JsonUtil.string2Json(f, Aritcle.class);
				DiaryPanel.getContext().areaTextPanel.setText(a.content);
				DiaryPanel.getContext().searchInputText.setText(a.title);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.currPath = f.getAbsolutePath();
		}
	}

	public static DiaryCatalogPanel getContext() {
		return context;
	}
	public DiaryCatalogPanel(JPanel parent) {
		context = this;
		root = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(root);
		setOpaque(false);
		File file = new File(dirPath);
		File[] listFiles = file.listFiles(filter);
		int x = 0;
		for (final File f : listFiles) {
			if (f.isDirectory()) {
				CategoryNode node = new CategoryNode(f);
				root.add(node);
				int i = 1;
				for (File file2 : f.listFiles(filter)) {
					Aritcle a = new Aritcle();
					a.sort = i;
					i++;
					AricleNode newChild = new AricleNode(file2, a);
					node.add(newChild);
				}
			} else {
				x++;
				Aritcle a = new Aritcle();
				a.sort = x;
				AricleNode node = new AricleNode(f, a);
				root.add(node);
			}
		}
		catalogTree = new JTree(model);
		catalogTree.setToggleClickCount(1);// 点击次数
		catalogTree.setOpaque(false);
		catalogTree.setBackground(Color.YELLOW);
		catalogTree.setRootVisible(false);// 隐藏根节点
		catalogTree.setDragEnabled(true);
		catalogTree.putClientProperty("JTree.lineStyle", "None");
		catalogTree.setCellRenderer(new TreeNodeRenderer());
		catalogTree.setUI(new MyTreeUI());
		catalogTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) catalogTree
						.getLastSelectedPathComponent(); // 返回最后选中的结点
				if (node instanceof AricleNode) {
					RightPanel.getContext().showPanel(RightPanel.DIARY);
					TitlePanel.getContext().updateAppTitle("日记");
					selectDiary(((AricleNode) node).getFile());
					setCurrTree(catalogTree);
					setCurrNode(node);
//					setCurrFile(((AricleNode) node).getFile());
				}
				if (node instanceof CategoryNode) {

				}
			}
		});
		catalogTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				TreeNodeRenderer.mouseRow = -1;
				catalogTree.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					final TreePath path = catalogTree.getPathForLocation(e.getX(), e.getY());
					if (null != path) {
						// path中的node节点（path不为空，这里基本不会空）
						final Object object = path.getLastPathComponent();
						if (object instanceof CategoryNode) {
							JPopupMenu pm = new JPopupMenu();
							pm.setBackground(Colors.DARK);
							pm.setBorder(LIGHT_GRAY_BORDER);
							pm.setBorderPainted(false);
							JMenuItem mit3 = new JMenuItem("删除分组");
							mit3.setFont(BASIC_FONT);
							mit3.setBackground(Colors.DARK);
							JMenuItem mit1 = new JMenuItem("更换名称");
							mit1.setOpaque(false);
							mit1.setFont(BASIC_FONT);
							JMenuItem mit2 = new JMenuItem("添加文章");
							JMenuItem mit0 = new JMenuItem("打开目录");
							mit2.setOpaque(false);
							mit0.setOpaque(false);
							mit2.setFont(BASIC_FONT);
							mit0.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									File f = ((CategoryNode) (object)).getFile();
									try {
										Clip.openFile(f.getAbsolutePath());
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							});
							// 删除分组
							mit3.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									((CategoryNode) (object)).removeFromParent();
									FileUtils.deleteDir(((CategoryNode) (object)).getFile());
//									catalogTree.setUI(new MyTreeUI());
//									catalogTree.revalidate();
									model.nodeStructureChanged(root);
								}
							});
							// 更换名称
							mit1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									File dirFile = ((CategoryNode) (object)).getFile();
									new InputMessageDialog(MainFrame.getContext(),
											"目录更名", dirFile.getName(), new Callback() {
												public void callback(String string) {
													String path = dirFile.getParentFile().getAbsolutePath() + File.separator + string;
													dirFile.renameTo(new File(path));
												}
											});
									model.nodeStructureChanged((CategoryNode) (object));
								}
							});
							mit2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									CategoryNode node = (CategoryNode) object;
									File f = createAricle(node.getFile());
									Aritcle aritcle = new Aritcle();
									aritcle.sort = node.getFile().listFiles().length;
									AricleNode newChild = new AricleNode(f, aritcle);

									((CategoryNode) (object)).insert(newChild, 0);
									model.nodeStructureChanged(node);
									catalogTree.expandPath(path);
								}
							});
							pm.add(mit0);
							pm.add(mit1);
							pm.add(mit2);
							pm.add(mit3);

							pm.show(catalogTree, e.getX(), e.getY());
						}
						if (object instanceof AricleNode) {
							JPopupMenu pm = new JPopupMenu();
							pm.setBackground(Colors.DARK);
							pm.setBorder(LIGHT_GRAY_BORDER);
							pm.setBorderPainted(false);
							JMenuItem mit0 = new JMenuItem("更换名称");
							JMenuItem mit1 = new JMenuItem("打开文件");
							JMenuItem mit2 = new JMenuItem("删除文章");
							//							mit0.setOpaque(false);
							mit0.setFont(BASIC_FONT);
							//							mit0.setFont(BASIC_FONT);
							mit2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									TreeNode parent2 = ((AricleNode) (object)).getParent();
									((AricleNode) (object)).removeFromParent();
									delAricle(((AricleNode) (object)).getFile());
									model.nodeStructureChanged(parent2);
								}
							});
							mit1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									File f = ((AricleNode) (object)).getFile();
									try {
										Clip.openFile(f.getAbsolutePath());
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							});
							mit0.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									final File aritcleFile = ((AricleNode) (object)).getFile();
									new InputMessageDialog(MainFrame.getContext(),
											"文章更名", aritcleFile.getName(), new Callback() {
												public void callback(String string) {
													String path = aritcleFile.getParentFile().getAbsolutePath() + File.separator + string + DiaryPanel.diarySuffix;
													aritcleFile.renameTo(new File(path));
												}
											});
									model.nodeStructureChanged(((AricleNode) (object)).getParent());
								}
							});
							pm.add(mit0);
							pm.add(mit1);
							pm.add(mit2);
							pm.show(catalogTree, e.getX(), e.getY());
						}
					}
				}
			}
		});
		catalogTree.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				int x = (int) arg0.getPoint().getX();
				int y = (int) arg0.getPoint().getY();
				//                TreePath path = catalogTree.getPathForLocation(x, y);
				catalogTree.getComponentAt(x, y).repaint();
				TreeNodeRenderer.mouseRow = catalogTree.getRowForLocation(x, y);
				catalogTree.repaint();
			}
		});
		catalogTree.setTransferHandler(new MyJTreeTransferHandler());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setViewportView(catalogTree);
		getViewport().setOpaque(false);
		//		jScrollPane.getViewport().setBackground( UI.backColor);
		//		jScrollPane.setBackground(UI.backColor);
		getVerticalScrollBar().setBackground(UI.backColor);
		//		jScrollPane.getVerticalScrollBar().setVisible(false);
		getVerticalScrollBar().setUI(new MyScrollBarUI());
		// 屏蔽横向滚动条
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	// 微软雅黑
	public static Font BASIC_FONT = new Font("微软雅黑", Font.PLAIN, 12);
	public static Font BASIC_FONT2 = new Font("微软雅黑", Font.TYPE1_FONT, 12);
	// 楷体
	public static Font DIALOG_FONT = new Font("楷体", Font.PLAIN, 16);

	public static Border GRAY_BORDER = BorderFactory.createLineBorder(Color.GRAY);
	public static Border ORANGE_BORDER = BorderFactory.createLineBorder(Color.ORANGE);
	public static Border LIGHT_GRAY_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

	public void addNode(File file) {
		CategoryNode node = new CategoryNode(file);
		System.out.println("-:" + root.getChildCount());
		root.insert(node, root.getChildCount());
		if (root.getChildCount() == 1) {
			model = new DefaultTreeModel(root);
			catalogTree.setModel(model);
		}
		//		catalogTree.putClientProperty("JTree.lineStyle", "None");
		catalogTree.setUI(new MyTreeUI());
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public File createAricle(File file) {
		String pathString = file.getAbsolutePath() + File.separator + StringUtils.getUUID() + ".properties";
		FileUtils.createFile(pathString);
		return new File(pathString);
	}

	public void delAricle(File file) {
		if (catalogTree != null) {
			file.delete();
		}
	}
	

	public void setCurrNode(DefaultMutableTreeNode node) {
		this.currNode = node;
	}

	public String currPath;

	public void setCurrTree(JTree catalogTree) {
		this.catalogTree = catalogTree;
	}

 
	public JTree getCatalogTree() {
		return catalogTree;
	}


	public DefaultTreeModel getModel() {
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

	public void setCatalogTree(JTree catalogTree) {
		this.catalogTree = catalogTree;
	}


	public String getCurrPath() {
		return currPath;
	}


	public void setCurrPath(String currPath) {
		this.currPath = currPath;
	}


	public DefaultMutableTreeNode getCurrNode() {
		return currNode;
	}

}
