package com.hdzx.tenement.common;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.hdzx.tenement.utils.ImageUtil;

public class UserSession {

    public static UserSession instance = null;

    private boolean isLogin = false;

    private boolean isaliLogin = false;

    private volatile String accessTicket = null;

    private volatile String aesKey = null;

    private volatile boolean isValidAesKey = false;

    private String imageHost;
    
    private String frontUrl;

    private String userName = null;

    private Bitmap headerIcon = null;

    private Drawable headerIconDrawable = null;

    private boolean isBusAreaReady;

    private UserBasic userBasic;

    private String sessionId;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isaliLogin() {
        return isaliLogin;
    }

    public void setIsaliLogin(boolean isaliLogin) {
        this.isaliLogin = isaliLogin;
    }

    public String getAccessTicket() {
        return accessTicket;
    }

    public void setAccessTicket(String accessTicket) {
        this.accessTicket = accessTicket;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
        isValidAesKey = aesKey != null;
    }

    public boolean isValidAesKey() {
        return isValidAesKey;
    }

    public void setValidAesKey(boolean isValidAesKey) {
        this.isValidAesKey = isValidAesKey;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void clear() {
        isLogin = false;
        accessTicket = null;
        userName = null;

        headerIcon = null;
        headerIconDrawable = null;

        userBasic = null;
    }

    public boolean isBusAreaReady() {
        return isBusAreaReady;
    }

    public void setBusAreaReady(boolean isBusAreaReady) {
        this.isBusAreaReady = isBusAreaReady;
    }

    public UserBasic getUserBasic() {
        return userBasic;
    }

    public void setUserBasic(UserBasic userBasic) {
        this.userBasic = userBasic;
    }

    public Bitmap getHeaderIcon() {
        return headerIcon;
    }

    public void setHeaderIcon(Drawable headerIcon) {
        Bitmap bitMap = ImageUtil.drawableToBitmap(headerIcon);
        setBitMapHeaderIcon(bitMap);
    }

    public void setHeaderIcon(Bitmap headerIcon) {
        setBitMapHeaderIcon(headerIcon);
    }

//	public void setOrginHeaderIcon(Drawable headerIcon)
//	{
//		this.headerIcon = headerIcon;
//	}

    public void setBitMapHeaderIcon(Bitmap bitMap) {
        //圆角
        float roundPx;
        //正方形长度
        int extbit;
        if (bitMap.getHeight() >= bitMap.getWidth()) {
            roundPx = (float) (bitMap.getHeight() / 2.0);
            extbit = bitMap.getHeight();
        } else {
            roundPx = (float) (bitMap.getWidth() / 2.0);
            extbit = bitMap.getWidth();
        }
        //当图片过小时，进行拉伸至正常状态
        if (extbit <= 240) {
            extbit = 240;
            roundPx = 120;
        }
        //进行图片拉伸形成正方形
        Bitmap mBitmap = Bitmap.createScaledBitmap(bitMap, extbit, extbit, true);
        //正方形处理圆角

        headerIcon = ImageUtil.getRoundedCornerBitmap(mBitmap, roundPx);
    }

    public Drawable getHeaderIconDrawable() {
        if (headerIconDrawable == null) {
            if (headerIcon != null) {
                headerIconDrawable = new BitmapDrawable(headerIcon);
            }
        }
        return headerIconDrawable;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
