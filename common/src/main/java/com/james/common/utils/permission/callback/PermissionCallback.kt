package com.james.common.utils.permission.callback

import android.content.Context
import com.james.common.utils.permission.PermissionUtil
import com.yanzhenjie.permission.AndPermission

/**
 * Author : Elson
 * Date   : 2020/7/7
 * Desc   :
 */
interface PermissionCallback {

    fun applyPermissions(): Array<String>
    fun onGranted(permissions: List<String?>?)
    fun onDenied(permissions: List<String?>?)
}

abstract class BaseCallback(var mContext: Context) : PermissionCallback {

    override fun onDenied(permissions: List<String?>?) {
        if (permissions != null && AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
            handleAlwaysDeniedPermission()
        } else {
            handleDenied(permissions)
        }
    }

    abstract fun handleDenied(permissions: List<String?>?)

    abstract fun handleAlwaysDeniedPermission()
}