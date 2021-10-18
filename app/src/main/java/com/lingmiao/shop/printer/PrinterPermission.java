package com.lingmiao.shop.printer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Create Date : 2021/10/1710:56 上午
 * Auther      : Fox
 * Desc        :
 **/
public class PrinterPermission {

    public static final int PERMISSION_ASK = 1983;
    // 蓝牙是否可用
    @SuppressLint("MissingPermission")
    public static boolean isBluetoothEnable() {
        return BluetoothAdapter.getDefaultAdapter() == null ? false : BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    // 检查蓝牙是否可用
    public static boolean checkBluetoothEnable(Activity context) {
        if (!isBluetoothEnable()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(intent, PERMISSION_ASK);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isBluetoothPermissionGranted(Activity activity) {
        boolean granted = false;
        int bluetoothGranted = activity.checkSelfPermission(Manifest.permission.BLUETOOTH);
        int bluetoothAdminGranted = activity.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN);
        return bluetoothGranted == PackageManager.PERMISSION_GRANTED && bluetoothAdminGranted == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForBluetoothPermissions(Activity activity) {
        String[] permissions = new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, PERMISSION_ASK);
    }
}
