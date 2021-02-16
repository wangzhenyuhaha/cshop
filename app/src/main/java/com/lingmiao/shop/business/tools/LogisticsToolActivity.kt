package com.lingmiao.shop.business.tools

import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.tools.fragment.ExpressCompanyFragment
import com.lingmiao.shop.business.tools.fragment.FreightModelFragment
import com.lingmiao.shop.business.tools.presenter.LogisticsToolPresenter
import com.lingmiao.shop.business.tools.presenter.impl.LogisticsToolPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.goods_fragment_goods_home.viewPager
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*


class LogisticsToolActivity : BaseActivity<LogisticsToolPresenter>(), LogisticsToolPresenter.View{

    private var mTabTitles = arrayOf("运费模板", "物流公司")

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_logistics_tool;
    }

    override fun createPresenter(): LogisticsToolPresenter {
        return LogisticsToolPresenterImpl(this);
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        val drawable = getDrawable(R.mipmap.goods_plus_blue)
        drawable?.setBounds(0, 0, 30, 30);

        toolbarView?.apply {
            setTitleContent(getString(R.string.tools_logistics_tool_title))
            setRightListener(drawable, getString(R.string.tools_logistics_model_add), R.color.color_3870EA) {
                ActivityUtils.startActivity(EditFreightModelActivity::class.java)
            }
        }
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(FreightModelFragment.newInstance())
        fragments.add(ExpressCompanyFragment.newInstance())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

}