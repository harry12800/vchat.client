package cn.harry12800.vchat.panels;

import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cn.harry12800.j2se.component.rc.RCSearchTextField;
import cn.harry12800.j2se.style.ui.Colors;
import cn.harry12800.j2se.utils.FontUtil;
import cn.harry12800.vchat.adapter.search.SearchResultItemsAdapter;
import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.GBC;
import cn.harry12800.vchat.db.model.ContactsUser;
import cn.harry12800.vchat.db.model.FileAttachment;
import cn.harry12800.vchat.db.model.Message;
import cn.harry12800.vchat.db.model.Room;
import cn.harry12800.vchat.db.service.ContactsUserService;
import cn.harry12800.vchat.db.service.FileAttachmentService;
import cn.harry12800.vchat.db.service.MessageService;
import cn.harry12800.vchat.db.service.RoomService;
import cn.harry12800.vchat.entity.SearchResultItem;

/**
 * Created by harry12800 on 17-5-29.
 */
@SuppressWarnings("serial")
public class SearchPanel extends ParentAvailablePanel {
	private static SearchPanel context;
	private RCSearchTextField searchTextField;
	private RoomService roomService = Launcher.roomService;
	private boolean setSearchMessageOrFileListener = false;

	private ContactsUserService contactsUserService = Launcher.contactsUserService;
	private MessageService messageService = Launcher.messageService;
	private FileAttachmentService fileAttachmentService = Launcher.fileAttachmentService;

	public SearchPanel(JPanel parent) {
		super(parent);
		context = this;
		initComponent();
		initView();
		setListeners();
	}

	private void initComponent() {
		searchTextField = new RCSearchTextField();
		searchTextField.setFont(FontUtil.getDefaultFont(14));
		searchTextField.setForeground(Colors.FONT_WHITE);
	}

	private void initView() {
		setBackground(Colors.DARK);
		this.setLayout(new GridBagLayout());
		this.add(searchTextField, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(0, 15, 0, 15));
	}

	public static SearchPanel getContext() {
		return context;
	}

	private void setListeners() {
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				ListPanel listPanel = ListPanel.getContext();
				SearchResultPanel searchResultPanel = SearchResultPanel.getContext();

				listPanel.showPanel(ListPanel.SEARCH);

				List<SearchResultItem> data = searchUserOrRoom(searchTextField.getText());
				searchResultPanel.setData(data);
				searchResultPanel.setKeyWord(searchTextField.getText());
				searchResultPanel.notifyDataSetChanged(false);
				searchResultPanel.getTipLabel().setVisible(false);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ListPanel listPanel = ListPanel.getContext();
				if (searchTextField.getText() == null || searchTextField.getText().isEmpty()) {
					listPanel.showPanel(listPanel.getPreviousTab());
					return;
				}

				SearchResultPanel searchResultPanel = SearchResultPanel.getContext();

				listPanel.showPanel(ListPanel.SEARCH);

				List<SearchResultItem> data = searchUserOrRoom(searchTextField.getText());
				searchResultPanel.setData(data);
				searchResultPanel.setKeyWord(searchTextField.getText());
				searchResultPanel.notifyDataSetChanged(false);
				searchResultPanel.getTipLabel().setVisible(false);

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// ESC清除已输入内容
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					searchTextField.setText("");
				}

