package com.lingmiao.shop.business.main.fragment

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
import com.lingmiao.shop.databinding.FragmentShopIdCardBinding
import com.lingmiao.shop.util.GlideUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopIDCardFragment : BaseVBFragment<FragmentShopIdCardBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopIdCardBinding.inflate(inflater, container, false)


    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {

        model.setTitle("上传身份证照片")
        model.applyShopInfo.value?.legalImg?.also {
            GlideUtils.setImageUrl(binding.legalImgImageView, it)
        }
        model.applyShopInfo.value?.legalBackImg?.also {
            GlideUtils.setImageUrl(binding.legalBackImgImageView, it)
        }
        model.applyShopInfo.value?.holdImg?.also {
            GlideUtils.setImageUrl(binding.holdImgImageView, it)
        }

        initListener()


    }


    private fun initListener() {


        binding.legalImgImageView.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.ID_CARD_FRONT,
                "上传国徽面照片",
                model.applyShopInfo.value?.legalImg
            )
        }


        binding.legalBackImgImageView.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.ID_CARD_BACK,
                "上传人像面照片",
                model.applyShopInfo.value?.legalBackImg
            )
        }

        binding.holdImgImageView.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.ID_CARD_HAND,
                "上传手持证件照",
                model.applyShopInfo.value?.holdImg
            )
        }
        binding.backTextView.setOnClickListener {
            findNavController().navigateUp()
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            ApplyShopInfoActivity.ID_CARD_FRONT -> {
                model.applyShopInfo.value?.legalImg = event.remoteUrl
                GlideUtils.setImageUrl(binding.legalImgImageView, event.remoteUrl)
            }
            ApplyShopInfoActivity.ID_CARD_BACK -> {
                model.applyShopInfo.value?.legalBackImg = event.remoteUrl
                GlideUtils.setImageUrl(binding.legalBackImgImageView, event.remoteUrl)
            }
            ApplyShopInfoActivity.ID_CARD_HAND -> {
                model.applyShopInfo.value?.holdImg = event.remoteUrl
                GlideUtils.setImageUrl(binding.holdImgImageView, event.remoteUrl)
            }
        }
    }


}