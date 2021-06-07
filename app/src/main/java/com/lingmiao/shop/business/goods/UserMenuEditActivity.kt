package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerEditPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerEditPreImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.goods.adapter.UserChildrenAdapter
import com.lingmiao.shop.business.goods.presenter.UserMenuEditPre
import com.lingmiao.shop.business.goods.presenter.impl.UserMenuEditPreImpl
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.goods_activity_user_menu_edit.*

/**
 * Desc   : 菜单 - 编辑常用菜单
 */
class UserMenuEditActivity: BaseActivity<UserMenuEditPre>(), UserMenuEditPre.GroupEditView {

    var mChildrenAdapter : UserChildrenAdapter? = null;
    var mChildrenList : MutableList<ShopGroupVO>? = null;

    companion object {
        const val KEY_LEVEL = "KEY_LEVEL"
        const val KEY_PARENT_GROUP_ID = "KEY_PARENT_GROUP_ID"
        const val KEY_GROUP = "KEY_GROUP"
        /**
         * @param groupLevel 分组的级别(一级、二级)
         * @param groupId 父分级的ID(一级分组的groupId默认为null、二级分组的groupId为一级分组ID)
         * @param groupVO 编辑分组场景下使用
         */
        fun openActivity(context: Context, groupId: String?, groupVO: ShopGroupVO?) {
            val intent = Intent(context, UserMenuEditActivity::class.java)
            intent.putExtra(KEY_PARENT_GROUP_ID, groupId)
            intent.putExtra(KEY_GROUP, groupVO)
            context.startActivity(intent)
        }
    }

    /**
     * 编辑模式 = true
     */
    private var isEditMode  = false
    /**
     * 分组的父分组ID
     */
    private var parentGroupId: String? = ""
    private lateinit var groupVO: ShopGroupVO

    override fun initBundles() {
        parentGroupId = intent.getStringExtra(KEY_PARENT_GROUP_ID)
        val group = intent.getSerializableExtra(KEY_GROUP) as? ShopGroupVO
        isEditMode = group != null
        groupVO = ShopGroupVO.convert(group)
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_user_menu_edit
    }

    override fun createPresenter(): UserMenuEditPre {
        return UserMenuEditPreImpl(this,this)
    }

    override fun initView() {
        window.setBackgroundDrawable(null)
        if(isEditMode){
            mToolBarDelegate.setMidTitle("编辑常用菜单")
            menuChildrenLayout.visiable()
        }else{
            mToolBarDelegate.setMidTitle("添加常用菜单")
            menuChildrenLayout.gone();
        }

        initAdapter();

        addChildrenTv.singleClick {
            DialogUtils.showInputDialog(context, "菜单名称", "", "请输入","取消", "保存",null) {
                mPresenter?.addGroup(it, groupVO.shopCatId!!);
            }
        }
        // 提交编辑/添加的按钮
        submitTv.setOnClickListener {
            groupVO.apply {
                shopCatPid = parentGroupId
                shopCatName = menuNameEdt.getViewText()
                shopCatDesc = ""
                sort = 0;
                isTop = 0
            }
            if (isEditMode) {
                mPresenter.modifyGroup(groupVO)
            } else {
                mPresenter.submit(groupVO)
            }
        }

        if(groupVO?.shopCatId?.length?:0 > 0) {
            mPresenter.getShopGroupAndChildren(groupVO?.shopCatId!!);
        } else {
            restoreUI()
        }
    }

    fun initAdapter() {
        mChildrenList = groupVO?.children ?: mutableListOf();
        mChildrenAdapter = UserChildrenAdapter(mChildrenList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = mChildrenAdapter?.getItem(position)
                if(view.id == R.id.cateChildrenMoreIv) {
                    mPresenter?.clickMenuView(item, position, view);
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
        }
        menuChildren.initAdapter(mChildrenAdapter!!)
    }

    /**
     * 数据回显
     */
    private fun restoreUI() {
        if (isEditMode) {
            menuNameEdt.setText(groupVO?.shopCatName)
            mChildrenList = groupVO?.children ?: mutableListOf()
            mChildrenAdapter?.replaceData(mChildrenList!!);
        }
    }

    override fun onSetGroup(item: ShopGroupVO) {
        groupVO = item;
        restoreUI();
    }

    override fun onDeleteGroupSuccess(position: Int) {
        mChildrenList?.removeAt(position);
        mChildrenAdapter?.replaceData(mChildrenList!!);
    }

    override fun onDeleteFailed() {
        mPresenter?.getShopGroupAndChildren(groupVO.shopCatId!!);
    }

    override fun onGroupAdded(item: ShopGroupVO) {
        mChildrenList?.add(item);
        mChildrenAdapter?.replaceData(mChildrenList!!);
    }

    override fun onUpdated(item: ShopGroupVO, position: Int) {
        mChildrenList?.set(position, item);
        mChildrenAdapter?.notifyDataSetChanged();
    }

    override fun onUpdateFail() {
        mPresenter?.getShopGroupAndChildren(groupVO.shopCatId!!);
    }

}