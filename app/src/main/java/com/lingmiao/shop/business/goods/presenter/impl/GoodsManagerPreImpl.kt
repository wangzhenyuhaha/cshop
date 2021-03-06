package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsManagerPre
import com.lingmiao.shop.business.me.api.MeRepository
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsManagerPreImpl(var context: Context, var view: GoodsManagerPre.View) : BasePreImpl(view),
    GoodsManagerPre {

    //选择分类
    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }

    //
    private val mGroupPreImpl: PopGroupPreImpl by lazy { PopGroupPreImpl(view) }

    var shopId: Int? = null

    fun getSellerId(): Int? {
        if (shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId
        }
        return shopId
    }

    override fun showCategoryPop(target: View) {

        //加载店铺的分类
        mCategoryPreImpl.showCenterThreeCategoryPop(context, "0", { items, _ ->
            if (items?.size ?: 0 > 0) {
                val item = items?.get(items.size - 1)
                view.onUpdateCategory(item)
            }
        })


//        if (UserManager.getLoginInfo()?.goodsCateId == null) {
//            mCoroutine.launch {
//                val resp = MeRepository.apiService.getShop().awaitHiResponse()
//                if (resp.isSuccess) {
//                    mCategoryPreImpl.showCenterCategoryPop(
//                        context,
//                        resp.data.goodsManagementCategory ?: "0"
//                    ) { items, str ->
//                        if (items?.size ?: 0 > 0) {
//                            val item = items?.get(items.size - 1)
//                            view.onUpdateCategory(item)
//                        }
//                    }
//                } else {
//                    mCategoryPreImpl.showCenterCategoryPop(context, "0") { items, str ->
//                        if (items?.size ?: 0 > 0) {
//                            val item = items?.get(items.size - 1)
//                            view.onUpdateCategory(item)
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            val id = UserManager.getLoginInfo()?.goodsCateId ?: "0"
//            mCategoryPreImpl.showCenterCategoryPop(context, id) { items, _ ->
//                if (items?.size ?: 0 > 0) {
//                    val item = items?.get(items.size - 1)
//                    view.onUpdateCategory(item)
//                }
//            }
//        }


    }

    override fun loadListData(page: IPage, oldDatas: List<*>, cId: String) {
        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = GoodsRepository.getCenterGoods(page.getPageIndex(), cId)
            if (resp.isSuccess) {
                val goodsList = resp.data.data

                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun add(ids: String, categoryId: String?, shopCatId: String?) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = GoodsRepository.addGoodsOfCenter(ids, categoryId, shopCatId, 0)

            handleResponse(resp) {
                view.onAddSuccess()
            }
            view.hideDialogLoading()
        }
    }


    //加载goods_management_category
    override fun loadCID() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            if (resp.isSuccess) {
                view.setCid(resp.data.goodsManagementCategory ?: "0")
            }

        }
    }

    override fun showCategoryPop() {
        mCategoryPreImpl.showCategoryPop(context, getSellerId()!!) { cate, names ->
            view.onUpdateCategory(cate?.categoryId, names)
        }
    }

    override fun showGroupPop() {
        mGroupPreImpl.showGoodsGroupPop(context) { group, names ->
            view.onUpdateGroup(group?.shopCatId, names)
        }
    }
}