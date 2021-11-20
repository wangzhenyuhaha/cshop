package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsInfoAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.presenter.GoodsDetailPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsDetailPreImpl
import com.lingmiao.shop.util.initAdapter
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

    private var mContent : String? = null

    private var mImages : String? = null

    companion object {
        const val KEY_DESC = "KEY_ITEM_DESC"
        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        const val KEY_ITEM = "GOODS_INFO_ITEM";
        const val KEY_IMAGES = "GOODS_IMAGES_ITEM";
        fun openActivity(context: Context, requestCode: Int, goodsVO: GoodsVOWrapper) {
            if (context is Activity) {
                val intent = Intent(context, GoodsDetailActivity::class.java)
                intent.putExtra(KEY_DESC, goodsVO.metaDescription)
                intent.putExtra(KEY_CATEGORY_ID, goodsVO.categoryId);
                intent.putExtra(KEY_IMAGES, goodsVO.intro)
                intent.putExtra(KEY_ITEM, goodsVO.goodsParamsList as? ArrayList<GoodsParamVo>)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    override fun initBundles() {
        mImages = intent.getStringExtra(KEY_IMAGES);
        mContent = intent.getStringExtra(KEY_DESC);
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        mInfoList = intent.getSerializableExtra(KEY_ITEM) as? MutableList<GoodsParamVo>?: mutableListOf();
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

        //商品描述
        etFeedbackContent.setText(mContent);

        mInfoAdapter = GoodsInfoAdapter(mInfoList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as? GoodsParamVo;
                if(view?.id == R.id.infoDeleteTv && position != 0) {
                    mInfoList.remove(item);
                    mInfoAdapter.replaceData(mInfoList);
                }
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as GoodsParamVo;
            }
        }
        goodsInfoRv.initAdapter(mInfoAdapter);
       // mInfoAdapter.replaceData(mInfoList);

        goodsInfoAddTv.setOnClickListener {
            DialogUtils.showInputDialog(
                this, "商品信息", "", "请输入具体信息",
                "取消", "保存", null
            ) {
                if(it?.isNotEmpty()) {
                    mPresenter.addInfo(categoryId!!, it)
                }
            }
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
            val images = goodsDetailImageRv.getSelectPhotos()?.map{it -> it.original}.joinToString(separator = ",");
            val intent = Intent()
            intent.putExtra(KEY_IMAGES, images);
            intent.putExtra(KEY_DESC, etFeedbackContent.text.toString());
            intent.putExtra(KEY_ITEM, mInfoList.filter { it?.paramValue != null } as? ArrayList<GoodsParamVo>);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        mPresenter?.loadInfoByCId(categoryId!!)

        mImages?.split(",")?.forEachIndexed { index, s ->
            if(s.isNotEmpty()) {
                goodsDetailImageRv.addData(GoodsGalleryVO("", s, ""));
            }

        }

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
        list.forEach { it ->
            mInfoList.forEach { i ->
                if(it.paramId == i.paramId) {
                    it.paramValue = i.paramValue;
                }
            }
        }
        mInfoList.clear();
        mInfoList.addAll(list);

        mInfoAdapter.replaceData(mInfoList);
    }

    override fun onInfoAdded(vo: GoodsParamVo) {
        mInfoList.add(vo);
        mInfoAdapter.replaceData(mInfoList);
    }

}