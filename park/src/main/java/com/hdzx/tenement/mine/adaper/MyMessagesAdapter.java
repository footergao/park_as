package com.hdzx.tenement.mine.adaper;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.mine.vo.Notice;
import com.hdzx.tenement.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 */
public class MyMessagesAdapter extends BaseAdapter {

    private List<Notice> postsVoList;

    private Context context;

    public MyMessagesAdapter(Context context, List<Notice> postsVoList) {
        this.postsVoList = postsVoList;
        this.context = context;
        
        Log.v("gl", "postsVoList=="+postsVoList);

    }

    @Override
    public int getCount() {
        return postsVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return postsVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_item_msg_myposts, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Notice item = (Notice) getItem(position);

        holder.tv_name.setText(item.getReplyUserNick());
        holder.tv_title.setText(item.getReplyContent());
        holder.tv_time.setText(item.getReplyTime());
        
        if(item.getReplyUserHeadPhoto()!=null&&!item.getReplyUserHeadPhoto().equals(""))
        	ImageLoader.getInstance().displayImage(
					UserSession.getInstance().getImageHost() + item.getReplyUserHeadPhoto(),
					 holder.img_photo,MyApplication.getInstance().getSimpleOptions());
        
        if(item.getImageUrl()!=null&&!item.getImageUrl().equals("")&&(item.getImageUrl().contains("jpg")||item.getImageUrl().contains("jpeg")||item.getImageUrl().contains("jpe")||item.getImageUrl().contains("png"))){
        	holder.img_content.setVisibility(View.VISIBLE);
        	ImageLoader.getInstance().displayImage(
					UserSession.getInstance().getImageHost() + item.getImageUrl(),
					 holder.img_content,MyApplication.getInstance().getSimpleOptions());
        }else{
        	holder.tv_content.setVisibility(View.VISIBLE);
        	holder.tv_content.setText(item.getSubject());
        	
        }
        return convertView;
    }

    class ViewHolder {
    	com.hdzx.tenement.widget.CircleImageView img_photo;
        TextView tv_name;
        TextView tv_title;
        TextView tv_time;
        TextView tv_num;
        ImageView img_content;
        TextView tv_content;
        

        public ViewHolder(View view) {
        	img_photo = (CircleImageView) view.findViewById(R.id.img_photo);
        	tv_name = (TextView) view.findViewById(R.id.tv_name);
        	tv_title = (TextView) view.findViewById(R.id.tv_title);
        	tv_time = (TextView) view.findViewById(R.id.tv_time);
        	tv_num = (TextView) view.findViewById(R.id.tv_num);
        	
        	tv_content = (TextView) view.findViewById(R.id.tv_content);
        	img_content = (ImageView) view.findViewById(R.id.img_content);
            view.setTag(this);
        }
    }
}
