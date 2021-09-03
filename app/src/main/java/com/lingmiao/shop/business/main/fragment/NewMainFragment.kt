package com.lingmiao.shop.business.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.james.common.base.BaseFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.show
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.GoodsCategoryActivity
import com.lingmiao.shop.business.goods.GoodsListActivity
import com.lingmiao.shop.business.goods.MenuManagerActivity
import com.lingmiao.shop.business.login.LoginActivity
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.main.*
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.MainPresenter
import com.lingmiao.shop.business.main.presenter.impl.MainPresenterImpl
import com.lingmiao.shop.business.me.ApplyVipActivity
import com.lingmiao.shop.business.me.HelpDocActivity
import com.lingmiao.shop.business.me.ManagerSettingActivity
import com.lingmiao.shop.business.me.ShopWeChatApproveActivity
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.me.event.ApplyVipEvent
import com.lingmiao.shop.business.sales.SalesActivityGoodsWarning
import com.lingmiao.shop.business.sales.SalesSettingActivity
import com.lingmiao.shop.business.sales.StatsActivity
import com.lingmiao.shop.business.sales.UserManagerActivity
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_new_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("UseRequireInsteadOfGet")
class NewMainFragment : BaseFragment<MainPresenter>(), MainPresenter.View {

    // 店铺状态及信息
    private var shopStatus: ShopStatus? = null

