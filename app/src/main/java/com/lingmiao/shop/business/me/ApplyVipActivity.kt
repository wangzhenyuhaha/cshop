package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.lingmiao.shop.R
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.me.presenter.impl.ApplyVipPreImpl
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import kotlinx.android.synthetic.main.fragment_my_new.*
import kotlinx.android.synthetic.main.me_activity_apply_vip.*
import kotlinx.android.synthetic.main.me_activity_apply_vip.ivMyHead
import kotlinx.android.synthetic.main.me_activity_apply_vip.ivMyShopVipStatus
import kotlinx.android.synthetic.main.me_activity_apply_vip.tvBalance
import kotlinx.android.synthetic.main.me_activity_apply_vip.tvMyName
import kotlinx.android.synthetic.main.me_activity_apply_vip.tvMyShopName
import kotlinx.android.synthetic.main.me_activity_apply_vip.ivMyShopStatus

/**
*   开通会员
*/
class ApplyVipActivity : BaseActivity<ApplyVipPresenter>(),ApplyVipPresenter.View {

    var mUserInfo : My? = null;

    var mIdentity : IdentityVo? = null;

    var mWallet : WalletVo? = null;

    companion object {
        fun openActivity(context: Context, my : My?, identity : IdentityVo?) {
            if (context is Activity) {
                val intent = Intent(context, ApplyVipActivity::class.java)
                intent.putExtra("clerk", my)
                intent.putExtra("identity", identity);
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        mUserInfo = intent?.getParcelableExtra<My>("clerk");
        mIdentity = intent?.getSerializableExtra("identity") as IdentityVo;
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_apply_vip
    }

    override fun createPresenter(): ApplyVipPresenter {
        return ApplyVipPreImpl(this)
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("开通会员")
        mPresenter?.getVipPriceList();
        if(mIdentity == null) {
            mPresenter?.getIdentity();
        } else {
            onSetVipInfo(mIdentity);
        }
        setUserInfo();
    }

    fun setUserInfo() {
        val loginInfo = UserManager.getLoginInfo()
        tvMyShopName.setText(loginInfo?.shopName)
        tvMyName.setText(mUserInfo?.uname)
        if(!TextUtils.isEmpty(mUserInfo?.face)){
            GlideUtils.setImageUrl(ivMyHead,mUserInfo?.face)
        }
        //ivMyShopStatus.visibility = if(loginInfo?.shopStatus=="OPEN") View.VISIBLE else View.GONE
    }

    override fun onSetVipPriceList(list: List<VipType>?) {
        galleryRv.setDataList(list);
    }

    override fun onSetVipInfo(item : IdentityVo?) {
        dueDateTv.setText(String.format("试用时间%s到期,购买后有效期将顺延", item?.get_DueDateStr()));

        if(item?.isVip() == true) {
            dueDateTv.setText(String.format("%s%s到期,购买后有效期将顺延", "会员时间", item.get_DueDateStr()));
            tvApply.setText("续费")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_try_logo)
            ivMyShopStatus.visiable();
        } else {
            dueDateTv.setText(String.format("%s%s到期,购买后有效期将顺延", "试用时间", item?.get_DueDateStr()));
            tvApply.setText("立即开通")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_vip_period)
            ivMyShopStatus.gone();
        }
    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        mWallet = data;
        tvBalance.setText(getString(R.string.wallet_value, formatDouble(data?.balanceAccount?.balanceAmount?:0.0)));
    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {

    }

}