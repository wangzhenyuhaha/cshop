package com.lingmiao.shop.business.me.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.*
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.DeliveryManagerActivity
import com.lingmiao.shop.business.me.bean.BannerBean
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopOperateSettingPresenterImpl
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import kotlinx.android.synthetic.main.goods_adapter_goods_gallery.*
import kotlinx.android.synthetic.main.goods_include_publish_section2.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.galleryRv
import kotlinx.coroutines.*

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

    override fun initBundles() {
        shopReq = arguments?.getSerializable("item") as ApplyShopInfo
    }

    override fun getLayoutId() = R.layout.me_fragment_shop_operate_setting

    override fun createPresenter() = ShopOperateSettingPresenterImpl(requireContext(), this)


    override fun initViewsAndData(rootView: View) {

        //设置Banner图上传
        //设置最多上传五张图
        galleryRv.setCountLimit(1, 5)
        //设置上传banner图的长宽比
        galleryRv.setAspectRatio(750, 176)


        // 营业时间
        tvShopOperateTime.setOnClickListener {
            mPresenter?.showWorkTimePop(it)
        }

        // 配送设置  已隐藏
        tvShopManageDelivery.setOnClickListener {
            ActivityUtils.startActivity(DeliveryManagerActivity::class.java)
        }

        // 保存
        tvShopOperateSubmit.setOnClickListener {
            if (tvShopManageNumber.getViewText().isEmpty()) {
                showToast("请输入未接订单自动取消时间")
                return@setOnClickListener
            }

            val cancelOrderTime = tvShopManageNumber.text?.toString()?.toInt() ?: 0
            if (cancelOrderTime > 5) {
                showToast("未接订单自动取消时间不能大于5分钟")
                return@setOnClickListener
            }

            if (linkTelEt.text.toString().isEmpty()) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }

            //自动接单
            shopReq.autoAccept = if (autoOrderSb.isChecked) 1 else 0
            shopReq.cancelOrderTime = cancelOrderTime
            shopReq.companyPhone = linkTelEt.text.toString()
            //获取是商家配送或者骑手配送
            shopReq.shopTemplateType = getTemplate()
            //提交设置数据
            mPresenter?.setSetting(shopReq, galleryRv.getSelectPhotos())
        }

        onLoadedShopSetting(shopReq)
        mPresenter?.loadTemplate()

        if (IConstant.official) {
            cb_model_rider.gone()
            tvRiderStatus.gone()
        }
        mPresenter?.getBanner()
    }

    fun getTemplate(): String {
        return if (cb_model_shop.isChecked) {
            FreightVoItem.TYPE_LOCAL
        } else if (cb_model_rider.isChecked) {
            FreightVoItem.TYPE_QISHOU
        } else {
            ""
        }
    }




    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        shopReq.openStartTime = item1?.itemName
        // 处理【第二天】文字，服务端不需要返回
        shopReq.openEndTime = item2?.itemName?.replace("第二天", "")
        shopReq.openTimeType = item2?.getFullDayType()
        setOperateTime()
    }

    override fun onSetSetting() {

    }

    var mLocalItem: FreightVoItem? = null

    var mRiderItem: FreightVoItem? = null

    override fun onLoadedTemplate(tcItem: FreightVoItem?, qsItem: FreightVoItem?) {
        this.mLocalItem = tcItem
        this.mRiderItem = qsItem
        tcItem?.apply {
            tvShopStatus.text = "已设置"
        }
        qsItem?.apply {
            tvRiderStatus.text = "已设置"
        }
        layoutShop.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.cb_model_shop) {
                if ((mLocalItem == null || mLocalItem?.id == null) && cb_model_shop.isChecked) {
                    showToast("请先设置商家配送模板")
                }
            } else if (checkedId == R.id.cb_model_rider) {
                if ((mRiderItem == null || mRiderItem?.id == null) && cb_model_rider.isChecked) {
                    showToast("请先设置骑手配送模板")
                }
            }
        }
        tvShopStatus.singleClick {
            //shopReq.accept_carriage决定是否显示骑手配送
            DeliveryManagerActivity.shop(mContext as Activity, mRiderItem, shopReq.accept_carriage)
        }
        tvRiderStatus.singleClick {
            //默认使用骑手配送
            DeliveryManagerActivity.rider(mContext as Activity, mRiderItem)
        }
    }

    override fun onSetBanner(bannerList: List<BannerBean>?) {
        val list = mutableListOf<GoodsGalleryVO>()
        bannerList?.forEachIndexed { index, bannerBean ->
            list.add(GoodsGalleryVO(original = bannerBean.banner_url, sort = null))
        }
        galleryRv.addDataList(list)
    }

    override fun onLoadedShopSetting(vo: ApplyShopInfo) {
        vo.apply {
            orderSetting?.apply {
                autoOrderSb.isChecked = autoAccept == 1
                tvShopManageNumber.setText(cancelOrderDay?.toString())
            }
            shopReq.openStartTime = vo.openStartTime
            shopReq.openEndTime = vo.openEndTime
            setOperateTime()
            linkTelEt.setText(companyPhone)

            cb_model_rider.isChecked = shopTemplateType == FreightVoItem.TYPE_QISHOU
            cb_model_shop.isChecked = shopTemplateType == FreightVoItem.TYPE_LOCAL

            if (shopReq.accept_carriage == 0) {
                //隐藏棋手
                cb_model_rider.gone()
                tvRiderStatus.gone()
            }
        }

    }

    // 处理显示【第二天】文字，服务端不保存,客户端计算
    fun setOperateTime() {
        tvShopOperateTime.setText(
            String.format(
                "%s%s%s%s",
                shopReq.openStartTime ?: "",
                if (shopReq.openStartTime?.isEmpty() == true) "" else "-",
                if (WorkTimeVo.isSecondDay(
                        shopReq.openStartTime,
                        shopReq.openEndTime
                    )
                ) "" else "第二天",
                shopReq.openEndTime ?: ""
            )
        )
    }

}