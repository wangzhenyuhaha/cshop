package com.lingmiao.shop.business.tools.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.presenter.EditFreightModelPresenter
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch


class EditFreightModelPresenterImpl(val view : EditFreightModelPresenter.View) : BasePreImpl(view), EditFreightModelPresenter {

    private var json :Gson;

    init {

        json = Gson();
    }

    override fun loadItem(id: String) {
        mCoroutine.launch {
            view.showDialogLoading();
            LogUtils.dTag("delete model" + id);
            val resp = ToolsRepository.getShipTemplates(id);
            handleResponse(resp) {
                view.loadItemSuccess(resp.data);
            }
            view.hideDialogLoading();
        }
    }

    override fun getFeeSetting(item: FreightVoItem): FeeSettingVo {
        return json?.getAdapter(TypeToken.get(FeeSettingVo::class.java)).fromJson(item?.feeSetting);
    }

    override fun getTimeSetting(item: FreightVoItem): TimeSettingVo {
        return json?.getAdapter(TypeToken.get(TimeSettingVo::class.java)).fromJson(item?.timeSetting);
    }

    override fun getArea(item: Item) : Map<String, Map<String, Any>> {
        return json?.getAdapter(TypeToken.get(LinkedTreeMap<String, LinkedTreeMap<String, Any>>()::class.java)).fromJson(item?.area);
    }

    var tempArea : Map<String, Map<String, Any>> ? = null;

    override fun getAreas(items : MutableList<Item>?): TempArea {
        val allAreas : MutableList<Map<String, Map<String, Any>>> = arrayListOf();
        if(items != null) {
            items?.forEachIndexed{ index : Int, it : Item->
                if(it != null) {
                    tempArea = getArea(it);
                    if(tempArea != null) {
                        it?.parsedArea = tempArea;
                        allAreas.add(tempArea!!);
                        it?.selectedIds = getIds(tempArea!!);
                    }
                }
            }
        }
        val items : MutableList<String> = arrayListOf();
        allAreas.forEachIndexed { index, linkedTreeMap ->
            items.addAll(getIds(linkedTreeMap))
        }
        val temp = TempArea(allAreas, items);
        return temp;
    }

    override fun updateModel(item: FreightVoItem) {
        mCoroutine.launch {
            view.showPageLoading();
            if(item?.isLocalTemplateType()) {
                val resp = ToolsRepository.updateShipTemplates(item?.id!!, item);
                handleResponse(resp) {
                    view.updateModelSuccess(resp.data);
                }
            } else {
                val resp = ToolsRepository.updateShipTemplates(item);
                handleResponse(resp) {
                    view.updateModelSuccess(true);
                }
            }
            view.hidePageLoading();
        }
    }

    override fun addModel(item: FreightVoItem) {
        mCoroutine.launch {
            view.showDialogLoading();
            LogUtils.dTag("add model" + item.name);
            val resp = ToolsRepository.addShipTemplate(item);
            handleResponse(resp) {
                view.updateModelSuccess(true);
            }
            view.hideDialogLoading();
        }
    }

    var tempMap1 : Map<String, Map<String, Any>> ? = null;
    var tempArray1 : ArrayList<Any> ? = null;

    fun getIds(item : Map<String, Map<String, Any>>) : MutableList<String> {
        val list : MutableList<String> = arrayListOf();
        for((key,value) in item) {
            if(value?.get("selected_all") == true || value?.get("level") == 3.0) {
                list.add(key);
            } else {
                if(value?.get("children") is Map<*, *>) {
                    tempMap1 = value?.get("children") as Map<String, Map<String, Any>>;
                    if(tempMap1 == null || tempMap1?.size == 0) {
                        list.add(key);
                    } else {
                        list.addAll(getIds(tempMap1!!));
                    }
                } else if(value?.get("children") is ArrayList<*>) {
                    tempArray1 = value?.get("children") as ArrayList<Any>;
                    if(tempArray1 == null || tempArray1?.size == 0) {
                        list.add(key);
                    }
                }
            }

        }
        return list;
    }
}