package com.hdzx.tenement.common;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserBasic implements Serializable {

    private static final long serialVersionUID = -914829262837762140L;
    private String idcard;              // 身份证
    private String mobilePhone;         // 手机号
    private String level;               // 用户等级：0-普通用户，1-实名认证用户，2-管理用户
    private String email;               // 电子邮件
    private String status;              // 状态：0-停用，1-启用
    private String loginName;           // 登录名
    private String userName;            // 用户名称
    private String realName;            // 真实姓名
    private String sex;                 // 性别：0-保密，1-男，2-女
    private String birthday;            // 生日
    private String certType;            // 证件类型：0-身份证，1-军官证，2-护照
    private String headphoto;           // 头像
    private String telephone;           // 座机
    private String age;                 // 年龄
    private String sign;                // 签名
    private String habit;               // 爱好
    private String jobTitle;            // 职位
    private String speciality;          // 特长
    private String editor;              // 修改者
    private String editTime;            // 修改时间
    private String creator;             // 创建者
    private String createTime;          // 创建时间
    private String userId;              // 用户编号
    private String lastLoginTime;       // 上次登录时间
    private String loginTime;           // 登录时间
    private String isOnline;            // 是否在线：0-否，1-是
    private String sourceId;            // 用户来源 1-自主注册 2-后台添加 3-后台导入
    private BigDecimal lifecircleId;    // 生活圈编号
    private String lifecircleName;      // 生活圈名称
    private BigDecimal regionalId;      // 小区ID
    private String regionalName;        // 小区名称
    private String isHelpMan;           // 是否是咚咚侠：0-否，1-是
    private String nickerName;          // 昵称
    private String ddxTaobaoName;       // 咚咚侠淘宝用户ID
    private String ddxTaobaoPass;       // 咚咚侠淘宝用户密码
    private String xbbTaobaoName;       // 享帮帮淘宝用户ID
    private String xbbTaobaoPass;       // 享帮帮淘宝用户密码

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getLifecircleId() {
        return lifecircleId == null ? null : lifecircleId.intValue();
    }

    public void setLifecircleId(BigDecimal lifecircleId) {
        this.lifecircleId = lifecircleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAge() {
        return age;
    }

	public void setAge(String age) {
        this.age = age;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getLifecircleName() {
        return lifecircleName;
    }

    public void setLifecircleName(String lifecircleName) {
        this.lifecircleName = lifecircleName;
    }

    public Integer getRegionalId() {
        return regionalId == null ? null : regionalId.intValue();
    }

    public void setRegionalId(BigDecimal regionalId) {
        this.regionalId = regionalId;
    }

    public String getRegionalName() {
        return regionalName;
    }

    public void setRegionalName(String regionalName) {
        this.regionalName = regionalName;
    }

    public String getIsHelpMan() {
        return isHelpMan;
    }

    public void setIsHelpMan(String isHelpMan) {
        this.isHelpMan = isHelpMan;
    }

    public String getNickerName() {
        return nickerName;
    }

    public void setNickerName(String nickerName) {
        this.nickerName = nickerName;
    }

    public String getDdxTaobaoName() {
        return ddxTaobaoName;
    }

    public void setDdxTaobaoName(String ddxTaobaoName) {
        this.ddxTaobaoName = ddxTaobaoName;
    }

    public String getDdxTaobaoPass() {
        return ddxTaobaoPass;
    }

    public void setDdxTaobaoPass(String ddxTaobaoPass) {
        this.ddxTaobaoPass = ddxTaobaoPass;
    }

    public String getXbbTaobaoName() {
        return xbbTaobaoName;
    }

    public void setXbbTaobaoName(String xbbTaobaoName) {
        this.xbbTaobaoName = xbbTaobaoName;
    }

    public String getXbbTaobaoPass() {
        return xbbTaobaoPass;
    }

    public void setXbbTaobaoPass(String xbbTaobaoPass) {
        this.xbbTaobaoPass = xbbTaobaoPass;
    }
}
