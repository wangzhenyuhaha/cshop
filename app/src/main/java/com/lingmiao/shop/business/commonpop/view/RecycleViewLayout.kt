package com.lingmiao.shop.business.commonpop.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.adapter.GoodsSpecItemAdapter
import com.lingmiao.shop.business.tuan.adapter.OrderGoodsAdapter
import com.lingmiao.shop.business.tuan.bean.GoodsSpecVo
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.james.common.utils.exts.isNotEmpty

/**
Create Date : 2021/1/161:50 PM
Auther      : Fox
Desc        :
 **/
abstract class RecycleViewLayout<T, Adapter : BaseQuickAdapter<T, BaseViewHolder>> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val inflater: LayoutInflater = LayoutInflater.from(context);

    var mAdapter: Adapter? = null;

    var recyclerView: RecyclerView? = null;

    var deleteListener: ((T, Int) -> Unit)? = null;

    abstract fun getAdapter() : Adapter;

    private fun createItem(goodsSpecList: MutableList<T>) {
        val view = inflater.inflate(R.layout.tuan_view_order_goods, this, false);
        recyclerView = view.findViewById(R.id.tuanOrderGoodsRv)
        mAdapter = getAdapter();
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        // 回显数据
        if (goodsSpecList.isNotEmpty()) {
            mAdapter?.addData(goodsSpecList!!)
        }
        this.addView(view);
    }

    fun removeItem(position: Int) {
        mAdapter?.remove(position)
    }

    fun addItems(goodsList: MutableList<T>?) {
        removeAllViews();
        if (goodsList?.isNullOrEmpty() == true) {
            return;
        }
        createItem(goodsList);
    }

    fun getList(): List<T> {
        return mAdapter?.data ?: arrayListOf();
    }


    fun getSize(): Int {
        return mAdapter?.data?.size ?: 0;
    }

}