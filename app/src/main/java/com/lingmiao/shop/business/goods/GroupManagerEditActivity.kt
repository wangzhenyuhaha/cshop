package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerEditPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerEditPreImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.parseString
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.isNotBlank
import kotlinx.android.synthetic.main.goods_group_manager_edit.*

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品 - 添加/编辑分组
 */
class GroupManagerEditActivity: BaseActivity<GroupManagerEditPre>(), GroupManagerEditPre.GroupEditView {

    companion object {
        const val KEY_LEVEL = "KEY_LEVEL"
        const val KEY_PARENT_GROUP_ID = "KEY_PARENT_GROUP_ID"
        const val KEY_GROUP = "KEY_GROUP"

        /**
         * @param groupLevel 分组的级别(一级、二级)
         * @param groupId 父分级的ID(一级分组的groupId默认为null、二级分组的groupId为一级分组ID)
         * @param groupVO 编辑分组场景下使用
         */
        fun openActivity(context: Context, groupLevel: Int, groupId: String?, groupVO: ShopGroupVO?) {
            val intent = Intent(context, GroupManagerEditActivity::class.java)
            intent.putExtra(KEY_LEVEL, groupLevel)
            intent.putExtra(KEY_PARENT_GROUP_ID, groupId)
            intent.putExtra(KEY_GROUP, groupVO)
            context.startActivity(intent)
        }
    }


    private var groupLevel: Int? = null
    /**
     * 编辑模式 = true
     */
    private var isEditMode  = false
    /**
     * 分组的父分组ID
     */
    private var parentGroupId: String? = null
    private lateinit var groupVO: ShopGroupVO

    override fun initBundles() {
        groupLevel = intent.getIntExtra(KEY_LEVEL, ShopGroupVO.LEVEL_1)
        parentGroupId = intent.getStringExtra(KEY_PARENT_GROUP_ID)
        val group = intent.getSerializableExtra(KEY_GROUP) as? ShopGroupVO
        isEditMode = group != null
        groupVO = ShopGroupVO.convert(group)
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_group_manager_edit
    }

    override fun createPresenter(): GroupManagerEditPre {
        return GroupManagerEditPreImpl(this)
    }

    override fun initView() {
        window.setBackgroundDrawable(null)
        if(isEditMode){
            toolbarView.setTitleContent("编辑分组")
        }else{
            toolbarView.setTitleContent("添加分组")
        }
        toolbarView.showDivider(false)
        restoreUI()

        // 商品分组图标
        addIconIv.setOnClickListener {
            PhotoHelper.openAlbum(this, 1, null) { list ->
                if (list.isNotEmpty()) {
                    groupVO.shopCatPic = list[0].compressPath
                    GlideUtils.setImageUrl(addIconIv, groupVO.shopCatPic)
                }
            }
        }

        // 提交编辑/添加的按钮
        submitTv.setOnClickListener {
            groupVO.apply {
                shopCatPid = parentGroupId
                shopCatName = groupNameEdt.getViewText()
                shopCatDesc = groupDescEdt.getViewText()
                sort = groupOrderEdt.getViewText().parseString()
                setDisable(showRadio.isChecked)
            }
            if (isEditMode) {
                mPresenter.modifyGroup(groupVO, groupLevel)
            } else {
                mPresenter.submit(groupVO, groupLevel)
            }
        }
    }

    /**
     * 数据回显
     */
    private fun restoreUI() {
        if (isEditMode) {
            groupVO.apply {
                groupNameEdt.setText(this.shopCatName)
                groupDescEdt.setText(this.shopCatDesc)
                groupOrderEdt.setText("${this.sort}")
                showRadio.isChecked = this.isGroupShow()
                hideRadio.isChecked = !this.isGroupShow()
                if (this.shopCatPic.isNotBlank()) {
                    GlideUtils.setImageUrl(addIconIv, this.shopCatPic)
                }
            }
        }
    }
}