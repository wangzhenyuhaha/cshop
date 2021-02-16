package com.james.common.utils.permission.callback

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.permission.runtime.Permission

/**
 * Author : Elson
 * Date   : 2020/8/4
 * Desc   :
 */
abstract class StorageCallback(context: Context) : BaseCallback(context) {

    override fun applyPermissions(): Array<String> {
        return Permission.Group.STORAGE
    }

    override fun handleDenied(permissions: List<String?>?) {
        ToastUtils.showShort("请打开SD卡存储权限")
    }

    override fun handleAlwaysDeniedPermission() {
        ToastUtils.showShort("请打开SD卡存储权限")
    }
}