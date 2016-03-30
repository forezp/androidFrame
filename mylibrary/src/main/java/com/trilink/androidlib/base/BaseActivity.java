package com.trilink.androidlib.base;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;

/**
 * Created by b508a on 2016/1/25.
 */
public class BaseActivity extends  BaseFragmentActivity {


    /**
     * 获取当前view的LayoutInflater实例
     * @return
     */
    protected LayoutInflater getLayouInflater() {
        LayoutInflater _LayoutInflater = LayoutInflater.from(this);
        return _LayoutInflater;
    }

    /**
     * 弹出toast 显示时长short
     * @param pMsg
     */
    protected void showToastMsg(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出toast 显示时长short
     * @param pMsg
     */
    protected void showToastMsgShort(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 弹出toase 显示时长long
     * @param pMsg
     */
    protected void showToastMsgLong(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
    }
    /**
     * 根据传入的类(class)打开指定的activity
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent _Intent = new Intent();
        _Intent.setClass(this, pClass);
        startActivity(_Intent);
    }

    protected void openActivityByIntent(Intent pIntent){
        startActivity(pIntent);
    }

}
