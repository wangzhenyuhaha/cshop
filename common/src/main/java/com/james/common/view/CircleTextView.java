package com.james.common.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.james.common.R;

public class CircleTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint paint;
    float circleRadius = 100;
    float centerX;
    float centerY;
    private String text = "测试";
    int color;
    Rect rect = new Rect();


    public  CircleTextView(Context context) {
        super(context);
        init();
    }

    public  CircleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        String string = typedArray.getString(R.styleable.CircleTextView_circleTint);
        color = Color.parseColor(string);
        init();
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        String string = typedArray.getString(R.styleable.CircleTextView_circleTint);
        color = Color.parseColor(string);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        circleRadius = centerX;
        //绘制一个圆形
        canvas.drawCircle(centerX, centerY, circleRadius, paint);

        super.onDraw(canvas);


    }
}
