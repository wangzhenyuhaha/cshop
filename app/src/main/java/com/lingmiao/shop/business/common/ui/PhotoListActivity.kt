package com.lingmiao.shop.business.common.ui

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.databinding.ActivityPhotoListBinding
import java.io.File
import java.util.ArrayList

class PhotoListActivity : BaseVBActivity<ActivityPhotoListBinding>() {

    private lateinit var temp: ArrayList<String>
    private var position: Int = 0
    private lateinit var adapter: PhotoListAdapter

    override fun initBundles() {
        val bundle = intent.getBundleExtra("Photo")

        temp = bundle.getStringArrayList("list") ?: ArrayList()
        position = bundle.getInt("position")


    }

    override fun getViewBinding() = ActivityPhotoListBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    override fun initView() {


        mToolBarDelegate.setMidTitle("${position + 1} / ${temp.size}")

        adapter = PhotoListAdapter()
        binding.viewPager2.adapter = adapter
        adapter.submitList(temp)



        binding.viewPager2.apply {
            setCurrentItem(position, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mToolBarDelegate.setMidTitle("${position + 1} / ${temp.size}")
                }
            })
        }

    }


}