package com.lingmiao.shop.business.me.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.isNetUrl
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.DeliveryManagerActivity
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopOperateSettingPresenterImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.goods_adapter_goods_gallery.*
import kotlinx.android.synthetic.main.goods_include_publish_section2.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.galleryRv
import kotlinx.coroutines.*
import java.io.File

/**
Create Date : 2021/3/24:12 PM
Auther      : Fox
Desc        : 运营设置
 **/
class ShopOperateSettingFragment : BaseFragment<ShopOperateSettingPresenter>(),
    ShopOperateSettingPresenter.View {


    var shopReq: ApplyShopInfo = ApplyShopInfo()

    companion object {


        const val TAG = "ShopOperateSetting"

        fun newInstance(item: ApplyShopInfo?): ShopOperateSettingFragment {
            return ShopOperateSettingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
        }
    }

    //获取Banner图
    private fun getBanner() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO)
        {
            val resp: List<BannerItem> = MeRepository.apiService.getBanner().awaitHiResponse().data

            val list = mutableListOf<GoodsGalleryVO>()
            for (it in resp) {
                list.add(GoodsGalleryVO(original = it.banner_url, sort = null))
            }
            withContext(Dispatchers.Main)
            {
                galleryRv.addDataList(list)
            }
        }
    }

    override fun initBundles() {
        shopReq = arguments?.getSerializable("item") as ApplyShopInfo;
    }

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_shop_operate_setting
    }

    override fun createPresenter(): ShopOperateSettingPresenter? {
        return ShopOperateSettingPresenterImpl(requireContext(), this)
    }

    override fun initViewsAndData(rootView: View) {

        getBanner()

        initSection2View()

        // 营业时间
        tvShopOperateTime.setOnClickListener {
            mPresenter?.showWorkTimePop(it)
        }

        // 未接订单自动取消时间
//        addTextChangeListener(tvShopManageNumber, "") {
//            if(it?.toInt() > 15) {
//                showToast("不能大于15分钟");
//                return@addTextChangeListener;
//            }
//        }

        rlShopManageNumber.setOnClickListener {

        }
        // 联系设置
        linkTelEt
//        tvShopManageLink.setOnClickListener {
//            ActivityUtils.startActivity(LinkInSettingActivity::class.java)
//        }

        // 配送设置
        tvShopManageDelivery.setOnClickListener {
            ActivityUtils.startActivity(DeliveryManagerActivity::class.java)
//            ActivityUtils.startActivity(LogisticsToolActivity::class.java)
        }


        // 保存
        tvShopOperateSubmit.setOnClickListener {
            if (tvShopManageNumber.getViewText()?.isEmpty()) {
                showToast("请输入未接订单自动取消时间");
                return@setOnClickListener;
            }
            val cancelOrderTime = tvShopManageNumber.text?.toString()?.toInt() ?: 0;
            if (cancelOrderTime > 5) {
                showToast("未接订单自动取消时间不能大于5分钟");
                return@setOnClickListener;
            }
            shopReq.autoAccept = if (autoOrderSb.isChecked) 1 else 0;
            shopReq.cancelOrderTime = cancelOrderTime;
            shopReq.companyPhone = linkTelEt.text.toString();
            shopReq.shopTemplateType = getTemplate();
            mPresenter?.setSetting(shopReq);


            val goodsGalleryList = galleryRv.getSelectPhotos()

            upPicture(goodsGalleryList)

        }

        if (shopReq != null) {
            onLoadedShopSetting(shopReq!!);
            mPresenter?.loadTemplate();
        } else {
            mPresenter?.loadShopSetting()
            mPresenter?.loadTemplate();
        }

        if (IConstant.official) {
            cb_model_rider.gone();
        }
    }

    //图片上传
    private fun upPicture(data: List<GoodsGalleryVO>) {


        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            val url = mutableListOf<String>()

            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            data.forEach {
                val request = async {
                    if (it.original.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it.original))
                    } else {
                        CommonRepository.uploadFile(
                            File(it.original!!),
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
                    data?.get(index)?.apply {
                        original = it?.data?.url
                        sort = "${index}"
                        url.add(original!!)
                    }
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {

            }
            if (url.isNotEmpty()) {
                val data = mutableListOf<BannerItem>()
                for (i in url) {
                    data.add(BannerItem(i))
                }
                val request = MeRepository.apiService.updateBanner(data).awaitHiResponse()
                Log.d(TAG, request.toString())
            } else {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO)
                {
                    val data = listOf<BannerItem>()
                    val request = MeRepository.apiService.updateBanner(data).awaitHiResponse()
                    Log.d(TAG, request.toString())
                }
            }
        }


    }


    fun getTemplate(): String {
        if (cb_model_shop.isChecked) {
            return FreightVoItem.TYPE_LOCAL
        } else if (cb_model_rider.isChecked) {
            return FreightVoItem.TYPE_QISHOU;
        } else {
            return "";
        }
    }


    private fun initSection2View() {
        galleryRv.setCountLimit(1, 5)
        galleryRv.setAspectRatio(750, 136)
        //  deleteIv.gone();
//        imageView.singleClick {
//            openGallery();
//        }
    }


    private fun openGallery() {
        val menus = MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
        MediaMenuPop(mContext, menus).apply {
            setOnClickListener { type ->
                when (type) {
                    MediaMenuPop.TYPE_SELECT_PHOTO -> {
                        PhotoHelper.openAlbum(context as Activity, 1, null, true, 32) {
//                            addDataList(convert2GalleryVO(it))
                            val item = convert2GalleryVO(it)[0];
                            GlideUtils.setImageUrl1(imageView, item?.original)
                        }
                    }
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        PhotoHelper.openCamera(context as Activity, null, true, 32) {
                            val item = convert2GalleryVO(it)[0];
                            GlideUtils.setImageUrl1(imageView, item?.original)
                        }
                    }
                }
            }
        }.showPopupWindow()
    }


    private fun convert2GalleryVO(list: List<LocalMedia>): List<GoodsGalleryVO> {
        val galleryList = mutableListOf<GoodsGalleryVO>()
        list.forEach {
            galleryList.add(GoodsGalleryVO.convert(it))
        }
        return galleryList
    }


    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        shopReq.openStartTime = item1?.itemName;
        shopReq.openEndTime = item2?.itemName;
        shopReq.openTimeType = item2?.getFullDayType();
        tvShopOperateTime.setText(String.format("%s-%s", item1?.itemName, item2?.itemName));
    }

    override fun onSetSetting() {

    }

    var mLocalItem: FreightVoItem? = null;

    var mRiderItem: FreightVoItem? = null;

    override fun onLoadedTemplate(tcItem: FreightVoItem?, qsItem: FreightVoItem?) {
        this.mLocalItem = tcItem;
        this.mRiderItem = qsItem;
        tcItem?.apply {
            tvShopStatus.text = "已设置"
        }
        qsItem?.apply {
            tvRiderStatus.text = "已设置"
        }
        layoutShop.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.cb_model_shop) {
                if ((mLocalItem == null || mLocalItem?.id == null) && cb_model_shop.isChecked) {
                    showToast("请先设置商家配送模板");
                }
            } else if (checkedId == R.id.cb_model_rider) {
                if ((mRiderItem == null || mRiderItem?.id == null) && cb_model_rider.isChecked) {
                    showToast("请先设置骑手配送模板");
                }
            }
        }
        tvShopStatus.singleClick {
            DeliveryManagerActivity.shop(mContext as Activity, mRiderItem);
        }
        tvRiderStatus.singleClick {
            DeliveryManagerActivity.rider(mContext as Activity, mRiderItem);
        }
    }

    override fun onLoadedShopSetting(vo: ApplyShopInfo) {
        vo?.apply {
            orderSetting?.apply {
                autoOrderSb.isChecked = autoAccept == 1;
                tvShopManageNumber.setText(cancelOrderDay?.toString())
            }
            tvShopOperateTime.setText(String.format("%s-%s", openStartTime, openEndTime));
            linkTelEt.setText(companyPhone);

            cb_model_rider.isChecked = shopTemplateType == FreightVoItem.TYPE_QISHOU
            cb_model_shop.isChecked = shopTemplateType == FreightVoItem.TYPE_LOCAL
        }

    }


    data class BannerItem(
        var banner_url: String? = null,
        var create_time: String? = null,
        var creater_id: String? = null,
        var id: String? = null,
        var is_delete: Int? = null,
        var link_url: String? = null,
        var merchant_id: String? = null,
        var org_id: String? = null,
        var remarks: String? = null,
        var shop_id: Int? = null,
        var update_time: String? = null,
        var updater_id: String? = null
    )


}