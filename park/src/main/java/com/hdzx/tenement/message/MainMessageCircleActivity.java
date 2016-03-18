package com.hdzx.tenement.message;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.swipemenulistview.SwipeMenu;
import com.hdzx.tenement.swipemenulistview.SwipeMenuCreator;
import com.hdzx.tenement.swipemenulistview.SwipeMenuItem;
import com.hdzx.tenement.swipemenulistview.SwipeMenuListView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Logger;
import com.hdzx.tenement.utils.Task;
import com.hdzx.tenement.vo.MessageOutlineBean;
import com.hdzx.tenement.vo.MsgCircleVo;

public class MainMessageCircleActivity extends Activity implements
		IContentReportor, OnItemClickListener {
	private String TAG = "MainMessageCircleFragment";
	private String HTTP_SET_READ_MSGS="setReadMessageByCategory";
	private ImageView iv_back;
	private TextView tv_title;
	private SwipeMenuListView listView;
	// true/false，是否查询最新消息
	private boolean getLastMessage = true;

	private List<MessageOutlineBean> outlineBeans;
	private List<MessageOutlineBean> allOutlineBeans;

	private MsgCircleAdapter msgCircleAdapter;
	private SwipeMenuCreator mMenuCreator;
	
	private int changePosition;
	private boolean isHidden=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenement_main_message_fragment);
		initView();
		
	}
	

	private void initView() {
		outlineBeans = new ArrayList<MessageOutlineBean>();
		tv_title = (TextView) findViewById(R.id.tv_title);
		listView = (SwipeMenuListView) findViewById(R.id.lv_msg_circle);
		iv_back = (ImageView)findViewById(R.id.back_iv);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		

		tv_title.setText(R.string.txt_messagecircle);

		Task.getAllMessageOutline(this, this, getLastMessage, "");
	}

	@Override
	public void onResume() {
		Logger.i(TAG, "onResume-->isHidden="+isHidden);
//		if (!isHidden) {// 显示
			Task.getAllMessageOutline(this, this, true, "");
//		}
		super.onResume();
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this != null) {
				Toast.makeText(this, "返回为空", Toast.LENGTH_SHORT)
						.show();
			}
			return;
		}
		if (!"000000".equals(rtnCode)) {
			if (this != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT)
						.show();
			}
			return;
		}
		if (HTTP_SET_READ_MSGS.equals(responseContent.getResponseCode())) {
			outlineBeans.get(changePosition).setUnreadCount(0);
			msgCircleAdapter.notifyDataSetChanged();
			return;
		}
		String jsonStr = responseContent.getDataJson();
		Gson gson = new Gson();
		MsgCircleVo msgCircleVo = gson.fromJson(jsonStr,
				new TypeToken<MsgCircleVo>() {
				}.getType());
		allOutlineBeans = msgCircleVo.getOutlineList();
		initList();
	}

	private void initList() {
		outlineBeans.clear();
		for (MessageOutlineBean messageOutlineBean : allOutlineBeans) {
			if (messageOutlineBean.getMsgMessage() != null) {
				if (getLastMessage) {

				}
				outlineBeans.add(messageOutlineBean);
			}
		}
		if (msgCircleAdapter == null) {
			msgCircleAdapter = new MsgCircleAdapter(this, outlineBeans);
			listView.setAdapter(msgCircleAdapter);
			listView.setOnItemClickListener(this);
			initMessageCircleList();
		} else {
			msgCircleAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.setClass(this, MessageCircleInfoActivity.class);
		intent.putExtra("category", outlineBeans.get(position).getCategory());
		intent.putExtra("categoryName", outlineBeans.get(position).getMsgMessage().getCategoryName());
		this.startActivity(intent);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	private void initMessageCircleList() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(MainMessageCircleActivity.this);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set item title
				deleteItem.setTitle("已读");
				// set item title fontsize
				deleteItem.setTitleSize(18);
				// set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// set a icon
				// deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		// set creator
		listView.setMenuCreator(creator);

		// step 2. listener item click event
		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				MessageOutlineBean item = outlineBeans.get(position);
				switch (index) {
				case 0:
//					outlineBeans.remove(position);
					setReadMessages(item);
					changePosition=position;
//					msgCircleAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	protected void setReadMessages(MessageOutlineBean item) {
		Task.setReadMessageByCategory(this, this, item.getCategory(), HTTP_SET_READ_MSGS);
	}
}
