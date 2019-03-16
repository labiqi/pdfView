package com.example.lcq.myapp.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BaseView extends View {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public BaseView(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
}
