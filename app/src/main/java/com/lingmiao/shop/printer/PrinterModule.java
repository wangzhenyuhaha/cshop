package com.lingmiao.shop.printer;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.lingmiao.shop.business.order.bean.OrderList;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Create Date : 2021/10/51:18 下午
 * Auther      : Fox
 * Desc        :
 **/
public class PrinterModule {

    public static IMyBinder myBinder;
    
    static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (IMyBinder) service;
            initDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    // 绑定服务
    public static void bind(Context context) {
        if(myBinder == null) {
            Intent intent = new Intent(context, PosprinterService.class);
            context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            initDevice();
        }
    }

    // 初始化打印设备
    static void initDevice() {
        initBindDevice(connectPrinterListener);
    }

    static PrinterConnectListener connectPrinterListener = new PrinterConnectListener() {
        @Override
        public void onConnect(boolean flag) {
            if(flag) {
                ToastUtils.showShort("打印蓝牙连接成功");
            }
        }

        @Override
        public void onDisConnect(boolean flag) {
//            ToastUtils.showShort("打印蓝牙已断开");
        }
    };

    // 初始化连接的蓝牙设备
    public static void initBindDevice(PrinterConnectListener listener) {
        List<String> btList = getEnableBluetoothList();
        if(myBinder == null || btList == null || btList.size() == 0 || btList.get(0) == null || btList.get(0).length() == 0) {
            ToastUtils.showShort("没有蓝牙打印机可连接");
            return;
        }
        String mac = btList.get(0);
        mac = mac.substring(mac.length() - 17);
        connect(mac, listener);
    }

    // 获取可用的蓝牙列表
    @SuppressLint("MissingPermission")
    public static List<String> getEnableBluetoothList() {
        Set<BluetoothDevice> device = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        List<String> btList = new ArrayList();
        if(device == null || device.size() == 0) {
            return btList;
        }
        //存在已经配对过的蓝牙设备
        for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
            BluetoothDevice btd = it.next();
            btList.add(String.format("%s\n%s", btd.getName(), btd.getAddress()));
        }
        return btList;
    }

    // 连接设备
    public static void connect(String deviceAddress,final PrinterConnectListener listener) {
        if(myBinder == null) {
            return;
        }
        myBinder.ConnectBtPort(deviceAddress, new TaskCallback() {
            @Override
            public void OnSucceed() {
                if(listener != null) {
                    listener.onConnect(true);
                }
            }

            @Override
            public void OnFailed() {
                if(listener != null) {
                    listener.onConnect(false);
                }
            }
        });
    }

    public static void disconnect(PrinterConnectListener listener) {
        myBinder.DisconnectCurrentPort(new TaskCallback() {
            @Override
            public void OnSucceed() {
                if (listener != null) {
                    listener.onDisConnect(true);
                }
            }

            @Override
            public void OnFailed() {
                if (listener != null) {
                    listener.onDisConnect(false);
                }
            }
        });
    }

    public static void printer(Activity activity, OrderList orderList) {
        if(!PrinterPermission.checkBluetoothEnable(activity)) {
            return;
        }
        if(myBinder == null) {
            bind(activity);
            ToastUtils.showShort("正在连接请稍后....");
            return;
        }
        initDevice();
//        ToastUtils.showShort("正在打印....");
        myBinder.WriteSendData(new TaskCallback() {
            @Override
            public void OnSucceed() {
                ToastUtils.showShort("打印成功");
            }

            @Override
            public void OnFailed() {
                ToastUtils.showShort("打印失败");
            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                Printer printer = new Printer(orderList);
                return printer.getPrintList();
            }
        });
    }

}
