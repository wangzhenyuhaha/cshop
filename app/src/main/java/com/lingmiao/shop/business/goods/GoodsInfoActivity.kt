package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.fragment.GoodsInfoDialogFragment
import com.lingmiao.shop.business.goods.presenter.GoodsInfoPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsInfoPreImpl
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.goods_activity_goods_info.*


/**
 * Desc   : 商品信息
 */
class GoodsInfoActivity : BaseActivity<GoodsInfoPre>(),
    GoodsInfoPre.PublicView {

    private var categoryId: String? = null

    //0 一级分类   1  二级分类
    private var level: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
    }

    companion object {

        //分类ID
        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"

        //0 一级分类   1  二级分类
        const val KEY_CATEGORY_LEVEL = "KEY_CATEGORY_LEVEL"

        const val SPEC_REQUEST_CODE = 1000

        fun openActivity(context: Context, requestCode: Int, item: CategoryVO?) {
            if (context is Activity) {
                val intent = Intent(context, GoodsInfoActivity::class.java)
                intent.putExtra(KEY_CATEGORY_ID, item?.categoryId)
                intent.putExtra(KEY_CATEGORY_LEVEL, item?.showLevel)
                context.startActivityForResult(intent, requestCode)
            }
        }

    }

    override fun useLightMode() = false

    override fun getLayoutId() = R.layout.goods_activity_goods_info

    override fun initBundles() {
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        level = intent.getIntExtra(KEY_CATEGORY_LEVEL, 1)
    }

    override fun createPresenter() = GoodsInfoPreImpl(this)

    override fun initView() {
        mToolBarDelegate.setMidTitle("商品信息")

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        smartRefreshLayout.setOnRefreshListener {
            mPresenter?.loadInfoList(categoryId)
        }

        infoNameTv.text = "商品信息"

        //显示图片
        goods_activity_goods_info_pictureButton.singleClick {
            GoodsInfoDialogFragment.nesInstance()
                .show(supportFragmentManager, "goodsInfoActivityDialog")
        }

        addInfoTv.singleClick {
            if (level == 1) {
                //二级分类
                DialogUtils.showInputDialog(
                    this, "商品信息", "", "请输入具体信息，不同信息用\",\"分隔",
                    "取消", "保存", null
                ) {
                    val its = it.split(",")
                    its.forEachIndexed { _, s ->
                        if (s.isNotEmpty()) {
                            mPresenter.addInfo(categoryId!!, s)
                        }
                    }
                }
            } else {
                GoodsInfoUpdateActivity.add(this, categoryId, SPEC_REQUEST_CODE)
            }
        }
        infoFlowLayout.apply {
            clickDeleteCallback = {
                if (it?.isNotEmpty() == true) {
                    mPresenter?.deleteInfo(it)
                }
            }
        }
        mPresenter?.loadInfoList(categoryId)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SPEC_REQUEST_CODE) {
            mPresenter?.loadInfoList(categoryId)
        }
    }

    override fun onLoadInfoListSuccess(list: List<GoodsParamVo>) {
        smartRefreshLayout.finishRefresh()
        infoFlowLayout.removeAllViews()
        infoFlowLayout.addSpecValues(categoryId, list)
    }

    override fun onAddInfo(vo: GoodsParamVo) {
        val list = mutableListOf<GoodsParamVo>()
        list.add(vo)
        infoFlowLayout.addSpecValues(categoryId, list)
    }

    override fun onInfoDeleted(id: String) {

    }


}