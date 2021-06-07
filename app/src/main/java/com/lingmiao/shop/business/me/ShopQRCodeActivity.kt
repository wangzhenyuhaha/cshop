package com.lingmiao.shop.business.me

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant.CACHE_PATH
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import com.lingmiao.shop.business.me.presenter.impl.ShopQRCodePreImpl
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.me_activity_qr_code.*

/**
Create Date : 2021/6/43:07 PM
Auther      : Fox
Desc        :
 **/
class ShopQRCodeActivity : BaseActivity<IShopQRCodePre>(), IShopQRCodePre.View {

    val path = CACHE_PATH + "share.jpg";

    override fun createPresenter(): IShopQRCodePre {
        return ShopQRCodePreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_qr_code;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("店铺二维码")

        mPresenter?.getQRCode();

        ivQRCode.singleClick {
            ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<Boolean?>() {
                override fun doInBackground(): Boolean? {
                    return ImageUtils.save(ImageUtils.view2Bitmap(it), path, Bitmap.CompressFormat.JPEG);
                }

                override fun onSuccess(result: Boolean?) {
                    SnackbarUtils.with(it)
                        .setDuration(SnackbarUtils.LENGTH_LONG)
                        .apply {
                            if(result == true) {
                                setMessage("图片保存在"+path).showSuccess(true)
                            } else {
                                setMessage("保存失败.").showError(true)
                            }
                        }
                }
            })
        }
    }

    override fun onSetQRCode(url: String) {
        GlideUtils.setImageUrl1(ivQRCode, url)
    }

    override fun onGetQRCodeFail() {

    }
}