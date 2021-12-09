package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.pop.GoodsCategoryPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.adapter.GoodsCategoryAdapter
import com.lingmiao.shop.business.goods.pop.GoodsThreeCategoryPop
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class PopCategoryPreImpl(val view: BaseView) : BasePreImpl(view) {

    companion object {
        // 一级商品分类id
        const val LV1_CATEGORY_ID = "0"
    }

    private var categoryName: String? = null
    private var categoryPop: GoodsCategoryPop? = null

    //三级分类PopWindow
    private var categoryThreePop: GoodsThreeCategoryPop? = null

    private var lv1Cache: MutableList<CategoryVO> = mutableListOf()
    private var lv2CacheMap: HashMap<String, List<CategoryVO>> = HashMap()
    private var lv3CacheMap: HashMap<String, List<CategoryVO>> = HashMap()

    var shopId: Int? = null;

    fun getSellerId(): String {
        if (shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId
        }
        return String.format("%s", shopId)
    }

    fun showCategoryPop(context: Context, id: Int, callback: (CategoryVO?, String?) -> Unit) {
        mCoroutine.launch {
            // 一级类目 categoryId=0
            val resp = GoodsRepository.loadUserCategory("0", getSellerId())
            //GoodsRepository.loadUserCategory(LV1_CATEGORY_ID, String.format("%s", id))
            if (resp.isSuccess) {
                showPopWindow(context, resp.data, callback)
            }
        }
    }

    fun showCenterCategoryPop(
        context: Context,
        id: String,
        callback: (List<CategoryVO>?, String?) -> Unit
    ) {
        mCoroutine.launch {
            // 一级类目 categoryId=0
            val resp = GoodsRepository.loadUserCategory(id, "0");
            //GoodsRepository.loadUserCategory(LV1_CATEGORY_ID, String.format("%s", id))
            if (resp.isSuccess) {
                showCenterPopWindow(context, resp.data, callback)
            }
        }
    }

    //中心库三级分类
    fun showCenterThreeCategoryPop(
        context: Context,
        id: String,
        callback: (List<CategoryVO>?, String?) -> Unit,
        query: (String, adapter: GoodsCategoryAdapter) -> Unit = { string, adapter ->
            view.showDialogLoading()
            mCoroutine.launch {
                val resp = GoodsRepository.loadUserCategory(string, "0")
                if (resp.isSuccess) {
                    adapter.replaceData(resp.data)
                    adapter.notifyDataSetChanged()
                    view.hideDialogLoading()
                }
            }
        }
    ) {
        mCoroutine.launch {
            // 一级类目 categoryId=0
            val resp = GoodsRepository.loadUserCategory(id, "0")
            if (resp.isSuccess) {
                showCenterThreePopWindow(context, resp.data, callback, query)
            }
        }
    }

    fun showCategoryPop(context: Context, callback: (CategoryVO?, String?) -> Unit) {
        showCategoryPop(context, 0, callback)
    }

    private fun showCenterPopWindow(
        context: Context,
        list: List<CategoryVO>,
        callback: (List<CategoryVO>?, String?) -> Unit
    ) {
        if (list == null || list?.size == 0) {
            view?.showToast("没有查找到相关分类")
            return;
        }
        categoryPop = GoodsCategoryPop(context).apply {
            lv1Callback = { items, names ->
                callback.invoke(items, names)
            }
        }
        categoryPop?.setLv1Data(list)
        categoryPop?.showPopupWindow()
    }


    private fun showCenterThreePopWindow(
        context: Context,
        list: List<CategoryVO>,
        callback: (List<CategoryVO>?, String?) -> Unit,
        query: (String, adapter: GoodsCategoryAdapter) -> Unit
    ) {
        if (list.isEmpty()) {
            view.showToast("没有查找到相关分类")
            return
        }
        categoryThreePop = GoodsThreeCategoryPop(context).apply {
            lv1Callback = { items, names ->
                callback.invoke(items, names)
            }
            this.query = query
        }

        categoryThreePop?.setLv1Data(list.filter { !it.children.isNullOrEmpty() })
        categoryThreePop?.showPopupWindow()
    }

    private fun showPopWindow(
        context: Context,
        list: List<CategoryVO>,
        callback: (CategoryVO?, String?) -> Unit
    ) {
        if (list == null || list?.size == 0) {
            view?.showToast("没有查找到相关分类")
            return;
        }
        categoryPop = GoodsCategoryPop(context).apply {
            lv1Callback = { items, names ->
                if (items != null && items?.size > 0) {
                    val it = items?.get(items?.size - 1)
                    callback.invoke(it, names)
                }
            }
        }
        categoryPop?.setLv1Data(list)
        categoryPop?.showPopupWindow()
    }

    override fun onDestroy() {
        lv1Cache.clear()
        lv2CacheMap.clear()
        lv3CacheMap.clear()
        super.onDestroy()
    }
}