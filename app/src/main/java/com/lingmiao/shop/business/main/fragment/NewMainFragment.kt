package com.lingmiao.shop.business.main.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.*
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.LoginActivity
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.main.ApplyShopHintActivity
import com.lingmiao.shop.business.main.MainActivity
import com.lingmiao.shop.business.main.MessageCenterActivity
import com.lingmiao.shop.business.main.bean.ApplyShopInfoEvent
import com.lingmiao.shop.business.main.bean.MainInfo
import com.lingmiao.shop.business.main.presenter.MainPresenter
import com.lingmiao.shop.business.main.presenter.impl.MainPresenterImpl
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.show
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.business.goods.*
import com.lingmiao.shop.business.main.bean.MainInfoVo
import com.lingmiao.shop.business.main.bean.TabChangeEvent
import com.lingmiao.shop.business.me.ManagerSettingActivity
import com.lingmiao.shop.business.sales.SalesActivityGoodsWarning
import com.lingmiao.shop.business.sales.SalesSettingActivity
import com.lingmiao.shop.business.sales.StatsActivity
import com.lingmiao.shop.business.sales.UserManagerActivity
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_new_main.*
import kotlinx.android.synthetic.main.fragment_new_main.civMainShopHead
import kotlinx.android.synthetic.main.fragment_new_main.ivMainMessage
import kotlinx.android.synthetic.main.fragment_new_main.llMainShopOpen
import kotlinx.android.synthetic.main.fragment_new_main.llMainShopOther
import kotlinx.android.synthetic.main.fragment_new_main.smartRefreshLayout
import kotlinx.android.synthetic.main.fragment_new_main.tvMainLoginOut
import kotlinx.android.synthetic.main.fragment_new_main.tvMainShopHint
import kotlinx.android.synthetic.main.fragment_new_main.tvMainShopName
import kotlinx.android.synthetic.main.fragment_new_main.tvMainShopNext
import kotlinx.android.synthetic.main.fragment_new_main.tvMainShopReason
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewMainFragment : BaseFragment<MainPresenter>(), MainPresenter.View {

    private var mainInfo: MainInfoVo? = null
    private var fromMain: Boolean? = null
    private var versionUpdateDialog: AppCompatDialog? = null
    private var accountSetting: AccountSetting? = null


    companion object {
        fun newInstance(): NewMainFragment {
            return NewMainFragment()
        }

        fun newInstance(fromType: Boolean): NewMainFragment {
            return NewMainFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("fromMain", fromType)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.fragment_new_main
    }

    override fun createPresenter(): MainPresenter? {
        return MainPresenterImpl(context!!, this)
    }

    fun setFromMain(fromType: Boolean) {
        fromMain = fromType
    }

    override fun initViewsAndData(rootView: View) {
//        fromMain = arguments?.getBoolean("fromMain", false)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            mPresenter?.requestMainInfoData()
        }
        LogUtils.d("fromMain:" + fromMain)
        val loginInfo = UserManager.getLoginInfo()
        ivMainMessage.setOnClickListener {
            ActivityUtils.startActivity(MessageCenterActivity::class.java)
        }

        tvMainShopNext.setOnClickListener {
            when (tvMainShopNext.text.toString()) {
                "申请开店 >>", "重新提交" -> {
                    ActivityUtils.startActivity(ApplyShopHintActivity::class.java)
                }
                "联系客服" -> {
                    OtherUtils.goToDialApp(activity, IConstant.SERVICE_PHONE)
                }
            }
        }
        tvMainLoginOut.setOnClickListener {
            DialogUtils.showDialog(activity!!, "退出登录", "确定退出登录吗？", null, View.OnClickListener {
                UserManager.loginOut()
                ActivityUtils.startActivity(LoginActivity::class.java)
                ActivityUtils.finishAllActivitiesExceptNewest()
                activity?.finish()
            })
        }

        showPageLoading()
        mPresenter?.requestMainInfoData()
        mPresenter?.requestAccountSettingData()
    }

    override fun useEventBus(): Boolean {
        return true
    }

    private fun initShopStatus(loginInfo: LoginInfo?) {
        switchStatusBtn.isChecked = loginInfo?.openStatus ?: false

        switchStatusBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter?.editShopStatus(if (isChecked) 1 else 0);
        }
        onShopStatusEdited();

        llMainShopOpen.visibility = View.GONE
        llMainShopOther.visibility = View.VISIBLE
        tvMainShopReason.visibility = View.GONE
        llHeader.visibility = View.GONE
        ivMainMessage.visibility = View.GONE
//        tvMainLoginOut.visibility = View.VISIBLE
        if (loginInfo != null) {
            //            APPLY("申请开店")	等待审核结果	等待审核提示页面---联系客服
            //            APPLYING("申请中")	申请信息的填写过程中	申请信息填写页面 ---重新填写
            //            OPEN("开启中")	正常	首页
            //            CLOSED("店铺关闭")	禁止运营	店铺关闭提示页面---联系客服
            //            REFUSED("审核拒绝")	修改注册重新申请	审核拒绝提示页面---重新填写
            //            UN_APPLY("未开店")	申请开店	申请开店提示页面 ---联系客服
            when (loginInfo.shopStatus) {
                "UN_APPLY" -> {
                    tvMainShopHint.text = "你还有没有开通店铺"
                    tvMainShopNext.text = "申请开店 >>"
                }

                "APPLY" -> {
                    tvMainShopName.text = "店铺待审核"
                    tvMainShopNext.text = "联系客服"
                    SpanUtils.with(tvMainShopHint)
                        .append("申请正在").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("审核中").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_fc0000
                            )
                        )
                        .append("，请耐心等待..").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .create()
                }
                "APPLYING" -> {

                }
                "OPEN" -> {
                    if (fromMain != true && ActivityUtils.getTopActivity() !is MainActivity) {
                        versionUpdateDialog?.dismiss()
                        activity?.finish()
                        ActivityUtils.startActivity(MainActivity::class.java)
                        return
                    }
                    llHeader.visibility = View.VISIBLE
                    llHeader.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary));
                    llMainShopOpen.visibility = View.VISIBLE
                    llMainShopOther.visibility = View.GONE
                    ivMainMessage.visibility = View.VISIBLE
                    //                    tvMainShopName.text=loginInfo?.nickname
                    initOpeningShopView()
                }

                "CLOSED" -> {
                    tvMainShopName.text = "店铺已关闭"
                    tvMainShopNext.text = "联系客服"
                    SpanUtils.with(tvMainShopHint)
                        .append("您的店铺").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("已关闭").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_fc0000
                            )
                        )
                        .create()
                    tvMainShopReason.text = loginInfo.statusReason
                    tvMainShopReason.show(true)
                }
                "REFUSED" -> {
                    tvMainShopName.text = "店铺审核不通过"
                    tvMainShopNext.text = "重新提交"
                    SpanUtils.with(tvMainShopHint)
                        .append("店铺申请资料").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("审核不通过\n").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_fc0000
                            )
                        )
                        .create()
                    tvMainShopReason.text = loginInfo.statusReason
                    tvMainShopReason.show(true)
                }

            }


        }
    }


    override fun onMainInfoSuccess(bean: MainInfo?) {

    }

    override fun onMainDataSuccess(bean: MainInfoVo?) {
        hidePageLoading()
        mainInfo = bean
        smartRefreshLayout.finishRefresh()
        initShopStatus(UserManager.getLoginInfo())
    }

    private fun initOpeningShopView() {
        if (mainInfo == null) {
            return;
        }
        setShop();

        // 总信息
        // 未接订单
        tvTodayUnTakeOrderCount.text = String.format("%s", mainInfo?.waitAcceptNum);
        // 已接订单
        tvTodayTakenOrderCount.text = String.format("%s", mainInfo?.alreadyAcceptNum);
        // 待配送
        tvTodayDistributeOrderCount.text = String.format("%s", mainInfo?.waitShipNum);

        // 待退款
        tvTodayRefund.text = String.format("%s", mainInfo?.refundNum);
        // 已完成
        tvTodayFinish.text = String.format("%s", mainInfo?.completeNum);
        // 失效订单
        tvTodayInvalid.text = String.format("%s", mainInfo?.cancelNum);

        //销售额
        tvTodaySales.text = String.format("%s", mainInfo?.tradeAmount);
        // 新用户数
        tvTodayNewUser.text = String.format("%s", mainInfo?.newMemberNum);
        // 订单量
        tvTodayOrderCount.text = String.format("%s", mainInfo?.allNum);

        // 管理设置
        tvManagerSetting.setOnClickListener {
            ActivityUtils.startActivity(ManagerSettingActivity::class.java)
        }
        // 商品管理
        tvGoodsManager.setOnClickListener {
            GoodsListActivity.openActivity(context!!);
        }
        // 商品分类
        tvCategoryManager.setOnClickListener {
            GoodsCategoryActivity.openActivity(context!!);
        }
        // 菜单管理
        tvMenuManager.setOnClickListener {
            ActivityUtils.startActivity(MenuManagerActivity::class.java)
//             ActivityUtils.startActivity(GoodsPublishActivity::class.java)
//             GoodsPublishActivity.openActivity(context!!, "");
        }
        // 营销设置
        tvSaleSetting.setOnClickListener {
            ActivityUtils.startActivity(SalesSettingActivity::class.java)
        }
        // 钱包账户
        tvWalletAccount.setOnClickListener {
            ActivityUtils.startActivity(MyWalletActivity::class.java);
        }
        // 用户管理
        tvUserManager.setOnClickListener {
            ActivityUtils.startActivity(UserManagerActivity::class.java)
        }
        // 数据统计
        tvDataAnalysis.setOnClickListener {
            ActivityUtils.startActivity(StatsActivity::class.java)
        }
        // 库存预警
        tvComeSoon.setOnClickListener {
            ActivityUtils.startActivity(SalesActivityGoodsWarning::class.java)
        }

        // 今日数据
        // 订单数量
        storeDataOfTodayTv.singleClick {
            EventBus.getDefault().post(TabChangeEvent(4))
            //ActivityUtils.startActivity(StatsActivity::class.java)
        }
        // 销售额
        salesDataOfTodayTv.singleClick {
            //ActivityUtils.startActivity(StatsActivity::class.java)
        }
        // 用户数据
        userDataOfTodayTv.singleClick {
            UserManagerActivity.newUser(context!!);
        }


        // 未接订单
        llTodayUnTakeOrderCount.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(0));
        }
        // 已接订单
        llMainWaitTakeGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(1))
        }
        // 配送中
        llMainWaitSendGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(1))
        }
        // 待退款
        llMainWaitRefund.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(2))
        }
        // 已完成
        layoutTodayFinish.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(3))
        }
        // 失效
        layoutTodayInvalid.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(4))
        }
    }

    override fun onMainInfoError(code: Int) {
        smartRefreshLayout.finishRefresh()
        hidePageLoading()
    }

    override fun onAccountSettingSuccess(bean: AccountSetting) {
        accountSetting = bean
//        if(BuildConfig.DEBUG){
//            bean.castUpdate = true
//            bean.needUpgrade = true
//        }
        if (!bean.needUpgrade) return
        versionUpdateDialog = DialogUtils.showVersionUpdateDialog(
            activity!!, "版本更新", bean.upgradeContent ?: "", null,
            View.OnClickListener {
                val builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(crateUIData(bean))
                builder.isDirectDownload = true
                builder.isShowNotification = false
                builder.isShowDownloadingDialog = true
                builder.isShowDownloadFailDialog = false
//                builder.downloadAPKPath = PathUtils.getExternalAppFilesPath() +"/new.apk"
                builder.executeMission(Utils.getApp())
            }, bean.castUpdate
        )
    }

    private fun crateUIData(bean: AccountSetting): UIData? {
        return UIData.create().setTitle("版本更新").setContent(bean.upgradeContent)
            .setDownloadUrl(bean.downloadUrl)
    }

    override fun onAccountSettingError(code: Int) {

    }

    override fun onShopStatusEdited() {
//        shopStatusTv.setTextColor(ContextCompat.getColor(context!!, if(switchStatusBtn.isChecked) R.color.color_0EA60 else R.color.white));
        shopStatusTv.text = if (switchStatusBtn.isChecked) "开店中" else "休息中";
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshShopStatus(event: ApplyShopInfoEvent) {
        mPresenter?.requestMainInfoData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLogo(event: LoginInfo) {
        event?.apply {
            setShop();
        }
    }

    fun setShop() {
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let {
            tvMainShopName.text = loginInfo.shopName
            if (!TextUtils.isEmpty(loginInfo.shopLogo)) GlideUtils.setImageUrl(
                civMainShopHead,
                loginInfo.shopLogo
            )
        }
    }

    override fun onResume() {
        super.onResume()
//        accountSetting?.let {
//            if(it.castUpdate && versionUpdateDialog!=null){
//                versionUpdateDialog?.dismiss()
//                onAccountSettingSuccess(it)
//            }
//        }
    }
}