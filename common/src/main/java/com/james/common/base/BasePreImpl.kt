package com.james.common.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.GsonUtils
import com.james.common.net.BaseResponse
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.exts.check
import com.james.common.utils.exts.isNotBlank


open class BasePreImpl(private var mRootView: BaseView? = null) : BasePresenter, LifecycleObserver {

    protected val TAG = this.javaClass.simpleName

    init {
        (mRootView as? LifecycleOwner)?.apply {
            lifecycle.addObserver(this@BasePreImpl)
        }
    }

    protected var mCoroutine: CoroutineSupport = CoroutineSupport()

    //    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        (mRootView as? LifecycleOwner)?.apply {
            lifecycle.addObserver(mCoroutine)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        // do nothing
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        // do nothing
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        // do nothing
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        // do nothing
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        (mRootView as? LifecycleOwner)?.apply {
            lifecycle.removeObserver(this@BasePreImpl)
            lifecycle.removeObserver(mCoroutine)
        }
        mRootView = null
    }

    fun handleErrorMsg(msg: String?) {
        if (msg.isNotBlank()) {
            try {
                val errorResp = GsonUtils.fromJson(msg, BaseResponse::class.java)
                if (errorResp != null) {
                    mRootView?.showToast(errorResp.message.check())
                }
            } catch (e: Exception) {
                // 防止部分接口返回 非常规格式 的字符串，如html
            }
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