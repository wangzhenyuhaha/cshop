package com.lingmiao.shop.business.tuan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class OrderGoodsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val inflater: LayoutInflater = LayoutInflater.from(context);


    var mAdapter: OrderGoodsAdapter? = null;

    var recyclerView: RecyclerView? = null;

    var deleteListener: ((GoodsVo, Int) -> Unit)? = null;


    fun addItems(goodsList: MutableList<GoodsVo>?) {
        removeAllViews();
        if (goodsList?.isNullOrEmpty() == true) {
            return;
        }
        createItem(goodsList);
    }

    fun getList(): List<GoodsVo> {
        return mAdapter?.data ?: arrayListOf();
    }


    fun getSize(): Int {
        return mAdapter?.data?.size ?: 0;
    }

    private fun createItem(goodsSpecList: MutableList<GoodsVo>) {
        val view = inflater.inflate(R.layout.tuan_view_order_goods, this, false);
        recyclerView = view.findViewById(R.id.tuanOrderGoodsRv)
        mAdapter = OrderGoodsAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as GoodsSpecVo;

            }
        }
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
}