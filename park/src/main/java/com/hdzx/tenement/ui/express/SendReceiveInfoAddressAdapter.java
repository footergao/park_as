package com.hdzx.tenement.ui.express;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.ExpressAddressVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class SendReceiveInfoAddressAdapter extends BaseAdapter {

    private Context context;
    private List<ExpressAddressVo> expressAddressVoList;

    public SendReceiveInfoAddressAdapter(Context context, List<ExpressAddressVo> expressAddressVoList) {
        this.context = context;
        this.expressAddressVoList = expressAddressVoList;
    }

    @Override
    public int getCount() {
        return expressAddressVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return expressAddressVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_express_sendreceiveinfo_address_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ExpressAddressVo item = (ExpressAddressVo) getItem(position);

        holder.textViewAddressalias.setText(item.getAddressalias());
        holder.textViewAddressinfo.setText(item.getAddressinfo());
        if (item.getSelectstatus()==1){
            //选中当前view
//            holder.linearLayoutAddressSelect.setBackgroundColor(Color.rgb(0xF9,
//                    0x3F, 0x25));
            holder.imageViewAddressSelect.setVisibility(View.VISIBLE);
        }else if (item.getSelectstatus()==0){
            //未选中
//            holder.linearLayoutAddressSelect.setBackgroundColor(0xffffffff);
            holder.imageViewAddressSelect.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout linearLayoutAddressSelect;
        TextView textViewAddressalias;
        TextView textViewAddressinfo;
        ImageView imageViewAddressSelect;

        public ViewHolder(View view) {
            linearLayoutAddressSelect = (LinearLayout) view.findViewById(R.id.lay_express_sendreciveinfo_address_listselect);
            textViewAddressalias = (TextView) view.findViewById(R.id.txt_express_sendreciveinfo_address_name);
            textViewAddressinfo = (TextView) view.findViewById(R.id.txt_express_sendreciveinfo_address_info);
            imageViewAddressSelect = (ImageView) view.findViewById(R.id.img_express_sendreciveinfo_address_select);
            view.setTag(this);
        }
    }
}
