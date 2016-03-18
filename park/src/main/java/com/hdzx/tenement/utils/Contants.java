package com.hdzx.tenement.utils;

/**
 * @author Jesley
 */
public class Contants {

    /**
     * 环境切换开关
     * true:正式环境
     * false:测试环境
     */
    public static final boolean isOnline = false;

    public static final String SERVER_HOST = getHost();

    public static final String APPID = "XBB";

    public static final String VERSION = "0.9.3";

    public static final String OS_AND = "and";

    /**
     * 注册类型，手机注册
     */
    public static final String REGISTER_PHONE_TYPE = "03";

    /**
     * 注册类型，找回密码
     */
    public static final String FIND_PASSWORD_PHONE_TYPE = "04";

    /**
     * 当前正在哪一个activity
     */
    public static String currentActivity = "0"; //0：不用 , 1:MicrosmicListActivity

    /**
     * 请求码常量
     */
    public static final int CODE_EXPRESS_ADDPHONE = 0x717;//添加手机请求码
    public static final int CODE_EXPRESS_EDITPHONE = 0x718;//修改手机备注请求码
    public static final int CODE_EXPRESS_COMPANY = 0x719;//获取快递公司请求码
    public static final int CODE_EXPRESS_WEIGHT = 0x720;//获取重量请求码

    /**
     * RSA公钥
     */
    public static String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFfTbnnVBLKg+tS61aRXvIqn8Z2rSL4KEXrhBEnRngwn2fTzzV8QI5FKmfBD0JQ+IqrG0bo8+dvrE4D2F7FDaOIfAT3/s81hcC4+OGq1i6IFEB1W63nieEL6Pg7IDPCTMp5uXybFWCRunNjilxnEvWqcDSl5Fd1mLVXi3tbwYfuwIDAQAB";

    /**
     * TCP-IP
     */
    public static String tcpIP = "192.168.0.62";

    public static String LONGIN_TYPE_LOG = "0";

    public static String LONGIN_TYPE_REFRSH = "1";
    
    public static boolean isPostMsg = false;
    /**
     * TCP-PORT
     */
    public static int tcpPort = 9000;

    public enum PROTOCOL_CONTEN_KEY {
        head,
        body
    }

    public enum PROTOCOL_REQ_HEAD {
        appid,
        version,
        sessionid,
        os,
        appver,
        uuid
    }

    public enum PROTOCOL_REQ_BODY {
        ticket,
        data
    }

    public enum PROTOCOL_REQ_BODY_DATA {
        /**
         * 请求AES密钥
         */
        imei,
        imsi,

        /**
         * 注册
         */
        mobilePhone,
        password,
        rePassword,
        captchas,

        /**
         * 登录
         */
        loginName,
        type,

        /**
         * 修改密码
         */
        oldPassword,
        newPassword,

        /**
         * 用户信息
         */
        email,
        idcard,
        userName,
        nickerName,
        sex,
        birthday,
        sign,
        headphoto,
        /*个推cid*/
        clientID,
        taskID,
        /**
         * 广告
         */
        appAdvertiseId,
        resolution,
        /**
         * 文章
         */
        articleClassId,

        /**
         * 快件收发
         */
        operType,
        enpId,
        enpName,
        enpPhone,
        enpVerifyCode,
        pageIndex,
        pageCount,
        pageNum,
        pageSize,
        lifecircleId,
        edProvince,
        edCity,

        /* 地址簿 */
        abId,
        abLastuseName,
        abLastuseContact,
        abProvince,
        abCity,
        abArea,
        arId,
        abOther,
        
        
        tgt,
        service,

        //支付
        mer_no,
        in_source,
        order_no,
        subject_name,
        order_amt,
        order_ccy,
        order_time,
        order_content,
        bank_no,
        spbill_create_ip,


        /**
         * 枚举结束标志
         */
        tagEnd

    }

    public enum PROTOCOL_RESP_HEAD {
        rtnCode,
        rtnMsg
    }

    public enum PROTOCOL_RESP_BODY {
        /**
         * AES密钥
         */
        aes,
        sessionid,
        ticket,
        imghost,
        frontUrl,

        RTN_CODE,
        RTN_MSG,
        /* 用户信息 */
        accessTicket,
        USERBASIC,
        USERAUTH,
        LOCALUSER,
        /* 求助 */
        updatetime,
        result,
        page,
        disList,
        replyList,
        moReplyList,
        fileName,
        fileURL,
        /* 预约挂号 */
        HospitalList,
        departList,
        hospitalDoctorInfoList,
        workInfo,
        PatientList,
        Patient,
        ReservationVo,
        messagelist,
        pageSize,
        curPage,
        dataCount,
        orderList,
        hospitalDoctorInfoVo,
        reservationList,
        MessageVo,
        pageNo,
        totalCount,
        /* 家政服务  */
        ReturnInfo,
        Status,
        RowID,
        UserArea,
        CategoryList,
        Category,
        rtnCode,
        rtnMsg,
        ServiceList,
        ServiceInfo,
        PageInfo,
        TotalNumCount,
        TotalPageCount, pageCount,

