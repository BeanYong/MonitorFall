package com.anasit.beanyong.monitorfall.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.entity.Monitor;
import com.anasit.beanyong.monitorfall.entity.MonitorManager;
import com.anasit.beanyong.monitorfall.util.SharedPreferencesUtil;
import com.anasit.beanyong.monitorfall.util.SmsUtil;

/**
 * Created by BeanYong on 2015/12/20.
 * 绑定号码页面
 */
public class BindTelActivity extends BaseActivity implements View.OnClickListener {
    private EditText mTel1, mTel2, mTel3;
    private ImageView mCancel, mOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_tel);
        ActivityManager.addActivity(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    private void initView() {
        mTel1 = (EditText) findViewById(R.id.bind_tel_et_1);
        mTel2 = (EditText) findViewById(R.id.bind_tel_et_2);
        mTel3 = (EditText) findViewById(R.id.bind_tel_et_3);
        mCancel = (ImageView) findViewById(R.id.bind_tel_cancel);
        mOk = (ImageView) findViewById(R.id.bind_tel_ok);

        String tel1 = SharedPreferencesUtil.getString(this, "1");
        String tel2 = SharedPreferencesUtil.getString(this, "2");
        String tel3 = SharedPreferencesUtil.getString(this, "3");
        if (TextUtils.isEmpty(tel1)) {
            mTel1.setText(tel1);
        }
        if (TextUtils.isEmpty(tel2)) {
            mTel2.setText(tel2);
        }
        if (TextUtils.isEmpty(tel3)) {
            mTel3.setText(tel3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_tel_cancel:
                finish();
                break;
            case R.id.bind_tel_ok:
                bindTel();//绑定号码
                break;
        }
    }

    /**
     * 绑定号码
     */
    private void bindTel() {
        if(TextUtils.isEmpty(SharedPreferencesUtil.getString(this, "psw"))){
            Toast.makeText(this, "尚未设置密码及检跌器电话号码，请先到设置页面进行设置！", Toast.LENGTH_LONG).show();
            return;
        }

        String tel1 = mTel1.getText().toString().trim();
        String tel2 = mTel2.getText().toString().trim();
        String tel3 = mTel3.getText().toString().trim();

        MonitorManager monitorManager = MonitorManager.getInstance();

        if (!TextUtils.isEmpty(tel1)) {
            Monitor m = new Monitor(monitorManager.getMonitors().size() + 1, tel1, "psw", 0.0, 0.0);
            monitorManager.addMonitor(m);
            SharedPreferencesUtil.put(this, "1", tel1);
            Toast.makeText(this, "绑定号码1成功", Toast.LENGTH_SHORT).show();
        } else {
            tel1 = "";
        }

        if (!TextUtils.isEmpty(tel2)) {
            Monitor m = new Monitor(monitorManager.getMonitors().size() + 1, tel2, "psw", 0.0, 0.0);
            monitorManager.addMonitor(m);
            SharedPreferencesUtil.put(this, "2", tel2);
            Toast.makeText(this, "绑定号码2成功", Toast.LENGTH_SHORT).show();
        } else {
            tel2 = "";
        }

        if (!TextUtils.isEmpty(tel3)) {
            Monitor m = new Monitor(monitorManager.getMonitors().size() + 1, tel3, "psw", 0.0, 0.0);
            monitorManager.addMonitor(m);
            SharedPreferencesUtil.put(this, "3", tel3);
            Toast.makeText(this, "绑定号码3成功", Toast.LENGTH_SHORT).show();
        } else {
            tel3 = "";
        }

        SmsUtil.sendMsg(this, SharedPreferencesUtil.getString(this, "monitor_tel"), SharedPreferencesUtil.getString(this, "psw") + "*3*" + tel1 + "*" + tel2 + "*" + tel3 + "*");
    }
}
