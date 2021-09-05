package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsDoubleItemPop
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.impl.PopCategoryPreImpl
import com.lingmiao.shop.business.main.bean.CategoryItem
import com.lingmiao.shop.business.main.pop.DoubleItemPop
import com.lingmiao.shop.business.main.presenter.ReplenishInfoPresenter
import kotlinx.coroutines.launch
import java.lang.Exception

class ReplenishInfoPresenterImpl(private var view: ReplenishInfoPresenter.View) :
    BasePreImpl(view), ReplenishInfoPresenter {

    //两个列表的PopWindow
    private var mTwoItemPop: DoubleItemPop<CategoryItem>? = null

    //当前选中的RecyclerView1的Item
    private var l1Data: CategoryItem? = null

    lateinit var mL1Adapter: DefaultItemAdapter<CategoryItem>
    lateinit var mL2Adapter: DefaultItemAdapter<CategoryItem>

    override fun showCategoryPop(
        context: Context,
        callback: (CategoryItem?, CategoryItem?) -> Unit
    ) {
        mL1Adapter = DefaultItemAdapter()
        mL2Adapter = DefaultItemAdapter()
        mTwoItemPop = object : DoubleItemPop<CategoryItem>(context) {
            override fun getFirstAdapter() = mL1Adapter
            override fun getSecondAdapter() = mL2Adapter
        }.apply {
            //点击第一个RecyclerView的item
            lv1Callback = {
                l1Data = it
                mL1Adapter.setSelectedItem(it.id)
                search2Category(it.id)
            }
            lv2Callback = {
                //获取到相关信息
                callback.invoke(l1Data, it)
            }
        }
        mTwoItemPop?.setPopTitle("请选择主营类目")
        //为第一个RecyclerView赋值
        searchCategory()
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
                    data.id = i.categoryId
                    data.name = i.name
                    temp.add(data)
                }
                mTwoItemPop?.setLv1Data(temp)
            }
        }
    }

    override fun search2Category(id: String) {

        mCoroutine.launch {
            try {
                val resp = GoodsRepository.loadCategory2(id.toInt())
                if (resp.isSuccess) {
                    val temp = mutableListOf<CategoryItem>()
                    for (i in resp.data) {
                        val data = CategoryItem()
                        data.id = i.mccid.toString()
                        data.name = i.mcc_name
                        temp.add(data)
                    }
                    mTwoItemPop?.setLv2Data(temp)
                }
            } catch (e: Exception) {

            }
        }
    }

}