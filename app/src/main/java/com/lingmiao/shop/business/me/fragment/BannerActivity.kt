package com.lingmiao.shop.business.me.fragment

import android.app.Activity
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.databinding.ActivityBannerBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter

class BannerActivity : BaseVBActivity<ActivityBannerBinding, BannerActivityPresenter>(),
    BannerActivityPresenter.View {

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

    //已有的图片数量
    private var number: Int = 0

    //自定义上传的Banner数量
    private var temp: Int = 0

    private lateinit var adapter: BannerAdapter

    override fun createPresenter() = BannerActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityBannerBinding.inflate(layoutInflater)

    override fun initBundles() {
        super.initBundles()
        //获取已有的Banner图数量
        number = intent?.getIntExtra("number", 0) ?: 0
        temp = number

    }

    override fun initView() {

        mToolBarDelegate?.setMidTitle("请选择Banner图")

        adapter = BannerAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.bannerCheck) {
                    //先判断有没有超出5个
                    if (number < 5) {
                        //没有超出5个
                        if (bannerArray[position].select) {
                            bannerArray[position].select = false
                            number--
                        } else {
                            bannerArray[position].select = true
                            number++
                        }
                    } else {
                        //超出五个
                        if (bannerArray[position].select) {
                            //点击选中的banner
                            bannerArray[position].select = false
                            number--
                            view.findViewById<CheckBox>(R.id.bannerCheck).isChecked = false
                        } else {
                            //点击未选中的banner
                            showToast("您最多可选张${5 - temp}Banner图")
                            view.findViewById<CheckBox>(R.id.bannerCheck).isChecked = false

                        }
                    }
                }
            }
        }

        mBinding.bannerList.initAdapter(adapter)
        adapter.replaceData(bannerArray)


        mBinding.tvSubmit.singleClick {

            val list: StringBuilder = StringBuilder()
            for (i in bannerArray) {
                if (i.select) {
                    list.append(i.id)
                    list.append(",")
                }
            }
            intent.putExtra("list", list.toString())
            setResult(Activity.RESULT_OK, intent)
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

data class BannerItem(val url: String, val id: Int, var select: Boolean = false)

interface BannerActivityPresenter : BasePresenter {

    interface View : BaseView {
    }
}

class BannerActivityPresenterImpl(val view: BannerActivityPresenter.View) : BasePreImpl(view),
    BannerActivityPresenter {

}