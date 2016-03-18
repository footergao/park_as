package com.hdzx.tenement.ui.express;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.ExpressWeightVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class ExpressWeightAdapter extends BaseAdapter {

    private Context context;
    private List<ExpressWeightVo> expressWeightList;

    public ExpressWeightAdapter(Context context, List<ExpressWeightVo> expressWeightList) {
        this.context = context;
        this.expressWeightList = expressWeightList;
    }

    @Override
    public int getCount() {
        return expressWeightList.size();
    }

    @Override
    public Object getItem(int position) {
        return expressWeightList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_express_weight_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        ExpressWeightVo expressWeightVo = (ExpressWeightVo) getItem(position);
        String weight = expressWeightVo.getEdStandardAbove() + "~" + expressWeightVo.getEdStandardBelow();
        holder.textViewExpressWeight.setText(weight);

        return convertView;
    }

    class ViewHolder {
        TextView textViewExpressWeight;

        public ViewHolder(View view) {
            textViewExpressWeight = (TextView) view.findViewById(R.id.txt_express_weight);
            view.setTag(this);
        }
    }
}
