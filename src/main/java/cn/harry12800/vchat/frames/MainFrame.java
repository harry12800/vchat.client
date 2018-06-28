package cn.harry12800.vchat.frames;

import cn.harry12800.common.module.user.dto.ShowAllUserResponse;
import cn.harry12800.lnk.core.util.ImageUtils;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.db.model.CurrentUser;
import cn.harry12800.vchat.panels.LeftPanel;
import cn.harry12800.vchat.panels.RightPanel;
import cn.harry12800.vchat.panels.RoomsPanel;
import cn.harry12800.vchat.utils.ClipboardUtil;
import cn.harry12800.vchat.utils.FontUtil;
import cn.harry12800.vchat.utils.IconUtil;
import cn.harry12800.vchat.utils.OSUtil;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

/**
 * Created by harry12800 on 17-5-28.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static int DEFAULT_WIDTH = 900;
	public static int DEFAULT_HEIGHT = 650;

	public int currentWindowWidth = DEFAULT_WIDTH;
	public int currentWindowHeight = DEFAULT_HEIGHT;

	private LeftPanel leftPanel;
	private RightPanel rightPanel;

	private static MainFrame context;
	private Image normalTrayIcon; // 正常时的任务栏图标
	private Image emptyTrayIcon; // 闪动时的任务栏图标
	private TrayIcon trayIcon;
	private boolean trayFlashing = false;
	private AudioStream messageSound; //消息到来时候的提示间

	public MainFrame() {
		context = this;
		initComponents();
		initView();
		initResource();
		ImageUtils.addImage(MainFrame.class);
		// 连接WebSocket
		//startWebSocket();
	}

	private void initResource() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initTray();
			}
		}).start();

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
		String tip = "Nickname：harry12800\r\nQQ:804151219\r\n开发者常用功能";
		SystemTray systemTray = SystemTray.getSystemTray();//获取系统托盘
		try {
			if (OSUtil.getOsType() == OSUtil.Mac_OS) {
				normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_dark.png", 20, 20).getImage();
			} else {
				normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher.png", 20, 20).getImage();
			}

			emptyTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_empty.png", 20, 20).getImage();
			trayIcon = new TrayIcon(ImageUtils.getByName("image/system.png"), tip);
//			trayIcon = new TrayIcon(normalTrayIcon, "Author：harry12800\nQQ:804151219\n开发者常用功能");
//			trayIcon.setImageAutoSize(true);
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
			trayIcon.setPopupMenu(menu);
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
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

}
