package com.forezp.newframe.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.forezp.newframe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by b508a on 2016/3/31.
 */
public class HomeFragment extends Fragment {
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
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initViews() {
        Uri uri = Uri.parse("http://pica.nipic.com/2008-03-19/2008319183523380_2.jpg");
        //Uri uri=Uri.parse("http://artapp-dev-bucket.oss-cn-beijing.aliyuncs.com/exercise/a4.png");
        userAvator.setImageURI(uri);
      //  sdv1 = (SimpleDraweeView) findViewById(R.id.sdv1);
        sdv1.setImageURI(Uri.parse("http://baike.soso.com/p/20090711/20090711101754-314944703.jpg"));
       // sdv2 = (SimpleDraweeView) findViewById(R.id.sdv2);
        sdv2.setImageURI(Uri.parse("http://img.taopic.com/uploads/allimg/130501/240451-13050106450911.jpg"));
        //sdv3 = (SimpleDraweeView) findViewById(R.id.sdv3);
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

}
