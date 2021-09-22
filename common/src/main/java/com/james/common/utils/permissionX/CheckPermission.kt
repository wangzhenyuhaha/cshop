package com.james.common.utils.permissionX

import androidx.fragment.app.FragmentActivity

object CheckPermission {

    private const val TAG = "InvisibleFragment"

    fun request(
        activity: FragmentActivity,
        vararg permissions: String,
        callback: PermissionCallback
    ) {

        val fragmentManager = activity.supportFragmentManager

        //找到Fragment
        val exitedFragment = fragmentManager.findFragmentByTag(TAG)

        val fragment = if (exitedFragment != null) {
            exitedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }

        fragment.requestNow(callback, *permissions)
    }


}

