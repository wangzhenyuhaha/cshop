package com.james.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.james.common.R;


/**
 * 错误页面
 * lqx
 */
public class EmptyLayout extends RelativeLayout {

    public static final int NO_NETWORK = 1;//没有网络
    public static final int LOADING = 2;//加载
    public static final int NO_DATA = 3;//没有数据
    public static final int HIDE_LAYOUT = 4;//全部隐藏
    public static final int NODATA_ENABLE_CLICK = 5;//没有数据,可以点击
    public static final int DATA_ERROR = 6;//数据错误

    private final Context context;
    private int errorState;


    private ProgressBar pwLoading;
    private TextView tvError;
    private ImageView ivError;


    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_error_layout, this);
        setBackgroundColor(-1);
        pwLoading = view.findViewById(R.id.pw_loading);
        ivError = view.findViewById(R.id.iv_error_layout);
        tvError = view.findViewById(R.id.tv_error_layout);
    }

//    public void setNetworkRefreshListener(OnClickListener listener) {
//        ivError.setOnClickListener(listener);
//    }

    public void dismiss() {
        errorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return errorState;
    }

    public boolean isLoadError() {
        return errorState == NO_NETWORK;
    }

    public boolean isLoading() {
        return errorState == LOADING;
    }


//    public void setDataErrorText(String text) {
//
//    }


//    public void setErrorImage(@DrawableRes int imgResource) {
//        img.setImageResource(imgResource);
//    }

    public void setErrorType(int errorType) {
        errorState = errorType;
        if (errorState == HIDE_LAYOUT) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        pwLoading.setVisibility(View.GONE);
//        pwLoading.stopSpinning();
//        tvError.setVisibility(View.GONE);
        switch (errorType) {
            case NO_NETWORK://网络错误
//                ivError.setImageResource(R.mipmap.ic_no_network);
                tvError.setText(R.string.error_view_network_error_click_to_refresh);
                break;
            case LOADING://加载
                pwLoading.setVisibility(View.VISIBLE);
//                pwLoading.startSpinning();
//                tvError.setVisibility(View.VISIBLE);
                tvError.setText(R.string.loading);
                break;
            case NO_DATA://没有数据
//                ivError.setImageResource(R.mipmap.ic_no_content);
                tvError.setText(R.string.error_view_no_data);
                break;
            case NODATA_ENABLE_CLICK://没有数据,可以点击
//                ivError.setImageResource(R.mipmap.ic_no_content);
                tvError.setText(R.string.error_view_no_data);
                break;
            case DATA_ERROR://数据错误
//                ivError.setImageResource(R.mipmap.ic_data_error);
                tvError.setText(R.string.error_view_data_error);
                break;
            default:
                break;
        }
    }

//    public void setNoDataContent(String noDataContent) {
//        strNoDataContent = noDataContent;
//    }
//
//
//    public void setTvNoDataContent() {
//        if (!TextUtils.isEmpty(strNoDataContent))
//            tv.setText(strNoDataContent);
//        else
//            tv.setText(R.string.error_view_no_data);
//    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            errorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }



    public void setNoDataImage(int resId) {
        ivError.setImageResource(resId);
    }

}
