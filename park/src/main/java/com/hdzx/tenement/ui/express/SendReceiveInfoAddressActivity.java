package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.ExpressAddressVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class SendReceiveInfoAddressActivity extends Activity {

    /**
     * 上门地址listview
     */
    private ListView listViewAddressInfo;

    /**
     * 上门地址list数据
     */
    private List<ExpressAddressVo> expressAddressVoList;

    /**
     * 上次选中的位置
     */
    private int lastPosition;

    //地址adapter
    SendReceiveInfoAddressAdapter sendReceiveInfoAddressAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_sendreceiveinfo_address);

        listViewAddressInfo = (ListView) findViewById(R.id.lv_address);
        expressAddressVoList = getData();
        sendReceiveInfoAddressAdapter = new SendReceiveInfoAddressAdapter(this.getApplicationContext(), expressAddressVoList);
        listViewAddressInfo.setAdapter(sendReceiveInfoAddressAdapter);

        initView();
    }

    public void initView() {
        lastPosition = 0;

        listViewAddressInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //最后选中清空
                expressAddressVoList.get(lastPosition).setSelectstatus(0);
                expressAddressVoList.get(position).setSelectstatus(1);
                sendReceiveInfoAddressAdapter.notifyDataSetChanged();
                lastPosition = position;
            }
        });
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_sendreciveinfo_address_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 快递收发-地址测试数据
     */
    private List<ExpressAddressVo> getData() {

        List<ExpressAddressVo> data = new ArrayList<ExpressAddressVo>();
        ExpressAddressVo expressOrderVo = new ExpressAddressVo();
        expressOrderVo.setAddressalias("环湖苑 第一套");
        expressOrderVo.setAddressinfo("无锡市滨湖区xxxxx");
        expressOrderVo.setSelectstatus(1);

        ExpressAddressVo expressOrderVo2 = new ExpressAddressVo();
        expressOrderVo2.setAddressalias("环湖苑 第二套");
        expressOrderVo2.setAddressinfo("无锡市滨湖区xxxxx");
        expressOrderVo2.setSelectstatus(0);

        ExpressAddressVo expressOrderVo3 = new ExpressAddressVo();
        expressOrderVo3.setAddressalias("环湖苑 第三套");
        expressOrderVo3.setAddressinfo("无锡市滨湖区xxxxx");
        expressOrderVo3.setSelectstatus(0);

        data.add(expressOrderVo);
        data.add(expressOrderVo2);
        data.add(expressOrderVo3);

        return data;
    }

}
