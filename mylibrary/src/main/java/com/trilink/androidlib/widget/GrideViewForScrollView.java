package com.trilink.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;



/**
 *
 * gridview 和scrollView 嵌套
 *
 */
public class GrideViewForScrollView extends GridView {
    public GrideViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrideViewForScrollView(Context context) {
        super(context);
    }

    public GrideViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}