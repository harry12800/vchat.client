package cn.harry12800.vchat.panels;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.vchat.adapter.RoomMembersAdapter;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.components.RCButton;
import cn.harry12800.vchat.components.RCListView;
import cn.harry12800.vchat.db.model.ContactsUser;
import cn.harry12800.vchat.db.model.CurrentUser;
import cn.harry12800.vchat.db.model.Room;
import cn.harry12800.vchat.db.service.ContactsUserService;
import cn.harry12800.vchat.db.service.CurrentUserService;
import cn.harry12800.vchat.db.service.RoomService;
import cn.harry12800.vchat.entity.SelectUserData;
import cn.harry12800.vchat.frames.AddOrRemoveMemberDialog;
import cn.harry12800.vchat.frames.MainFrame;

/**
 * Created by harry12800 on 07/06/2017.
 */
@SuppressWarnings("serial")
public class RoomMembersPanel extends ParentAvailablePanel {
	public static final int ROOM_MEMBER_PANEL_WIDTH = 200;
	private static RoomMembersPanel roomMembersPanel;

	private RCListView listView = new RCListView();
	private JPanel operationPanel = new JPanel();
	private JButton leaveButton;

	private List<String> members = new ArrayList<>();
	private String roomId;
	private RoomService roomService = Launcher.roomService;
	private CurrentUserService currentUserService = Launcher.currentUserService;
	private CurrentUser currentUser;
	private Room room;
	private ContactsUserService contactsUserService = Launcher.contactsUserService;
	private RoomMembersAdapter adapter;
	private AddOrRemoveMemberDialog addOrRemoveMemberDialog;

	public RoomMembersPanel(JPanel parent) {
		super(parent);
		roomMembersPanel = this;

		initComponents();
		initView();
		setListeners();

		currentUser = currentUserService.findAll().get(0);
	}

	private void initComponents() {
		setBorder(new LineBorder(Colors.LIGHT_GRAY));
		setBackground(Colors.FONT_WHITE);

		setPreferredSize(new Dimension(ROOM_MEMBER_PANEL_WIDTH, MainFrame.getContext().currentWindowHeight));
		setVisible(false);
		listView.setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
		listView.setContentPanelBackground(Colors.FONT_WHITE);
		listView.getContentPanel().setBackground(Colors.FONT_WHITE);

		operationPanel.setPreferredSize(new Dimension(60, 80));
		operationPanel.setBackground(Colors.FONT_WHITE);

		leaveButton = new RCButton("退出群聊", Colors.WINDOW_BACKGROUND_LIGHT, Colors.WINDOW_BACKGROUND,
				Colors.SCROLL_BAR_TRACK_LIGHT);
		leaveButton.setForeground(Colors.RED);
		leaveButton.setPreferredSize(new Dimension(180, 30));

	}

	private void initView() {
		operationPanel.add(leaveButton);

		setLayout(new GridBagLayout());
		add(listView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1000));
		add(operationPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(1, 1).setInsets(10, 0, 5, 0));

