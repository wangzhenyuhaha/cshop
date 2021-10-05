package com.lingmiao.shop.printer;

import static android.content.Context.BIND_AUTO_CREATE;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.lingmiao.shop.business.order.bean.OrderList;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.DataForSendToPrinterPos58;
import net.posprinter.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
            Log.e("myBinder", "connect");
            BlueToothModule.initBindDevice(new PrinterListener() {

                @Override
                public void onConnect(boolean flag) {

                }

                @Override
                public void onDisConnect(boolean flag) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("myBinder", "disconnect");
        }
    };

    public static void bind(Context context) {
        if(myBinder == null) {
            Intent intent = new Intent(context, PosprinterService.class);
            context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            BlueToothModule.initBindDevice(new PrinterListener() {
                @Override
                public void onConnect(boolean flag) {
                }

                @Override
                public void onDisConnect(boolean flag) {
                }
            });
        }
    }

    public static void connect(String BtAdress,final PrinterListener listener) {
        if(myBinder == null) {
            return;
        }
        myBinder.ConnectBtPort(BtAdress, new TaskCallback() {
            @Override
            public void OnSucceed() {
                BlueToothModule.isConnect = true;
                ToastUtils.showShort("蓝牙连接成功");
                if(listener != null) {
                    listener.onConnect(true);
                }
            }

            @Override
            public void OnFailed() {
                BlueToothModule.isConnect = false;
                if(listener != null) {
                    listener.onConnect(false);
                }
            }
        } );
    }

    public static void disConnect(final PrinterListener listener){
        myBinder.DisconnectCurrentPort(new TaskCallback() {
            @Override
            public void OnSucceed() {
                BlueToothModule.isConnect = false;
                if(listener != null) {
                    listener.onDisConnect(true);
                }
            }

            @Override
            public void OnFailed() {
                BlueToothModule.isConnect = false;
                if(listener != null) {
                    listener.onDisConnect(false);
                }
            }
        });
    }

    public static void printer(Activity activity, OrderList orderList) {
        if(!BlueToothModule.checkBluetoothEnable(activity)) {
            return;
        }

        BlueToothModule.initBindDevice(new PrinterListener(){

            @Override
            public void onConnect(boolean flag) {

            }

            @Override
            public void onDisConnect(boolean flag) {

            }
        });
        if(myBinder == null) {
            PrinterModule.bind(activity);
            ToastUtils.showShort("正在连接请稍后....");
            return;
        }
        ToastUtils.showShort("正在打印....");
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
