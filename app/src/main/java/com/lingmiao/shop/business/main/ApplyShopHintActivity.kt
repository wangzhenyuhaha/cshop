package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.ApplyShopHint
import com.lingmiao.shop.business.main.presenter.ApplyShopHintPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopHintPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.main_activity_apply_shop_hint.*

/**
*   申请开店
*/
class ApplyShopHintActivity : BaseActivity<ApplyShopHintPresenter>(),ApplyShopHintPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_hint
    }

    companion object {
        private const val REQUEST_CODE_SERVICE = 8
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")

        //showPageLoading()
        //mPresenter.requestApplyShopHintData()
        ivApplyShopHintNext.isSelected = true
        ivApplyShopHintNext.setOnClickListener { ivApplyShopHintNext.isSelected = !ivApplyShopHintNext.isSelected}
        tvApplyShopHintService.setOnClickListener {
            val intent = Intent(this,UserServiceH5Activity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SERVICE)
        }
        tvApplyShopHintNext.setOnClickListener {
            if(!ivApplyShopHintNext.isSelected){
                showToast("请先同意商城入驻协议")
                return@setOnClickListener
            }
            ActivityUtils.startActivity(ApplyShopInfoActivity::class.java)
            finish()
        }
    }



    override fun createPresenter(): ApplyShopHintPresenter {
        return ApplyShopHintPresenterImpl(this, this)
    }

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