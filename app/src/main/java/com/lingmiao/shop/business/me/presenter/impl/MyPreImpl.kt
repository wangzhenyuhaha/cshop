package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import android.graphics.Bitmap
import com.blankj.utilcode.util.*
import com.fox7.wx.WxShare
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.bean.ShareVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import com.lingmiao.shop.util.BitmapShareUtils
import com.lingmiao.shop.util.GlideUtils
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.launch


class MyPreImpl(val context: Context, private var view: MyPresenter.View) : BasePreImpl(view), MyPresenter {

	private val mWallet: MyWalletPresenter by lazy { MyWalletPresenterImpl(view) }

    override fun getMyData() {
		mCoroutine.launch {
			val resp = MeRepository.apiService.getMyData().awaitHiResponse()
			if (resp.isSuccess) {
				view.onMyDataSuccess(resp.data)
			}else{
				view.ontMyDataError()
			}
		}
    }

	override fun getIdentity() {
		mCoroutine.launch {
			val identity = MainRepository.apiService.getShopIdentity().awaitHiResponse()
			handleResponse(identity) {
				view.onSetVipInfo(identity?.data?.data);
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
		share.miniTypeToRelease();
		share.miniTypeToRelease();

		var imageByes : ByteArray? = null;
		if(item.imageUrl == null || item.imageUrl.length == 0) {
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
					imageByes = ImageUtils.drawable2Bytes(ResourceUtils.getDrawable(R.mipmap.ic_launcher));
				}
				share.shareMini(IWXConstant.APP_ORIGINAL_ID, item.path, imageByes!!);
			}
		})
		 //share.shareImageResource(R.mipmap.ic_launcher);
		//share.shareWeb("www.baidu.com", R.mipmap.ic_launcher);
		// share.shareText("分享一个商品：" + goodsVO?.goodsName)
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

	/**
	 * 加载钱包数据
	 */
	override fun loadWalletData() {
		mWallet.loadWalletData();
	}

}