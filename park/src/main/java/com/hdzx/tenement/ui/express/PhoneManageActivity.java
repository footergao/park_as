package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.swipemenulistview.SwipeMenu;
import com.hdzx.tenement.swipemenulistview.SwipeMenuCreator;
import com.hdzx.tenement.swipemenulistview.SwipeMenuItem;
import com.hdzx.tenement.swipemenulistview.SwipeMenuListView;
import com.hdzx.tenement.ui.common.NotePhoneDialog;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.DensityUtil;
import com.hdzx.tenement.vo.ExpressPhoneVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class PhoneManageActivity extends Activity implements IContentReportor {

    //等待动画
    private ProgressDialog progressDialog;

    //http
    private HttpAsyncTask httpAsyncTask;

    //http TAG
    private String TAG_GETPHONELIST = "getphonelist";

    private String TAG_DELPHONE = "delphone";

    private int listflag = 0;

    private SwipeMenuListView swipeMenuListView;

    private List<ExpressPhoneVo> expressPhoneVoList;

    private TextView textViewMyPhone;
    //手机管理adapter
    PhoneManageAdapter phoneManageAdapter;

    private ScrollView scrollview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_phonemanage);

        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.lv_phonenum);
        textViewMyPhone = (TextView) findViewById(R.id.txt_express_phonemanage_phonenum_myself);
        scrollview = (ScrollView) findViewById(R.id.swipescrollview);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        initView();
    }

    public void initView() {

        initData();

        initMessageCircleList();

        swipeMenuListView.setSwipeScrollView(scrollview);

        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpressPhoneVo expressPhoneVo = expressPhoneVoList.get(position);
                Intent intent = new Intent(view.getContext(), PhoneManageEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putInt("id", expressPhoneVo.getEnpId());
                bundle.putString("name", expressPhoneVo.getEnpName());
                bundle.putString("phone", expressPhoneVo.getEnpPhone());
                intent.putExtras(bundle);
                startActivityForResult(intent, Contants.CODE_EXPRESS_EDITPHONE);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取通知手机列表
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密

        //BODY
        reqContent.setRequestTicket(true);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_NOTE_PHONE.getValue());
        httpRequestEntity.setRequestCode(TAG_GETPHONELIST);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    /**
     * 删除通知手机
     *
     * @param position
     * @param EnpId
     * @param EnpName
     */
    private void delPhone(int position, int EnpId, String EnpName) {
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密
        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), "002");
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpId.name(), EnpId);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.enpName.name(), EnpName);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.SET_NOTE_PHONE.getValue());
        httpRequestEntity.setRequestCode(TAG_DELPHONE);
        httpRequestEntity.setUserData(position);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), PhoneManageActivity.this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    private void initMessageCircleList() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                //set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
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
                ExpressPhoneVo item = expressPhoneVoList.get(position);
                switch (index) {
                    case 0:
                        //删除通知手机
                        delPhone(position, item.getEnpId(), item.getEnpName());
                        progressDialog.show();
                        break;
                    default:
                        break;
                }
                return false;
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
            case R.id.lay_express_phonemanage_back:
                finish();
                break;
            case R.id.txt_express_phonemanage_add:
                //添加其他号码
                Intent intent = new Intent(this, PhoneManageAddActivity.class);
                startActivityForResult(intent, Contants.CODE_EXPRESS_ADDPHONE);
                break;
            case R.id.txt_express_phonemanage_why:
                NotePhoneDialog dialog = new NotePhoneDialog(getApplicationContext(), "1111111111");
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.CODE_EXPRESS_ADDPHONE && resultCode == Contants.CODE_EXPRESS_ADDPHONE) {
            //新增手机-刷新
            progressDialog.show();
            initData();
        } else if (requestCode == Contants.CODE_EXPRESS_EDITPHONE && resultCode == Contants.CODE_EXPRESS_EDITPHONE) {
            //新增手机-刷新
            progressDialog.show();
            initData();
        }
    }

    /**
     * http 回调
     *
     * @param responseContent
     */
    @Override
    public void reportBackContent(ResponseContentTamplate responseContent) {
        String rtnCode = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnCode.name());
        if ("".equals(rtnCode) || rtnCode == null) {
            Toast.makeText(getApplicationContext(), "返回为空", Toast.LENGTH_SHORT).show();
        } else if (!"000000".equals(rtnCode)) {
            String rtnMsg = (String) responseContent.getInMapHead(Contants.PROTOCOL_RESP_HEAD.rtnMsg.name());
            Toast.makeText(getApplicationContext(), rtnMsg, Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new Gson();

            if (responseContent.getResponseCode().equals(TAG_GETPHONELIST)) {
                //获取手机管理列表
                String jsonStr = responseContent.getDataJson();
                expressPhoneVoList = gson.fromJson(jsonStr, new TypeToken<List<ExpressPhoneVo>>() {
                }.getType());

                for (int i = 0; i < expressPhoneVoList.size(); i++) {
                    if (expressPhoneVoList.get(i).getEnpStatus().equals("000")) {
                        String phonenum = expressPhoneVoList.get(i).getEnpPhone();
                        textViewMyPhone.setText(phonenum.substring(0, 3) + "****" + phonenum.substring(phonenum.length() - 4, phonenum.length()));
                        expressPhoneVoList.remove(i);
                        break;
                    }

                }
                phoneManageAdapter = new PhoneManageAdapter(this.getApplicationContext(), expressPhoneVoList);
                swipeMenuListView.setAdapter(phoneManageAdapter);
//                setListViewHeight(swipeMenuListView);
                scrollview.smoothScrollTo(0, 0);

                progressDialog.dismiss();
            } else if (responseContent.getResponseCode().equals(TAG_DELPHONE)) {
                //删除手机
                progressDialog.dismiss();

                int position = (int) responseContent.getUserData();
                expressPhoneVoList.remove(position);
                phoneManageAdapter.notifyDataSetChanged();
            }
        }
    }
}
