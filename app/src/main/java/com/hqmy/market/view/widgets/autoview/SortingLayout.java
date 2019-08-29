package com.hqmy.market.view.widgets.autoview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hqmy.market.R;

/**
 * 自定义排序视图
 * Created by rzb on 2019/5/4.
 */
public class SortingLayout extends LinearLayout implements View.OnClickListener {
    Context context;
    LinearLayout sale_layout, price_layout;
    TextView all_tv, sale_tv, price_tv;
    ImageView sale_iv, price_iv;
    int sale_type = 2;
    int price_type = 2;
    ClickListener listener;

    public SortingLayout(Context context) {
        this(context, null);
    }

    public SortingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = inflate(context, R.layout.sorting_layout, this);
        sale_layout = (LinearLayout) view.findViewById(R.id.sale_layout);
        price_layout = (LinearLayout) view.findViewById(R.id.price_layout);
        all_tv = (TextView) view.findViewById(R.id.all_tv);
        sale_tv = (TextView) view.findViewById(R.id.sale_tv);
        price_tv = (TextView) view.findViewById(R.id.price_tv);
        sale_iv = (ImageView) view.findViewById(R.id.sale_iv);
        price_iv = (ImageView) view.findViewById(R.id.price_iv);
        all_tv.setOnClickListener(this);
        sale_layout.setOnClickListener(this);
        price_layout.setOnClickListener(this);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_tv://综合排序
                all_tv.setTextColor(Color.parseColor("#DF443B"));
                sale_tv.setTextColor(Color.parseColor("#212121"));
                price_tv.setTextColor(Color.parseColor("#212121"));
                sale_iv.setImageResource(R.mipmap.arrow_gray);
                price_iv.setImageResource(R.mipmap.arrow_gray);
                sale_type = 1;
                price_type = 1;
                if (listener != null) {
                    listener.all();
                }
                break;
            case R.id.sale_layout://按销量排序
                all_tv.setTextColor(Color.parseColor("#212121"));
                sale_tv.setTextColor(Color.parseColor("#DF443B"));
                price_tv.setTextColor(Color.parseColor("#212121"));
                if (sale_type == 1) {
                    sale_iv.setImageResource(R.mipmap.arrow_03);
                    sale_type = 2;
                    if (listener != null) {
                        listener.saleDesc();
                    }
                } else {
                    sale_type = 1;
                    sale_iv.setImageResource(R.mipmap.arrow_02);
                    if (listener != null) {
                        listener.saleAsc();
                    }
                }
                price_iv.setImageResource(R.mipmap.arrow_gray);
                break;
            case R.id.price_layout://按价格排序

                all_tv.setTextColor(Color.parseColor("#212121"));
                sale_tv.setTextColor(Color.parseColor("#212121"));
                price_tv.setTextColor(Color.parseColor("#DF443B"));
                if (price_type == 1) {
                    price_iv.setImageResource(R.mipmap.arrow_03);
                    price_type = 2;
                    if (listener != null) {
                        listener.priceDesc();
                    }
                } else {
                    price_type = 1;
                    price_iv.setImageResource(R.mipmap.arrow_02);
                    if (listener != null) {
                        listener.priceAsc();
                    }
                }
                sale_iv.setImageResource(R.mipmap.arrow_gray);
                break;
        }
    }

    public interface ClickListener {
        void all();//综合排序

        void saleDesc();//按销量降序

        void saleAsc();//按销量升序

        void priceDesc();//按价格降序

        void priceAsc();//按价格升序
    }
}
