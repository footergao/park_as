package com.hdzx.tenement.community.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.community.adapter.ConvenPhoneAdapter;
import com.hdzx.tenement.community.vo.ConvenPhone;
import com.hdzx.tenement.community.vo.ConvenPhoneSet;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.utils.Contants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: hope chen
 * Date: 2015/12/23
 * Description: 便民电话界面
 */
public class CommonConvenPhoneListActivity extends Activity implements IContentReportor, View.OnClickListener {

    List<String> groupItem = new ArrayList<String>();
    List<List<ConvenPhone>> childItem = new ArrayList<List<ConvenPhone>>();
    private ExpandableListView epListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_conven_phone_list);

        initView();
        makeRequest();
    }

    /**
     * 界面元素初始化
     */
    private void initView() {
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.txt_phone);

        epListView = (ExpandableListView) findViewById(R.id.phone_epListView);
        epListView.setDividerHeight(2);
        epListView.setGroupIndicator(null);
        epListView.setClickable(true);
    }

    /**
     * 发送http请求
     */
    private void makeRequest() {
        if (UserSession.getInstance().getUserBasic() == null) {
            Toast.makeText(getApplicationContext(), "请登录后再试！", Toast.LENGTH_SHORT).show();
            return;
        }
        Integer lifeCycleId = UserSession.getInstance().getUserBasic().getLifecircleId();
        if (lifeCycleId == null) {
            Toast.makeText(getApplicationContext(), "请先设置您的生活圈！", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), lifeCycleId);
        // SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_CONVEN_PHONE.getValue());
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this, this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if (StringUtils.isBlank(rtnCode)) {
            Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT).show();
        } else {
            if (!rtnCode.equals(Contants.ResponseCode.CODE_000000)) {
                String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
                Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                List<ConvenPhoneSet> phoneSetList = gson.fromJson(responseContent.getDataJson(),
                        new TypeToken<List<ConvenPhoneSet>>() {
                        }.getType());

                drawExpandableListView(phoneSetList);
            }
        }
    }

    /**
     * 绘制ExpandableListView
     *
     * @param phoneSetList 电话集合
     */
    private void drawExpandableListView(List<ConvenPhoneSet> phoneSetList) {
        setGroupData(phoneSetList);
        setChildGroupData(phoneSetList);
        ConvenPhoneAdapter phoneAdapter = new ConvenPhoneAdapter(groupItem, childItem);
        phoneAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        epListView.setAdapter(phoneAdapter);
        // 展开全部
        for (int i = 0; i < groupItem.size(); i++) {
            epListView.expandGroup(i);
        }
        // 不能点击收缩
        epListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }

    /**
     * 设置父节点数据
     *
     * @param phoneSetList 电话集合
     */
    private void setGroupData(List<ConvenPhoneSet> phoneSetList) {
        for (ConvenPhoneSet phoneSet : phoneSetList) {
            groupItem.add(phoneSet.getPhoneTypeName());
        }
    }

    /**
     * 设置子节点数据
     *
     * @param phoneSetList 电话集合
     */
    private void setChildGroupData(List<ConvenPhoneSet> phoneSetList) {
        for (ConvenPhoneSet phoneSet : phoneSetList) {
            childItem.add(phoneSet.getConveniencePhones());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                CommonConvenPhoneListActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
