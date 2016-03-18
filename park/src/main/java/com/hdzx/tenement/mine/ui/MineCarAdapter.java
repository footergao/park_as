package com.hdzx.tenement.mine.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MineCarVo;

import java.util.List;

public class MineCarAdapter extends BaseAdapter {

    private Context context;
    private List<MineCarVo> mineCarVoList;

    public MineCarAdapter(Context context, List<MineCarVo> mineCarVoList) {
        this.context = context;
        this.mineCarVoList = mineCarVoList;
    }

    @Override
    public int getCount() {
        return mineCarVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mineCarVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_main_mine_car_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        MineCarVo mineCarVo = (MineCarVo) getItem(position);
        holder.textViewMineCarName.setText(mineCarVo.getCarName());
        holder.textViewMineLicenseplate.setText(mineCarVo.getLicensePlate());

        return convertView;
    }

    class ViewHolder {
        TextView textViewMineCarName;
        TextView textViewMineLicenseplate;

        public ViewHolder(View view) {
            textViewMineCarName = (TextView) view.findViewById(R.id.txt_main_mine_carname);
            textViewMineLicenseplate = (TextView) view.findViewById(R.id.txt_main_mine_licenseplate);
            view.setTag(this);
        }
    }
}
