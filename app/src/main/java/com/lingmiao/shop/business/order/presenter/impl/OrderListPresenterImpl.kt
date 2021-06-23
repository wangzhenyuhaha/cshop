package com.lingmiao.shop.business.order.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.api.OrderRepository
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.bean.OrderTabNumberEvent
import com.lingmiao.shop.business.order.presenter.OrderListPresenter
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.order.bean.OrderServiceVo
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class OrderListPresenterImpl(var view: OrderListPresenter.StatusView) : BasePreImpl(view),
    OrderListPresenter {
    override fun loadListData(
        page: IPage,
        status: String,
        start: Long?,
        end: Long?,
        datas: MutableList<OrderList>
    ) {
        mCoroutine.launch {

            if (datas.isEmpty()) {
                view.showPageLoading()
            }

            val resp = OrderRepository.apiService.getOrderList(page.getPageIndex().toString(),IConstant.PAGE_SIZE.toString(),
                status, start, end).awaitHiResponse()
            if (resp.isSuccess) {
                val orderList = resp.data.data
                LogUtils.d("orderList:"+orderList?.size)
                if(page.isRefreshing()){
                    LogUtils.d("page.isRefreshing()")
                    EventBus.getDefault().post(OrderTabNumberEvent(status,resp.data.dataTotal))
                }
                view.onLoadMoreSuccess(orderList, orderList.isNotEmpty())
                if(page.isRefreshing()&&orderList.isNullOrEmpty()){
//                    view.showNoData()
                    LogUtils.d("showNoData")
                }
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }


    override fun deleteOrder(tradeSn: String) {
        mCoroutine.launch {
            try {
                val resp = OrderRepository.apiService.deleteOrder(tradeSn)
                view.hideDialogLoading()
                LogUtils.d("lqx","deleteOrder:"+ resp.isSuccessful)
                if (resp.isSuccessful) {
                    view.onDeleteOrderSuccess()
                } else {
                }
            }catch (e:Exception){
                e.printStackTrace()
                view.hideDialogLoading()
            }

        }
    }

    override fun takeOrder(tradeSn: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.takeOrder(tradeSn, 1);
            if(resp.isSuccessful) {
                view.onTakeSuccess()
            }
            view.hideDialogLoading()
        }
    }

    override fun refuseOrder(tradeSn: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.takeOrder(tradeSn, 0);
            if(resp.isSuccessful) {
                view.onRefuseSuccess()
            }
            view.hideDialogLoading()
        }
    }

    override fun shipOrder(sn: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.ship(sn);
            if(resp.isSuccessful) {
                view.onShipped()
            }
            view.hideDialogLoading()
        }
    }

    override fun signOrder(sn: String) {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.sign(sn);
            if(resp.isSuccessful) {
                view.onSigned()
            }
            view.hideDialogLoading()
        }
    }

    override fun refuseService(sn: String) {
        mCoroutine.launch {
            val item = OrderServiceVo();
            val resp = OrderRepository.apiService.service(sn, item);
            if(resp.isSuccessful) {
                view.onRefusedService()
            }
            view.hideDialogLoading()
        }
    }

    override fun acceptService(sn: String) {
        mCoroutine.launch {
            val item = OrderServiceVo();
            val resp = OrderRepository.apiService.service(sn, item);
            if(resp.isSuccessful) {
                view.onAcceptService()
            }
            view.hideDialogLoading()
        }
    }

    override fun verifyOrder(id: String) {
        mCoroutine.launch {
            view?.showDialogLoading();
            val resp = OrderRepository.apiService.verifyOrderShip(id).awaitHiResponse()
            if (resp.isSuccess && resp?.data?.status == true) {
                view?.verifySuccess()
                view?.showToast(resp?.data?.desc)
            } else {
                view?.verifyFailed()
            }
            view?.hideDialogLoading();
        }
    }

}
