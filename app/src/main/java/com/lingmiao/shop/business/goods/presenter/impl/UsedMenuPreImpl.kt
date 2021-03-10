package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.james.common.base.BasePreImpl
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.presenter.UsedMenuPre
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class UsedMenuPreImpl(var view: UsedMenuPre.PubView) : BasePreImpl(view), UsedMenuPre {

    override fun loadLv1GoodsGroup() {
//        mCoroutine.launch {
//            val resp = GoodsRepository.loadLv1ShopGroup()
//            if (resp.isSuccess) {
//                view.onLoadMoreSuccess(resp.data, resp.data.isNotEmpty())
//            }
//        }
    }

    override fun loadLv2GoodsGroup(lv1GroupId: String?) {
        if (lv1GroupId.isNullOrBlank()) return
//        mCoroutine.launch {
//            val resp = GoodsRepository.loadLv2ShopGroup(lv1GroupId)
//            if (resp.isSuccess) {
//                view.onLoadMoreSuccess(resp.data, resp.data.isNotEmpty())
//            }
//        }
    }

    override fun deleteGoodsGroup(groupVO: MenuVo?, position: Int) {
//       if (groupVO?.shopCatId.isNullOrBlank()) return
//        mCoroutine.launch {
//            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
//            handleResponse(resp) {
//                view.showToast("删除成功")
//                view.onDeleteGroupSuccess(position)
//            }
//        }
    }

}