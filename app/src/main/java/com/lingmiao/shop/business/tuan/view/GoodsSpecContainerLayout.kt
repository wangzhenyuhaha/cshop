package com.lingmiao.shop.business.tuan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.adapter.GoodsSpecItemAdapter
import com.lingmiao.shop.business.tuan.bean.GoodsSpecVo
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.james.common.utils.exts.isNotEmpty

class GoodsSpecContainerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val inflater: LayoutInflater = LayoutInflater.from(context);

    var mAdapter: GoodsSpecItemAdapter? = null;

    var recyclerView: RecyclerView? = null;

    var deleteListener: ((GoodsSpecVo, Int) -> Unit)? = null;

    fun addItems(goodsList: MutableList<GoodsSpecVo>) {
        removeAllViews();
        if (goodsList.isNullOrEmpty()) {
            return;
        }
        createItem(goodsList);
    }

    fun getList(): List<GoodsSpecVo> {
        return mAdapter?.data ?: arrayListOf();
    }


    fun getSize(): Int {
        return mAdapter?.data?.size ?: 0;
    }

    private fun createItem(goodsSpecList: MutableList<GoodsSpecVo>) {
        val view = inflater.inflate(R.layout.tuan_view_goods_spec, this, false);
        recyclerView = view.findViewById(R.id.rv_tuan_goods_spec_list)
        mAdapter = GoodsSpecItemAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as GoodsSpecVo;
                if (view.id == R.id.tv_tuan_goods_spec_option) {
//                    remove(position);
                    deleteListener?.invoke(bItem, position);
                }
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