package com.lingmiao.shop.business.main

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ReflectUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.permission.interceptor.LocationInterceptor
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.ApplyShopAddress
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.main.presenter.ApplyShopAddressPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopAddressPresenterImpl
import com.lingmiao.shop.util.OtherUtils
import com.tencent.lbssearch.TencentSearch
import com.tencent.lbssearch.`object`.param.Geo2AddressParam
import com.tencent.lbssearch.`object`.param.SearchParam
import com.tencent.lbssearch.`object`.result.Geo2AddressResultObject
import com.tencent.lbssearch.`object`.result.SearchResultObject
import com.tencent.lbssearch.httpresponse.AdInfo
import com.tencent.lbssearch.httpresponse.BaseObject
import com.tencent.lbssearch.httpresponse.Poi
import com.tencent.map.geolocation.*
import com.tencent.map.tools.net.http.HttpResponseListener
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.maps.LocationSource
import com.tencent.tencentmap.mapsdk.maps.LocationSource.OnLocationChangedListener
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import com.tencent.tencentmap.mapsdk.maps.model.*
import kotlinx.android.synthetic.main.main_activity_apply_shop_address.*
import org.greenrobot.eventbus.EventBus


/**
 *   首页-提交资料-输入地址
 */
class ApplyShopAddressActivity : BaseActivity<ApplyShopAddressPresenter>(),
    ApplyShopAddressPresenter.View,
    LocationSource {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var mapView: TencentMap? = null
    private var locationManager: TencentLocationManager? = null
    private var locationChangedListener: OnLocationChangedListener? = null
    private var locationRequest: TencentLocationRequest? = null
    private var mLocationListener: TencentLocationListener? = null

    private var selectedIndex = -1
    private var lastSearchTime: Long = 0
    private val tencentSearch = TencentSearch(this)
    private var currentLatLng:LatLng?=null
    private var city = "深圳"
    private var customMarker:Marker?=null
    private var adInfo:AdInfo?=null

    private val adapter =
        object : BaseQuickAdapter<Poi, BaseViewHolder>(R.layout.main_adapter_shop_address) {
            override fun convert(helper: BaseViewHolder, item: Poi) {
                helper.setText(R.id.tvShopAddressTitle, item.title)
                val ivShopAddressSelected = helper.getView<ImageView>(R.id.ivShopAddressSelected)
                ivShopAddressSelected.visibility =
                    if (helper.layoutPosition == selectedIndex) View.VISIBLE else View.GONE
                helper.setText(
                    R.id.tvShopAddressDesc,
                    tranDistanceString(item._distance) + " | " + item.address
                )
            }

        }

    private fun tranDistanceString(distance: Float): String {
        return when {
            distance <= 100 -> {
                "100m内"
            }
            distance <= 1000 -> {
                "1km内"
            }
            else -> {
                (distance / 1000).toInt().toString() + "km"
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_address
    }

    override fun useLightMode(): Boolean {
        return true
    }

    override fun initView() {
        rlBaseRoot.fitsSystemWindows = false
        mToolBarDelegate.hideToolBar()
        OtherUtils.setStatusBarTransparent(this)

        etShopAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (System.currentTimeMillis() - lastSearchTime < 1000) return
                lastSearchTime = System.currentTimeMillis()
                searchAddressByKeyword(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        tvShopAddressCancel.setOnClickListener {
            finish()
        }

        tvShopAddressConfirm.setOnClickListener {
            if(selectedIndex<0){
                showToast("请选择地址")
                return@setOnClickListener
            }
            try {
              val poi =  adapter.data[selectedIndex]
                //EventBus.getDefault().post(ApplyShopPoiEvent(poi,adInfo))
                // finish()
            }catch (e:Exception){
                e.printStackTrace()
            }

        }

        initAdapter()
        initMap()

    }

    private fun getAddress(tencentLocation: LatLng?) {

        currentLatLng = LatLng(
            tencentLocation?.latitude!!,
            tencentLocation?.longitude!!
        )
//        if(tencentLocation.address.contains("市")){
//            var tempCity = tencentLocation.address.substring(0,tencentLocation.address.indexOf("市")+1)
//            try {
//                if(tempCity.contains("省")){
//                    tempCity = tempCity.substring(tempCity.indexOf("省")+1)
//                    city = tempCity
//                    LogUtils.d("城市:"+tempCity)
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//
//        }


        //https://lbs.qq.com/mobile/androidMapSDK/developerGuide/drawPoints
        //创建Marker对象之前，设置属性
        val position = LatLng(tencentLocation.latitude, tencentLocation.longitude)
        addMyMarker(position)

        reGeocoder(position,false)
        //                        mCoroutine.launch {
        //                            delay(5000)
        //                            reGeocoder(position)
        //                        }

    }


    var zoom = 17.0f;
    private fun initMap() {
        mapView = mvShopAddress.map
//        mapView?.setOnMarkerDragListener(object : TencentMap.OnMarkerDragListener{
//            override fun onMarkerDragEnd(marker: Marker?) {
//                val latLng = marker?.getPosition()
//                val latitude: Double = latLng?.latitude?:0.0
//                val longitude: Double = latLng?.longitude?:0.0
//                getAddress(latLng);
//            }
//
//            override fun onMarkerDragStart(p0: Marker?) {
//
//            }
//
//            override fun onMarkerDrag(marker: Marker?) {
//
//            }
//
//        });
        mapView?.setOnCameraChangeListener(object : TencentMap.OnCameraChangeListener{
            override fun onCameraChangeFinished(position: CameraPosition?) {
                val latLng = position?.target
                val latitude: Double = latLng?.latitude?:0.0
                val longitude: Double = latLng?.longitude?:0.0
                getAddress(latLng);
//                val position = LatLng(latitude, longitude)
//                addMyMarker(position);
                position.apply {
                    zoom = zoom;

                }
            }

            override fun onCameraChange(position: CameraPosition?) {
//                val latLng = position?.target;
//                if(latLng != null) {
//                    val position = LatLng(latLng?.latitude, latLng?.longitude)
//                    addMyMarker(position)
//                }
            }

        });
        doIntercept(LocationInterceptor(context),failed = {}){
            //地图上设置定位数据源
            mapView?.setLocationSource(this);
            //设置当前位置可见
            mapView?.setMyLocationEnabled(true);
            //            ToastUtils.showShort("获取成功")
            locationRequest = TencentLocationRequest.create()
            locationManager = TencentLocationManager.getInstance(this)
            mLocationListener = object : TencentLocationListener {
                override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {
                    LogUtils.d(p0, p1, p2)
                }

                override fun onLocationChanged(
                    tencentLocation: TencentLocation,
                    i: Int,
                    s: String?
                ) {
                    //                    LogUtils.d(p0,p1,p2)
                    //其中 locationChangeListener 为 LocationSource.active 返回给用户的位置监听器
                    //用户通过这个监听器就可以设置地图的定位点位置

                    //其中 locationChangeListener 为 LocationSource.active 返回给用户的位置监听器
                    //用户通过这个监听器就可以设置地图的定位点位置
                    if (i == TencentLocation.ERROR_OK && locationChangedListener != null) {
                        //                        val location = Location(tencentLocation.getProvider())
                        //                        //设置经纬度
                        //                        location.setLatitude(tencentLocation.getLatitude())
                        //                        location.setLongitude(tencentLocation.getLongitude())
                        //                        //设置精度，这个值会被设置为定位点上表示精度的圆形半径
                        //                        location.setAccuracy(tencentLocation.getAccuracy())
                        //                        //设置定位标的旋转角度，注意 tencentLocation.getBearing() 只有在 gps 时才有可能获取
                        //                        location.setBearing(tencentLocation.getBearing() as Float)
                        //                        //将位置信息返回给地图
                        //                        locationChangedListener?.onLocationChanged(location)



                        currentLatLng = LatLng(
                            tencentLocation.latitude,
                            tencentLocation.longitude
                        )
                        if(tencentLocation.address.contains("市")){
                            var tempCity = tencentLocation.address.substring(0,tencentLocation.address.indexOf("市")+1)
                            try {
                                if(tempCity.contains("省")){
                                    tempCity = tempCity.substring(tempCity.indexOf("省")+1)
                                    city = tempCity
                                    LogUtils.d("城市:"+tempCity)
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }

                        }


                        //https://lbs.qq.com/mobile/androidMapSDK/developerGuide/drawPoints
                        //创建Marker对象之前，设置属性
                        val position = LatLng(tencentLocation.latitude, tencentLocation.longitude)
                        addMyMarker(position)

                        reGeocoder(position,false)
                        //                        mCoroutine.launch {
                        //                            delay(5000)
                        //                            reGeocoder(position)
                        //                        }

                    }
                }

            }
            locationManager?.requestSingleFreshLocation(
                null,
                mLocationListener,
                Looper.getMainLooper()
            )
        }
    }


    private fun addMyMarker(position: LatLng) {

//        val cameraSigma = CameraUpdateFactory.newCameraPosition(
//            CameraPosition(
//                position,  //中心点坐标，地图目标经纬度
//                zoom,  //目标缩放级别
//                0f,  //目标倾斜角[0.0 ~ 45.0] (垂直地图时为0)
//                0f
//            )
//        ) //目标旋转角 0~360° (正北方为0)

        val cameraSigma = CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(position).zoom(zoom).build());
        mapView?.moveCamera(cameraSigma) //移动地图

        val custom =
            BitmapDescriptorFactory.fromResource(R.mipmap.main_shop_location)
//        customMarker?.remove()

        if(customMarker != null) {
            customMarker?.startAnimation()
            customMarker?.position = position;
        } else {
            customMarker = mapView?.addMarker(
                MarkerOptions(position)
                    .icon(custom)
                    .alpha(0.7f)
                    .flat(true)
                    .clockwise(false)
                    .rotation(0f)
            )
        }
    }

    private fun initAdapter() {
        rvShopAddress.layoutManager = LinearLayoutManager(this)
        rvShopAddress.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            selectedIndex = position
            adapter.notifyDataSetChanged()
            val poi = adapter.data[position] as Poi
            addMyMarker(poi.latLng)
            adInfo = null
            reGeocoder(poi.latLng,true)
        }
    }


    override fun createPresenter(): ApplyShopAddressPresenter {
        return ApplyShopAddressPresenterImpl(this, this)
    }

    override fun onApplyShopAddressSuccess(bean: ApplyShopAddress) {
        hidePageLoading()
    }

    override fun onApplyShopAddressError(code: Int) {
        hidePageLoading()
    }

    override fun onResume() {
        super.onResume()
        mvShopAddress.onResume()
    }

    override fun onPause() {
        super.onPause()
        mvShopAddress.onPause()
    }

    override fun onStop() {
        super.onStop()
        mvShopAddress.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mvShopAddress.onDestroy()
    }

    override fun activate(onLocationChangedListener: OnLocationChangedListener) {
        //这里我们将地图返回的位置监听保存为当前 Activity 的成员变量
        locationChangedListener = onLocationChangedListener
        //开启定位
        var err: Int? = locationManager?.requestLocationUpdates(
            locationRequest, mLocationListener, Looper.myLooper()
        )
        when (err) {
            1 -> Toast.makeText(
                this,
                "设备缺少使用腾讯定位服务需要的基本条件",
                Toast.LENGTH_SHORT
            ).show()
            2 -> Toast.makeText(
                this,
                "manifest 中配置的 key 不正确", Toast.LENGTH_SHORT
            ).show()
            3 -> Toast.makeText(
                this,
                "自动加载libtencentloc.so失败", Toast.LENGTH_SHORT
            ).show()
            else -> {
            }
        }
    }

    override fun deactivate() {
        //当不需要展示定位点时，需要停止定位并释放相关资源
        locationManager?.removeUpdates(mLocationListener)
        locationManager = null
        locationRequest = null
        locationChangedListener = null
    }


    /**
     * 逆地理编码
     */
    protected fun reGeocoder(latLng: LatLng,onlyParse:Boolean) {
//        val str: String = etRegeocoder.getText().toString().trim()
//        val latLng: LatLng = str2Coordinate(this, str) ?: return
//        val tencentSearch = TencentSearch(this)
        //还可以传入其他坐标系的坐标，不过需要用coord_type()指明所用类型
        //这里设置返回周边poi列表，可以在一定程度上满足用户获取指定坐标周边poi的需求
        val geo2AddressParam = Geo2AddressParam(latLng).getPoi(true)
            .setPoiOptions(
                Geo2AddressParam.PoiOptions()
                    .setRadius(5000).setCategorys("房产小区,室内及附属设施")
                    .setPageIndex(1)
                    .setPageSize(20)
                    .setPolicy(Geo2AddressParam.PoiOptions.POLICY_O2O)
            )
        tencentSearch.geo2address(geo2AddressParam, object : HttpResponseListener<BaseObject?> {
            override fun onSuccess(arg0: Int, arg1: BaseObject?) {
                if (arg1 == null) {
                    return
                }
                val obj = arg1 as Geo2AddressResultObject
                adInfo = obj.result.ad_info
                LogUtils.d(adInfo)
                if(onlyParse) {

                }else{
                    adapter.setNewData(obj.result.pois)
                }
            }

            override fun onFailure(
                arg0: Int,
                arg1: String,
                arg2: Throwable?
            ) {
                LogUtils.e("error code:$arg0, msg:$arg1")
            }
        })
    }

    private fun searchAddressByKeyword(keyword: String) {
        LogUtils.d("keyword:$keyword")
        selectedIndex = -1
        //城市搜索
        val region = SearchParam.Region(city) //设置搜索范围扩大
            .autoExtend(true)

//        val bean = SearchParam(keyword,region).pageIndex(1).pageSize(20)

        val bean = ReflectUtils.reflect(SearchParam::class.java).newInstance(keyword,region).get<SearchParam>()
            .pageIndex(1).pageSize(20)
//            .boundary(SearchParam.Region("深圳市").autoExtend(true))
//        val tencentSearch = TencentSearch(this)
        tencentSearch.search(bean, object : HttpResponseListener<BaseObject> {
            override fun onSuccess(p0: Int, arg1: BaseObject) {
                LogUtils.d("onSuccess")
                LogUtils.d(arg1)
                val obj = arg1 as SearchResultObject
                val list = obj.data
                val poiList = mutableListOf<Poi>()
                for (searchResultData in list) {
                    val poi = Poi()
                    poi.title = searchResultData.title
                    poi.address = searchResultData.address
                    poi.latLng = searchResultData.latLng
                    if(currentLatLng!=null){
                        poi._distance = TencentLocationUtils.distanceBetween(poi.latLng.latitude,poi.latLng.longitude,
                            currentLatLng!!.latitude,currentLatLng!!.longitude).toFloat()
                    }
                    poiList.add(poi)

                }
                adapter.setNewData(poiList)
            }

            override fun onFailure(p0: Int, p1: String?, p2: Throwable?) {
                LogUtils.d("onFailure")
                p2?.printStackTrace()
            }

        })
    }
}

