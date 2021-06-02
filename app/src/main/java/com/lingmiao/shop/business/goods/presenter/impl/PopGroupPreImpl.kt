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
class PopGroupPreImpl(view: BaseView) : BasePreImpl(view) {

    private var goodsGroupPop: GoodsGroupPop? = null

    private var lv1Cache: MutableList<ShopGroupVO> = mutableListOf()

    fun showGoodsGroupPop(context: Context, callback: (String?, String?) -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadLv1ShopGroup()
            if (resp.isSuccess) {
                showPopWindow(context, resp.data, callback)
            }
        }
    }

    private fun showPopWindow(
        context: Context,
        list: List<ShopGroupVO>,
        callback: (String?, String?) -> Unit
    ) {
        goodsGroupPop = GoodsGroupPop(context).apply {
            lv1Callback = { groupVO, groupName ->
                if(groupVO != null && groupVO?.size > 0) {
                    val it  = groupVO?.get(groupVO?.size -1)
                    callback.invoke(it?.shopCatId, groupName)
                }
            }
        }
        goodsGroupPop?.setLv1Data(list)
        goodsGroupPop?.showPopupWindow()
    }

    override fun onDestroy() {
        lv1Cache.clear()
        super.onDestroy()
    }
}