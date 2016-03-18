package com.hdzx.tenement.community.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.hdzx.tenement.R;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CommonDynamicPopWindow extends PopupWindow implements OnClickListener
{
    private Context context = null;
    
    private DisplayImageOptions displayImageOptions = null;
    
    private int singleHeight = 100;
    
    private ServiceBean serviceBean = null;
    
    private View mainView = null;
    
    private ImageView serviceImageIv = null;
    
    private TextView serviceNameTv = null;
    
    private TextView selectedTv = null;
    
    private TextView targetTv = null;
    
    private LinearLayout contentContainer = null;
    
    public CommonDynamicPopWindow(Context context)
    {
        super(context);
        this.context = context;
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.common_pop_window_layout, null);
        
        serviceImageIv = (ImageView) mainView.findViewById(R.id.service_image_id);
        serviceNameTv = (TextView) mainView.findViewById(R.id.service_name_tv_id);
        contentContainer = (LinearLayout) mainView.findViewById(R.id.pop_content_id);
        Button submit = (Button) mainView.findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (selectedTv == null)
                {
                    Toast.makeText(CommonDynamicPopWindow.this.context, "您未选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (targetTv != null)
                {
                    targetTv.setText(selectedTv.getText());
                }
                
                //测试时使用，等服务器修改好对应需要修改
                if ("serviceLevel".equals(serviceBean.getMappingKey()))
                {
                    serviceBean.setValue("1");
                }
                else
                {
                    serviceBean.setValue(selectedTv.getText().toString());
                }
                
                CommonDynamicPopWindow.this.dismiss();
            }
            
        });
        
        initDisplayImageOptions();
        
        super.setContentView(mainView);
        
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
    }
    
    private void initDisplayImageOptions()
    {
         displayImageOptions = new DisplayImageOptions.Builder().build();
    }
    
    public void initView(ServiceBean serviceBean, TextView targetTv)
    {
        this.serviceBean = serviceBean;
        this.targetTv = targetTv;
        if (serviceBean != null)
        {
            if (serviceBean.getServiceIcon() != null && !"".equals(serviceBean.getServiceIcon()))
            {
                serviceImageIv.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(serviceBean.getServiceIcon(), serviceImageIv, displayImageOptions);
            }
            else
            {
                serviceImageIv.setVisibility(View.GONE);
            }
            
            serviceNameTv.setText(serviceBean.getServiceName());
            
            List<ServiceBean> chidrenList = serviceBean.getChildren();
            if (chidrenList != null && !chidrenList.isEmpty())
            {
                contentContainer.removeAllViews();
                ServiceBean item = null;
                for (int i = 0; i < chidrenList.size(); i++)
                {
                    if (i != 0)
                    {
                        contentContainer.addView(makeEmptyComp());
                    }
                    
                    item = chidrenList.get(i);
                    
                    TextView tv = new TextView(context);
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(14);
                    tv.setText(item.getServiceName());
                    tv.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, singleHeight);
                    tv.setLayoutParams(tvLayoutParams);
                    tv.setOnClickListener(this);
                    
                    contentContainer.addView(tv);
                    
                    if (targetTv.getText().toString().equals(item.getServiceName()))
                    {
                        selectedTv = tv;
                        selectedTv.setBackgroundColor(Color.RED);
                    }
                }
            }
        }
    }
    
    private View makeEmptyComp()
    {
        LinearLayout.LayoutParams viewLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        View view = new View(context);
        view.setBackgroundColor(Color.GRAY);
        view.setLayoutParams(viewLParams);
        
        return view;
    }

    public int getSingleHeight()
    {
        return singleHeight;
    }

    public void setSingleHeight(int singleHeight)
    {
        this.singleHeight = singleHeight;
    }

    @Override
    public void onClick(View v)
    {
        if (v instanceof TextView)
        {
            if (selectedTv != null)
            {
                selectedTv.setBackgroundColor(Color.TRANSPARENT);
            }
            
            selectedTv = (TextView) v;
            selectedTv.setBackgroundColor(Color.RED);
        }
    }
}
