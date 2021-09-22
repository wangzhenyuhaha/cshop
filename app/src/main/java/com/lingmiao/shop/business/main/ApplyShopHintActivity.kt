package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.*
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.main.bean.ApplyShopHint
import com.lingmiao.shop.business.main.presenter.ApplyShopHintPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopHintPresenterImpl
import kotlinx.android.synthetic.main.main_activity_apply_shop_hint.*
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*

/**
 *   申请开店
 */
class ApplyShopHintActivity : BaseActivity<ApplyShopHintPresenter>(), ApplyShopHintPresenter.View {


    override fun getLayoutId() = R.layout.main_activity_apply_shop_hint

    companion object {
        private const val REQUEST_CODE_SERVICE = 8
    }

    override fun useLightMode() = false


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")


        //showPageLoading()
        //mPresenter.requestApplyShopHintData()
        ivApplyShopHintNext.isSelected = true
        ivApplyShopHintNext.setOnClickListener {
            ivApplyShopHintNext.isSelected = !ivApplyShopHintNext.isSelected
        }
        tvApplyShopHintService.setOnClickListener {
            val intent = Intent(this, UserServiceH5Activity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SERVICE)
        }
        tvApplyShopHintNext.setOnClickListener {
            if (!ivApplyShopHintNext.isSelected) {
                showToast("请先同意商城入驻协议")
                return@setOnClickListener
            }
            ActivityUtils.startActivity(ApplyShopInfoActivity::class.java)
            finish()
        }

    }

    override fun createPresenter() = ApplyShopHintPresenterImpl(this, this)


    override fun onApplyShopHintSuccess(bean: ApplyShopHint) {
        hidePageLoading()
    }

    override fun onApplyShopHintError(code: Int) {
        hidePageLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SERVICE && resultCode == Activity.RESULT_OK) {
            ivApplyShopHintNext.isSelected = true
        }
    }

}