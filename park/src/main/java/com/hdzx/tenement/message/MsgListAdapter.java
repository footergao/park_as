package com.hdzx.tenement.message;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MessageBean;

public class MsgListAdapter extends BaseAdapter{
	private List<MessageBean> outlineBeans;
	private Context mContext;

    public MsgListAdapter(Context context, List<MessageBean> outlineBeans) {
        this.mContext = context;
        this.outlineBeans = outlineBeans;
    }

    @Override
    public int getCount() {
        return outlineBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return outlineBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.tenement_messagecircle_info_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        MessageBean item = (MessageBean) getItem(position);
        holder.textViewMsgTitle.setText(item.getTitle());
        holder.textViewMsgInfo.setText(item.getContent());
        holder.textViewMsgTime.setText(item.getCreateDate());
        if (item.isRead()) {
			holder.tv_detail.setTextColor(mContext.getResources().getColor(R.color.grat));
		}else{
			holder.tv_detail.setTextColor(mContext.getResources().getColor(R.color.pink));
		}

        return convertView;
    }

    class ViewHolder {
        TextView textViewMsgTitle;
        TextView textViewMsgTime;
        TextView textViewMsgInfo;
        TextView tv_detail;

        public ViewHolder(View view) {
            textViewMsgTitle = (TextView) view.findViewById(R.id.txt_messagecircle_info_list_title);
            textViewMsgTime = (TextView) view.findViewById(R.id.txt_messagecircle_info_list_time);
            textViewMsgInfo = (TextView) view.findViewById(R.id.txt_messagecircle_info_list_detail);
            tv_detail = (TextView) view.findViewById(R.id.tv_detail);
            view.setTag(this);
        }
    }

}
