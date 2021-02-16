package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.SpecKeyAdapter
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.presenter.SpecKeyPre
import com.lingmiao.shop.business.goods.presenter.impl.SpecKeyPreImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import kotlinx.android.synthetic.main.goods_activity_spec_key.*
import kotlinx.android.synthetic.main.goods_activity_spec_key.toolbarView

/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 规格名称列表页
 */
class SpecKeyActivity : BaseActivity<SpecKeyPre>(), SpecKeyPre.SpceKeyView {

    companion object {
        const val MAX_SPEC_SELECTED = 5 //规格最大选择数量

        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        const val KEY_SPEC_LIST = "KEY_SPEC_LIST"

        fun openActivity(context: Context, requestCode: Int, categoryId: String?, specList: List<SpecKeyVO>) {
            val intent = Intent(context, SpecKeyActivity::class.java)
            intent.putExtra(KEY_CATEGORY_ID, categoryId)
            intent.putExtra(KEY_SPEC_LIST, specList as ArrayList<SpecKeyVO>)
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            }
        }

        fun virtualType(context: Context, requestCode: Int, categoryId: String?, specList: List<SpecKeyVO>) {
            val intent = Intent(context, SpecKeyActivity::class.java)
            intent.putExtra(KEY_CATEGORY_ID, categoryId)
            intent.putExtra(KEY_SPEC_LIST, specList as ArrayList<SpecKeyVO>)
            intent.putExtra("type", 2)
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var mType : Int? = 0
    private var categoryId: String? = null
    private var specList: List<SpecKeyVO>? = null

    private var mAdapter: SpecKeyAdapter? = null

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_spec_key
    }

    override fun initBundles() {
        mType = intent.getIntExtra("type", 0)
        categoryId = intent.getStringExtra(KEY_CATEGORY_ID)
        specList = intent.getSerializableExtra(KEY_SPEC_LIST) as? List<SpecKeyVO>
    }

    override fun createPresenter(): SpecKeyPre {
        return SpecKeyPreImpl(this)
    }

    override fun initView() {
        toolbarView.setTitleContent(if(mType == 2) "添加套餐" else "添加规格名")
        toolbarView.setRightListener("确定", R.color.color_3870EA) {
            setSpecResult()
        }
        initRecyclerView()

        mPresenter.loadGoodsSpecKey(categoryId, specList)
    }

    override fun onLoadSuccess(list: List<MultiItemEntity>) {
        mAdapter?.addData(list)
    }

    override fun onLoadFailed() {

    }

    override fun onAddSpecSuccess(spceVo: SpecKeyVO) {
        mAdapter?.addSpecKey(spceVo)
    }


    // ---------------------- Private Method -------------------------

    private fun setSpecResult() {
        val intent = Intent()
        val selectedSpecList = mutableListOf<SpecKeyVO>()
        mAdapter?.data?.forEach {
            if (it is SpecKeyVO) {
                if (it.isSelected) {
                    selectedSpecList.add(it)
                }
            }
        }
        intent.putExtra(KEY_SPEC_LIST, selectedSpecList as ArrayList<SpecKeyVO>)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initRecyclerView() {
        mAdapter = SpecKeyAdapter().apply {
            setOnItemClickListener { _, view, position ->
                clickAdapterItem(position)
            }
        }
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@SpecKeyActivity, 3)
            itemAnimator?.apply {
                (this as? SimpleItemAnimator)?.supportsChangeAnimations = false
            }
        }
    }

    private fun clickAdapterItem(position: Int) {
        val item = mAdapter?.getItem(position)
        when (item?.itemType) {
            SpecKeyAdapter.TYPE_ADD_BTN -> {
                showInputDialog()
            }

            SpecKeyAdapter.TYPE_SPEC -> {
                (item as SpecKeyVO).apply {
                    // 选中的规格名称不能超过5个
                    if (!isSelected && checkCountOutOfLimit()) {
                         showToast("最多选择5个")
                        return
                    }
                    isSelected = !isSelected
                }
                mAdapter?.notifyItemChanged(position)
            }
        }
    }

    private fun checkCountOutOfLimit(): Boolean {
        var count = 0
        mAdapter?.data?.forEach {
            if (it is SpecKeyVO) {
                if (it.isSelected) {
                    ++count
                }
            }
        }
        return count >= MAX_SPEC_SELECTED
    }

    private fun showInputDialog() {
        DialogUtils.showInputDialog(
            this@SpecKeyActivity,
            "添加规格名称", "", "请输入规格名",
            "取消", "保存", null
        ) { edtInput ->
            mPresenter.submitSpecKey(categoryId, edtInput)
        }
    }
}