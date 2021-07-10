package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.amap.api.mapcore.util.it
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsThreeItemPop
import com.lingmiao.shop.business.tools.bean.RegionVo

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
class PopAddressPreImpl(val view: BaseView) : BasePreImpl(view) {

    companion object {
        // 一级商品分类id
        const val LV1_CATEGORY_ID = "0"
    }

    private var categoryName: String? = null
    private var typePop: AbsThreeItemPop<RegionVo>? = null

    private var lv1Cache: MutableList<RegionVo> = mutableListOf()
    private var lv2CacheMap: HashMap<String, List<RegionVo>> = HashMap()
    private var lv3CacheMap: HashMap<String, List<RegionVo>> = HashMap()
    private var mList : MutableList<RegionVo> = mutableListOf();

    var shopId : Int? = null;

    fun getSellerId() : String? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return String.format("%s", shopId);
    }

    fun showTypePop(
        context: Context,
        targetView: View,
        list: List<RegionVo>,old : List<RegionVo>?,
        callback: (String?, List<RegionVo>?)-> Unit)
    {
        typePop = object : AbsThreeItemPop<RegionVo>(context, ""){

            override fun getAdapter1(): DefaultItemAdapter<RegionVo> {
                return DefaultItemAdapter();
            }

            override fun getAdapter2(): DefaultItemAdapter<RegionVo> {
                return DefaultItemAdapter();
            }

            override fun getAdapter3(): BaseQuickAdapter<RegionVo, BaseViewHolder> {
                return DefaultItemAdapter();
            }

            override fun getLevel(): Int {
                return LEVEL_3;
            }

            override fun getData2(data1: RegionVo): List<RegionVo> {
                return mutableListOf();
            }

            override fun getData3(data2: RegionVo): List<RegionVo> {
                return mutableListOf();
            }

        }.apply {
            lv1Callback = {
                mList.add(it);
                categoryName = it.localName;
                it.children?.toList()?.let { it1 -> typePop?.setLv2Data(it1) };
            }
            lv2Callback = {
                mList.add(it);
                if(it.children == null || it.children?.size == 0) {
                    callback.invoke(categoryName, mList);
                    dismiss();
                } else {
                    categoryName = "${categoryName}/${it.localName}"
                    it.children?.toList()?.let { it1 ->
                        typePop?.setLv3Data(it1)
                    }
                }
            }
            lv3Callback = {
                mList.add(it);
                categoryName = "${categoryName}/${it.localName}"
                callback.invoke(categoryName, mList)
                dismiss();
            }
        }
        typePop?.setLv1Data(list);
        if(old == null || old.size == 0) {
            typePop?.showPopupWindow(targetView)
            return;
        }
        if(old.size > 0) {
            var first = old.get(0);
            val list =  list?.filter { it?.parentId == first.id };
            typePop?.setLv2Data(list);
        }
        if(old.size > 1) {
            var second = old.get(1);
            val list =  list?.filter { it?.parentId == second.id };
            typePop?.setLv3Data(list);
        }
        typePop?.showPopupWindow(targetView)
    }

    override fun onDestroy() {
        lv1Cache.clear()
        lv2CacheMap.clear()
        lv3CacheMap.clear()
        super.onDestroy()
    }
}