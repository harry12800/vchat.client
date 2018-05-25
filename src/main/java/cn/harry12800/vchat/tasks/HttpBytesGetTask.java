package cn.harry12800.vchat.tasks;

import cn.harry12800.vchat.utils.HttpUtil;

import java.io.IOException;

/**
 * Created by harry12800 on 2017/6/13.
 */
public class HttpBytesGetTask extends HttpTask {
	@Override
	public void execute(String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					byte[] ret = HttpUtil.getBytes(url, headers, requestParams);
					if (listener != null) {
						listener.onSuccess(ret);
					}
				} catch (IOException e) {
					if (listener != null) {
						listener.onFailed();
					}
				}

			}
		}).start();
	}
}
