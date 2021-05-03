package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.amap.api.mapcore.util.it
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import com.lingmiao.shop.business.goods.presenter.GoodsDetailPre
import com.lingmiao.shop.business.goods.presenter.MenuEditPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class CategoryEditPreImpl(var context: Context, var view: CategoryEditPre.PubView) : BasePreImpl(view),CategoryEditPre {

    override fun loadLv1GoodsGroup() {
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory("0", UserManager.getLoginInfo()?.shopId);
            if (resp.isSuccess) {
                view.onLoadMoreSuccess(resp.data, resp.data.isNotEmpty())
            }
        }
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

    override fun deleteGoodsGroup(groupVO: CategoryVO?, position: Int) {
//       if (groupVO?.shopCatId.isNullOrBlank()) return
//        mCoroutine.launch {
//            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
//            handleResponse(resp) {
//                view.showToast("删除成功")
//                view.onDeleteGroupSuccess(position)
//            }
//        }
    }

    override fun add(vo: CategoryVO) {
        mCoroutine.launch {
            val resp = GoodsRepository.addCategory(vo);
            handleResponse(resp) {
                view.showToast("添加成功")
                view.addSuccess(vo);
            }
        }
    }

    override fun add(pId: Int, name: String) {
        var item = CategoryVO();
        item.showLevel = if(pId == 0 ) 0 else 1;
        item.name = name;
        item.parentId = pId;

        mCoroutine.launch {
            val resp = GoodsRepository.addCategory(item);
            handleResponse(resp) {
                view.showToast("添加成功")
                view.addSuccess(item);
            }
        }
    }

}