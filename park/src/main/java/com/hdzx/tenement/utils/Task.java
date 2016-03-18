package com.hdzx.tenement.utils;

import java.util.Map;

import android.content.Context;

import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

public class Task {
	public static final int TASK_SUCCESS = 200;
	public static final int TASK_FAILED = -1;
	public static final int TASK_TIMEOUT = -200;
	public static final int TASK_UNKNOWN_HOST = -300;
	public static final int TASK_NETWORK_ERROR = -100;

	public static final int TASK_DOWNLOAD = 1;
	/**
	 * 任务参数
	 */
	private Map<String, Object> taskParams;

	public Map<String, Object> getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(Map<String, Object> taskParams) {
		this.taskParams = taskParams;
	}

	/**
	 * 获取消息类别列表
	 * 
	 * @param context
	 * @param contentReportor
	 * @param getLastMessage
	 *            true/false，是否查询最新消息
	 * @param requestCode
	 */
	public static void getAllMessageOutline(Context context,
			IContentReportor contentReportor, boolean getLastMessage,
			String requestCode) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("getLastMessage", String.valueOf(getLastMessage));

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_ALL_MESSAGE_OUTLINE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask task = new HttpAsyncTask(context, contentReportor);
		task.execute(httpRequestEntity);

	}

	/**
	 * 
	 * @param context
	 * @param contentReportor
	 * @param category
	 *            消息类别
	 * @param pageNum
	 *            页面开始页面，从1开始
	 * @param pageSize
	 *            页面记录个数
	 * @param requestCode
	 */
	public static void getUserMessage(Context context,
			IContentReportor contentReportor, String category, String pageNum,
			String pageSize, String requestCode) {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("category", category);
		reqContent.appendData("pageNum", pageNum);
		reqContent.appendData("pageSize", pageSize);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_USER_MESSAGE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask task = new HttpAsyncTask(context, contentReportor);
		task.execute(httpRequestEntity);
	}

	/**
	 * 设置已读消息
	 * 
	 * @param context
	 * @param contentReportor
	 * @param messageId
	 *            消息ID
	 * @param requestCode
	 */
	public static void setReadMessage(Context context,
			IContentReportor contentReportor, String messageId,
			String requestCode) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("messageId", messageId);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.SET_READ_MESSAGE.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask task = new HttpAsyncTask(context, contentReportor);
		task.execute(httpRequestEntity);
	}

	/**
	 * 设置已读消息列表
	 * 
	 * @param context
	 * @param contentReportor
	 * @param messageIds
	 * @param requestCode
	 */
	public static void setReadMessages(Context context,
			IContentReportor contentReportor, String messageIds,
			String requestCode) {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("messageIds", messageIds);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.SET_READ_MESSAGES.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask task = new HttpAsyncTask(context, contentReportor);
		task.execute(httpRequestEntity);
	}

	/**
	 * 根据category设置已读消息
	 * 
	 * @param context
	 * @param contentReportor
	 * @param category
	 *            消息类别代码
	 * @param requestCode
	 */
	public static void setReadMessageByCategory(Context context,
			IContentReportor contentReportor, String category,
			String requestCode) {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		reqContent.setRequestTicket(true);
		reqContent.appendData("category", category);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.SET_READ_MESSAGE_BY_CATEGORY
						.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask task = new HttpAsyncTask(context, contentReportor);
		task.execute(httpRequestEntity);
	}

	public static void getBrandsList(Context context,
			IContentReportor contentReportor, String requestCode) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.type.name(), "1");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_BUSSINESSLIST.getValue());
		httpRequestEntity.setRequestCode(requestCode);
		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(context,
				contentReportor);
		httpAsyncTask.execute(httpRequestEntity);
	}

	public static void getAdvertiseInfo(Context context,
			IContentReportor contentReportor, String advertiseId,
			String resolution, String requestCode) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);

		reqContent.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.appAdvertiseId.name(),
				advertiseId);
		reqContent.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.resolution.name(), resolution);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_ADVERTISEMENT.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask advertisementTask = new HttpAsyncTask(context,
				contentReportor);
		advertisementTask.execute(httpRequestEntity);
	}

	/**
	 * 查询服务页面项
	 * 
	 * @param context
	 * @param contentReportor
	 * @param pid
	 *            父节点id（顶层节点id是0）(必填)
	 * @param resolution
	 *            查询深度(不传就是查询所有)
	 * @param requestCode
	 */
	public static void getPageItemsTree(Context context,
			IContentReportor contentReportor, String pid, String deep,
			String requestCode) {

		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);

		reqContent.appendData("pid", pid);
//		reqContent.appendData("deep", deep);

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_SERVICE_INFO.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		httpRequestEntity.setRequestCode(requestCode);
		HttpAsyncTask advertisementTask = new HttpAsyncTask(context,
				contentReportor);
		advertisementTask.execute(httpRequestEntity);
	}

	public static void getArticleList(Context context,
			IContentReportor contentReportor, String requestCode,String articleClassId) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);

		reqContent.appendData(
				Contants.PROTOCOL_REQ_BODY_DATA.articleClassId.name(), articleClassId);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_ARTICLELIST.getValue());
		httpRequestEntity.setRequestCode(requestCode);
		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		HttpAsyncTask httpAsyncTask = new HttpAsyncTask(context,
				contentReportor);
		httpAsyncTask.execute(httpRequestEntity);
	}
	
	
	
}
