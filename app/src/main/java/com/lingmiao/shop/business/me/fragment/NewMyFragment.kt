package com.lingmiao.shop.business.me.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.me.*
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.lingmiao.shop.business.me.presenter.impl.MyPreImpl
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.formatDouble
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_my_new.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的
 */
class NewMyFragment : BaseFragment<MyPresenter>(), View.OnClickListener, MyPresenter.View {

    private var identity: IdentityVo? = null
    private var my: My? = null

    companion object {
        fun newInstance(): NewMyFragment {
            return NewMyFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_new
    }

    override fun createPresenter(): MyPresenter {
        return MyPreImpl(mContext, this)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {
        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        // rlMyPersonInfo.setOnClickListener(this)
        tvVip.setOnClickListener(this)
        tvMyWallet.setOnClickListener(this)
        tvBalance.setOnClickListener(this)
        tvSetting.setOnClickListener(this)
        tvManagerSetting.setOnClickListener(this)
        tvShopQRCode.setOnClickListener(this)
        tvShareManager.setOnClickListener(this)
        tvWeChatApprove.setOnClickListener(this)
//        rlMyShopManage.setOnClickListener(this)
        rlMyFeedback.setOnClickListener(this)
        rlMyContactService.setOnClickListener(this)
        rlHelpDoc.setOnClickListener(this)
//        rlMySetting.setOnClickListener(this)
        mPresenter?.onCreate()

        refreshData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlMyPersonInfo -> {//个人信息
                val intent = Intent(activity, PersonInfoActivity::class.java)
                my?.let {
                    intent.putExtra("bean", my!!)
                }
                startActivity(intent)
            }
            R.id.tvVip -> {
                ApplyVipActivity.openActivity(requireActivity(), my, identity)
            }
            R.id.tvMyWallet -> {//我的钱包
                ActivityUtils.startActivity(MyWalletActivity::class.java)
            }
            R.id.rlMyShopManage -> {//店铺管理
                ActivityUtils.startActivity(ShopManageActivity::class.java)
            }
            R.id.rlMyFeedback -> {//建议反馈
                ActivityUtils.startActivity(FeedbackActivity::class.java)
            }
            R.id.rlMyContactService -> {//联系客服
                OtherUtils.goToDialApp(activity, IConstant.SERVICE_PHONE)
            }
            R.id.tvShareManager -> {
                if(isAudited()) {
                    my?.shopId?.apply {
                        mPresenter?.getShareInfo(this)
                    }
                } else {
                    showToast("店铺审核中，审核通过后即可店铺分享");
                }
            }
            R.id.tvWeChatApprove -> {

                val context = ActivityUtils.getTopActivity()
                val intent = Intent(context, ShopWeChatApproveActivity::class.java)
                context.startActivity(intent)
            }
            R.id.tvManagerSetting,
            R.id.tvSetting,
            R.id.rlMySetting -> {//设置
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
            R.id.tvShopQRCode -> {
                // 二维码
                if(isAudited()) {
                    val context = ActivityUtils.getTopActivity()
                    val intent = Intent(context, ShopQRCodeActivity::class.java)
                    intent.putExtra("SHOP_ID", my?.shopId)
                    context.startActivity(intent)
                } else {
                    showToast("店铺审核中，审核通过后即可查看二维码");
                }
            }
            R.id.tvBalance -> {
                // 余额
            }
            R.id.rlHelpDoc -> {
                ActivityUtils.startActivity(HelpDocActivity::class.java)
            }
        }
    }

    fun isAudited() : Boolean {
        return UserManager.getLoginInfo()?.shopStatus == ShopStatusConstants.FINAL_OPEN;
    }

    override fun onMyDataSuccess(bean: My) {
        smartRefreshLayout.finishRefresh()
        my = bean
        tvMyName.text = bean.uname
        tvMyShopName.text = bean.shopName
        tvMyAccount.text = bean.founderText

        if (!TextUtils.isEmpty(bean.mobile) && bean.mobile?.length!! > 7) {
            tvMyPhone.text = "${bean.mobile!!.substring(0, 3)}****${bean.mobile!!.substring(7)}"
        }

        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.shopName = my?.shopName
        loginInfo?.shopLogo = my?.shopLogo
        loginInfo?.clerkId = my?.clerkId
        loginInfo?.mobile = my?.mobile
        loginInfo?.shopId = my?.shopId
        setUserUi(loginInfo)
        if (loginInfo != null) {
            UserManager.setLoginInfo(loginInfo)
        }

    }

    fun setUserUi(loginInfo: LoginInfo?) {
        loginInfo?.apply {
            tvMyShopName.text = shopName
            GlideUtils.setImageUrl(ivMyHead, shopLogo)
        }
    }

    override fun ontMyDataError() {
        smartRefreshLayout.finishRefresh()

    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        tvBalance.text = getString(
            R.string.wallet_value,
            formatDouble(data?.balanceAccount?.balanceAmount ?: 0.0)
        )
    }

    override fun onLoadedAccount(data: WithdrawAccountVo?) {

    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {

    }

    override fun onSetVipInfo(item: IdentityVo?) {
        this.identity = item
        tvTryHint.text = item?.shopTitle
        if (item?.isVip() == true) {
            ivTryLogo.setImageResource(R.mipmap.ic_try_logo)
            tvTryHint.text = String.format("%s%s", item.shopTitle, item.get_VipHint())
            tvVip.text = "续费"
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_try_logo)
            ivMyShopStatus.visiable()
        } else {
            ivTryLogo.setImageResource(R.mipmap.ic_vip_period)
            tvTryHint.text = String.format("%s%s", item?.shopTitle, item?.get_CommonHint())
            tvVip.text = "立即开通"
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_vip_period)
            ivMyShopStatus.gone()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePersonInfo(event: PersonInfoRequest) {
        refreshData()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLogo(event: LoginInfo) {
        event?.apply {
            setUserUi(event)
        }
    }

    fun refreshData() {
        mPresenter?.getMyData()
        mPresenter?.getIdentity()
        mPresenter?.loadWalletData()
    }

}