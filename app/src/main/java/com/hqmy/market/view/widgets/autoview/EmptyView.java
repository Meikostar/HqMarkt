package com.hqmy.market.view.widgets.autoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;


public class EmptyView extends LinearLayout {
    private ImageView img_empty;
    private TextView tv_empty_tip;

    public EmptyView(Context context) {
        this(context, null, 0);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(context != null){
            inflate(context, R.layout.empty_view, this);
        }
        img_empty = findViewById(R.id.img_empty);
        tv_empty_tip = findViewById(R.id.tv_empty_tip);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setTvEmptyTip(String tip) {
        tv_empty_tip.setText(tip);
    }
}
