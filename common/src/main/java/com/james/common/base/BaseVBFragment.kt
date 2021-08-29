package com.james.common.base

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.base.delegate.*
import com.james.common.databinding.FragmentBaseBinding
import com.james.common.view.EmptyLayout
import kotlinx.android.synthetic.main.fragment_base.*
import org.greenrobot.eventbus.EventBus
import java.lang.NullPointerException


//If you want a layout file to be ignored while generating binding classes,
//add the tools:viewBindingIgnore="true" attribute to the root view of that layout file

//BasePresenter onCreate()--onDestroy
//BaseView 对页面，Dialog，Toast的操作
abstract class BaseVBFragment<VB : ViewBinding, P : BasePresenter> : Fragment(), BaseView {

    protected var context: AppCompatActivity? = null

    protected var mToolBarDelegate: ToolBarDelegate? = null
    protected var mLoadingDelegate: LoadingDelegate? = null
    protected var mPageLoadingDelegate: PageLoadingDelegate? = null

    protected var elEmpty: EmptyLayout? = null

    //引用EmptyLayout
    protected lateinit var emptyLayout: EmptyLayout

    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBundles()
        mPresenter = createPresenter()
        mPresenter?.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(useBaseLayout()) {
            mRootBinding = FragmentBaseBinding.inflate(layoutInflater, container, false);
            if(getBinding(inflater, container) != null) {
                binding = getBinding(inflater, container);
                mRootBinding.containerView.addView(binding.root);
            }
            return mRootBinding.root;
        } else {
            if(getBinding(inflater, container) == null) {
                throw NullPointerException("the method of getViewBinding is not null");
            }
            binding = getBinding(inflater, container);
            return binding.root;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        if(useBaseLayout()) {
            elEmpty = mRootBinding.elEmpty;
        } else {
            elEmpty = binding.root.findViewById(R.id.elEmpty);
        }
        initViewsAndData(view)
    }

    private fun isPageFinish(): Boolean {
        return (requireActivity() as Activity).isFinishing && !useBaseLayout()
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
        content?.apply {
            ToastUtils.setGravity(Gravity.CENTER, 0, 100)
            ToastUtils.showShort(this)
        }
    }

    override fun showBottomToast(content: String?) {
        content?.apply {
            ToastUtils.setGravity(Gravity.BOTTOM, 0, 100)
            ToastUtils.showShort(this)
        }
    }

    /**
     * 页面 Loading
     */
    protected open fun getPageLoadingDelegate(): PageLoadingDelegate {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = DefaultPageLoadingDelegate(requireContext())
            mPageLoadingDelegate?.setPageLoadingLayout(elEmpty!!)
        }
        return mPageLoadingDelegate!!
    }

    /**
     * DialogLoading
     */
    protected open fun getLoadingDelegate(): LoadingDelegate {
        if (mLoadingDelegate == null) {
            mLoadingDelegate = DefaultLoadingDelegate(requireContext())
        }
        return mLoadingDelegate!!
    }

    // ----------------------- Protected Method -----------------------

    protected abstract fun createPresenter(): P

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected lateinit var binding: VB

    protected lateinit var mRootBinding: FragmentBaseBinding

    protected open fun initBundles() {
        // do nothing
    }

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

