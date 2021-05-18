package com.lingmiao.shop.business.me.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.DeliveryManagerActivity
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopOperateSettingPresenterImpl
import com.lingmiao.shop.business.tools.adapter.addTextChangeListener
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*

/**
Create Date : 2021/3/24:12 PM
Auther      : Fox
Desc        :
 **/
class ShopOperateSettingFragment : BaseFragment<ShopOperateSettingPresenter>(), ShopOperateSettingPresenter.View {

    var shopReq: ApplyShopInfo = ApplyShopInfo()

    companion object {
        fun newInstance(item : ApplyShopInfo?): ShopOperateSettingFragment {
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
        return ShopOperateSettingPresenterImpl(context!!, this)
    }

    override fun initViewsAndData(rootView: View) {

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
            val cancelOrderTime =  tvShopManageNumber.text?.toString()?.toInt()?:0;
            if(cancelOrderTime > 15) {
                showToast("不能大于15分钟");
                return@setOnClickListener;
            }
            shopReq.autoAccept = autoOrderSb.isChecked;
            shopReq.cancelOrderTime = cancelOrderTime;
            shopReq.companyPhone = linkTelEt.text.toString();

            mPresenter?.setSetting(shopReq);
        }

        if(shopReq != null) {
            onLoadedShopSetting(shopReq!!);
        } else {
            showPageLoading()
            mPresenter?.loadShopSetting()
        }
    }


    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        shopReq.openStartTime = item1?.itemName;
        shopReq.openEndTime = item2?.itemName;
        shopReq.openTimeType = item2?.getFullDayType();
        tvShopOperateTime.setText(String.format("%s-%s", item1?.itemName, item2?.itemName));
    }

    override fun onSetSetting() {

    }

    override fun onLoadedShopSetting(vo : ApplyShopInfo) {
        autoOrderSb.isChecked = vo?.autoAccept ?: false;
        tvShopManageNumber.setText(vo?.cancelOrderTime?.toString())
        tvShopOperateTime.setText(String.format("%s-%s", vo?.openStartTime, vo?.openEndTime));
        linkTelEt.setText(vo.companyPhone);
    }

}