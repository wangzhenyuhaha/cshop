package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BasePreImpl
import com.james.common.exception.BizException
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.*
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.GoodsListActivity
import com.lingmiao.shop.business.goods.GoodsScanActivity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsTypeVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsFragment
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


    //切换商品类型
    private val mGoodsTypePreImpl: GoodsTypePopPreImpl by lazy { GoodsTypePopPreImpl(view) }
    override fun showGoodsType(isVirtual: Int) {
        mGoodsTypePreImpl.showPop(context, GoodsTypeVO.getValue(isVirtual)) { item ->
            view.onSetGoodsTypeNew(item?.value)
        }
    }

    //选择商品分类
    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }
    override fun showCategoryPop() {
        mCategoryPreImpl.showCategoryPop(context, getSellerId()!!) { cate, names ->
            view.onUpdateCategory(cate?.categoryId, names)
        }
    }

    //选择商品菜单
    private val mGroupPreImpl: PopGroupPreImpl by lazy { PopGroupPreImpl(view) }
    override fun showGroupPop() {
        mGroupPreImpl.showGoodsGroupPop(context) { group, names ->
            view.onUpdateGroup(group?.shopCatId, names)
        }
    }

    //根据商品名搜索商品
    override fun searchGoods(
        searchText: String
    ) {
        mCoroutine.launch {
            val resp =
                GoodsRepository.loadGoodsListByNameFromCenter(
                    1,
                    pageSize = 100,
                    goodsName = searchText
                )
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                if (goodsList != null) {
                    view.searchGoodsSuccess(goodsList)
                }
            } else {
                view.searchGoodsFailed()
            }
            view.hidePageLoading()
        }
    }

    //根据商品名称从中心库搜索商品,从中心库拿到的菜单，分类设为null，暂时只能手动选择
    override fun loadGoodsInfoFromCenter(id: String) {
        view.showDialogLoading()
        mCoroutine.launch {
            val resp = GoodsRepository.searchGoodsFromCenter(id)
            handleResponse(resp) {
                view.onLoadGoodsSuccess(resp.data, true)
            }
            view.hideDialogLoading()
        }
    }


    private val mExpirePreImpl: ExpirePopPreImpl by lazy { ExpirePopPreImpl(view) }
    private val mUseTimePreImpl: UseTimePopPreImpl by lazy { UseTimePopPreImpl(view) }

    private val mGoodsDeliveryPreImpl: GoodsDeliveryPopPreImpl by lazy {
        GoodsDeliveryPopPreImpl(
            view
        )
    }
    private val mGoodsUnitPreImpl: GoodsUnitPopPreImpl by lazy { GoodsUnitPopPreImpl(view) }

    var shopId: Int? = null

    fun getSellerId(): Int? {
        if (shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId
        }
        return shopId
    }


    //GoodsPublishNewPre
    /**
     * 配送方式
     */
    override fun showDeliveryTypePop(str: String?) {
        mGoodsDeliveryPreImpl.showTypePop(context, str) { name, it ->
            view.onUpdateSpeed(it?.value, name)
        }
    }

    /**
     * 配送模工
     */
    override fun showDeliveryModelPop(str: String?) {
        mGoodsDeliveryPreImpl.showModelPop(context, str) { name, it ->
            view.onUpdateModel(it?.value, name)
        }
    }

    /**
     * 重量
     */
    override fun showGoodsWeightPop(id: String?) {
        mGoodsUnitPreImpl?.showWeightPop(context, id) { name, it ->
            view?.onUpdateGoodsWeight(it?.value, name)
        }
    }

    /**
     * 单位
     */
    override fun showGoodsUnitPop(id: String?) {
        mGoodsUnitPreImpl?.showUnitPop(context, id) { name, it ->
            view?.onUpdateGoodsUnit(it?.value, name)
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
                view.onLoadGoodsSuccess(resp.data, false)
                view.onSetUseTimeStr(mUseTimePreImpl.getUseTimeStr(resp?.data?.availableDate))
            }
            view.hideDialogLoading()
        }
    }


    //发布券包
    override fun publishTicket(
        goodsVO: GoodsVOWrapper,
        isVirtualGoods: Boolean,
        isMutilSpec: Boolean,
        scan: Boolean,
        type: Int,
        isFromCenter: Int
    ) {
        loadSpecKeyList(goodsVO, isMutilSpec) {

            try {
                checkNotNull(goodsVO.num) { "请输入券包内电子券的数量" }
                checkNotNull(goodsVO.couponID) { "请输入券包绑定的商品" }
                checkNotBlack(goodsVO.goodsName) { "请输入商品名称" }
                checkNotBlack(goodsVO.goodsName) { "请输入商品名称" }
                checkNotBlack(goodsVO.thumbnail) { "请添加缩略图" }
                checkBoolean(goodsVO.goodsGalleryList.isNotEmpty()) { "请添加商品主图" }
                if (isVirtualGoods) {
                    //useless
                    checkBoolean(goodsVO.skuList.isNotEmpty()) { "请添加商品规格" }
                    checkNotBlack(goodsVO.availableDate) { "请设置使用时间" }
                    checkNotBlack(goodsVO.availableDate) { "请设置有效期" }
                } else {
                    if (isMutilSpec) {
                        //useless
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
                    return@uploadImages
                }) {
                    uploadThumbnailImage(goodsVO, fail = {
                        view.showToast("商品缩略图片上传失败，请重试")
                        view.hideDialogLoading()
                        return@uploadThumbnailImage
                    }) {
                        //goodsVO.intro 不是必须
                        if (goodsVO.intro.isNotBlank() && (goodsVO.intro?.startsWith("<p>") != true)) {
                            uploadDesImages(goodsVO, fail = {
                                view.showToast("图片上传失败，请重试")
                                view.hideDialogLoading()
                            }) {
                                if (scan) {
                                    goodsVO.goodsId = null
                                }
                                if (goodsVO.goodsId.isNullOrBlank()) {
                                    submitTicket(goodsVO, scan, type, isFromCenter) // 添加券包
                                } else {
                                    //编辑券包
                                    modifyTicket(goodsVO, is_up = type.toString()) // 编辑券包
                                }
                            }
                        } else {
                            if (scan) {
                                goodsVO.goodsId = null
                            }
                            if (goodsVO.goodsId.isNullOrBlank()) {
                                submitTicket(goodsVO, scan, type, isFromCenter) // 添加券包
                            } else {
                                //编辑券包
                                modifyTicket(goodsVO, is_up = type.toString()) // 编辑券包
                            }
                        }
                    }
                }
            } catch (e: BizException) {
                view.showToast(e.message.check())
            }
        }
    }

    /**
     * 发布
     */
    //isMutilSpec是否多规格
    //scan是否扫码处跳转
    //type 保存  0  保存并上架  1
    override fun publish(
        goodsVO: GoodsVOWrapper,
        isVirtualGoods: Boolean,
        isMutilSpec: Boolean,
        scan: Boolean,
        type: Int,
        isFromCenter: Int
    ) {
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
                    return@uploadImages
                }) {
                    uploadThumbnailImage(goodsVO, fail = {
                        view.showToast("商品缩略图片上传失败，请重试")
                        view.hideDialogLoading()
                        return@uploadThumbnailImage
                    }) {
                        //goodsVO.intro 不是必须
                        if (goodsVO.intro.isNotBlank() && (goodsVO.intro?.startsWith("<p>") != true)) {
                            uploadDesImages(goodsVO, fail = {
                                view.showToast("图片上传失败，请重试")
                                view.hideDialogLoading()
                            }) {
                                if (scan) {
                                    goodsVO.goodsId = null
                                }
                                if (goodsVO.goodsId.isNullOrBlank()) {
                                    submitGoods(goodsVO, scan, type, isFromCenter) // 添加商品
                                } else {
                                    modifyGoods(goodsVO, is_up = type.toString()) // 编辑商品
                                }
                            }
                        } else {
                            if (scan) {
                                goodsVO.goodsId = null
                            }
                            if (goodsVO.goodsId.isNullOrBlank()) {
                                submitGoods(goodsVO, scan, type, isFromCenter) // 添加商品
                            } else {
                                modifyGoods(goodsVO, is_up = type.toString()) // 编辑商品
                            }
                        }
                    }
                }
            } catch (e: BizException) {
                view.showToast(e.message.check())
            }
        }
    }


    /**
     * 编辑商品时，获取商品关联的规格值
     */
    private fun loadSpecKeyList(
        goodsVO: GoodsVOWrapper,
        isMutilSpec: Boolean,
        callback: () -> Unit
    ) {
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

    override fun addGoodsSkuBarCodeLog(id: Int, bar_code: String, url: String) {
        mCoroutine.launch {

            val resp = GoodsRepository.addGoodsSkuBarCodeLog(id, bar_code, url)
            handleResponse(resp) {

            }
        }
    }

    private fun submitTicket(goodsVO: GoodsVOWrapper, scan: Boolean, type: Int, isFromCenter: Int) {
        mCoroutine.launch {
            if (type == 0) {
                //仅保存
                val resp =
                    GoodsRepository.submitTicket(goodsVO, type.toString())
                view.hideDialogLoading()
                handleResponse(resp) {
                    view.showToast("商品保存成功")
                    EventBus.getDefault().post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                    EventBus.getDefault().post(RefreshGoodsStatusEvent())
                    ActivityUtils.finishToActivity(GoodsListActivity::class.java, false)
                }

            } else {
                //保存上架
                val resp =
                    GoodsRepository.submitTicket(goodsVO, "0")
                if (resp.isSuccess) {
                    resp.data.goodsId?.let { shangjiaTicket(it) }
                }
            }
        }
    }

    private fun modifyTicket(goodsVO: GoodsVOWrapper, is_up: String) {
        mCoroutine.launch {
            if (is_up == "0") {
                //仅保存
                val resp = GoodsRepository.modifyTickets(goodsVO.goodsId!!, is_up, goodsVO)
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
            } else {
                //保存上架
                val resp = GoodsRepository.modifyTickets(goodsVO.goodsId!!, "0", goodsVO)
                if (resp.isSuccess) {
                    resp.data.goodsId?.let { shangjiaTicket(it, true) }
                } else {
                    handleErrorMsg(resp.msg)
                    // 编辑的商品，如果没有设置过规格，如果接口调用失败，就置空规格。
                    if (!goodsVO.isAddSpec) {
                        goodsVO.skuList = null
                    }
                }

            }
        }
    }

    //上架商品
    private fun shangjiaTicket(goodsID: String, type: Boolean = false) {
        mCoroutine.launch {
            val resp2 =
                GoodsRepository.makeGoodsEnable(goodsID)
            view.hideDialogLoading()
            if (resp2.isSuccess) {
                if (type) {
                    view.showToast("商品修改成功")
                } else {
                    view.showToast("商品上架成功")
                }

                EventBus.getDefault().post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                ActivityUtils.finishToActivity(GoodsListActivity::class.java, false)
            }
        }
    }

    private fun submitGoods(goodsVO: GoodsVOWrapper, scan: Boolean, type: Int, isFromCenter: Int) {
        mCoroutine.launch {
            val resp =
                GoodsRepository.submitGoods(goodsVO, type.toString(), isFromCenter)

            view.hideDialogLoading()
            handleResponse(resp) {

                if (scan) {
                    view.loadGoodsInfo(resp.data.goodsId)
                    if (type == 0) {
                        //仅保存
                        DialogUtils.showDialog(ActivityUtils.getTopActivity(),
                            "商品保存成功",
                            "是否继续扫码？",
                            "查看商品",
                            "继续",
                            {
                                ActivityUtils.startActivity(GoodsListActivity::class.java)
                                EventBus.getDefault()
                                    .post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_IS_AUTH))
                                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                                view.finish()
                            },
                            {
                                view.finish()
                                ActivityUtils.startActivity(GoodsScanActivity::class.java)
                            })
                    } else {
                        //保存上架
                        DialogUtils.showDialog(ActivityUtils.getTopActivity(),
                            "商品上架成功",
                            "是否继续扫码？",
                            "查看商品",
                            "继续",
                            {
                                ActivityUtils.startActivity(GoodsListActivity::class.java)
                                EventBus.getDefault()
                                    .post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                                view.finish()
                            },
                            {
                                view.finish()
                                ActivityUtils.startActivity(GoodsScanActivity::class.java)
                            })
                    }

                } else {
                    if (type == 0) {
                        //仅保存
                        view.showToast("商品保存成功")
                    } else {
                        //保存上架
                        view.showToast("商品上架成功")
                    }
                    EventBus.getDefault().post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                    EventBus.getDefault().post(RefreshGoodsStatusEvent())
                    ActivityUtils.finishToActivity(GoodsListActivity::class.java, false)
                }

            }
        }
    }

    private fun modifyGoods(goodsVO: GoodsVOWrapper, is_up: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.modifyGoods(goodsVO.goodsId!!, is_up, goodsVO)
            view.hideDialogLoading()
            if (resp.isSuccess) {
                view.showToast("商品修改成功")
                EventBus.getDefault().post(RefreshGoodsStatusEvent(type = 1))
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

    private fun uploadThumbnailImage(
        goodsVO: GoodsVOWrapper,
        fail: () -> Unit,
        success: () -> Unit
    ) {
        if (goodsVO.thumbnail.isNetUrl()) {
            success.invoke()
            return
        }
        mCoroutine.launch {
            val resp = CommonRepository.uploadFile(
                File(goodsVO.thumbnail),
                true,
                CommonRepository.SCENE_GOODS
            )
            if (resp.isSuccess) {
                goodsVO.thumbnail = resp.data.url
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
            if (requestList.size == 0) {
                success.invoke()
                return@launch
            }
            // 多个接口相互等待
            val respList = requestList.awaitAll()

            var isAllSuccess: Boolean = respList?.filter { !it.isSuccess }?.size == 0

            if (isAllSuccess) {
                val urls = respList.map { it -> it.data.url }.joinToString(separator = ",")

                goodsVO.intro = urls
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


    //useless
    override fun showExpirePop(str: String) {
        mExpirePreImpl.showPop(context, str) { item ->
            view.onUpdateExpire(item)
        }
    }

    //useless
    override fun showUseTimePop(str: String) {
        mUseTimePreImpl.showPop(context, str) { items ->
            view.onUpdateUseTime(items)
        }
    }
}
