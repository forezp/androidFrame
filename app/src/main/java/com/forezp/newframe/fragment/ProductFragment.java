package com.forezp.newframe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.forezp.newframe.R;
import com.forezp.newframe.common.URL;
import com.forezp.newframe.domain.User;
import com.google.gson.Gson;
import com.trilink.androidlib.utils.LG;
import com.trilink.androidlib.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by b508a on 2016/3/31.
 */
public class ProductFragment extends Fragment {

    @Bind(R.id.btn_okhttp_test)
    Button btnOkhttpTest;
    @Bind(R.id.tv_content)
    TextView tvContent;
    private View rootView;
    private Context mContext;
    private final String TAG="ProductFragment";
    private ArrayList<User> list =new ArrayList<User>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_okhttp, container, false);
        ButterKnife.bind(this, rootView);
        mContext=getActivity();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_okhttp_test)
    public void onClick() {

        String url = URL.PATH + URL.map.get("getAllUsers");
        OkHttpUtils.post()
                .url(url)

                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LG.e(TAG, e.getMessage());
                    }
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject responseobj = new JSONObject(s);
                            boolean isSucess = responseobj.getBoolean("success");
                            if (isSucess) {
                                JSONArray jsonArray=responseobj.getJSONArray("rows");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    /**
                                     * gson  解析
                                     */
                                    User u= new Gson().fromJson(jsonObject.toString(),User.class);
                                    list.add(u);
                                }
//                                JSONObject jsonBody=responseobj.getJSONObject("body");
//                                JSONObject jsonUser=jsonBody.getJSONObject("User");
//                                int id=jsonUser.getInt("id");
//                                String mobile=jsonUser.getString("mobile");
//                                String password=jsonUser.getString("password");
//                                String userName=jsonUser.getString("userName");
//                                SharedPreferences.Editor ed = sp.edit();
//                                ed.putString(Constants.SP_MOBILE, mobile);
//                                ed.putString(Constants.SP_PASSWORD, password);
//                                ed.putInt(Constants.SP_ID, id);
//                                ed.putString(Constants.SP_USERNAME, userName);
//                                ed.commit();
//                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                                startActivity(intent);
//
                                if(list.size()>0){
                                    User u=list.get(0);
                                    tvContent.setText(u.toString());
                                    T.show(mContext, u.toString());
                                }
                            } else {
                                String msg = responseobj.getString("msg");
                                T.show(mContext, msg);
                            }
                        } catch (Exception e) {
                            //Log.i("--loginFaild--", e.getMessage());
                        }
                    }
                });
    }

}
