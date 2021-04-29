package com.lingmiao.shop.business.me.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.AccountSettingActivity
import com.lingmiao.shop.business.me.FeedbackActivity
import com.lingmiao.shop.business.me.PersonInfoActivity
import com.lingmiao.shop.business.me.ShopManageActivity
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.lingmiao.shop.business.me.presenter.impl.MyPreImpl
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseFragment
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.wallet.bean.WalletVo
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的
 */
@Deprecated("暂时不用")
class MyFragment : BaseFragment<MyPresenter>(), View.OnClickListener,MyPresenter.View {

    private var my:My? = null

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.fragment_my
    }

    override fun createPresenter(): MyPresenter? {
        return MyPreImpl(this)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {
        rlMyPersonInfo.setOnClickListener(this)
        rlMyWallet.setOnClickListener(this)
        rlMyShopManage.setOnClickListener(this)
        rlMyFeedback.setOnClickListener(this)
        rlMyContactService.setOnClickListener(this)
        rlMySetting.setOnClickListener(this)
        mPresenter?.onCreate()
        mPresenter?.getMyData()
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
            R.id.rlMyWallet -> {//我的钱包
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
            R.id.rlMySetting -> {//设置
                ActivityUtils.startActivity(AccountSettingActivity::class.java)
            }
        }
    }

    override fun onSetVipInfo(item: IdentityVo?) {

    }

    override fun onMyDataSuccess(bean: My) {
        val loginInfo = UserManager.getLoginInfo()
//        if(loginInfo!=null){
//            tvMyShopName.text = loginInfo.shopName
//        }
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

        ivMyShopStatus.visibility = if(loginInfo?.shopStatus=="OPEN") View.VISIBLE else View.GONE

        loginInfo?.clerkId = my?.clerkId
        loginInfo?.mobile = my?.mobile
        loginInfo?.shopId = my?.shopId
        if (loginInfo != null) {
            UserManager.setLoginInfo(loginInfo)
        }

    }

    override fun ontMyDataError() {

    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePersonInfo(event:PersonInfoRequest){
        mPresenter?.getMyData()
    }
}