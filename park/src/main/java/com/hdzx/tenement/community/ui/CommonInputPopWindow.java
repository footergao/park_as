package com.hdzx.tenement.community.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.hdzx.tenement.R;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommonInputPopWindow extends PopupWindow
{
    private Context context = null;
    
    private DisplayImageOptions displayImageOptions = null;
    
    private ServiceBean serviceBean = null;
    
    private View mainView = null;
    
    private ImageView serviceImageIv = null;
    
    private TextView serviceNameTv = null;
    
    private TextView targetTv = null;
    
    private EditText inputEt = null;
    
    public CommonInputPopWindow(Context context)
    {
        super(context);
        this.context = context;
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.common_input_pop_window, null);
        
        serviceImageIv = (ImageView) mainView.findViewById(R.id.service_image_id);
        serviceNameTv = (TextView) mainView.findViewById(R.id.service_name_tv_id);
        Button submit = (Button) mainView.findViewById(R.id.submit);
        submit.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (targetTv != null)
                {
                    targetTv.setText(inputEt.getText().toString().trim());
                }
                serviceBean.setValue(inputEt.getText().toString().trim());
                CommonInputPopWindow.this.dismiss();
            }
            
        });
        
        inputEt = (EditText) mainView.findViewById(R.id.input_et);
        
        initDisplayImageOptions();
        
        super.setContentView(mainView);
        
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    
    private void initDisplayImageOptions()
    {
         displayImageOptions = new DisplayImageOptions.Builder().build();
    }
    
    public void initView(ServiceBean serviceBean, TextView targetTv)
    {
        this.serviceBean = serviceBean;
        this.targetTv = targetTv;
        
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
        
        if (targetTv != null)
        {
            inputEt.setText(targetTv.getText());
        }
    }
}
