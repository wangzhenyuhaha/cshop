package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import android.graphics.Bitmap
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ThreadUtils
import com.fox7.wx.WxShare
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.ShareVo
import com.lingmiao.shop.business.sales.presenter.PopularizePre
import com.lingmiao.shop.util.BitmapShareUtils
import com.lingmiao.shop.util.GlideUtils
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.launch

class PopularizePreImpl(val context: Context, val view: PopularizePre.View) :
    BasePreImpl(view), PopularizePre {

    override fun getMyData() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getMyData().awaitHiResponse()
            view.showDialogLoading()
            if (resp.isSuccess) {
                view.onMyDataSuccess(resp.data)
            }else{
                view.ontMyDataError()
            }
        }
    }

    override fun getShareInfo(shopId : Int) {
        mCoroutine.launch {
            val identity = MainRepository.apiService.getShareInfo(1, shopId?.toString(), 2).awaitHiResponse()
            handleResponse(identity) {
                //view.onSetShareInfo(identity?.data);
                clickShareShop(identity?.data);
            }
        }
    }

    fun clickShareShop(item : ShareVo) {
        //调用api接口，发送数据到微信
        val api = WXAPIFactory.createWXAPI(context, IWXConstant.APP_ID)
        var share = WxShare(context, api);
        share.mTitle = item.title;
        share.mDescription = item.content;
        share.shareToFriend();
        share.miniType(IConstant.official);

        var imageByes : ByteArray? = null;
        if(item.imageUrl == null || item.imageUrl.isEmpty()) {
            imageByes = ImageUtils.drawable2Bytes(ResourceUtils.getDrawable(R.mipmap.ic_launcher));
            share.shareMini(IWXConstant.APP_ORIGINAL_ID, item.path, imageByes);
            return;
        }

        ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<Bitmap?>() {
            override fun doInBackground(): Bitmap? {
                return GlideUtils.getImage(context, item.imageUrl);
            }

            override fun onSuccess(result: Bitmap?) {
                if(result != null) {
                    imageByes = ImageUtils.bitmap2Bytes(BitmapShareUtils.drawWXMiniBitmap(result));
                } else {
                    imageByes = ImageUtils.bitmap2Bytes(
                        ConvertUtils.drawable2Bitmap(
                            ResourceUtils.getDrawable(
                                R.mipmap.ic_launcher)));
                }
                share.shareMini(IWXConstant.APP_ORIGINAL_ID, item.path, imageByes!!);
            }
        })
    }
}

