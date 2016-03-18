package com.hdzx.tenement.community.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.widget.PopupWindow.OnDismissListener;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.HandleResult;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.common.vo.MediaDataHolder;
import com.hdzx.tenement.community.vo.ExtraInfo;
import com.hdzx.tenement.community.vo.ServiceBean;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.widget.LineBreakLayout;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * 
 * @author Jesley
 *
 */
public class CommonDynamicAcitivity extends Activity implements OnClickListener, IContentReportor
{
    private static final String HTTP_REQUEST_CODE_SUBMIT_ORDER = "http_request_code_submit_order";
    
    private static final String HTTP_REQUEST_CODE_UPLOAD_FILE = "http_request_code_upload_file";
    
    private int singleContainerHeight = 0;
    
    private int singleImageSize = 0;
    
    private DisplayMetrics displayMetrics = null;
    
    private ServiceBean serviceBean = null;
    
    private List<ServiceBean> currDisplaySubServiceBeanList = null;
    
    private ServiceBean currDisplayServiceBean = null;
    
    private LinearLayout choiseItemContainer = null;
    
    private LinearLayout serviceCompContainer = null;
    
    private Button serviceDisplayButton = null;
    
    private View releaseView = null;
    
    private TextView titleTv = null;
    
    private TextView subServiceTitleTv = null;
    
    private View commonContentContainer = null;
    
    private CommonDynamicPopWindow commonDynamicPopWindow = null;
    
    private CommonDateTimePicker commonDateTimePicker = null;
    
    private CommonInputPopWindow commonInputPopWindow = null;
    
    private HttpAsyncTask task = null;
    
    private MediaDataHolder mediaDataHolder = null;
    
    private ProgressDialog progressDialog = null;
    
    private OnClickListener mulitCompClickListener = new OnClickListener()
    {

        @Override
        @SuppressWarnings("unchecked")
        public void onClick(View v)
        {
            if (v instanceof CheckedTextView)
            {
                CheckedTextView ctv = (CheckedTextView) v;
                ctv.setChecked(!ctv.isChecked());
                
                ServiceBean serviceBean = (ServiceBean) v.getTag();
                if (ctv.isChecked())
                {
                    ctv.setTextColor(Color.RED);
                    Set<String> value = (Set<String>) serviceBean.getValue();
                    if (value == null)
                    {
                        value = new HashSet<String>();
                        serviceBean.setValue(value);
                    }
                    
                    value.add(ctv.getText().toString());
                }
                else
                {
                    ctv.setTextColor(Color.GRAY);
                    Set<String> value = (Set<String>) serviceBean.getValue();
                    if (value != null)
                    {
                        value.remove(ctv.getText().toString());
                    }
                }
            }
        }
        
    };
    
