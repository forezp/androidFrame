package com.forezp.newframe.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forezp.newframe.R;
import com.forezp.newframe.common.Constants;
import com.forezp.newframe.common.URL;
import com.trilink.androidlib.base.BaseActivity;
import com.trilink.androidlib.utils.CheckUtils;
import com.trilink.androidlib.utils.LG;
import com.trilink.androidlib.utils.NetUtils;
import com.trilink.androidlib.utils.StringUtils;
import com.trilink.androidlib.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by b508a on 2016/3/30.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_login_input_name)
    EditText etLoginInputName;
    @Bind(R.id.et_login_input_password)
    EditText etLoginInputPassword;
    @Bind(R.id.tv_forget_pwd_login)
    TextView tvForgetPwdLogin;
    @Bind(R.id.btn_confirm_login)
    Button btnConfirmLogin;
    @Bind(R.id.tv_register_login)
    TextView tvRegisterLogin;
    private Context mContext;
    private SharedPreferences sp;
    private final String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        tvRegisterLogin.setText(Html.fromHtml("<u>没有账号，立即注册</u>"));

    }

    @OnClick({R.id.tv_forget_pwd_login, R.id.btn_confirm_login, R.id.tv_register_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd_login:


                break;
            case R.id.btn_confirm_login:
                login();
                break;
            case R.id.tv_register_login:
                break;
        }
    }

    /**
     * confirm login
     */
    private  void login(){


        String telstr = etLoginInputName.getText().toString();
        String pwdstr = etLoginInputPassword.getText().toString();
        if (telstr.equals("") || pwdstr.equals("")) {
            T.show(this, "手机号或密码不能为空");
            return;
        }
        if (!CheckUtils.isMobileNum(telstr)) {
            T.show(this, "手机号码输入有误");
            return;
        }
        String pwd = StringUtils.MD5(pwdstr);
        doLogin(telstr, pwd);

    }

    /**
     * login
     * @param mobile
     * @param password
     */
    private  void doLogin(final String mobile, final String password){


        if (!NetUtils.checkNetWorkIsAvailable(this)) {

            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String ,String > params=new HashMap<String ,String>(){
            {
                put("mobile",mobile);
                put("password",password);
            }
        };
        String url = URL.PATH + URL.map.get("login");
        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                        LG.e(TAG,e.getMessage());
                    }
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject responseobj = new JSONObject(s);
                            boolean  isSucess = responseobj.getBoolean("success");
                            if (isSucess) {
                                JSONObject jsonBody=responseobj.getJSONObject("body");
                                JSONObject jsonUser=jsonBody.getJSONObject("User");
                                int id=jsonUser.getInt("id");
                                String mobile=jsonUser.getString("mobile");
                                String password=jsonUser.getString("password");
                                String userName=jsonUser.getString("userName");
                                SharedPreferences.Editor ed = sp.edit();
                                ed.putString(Constants.SP_MOBILE, mobile);
                                ed.putString(Constants.SP_PASSWORD, password);
                                ed.putInt(Constants.SP_ID, id);
                                ed.putString(Constants.SP_USERNAME, userName);
                                ed.commit();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                               startActivity(intent);
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            } else {
                                String msg=responseobj.getString("msg");
                                T.show(mContext, msg);

                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "登录失败",Toast.LENGTH_SHORT).show();
                            Log.i("--loginFaild--", e.getMessage());

                        }
                    }
                });
    }
}
