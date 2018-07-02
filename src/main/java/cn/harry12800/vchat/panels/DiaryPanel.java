package cn.harry12800.vchat.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.harry12800.j2se.action.CtrlSAction;
import cn.harry12800.j2se.action.EnterAction;
import cn.harry12800.j2se.component.ClickAction;
import cn.harry12800.j2se.component.InputText;
import cn.harry12800.j2se.component.MButton;
import cn.harry12800.j2se.component.PlainButton;
import cn.harry12800.j2se.dialog.InputMessageDialog;
import cn.harry12800.j2se.dialog.InputMessageDialog.Callback;
import cn.harry12800.j2se.style.UI;
import cn.harry12800.lnk.core.util.JsonUtil;
import cn.harry12800.tools.FileUtils;
import cn.harry12800.tools.Lists;
import cn.harry12800.tools.MachineUtils;
import cn.harry12800.tools.StringUtils;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.app.config.Contants;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.model.diary.AreaTextPanel;
import cn.harry12800.vchat.model.diary.AricleNode;
import cn.harry12800.vchat.model.diary.BaiduReader;
import cn.harry12800.vchat.model.diary.Diary;
import cn.harry12800.vchat.model.diary.RelativeDateFormat;
import cn.harry12800.vchat.model.diary.SearchInputText;
import cn.harry12800.vchat.model.diary.SelectButtonDialog;
import cn.harry12800.vchat.utils.HttpUtil;

