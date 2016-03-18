package com.hdzx.tenement.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.alibaba.mobileim.YWIMKit;
import com.google.gson.Gson;
import com.hdzx.tenement.aliopenimi.sample.LoginSampleHelper;
import com.hdzx.tenement.common.UserBasic;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.http.protocol.ResponseContentTamplate;
import com.hdzx.tenement.ui.common.LoginActivity;
import com.hdzx.tenement.utils.AESUtils;
import com.hdzx.tenement.utils.CommonUtil;
import com.hdzx.tenement.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * User: hope chen
 * Date: 2015/11/25
 * Description: 自定义cordova插件
 */
public class TenementPlugin extends CordovaPlugin {

    private static final String LOG_TAG = "TenementPlugin";
    public static final String HTTP_URL_FORMAT = "%s/%s/%s;jsessionid=%s";
    private Activity activity;
    private CordovaWebView webView;
    private CallbackContext myCallbackContext;
    private static final Map<String, CallbackContext> callbacks = new HashMap<String, CallbackContext>();

    private static final String ACTION_LOGIN = "LOGIN";                     // 登录
    private static final String ACTION_IMAGEHOST = "IMAGEHOST";             // 获取imagehost
    private static final String ACTION_ACCESS_TICKET = "ACCESS_TICKET";     // 票据
    private static final String ACTION_USERINFO = "USERINFO";               // 用户信息
    private static final String ACTION_ACTIVITY = "ACTIVITY";               // 调用原生activity
    private static final String ACTION_LOG = "LOG";                         // 打印日志
    private static final String ACTION_BACKTONATURE = "BACKTONATURE";       // 返回上一个原生页面
    private static final String ACTION_REMOTE_REQUEST = "REMOTE_REQUEST";   // 发送请求到远程服务器
    private static final String ACTION_BROADCAST = "BROADCAST";             // 广播监听
    private static final String ACTION_SEND_BROADCAST = "SEND_BROADCAST";   // 广播监听
    private static final String ACTION_PHONE_DIAL = "PHONEDIAL";            // 拨号
    private static final String ACTION_CHATTING = "CHATTING";               // 阿里百川聊天

    public static final String CALLBACK_KEY = "CallbackContext";

    BroadcastReceiver receiver;

