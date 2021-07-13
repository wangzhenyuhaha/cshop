package com.james.common.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.base.delegate.DefaultLoadingDelegate
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.LoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.view.EmptyLayout


//If you want a layout file to be ignored while generating binding classes,
//add the tools:viewBindingIgnore="true" attribute to the root view of that layout file

//BasePresenter onCreate()--onDestroy
//BaseView 对页面，Dialog，Toast的操作
abstract class BaseVBFragment<VB : ViewBinding, P : BasePresenter> : Fragment(), BaseView {


    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    //引用EmptyLayout
    protected lateinit var emptyLayout: EmptyLayout

    protected var mPresenter: P? = null

    //造作Dialog
    protected val mLoadingDelegate: LoadingDelegate by lazy {
        DefaultLoadingDelegate(requireActivity())
    }

    //操作EmptyLayout
    protected val mPageLoadingDelegate: PageLoadingDelegate by lazy {
        DefaultPageLoadingDelegate(requireActivity()).apply {
            setPageLoadingLayout(emptyLayout)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBundles()
        mPresenter = createPresenter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyLayout = view.findViewById(R.id.el_empty)

        //初始化数据
        initViewsAndData(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //---------------------override---------------------

    protected abstract fun createPresenter(): P?

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun initViewsAndData(rootView: View)

    //---------------------method---------------------

    protected open fun initBundles() {
    }

    //---------------------重写BaseView中的抽象方法---------------------

    //查看当前Activity是否完成
    private fun isPageFinish(): Boolean {
        return requireActivity().isFinishing
    }


    override fun showPageLoading() {
        if (isPageFinish()) return
        emptyLayout.setErrorType(EmptyLayout.LOADING)
    }

    override fun hidePageLoading() {
        if (isPageFinish()) return
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
    }

    override fun showNoData() {
        if (isPageFinish()) return
        emptyLayout.setErrorType(EmptyLayout.NO_DATA)
    }

    override fun showNoNetwork() {
        if (isPageFinish()) return
        emptyLayout.setErrorType(EmptyLayout.NO_NETWORK)
    }

    override fun showDataError() {
        if (isPageFinish()) return
        emptyLayout.setErrorType(EmptyLayout.DATA_ERROR)
    }

    override fun showDialogLoading() {
        mLoadingDelegate.showDialogLoading(getString(R.string.loading))
    }

    override fun showDialogLoading(content: String?) {
        mLoadingDelegate.showDialogLoading(content)
    }

    override fun hideDialogLoading() {
        mLoadingDelegate.hideDialogLoading()
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


}

