package com.hdzx.tenement.message;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.utils.TimeFormatUtil;
import com.hdzx.tenement.vo.MessageOutlineBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MsgCircleAdapter extends BaseAdapter {
	private List<MessageOutlineBean> outlineBeans;
	private Context mContext;
	public MsgCircleAdapter(Context context,
	List<MessageOutlineBean> outlineBeans) {
		this.mContext = context;
		this.outlineBeans = outlineBeans;
	}

	@Override
	public int getCount() {
		return outlineBeans == null ? 0 : outlineBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return outlineBeans == null ? null : outlineBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.tenement_messagecircle_list, null);
			new ViewHolder(convertView);
		}
		holder = (ViewHolder) convertView.getTag();
		MessageOutlineBean item = (MessageOutlineBean) getItem(position);
		
		
		holder.textViewMsgType.setText(item.getMsgMessage().getCategoryName());
		holder.textViewMsgInfo.setText(item.getMsgMessage().getTitle());
		String time=TimeFormatUtil.formatTime(item.getMsgMessage().getCreateDate());
		holder.textViewMsgTime.setText(time);
		if (item.getUnreadCount()==0) {
			holder.tv_read.setVisibility(View.GONE);
		}else{
			holder.tv_read.setVisibility(View.VISIBLE);
			holder.tv_read.setText(item.getUnreadCount()+"");
		}
		ImageLoader.getInstance().displayImage(item.getIcon(),
				holder.imageViewMsgTypeIcon, MyApplication.getInstance().getSimpleOptions());

		return convertView;
	}

	class ViewHolder {
		ImageView imageViewMsgTypeIcon;
		TextView textViewMsgType;
		TextView textViewMsgTime;
		TextView textViewMsgInfo;
		TextView tv_read;

		public ViewHolder(View view) {
			imageViewMsgTypeIcon = (ImageView) view
					.findViewById(R.id.img_messagecircle_list_typeicon);
			textViewMsgType = (TextView) view
					.findViewById(R.id.txt_messagecircle_list_type);
			textViewMsgTime = (TextView) view
					.findViewById(R.id.txt_messagecircle_list_time);
			textViewMsgInfo = (TextView) view
					.findViewById(R.id.txt_messagecircle_list_info);
			tv_read = (TextView) view.findViewById(R.id.tv_red_tip);
			view.setTag(this);
		}
	}

}
