package com.lingmiao.shop.business.me.fragment

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.addTextChangeListener
import com.lingmiao.shop.business.tools.bean.PeekTime
import com.lingmiao.shop.databinding.ActivityBannerBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter

class BannerActivity : BaseVBActivity<ActivityBannerBinding, BannerActivityPresenter>(),
    BannerActivityPresenter.View {

    private val bannerArray = listOf<BannerItem>(
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142218.jpg",
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
        )
    )

    private lateinit var adapter: BannerAdapter

    override fun createPresenter() = BannerActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityBannerBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("请选择Banner图")

        adapter = BannerAdapter()

        mBinding.bannerList.initAdapter(adapter)
         adapter.replaceData(bannerArray)
        //
        //        mRangeAdapter.replaceData(mRangeList)

        mBinding.tvSubmit.singleClick {
            finish()
        }
    }

    override fun useLightMode() = false
}

class BannerAdapter : BaseQuickAdapter<BannerItem, BaseViewHolder>(R.layout.tools_adapter_banner) {

    override fun convert(helper: BaseViewHolder, item: BannerItem?) {
        helper.addOnClickListener(R.id.bannerCheck)
        val imageView = helper.getView<ImageView>(R.id.banner_body)
        GlideUtils.setImageUrl(imageView, item?.url)

    }

}

data class BannerItem(val url: String, val id: Int)

interface BannerActivityPresenter : BasePresenter {

    interface View : BaseView {
    }
}

class BannerActivityPresenterImpl(val view: BannerActivityPresenter.View) : BasePreImpl(view),
    BannerActivityPresenter {

}