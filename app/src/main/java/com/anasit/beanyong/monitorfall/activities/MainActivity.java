package com.anasit.beanyong.monitorfall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.util.SharedPreferencesUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mMap = null;
    private Button mMonitoredList, mBindTel, mSettings,mLocateOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        mMap.setOnClickListener(this);
        mMonitoredList.setOnClickListener(this);
        mBindTel.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mLocateOld.setOnClickListener(this);
    }

    private void initView() {
        mMap = (ImageView) findViewById(R.id.left_menu_map);
        mMonitoredList = (Button) findViewById(R.id.btn_monitored_list);
        mBindTel = (Button) findViewById(R.id.btn_bind_tel);
        mSettings = (Button) findViewById(R.id.btn_settings);
        mLocateOld = (Button) findViewById(R.id.btn_locate_old);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_menu_map://地图
                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_monitored_list://已绑定检跌器
                Intent intent2 = new Intent(this, MonitorListActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_bind_tel://绑定号码
                Intent intent3 = new Intent(this, BindTelActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_locate_old://定位老人
                Intent intent5 = new Intent(this, MapActivity.class);
                intent5.putExtra("lat", SharedPreferencesUtil.getDouble(this, "oldManLat"));
                intent5.putExtra("lon", SharedPreferencesUtil.getDouble(this, "oldManLon"));
                startActivity(intent5);
                break;
            case R.id.btn_settings://设置
                Intent intent4 = new Intent(this, SettingsActivity.class);
                startActivity(intent4);
                break;
        }
    }

    //==================================================生命周期============================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }
}
