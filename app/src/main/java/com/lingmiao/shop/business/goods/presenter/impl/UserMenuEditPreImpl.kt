package com.lingmiao.shop.business.goods.presenter.impl

import android.app.Activity
import android.content.Context
import android.view.View
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.GroupRefreshEvent
import com.james.common.base.BasePreImpl
import com.james.common.exception.BizException
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.checkNotBlack
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.pop.ChildrenGoodsMenuPop
import com.lingmiao.shop.business.goods.pop.ChildrenMenuPop
import com.lingmiao.shop.business.goods.presenter.UserMenuEditPre
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   :
 */
class UserMenuEditPreImpl(val context: Context, var view: UserMenuEditPre.GroupEditView) :
    BasePreImpl(view),
    UserMenuEditPre {

    private val menuPopPre: ChildrenMenuPreImpl by lazy { ChildrenMenuPreImpl(context, view) }

    private val quantityPopPre: QuantityPricePreImpl by lazy { QuantityPricePreImpl(context, view) }

    override fun getShopId(): String {
        return UserManager.getLoginInfo()?.shopId?.toString() ?: "";
    }

    override fun getShopGroup(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.getShopGroup(id)
            handleResponse(resp) {
                view.onSetGroup(resp.data)
            }
        }
    }

    override fun getShopGroupAndChildren(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.getShopGroup(id);
            if (resp.isSuccess) {
                val item = resp.data;
                val listResp = GoodsRepository.load2LvShopGroup(id, 0)
                handleResponse(listResp) {
                    item.children = listResp?.data as MutableList<ShopGroupVO>?;
                    view.onSetGroup(item);
                }
            }
        }
    }

    override fun addGroup(str: String, pid: String) {
        mCoroutine.launch {
            val groupVO = ShopGroupVO();
            groupVO.shopId = getShopId().toString();
            groupVO.shopCatName = str;
            //groupVO.showLevel = level;
            groupVO.shopCatPid = pid
            groupVO.isTop = 0;
            //groupVO.shopCatId = getShopId().toString();

            val resp = GoodsRepository.submitShopGroup(groupVO)
            handleResponse(resp) {
                view.onGroupAdded(it)
            }
        }
    }

    override fun submit(groupVO: ShopGroupVO) {
        try {
            checkNotBlack(groupVO.shopCatName) { "请输入菜单名称" }
            //checkNotBlack("${groupVO.sort}") { "请输入菜单排序" }

            view.showDialogLoading("正在提交...")
            mCoroutine.launch {
                uploadImg(groupVO.shopCatPic) {
                    groupVO.shopCatPic = it
                    val resp = GoodsRepository.submitShopGroup(groupVO)
                    handleResponse(resp) {
                        EventBus.getDefault().post(GroupRefreshEvent())
                        view.showToast("添加成功")
                        view.finish()
                    }
                }
                view.hideDialogLoading()
            }
        } catch (e: BizException) {
            view.showToast(e.message)
        }
    }

    override fun modifyGroup(groupVO: ShopGroupVO) {
        if (groupVO.shopCatId.isNullOrBlank()) {
            return
        }
        view.showDialogLoading("正在提交...")
        mCoroutine.launch {
            uploadImg(groupVO.shopCatPic) {
                groupVO.shopCatPic = it
                val resp = GoodsRepository.updateShopGroup(groupVO.shopCatId!!, groupVO)
                handleResponse(resp) {
                    EventBus.getDefault().post(GroupRefreshEvent())
                    view.showToast("编辑成功")
                    view.finish()
                }
            }
            view.hideDialogLoading()
        }
    }

    private suspend fun uploadImg(imgPath: String?, callback: suspend (String?) -> Unit) {
        if (imgPath.isNullOrBlank() || imgPath.isNetUrl()) {
            callback.invoke(imgPath)
            return
        }
        val resp = CommonRepository.uploadFile(File(imgPath), true, CommonRepository.SCENE_GOODS)
        if (resp.isSuccess) {
            callback.invoke(resp.data.url)
        } else {
            handleErrorMsg(resp.msg)
        }
    }

    override fun deleteGoodsGroup(groupVO: ShopGroupVO?, position: Int) {
        if (groupVO?.shopCatId.isNullOrBlank()) return
        mCoroutine.launch {
            val resp = GoodsRepository.deleteShopGroup(groupVO?.shopCatId)
            handleResponse(resp) {
                view.showToast("删除成功")
                view.onDeleteGroupSuccess(position)
            }
        }
    }


    override fun clickMenuView(item: ShopGroupVO?, position: Int, target: View) {
        if (item == null || item.shopCatId == null) {
            return
        }
        menuPopPre.showMenuPop(
            ChildrenMenuPop.TYPE_EDIT or ChildrenMenuPop.TYPE_DELETE,
            target
        ) { menuType ->
            when (menuType) {
                ChildrenMenuPop.TYPE_EDIT -> {
                    DialogUtils.showInputDialog(
                        context as Activity,
                        "菜单名称",
                        "",
                        "请输入",
                        item?.shopCatName ?: "",
                        "取消",
                        "保存",
                        null
                    ) {
                        item.shopCatName = it;
                        update(item, {
                            view?.onUpdated(item, position);
                        }, {

                        });
                    }
                }
                ChildrenMenuPop.TYPE_DELETE -> {
                    DialogUtils.showDialog(context as Activity,
                        "删除提示", "删除后不可恢复，确定要删除该二级菜单吗？",
                        "取消", "确定删除",
                        null, View.OnClickListener {
                            delete(item.shopCatId!!, {
                                view?.onDeleteGroupSuccess(position);
                            }, {

                            })
                        })
                }
            }
        }
    }

    fun delete(id: String, successCallback: (Unit) -> Unit, failedCallback: () -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.deleteShopGroup(id)

            handleResponse(resp) {
                successCallback.invoke(resp.data)
            }

        }
    }

    fun update(
        item: ShopGroupVO,
        successCallback: (ShopGroupVO) -> Unit,
        failedCallback: () -> Unit
    ) {
        mCoroutine.launch {
            val resp = GoodsRepository.updateShopGroup(item.shopCatId!!, item)
            option(resp, successCallback, failedCallback);
        }
    }


    fun option(
        resp: HiResponse<ShopGroupVO>,
        successCallback: (ShopGroupVO) -> Unit,
        failedCallback: () -> Unit
    ) {
        if (resp?.isSuccess) {
            successCallback.invoke(resp?.data);
        } else {
            failedCallback?.invoke();
        }
    }
}