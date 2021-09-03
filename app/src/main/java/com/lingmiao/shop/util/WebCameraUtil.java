package com.lingmiao.shop.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lingmiao.shop.MyApp;

/**
 * Create Date : 2021/9/33:05 下午
 * Auther      : Fox
 * Desc        :
 **/
public class WebCameraUtil {

    public static final int REQUEST_CAMERA = 19832;
    public interface CallBack {
        void call();
    }

    static final String[] permissionList = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static boolean isPermissionGranted(Fragment fragment) {
        if(fragment == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isStorage = ContextCompat.checkSelfPermission(MyApp.getInstance(), permissionList[1]) == PackageManager.PERMISSION_GRANTED;
            boolean isCamera = ContextCompat.checkSelfPermission(MyApp.getInstance(), permissionList[0]) == PackageManager.PERMISSION_GRANTED;
            return isStorage && isCamera;
        } else {
            return true;
        }
    }

    public static void permissionHandle(Fragment fragment, CallBack callBack) {
        if(fragment == null) {
            return;
        }
        if(isPermissionGranted(fragment)) {
            callBack.call();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragment.requestPermissions(permissionList, REQUEST_CAMERA);
            }
        }
    }

    public static boolean isPermissionGranted(Activity mActivity) {
        if(mActivity == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isStorage = ContextCompat.checkSelfPermission(MyApp.getInstance(), permissionList[1]) == PackageManager.PERMISSION_GRANTED;
            boolean isCamera = ContextCompat.checkSelfPermission(mActivity, permissionList[0]) == PackageManager.PERMISSION_GRANTED;
            return isStorage && isCamera;
        } else {
            return true;
        }
    }

    public static void permissionHandle(Activity mActivity, CallBack callBack) {
        if(mActivity == null) {
            return;
        }
        if(isPermissionGranted(mActivity)) {
            callBack.call();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(permissionList, REQUEST_CAMERA);
            }
        }
    }

}