    private Gson gson = new Gson();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.d(LOG_TAG, "initialize");
        super.initialize(cordova, webView);
        this.activity = cordova.getActivity();
        this.webView = webView;

    }

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        // action为空，则跳出
        if (StringUtils.isEmpty(action)) {
            return false;
        }
        // 跳转登录页
        if (action.equals(ACTION_LOGIN)) {
            Intent intent = new Intent(this.cordova.getActivity(), LoginActivity.class);
            String key = saveCallbackContext(callbackContext);
            intent.putExtra(CALLBACK_KEY, key);
            this.cordova.getActivity().startActivity(intent);
            return true;
        }
        // 获取imagehost
        if (action.equals(ACTION_IMAGEHOST)) {
            UserSession userSession = UserSession.getInstance();
            if (userSession != null) {
                callbackContext.success(userSession.getImageHost());
            } else {
                callbackContext.error("error!");
            }
            return true;
        }
        // 获取用户票据
        if (action.equals(ACTION_ACCESS_TICKET)) {
            UserSession userSession = UserSession.getInstance();
            if (userSession != null) {
                callbackContext.success(userSession.getAccessTicket());
            } else {
                callbackContext.error("Not logined!");
            }
            return true;
        }
        // 获取用户信息
        if (action.equals(ACTION_USERINFO)) {
            UserBasic userBasic = UserSession.getInstance().getUserBasic();
            if (userBasic != null) {
                JSONObject jsonObject = new JSONObject(gson.toJson(userBasic));
                callbackContext.success(jsonObject);
            } else {
                callbackContext.error("Not logined!");
            }
            return true;
        }
        // Log日志
        if (action.equals(ACTION_LOG)) {
            JSONObject obj = args.optJSONObject(0);
            Log.i(LOG_TAG, "appid = " + obj.optString("appid"));
            Log.i(LOG_TAG, "appversion = " + obj.optString("appversion"));
            Log.i(LOG_TAG, "content = " + obj.optString("content"));
            return true;
        }
        // 返回原生页面
        if (action.equals(ACTION_BACKTONATURE)) {
            this.cordova.getActivity().finish();
            callbackContext.success();
            return true;
        }
        // 拨号
        if (action.equals(ACTION_PHONE_DIAL)) {
            final String phonenumber = args.getString(0);
            if (StringUtils.isNotBlank(phonenumber)) {
                if (PhoneNumberUtils.isGlobalPhoneNumber(phonenumber)) {
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phonenumber));
                            cordova.getActivity().startActivity(intent);
                            callbackContext.success();
                        }
                    });
                } else {
                    callbackContext.error("Invalid Phonenumber");
                }
            } else {
                callbackContext.error("Empty Phonenumber");
            }
            return true;
        }
        // 调用原生activity
        if (action.equals(ACTION_ACTIVITY)) {
            this.myCallbackContext = callbackContext;
            final CordovaArgs finalArgs = args;
            Log.v("gl", "getBaseArgs=="+finalArgs.getBaseArgs());
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run(){
                    try {
                        // 启动新的原生Activity
                        Intent intent = new Intent();
                        intent.setClass(cordova.getActivity().getApplicationContext(), Class.forName(finalArgs.optString(0)));
                        if (!finalArgs.getBaseArgs().isNull(1)) { // 带参数的跳转
                            // 接收的参数
                        	JSONObject object;
                        	boolean arrayb= finalArgs.getBaseArgs().opt(1) instanceof JSONArray;
                        	boolean objectb= finalArgs.getBaseArgs().opt(1) instanceof JSONObject;
                        	Log.v("gl", "arrayb=="+arrayb+"===objectb"+objectb);
                        	if(arrayb){
                        		JSONArray array = (JSONArray) finalArgs.getBaseArgs().optJSONArray(1);
                        		
                        		intent.putExtra("list", array.toString());
                        		
                        	}else{
                        		object = finalArgs.getBaseArgs().optJSONObject(1);
                        		intent.putExtras(JSONObjectToBunble(object)); 
                        		 // 接收的参数
                                Log.v("gl", "jsondata=="+JSONObjectToBunble(object));
                        	}
                            cordova.startActivityForResult(TenementPlugin.this, intent, 1);
                        } else { // 不带参数的跳转
                            cordova.getActivity().startActivity(intent);
                            myCallbackContext.success();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        myCallbackContext.error("Invalid Activity");
                    } 
                }
            });
            return true;
        }
        // 广播监听
        if (action.equals(ACTION_BROADCAST)) {
            this.myCallbackContext = callbackContext;
            final CordovaArgs finalArgs = args;
            // We need to listen to power events to update battery status
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(finalArgs.optString(0));
            if (this.receiver == null) {
                this.receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (finalArgs.optString(0).equals(Contants.BROADCAST_ACTION.UPDATE_ORDER.getValue())) {
                            updateOrder(intent);
                        } else {
                            myCallbackContext.error("Invalid NativeCallJs");
                        }
                    }
                };
                webView.getContext().registerReceiver(this.receiver, intentFilter);
            }

            // Don't return any result now, since status results will be sent when events come in from broadcast receiver
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        // 发送请求到远程服务器
        if (action.equals(ACTION_REMOTE_REQUEST)) {
            callbackContext.disable();
            final String callbackKey = saveCallbackContext(callbackContext);
            JSONObject obj = args.optJSONObject(0);
            String method = obj.optString("method");
            String param = obj.optString("param");
            Map paramMap = gson.fromJson(param, Map.class);
            String url = String.format(HTTP_URL_FORMAT, Contants.SERVER_HOST, Contants.REMOTE_DOMAIN, method, UserSession.getInstance().getSessionId());
            KJHttp kjh = new KJHttp();
            kjh.cleanCache();
            HttpParams params = new HttpParams();
            params.putJsonParams(encodeRequest(paramMap));
            kjh.jsonPost(url, params, new HttpCallBack() {
                @Override
                public void onSuccess(String response) {
                    CallbackContext callback = retriveCallbackContext(callbackKey);
                    callback.enable();
                    KJLoger.debug("log:" + response);
                    ResponseContentTamplate responseContent = gson.fromJson(response, ResponseContentTamplate.class);
                    responseContent.parseContent4Json(response);
                    if (StringUtils.isBlank(responseContent.getErrorMsg())) {
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> data = new HashMap<>();
                        data.put("data", responseContent.getData());
                        map.put(Contants.PROTOCOL_CONTEN_KEY.head.name(), responseContent.getHead());
                        map.put(Contants.PROTOCOL_CONTEN_KEY.body.name(), data);
                        callback.success(gson.toJson(map));
                    } else {
                        callback.error(responseContent.getErrorMsg());
                    }
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    CallbackContext callback = retriveCallbackContext(callbackKey);
                    callback.enable();
                    KJLoger.debug("errorNo:" + errorNo);
                    KJLoger.debug("strMsg:" + strMsg);
                    callback.error(strMsg);
                }
            });
        }
        // 发送广播
        if (action.equals(ACTION_SEND_BROADCAST)) {
            if (args.isNull(1)) {
                return false;
            }
            JSONObject extras = args.optJSONObject(1);
            Map<String, String> extrasMap = new HashMap<>();

            if (extras != null && extras.names() != null) {
                JSONArray extraNames = extras.names();
                for (int i = 0; i < extraNames.length(); i++) {
                    String key = extraNames.getString(i);
                    String value = extras.getString(key);
                    extrasMap.put(key, value);
                }
            }
            sendBroadcast(args.optString(0), extrasMap);
            callbackContext.success();
            return true;
        }
        // 调用阿里百川聊天界面
        if (action.equals(ACTION_CHATTING)) {
            final String target = args.getString(0);
            Log.v("gl", "target=="+target);
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    YWIMKit mIMKit = LoginSampleHelper.getInstance().getIMKit();
                    Intent intent = mIMKit.getChattingActivityIntent(target);
                    cordova.getActivity().startActivity(intent);
                    callbackContext.success();
                }
            });
            return true;
        }
        return false;
    }

    // onActivityResult为第二个Activity执行完后的回调接收方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        PluginResult mPlugin;
        switch (resultCode) {    // resultCode为回传的标记，约定在新的原生Activity中回传的是RESULT_OK
            case Activity.RESULT_OK:
                Bundle bundle = intent.getExtras();
                // 新的原生Activity把值传回Cordova
                mPlugin = new PluginResult(PluginResult.Status.OK, BundleToJSONObject(bundle));
                break;
            default:
                mPlugin = new PluginResult(PluginResult.Status.ERROR, "resultCode should be 'RESULT_OK'");
                break;
        }
        mPlugin.setKeepCallback(false);
        myCallbackContext.sendPluginResult(mPlugin);
    }

    /**
     * 调用页面js更新订单信息
     *
     * @param intent the information
     */
    @JavascriptInterface
    private void updateOrder(Intent intent) {
//        myCallbackContext.disable();
//        final String callbackKey = saveCallbackContext(myCallbackContext);
        int orderId = intent.getIntExtra("orderId", 0);
//            int orderId = Integer.parseInt(StringUtils.defaultString(intent.getStringExtra("orderId"), "0"));

//        String js = "alert('" + orderId + "');";
//        injectJS(js);
//        String js = "testmm()";
//        injectJS(js);
//
//        js = "updateOrderStream('" + orderId + "')";
//        injectJS(js);
//        js = "updateHead('" + orderId + "')";
//        injectJS(js);
        PluginResult result = new PluginResult(PluginResult.Status.OK, orderId);
        result.setKeepCallback(true);
        myCallbackContext.sendPluginResult(result);

            /*String method = "/order/order.getOrderInfoById.json";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id", orderId);
            String url = String.format(HTTP_URL_FORMAT, Contants.SERVER_HOST, Contants.REMOTE_DOMAIN, method, UserSession.getInstance().getSessionId());
            KJHttp kjh = new KJHttp();
            kjh.cleanCache();
            HttpParams params = new HttpParams();
            params.putJsonParams(encodeRequest(paramMap));
            kjh.jsonPost(url, params, new HttpCallBack() {
                @Override
                public void onSuccess(String response) {
//                    CallbackContext callback = retriveCallbackContext(callbackKey);
//                    callback.enable();
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    KJLoger.debug("log:" + response);
                    ResponseContentTamplate responseContent = gson.fromJson(response, ResponseContentTamplate.class);
                    responseContent.parseContent4Json(response);
                    if (StringUtils.isBlank(responseContent.getErrorMsg())) {
                        if (StringUtils.isNotBlank(responseContent.getDataJson())) {
                            String js = "updateOrderStream('" + gson.toJson(responseContent.getData()) + "')";
//                        String js = "alert('Java methond callback() has been called.');";
                            injectJS(js);
                        }
                        result = new PluginResult(PluginResult.Status.OK);
//                        callback.success();
//                    } else {
//                        callback.error();
                    }
                    result.setKeepCallback(true);
                    myCallbackContext.sendPluginResult(result);
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    result.setKeepCallback(true);
                    myCallbackContext.sendPluginResult(result);
                }
            });*/
    }

    /**
     * 执行页面javascript
     *
     * @param js javascript
     */
    private void injectJS(final String js) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                webView.loadUrl("javascript:" + js);
            }
        });
    }

    /**
     * 发送广播
     *
     * @param action action
     * @param extras 参数
     */
    private void sendBroadcast(String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            intent.putExtra(key, value);
        }

        this.cordova.getActivity().sendBroadcast(intent);
    }

    private static synchronized String saveCallbackContext(CallbackContext callback) {
        String key = Long.toHexString(System.currentTimeMillis() + (long) (10000 * Math.random()));
        callbacks.put(key, callback);
        return key;
    }

    public static synchronized CallbackContext retriveCallbackContext(String key) {
        if (key == null)
            return null;
        return callbacks.remove(key);
    }

    /**
     * 组织请求报文，并加密
     *
     * @param paramMap 请求参数
     * @return 加密报文
     */
    private String encodeRequest(Map paramMap) {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        Map<String, Object> headMap = new HashMap<String, Object>();
        headMap.put(Contants.PROTOCOL_REQ_HEAD.appid.name(), Contants.APPID);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.sessionid.name(), UserSession.getInstance().getSessionId());
        headMap.put(Contants.PROTOCOL_REQ_HEAD.version.name(), Contants.VERSION);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.os.name(), Contants.OS_AND);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.appver.name(), CommonUtil.appVersion);
        headMap.put(Contants.PROTOCOL_REQ_HEAD.uuid.name(), CommonUtil.uuid);

        Map<String, Object> bodyMap = new HashMap<String, Object>();
        if (UserSession.getInstance().getAccessTicket() != null) {
            bodyMap.put(Contants.PROTOCOL_REQ_BODY.ticket.name(), UserSession.getInstance().getAccessTicket());
        }
        bodyMap.put(Contants.PROTOCOL_REQ_BODY.data.name(), paramMap);
        String bodyString = gson.toJson(bodyMap);
        String bodyEncodeString = AESUtils.encrypt(UserSession.getInstance().getAesKey(), bodyString);

        requestMap.put(Contants.PROTOCOL_CONTEN_KEY.head.name(), headMap);
        requestMap.put(Contants.PROTOCOL_CONTEN_KEY.body.name(), bodyEncodeString);
        return gson.toJson(requestMap);
    }

    /**
     * JSON对象转成Bundle，用于Activity之间传值；
     * 键和值都被转成String类型
     *
     * @param jsonObject
     * @return
     */
    private static Bundle JSONObjectToBunble(JSONObject jsonObject) {
        Bundle bundle = new Bundle();
        if (jsonObject == null) {
            return bundle;
        }
        Iterator<?> it = jsonObject.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            try {
                bundle.putString(key, jsonObject.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }

    /**
     * Bundle转成JSON对象，用于Activity之间传值；
     * 键被转成String类型,值被转成Object类型
     *
     * @param bundle
     * @return
     */
    private static JSONObject BundleToJSONObject(Bundle bundle) {
        JSONObject jsonObject = new JSONObject();
        if (bundle == null) {
            return jsonObject;
        }
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            try {
                jsonObject.put(key, bundle.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.cordova.getActivity().unregisterReceiver(receiver);
        }
    }
}
