package com.lingmiao.shop.business.me.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
        shopReq = arguments?.getSerializable("item") as ApplyShopInfo;
    }

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_shop_operate_setting
    }

    override fun createPresenter(): ShopOperateSettingPresenter? {
        return ShopOperateSettingPresenterImpl(requireContext(), this)
    }

    override fun initViewsAndData(rootView: View) {
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
//        tvShopManageLink.setOnClickListener {
//            ActivityUtils.startActivity(LinkInSettingActivity::class.java)
//        }

        // 配送设置
        tvShopManageDelivery.setOnClickListener {
            ActivityUtils.startActivity(DeliveryManagerActivity::class.java)
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
            if(galleryRv.getSelectPhotos().isEmpty()) {
                showToast("请上传店铺广告图");
                return@setOnClickListener;
            }
            shopReq.autoAccept = if (autoOrderSb.isChecked) 1 else 0;
            shopReq.cancelOrderTime = cancelOrderTime;
            shopReq.companyPhone = linkTelEt.text.toString();
            shopReq.shopTemplateType = getTemplate();
            mPresenter?.setSetting(shopReq, galleryRv.getSelectPhotos());
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
            tvRiderStatus.gone();
        }
        mPresenter?.getBanner();
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
        galleryRv.setAspectRatio(750, 176)
    }

    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        shopReq.openStartTime = item1?.itemName;
        // 处理【第二天】文字，服务端不需要返回
        shopReq.openEndTime = item2?.itemName?.replace("第二天", "");
        shopReq.openTimeType = item2?.getFullDayType();
        setOperateTime();
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

    override fun onSetBanner(bannerList: List<BannerBean>?) {
        val list = mutableListOf<GoodsGalleryVO>()
        bannerList?.forEachIndexed { index, bannerBean ->
            list.add(GoodsGalleryVO(original = bannerBean.banner_url, sort = null))
        }
        galleryRv.addDataList(list)
    }

    override fun onLoadedShopSetting(vo: ApplyShopInfo) {
        vo?.apply {
            orderSetting?.apply {
                autoOrderSb.isChecked = autoAccept == 1;
                tvShopManageNumber.setText(cancelOrderDay?.toString())
            }
            shopReq.openStartTime = vo.openStartTime;
            shopReq.openEndTime = vo.openEndTime;
            setOperateTime();
            linkTelEt.setText(companyPhone);

            cb_model_rider.isChecked = shopTemplateType == FreightVoItem.TYPE_QISHOU
            cb_model_shop.isChecked = shopTemplateType == FreightVoItem.TYPE_LOCAL
        }

    }

    // 处理显示【第二天】文字，服务端不保存,客户端计算
    fun setOperateTime() {
        tvShopOperateTime.setText(String.format("%s%s%s%s", shopReq.openStartTime ?:"", if(shopReq.openStartTime?.isEmpty() == true) "" else "-" , if(WorkTimeVo.isSecondDay(shopReq.openStartTime, shopReq.openEndTime)) "" else "第二天", shopReq.openEndTime ?:""));
    }

}