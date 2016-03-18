cordova.define("cordova-plugin-tenement.zixng", function(require, exports, module) {

    var exec = require('cordova/exec');
    module.exports ={
        showzxing: function() {
        exec(null, null, "WebZxing", "showzxing",[]);
    }
};
});