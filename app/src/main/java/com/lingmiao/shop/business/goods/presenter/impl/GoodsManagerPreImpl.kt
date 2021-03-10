package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.GoodsManagerPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsManagerPreImpl(var context: Context, var view: GoodsManagerPre.View) : BasePreImpl(view),GoodsManagerPre {

    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }

    override fun showCategoryPop(target: View) {
        mCategoryPreImpl?.showTypePop(context, target) { categoryId, categoryName ->
            view.onUpdateCategory(categoryId, categoryName)
        }
    }

    override fun loadListData(page: IPage, oldDatas: List<*>, cId: String) {
        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = GoodsRepository.loadGoodsListByCId(page.getPageIndex(), cId)
            if (resp.isSuccess) {
                val goodsList = datas();//resp.data.data

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