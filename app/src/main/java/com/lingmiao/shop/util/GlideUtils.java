package com.lingmiao.shop.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.autonavi.base.amap.mapcore.FileUtil;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lingmiao.shop.MyApp;
import com.lingmiao.shop.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public static void setImageUrl(ImageView iv, String url, @DrawableRes int res) {
        RequestOptions options = new RequestOptions().centerCrop()
                .placeholder(res);
        Glide.with(iv.getContext()).load(url)
                .apply(options)
                .into(iv);
    }


    public static void setImageUrl1(ImageView iv, String url) {
        RequestOptions options = new RequestOptions().centerCrop()
                .placeholder(R.color.color_F1F1F1);
        Glide.with(iv.getContext()).load(url)
                .apply(options)
                .into(iv);
    }

    public static void setImageUrl12(ImageView iv, String url) {
        RequestOptions options = new RequestOptions().centerInside()
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


    public static Bitmap getImage(Context context, String url) {
        try {
            return  Glide.with(context).asBitmap()
                    .load(url)
                    .submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
