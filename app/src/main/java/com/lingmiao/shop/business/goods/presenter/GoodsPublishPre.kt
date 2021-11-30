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

    //切换商品类型
    fun showGoodsType(isVirtual: Boolean)

    //设置商品分类
    fun showCategoryPop()

    //设置商品菜单
    fun showGroupPop()

    fun loadGoodsInfo(goodsId: String?)

    fun publish(
        goodsVO: GoodsVOWrapper,
        isVirtualGoods: Boolean,
        isMutilSpec: Boolean,
        scan: Boolean,
        type: Int
    )


    //useless
    //有效期至
    fun showExpirePop(str: String)

    //使用时间
    fun showUseTimePop(str: String)


    interface PublishView : BaseView {
        //切换实体商品虚拟商品
        fun onSetGoodsType(isVirtual: Boolean)

        //切换分类
        fun onUpdateCategory(categoryId: String?, categoryName: String?)

        //设置菜单
        fun onUpdateGroup(groupId: String?, groupName: String?)


        fun onSetUseTimeStr(useTime: String);
        fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean)


        fun finish()

        //useless
        //有效期至
        fun onUpdateExpire(item: GoodsUseExpireVo?)

        //useless
        //使用时间
        fun onUpdateUseTime(list: List<MultiPickerItemBean>?)
    }

}