    // 首页订单统计数据
    private var mainInfo: MainInfoVo? = null
    private var loginInfo: LoginInfo? = null
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_new_main
    }

    override fun createPresenter(): MainPresenter {
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
            refreshPageData()
        }
        ivMainMessage.setOnClickListener {
            if (UserManager.isLogin()) {
                ActivityUtils.startActivity(MessageCenterActivity::class.java)
            }
        }

        readApplyShop.singleClick {
            DialogUtils.showDialog(activity!!, R.mipmap.apply_shop_hint)
        }
        promoCode.text =
            if (UserManager.getPromCode().isEmpty()) "请输入推广码（选填）" else UserManager.getPromCode()

        promoCode.setOnClickListener {
            DialogUtils.showInputDialogEmpty(
                requireActivity(),
                "推广码",
                "",
                "请输入",
                UserManager.getPromCode(),
                "取消",
                "保存",
                null
            ) {
                if (it.isNotEmpty()) {
                    promoCode.text = it
                    UserManager.setPromCode(it)
                } else {
                    promoCode.text = "请输入推广码（选填）"
                    UserManager.setPromCode("")
                }
            }
        }

        readWXPay.singleClick {
            DialogUtils.showDialogSameBig(activity!!, R.mipmap.wechat_pay)
        }

        tvMainShopNext.setOnClickListener {
            when (tvMainShopNext.text.toString()) {
                "申请开店 >>", "重新提交" -> {
                    ActivityUtils.startActivity(ApplyShopHintActivity::class.java)
                }
                "联系客服" -> {
                    OtherUtils.goToDialApp(activity, IConstant.SERVICE_PHONE)
                }
                "立即购买" -> {
                    ActivityUtils.startActivity(ApplyVipActivity::class.java)
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
        this.loginInfo = loginInfo
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
                ShopStatusConstants.UN_APPLY -> {
                    tvMainShopHint.text = "你还有没有开通店铺"
                    tvMainShopNext.text = "申请开店 >>"
                }
                ShopStatusConstants.APPLY,
                ShopStatusConstants.APPLYING -> {
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
                    llHeader.setBackgroundColor(ContextCompat.getColor(context!!, R.color.primary))
                    llMainShopOpen.visibility = View.VISIBLE
                    llMainShopOther.visibility = View.GONE
                    ivMainMessage.visibility = View.VISIBLE

                    // 认证中
                    authLayout.gone()
                    shopAuthHint.gone()
                    shopStatusLayout.gone()

                    if (loginInfo.shopStatus == ShopStatusConstants.OPEN ||
                        loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_APPLYING
                    ) {
                        // 开启中，显示审核中
                        authLayout.visiable()
                        shopAuthStatus.text = "店铺审核中"
                        shopAuthHint.gone()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_APPROVED) {
                        // 进见通过，显示电子签约
                        authLayout.visiable()
                        shopAuthStatus.text = "审核通过,请进行电子签约"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_ING) {
                        // 签约审核中
                        authLayout.visiable();
                        shopAuthStatus.text = "签约已提交，等待通过";
                        shopAuthHint.visiable();
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED) {
                        // 进见补充资料
                        authLayout.visiable()
                        shopAuthStatus.text = "店铺信息不完善，请补充资料"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED) {
                        // 微信认证中
                        authLayout.visiable()
                        shopAuthStatus.text = "店铺签约成功,请进行商户认证"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED) {
                        // 微信认证中
                        authLayout.visiable()
                        shopAuthStatus.text = "店铺签约成功,请进行商户认证"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.WEIXIN_AUTHEN_APPLYING) {
                        // 微信认证中
                        authLayout.visiable()
                        shopAuthStatus.text = "店铺签约成功,请进行商户认证"
                        shopAuthHint.visiable()
                        //DialogUtils.showImageDialog(activity!!, R.mipmap.wechat_account, "店铺签约成功，请使用微信扫码进行商户认证，认证成功后才进行正常结算");
                    } else if (loginInfo.shopStatus == ShopStatusConstants.OVERDUE) {
                        // 过期
                        authLayout.visiable()
                        shopAuthStatus.text = "会员已过期,请继续购买"
                        shopAuthHint.visiable()
                    } else if (loginInfo.shopStatus == ShopStatusConstants.FINAL_OPEN) {
                        // 最终状态，显示店铺状态
                        shopStatusLayout.visiable()
                        mPresenter?.getWaringNumber()
                        switchStatusBtn.setCheckedNoEvent(loginInfo?.openStatus ?: false)
                    }
                    //                    tvMainShopName.text=loginInfo?.nickname
                    initOpeningShopView()
                }

                ShopStatusConstants.CLOSED -> {
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
                ShopStatusConstants.REFUSED,
                ShopStatusConstants.ALLINPAY_REFUSED -> {
                    //店铺审核未通过，重新提交页面
                    tvMainShopName.text = "店铺审核未通过"
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

    override fun onMainDataSuccess(bean: MainInfoVo?, status: ShopStatus?) {
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

        switchStatusBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter?.editShopStatus(if (isChecked) 1 else 0)
        }
        onShopStatusEdited()
        // 申请状态点击
        authLayout.setOnClickListener {
            if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_COMPLIANCE_REFUSED) {
                // 进见补填
                ActivityUtils.startActivity(ApplySupplementActivity::class.java)
            } else if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_APPROVED
                || loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_ING
                || loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_REFUSED
            ) {
                // 电子签约
                ActivityUtils.startActivity(ElectricSignActivity::class.java)
            } else if (loginInfo?.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED
                || loginInfo?.shopStatus == ShopStatusConstants.WEIXIN_AUTHEN_APPLYING
            ) {
                // 微信认证
                ActivityUtils.startActivity(ShopWeChatApproveActivity::class.java)
            } else if (loginInfo?.shopStatus == ShopStatusConstants.OVERDUE) {
                // 过期续费
                ActivityUtils.startActivity(ApplyVipActivity::class.java)
            }
        }

        // 总信息
        // 未接订单
        tvTodayUnTakeOrderCount.text = String.format("%s", mainInfo?.waitAcceptNum)
        // 已接订单
        tvTodayTakenOrderCount.text = String.format("%s", mainInfo?.alreadyAcceptNum)
        // 待配送
        tvTodayDistributeOrderCount.text = String.format("%s", mainInfo?.waitRogNum)

        // 待退款
        tvTodayRefund.text = String.format("%s", mainInfo?.refundNum)
        // 已完成
        tvTodayFinish.text = String.format("%s", mainInfo?.completeNum)
        // 失效订单
        tvTodayInvalid.text = String.format("%s", mainInfo?.cancelNum)

        //销售额
        tvTodaySales.text = String.format("%s", mainInfo?.tradeAmount)
        // 新用户数
        tvTodayNewUser.text = String.format("%s", mainInfo?.newMemberNum)
        // 订单量
        tvTodayOrderCount.text = String.format("%s", mainInfo?.allNum)

        // 管理设置
        tvManagerSetting.setOnClickListener {
            ActivityUtils.startActivity(ManagerSettingActivity::class.java)
        }
        // 商品管理
        tvGoodsManager.setOnClickListener {
            if (shopStatus?.templateId ?: 0 <= 0 && shopStatus?.haveCategory == false) {
                ToastUtils.showLong("请先完善店铺管理设置与分类设置，否则店铺不能进行正常营业与商品上传")
            } else if (shopStatus?.templateId ?: 0 <= 0) {
                ToastUtils.showLong("请先完善店铺管理设置，否则店铺不能进行正常营业与商品上传")
            } else if (shopStatus?.haveCategory == false) {
                ToastUtils.showLong("请先完善分类设置，否则店铺不能进行正常营业与商品上传")
            }
            GoodsListActivity.openActivity(context!!)
        }
        // 商品分类
        tvCategoryManager.setOnClickListener {
            GoodsCategoryActivity.openActivity(context!!)
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
            ActivityUtils.startActivity(MyWalletActivity::class.java)
        }
        // 用户管理
        tvUserManager.setOnClickListener {
            UserManagerActivity.allUser(context!!)
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
            UserManagerActivity.newUser(context!!)
        }


        // 未接订单
        llTodayUnTakeOrderCount.setOnClickListener {
            EventBus.getDefault().post(TabChangeEvent(0))
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
        tvHelpDoc.setOnClickListener {
            ActivityUtils.startActivity(HelpDocActivity::class.java)
        }
    }

    override fun onMainInfoError(code: Int) {
        smartRefreshLayout.finishRefresh()
        hidePageLoading()
    }

    override fun onAccountSettingSuccess(bean: AccountSetting) {
        accountSetting = bean
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
        shopStatusTv.text = if (switchStatusBtn.isChecked) "开店中" else "休息中"
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
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshShopStatus(event: ApplyShopInfoEvent) {
        refreshPageData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateLogo(event: LoginInfo) {
        event?.apply {
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