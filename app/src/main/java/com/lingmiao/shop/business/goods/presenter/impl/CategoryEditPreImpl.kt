package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class CategoryEditPreImpl(var context: Context, var view: CategoryEditPre.PubView) : BasePreImpl(view),CategoryEditPre {

    var shopId : Int? = null;

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }

    override fun loadLv1GoodsGroup() {
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory("0", getSellerId());
            if (resp.isSuccess) {
                val list: List<CategoryVO> = resp?.data;
                list?.forEachIndexed { index, it ->
                    it.showLevel = 0;
                }
                view.onLoadMoreSuccess(list, list.isNotEmpty())
            }
        }
    }

    override fun loadLv2GoodsGroup(lv1GroupId: String?) {
        if (lv1GroupId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory(lv1GroupId, getSellerId())
            if (resp.isSuccess) {
                val list: List<CategoryVO> = resp?.data;
                list?.forEachIndexed { index, it ->
                    it.showLevel = 1;
                }
                view.onLoadedLv2(lv1GroupId, resp.data)
            }
        }
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

    override fun add(pId: Int, name: String) {
        var item = CategoryVO();
        item.showLevel = if(pId == 0 ) 0 else 1;
        item.name = name;
        item.parentId = pId;

        mCoroutine.launch {
            val resp = GoodsRepository.addCategory(item);
            handleResponse(resp) {
                item = resp.data;
                view.showToast("添加成功")
                view.addSuccess(pId, item);
            }
        }
    }

}