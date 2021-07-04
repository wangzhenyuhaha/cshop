package com.lingmiao.shop.business.main

import android.content.Intent
import android.view.View
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.checkNotBlack
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import kotlinx.android.synthetic.main.main_activity_apply_shop_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *   首页-提交资料
 */
class ApplyShopInfoActivity : BaseActivity<ApplyShopInfoPresenter>(), ApplyShopInfoPresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var selectedCategoryList: List<ApplyShopCategory>? = null

    private var applyShopInfo = ApplyShopInfo()
    private var adInfo: AddressData? = null

    companion object {
        private const val LICENSE = 1
        private const val IDCARD_FRONT = 2
        private const val IDCARD_BACK = 3
        private const val IDCARD_HAND = 4
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_info
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")


        rlShopInfoSelf.setOnClickListener(this)
        rlShopInfoCompany.setOnClickListener(this)
        rlShopInfoCategory.setOnClickListener(this)
        rlShopInfoAddress.setOnClickListener(this)
        rlShopInfoLicense.setOnClickListener(this)
        rlShopInfoIdCardFront.setOnClickListener(this)
        rlShopInfoIdCardBack.setOnClickListener(this)
        rlShopInfoIdCardHand.setOnClickListener(this)
        tvApplyShopInfoNext.setOnClickListener(this)

        rlShopInfoSelf.isSelected = true
        applyShopInfo.shopType = "0"
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let {
            if (it.shopStatus != null && it.shopStatus != "UN_APPLY") mPresenter.requestShopInfoData()
        }
    }


    override fun createPresenter(): ApplyShopInfoPresenter {
        return ApplyShopInfoPresenterImpl(this, this)
    }

    override fun onShopInfoSuccess(bean: ApplyShopInfo) {
        if (bean.shopName != null) {
            applyShopInfo = bean
            applyShopInfo.shopType = "0"
//            if(applyShopInfo.shopType!=null){
//                if (applyShopInfo.shopType == "0") {
//                    rlShopInfoSelf.isSelected = true
//                    rlShopInfoCompany.isSelected = false
//                } else {
//                    rlShopInfoCompany.isSelected = true
//                    rlShopInfoSelf.isSelected = false
//                }
//            }
            etShopInfoName.setText(applyShopInfo.shopName)
            tvShopInfoCategory.text = applyShopInfo.categoryNames?.replace(" ", "/")

            tvShopInfoAddress.text = applyShopInfo.shopProvince.orEmpty() +
                    applyShopInfo.shopCity.orEmpty() +
                    applyShopInfo.shopCounty.orEmpty() +
                    applyShopInfo.shopTown.orEmpty() +
                    applyShopInfo.shopAdd
            etShopInfoContactName.setText(applyShopInfo.linkName)
            etShopInfoContactPhone.setText(applyShopInfo.linkPhone)

            if (!applyShopInfo.licenceImg.isNullOrEmpty()) tvShopInfoLicense.text = "已上传"
            if (!applyShopInfo.legalImg.isNullOrEmpty()) tvShopInfoIdCardFront.text = "已上传"
            if (!applyShopInfo.legalBackImg.isNullOrEmpty()) tvShopInfoIdCardBack.text = "已上传"
            if (!applyShopInfo.holdImg.isNullOrEmpty()) tvShopInfoIdCardHand.text = "已上传"

        }
    }

    override fun onShopInfoError(code: Int) {

    }

    override fun onApplyShopInfoSuccess() {
        hideDialogLoading()
        showToast("申请成功")
        EventBus.getDefault().post(ApplyShopInfoEvent())
        finish()
    }

    override fun onApplyShopInfoError(code: Int) {
        hideDialogLoading()
    }

    // 单选
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopCategory(applyShopCategory: CategoryVO) {
        val categoryIdSB = StringBuilder()
        val categoryNameSB = StringBuilder()
        categoryIdSB.append(applyShopCategory.categoryId)
        categoryNameSB.append(applyShopCategory.name)
        applyShopInfo.goodsManagementCategory = categoryIdSB.toString()
        tvShopInfoCategory.text = categoryNameSB.toString()
    }

    // 多选
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopCategory(event: List<ApplyShopCategory>) {
        selectedCategoryList = event
        val categoryIdSB = StringBuilder()
        val categoryNameSB = StringBuilder()
        selectedCategoryList?.forEachIndexed { index, applyShopCategory ->
            categoryIdSB.append(applyShopCategory.categoryId)
            categoryNameSB.append(applyShopCategory.name)
//            if (index < (selectedCategoryList?.size ?: 0) - 1) {
//                categoryIdSB.append(",")
//                categoryNameSB.append("/")
//            }
        }

        applyShopInfo.goodsManagementCategory = categoryIdSB.toString()
        tvShopInfoCategory.text = categoryNameSB.toString()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopAddress(event: ApplyShopPoiEvent) {
        //poi = event.poi

        applyShopInfo.shopLat = event?.adInfo?.latLng?.latitude
        applyShopInfo.shopLng = event?.adInfo?.latLng?.longitude
        applyShopInfo.shopAdd = event?.adInfo?.address
        tvShopInfoAddress.text = applyShopInfo.shopAdd
        if (event.adInfo != null) {
            adInfo = event.adInfo
            applyShopInfo.shopProvince = adInfo?.province
            applyShopInfo.shopCity = adInfo?.city
            applyShopInfo.shopCounty = adInfo?.district
//            applyShopInfo.shopProvince?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
//            applyShopInfo.shopCity?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
//            applyShopInfo.shopCounty?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun updateShopAdInfo(event: AdInfo) {
//        adInfo = event
//        applyShopInfo.shopProvince = event.province
//        applyShopInfo.shopCity = event.city
//        applyShopInfo.shopCounty = event.district
//
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            LICENSE -> {
                applyShopInfo.licenceImg = event.remoteUrl
                tvShopInfoLicense.text = "已上传"
            }
            IDCARD_FRONT -> {
                applyShopInfo.legalImg = event.remoteUrl
                tvShopInfoIdCardFront.text = "已上传"
            }
            IDCARD_BACK -> {
                applyShopInfo.legalBackImg = event.remoteUrl
                tvShopInfoIdCardBack.text = "已上传"
            }
            IDCARD_HAND -> {
                applyShopInfo.holdImg = event.remoteUrl
                tvShopInfoIdCardHand.text = "已上传"
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rlShopInfoSelf -> {//个人店铺和企业店铺参数值是 shop_type 0-个人 1-企业
                rlShopInfoSelf.isSelected = true
                rlShopInfoCompany.isSelected = false
                applyShopInfo.shopType = "0"
            }
            R.id.rlShopInfoCompany -> {
                rlShopInfoCompany.isSelected = true
                rlShopInfoSelf.isSelected = false
                applyShopInfo.shopType = "1"
            }
            R.id.rlShopInfoCategory -> {
                val intent = Intent(this, ApplyShopCategoryActivity::class.java)
                intent.putExtra("goodsManagementCategory", applyShopInfo.goodsManagementCategory)
                startActivity(intent)
            }
            R.id.rlShopInfoAddress -> {
               AddressActivity.openActivity(this, adInfo?.latLng, adInfo?.address);
                // ActivityUtils.startActivity(ApplyShopAddressActivity::class.java)
            }
            R.id.rlShopInfoLicense -> {
                goUploadImageActivity(LICENSE, "上传营业执照", applyShopInfo.licenceImg)
            }
            R.id.rlShopInfoIdCardFront -> {
                goUploadImageActivity(IDCARD_FRONT, "上传身份证正面照", applyShopInfo.legalImg)
            }
            R.id.rlShopInfoIdCardBack -> {
                goUploadImageActivity(IDCARD_BACK, "上传身份证反面照", applyShopInfo.legalBackImg)
            }
            R.id.rlShopInfoIdCardHand -> {
                goUploadImageActivity(IDCARD_HAND, "上传手持证件照", applyShopInfo.holdImg)
            }
            R.id.tvApplyShopInfoNext -> {
                submitApplyInfo()

            }
        }
    }

    private fun submitApplyInfo() {
        applyShopInfo.shopName = etShopInfoName.text.toString()
        applyShopInfo.linkName = etShopInfoContactName.text.toString()
        applyShopInfo.linkPhone = etShopInfoContactPhone.text.toString()
        try {
            checkNotBlack(applyShopInfo.shopType) { "请选择店铺类型" }
            checkNotBlack(applyShopInfo.shopName) { "请输入店铺名称" }
            checkNotBlack(applyShopInfo.goodsManagementCategory) { "请选择经营类目" }
            checkNotNull(applyShopInfo.shopAdd) { "请选择店铺地址" }
            checkNotBlack(applyShopInfo.linkName) { "请输入联系人" }
            checkNotBlack(applyShopInfo.linkPhone) { "请输入联系电话" }
            checkNotBlack(applyShopInfo.licenceImg) { "请上传营业执照" }
            checkNotBlack(applyShopInfo.legalImg) { "请上传身份证正面照" }
            checkNotBlack(applyShopInfo.legalBackImg) { "请上传身份证反面照" }
            checkNotBlack(applyShopInfo.holdImg) { "手持证件照" }
            showDialogLoading()
            mPresenter?.requestApplyShopInfoData(applyShopInfo)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            showToast(e.message ?: "")
        }

    }

    private fun goUploadImageActivity(type: Int, title: String, imageUrl: String? = null) {
        val intent = Intent(this, ApplyShopUploadActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("title", title)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }

}