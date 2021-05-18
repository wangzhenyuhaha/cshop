package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsInfoAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsInfoVo
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
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
    private lateinit var mInfoList : MutableList<GoodsParamVo>;

    private var categoryId: String? = null

    companion object {
        const val KEY_DESC = "KEY_DESC"
        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"

        fun openActivity(context: Context, requestCode: Int, content: String?, goodsVO: GoodsVOWrapper) {
            if (context is Activity) {
                val intent = Intent(context, GoodsDetailActivity::class.java)
                intent.putExtra(KEY_DESC, content)
                intent.putExtra(KEY_CATEGORY_ID, goodsVO.categoryId);
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    override fun initBundles() {
        categoryId = intent.getStringExtra(SpecSettingActivity.KEY_CATEGORY_ID)

    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_detail;
    }

    override fun createPresenter(): GoodsDetailPre {
        return GoodsDetailPreImpl(this, this);
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_detail_title))


        mInfoList = arrayListOf();
//        mInfoList.add(GoodsParamVo());

        mInfoAdapter = GoodsInfoAdapter(mInfoList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as GoodsParamVo;
                if(view?.id == R.id.infoDeleteTv && position != 0) {
                    mInfoList.remove(item);
                    mInfoAdapter.replaceData(mInfoList);
                }
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as GoodsParamVo;
            }
        }
        goodsInfoRv.apply {
            layoutManager = initLayoutManager()
            adapter = mInfoAdapter;
        }
       // mInfoAdapter.replaceData(mInfoList);


        goodsInfoAddTv.setOnClickListener {
//            mInfoList.add(GoodsParamVo());
//            mInfoAdapter.replaceData(mInfoList);
        }

        goodsContentHintTv.singleClick {
            DialogUtils.showDialog(context!!, R.mipmap.ic_goods_detail);
        }
        goodsInfoHintTv.singleClick {
            DialogUtils.showDialog(context!!, R.mipmap.ic_goods_info);
        }

        goodsImageHintTv.singleClick {
            DialogUtils.showDialog(context!!, R.mipmap.ic_goods_image);
        }
        goodsDetailImageRv.setCountLimit(1, 20)

        goodsDetailSubmitTv.singleClick {
            setResult(Activity.RESULT_OK);
            finish();
        }
        mPresenter?.loadInfoByCId(categoryId!!)
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
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

    override fun onLoadedInfoList(list: List<GoodsParamVo>) {
        mInfoList.clear();
        mInfoList.addAll(list);
        mInfoAdapter.replaceData(mInfoList);
    }

}