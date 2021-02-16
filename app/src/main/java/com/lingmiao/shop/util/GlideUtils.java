package com.lingmiao.shop.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lingmiao.shop.MyApp;
import com.lingmiao.shop.R;

import java.io.File;
import java.util.List;

/**
 * Created by lqx on 2016/9/14.
 * desc:GlideUtils
 */
public class GlideUtils {


    public static void setImageUrl(ImageView iv, String url) {
        Glide.with(iv.getContext()).load(url)
                .apply(new RequestOptions().centerCrop())
                .into(iv);
    }

    public static void setImageUrl1(ImageView iv, String url) {
        RequestOptions options = new RequestOptions().centerCrop()
                .placeholder(R.color.color_F1F1F1);
        Glide.with(iv.getContext()).load(url)
                .apply(options)
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, File file) {
        Glide.with(iv.getContext()).load(file)
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, File file, float corner) {
        Glide.with(iv.getContext()).load(file)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
                .into(iv);
    }

    public static void setImageUrl(ImageView iv, String url, float corner) {
        Glide.with(Utils.getApp()).load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
                .into(iv);
    }

    public static void setCornerImageUrl(ImageView iv, String url, float corner) {
        Glide.with(Utils.getApp()).load(url)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(ConvertUtils.dp2px(corner))))
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }

//    public static void setImageUrl(Context context, ImageView iv, String url) {
//        Glide.with(context).load(url)
////                .error(R.mipmap.default_load)
////                .placeholder(R.mipmap.default_load)
//                .into(iv);
//    }
//
//    public static void setImageUrl(Fragment context, ImageView iv, String url) {
//        Glide.with(context).load(url)
////                .error(R.mipmap.default_load)
////                .placeholder(R.mipmap.default_load)
//                .into(iv);
//    }

    public static void setCircleImageUrl(Context context, ImageView iv, String url) {
        Glide.with(context).load(url)
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }

    public static void setCircleImageUrl(Fragment context, ImageView iv, String url) {
        Glide.with(context).load(url)
//                .error(R.mipmap.default_load)
//                .placeholder(R.mipmap.default_load)
                .into(iv);
    }


}
