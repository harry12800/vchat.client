package cn.harry12800.vchat.adapter;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import cn.harry12800.j2se.action.AbstractMouseListener;
import cn.harry12800.j2se.component.rc.adapter.BaseAdapter;
import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.j2se.utils.TimeUtil;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.db.model.Room;
import cn.harry12800.vchat.db.service.RoomService;
import cn.harry12800.vchat.entity.RoomItem;
import cn.harry12800.vchat.panels.ChatPanel;
import cn.harry12800.vchat.utils.AvatarUtil;

/**
 * Created by harry12800 on 17-5-30.
 */
public class RoomItemsAdapter extends BaseAdapter<RoomItemViewHolder> {
	private List<RoomItem> roomItems;
	private List<RoomItemViewHolder> viewHolders = new ArrayList<>();
	private RoomItemViewHolder selectedViewHolder; // 当前选中的viewHolder
	private RoomService roomService = Launcher.roomService;

	public RoomItemsAdapter(List<RoomItem> roomItems) {
		this.roomItems = roomItems;
	}

	@Override
	public int getCount() {
		return roomItems.size();
	}

	@Override
	public RoomItemViewHolder onCreateViewHolder(int viewType) {
		return new RoomItemViewHolder();
	}

	@Override
	public void onBindViewHolder(RoomItemViewHolder viewHolder, int position) {
		if (!viewHolders.contains(viewHolder)) {
			viewHolders.add(viewHolder);
		}
		// viewHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));

		RoomItem item = roomItems.get(position);

		viewHolder.setTag(item.getRoomId());

		viewHolder.roomName.setText(item.getTitle());

		ImageIcon icon = new ImageIcon();
		// 群组头像
		String type = item.getType();
		if (type.equals("c") || type.equals("p")) {
			String[] memberArr = getRoomMembers(item.getRoomId());

			icon.setImage(AvatarUtil.createOrLoadGroupAvatar(item.getTitle(), memberArr, type).getScaledInstance(40, 40,
					Image.SCALE_SMOOTH));
		}
		// 私聊头像
		else if (type.equals("d")) {
			Image image = AvatarUtil.createOrLoadUserAvatar(item.getTitle()).getScaledInstance(40, 40,
					Image.SCALE_SMOOTH);
			icon.setImage(image);
		}
		viewHolder.avatar.setIcon(icon);

		// 消息
		viewHolder.brief.setText(item.getLastMessage());
		if (item.getLastMessage() != null && item.getLastMessage().length() > 15) {
			viewHolder.brief.setText(item.getLastMessage().substring(0, 15) + "...");
		} else {
			viewHolder.brief.setText(item.getLastMessage());
		}

		// 时间
		if (item.getTimestamp() > 0) {
			viewHolder.time.setText(TimeUtil.diff(item.getTimestamp()));
		}

		// 未读消息数
		if (item.getUnreadCount() > 0) {
			viewHolder.unreadCount.setVisible(true);
			viewHolder.unreadCount.setText(item.getUnreadCount() + "");
		} else {
			viewHolder.unreadCount.setVisible(false);
		}

		// 设置是否激活
		if (ChatPanel.CHAT_ROOM_OPEN_ID != null && item.getRoomId().equals(ChatPanel.CHAT_ROOM_OPEN_ID)) {
			setBackground(viewHolder, Colors.ITEM_SELECTED);
			selectedViewHolder = viewHolder;
		}
		// viewHolder.unreadCount.setVisible(true);
		// viewHolder.unreadCount.setText(item.getUnreadCount() + "1");
		MouseListener[] mouseListeners = viewHolder.getMouseListeners();
		System.out.println(mouseListeners);
		System.out.println(mouseListeners.length);
		if (mouseListeners.length == 0)
			viewHolder.addMouseListener(new AbstractMouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (selectedViewHolder != viewHolder) {
							// 进入房间
							enterRoom(item.getRoomId());
							for (RoomItemViewHolder holder : viewHolders) {
								if (holder != viewHolder) {
									setBackground(holder, Colors.DARK);
								}
							}

							// setBackground(viewHolder, Colors.ITEM_SELECTED);
							selectedViewHolder = viewHolder;
						} else {
							ChatPanel.getContext().updateUnreadCount(0);
						}
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					if (selectedViewHolder != viewHolder) {
						setBackground(viewHolder, Colors.ITEM_SELECTED_DARK);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (selectedViewHolder != viewHolder) {
						setBackground(viewHolder, Colors.DARK);
					}
				}
			});
	}

	private String[] getRoomMembers(String roomId) {
		Room room = roomService.findById(roomId);
		String members = room.getMember();
		String[] memberArr = null;

		List<String> roomMembers = new ArrayList<>();
		if (members != null) {
			String[] userArr = members.split(",");
			for (int i = 0; i < userArr.length; i++) {
				if (!roomMembers.contains(userArr[i])) {
					roomMembers.add(userArr[i]);
				}
			}
		}
		String creator = room.getCreatorName();
		if (creator != null) {
			if (!roomMembers.equals(creator)) {
				roomMembers.add(creator);
			}
		}

		memberArr = roomMembers.toArray(new String[] {});
		return memberArr;
	}

	private void setBackground(RoomItemViewHolder holder, Color color) {
		holder.setBackground(color);
		holder.nameBrief.setBackground(color);
		holder.timeUnread.setBackground(color);
	}

	private void enterRoom(String roomId) {
		// 加载房间消息
		ChatPanel.getContext().enterRoom(roomId);

		// TitlePanel.getContext().hideRoomMembersPanel();
		/*
		 * RoomMembersPanel.getContext().setRoomId(roomId); if
		 * (RoomMembersPanel.getContext().isVisible()) {
		 * RoomMembersPanel.getContext().updateUI(); }
		 */
	}

}
