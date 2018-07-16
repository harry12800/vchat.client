package cn.harry12800.vchat.frames.upgrade;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import cn.harry12800.tools.Lists;
import cn.harry12800.tools.Maps;

public class DownLoadFrame extends JFrame {
	private int xx;
	private int yy;
	private boolean isDraging;
	private boolean flag = false;
	private static final long serialVersionUID = 1L;
	private List<Resource> list;
	private List<MyRCProgressBar> progressarList = Lists.newArrayList();
	private Map<Resource, MyRCProgressBar> maps = Maps.newHashMap();
	private MyRCProgressBar progressar;
	private Resource resource;
	private String size = "";
	private DownloadPanel downloadPanel;

	public DownLoadFrame(List<Resource> resources) {
		setName("版本更新(120.78.177.24)");
		setType(Frame.Type.UTILITY);
		this.list = resources;
		initListener();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		for (Resource resource : resources) {
			MyRCProgressBar pro = new MyRCProgressBar();
			//			Progressar pro= new Progressar("下载进度", cn.harry12800.j2se.component.Progressar.Type.percent);
			progressarList.add(pro);
			maps.put(resource, pro);
		}
		setUndecorated(true);
		setSize(400, 300);
		this.downloadPanel = new DownloadPanel(this, resources, maps);
		setContentPane(downloadPanel);
		setLocationRelativeTo(null);
		//		setAlwaysOnTop(true);
		setVisible(true);
	}

	static long x = 0;
	static long y = 0;
	static long lengths = 0;
	Timer timer = null;

	public void inputstreamtofile(InputStream ins, File file)
			throws Exception {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DecimalFormat df = new DecimalFormat(".##");
				if (x >= 1024 * 1024) {
					double d = x * 1.0 / 1024 / 1024;
					String st = df.format(d);
					progressar.setDesc("(" + size + ")下载速度：" + st + "Mb/s    ");
				} else if (x >= 1024) {
					double d = x * 1.0 / 1024;
					String st = df.format(d);
					progressar.setDesc("(" + size + ")下载速度：" + st + "kb/s    ");
				} else {
					progressar.setDesc("(" + size + ")下载速度：" + x + "b/s    ");
				}
				int pros = (int) ((100 * 1.0) * (y * 1.0 / lengths));
				progressar.setVal(pros);
				x = 0;
			}
		}, 1000, 1000);

		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
			x += bytesRead;
			y += bytesRead;
		}
		System.out.println("长度：" + y + "  " + lengths);
		if (y != lengths)
			throw new Exception("下载失败！");
		progressar.setDesc("(" + size + ")下载速度：0kb/s    ");
		int pros = (int) ((100 * 1.0) * (y * 1.0 / lengths));
		progressar.setVal(pros);
		x = 0;
		y = 0;
		timer.cancel();
		os.close();
		ins.close();
	}

	public boolean downloadUrlFile(int i, String url, String path) throws Exception {
		System.out.println(path);
		System.out.println(url);
		try {
			resource = list.get(i);
			progressar = maps.get(resource);
			URL source = new URL(url);
			HttpURLConnection openConnection = (HttpURLConnection) source.openConnection();
			lengths = openConnection.getContentLengthLong();
			if (lengths < 1024) {
				size = lengths + "byte";
			} else if (lengths < 1024 * 1024) {
				size = lengths / 1024 + "KB";
			} else if (lengths < 1024 * 1024 * 1024) {
				DecimalFormat df = new DecimalFormat(".##");
				String st = df.format(1.0 * lengths / 1024 / 1024);
				size = st + "MB";
			} else {
				DecimalFormat df = new DecimalFormat(".##");
				String st = df.format(1.0 * lengths / 1024 / 1024 / 1024);
				size = st + "GB";
			}
			System.out.println("lengths:" + lengths);
			System.out.println(lengths / 1024 + "KB");
			if (HttpURLConnection.HTTP_OK == openConnection.getResponseCode()) {
				InputStream in = openConnection.getInputStream();
				inputstreamtofile(in, new File(path));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		} finally {
			if (timer != null) {
				timer.cancel();
			}
		}
		return true;
	}

	public void updateResources(List<Resource> resources) {
		this.list = resources;
		progressarList.clear();
		maps.clear();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		for (Resource resource : resources) {
			MyRCProgressBar pro = new MyRCProgressBar();
			//			Progressar pro= new Progressar("下载进度", cn.harry12800.j2se.component.Progressar.Type.percent);
			progressarList.add(pro);
			maps.put(resource, pro);
		}
		setSize(400, 300);
		this.downloadPanel = new DownloadPanel(this, resources, maps);
		setContentPane(downloadPanel);
		setVisible(true);
		revalidate();
		setVisible(true);
	}

	private void initListener() {
		//setAlwaysOnTop(true);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				requestFocus();
				isDraging = true;
				xx = e.getX();
				yy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setFlag(true);// 窗体内部暂时设置为不能取色
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - xx, top + e.getY() - yy);
				}
			}
		});

	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setShowInfo(String text) {
		downloadPanel.getLabel().setText(text);
	}
}
