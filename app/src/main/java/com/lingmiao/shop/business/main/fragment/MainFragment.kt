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
                "???????????? >>", "????????????" -> {
                    ActivityUtils.startActivity(ApplyShopHintActivity::class.java)
                }
                "????????????" -> {
                    OtherUtils.goToDialApp(activity, IConstant.SERVICE_PHONE)
                }
            }
        }
        tvMainLoginOut.setOnClickListener {
            DialogUtils.showDialog(activity!!,"????????????","????????????????????????",null,View.OnClickListener{
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

        // ?????????
        llMainWaitSendGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_SEND_GOODS))
        }
        // ?????????
        llMainWaitRefund.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(IConstant.TAB_WAIT_REFUND))
        }

        // ????????????
        tvToolShopManager.setOnClickListener {
            ActivityUtils.startActivity(ShopManageActivity::class.java);
        }
        // ????????????
        tvToolGoodsGroup.setOnClickListener {
            GroupManagerLv1Activity.openActivity(mContext)
        }
        // ????????????
        tvToolLogisticTool.setOnClickListener {
            ActivityUtils.startActivity(LogisticsToolActivity::class.java)
        }
        // ??????
        tvToolWalletAccount.setOnClickListener {
            ActivityUtils.startActivity(MyWalletActivity::class.java);
        }

        // ????????????
        tvToolTuanRegister.setOnClickListener {
            ActivityUtils.startActivity(ActivityIndexActivity::class.java);
        }
        // ????????????
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
            //            APPLY("????????????")	??????????????????	????????????????????????---????????????
            //            APPLYING("?????????")	??????????????????????????????	???????????????????????? ---????????????
            //            OPEN("?????????")	??????	??????
            //            CLOSED("????????????")	????????????	????????????????????????---????????????
            //            REFUSED("????????????")	????????????????????????	????????????????????????---????????????
            //            UN_APPLY("?????????")	????????????	???????????????????????? ---????????????
            when (loginInfo.shopStatus) {
                "UN_APPLY" -> {
                    tvMainShopHint.text = "???????????????????????????"
                    tvMainShopNext.text = "???????????? >>"
                }

                "APPLY" -> {
                    tvMainShopName.text = "???????????????"
                    tvMainShopNext.text = "????????????"
                    SpanUtils.with(tvMainShopHint)
                        .append("????????????").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("?????????").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_fc0000
                            )
                        )
                        .append("??????????????????..").setForegroundColor(
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
                    tvMainShopName.text = "???????????????"
                    tvMainShopNext.text = "????????????"
                    SpanUtils.with(tvMainShopHint)
                        .append("????????????").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("?????????").setForegroundColor(
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
                    tvMainShopName.text = "?????????????????????"
                    tvMainShopNext.text = "????????????"
                    SpanUtils.with(tvMainShopHint)
                        .append("??????????????????").setForegroundColor(
                            ContextCompat.getColor(
                                Utils.getApp(),
                                R.color.color_909090
                            )
                        )
                        .append("???????????????\n").setForegroundColor(
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


        // ?????????
        // ???????????????
        tvMainPayCount.text = String.format("%s", mainInfo?.paidOrderNum);
        // ?????????
        tvTradePrice.text = String.format("%s", mainInfo?.tradeAmount);
        // ?????????
        tvAveragePrice.text = String.format("%s", mainInfo?.orderAvgPrice);

        if(mainInfo?.communityShop == 1) {
            // ??????
            ll_tuan_today.visibility = View.VISIBLE;
            // ????????????
            tvTuanCount.text = String.format("%s", mainInfo?.distributorNum);
            // ???????????????
            tvTuanOrderCount.text = String.format("%s", mainInfo?.distributorOrderNum);
            // ???????????????
            tvTuanOrderRate.text = String.format("%s%%", mainInfo?.distributorOrderRatio);


            // ?????????
            ll_take_today.visibility = View.VISIBLE;
            // ???????????????
            tvTakePointCount.text = String.format("%s", mainInfo?.siteNum);
            // ???????????????
            tvTakePointOrderCount.text = String.format("%s", mainInfo?.siteOrderNum);
            // ??????????????????
            tvTakePointAverageOrderCount.text = String.format("%s%%", mainInfo?.distributorOrderRatio);


            //????????????
            ll_tuan_yesterday.visibility = View.VISIBLE;

            // ????????????
            tvActiveTuanCount.text = String.format("%s", mainInfo?.lastDayActiveDistributorNum);
            //????????????
            tvNewTuanCount.text = String.format("%s", mainInfo?.lastDayNewDistributorNum);
            // ????????????
//        tvNewUserCount.text = String.format("%s", mainInfo?.lastDayNewMemberNum);
            // ???????????????
            tvTakePointNewCount.text = String.format("%s", mainInfo?.lastDayNewSiteNum);

        } else {

            ll_tuan_today.visibility = View.GONE;

            ll_take_today.visibility = View.GONE;

            ll_tuan_yesterday.visibility = View.GONE;
        }

        // ??????
        // ???????????????
        tvYesterdayMainPayCount.text = String.format("%s", mainInfo?.lastDayPaidOrderNum);
        // ?????????
        tvYesterdayTradePrice.text = String.format("%s", mainInfo?.lastDayTradeAmount);
        // ?????????
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
        versionUpdateDialog = DialogUtils.showVersionUpdateDialog(requireActivity(), "????????????", bean.upgradeContent ?: "", null,
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
        return UIData.create().setTitle("????????????").setContent(bean.upgradeContent)
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