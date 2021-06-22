package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.*
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.goods.adapter.GoodsSpecAdapter
import com.lingmiao.shop.business.goods.presenter.GoodsSpecPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsSpecPreImpl
import com.lingmiao.shop.util.initAdapter
import com.lingmiao.shop.widget.EmptyView
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.goods_activity_goods_spec.*
import kotlinx.android.synthetic.main.goods_activity_goods_spec.smartRefreshLayout

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格设置页
 */
class GoodsSpecActivity : BaseActivity<GoodsSpecPre>(),
    GoodsSpecPre.PublicView {

    private lateinit var mAdapter : GoodsSpecAdapter;

    private lateinit var categoryId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
    }

    companion object {

        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"

        const val SPEC_REQUEST_CODE = 1000

        fun openActivity(context: Context, requestCode: Int, id : String) {
            if (context is Activity) {
                val intent = Intent(context, GoodsSpecActivity::class.java)
                intent.putExtra(KEY_CATEGORY_ID, id)
                context.startActivityForResult(intent, requestCode)
            }
        }

    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_spec
    }

    override fun initBundles() {
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
    }

    override fun createPresenter(): GoodsSpecPre {
        return GoodsSpecPreImpl(this, this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("规格信息")
        mToolBarDelegate.setRightText("新增", ContextCompat.getColor(context,R.color.white), View.OnClickListener {
            mPresenter?.showAddPop(categoryId);
        });

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            mPresenter?.loadList(categoryId);
        }

        mAdapter = GoodsSpecAdapter().apply {
            setOnItemClickListener { adapter, view, position ->

            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = mAdapter.data[position] as SpecKeyVO
                when(view.id) {
                    R.id.deleteSpecTv -> {
                        if(item?.specId?.isNotEmpty() == true) {
                            DialogUtils.showDialog(context as Activity,
                                "删除提示", "删除后不可恢复，确定要删除吗？",
                                "取消", "确定删除",
                                null, View.OnClickListener {
                                    mPresenter?.delete(item?.specId!!);
                                })
                        }

                    }
                    R.id.addSpecValueTv -> {
                        DialogUtils.showInputDialog(
                            context as Activity, "规格值", "", "请输入具体规格值，不同值用\",\"分隔",
                            "取消", "保存", null
                        ) { _str ->
                            if(_str?.isNotEmpty()) {
                                mPresenter?.addSpecValue(item?.specId!!, _str);
                            }
                        }
                    }
                }
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
        rvSpec.initAdapter(mAdapter)

        mPresenter?.loadList(categoryId);
    }

    override fun onLoaded(list: List<SpecKeyVO>) {
        smartRefreshLayout.finishRefresh()
        mAdapter.replaceData(list);
    }

    override fun onAdded(vo : GoodsSpecVo) {
        mPresenter?.loadList(categoryId);
    }

    override fun onDeleted(id: String) {
        mPresenter?.loadList(categoryId);
    }

    override fun onAddSpecValueSuccess(specKeyId: String, data: List<SpecValueVO>?) {
        mAdapter?.data?.forEachIndexed { index, item ->
            if(item.specId == specKeyId && data?.isNotEmpty() == true) {
                item.valueList?.addAll(data)
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}