package com.lingmiao.shop.business.main

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.presenter.ApplyShopCategoryPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopCategoryPresenterImpl
import com.james.common.base.BaseActivity
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import kotlinx.android.synthetic.main.main_activity_apply_shop_category.*
import org.greenrobot.eventbus.EventBus

/**
 *   首页-提交资料-经营类目
 */
class ApplyShopCategoryActivity : BaseActivity<ApplyShopCategoryPresenter>(),
    ApplyShopCategoryPresenter.View {

    private val tempList = mutableListOf<CategoryVO>()
    private val initSelectedList = mutableListOf<String>()

    private var mSelectedPosition = -1;
    private val adapter = object :
        BaseQuickAdapter<CategoryVO, BaseViewHolder>(R.layout.main_adapter_shop_category) {
        override fun convert(helper: BaseViewHolder, item: CategoryVO) {
            helper.setText(R.id.tvShopCategoryName, item.name)
            val ivShopCategorySelect= helper.getView<ImageView>(R.id.ivShopCategorySelect)
            val viShopCategoryDivide= helper.getView<View>(R.id.viShopCategoryDivide)

            // 多选用：item.selected
            // 单选
            ivShopCategorySelect.isSelected = mSelectedPosition == helper?.adapterPosition;
            viShopCategoryDivide.visibility = if(helper.layoutPosition==tempList.size-1) View.GONE else View.VISIBLE
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_category
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("选择经营类目")

        val goodsManagementCategory = intent.getStringExtra("goodsManagementCategory")
        goodsManagementCategory?.let {
            val idStringList = it.split(",")
            for (idString in idStringList) {
                idString?.apply {
                    initSelectedList.add(idString)
                }
            }
        }
        showPageLoading()
        mPresenter.requestApplyShopCategoryData()
        rvShopCategoryList.layoutManager = LinearLayoutManager(this)


        rvShopCategoryList.adapter = adapter

        adapter.setOnItemClickListener { adapter, view, position ->
            mSelectedPosition = position;
            adapter.notifyDataSetChanged();
        }

        tvShopCategoryNext.setOnClickListener {
            val tempList = adapter.data as List<CategoryVO>
            if(tempList.isEmpty()){
                showToast("获取类目数据失败")
                return@setOnClickListener
            }
            if(mSelectedPosition < 0 || mSelectedPosition > adapter.data.size) {
                showToast("请至少选择1个类目")
                return@setOnClickListener
            }
            // 单选
            val item = tempList[mSelectedPosition];
            EventBus.getDefault().post(item)
            finish()
        }
    }


    override fun createPresenter(): ApplyShopCategoryPresenter {
        return ApplyShopCategoryPresenterImpl(this, this)
    }

    override fun onApplyShopCategorySuccess(list:List<CategoryVO>) {
        hidePageLoading()
        tempList.clear()
        tempList.addAll(list)
        if(initSelectedList.size>0){
            tempList.forEachIndexed { index, item ->
                if(initSelectedList.contains(item.categoryId)){
                    item.isChecked = true
                    mSelectedPosition = index;
                }
            }
        }
        adapter.setNewData(list)
    }

    override fun onApplyShopCategoryError(code: Int) {
        hidePageLoading()
    }

}