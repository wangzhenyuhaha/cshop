package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.presenter.GoodsInfoEditPre
import kotlinx.coroutines.launch


/**
 * Desc   : 商品信息编辑
 */
class GoodsInfoUpdatePreImpl(val context: Context, val view: GoodsInfoEditPre.PublicView) : BasePreImpl(view),
    GoodsInfoEditPre {

    private val mItemPreImpl: ChildrenCatePopPreImpl by lazy { ChildrenCatePopPreImpl(view) }

    var mCateList : List<CategoryVO> ? = null;

    override fun addInfo(cId: String, name: String?) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val vo : GoodsParamVo = GoodsParamVo();
            vo.paramName = name;
            vo.categoryId = cId;
            vo.groupId = 0;
            vo.isIndex = 0;
            vo.paramType = 1
            vo.required = 0;
            val resp = GoodsRepository.addInfo(vo);
            handleResponse(resp) {
                view?.showToast("添加成功")
                view?.onAddInfo(resp.data);
            }
            view?.hideDialogLoading()
        }
    }

    override fun updateInfo(item: GoodsParamVo) {

    }

    override fun itemClick(id: String?) {
        if(mCateList == null) {
            loadList(id) {
                show();
            }
        } else {
            show();
        }
    }

    fun show() {
        if(mCateList == null || mCateList?.size == 0) {
            view?.showToast("没有查找到相关分类")
            return;
        }
        mItemPreImpl?.showPop(context, "", mCateList) {
            view?.onSetCategories(it);
        }
    }

    fun loadList(id: String?, call: ((List<CategoryVO>) -> Unit)?) {
        if (id.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadUserCategory(id, getSellerId()?.toString())
            if (resp.isSuccess) {
                mCateList = resp?.data;
                call?.invoke(resp?.data);
            }
        }
    }

    override fun loadCateList(id: String?) {
        loadList(id) {
            mCateList = it;
        }
    }

    var shopId : Int? = null;

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }
}