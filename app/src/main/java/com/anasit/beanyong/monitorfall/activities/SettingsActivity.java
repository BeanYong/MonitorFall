package com.anasit.beanyong.monitorfall.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.util.SharedPreferencesUtil;
import com.anasit.beanyong.monitorfall.util.SmsUtil;

/**
 * Created by BeanYong on 2016/2/18.
 */
public class SettingsActivity extends BaseActivity {
    private Button mOk;
    private EditText mMonitorTel, mPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActivityManager.addActivity(this);
        initView();
        initEvent();
    }

    private void initView() {
        mMonitorTel = (EditText) findViewById(R.id.et_monitor_tel);
        mPsw = (EditText) findViewById(R.id.et_psw);
        mOk = (Button) findViewById(R.id.btn_ok);

        String monitor_tel = SharedPreferencesUtil.getString(this, "monitor_tel");
        String psw = SharedPreferencesUtil.getString(this, "psw");
        if(!TextUtils.isEmpty(monitor_tel)){
            mMonitorTel.setText(monitor_tel);
        }
        if(!TextUtils.isEmpty(psw)){
            mPsw.setText(psw);
        }
    }

    private void initEvent() {
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = mMonitorTel.getText().toString().trim();
                String psw = mPsw.getText().toString().trim();
                if (TextUtils.isEmpty(tel) || TextUtils.isEmpty(psw)) {
                    Toast.makeText(SettingsActivity.this, "检跌器电话号码或密码为空，请输入！", Toast.LENGTH_LONG).show();
                    return;
                }

                if (psw.length() != 6) {
                    Toast.makeText(SettingsActivity.this, "密码必须为6位数字！", Toast.LENGTH_LONG).show();
                    return;
                }

                String oldPsw = SharedPreferencesUtil.getString(SettingsActivity.this, "psw");
                if (TextUtils.isEmpty(oldPsw)) {
                    oldPsw = "";
                }
                SmsUtil.sendMsg(SettingsActivity.this, tel, oldPsw + "*1*" + psw + "*");
                SharedPreferencesUtil.put(SettingsActivity.this, "monitor_tel", tel);
                SharedPreferencesUtil.put(SettingsActivity.this, "psw", psw);
                Toast.makeText(SettingsActivity.this, "保存成功！", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }
}