    private OnClickListener comPopupClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Object tag = v.getTag();
            if (tag != null && tag instanceof ServiceBean)
            {
                ServiceBean sb = (ServiceBean) tag;
                View holder = sb.getViewHolder();
                if (holder != null && holder instanceof TextView)
                {
                    initCommonPopWindow();
                    commonDynamicPopWindow.initView(sb, (TextView) holder);
                    commonDynamicPopWindow.setFocusable(true);
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 0.5f;
                    getWindow().setAttributes(params);
                    commonDynamicPopWindow.showAtLocation(commonContentContainer, Gravity.BOTTOM, 0, 0);
                }
            }
        }
        
    };
    
    private OnClickListener dateTimeClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Object tag = v.getTag();
            if (tag != null && tag instanceof ServiceBean)
            {
                ServiceBean sb = (ServiceBean) tag;
                View holder = sb.getViewHolder();
                if (holder != null && holder instanceof TextView)
                {
                    initCommonDateTimeWindow();
                    commonDateTimePicker.init(sb, (TextView) holder);
                    commonDateTimePicker.setFocusable(true);
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 0.5f;
                    getWindow().setAttributes(params);
                    commonDateTimePicker.showAtLocation(commonContentContainer, Gravity.BOTTOM, 0, 0);
                }
            }
        }
        
    };
    
    private OnClickListener inputClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Object tag = v.getTag();
            if (tag != null && tag instanceof ServiceBean)
            {
                ServiceBean sb = (ServiceBean) tag;
                View holder = sb.getViewHolder();
                if (holder != null && holder instanceof TextView)
                {
                    initCommonInputWindow();
                    commonInputPopWindow.initView(sb, (TextView) holder);
                    commonInputPopWindow.setFocusable(true);
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 0.5f;
                    getWindow().setAttributes(params);
                    commonInputPopWindow.showAtLocation(commonContentContainer, Gravity.BOTTOM, 0, 0);
                }
            }
        }
        
    };
    
    private OnClickListener buttonClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Object tag = v.getTag();
            if (tag != null && tag instanceof ServiceBean)
            {
                ServiceBean sb = (ServiceBean) tag;
                View holder = sb.getViewHolder();
                
                Intent intent = new Intent(CommonDynamicAcitivity.this, CommonMediaSelectedActivity.class);
                intent.putExtra("ServiceBean", sb);
                intent.putExtra("MediaDataHolder", mediaDataHolder);
                CommonDynamicAcitivity.this.startActivityForResult(intent, 0);
            }
        }
        
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.common_dynamic_layout);
        
        Intent intent = this.getIntent();
        if (intent != null)
        {
            serviceBean = (ServiceBean) intent.getSerializableExtra("ServiceBean");
        }
        
        initCommonValue();
        initView();
    }
    
    private void initCommonValue()
    {
        displayMetrics = this.getResources().getDisplayMetrics();
        singleContainerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, displayMetrics);
        singleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23, displayMetrics);
    }
    
    private void initCommonView()
    {
        titleTv = (TextView) this.findViewById(R.id.titile_tv);
        if (serviceBean != null)
        {
            titleTv.setText(serviceBean.getServiceName());
        }
        else
        {
            titleTv.setText("空");
        }
        
        subServiceTitleTv = (TextView) this.findViewById(R.id.sub_service_title_tv);
        
        ImageView backIv = (ImageView) this.findViewById(R.id.back_iv);
        backIv.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
            
        });
        
        serviceDisplayButton = (Button) this.findViewById(R.id.service_display_button);
        serviceDisplayButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (choiseItemContainer.getVisibility() == View.VISIBLE)
                {
                    choiseItemContainer.setVisibility(View.GONE);
                    serviceDisplayButton.setText("选择项目");
                }
                else
                {
                    choiseItemContainer.setVisibility(View.VISIBLE);
                    serviceDisplayButton.setText("隐藏项目");
                }
            }
            
        });
        
        releaseView = this.findViewById(R.id.release_view);
        
        releaseView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                submitOrder();
            }
            
        });
    }

    private void initView()
    {
        commonContentContainer = this.findViewById(R.id.common_content_root_id);
        choiseItemContainer = (LinearLayout) this.findViewById(R.id.choise_item_container_id);
        serviceCompContainer = (LinearLayout) this.findViewById(R.id.service_component_container_id);
        
        initCommonView();
        
        initUpArea();
    }
    
    @Override
    public void onBackPressed()
    {
        if (commonDynamicPopWindow != null && commonDynamicPopWindow.isShowing())
        {
            commonDynamicPopWindow.dismiss();
        }
        else if (commonDateTimePicker != null && commonDateTimePicker.isShowing())
        {
            commonDateTimePicker.dismiss();
        }
        else
        {
            deleteMediaDataHolder();
            super.onBackPressed();
        }
    }
    
    private void initUpArea()
    {
        if (serviceBean != null && serviceBean.getChildren() != null && !serviceBean.getChildren().isEmpty())
        {
            LinearLayout ll = null;
            LinearLayout.LayoutParams llLayoutParams = null;
            TextView tv = null;
            LinearLayout.LayoutParams tvLayoutParams = null;
            int llWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            int tvWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
            for (ServiceBean item : serviceBean.getChildren())
            {
                ll = new LinearLayout(this);
                llLayoutParams = new LinearLayout.LayoutParams(llWidth, llWidth);
                ll.setLayoutParams(llLayoutParams);
                ll.setGravity(Gravity.CENTER);
                ll.setTag(item);
                ll.setOnClickListener(this);
                
                tv = new TextView(this);
                tvLayoutParams = new LinearLayout.LayoutParams(tvWidth, tvWidth);
                tv.setLayoutParams(tvLayoutParams);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(14);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.common_gray_circle);
                tv.setText(item.getServiceName());
                
                ll.addView(tv);
                choiseItemContainer.addView(ll);
            }
            
            initServiceView(serviceBean.getChildren().get(0));
           
        }
    }
    
    private void initServiceView(ServiceBean serviceBean)
    {
        if (serviceBean != null)
        {
            currDisplayServiceBean = serviceBean;
            subServiceTitleTv.setText(currDisplayServiceBean.getServiceName());
            currDisplaySubServiceBeanList = currDisplayServiceBean.getChildren();
            initDownArea(currDisplaySubServiceBeanList);
        }
    }
    
    private void initDownArea(List<ServiceBean> serviceBeanList)
    {
        if (serviceBeanList != null && !serviceBeanList.isEmpty())
        {
            serviceCompContainer.removeAllViews();
            for (ServiceBean item : serviceBeanList)
            {
                if ("008".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeMultiSelectedComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else if ("009".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeJumpPageComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else if ("005".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeCommoBoxComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else if ("004".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeDateTimeComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else if ("004".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeSingleInputComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else if ("007".equals(item.getServiceCompType()))
                {
                    serviceCompContainer.addView(makeButtonComp(item));
                    serviceCompContainer.addView(makeEmptyComp());
                }
                else
                {
                    System.out.println("ServiceCompType=" + item.getServiceCompType() + ", name=" + item.getServiceName());
                }
            }
        }
    }
    
    private View makeEmptyComp()
    {
        LinearLayout.LayoutParams viewLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25);
        View view = new View(this);
        view.setLayoutParams(viewLParams);
        
        return view;
    }
    
    private View makeMultiSelectedComp(ServiceBean serviceBean)
    {
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams llLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(llLayoutParams);
        ll.setBackgroundColor(Color.WHITE);
        ll.setTag(serviceBean);
        ll.setPadding(15, 20, 15, 20);
        
        TextView tv = new TextView(this);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(14);
        tv.setText(serviceBean.getServiceName());
        
        ll.addView(tv);
        
        if (serviceBean.getChildren() != null && !serviceBean.getChildren().isEmpty())
        {
            LineBreakLayout lbl = new LineBreakLayout(this);
            ViewGroup.LayoutParams lblLayoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lbl.setLayoutParams(lblLayoutParams);
            
            CheckedTextView ctv = null;
            
            for (ServiceBean item : serviceBean.getChildren())
            {
                ctv = new CheckedTextView(this);
                ctv.setText(item.getServiceName());
                ctv.setTextColor(Color.GRAY);
                ctv.setTextSize(12);
                ctv.setBackgroundResource(R.drawable.common_button_shap_selector);
                ctv.setTag(serviceBean);
//                ctv.setTag(item);
                
                ctv.setOnClickListener(mulitCompClickListener);
                
                lbl.addView(ctv);
            }
            
            ll.addView(lbl);
        }
        
        return ll;
    }
    
    private View makeSingleCommonComp(ServiceBean serviceBean)
    {
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, singleContainerHeight);
        rl.setLayoutParams(rlLayoutParams);
        rl.setGravity(Gravity.CENTER_VERTICAL);
        rl.setPadding(15, 0, 15, 0);
        rl.setBackgroundColor(Color.WHITE);
        rl.setTag(serviceBean);
        
        TextView tv = new TextView(this);
        tv.setTextSize(14);
        tv.setTextColor(Color.BLACK);
        tv.setText(serviceBean.getServiceName());
        RelativeLayout.LayoutParams tvLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rl.addView(tv, tvLayoutParams);
        
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.icon_arrow_right);
        int id = 0;
//        try
//        {
//            id = Integer.parseInt(serviceBean.getId());
//        }
//        catch (Exception e)
//        {
//            id = getRandomId(); 
//        }
//        iv.setId(id);
        if (serviceBean.getId() != null)
        {
            id = serviceBean.getId();
        }
        else
        {
            id = getRandomId(); 
        }
        iv.setId(id);
        RelativeLayout.LayoutParams ivLayoutParams = new RelativeLayout.LayoutParams(singleImageSize, singleImageSize);
        ivLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl.addView(iv, ivLayoutParams);
        
        TextView tv2 = new TextView(this);
        tv2.setTextSize(14);
        tv2.setTextColor(Color.GRAY);
        tv2.setText("");
        RelativeLayout.LayoutParams tv2LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2LayoutParams.addRule(RelativeLayout.LEFT_OF, id);
        tv2LayoutParams.setMarginEnd(20);
        rl.addView(tv2, tv2LayoutParams);
        
        serviceBean.setViewHolder(tv2);
        
        return rl;
    }
    
    private View makeJumpPageComp(ServiceBean serviceBean)
    {
        View view = makeSingleCommonComp(serviceBean);
        view.setOnClickListener(inputClickListener);
        
        return view;
    }
    
    private View makeCommoBoxComp(ServiceBean serviceBean)
    {
        View view = makeSingleCommonComp(serviceBean);
        view.setOnClickListener(comPopupClickListener);
        return view;
    }
    
    private View makeDateTimeComp(ServiceBean serviceBean)
    {
        View view = makeSingleCommonComp(serviceBean);
        view.setOnClickListener(dateTimeClickListener);
        return view;
    }
    
    private View makeSingleInputComp(ServiceBean serviceBean)
    {
        View view = makeSingleCommonComp(serviceBean);
        view.setOnClickListener(inputClickListener);
        return view;
    }
    
    private View makeButtonComp(ServiceBean serviceBean)
    {
        View view = makeSingleCommonComp(serviceBean);
        view.setOnClickListener(buttonClickListener);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        ServiceBean seviceBean = (ServiceBean) v.getTag();
        if (seviceBean != currDisplayServiceBean)
        {
            initServiceView(seviceBean);
        }
    }
    
    private int getRandomId()
    {
        Random random  = new Random();
        random.setSeed((new Date()).getTime());
        return random.nextInt(1000);
    }
    
    private void initCommonPopWindow()
    {
        if (commonDynamicPopWindow == null)
        {
            commonDynamicPopWindow = new CommonDynamicPopWindow(this);
            commonDynamicPopWindow.setOnDismissListener(new OnDismissListener()
            {
                @Override
                public void onDismiss()
                {
                    if (commonDynamicPopWindow != null)
                    {
                        // 设置透明度（这是窗体本身的透明度，非背景）
                        // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                }
            });
            
            commonDynamicPopWindow.setSingleHeight(singleContainerHeight);
        }
    }
    
    private void initCommonDateTimeWindow()
    {
        if (commonDateTimePicker == null)
        {
            commonDateTimePicker = new CommonDateTimePicker(this);
            commonDateTimePicker.setOnDismissListener(new OnDismissListener()
            {
                @Override
                public void onDismiss()
                {
                    if (commonDateTimePicker != null)
                    {
                        // 设置透明度（这是窗体本身的透明度，非背景）
                        // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                }
            });
        }
    }
    
    private void initCommonInputWindow()
    {
        if (commonInputPopWindow == null)
        {
            commonInputPopWindow = new CommonInputPopWindow(this);
            commonInputPopWindow.setOnDismissListener(new OnDismissListener()
            {
                @Override
                public void onDismiss()
                {
                    if (commonInputPopWindow != null)
                    {
                        // 设置透明度（这是窗体本身的透明度，非背景）
                        // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                    }
                }
            });
        }
    }
    
    private HandleResult checkStringCommon(ServiceBean serviceBean, Map<String, Object> paraMap)
    {
        HandleResult result = new HandleResult();
        String value = (String) serviceBean.getValue();
        if (value == null || "".equals(value.trim()))
        {
            result.setMessage("请输入/选择" + serviceBean.getServiceName());
        }
        else
        {
            paraMap.put(serviceBean.getMappingKey(), value);
        }
        
        return result;
    }
    
    private HandleResult checkMultiSelectedCommon(ServiceBean serviceBean, Map<String, Object> paraMap)
    {
        HandleResult result = new HandleResult();
        
        Object value = serviceBean.getValue();
        String str = null;
        if (value != null && (value instanceof Set))
        {
            Set<String> selectedSet = (Set<String>) value;
            if (selectedSet.size() > 0)
            {
                for (String entity : selectedSet)
                {
                    if (str == null)
                    {
                        str = entity;
                    }
                    else
                    {
                        str = str + "," + entity;
                    }
                }
            }
        }
        
        if (str == null)
        {
            result.setMessage("请选择" + serviceBean.getServiceName());
        }
        else
        {
            paraMap.put(serviceBean.getMappingKey(), str);
        }
        
        return result;
    }
    
    private HandleResult checkExtraInfo(ServiceBean serviceBean, Map<String, Object> paraMap)
    {
        HandleResult result = new HandleResult();
        
        return result;
    }
    
    private boolean checkOrder(Map<String, Object> paraMap)
    {
        HandleResult result = null;
        paraMap.put("serviceId", currDisplayServiceBean.getId());
        paraMap.put("serviceName", currDisplayServiceBean.getServiceName());
        paraMap.put("soType", "000");
        
        for (ServiceBean item : currDisplayServiceBean.getChildren())
        {
            
            switch(item.getMappingKey())
            {
                case "serviceAddress":
                case "serviceTime":
                case "serviceTimeType":
                case "serviceLevel":
                case "serviceCostType":
                    result = checkStringCommon(item, paraMap);
                    break;
                    
                case "serviceContent":
                    result = checkMultiSelectedCommon(item, paraMap);
                    break;
                    
                default:
                    result = checkExtraInfo(item, paraMap);
                    break;
            }
            
            if (result.isFail())
            {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
                return result.isSucess();
            }
        }
        
        return result.isSucess();
    }
    
    private void submitOrder()
    {
        if (currDisplayServiceBean != null && currDisplayServiceBean.getChildren() != null && !currDisplayServiceBean.getChildren().isEmpty())
        {
            Map<String, Object> paraMap = new HashMap<String, Object>();

            if (checkOrder(paraMap))
            {
                if (mediaDataHolder != null)
                {
                    if (mediaDataHolder.getUri() != null)
                    {
                        makeUploadFileRequest(paraMap);
                    }
                    else
                    {
                        makeExtraInfo(paraMap, null);
                        makeSubmitOrderRequest(paraMap);
                    }
                }
                else
                {
                    makeSubmitOrderRequest(paraMap);
                }
            }
            
            
        }
    }
    
    private void makeSubmitOrderRequest(Map<String, Object> paraMap)
    {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        reqContent.setRequestTicket(true);
        reqContent.appendData(paraMap);

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SUBMIT_ORDER.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(HTTP_REQUEST_CODE_SUBMIT_ORDER);
        task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            mediaDataHolder = (MediaDataHolder) data.getParcelableExtra("MediaDataHolder");
        }
    }
    
    private void makeUploadFileRequest(Object userData)
    {
        if (mediaDataHolder != null && mediaDataHolder.getUri() != null)
        {
            String filePath = mediaDataHolder.getUri().getPath();
            int index = filePath.lastIndexOf(".");
            String suffix = filePath.substring(index + 1);
            File f = new File(filePath);
            String fileName = f.getName();
            String contentType = null;
            if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.IMAGE)
            {
                if ("jpg".equalsIgnoreCase(suffix)
                        || "jpe".equalsIgnoreCase(suffix)
                        || "jpeg".equalsIgnoreCase(suffix))
                {
                    contentType = "image/jpeg";
                }
                else
                {
                    contentType = "image/" + suffix;
                }
            }
            else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO)
            {
                contentType = "audio/" + suffix;
            }
            else if (mediaDataHolder.getType() == Contants.MEDIA_TYPE.AUDIO)
            {
                if ("mp4".equalsIgnoreCase(suffix))
                {
                    contentType = "video/mpeg4";
                }
            }
            
            
            try
            {
                RequestParams rp = new RequestParams();
                rp.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), UserSession.getInstance().getSessionId());
                String secret = AESUtils.encrypt(UserSession.getInstance().getAesKey(), UserSession.getInstance().getSessionId());
                rp.put("secret", secret);
                rp.put("myfile", f, contentType, fileName);
                
                RequestContentTemplate reqContent = new RequestContentTemplate();
                reqContent.setRequestTicket(true);

                HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.UPLOAD_FILE.getValue());
                httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
                httpRequestEntity.setRequestParams(rp);
                httpRequestEntity.setRequestCode(HTTP_REQUEST_CODE_UPLOAD_FILE);
                httpRequestEntity.setHasData(false);
                httpRequestEntity.setUserData(userData);
                task = new HttpAsyncTask(this, this);
                task.execute(httpRequestEntity);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void makeExtraInfo(Map<String, Object> paraMap, String url)
    {
        if (mediaDataHolder != null)
        {
            List<ExtraInfo> extraInfoList = null;
            ExtraInfo extraInfo = null;
            String text = mediaDataHolder.getText().trim();
            if (text != null && !"".equals(text))
            {
                extraInfo = new ExtraInfo();
                extraInfo.setSoaKey("文本");
                extraInfo.setSoaValue(text);
                if (extraInfoList == null)
                {
                    extraInfoList = new ArrayList<ExtraInfo>();
                }
                extraInfoList.add(extraInfo);
            }
            
            if (url != null)
            {
                extraInfo = new ExtraInfo();
                switch(mediaDataHolder.getType())
                {
                    case IMAGE:
                        extraInfo.setSoaKey("图片");
                        break;
                        
                    case AUDIO:
                        extraInfo.setSoaKey("音频");
                        break;
                        
                    case VEDIO:
                        extraInfo.setSoaKey("视频");
                        break;
                        
                    default:
                        extraInfo.setSoaKey("未知");
                        break;
                }
                extraInfo.setSoaValue(url);
                
                if (extraInfoList == null)
                {
                    extraInfoList = new ArrayList<ExtraInfo>();
                }
                extraInfoList.add(extraInfo);
            }
            
            if (extraInfoList != null)
            {
                paraMap.put("expandPro", extraInfoList);
            }
        }
    }
    
    private void deleteMediaDataHolder()
    {
        if (mediaDataHolder != null)
        {
            if (mediaDataHolder.isDelete())
            {
                File f = new File(mediaDataHolder.getUri().getPath());
                if (f.exists())
                {
                    f.delete();
                }
            }
            
            mediaDataHolder = null;
        }
    }
    
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void reportBackContent(ResponseContentTamplate responseContent)
    {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null)
        {
            Toast.makeText(this, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
        }
        else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode))
        {
            String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
        }
        else
        {
            String errMsg = responseContent.getErrorMsg();
            if (errMsg != null && !"".equals(errMsg))
            {
                Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (HTTP_REQUEST_CODE_SUBMIT_ORDER.equals(responseContent.getResponseCode()))
                {
                    Toast.makeText(this, "订单提交成功，跳转订单确认、支付页面", Toast.LENGTH_SHORT).show();
                }
                else if (HTTP_REQUEST_CODE_UPLOAD_FILE.equals(responseContent.getResponseCode()))
                {
                    Object data = responseContent.getData();
                    if (data != null && data instanceof List)
                    {
                        List<String> uriList = (List<String>) data;
                        if (uriList.isEmpty())
                        {
                            Toast.makeText(this, "文件上传失败", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Map<String, Object> paraMap = (Map<String, Object>) responseContent.getUserData();
                            makeExtraInfo(paraMap, uriList.get(0));
                            makeSubmitOrderRequest(paraMap);
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "文件上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
