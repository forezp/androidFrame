package com.forezp.newframe;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by b508a on 2016/3/30.
 */
public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
        OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);

        OkHttpUtils.getInstance().setCertificates();
    }
}
