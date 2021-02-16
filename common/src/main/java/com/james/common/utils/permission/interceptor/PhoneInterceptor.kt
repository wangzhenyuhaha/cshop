package com.james.common.utils.permission.interceptor

import android.content.Context
import com.james.common.utils.exts.Interceptor
import com.james.common.utils.permission.PermissionUtil
import com.james.common.utils.permission.callback.LocationCallback
import com.james.common.utils.permission.callback.PhoneCallback
import com.james.common.utils.permission.callback.StorageCallback

/**
 * Author : Elson
 * Date   : 2020/8/4
 * Desc   : SD卡权限
 */
class PhoneInterceptor(val context: Context) : Interceptor() {

    override fun intercept(success: ((Any?) -> Unit)?, failed: ((Any?) -> Unit)?) {
        PermissionUtil.applyPermission(context, object : PhoneCallback(context) {
            override fun onGranted(permissions: List<String?>?) {
                success?.invoke(null)
            }
        })
    }

}