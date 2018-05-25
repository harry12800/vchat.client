package cn.harry12800.vchat.panels;

import cn.harry12800.vchat.adapter.ContactsItemsAdapter;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.RCListView;
import cn.harry12800.vchat.db.model.ContactsUser;
import cn.harry12800.vchat.db.service.ContactsUserService;
import cn.harry12800.vchat.db.service.CurrentUserService;
import cn.harry12800.vchat.entity.ContactsItem;
import cn.harry12800.vchat.utils.AvatarUtil;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry12800 on 17-5-30.
 */
public class ContactsPanel extends ParentAvailablePanel {
	private static ContactsPanel context;

	private RCListView contactsListView;
	private List<ContactsItem> contactsItemList = new ArrayList<>();
	private ContactsUserService contactsUserService = Launcher.contactsUserService;
	private Logger logger = Logger.getLogger(this.getClass());
	private CurrentUserService currentUserService = Launcher.currentUserService;
	private String currentUsername;

	public ContactsPanel(JPanel parent) {
		super(parent);
		context = this;

		initComponents();
		initView();
		initData();
		contactsListView.setAdapter(new ContactsItemsAdapter(contactsItemList));

		// TODO: 从服务器获取通讯录后，调用下面方法更新UI
		notifyDataSetChanged();
	}

	private void initComponents() {
		contactsListView = new RCListView();
	}

	private void initView() {
		setLayout(new GridBagLayout());
		contactsListView.setContentPanelBackground(Colors.DARK);
		add(contactsListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
	}

	private void initData() {
		contactsItemList.clear();

		List<ContactsUser> contactsUsers = contactsUserService.findAll();
		for (ContactsUser contactsUser : contactsUsers) {
			ContactsItem item = new ContactsItem(contactsUser.getUserId(),
					contactsUser.getUsername(), "d");

			contactsItemList.add(item);
		}

	}

	public void notifyDataSetChanged() {
		initData();
		((ContactsItemsAdapter) contactsListView.getAdapter()).processData();
		contactsListView.notifyDataSetChanged(false);

		// 通讯录更新后，获取头像
		getContactsUserAvatar();
	}

	public static ContactsPanel getContext() {
		return context;
	}

	/**
	 * 获取通讯录中用户的头像
	 */
	private void getContactsUserAvatar() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (ContactsItem user : contactsItemList) {
					if (!AvatarUtil.customAvatarExist(user.getName())) {
						final String username = user.getName();
						//logger.debug("获取头像:" + username);
						getUserAvatar(username, true);
					}
				}

				// 自己的头像每次启动都去获取
				currentUsername = currentUserService.findAll().get(0).getUsername();
				getUserAvatar(currentUsername, true);
			}
		}).start();

	}

	/**
	 * 更新指定用户头像
	 * @param username 用户名
	 * @param hotRefresh 是否热更新，hotRefresh = true， 将刷新该用户的头像缓存
	 */
	public void getUserAvatar(String username, boolean hotRefresh) {

		// TODO: 服务器获取头像，这里从资源文件夹中获取
		try {
			URL url = getClass().getResource("/avatar/" + username + ".png");
			BufferedImage image = ImageIO.read(url);

			processAvatarData(image, username);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (hotRefresh) {
			AvatarUtil.refreshUserAvatarCache(username);

			if (username.equals(currentUsername)) {
				MyInfoPanel.getContext().reloadAvatar();
			}
		}
	}

	/**
	 * 处理头像数据
	 * @param image
	 * @param username
	 */
	private void processAvatarData(BufferedImage image, String username) {
		if (image != null) {
			AvatarUtil.saveAvatar(image, username);
		} else {
			AvatarUtil.deleteCustomAvatar(username);
		}
	}

}
