package com.lingmiao.shop.business.sales

import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsSearchActivity
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import com.lingmiao.shop.business.goods.fragment.GoodsStatusNewFragment

class SalesActivityGoodsWarning : BaseActivity<BasePresenter>() {


    private lateinit var goodsSearchLayout: LinearLayout

    private lateinit var viewPager2: ViewPager2


    override fun onBackPressed() {
        if (viewPager2.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager2.currentItem = viewPager2.currentItem - 1
        }
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_warning
    }

    override fun initView() {
        goodsSearchLayout = findViewById(R.id.goods_activity_goods_warning_goodsSearchLayout)
        viewPager2 = findViewById(R.id.goods_activity_goods_warning_viewPager2)
        initTitle()
        initViewPager2()
        goodsSearchLayout.singleClick {
            GoodsSearchActivity.openActivity(context!!)
        }
    }


    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_warning_title))
    }


    private fun initViewPager2() {
        val adapter = GoodsWarningPagerAdapter(this)
        viewPager2.adapter = adapter
    }

    private inner class GoodsWarningPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 1
        override fun createFragment(position: Int) =
            // 该Fragment加载预警商品
            GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUE_WARNING)
    }

    override fun useLightMode(): Boolean {
        return false
    }

}