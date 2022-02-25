package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.goods.api.request.RiderSettingVo
import com.lingmiao.shop.business.goods.presenter.impl.ItemPopPreImpl
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.OperationSettingPresenter
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import kotlinx.coroutines.launch

class OperationSettingPreImpl(val context: Context, val view: OperationSettingPresenter.View) :
    BasePreImpl(view), OperationSettingPresenter {

    //加载店铺数据
    override fun loadShopInfo() {
        mCoroutine.launch {
            view.showPageLoading()
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            handleResponse(resp) {
                view.onLoadedShopInfo(resp.data)
            }
            view.hidePageLoading()
        }
    }

    private val mItemPreImpl: ItemPopPreImpl by lazy { ItemPopPreImpl(view) }

    override fun showWorkTimePop(target: android.view.View) {
        mItemPreImpl.showWorkTimePop(context, "") { item1: WorkTimeVo?, item2: WorkTimeVo? ->
            view.onUpdateWorkTime(item1, item2)
        }
    }

    override fun loadTemplate() {
        mCoroutine.launch {
            val tcsp = ToolsRepository.shipTemplates(FreightVoItem.TYPE_LOCAL)
            val qssp = ToolsRepository.shipTemplates(FreightVoItem.TYPE_QISHOU)
            if (tcsp.isSuccess && qssp.isSuccess) {
                val tcItem = if (tcsp.data?.size ?: 0 > 0) tcsp.data?.get(0) else null
                val qsItem = if (qssp.data?.size ?: 0 > 0) qssp.data?.get(0) else null
                view.onLoadedTemplate(tcItem, qsItem)
            }
        }
    }

    override fun updateModel(id: String, isToSeller: Int, toRiderTime: Int) {
        mCoroutine.launch {
            val req = RiderSettingVo()
            req.toSeller = isToSeller
            req.toSellerTime = toRiderTime
            ToolsRepository.updateRiderSetting(id, req)
        }
    }

    override fun takeSelf(type: Int) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MeRepository.apiService.setTakeSelf(type).awaitHiResponse()
            if (resp.isSuccess) {
                view.hideDialogLoading()
                view.showToast("保存成功")
            } else {
                view.hideDialogLoading()
                //保存失败，开关恢复原样
                view.setTakeSelfFailed()
            }

        }
    }

    override fun setSetting(data: ApplyShopInfo) {
        mCoroutine.launch {
            view.showDialogLoading()
            updateShop(data)
        }
    }

    private fun updateShop(data: ApplyShopInfo) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.updateShop(data).awaitHiResponse()
            if (resp.isSuccess) {
                view.showToast("保存成功")
                view.onSetSetting()
            } else {
                view.showToast("保存失败")
            }
            view.hideDialogLoading()
        }
    }
}
