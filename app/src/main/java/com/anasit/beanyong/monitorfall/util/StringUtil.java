package com.anasit.beanyong.monitorfall.util;

import android.text.TextUtils;

/**
 * Created by BeanYong on 2015/12/11.
 * 字符串工具类
 */
public class StringUtil {
    /**
     * 从target中提取经度、纬度，以double数组形式返回
     *
     * @param target
     * @return 长度为2的double数组，第一个值为经度，第二个值为纬度
     */
    public static double[] getLonLat(String target) {
        String[] temp;
        double[] result = new double[2];
        temp = target.split("Longitude: ")[1].split("  ");
        temp[1] = temp[1].split("Latitude: ")[1].split("  ")[0];
        result[0] = Double.parseDouble(temp[0]);
        result[1] = Double.parseDouble(temp[1]);
        System.out.println(result[0] + "," + result[1]);
        return result;
    }

    /**
     * 判断str1和str2是否均不为空并且相等
     *
     * @param str1
     * @param str2
     * @return 均不为空并且相等为true
     */
    public static boolean isConsistent(String str1, String str2) {
        return !TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2) && TextUtils.equals(str1, str2);
    }
}
