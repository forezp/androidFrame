package com.forezp.newframe.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
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


public class MainActivity extends ActionBarActivity {

    protected String TAG = "MainActivity";
    @Bind(R.id.user_avator)
    SimpleDraweeView userAvator;
    @Bind(R.id.sdv1)
    SimpleDraweeView sdv1;
    @Bind(R.id.sdv2)
    SimpleDraweeView sdv2;
    @Bind(R.id.sdv3)
    SimpleDraweeView sdv3;
    @Bind(R.id.sdv4)
    SimpleDraweeView sdv4;
    @Bind(R.id.sdv5)
    SimpleDraweeView sdv5;
    @Bind(R.id.sdv6)
    SimpleDraweeView sdv6;
    @Bind(R.id.gson_test)
    Button gsonTest;
    private Context mContext;
    private ArrayList<User> list=new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        initViews();

    }

    private void initViews() {
        Uri uri = Uri.parse("http://pica.nipic.com/2008-03-19/2008319183523380_2.jpg");
        //Uri uri=Uri.parse("http://artapp-dev-bucket.oss-cn-beijing.aliyuncs.com/exercise/a4.png");
        userAvator.setImageURI(uri);
        sdv1 = (SimpleDraweeView) findViewById(R.id.sdv1);
        sdv1.setImageURI(Uri.parse("http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"));
        sdv2 = (SimpleDraweeView) findViewById(R.id.sdv2);
        sdv2.setImageURI(Uri.parse("http://img.taopic.com/uploads/allimg/130501/240451-13050106450911.jpg"));
        sdv3 = (SimpleDraweeView) findViewById(R.id.sdv3);
        Uri uri1 = Uri.parse("http://pic.joke01.com/uppic/13-05/30/30215236.gif");
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri1)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request).setAutoPlayAnimations(true)
                .build();
        sdv3.setController(controller);
        //  sdv4=(SimpleDraweeView)findViewById(R.id.sdv4);
        sdv4.setImageURI(Uri.parse("http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"));
        // sdv5=(SimpleDraweeView)findViewById(R.id.sdv5);
        //sdv5.setImageURI(Uri.parse("http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"));
        // sdv6=(SimpleDraweeView)findViewById(R.id.sdv6);
        // sdv6.setImageURI(Uri.parse("http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"));
    }


    @OnClick(R.id.gson_test)
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
                                JSONArray  jsonArray=responseobj.getJSONArray("rows");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
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
                                    T.show(mContext,u.toString());
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
