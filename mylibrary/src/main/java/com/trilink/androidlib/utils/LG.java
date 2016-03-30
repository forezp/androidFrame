package com.trilink.androidlib.utils;



import android.util.Log;

/**
 * Created by b508a on 2015/12/28.
 */
public class LG {

    /**
     * 是否开启debug
     */
    public static boolean isDebug=true;


    /**
     * 错误
     *
     * 2014-5-8
     * @param TAG
     * @param msg
     */
    public static void e(String TAG,String msg){
        if(isDebug){
            Log.e(TAG, msg+"");
        }
    }

    /**
     * 错误
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void e(Class<?> clazz,String msg){
        if(isDebug){
            Log.e(clazz.getSimpleName(), msg+"");
        }
    }
    /**
     * 信息
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void i(Class<?> clazz,String msg){
        if(isDebug){
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }
    /**
     * 警告
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void w(Class<?> clazz,String msg){
        if(isDebug){
            Log.w(clazz.getSimpleName(), msg+"");
        }
    }
}



