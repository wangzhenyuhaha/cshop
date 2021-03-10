package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.*
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * @author elson
 * @date 2020/7/30
 * @Desc
 */
interface GoodsPublishPre : BasePresenter {

    fun loadGoodsInfo(goodsId: String?)

    fun publish(goodsVO: GoodsVOWrapper,isVirtualGoods : Boolean, isMutilSpec: Boolean)

    fun showCategoryPop()

    fun showGroupPop()

    fun showExpirePop(str : String)

    fun showUseTimePop(str : String)

    fun showGoodsType(isVirtual : Boolean)

    interface PublishView : BaseView {
        fun onSetGoodsType(isVirtual : Boolean);
        fun onSetUseTimeStr(useTime : String);
        fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper)
        fun onUpdateCategory(categoryId: String?, categoryName: String?)
        fun onUpdateGroup(groupId: String?, groupName: String?)
        fun finish()
        fun onUpdateExpire(item : GoodsUseExpireVo?)
        fun onUpdateUseTime(list : List<MultiPickerItemBean>?)
    }

}

