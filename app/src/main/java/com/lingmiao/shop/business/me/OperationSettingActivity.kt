package com.lingmiao.shop.business.me

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.presenter.OperationSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.OperationSettingPreImpl
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.bean.TimeSettingVo
import com.lingmiao.shop.business.wallet.MyWalletActivity
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.databinding.ActivityOperationSettingBinding
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.android.synthetic.main.wallet_activity_my_wallet.*

class OperationSettingActivity :
    BaseVBActivity<ActivityOperationSettingBinding, OperationSettingPresenter>(),
    OperationSettingPresenter.View {

    private var shopReq: ApplyShopInfo = ApplyShopInfo()

    //商家配送模板
    private var mLocalItem: FreightVoItem? = null

    //骑手配送模板
    private var mRiderItem: FreightVoItem? = null

    //true不可见  false可见
    //接单设置可见行
    private val jiedanVisibility: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //订单打印可见性
    private val dingdandayingVisibility: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //取货方式可见行
    private val quhuofanshiVisibility: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //骑手备付金
    private var riderPrice: Double? = null

    override fun useLightMode() = false

    override fun createPresenter() = OperationSettingPreImpl(this, this)

    override fun getViewBinding() = ActivityOperationSettingBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("店铺管理")

        //加载数据
        mPresenter?.loadShopInfo()


        //加载骑手备付金余额
        mPresenter?.loadRiderMoneyInfo()


    }

    override fun onLoadedShopInfo(bean: ApplyShopInfo) {
        shopReq = bean

        // 营业时间
        mBinding.tvShopOperateTime.setOnClickListener {
            mPresenter?.showWorkTimePop(it)
        }

        //接单设置
        mBinding.more1.setOnClickListener {
            val temp = jiedanVisibility.value ?: true
            jiedanVisibility.value = !temp
        }
        mBinding.less1.setOnClickListener {
            val temp = jiedanVisibility.value ?: true
            jiedanVisibility.value = !temp
        }

        //关注微信公众号
        mBinding.toWeChatPublic.setOnClickListener {
            val api = WXAPIFactory.createWXAPI(context, IWXConstant.APP_ID)
            val req = WXLaunchMiniProgram.Req()
            req.userName = IWXConstant.APP_ORIGINAL_ID
            req.path = "pages/cshop-follow/cshop-follow"
            req.miniprogramType =
                if (IConstant.official) WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE else WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW
            api.sendReq(req)
        }

        //监听接单设置
        mBinding.jiedanxuanze.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.jiedanno) {
                //手动接单(只在展开的时候有效)
                if (mBinding.less1.visibility == View.VISIBLE) {
                    mBinding.jiedanshijian.visiable()
                } else {
                    mBinding.jiedanshijian.gone()
                }

            } else if (checkedId == R.id.jiedanyes) {
                //自动接单
                mBinding.jiedanshijian.gone()
            }
        }

        jiedanVisibility.observe(this) {
            if (it) {
                //不可见
                mBinding.more1.visiable()
                mBinding.less1.gone()
                mBinding.jiedanxuanze.gone()
                mBinding.jiedanshijian.gone()
            } else {
                //可见
                mBinding.more1.gone()
                mBinding.less1.visiable()
                mBinding.jiedanxuanze.visiable()
                if (mBinding.jiedanno.isChecked) {
                    mBinding.jiedanshijian.visiable()
                } else {
                    mBinding.jiedanshijian.gone()
                }

            }
        }


        //订单打印
        mBinding.more2.setOnClickListener {
            val temp = dingdandayingVisibility.value ?: true
            dingdandayingVisibility.value = !temp
        }
        mBinding.less2.setOnClickListener {
            val temp = dingdandayingVisibility.value ?: true
            dingdandayingVisibility.value = !temp
        }
        dingdandayingVisibility.observe(this) {
            if (it) {
                //不可见
                mBinding.more2.visiable()
                mBinding.less2.gone()
                mBinding.dindandaying.gone()
            } else {
                mBinding.more2.gone()
                mBinding.less2.visiable()
                mBinding.dindandaying.visiable()
            }
        }
        UserManager.setAutoPrint(bean.autoPrint == 1)

        //取货方式
        mBinding.more3.setOnClickListener {
            val temp = quhuofanshiVisibility.value ?: true
            quhuofanshiVisibility.value = !temp
        }
        mBinding.less3.setOnClickListener {
            val temp = quhuofanshiVisibility.value ?: true
            quhuofanshiVisibility.value = !temp
        }
        quhuofanshiVisibility.observe(this) {
            if (it) {
                //不可见
                mBinding.more3.visiable()
                mBinding.less3.gone()
                mBinding.daodianziti.gone()
                mBinding.rlShopManageDelivery.gone()
                mBinding.waimaipesong.gone()
                mBinding.qishouchaoshi.gone()
            } else {
                mBinding.more3.gone()
                mBinding.less3.visiable()
                mBinding.daodianziti.visiable()
                mBinding.rlShopManageDelivery.visiable()
                mBinding.waimaipesong.visiable()
                mBinding.qishouchaoshi.visiable()
                if (shopReq.shop_accept_carriage == 0) {
                    //隐藏棋手
                    mBinding.qishouchaoshi.gone()
                }
            }
        }

        //加载设置
        onLoadedShopSetting(shopReq)


        //配送设置
        mPresenter?.loadTemplate()

        //到店自提
        mBinding.takeSelf.setOnClickListener {
            mPresenter?.takeSelf(if (takeSelf.isChecked) 1 else 0)
        }

        //保存
        mBinding.tvShopOperateSubmit.setOnClickListener {
            //未接订单自动取消时间(只在手动接单时有效)
            if (mBinding.jiedanno.isChecked && mBinding.tvShopManageNumber.getViewText()
                    .isEmpty()
            ) {
                showToast("请输入未接订单自动取消时间")
                return@setOnClickListener
            }

            val cancelOrderTime =
                if (mBinding.jiedanno.isChecked) mBinding.tvShopManageNumber.text?.toString()
                    ?.toInt() ?: 0
                else 5

            //输入手机号码
            if (mBinding.linkTelEt.text.toString().isEmpty()) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }
            //未完待续:增加对营业时间未设置时的判断
            //自动接单
            shopReq.autoAccept = if (mBinding.jiedanyes.isChecked) 1 else 0
            // 自动打印
            shopReq.autoPrint = if (mBinding.dayingyes.isChecked) 1 else 0
            // 取消订单
            shopReq.cancelOrderTime = cancelOrderTime
            // 联系电话
            shopReq.companyPhone = mBinding.linkTelEt.text.toString()
            //配送设置
            shopReq.shopTemplateType = getTemplate()
            if (shopReq.shopLogo.isNullOrEmpty()) {
                showToast("请上传店铺LOGO")
                return@setOnClickListener
            }
            //如果选了骑手配送则需要填写骑手超时未接单系统提示时间
            if (mBinding.cbModelRider.isChecked) {
                if (mBinding.riderToShop.getViewText().isEmpty()) {
                    showToast("请填写骑手超时未接单系统提示时间")
                    return@setOnClickListener
                }
                if (mLocalItem == null || mLocalItem?.id == null || mLocalItem?.id?.length == 0) {
                    showToast("请先配置商家配送")
                    return@setOnClickListener
                }
                val setting = TimeSettingVo()
                setting.isAllowTransTemp = 0
                setting.transTempLimitTime = mBinding.riderToShop.getViewText().toInt()

                mPresenter?.updateModel(
                    mLocalItem?.id!!,
                    setting.isAllowTransTemp!!,
                    setting.transTempLimitTime!!
                )
            }
            //提交设置
            mPresenter?.setSetting(shopReq)
        }

        mBinding.shopSetting.singleClick {
            //跳转到店铺设置页面，返回时将数据带回
            val intent = Intent(this, ShopSettingActivity::class.java)
            intent.putExtra("item", shopReq)
            startActivityForResult(intent, 22)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            shopReq = data?.getSerializableExtra("item") as ApplyShopInfo
            if (!shopReq.shopLogo.isNullOrEmpty()) {
                mBinding.shopSetting.text = "已设置"
            } else {
                mBinding.shopSetting.text = "请设置"
            }
        }
    }

    override fun onLoadedTemplate(tcItem: FreightVoItem?, qsItem: FreightVoItem?) {
        this.mLocalItem = tcItem
        this.mRiderItem = qsItem

        //根据返回情况确定模板是否已配置
        //商家
        tcItem?.apply {
            mBinding.tvShopStatus.text = "已设置"
        }
        //骑手
        qsItem?.apply {
            mBinding.tvRiderStatus.text = "已设置"
        }

        qsItem?.rider_to_seller_time?.let { mBinding.riderToShop.setText(it.toString()) }

        mBinding.layoutShop.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cb_model_shop) {
                if ((mLocalItem == null || mLocalItem?.id == null) && mBinding.cbModelShop.isChecked) {
                    showToast("请先设置商家配送模板")
                }
            } else if (checkedId == R.id.cb_model_rider) {
                if ((mRiderItem == null || mRiderItem?.id == null) && mBinding.cbModelRider.isChecked) {
                    showToast("请先设置骑手配送模板")
                }
                //提示充钱
                if (riderPrice == null || riderPrice ?: 0.0 < 10.0)
                    DialogUtils.showDialog(
                        this,
                        "提示",
                        "需要充足的骑手费用扣款才能接单(可在配送费代扣金中充值)",
                        "确认",
                        "去充值",
                        {},
                        {
                            ActivityUtils.startActivity(MyWalletActivity::class.java)
                        })
            }
        }
        mBinding.tvShopStatus.singleClick {
            //shopReq.accept_carriage决定是否显示骑手配送
            DeliveryManagerActivity.shop(
                this,
                mRiderItem,
                shopReq.shop_accept_carriage
            )
        }
        mBinding.tvRiderStatus.singleClick {
            //默认使用骑手配送
            DeliveryManagerActivity.rider(this, mRiderItem)
        }
    }

    override fun onUpdateWorkTime(workTimeVo1: WorkTimeVo?, workTimeVo2: WorkTimeVo?) {
        shopReq.openStartTime = workTimeVo1?.itemName
        // 处理【第二天】文字，服务端不需要返回
        shopReq.openEndTime = workTimeVo2?.itemName?.replace("第二天", "")
        shopReq.openTimeType = workTimeVo2?.getFullDayType()
        setOperateTime()
    }

    // 处理显示【第二天】文字，服务端不保存,客户端计算
    private fun setOperateTime() {
        mBinding.tvShopOperateTime.text = String.format(
            "%s%s%s%s",
            shopReq.openStartTime ?: "",
            if (shopReq.openStartTime?.isEmpty() == true) "" else "-",
            if (WorkTimeVo.isSecondDay(
                    shopReq.openStartTime,
                    shopReq.openEndTime
                )
            ) "" else "第二天",
            shopReq.openEndTime ?: ""
        )
    }

    override fun onLoadedShopSetting(vo: ApplyShopInfo) {
        vo.apply {
            orderSetting?.apply {
                //自动接单
                if (autoAccept == 1) {
                    //自动接单
                    mBinding.jiedanyes.isChecked = true
                    mBinding.jiedanno.isChecked = false
                } else {
                    mBinding.jiedanyes.isChecked = false
                    mBinding.jiedanno.isChecked = true
                }
                //未接订单自动取消时间
                mBinding.tvShopManageNumber.setText(cancelOrderDay?.toString())
                //是否自动打印
                if (autoPrint == 1) {
                    //自动打印
                    mBinding.dayingyes.isChecked = true
                    mBinding.dayingno.isChecked = false
                } else {
                    mBinding.dayingyes.isChecked = false
                    mBinding.dayingno.isChecked = true
                }
            }
            //营业时间
            shopReq.openStartTime = vo.openStartTime
            shopReq.openEndTime = vo.openEndTime
            setOperateTime()

            //是否开启自提，1开启，0不开启
            mBinding.takeSelf.isChecked = is_self_take == 1

            //联系电话
            mBinding.linkTelEt.setText(companyPhone)

            //确定所选配送模板
            mBinding.cbModelRider.isChecked = shopTemplateType == FreightVoItem.TYPE_QISHOU
            mBinding.cbModelShop.isChecked = shopTemplateType == FreightVoItem.TYPE_LOCAL

            //后台确定是否隐藏骑手
            if (shopReq.shop_accept_carriage == 0) {
                //隐藏棋手
                mBinding.cbModelRider.gone()
                mBinding.tvRiderStatus.gone()
                mBinding.qishouchaoshi.gone()
            }

            //店铺设置
            if (!shopReq.shopLogo.isNullOrEmpty()) {
                mBinding.shopSetting.text = "已设置"
            } else {
                mBinding.shopSetting.text = "请设置"
            }
        }

    }

    fun getTemplate(): String {
        return if (cb_model_shop.isChecked) {
            FreightVoItem.TYPE_LOCAL
        } else if (cb_model_rider.isChecked) {
            FreightVoItem.TYPE_QISHOU
        } else {
            ""
        }
    }

    override fun setTakeSelfFailed() {
        takeSelf.isChecked = !takeSelf.isChecked
    }

    override fun onSetSetting() {
        UserManager.setAutoPrint(mBinding.dayingyes.isChecked)

    }

    override fun loadRiderMoneySuccess(info: AccountVo?) {
        riderPrice = info?.balanceAmount
    }
}