        /* 手机召车  */
        responseData,
        carLongitude,
        carLatitude,
        userId,
        recordNum,
        orderInfo,
        orderId,
        state,
        carNo,
        schedulingTime,
        taxiPhone,
        time,


        /* 实名认证  */
        cardnum,
        name,
        sex,
        nation,
        address,
        mobile,
        birthday,
        idcardcode,
        
        
        /* 个人信息实名认证  */
        realName,
        idcardNo,
        idcardUrl,
        certType,
        expandPro,
        haimanPhoto,
        
       /* 社区交流*/
        threadId,
        postId,
        content,
        type,
        deptId,
        offset,
        forumId,
        subject,
        attachments,
        

        /* 投诉建议*/
        adviceContent,
        files,
        userCellPhone,
        userName,
        lifeCicleId,
        adviceId,
        message,
        msgType,
        relationId,
        replayType,
        
        /* 设施报修*/
        repairContent,
        repairId,
        repairType,
        
    }

    public static final String REMOTE_DOMAIN = "tenement-service";

    public enum PROTOCOL_COMMAND {
        /**
         * AES初始化接口
         */
        GET_AES("/tenement-service/system/initsys.json"),
        GET_TGT("/tenement-service/system/system.getTicketGrantingTicket.json"),
        GET_ST("/tenement-service/system/system.getServiceTicket.json"),

        /**
         * 用户接口
         */
        /*登录*/
        LOGIN("/tenement-service/user/user.toLogin.json"),
        /*登出*/
        LOGOUT("/tenement-service/user/user.toLoginOut.json"),
        /*注册*/
        REGIST("/tenement-service/user/user.toRegist.json"),
        /*发送短信验证码*/
        CAPTCHAS("/tenement-service/user/user.sendCaptchas.json"),
        /*修改密码*/
        SET_PASSWORD("/tenement-service/user/user.modifyPassword.json"),
        /*重置密码*/
        RE_PASSWORD("/tenement-service/user/user.resetPassword.json"),
        /*验证邮箱*/
        CHECK_EMAIL("/tenement-service/user/user.isEmailExist.json"),
        /*验证身份证是否存在*/
        CHECK_ID("/tenement-service/user/user.isIDcodeExist.json"),
        /*验证登录名是否存在*/
        CHECK_LOGINNAME("/tenement-service/user/user.isLoginNameExist.json"),
        /*获取用户主表信息*/
        GET_USETBASIC("/tenement-service/user/user.getUserCoreInfo.json"),
        /*获取用户基本信息*/
        GET_USETBASICINFO("/tenement-service/user/user.getUserBasicInfo.json"),
        /*更新用户基本信息*/
        SET_USETBASICINFO("/tenement-service/user/user.updateUserBasicInfo.json"),
        /*更新用户主表信息*/
        SET_USETBASIC("/tenement-service/user/user.updateUserCoreInfo.json"),

        /*绑定cid到服务端*/
        BIND_CID("/tenement-service/push/gtPush.toBindCid.json"),
        /*解绑cid*/
        UNBIND_CID("/tenement-service/push/gtPush.toUnBindCid.json"),
        /*获取移动端消息状态*/
        GET_GETUISTATUS("/tenement-service/push/gtPush.getAppMsgStatus.json"),
        /*个推发送测试*/
        GETUI_TEST("/tenement-service/push/gtPush.toSendPush.json"),
        /*获取广告*/
        GET_ADVERTISEMENT("/tenement-service/advertise/advertise.getAdvertiseInfo.json"),

        /*品牌街*/
        GET_BUSSINESSLIST("/tenement-service/brands/brands.getBrandsList.json"),
        
        GET_ARTICLELIST("/tenement-service/brands/brands.getArticleList.json"),
       

        /*上传头像*/
        PUT_HEADER_IMAGE("/tenement-service/file/upload.json"),

