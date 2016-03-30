package com.forezp.newframe.common;

import java.util.HashMap;

/**
 * Created by b508a on 2016/3/30.
 */
public class URL {


    public static final String PATH="http://192.168.9.61:8080/ArtAppAdmin/rest/";

    public static final HashMap map=new HashMap<String, String>(){

        {
            put("login", "user/login");//登录
            put("regist", "user/regist");
            put("updatePassword", "user/updatePassword");
            put("forgetPassword", "user/forgetPassword");
            put("sendMsg", "user/sendMsg");
            put("getAllUsers","user/getAllUsers");

        }
    };


}
