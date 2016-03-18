package com.hdzx.tenement.message;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Task;
import com.hdzx.tenement.vo.MessageBean;

/**
 * Created by anchendong on 15/7/22.
 */
public class MessageCircleInfoActivity extends Activity implements
		IContentReportor {

	private TextView textViewTitle;
	private PullToRefreshListView listViewInfo;
	private List<MessageBean> messageBeans;

	private MsgListAdapter msgListAdapter;

	private String category;
	private String categoryName;
	private int pageNum = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tenement_messagecircle_info);
		initView();

	}

	private void initView() {
		messageBeans = new ArrayList<MessageBean>();
		if (getIntent() != null) {
			category = getIntent().getStringExtra("category");
			categoryName = getIntent().getStringExtra("categoryName");
		}
		textViewTitle = (TextView) findViewById(R.id.tv_title);
		listViewInfo = (PullToRefreshListView) findViewById(R.id.lv_messagecircle_info);
		
		textViewTitle.setText(categoryName);

		listViewInfo.setMode(PullToRefreshBase.Mode.BOTH);
		ILoadingLayout loadingLayout = listViewInfo.getLoadingLayoutProxy(true,
				false);
		loadingLayout.setPullLabel("下拉刷新");
		loadingLayout.setRefreshingLabel("加载中……");
		loadingLayout.setReleaseLabel("释放刷新当前画面");

		ILoadingLayout loadingLayout2 = listViewInfo.getLoadingLayoutProxy(
				false, true);
		loadingLayout2.setPullLabel("上拉加载更多");
		loadingLayout2.setRefreshingLabel("加载中……");
		loadingLayout2.setReleaseLabel("释放加载更多");
	}

	private void getUserMessage() {
		Task.getUserMessage(this, this, category, pageNum + "", "10", "");
	}

	@Override
	protected void onResume() {
		pageNum=1;
		messageBeans.clear();
		getUserMessage();
		super.onResume();
	}
	
	private void initListView() {
		if (msgListAdapter == null) {
			msgListAdapter = new MsgListAdapter(this, messageBeans);
			listViewInfo.setAdapter(msgListAdapter);
			listViewInfo
					.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							pageNum++;
							getUserMessage();
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							pageNum++;
							getUserMessage();
						}
					});

			listViewInfo
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(view.getContext(),
									MessageCircleDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("messageBean", messageBeans.get(position-1));
							bundle.putSerializable("categoryName", categoryName);
							intent.putExtras(bundle);
							startActivity(intent);
							messageBeans.get(position-1).setRead(true);
						}
					});

		} else {
			new FinishRefresh().execute();
			msgListAdapter.notifyDataSetChanged();
		}
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			listViewInfo.onRefreshComplete();
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_iv:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this != null) {
				Toast.makeText(this, "返回为空", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (!"000000".equals(rtnCode)) {
			if (this != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		String jsonStr = responseContent.getDataJson();
		Gson gson = new Gson();

		List<MessageBean> beans = gson.fromJson(jsonStr,
				new TypeToken<List<MessageBean>>() {
				}.getType());
		if (null!=beans) {
			messageBeans.addAll(beans);
		}
		initListView();
	}
}
