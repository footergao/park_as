package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.HttpAsyncTask;
import com.hdzx.tenement.http.protocol.HttpRequestEntity;
import com.hdzx.tenement.http.protocol.IContentReportor;
import com.hdzx.tenement.http.protocol.RequestContentTemplate;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.mine.adaper.MineAddressListViewAdapter;
import com.hdzx.tenement.mine.vo.MineAddressVo;
import com.hdzx.tenement.swipemenulistview.SwipeMenu;
import com.hdzx.tenement.swipemenulistview.SwipeMenuCreator;
import com.hdzx.tenement.swipemenulistview.SwipeMenuItem;
import com.hdzx.tenement.swipemenulistview.SwipeMenuListView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DensityUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 我的地址簿
 */
public class MineAddressActivity extends Activity implements IContentReportor,
        View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String OPER_TYPE_LIMITED = "011";   // 由当前生活圈生成的地址
    public static final String OPER_TYPE_ALL = "009";       // 全部

    private static final String HTTP_TAG_LIST = "list";
    private static final String HTTP_TAG_DELETE = "delete";

    SwipeMenuListView swipeMenuListView;
    MineAddressListViewAdapter addressListAdapter = null;

    private boolean isEdit;

    private List<MineAddressVo> addressVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenement_main_mine_address);

        initView();
        initAddressListView();

        Bundle bundle = getIntent().getExtras();
        isEdit = bundle.getBoolean("isEdit", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeListRequest();
    }

    private void initView() {
        ((TextView) findViewById(R.id.txt_nav_title)).setText(R.string.main_mine_address_title);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.address_lv);
        swipeMenuListView.setSwipeScrollView(scrollView);

        /* 事件绑定 */
        findViewById(R.id.layout_addAddr).setOnClickListener(this);
        swipeMenuListView.setOnItemClickListener(this);
    }

    private void initAddressListView() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(R.color.green);
                editItem.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                editItem.setTitle(getString(R.string.modify));
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                // set item title
                deleteItem.setTitle(getString(R.string.delete));
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
                // deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        swipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                MineAddressVo address = addressVoList.get(position);
                switch (index) {
                    case 0:
                        // 编辑地址
                        Intent intent = new Intent(MineAddressActivity.this, MineAddressFormActivity.class);
                        intent.putExtra(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), MineAddressFormActivity.OPER_TYPE_EDIT);
                        intent.putExtra(Contants.PROTOCOL_RESP_BODY.address.name(), address);
                        startActivity(intent);
                        break;
                    case 1:
                        // 删除地址
                        makeDeleteRequest(address);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送请求：获取地址簿列表
     */
    private void makeListRequest() {
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
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), OPER_TYPE_ALL);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.lifecircleId.name(), lifeCycleId);
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.GET_USER_ADDRESS.getValue());
        httpRequestEntity.setRequestCode(HTTP_TAG_LIST);
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this, this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    /**
     * 发送请求：删除地址簿
     */
    private void makeDeleteRequest(MineAddressVo address) {
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), MineAddressFormActivity.OPER_TYPE_DELETE);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.abId.name(),
                String.valueOf((new BigDecimal(address.getAbId())).intValue()));
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST,
                Contants.PROTOCOL_COMMAND.UPDATE_USER_ADDRESS.getValue());
        httpRequestEntity.setRequestCode(HTTP_TAG_DELETE);
        httpRequestEntity.setUserData(address);
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
                if (responseContent.getResponseCode().equals(HTTP_TAG_LIST)) {
                    Gson gson = new Gson();
                    addressVoList = gson.fromJson(responseContent.getDataJson(),
                            new TypeToken<List<MineAddressVo>>() {
                            }.getType());
                    addressListAdapter = new MineAddressListViewAdapter(this, addressVoList);
                    swipeMenuListView.setAdapter(addressListAdapter);
                }
                if (responseContent.getResponseCode().equals(HTTP_TAG_DELETE)) {
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    // 删除界面上的
                    addressVoList.remove((MineAddressVo) responseContent.getUserData());
                    addressListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_nav_back:
                // 返回
                finish();
                break;
            case R.id.layout_addAddr:
                // 添加新地址
                Intent intent = new Intent(this, MineAddressFormActivity.class);
                intent.putExtra(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), MineAddressFormActivity.OPER_TYPE_ADDNEW);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MineAddressVo addressVo = (MineAddressVo) parent.getItemAtPosition(position);
        if (isEdit) {
            // 编辑地址
            Intent intent = new Intent(this, MineAddressFormActivity.class);
            intent.putExtra(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), MineAddressFormActivity.OPER_TYPE_EDIT);
            intent.putExtra(Contants.PROTOCOL_RESP_BODY.address.name(), addressVo);
            startActivity(intent);
        } else {
            // 选择
            addressListAdapter.setSelectedPosition(position);
            addressListAdapter.notifyDataSetChanged();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
//        bundle.putSerializable("ADDRESS", addressVo);
            bundle.putString("ADDRESS", new Gson().toJson(addressVo));
            intent.putExtras(bundle);     // 返回地址簿
            MineAddressActivity.this.setResult(RESULT_OK, intent);
            MineAddressActivity.this.finish();
        }
    }
}
