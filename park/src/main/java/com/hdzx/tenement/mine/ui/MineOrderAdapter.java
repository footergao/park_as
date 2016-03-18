package com.hdzx.tenement.mine.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MineOrderVo;

import java.util.List;

public class MineOrderAdapter extends BaseAdapter {

    private Context context;
    private List<MineOrderVo> mineOrderVoList;


    public MineOrderAdapter(Context context, List<MineOrderVo> mineOrderVoList) {
        this.context = context;
        this.mineOrderVoList = mineOrderVoList;
    }

    @Override
    public int getCount() {
        return mineOrderVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mineOrderVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_main_mine_order_tab_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        MineOrderVo mineOrderVo = (MineOrderVo) getItem(position);

        holder.textViewOrderStatus.setText(mineOrderVo.getStatus());
        holder.textViewOrderStatusInfo.setText(mineOrderVo.getStatusInfo());
        if (mineOrderVo.getStatusInfo().equals("等待付款")) {
            holder.textViewOrderCancle.setVisibility(View.VISIBLE);
        }

        holder.textViewOrdertype.setText(mineOrderVo.getOrdertype());
        holder.textViewOrdertypeStatus.setText(mineOrderVo.getOrdertypeStatus());
        holder.textViewOrderCost.setText(mineOrderVo.getCost());


        return convertView;
    }

    class ViewHolder {
        TextView textViewOrderStatus;
        TextView textViewOrderStatusInfo;
        TextView textViewOrdertype;
        TextView textViewOrdertypeStatus;
        TextView textViewOrderCost;

        TextView textViewOrderCancle;

        public ViewHolder(View view) {
            textViewOrderStatus = (TextView) view.findViewById(R.id.txt_main_mine_order_status);
            textViewOrderStatusInfo = (TextView) view.findViewById(R.id.txt_main_mine_order_statusInfo);
            textViewOrdertype = (TextView) view.findViewById(R.id.txt_main_mine_order_ordertype);
            textViewOrdertypeStatus = (TextView) view.findViewById(R.id.txt_main_mine_order_ordertypeStatus);
            textViewOrderCost = (TextView) view.findViewById(R.id.txt_main_mine_order_cost);
            textViewOrderCancle = (TextView) view.findViewById(R.id.txt_main_mine_order_cancle);


            view.setTag(this);
        }
    }
}
