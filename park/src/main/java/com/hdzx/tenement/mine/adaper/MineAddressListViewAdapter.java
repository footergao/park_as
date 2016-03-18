package com.hdzx.tenement.mine.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.mine.vo.MineAddressVo;
import com.hdzx.tenement.swipemenulistview.BaseSwipListAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MineAddressListViewAdapter extends BaseSwipListAdapter {

    private Context context = null;

    private List<MineAddressVo> addressList = null;

    private int selectedPosition = -1;// 选中的位置

    public MineAddressListViewAdapter(Context context, List<MineAddressVo> addressList) {
        super();
        this.context = context;
        this.addressList = addressList;
    }

    public MineAddressListViewAdapter(Context context) {
        super();
        this.context = context;
    }

    public List<MineAddressVo> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<MineAddressVo> addressList) {
        this.addressList = addressList;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (addressList != null) {
            count = addressList.size();
        }
        return count;
    }

    @Override
    public MineAddressVo getItem(int position) {
        MineAddressVo item = null;
        if (addressList != null) {
            item = addressList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MineAddressVo item = (MineAddressVo) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tenement_main_mine_address_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            viewHolder.phoneNumTv = (TextView) convertView.findViewById(R.id.phone_num_tv);
//            viewHolder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            viewHolder.addressTv = (TextView) convertView.findViewById(R.id.address_tv);
            viewHolder.selectedIv = (ImageView) convertView.findViewById(R.id.selected_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTv.setText(item.getAbLastuseName());
        viewHolder.phoneNumTv.setText(item.getAbLastuseContact());
//        viewHolder.typeTv.setVisibility(View.VISIBLE);
        String address = StringUtils.defaultString(item.getAbProvince(), "") + StringUtils.defaultString(item.getAbCity(), "")
                + StringUtils.defaultString(item.getAbArea(), "")+ StringUtils.defaultString(item.getAbStreet(), "")
                + StringUtils.defaultString(item.getAbOther(), "");
        viewHolder.addressTv.setText(address);

        /* 显示选中红勾 */
        if (selectedPosition == position) {
            viewHolder.selectedIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedIv.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {

        private TextView nameTv = null;
        private TextView phoneNumTv = null;
        private TextView typeTv = null;
        private TextView addressTv = null;
        private ImageView selectedIv = null;
    }
}
