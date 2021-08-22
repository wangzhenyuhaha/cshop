package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.business.goods.presenter.impl.GoodsMenuPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.SearchStatusPreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.ApplyBank
import com.lingmiao.shop.business.main.presenter.SubBranchPre
import kotlinx.coroutines.launch


class SubBranchPreImpl(var context: Context, var view: SubBranchPre.StatusView) : BasePreImpl(view),
    SubBranchPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }

    private val statusPreImpl: SearchStatusPreImpl by lazy {
        SearchStatusPreImpl(context, view);
    }

    override fun loadListData(page: IPage, oldDatas: List<*>, searchText: String?) {
        Log.d("WZYTest", "ccc")

        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            Log.d("WZY", "aaaaaa")
            val body = ApplyBank()
            body.pageNum = page.getPageIndex()
            body.pageSize = 30
            val subBody = ApplyBank.ApplyBankDetail()
            subBody.bafName = searchText
            body.body = subBody

            val response = MainRepository.apiService.searchBankCard(body)
                .awaitHiResponse()


            Log.d("WZY", "123")
            if (response.isSuccess) {

                val goodsList = response.data.records
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {

                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun loadSubListData(
        page: IPage,
        oldDatas: List<*>,
        bank: String,
        searchText: String?
    ) {
//        if (searchText.isNullOrBlank()) {
//            view.showToast("请输入搜索文字")
//            return
//        }

        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }

            val body = ApplyBank()

            body.pageNum = page.getPageIndex()
            body.pageSize = 30
            val bodyDetail = ApplyBank.ApplyBankDetail()
            bodyDetail.bankCode = bank
            bodyDetail.netBankName = searchText
            body.body = bodyDetail

            val response = MainRepository.apiService.searchSubBankCard(body)
                .awaitHiResponse()
            Log.d("AAA", page.getPageIndex().toString())
            //page.getPageIndex()
            if (response.isSuccess) {
                Log.d("AAA", "Success")
                val goodsList = response.data.records
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                Log.d("AAA", "NoSuccess")
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

//    override fun clickMenuView(goodsVO: BankDetail?, position: Int, target: View) {
//        if (goodsVO == null) {
//            return
//        }
//        menuPopPre.showMenuPop(goodsVO.getMenuType(), target) { menuType ->
//            when (menuType) {
//                GoodsMenuPop.TYPE_EDIT -> {
//                    menuPopPre.clickEditGoods(context, goodsVO)
//                }
//                GoodsMenuPop.TYPE_ENABLE -> {
//                    menuPopPre.clickEnableGoods(goodsVO.goodsId) {
//                        view.onGoodsEnable(goodsVO.goodsId, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_DISABLE -> {
//                    menuPopPre.clickDisableGoods(goodsVO.goodsId) {
//                        view.onGoodsDisable(goodsVO.goodsId, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_QUANTITY -> {
//                    menuPopPre.clickQuantityGoods(goodsVO.goodsId) {
//                        view.onGoodsQuantity(it, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_DELETE -> {
//                    menuPopPre.clickDeleteGoods(goodsVO.goodsId) {
//                        view.onGoodsDelete(goodsVO.goodsId, position)
//                    }
//                }
//            }
//        }
//    }
//
//    override fun clickItemView(item: BankDetail?, position: Int) {
//        GoodsPublishNewActivity.openActivity(context, item?.goodsId)
//    }
//
//    override fun clickSearchMenuView(target: View) {
//        statusPreImpl?.showMenuPop(
//            StatusMenuPop.TYPE_GOODS_OWNER or StatusMenuPop.TYPE_GOODS_NAME,
//            target) { menuType ->
//            when (menuType) {
//                StatusMenuPop.TYPE_GOODS_OWNER->{
//                    view?.onShiftGoodsOwner()
//                }
//                StatusMenuPop.TYPE_GOODS_NAME->{
//                    view?.onShiftGoodsName()
//                }
//            }
//        };
//    }

}