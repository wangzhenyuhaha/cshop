package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsUseExpireVo
import com.lingmiao.shop.business.goods.api.bean.MultiPickerItemBean
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.pop.ExpireListPop
import com.lingmiao.shop.business.goods.pop.GoodsGroupPop
import com.lingmiao.shop.business.goods.pop.UseTimeListPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.james.common.utils.exts.isNotBlank
import kotlinx.android.synthetic.main.goods_include_publish_section_v_time.*
import kotlinx.coroutines.launch

/**
Create Date : 2021/1/1212:07 PM
Auther      : Fox
Desc        :
 **/
class UseTimePopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var mExpire : UseTimeListPop ? = null;

    private var mList : MutableList<MultiPickerItemBean> = mutableListOf();

    fun showPop(context: Context, string: String, callback: (List<MultiPickerItemBean>?) -> Unit) {
        mList = getDefaultUseTimeList(string);
        mExpire = UseTimeListPop(context, mList).apply {
            listener = { items ->
                callback.invoke(items);
            }
        }
        mExpire?.showPopupWindow()
    }

    fun getDefaultUseTimeList(string: String?) : MutableList<MultiPickerItemBean> {
        var useTimeList = mutableListOf<MultiPickerItemBean>();
        useTimeList?.add(MultiPickerItemBean("全选","一,二,三,四,五,六,日", string?.indexOf("一,二,三,四,五,六,日")?:0>-1 ));
        useTimeList?.add(MultiPickerItemBean("星期一","一", string?.indexOf("一")?:0>-1 ));
        useTimeList?.add(MultiPickerItemBean("星期二", "二", string?.indexOf("二")?:0>-1 ));
        useTimeList?.add(MultiPickerItemBean("星期三", "三", string?.indexOf("三")?:0>-1 ));
        useTimeList?.add(MultiPickerItemBean("星期四", "四", string?.indexOf("四")?:0>-1));
        useTimeList?.add(MultiPickerItemBean("星期五", "五", string?.indexOf("五")?:0>-1));
        useTimeList?.add(MultiPickerItemBean("星期六", "六", string?.indexOf("六")?:0>-1));
        useTimeList?.add(MultiPickerItemBean("星期日", "日", string?.indexOf("日")?:0>-1));
        return useTimeList;
    }

    fun getUseTimeStr(str: String?) : String {
        var labels = "";
        var list = getDefaultUseTimeList(str);
        list?.forEachIndexed { index, item ->
            if(str?.indexOf(item?.value?:"") ?:0 > -1) {
                labels += item?.name;
                if(index < list?.size-1) {
                    labels += ",";
                }
            }
        };
        return labels;
    }

    override fun onDestroy() {
        mList.clear()
        super.onDestroy()
    }

}