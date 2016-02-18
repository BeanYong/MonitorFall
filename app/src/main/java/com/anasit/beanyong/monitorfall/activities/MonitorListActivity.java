package com.anasit.beanyong.monitorfall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.adapter.MonitorListAdapter;
import com.anasit.beanyong.monitorfall.entity.MonitorManager;
import com.anasit.beanyong.monitorfall.util.SharedPreferencesUtil;
import com.anasit.beanyong.monitorfall.util.SmsUtil;

/**
 * Created by BeanYong on 2015/12/17.
 * 显示所有的Monitor
 */
public class MonitorListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mMonitorList;
    private MonitorListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_list);
        ActivityManager.addActivity(this);
        mMonitorList = (ListView) findViewById(R.id.monitor_list_lv);
        MonitorManager monitorManager = MonitorManager.getInstance();
        mAdapter = new MonitorListAdapter(this, monitorManager.getMonitors(), R.layout.monitor_list_item);
        mMonitorList.setAdapter(mAdapter);
        mMonitorList.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //发送获取位置短信，获取成功后跳转至地图页面并显示位置
        SmsUtil.sendMsg(this, SharedPreferencesUtil.getString(this, "1"),
                SharedPreferencesUtil.getString(this, "psw") + "*4*");
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("id", position + 1);
        startActivity(intent);
    }
}
