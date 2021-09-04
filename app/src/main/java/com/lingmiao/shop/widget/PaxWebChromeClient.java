package com.lingmiao.shop.widget;

/**
 * Create Date : 2021/9/210:30 下午
 * Auther      : Fox
 * Desc        :
 **/

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.james.common.view.dialog.LoadingDailog;
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO;
import com.lingmiao.shop.business.photo.PhotoHelper;
import com.lingmiao.shop.util.WebCameraUtil;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PaxWebChromeClient extends WebChromeClient {
    private static final int CHOOSE_REQUEST_CODE = 0x9001;
    private static final int CAMERA_REQUEST_CODE = 0x9003;
    private Activity mActivity;
    private ValueCallback<Uri> uploadFile;//定义接受返回值
    private ValueCallback<Uri[]> uploadFiles;
    private ProgressBar bar;
    private TextView mTitle;

    LoadingDailog loadingDailog;

    public PaxWebChromeClient(@NonNull Activity mActivity, TextView title) {
        this.mActivity = mActivity;
        this.bar =new ProgressBar(mActivity);
        this.mTitle=title;
        loadingDailog = new LoadingDailog.Builder(mActivity)
                .setCancelable(true)
                .setCancelOutside(false)
                .create();
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            bar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == bar.getVisibility()) {
                bar.setVisibility(View.VISIBLE);
            }
            bar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override

    public void onReceivedTitle(WebView view, String title) {

        super.onReceivedTitle(view, title);

        mTitle.setText(title);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        //                super.onPermissionRequest(request);//必须要注视掉
        request.grant(request.getResources());
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        super.onPermissionRequestCanceled(request);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.uploadFile = uploadFile;
        handleImages(acceptType);
    }

    // For Android  > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        this.uploadFile = uploadFile;
        handleImages(acceptType);
    }

    WebCameraUtil.CallBack callBack = () -> takePicture();

    // For Android  >= 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        this.uploadFiles = filePathCallback;
        String[] strings = fileChooserParams.getAcceptTypes();
        if(strings.length > 0) {
            handleImages(strings[0]);
        } else {
            openFileChooseProcess();
        }
        return true;
    }

    private void handleImages(String AcceptType) {
        if(AcceptType.equals("image/*")) {
            WebCameraUtil.permissionHandle(mActivity, callBack);
        } else {
            openFileChooseProcess();
        }
    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "Choose"), CHOOSE_REQUEST_CODE);
    }

    Uri  mImageUri;
    void takePicture() {
        mImageUri = getImageUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);
        mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    Uri getImageUri() {
        //创建用来保存照片的文件
        File imageFile = createImageFile();
        Uri  mImageUri;
        if(imageFile!=null){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                /*7.0以上要通过FileProvider将File转化为Uri*/
                mImageUri = FileProvider.getUriForFile(mActivity,"com.lingmiao.shop.versionProvider",imageFile);
            }else {
                /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                mImageUri = Uri.fromFile(imageFile);
            }
            return mImageUri;
        }
        return null;
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = this.mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        handle(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        handle(requestCode, resultCode, data);
    }

    void handle(int requestCode, int resultCode, Intent data) {
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{mImageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (uploadFiles != null) {
            uploadFiles.onReceiveValue(results);
            uploadFiles = null;
        } else if (uploadFile != null) {
            uploadFile.onReceiveValue(results[0]);
            uploadFile = null;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if(requestCode == WebCameraUtil.REQUEST_CAMERA && WebCameraUtil.isPermissionGranted(mActivity)) {
            callBack.call();
        }
    }
}