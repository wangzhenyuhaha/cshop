package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.james.common.base.BasePreImpl
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.base.UserManager
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
        mCoroutine.launch {
            val resp = GoodsRepository.load2LvShopGroup("0", 0)
            if (resp.isSuccess) {
                resp.data.forEachIndexed { index, item ->
                    item.children?.forEachIndexed { _index, _item ->
                        item.addSubItem(_item)
                    }
                }
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

    override fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int) {
//       if (groupVO?.shopCatId.isNullOrBlank()) return
//        mCoroutine.launch {
//            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
//            handleResponse(resp) {
//                view.showToast("删除成功")
//                view.onDeleteGroupSuccess(position)
//            }
//        }
    }

    fun getShopId() : Int {
        return UserManager.getLoginInfo()?.shopId?:0;
    }

    override fun addGroup(str: String, level : Int) {
        mCoroutine.launch {
            val groupVO = ShopGroupVO();
            groupVO.shopId = getShopId().toString();
            groupVO.shopCatName = str;
            groupVO.showLevel = level;
            groupVO.shopCatPid = "0"
            groupVO.isTop = 0;
            //groupVO.shopCatId = getShopId().toString();

            val resp = GoodsRepository.submitShopGroup(groupVO)
            handleResponse(resp) {
                view.onGroupAdded(groupVO)
            }
        }
    }

}