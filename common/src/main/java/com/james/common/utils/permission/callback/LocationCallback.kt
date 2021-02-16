package com.james.common.utils.permission.callback

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.permission.runtime.Permission

/**
 * Author : Elson
 * Date   : 2020/8/4
 * Desc   : 定位权限
 */
abstract class LocationCallback(context: Context) : BaseCallback(context) {

    override fun applyPermissions(): Array<String> {
        return Permission.Group.LOCATION
    }

    override fun handleDenied(permissions: List<String?>?) {
        ToastUtils.showShort("请打开定位权限")
    }

    override fun handleAlwaysDeniedPermission() {
        ToastUtils.showShort("请打开定位权限")
    }
}