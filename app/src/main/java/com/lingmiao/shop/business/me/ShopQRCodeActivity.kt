package com.lingmiao.shop.business.me

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseVBActivity
import com.james.common.databinding.IncludeTabBinding
import com.lingmiao.shop.base.IConstant.CACHE_PATH
import com.lingmiao.shop.business.me.fragment.QRImageFragment
import com.lingmiao.shop.databinding.ActivityViewpagerBinding


/**
Create Date : 2021/6/43:07 PM
Author      : Fox
Desc        :
 **/
class ShopQRCodeActivity : BaseVBActivity<ActivityViewpagerBinding, BasePreImpl>() {

    val path = CACHE_PATH + "share.jpg"
    private var shopId: Int? = null

    private var codeType: Int? = 0

    private val tabName = listOf("物料二维码", "贴纸二维码")

    override fun getViewBinding(): ActivityViewpagerBinding {
        return ActivityViewpagerBinding.inflate(layoutInflater)
    }

    override fun createPresenter(): BasePreImpl {
        return BasePreImpl(this)
    }

    override fun initBundles() {
        shopId = intent.extras?.getInt("SHOP_ID")
        codeType = intent.extras?.getInt("QRCODE_TYPE")
    }

    override fun initView() {
        when (codeType) {
            0 -> {
                mToolBarDelegate?.setMidTitle("扫码立牌")
            }
            1 -> {

            }
            2 -> {

            }
        }


        mBinding.activityViewpagerViewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 1
            override fun createFragment(position: Int) = QRImageFragment.newInstance(codeType ?: 0)
//                when (position) {
//                    0 -> QRImageFragment.newInstance(0)
//                    else -> QRImageFragment.newInstance(1)
//                }
        }

//        TabLayoutMediator(
//            IncludeTabBinding.bind(mBinding.root).viewpager2TabLayout,
//            mBinding.activityViewpagerViewpager2
//        ) { tab, position ->
//            tab.text = tabName[position]
//        }.attach()

    }

    override fun useLightMode() = false
}