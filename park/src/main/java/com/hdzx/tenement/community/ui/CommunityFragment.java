package com.hdzx.tenement.community.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.ui.PluginActivity;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommunityFragment extends Fragment implements IContentReportor,
		OnClickListener {

	private ViewGroup myContainer = null;

	private DisplayMetrics displayMetrics = null;

	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tenement_main_community,
				container, false);
		myContainer = (ViewGroup) view.findViewById(R.id.myContainer);

		init(view);
		return view;
	}


	private void init(View view) {
		displayMetrics = this.getResources().getDisplayMetrics();

		makeServiceInfoRequest();
	}

	private void initView(ViewGroup myContainer,
			List<ServiceBean> serviceBeanList) {
		LinearLayout oneServiceLayout = null;
		LinearLayout.MarginLayoutParams layoutParams = null;
		LinearLayout.LayoutParams viewLParams = null;
		View view = null;
		TextView titleText = null;
		LinearLayout.LayoutParams textLParams = null;
		String serviceName = null;
		LinearLayout headLayout = null;
		ImageView imageView= null;

		if (serviceBeanList != null) {
			for (ServiceBean item : serviceBeanList) {
				oneServiceLayout = new LinearLayout(activity);
				oneServiceLayout.setOrientation(LinearLayout.VERTICAL);
				layoutParams = new LinearLayout.MarginLayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				oneServiceLayout.setLayoutParams(layoutParams);
				oneServiceLayout.setBackgroundColor(Color.WHITE);
				
				headLayout = new LinearLayout(activity);
				headLayout.setOrientation(LinearLayout.HORIZONTAL);
				headLayout.setLayoutParams(layoutParams);
				headLayout.setGravity(Gravity.CENTER_VERTICAL);
				
				
				imageView = new ImageView(this.activity);
				textLParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				textLParams.leftMargin = 20;
				textLParams.topMargin = 20;
				textLParams.bottomMargin = 5;
				imageView.setLayoutParams(textLParams);
				
				titleText = new TextView(activity);
				textLParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				textLParams.leftMargin = 20;
				textLParams.topMargin = 20;
				textLParams.bottomMargin = 5;
				titleText.setLayoutParams(textLParams);
				
				
				serviceName = item.getServiceName();
				if ("城市服务".equals(serviceName)) {
					titleText.setTextColor(getResources().getColor(R.color.yellow_main));
					ImageLoader.getInstance().displayImage("drawable://" + R.drawable.comm_life,
							imageView);
				} else if ("物业服务".equals(serviceName)) {
					titleText.setTextColor(getResources().getColor(R.color.blue_main));
					ImageLoader.getInstance().displayImage("drawable://" + R.drawable.comm_help,
							imageView);
				} else if ("社区服务".equals(serviceName)) {
					titleText.setTextColor(getResources().getColor(R.color.green_main));
					ImageLoader.getInstance().displayImage("drawable://" + R.drawable.comm_service,
							imageView);
				} else {
					titleText.setTextColor(Color.BLACK);
				}
				titleText.setText(serviceName);

				if (!item.getServiceName().equals("物业服务资格指派")){
					headLayout.addView(titleText);
					headLayout.addView(imageView);
					oneServiceLayout.addView(headLayout);
				}

				if (item.getChildren() != null && !item.getChildren().isEmpty()) {
					if (!item.getServiceName().equals("物业服务资格指派"))
						initSubView(oneServiceLayout, item.getChildren());
				}

				myContainer.addView(oneServiceLayout);

				viewLParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 30);
				view = new View(this.activity);
				view.setLayoutParams(viewLParams);
				myContainer.addView(view);
			}
		}
	}

	private void initSubView(ViewGroup container,
			List<ServiceBean> serviceBeanList) {
		LinearLayout oneSubServiceLayout = new LinearLayout(this.activity);
		oneSubServiceLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		oneSubServiceLayout.setLayoutParams(layoutParams);

		TextView titleText;
		ImageView imageView;
		LinearLayout onServiceLL;
		LinearLayout.LayoutParams llParams;
		LinearLayout.LayoutParams imageLParams;
		LinearLayout.LayoutParams textLParams;
		int width = displayMetrics.widthPixels / 4;
		int imageWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
		for (ServiceBean item : serviceBeanList) {
			onServiceLL = new LinearLayout(this.activity);
			llParams = new LinearLayout.LayoutParams(width, width);
			onServiceLL.setLayoutParams(llParams);
			onServiceLL.setOrientation(LinearLayout.VERTICAL);
			onServiceLL.setGravity(Gravity.CENTER);
			onServiceLL.setOnClickListener(this);
			onServiceLL.setTag(item);

			imageView = new ImageView(this.activity);
			ImageLoader.getInstance().displayImage(item.getServiceIcon(),
					imageView, MyApplication.getInstance().getSimpleOptions());
			imageLParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);
			imageView.setLayoutParams(imageLParams);

			titleText = new TextView(this.activity);
			titleText.setText(item.getServiceName());
			titleText.setTextSize(12);
			titleText.setTextColor(Color.BLACK);
			textLParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			textLParams.topMargin = 20;
			titleText.setLayoutParams(textLParams);

			onServiceLL.addView(imageView);
			onServiceLL.addView(titleText);

			oneSubServiceLayout.addView(onServiceLL);
		}

		container.addView(oneSubServiceLayout);
	}

	public void makeServiceInfoRequest() {
		RequestContentTemplate reqContent = new RequestContentTemplate();
		reqContent.setEncryptoType(CryptoTyepEnum.aes);
		// reqContent.setRequestTicket(true);
		reqContent.appendData("pid", "0");
		// reqContent.appendData("deep", "4");
		reqContent.appendData(Contants.PROTOCOL_REQ_BODY.ticket.name(),
				UserSession.getInstance().getAccessTicket());

		HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
				Contants.SERVER_HOST,
				Contants.PROTOCOL_COMMAND.GET_SERVICE_INFO.getValue());
		httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
		HttpAsyncTask task = new HttpAsyncTask(activity, this);
		task.execute(httpRequestEntity);
	}

	@Override
	public void reportBackContent(ResponseContentTamplate responseContent) {
		String rtnCode = (String) responseContent
				.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
		if ("".equals(rtnCode) || rtnCode == null) {
			if (this.activity != null) {
				Toast.makeText(this.activity, "网络异常，请稍后尝试", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
			if (this.activity != null) {
				String rtnMsg = (String) responseContent
						.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
				Toast.makeText(this.activity, rtnMsg, Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			String jsonStr = responseContent.getDataJson();
			Log.v("", "serviceInfo:" + jsonStr);
			Gson gson = new Gson();
			List<ServiceBean> serviceBeanList = gson.fromJson(jsonStr,
					new TypeToken<List<ServiceBean>>() {
					}.getType());
			if (serviceBeanList != null && !serviceBeanList.isEmpty()) {
				for (ServiceBean item : serviceBeanList.get(0).getChildren()) {
					if ("社区通".equals(item.getServiceName())) {
						if (item.getChildren() != null
								&& !item.getChildren().isEmpty()) {
							initView(myContainer, item.getChildren());
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		ServiceBean service = (ServiceBean) v.getTag();
		if (service.getServiceName().equals("便民电话")) {
			intent = new Intent(activity, CommonConvenPhoneListActivity.class);
		} else if (service.getServiceName().equals("快递查询")) {
			intent = new Intent(activity, ExpressInquiry.class);
		} else if (service.getServiceName().equals("家政服务")
				|| service.getServiceName().equals("快递收发")
				|| service.getServiceName().equals("家居维修")
				|| service.getServiceName().equals("洗刷刷")) {
			intent = new Intent(this.activity, PluginActivity.class);
			intent.putExtra(PluginActivity.PARAM_URL,
					Contants.getHost()
							+ "/help/app/server/index.html?serversName="
							+ service.getServiceName());
			intent.putExtra(PluginActivity.PARAM_TITLE,
					service.getServiceName());
			intent.putExtra(PluginActivity.PARAM_DISPLAY_TOPBAR, true);
		} else if (service.getServiceName().equals("设施报修")) {
			intent = new Intent(activity, CommunityRepairActivity.class);
		} else if (service.getServiceName().equals("投诉建议")) {
			intent = new Intent(activity, CommunitySuggestActivity.class);
		}else if (service.getServiceName().equals("手机开门")) {
			intent = new Intent(activity, OpenDoorActivity.class);
		}
		if (intent != null) {
			this.startActivity(intent);
		} else {
			Toast.makeText(this.activity, "敬请期待！", Toast.LENGTH_SHORT).show();
		}
	}
}
