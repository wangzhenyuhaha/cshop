package com.james.common.base

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.base.delegate.*
import com.james.common.databinding.ActivityBaseBinding
import com.james.common.view.EmptyLayout
import org.greenrobot.eventbus.EventBus


//Presenter 生命周期方法
//BaseView 页面信息显示，Dialog显示，Toast显示
abstract class BaseVBActivity<VB : ViewBinding, P : BasePresenter> : AppCompatActivity(), BaseView {

    protected var context: AppCompatActivity? = null

    protected var rlBaseRoot: RelativeLayout? = null
    protected var tlBar: Toolbar? = null
    protected var elEmpty: EmptyLayout? = null

    protected var mToolBarDelegate: ToolBarDelegate? = null
    protected var mLoadingDelegate: LoadingDelegate? = null
    protected var mPageLoadingDelegate: PageLoadingDelegate? = null

    //ViewBinding
    protected lateinit var mBinding: VB
    protected var mPresenter: P? = null
    protected var savedInstanceState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        initBundles()
        if (useBaseLayout()) {
            mBinding = ActivityBaseBinding.inflate(layoutInflater) as VB;
            setContentView(mBinding.root)
        } else {
            if (getViewBinding() != null) {
                mBinding = getViewBinding();
                setContentView(mBinding.root)
            }
        }
        context = this
        mPresenter = createPresenter()
        mPresenter!!.onCreate()
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initSelf()
//        addContentView()
        initView()
    }

    fun initSelf() {
        rlBaseRoot = findViewById(R.id.rl_base_root)
        tlBar = findViewById(R.id.tl_bar)
        elEmpty = findViewById(R.id.el_empty)
        mToolBarDelegate = ToolBarDelegate(this, tlBar, useLightMode())
    }

//    fun addContentView() {
//        if (getLayoutId() != 0 && useBaseLayout()) {
//            layoutInflater.inflate(getLayoutId(), findViewById(R.id.container), true)
//        }
//    }

    // ------------------- Override Method start ------------------
    protected open fun initBundles() {}

    protected abstract fun createPresenter(): P

    protected abstract fun getViewBinding(): VB

    protected abstract fun initView()

    /**
     * 是否使用基类里面的布局，
     * @return true：将子类的布局作为 View 添加到父容器中。
     * @implNote 返回false时，并且使用showPageLoading时要在自定义布局中加入empty view
     */
    protected open fun useBaseLayout(): Boolean {
        return false
    }


    /**
     * 是否开启事件注册
     */
    protected open fun useEventBus(): Boolean {
        return false
    }

    /**
     * DialogLoading
     */
    protected open fun getLoadingDelegate(): LoadingDelegate {
        if (mLoadingDelegate == null) {
            mLoadingDelegate = DefaultLoadingDelegate(this)
        }
        return mLoadingDelegate!!
    }

    /**
     * 页面 Loading
     */
    protected open fun getPageLoadingDelegate(): PageLoadingDelegate {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = DefaultPageLoadingDelegate(this)
            mPageLoadingDelegate?.setPageLoadingLayout(elEmpty!!)
        }
        return mPageLoadingDelegate!!
    }
    // ------------------- Override Method end ------------------

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun showPageLoading() {
        getPageLoadingDelegate().showPageLoading()
    }

    override fun hidePageLoading() {
        getPageLoadingDelegate().hidePageLoading()
    }

    override fun showNoData() {
        getPageLoadingDelegate().showNoData()
    }

    override fun showNoNetwork() {
        getPageLoadingDelegate().showNoNetwork()
    }

    override fun showDataError() {
        getPageLoadingDelegate().showDataError()
    }

    override fun showDialogLoading() {
        getLoadingDelegate().showDialogLoading(getString(R.string.loading))
    }

    override fun showDialogLoading(content: String?) {
        getLoadingDelegate().showDialogLoading(content)
    }

    override fun hideDialogLoading() {
        getLoadingDelegate().hideDialogLoading()
    }

    override fun showToast(content: String?) {
        if (!TextUtils.isEmpty(content)) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0)
            ToastUtils.showShort(content)
        }
    }

    override fun showBottomToast(content: String?) {
        if (!TextUtils.isEmpty(content)) {
            ToastUtils.setGravity(Gravity.BOTTOM, 0, 100)
            ToastUtils.showShort(content)
        }
    }

    /**
     * note :
     * true : 底部白菜
     * false: 主题色
     * @return 默认为 true
     */
    open fun useLightMode(): Boolean {
        return true
    }

}
