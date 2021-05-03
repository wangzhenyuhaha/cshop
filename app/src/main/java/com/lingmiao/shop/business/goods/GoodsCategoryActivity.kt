package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.amap.api.mapcore.util.it
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.CategoryAdapter
import com.lingmiao.shop.business.goods.adapter.UsedMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import com.lingmiao.shop.business.goods.presenter.impl.CategoryEditPreImpl
import kotlinx.android.synthetic.main.goods_activity_goods_catetory.*

/**
Create Date : 2021/4/263:31 PM
Auther      : Fox
Desc        :
 **/
class GoodsCategoryActivity : BaseLoadMoreActivity<CategoryVO, CategoryEditPre>(), CategoryEditPre.PubView {

    private var list : MutableList<CategoryVO>? = mutableListOf();

    companion object {

        fun openActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, GoodsCategoryActivity::class.java)
//                intent.putExtra(KEY_DESC, content)
                context.startActivity(intent)
            }
        }
    }
    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_catetory;
    }

    override fun initAdapter(): BaseQuickAdapter<CategoryVO, BaseViewHolder> {
        return CategoryAdapter(list).apply {
            setOnItemClickListener { adapter, view, position ->

//                GroupManagerLv2Activity.openActivity(
//                    this@GroupManagerLv1Activity,
//                    mAdapter.getItem(position)?.shopCatId
//                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.menuAddGoodsIv -> {
                        val item = getItem(position) as CategoryVO;
                        showAddDialog(item.categoryId!!.toInt());
                    }
                    R.id.groupEditTv -> {
//                        GroupManagerEditActivity.openActivity(
//                            activity!!,
//                            ShopGroupVO.LEVEL_1, null, mAdapter.getItem(position)
//                        )
                    }
                    R.id.groupDeleteTv -> {
                        mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
                    }
                }
            }
//            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean

//                if(menuBottom.visibility != View.VISIBLE) {
//                    menuBottom.visibility = View.VISIBLE;
//                }
//                setBatchEditModel(true);
//                return@OnItemLongClickListener true;
//            }
        }
    }

    override fun createPresenter(): CategoryEditPre {
        return CategoryEditPreImpl(this, this);
    }

    override fun onDeleteGroupSuccess(position: Int) {

    }

    override fun addSuccess(vo: CategoryVO) {
        var item = CategoryVO();
        item.showLevel = 0;
//        item.name = it;
        list?.add(item);
        mAdapter?.replaceData(list!!);
    }

    fun showAddDialog(pId : Int) {
        DialogUtils.showInputDialog(context!!, "分类名称", "", "请输入","取消", "保存",null) {

            mPresenter?.add(pId, it);

        }
    }
    override fun initOthers() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_category_manager_title))
        // 禁用上拉加载、下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))


        categoryAddTv.setOnClickListener {
            showAddDialog(0);
        }
        categoryCancelTv.setOnClickListener {
            categoryBottom.visibility = View.GONE;
            categoryAllCheckCb.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as UsedMenuAdapter)?.setBatchEditModel(false);
        }
        categoryAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        }
        categoryDeleteTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }

        mSmartRefreshLayout?.setEnableLoadMore(false);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadLv1GoodsGroup();
    }
}