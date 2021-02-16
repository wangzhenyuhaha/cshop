package com.james.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.base.delegate.DefaultLoadingDelegate
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.LoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.view.EmptyLayout
import kotlinx.android.synthetic.main.fragment_base.*
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
abstract class BaseFragment<P : BasePresenter> : Fragment(), BaseView {

    protected lateinit var mContext: Context
    protected var mPresenter: P? = null
    protected var mLoadingDelegate: LoadingDelegate? = null
    protected var mPageLoadingDelegate: PageLoadingDelegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBundles()
        mPresenter = createPresenter()
        mPresenter?.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getLayoutId() == null || getLayoutId() == 0) {
            return null
        }
        // 使用子 Fragment 自己的布局
        if (!useBaseLayout()) {
            return inflater.inflate(getLayoutId()!!, parent, false)
        }

        // 将子布局添加到父布局中
        val rootView = inflater.inflate(R.layout.fragment_base, parent, false)
        val containerView = rootView.findViewById<FrameLayout>(R.id.containerView)
        inflater.inflate(getLayoutId()!!, containerView, true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        initViewsAndData(view)
    }

    private fun isPageFinish(): Boolean {
        return (mContext as Activity).isFinishing && !useBaseLayout()
    }

    override fun showPageLoading() {
        if (isPageFinish()) return
        elEmpty.setErrorType(EmptyLayout.LOADING)
    }

    override fun hidePageLoading() {
        if (isPageFinish()) return
        elEmpty.setErrorType(EmptyLayout.HIDE_LAYOUT)
    }

    override fun showNoData() {
        if (isPageFinish()) return
        elEmpty.setErrorType(EmptyLayout.NO_DATA)
    }

    override fun showNoNetwork() {
        if (isPageFinish()) return
        elEmpty.setErrorType(EmptyLayout.NO_NETWORK)
    }

    override fun showDataError() {
        if (isPageFinish()) return
        elEmpty.setErrorType(EmptyLayout.DATA_ERROR)
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
        content?.apply {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0)
            ToastUtils.showShort(this)
        }
    }

    override fun showBottomToast(content: String?) {
        content?.apply {
            ToastUtils.setGravity(Gravity.BOTTOM, 0, 100)
            ToastUtils.showShort(this)
        }
    }

    protected fun getLoadingDelegate(): LoadingDelegate {
        if (mLoadingDelegate == null) {
            mLoadingDelegate = DefaultLoadingDelegate(mContext)
        }
        return mLoadingDelegate as LoadingDelegate
    }

    protected open fun getPageLoadingDelegate(): PageLoadingDelegate? {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = DefaultPageLoadingDelegate(mContext).apply {
                setPageLoadingLayout(elEmpty)
            }
        }
        return mPageLoadingDelegate
    }

    // ----------------------- Protected Method -----------------------
    protected abstract fun getLayoutId(): Int?
    protected open fun initBundles() {
        // do nothing
    }

    protected abstract fun createPresenter(): P?
    protected abstract fun initViewsAndData(rootView: View)

    /**
     * 是否使用基类里面的布局，
     * @return true：将子类的布局作为 View 添加到父容器中。
     */
    open fun useBaseLayout(): Boolean {
        return true
    }

    /**
     * 是否开启事件注册
     */
    protected open fun useEventBus(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}