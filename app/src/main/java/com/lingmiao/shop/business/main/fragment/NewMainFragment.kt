package com.lingmiao.shop.business.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.james.common.base.BaseFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.GoodsCategoryActivity
import com.lingmiao.shop.business.goods.GoodsListActivity
import com.lingmiao.shop.business.goods.GoodsScanActivity
import com.lingmiao.shop.business.goods.MenuGoodsManagerActivity
import com.lingmiao.shop.business.login.LoginActivity
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.main.*
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.pop.ApplyInfoPop
import com.lingmiao.shop.business.main.presenter.MainPresenter
import com.lingmiao.shop.business.main.presenter.impl.MainPresenterImpl
import com.lingmiao.shop.business.me.ApplyVipActivity
import com.lingmiao.shop.business.me.HelpDocActivity
import com.lingmiao.shop.business.me.OperationSettingActivity
import com.lingmiao.shop.business.me.ShopWeChatApproveActivity
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.event.ApplyVipEvent
import com.lingmiao.shop.business.sales.*
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.WebCameraUtil
import com.lingmiao.shop.util.WebCameraUtil.CallBack
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_new_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewMainFragment : BaseFragment<MainPresenter>(), MainPresenter.View {

    // ?????????????????????
    private var shopStatus: ShopStatus? = null

    private val model by activityViewModels<MainViewModel>()

    // ????????????????????????
    private var mainInfo: MainInfoVo? = null
    private var loginInfo: LoginInfo? = null
    private var fromMain: Boolean? = null
    private var versionUpdateDialog: AppCompatDialog? = null
    private var accountSetting: AccountSetting? = null

    //????????????
    //???????????????
    private var pop: ApplyInfoPop? = null


    //??????????????????
    var startTime: Long? = null

    //??????????????????
    var endTime: Long? = null

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

    override fun getLayoutId() = R.layout.fragment_new_main


    override fun createPresenter() = MainPresenterImpl(requireContext(), this)


    fun setFromMain(fromType: Boolean) {
        fromMain = fromType
    }


    override fun initViewsAndData(rootView: View) {
//        fromMain = arguments?.getBoolean("fromMain", false)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)

        pop = ApplyInfoPop(requireContext())

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            refreshPageData()
        }
        ivMainMessage.setOnClickListener {
            if (UserManager.isLogin()) {
                ActivityUtils.startActivity(MessageCenterActivity::class.java)
            }
        }


        //??????????????????
        refreshOne.setOnClickListener {
            showDialogLoading("?????????...")
            refreshPageData()
        }
        refreshTwo.setOnClickListener {
            showDialogLoading("?????????...")
            refreshPageData()
        }

        readApplyShopIV.singleClick {
            DialogUtils.showDialog(requireActivity(), R.mipmap.apply_shop_hint)
        }

// FrameLayout.LayoutParams(w, h)

        promoCode.text =
            if (UserManager.getPromCode().isEmpty()) "??????????????????????????????" else UserManager.getPromCode()

        promoCode.setOnClickListener {
            DialogUtils.showInputDialogEmpty(
                requireActivity(),
                "?????????",
                "",
                "?????????",
                UserManager.getPromCode(),
                "??????",
                "??????",
                null
            ) {
                if (it.isNotEmpty()) {
                    promoCode.text = it
                    UserManager.setPromCode(it)
                } else {
                    promoCode.text = "??????????????????????????????"
                    UserManager.setPromCode("")
                }
            }
        }

        readWXPay.singleClick {
            DialogUtils.showDiaLogBySelf(requireActivity(), R.mipmap.wechat_pay, 750, 1000)
        }

        readTongLian.singleClick {
            DialogUtils.showDiaLogBySelf(requireActivity(), R.mipmap.tonglian, 800, 550)
        }

        tvMainShopNext.setOnClickListener {
            when (tvMainShopNext.text.toString()) {
                "???????????? >>", "????????????" -> {
                    ActivityUtils.startActivity(ApplyShopHintActivity::class.java)
                }
                "????????????" -> {
                    OtherUtils.goToDialApp(activity, IConstant.SERVICE_PHONE)
                }
                "????????????" -> {
                    ActivityUtils.startActivity(ApplyVipActivity::class.java)
                }
            }
        }
        tvMainLoginOut.setOnClickListener {
            DialogUtils.showDialog(
                requireActivity(),
                "????????????",
                "????????????????????????",
                null
            ) {
                UserManager.loginOut()
                ActivityUtils.startActivity(LoginActivity::class.java)
                ActivityUtils.finishAllActivitiesExceptNewest()
                activity?.finish()
            }
        }


        //?????????????????????
        //?????????????????????????????????

        //????????????

        //?????????????????????

        showPageLoading()
        mPresenter?.requestMainInfoData()
        mPresenter?.requestAccountSettingData()

        pop?.liveData?.observe(this, Observer {
            if (it == 0) {
                //??????????????????
                WebCameraUtil.permissionHandle(activity, callBack)
            } else {
                //????????????????????????
                ApplyShopInfoActivity.openActivity(requireContext(), false)
            }

            pop?.dismiss()
        })
    }

    override fun useEventBus() = true


    private fun initShopStatus(loginInfo: LoginInfo?) {
        this.loginInfo = loginInfo
        llMainShopOpen.visibility = View.GONE
        llMainShopOther.visibility = View.VISIBLE
        tvMainShopReason.visibility = View.GONE
        llHeader.visibility = View.GONE
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
                ShopStatusConstants.UN_APPLY -> {
                    tvMainShopHint.text = "???????????????????????????"
                    tvMainShopNext.text = "???????????? >>"
                    remark.gone()
                }
                ShopStatusConstants.APPLY,
                ShopStatusConstants.APPLYING -> {
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
                    remark.gone()
                }
                ShopStatusConstants.OPEN,
                ShopStatusConstants.OVERDUE,
                ShopStatusConstants.ALLINPAY_APPLYING,
                ShopStatusConstants.ALLINPAY_APPROVED,
                ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED,
                ShopStatusConstants.ALLINPAY_ELECTSIGN_REFUSED,
                ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED,
                ShopStatusConstants.ALLINPAY_ELECTSIGN_ING,
                ShopStatusConstants.WEIXIN_AUTHEN_APPLYING,
                ShopStatusConstants.FINAL_OPEN -> {
                    if (fromMain != true && ActivityUtils.getTopActivity() !is MainActivity) {
                        versionUpdateDialog?.dismiss()
                        activity?.finish()
                        ActivityUtils.startActivity(MainActivity::class.java)
                        return
                    }
                    llHeader.visibility = View.VISIBLE
                    llHeader.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primary
                        )
                    )
                    llMainShopOpen.visibility = View.VISIBLE
                    llMainShopOther.visibility = View.GONE
                    ivMainMessage.visibility = View.VISIBLE

                    // ?????????
                    authLayout.gone()
                    shopAuthHint.gone()
                    shopStatusLayout.gone()

                    if (loginInfo.shopStatus == ShopStatusConstants.OPEN ||
                        loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_APPLYING
                    ) {
                        // ???????????????????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "???????????????"
                        shopAuthHint.gone()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_APPROVED) {
                        // ?????????????????????????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "????????????,?????????????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_ING) {
                        // ???????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "??????????????????????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED) {
                        // ??????????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "??????????????????????????????????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED) {
                        // ???????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "??????????????????,?????????????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED) {
                        // ???????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "??????????????????,?????????????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.WEIXIN_AUTHEN_APPLYING) {
                        // ???????????????
                        authLayout.visiable()
                        shopAuthStatus.text = "??????????????????,?????????????????????"
                        shopAuthHint.visiable()
                        //DialogUtils.showImageDialog(activity!!, R.mipmap.wechat_account, "???????????????????????????????????????????????????????????????????????????????????????????????????");
                    } else if (loginInfo.shopStatus == ShopStatusConstants.OVERDUE) {
                        // ??????
                        authLayout.visiable()
                        shopAuthStatus.text = "???????????????,???????????????"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.FINAL_OPEN) {
                        // ?????????????????????????????????
                        shopStatusLayout.visiable()
                        mPresenter?.getWaringNumber()
                        switchStatusBtn.setCheckedNoEvent(loginInfo.openStatus ?: false)
                    }
                    //                    tvMainShopName.text=loginInfo?.nickname
                    initOpeningShopView()
                    remark.gone()
                }

                ShopStatusConstants.CLOSED -> {
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
                    remark.gone()
                }
                ShopStatusConstants.REFUSED,
                ShopStatusConstants.ALLINPAY_REFUSED -> {
                    //??????????????????????????????????????????
                    tvMainShopName.text = "?????????????????????"
                    tvMainShopNext.text = "????????????"

                    if (!shopStatus?.remark.isNullOrEmpty()) {
                        remark.visiable()
                        remark.text = shopStatus?.remark
                    }
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
//                    tvMainShopReason.text = loginInfo.statusReason
//                    tvMainShopReason.visiable()
                }
            }
        }
    }


    override fun onMainInfoSuccess(bean: MainInfo?) {

    }

    override fun onMainDataSuccess(bean: MainInfoVo?, status: ShopStatus?) {

        startTime = bean?.start_time
        endTime = bean?.end_time

        hidePageLoading()
        shopStatus = status
        mainInfo = bean
        smartRefreshLayout.finishRefresh()
        initShopStatus(UserManager.getLoginInfo())
    }

    private fun initOpeningShopView() {
        if (mainInfo == null) {
            return
        }
        setShop()

        switchStatusBtn.setOnCheckedChangeListener { _, isChecked ->
            mPresenter?.editShopStatus(if (isChecked) 1 else 0)
        }
        onShopStatusEdited()
        // ??????????????????
        authLayout.setOnClickListener {
            if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED) {
                // ????????????
                pop?.apply {
                    setList(listOf("????????????????????????????????????????????????", "??????????????????????????????????????????"))
                    setTitle("??????")
                    showPopupWindow()
                }

            } else if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_APPROVED
                || loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_ING
                || loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_REFUSED
            ) {
                // ????????????
                ActivityUtils.startActivity(ElectricSignActivity::class.java)
            } else if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED
                || loginInfo?.shopStatus == ShopStatusConstants.WEIXIN_AUTHEN_APPLYING
            ) {
                // ????????????
                ActivityUtils.startActivity(ShopWeChatApproveActivity::class.java)
            } else if (loginInfo?.shopStatus == ShopStatusConstants.OVERDUE) {
                // ????????????
                ActivityUtils.startActivity(ApplyVipActivity::class.java)
            }
        }

        // ?????????
        // ????????????
        tvTodayUnTakeOrderCount.text = String.format("%s", mainInfo?.waitAcceptNum)
        // ????????????
        tvTodayTakenOrderCount.text = String.format("%s", mainInfo?.alreadyAcceptNum)
        // ?????????
        tvTodayDistributeOrderCount.text = String.format("%s", mainInfo?.waitRogNum)

        // ?????????
        tvTodayRefund.text = String.format("%s", mainInfo?.refundNum)
        // ?????????
        tvTodayFinish.text = String.format("%s", mainInfo?.completeNum)
        // ????????????
        tvTodayInvalid.text = String.format("%s", mainInfo?.cancelNum)

        //?????????
        tvTodaySales.text = String.format("%s", mainInfo?.tradeAmount)
        // ????????????
        tvTodayNewUser.text = String.format("%s", mainInfo?.newMemberNum)
        // ?????????
        tvTodayOrderCount.text = String.format("%s", mainInfo?.allNum)

        // ????????????
        tvManagerSetting.setOnClickListener {
            ActivityUtils.startActivity(OperationSettingActivity::class.java)
        }
        // ????????????
        tvGoodsManager.setOnClickListener {
            if (shopStatus?.templateId ?: 0 <= 0 && shopStatus?.haveCategory == false) {
                ToastUtils.showLong("???????????????????????????????????????????????????????????????????????????????????????????????????")
                return@setOnClickListener
            } else if (shopStatus?.templateId ?: 0 <= 0) {
                ToastUtils.showLong("????????????????????????????????????????????????????????????????????????????????????")
                return@setOnClickListener
            } else if (shopStatus?.haveCategory == false) {
                ToastUtils.showLong("??????????????????????????????????????????????????????????????????????????????")
                return@setOnClickListener
            } else if (loginInfo?.have_opentime == false) {
                ToastUtils.showLong("??????????????????????????????????????????????????????????????????????????????")
                return@setOnClickListener
            } else if (loginInfo?.have_shoplogo == false) {
                ToastUtils.showLong("??????????????????logo??????????????????????????????????????????????????????")
                return@setOnClickListener
            } else if (loginInfo?.have_mobile == false) {
                ToastUtils.showLong("??????????????????????????????????????????????????????????????????????????????")
                return@setOnClickListener
            }
            GoodsListActivity.openActivity(requireContext())
        }
        // ????????????
        tvCategoryManager.setOnClickListener {
            GoodsCategoryActivity.openActivity(requireContext())
        }
        // ????????????
        tvMenuManager.setOnClickListener {
            ActivityUtils.startActivity(MenuGoodsManagerActivity::class.java)
//             ActivityUtils.startActivity(GoodsPublishActivity::class.java)
//             GoodsPublishActivity.openActivity(context!!, "");
        }
        tvPopularize.setOnClickListener {
            ActivityUtils.startActivity(PopularizeActivity::class.java)
        }
        // ????????????
        tvSaleSetting.setOnClickListener {
            ActivityUtils.startActivity(SalesMarketingActivity::class.java)
        }
        // ????????????
        tvWalletAccount.setOnClickListener {
            ActivityUtils.startActivity(MyWalletActivity::class.java)
        }
        // ????????????
        tvUserManager.setOnClickListener {
            UserManagerActivity.allUser(requireContext())
        }
        // ????????????
        tvDataAnalysis.setOnClickListener {
            ActivityUtils.startActivity(StatsActivity::class.java)
        }
        // ????????????
        tvComeSoon.setOnClickListener {
            ActivityUtils.startActivity(SalesActivityGoodsWarning::class.java)
        }

        // ????????????
        // ????????????
        storeDataOfTodayTv.singleClick {
            model.setTime(startTime, endTime, 5)
            EventBus.getDefault().post(TabChangeEvent(4))
        }
        // ?????????
        salesDataOfTodayTv.singleClick {
            //ActivityUtils.startActivity(StatsActivity::class.java)
        }
        // ????????????
        userDataOfTodayTv.singleClick {
            UserManagerActivity.newUser(requireContext())
        }


        // ????????????
        llTodayUnTakeOrderCount.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(0))
        }
        // ????????????
        llMainWaitTakeGoods.setOnClickListener {
            //????????????????????????
            EventBus.getDefault().post(TabChangeEvent(1, "ACCEPT"))
        }
        // ?????????
        llMainWaitSendGoods.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(1, "SHIPPED"))
        }
        // ?????????
        llMainWaitRefund.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(2))
        }
        // ?????????
        layoutTodayFinish.setOnClickListener {
            //??????ViewModel????????????
            model.setTime(startTime, endTime, 3)
            EventBus.getDefault().post(TabChangeEvent(3))
        }
        // ??????
        layoutTodayInvalid.setOnClickListener {
            model.setTime(startTime, endTime, 4)
            EventBus.getDefault()
                .post(TabChangeEvent(4))
        }
        tvHelpDoc.setOnClickListener {
            ActivityUtils.startActivity(HelpDocActivity::class.java)
        }
        //????????????
        tvScan.setOnClickListener {
            ActivityUtils.startActivity(GoodsScanActivity::class.java)
        }
    }

    // ??????/??????????????????????????????
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (WebCameraUtil.isPermissionGranted(this)) {
            callBack.call()
        }
    }

    var callBack = CallBack {
        if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED) {
            // ????????????
            toApplySupplement()
        }
    }

    fun toApplySupplement() {
        ActivityUtils.startActivity(ApplySupplementActivity::class.java)
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
            requireActivity(), "????????????", bean.upgradeContent ?: "", null,
            {
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
        switchStatusBtn.isChecked = UserManager.getLoginInfo()?.openStatus == true
        shopStatusTv.text = if (switchStatusBtn.isChecked) "?????????" else "?????????"
    }

    override fun applyVIP(message: String) {
        DialogUtils.showDialog(requireActivity(), "????????????", message, "??????", "??????", null,
            {
                mPresenter?.searchData()
            })
    }

    override fun applyVI2(my: My?, identity: IdentityVo?) {
        ApplyVipActivity.openActivity(requireActivity(), my, identity)
    }

    @SuppressLint("SetTextI18n")
    override fun onWarningNumber(data: Int) {
        when {
            data > 99 -> {
                tvComeSoon_number.visibility = View.VISIBLE
                tvComeSoon_number.text = "99+"
            }
            data > 0 -> {
                tvComeSoon_number.visibility = View.VISIBLE
                tvComeSoon_number.text = data.toString()
            }
            else -> {
                tvComeSoon_number.visibility = View.GONE
            }
        }
        hideDialogLoading()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshShopStatus(event: ApplyShopInfoEvent) {
        refreshPageData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLogo(event: LoginInfo) {
        event.apply {
            setShop()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun vipApplied(event: ApplyVipEvent?) {
        event?.apply {
            refreshPageData()
        }
    }

    fun setShop() {
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let {
            tvMainShopName.text = loginInfo.shopName
            if (!TextUtils.isEmpty(loginInfo.shopLogo)) {
                GlideUtils.setImageUrl(civMainShopHead, loginInfo.shopLogo)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshPageData()
    }

    private fun refreshPageData() {
        mPresenter?.requestMainInfoData()
        if (loginInfo?.shopStatus == ShopStatusConstants.FINAL_OPEN) {
            mPresenter?.getWaringNumber()
        }
    }
}