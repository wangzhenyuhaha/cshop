package com.lingmiao.shop.business.tools.presenter.impl

import com.blankj.utilcode.util.ResourceUtils
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import com.lingmiao.shop.business.tools.bean.Item
import com.lingmiao.shop.business.tools.bean.RegionVo
import com.lingmiao.shop.business.tools.bean.TempArea
import com.lingmiao.shop.business.tools.presenter.AreasPresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.james.common.base.BasePreImpl

class AreasPresenterImpl(val view : AreasPresenter.View ) : BasePreImpl(view),AreasPresenter  {

    private fun filterData(original : MutableList<RegionVo>, mEditItem: Item?, mTempArea: TempArea?) : MutableList<RegionVo> {
        // 删除【已存在于配送区域列表的项、省&市没有children】的数据
        var list = removeItem(original, mEditItem, mTempArea);
        var pList : MutableIterator<RegionVo>? = list?.toMutableList()?.listIterator();
        var it : RegionVo ? = null;

        while(pList?.hasNext()!!) {
            it = pList?.next();
            when(it?.getLevel()) {
                AreasAdapter.TYPE_LEVEL_0 -> {
                    // 过滤-存在于配送区域列表的其他省
                    it?.children = filterData(it?.children!!, mEditItem, mTempArea);
                    // 删除没有市的省
                    it?.children = removeNullChildrenItem(it?.children!!);
                }
                AreasAdapter.TYPE_LEVEL_1 -> {
                    // 过滤掉存在于配送区域列表的其他省下的市
                    it?.children = filterData(it?.children!!, mEditItem, mTempArea);
                }
                AreasAdapter.TYPE_AREA -> {

                }
            }
        }
        return list;
    }

    override fun getJsonList(mEditItem: Item?, mTempArea: TempArea?): MutableList<RegionVo> {
        // 读取json文件
        val regionsType = object : TypeToken<List<RegionVo>>(){}.type;
        var json = Gson().fromJson<MutableList<RegionVo>>(ResourceUtils.readAssets2String("areas.json"), regionsType);
        // 过滤项
        json = filterData(json, mEditItem, mTempArea);
        // 删除那些没有市的省
        json = removeNullChildrenItem(json);
        json?.forEach {
            it?.addItem(mEditItem, mTempArea);
        }
        return json;
    }

    override fun getResultList(mList: MutableList<RegionVo>): MutableList<RegionVo> {
        var list : MutableList<RegionVo> = arrayListOf();

        mList.forEachIndexed { index, regionVo ->
            when(regionVo.getLevel()) {
                AreasAdapter.TYPE_LEVEL_0 -> {
                    if(regionVo?.isCheck || regionVo?.selectedAll == true) {
                        regionVo.children = null;
                        regionVo.selectedAll = true;
                        list.add(regionVo);
                    } else {
                        regionVo.children = getResultList(regionVo.children!!);
                    }
                }
                AreasAdapter.TYPE_LEVEL_1 -> {
                    if(regionVo?.isCheck || regionVo.selectedAll == true) {
                        regionVo.children = null;
                        regionVo.selectedAll = true;
                        list.add(regionVo);
                    } else {
                        regionVo.children = getResultList(regionVo.children!!);
                    }
                }
                AreasAdapter.TYPE_AREA -> {
                    if(regionVo?.isCheck || regionVo.selectedAll == true) {
                        list.add(regionVo);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 删除【区域列表已存在、空下级】的数据
     */
    private fun removeItem(list : MutableList<RegionVo>, mEditItem: Item?, mTempArea: TempArea?) : MutableList<RegionVo>{
        var aIt : RegionVo ? = null;
        var aList : MutableIterator<RegionVo> = list?.toMutableList()?.iterator();
        while(aList?.hasNext()!!) {
            aIt = aList?.next();
            // 区下面没有数据，不要删除区
            if(mTempArea?.existId(aIt?.id) == true && mEditItem?.existId(aIt?.id) ?: false == false || ((aIt?.children == null || aIt?.children?.size == 0) && !aIt?.isLastNode())) {
                aList.remove();
                list.remove(aIt);
            }
        }
        return list;
    }

    private fun removeNullChildrenItem(list : MutableList<RegionVo>) : MutableList<RegionVo>{
        var aIt : RegionVo ? = null;
        var aList : MutableIterator<RegionVo> = list?.toMutableList()?.iterator();
        while(aList?.hasNext()!!) {
            aIt = aList?.next();
            if(aIt?.children == null || aIt?.children?.size == 0) {
                aList.remove();
                list.remove(aIt);
            }
        }
        return list;
    }

}