package cn.harry12800.vchat.handler;

import org.springframework.stereotype.Component;
import cn.harry12800.common.core.annotion.SocketCommand;
import cn.harry12800.common.core.annotion.SocketModule;
import cn.harry12800.common.core.model.ResultCode;
import cn.harry12800.common.module.ModuleId;
import cn.harry12800.common.module.UserCmd;
import cn.harry12800.common.module.user.dto.ShowAllUserResponse;
import cn.harry12800.common.module.user.dto.UserResponse;
import cn.harry12800.lnk.client.ResultCodeMap;
import cn.harry12800.vchat.frames.LoginFrame;
import cn.harry12800.vchat.frames.MainFrame;

/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@Component
@SocketModule(module = ModuleId.USER)
public class UserLoginHandler {

	private ResultCodeMap resultCodeTip = new ResultCodeMap();

	/**
	 * 创建并登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.REGISTER_AND_LOGIN, desc = "创建并登录帐号")
	public void registerAndLogin(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			UserResponse playerResponse = new UserResponse();
			playerResponse.readFromBytes(data);
		} else {
		}
	}

	/**
	 * 登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.LOGIN, desc = "登录帐号")
	public void login(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			UserResponse userResponse = new UserResponse();
			userResponse.readFromBytes(data);
			LoginFrame.getContext().loginSuccess(userResponse);
		} else {
			LoginFrame.getContext().loginFail(resultCodeTip.getTipContent(resultCode));
		}
	}

	/**
	 * 登录帐号
	 * @param resultCode
	 * @param data {@link null}
	 */
	@SocketCommand(cmd = UserCmd.SHOW_ALL_USER, desc = "登录帐号")
	public void login1(int resultCode, byte[] data) {
		if (resultCode == ResultCode.SUCCESS) {
			ShowAllUserResponse userResponse = new ShowAllUserResponse();
			userResponse.readFromBytes(data);
			MainFrame.getContext().ShowAllUser(userResponse);
		} else {
			LoginFrame.getContext().loginFail(resultCodeTip.getTipContent(resultCode));
		}
	}

}
