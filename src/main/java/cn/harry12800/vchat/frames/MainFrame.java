package cn.harry12800.vchat.frames;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import cn.harry12800.common.module.user.dto.ShowAllUserResponse;
import cn.harry12800.j2se.dialog.MessageDialog;
import cn.harry12800.j2se.popup.ListItem;
import cn.harry12800.j2se.popup.PopupFrame;
import cn.harry12800.j2se.style.J2seColor;
import cn.harry12800.lnk.core.util.ImageUtils;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.RCProgressBar;
import cn.harry12800.vchat.frames.components.GBC;
import cn.harry12800.vchat.frames.components.GradientProgressBarUI;
import cn.harry12800.vchat.panels.LeftPanel;
import cn.harry12800.vchat.panels.RightPanel;
import cn.harry12800.vchat.panels.RoomsPanel;
import cn.harry12800.vchat.utils.ClipboardUtil;
import cn.harry12800.vchat.utils.FontUtil;
import cn.harry12800.vchat.utils.IconUtil;
import cn.harry12800.vchat.utils.OSUtil;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * Created by harry12800 on 17-5-28.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static int DEFAULT_WIDTH = 900;
	public static int DEFAULT_HEIGHT = 650;
	public static Border LIGHT_GRAY_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

	public int currentWindowWidth = DEFAULT_WIDTH;
	public int currentWindowHeight = DEFAULT_HEIGHT;

	private LeftPanel leftPanel;
	private RightPanel rightPanel;

	private static MainFrame context;
	private Image normalTrayIcon; // 正常时的任务栏图标
	private Image emptyTrayIcon; // 闪动时的任务栏图标
	private TrayIcon trayIcon;
	private boolean trayFlashing = false;
	private AudioStream messageSound; // 消息到来时候的提示间

	public MainFrame() {
		context = this;
		initComponents();
		initView();
		initResource();
		ImageUtils.addImage(MainFrame.class);
		// 连接WebSocket
		// startWebSocket();
	}

	private void initResource() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initTray();
			}
		}).start();
		registerHotKey();
	}

	/**
	 * 播放消息提示间
	 */
	public void playMessageSound() {
		try {
			InputStream inputStream = getClass().getResourceAsStream("/wav/msg.wav");
			messageSound = new AudioStream(inputStream);
			AudioPlayer.player.start(messageSound);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化系统托盘图标
	 */
	private void initTray() {
		String tip = "账号：" + Launcher.currentUser.getUsername() + "\r\nAuthor：harry12800\r\nQQ:804151219\r\n开发者常用功能";
		SystemTray systemTray = SystemTray.getSystemTray();// 获取系统托盘
		try {
			if (OSUtil.getOsType() == OSUtil.Mac_OS) {
				normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_dark.png", 20, 20).getImage();
			} else {
				normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher.png", 20, 20).getImage();
			}

			emptyTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_empty.png", 20, 20).getImage();
			trayIcon = new TrayIcon(ImageUtils.getByName("image/system.png"), tip);
			// trayIcon = new TrayIcon(normalTrayIcon,
			// "Author：harry12800\nQQ:804151219\n开发者常用功能");
			// trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					// 显示主窗口
					setVisible(true);

					// 任务栏图标停止闪动
					if (trayFlashing) {
						trayFlashing = false;
						trayIcon.setImage(normalTrayIcon);
					}

					super.mouseClicked(e);
				}
			});
			PopupMenu menu = new PopupMenu();
			MenuItem mit0 = new MenuItem("打开主界面");
			MenuItem mit1 = new MenuItem("版本更新");
			MenuItem mit2 = new MenuItem("退出");
			menu.add(mit0);
			menu.add(mit1);
			menu.addSeparator();
			menu.add(mit2);

			mit0.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.this.setVisible(true);
				}
			});
			mit2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			trayIcon.setPopupMenu(menu);
			systemTray.add(trayIcon);
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 1) {
						MainFrame.getContext().setVisible(true);
					}
					if (e.getButton() == 3) {
						//						Point point = e.getPoint();
						//						showPopup1(menu,trayIcon,point);
					}
				}

			});
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void registerHotKey() {
		int SCREEN_SHOT_CODE = 10001;
		JIntellitype.getInstance().registerHotKey(SCREEN_SHOT_CODE,
				JIntellitype.MOD_ALT, 'V');
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@Override
			public void onHotKey(int markCode) {
				if (markCode == SCREEN_SHOT_CODE) {
					if (MainFrame.this.isVisible()) {
						MainFrame.this.setVisible(false);
					} else {
						MainFrame.this.setVisible(true);
					}
				}
			}
		});
	}

	public void showPopup1(PopupMenu menu, TrayIcon trayIcon2, Point point) {
		//		menu.setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
		//		menu.setBackground(Colors.FONT_WHITE);
		//		menu.setBorder(LIGHT_GRAY_BORDER);
		MenuItem mit0 = new MenuItem("打开主界面");
		MenuItem mit1 = new MenuItem("版本更新");
		MenuItem mit2 = new MenuItem("退出");
		menu.add(mit0);
		menu.add(mit1);
		menu.addSeparator();
		menu.add(mit2);
		int defaultWidth = 150;
		int h = 79;
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int x = point.x;
		int y = point.y;
		if (x < 0) {
			x = 0;
		}
		if (x > size.width - defaultWidth) {
			x = point.x - defaultWidth;
		}
		if (y > size.height - h) {
			y -= h;
		}
		//		menu.show(origin, x, y);
		setVisible(true);
		requestFocus();
	}

	static PopupFrame popupFrame = null;

	protected synchronized static void showPopup(Point point) {
		if (popupFrame == null) {
			popupFrame = new PopupFrame();
			ListItem font = new ListItem("选择字体", 188, 25);
			PopupFrame fontPop = new PopupFrame();
			font.addPop(fontPop);
			ListItem font1 = new ListItem("微软雅黑", 188, 25);
			ListItem font2 = new ListItem("萝莉体", 188, 25);
			ListItem font3 = new ListItem("华文行楷", 188, 25);
			ListItem font4 = new ListItem("汉仪舒同体简", 188, 25);
			ListItem font5 = new ListItem("汉仪中圆繁", 188, 25);
			ListItem font6 = new ListItem("汉仪小隶书简", 188, 25);
			ListItem showItem = new ListItem("打开主面板", 188, 25);
			fontPop.addItem(font1);
			fontPop.addItem(font2);
			fontPop.addItem(font3);
			fontPop.addItem(font4);
			fontPop.addItem(font5);
			fontPop.addItem(font6);
			font1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.微软雅黑);
					MainFrame.getContext().repaint();
				}
			});
			font2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.萝莉体);
					MainFrame.getContext().repaint();
				}
			});
			font3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.华文行楷);
					MainFrame.getContext().repaint();
				}
			});
			font4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.汉仪舒同体简);
					MainFrame.getContext().repaint();
				}
			});
			font5.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.汉仪中圆繁);
					MainFrame.getContext().repaint();
				}
			});
			font6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					J2seFont.setDefinedFont(J2seFont.汉仪小隶书简);
					MainFrame.getContext().repaint();
				}
			});
			ListItem styleItem = new ListItem("选择风格", 188, 25);
			ListItem ssItem = new ListItem("默认风格", 188, 25);
			ListItem blueItem = new ListItem("蓝色风格", 188, 25);
			ListItem pinkItem = new ListItem("红色风格", 188, 25);
			PopupFrame stylePop = new PopupFrame();
			styleItem.addPop(stylePop);
			stylePop.addItem(ssItem);
			stylePop.addItem(blueItem);
			stylePop.addItem(pinkItem);

			ListItem updateItem = new ListItem("版本更新", 188, 25);
			ListItem exitItem = new ListItem("退出", 188, 25);

			showItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					MainFrame.getContext().setVisible(true);
				}
			});
			exitItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JIntellitype.getInstance().cleanUp();
					System.exit(1);
				}
			});
			blueItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					J2seColor.setBackgroudColor(Color.BLUE);
					MainFrame.getContext().repaint();
				}
			});
			ssItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					J2seColor.setBackgroudColor(new Color(153, 133, 245));
					MainFrame.getContext().repaint();
				}
			});
			pinkItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					J2seColor.setBackgroudColor(Color.PINK);
					MainFrame.getContext().repaint();
				}
			});
			updateItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//					MainFrame.getContext().updateSystem();
				}
			});
			popupFrame.addItem(font);
			popupFrame.addItem(styleItem);
			popupFrame.addSeparator();
			popupFrame.addItem(showItem);
			popupFrame.addItem(updateItem);
			popupFrame.addSeparator();
			popupFrame.addItem(exitItem);
			popupFrame.show(point);
		} else {
			popupFrame.show(point);
		}
	}

	/**
	 * 清除剪切板缓存文件
	 */
	public void clearClipboardCache() {
		ClipboardUtil.clearCache();
	}

	/**
	 * 设置任务栏图标闪动
	 */
	public void setTrayFlashing() {
		trayFlashing = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (trayFlashing) {
					try {
						trayIcon.setImage(emptyTrayIcon);
						Thread.sleep(800);

						trayIcon.setImage(normalTrayIcon);
						Thread.sleep(800);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	public boolean isTrayFlashing() {
		return trayFlashing;
	}

	public static MainFrame getContext() {
		return context;
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		// 任务栏图标
		if (OSUtil.getOsType() != OSUtil.Mac_OS) {
			setIconImage(IconUtil.getIcon(this, "/image/ic_launcher.png").getImage());
		}

		UIManager.put("Label.font", FontUtil.getDefaultFont());
		UIManager.put("Panel.font", FontUtil.getDefaultFont());
		UIManager.put("TextArea.font", FontUtil.getDefaultFont());

		UIManager.put("Panel.background", Colors.WINDOW_BACKGROUND);
		UIManager.put("CheckBox.background", Colors.WINDOW_BACKGROUND);

		leftPanel = new LeftPanel();
		leftPanel.setPreferredSize(new Dimension(260, currentWindowHeight));

		rightPanel = new RightPanel();
	}

	private void initView() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		if (OSUtil.getOsType() != OSUtil.Mac_OS) {
			// 隐藏标题栏
			setUndecorated(true);

			String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			try {
				UIManager.setLookAndFeel(windows);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		setListeners();

		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);
//		south.setVisible(false);
		JPanel panel = new JPanel();
		RCProgressBar progressBar = new RCProgressBar();
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(50);
		progressBar.setUI(new GradientProgressBarUI());
		panel.setLayout(new GridBagLayout());
		panel.setPreferredSize(new Dimension(currentWindowWidth, 5));
		panel.add(progressBar, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
//		progressBarPanel.add(panel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 1));
		add(panel, BorderLayout.SOUTH);
		centerScreen();
	}

	/**
	 * 使窗口在屏幕中央显示
	 */
	private void centerScreen() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		this.setLocation((tk.getScreenSize().width - currentWindowWidth) / 2,
				(tk.getScreenSize().height - currentWindowHeight) / 2);
	}

	private void setListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				currentWindowWidth = (int) e.getComponent().getBounds().getWidth();
				currentWindowHeight = (int) e.getComponent().getBounds().getHeight();
			}
		});
	}

	@Override
	public void dispose() {
		// 移除托盘图标
		SystemTray.getSystemTray().remove(trayIcon);
		super.dispose();
	}

	public void showAllUser(ShowAllUserResponse userResponse) {
		RoomsPanel.getContext().initData(userResponse);
	}

	public void alert(String info) {
		new MessageDialog(MainFrame.getContext(), "温馨提示", info);
	}

}
