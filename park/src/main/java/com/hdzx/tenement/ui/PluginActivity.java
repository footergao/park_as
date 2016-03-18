package com.hdzx.tenement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.google.gson.Gson;
import com.hdzx.tenement.aliopenimi.sample.LoginSampleHelper;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.vo.OrderPushVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.cordova.CordovaActivity;

/**
 * User: hope chen
 * Date: 2015/12/22
 * Description: cordova界面
 */
public class PluginActivity extends CordovaActivity {

    public static final String PARAM_URL = "url";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_DISPLAY_TOPBAR = "display_top_bar";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        if (intent != null) {
            launchUrl = intent.getStringExtra(PARAM_URL);
            activityTitle = intent.getStringExtra(PARAM_TITLE);
            isDisplayTopBar = intent.getBooleanExtra("display_top_bar", false);  // false: 隐藏头  true: 显示头
        }
        super.init();
        addMyPushListener();
        loadUrl(launchUrl);
    }

    private void addMyPushListener() {
        YWIMKit mIMKit = LoginSampleHelper.getInstance().getIMKit();
        if (mIMKit == null) {
            return;
        }
        IYWConversationService mConversationService = mIMKit.getConversationService();
        // 不管之前是否已添加，此时都先移除
        mConversationService.removePushListener(iywPushListener);
        mConversationService.addPushListener(iywPushListener);
    }

    private IYWPushListener iywPushListener = new IYWPushListener() {
        // 收到群聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
        @Override
        public void onPushMessage(YWTribe arg0, YWMessage arg1) {
            Log.v(TAG, "收到群聊消息-------->" + arg1.getContent());
        }

        // 收到单聊消息时会回调该方法，开发者可以在该方法内更新该会话的未读数
        @Override
        public void onPushMessage(IYWContact arg0, YWMessage message) {
            Log.v("", "收到单聊消息--->getContent():" +
                    message.getMessageBody().getContent() + "\n" +
                    "getSubType:" + message.getSubType());
            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
                Log.e("", message.getMessageBody().getContent());
                analysis(message.getMessageBody().getContent());
            }
        }
    };

    //解析推送消息
    private void analysis(String message) {
        Gson gson = new Gson();
        Log.v("gl", "message=="+message);
        
        OrderPushVo pushVo = gson.fromJson(message, OrderPushVo.class);

        // 解析如果是订单被抢通知，咚咚侠接单，拒接，咚咚侠确认支付，评价
        if (StringUtils.equals(pushVo.getInteractionType(), OrderPushVo.GRAB_ORDER_USER)
            || StringUtils.equals(pushVo.getInteractionType(), OrderPushVo.CDDX_ORDER_ACCEPT)
            || StringUtils.equals(pushVo.getInteractionType(), OrderPushVo.CDDX_ORDER_REFUSE)
            || StringUtils.equals(pushVo.getInteractionType(), OrderPushVo.CONFIRM_ORDER_PAY)
            || StringUtils.equals(pushVo.getInteractionType(), OrderPushVo.EVALU_ORDER)
                ) {
            // 如果是订单被抢通知，咚咚侠接单，拒接，咚咚侠确认支付，评价，则发送广播
            Intent intent = new Intent();
            intent.setAction(Contants.BROADCAST_ACTION.UPDATE_ORDER.getValue());
            intent.putExtra("orderId", pushVo.getInteraction());
            sendBroadcast(intent);
        }
    }
}
