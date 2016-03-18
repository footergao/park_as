package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.vo.MineAddressVo;
import com.hdzx.tenement.utils.CloseKeyBoard;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.vo.LifeCircleAddressBean;
import com.hdzx.tenement.widget.CityPicker;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: hope chen
 * Date: 2015/12/24
 * Description: 我的地址编辑/新增界面
 */
public class MineAddressFormActivity extends Activity implements IContentReportor, View.OnClickListener {

    /* 地址簿操作类型 */
    public static final String OPER_TYPE_ADDNEW = "000";    // 新增
    public static final String OPER_TYPE_EDIT = "001";      // 编辑
    public static final String OPER_TYPE_DELETE = "002";    // 删除
    public static final String ADDRESS_SEPARATOR = "-";

    EditText etName;        // 姓名
    EditText etContact;     // 联系方式
    TextView tvArea;        // 省市区
    EditText etDetail;      // 详细地址

    private String operType;    // 操作类型
    private MineAddressVo address;

    private CityPicker cityPick;    // 地址选择器
    private LifeCircleAddressBean addressBean = new LifeCircleAddressBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        operType = bundle.getString(Contants.PROTOCOL_REQ_BODY_DATA.operType.name());
        address = (MineAddressVo) bundle.getSerializable(Contants.PROTOCOL_RESP_BODY.address.name());

        setContentView(R.layout.tenement_main_mine_address_form);
        initView();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.et_address_name);
        etContact = (EditText) findViewById(R.id.et_address_contact);
        tvArea = (TextView) findViewById(R.id.tv_address_area);
        etDetail = (EditText) findViewById(R.id.et_address_detail);
        if (operType.equals(OPER_TYPE_EDIT)) {
            ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.main_mine_address_edit);
        }
        /* 值设置 */
        if (address != null) {
            etName.setText(address.getAbLastuseName());
            etContact.setText(address.getAbLastuseContact());
            tvArea.setText(StringUtils.join(new String[]{address.getAbProvince(), address.getAbCity(), address.getAbArea()}, ADDRESS_SEPARATOR));
            etDetail.setText(address.getAbOther());

            addressBean.setProvince(address.getAbProvince());
            addressBean.setCity(address.getAbCity());
            addressBean.setArea(address.getAbArea());
        }
        /* 事件绑定 */
        findViewById(R.id.tv_address_area).setOnClickListener(this);
        findViewById(R.id.layout_saveAddr).setOnClickListener(this);
    }

    private void initPicker() {
        if (cityPick == null) {
            cityPick = new CityPicker(MineAddressFormActivity.this);
            cityPick.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (cityPick != null) {
                        // 设置透明度（这是窗体本身的透明度，非背景）
                        // alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
                        WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.alpha = 1.0f;
                        getWindow().setAttributes(params);
                        cityPick.dismiss();
                    }
                }
            });
            cityPick.setLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cityPick != null) {
                        cityPick.dismiss();
                    }
                }
            });
            cityPick.setRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cityPick != null) {
                        addressBean = cityPick.getLocation();
                        String[] area = {addressBean.getProvince(), addressBean.getCity(), addressBean.getArea()};
                        // 如果区为空，则不显示区，如北京市
                        if (StringUtils.isBlank(addressBean.getArea())) {
                        	
                        	addressBean = setLocation(addressBean.getProvince(), addressBean.getProvince(), addressBean.getCity());
                        	
                        	
                            area = new String[]{addressBean.getProvince(), addressBean.getCity()};
                        }
                        tvArea.setText(StringUtils.join(area, "-"));
                        cityPick.dismiss();
                    }
                }
            });
        }

    }
    
    public LifeCircleAddressBean setLocation(String mCurrentProviceName,String mCurrentCityName,String mCurrentAreaName){
		addressBean.setArea(mCurrentAreaName);
		addressBean.setCity(mCurrentCityName);
		addressBean.setProvince(mCurrentProviceName);
		addressBean.setLat("");
		addressBean.setLng("");
		return addressBean;
	}

    /**
     * http请求
     */
    private void makeRequest() {
        String promptPrefix = "请输入";
        if (StringUtils.isBlank(etName.getText())) {
            Toast.makeText(getApplicationContext(), promptPrefix + etName.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(etContact.getText())) {
            Toast.makeText(getApplicationContext(), promptPrefix + etContact.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }else if(!isMobileNO(etContact.getText().toString())){
        	  Toast.makeText(getApplicationContext(), promptPrefix + "正确的" +etContact.getHint(), Toast.LENGTH_SHORT).show();
        	  return;
        }
        if (StringUtils.isBlank(tvArea.getText()) || StringUtils.contains(tvArea.getText(), "全部")) {
            promptPrefix = "请选择";
            Toast.makeText(getApplicationContext(), promptPrefix + tvArea.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(etDetail.getText())) {
            Toast.makeText(getApplicationContext(), promptPrefix + etDetail.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }

        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), operType);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abId.name(), address == null ? null :
                String.valueOf((new BigDecimal(address.getAbId())).intValue()));        // 处理33.0带小数点问题，后台转型报错
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abLastuseName.name(), etName.getText().toString());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abLastuseContact.name(), etContact.getText().toString());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abProvince.name(), addressBean.getProvince());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abCity.name(), addressBean.getCity());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abArea.name(), addressBean.getArea());
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), null);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.arId.name(), null);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abOther.name(), etDetail.getText().toString());
        // SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.UPDATE_USER_ADDRESS.getValue());
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this, this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    /**
     * 处理http响应
     *
     * @param responseContent 响应内容
     */
    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        String message = "失败";
        if (StringUtils.isNotBlank(rtnCode)) {
            if (!rtnCode.equals(Contants.ResponseCode.CODE_000000)) {
                message = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            } else {
                message = "成功";
                finish();
            }
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                // 返回
                finish();
                break;
            case R.id.tv_address_area:
            	CloseKeyBoard.closeInputKeyBoard(this);
                // 选择省市区
                initPicker();
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.5f;
                getWindow().setAttributes(params);
                cityPick.showAtLocation(findViewById(R.id.layout_addrForm), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.layout_saveAddr:
                // 保存
                makeRequest();
                break;
            default:
                break;
        }
    }
    
    
    public static boolean isMobileNO(String mobiles){  
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Matcher m = p.matcher(mobiles);  
		return m.matches();  
		}
}
