package com.hdzx.tenement.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hdzx.tenement.common.vo.AdvertisementImage;
import com.hdzx.tenement.utils.Logger;

import java.util.List;

/**
 * Created by anchendong on 15/7/20.
 */
public class MainAdAdapter extends PagerAdapter {

    // 滑动的图片集合
    private List<ImageView> imageViews;

    //广告对象
    private List<AdvertisementImage> adDomains;
    
    private Activity activity;

    public MainAdAdapter(List<ImageView> imageViews, List<AdvertisementImage> adDomains,Activity activity) {
        this.adDomains = adDomains;
        this.imageViews = imageViews;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return adDomains.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView iv = imageViews.get(position);
        ((ViewPager) container).addView(iv);
        // 在这个方法里面设置图片的点击事件
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 处理跳转逻辑
            	
            	
            	Intent  intent = new Intent();
            	intent.setClass(activity, ProductDetailActivity.class);
            	intent.putExtra("poster", adDomains.get(position).getLinkUrl());
            	activity.startActivity(intent);
            	
            	Logger.v("gl", "linkurl=="+adDomains.get(position).getLinkUrl());
            	Logger.v("gl", "linkurl"+adDomains.get(position).getImageUrl());
            	
            	
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


}
