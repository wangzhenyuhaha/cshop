package com.lingmiao.shop.business.sales

import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.ShopQRCodeActivity
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.sales.presenter.PopularizePre
import com.lingmiao.shop.business.sales.presenter.impl.PopularizePreImpl
import com.lingmiao.shop.databinding.ActivityPopularizeBinding
import kotlinx.android.synthetic.main.fragment_my_new.*

class PopularizeActivity : BaseVBActivity<ActivityPopularizeBinding, PopularizePre>(),
    PopularizePre.View {

    override fun createPresenter() = PopularizePreImpl(this, this)

    override fun getViewBinding() = ActivityPopularizeBinding.inflate(layoutInflater)

    private var my: My? = null

    override fun initView() {

        mPresenter?.getMyData()

        mBinding.container1.singleClick {
            if (isAudited()) {
                //ShopStatusConstants.FINAL_OPEN
                val context = ActivityUtils.getTopActivity()
                val intent = Intent(context, ShopQRCodeActivity::class.java)
                intent.putExtra("SHOP_ID", my?.shopId)
                intent.putExtra("QRCODE_TYPE",0)
                context.startActivity(intent)
            } else if (UserManager.getLoginInfo()?.shopStatus == ShopStatusConstants.OVERDUE) {
                showToast(UserManager.getLoginInfo()?.statusReason)
            } else {
                showToast("店铺审核中，审核通过后即可查看二维码")
            }
        }

        mBinding.container2.singleClick {
            if (isAudited()) {
                //ShopStatusConstants.FINAL_OPEN
                val context = ActivityUtils.getTopActivity()
                val intent = Intent(context, ShopQRCodeActivity::class.java)
                intent.putExtra("SHOP_ID", my?.shopId)
                intent.putExtra("QRCODE_TYPE",1)
                context.startActivity(intent)
            } else if (UserManager.getLoginInfo()?.shopStatus == ShopStatusConstants.OVERDUE) {
                showToast(UserManager.getLoginInfo()?.statusReason)
            } else {
                showToast("店铺审核中，审核通过后即可查看二维码")
            }
        }

        mBinding.container3.singleClick {

        }

        mBinding.container4.singleClick {
            if (isAudited()) {
                my?.shopId?.apply {
                    mPresenter?.getShareInfo(this)
                }
            } else {
                showToast("店铺审核中，审核通过后即可店铺分享");
            }
        }


    }

    private fun isAudited(): Boolean {
        return UserManager.getLoginInfo()?.shopStatus == ShopStatusConstants.FINAL_OPEN;
    }

    override fun onMyDataSuccess(bean: My) {
        my = bean
        hideDialogLoading()
    }

    override fun ontMyDataError() {
        hideDialogLoading()
        showToast("网络错误")
    }

    override fun useLightMode() = false

}