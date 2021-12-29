package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.MenuGoodsManagerPre
import kotlinx.coroutines.launch

class MenuGoodsManagerPreImpl(val context: Context, val view: MenuGoodsManagerPre.View) :
    BasePreImpl(view), MenuGoodsManagerPre {

    //加载一级菜单
    override fun loadLv1GoodsGroup(isTop: Int, isSecond: Boolean) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup(isTop)
            if (resp.isSuccess) {
                view.onLoadLv1GoodsGroupSuccess(resp.data, isTop, isSecond)
            }
        }
    }

    //排序
    override fun sort(isTop: Int, cid: String, sort: Int) {
        mCoroutine.launch {
            val resp = GoodsRepository.sort(isTop, cid, sort)
            handleResponse(resp) {
                view.onSortSuccess(isTop)
            }
        }

    }

    //删除一级菜单
    override fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int, isTop: Int) {
        if (groupVO?.shopCatId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
            handleResponse(resp) {
                view.showToast("删除成功")
                view.onDeleteGroupSuccess(position, isTop)
            }
        }
    }

    //修改二级菜单的名称
    override fun updateSecondGroup(
        item: ShopGroupVO,
        newName: String,
        position: Int
    ) {
        mCoroutine.launch {
            val resp = GoodsRepository.updateShopGroup(item.shopCatId!!, item)
            if (resp.isSuccess) {
                view.updateSecondGroupSuccess(newName, position)
            }
        }
    }


}

