package com.hdzx.tenement.mine.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MyPostsAdapter;
import com.hdzx.tenement.mine.vo.BbsForums;
import com.hdzx.tenement.mine.vo.BbsPosts;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.Contants;

public class ForumPostsActivity extends Activity implements IContentReportor {

	//
	private List<BbsPosts> bbsPostsVoList;
	private MyPostsAdapter myPostsAdapter;
	private com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView lst_my_posts;
	private String RQ_THREAD_POSTS = "rq_thread_posts";
	private String RQ_FORUM_POSTS = "rq_forum_posts";
	private String RQ_FORUM_MORE_POSTS = "rq_forum_more_posts";
	private HttpAsyncTask task = null;
	private final int PAGE_SIZE = 5;
	private String forumId = "";
	private Button btn_forum;
	private String updatemoretime = "";
	private String updaterefreshtime = "";
	private List<BbsForums> forumlist;
	private int pos = 0;
	private int REQUEST_TYPE = 100;
	private int REQUEST_WRITE = 101;
	private ImageView img_write;
	//
	private AnimationDrawable animationDrawable;
	private MediaPlayer mediaPlayer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.forum_posts_activity);
		lst_my_posts = (PullToRefreshListView) findViewById(R.id.lst_my_posts);
		btn_forum = (Button) findViewById(R.id.btn_forum);
		img_write = (ImageView) findViewById(R.id.img_write);
		initAudioAndAnim();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		initIndicator();
		bbsPostsVoList = new ArrayList<BbsPosts>();
		myPostsAdapter = new MyPostsAdapter(this, bbsPostsVoList, mediaPlayer,
				animationDrawable);
		lst_my_posts.setAdapter(myPostsAdapter);

		lst_my_posts
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// Log.v("gl",
						// position+"getThreadId==="+((BbsPosts)parent.getAdapter().getItem(position-1)).getContent());
						Intent intent = new Intent(parent.getContext(),
								MinePostsDtlActivity.class);
						intent.putExtra("threadId",
								bbsPostsVoList.get(position - 1).getThreadId());
						intent.putExtra("postId",
								bbsPostsVoList.get(position - 1).getPostId());
						parent.getContext().startActivity(intent);
					}
				});

		lst_my_posts
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {

						Log.v("gl", "updatetime==" + updaterefreshtime);

						if (!forumId.equals(""))
							getData();
						else
							lst_my_posts.onRefreshComplete();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.v("gl", "updatetime==" + updatemoretime);

						if (!forumId.equals(""))
							getMoreData();
						else
							lst_my_posts.onRefreshComplete();
					}
				});

		btn_forum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ForumPostsActivity.this,
						ForumTypeActivity.class);
				if (forumlist != null && forumlist.size() > 0) {
					intent.putExtra("list", (Serializable) forumlist);
					intent.putExtra("pos", pos);
					startActivityForResult(intent, REQUEST_TYPE);
				} else {
					Toast.makeText(ForumPostsActivity.this, "暂无更多版块",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		img_write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ForumPostsActivity.this,
						WritePostActivity.class);
				intent.putExtra("forumId", forumId);
				startActivityForResult(intent, REQUEST_WRITE);
			}
		});

		getForumData();
		// updaterefreshtime = DateUtil.getFormatDate(new Date(),
		// "yyyyMMddHHmmss");
	}

	/**
	 * 初始化数据
	 */
	private void getData() {

		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.forumId.name(),
				forumId);

		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.updatetime.name(),
				updaterefreshtime);
		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_THREAD_REFRESH_LST.getValue());
		httpRequestEntity.setRequestCode(RQ_THREAD_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	/**
	 * 初始化数据
	 */
	private void getMoreData() {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.forumId.name(),
				forumId);
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.updatetime.name(),
				updatemoretime);
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.offset.name(),
				PAGE_SIZE);
		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_THREAD_MORE_LST.getValue());
		httpRequestEntity.setRequestCode(RQ_FORUM_MORE_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	/**
	 * 初始化数据
	 */
	private void getForumData() {

		initListValue();

		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent
				.appendData(Contants.PROTOCOL_RESP_BODY.deptId.name(),
						UserSession.getInstance().getUserBasic()
								.getLifecircleId()
								+ "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_FORUM_LST.getValue());
		httpRequestEntity.setRequestCode(RQ_FORUM_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
		task.execute(httpRequestEntity);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_imageView:
			finish();
			break;
		case R.id.tv_msg:
			Intent intent = new Intent(this, MineMessageLstActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		// TODO Auto-generated method stub
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if (rtnCode == null || "".equals(rtnCode)) {
			Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT)
					.show();
		} else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
			Toast.makeText(getApplicationContext(),
					responseContent.getErrorMsg(), Toast.LENGTH_SHORT).show();
		} else {
			if (RQ_THREAD_POSTS.equals(responseContent.getResponseCode())) {
				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {
					int size = 0;
					String update_new_time = "";

					try {
						JSONObject json = new JSONObject(jsonStr);
						jsonStr = json.get("data").toString();
						size = json.getInt("size");
						update_new_time = json.get("updatetimeNew").toString();
						String update_old_time = json.get("updatetimeOld")
								.toString();
						if (updatemoretime.equals("")) {
							updatemoretime = update_old_time;
						}

						Log.v("gl", "update_new_time==" + update_new_time);
						Log.v("gl", "update_old_time==" + update_old_time);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (size != 0) {

						Log.v("gl", "size" + size);

						updaterefreshtime = update_new_time;
						Gson gson = new Gson();
						List<BbsPosts> list = gson.fromJson(jsonStr,
								new TypeToken<List<BbsPosts>>() {
								}.getType());

						bbsPostsVoList.addAll(0, list);
					}
				}
				showInfo();
			} else if (RQ_FORUM_POSTS.equals(responseContent.getResponseCode())) {

				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {
					Gson gson = new Gson();
					forumlist = gson.fromJson(jsonStr,
							new TypeToken<List<BbsForums>>() {
							}.getType());

					if (forumlist != null && forumlist.size() > 0) {

						forumId = forumlist.get(0).getForumId();
						btn_forum.setText(forumlist.get(0).getForumName());

						getData();
					} else {

						btn_forum.setText("暂无");

					}
				}
			} else if (RQ_FORUM_MORE_POSTS.equals(responseContent
					.getResponseCode())) {
				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						jsonStr = json.get("data").toString();
						String update_time = json.get("updatetime").toString();
						if (!update_time.equals(""))
							updatemoretime = update_time;// 更新时间戳
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Gson gson = new Gson();
					List<BbsPosts> list = gson.fromJson(jsonStr,
							new TypeToken<List<BbsPosts>>() {
							}.getType());

					bbsPostsVoList.addAll(list);
				}
				showInfo();
			}
		}
	}

	private void showInfo() {
		// TODO Auto-generated method stub
		myPostsAdapter.notifyDataSetChanged();

		lst_my_posts.onRefreshComplete();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_TYPE) {
			if (data != null) {
				int postion = data.getIntExtra("pos", 0);
				Log.v("gl", "pos get==" + postion);
				if (pos != postion) {
					pos = postion;
					btn_forum.setText(forumlist.get(pos).getForumName());
					forumId = forumlist.get(pos).getForumId();

					initListValue();
					getData();
				}
			}
		} else if (requestCode == REQUEST_WRITE) {

			if (data != null) {
				getData();
			}
		}
	}

	private void initListValue() {
		// TODO Auto-generated method stub
		updatemoretime = "";
		updaterefreshtime = "";
		bbsPostsVoList.clear();

	}

	private void initIndicator() {
		ILoadingLayout loadingLayout = lst_my_posts.getLoadingLayoutProxy(true,
				false);
		loadingLayout.setPullLabel("下拉刷新");
		loadingLayout.setRefreshingLabel("加载中……");
		loadingLayout.setReleaseLabel("释放刷新当前画面");

		ILoadingLayout loadingLayout2 = lst_my_posts.getLoadingLayoutProxy(
				false, true);
		loadingLayout2.setPullLabel("上拉加载更多");
		loadingLayout2.setRefreshingLabel("加载中……");
		loadingLayout2.setReleaseLabel("释放加载更多");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		killMediaPlayer();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}
		if (animationDrawable != null) {
			if (animationDrawable.isRunning()) {
				animationDrawable.stop();
			}
		}
	}

	private void killMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

	}

	private void initAudioAndAnim() {
		// TODO Auto-generated method stub
		mediaPlayer = new MediaPlayer();
		animationDrawable = (AnimationDrawable) getResources().getDrawable(
				R.anim.animation_audio);
	}

}