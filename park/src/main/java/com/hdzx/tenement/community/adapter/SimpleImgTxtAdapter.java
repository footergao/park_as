package com.hdzx.tenement.community.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.vo.Arcticle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 */
public class SimpleImgTxtAdapter extends BaseAdapter {

	private List<Arcticle> lst;

	private Context context;

	public SimpleImgTxtAdapter(Context context, List<Arcticle> lst) {
		this.lst = lst;
		this.context = context;
	}

	@Override
	public int getCount() {
		return lst.size();
	}

	@Override
	public Object getItem(int position) {
		return lst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_icon_list, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Arcticle item = (Arcticle) getItem(position);

		ImageLoader.getInstance().displayImage(
				UserSession.getInstance().getImageHost() + item.getAcPicture(),
				holder.img_icon, MyApplication.getInstance().getSimpleOptions());
		holder.tv_name.setText(item.getAcName());

		return convertView;
	}

	class ViewHolder {
		ImageView img_icon;
		TextView tv_name;

		public ViewHolder(View view) {
			img_icon = (ImageView) view.findViewById(R.id.img_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(this);
		}
	}
}
