package com.hdzx.tenement.mine.adaper;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.community.vo.Suggest;
import com.hdzx.tenement.mine.vo.BbsForums;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;

public class TxtSimpleAdapter extends BaseAdapter{
	
	private List<BbsForums> list;
	private LayoutInflater inflater = null;
	private Context mContext;
	private int pos=0;

	public TxtSimpleAdapter(Context context,List<BbsForums> list,int pos) {
		super();
		this.list = list;
		this.mContext=context;
		this.pos = pos;
		inflater = LayoutInflater.from(mContext);
		
		Log.v("gl", "pos=="+pos);
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
		BbsForums lifeCircleVo = (BbsForums) list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_checked_list,
					null);
			holder = new ViewHolder(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(lifeCircleVo.getForumName());
		
		if(pos==position)
			holder.img_checked.setVisibility(View.VISIBLE);
		else
			holder.img_checked.setVisibility(View.INVISIBLE);
		return convertView;
	}
	
	class ViewHolder{
		private TextView tv_name;
		private ImageView img_checked;
		public  ViewHolder(View view){
			tv_name=(TextView)view.findViewById(R.id.tv_list_item);
			img_checked= (ImageView) view.findViewById(R.id.img_checked);
			view.setTag(this);
		}
	}

}
