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
import com.lingmiao.shop.business.goods.presenter.GoodsInfoPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsInfoPreImpl
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.goods_activity_goods_info.*
import kotlinx.android.synthetic.main.goods_activity_goods_info.smartRefreshLayout


/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格设置页
 */
class GoodsInfoActivity : BaseActivity<GoodsInfoPre>(),
    GoodsInfoPre.PublicView {

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
                val intent = Intent(context, GoodsInfoActivity::class.java)
                intent.putExtra(KEY_CATEGORY_ID, id)
                context.startActivityForResult(intent, requestCode)
            }
        }

    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_info
    }

    override fun initBundles() {
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
    }

    override fun createPresenter(): GoodsInfoPre {
        return GoodsInfoPreImpl(this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("商品信息")


        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            mPresenter?.loadInfoList(categoryId);
        }

        infoNameTv.setText("商品信息");

        addInfoTv.singleClick {
            DialogUtils.showInputDialog(
                this, "商品信息", "", "请输入具体信息，不同信息用\",\"分隔",
                "取消", "保存", null
            ) {
                val its = it?.split(",");
                its.forEachIndexed { index, s ->
                    if(s?.isNotEmpty()) {
                        mPresenter.addInfo(categoryId!!, s)
                    }
                }
            }
        }
        infoFlowLayout.apply {
            clickDeleteCallback = {
                if(it?.isNotEmpty() == true) {
                    mPresenter?.deleteInfo(it!!);
                }
            }
        }
        mPresenter?.loadInfoList(categoryId);

    }

    override fun onLoadInfoListSuccess(list: List<GoodsParamVo>) {
        smartRefreshLayout.finishRefresh()
        infoFlowLayout.removeAllViews();
        infoFlowLayout.addSpecValues(categoryId, list);
    }

    override fun onAddInfo(vo : GoodsParamVo) {
        val list = mutableListOf<GoodsParamVo>();
        list.add(vo)
        infoFlowLayout.addSpecValues(categoryId, list);
    }

    override fun onInfoDeleted(id: String) {

    }
}