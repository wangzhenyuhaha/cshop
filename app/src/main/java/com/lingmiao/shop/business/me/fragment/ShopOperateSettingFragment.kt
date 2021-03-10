package com.lingmiao.shop.business.me.fragment

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.me.DeliveryManagerActivity
import com.lingmiao.shop.business.me.LinkInSettingActivity
import com.lingmiao.shop.business.me.ManagerSettingActivity
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopOperateSettingPresenterImpl
import com.lingmiao.shop.business.tools.LogisticsToolActivity
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*

/**
Create Date : 2021/3/24:12 PM
Auther      : Fox
Desc        :
 **/
class ShopOperateSettingFragment : BaseFragment<ShopOperateSettingPresenter>(), ShopOperateSettingPresenter.View {


    companion object {
        fun newInstance(): ShopOperateSettingFragment {
            return ShopOperateSettingFragment()
        }
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
        // rlShopOperate


        // 未接订单自动取消时间
        // tvShopManageNumber

        rlShopManageNumber.setOnClickListener {

        }
        // 联系设置
        tvShopManageLink.setOnClickListener {
            ActivityUtils.startActivity(LinkInSettingActivity::class.java)
        }
        // 配送设置
        tvShopManageDelivery.setOnClickListener {
            ActivityUtils.startActivity(DeliveryManagerActivity::class.java)
//            ActivityUtils.startActivity(LogisticsToolActivity::class.java)
        }

        // 保存
        tvShopOperateSubmit.setOnClickListener {

        }
    }

    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        tvShopOperateTime.setText(String.format("%s-%s", item1?.itemName, item2?.itemName));
    }

}