package com.lingmiao.shop.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Create Date : 2021/6/911:51 AM
 * Auther      : Fox
 * Desc        :
 **/
public class BitmapShareUtils {

    static int MAX_WX_IMAGE = 32;

    public static Bitmap drawWXMiniBitmap(Bitmap bitmap) {
        bitmap = imageZoom1(bitmap);

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
            mCanvas.drawBitmap(bitmap, left, (height - height_head) / 4, mPicturePaint);
        } else {
            mCanvas.drawBitmap(bitmap, left, top, mPicturePaint);
        }
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        //mBitmap = compressImage(mBitmap);
        return mBitmap;
    }

    public static Bitmap imageZoom1(Bitmap src_bitmap) {
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

    public static Bitmap compressImage(Bitmap image) {
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于128kb,大于继续压缩
        LogUtils.d("i : " + baos.toByteArray().length/1024);
        while (baos.toByteArray().length / 1024 > MAX_WX_IMAGE && options > 9) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少1
            options -= 1;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
}
