package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.SalesAdapter
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesSettingPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SalesSettingPreImpl
import com.lingmiao.shop.widget.EmptyView

/**
Create Date : 2021/3/101:08 AM
Auther      : Fox
Desc        :
 **/
class SalesSettingActivity : BaseLoadMoreActivity<SalesVo,ISalesSettingPresenter>(), ISalesSettingPresenter.PubView {

    companion object {
        val REQUEST_EDIT = 199;
        val REQUEST_GOODS = 299;
    }

    override fun initAdapter(): BaseQuickAdapter<SalesVo, BaseViewHolder> {

        return SalesAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                val item = mAdapter.getItem(position)
                when(view.id) {
                    R.id.tvSalesDelete -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该活动吗？",
                            "取消", "确定删除",
                            null, View.OnClickListener {
                            mPresenter?.delete(item?.id, position);
                        })
                    }
                    R.id.tvSalesEnd -> {
                        DialogUtils.showDialog(context as Activity,
                            "结束提示", "结束后不可恢复，确定要结束该活动吗？",
                            "取消", "确定结束",
                            null, View.OnClickListener {

                            })
                    }
                    R.id.tvSalesView -> {
                        SalesActivityEditActivity.edit(context as Activity, item, REQUEST_EDIT)
                    }
                    R.id.salesGoodsTv -> {
                        SalesGoodsActivity.edit(context as Activity, item, REQUEST_GOODS)
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_EDIT) {
                mLoadMoreDelegate?.refresh()
            } else if(requestCode == REQUEST_GOODS) {


            }
        }
    }

    override fun createPresenter(): ISalesSettingPresenter {
        return SalesSettingPreImpl(this, this);
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_setting_title))
        mToolBarDelegate.setRightText("新增", View.OnClickListener {
            SalesActivityEditActivity.open(context as Activity, REQUEST_EDIT)
        });
    }

    override fun autoRefresh(): Boolean {
        return true;
    }

    override fun onDelete(position: Int) {
        if(position < mAdapter.data.size) {
            mAdapter.remove(position);
        }
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}