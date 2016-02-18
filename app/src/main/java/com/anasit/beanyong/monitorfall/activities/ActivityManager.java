package com.anasit.beanyong.monitorfall.activities;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeanYong on 2015/12/11.
 * Activity管理类
 */
public class ActivityManager {
    /**
     * Activity列表
     */
    private static List<Activity> sActivityList = new ArrayList<>();

    /**
     * 向Activity列表中添加Activity
     *
     * @param activity
     */
    protected static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    /**
     * 从Activity列表中删除Activity
     *
     * @param activity
     */
    protected static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    /**
     * 从Activity列表中删除所有的activity
     */
    protected static void removeAllActivities() {
        for (Activity activity : sActivityList) {
            activity.finish();
        }
        sActivityList.removeAll(sActivityList);
    }

    protected static int getSize() {
        return sActivityList.size();
    }
}
