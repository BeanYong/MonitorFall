package com.anasit.beanyong.monitorfall;

import android.app.Application;
import android.content.Context;

/**
 * Created by BeanYong on 2015/12/17.
 */
public class MonitorFallApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