				super.keyTyped(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (searchTextField.getText().length() > 8) {
					e.consume();
				}
			}
		});

	}

	/**
	 * 清空搜索文本
	 */
	public void clearSearchText() {
		searchTextField.setText("");
	}

	/**
	 * 搜索用户或房间
	 * 
	 * @param key
	 * @return
	 */
	private List<SearchResultItem> searchUserOrRoom(String key) {
		List<SearchResultItem> list = new ArrayList<>();

		list.add(new SearchResultItem("searchAndListMessage", "搜索 \"" + key + "\" 相关消息", "searchMessage"));
		list.add(new SearchResultItem("searchFile", "搜索 \"" + key + "\" 相关文件", "searchFile"));

		// 搜索通讯录
		list.addAll(searchContacts(key));

		// 搜索房间
		list.addAll(searchChannelAndGroup(key));

		if (!setSearchMessageOrFileListener) {
			// 查找消息、文件
			SearchResultPanel.getContext()
					.setSearchMessageOrFileListener(new SearchResultItemsAdapter.SearchMessageOrFileListener() {
						@Override
						public void onSearchMessage() {
							searchAndListMessage(searchTextField.getText());
						}

						@Override
						public void onSearchFile() {
							searchAndListFile(searchTextField.getText());
						}
					});

			setSearchMessageOrFileListener = true;
		}

		return list;
	}

	/**
	 * 搜索并展示消息
	 *
	 * @param key
	 */
	private void searchAndListMessage(String key) {
		SearchResultPanel searchResultPanel = SearchResultPanel.getContext();
		List<Message> messages = messageService.search(key);
		List<SearchResultItem> searchResultItems = new ArrayList<>();

		if (messages == null || messages.size() < 1) {
			searchResultPanel.getTipLabel().setVisible(true);
		} else {
			searchResultPanel.getTipLabel().setVisible(false);

			SearchResultItem item;
			for (Message msg : messages) {
				String content = msg.getMessageContent();
				int startPos = content.toLowerCase().indexOf(key.toLowerCase());
				int endPos = startPos + 10;
				// endPos = endPos > content.length() ? content.length() : endPos;
				if (endPos > content.length()) {
					endPos = content.length();
					content = content.substring(startPos, endPos);
				} else {
					content = content.substring(startPos, endPos) + "...";
				}

				item = new SearchResultItem(msg.getId(), content, "message");
				item.setTag(msg.getRoomId());

				searchResultItems.add(item);
			}
		}

		searchResultPanel.setData(searchResultItems);
		searchResultPanel.setKeyWord(key);
		searchResultPanel.notifyDataSetChanged(false);
	}

	/**
	 * 搜索并展示文件
	 *
	 * @param key
	 */
	private void searchAndListFile(String key) {
		SearchResultPanel searchResultPanel = SearchResultPanel.getContext();
		List<FileAttachment> fileAttachments = fileAttachmentService.search(key);
		List<SearchResultItem> searchResultItems = new ArrayList<>();

		if (fileAttachments == null || fileAttachments.size() < 1) {
			searchResultPanel.getTipLabel().setVisible(true);
		} else {
			searchResultPanel.getTipLabel().setVisible(false);
			SearchResultItem item;
			for (FileAttachment file : fileAttachments) {
				String content = file.getTitle();
				// content = content.length() > 10 ? content.substring(0, 10) : content;

				item = new SearchResultItem(file.getId(), content, "file");
				// item.setTag(msg.getRoomId());

				searchResultItems.add(item);
			}
		}

		searchResultPanel.setKeyWord(key);
		searchResultPanel.setData(searchResultItems);
		searchResultPanel.notifyDataSetChanged(false);
	}

	/**
	 * 搜索房间，但不包含直接聊天
	 *
	 * @param key
	 * @return
	 */
	private List<SearchResultItem> searchChannelAndGroup(String key) {
		List<Room> rooms = roomService.searchByName(key);
		List<SearchResultItem> retList = new ArrayList<>();

		SearchResultItem item;
		for (Room room : rooms) {
			item = new SearchResultItem(room.getRoomId(), room.getName(), room.getType());
			retList.add(item);
		}
		return retList;
	}

	/**
	 * 搜索通讯录
	 *
	 * @param key
	 * @return
	 */
	private List<SearchResultItem> searchContacts(String key) {
		List<ContactsUser> contactsUsers = contactsUserService.searchByUsernameOrName(key, key);
		List<SearchResultItem> retList = new ArrayList<>();
		SearchResultItem item;
		for (ContactsUser user : contactsUsers) {
			item = new SearchResultItem(user.getUserId(), user.getUsername(), "d");
			retList.add(item);
		}
		return retList;
	}

}
