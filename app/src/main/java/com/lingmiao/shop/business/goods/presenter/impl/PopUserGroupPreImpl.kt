package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.pop.GoodsGroupPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分组
 */
class PopUserGroupPreImpl(view: BaseView) : BasePreImpl(view) {

    private var goodsGroupPop: GoodsGroupPop? = null

    private var lv1Cache: MutableList<ShopGroupVO> = mutableListOf()

    fun showGoodsGroupPop(
        context: Context,
        isTop: Int,
        callback: (List<ShopGroupVO>?, String?) -> Unit
    ) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup(isTop)
            if (resp.isSuccess) {
                showPopWindow(context, resp.data, callback)
            }
        }
    }

    //获取全部菜单
    fun showAllGoodsGroupPop(
        context: Context,
        callback: (List<ShopGroupVO>?, String?) -> Unit
    ) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup()
            if (resp.isSuccess) {
                showPopWindow(context, resp.data, callback)
            }
        }
    }

    fun showGoodsGroupPop(
        context: Context,
        isTop: Int,
        path: String?,
        callback: (List<ShopGroupVO>?, String?) -> Unit
    ) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup(isTop, path)
            if (resp.isSuccess) {
                showPopWindow(context, resp.data.filter { it.catPath.equals(path) }, callback)
            }
        }
    }

    private fun showPopWindow(
        context: Context,
        list: List<ShopGroupVO>,
        callback: (List<ShopGroupVO>?, String?) -> Unit
    ) {
        goodsGroupPop = GoodsGroupPop(context).apply {
            lv1Callback = { groupVO, groupName ->
                callback.invoke(groupVO, groupName)
            }
        }
        goodsGroupPop?.setTitle("请选择商品菜单", "(移动商品到所选菜单)")
        goodsGroupPop?.setLv1Data(list)
        goodsGroupPop?.showPopupWindow()
    }

    override fun onDestroy() {
        lv1Cache.clear()
        super.onDestroy()
    }
}