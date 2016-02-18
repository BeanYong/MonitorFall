package com.anasit.beanyong.monitorfall.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BeanYong on 2015/12/17.
 * 用于持久化Monitor对象信息
 */
public class SharedPreferencesUtil {
    /**
     * 存储监视器对象信息的SharedPreferences
     */
    private final static String SP_NAME = "monitors_info";

    /**
     * 将字符串存入SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 将整数存入SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 将double型存入SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, double value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, (float) value);
        editor.commit();
    }

    /**
     * 根据键获取字符串
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, null);
        return value;
    }

    /**
     * 根据键获取整数
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, 0);
        return value;
    }

    /**
     * 根据键获取double型
     *
     * @param context
     * @param key
     * @return
     */
    public static double getDouble(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        double value = sp.getFloat(key, 0);
        return value;
    }
}
