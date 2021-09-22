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
import com.lingmiao.shop.business.goods.GroupManagerLv1Activity
import com.lingmiao.shop.business.login.LoginActivity
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.main.ApplyShopHintActivity
import com.lingmiao.shop.business.main.MainActivity
import com.lingmiao.shop.business.main.MessageCenterActivity
import com.lingmiao.shop.business.main.presenter.MainPresenter
import com.lingmiao.shop.business.main.presenter.impl.MainPresenterImpl
import com.lingmiao.shop.business.me.ShopManageActivity
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.tools.LogisticsToolActivity
import com.lingmiao.shop.business.tuan.ActivityIndexActivity
import com.lingmiao.shop.business.tuan.OrderIndexActivity
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.show
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragment : BaseFragment<MainPresenter>(), MainPresenter.View {

    private var mainInfo: MainInfo? = null
    private var fromMain: Boolean? = null
    private var versionUpdateDialog: AppCompatDialog? = null
    private var accountSetting: AccountSetting? = null


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }

        fun newInstance(fromType: Boolean): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("fromMain", fromType)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.fragment_main
    }

    override fun createPresenter(): MainPresenter? {
        return MainPresenterImpl(context!!, this)
    }

    fun setFromMain(fromType: Boolean){
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
            DialogUtils.showDialog(activity!!,"退出登录","确定退出登录吗？",null,View.OnClickListener{
                UserManager.loginOut()
                ActivityUtils.startActivity(LoginActivity::class.java)
                ActivityUtils.finishAllActivitiesExceptNewest()
                activity?.finish()
            })
        }

        llMainWaitSendGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_SEND_GOODS))
        }
        llMainWaitRefund.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_REFUND))
        }

        // 待发货
        llMainWaitSendGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_SEND_GOODS))
        }
        // 待退款
        llMainWaitRefund.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_REFUND))
        }

        // 店铺管理
        tvToolShopManager.setOnClickListener {
            ActivityUtils.startActivity(ShopManageActivity::class.java);
        }
        // 商品分组
        tvToolGoodsGroup.setOnClickListener {
            GroupManagerLv1Activity.openActivity(mContext)
        }
        // 物流工具
        tvToolLogisticTool.setOnClickListener {
            ActivityUtils.startActivity(LogisticsToolActivity::class.java)
        }
        // 银包
        tvToolWalletAccount.setOnClickListener {
            ActivityUtils.startActivity(MyWalletActivity::class.java);
        }

        // 团购报名
        tvToolTuanRegister.setOnClickListener {
            ActivityUtils.startActivity(ActivityIndexActivity::class.java);
        }
        // 团购供货
        tvToolTuanManager.setOnClickListener {
            ActivityUtils.startActivity(OrderIndexActivity::class.java);
        }

        showPageLoading()
        mPresenter?.requestMainInfoData()
        mPresenter?.requestAccountSettingData()



    }

    override fun useEventBus(): Boolean {
        return true
    }

    private fun initShopStatus(loginInfo: LoginInfo?) {
        llMainShopOpen.visibility = View.GONE
        llMainShopOther.visibility = View.VISIBLE
        tvMainShopReason.visibility = View.GONE
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
                    if(fromMain!=true&& ActivityUtils.getTopActivity() !is MainActivity){
                        versionUpdateDialog?.dismiss()
                        activity?.finish()
                        ActivityUtils.startActivity(MainActivity::class.java)
                        return
                    }
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
        hidePageLoading()
        mainInfo = bean
        smartRefreshLayout.finishRefresh()
        initShopStatus(UserManager.getLoginInfo())
    }

    private fun initOpeningShopView() {
        if (mainInfo == null) return
//        tvMainTodayIncome.text = mainInfo?.dayIncome?.toString()
        tvMainPayCount.text = mainInfo?.allOrdersNum
//        tvMainVisitorCount.text = mainInfo?.visitNum?.toString()
//        tvMainNewVisitorCount.text = mainInfo?.newVisitNum?.toString()
        tvMainWaitSendGoodsCount.text = mainInfo?.waitShipOrderNum
        tvMainWaitRefundCount.text = mainInfo?.afterSaleOrderNum
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let {
            tvMainShopName.text = loginInfo.shopName
            if (!TextUtils.isEmpty(loginInfo.shopLogo)) GlideUtils.setImageUrl(
                civMainShopHead,
                loginInfo.shopLogo
            )
        }


        // 总信息
        // 支付订单数
        tvMainPayCount.text = String.format("%s", mainInfo?.paidOrderNum);
        // 交易额
        tvTradePrice.text = String.format("%s", mainInfo?.tradeAmount);
        // 客单价
        tvAveragePrice.text = String.format("%s", mainInfo?.orderAvgPrice);

        if(mainInfo?.communityShop == 1) {
            // 团长
            ll_tuan_today.visibility = View.VISIBLE;
            // 团长总数
            tvTuanCount.text = String.format("%s", mainInfo?.distributorNum);
            // 团均订单数
            tvTuanOrderCount.text = String.format("%s", mainInfo?.distributorOrderNum);
            // 团长出单率
            tvTuanOrderRate.text = String.format("%s%%", mainInfo?.distributorOrderRatio);


            // 自提点
            ll_take_today.visibility = View.VISIBLE;
            // 自提点总数
            tvTakePointCount.text = String.format("%s", mainInfo?.siteNum);
            // 点均订单数
            tvTakePointOrderCount.text = String.format("%s", mainInfo?.siteOrderNum);
            // 自提点出单率
            tvTakePointAverageOrderCount.text = String.format("%s%%", mainInfo?.distributorOrderRatio);


            //团长综合
            ll_tuan_yesterday.visibility = View.VISIBLE;

            // 活跃团长
            tvActiveTuanCount.text = String.format("%s", mainInfo?.lastDayActiveDistributorNum);
            //团长新增
            tvNewTuanCount.text = String.format("%s", mainInfo?.lastDayNewDistributorNum);
            // 用户新增
//        tvNewUserCount.text = String.format("%s", mainInfo?.lastDayNewMemberNum);
            // 自提点新增
            tvTakePointNewCount.text = String.format("%s", mainInfo?.lastDayNewSiteNum);

        } else {

            ll_tuan_today.visibility = View.GONE;

            ll_take_today.visibility = View.GONE;

            ll_tuan_yesterday.visibility = View.GONE;
        }

        // 昨日
        // 支付订单数
        tvYesterdayMainPayCount.text = String.format("%s", mainInfo?.lastDayPaidOrderNum);
        // 交易额
        tvYesterdayTradePrice.text = String.format("%s", mainInfo?.lastDayTradeAmount);
        // 客单价
        tvYesterdayAveragePrice.text = String.format("%s", mainInfo?.lastDayOrderAvgPrice);
    }

    override fun onMainInfoError(code: Int) {
        smartRefreshLayout.finishRefresh()
        hidePageLoading()
    }

    override fun onMainDataSuccess(bean: MainInfoVo?, status: ShopStatus?) {

    }

    override fun onAccountSettingSuccess(bean: AccountSetting) {
        accountSetting = bean
//        if(BuildConfig.DEBUG){
//            bean.castUpdate = true
//            bean.needUpgrade = true
//        }
        if(!bean.needUpgrade) return
        versionUpdateDialog = DialogUtils.showVersionUpdateDialog(requireActivity(), "版本更新", bean.upgradeContent ?: "", null,
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

    }

    override fun applyVIP(message: String) {
        //nothing
    }

    override fun applyVI2(my: My?, identity: IdentityVo?) {
        //nothing
    }

    override fun onWarningNumber(data: Int) {
       //xi xi
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshShopStatus(event: ApplyShopInfoEvent) {
        mPresenter?.requestMainInfoData()
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