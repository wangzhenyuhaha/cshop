package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsTypeVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsFragment
import com.james.common.base.BasePreImpl
import com.james.common.exception.BizException
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.exts.*
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.GoodsListActivity
import com.lingmiao.shop.business.goods.presenter.GoodsPublishNewPre
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * @author elson
 * @date 2020/7/30
 * @Desc 发布商品页面
 */
class GoodsPublishPreNewImpl(var context: Context, val view: GoodsPublishNewPre.PublishView) :
    BasePreImpl(view), GoodsPublishNewPre {

    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }
    private val mGroupPreImpl: PopGroupPreImpl by lazy { PopGroupPreImpl(view) }
    private val mExpirePreImpl: ExpirePopPreImpl by lazy { ExpirePopPreImpl(view) }
    private val mUseTimePreImpl: UseTimePopPreImpl by lazy { UseTimePopPreImpl(view) }
    private val mGoodsTypePreImpl: GoodsTypePopPreImpl by lazy { GoodsTypePopPreImpl(view) }
    private val mGoodsDeliveryPreImpl: GoodsDeliveryPopPreImpl by lazy { GoodsDeliveryPopPreImpl(view) }
    private val mGoodsUnitPreImpl : GoodsUnitPopPreImpl by lazy { GoodsUnitPopPreImpl(view) }

    var shopId : Int? = null;

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }


    //GoodsPublishNewPre
    /**
     * 配送方式
     */
    override fun showDeliveryTypePop(str: String?) {
        mGoodsDeliveryPreImpl?.showTypePop(context, str) { name, it ->
            view?.onUpdateSpeed(it?.value, name);
        }
    }

    /**
     * 配送模工
     */
    override fun showDeliveryModelPop(str: String?) {
        mGoodsDeliveryPreImpl?.showModelPop(context, str) { name, it ->
            view?.onUpdateModel(it?.value, name);
        }
    }

    /**
     * 重量
     */
    override fun showGoodsWeightPop(id: String?) {
        mGoodsUnitPreImpl?.showWeightPop(context, id) { name, it ->
            view?.onUpdateGoodsWeight(it?.value, name);
        }
    }

    /**
     * 单位
     */
    override fun showGoodsUnitPop(id: String?) {
        mGoodsUnitPreImpl?.showUnitPop(context, id) { name, it ->
            view?.onUpdateGoodsUnit(it?.value, name);
        }
    }
















    //GoodsPublishPre

    /**
     * 编辑商品时，根据"商品Id"拉去接口数据
     */
    override fun loadGoodsInfo(goodsId: String?) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        view.showDialogLoading()
        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsById(goodsId)
            handleResponse(resp) {
                view.onLoadGoodsSuccess(resp.data)
                view.onSetUseTimeStr(mUseTimePreImpl.getUseTimeStr(resp?.data?.availableDate))
            }
            view.hideDialogLoading()
        }
    }

    /**
     * 发布
     */
    override fun publish(goodsVO: GoodsVOWrapper, isVirtualGoods: Boolean, isMutilSpec: Boolean) {
        loadSpecKeyList(goodsVO, isMutilSpec) {
            try {
                checkNotBlack(goodsVO.goodsName) { "请输入商品名称" }
                checkNotBlack(goodsVO.thumbnail) { "请添加缩略图" }
                checkBoolean(goodsVO.goodsGalleryList.isNotEmpty()) { "请添加商品主图" }
                if (isVirtualGoods) {
                    checkBoolean(goodsVO.skuList.isNotEmpty()) { "请添加商品规格" }
                    checkNotBlack(goodsVO.availableDate) { "请设置使用时间" }
                    checkNotBlack(goodsVO.availableDate) { "请设置有效期" }
                } else {
//                    checkBoolean(goodsVO.isSelectDeliveryWay()) { "请选择配送方式" }
                    if (isMutilSpec) {
                        checkBoolean(goodsVO.skuList.isNotEmpty()) { "请添加商品规格" }
                        checkBoolean(goodsVO.skuList?.get(0)?.specList?.isNotEmpty() == true) { "请完善商品多规格" }
                    } else {
                        checkNotBlack(goodsVO.price) { "请输入商品价格" }
                        checkNotBlack(goodsVO.quantity) { "请输入商品库存" }
                    }
                }

                // 上传图片
                view.showDialogLoading("正在发布...")
                uploadImages(goodsVO, fail = {
                    view.showToast("图片上传失败，请重试")
                    view.hideDialogLoading()
                    return@uploadImages;
                }) {
                    uploadThumbnailImage(goodsVO, fail = {
                        view.showToast("商品缩略图片上传失败，请重试")
                        view.hideDialogLoading()
                        return@uploadThumbnailImage;
                    }) {
                        if(goodsVO?.intro.isNotBlank()) {
                            uploadDesImages(goodsVO, fail = {
                                view.showToast("图片上传失败，请重试")
                                view.hideDialogLoading()
                            }){
                                if (goodsVO.goodsId.isNullOrBlank()) {
                                    submitGoods(goodsVO) // 添加商品
                                } else {
                                    modifyGoods(goodsVO) // 编辑商品
                                }
                            }
                        } else {
                            if (goodsVO.goodsId.isNullOrBlank()) {
                                submitGoods(goodsVO) // 添加商品
                            } else {
                                modifyGoods(goodsVO) // 编辑商品
                            }
                        }
                    }
                }
            } catch (e: BizException) {
                view.showToast(e.message.check())
            }
        }
    }


    override fun showCategoryPop() {
        mCategoryPreImpl.showCategoryPop(context, getSellerId()!!) { cate, names ->
            view.onUpdateCategory(cate?.categoryId, names)
        }
    }

    override fun showGroupPop() {
        mGroupPreImpl.showGoodsGroupPop(context) { group, names ->
            view.onUpdateGroup(group?.shopCatId, names)
        }
    }

    override fun showExpirePop(str: String) {
        mExpirePreImpl.showPop(context, str) { item ->
            view?.onUpdateExpire(item)
        }
    }

    override fun showUseTimePop(str: String) {
        mUseTimePreImpl.showPop(context, str) { items ->
            view?.onUpdateUseTime(items)
        }
    }

    override fun showGoodsType(str: Boolean) {
        mGoodsTypePreImpl.showPop(context, GoodsTypeVO.getValue(str)) { item ->
            view?.onSetGoodsType(GoodsTypeVO.isVirtual(item?.value));
        }
    }
















    /**
     * 编辑商品时，获取商品关联的规格值
     */
    private fun loadSpecKeyList(goodsVO: GoodsVOWrapper, isMutilSpec : Boolean , callback: () -> Unit) {
        if (goodsVO.goodsId.isNullOrBlank() || goodsVO.isAddSpec || !isMutilSpec) {
            callback.invoke()
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsAppSku(goodsVO.goodsId!!)
            handleResponse(resp) {
                goodsVO.skuList = it.skuList
                callback.invoke()
            }
        }
    }

    private fun submitGoods(goodsVO: GoodsVOWrapper) {
        mCoroutine.launch {
            val resp = GoodsRepository.submitGoods(goodsVO)
            view.hideDialogLoading()
            handleResponse(resp) {
                view.showToast("商品上架成功")
                EventBus.getDefault().post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                ActivityUtils.finishToActivity(GoodsListActivity::class.java,false)
               // view.finish()
            }
        }
    }

    private fun modifyGoods(goodsVO: GoodsVOWrapper) {
        mCoroutine.launch {
            val resp = GoodsRepository.modifyGoods(goodsVO.goodsId!!, goodsVO)
            view.hideDialogLoading()
            if (resp.isSuccess) {
                view.showToast("商品修改成功")
                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                view.finish()
            } else {
                handleErrorMsg(resp.msg)
                // 编辑的商品，如果没有设置过规格，如果接口调用失败，就置空规格。
                if (!goodsVO.isAddSpec) {
                    goodsVO.skuList = null
                }
            }
        }
    }

    private fun uploadThumbnailImage(goodsVO: GoodsVOWrapper, fail: () -> Unit, success: () -> Unit) {
        if(goodsVO.thumbnail.isNetUrl()) {
            success.invoke();
            return;
        }
        mCoroutine.launch {
            val resp = CommonRepository.uploadFile(
                File(goodsVO.thumbnail),
                true,
                CommonRepository.SCENE_GOODS
            )
            if(resp.isSuccess) {
                goodsVO.thumbnail = resp.data.url;
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    private fun uploadDesImages(goodsVO: GoodsVOWrapper, fail: () -> Unit, success: () -> Unit) {
        mCoroutine.launch {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            goodsVO.intro?.split(",")?.forEachIndexed { index, it ->
                val request = async {
                    if (it.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it))
                    } else {
                        CommonRepository.uploadFile(
                            File(it),
                            true,
                            CommonRepository.SCENE_GOODS
                        )
                    }
                }
                requestList.add(request)
            }
            if(requestList.size == 0) {
                success.invoke();
                return@launch;
            }
            // 多个接口相互等待
            val respList = requestList.awaitAll()

            var isAllSuccess: Boolean = respList?.filter { !it.isSuccess }?.size == 0

            if (isAllSuccess) {
                val urls = respList.map{it -> it.data.url}.joinToString(separator = ",");

                goodsVO.intro = urls;
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    private fun uploadImages(goodsVO: GoodsVOWrapper, fail: () -> Unit, success: () -> Unit) {
        mCoroutine.launch {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            goodsVO.goodsGalleryList!!.forEach {
                val request = async {
                    if (it.original.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it.original))
                    } else {
                        CommonRepository.uploadFile(
                            File(it.original),
                            true,
                            CommonRepository.SCENE_GOODS
                        )
                    }
                }
                requestList.add(request)
            }

            // 多个接口相互等待
            val respList = requestList.awaitAll()
            var isAllSuccess: Boolean = true
            respList.forEachIndexed { index, it ->
                if (it.isSuccess) {
                    goodsVO.goodsGalleryList?.get(index)?.apply {
                        original = it?.data?.url
                        sort = "${index}"
                    }
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }
}
