package com.james.common.base

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.R
import com.james.common.base.delegate.*
import com.james.common.net.BaseResponse
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.exts.check
import com.james.common.utils.exts.isNotBlank
import com.james.common.view.EmptyLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


//Presenter 生命周期方法
//BaseView 页面信息显示，Dialog显示，Toast显示
abstract class BaseVBActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {

    //当前Activity实例
    protected lateinit var mContext: AppCompatActivity

    //ViewBinding
    protected lateinit var binding: VB

    //activity_base中的ToolBar
    protected lateinit var tlBar: Toolbar

    //activity_base中的EmptyLayout
    protected lateinit var elEmpty: EmptyLayout

    //对ToolBar的操作m
    protected lateinit var mToolBarDelegate: ToolBarDelegate

    //这是一个用来发送网络请求的launch协程
    protected var searchJob: Job? = null
    private val scope = MainScope()

    //对Dialog的操作
    // showDialogLoading(content: String?)
    // hideDialogLoading()
    protected val mLoadingDelegate: LoadingDelegate by lazy {
        DefaultLoadingDelegate(this)
    }

    //对EmptyLayout的操作
    // setPageLoadingLayout(layout: EmptyLayout)
    // showPageLoading()
    // hidePageLoading()
    // showNoData()
    // showNoNetwork()
    // showDataError()
    protected val mPageLoadingDelegate: PageLoadingDelegate by lazy {
        DefaultPageLoadingDelegate(this).apply {
            //设置要操作的EmptyLayout
            setPageLoadingLayout(elEmpty)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBundles()

        //初始化布局
        binding = getViewBinding()
        setContentView(binding.root)

        mContext = this

        initSelf()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    //---------------------override---------------------

    protected abstract fun getViewBinding(): VB

    protected abstract fun initView()

    //---------------------method---------------------

    //初始化数据
    protected open fun initBundles() {}

    //初始化ToolBar操作
    private fun initSelf() {
        //控件引用
        tlBar = findViewById(R.id.tl_bar)
        elEmpty = findViewById(R.id.el_empty)

        //@param context当前Activity对象
        //@param toolbar当前ToolBar的对象
        //@param lightMode设置ToolBar模式
        //初始化对ToolBar的操作类
        mToolBarDelegate = ToolBarDelegate(this, tlBar, useLightMode())


    }

    //默认为true,为白色ToolBar，false为蓝色ToolBar
    protected open fun useLightMode(): Boolean = true

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //---------------------重写BaseView中的抽象方法---------------------

    override fun showPageLoading() {
        mPageLoadingDelegate.showPageLoading()
    }


    override fun hidePageLoading() {
        mPageLoadingDelegate.hidePageLoading()
    }


    override fun showNoData() {
        mPageLoadingDelegate.showNoData()
    }


    override fun showNoNetwork() {
        mPageLoadingDelegate.showNoNetwork()
    }


    override fun showDataError() {
        mPageLoadingDelegate.showDataError()
    }


    override fun showDialogLoading() {
        mLoadingDelegate.showDialogLoading(getString(R.string.loading))
    }


    override fun showDialogLoading(@Nullable content: String?) {
        mLoadingDelegate.showDialogLoading(content)
    }


    override fun hideDialogLoading() {
        mLoadingDelegate.hideDialogLoading()
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

    fun handleErrorMsg(msg: String?) {
        if (msg.isNotBlank()) {
            try {
                val errorResp = GsonUtils.fromJson(msg, BaseResponse::class.java)
                if (errorResp != null) {
                    showToast(errorResp.message.check())
                    hidePageLoading()
                }
            } catch (e: Exception) {
                showToast(msg)
                hidePageLoading()
            }
        } else {
            showNoData()
        }
    }

    fun <T> handleResponse(resp: HiResponse<T>, success: (T) -> Unit) {
        if (resp.isSuccess) {
            success.invoke(resp.data)
        } else {
            handleErrorMsg(resp.msg)
        }
    }

}