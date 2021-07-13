package com.lingmiao.shop.business.me

import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.james.common.base.BaseVBActivity
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.fragment.ShopBaseSettingFragment
import com.lingmiao.shop.business.me.fragment.ShopOperateSettingFragment
import com.lingmiao.shop.databinding.ActivityViewpagerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:05 PM
Author      : Fox
Desc        :
 **/
class ManagerSettingActivity : BaseVBActivity<ActivityViewpagerBinding>() {

    val tabName = listOf("基础", "运营")

    private lateinit var tab: TabLayout

    override fun getViewBinding() = ActivityViewpagerBinding.inflate(layoutInflater)

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.manager_setting_title))
        tab = findViewById(R.id.viewpager2_tabLayout)

        //请求数据
        loadShopInfo()
    }


    override fun useLightMode(): Boolean {
        return false
    }

    //获取数据
    private fun loadShopInfo() {
        //避免重复请求
        searchJob?.cancel()
        searchJob = lifecycleScope.launch(Dispatchers.Main)
        {
            //waiting
            showPageLoading()

            val resp = MeRepository.apiService.getShop().awaitHiResponse()

            handleResponse(resp) {
                initViewPager2(resp.data)
                hidePageLoading()
            }

        }
    }

    //初始化ViewPager2
    private fun initViewPager2(data: ApplyShopInfo) {

        binding.activityViewpagerViewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int) =
                when (position) {
                    0 -> ShopBaseSettingFragment.newInstance(data)
                    else -> ShopOperateSettingFragment.newInstance(data)
                }
        }

        TabLayoutMediator(
            tab,
            binding.activityViewpagerViewpager2
        ) { tab, position ->
            tab.text = tabName[position]
        }.attach()

    }

}