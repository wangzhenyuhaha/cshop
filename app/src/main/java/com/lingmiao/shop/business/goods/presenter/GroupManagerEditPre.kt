package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品 - 添加/编辑分组
 */
interface GroupManagerEditPre : BasePresenter {

    fun submit(groupName: ShopGroupVO, groupLevel: Int?)

    fun modifyGroup(groupVO: ShopGroupVO, groupLevel: Int?)

    interface GroupEditView : BaseView {
        fun finish()
    }
}