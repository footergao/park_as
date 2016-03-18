package com.hdzx.tenement.mine.adaper;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.mine.vo.BbsPosts;
import com.hdzx.tenement.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 */
public class MyPostsDtlAdapter extends BaseAdapter {

	private List<BbsPosts> postsVoList;

	private Context context;

	private Handler handler;

	public MyPostsDtlAdapter(Context context, List<BbsPosts> postsVoList,
			Handler handler) {
		this.postsVoList = postsVoList;
		this.context = context;
		this.handler = handler;

		Log.v("gl", "postsVoList==" + postsVoList);

	}

	@Override
	public int getCount() {
		return postsVoList.size();
	}

	@Override
	public Object getItem(int position) {
		return postsVoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.tenement_item_dtl_myposts, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		final BbsPosts item = (BbsPosts) getItem(position);

		holder.tv_name.setText(item.getUserNickName());
		holder.tv_title.setText(item.getContent());
		holder.tv_time.setText(item.getCreateDate());

		if (item.getUserHeadPhoto() != null
				&& !item.getUserHeadPhoto().equals(""))
			ImageLoader.getInstance().displayImage(
					UserSession.getInstance().getImageHost()
							+ item.getUserHeadPhoto(), holder.img_photo,
							MyApplication.getInstance().getSimpleOptions());

		holder.tv_reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("postId", item.getPostId());
				bundle.putString("nickName", item.getUserNickName());
				msg.setData(bundle);
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		
		
		if(item.getReferUserName()!=null&&!item.getReferUserName().equals("")){
			holder.layout_refer.setVisibility(View.VISIBLE);
			holder.tv_refer.setText(item.getReferUserName());
		}

		return convertView;
	}

	class ViewHolder {
		com.hdzx.tenement.widget.CircleImageView img_photo;
		TextView tv_name;
		TextView tv_title;
		TextView tv_time;
		TextView tv_reply;
		TextView tv_refer;
		LinearLayout layout_refer;

		public ViewHolder(View view) {
			img_photo = (CircleImageView) view.findViewById(R.id.img_photo);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_reply = (TextView) view.findViewById(R.id.tv_reply);
			tv_refer = (TextView) view.findViewById(R.id.tv_refer);
			layout_refer = (LinearLayout) view.findViewById(R.id.layout_refer);
			view.setTag(this);
		}
	}
}
