package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.fox7.wx.WxShare
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.QRPresenter
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.launch

import android.graphics.Bitmap
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ThreadUtils
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.bean.ShareVo
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import com.lingmiao.shop.util.BitmapShareUtils
import com.lingmiao.shop.util.GlideUtils
import kotlinx.coroutines.launch
/**
Create Date : 2021/8/2912:40 下午
Auther      : Fox
Desc        :
 **/
class QRCodeOfStickyPreImpl (val context: Context, private var view: QRPresenter.View) : BasePreImpl(view), QRPresenter {

    override fun requestQRUrl() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getNewQRCode();
            if(resp.isSuccessful) {
                resp.body()?.string()?.let { view.setQRUrl(it) }
            } else {
                view.showToast("获取失败");
                view.getQRUrlFailed();
            }
        }
    }

}