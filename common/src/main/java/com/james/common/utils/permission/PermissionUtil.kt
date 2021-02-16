package com.james.common.utils.permission

import android.content.Context
import com.james.common.utils.permission.callback.PermissionCallback
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission


/**
 * Author : Elson
 * Date   : 2020/7/7
 * Desc   : 权限管理
 */
class PermissionUtil {

    companion object {
        @JvmStatic
        fun applyPermission(context: Context?, callback: PermissionCallback) {
            if (context == null) return
            val permissions = callback.applyPermissions()
            if (AndPermission.hasPermissions(context, permissions)) {
                callback.onGranted(permissions.asList())
                return
            }
            AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale { context, data, executor ->
                    executor.execute()
                }
                .onGranted { permissions: List<String?>? ->
                    callback.onGranted(permissions)
                }
                .onDenied { permissions: List<String?>? ->
                    callback.onDenied(permissions)
                }.start()
        }

        @JvmStatic
        fun applySetting(context: Context?) {
            if (context == null) return
            AndPermission.with(context)
                .runtime()
                .setting()
                .start(1000)
        }
    }
}