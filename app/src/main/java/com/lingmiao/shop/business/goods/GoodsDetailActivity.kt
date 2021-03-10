package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsInfoAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsInfoVo
import com.lingmiao.shop.business.goods.presenter.GoodsDetailPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsDetailPreImpl
import kotlinx.android.synthetic.main.goods_activity_goods_detail.*

/**
Create Date : 2021/3/611:10 AM
Auther      : Fox
Desc        :
 **/
class GoodsDetailActivity : BaseActivity<GoodsDetailPre>(), GoodsDetailPre.View {

    private lateinit var mInfoAdapter : GoodsInfoAdapter;
    private lateinit var mInfoList : MutableList<GoodsInfoVo>;

    companion object {
        const val KEY_DESC = "KEY_DESC"

        fun openActivity(context: Context, requestCode: Int, content: String?) {
            if (context is Activity) {
                val intent = Intent(context, GoodsDetailActivity::class.java)
                intent.putExtra(KEY_DESC, content)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    override fun createPresenter(): GoodsDetailPre {
        return GoodsDetailPreImpl(this, this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_detail_title))


        mInfoList = arrayListOf();
        mInfoList.add(GoodsInfoVo());

        mInfoAdapter = GoodsInfoAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as GoodsInfoVo;
                if(view?.id == R.id.infoDeleteTv && position != 0) {
                    mInfoList.remove(item);
                    mInfoAdapter.replaceData(mInfoList);
                }
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as GoodsInfoVo;
            }
        }
        goodsInfoRv.apply {
            layoutManager = initLayoutManager()
            adapter = mInfoAdapter;
        }
        mInfoAdapter.replaceData(mInfoList);


        goodsInfoAddTv.setOnClickListener {
            mInfoList.add(GoodsInfoVo());
            mInfoAdapter.replaceData(mInfoList);
        }

        goodsDetailImageRv.setCountLimit(1, 20)

    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_detail;
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if(KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}