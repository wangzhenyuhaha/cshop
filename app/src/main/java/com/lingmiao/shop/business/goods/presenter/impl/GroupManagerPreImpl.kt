package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class GroupManagerPreImpl(var view: GroupManagerPre.GroupManagerView) : BasePreImpl(view), GroupManagerPre {


    override fun loadLv1GoodsGroup() {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup(1)
            if (resp.isSuccess) {
                view.onLoadMoreSuccess(resp.data, false)
            }
        }
    }

    override fun loadLv2GoodsGroup(lv1GroupId: String?) {
        if (lv1GroupId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv2ShopGroup(lv1GroupId)
            if (resp.isSuccess) {
                view.onLoadMoreSuccess(resp.data, false)
            }
        }
    }

    override fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int) {
       if (groupVO?.shopCatId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
            handleResponse(resp) {
                view.showToast("删除成功")
                view.onDeleteGroupSuccess(position)
            }
        }
    }

}