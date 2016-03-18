package com.hdzx.tenement.ui.express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.hdzx.tenement.R;
import com.hdzx.tenement.http.protocol.*;
import com.hdzx.tenement.pulltorefresh.library.ILoadingLayout;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshBase;
import com.hdzx.tenement.pulltorefresh.library.PullToRefreshListView;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.vo.ExpressOrderVo;

import java.util.List;

/**
 * Created by anchendong on 15/7/27.
 */
public class WaitReceiveActivity extends Activity implements IContentReportor {

    private PullToRefreshListView listViewOrder;

    private List<ExpressOrderVo> ExpressOrderVoList;

    private WaitReceiveAdapter waitReceiveAdapter;

    //http
    private HttpAsyncTask httpAsyncTask;

    //http TAG
    private String TAG_GETEXPRESSORDER = "getxpressorder";

    //分页索引，从0开始
    private int pageIndex = 0;
    //分页大小
    private int pageCount = 10;

    //等待动画
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_express_waitreceive);
        listViewOrder = (PullToRefreshListView) findViewById(R.id.lv_express_order);
        progressDialog =new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        initView();
    }

    private void initView() {
        listViewOrder.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout loadingLayout = listViewOrder.getLoadingLayoutProxy(true, false);
        loadingLayout.setPullLabel("下拉刷新");
        loadingLayout.setRefreshingLabel("加载中……");
        loadingLayout.setReleaseLabel("释放刷新当前画面");

        ILoadingLayout loadingLayout2 = listViewOrder.getLoadingLayoutProxy(false, true);
        loadingLayout2.setPullLabel("上拉加载更多");
        loadingLayout2.setRefreshingLabel("加载中……");
        loadingLayout2.setReleaseLabel("释放加载更多");

        getData(0);

        listViewOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = ExpressOrderVoList.size();
                getData(1);
                new FinishRefresh().execute();
            }
        });

        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * 初始化数据
     *
     * @param stauts 0-刷新 1-下拉刷新 2-上推加载
     */
    private void getData(int stauts) {
        //获取通知手机列表
        //HEARD
        RequestContentTemplate reqContent = new RequestContentTemplate();
        reqContent.setEncryptoType(Contants.CryptoTyepEnum.aes);//请求使用AES加密

        //BODY
        reqContent.setRequestTicket(true);
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.operType.name(), "000");
        if (stauts == 0 || stauts == 1) {
            reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageIndex.name(), 0);
        } else {
            reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageIndex.name(), pageIndex);
        }
        reqContent.appendData(Contants.PROTOCOL_REQ_BODY_DATA.pageCount.name(), pageCount);

        //SEND
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(reqContent, Contants.SERVER_HOST, Contants.PROTOCOL_COMMAND.GET_EXPRESS_ORDER.getValue());
        httpRequestEntity.setRequestCode(TAG_GETEXPRESSORDER);
        httpRequestEntity.setUserData(stauts);
        httpRequestEntity.setResponseDecryptoType(Contants.CryptoTyepEnum.aes);//返回使用AES密钥解密
        httpAsyncTask = new HttpAsyncTask(getApplicationContext(), this);
        httpAsyncTask.execute(httpRequestEntity);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_express_waitreceive_back:
                finish();
                break;
            default:
                break;
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
            progressDialog.dismiss();
            Gson gson = new Gson();
            if (responseContent.getResponseCode().equals(TAG_GETEXPRESSORDER)) {
                String jsonStr = responseContent.getDataJson();

//                //获取订单列表
//                if (responseContent.getUserData() == 0) {
//                    //首次加载
//                    ExpressOrderVoList = gson.fromJson(jsonStr, new TypeToken<List<ExpressOrderVo>>() {
//                    }.getType());
//                    waitReceiveAdapter = new WaitReceiveAdapter(this.getApplicationContext(), ExpressOrderVoList);
//                    listViewOrder.setAdapter(waitReceiveAdapter);
//                } else if (responseContent.getUserData() == 1) {
//                    //刷新
//
//                } else if (responseContent.getUserData() == 2) {
//                    //加载
//                    List<ExpressOrderVo> addexpressorderList = gson.fromJson(jsonStr, new TypeToken<List<ExpressOrderVo>>() {
//                    }.getType());
//                    ExpressOrderVoList.addAll(addexpressorderList);
//                    new FinishRefresh().execute();
//                }
            }
        }
    }

    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listViewOrder.onRefreshComplete();
        }
    }
}
