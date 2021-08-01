package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.widget.Toast
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.presenter.IShopMapAddressPresenter

/**
Create Date : 2021/7/2710:43 上午
Auther      : Fox
Desc        :
 **/
class ShopMapAddressPresenterImpl(val context: Context, private var view: IShopMapAddressPresenter.View) : BasePreImpl(view), IShopMapAddressPresenter  {

    val deepType = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息";

    // 搜索
    private var geocoderSearch: GeocodeSearch? = null
    private var query: PoiSearch.Query? = null
    private var poiSearch: PoiSearch? = null
    override fun search(city: String?, key: String?, latlng: LatLng?) {

        if(key?.isEmpty() == true) {
            query = PoiSearch.Query(key, "", "");
        } else {
            // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个city参数表示poi搜索区域（空字符串代表全国）
            query = PoiSearch.Query(key, deepType, city);
        }

        // 设置每页最多返回多少条poiitem
        query!!.setPageSize(20);
        // 设置查第一页
        query!!.setPageNum(0);

        poiSearch = PoiSearch(context, query);
        if(key?.isEmpty() == true) {
            val lp = LatLonPoint(latlng!!.latitude, latlng!!.longitude);
            // 设置搜索区域为以lp点为圆心，其周围3000米范围
            poiSearch?.setBound(PoiSearch.SearchBound(lp, 3000, true));
        }
        poiSearch?.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

            }

            override fun onPoiSearched(poiResult: PoiResult?, p1: Int) {
                if (poiResult == null || poiResult?.pois == null || poiResult?.pois.size == 0 || poiResult?.pois.get(
                        0
                    ) == null
                ) {
                    Toast.makeText(context, "未搜索到当前位置信息", Toast.LENGTH_SHORT).show()
                    return
                }
                view?.setList(poiResult?.pois);
            }
        });
        // 异步搜索
        poiSearch?.searchPOIAsyn();
    }


}