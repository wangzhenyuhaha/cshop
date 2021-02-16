package com.lingmiao.shop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.Utils;
import com.lingmiao.shop.R;


/**
 * 订单tab view
 */
public class IOrderTabView extends RelativeLayout {
    private TextView tvTabTitle;
    private TextView tvTabNumber;
    private View viTabLine;

    public IOrderTabView(Context context) {
        this(context, null);
    }

    public IOrderTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IOrderTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IOrderTabView, defStyleAttr, 0);

        if (attributes != null) {

            boolean isSelected = attributes.getBoolean(R.styleable.IOrderTabView_tvSelected, false);
            String title = attributes.getString(R.styleable.IOrderTabView_tvTitle);
            attributes.recycle();

            View view;
            if("售后/退款".equals(title)){
                view = View.inflate(context, R.layout.order_view_tab_after_sale, this);
            }else{
                view = View.inflate(context, R.layout.order_view_tab_single, this);
            }

            tvTabTitle = view.findViewById(R.id.tvTabTitle);
            tvTabNumber = view.findViewById(R.id.tvTabNumber);
            viTabLine = view.findViewById(R.id.viTabLine);
            tvTabTitle.setText(title);
            tvTabNumber.setVisibility(View.GONE);
            viTabLine.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            tvTabTitle.setTextColor(ContextCompat.getColor(context,isSelected?R.color.color_3870EA:R.color.color_black));
        }
    }

    public void setTabNumber(int number) {
        tvTabNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
        String numberString = number > 99 ? "99+" : (number + "");
        tvTabNumber.setText(numberString);
    }

    public void setTabSelected(Boolean isSelected) {
        viTabLine.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        tvTabTitle.setTextColor(ContextCompat.getColor(Utils.getApp(),isSelected?R.color.color_3870EA:R.color.color_black));
    }

}

