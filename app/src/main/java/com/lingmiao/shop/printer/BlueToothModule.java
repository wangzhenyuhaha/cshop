package com.lingmiao.shop.printer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Create Date : 2021/10/53:45 下午
 * Auther      : Fox
 * Desc        :
 **/
public class BlueToothModule {

    public static boolean isConnect = false;
    // 判断时候打开蓝牙设备
    @SuppressLint("MissingPermission")
    public static boolean isBluetoothEnable() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

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

    public static final int PERMISSION_ASK = 1983;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForBluetoothPermissions(Activity activity) {
        String[] permissions = new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        };
//        activity.requestPermissions(permissions, PERMISSION_ASK);
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, PERMISSION_ASK);
    }

    @SuppressLint("MissingPermission")
    public static List<String> getEnableBluetoothList() {
        Set<BluetoothDevice> device = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        List<String> btList = new ArrayList();
        if (device.size() > 0) {
            //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                btList.add(String.format("%s\n%s", btd.getName(), btd.getAddress()));
            }
        }
        return btList;
    }

    public static void initBindDevice(PrinterListener listener) {
        List<String> btList = getEnableBluetoothList();
        if(PrinterModule.myBinder == null || btList == null || btList.get(0).length() == 0) {
            ToastUtils.showShort("没有蓝牙打印机可连接");
            return;
        }
        String mac = btList.get(0);
        mac = mac.substring(mac.length() - 17);
        PrinterModule.connect(mac, listener);
    }

}
