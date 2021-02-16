package com.james.common.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.blankj.utilcode.util.ToastUtils;
import com.james.common.R;
import com.james.common.base.delegate.DefaultLoadingDelegate;
import com.james.common.base.delegate.DefaultPageLoadingDelegate;
import com.james.common.base.delegate.LoadingDelegate;
import com.james.common.base.delegate.PageLoadingDelegate;
import com.james.common.base.delegate.ToolBarDelegate;
import com.james.common.view.EmptyLayout;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lqx on 2016/9/20.
 * desc:通用的Activity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected AppCompatActivity context;

    protected RelativeLayout rlBaseRoot;
    protected Toolbar tlBar;
    protected EmptyLayout elEmpty;

    protected ToolBarDelegate mToolBarDelegate;
    protected LoadingDelegate mLoadingDelegate;
    protected PageLoadingDelegate mPageLoadingDelegate;

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBundles();
        if (useBaseLayout()) {
            setContentView(R.layout.activity_base);
        } else {
            if (getLayoutId() != 0) {
                setContentView(getLayoutId());
            }
        }
        context = this;
        mPresenter = createPresenter();
        mPresenter.onCreate();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        initSelf();
        addContentView();
        initView();
    }

    private void initSelf() {
        rlBaseRoot = findViewById(R.id.rl_base_root);
        tlBar = findViewById(R.id.tl_bar);
        elEmpty = findViewById(R.id.el_empty);

        mToolBarDelegate = new ToolBarDelegate(this, tlBar,useLightMode());
    }

    private void addContentView() {
        if (getLayoutId() != 0 && useBaseLayout()) {
            getLayoutInflater().inflate(getLayoutId(), findViewById(R.id.container), true);
        }
    }

    // ------------------- Override Method start ------------------
    protected void initBundles() {}

    @NonNull
    protected abstract P createPresenter();

    protected abstract void initView();

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 是否使用基类里面的布局，
     * @return true：将子类的布局作为 View 添加到父容器中。
     * @implNote 返回false时，并且使用showPageLoading时要在自定义布局中加入empty view
     */
    protected boolean useBaseLayout() {
        return true;
    }

    /**
     * 是否开启事件注册
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * DialogLoading
     */
    protected LoadingDelegate getLoadingDelegate() {
        if (mLoadingDelegate == null) {
            mLoadingDelegate = new DefaultLoadingDelegate(this);
        }
        return mLoadingDelegate;
    }

    /**
     * 页面 Loading
     */
    protected PageLoadingDelegate getPageLoadingDelegate() {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = new DefaultPageLoadingDelegate(this);
            mPageLoadingDelegate.setPageLoadingLayout(elEmpty);
        }
        return mPageLoadingDelegate;
    }
    // ------------------- Override Method end ------------------

    // ----------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void showPageLoading() {
        getPageLoadingDelegate().showPageLoading();
    }

    @Override
    public void hidePageLoading() {
        getPageLoadingDelegate().hidePageLoading();
    }

    @Override
    public void showNoData() {
        getPageLoadingDelegate().showNoData();
    }

    @Override
    public void showNoNetwork() {
        getPageLoadingDelegate().showNoNetwork();
    }

    @Override
    public void showDataError() {
        getPageLoadingDelegate().showDataError();
    }

    @Override
    public void showDialogLoading() {
        getLoadingDelegate().showDialogLoading(getString(R.string.loading));
    }

    @Override
    public void showDialogLoading(@Nullable String content) {
        getLoadingDelegate().showDialogLoading(content);
    }

    @Override
    public void hideDialogLoading() {
        getLoadingDelegate().hideDialogLoading();
    }

    @Override
    public void showToast(String content) {
        if (!TextUtils.isEmpty(content)) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showShort(content);
        }
    }

    @Override
    public void showBottomToast(String content) {
        if (!TextUtils.isEmpty(content)) {
            ToastUtils.setGravity(Gravity.BOTTOM, 0, 100);
            ToastUtils.showShort(content);
        }
    }

    public boolean useLightMode() {
        return true;
    }

}
