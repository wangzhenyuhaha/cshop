package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.SpecSettingParams
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import razerdp.basepopup.BasePopupWindow


/**
 * Author : Elson
 * Date   : 2020/8/3
 * Desc   : 规格设置页批量设置
 */
class BatchSettingPop(context: Context, var callback: ((SpecSettingParams) -> Unit)?) : BasePopupWindow(context) {

    private lateinit var priceEdt: EditText
    private lateinit var quantityEdt: EditText
    private lateinit var weightEdt: EditText
    private lateinit var marketPriceEdt: EditText
    private lateinit var costPriceEdt: EditText
    private lateinit var goodsNoEdt: EditText
    private lateinit var goodsSKUEdt: EditText

    private lateinit var batchSkuLine : View;
    private lateinit var batchNoLine : View;
    private lateinit var batchWeightLine : View

    private lateinit var batchSkuLayout : LinearLayout;
    private lateinit var batchNoLayout : LinearLayout;
    private lateinit var batchWeightLayout : LinearLayout

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_batch_setting)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        priceEdt = contentView.findViewById(R.id.priceEdt)
        quantityEdt = contentView.findViewById(R.id.quantityEdt)
        weightEdt = contentView.findViewById(R.id.weightEdt)
        marketPriceEdt = contentView.findViewById(R.id.marketPriceEdt)
        costPriceEdt = contentView.findViewById(R.id.costPriceEdt)
        goodsNoEdt = contentView.findViewById(R.id.goodsNoEdt)
        goodsSKUEdt = contentView.findViewById(R.id.goodsSKUEdt)

        batchSkuLine = contentView.findViewById<View>(R.id.batchSkuLine);
        batchNoLine = contentView.findViewById<View>(R.id.batchNoLine);
        batchWeightLine = contentView.findViewById<View>(R.id.batchWeightLine);
        batchSkuLayout = contentView.findViewById<LinearLayout>(R.id.batchSkuLayout);
        batchNoLayout = contentView.findViewById<LinearLayout>(R.id.batchNoLayout);
        batchWeightLayout = contentView.findViewById<LinearLayout>(R.id.batchWeightLayout);


        contentView.findViewById<ImageView>(R.id.closeIv).setOnClickListener {
            clearEdt()
            dismiss()
        }
        contentView.findViewById<TextView>(R.id.confirmTv).setOnClickListener {
            setResult()
            dismiss()
        }
    }

    fun show(isVirtual: Boolean) {
        // 虚拟商品，此处不显示
        if(isVirtual) {
            batchSkuLine.gone();
            batchNoLine.gone();
            batchWeightLine.gone();

            batchSkuLayout.gone();
            batchNoLayout.gone();
            batchWeightLayout.gone();
        }
        showPopupWindow();
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

    private fun setResult() {
        val params = SpecSettingParams().apply {
            price = priceEdt.getViewText()
            quantity = quantityEdt.getViewText()
            weight = weightEdt.getViewText()
            marketPrice = marketPriceEdt.getViewText()
            costPrice = costPriceEdt.getViewText()
            goodsNo = goodsNoEdt.getViewText()
            goodsSKU = goodsSKUEdt.getViewText()
        }
        clearEdt()
        callback?.invoke(params)
    }

    private fun clearEdt() {
        priceEdt.setText("")
        quantityEdt.setText("")
        weightEdt.setText("")
        marketPriceEdt.setText("")
        costPriceEdt.setText("")
        goodsNoEdt.setText("")
        goodsSKUEdt.setText("")
    }
}