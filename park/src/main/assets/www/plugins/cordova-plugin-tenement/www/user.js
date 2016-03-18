/**
 * Created by anchendong on 15/7/15.
 */
cordova.define("cordova-plugin-tenement.user", function(require, exports, module) {

    var exec = require('cordova/exec');
    module.exports ={
        //获取用户信息
        getUser: function(successCallback, errorCallback) {

            exec(successCallback, errorCallback, "WebUser", "getUser",[]);
        }
    };
});