package com.lingmiao.shop.business.me.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.lingmiao.shop.business.me.presenter.impl.MyPreImpl
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.me.*
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.util.formatDouble
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_my_new.*
import kotlinx.android.synthetic.main.fragment_my_new.smartRefreshLayout
import kotlinx.android.synthetic.main.fragment_my_new.tvManagerSetting
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的
 */
class NewMyFragment : BaseFragment<MyPresenter>(), View.OnClickListener,MyPresenter.View {

    private var identity : IdentityVo? = null
    private var my:My? = null

    companion object {
        fun newInstance(): NewMyFragment {
            return NewMyFragment()
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.fragment_my_new
    }

    override fun createPresenter(): MyPresenter? {
        return MyPreImpl(this)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {
        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        rlMyPersonInfo.setOnClickListener(this)
        tvVip.setOnClickListener(this)
        tvMyWallet.setOnClickListener(this)
        tvBalance.setOnClickListener(this)
        tvSetting.setOnClickListener(this)
        tvManagerSetting.setOnClickListener(this)
        tvShopQRCode.setOnClickListener(this);
//        rlMyShopManage.setOnClickListener(this)
        rlMyFeedback.setOnClickListener(this)
        rlMyContactService.setOnClickListener(this)
//        rlMySetting.setOnClickListener(this)
        mPresenter?.onCreate()

        refreshData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlMyPersonInfo -> {//个人信息
                val intent = Intent(activity,PersonInfoActivity::class.java)
                my?.let {
                    intent.putExtra("bean", my!!)
                }
                startActivity(intent)
            }
            R.id.tvVip -> {
                ApplyVipActivity.openActivity(activity!!, my, identity);
            }
            R.id.tvMyWallet -> {//我的钱包
                ActivityUtils.startActivity(MyWalletActivity::class.java);
            }
            R.id.rlMyShopManage -> {//店铺管理
                ActivityUtils.startActivity(ShopManageActivity::class.java)
            }
            R.id.rlMyFeedback -> {//建议反馈
                ActivityUtils.startActivity(FeedbackActivity::class.java)
            }
            R.id.rlMyContactService -> {//联系客服
                OtherUtils.goToDialApp(activity,IConstant.SERVICE_PHONE)
            }
            R.id.tvManagerSetting,
            R.id.tvSetting,
            R.id.rlMySetting -> {//设置
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
            R.id.tvShopQRCode -> {
                // 二维码
                ActivityUtils.startActivity(ShopQRCodeActivity::class.java)
            }
            R.id.tvBalance -> {
                // 余额
            }
        }
    }


    override fun onMyDataSuccess(bean: My) {
        smartRefreshLayout.finishRefresh()
        val loginInfo = UserManager.getLoginInfo()
        if(loginInfo!=null){
            tvMyShopName.text = loginInfo.shopName
        }
        my = bean
        tvMyName.text = bean.uname
        tvMyShopName.text = bean.shopName
        tvMyAccount.text = bean.founderText
        if(!TextUtils.isEmpty(bean.face)){
            GlideUtils.setImageUrl(ivMyHead,bean.face)
        }
        if(!TextUtils.isEmpty(bean.mobile)&& bean.mobile?.length!! >7){
            tvMyPhone.text = "${bean.mobile!!.substring(0, 3)}****${bean.mobile!!.substring(7)}"
        }

        loginInfo?.clerkId = my?.clerkId
        loginInfo?.mobile = my?.mobile
        loginInfo?.shopId = my?.shopId
        if (loginInfo != null) {
            UserManager.setLoginInfo(loginInfo)
        }

        //tvBalance.text = String.format("￥%s", 0);
//        tvDeposit.text = String.format("￥%s", 0);
//        tvUseTime.text = String.format("%s天", 0);

    }

    override fun ontMyDataError() {
        smartRefreshLayout.finishRefresh()

    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        tvBalance.text = getString(R.string.wallet_value, formatDouble(data?.balanceAccount?.balanceAmount?:0.0))
    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {

    }

    override fun onSetVipInfo(item: IdentityVo?) {
        this.identity = item;
        tvTryHint.setText(item?.shopTitle)
        if(item?.isVip() == true) {
            ivTryLogo.setImageResource(R.mipmap.ic_try_logo)
            tvTryHint.setText(String.format("%s%s", item.shopTitle, item.get_VipHint()));
            tvVip.setText("续费")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_try_logo)
            ivMyShopStatus.visiable();
        } else {
            ivTryLogo.setImageResource(R.mipmap.ic_vip_period)
            tvTryHint.setText(String.format("%s%s", item?.shopTitle, item?.get_CommonHint()));
            tvVip.setText("立即开通")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_vip_period)
            ivMyShopStatus.gone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePersonInfo(event:PersonInfoRequest){
        refreshData()
    }

    fun refreshData() {
        mPresenter?.getMyData()
        mPresenter?.getIdentity();
        mPresenter?.loadWalletData();
    }
}