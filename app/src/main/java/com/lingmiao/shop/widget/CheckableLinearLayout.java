package com.lingmiao.shop.widget;

/**
 * Create Date : 2021/4/284:32 PM
 * Auther      : Fox
 * Desc        :
 **/

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by new on 2018/2/27.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private boolean mChecked = false;

    private OnClickListener mListener;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //点击的时候让控件变为选中状态，再次点击就取消选中状态
    public void init(){
        super.setOnClickListener(v -> {
            toggle();
            if(mListener != null){
                mListener.onClick(v);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mListener = l;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

}