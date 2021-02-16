package com.lingmiao.shop.business.goods.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品管理
 */
class GoodsHomePageAdapter(
    val fm: FragmentManager,
    val fragments: MutableList<Fragment>,
    val tabTitles: Array<String>
): FragmentPagerAdapter(fm) {

    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
        val fragment = super.instantiateItem(container, position) as Fragment
        fm.beginTransaction().show(fragment).commitAllowingStateLoss()
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment: Fragment = fragments.get(position)
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}