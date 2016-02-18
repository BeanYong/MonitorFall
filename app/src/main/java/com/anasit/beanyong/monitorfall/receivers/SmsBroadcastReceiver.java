package com.anasit.beanyong.monitorfall.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.activities.MapActivity;
import com.anasit.beanyong.monitorfall.services.MediaService;
import com.anasit.beanyong.monitorfall.util.StringUtil;

/**
 * Created by BeanYong on 2015/12/3.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS = "android.provider.Telephony.SMS_RECEIVED";
    private final static String BATTERY_LOW = "Battery is low!";//低电报警
    private final static String DANGEROUS_TYPE = "Dangerous type:";//危险类型

    private final static String WARNING_HELP = "老人求救！请及时处理！";
    private final static String WARNING_FAINT = "老人可能昏厥！请及时处理！";
    private final static String WARNING_FALL = "老人摔倒！请及时处理！";
    private final static String WARNING_SAFE = "老人解除报警！";
    private final static String WARNING_BATTERY_LOW = "报警器电源不足请及时充电！";
    private String mRawSms;//原始短信内容

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ACTION_SMS) {//收到短信
            Bundle bundle = intent.getExtras();
            String smsContent = readSmsContent(bundle);//读取短信内容
            System.out.println("smscontent:"+smsContent);
            mRawSms = smsContent;
            String notificationContent = checkSms(context, smsContent);
            if (!TextUtils.isEmpty(notificationContent)) {
                sendNotification(context, notificationContent);//根据短信内容发送Notification
            }
        }
    }

    /**
     * 检查是否为目标短信
     *
     * @param context
     * @param smsContent
     * @return
     */
    private String checkSms(Context context, String smsContent) {
        if(smsContent.length() < 15){
            return null;
        }
        String flagStr = smsContent.substring(0, 15);//判断依据
        String result = null;//返回的结果
        if (flagStr.equals(DANGEROUS_TYPE)) {//危险报警
            int dangerousType = Integer.parseInt(smsContent.charAt(15) + "");
            switch (dangerousType) {
                case 1://求救报警
                    sendNotification(context, WARNING_HELP);
                    operateRing(context, MediaService.ACTION_START);
                    break;
                case 2://跌倒报警
                    sendNotification(context, WARNING_FALL);
                    operateRing(context, MediaService.ACTION_START);
                    break;
                case 3://昏厥报警
                    sendNotification(context, WARNING_FAINT);
                    operateRing(context, MediaService.ACTION_START);
                    break;
                case 4://平安通知
                    sendNotification(context, WARNING_SAFE);
                    operateRing(context, MediaService.ACTION_STOP);
                    break;
            }
        } else if (flagStr.equals(BATTERY_LOW)) {//低电量
            result = "报警器电源不足请及时充电！";
        }
        return result;
    }

    /**
     * 读取短信内容
     *
     * @param bundle
     * @return 短信字符串
     */
    private String readSmsContent(Bundle bundle) {
        StringBuilder smsContent = null;
        if (bundle != null) {
            Object[] pdusObjects = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdusObjects.length];
            for (int i = 0; i < pdusObjects.length; i++) {
                messages[i] = SmsMessage
                        .createFromPdu((byte[]) pdusObjects[i]);
            }

            smsContent = new StringBuilder();
            for (SmsMessage message : messages) {
                smsContent.append(message.getDisplayMessageBody());
                System.out.println(smsContent.toString());
            }
        }
        return smsContent.toString();
    }

    /**
     * 根据短信内容发送Notification
     *
     * @param context
     * @param smsContent 短信内容
     */
    private void sendNotification(Context context, String smsContent) {
        if (smsContent != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("收到报警信息").setContentText(smsContent).setTicker("收到报警信息").setDefaults(Notification.DEFAULT_ALL).
                    setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true);
            Intent intent = new Intent(context, MapActivity.class);
            double[] latLon = StringUtil.getLonLat(mRawSms);
            intent.putExtra("lat",latLon[1]);
            intent.putExtra("lon",latLon[0]);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0x110, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }

    /**
     * 操作铃声
     *
     * @param context
     * @param action  MediaService.ACTION_STOP 停止响铃及振动，MediaService.ACTION_STOP 开始响铃及振动
     */
    private void operateRing(Context context, String action) {
        Intent intent = new Intent(context, MediaService.class);
        intent.setAction(action);
        context.startService(intent);
    }
}
