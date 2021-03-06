package cn.harry12800.vchat.db.model;

/**
 * Created by harry12800 on 09/03/2017.
 */

public class ContactsUser extends BasicModel {
	
	private String userId;
	private String friendId;

	private String username;

	private String name;

	public ContactsUser() {
	}

	public ContactsUser(String userId, String username, String name, String friendId) {
		this.userId = userId;
		this.username = username;
		this.name = name;
		this.friendId = friendId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ContactsUser{" + "userId='" + userId + '\'' + ", username='" + username + '\'' + ", name='" + name
				+ '\'' + '}';
	}
}
