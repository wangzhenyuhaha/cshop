package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.TranslateAnimation
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener
import com.blankj.utilcode.util.KeyboardUtils
import com.fox7.map.AMapUtil
import com.fox7.map.ToastUtil
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.exts.singleClick
import com.james.common.utils.permission.interceptor.LocationInterceptor
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.bean.AddressData
import com.lingmiao.shop.business.main.bean.ApplyShopAddress
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.main.presenter.ApplyShopAddressPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopAddressPresenterImpl
import kotlinx.android.synthetic.main.main_activity_address.*
import org.greenrobot.eventbus.EventBus

/**
Create Date : 2021/4/203:06 PM
Auther      : Fox
Desc        :
 **/
class AddressActivity : BaseActivity<ApplyShopAddressPresenter>(),
    ApplyShopAddressPresenter.View {

    // 地图
    private var markerOption: MarkerOptions? = null
    private var aMap: AMap? = null

    // 定位
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mFirstFix = false


    // 搜索
    private var geocoderSearch: GeocodeSearch? = null
    private var query: PoiSearch.Query? = null
    private var poiSearch: PoiSearch? = null

    // 缩放比例
    private var zoom = 19.0f;

    private var addressData : AddressData? = null
    // 城市
    private var city : String? = "";
    // 经伟度
    private var latlng: LatLng? = null;
    // 目标marker
    private var marker: Marker? = null
    // 地图高
    private var mMapHeight = 0
    // 地图宽
    private var mMapWidth = 0

    companion object {

        fun openActivity(context: Context, latLng: LatLng?) {
            if (context is Activity) {
                val intent = Intent(context, AddressActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, latLng)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        latlng = intent.getParcelableExtra<LatLng>(IConstant.BUNDLE_KEY_OF_ITEM);
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_address;
    }

    override fun createPresenter(): ApplyShopAddressPresenter {
        return ApplyShopAddressPresenterImpl(this, this)
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("店铺地址")
        mToolBarDelegate.setRightText("保存", ContextCompat.getColor(context,R.color.white), View.OnClickListener {
            if(latlng != null) {
                EventBus.getDefault().post(ApplyShopPoiEvent(addressData))
                finish();
            }
        });

        initGeoSearch()

        initMap();

        initData();

        // 搜索
        btnSearch.singleClick {
            KeyboardUtils.hideSoftInput(it);
            searchAddressByKeyword(etShopAddress.text.toString())
        }
    }

    private fun initMap() {
        /*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        // 此方法必须重写
         mvShopAddress.onCreate(savedInstanceState)

        if (aMap == null) {
            aMap = mvShopAddress.getMap()
            //旋转手势是否可用
            aMap?.uiSettings?.isRotateGesturesEnabled = false
            //是否显示指南针
            aMap?.uiSettings?.isCompassEnabled = false
        }

        mapPreViewListener()

        mapListener();
    }

    private fun initData() {
        if(latlng == null) {

            doIntercept(LocationInterceptor(context),failed = {}){
                //显示定位层并且可以触发定位
                // aMap?.isMyLocationEnabled = true;
                locate();
            }
        } else {

            geo();

            mapMove()

            addMarkersToMap()
        }
    }

    private fun mapListener() {
        // 地图状态侦听
        aMap?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(cameraPosition: CameraPosition) {

            }
            override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
                // 记录缩放比例
                zoom = cameraPosition.zoom

                //屏幕中心的Marker跳动
                startJumpAnimation()
            }
        })
        // 定位源
        aMap?.setLocationSource(object : LocationSource {
            override fun deactivate() {
                mListener = null
                stopLocation()
            }

            override fun activate(listener: LocationSource.OnLocationChangedListener?) {
                mListener = listener

                locate();
            }

        });
    }

    private fun locate() {
        if (mlocationClient == null) {
            mlocationClient = AMapLocationClient(context)
            mLocationOption = AMapLocationClientOption()
            //设置定位监听
            mlocationClient?.setLocationListener(mLocationListener)
            //设置为高精度定位模式
            mLocationOption?.setLocationMode(AMapLocationMode.Hight_Accuracy)
            //设置定位参数
            mlocationClient?.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
        }
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient?.startLocation()
    }

    private fun stopLocation() {
        if (mlocationClient != null) {
            mlocationClient?.stopLocation()
            mlocationClient?.setLocationListener(null);
            mlocationClient?.onDestroy()
        }
        mlocationClient = null
    }

    private fun mapPreViewListener() {
        val vto: ViewTreeObserver = mvShopAddress.getViewTreeObserver()
        //获取地图view的宽高
        vto.addOnPreDrawListener {
            mMapHeight = mvShopAddress.getMeasuredHeight()
            mMapWidth = mvShopAddress.getMeasuredWidth()

            marker?.setPositionByPixels(mMapWidth / 2, mMapHeight / 2)
            true
        }
    }

    /**
     * 定位
     */
    val mLocationListener : AMapLocationListener = object : AMapLocationListener {
        override fun onLocationChanged(amapLocation: AMapLocation?) {
            if (amapLocation != null) {//mListener != null &&
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    stopLocation();
                    //mlocationClient?.stopLocation();
                    //mlocationClient?.setLocationListener(null);
                    //省信息
                    //省信息
                    var mProvince = amapLocation.getProvince()
                    //城市信息
                    //城市信息
                    var mCity = amapLocation.getCity()
                    //城区信息
                    //城区信息
                    var mArea = amapLocation.getDistrict()
                    //街道信息
                    //街道信息
                    val street: String = amapLocation.getStreet()
                    //街道门牌号信息
                    //街道门牌号信息
                    val streetNum: String = amapLocation.getStreetNum()
                    //详细地址
                    //详细地址
                    val mAddress = street + streetNum

                    if (!mFirstFix) {
                        //城市信息
                        city = amapLocation.getCity()
                        latlng = LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())

                        Log.e("定位", "province:$mProvince--city:$mCity--area:$mArea--street:$street--streetNum:$streetNum--mAddress:$mAddress")

                        Log.e("定位", "latlng : $amapLocation")

                        addressData = AddressData();
                        addressData?.province = mProvince;
                        addressData?.city = mCity;
                        addressData?.district = mArea;
                        addressData?.address = mAddress;
                        addressData?.latLng = latlng;
                        mFirstFix = true

                        mapMove();

                        addMarkersToMap();

                    }
                } else {
                    val errText =
                        "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo()
                    Log.e("AmapErr", errText)
                }
            }
        }
    };

    //dip和px转换
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap() {
        if (marker != null) {
            marker?.position = latlng;
        } else {
            val icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location)
            markerOption = MarkerOptions().icon(icon).position(latlng).draggable(true)
            marker = aMap!!.addMarker(markerOption)
        }

        val screenPosition = aMap!!.projection.toScreenLocation(latlng)
        marker?.setPositionByPixels(screenPosition.x, screenPosition.y)

        Log.e("定位", "lt:$latlng?.latitude -- lon:$latlng?.longitude")
        Log.e("定位", "x:$screenPosition.x -- y:$screenPosition.y")
    }

    fun startJumpAnimation() {

        if(marker == null) {
            addMarkersToMap();
        }

        if(marker != null) {

            //根据屏幕距离计算需要移动的目标点
            val latLng: LatLng = marker!!.getPosition()
            val point = aMap!!.projection.toScreenLocation(latLng)
            point.y -= dip2px(this, 25f)
            val target = aMap!!.projection.fromScreenLocation(point)
            //使用TranslateAnimation,填写一个需要移动的目标点
            //使用TranslateAnimation,填写一个需要移动的目标点
            val animation: Animation = TranslateAnimation(target)
            animation.setInterpolator { input -> // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    (0.5f - 2 * (0.5 - input) * (0.5 - input)).toFloat()
                } else {
                    (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input).toDouble())).toFloat()
                }
            }
            //整个移动所需要的时间
            //整个移动所需要的时间
            animation.setDuration(600)
            //设置动画
            //设置动画
            marker!!.setAnimation(animation)
            //开始动画
            //开始动画
            marker!!.startAnimation()

            latlng = latLng;
            geo();
        }

    }

    fun mapMove() {
        aMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latlng,
                zoom
            )
        )
        val screenPosition = aMap!!.projection.toScreenLocation(latlng);

        Log.e("定位", "1x:$screenPosition.x -- y:$screenPosition.y")
    }


    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mvShopAddress.onResume()
        mapPreViewListener()
    }

    /**
     * 方法必须重写
     */
    override fun onPause(): Unit {
        super.onPause()
        mvShopAddress.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvShopAddress.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy(): Unit {
        super.onDestroy()
        mvShopAddress.onDestroy()
    }

    fun searchAddressByKeyword(key : String) {
//        query = PoiSearch.Query(key, "", city)
        query = PoiSearch.Query(key, "")
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        // 设置每页最多返回多少条poiitem
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        // 设置每页最多返回多少条poiitem
        query!!.pageSize = 10
        //设置查询页码
        //设置查询页码
        query!!.pageNum = 0

        poiSearch = PoiSearch(context, query)
        poiSearch?.setOnPoiSearchListener(object : OnPoiSearchListener{
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

            }

            override fun onPoiSearched(poiResult: PoiResult?, p1: Int) {
                if(poiResult == null || poiResult?.pois == null || poiResult?.pois.size == 0 || poiResult?.pois.get(0) == null) {
                    Toast.makeText(context, "未搜索到当前位置信息", Toast.LENGTH_SHORT).show()
                    return
                }
                val poiItem: PoiItem = poiResult.getPois().get(0);

                latlng = LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude)

                addressData = AddressData();
                addressData?.province = poiItem.provinceName;
                addressData?.city = poiItem.cityName;
                addressData?.district = poiItem.adName;
                addressData?.address = poiItem.title;
                addressData?.latLng = latlng;

                Log.e("定位", "new latlng :$latlng")

                mapMove();

            }

        })

        poiSearch?.searchPOIAsyn()
    }

    private fun initGeoSearch() {
        geocoderSearch = GeocodeSearch(this)
        geocoderSearch!!.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
            override fun onRegeocodeSearched(result: RegeocodeResult, rCode: Int) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if(result?.regeocodeAddress == null || result.regeocodeAddress.formatAddress == null) {
                        ToastUtil.show(this@AddressActivity, "对不起，没有搜索到相关数据！");
                        return;
                    }

                    addressData = AddressData();
                    addressData?.province = result.regeocodeAddress.province;
                    addressData?.city = result.regeocodeAddress.city;
                    addressData?.district = result.regeocodeAddress.district;
                    addressData?.address = result.regeocodeAddress.formatAddress;
                    addressData?.latLng = latlng;

                    city = result.regeocodeAddress.city;

                } else {
                    ToastUtil.showerror(this@AddressActivity, rCode)
                }
            }

            override fun onGeocodeSearched(geocodeResult: GeocodeResult, i: Int) {

            }
        })
    }

    private fun geo() {
        if(latlng != null) {
            val query = RegeocodeQuery(
                // 第一个参数表示一个Latlng，
                LatLonPoint(latlng!!.latitude, latlng!!.longitude),
                // 第二参数表示范围多少米
                2000.0f,
                // 第三个参数表示是火系坐标系还是GPS原生坐标系
                GeocodeSearch.AMAP
            )
            // 设置异步逆地理编码请求
            geocoderSearch?.getFromLocationAsyn(query)
        }
    }

    override fun onApplyShopAddressSuccess(bean: ApplyShopAddress) {

    }

    override fun onApplyShopAddressError(code: Int) {

    }

}