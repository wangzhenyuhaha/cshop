package com.lingmiao.shop.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;

/**
 * Create Date : 2021/6/911:51 AM
 * Auther      : Fox
 * Desc        :
 **/
public class BitmapShareUtils {

    static int MAX_WX_IMAGE = 32;

    /**
     * 微信分享图片
     * note :
     * 1,图片大小不超过32kb
     * 2,图片先压缩大小，再绘制（左右/上下边距）
     * @param bitmap
     * @return
     */
    public static Bitmap drawWXMiniBitmap(Bitmap bitmap) {

        bitmap = compress(bitmap);

        int width;
        int height;
        // 先按5：4生成一张白色背景图片
        boolean isWidthLong = bitmap.getWidth() > bitmap.getHeight();
        LogUtils.d("i : " + isWidthLong);
        if (isWidthLong) {
            width = bitmap.getWidth();
            // 微信显示小程序的图片是5：4
            height = width * 4 / 5;
        } else {
            height = bitmap.getHeight();
            width = height * 5 / 4;
        }
        //LogUtils.d("i : " + width + " " + height);
        // 这个倍数多次测试之后显示效果最佳
        width = width * 5 / 4;
        height = height * 5 / 4;
        //LogUtils.d(" ..i : " + width + " " + height);

        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
        Canvas mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        // 绘制画笔
        Paint mPicturePaint = new Paint();
        // 绘制背景图片
        mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPicturePaint);
        // 绘制图片的宽、高
        int width_head = bitmap.getWidth();
        int height_head = bitmap.getHeight();
        // 绘制图片－－保证其在水平方向居中
        int left = (width - width_head) / 2;
        int top = (height - height_head) / 2;
        if (isWidthLong) {
            mCanvas.drawBitmap(bitmap, left, top, mPicturePaint);
        } else {
            mCanvas.drawBitmap(bitmap, left, top, mPicturePaint);
        }
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        return mBitmap;
    }

    public static Bitmap compress(Bitmap src_bitmap) {
        // 图片允许最大空间 单位：KB
        double maxSize = MAX_WX_IMAGE;
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        LogUtils.d(b.length + "i :size " + maxSize +" ;" + mid + ";      " + (mid > maxSize));
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            LogUtils.d("i size : " + maxSize);
            return ImageUtils.compressBySampleSize(src_bitmap, MAX_WX_IMAGE);
        }
        return src_bitmap;
    }

}
