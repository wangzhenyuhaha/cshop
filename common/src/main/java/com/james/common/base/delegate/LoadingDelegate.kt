package com.james.common.base.delegate

import android.content.Context
import android.text.TextUtils
import com.james.common.view.dialog.LoadingDailog

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : LoadingDialog 操作类接口定义
 */
interface LoadingDelegate {

    fun showDialogLoading(content: String?)
    fun hideDialogLoading()
}

/**
 * LoadingDialog 委托类
 */
class DefaultLoadingDelegate(val mContext: Context) : LoadingDelegate {

    private var mLoadingDailog: LoadingDailog? = null

    override fun showDialogLoading(content: String?) {
        if (mLoadingDailog == null) {
            mLoadingDailog = LoadingDailog.Builder(mContext)
                .setCancelable(true)
                .setCancelOutside(false)
                .create()
        }
        if (mLoadingDailog != null && !mLoadingDailog!!.isShowing) {
            mLoadingDailog!!.setLoadingText(content)
            mLoadingDailog!!.show()
        }
    }

    override fun hideDialogLoading() {
        if (mLoadingDailog != null && mLoadingDailog!!.isShowing()) {
            mLoadingDailog!!.dismiss()
            mLoadingDailog = null
        }
    }
}