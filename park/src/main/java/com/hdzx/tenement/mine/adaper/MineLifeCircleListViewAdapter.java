package com.hdzx.tenement.mine.adaper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.mine.vo.MineAddressVo;

import java.util.List;

public class MineLifeCircleListViewAdapter extends BaseAdapter {

    private Context context = null;

    private List<LifeCircleAddressVo> myLifeCircleList = null;

    private int selectedPosition = -1;// 选中的位置
    
    private String lifeId = "";// 
    
    public MineLifeCircleListViewAdapter(Context context, List<LifeCircleAddressVo> myLifeCircleList,String lifeId) {
        super();
        this.context = context;
        this.myLifeCircleList = myLifeCircleList;
        this.lifeId =lifeId;
    }

    public MineLifeCircleListViewAdapter(Context context) {
        super();
        this.context = context;
    }

    public List<LifeCircleAddressVo> myLifeCircleList() {
        return myLifeCircleList;
    }

    public void setAddressList(List<LifeCircleAddressVo> myLifeCircleList) {
        this.myLifeCircleList =myLifeCircleList;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (myLifeCircleList != null) {
            count = myLifeCircleList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (myLifeCircleList != null) {
            item = myLifeCircleList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LifeCircleAddressVo item = (LifeCircleAddressVo) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tenement_common_position_item, null);
            viewHolder = new ViewHolder();
            
            viewHolder.layoutItem = convertView
					.findViewById(R.id.position_item_layout);
            viewHolder.tvItem = (TextView) convertView
					.findViewById(R.id.area_name_textView);
            viewHolder.tvCityItem = (TextView) convertView
					.findViewById(R.id.city_name_textView);
            viewHolder.iv = (ImageView) convertView
					.findViewById(R.id.area_selected_ImageView);
			

            
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.tvCityItem.setText("["
				+ item.getLifecircleCity() + "]");
        viewHolder.tvItem.setText(item.getLifecircleName());
        
        Log.v("gl", "lifeId=="+lifeId+"item.getLifecircleId()=="+item.getLifecircleId());
        if (lifeId.equals(item.getLifecircleId()+"")) {

    		viewHolder.layoutItem
					.setBackgroundResource(R.drawable.common_position_area_selected);

    		viewHolder.iv.setVisibility(View.VISIBLE);

		} else {
			viewHolder.layoutItem
					.setBackgroundResource(R.drawable.common_position_area_no_select);
			viewHolder.iv.setVisibility(View.INVISIBLE);
			// lastSelectedView.invalidate();
		}
        

        /* 显示选中红勾 */
//    	if (selectedPosition == position) {
//
//    		viewHolder.layoutItem
//					.setBackgroundResource(R.drawable.common_position_area_selected);
//
//    		viewHolder.iv.setVisibility(View.VISIBLE);
//
//		} else {
//			viewHolder.layoutItem
//					.setBackgroundResource(R.drawable.common_position_area_no_select);
//			viewHolder.iv.setVisibility(View.INVISIBLE);
//			// lastSelectedView.invalidate();
//		}


        return convertView;
    }

    private class ViewHolder {

        private View layoutItem = null;
        private TextView tvItem = null;
        private TextView tvCityItem = null;
        private ImageView iv = null;
    }
}