		adapter = new RoomMembersAdapter(members);
		listView.setAdapter(adapter);
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
		room = roomService.findById(roomId);
	}

	public void setVisibleAndUpdateUI(boolean aFlag) {
		if (aFlag) {
			updateUI();
			setVisible(aFlag);
		}

		setVisible(aFlag);
	}

	public void updateUI() {
		if (roomId != null) {
			try {
				room = roomService.findById(roomId);
			} catch (Exception e) {
				e.printStackTrace();
				room = roomService.findById(roomId);
			}

			getRoomMembers();

			// 单独聊天，不显示退出按钮
			if (room.getType().equals("d")) {
				leaveButton.setVisible(false);
			} else {
				leaveButton.setVisible(true);
			}

			listView.notifyDataSetChanged(false);

			setLeaveButtonVisibility(true);

			if (isRoomCreator()) {
				leaveButton.setText("解散群聊");
			} else {
				leaveButton.setText("退出群聊");
			}

		}
	}

	private void getRoomMembers() {
		members.clear();

		// 单独聊天，成员只显示两人
		if (room.getType().equals("d")) {
			members.add(currentUser.getUsername());
			members.add(room.getName());
		} else {
			String roomMembers = room.getMember();
			String[] userArr = new String[] {};
			if (roomMembers != null) {
				userArr = roomMembers.split(",");
			}

			if (isRoomCreator()) {
				members.remove("添加成员");
				members.add("添加成员");

				if (userArr.length > 1) {
					members.remove("删除成员");
					members.add("删除成员");
				}
			}

			if (room.getCreatorName() != null) {
				members.add(room.getCreatorName());
			}

			for (int i = 0; i < userArr.length; i++) {
				if (!members.contains(userArr[i])) {
					members.add(userArr[i]);
				}
			}
		}
	}

	/**
	 * 判断当前用户是否是房间创建者
	 *
	 * @return
	 */
	private boolean isRoomCreator() {
		return room.getCreatorName() != null && room.getCreatorName().equals(currentUser.getUsername());
	}

	public static RoomMembersPanel getContext() {
		return roomMembersPanel;
	}

	public void setLeaveButtonVisibility(boolean visible) {
		operationPanel.setVisible(visible);
	}

	private void setListeners() {
		adapter.setAddMemberButtonMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectAndAddRoomMember();
				super.mouseClicked(e);
			}
		});

		adapter.setRemoveMemberButtonMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectAndRemoveRoomMember();
				super.mouseClicked(e);
			}
		});

		leaveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isRoomCreator()) {
					int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "确认解散群聊？", "确认解散群聊",
							JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						deleteChannelOrGroup(room.getRoomId());
					}
				} else {
					int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "退出群聊，并从聊天列表中删除该群聊", "确认退出群聊",
							JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						leaveChannelOrGroup(room.getRoomId());
					}
				}
				super.mouseClicked(e);
			}
		});
	}

	/**
	 * 选择并添加群成员
	 */
	private void selectAndAddRoomMember() {
		List<ContactsUser> contactsUsers = contactsUserService.findAll();
		List<SelectUserData> selectUsers = new ArrayList<>();

		for (ContactsUser contactsUser : contactsUsers) {
			if (!members.contains(contactsUser.getUsername())) {
				selectUsers.add(new SelectUserData(contactsUser.getUsername(), false));
			}
		}
		addOrRemoveMemberDialog = new AddOrRemoveMemberDialog(MainFrame.getContext(), true, selectUsers);
		addOrRemoveMemberDialog.getOkButton().setText("添加");
		addOrRemoveMemberDialog.getOkButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (((JButton) e.getSource()).isEnabled()) {
					((JButton) e.getSource()).setEnabled(false);
					List<SelectUserData> selectedUsers = addOrRemoveMemberDialog.getSelectedUser();
					String[] userArr = new String[selectedUsers.size()];
					for (int i = 0; i < selectedUsers.size(); i++) {
						userArr[i] = selectedUsers.get(i).getName();
					}

					inviteOrKick(userArr, "invite");
				}
				super.mouseClicked(e);
			}
		});
		addOrRemoveMemberDialog.setVisible(true);
	}

	/**
	 * 选择并移除群成员
	 */
	private void selectAndRemoveRoomMember() {
		List<SelectUserData> userDataList = new ArrayList<>();
		for (String member : members) {
			if (member.equals(room.getCreatorName()) || member.equals("添加成员") || member.equals("删除成员")) {
				continue;
			}
			userDataList.add(new SelectUserData(member, false));
		}

		addOrRemoveMemberDialog = new AddOrRemoveMemberDialog(MainFrame.getContext(), true, userDataList);
		addOrRemoveMemberDialog.getOkButton().setText("移除");
		addOrRemoveMemberDialog.getOkButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (((JButton) e.getSource()).isEnabled()) {
					((JButton) e.getSource()).setEnabled(false);
					List<SelectUserData> selectedUsers = addOrRemoveMemberDialog.getSelectedUser();
					String[] userArr = new String[selectedUsers.size()];
					for (int i = 0; i < selectedUsers.size(); i++) {
						userArr[i] = selectedUsers.get(i).getName();
					}

					inviteOrKick(userArr, "kick");
				}

				super.mouseClicked(e);
			}
		});
		addOrRemoveMemberDialog.setVisible(true);
	}

	private void inviteOrKick(final String[] usernames, String type) {
		// TODO: 添加或删除成员
		JOptionPane.showMessageDialog(null, usernames, type, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 删除Channel或Group
	 *
	 * @param roomId
	 */
	private void deleteChannelOrGroup(String roomId) {
		JOptionPane.showMessageDialog(null, "删除群聊：" + roomId, "删除群聊", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 退出Channel或Group
	 *
	 * @param roomId
	 */
	private void leaveChannelOrGroup(final String roomId) {
		JOptionPane.showMessageDialog(null, "退出群聊：" + roomId, "退出群聊", JOptionPane.INFORMATION_MESSAGE);
	}

}
