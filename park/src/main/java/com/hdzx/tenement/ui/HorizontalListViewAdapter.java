package com.hdzx.tenement.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.CommodityVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HorizontalListViewAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private int selectIndex = -1;
	private List<CommodityVo> commodityVoList;

	public HorizontalListViewAdapter(Activity activity,
			List<CommodityVo> commodityVoList) {
		// TODO Auto-generated constructor stub
		this.mContext = activity;
		this.commodityVoList = commodityVoList;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return commodityVoList.size();
	}

	@Override
	public CommodityVo getItem(int position) {
		return commodityVoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.horizontal_list_item, null);
			holder.mImage = (ImageView) convertView
					.findViewById(R.id.img_list_item);
			holder.mTitle = (TextView) convertView
					.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == selectIndex) {
			convertView.setSelected(true);
		} else {
			convertView.setSelected(false);
		}

		holder.mTitle.setText(getItem(position).getName());

		ImageLoader.getInstance().displayImage(getItem(position).getLogo(),
				holder.mImage, MyApplication.getInstance().getSimpleOptions());

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle;
		private ImageView mImage;
	}

	public void setSelectIndex(int i) {
		selectIndex = i;
	}
}