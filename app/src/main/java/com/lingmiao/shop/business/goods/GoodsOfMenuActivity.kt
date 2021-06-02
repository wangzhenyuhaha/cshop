package com.lingmiao.shop.business

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.fragment.GoodsListOfMenuFragment
import com.lingmiao.shop.business.goods.fragment.GoodsListToMenuFragment
import com.lingmiao.shop.business.goods.presenter.GoodsOfMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsOfMenuPreImpl
import kotlinx.android.synthetic.main.sales_activity_stats.*

/**
Create Date : 2021/5/312:22 PM
Auther      : Fox
Desc        :
 **/
class GoodsOfMenuActivity : BaseActivity<GoodsOfMenuPre>(), GoodsOfMenuPre.View {

    companion object {

        const val KEY_PARAM_ITEM = "KEY_PARENT_ITEM"
        const val KEY_PARAM_ID = "KEY_PARENT_ID"

        fun openActivity(context: Context, item : ShopGroupVO) {
            val intent = Intent(context, GoodsOfMenuActivity::class.java);
            intent.putExtra(KEY_PARAM_ITEM, item);
            intent.putExtra(KEY_PARAM_ID, item?.shopCatId);
            context.startActivity(intent)
        }
    }

    /**
     * 分组的父分组ID
     */
    private var groupId: String? = ""
    private var item : ShopGroupVO? = null

    override fun initBundles() {
        groupId = intent.getStringExtra(KEY_PARAM_ID)
        item = intent.getSerializableExtra(KEY_PARAM_ITEM) as ShopGroupVO?;
    }

    override fun createPresenter(): GoodsOfMenuPre {
        return GoodsOfMenuPreImpl(this, this);
    }

    override fun useLightMode(): Boolean {
        return false
    }

    private var mTabTitles = arrayOf("已添加", "新增")

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_stats;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("菜单商品管理")
//        mToolBarDelegate.setRightText("添加", View.OnClickListener{
//
//        })

        val fragments = mutableListOf<Fragment>()
        fragments.add(GoodsListOfMenuFragment.newInstance(item!!))
        fragments.add(GoodsListToMenuFragment.newInstance(item!!))

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)

    }

}