        /*获取个人信息*/
        GET_PERSON_INFO("/tenement-service/user/user.selectSysUser.json"),
        /*更新个人信息*/
        UPDATE_PERSON_INFO("/tenement-service/user/user.updateSysUser.json"),
        /*实名认证*/
        REAL_NAME_INFO("/tenement-service/user/realName.applyRealName.json"),
        /*申请咚咚侠*/
        BUILD_DDX("/tenement-service/haiman/haiman.applyHaiman.json"),
        /*快件收发-获取手机列表*/
        GET_NOTE_PHONE("/tenement-service/express/express.getNotifyPhone.json"),
        /*快件收发-更新通知手机*/
        SET_NOTE_PHONE("/tenement-service/express/express.updateNotifyPhone.json"),
        /*快件收发-添加手机*/
        ADD_NOTE_PHONE("/tenement-service/express/express.addNotifyPhone.json"),
        /*快件收发-获取快递收发订单列表（含代收）*/
        GET_EXPRESS_ORDER("/tenement-service/express/express.getExpressOrder.json"),
        /*快件收发-获取快递公司*/
        GET_EXPRESS_COMPANY("/tenement-service/express/express.getDelivery.json"),
        /*快件收发-获取快递收费标准*/
        GET_EXPRESS_WEIGHT("/tenement-service/express/express.getDeliveryStandard.json"),
        /*快件收发-提交寄件订单*/
        SUBMIT_EXPRESS_ORDER("/tenement-service/express/express.addTakeOrder.json"),

        /*获取服务页面信息*/
        GET_SERVICE_INFO("/tenement-service/appservice/appserviceItem.getPageItemsTree.json"),

        /* 便民电话 */
        GET_CONVEN_PHONE("/tenement-service/estate/conveniencePhone.getAllConvenPhone.json"),

        /* 获取用户地址簿 */
        GET_USER_ADDRESS("/tenement-service/address/address.getUserAddress.json"),
        /* 新增/更新地址簿 */
        UPDATE_USER_ADDRESS("/tenement-service/address/address.updateUserAddress.json"),

        /*提交订单*/
        SUBMIT_ORDER("/tenement-service/order/order.addOrderInfo.json"),

        /*上传文件*/
        UPLOAD_FILE("/tenement-service/file/uploadStream.json"),
        
        
        /*更新生活圈*/
        UPDATE_USER_LIFE_CIRCLE("/tenement-service/address/address.updateUserLifecircle.json"),
        /*定位生活圈*/
        UPDATE_NOW_LIFE_CIRCLE("/tenement-service/address/address.updateNowLifecircle.json"),
        /*根据城市坐标获取生活圈列表*/
        GET_LIFE_CIRCLE("/tenement-service/address/address.getLifecircle.json"),
        /*根据关键字搜索生活圈列表*/
        GET_LIFE_CIRCLE_BY_KEYWORD("/tenement-service/address/address.getLifecircleByKeyword.json"),
        /*获取我的生活圈列表*/
        GET_MY_LIFE_CIRCL("/tenement-service/address/address.getUserLifecircle.json"),
        /*获取生活圈图标列表*/
        GET_LIFE_CIRCL_FLAG("/tenement-service/address/address.getLifecircleFlag.json"),
        
        
        /* 获取所有可接订单和当前咚咚侠可接订单数目*/
        GET_CAN_ACCEPT_ORDER_NUM("/tenement-service/order/order.getCanAcceptOrderInfo.json"),
        
        /*获取我的帖子*/
        GET_MY_POSTS("/tenement-service/bbs/bbsThreads.getMyThreads.json"),
        GET_Theme_POST("/tenement-service/bbs/bbsThreads.getThreadPosts.json"),
        GET_Message_Lst("/tenement-service/bbs/bbsThreads.getMyNotices.json"),
        SEND_Message("/tenement-service/bbs/bbsThreads.postContent.json"),
        GET_FORUM_LST("/tenement-service/bbs/bbsForums.getForumsByDept.json"),
        GET_THREAD_LST("/tenement-service/bbs/bbsThreads.getThreadPosts.json"),
        GET_THREAD_REFRESH_LST("/tenement-service/bbs/bbsThreads.getRefreshThreads.json"),
        GET_THREAD_MORE_LST("/tenement-service/bbs/bbsThreads.getMoreThreads.json"),
        POST_MESSAGE("/tenement-service/bbs/bbsThreads.postContent.json"),
        SET_READ_POST("/tenement-service/bbs/bbsThreads.setPostReaded.json"),
        
        /*投诉建议*/
        SEND_SUGGEST("/tenement-service/estate/propertyManagement.toAdvice.json"),
        GET_SUGGEST_LST("/tenement-service/estate/propertyManagement.toListAdvice.json"),
        UPDATE_SUGGEST_SURE("/tenement-service/estate/propertyManagement.toConfirmAdvice.json"),
        GET_SUGGEST_DTL("/tenement-service/estate/propertyManagement.getAdviceDetail.json"),
        SEND_SUGGEST_MSG("/tenement-service/estate/propertyManagement.toSendMessage.json"),
        
        
        /*设施报修*/
        SEND_REPAIR("/tenement-service/estate/propertyManagement.toRepair.json"),
        GET_REPAIR_LST("/tenement-service/estate/propertyManagement.toListRepair.json"),
        UPDATE_REPAIR_SURE("/tenement-service/estate/propertyManagement.toConfirmRepair.json"),
        GET_REPAIR_DTL("/tenement-service/estate/propertyManagement.getRepairDetail.json"),
        GET_DICTIONARY("/tenement-service/dictionary/dictionary.getDictionaryByType.json"),
        
