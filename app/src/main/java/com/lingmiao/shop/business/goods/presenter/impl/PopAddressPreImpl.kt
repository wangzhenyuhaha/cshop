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

    private var categoryName: String? = null
    private var addressPop: AbsThreeItemPop<RegionVo>? = null

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



    fun showAddressPop(
        context: Context,
        targetView: View,
        list: List<RegionVo>,old : List<RegionVo>?,
        callback: (String?, List<RegionVo>?)-> Unit)
    {
        var l1Adapter = DefaultItemAdapter<RegionVo>();
        var l2Adapter = DefaultItemAdapter<RegionVo>();
        var l3Adapter = DefaultItemAdapter<RegionVo>();

        addressPop = object : AbsThreeItemPop<RegionVo>(context, "请选择省市区"){

            override fun getAdapter1(): DefaultItemAdapter<RegionVo> {
                return l1Adapter;
            }

            override fun getAdapter2(): DefaultItemAdapter<RegionVo> {
                return l2Adapter;
            }

            override fun getAdapter3(): BaseQuickAdapter<RegionVo, BaseViewHolder> {
                return l3Adapter;
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
                mList.clear();
                mList.add(it);
                categoryName = it.localName;

                l1Adapter.setSelectedStr(it.localName);
                it.children?.toList()?.let { it1 -> addressPop?.setLv2Data(it1) };
            }
            lv2Callback = {
                mList.add(it);
                l2Adapter.setSelectedStr(it.localName);
                if(it.children == null || it.children?.size == 0) {
                    callback.invoke(categoryName, mList);
                    dismiss();
                } else {
                    categoryName = "${categoryName}/${it.localName}"
                    it.children?.toList()?.let { it1 ->
                        addressPop?.setLv3Data(it1)
                    }
                }
            }
            lv3Callback = {
                mList.add(it);
                l3Adapter.setSelectedStr(it.localName);
                categoryName = "${categoryName}/${it.localName}"
                callback.invoke(categoryName, mList)
                dismiss();
            }
        }
        addressPop?.setLv1Data(list);
        if(old == null || old.isEmpty()) {
            addressPop?.showPopupWindow()
            return;
        }
        var first = old.get(0);
        val _firstList =  list?.filter { it?.id == first.id };
//        if(old.isNotEmpty()) {
//            var first = old.get(0);
//            val _list =  list?.filter { it?.id == first.id };
//            addressPop?.setLv2Data(_list);
//        }
        if(old.size > 2) {
            var second = old.get(1);
            var _secondList =  getListByName(list, first.localName);
            if (_secondList != null) {
                addressPop?.setLv2Data(_secondList);


                var _thirdList =  getListByName(_secondList, second.localName);
                _thirdList?.let {
                    addressPop?.setLv3Data(it);
                }
            };

            l1Adapter.setSelectedStr(old.get(0).localName);
            l2Adapter.setSelectedStr(old.get(1).localName);
            l3Adapter.setSelectedStr(old.get(2).localName);
        }


//        addressPop?.setPopTitle()
        addressPop?.showPopupWindow("请选择省市区")
    }

    fun getListByName(list : List<RegionVo>, first : String?, name : String?): List<RegionVo>? {
        if(name?.isNullOrBlank() == true) {
            return list;
        }
        list.forEachIndexed { index, regionVo ->
            if(regionVo.localName?.indexOf(first?:"")?:0 > -1) {
                val _list = regionVo.children?.toList();
                if(_list != null) {
                    return getListByName(_list, name);
                }
                return listOf<RegionVo>();
            }
        }
        return listOf<RegionVo>();
    }

    fun getListByName(list : List<RegionVo>, name : String?): List<RegionVo>? {
        if(name?.isNullOrBlank() == true) {
            return list;
        }
        list.forEachIndexed { index, regionVo ->
            if(regionVo.localName?.indexOf(name?:"")?:0 > -1) {
                val _list = regionVo.children?.toList();
                return _list;
            }
        }
        return listOf<RegionVo>();
    }

    override fun onDestroy() {
        lv1Cache.clear()
        lv2CacheMap.clear()
        lv3CacheMap.clear()
        super.onDestroy()
    }
}