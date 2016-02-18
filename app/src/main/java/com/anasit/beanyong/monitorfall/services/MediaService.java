package com.anasit.beanyong.monitorfall.services;

import android.app.Service;
import android.content.Intent;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import com.anasit.beanyong.monitorfall.R;

/**
 * Created by BeanYong on 2015/12/8.
 * 用于播放铃声和振动的服务
 */
public class MediaService extends Service {
    public final static String ACTION_START = "start";
    public final static String ACTION_STOP = "stop";

    private SoundPool mSoundPool = null;
    private int mSoundId = 0;//需要播放的声音id
    private Vibrator mVibrator = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (ACTION_START.equals(intent.getAction())) {//开始响铃并且振动
                if (mSoundPool == null) {
                    mSoundPool = new SoundPool(1, 0, 5);
                    mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            mSoundPool.play(sampleId, 1, 1, 0, -1, 1);
                        }
                    });
                    mSoundId = mSoundPool.load(this, R.raw.osmium, 1);//需要播放的声音id
                }

                if (mVibrator == null) {
                    mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (mVibrator.hasVibrator()) {//设备支持振动
                        mVibrator.vibrate(60000);
                    } else {
                        mVibrator = null;
                    }
                }

                System.out.println(intent.getAction());
            } else {//关闭响铃及振动
                if (mSoundPool != null) {
                    mSoundPool.stop(mSoundId);
                    mSoundPool.unload(mSoundId);
                    mSoundPool.release();
                    mSoundPool = null;
                }

                if (mVibrator != null) {
                    mVibrator.cancel();
                    mVibrator = null;
                }

                System.out.println(intent.getAction());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSoundPool != null) {
            mSoundPool.stop(mSoundId);
            mSoundPool.unload(mSoundId);
            mSoundPool.release();
            mSoundPool = null;
        }

        if (mVibrator != null) {
            mVibrator.cancel();
            mVibrator = null;
        }

        System.out.println("MediaService destroy");
    }
}
