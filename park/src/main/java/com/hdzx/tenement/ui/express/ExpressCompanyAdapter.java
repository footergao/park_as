package com.hdzx.tenement.ui.express;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class ExpressCompanyAdapter extends BaseAdapter {

    private Context context;
    private List<String> expressCompanyList;

    public ExpressCompanyAdapter(Context context, List<String> expressCompanyList) {
        this.context = context;
        this.expressCompanyList = expressCompanyList;
    }

    @Override
    public int getCount() {
        return expressCompanyList.size();
    }

    @Override
    public Object getItem(int position) {
        return expressCompanyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_express_company_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();


        holder.textViewExpressCompany.setText(getItem(position).toString());


        return convertView;
    }

    class ViewHolder {
        TextView textViewExpressCompany;


        public ViewHolder(View view) {
            textViewExpressCompany = (TextView) view.findViewById(R.id.txt_express_companyname);
            view.setTag(this);
        }
    }
}
