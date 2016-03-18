package com.hdzx.tenement.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anchendong on 15/7/17.
 */
public class PreferencesUtils {

    private  SharedPreferences preferences;

    private  String TAG_SP = "tenement";

    public static PreferencesUtils instance = null;

    public static PreferencesUtils getInstance() {
        if (instance == null) {
            instance = new PreferencesUtils();
        }
        return instance;
    }

    private  SharedPreferences getStore(Context context) {
        return context.getSharedPreferences(TAG_SP, Context.MODE_PRIVATE);
    }

    private  SharedPreferences.Editor getEditor(Context context) {
        return getStore(context).edit();
    }

    /**
     * 存储string
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public  boolean saveString(Context context, String key, String value) {
        return getEditor(context).putString(key, value).commit();
    }

    /**
     * 存储int
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public  boolean saveInt(Context context, String key, int value) {
        return getEditor(context).putInt(key, value).commit();
    }

    /**
     * 存储float
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public  boolean saveFloat(Context context, String key, float value) {
        return getEditor(context).putFloat(key, value).commit();
    }

    /**
     * 存储long
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public  boolean saveLong(Context context, String key, long value) {
        return getEditor(context).putLong(key, value).commit();
    }

    /**
     * 存储boolean
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public boolean saveBoolean(Context context, String key, boolean value) {
        return getEditor(context).putBoolean(key, value).commit();
    }

    /**
     * 获取string
     *
     * @param context
     * @param key
     * @return
     */
    public String takeString(Context context, String key) {
        return getStore(context).getString(key, "");
    }

    /**
     * 获取int
     *
     * @param context
     * @param key
     * @return
     */
    public int takeInt(Context context, String key) {
        return getStore(context).getInt(key, 0);
    }

    /**
     * 获取long
     *
     * @param context
     * @param key
     * @return
     */
    public long takeLong(Context context, String key) {
        return getStore(context).getLong(key, 0);
    }

    /**
     * 获取float
     *
     * @param context
     * @param key
     * @return
     */
    public float takeFloat(Context context, String key) {
        return getStore(context).getFloat(key, 0);
    }

    /**
     * 获取boolean
     *
     * @param context
     * @param key
     * @return
     */
    public boolean takeBoolean(Context context, String key) {
        return getStore(context).getBoolean(key, false);
    }

    /**
     * 清除缓存
     * @param context
     * @param key
     * @return
     */
    public boolean remove(Context context, String key) {
        return getEditor(context).remove(key).commit();
    }
}