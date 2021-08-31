package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.SpecSettingAdapter
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.pop.BatchSettingPop
import com.lingmiao.shop.business.goods.presenter.SpecSettingPre
import com.lingmiao.shop.business.goods.presenter.impl.SpecSettingPreImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.isNotEmpty
import com.james.common.utils.exts.singleClick
import kotlinx.android.synthetic.main.goods_activity_spec_setting.*


/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格设置页
 */
class SpecSettingActivity : BaseActivity<SpecSettingPre>(),
    SpecSettingPre.SpceSettingView {

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        const val KEY_GOODS_ID = "KEY_GOODS_ID"
        const val KEY_SKU_LIST = "KEY_SKU_LIST"
        const val KEY_SPEC_KEY_LIST = "KEY_SPEC_KEY_LIST"

        const val SPEC_REQUEST_CODE = 1000

        fun openActivity(context: Context, requestCode: Int, goodsVO: GoodsVOWrapper) {
            if (context is Activity) {
                val intent = Intent(context, SpecSettingActivity::class.java)
                intent.putExtra(KEY_CATEGORY_ID, goodsVO.categoryId)
                intent.putExtra(KEY_GOODS_ID, goodsVO.goodsId)
                intent.putExtra(KEY_SKU_LIST, goodsVO.skuList as? ArrayList)
                intent.putExtra(KEY_SPEC_KEY_LIST, goodsVO.specKeyList as? ArrayList)
                context.startActivityForResult(intent, requestCode)
            }
        }

        fun openActivity(context: Context, requestCode: Int, isVirtual : Boolean, goodsVO: GoodsVOWrapper) {
            if (context is Activity) {
                val intent = Intent(context, SpecSettingActivity::class.java)
                intent.putExtra(KEY_CATEGORY_ID, goodsVO.categoryId)
                intent.putExtra(KEY_GOODS_ID, goodsVO.goodsId)
                intent.putExtra(KEY_SKU_LIST, goodsVO.skuList as? ArrayList)
                intent.putExtra(KEY_SPEC_KEY_LIST, goodsVO.specKeyList as? ArrayList)
                intent.putExtra("type", if(isVirtual) 2 else 0)
                context.startActivityForResult(intent, requestCode)
            }
        }

    }

    /**
     * 虚拟的=2
     */
    private var mType : Int? = 0
    private var categoryId: String? = null
    private var goodsId: String? = null
    private var skuList: List<GoodsSkuVOWrapper>? = null
    private var specKeyList: List<SpecKeyVO>? = null

    private var mAdapter: SpecSettingAdapter? = null

    // 批量设置的弹窗
    private var batchSettingPop: BatchSettingPop? = null

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_spec_setting
    }

    override fun initBundles() {
        mType = intent.getIntExtra("type", 0)
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        goodsId = intent.getStringExtra(KEY_GOODS_ID)
        skuList = intent.getSerializableExtra(KEY_SKU_LIST) as? List<GoodsSkuVOWrapper>
        specKeyList = intent.getSerializableExtra(KEY_SPEC_KEY_LIST) as? List<SpecKeyVO>
    }

    override fun createPresenter(): SpecSettingPre {
        return SpecSettingPreImpl(this, this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("规格设置")
        initSpecContainerLayout()
        initAdapter()
        initBottomView()
        if(skuList == null || specKeyList == null) {
            mPresenter.loadSpecKeyList(goodsId)
        }
        if(skuList == null && specKeyList == null && goodsId == null) {
            mPresenter.loadSpecListByCid(categoryId);
        }
    }

    private fun initAdapter() {
        mAdapter = SpecSettingAdapter(mType!!)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SpecSettingActivity)
            adapter = mAdapter
        }
        // 回显数据
        if (skuList.isNotEmpty()) {
            mAdapter?.addData(skuList!!)
        }
    }

    private fun initSpecContainerLayout() {
        specContainerLayout.apply {
            bindAddSpecBtn(addSpecLl)
            loadSpecValueListener = {
                mPresenter?.showAddOldKey(categoryId, it, getSpecValueList(it));
            }
            addSpecValueListener = {
                showInputValueDialog(it)
            }
            deleteSpecValueListener = {
                mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
            }
            deleteSpecItemListener = {
                mAdapter?.clear()
                mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
            }
            // 非商品编辑状态下的数据回显
            specContainerLayout.addSpecItems(specKeyList, true)
        }
        addSpecLl.setOnClickListener {
            SpecKeyActivity.openActivity(this, SPEC_REQUEST_CODE, categoryId, specContainerLayout.getSpecList())
        }
    }

    private fun showInputValueDialog(specKeyID: String) {
        DialogUtils.showInputDialog(
            this, "添加规格值", "", "请输入规格名，多个用逗号\",\"分隔",
            "取消", "保存", null
        ) {
//            val list = skuList?.filter { _it->_it.specList == specKeyID };
            mPresenter.submitSpecValue(specKeyID, it)
        }
    }

    private fun initBottomView() {
        // 全选
        allCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.updateCheckStatus(isChecked)
        }
        // 批量设置
        settingTv.singleClick {
            if (mAdapter?.isSpecSelected() == false) {
                showToast("请选择需要设置的sku")
                return@singleClick
            }
            showBatchSettingPop()
        }
        // 保存
        confirmTv.singleClick {
            var list = mAdapter?.data?.filter { it?.price == ""};
            if(list?.isNotEmpty() == true) {
                showToast("请输入商品价格")
                return@singleClick
            }

            list = mAdapter?.data?.filter { it?.quantity == "" };
            if(list?.isNotEmpty() == true) {
                showToast("请输入商品库存")
                return@singleClick
            }

            val intent = Intent()
            intent.putExtra(KEY_SKU_LIST, mAdapter?.data as? ArrayList<GoodsSkuVOWrapper>)
            intent.putExtra(KEY_SPEC_KEY_LIST, specContainerLayout.getSpecKeyAndValueList() as? ArrayList<*>)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


    private fun showBatchSettingPop() {
        if (batchSettingPop == null) {
            batchSettingPop = BatchSettingPop(this, {
                mAdapter?.updateSetting(it)
            })
        }
        batchSettingPop!!.show(mType == 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (requestCode == SPEC_REQUEST_CODE) {
            val list = data.getSerializableExtra(SpecKeyActivity.KEY_SPEC_LIST) as? ArrayList<SpecKeyVO>
            specContainerLayout.addSpecItems(list, true)
            mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
        }
    }

    override fun onAddSpecValueSuccess(specKeyID: String, spceValueList: List<SpecValueVO>) {
        specContainerLayout.updateSpecValues(specKeyID, spceValueList)
        mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
    }

    override fun onLoadSkuListSuccess(list: List<GoodsSkuVOWrapper>) {
        mAdapter?.replaceData(list)
    }

    override fun onLoadCacheSkuListSuccess(skuCache: GoodsSkuCacheVO) {
        skuCache?.skuList?.forEachIndexed { index, goodsSkuVOWrapper ->
            goodsSkuVOWrapper?.isEditable = false;
        }
        // 展示规格值
        mAdapter?.replaceData(skuCache.skuList ?: arrayListOf())
        // 展示顶部规格名称
        specContainerLayout.addSpecItems(skuCache.specInfo, true)


        mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
        //mPresenter.loadSpecListByCid(categoryId);
    }

    override fun onLoadedSpecListByCid(list: List<SpecKeyVO>) {
        specContainerLayout.addSpecItems(list, true)
        mPresenter.getSpecKeyList(mAdapter?.data!!, specContainerLayout.getSpecKeyAndValueList())
    }
}