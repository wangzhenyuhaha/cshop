package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.DeliveryTempAdapter
import com.lingmiao.shop.business.goods.api.bean.DeliveryTempVO
import com.lingmiao.shop.business.goods.presenter.DeliveryTempPre
import com.lingmiao.shop.business.goods.presenter.impl.DeliveryTempPreImpl
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import kotlinx.android.synthetic.main.goods_activity_delivery_temp.*
import kotlinx.android.synthetic.main.goods_activity_group_manager.*
import kotlinx.android.synthetic.main.goods_activity_group_manager.toolbarView

/**
 * Author : Elson
 * Date   : 2020/8/7
 * Desc   : 配送模板
 */
class DeliveryTempActivity : BaseLoadMoreActivity<DeliveryTempVO, DeliveryTempPre>(),
    DeliveryTempPre.TempView {

    companion object {
        const val KEY_TEMP_TYPE = "KEY_TEMP_TYPE"
        const val KEY_TEMP_ENTITY = "KEY_TEMP_ENTITY"

        fun openActivity(context: Context, requestCode: Int, tempType: String?) {
            if (context is Activity) {
                val intent = Intent(context, DeliveryTempActivity::class.java)
                intent.putExtra(KEY_TEMP_TYPE, tempType)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var tempType: String? = null
    private var localAdapter: DeliveryTempAdapter? = null

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_delivery_temp
    }

    override fun initBundles() {
        tempType = intent.getStringExtra(KEY_TEMP_TYPE)
    }

    override fun createPresenter(): DeliveryTempPre {
        return DeliveryTempPreImpl(this)
    }

    override fun initOthers() {
        // 禁用上拉加载、下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        toolbarView.setTitleContent("选择模板")
        submitTv.setOnClickListener {
            saveTempAndFinishPage()
        }
    }

    private fun saveTempAndFinishPage() {
        val tempVO = localAdapter?.selectTempVO
        if (tempVO == null) {
            showToast("请先选择模板")
            return
        }
        val intent = Intent()
        intent.putExtra(KEY_TEMP_ENTITY, tempVO)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadTempList(tempType)
    }

    override fun initAdapter(): BaseQuickAdapter<DeliveryTempVO, BaseViewHolder> {
        localAdapter = DeliveryTempAdapter().apply {
            setOnItemClickListener { _, _, position ->
                localAdapter?.selectTemplate(position)
            }
        }
        return localAdapter!!
    }

}