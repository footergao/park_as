package com.hdzx.tenement.mine.adaper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;

public class MyLifeCircleSimpleAdapter extends BaseAdapter{
	
	private List<LifeCircleAddressVo> list;
	private LayoutInflater inflater = null;
	private Context mContext;

	public MyLifeCircleSimpleAdapter(List<LifeCircleAddressVo> list,Context context) {
		super();
		this.list = list;
		this.mContext=context;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LifeCircleAddressVo lifeCircleVo = (LifeCircleAddressVo) list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_simple_list,
					null);
			holder = new ViewHolder(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(lifeCircleVo.getLifecircleName());
		return convertView;
	}
	
	class ViewHolder{
		private TextView tv_name;
		public  ViewHolder(View view){
			tv_name=(TextView)view.findViewById(R.id.tv_list_item);
			view.setTag(this);
		}
	}

}
