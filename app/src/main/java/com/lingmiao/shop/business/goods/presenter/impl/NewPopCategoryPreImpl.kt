package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.pop.GoodsCategoryPop
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.commonpop.adapter.CenterItemAdapter
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsThreeItemPop
import com.lingmiao.shop.business.commonpop.pop.AbsTwoItemPop
import com.lingmiao.shop.business.goods.adapter.GoodsCategoryAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class NewPopCategoryPreImpl(view: BaseView) : BasePreImpl(view) {

    companion object {
        // 一级商品分类id
        const val LV1_CATEGORY_ID = "0"
    }

    private var categoryName: String? = null
    private var categoryPop: GoodsCategoryPop? = null
    private var typePop: AbsThreeItemPop<CategoryVO>? = null

    private var lv1Cache: MutableList<CategoryVO> = mutableListOf()
    private var lv2CacheMap: HashMap<String, List<CategoryVO>> = HashMap()
    private var lv3CacheMap: HashMap<String, List<CategoryVO>> = HashMap()

    private var mSelectList: MutableList<CategoryVO> = arrayListOf();
    fun showTypePop(context: Context, targetView : View, callback: (List<CategoryVO>?, String?) -> Unit) {
        mCoroutine.launch {
            // 一级类目 categoryId=0
            val resp = GoodsRepository.loadUserCategory(UserManager?.getLoginInfo()?.goodsCateId?:"0","0")
            if (resp.isSuccess) {
                showTypePop(context, targetView, resp.data, callback)
            }
        }
    }

    private fun showTypePop(
        context: Context,
        targetView : View,
        list: List<CategoryVO>,
        callback: (List<CategoryVO>?, String?) -> Unit
    ) {
        typePop = object : AbsTwoItemPop<CategoryVO>(context, ""){

            override fun getAdapter1(): DefaultItemAdapter<CategoryVO> {
                return DefaultItemAdapter();
            }

            override fun getAdapter2(): DefaultItemAdapter<CategoryVO> {
                return DefaultItemAdapter();
            }

            override fun getData2(data1: CategoryVO): List<CategoryVO> {
                return mutableListOf();
            }

            override fun getLevel(): Int {
                return LEVEL_2;
            }

        }.apply {
            lv1Callback = {
                mSelectList.clear();
                mSelectList.add(it);
                categoryName = it.name
                loadLv2Category2(it);
            }
            lv2Callback = {
                mSelectList.add(it);
                categoryName = "${categoryName}/${it.name}"
                callback.invoke(mSelectList, categoryName)
                dismiss();
            }
        }
        typePop?.setLv1Data(list)
        typePop?.showPopupWindow(targetView)
    }

    private fun loadLv2Category2(categoryVO: CategoryVO) {
        if (categoryVO.categoryId.isNullOrBlank()) return
        val lv2List = lv2CacheMap[categoryVO.categoryId!!]
        if (lv2List.isNotEmpty()) {
            typePop?.setLv2Data(lv2List!!)
            return
        }

        mCoroutine.launch {
            val resp = GoodsRepository.loadCategory(categoryVO.categoryId!!)
            if (resp.isSuccess) {
                lv2CacheMap[categoryVO.categoryId!!] = resp.data
                typePop?.setLv2Data(resp.data)
            }
        }
    }

    private fun loadLv2Category(categoryVO: CategoryVO) {
        if (categoryVO.categoryId.isNullOrBlank()) return
        val lv2List = lv2CacheMap[categoryVO.categoryId!!]
        if (lv2List.isNotEmpty()) {
            categoryPop?.setLv2Data(lv2List!!)
            return
        }

        mCoroutine.launch {
            val resp = GoodsRepository.loadCategory(categoryVO.categoryId!!)
            if (resp.isSuccess) {
                lv2CacheMap[categoryVO.categoryId!!] = resp.data
                categoryPop?.setLv2Data(resp.data)
            }
        }
    }

    private fun showPopWindow(
        context: Context,
        list: List<CategoryVO>,
        callback: (String?, String?) -> Unit
    ) {
        categoryPop = GoodsCategoryPop(context).apply {
            lv1Callback = {
                categoryName = it.name
                loadLv2Category(it)
            }
            lv2Callback = {
                categoryName = "${categoryName}/${it.name}"
                loadLv3Category(it)
            }
            lv3Callback = {
                categoryName = "${categoryName}/${it.name}"
                callback.invoke(it.categoryId, categoryName)
            }
        }
        categoryPop?.setLv1Data(list)
        categoryPop?.showPopupWindow()
    }

    private fun loadLv3Category(categoryVO: CategoryVO) {
        if (categoryVO.categoryId.isNullOrBlank()) return
        val lv3List = lv3CacheMap[categoryVO.categoryId!!]
        if (lv3List.isNotEmpty()) {
            categoryPop?.setLv3Data(lv3List!!)
            return
        }

        mCoroutine.launch {
            val resp = GoodsRepository.loadCategory(categoryVO.categoryId!!)
            if (resp.isSuccess) {
                lv3CacheMap[categoryVO.categoryId!!] = resp.data
                categoryPop?.setLv3Data(resp.data)
            }
        }
    }

    override fun onDestroy() {
        lv1Cache.clear()
        lv2CacheMap.clear()
        lv3CacheMap.clear()
        super.onDestroy()
    }
}