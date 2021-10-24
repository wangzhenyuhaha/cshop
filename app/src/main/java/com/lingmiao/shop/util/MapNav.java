package com.lingmiao.shop.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.AppUtils;
import com.fox7.map.ToastUtil;
import com.lingmiao.shop.widget.ListDialog;
import java.util.ArrayList;

/**
 * Create Date : 2021/10/233:05 下午
 * Auther      : Fox
 * Desc        :
 **/
public class MapNav {

    private static ArrayList<String> mapData = new ArrayList<>();

    static {
        mapData.add("1@百度地图");
        mapData.add("2@高德地图");
    }

    /**
     * 地图选择弹窗
     */
    public static void chooseMapDialog(Context context,String address) {
        if(context == null) {
            return;
        }
        ListDialog dialog = new ListDialog(context, (ListDialog.DialogItemClickListener) (position, id, text, chooseIndex) -> {//1:百度   2：高德   3.腾讯
            if (id.equals("1")) {
                if (AppUtils.isAppInstalled("com.baidu.BaiduMap")) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("baidumap://map/navi?query=" + address + "&mode=riding&coord_type=bd09ll&src=" + context.getPackageName()));
                    context.startActivity(intent);
                } else {
                    ToastUtil.show(context, "请先安装百度地图客户端");
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("market://details?id=com.baidu.BaiduMap"));
                    context.startActivity(intent);
                }
            } else if (id.equals("2")) {
                if (AppUtils.isAppInstalled("com.autonavi.minimap")) {
                    String uri = String.format("amapuri://route/plan/?dname=%s&dev=0&t=0&featureName=OnRideNavi&rideType=elebike&sourceApplication=%s", address, context.getPackageName());
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setPackage("com.autonavi.minimap");
                    context.startActivity(intent);
                } else {
                    ToastUtil.show(context, "请先安装高德地图客户端");
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("market://details?id=com.autonavi.minimap"));
                    context.startActivity(intent);
                }
            }
        }, mapData, 0);
        dialog.show();
    }

}
