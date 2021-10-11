package com.lingmiao.shop.business.me

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.james.common.base.BaseVBActivity
import com.james.common.databinding.IncludeTabBinding
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.fragment.ShopBaseSettingFragment
import com.lingmiao.shop.business.me.fragment.ShopOperateSettingFragment
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ManagerSettingPresenterImpl
import com.lingmiao.shop.databinding.ActivityViewpagerBinding

/**
Create Date : 2021/3/24:05 PM
Author      : Fox
Desc        :
 **/
class ManagerSettingActivity : BaseVBActivity<ActivityViewpagerBinding, ManagerSettingPresenter>(),
    ManagerSettingPresenter.View {

    private val tabName = listOf("基础", "运营")

    override fun getViewBinding() = ActivityViewpagerBinding.inflate(layoutInflater)


    override fun initView() {
        mToolBarDelegate?.setMidTitle(getString(R.string.manager_setting_title))

        mPresenter?.loadShopInfo()
    }


    override fun useLightMode() = false


    override fun createPresenter() = ManagerSettingPresenterImpl(this)


    override fun onLoadedShopInfo(bean: ApplyShopInfo) {
        mBinding.activityViewpagerViewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int) =
                when (position) {
                    0 -> ShopBaseSettingFragment.newInstance(bean)
                    else -> ShopOperateSettingFragment.newInstance(bean)
                }
        }

        TabLayoutMediator(
            IncludeTabBinding.bind(mBinding.root).viewpager2TabLayout,
            mBinding.activityViewpagerViewpager2
        ) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

}