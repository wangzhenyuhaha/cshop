package com.james.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.james.common.R;

/**
 * 支持圆角的TextView
 */
public class ITextView extends AppCompatTextView {

    public ITextView(Context context) {
        this(context, null);
    }

    public ITextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ITextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IView, defStyleAttr, 0);

        if (attributes != null) {

            int itvBorderWidth = attributes.getDimensionPixelSize(R.styleable.IView_ivBorderWidth, 0);
            int itvBorderColor = attributes.getColor(R.styleable.IView_ivBorderColor, Color.WHITE);
            float itvRadius = attributes.getDimension(R.styleable.IView_ivRadius, 0);
            int itvBgColor = attributes.getColor(R.styleable.IView_ivBgColor, Color.TRANSPARENT);
            attributes.recycle();

            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(itvBgColor);
            gd.setCornerRadius(itvRadius);
            if (itvBorderWidth > 0) {
                gd.setStroke(itvBorderWidth, itvBorderColor);
            }

            this.setBackground(gd);
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColor(color);
    }
}

