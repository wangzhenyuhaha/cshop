package com.lingmiao.shop.business.me.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.bean.BannerBean

interface ShopSettingPresenter : ShopManagePresenter {

    fun updateShopSlogan(bean: ApplyShopInfo)

    fun updateShopNotice(bean: ApplyShopInfo)

    fun setBanner(gallery: List<GoodsGalleryVO>?)

    fun getBanner()

    interface View : ShopManagePresenter.View {

        fun onUpdateSloganSuccess(string: String?)
        fun onSetBanner(bannerList: List<BannerBean>?)
        fun onUpdateNoticeSuccess(string: String?)
    }

}