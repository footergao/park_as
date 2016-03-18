package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MyLifeCircleSimpleAdapter;
import com.hdzx.tenement.mine.vo.LifeCircleAddressVo;
import com.hdzx.tenement.ui.MainActivity;
import com.hdzx.tenement.utils.BeansUtil;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.vo.LifeCircleAddressBean;
import com.hdzx.tenement.widget.CityPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddLifeCricleActivity extends Activity implements IContentReportor,
        OnClickListener {

    private final String TAG = "MyLifeCircleActivity";

    private String GET_LIFE_CIRCLE = "getLifecircle";
    private String GET_LIFE_CIRCLE_BY_KEYWORD = "getLifecircleByKeyword";
    private String GET_MY_LIFE_CIRCLE = "getMyLifecircle";// 获取我的生活圈
    private String UPDATE_NOW_LIFE_CIRCLE = "updateNowLifecircle";// 更新当前生活圈

    private ListView listView;

    private TextView tv_title;

    private TextView tv_location;

    private EditText edt_search;

    /**
     * 地址选择器
     */
    private CityPicker cityPick;

    /**
     * 地址bean
     */
    private LifeCircleAddressBean addressBean = new LifeCircleAddressBean();

    /**
     * 定位后获得的生活圈
     */
    private List<LifeCircleAddressVo> addressVoList = new ArrayList<LifeCircleAddressVo>();

    private LinearLayout rl_location;

    private MyLifeCircleSimpleAdapter adapter;

    private String search;

    private ImageView backIV = null;

    private RelativeLayout lay_title;// 附近生活圈

    // 百度定位
    private LocationClient locationClient;
    private MyLocationListener locationListener = null;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_main_mine_add_circle);

        initView();
    }

    private void initView() {

        Log.v("gl", "lifecircleId===="
                + UserSession.getInstance().getUserBasic().getLifecircleId()
                + "");

        lay_title = (RelativeLayout) findViewById(R.id.lay_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.txt_my_life_circle);
        lay_title.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.lv_my_life_circle);
        tv_location = (TextView) findViewById(R.id.tv_now_position);

        edt_search = (EditText) findViewById(R.id.edt_search_life_circle);

        rl_location = (LinearLayout) findViewById(R.id.rl_location);


        backIV = (ImageView) this.findViewById(R.id.back_imageView);
        backIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        initBaiduLocation();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            int locType = location.getLocType();
            if (locType == BDLocation.TypeGpsLocation
                    || locType == BDLocation.TypeNetWorkLocation) {
                String cityName = location.getCity();// 市
                String provinceName = location.getProvince();// 省
                String districtName = location.getDistrict();// 区/县

                addressBean.setCity(cityName);
                addressBean.setProvince(provinceName);
                addressBean.setArea(districtName);
                addressBean.setLat(location.getLatitude() + "");
                addressBean.setLng(location.getLongitude() + "");
                if (cityName != null) {
                    stopPosition();
                    // currCityTV.setText(cityName);
                    tv_location.setText(provinceName + "-" + cityName + "-"
                            + districtName);
                    getLifeCircle();

                }
            }
        }
    }

    protected void getMyLifeCircle() {

        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        reqContent.setRequestTicket(true);

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
                Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_MY_LIFE_CIRCL.getValue());
        httpRequestEntity.setRequestCode(GET_MY_LIFE_CIRCLE);
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        HttpAsyncTask task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {

        String rtnCode = (String) responseContent
                .getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null) {
            if (this != null) {
                Toast.makeText(this, "网络异常，请稍后尝试", Toast.LENGTH_SHORT).show();
            }
        } else if (!Contants.ResponseCode.CODE_000000.equals(rtnCode)) {
            if (this != null) {

                String rtnMsg = (String) responseContent
                        .getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
                Toast.makeText(this, rtnMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (GET_LIFE_CIRCLE.equals(responseContent.getResponseCode())) {
                // 获取附近生活圈
                String jsonStr = responseContent.getDataJson().trim();
                Log.v(TAG, "query by location jsonStr:" + jsonStr);
                Gson gson = new Gson();
                addressVoList.clear();
                addressVoList = gson.fromJson(jsonStr,
                        new TypeToken<List<LifeCircleAddressVo>>() {
                        }.getType());

                showInfo();
            } else if (UPDATE_NOW_LIFE_CIRCLE.equals(responseContent
                    .getResponseCode())) {
                // 更新当前的生活圈
                String jsonStr = responseContent.getDataJson().trim();
                Log.v(TAG, "update now lifecircle jsonStr:" + jsonStr);

                // 获取用户全部信息
                UserBasic userBasicInfo = (UserBasic) BeansUtil.map2Bean(
                        (Map<String, String>) responseContent.getData(),
                        UserBasic.class);
                UserSession.getInstance().setUserBasic(userBasicInfo);

                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, intent);
                finish();

                Log.v(TAG,
                        "update getLifecircleId:"
                                + userBasicInfo.getLifecircleName());
            } else {

                String jsonStr = responseContent.getDataJson();
                Log.v(TAG, "query by key world addressVoList:" + jsonStr);
                Gson gson = new Gson();
                addressVoList.clear();
                addressVoList = gson.fromJson(jsonStr,
                        new TypeToken<List<LifeCircleAddressVo>>() {
                        }.getType());

                showInfo();
            }

        }
    }

    private void showInfo() {
        // TODO Auto-generated method stub
        adapter = new MyLifeCircleSimpleAdapter(addressVoList, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                LifeCircleAddressVo addressVo = addressVoList.get(position);

                Intent intent = new Intent();
                intent.setClass(AddLifeCricleActivity.this,
                        SelectCellActivity.class);
                intent.putExtra("addressVo", addressVo);
                startActivity(intent);
            }
        });
    }

    /**
     * 根据地址获取lifcircle
     */
    private void getLifeCircle() {

        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        reqContent.setRequestTicket(true);
        reqContent.appendData("operType", "008");// 008-小区选择使用，009-查询
        reqContent.appendData("province", addressBean.getProvince());
        reqContent.appendData("city", addressBean.getCity());
        if ("全部".equals(addressBean.getArea())) {
            reqContent.appendData("area", "");
        } else {
            reqContent.appendData("area", addressBean.getArea());
        }

        reqContent.appendData("lng", addressBean.getLng());
        reqContent.appendData("lat", addressBean.getLat());

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
                Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_LIFE_CIRCLE.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(GET_LIFE_CIRCLE);
        HttpAsyncTask task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        try {
            span = Integer.valueOf("1000");// 1000ms毫秒
        } catch (Exception e) {
        }
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        // option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        // option.setIsNeedLocationDescribe(true);//
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        // option.setIsNeedLocationPoiList(true);//
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationClient.setLocOption(option);
    }

    private void stopPosition() {
        if (locationClient != null) {
            locationClient.stop();
            locationClient.unRegisterLocationListener(locationListener);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        stopPosition();
    }


    private void initBaiduLocation() {
        locationClient = new LocationClient(this);
        registerLocation();
        initLocation();

        locationClient.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    private void registerLocation() {
        locationListener = new MyLocationListener();
        locationClient.registerLocationListener(locationListener);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_search:
                queryLifeCircle();
                break;
            case R.id.rl_location:
                Log.i(TAG, "onclick i am here");
                initPicker();
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                cityPick.showAtLocation(rl_location, Gravity.BOTTOM, 0, 0);
                break;

            default:
                break;
        }
    }

    protected void updateNowLifeCircle(String lifecircleId, String regionalId) {

        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        reqContent.setRequestTicket(true);

        reqContent.appendData("lifecircleId", lifecircleId);
        reqContent.appendData("regionalId", regionalId);

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
                Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.UPDATE_NOW_LIFE_CIRCLE.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(UPDATE_NOW_LIFE_CIRCLE);
        HttpAsyncTask task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);
    }

    private void initPicker() {

        if (cityPick == null) {
            cityPick = new CityPicker(AddLifeCricleActivity.this);
            cityPick.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (cityPick != null) {
                        // 设置透明度（这是窗体本身的透明度，非背景）
                        // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                        WindowManager.LayoutParams params = getWindow()
                                .getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                        cityPick.dismiss();
                    }
                }
            });

            cityPick.setLeftListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cityPick != null) {
                        cityPick.dismiss();
                    }
                }
            });

            cityPick.setRightListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cityPick != null) {
                        addressBean = cityPick.getLocation();
                        tv_location.setText(addressBean.getProvince() + "-"
                                + addressBean.getCity() + "-"
                                + addressBean.getArea());
                        getLifeCircle();
                        cityPick.dismiss();
                    }
                }
            });
        }

    }

    /**
     * TODO 根据关键字搜索生活圈
     */
    private void queryLifeCircle() {
        search = edt_search.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            return;
        }
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(CryptoTyepEnum.aes);
        reqContent.setRequestTicket(true);
        reqContent.appendData("keyword", search);
        reqContent.appendData("province", addressBean.getProvince());
        reqContent.appendData("city", addressBean.getCity());

        reqContent.appendData("lng", addressBean.getLng());
        reqContent.appendData("lat", addressBean.getLat());

        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent,
                Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_LIFE_CIRCLE_BY_KEYWORD.getValue());
        httpRequestEntity.setResponseDecryptoType(CryptoTyepEnum.aes);
        httpRequestEntity.setRequestCode(GET_LIFE_CIRCLE_BY_KEYWORD);
        HttpAsyncTask task = new HttpAsyncTask(this, this);
        task.execute(httpRequestEntity);

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_OK, intent);
        finish();

    }
}
