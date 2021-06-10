package com.lingmiao.shop.business.goods.presenter.impl

import android.app.Activity
import android.content.Context
import android.view.View
import com.amap.api.mapcore.util.it
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.RebateResponseVo
import com.lingmiao.shop.business.goods.api.bean.RebateVo
import com.lingmiao.shop.business.goods.pop.GoodsRebatePop
import com.lingmiao.shop.business.goods.presenter.GoodsBatchPre
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.bean.ShareVo
import kotlinx.coroutines.launch

open class GoodsBatchPreImpl(open val context: Context,open val view: GoodsBatchPre.View) : BasePreImpl(view),GoodsBatchPre {


    override fun clickOffLine(oldList: List<GoodsVO>, callback: () -> Unit) {
        var list = oldList.filter { it?.isChecked == true };
        if(list?.size == 0) {
            view?.showToast("请选择需要下架的商品!");
            return;
        }
        if(list?.filter { it?.marketEnable == 0 }?.size > 0) {
            view.showToast("你选择的商品中存在已下架商品，已下架的商品不能进行批量下架操作！");
            return;
        }
        var text = String.format("已选中%s个商品，下架后不可售卖，确定要下架吗？", list?.size);
        var goodsIds = list?.map { it?.goodsId }.joinToString(separator = ",");
        DialogUtils.showDialog(
            context as Activity, "批量下架提示", text,
            "取消", "确定下架", View.OnClickListener {

            }, View.OnClickListener {
                offLine(goodsIds, callback);
            })
    }

    override fun clickOnLine(oldList: List<GoodsVO>, callback: () -> Unit) {
        var list = oldList.filter { it?.isChecked == true };
        if(list?.size == 0) {
            view?.showToast("请选择需要上架的商品!");
            return;
        }
        if(list?.filter { it?.marketEnable == 1 }?.size > 0) {
            view?.showToast("你选择的商品中存在已上架商品，已上架的商品不能进行批量上架操作！");
            return;
        }
        var text = String.format("已选中%s个商品，确定要上架吗？", list?.size);
        var goodsIds = list?.map { it?.goodsId }.joinToString(separator = ",");
        DialogUtils.showDialog(
            context as Activity, "批量上架提示", text,
            "取消", "确定上架", View.OnClickListener {

            }, View.OnClickListener {
                onLine(goodsIds, callback);
            })
    }

    override fun clickOnBatchRebate(oldList: List<GoodsVO>, callback: () -> Unit) {
        var list = oldList.filter { it?.isChecked == true };
        if(list?.size == 0) {
            view?.showToast("请选择需要设置佣金的商品!");
            return;
        }
        var str = list?.map { it?.goodsId }.joinToString(separator = ",");

        showRebatePop(str, list?.size, callback);
    }

    override fun clickOnRebate(goodsId: String?, callback: () -> Unit) {
        if(goodsId.isNullOrEmpty()) {
            return;
        }
        getRebateById(goodsId) {
            val rebatePop = GoodsRebatePop(context!!)
            rebatePop.setFirstRebate(it?.grade1Rebate ?: "");
            rebatePop.setSecondRebate(it?.grade2Rebate ?: "");
            rebatePop.setInviteRebate(it?.inviterRate ?: "");
            rebatePop.setConfirmListener { first, second, invite ->
                if (first.isNullOrBlank()) {
                    view?.showToast("请输入一级级分销佣金")
                    return@setConfirmListener
                }
                if (second.isNullOrBlank()) {
                    view?.showToast("请输入二级级分销佣金")
                    return@setConfirmListener
                }
                if (invite.isNullOrBlank()) {
                    view?.showToast("请输入会员邀请佣金")
                    return@setConfirmListener
                }
                var rebate = RebateVo(first, second, invite);
                exeRebateRequest(goodsId, rebate) {
                    rebatePop.dismiss();
                    callback.invoke();
                }
            }
            rebatePop.showPopupWindow()
        }
    }

