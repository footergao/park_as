package com.hdzx.tenement.mine.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.hdzx.tenement.mine.adaper.MyPostsAdapter;
import com.hdzx.tenement.mine.vo.BbsPosts;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.Contants;

public class MinePostsActivity extends Activity implements IContentReportor {
	//
	private List<BbsPosts> bbsPostsVoList;
	private MyPostsAdapter myPostsAdapter;
	private com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView lst_my_posts;
	private String RQ_MY_POSTS = "rq_my_posts";
	private HttpAsyncTask task = null;
	private final int PAGE_SIZE = 5;
	private int pageIndex = 1;
	private  TextView tv_msg;
	//
	private AnimationDrawable animationDrawable;
	private MediaPlayer mediaPlayer = null;
	//
	private ImageView img_new_post;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mine_posts_activity);
		lst_my_posts = (PullToRefreshListView) findViewById(R.id.lst_my_posts);
		img_new_post = (ImageView) findViewById(R.id.img_new_post);
		showPostFlag();
		
		initAudioAndAnim();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		initIndicator();
		bbsPostsVoList = new ArrayList<BbsPosts>();
		
		myPostsAdapter = new MyPostsAdapter(this, bbsPostsVoList, mediaPlayer, animationDrawable);
		lst_my_posts.setAdapter(myPostsAdapter);

		lst_my_posts
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
//						Log.v("gl", position+"getThreadId==="+((BbsPosts)parent.getAdapter().getItem(position-1)).getContent());
						Intent intent = new Intent(parent.getContext(),
								MinePostsDtlActivity.class);
						intent.putExtra("threadId",bbsPostsVoList.get(position-1).getThreadId());
						intent.putExtra("postId",bbsPostsVoList.get(position-1).getPostId());
						parent.getContext().startActivity(intent);
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
		
		tv_msg= (TextView) findViewById(R.id.tv_msg);
		tv_msg.setVisibility(View.VISIBLE);
		tv_msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MinePostsActivity.this,MineMessageLstActivity.class);
				startActivity(intent);
			}
		});

		getData(pageIndex);
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
				Contants.PROTOCOL_COMMAND.GET_MY_POSTS.getValue());
		httpRequestEntity.setRequestCode(RQ_MY_POSTS);
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
		animationDrawable = (AnimationDrawable) getResources().getDrawable(R.anim.animation_audio);
	}
	
	
	private void showPostFlag() {
		// TODO Auto-generated method stub
		if(Contants.isPostMsg)
			img_new_post.setVisibility(View.VISIBLE);
		else
			img_new_post.setVisibility(View.GONE);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showPostFlag();
	}
}