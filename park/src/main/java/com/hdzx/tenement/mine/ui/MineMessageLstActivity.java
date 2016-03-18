package com.hdzx.tenement.mine.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MyMessagesAdapter;
import com.hdzx.tenement.mine.vo.Notice;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;

public class MineMessageLstActivity extends Activity implements IContentReportor {

	//
	private List<Notice> bbsPostsVoList;
	private MyMessagesAdapter myPostsAdapter;
	private com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView lst_my_posts;
	private String RQ_MY_POSTS = "rq_my_posts";
	private String RQ_READ_POSTS = "rq_read_posts";
	private HttpAsyncTask task = null;
	private final int PAGE_SIZE = 20;
	private int pageIndex = 1;
	private TextView tv_title;
	private TextView tv_msg;
	private String threadId="";
	private String postId="";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mine_posts_activity);
		lst_my_posts = (PullToRefreshListView) findViewById(R.id.lst_my_posts);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		tv_title.setText("消息列表");
		tv_msg.setVisibility(View.GONE);
		
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		initIndicator();
		bbsPostsVoList = new ArrayList<Notice>();
		
		myPostsAdapter = new MyMessagesAdapter(this, bbsPostsVoList);
		lst_my_posts.setAdapter(myPostsAdapter);

		lst_my_posts
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						threadId= bbsPostsVoList.get(position-1).getThreadId();
						postId= bbsPostsVoList.get(position-1).getPostId();
//						setReadMessages(postId);
//						Log.v("gl", position+"getThreadId==="+((BbsPosts)parent.getAdapter().getItem(position-1)).getContent());
						
						Intent intent = new Intent(MineMessageLstActivity.this,MinePostsDtlActivity.class);
						intent.putExtra("threadId",threadId);
						startActivity(intent);						
					}
				});

		lst_my_posts
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						
						pageIndex = 1;
						bbsPostsVoList.clear();
		                getData(pageIndex);
						
						
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						
						getData(++pageIndex);
						
					}
				});

//		getData(pageIndex);
	}

	/**
	 * 初始化数据
	 */
	private void getData(int pageIndex) {
		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageIndex.name(),
				pageIndex + "");
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageSize.name(),
				PAGE_SIZE + "");

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_Message_Lst.getValue());
		httpRequestEntity.setRequestCode(RQ_MY_POSTS);
		httpRequestEntity.setHasData(true);

		httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);// 返回使用AES密钥解密
		task = new HttpAsyncTask(this, this);
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
	private void setReadMessages(String postId) {

		// HEARD
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);// 请求使用AES加密

		// BODY
		reqContent.setRequestTicket(true);
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(),
				"000");
		reqContent.appendData(Contants.PROTOCOL_RESP_BODY.postId.name(),postId);

		// SEND
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.SET_READ_POST.getValue());
		httpRequestEntity.setRequestCode(RQ_READ_POSTS);
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

			if (RQ_MY_POSTS.equals(responseContent.getResponseCode())) {
				String jsonStr = responseContent.getDataJson().trim();
				System.out.println("data=" + jsonStr);
				if (jsonStr != null && !"".equals(jsonStr)
						&& jsonStr instanceof String) {

					try {
						JSONObject json = new JSONObject(jsonStr);
						jsonStr = json.get("data").toString();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Gson gson = new Gson();
					List<Notice> list = gson.fromJson(jsonStr,
							new TypeToken<List<Notice>>() {
							}.getType());

					bbsPostsVoList.addAll(list);
					
					if(bbsPostsVoList.isEmpty())
						Contants.isPostMsg =false;
					else
						Contants.isPostMsg =true;
					
					Log.v("gl", "Contants.isPostMsg="+Contants.isPostMsg);
				}
				showInfo();
			}else if (RQ_READ_POSTS.equals(responseContent.getResponseCode())) {
				
				Intent intent = new Intent(this,
						MinePostsDtlActivity.class);
				intent.putExtra("threadId",threadId);
				startActivity(intent);
				
				return;
			}
		}
	}

	private void showInfo() {
		// TODO Auto-generated method stub
		myPostsAdapter.notifyDataSetChanged();

		lst_my_posts.onRefreshComplete();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pageIndex = 1;
		bbsPostsVoList.clear();
        getData(pageIndex);
		
		
	}
}