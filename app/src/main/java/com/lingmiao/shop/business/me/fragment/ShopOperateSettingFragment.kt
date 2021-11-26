package com.lingmiao.shop.business.me.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.DeliveryManagerActivity
import com.lingmiao.shop.business.me.bean.BannerBean
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopOperateSettingPresenterImpl
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import kotlinx.android.synthetic.main.goods_adapter_goods_gallery.*
import kotlinx.android.synthetic.main.goods_include_publish_section2.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.galleryRv
import kotlinx.coroutines.*

/**
Create Date : 2021/3/24:12 PM
Auther      : Fox
Desc        : 运营设置
 **/
class ShopOperateSettingFragment : BaseFragment<ShopOperateSettingPresenter>(),
    ShopOperateSettingPresenter.View {

    private val bannerArray = listOf(
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211126150741.jpg",
            0
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142234.png",
            1
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142243.jpg",
            2
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142257.jpg",
            3
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142306.jpg",
            4
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142314.jpg",
            5
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142321.jpg",
            6
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142405.jpg",
            7
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142634.jpg",
            8
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142643.jpg",
            9
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142650.jpg",
            10
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142657.jpg",
            11
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142705.jpg",
            12
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142712.png",
            13
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116101006.jpg",
            14
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100955.jpg",
            15
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100947.jpg",
            16
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100940.jpg",
            17
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100933.jpg",
            18
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100925.jpg",
            19
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100915.jpg",
            20
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100905.jpg",
            21
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100853.jpg",
            22
        )
    )

    var shopReq: ApplyShopInfo = ApplyShopInfo()

    //商家配送模板
    var mLocalItem: FreightVoItem? = null

    //骑手配送模板
    var mRiderItem: FreightVoItem? = null

    companion object {

        const val TAG = "ShopOperateSetting"

        fun newInstance(item: ApplyShopInfo?): ShopOperateSettingFragment {
            return ShopOperateSettingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
        }

    }

    //获取店铺信息
    override fun initBundles() {
        shopReq = arguments?.getSerializable("item") as ApplyShopInfo
    }

    override fun getLayoutId() = R.layout.me_fragment_shop_operate_setting

    override fun createPresenter() = ShopOperateSettingPresenterImpl(requireContext(), this)

    override fun initViewsAndData(rootView: View) {

        // 营业时间
        tvShopOperateTime.setOnClickListener {
            mPresenter?.showWorkTimePop(it)
        }

        //配送设置，无用
        tvShopManageDelivery.setOnClickListener {
            ActivityUtils.startActivity(DeliveryManagerActivity::class.java)
        }

        //设置Banner图
        initBanner()

        //加载设置
        onLoadedShopSetting(shopReq)

        //配送设置
        mPresenter?.loadTemplate()

        //加载Banner图
        mPresenter?.getBanner()

        //图片库
        photoSelect.singleClick {
            val intent = Intent(requireActivity(), BannerActivity::class.java)
            intent.putExtra("number", galleryRv.getSelectPhotos().size)
            startActivityForResult(intent, 22)
        }

        //到店自提
        takeSelf.setOnClickListener {
            mPresenter?.takeSelf(if (takeSelf.isChecked) 1 else 0)
        }

        // 保存
        tvShopOperateSubmit.setOnClickListener {

            //未接订单自动取消时间
            if (tvShopManageNumber.getViewText().isEmpty()) {
                showToast("请输入未接订单自动取消时间")
                return@setOnClickListener
            }
            val cancelOrderTime = tvShopManageNumber.text?.toString()?.toInt() ?: 0

            //输入手机号码
            if (linkTelEt.text.toString().isEmpty()) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }
            //未完待续:增加对营业时间未设置时的判断
            //自动接单
            shopReq.autoAccept = if (autoOrderSb.isChecked) 1 else 0
            // 自动打印
            shopReq.autoPrint = if (autoPrinterSb.isChecked) 1 else 0
            // 取消订单
            shopReq.cancelOrderTime = cancelOrderTime
            // 联系电话
            shopReq.companyPhone = linkTelEt.text.toString()
            //配送设置
            shopReq.shopTemplateType = getTemplate()
            //提交设置
            mPresenter?.setSetting(shopReq, galleryRv.getSelectPhotos())
        }

    }

    //设置Banner图上传
    private fun initBanner() {
        galleryRv.setCountLimit(1, 5)
        galleryRv.setAspectRatio(750, 176)
    }

    fun getTemplate(): String {
        return if (cb_model_shop.isChecked) {
            FreightVoItem.TYPE_LOCAL
        } else if (cb_model_rider.isChecked) {
            FreightVoItem.TYPE_QISHOU
        } else {
            ""
        }
    }

    override fun onUpdateWorkTime(item1: WorkTimeVo?, item2: WorkTimeVo?) {
        shopReq.openStartTime = item1?.itemName
        // 处理【第二天】文字，服务端不需要返回
        shopReq.openEndTime = item2?.itemName?.replace("第二天", "")
        shopReq.openTimeType = item2?.getFullDayType()
        setOperateTime()
    }

    override fun onSetSetting() {
        UserManager.setAutoPrint(autoPrinterSb.isChecked)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            //获得的Banner
            val tempList = mutableListOf<GoodsGalleryVO>()
            //获得的List
            val list = data?.getStringExtra("list")?.split(",")

            try {
                list?.also {
                    if (it.isNotEmpty()) {
                        for (i in list) {
                            if (i.isNotEmpty()) {
                                tempList.add(GoodsGalleryVO(null, bannerArray[i.toInt()].url, null))
                            }
                        }
                    }
                }

                galleryRv.addDataList(tempList)
            } catch (e: Exception) {
                showToast("图片获取失败，请自行上传")
            }


        }
    }

    override fun onLoadedTemplate(tcItem: FreightVoItem?, qsItem: FreightVoItem?) {
        this.mLocalItem = tcItem
        this.mRiderItem = qsItem

        //根据返回情况确定模板是否已配置
        //商家
        tcItem?.apply {
            tvShopStatus.text = "已设置"
        }
        //骑手
        qsItem?.apply {
            tvRiderStatus.text = "已设置"
        }
        layoutShop.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.cb_model_shop) {
                if ((mLocalItem == null || mLocalItem?.id == null) && cb_model_shop.isChecked) {
                    showToast("请先设置商家配送模板")
                }
            } else if (checkedId == R.id.cb_model_rider) {
                if ((mRiderItem == null || mRiderItem?.id == null) && cb_model_rider.isChecked) {
                    showToast("请先设置骑手配送模板")
                }
            }
        }
        tvShopStatus.singleClick {
            //shopReq.accept_carriage决定是否显示骑手配送
            DeliveryManagerActivity.shop(mContext as Activity, mRiderItem, shopReq.accept_carriage)
        }
        tvRiderStatus.singleClick {
            //默认使用骑手配送
            DeliveryManagerActivity.rider(mContext as Activity, mRiderItem)
        }
    }

    override fun onSetBanner(bannerList: List<BannerBean>?) {
        val list = mutableListOf<GoodsGalleryVO>()
        bannerList?.forEachIndexed { _, bannerBean ->
            list.add(GoodsGalleryVO(original = bannerBean.banner_url, sort = null))
        }
        galleryRv.addDataList(list)
    }

    override fun setTakeSelfFailed() {
        takeSelf.isChecked= !takeSelf.isChecked
    }

    override fun onLoadedShopSetting(vo: ApplyShopInfo) {
        vo.apply {
            orderSetting?.apply {
                //自动接单
                autoOrderSb.isChecked = autoAccept == 1
                //是否自动打印
                autoPrinterSb.isChecked = autoPrint == 1
                //未接订单自动取消时间
                tvShopManageNumber.setText(cancelOrderDay?.toString())
            }
            //营业时间
            shopReq.openStartTime = vo.openStartTime
            shopReq.openEndTime = vo.openEndTime
            setOperateTime()

            //是否开启自提，1开启，0不开启
            takeSelf.isChecked = is_self_take == 1

            //联系电话
            linkTelEt.setText(companyPhone)

            //确定所选配送模板
            cb_model_rider.isChecked = shopTemplateType == FreightVoItem.TYPE_QISHOU
            cb_model_shop.isChecked = shopTemplateType == FreightVoItem.TYPE_LOCAL

            //后台确定是否隐藏骑手
            if (shopReq.accept_carriage == 0) {
                //隐藏棋手
                cb_model_rider.gone()
                tvRiderStatus.gone()
            }
        }

    }

    // 处理显示【第二天】文字，服务端不保存,客户端计算
    private fun setOperateTime() {
        tvShopOperateTime.text = String.format(
            "%s%s%s%s",
            shopReq.openStartTime ?: "",
            if (shopReq.openStartTime?.isEmpty() == true) "" else "-",
            if (WorkTimeVo.isSecondDay(
                    shopReq.openStartTime,
                    shopReq.openEndTime
                )
            ) "" else "第二天",
            shopReq.openEndTime ?: ""
        )
    }

}