    override fun clickDelete(oldList: List<GoodsVO>?, callback: (ids:String) -> Unit) {
        var list = oldList?.filter { it?.isChecked == true };
        if(list?.size == 0) {
            view?.showToast("请选择需要删除的商品!");
            return;
        }

        val ids = list?.map { it?.goodsId }?.joinToString(separator = ",");
        DialogUtils.showDialog(context as Activity,
            "删除提示", "删除后不可恢复，确定要删除该商品吗？",
            "取消", "确定删除",
            null, View.OnClickListener {
                exeBatchDeleteRequest(ids!!, {
                    callback.invoke(ids);
                });
            });
    }

    override fun getCheckedCount(list: List<GoodsVO>?): Int {
        return list?.filter { it?.isChecked == true }?.size?:0;
    }

    override fun clickShare(id: GoodsVO?, callback: (ShareVo) -> Unit) {
        mCoroutine.launch {
            val item = MainRepository.apiService.getShareInfo(1, id?.goodsId!!, 1).awaitHiResponse()
            handleResponse(item) {
                callback.invoke(it);
            }
        }
    }

    /**
     * 批量删除
     */
    private fun exeBatchDeleteRequest(goodsId: String, callback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.deleteGoods(goodsId)
            if(resp?.isSuccess) {
                view?.showToast("删除成功")
                callback.invoke();
            } else {
                view?.showToast("操作失败")
            }
        }
    }

    /**
     * 批量修改佣金
     */
    private fun showRebatePop(goodsIds: String, count : Int, callback: () -> Unit) {
        if(context!=null) {
            val rebatePop = GoodsRebatePop(context!!)
            rebatePop.setGoodsCount(count);
            rebatePop.setConfirmListener { first, second, invite ->
                if (first.isNullOrBlank()) {
                    view?.showToast("请输入一级级分销佣金")
                    return@setConfirmListener
                }
                if (first.isNullOrBlank()) {
                    view?.showToast("请输入二级级分销佣金")
                    return@setConfirmListener
                }
                if (first.isNullOrBlank()) {
                    view?.showToast("请输入会员邀请佣金")
                    return@setConfirmListener
                }
                var rebate = RebateVo(first, second, invite);

                exeBatchRebateRequest(goodsIds, rebate) {
                    rebatePop.dismiss();
                    callback.invoke();
                }
            }
            rebatePop.showPopupWindow()
        }

    }

    /**
     * 更新佣金
     */
    private fun getRebateById(goodsId: String, callback: (RebateResponseVo) -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadRebateById(goodsId)
            if(resp?.isSuccess) {
                callback.invoke(resp.data);
            } else {
//                view?.showToast("更新失败")
                callback.invoke(RebateResponseVo());
            }
        }
    }

    /**
     * 批量更新佣金
     */
    private fun exeRebateRequest(goodsId: String, rebate: RebateVo, callback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.rebate(goodsId, rebate)
            if(resp?.isSuccess) {
                view?.showToast("设置成功")
                callback.invoke();
            } else {
                view?.showToast("操作失败")
            }
        }
    }

    /**
     * 批量更新佣金
     */
    private fun exeBatchRebateRequest(goodsId: String, rebate: RebateVo, callback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.batchRebate(goodsId, rebate)
            if(resp?.isSuccess) {
                view?.showToast("设置成功")
                callback.invoke();
            } else {
                view?.showToast("操作失败")
            }
        }
    }

    /**
     * 商品上架
     */
    private fun onLine(goodsId: String?, callback: () -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.makeGoodsEnable(goodsId)

            if(resp?.isSuccess) {
                view?.showToast("上架成功")
                callback.invoke()
            } else {
                view?.showToast("操作失败")
            }
        }
    }

    /**
     * 商品下架
     */
    private fun offLine(goodsId: String?, callback: () -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.makeGoodsDisable(goodsId)
            if(resp?.isSuccess) {
                view?.showToast("下架成功")
                callback.invoke()
            } else {
                view?.showToast("操作失败")
            }
        }
    }

}