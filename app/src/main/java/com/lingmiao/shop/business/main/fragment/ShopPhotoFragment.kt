package com.lingmiao.shop.business.main.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.databinding.FragmentShopPhotoBinding
import com.lingmiao.shop.util.GlideUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopPhotoFragment : BaseVBFragment<FragmentShopPhotoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopPhotoBinding.inflate(inflater, container, false)


    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {

        model.setTitle("上传店铺照片")
        model.applyShopInfo.value?.shopPhotoFront?.also {
            GlideUtils.setImageUrl(binding.shopFront, it)
        }
        model.applyShopInfo.value?.shopPhotoInside?.also {
            GlideUtils.setImageUrl(binding.shopInside, it)
        }


        initListener()

    }

    private fun initListener() {


        binding.shopFront.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.SHOP_FRONT,
                "店铺门头照片",
                model.applyShopInfo.value?.shopPhotoFront
            )
        }


        binding.shopInside.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.SHOP_INSIDE,
                "店铺内景照片",
                model.applyShopInfo.value?.shopPhotoInside
            )
        }

        binding.backTextView.setOnClickListener {
            findNavController().navigateUp()
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            ApplyShopInfoActivity.SHOP_FRONT -> {
                model.applyShopInfo.value?.shopPhotoFront = event.remoteUrl
                GlideUtils.setImageUrl(binding.shopFront, event.remoteUrl)
            }
            ApplyShopInfoActivity.SHOP_INSIDE -> {
                model.applyShopInfo.value?.shopPhotoInside = event.remoteUrl
                GlideUtils.setImageUrl(binding.shopInside, event.remoteUrl)
            }

        }
    }


}