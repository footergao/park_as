cordova.define("com.hdzx.plugin.TenementPlugin", function (require, exports, module) {
    var channel = require('cordova/channel'),
        cordova = require('cordova');
    var PLUGIN_NAME = "Tenement";

    function TenementApi() {
    }

    /**
     * 拨号
     * @param phonenumber   电话号码
     * @param successCallback
     * @param errorCallback
     */
    TenementApi.prototype.phoneDial = function (phonenumber, successCallback, errorCallback) {
        this.createEventWithArgs("PHONEDIAL", [phonenumber], successCallback, errorCallback);
    };

    /**
     * 获取ImageHost
     */
    TenementApi.prototype.getImageHost = function (successCallback, errorCallback) {
        this.createEvent("IMAGEHOST", successCallback, errorCallback);
    };

    /**
     * 获取用户票据
     */
    TenementApi.prototype.getAccessTicket = function (successCallback, errorCallback) {
        this.createEvent("ACCESS_TICKET", successCallback, errorCallback);
    };

    /**
     * 获取用户信息
     */
    TenementApi.prototype.getUserInfo = function (successCallback, errorCallback) {
        this.createEvent("USERINFO", successCallback, errorCallback);
    };

    /**
     * 记录日志
     * @param appid 应用的id
     * @param appversion 应用版本
     * @param content   日志内容
     */
    TenementApi.prototype.log = function (appid, appversion, content) {
        this.createEventWithArgs("LOG",
            [{
                "appid": appid,
                "appversion": appversion,
                "content": content
            }], null, null
        );
    };

    /**
     * 跳转登录页
     */
    TenementApi.prototype.login = function (successCallback, errorCallback) {
        this.createEvent("LOGIN", successCallback, errorCallback);
    };

    /**
     * 调用原生activity
     * @param activityClassName 包名+activity类名
     * @param jsonData          接口参数，json对象
     * @param successCallback   返回服务器响应内容，明文
     * @param errorCallback	     返回错误信息
     */
    TenementApi.prototype.startActivity = function (activityClassName, jsonData, successCallback, errorCallback) {
        this.createEventWithArgs("ACTIVITY", [activityClassName, jsonData], successCallback, errorCallback);
    };

    /**
     * 发送广播
     * @param action            broadcast action
     * @param params            参数，json对象
     * @param successCallback   返回服务器响应内容，明文
     * @param errorCallback	     返回错误信息
     */
    TenementApi.prototype.sendBroadcast = function (action, params, successCallback, errorCallback) {
        this.createEventWithArgs("SEND_BROADCAST", [action, params], successCallback, errorCallback);
    };

    /**
     * nativeCallJs
     * @param action            broadcast action
     * @param jsonData          参数，json对象
     * @param successCallback   返回服务器响应内容，明文
     * @param errorCallback	     返回错误信息
     */
    TenementApi.prototype.nativeCallJs = function (action, jsonData, successCallback, errorCallback) {
        this.createEventWithArgs("BROADCAST", [action, jsonData], successCallback, errorCallback);
    };

    /**
     * 发送请求
     * @param method            接口方法
     * @param jsonData          接口参数，json对象
     * @param successCallback   返回服务器响应内容，明文
     * @param errorCallback	     返回错误信息
     */
    TenementApi.prototype.sendRequest = function (method, jsonData, successCallback, errorCallback) {
        this.createEventWithArgs("REMOTE_REQUEST",
            [{
                "method": method,
                "param": jsonData
            }], successCallback, errorCallback);
    };

    /**
     * 返回原生页面
     */
    TenementApi.prototype.popViewController = function (successCallback) {
        this.createEvent("BACKTONATURE", successCallback, null);
    };

    /**
     * 阿里百川聊天
     * @param target 聊天对象百川UserId
     * @param successCallback
     * @param errorCallback
     */
    TenementApi.prototype.chatting = function (target, successCallback, errorCallback) {
        this.createEventWithArgs("CHATTING", [target], successCallback, errorCallback);
    };

    /**
     * create cordova event
     * @param action            with this action name
     * @param successCallback   success callback function
     * @param errorCallback     error callback function
     */
    TenementApi.prototype.createEvent = function (action, successCallback, errorCallback) {
        this.createEventWithArgs(action, [], successCallback, errorCallback);
        /*
         cordova.exec(
         successCallback,        // success callback function
         errorCallback,          // error callback function
         PLUGIN_NAME,             // mapped to our native Java class called "CalendarPlugin"
         action,                 // with this action name
         []                      // and this array of custom arguments to create our entry
         );
         */
    };

    /**
     * create cordova event
     * @param action            with this action name
     * @param successCallback   success callback function
     * @param errorCallback     error callback function
     * @param args              custom arguments
     */
    TenementApi.prototype.createEventWithArgs = function (action, args, successCallback, errorCallback) {
        cordova.exec(
            successCallback,        // success callback function
            errorCallback,          // error callback function
            PLUGIN_NAME,            // mapped to our native Java class
            action,                 // with this action name
            args                    // and this array of custom arguments to create our entry
        );
    };

    module.exports = new TenementApi();
});