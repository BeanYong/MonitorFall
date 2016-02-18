package com.anasit.beanyong.monitorfall.util;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by BeanYong on 2015/12/16.
 * 短信工具类
 */
public class SmsUtil {
    /**
     * 向tel发送一条短信，内容为msg
     *
     * @param context
     * @param tel
     * @param msg
     */
    public static void sendMsg(Context context, String tel, String msg) {
        if (!(TextUtils.isEmpty(tel) || TextUtils.isEmpty(msg))) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(tel, null, msg, null, null);
        } else {
            Toast.makeText(context, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
}
