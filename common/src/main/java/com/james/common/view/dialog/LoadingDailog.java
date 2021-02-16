package com.james.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.james.common.R;
import com.james.common.utils.exts.ViewExtKt;

/**
 * Author : Elson
 * Date   : 2020/7/7
 * Desc   :
 */
public class LoadingDailog extends Dialog {

    private TextView loadingMsgTv;

    public LoadingDailog(Context context) {
        super(context);
    }

    public LoadingDailog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setLoadingText(String content) {
        if (loadingMsgTv == null) {
            View rootView = getWindow().getDecorView();
            if (rootView != null) {
                loadingMsgTv = findViewById(R.id.tipTextView);
            }
        }
        if (loadingMsgTv != null) {
            ViewExtKt.show(loadingMsgTv, !TextUtils.isEmpty(content));
            loadingMsgTv.setText(content);
        }
    }

    public static class Builder {
        private Context context;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置是否可以按返回键取消
         */
        public LoadingDailog.Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以点击外部取消
         */
        public LoadingDailog.Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public LoadingDailog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            LoadingDailog loading = new LoadingDailog(context, R.style.LoadingDialogStyle);
            loading.setContentView(view);
            loading.setCancelable(isCancelable);
            loading.setCanceledOnTouchOutside(isCancelOutside);
            //实现loading的透明度
            WindowManager.LayoutParams lp=loading.getWindow().getAttributes();
            lp.alpha = 0.6f;
            loading.getWindow().setAttributes(lp);
            return loading;
        }
    }

}
