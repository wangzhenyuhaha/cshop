package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.*
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.business.goods.presenter.GoodsSpecPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsSpecPreImpl
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

    private var categoryId: String? = null

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
        return GoodsSpecPreImpl(this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("规格信息")

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            mPresenter?.loadList(categoryId);
        }

        specNameTv.setText("规格信息");

        addSpecTv.singleClick {
            DialogUtils.showInputDialog(
                this, "规格信息", "", "请输入具体信息，不同信息用\",\"分隔",
                "取消", "保存", null
            ) {
                val its = it?.split(",");
                its.forEachIndexed { index, s ->
                    if(s?.isNotEmpty()) {
                        mPresenter.add(categoryId!!, s)
                    }
                }
            }
        }

        specFlowLayout.apply {
            clickDeleteCallback = {
//                if(it?.isNotEmpty() == true) {
//                    mPresenter?.delete(it!!);
//                }
            }
        }
        mPresenter?.loadList(categoryId);
    }

    override fun onLoaded(list: List<GoodsSpecVo>) {
        smartRefreshLayout.finishRefresh()
        specFlowLayout.removeAllViews();
        specFlowLayout.addSpecValues(categoryId, list);
    }

    override fun onAdded(vo : GoodsSpecVo) {
        val list = mutableListOf<GoodsSpecVo>();
        list.add(vo)
        specFlowLayout.addSpecValues(categoryId, list);
    }

    override fun onDeleted(id: String) {

    }
}