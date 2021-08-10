package com.lingmiao.shop.business.main

import android.graphics.Bitmap
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.databinding.ActivityApplyShopInfoBankCardBinding


class ApplyShopInfoBankCardActivity :
    BaseVBActivity<ActivityApplyShopInfoBankCardBinding, BasePresenter>() {

    private val lastFrontResult = ""
    private val lastBackResult = ""
    private val currentImage: Bitmap? = null

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getViewBinding() = ActivityApplyShopInfoBankCardBinding.inflate(layoutInflater)

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {

        mToolBarDelegate?.setMidTitle("添加银行卡")

        //初始化
//        initComponent()


    }


//    private fun initComponent() {
//
//        mBinding.imageView.setOnClickListener {
//
//
//            CheckPermission.request(
//                this,
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) { allGranted, deniedList ->
//                if (allGranted) {
//                    startCaptureActivity()
//                } else {
//                    for (i in deniedList) {
//                        Log.d("WZY", "$i 没有获得")
//                    }
//                }
//
//
//            }
//
//
//        }
//
//
//    }


//    private fun startCaptureActivity() {
//        Log.d("WZY", "0")
////        val config = MLBcrCaptureConfig.Factory()
////            .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
////            .create()
////        val bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config)
////        bcrCapture.captureFrame(this, this.callback)
//
//        val config = MLCnIcrCaptureConfig.Factory() // 设置识别身份证的正反面。
//            // true：正面。
//            // false：反面。
//            .setFront(true)
//            .create()
//        val icrCapture = MLCnIcrCaptureFactory.getInstance().getIcrCapture(config)
//        icrCapture.capture(idCallback, this)
//        Log.d("WZY", "1")
//    }
//
//
//    private fun doIt() {
//        startCaptureActivity()
//    }
//
//    private val callback: MLBcrCapture.Callback = object : MLBcrCapture.Callback {
//        override fun onSuccess(result: MLBcrCaptureResult) {
//            if (result == null) {
//                return
//            }
//            val bitmap = result.originalBitmap
//            Log.d("WZY", result.number)
//        }
//
//        override fun onCanceled() {}
//        override fun onFailure(recCode: Int, bitmap: Bitmap) {
//            Log.d("WZY", "失败获取")
//        }
//
//        override fun onDenied() {}
//    }
//
//    private val idCallback: MLCnIcrCapture.CallBack = object : MLCnIcrCapture.CallBack {
//        override fun onSuccess(idCardResult: MLCnIcrCaptureResult) {
//            if (idCardResult == null) {
//                return
//            }
//            Log.d("WZY", "2")
//            Log.d("WZY", "存在过 ")
//            Log.d("WZY", idCardResult.idNum.toString())
//        }
//
//        override fun onCanceled() {
//            Log.d("WZY", "onCanceled ")
//        }
//
//        // 识别不到任何文字信息或识别过程发生系统异常的回调方法。
//        // retCode：错误码。
//        // bitmap：检测失败的身份证图片。
//        override fun onFailure(retCode: Int, bitmap: Bitmap) {
//            Log.d("WZY", "onFailure ")
//        }
//
//        override fun onDenied() {
//            Log.d("WZY", "onDenied ")
//        }
//    }


}


//
//    private void initComponent() {
//
//
//
//        frontDeleteImg.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.avatar_add:
//                startCaptureActivity();
//                break;
//            case R.id.avatar_delete:
//                showFrontDeleteImage();
//                lastFrontResult = "";
//                break;
//            case R.id.back:
//                finish();
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (currentImage != null && !currentImage.isRecycled()) {
//            currentImage.recycle();
//            currentImage = null;
//        }
//    }
//
//    private void startCaptureActivity() {
//        MLBcrCaptureConfig config = new MLBcrCaptureConfig.Factory()
//                .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
//                .create();
//        MLBcrCapture bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config);
//
//        bcrCapture.captureFrame(this, this.callback);
//    }
//
//    private String formatIdCardResult(MLBcrCaptureResult result) {
//        StringBuilder resultBuilder = new StringBuilder();
//
//        resultBuilder.append("Number：");
//        resultBuilder.append(result.getNumber());
//        resultBuilder.append("\r\n");
//
//        return resultBuilder.toString();
//    }
//
//    private MLBcrCapture.Callback callback = new MLBcrCapture.Callback() {
//        @Override
//        public void onSuccess(MLBcrCaptureResult result) {
//            if (result == null) {
//                return;
//            }
//            Bitmap bitmap = result.getOriginalBitmap();
//            showSuccessResult(bitmap, result);
//        }
//
//        @Override
//        public void onCanceled() {
//        }
//
//        @Override
//        public void onFailure(int recCode, Bitmap bitmap) {
//            showResult.setText(" RecFailed ");
//        }
//
//        @Override
//        public void onDenied() {
//        }
//    };
//
//    private void showSuccessResult(Bitmap bitmap, MLBcrCaptureResult idCardResult) {
//        showFrontImage(bitmap);
//        lastFrontResult = formatIdCardResult(idCardResult);
//        showResult.setText(lastFrontResult);
//        showResult.append(lastBackResult);
//        ((ImageView) findViewById(R.id.number)).setImageBitmap(idCardResult.getNumberBitmap());
//    }
//
//    private void showFrontImage(Bitmap bitmap) {
//        frontImg.setVisibility(View.VISIBLE);
//        frontImg.setImageBitmap(bitmap);
//        frontSimpleImg.setVisibility(View.GONE);
//        frontAddView.setVisibility(View.GONE);
//        frontDeleteImg.setVisibility(View.VISIBLE);
//    }
//
//    private void showFrontDeleteImage() {
//        frontImg.setVisibility(View.GONE);
//        frontSimpleImg.setVisibility(View.VISIBLE);
//        frontAddView.setVisibility(View.VISIBLE);
//        frontDeleteImg.setVisibility(View.GONE);
//    }
