package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.presenter.GoodsInfoEditPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsInfoUpdatePreImpl
import kotlinx.android.synthetic.main.goods_activity_goods_info_update.*

/**
Create Date : 2021/6/163:00 PM
Auther      : Fox
Desc        :
 **/
class GoodsInfoUpdateActivity : BaseActivity<GoodsInfoEditPre>(), GoodsInfoEditPre.PublicView {

    //当前一级分类的ID
    private var cId: String = ""

    companion object {

        //id 分类ID   result返回码
        fun add(context: Context, id: String?, result: Int) {
            if (context is Activity) {
                val intent = Intent(context, GoodsInfoUpdateActivity::class.java)
                intent.putExtra("pId", id)
                context.startActivityForResult(intent, result)
            }
        }

        fun update(context: Context, id: String?, result: Int) {
            if (context is Activity) {
                val intent = Intent(context, GoodsInfoUpdateActivity::class.java)
                intent.putExtra("pId", id)
                context.startActivityForResult(intent, result)
            }
        }
    }

    override fun initBundles() {
        cId = intent.getStringExtra("pId") ?: ""
    }

    override fun useLightMode() = false

    override fun createPresenter() = GoodsInfoUpdatePreImpl(this, this)

    override fun getLayoutId() = R.layout.goods_activity_goods_info_update

    override fun initView() {

        mToolBarDelegate.setMidTitle(getString(R.string.goods_info_add_title))

        //选择同步的分类
        tvCategoriesName.singleClick {
            //一级分类的ID
            mPresenter?.itemClick(cId)
        }

        infoSubmitTv.singleClick {
            if (tvGoodsInfoName?.getViewText() == null) {

                return@singleClick
            }
            mPresenter?.addInfo(cId, tvGoodsInfoName?.getViewText())
        }

        //useless
        if (cId.isEmpty()) {
            mPresenter?.loadCateList(cId)
        }
    }

    override fun onAddInfo(vo: GoodsParamVo) {
        setResult(Activity.RESULT_OK)
    }

    override fun onUpdated(vo: GoodsParamVo) {

    }

    override fun onSetCategories(list: List<CategoryVO>?) {
        tvCategoriesName.text = list?.joinToString(separator = ",") { it.name }
    }
}