        /*手机开门*/
        UPDATE_OPEN_DOOR("/tenement-service/door/door.getDoorQrcode.json"),
        
        /*获取消息类别列表*/
        GET_ALL_MESSAGE_OUTLINE("/tenement-service/msg/message.getAllMessageOutline.json"),
        /*获取用户系统消息*/
        GET_USER_MESSAGE("/tenement-service/msg/message.getUserMessage.json"),
        /*设置已读消息*/
        SET_READ_MESSAGE("/tenement-service/msg/message.setReadMessage.json"),
        /*设置已读消息列表*/
        SET_READ_MESSAGES("/tenement-service/msg/message.setReadMessages.json"),
        /*根据category设置已读消息*/
        SET_READ_MESSAGE_BY_CATEGORY("/tenement-service/msg/message.setReadMessageByCategory.json"),
        /*删除消息*/
        DELETE_MESSAGE("/tenement-service/msg/message.deleteMessage.json"),
        /*删除消息列表*/
        DELETE_MESSAGES("/tenement-service/msg/message.deleteMessages.json"),

        /*支付-获取银行列表*/
        GET_BANK_LST("/tenement-service/pay/payment.getBankList.json"),
        /*支付-支付接口*/
        GET_PAY("/tenement-service/pay/payment.applyPay.json"),

        COMMAND_END("end");

        private String value;

        PROTOCOL_COMMAND(String name) {
            this.value = name;
        }

        public String getValue() {
            return value;
        }


        public String getCode() {
            return this.name();
        }

        public static String getValue(String code) {
            for (PROTOCOL_COMMAND item : PROTOCOL_COMMAND.values()) {
                if (item.getCode().equals(code)) {
                    return item.getValue();
                }
            }

            return code;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    public interface ResponseCode {
        String CODE_000000 = "000000";
        String CODE_900905 = "900905";
        //ticket过期
        String CODE_900001 = "900001";
        //重复登录
        String CODE_900002 = "900002";
        //sessionid与AES过期
        String CODE_900003 = "900003";
    }

    public interface ResponseInnerCode {
        //无法获取用户名和密码刷取票据
        String CODE_CUST_0X900000 = "0X900000";
    }

    public enum CryptoTyepEnum {
        none,
        rsa,
        aes
    }

    public static String getHost() {
        String host = null;
        if (isOnline) {
        	host = "http://bangbangclub.cn";
//            host = "http://www.zjgsmwy.com";
        } else {
//        	host = "http://192.168.1.221";
            host = "http://192.168.0.48";
        }
        return host;
    }

    /**
     * 获取html5应用地址
     * @return
     */
    public static String getAppHostNull() {
        return isOnline ? "http://www.zjgsmwy.com" : "http://192.168.1.221";
    }

    /** 
     * PreferencesUtils key
     */
    public enum PREFERENCES_KEY {
        usn,
        psw,
        auto_city,
        door_status,
        keyEnd
    }

    public enum MEDIA_TYPE {
        IMAGE,
        AUDIO,
        VEDIO,
        NONE
    }

    public enum OssFilePath {
        //用户头像
        USER_HEAD("user/head", 1),
        //应用图标
        APP_ICON("app/icon", 2),
        //用户其他数据
        USER_DATA("user/data", 3),
        //应用其他数据
        APP_DATA("app/data", 4),
        //物业服务其他数据
        ESTATE_DATA("est/data", 5),
        //消息数据
        MSG_ICON("message/config/icon", 6),
        //活动图标
        ACTIVE_BANNER("active/banner", 7),
        //用户实名
        USER_REALNAME("user/real", 8),
        //申请咚咚侠
        USER_HAIMAN("user/ddx", 9),
        //嗨单数据
        HAI_ORDER("user/order", 10),
        //设施报修
        FACILITIES_REPAIR("est/faci", 11),
        //投诉建议
        COMPLAINTS_SUGGESTIONS("user/cosu", 12);

        // 成员变量
        private String name;
        private int index;

        // 构造方法
        OssFilePath(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (OssFilePath c : OssFilePath.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // 普通方法
        public static OssFilePath getOssFilePath(int index) {
            for (OssFilePath c : OssFilePath.values()) {
                if (c.getIndex() == index) {
                    return c;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    // 广播定义
    public enum BROADCAST_ACTION {
        UPDATE_ORDER("com.tenement.updateOrderStream");

        private String value;

        BROADCAST_ACTION(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static String getValue(String name) {
            for (BROADCAST_ACTION item : BROADCAST_ACTION.values()) {
                if (item.name().equals(name)) {
                    return item.value;
                }
            }
            return name;
        }
    }
}
