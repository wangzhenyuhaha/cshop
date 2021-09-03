package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsDoubleItemPop
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.impl.PopCategoryPreImpl
import com.lingmiao.shop.business.main.bean.CategoryItem
import com.lingmiao.shop.business.main.presenter.ReplenishInfoPresenter
import kotlinx.coroutines.launch

class ReplenishInfoPresenterImpl(private var view: ReplenishInfoPresenter.View) :
    BasePreImpl(view), ReplenishInfoPresenter {

    //两个列表的PopWindow
    private var mTwoItemPop: AbsDoubleItemPop<CategoryItem>? = null

    private var l1Data: CategoryItem? = null

    private var l2Data: CategoryItem? = null

    lateinit var mL1Adapter: DefaultItemAdapter<CategoryItem>
    lateinit var mL2Adapter: DefaultItemAdapter<CategoryItem>

    override fun showCategoryPop(
        context: Context,
        callback: (CategoryItem?, CategoryItem?) -> Unit
    ) {
        mL1Adapter = DefaultItemAdapter()
        mL2Adapter = DefaultItemAdapter()
        mTwoItemPop = object : AbsDoubleItemPop<CategoryItem>(context) {

            override fun getFirstAdapter() = mL1Adapter

            override fun getSecondAdapter() = mL2Adapter

            ///CategoryItem.getWorkTimeList()
            //为第二个RecyclerView赋值
            override fun getData2(data1: CategoryItem) =
                CategoryItem.getWorkTimeList(data1, CategoryItem.getWorkTimeList())

        }.apply {
            lv1Callback = {
                l1Data = it
                mL1Adapter.setSelectedItem(it.itemValue)
            }
            lv2Callback = {
                l2Data = it
                callback.invoke(l1Data, it)
            }
        }
        mTwoItemPop?.setPopTitle("请选择主营类目")
        //为第一个RecyclerView赋值

        searchCategory()
        search2Category()
        mTwoItemPop?.showPopupWindow()
    }

    override fun onDestroy() {
        mTwoItemPop?.dismiss()
        mTwoItemPop = null
        super.onDestroy()
    }

    override fun searchCategory() {
        mCoroutine.launch {
            val resp = GoodsRepository.loadCategory()
            if (resp.isSuccess) {
                val temp = mutableListOf<CategoryItem>()

                for (i in resp.data) {
                    val data = CategoryItem()
                    data.goodsManagementCategory = i.categoryId
                    data.categoryNames = i.name
                    temp.add(data)
                }

                mTwoItemPop?.setLv1Data(temp)
//            						view.onApplyShopCategorySuccess(resp.data)
            }
        }
    }

    override fun search2Category() {

        //666为例子
        mCoroutine.launch {
            val resp = GoodsRepository.loadCategory2(670)
           // Log.d("WZYAAA", resp.data.size.toString() )
            if (resp.isSuccess)
            {

                Log.d("WZYAAA", "哈哈哈" )
                Log.d("WZYAAA", resp.data[0].mcc_name )
//                for (i in resp.data)
//                {
//                    Log.d("WZYAAA", i. )
//                }
            }else{
                Log.d("WZYAAA", "失败了" )
            }
        }
    }


}