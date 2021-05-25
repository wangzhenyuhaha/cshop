package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.presenter.GoodsMenuSelectPre
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品待审核列表
 */
class GoodsMenuSelectPreImpl(val context: Context, val view: GoodsMenuSelectPre.StatusView) :
    BasePreImpl(view), GoodsMenuSelectPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }


    override fun loadListData(page: IPage, oldDatas: List<*>) {
        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = GoodsRepository.loadGoodsList(page.getPageIndex(), GoodsVO.MARKET_STATUS_ENABLE.toString(), "1,2");
                //GoodsRepository.loadGoodsListByCId(page.getPageIndex(), "")
            if (resp.isSuccess) {
                val goodsList = resp.data.data

                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    fun datas() : List<GoodsVO> {
        val data = mutableListOf<GoodsVO>()

        data.add(GoodsVO("222", "2",  12324342, 1, "2323",
            "测试可乐", "111", "222", 1,1, 1,
            23.22, 0, "", "", "", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa0.att.hudong.com%2F30%2F29%2F01300000201438121627296084016.jpg&refer=http%3A%2F%2Fa0.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617688152&t=d3e40ffcbb2ac47f8d88127f72876783", ""))


        data.add(GoodsVO("222", "2",  12324342, 1, "2323",
            "测试方便面", "111", "222", 1,1, 1,
            23.22, 0, "", "", "", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2771978851,2906984932&fm=26&gp=0.jpg", ""))
        return data;
    }
}