package com.hdzx.tenement.mine.adaper;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hdzx.tenement.MyApplication;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.mine.vo.BbsAttachments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 */
public class SimpleImgAdapter extends BaseAdapter {

    private List<BbsAttachments> lst;

    private Context context;

    public SimpleImgAdapter(Context context, List<BbsAttachments> lst) {
        this.lst = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.griditem, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        BbsAttachments item = (BbsAttachments) getItem(position);
        
        if(item.getFileType().equals("1")){
        	ImageLoader.getInstance().displayImage(
					UserSession.getInstance().getImageHost() + item.getFileUrl(),
					 holder.img_photo,MyApplication.getInstance().getSimpleOptions());
        }
        
        return convertView;
    }

    class ViewHolder {
    	ImageView img_photo;

        public ViewHolder(View view) {
        	img_photo = (ImageView) view.findViewById(R.id.griditem_pic);
            view.setTag(this);
        }
    }
}
