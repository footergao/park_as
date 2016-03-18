package com.hdzx.tenement.ui.express;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.ExpressOrderVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class WaitReceiveAdapter extends BaseAdapter {

    private List<ExpressOrderVo> expressOrderVoList;

    private Context context;

    public WaitReceiveAdapter(Context context, List<ExpressOrderVo> expressOrderVoList) {
        this.context = context;
        this.expressOrderVoList = expressOrderVoList;
    }

    @Override
    public int getCount() {
        return expressOrderVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return expressOrderVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_express_waitreceive_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ExpressOrderVo item = (ExpressOrderVo) getItem(position);
        if (item.getEcStatus().equals("006")){
            holder.textViewOrderstatus.setText("等待交件");
            holder.textViewOrderstatus.setTextColor(Color.parseColor("#4AD36A"));
        }
        else if (item.getEcStatus().equals("007")){
            holder.textViewOrderstatus.setText("等待评价");
            holder.textViewOrderstatus.setTextColor(Color.parseColor("#F0F0F0"));
        } else if(item.getEcStatus().equals("005")){
            holder.textViewOrderstatus.setText("等待抢单");
            holder.textViewOrderstatus.setTextColor(Color.parseColor("#ef6e5a"));
        }
        holder.textViewOrderid.setText(item.getEcId());
        return convertView;
    }

    class ViewHolder {
        TextView textViewOrderid;
        TextView textViewOrderstatus;

        public ViewHolder(View view) {
            textViewOrderid = (TextView) view.findViewById(R.id.txt_express_waitreceive_orderid);
            textViewOrderstatus = (TextView) view.findViewById(R.id.txt_express_waitreceive_orderstatus);
            view.setTag(this);
        }
    }
}
