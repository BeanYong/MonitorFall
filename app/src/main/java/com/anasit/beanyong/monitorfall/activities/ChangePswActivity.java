package com.anasit.beanyong.monitorfall.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.entity.MonitorManager;
import com.anasit.beanyong.monitorfall.util.StringUtil;

/**
 * Created by BeanYong on 2015/12/16.
 */
public class ChangePswActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOldPsw, mNewPsw, mConfirmPsw;
    private ImageView mCancel, mOk;
    private int mMonitorId;//要更改的监视器id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        mMonitorId = getIntent().getIntExtra("monitor_id", 0);
        System.out.println("monitor_id = " + mMonitorId);
        initView();
        initEvent();
    }

    private void initEvent() {
        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    private void initView() {
        mCancel = (ImageView) findViewById(R.id.change_psw_cancel);
        mOk = (ImageView) findViewById(R.id.change_psw_ok);
        mOldPsw = (EditText) findViewById(R.id.change_psw_old_psw);
        mNewPsw = (EditText) findViewById(R.id.change_psw_new_psw);
        mConfirmPsw = (EditText) findViewById(R.id.change_psw_confirm_psw);
    }

    /**
     * 更改密码
     */
    private void changePsw() {
        String oldPsw = mOldPsw.getText().toString().trim();
        String newPsw = mNewPsw.getText().toString().trim();
        String confirmPsw = mConfirmPsw.getText().toString().trim();

        if (TextUtils.isEmpty(oldPsw) && StringUtil.isConsistent(newPsw, confirmPsw)) {//旧密码不为空，新密码与确认密码一致
            MonitorManager manager = MonitorManager.getInstance();
            manager.changePsw(this, manager.getMonitorById(mMonitorId), oldPsw, newPsw);
        } else {
            Toast.makeText(this, "密码为空或两次输入密码不一致", Toast.LENGTH_SHORT).show();
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
            case R.id.change_psw_cancel://取消
                onDestroy();
                break;
            case R.id.change_psw_ok://确定
                changePsw();
                break;
        }
    }
}
