package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.CheckBox
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.DeliveryTempActivity.Companion.KEY_TEMP_ENTITY
import com.lingmiao.shop.business.goods.api.bean.DeliveryTempVO
import com.lingmiao.shop.business.goods.api.request.DeliveryRequest
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.utils.exts.checkBoolean
import com.james.common.utils.exts.checkNotBlack
import com.james.common.utils.exts.getViewText
import kotlinx.android.synthetic.main.goods_activity_delivery_setting.*

/**
 * Author : Elson
 * Date   : 2020/8/7
 * Desc   : 发货方式
 */
class DeliverySettingActivity : BaseActivity<BasePresenter>() {

    companion object {
        const val KEY_Delivery = "KEY_Delivery"

        const val REQUEST_GLOBAL = 100
        const val REQUEST_LOCAL = 101

        const val DELIVERY_TYPE_GLOBAL = "KUAIDI"
        const val DELIVERY_TYPE_LOCAL = "TONGCHENG"

        fun openActivity(context: Context, requestCode: Int, deliveryVO: DeliveryRequest?) {
            if (context is Activity) {
                val intent = Intent(context, DeliverySettingActivity::class.java)
                intent.putExtra(KEY_Delivery, deliveryVO)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var deliveryRequest: DeliveryRequest? = null

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_delivery_setting
    }

    override fun initBundles() {
        deliveryRequest = intent.getSerializableExtra(KEY_Delivery) as DeliveryRequest?
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initView() {
        if (deliveryRequest == null) {
            finish()
            return
        }

        window.decorView.background = null
        toolbarView.setTitleContent("发货方式")

        updateParentCheckbox(globalDeliveryChk, globalCostChk, globalTemplateChk)
        updateChildCheckbox(globalDeliveryChk, globalCostChk, globalTemplateChk)
        updateChildCheckbox(globalDeliveryChk, globalTemplateChk, globalCostChk)
        globalTemplateTv.setOnClickListener {
            goDeliveryTempPage(globalTemplateChk.isChecked, REQUEST_GLOBAL, DELIVERY_TYPE_GLOBAL)
        }

        updateParentCheckbox(localDeliveryChk, localCostChk, localTemplateChk)
        updateChildCheckbox(localDeliveryChk, localCostChk, localTemplateChk)
        updateChildCheckbox(localDeliveryChk, localTemplateChk, localCostChk)
        localTemplateTv.setOnClickListener {
            goDeliveryTempPage(localTemplateChk.isChecked, REQUEST_LOCAL, DELIVERY_TYPE_LOCAL)
        }

        restoreUI(deliveryRequest!!)

        submitTv.setOnClickListener {
            getDeliveryRequest()?.apply {
                if (checkRequest(this)) {
                    return@apply
                }
                val intent = Intent()
                intent.putExtra(KEY_Delivery, this)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun checkRequest(request: DeliveryRequest): Boolean {
        try {
            checkBoolean(request.isSelectDeliveryWay()) { "请选择发货方式" }
            if (request.isCheckGlobal()) {
                checkBoolean(request.isCheckGlobalOptions()) { "请输入一种物流发货方式" }
                if (request.isCheckGlobalCostPriceWay()) {
                    checkNotBlack(request.freightUnifiedPrice) { "请输入物流发货统一运费" }
                } else {
                    checkNotBlack(request.templateName) { "请选择物流发货运费模板" }
                }
            }
            if (request.isCheckLocal()) {
                checkBoolean(request.isCheckLocalOptions()) { "请输入一种同城配送方式" }
                if (request.isCheckLocalCostPriceWay()) {
                    checkNotBlack(request.localFreightUnifiedPrice) { "请输入同城配送统一运费" }
                } else {
                    checkNotBlack(request.localTemplateName) { "请选择同城配送运费模板" }
                }
            }
            return false
        } catch (e: Exception) {
            showToast(e.message)
            return true
        }
    }

    private fun restoreUI(deliveryRequest: DeliveryRequest) {
        deliveryRequest.apply {
            globalDeliveryChk.isChecked = isCheckGlobal()
            globalCostChk.isChecked = isCheckGlobalCostPriceWay()
            globalTemplateChk.isChecked = isCheckGlobalTempPriceWay()
            globalCostEdt.setText(deliveryRequest.freightUnifiedPrice)

            localDeliveryChk.isChecked = isCheckLocal()
            localCostChk.isChecked = isCheckLocalCostPriceWay()
            localTemplateChk.isChecked = isCheckLocalTempPriceWay()
            localCostEdt.setText(deliveryRequest.localFreightUnifiedPrice)

            selfDeliveryChk.isChecked = isCheckSelfTake()
        }
    }

    private fun getDeliveryRequest(): DeliveryRequest? {
        return deliveryRequest?.apply {
            // 数据清除
            reset()
            // 物流发货
            isGlobal = getCheckedStatus(globalDeliveryChk.isChecked)
            if (globalCostChk.isChecked) {
                freightPricingWay = GoodsConfig.DELIVERY_PRICE_WAY_COST
                freightUnifiedPrice = globalCostEdt.getViewText()
            }
            if (globalTemplateChk.isChecked) {
                freightPricingWay = GoodsConfig.DELIVERY_PRICE_WAY_TEMP
                (globalTemplateTv.tag as? DeliveryTempVO)?.apply {
                    templateId = this.id
                    templateName = this.name
                }
            }

            // 同城发货
            isLocal = getCheckedStatus(localDeliveryChk.isChecked)
            if (localCostChk.isChecked) {
                localFreightPricingWay = GoodsConfig.DELIVERY_PRICE_WAY_COST
                localFreightUnifiedPrice = localCostEdt.getViewText()
            }
            if (localTemplateChk.isChecked) {
                localFreightPricingWay = GoodsConfig.DELIVERY_PRICE_WAY_TEMP
                (localTemplateTv.tag as? DeliveryTempVO)?.apply {
                    localTemplateId = this.id
                    localTemplateName = this.name
                }
            }

            // 货物自提
            isSelfTake = getCheckedStatus(selfDeliveryChk.isChecked)
        }
    }

    private fun updateParentCheckbox(parentChk: CheckBox, selectedChk: CheckBox, releaseChk: CheckBox) {
        parentChk.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                selectedChk.isChecked = false
                releaseChk.isChecked = false
            }
        }
    }

    private fun updateChildCheckbox(parentChk: CheckBox, selectedChk: CheckBox, releaseChk: CheckBox) {
        selectedChk.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!parentChk.isChecked) {
                showToast("请先勾选上一级发货方式")
                selectedChk.isChecked = false
                releaseChk.isChecked = false
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                releaseChk.isChecked = false
            }
        }
    }

    /**
     * 进入模板页面
     */
    private fun goDeliveryTempPage(isCheck: Boolean, requestCode: Int, type: String?) {
        if (!isCheck) {
            showToast("请先勾选运费模板")
            return
        }
        DeliveryTempActivity.openActivity(this, requestCode, type)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            REQUEST_GLOBAL -> {
                val tempVO = data.getSerializableExtra(KEY_TEMP_ENTITY) as? DeliveryTempVO
                globalTemplateTv.text = tempVO?.name
                globalTemplateTv.tag = tempVO
            }
            REQUEST_LOCAL -> {
                val tempVO = data.getSerializableExtra(KEY_TEMP_ENTITY) as? DeliveryTempVO
                localTemplateTv.text = tempVO?.name
                localTemplateTv.tag = tempVO
            }
        }
    }

}