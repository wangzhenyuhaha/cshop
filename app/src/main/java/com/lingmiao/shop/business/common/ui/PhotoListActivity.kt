package com.lingmiao.shop.business.common.ui

import androidx.viewpager2.widget.ViewPager2
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.databinding.ActivityPhotoListBinding
import java.util.*

class PhotoListActivity : BaseVBActivity<ActivityPhotoListBinding, BasePresenter>() {

    private lateinit var temp: ArrayList<String>
    private var position: Int = 0
    private lateinit var adapter: PhotoListAdapter

    override fun initBundles() {
        val bundle = intent.getBundleExtra("Photo")

        temp = bundle?.getStringArrayList("list") ?: ArrayList()
        position = bundle?.getInt("position") ?: 0


    }

    override fun getViewBinding() = ActivityPhotoListBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    override fun initView() {

        mToolBarDelegate?.setMidTitle("${position + 1} / ${temp.size}")

        adapter = PhotoListAdapter()
        mBinding.viewPager2.adapter = adapter
        adapter.submitList(temp)



        mBinding.viewPager2.apply {
            setCurrentItem(position, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mToolBarDelegate?.setMidTitle("${position + 1} / ${temp.size}")
                }
            })
        }

    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl()
    }


}