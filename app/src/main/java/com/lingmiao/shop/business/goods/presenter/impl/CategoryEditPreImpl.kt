package com.lingmiao.shop.business.goods.presenter.impl

import android.app.Activity
import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.GoodsInfoActivity
import com.lingmiao.shop.business.goods.GoodsSpecActivity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.pop.CateMenuPop
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class CategoryEditPreImpl(var context: Context, var view: CategoryEditPre.PubView) : BasePreImpl(view),CategoryEditPre {

    var shopId : Int? = null;

    private val menuPopPre: CateMenuPreImpl by lazy { CateMenuPreImpl(context, view) }

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }

    override fun loadLv1GoodsGroup() {
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory("0", getSellerId());
            if (resp.isSuccess) {
                val list: List<CategoryVO> = resp?.data;
                list?.forEachIndexed { index, it ->
                    it.showLevel = 0;
                    it.children?.forEachIndexed { index, categoryVO ->
                        categoryVO.showLevel = 1;
                        it.addSubItem(categoryVO);
                    }
                }
                view.onLoadMoreSuccess(list, list.isNotEmpty())
            }
        }
    }

    override fun loadLv2GoodsGroup(lv1GroupId: String?) {
        if (lv1GroupId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory(lv1GroupId, getSellerId())
            if (resp.isSuccess) {
                val list: List<CategoryVO> = resp?.data;
                list?.forEachIndexed { index, it ->
                    it.showLevel = 1;
                }
                view.onLoadedLv2(lv1GroupId, resp.data)
            }
        }
    }

    override fun update(item: CategoryVO) {
        mCoroutine.launch {
            val resp = GoodsRepository.updateCategory(item);
            handleResponse(resp) {
                view.onUpdated(item);
                view.showToast("修改成功")
            }
        }
    }

    override fun delete(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.deleteCate(id);
            handleResponse(resp) {
                view.onDeleted(id);
                view.showToast("删除成功")
            }
        }
    }

    override fun add(pId: Int, name: String) {
        var item = CategoryVO();
        item.showLevel = if(pId == 0 ) 0 else 1;
        item.name = name;
        item.parentId = pId;
        item.sellerId = getSellerId().toString();

        mCoroutine.launch {
            val resp = GoodsRepository.addCategory(item);
            handleResponse(resp) {
                item = resp.data;
                view.showToast("添加成功")
                view.onAdded(pId, item);
            }
        }
    }

    override fun clickMenuView(item: CategoryVO?, position: Int, target: View) {
        if (item == null || item.categoryId == null) {
            return
        }
        menuPopPre.showMenuPop(item.getMenuType(), target) { menuType ->
            when (menuType) {
                CateMenuPop.TYPE_GOODS_INFO -> {
                    GoodsInfoActivity.openActivity(context, 1009, item.categoryId!!);
                }
                CateMenuPop.TYPE_SPEC -> {
                    GoodsSpecActivity.openActivity(context, 1019, item.categoryId!!)
                }
                CateMenuPop.TYPE_EDIT -> {
                    showEditDialog(item);
                }
                CateMenuPop.TYPE_CHILDREN -> {
                    showAddDialog(item.categoryId?.toInt()!!);
                }
                CateMenuPop.TYPE_DELETE -> {
                    delete(item.categoryId)
                }
            }
        }
    }

    override fun showEditDialog(item: CategoryVO) {
        DialogUtils.showInputDialog(
            context as Activity,
            "分类名称",
            "",
            "请输入",
            item?.name,
            "取消",
            "保存",
            null
        ) {
            item.name = it;
            update(item);
        }
    }

    override fun showAddDialog(pId : Int) {
        DialogUtils.showInputDialog(context as Activity, "分类名称", "", "请输入","取消", "保存",null) {
            add(pId, it);
        }
    }

}