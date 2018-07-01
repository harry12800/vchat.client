package cn.harry12800.vchat.adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.RCBorder;
import cn.harry12800.vchat.entity.SelectUserData;
import cn.harry12800.vchat.listener.AbstractMouseListener;
import cn.harry12800.vchat.utils.AvatarUtil;
import cn.harry12800.vchat.utils.CharacterParser;
import cn.harry12800.vchat.utils.IconUtil;

/**
 * Created by harry12800 on 17-5-30.
 */
public class SelectUserItemsAdapter extends BaseAdapter<SelectUserItemViewHolder> {
	private final ImageIcon checkIcon;
	private final ImageIcon uncheckIcon;
	private List<SelectUserData> userList;
	private List<SelectUserItemViewHolder> viewHolders = new ArrayList<>();
	Map<Integer, String> positionMap = new HashMap<>();
	private AbstractMouseListener mouseListener;

	public SelectUserItemsAdapter(List<SelectUserData> userList) {
		checkIcon = IconUtil.getIcon(this, "/image/check.png");
		uncheckIcon = IconUtil.getIcon(this, "/image/uncheck.png");
		setUserList(userList);
	}

	public void setUserList(List<SelectUserData> userList) {
		this.userList = userList;

		if (userList != null) {
			processData();
		}
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public SelectUserItemViewHolder onCreateViewHolder(int viewType) {
		return new SelectUserItemViewHolder();
	}

	@Override
	public HeaderViewHolder onCreateHeaderViewHolder(int viewType, int position) {
		for (int pos : positionMap.keySet()) {
			if (pos == position) {
				String ch = positionMap.get(pos);

				return new ContactsHeaderViewHolder(ch.toUpperCase());
			}
		}

		return null;
	}

	@Override
	public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int position) {
		ContactsHeaderViewHolder holder = (ContactsHeaderViewHolder) viewHolder;
		holder.setPreferredSize(new Dimension(100, 25));
		holder.setBackground(Colors.LIGHT_GRAY);
		holder.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.LIGHT_GRAY));
		holder.setOpaque(true);

		holder.letterLabel = new JLabel();
		holder.letterLabel.setText(holder.getLetter());
		holder.letterLabel.setForeground(Colors.FONT_GRAY_DARKER);

		holder.setLayout(new BorderLayout());
		holder.add(holder.letterLabel, BorderLayout.WEST);
	}

	@Override
	public void onBindViewHolder(SelectUserItemViewHolder viewHolder, int position) {
		viewHolders.add(position, viewHolder);
		String name = userList.get(position).getName();

		// 头像
		ImageIcon imageIcon = new ImageIcon(
				AvatarUtil.createOrLoadUserAvatar(name).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		viewHolder.avatar.setIcon(imageIcon);

		// 名字
		viewHolder.username.setText(name);

		if (userList.get(position).isSelected()) {
			viewHolder.icon.setIcon(checkIcon);
		} else {
			viewHolder.icon.setIcon(uncheckIcon);
		}

		viewHolder.addMouseListener(mouseListener);
	}

	private void processData() {
		Collections.sort(userList, new Comparator<SelectUserData>() {
			@Override
			public int compare(SelectUserData o1, SelectUserData o2) {
				String tc = CharacterParser.getSelling(o1.getName().toUpperCase());
				String oc = CharacterParser.getSelling(o2.getName().toUpperCase());
				return tc.compareTo(oc);
			}
		});

		int index = 0;
		String lastChara = "";
		for (SelectUserData item : userList) {
			String ch = CharacterParser.getSelling(item.getName()).substring(0, 1).toUpperCase();
			if (!ch.equals(lastChara)) {
				lastChara = ch;
				positionMap.put(index, ch);
			}

			index++;
		}
	}

	public void setMouseListener(AbstractMouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}
}
