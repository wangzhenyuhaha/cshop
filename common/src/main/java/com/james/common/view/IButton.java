package com.james.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.james.common.R;


/**
 * 支持圆角.按下效果的TextView
 */
public class IButton extends AppCompatTextView {

    public IButton(Context context) {
        this(context, null);
    }

    public IButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IView, defStyleAttr, 0);

        if (attributes != null) {

            int itvBorderWidth = attributes.getDimensionPixelSize(R.styleable.IView_ivBorderWidth, 0);
            int itvBorderColor = attributes.getColor(R.styleable.IView_ivBorderColor, Color.WHITE);
            float itvRadius = attributes.getDimension(R.styleable.IView_ivRadius, 0);
            int itvBgColor = attributes.getColor(R.styleable.IView_ivBgColor, Color.TRANSPARENT);
            int itvBgSelectedColor = attributes.getColor(R.styleable.IView_ivBgSelectedColor, getResources().getColor(R.color.gray_bg_click));
            attributes.recycle();

            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(itvBgColor);
            gd.setCornerRadius(itvRadius);
            if (itvBorderWidth > 0) {
                gd.setStroke(itvBorderWidth, itvBorderColor);
            }

//            this.setBackground(gd);
            StateListDrawable stateListDrawable = getStateListDrawable(getContext(),itvBgColor,itvBgSelectedColor,itvRadius);
            setBackgroundDrawable(stateListDrawable);
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColor(color);
    }

    private static StateListDrawable getStateListDrawable(Context context,int color,int selectedColor,float corner) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        float[] roundedCorner = new float[]{corner, corner, corner, corner, corner, corner, corner, corner};
        float[] innerRoundedCorner = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
        RoundRectShape roundedShape = new RoundRectShape(roundedCorner, null, innerRoundedCorner);

        ShapeDrawable pressedDrawable = new ShapeDrawable(roundedShape);
        pressedDrawable.getPaint().setColor(selectedColor);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);

        ShapeDrawable normalDrawable = new ShapeDrawable(roundedShape);
        normalDrawable.getPaint().setColor(color);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        return stateListDrawable;
    }
}

