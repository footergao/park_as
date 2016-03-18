package com.hdzx.tenement.ui.express;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.ExpressPhoneVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class PhoneManageAdapter extends BaseAdapter{

    private Context context;
    private List<ExpressPhoneVo> expressPhoneVoList;

    public PhoneManageAdapter(Context context,List<ExpressPhoneVo> expressPhoneVoList){
        this.context=context;
        this.expressPhoneVoList=expressPhoneVoList;
    }

    @Override
    public int getCount() {
        return expressPhoneVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return expressPhoneVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_express_phonemanage_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ExpressPhoneVo item = (ExpressPhoneVo) getItem(position);

        String expressphonenum=item.getEnpPhone();
        String phonenum=expressphonenum.substring(0,3)+"****"+expressphonenum.substring(expressphonenum.length()-4,expressphonenum.length());
        holder.textViewPhonenum.setText(phonenum);
        holder.textViewPhonename.setText(item.getEnpName());

        return convertView;
    }

    class ViewHolder {
        TextView textViewPhonenum;
        TextView textViewPhonename;

        public ViewHolder(View view) {
            textViewPhonenum = (TextView) view.findViewById(R.id.txt_express_phonemanage_phonenum);
            textViewPhonename = (TextView) view.findViewById(R.id.txt_express_phonemanage_phonename);
            view.setTag(this);
        }
    }
}
