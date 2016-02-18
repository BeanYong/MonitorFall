package com.anasit.beanyong.monitorfall.entity;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.anasit.beanyong.monitorfall.util.SmsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeanYong on 2015/12/16.
 * 监视器管理者
 */
public class MonitorManager {
    /**
     * 单例对象
     */
    private static MonitorManager INSTANCE = new MonitorManager();
    /**
     * 持有所有的监视器对象
     */
    private List<Monitor> monitors = new ArrayList<>();

    private MonitorManager() {
        //TODO 初始化monitors
        for (int i = 0; i < 3; i++) {
            monitors.add(new Monitor(i + 1, "13007215142", "password", 28.68, 115.89));
        }
    }

    public static MonitorManager getInstance() {
        return INSTANCE;
    }

    /**
     * 添加监控器
     *
     * @param monitor
     */
    public void addMonitor(Monitor monitor) {
        if (monitor != null) {
            monitors.add(monitor);
        }
    }

    /**
     * 根据id返回监视器对象
     *
     * @param id
     * @return
     */
    public Monitor getMonitorById(int id) {
        for (Monitor monitor : monitors) {
            if (monitor.getId() == id) {
                return monitor;
            }
        }
        return null;
    }

    /**
     * 获取所有的监视器对象
     *
     * @return
     */
    public List<Monitor> getMonitors() {
        return monitors;
    }

    /**
     * 更改密码
     *
     * @param context
     * @param monitor
     * @param oldPsw
     * @param newPsw
     */
    public void changePsw(Context context, Monitor monitor, String oldPsw, String newPsw) {
        if (TextUtils.isEmpty(monitor.getTel())) {
            Toast.makeText(context, "请先绑定号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.equals(monitor.getPsw(), oldPsw)) {//密码正确，更改密码
            SmsUtil.sendMsg(context, monitor.getTel(), oldPsw + "*1*+" + newPsw + "*");
        } else {
            Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据id发送更新位置短息
     *
     * @param id
     */
    public void updateLocationById(Context context, int id) {
        Monitor monitor = monitors.get(id);
        SmsUtil.sendMsg(context, monitor.getTel(), monitor.getPsw() + "*4*");
    }
}
