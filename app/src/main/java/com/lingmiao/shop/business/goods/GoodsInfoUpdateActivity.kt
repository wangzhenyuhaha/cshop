package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
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

    private var cId : String =""

    companion object {
        fun add(context: Context, id : String?, result : Int) {
            if(context is Activity) {
                val intent = Intent(context, GoodsInfoUpdateActivity::class.java);
                intent.putExtra("pId", id);
                context.startActivityForResult(intent, result)
            }
        }

        fun update(context: Context, id : String?, result : Int) {
            if(context is Activity) {
                val intent = Intent(context, GoodsInfoUpdateActivity::class.java);
                intent.putExtra("pId", id);
                context.startActivityForResult(intent, result)
            }
        }
    }


    override fun initBundles() {
        cId = intent.getStringExtra("pId")
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun createPresenter(): GoodsInfoEditPre {
        return GoodsInfoUpdatePreImpl(this, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_info_update
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_info_add_title))


        tvCategoriesName.singleClick {
            mPresenter?.itemClick(cId);
        }
        infoSubmitTv.singleClick {
            if(tvGoodsInfoName?.getViewText() == null) {

                return@singleClick;
            }
            mPresenter?.addInfo(cId, tvGoodsInfoName?.getViewText())
        }
        if(cId?.isNullOrEmpty()) {
            mPresenter?.loadCateList(cId);
        }
    }

    override fun onAddInfo(vo: GoodsParamVo) {
        setResult(Activity.RESULT_OK);
    }

    override fun onUpdated(vo: GoodsParamVo) {

    }

    override fun onSetCategories(list: List<CategoryVO>?) {
        tvCategoriesName.setText(list?.map { it?.name }?.joinToString(separator = ","));
    }
}