public class DiaryPanel extends JPanel implements DropTargetListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String diarySuffix = ".properties";
	public MButton synchronousDiary = new MButton("同步", 80, 25);
	public MButton see = new MButton("预览", 80, 25);
	public MButton reader = new MButton("朗读", 80, 25);
	public MButton stopReader = new MButton("停止朗读", 80, 25);
	public MButton newA = new MButton("新建", 80, 25);
	public MButton cmd = new MButton("cmd执行", 80, 25);
	public AreaTextPanel areaTextPanel = new AreaTextPanel();
	public InputText searchInputText = new SearchInputText(30);
	cn.harry12800.vchat.model.diary.SearchResultPanel searchResultPanel = new cn.harry12800.vchat.model.diary.SearchResultPanel();
	private static DiaryPanel context;

	public static DiaryPanel getContext() {
		return context;
	}

	@SuppressWarnings("deprecation")
	public DiaryPanel(JPanel parent) {
		context = this;
		setSize(700, 600);
		searchInputText.setNextFocusableComponent(searchResultPanel);
		setPreferredSize(new Dimension(700, 600));
		searchInputText.setBorder(new EmptyBorder(new Insets(5, 10, 5, 25)));
		setOpaque(false);
		setLayout(new BorderLayout());
		add(searchInputText, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		CardLayout cardLayout = new CardLayout();
		centerPanel.setLayout(cardLayout);
		centerPanel.add(areaTextPanel, "area");
		centerPanel.add(searchResultPanel, "search");
		initBtnListener();
		JPanel jPanel = new JPanel();
		add(jPanel, BorderLayout.SOUTH);
		jPanel.setLayout(new FlowLayout());
		jPanel.add(synchronousDiary);
		jPanel.add(see);
		jPanel.add(newA);
		jPanel.add(cmd);
		jPanel.add(reader);
		jPanel.add(stopReader);
		jPanel.setBackground(UI.foreColor);
		searchInputText.setCtrlSAction(new CtrlSAction() {
			public void ctrlS() {
				saveAricle();
			}
		});
		searchInputText.setEnterAction(new EnterAction() {
			public void enter() {
				// areaTextPanel.setVisible(false);
				List<File> searchResult = getSearchResult();
				searchResultPanel.removeAll();
				int x = 0;
				for (final File file : searchResult) {
					Diary a = null;
					try {
						a = JsonUtil.string2Json(file, Diary.class);
					} catch (Exception e2) {
						continue;
					}
					PlainButton plainButton = new PlainButton(a.getTitle(), 490, 25,
							PlainButton.createBgColorBuilder(UI.foreColor));
					plainButton.addMouseListener(new ClickAction(plainButton) {
						public void leftClick(MouseEvent e) {
							// searchResultPanel.setVisible(false);
							DiaryCatalogPanel.getContext().currPath = file.getAbsolutePath();
							Diary a;
							try {
								a = JsonUtil.string2Json(new File(DiaryCatalogPanel.getContext().currPath),
										Diary.class);
								areaTextPanel.setText(a.getContent());
								searchInputText.setText(a.getTitle());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							cardLayout.show(centerPanel, "area");
							// areaTextPanel.setVisible(true);
							// areaTextPanel.requestFocus();
						}
					});
					plainButton.setBounds(1, x * 26 + 1, 490, 25);
					x++;
					searchResultPanel.add(plainButton);
					;
				}
				// searchResultPanel.repaint();
				// searchResultPanel.setVisible(true);
				cardLayout.show(centerPanel, "search");
				searchResultPanel.requestFocus();
				searchResultPanel.addFocusListener(new FocusListener() {
					public void focusLost(FocusEvent e) {
						// searchResultPanel.setVisible(false);
					}

					public void focusGained(FocusEvent e) {

					}
				});
				searchResultPanel.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyReleased(KeyEvent e) {

						if (e.getKeyCode() == KeyEvent.VK_UP) {
						}
					}

					@Override
					public void keyPressed(KeyEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
		});

		areaTextPanel.setCtrlSAction(new CtrlSAction() {
			public void ctrlS() {
				saveAricle();
			}
		});
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	/**
	 * 保存文章。。 第一：保存本文章、
	 */
	protected void saveAricle() {
		String text = areaTextPanel.area.getText().trim();
		String title = searchInputText.getText().trim();
		if ("null".equals(title) || "".equals(title)) {
			title = text.split("\\n")[0];
		}
		searchInputText.setText(title);
		Diary a = new Diary();
		if (DiaryCatalogPanel.getContext().getCatalogTree() != null) {
			a = ((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()).aritcle;
		}
		a.setTitle(title);
		a.setContent(text);
		a.setUpdateTime(new Date());
		String currPath = DiaryCatalogPanel.getContext().currPath;
		JsonUtil.saveObj(a, currPath);
		if (DiaryCatalogPanel.getContext().getCatalogTree() != null) {
			((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()).plainButton.text.setText(title);
			((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()).plainButton.updatedateL
					.setText(RelativeDateFormat.format(new Date()));
			DiaryCatalogPanel.getContext().getModel()
					.reload(((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()));
		}
		MainFrame.getContext().alert("本地保存成功！");
		saveToServer(a, currPath);
	}

	private void saveToServer(Diary a, String path) {

		Map<String, String> headers = new HashMap<>(0);
		try {
			String post = HttpUtil.postJson(Contants.diarySaveUrl, headers, JsonUtil.object2String(a));
			System.out.println(post);
			Result diary = JsonUtil.string2Json(post, Result.class);
			a.setId(diary.content.getId());
			JsonUtil.saveObj(a, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class Result {
		public Integer code;
		public String msg;
		public Diary content;

		@Override
		public String toString() {
			return "Result [code=" + code + ", msg=" + msg + ", d=" + content + "]";
		}

	}

	protected List<File> getSearchResult() {
		String searchKey = searchInputText.getText().trim();
		List<File> list = FileUtils.traverseDir(DiaryCatalogPanel.getContext().dirPath, new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isFile();
			}
		});

		List<File> files = Lists.newArrayList();
		for (File file : list) {
			String code = FileUtils.getSrcByFilePath(file, "UTF-8");
			if (code.contains(searchKey)) {
				files.add(file);
			}
		}
		return files;
	}

	static class ResultAll {
		public Integer code;
		public String msg;
		public List<Diary> content;

		@Override
		public String toString() {
			return "ResultAll [code=" + code + ", msg=" + msg + ", content=" + content + "]";
		}

	}

	static class ResultCatalogAll {
		public Integer code;
		public String msg;
		public List<DiaryCatalog> content;

	}

	public static void main(String[] args) {
		String url = "http://127.0.0.1/v1/diary/getAll";
		try {
			String string = HttpUtil.get(url);
			System.out.println(string);
			ResultAll string2Json = JsonUtil.string2Json(string, ResultAll.class);
			System.err.println(string2Json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initBtnListener() {
		synchronousDiary.addMouseListener(new ClickAction(synchronousDiary) {
			public void leftClick(MouseEvent e) {
				if (!Launcher.currentUser.getUsername().equals("周国柱"))
					return;
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							synchronousDiary.setEnabled(false);
							String dirPath = DiaryCatalogPanel.getContext().dirPath;
							String catalogString2Json = HttpUtil.get(Contants.diaryCatalogUrl);
							ResultCatalogAll catalogObj = JsonUtil.string2Json(catalogString2Json,
									ResultCatalogAll.class);
							List<DiaryCatalog> catalogList = catalogObj.content;
							Map<String, DiaryCatalog> catalogMaps = new HashMap<>();
							for (DiaryCatalog diaryCatalog : catalogList) {
								catalogMaps.put(diaryCatalog.getId(), diaryCatalog);
							}
							String diaryString2Json = HttpUtil.get(Contants.diaryUrl);
							ResultAll string2Json = JsonUtil.string2Json(diaryString2Json, ResultAll.class);
							List<Diary> diaryList = string2Json.content;
							for (Diary diary : diaryList) {
								String catalogName = catalogMaps.get(diary.getCatalogId()).getName();
								String tempPath = dirPath + File.separator + catalogName;
								if (!new File(tempPath).exists()) {
									new File(tempPath).mkdir();
								}
								JsonUtil.saveObj(diary, tempPath + File.separator + diary.getId() + ".properties");
							}
							MainFrame.getContext().alert("同步完成！");
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							synchronousDiary.setEnabled(true);
						}
					}
				}).start();
			}
		});

		see.addMouseListener(new ClickAction(see) {
			public void leftClick(MouseEvent e) {
				File selectFile = new File(DiaryCatalogPanel.getContext().currPath);
				if (selectFile.exists()) {
					File parentFile = selectFile.getParentFile();
					File[] listFiles = parentFile.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							System.out.println(pathname.getName());
							return pathname.getName().endsWith("properties");
						}
					});
					System.out.println(listFiles.length);
					Diary[] aritcles = new Diary[listFiles.length];
					@SuppressWarnings("unused")
					int currIndex = 0;
					for (int i = 0; i < listFiles.length; i++) {
						try {
							if (selectFile.getName().equals(listFiles[i].getName()))
								currIndex = i;
							aritcles[i] = JsonUtil.string2Json(listFiles[i], Diary.class);
						} catch (Exception e1) {
							MainFrame.getContext().alert(e1.getMessage());
							return;
						}
					}

					// dialog = new NativeDiaryScanDialog(DiaryPanel.this, aritcles);
					// DiaryScanDialog dialog = new DiaryScanDialog(DiaryPanel2.this, aritcles,
					// currIndex);
					// dialog.setVisible(true);
				} else {
					MainFrame.getContext().alert("没有可预览的文章！");
				}
			}
		});

		/**
		 * 新建
		 */
		newA.addMouseListener(new ClickAction(newA) {
			public void leftClick(MouseEvent e) {
				List<String> printName = Lists.newArrayList();
				printName.add("新建文件夹");
				printName.add("新建文章");
				new SelectButtonDialog(MainFrame.getContext(), printName) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void exe(String name) {
						if ("新建文件夹".equals(name)) {
							new InputMessageDialog(MainFrame.getContext(), name, name, new Callback() {
								public void callback(String string) {
									if (!new File(DiaryCatalogPanel.getContext().dirPath + File.separator + string)
											.exists()) {
										FileUtils.createDirectory(
												DiaryCatalogPanel.getContext().dirPath + File.separator + string);
										DiaryCatalogPanel.getContext().addNode(new File(
												DiaryCatalogPanel.getContext().dirPath + File.separator + string));
									} else {
										MainFrame.getContext().alert("已存在该目录！");
									}
								}
							});
						}
					}
				};
			}
		});

		cmd.addMouseListener(new ClickAction(cmd) {
			public void leftClick(MouseEvent e) {
				if (DiaryCatalogPanel.getContext().getCatalogTree() != null) {
					File file = ((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()).getFile();
					final String srcByFilePath = FileUtils.getSrcByFilePath(file, "utf-8");
					try {
						String[] runtimeOutErr = MachineUtils.runtimeOutErr(srcByFilePath);
						areaTextPanel.setText(srcByFilePath + "\r\n" + runtimeOutErr[0] + "\r\n" + runtimeOutErr[1]);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		reader.addMouseListener(new ClickAction(reader) {
			public void leftClick(MouseEvent e) {
				if (DiaryCatalogPanel.getContext().getCatalogTree() != null) {
					stopReader();
					File file = ((AricleNode) DiaryCatalogPanel.getContext().getCurrNode()).getFile();
					final String srcByFilePath = FileUtils.getSrcByFilePath(file, "utf-8");
					thread = new BaiduReader(srcByFilePath);
					thread.start();
				}
			}
		});
		stopReader.addMouseListener(new ClickAction(stopReader) {
			public void leftClick(MouseEvent e) {
				stopReader();
			}
		});
	}

	protected void stopReader() {
		if (thread != null)
			thread.setReadStatus(false);
	}

	private BaiduReader thread;

	// coursor = 
	// Toolkit.getDefaultToolkit().createCustomCursor(new 
	// ImageIcon("image/pencil.gif").getImage(),new Point(10,20), "stick");
	// protected void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// Graphics2D g2d = (Graphics2D) g.create();
	// GradientPaint p2 = new GradientPaint(0, 1, new Color(186, 131, 164, 200), 0,
	// 20, new Color(255, 255, 255, 255));
	// g2d.setPaint(p2);
	// // g2d.drawRoundRect(1, 20, width, size * 25 + 1, 5, 5);
	// // g2d.fillRoundRect(1, 20, width, size * 25 + 1, 5, 5);
	// g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
	// BasicStroke.JOIN_ROUND)); // 设置新的画刷
	// g2d.setFont(new Font("宋体", Font.PLAIN, 12));
	// g2d.drawString("日记", 5, 15);
	// g2d.dispose();
	// }

	@Override
	public void drop(DropTargetDropEvent dtde) {
		try {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) // 如果拖入的文件格式受支持
			{
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				// 接收拖拽来的数据
				// @SuppressWarnings("unchecked")
				// List<File> list = (List<File>) (dtde.getTransferable()
				// .getTransferData(DataFlavor.javaFileListFlavor));
				// MainFrame.save(this, list);
			} else {
				dtde.rejectDrop();// 否则拒绝拖拽来的数据
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {

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
		if (DiaryCatalogPanel.getContext().getCatalogTree() != null) {
			file.delete();
		}
	}
}
