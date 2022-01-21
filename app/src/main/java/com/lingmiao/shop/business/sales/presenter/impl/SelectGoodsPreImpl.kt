package com.lingmiao.shop.business.sales.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.impl.PopUserCatePreImpl
import com.lingmiao.shop.business.sales.presenter.SelectGoodsPresenter
import kotlinx.coroutines.launch

class SelectGoodsPreImpl(val context: Context, val view: SelectGoodsPresenter.View) :
    BasePreImpl(view), SelectGoodsPresenter {

    private val mCatePopPreImpl: PopUserCatePreImpl by lazy { PopUserCatePreImpl(view) }

    override fun showCategoryPop(cid: String?, target: View) {
        mCatePopPreImpl.showGoodsGroupPop(context, cid) { its, name ->
            view.onUpdatedCategory(its, name)
        }
    }

    override fun loadListData(cid: String?, page: IPage, list: List<*>) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsListOfCatePath(page.getPageIndex(), cid)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

}
