package com.hdzx.tenement.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.hdzx.tenement.R;
import com.hdzx.tenement.utils.BaiduLocationClient;

public class BaiduLocationTest extends Activity
{
    private LocationClient mLocationClient;
    private TextView LocationResult, ModeInfor;
    private Button startLocation;
    private RadioGroup selectMode, selectCoordinates;
    private EditText frequence;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private CheckBox checkGeoLocation;
    private MyLocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_baidu_location);
        mLocationClient = BaiduLocationClient.getInstance().getClient();

        LocationResult = (TextView) findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        ModeInfor = (TextView) findViewById(R.id.modeinfor);
        ModeInfor.setText("高精度定位模式下，会同时使用GPS、Wifi和基站定位，返回的是当前条件下精度最好的定位结果");
        frequence = (EditText) findViewById(R.id.frequence);
        checkGeoLocation = (CheckBox) findViewById(R.id.geolocation);
        startLocation = (Button) findViewById(R.id.addfence);
        startLocation.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                initLocation();

                if (startLocation.getText().equals("开启定位"))
                {
                    mLocationClient.start();// 定位SDK
                                            // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText("停止定位");
                }
                else
                {
                    mLocationClient.stop();
                    startLocation.setText("开启定位");
                }

            }
        });
        selectMode = (RadioGroup) findViewById(R.id.selectMode);
        selectCoordinates = (RadioGroup) findViewById(R.id.selectCoordinates);
        selectMode.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                String ModeInformation = null;
                switch (checkedId)
                {
                    case R.id.radio_hight:
                        tempMode = LocationMode.Hight_Accuracy;
                        ModeInformation = "高精度定位模式下，会同时使用GPS、Wifi和基站定位，返回的是当前条件下精度最好的定位结果";
                        break;
                    case R.id.radio_low:
                        tempMode = LocationMode.Battery_Saving;
                        ModeInformation = "低功耗定位模式下，仅使用网络定位即Wifi和基站定位，返回的是当前条件下精度最好的网络定位结果";
                        break;
                    case R.id.radio_device:
                        tempMode = LocationMode.Device_Sensors;
                        ModeInformation = "仅用设备定位模式下，只使用用户的GPS进行定位。这个模式下，由于GPS芯片锁定需要时间，首次定位速度会需要一定的时间";
                        break;
                    default:
                        break;
                }
                ModeInfor.setText(ModeInformation);
            }
        });
        selectCoordinates.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // TODO Auto-generated method stub
                switch (checkedId)
                {
                    case R.id.radio_gcj02:
                        tempcoor = "gcj02";// 国家测绘局标准
                        break;
                    case R.id.radio_bd09ll:
                        tempcoor = "bd09ll";// 百度经纬度标准
                        break;
                    case R.id.radio_bd09:
                        tempcoor = "bd09";// 百度墨卡托标准
                        break;
                    default:
                        break;
                }
            }
        });

        registerLocation();
    }

    private void registerLocation()
    {
        locationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(locationListener);
    }

    @Override
    protected void onStop()
    {
        mLocationClient.stop();
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(locationListener);
    }

    private void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        try
        {
            span = Integer.valueOf(frequence.getText().toString());
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(checkGeoLocation.isChecked());// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        // option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        // option.setIsNeedLocationDescribe(true);//
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        // option.setIsNeedLocationPoiList(true);//
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener
    {

        @Override
        public void onReceiveLocation(BDLocation location)
        {
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\ncityCode:");
            sb.append(location.getCityCode());
            sb.append("\ncity:");
            sb.append(location.getCity());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation)
            {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            }
            else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
            {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                // 运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            }
            else if (location.getLocType() == BDLocation.TypeOffLineLocation)
            {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            }
            else if (location.getLocType() == BDLocation.TypeServerError)
            {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkException)
            {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            }
            else if (location.getLocType() == BDLocation.TypeCriteriaException)
            {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            LocationResult.setText(sb.toString());
            Log.i("BaiduLocationApiDem", sb.toString());
        }

    }
}
