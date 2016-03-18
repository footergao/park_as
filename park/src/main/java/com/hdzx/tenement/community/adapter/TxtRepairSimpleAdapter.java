package com.hdzx.tenement.community.adapter;

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
import com.hdzx.tenement.community.vo.Repair;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TxtRepairSimpleAdapter extends BaseAdapter{
	
	private List<Repair> list;
	private LayoutInflater inflater = null;
	private Context mContext;

	public TxtRepairSimpleAdapter(Context context,List<Repair> list) {
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
		Repair lifeCircleVo = (Repair) list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_simple_list,
					null);
			holder = new ViewHolder(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(lifeCircleVo.getRepairContent());
		
		
		Log.v("gl", "lifeCircleVo.getHasnewMsg()=="+lifeCircleVo.getHasNewMsg());
		
		if(lifeCircleVo.getHasNewMsg())
			showMsgCircleFlag(true, holder.img_new);
		else
			showMsgCircleFlag(false, holder.img_new);
			
		
		return convertView;
	}
	
	class ViewHolder{
		private TextView tv_name;
		private ImageView img_new;
		public  ViewHolder(View view){
			tv_name=(TextView)view.findViewById(R.id.tv_list_item);
			img_new=(ImageView)view.findViewById(R.id.img_new);
			view.setTag(this);
		}
	}
	private void showMsgCircleFlag(boolean isNewMsg ,ImageView img) {
		// TODO Auto-generated method stub
		if(isNewMsg)
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.icon_new_msg, img);
		else
			ImageLoader.getInstance().displayImage("drawable://"+R.drawable.ic_action_next_item, img);
		
	}

}
