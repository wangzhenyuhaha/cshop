package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Intent
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.presenter.OperationSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.OperationSettingPreImpl
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.databinding.ActivityOperationSettingBinding
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*

class OperationSettingActivity :
    BaseVBActivity<ActivityOperationSettingBinding, OperationSettingPresenter>(),
    OperationSettingPresenter.View {

    private var shopReq: ApplyShopInfo = ApplyShopInfo()

    //商家配送模板
    private var mLocalItem: FreightVoItem? = null

    //骑手配送模板
    private var mRiderItem: FreightVoItem? = null

    override fun useLightMode() = false

    override fun createPresenter() = OperationSettingPreImpl(this, this)

    override fun getViewBinding() = ActivityOperationSettingBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("运营管理")

        //加载数据
        mPresenter?.loadShopInfo()

    }

    override fun onLoadedShopInfo(bean: ApplyShopInfo) {

        //在本地保存是否自动打印
        UserManager.setAutoPrint(bean.autoPrint == 1)
        shopReq = bean

        // 营业时间
        mBinding.tvShopOperateTime.setOnClickListener {
            mPresenter?.showWorkTimePop(it)
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
            //未接订单自动取消时间
            if (mBinding.tvShopManageNumber.getViewText().isEmpty()) {
                showToast("请输入未接订单自动取消时间")
                return@setOnClickListener
            }
            val cancelOrderTime = mBinding.tvShopManageNumber.text?.toString()?.toInt() ?: 0

            //输入手机号码
            if (mBinding.linkTelEt.text.toString().isEmpty()) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }
            //未完待续:增加对营业时间未设置时的判断
            //自动接单
            shopReq.autoAccept = if (mBinding.autoOrderSb.isChecked) 1 else 0
            // 自动打印
            shopReq.autoPrint = if (mBinding.autoPrinterSb.isChecked) 1 else 0
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

        mBinding.layoutShop.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cb_model_shop) {
                if ((mLocalItem == null || mLocalItem?.id == null) && mBinding.cbModelShop.isChecked) {
                    showToast("请先设置商家配送模板")
                }
            } else if (checkedId == R.id.cb_model_rider) {
                if ((mRiderItem == null || mRiderItem?.id == null) && mBinding.cbModelRider.isChecked) {
                    showToast("请先设置骑手配送模板")
                }
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
                mBinding.autoOrderSb.isChecked = autoAccept == 1
                //是否自动打印
                mBinding.autoPrinterSb.isChecked = autoPrint == 1
                //未接订单自动取消时间
                mBinding.tvShopManageNumber.setText(cancelOrderDay?.toString())
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
                mBinding.cbModelShop.gone()
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
        UserManager.setAutoPrint(autoPrinterSb.isChecked)
    }
}