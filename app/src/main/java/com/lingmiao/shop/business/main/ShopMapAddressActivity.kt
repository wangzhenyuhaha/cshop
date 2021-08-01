package com.lingmiao.shop.business.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.amap.api.maps.model.animation.TranslateAnimation
import com.amap.api.services.core.PoiItem
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.adapter.AddressAdapter
import com.lingmiao.shop.business.main.bean.AddressData
import com.lingmiao.shop.business.main.presenter.IShopMapAddressPresenter
import com.lingmiao.shop.business.main.presenter.impl.ShopMapAddressPresenterImpl
import com.lingmiao.shop.databinding.MainActivityShopMapAddressBinding
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.initAdapter
import com.lingmiao.shop.widget.TextWatcherAdapter
import com.luck.picture.lib.tools.ScreenUtils.dip2px


/**
Create Date : 2021/7/2710:42 上午
Auther      : Fox
Desc        :
 **/
class ShopMapAddressActivity : BaseVBActivity<MainActivityShopMapAddressBinding, IShopMapAddressPresenter>(), IShopMapAddressPresenter.View  {

    // 地图
    private var markerOption: MarkerOptions? = null
    private var aMap: AMap? = null

    // 定位
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null

    // 缩放比例
    private var zoom = 19.0f;

    // 目标marker
    private var marker: Marker? = null

    // 地图高
    private var mMapHeight = 0

    // 地图宽
    private var mMapWidth = 0

    private lateinit var mMapAdapter : AddressAdapter;

    private var province: String? = null
    private var city: String? = null
    private var district: String? = null
    private var address: String? = null
    // 经伟度
    private var latlng: LatLng? = null;
    private var addressData: AddressData? = null

    private var lastSearchTime: Long = 0

    var isAutoSet = false;

    companion object {

        fun openActivity(context: Context, code : Int, shopProvince : String?, shopCity: String?, shopCounty: String?, shopAdd: String?, latLng: LatLng?) {
            if (context is Activity) {
                //latLng: LatLng?, address: String?
                //LatLng(shopManage?.shopLat?: 0.0, shopManage?.shopLng?:0.0), shopManage?.shopAdd
                val intent = Intent(context, ShopMapAddressActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, latLng)
                intent.putExtra("address", shopAdd)
                intent.putExtra("province", shopProvince)
                intent.putExtra("city", shopCity)
                intent.putExtra("district", shopCounty)
                context.startActivityForResult(intent, code)
            }
        }
    }

    override fun initBundles() {
        latlng = intent.getParcelableExtra<LatLng>(IConstant.BUNDLE_KEY_OF_ITEM);
        address = intent.getStringExtra("address");
        province = intent.getStringExtra("province")
        city = intent.getStringExtra("city")
        district = intent.getStringExtra("district")
    }

    override fun createPresenter(): IShopMapAddressPresenter {
        return ShopMapAddressPresenterImpl(this, this);
    }

    override fun getViewBinding(): MainActivityShopMapAddressBinding {
        return MainActivityShopMapAddressBinding.inflate(layoutInflater);
    }

    override fun useBaseLayout(): Boolean {
        return true;
    }

    var textWatcherAdapter : TextWatcherAdapter = object : TextWatcherAdapter(){
        override fun afterTextChanged(s: Editable?) {
            if (System.currentTimeMillis() - lastSearchTime < 1000) {
                return;
            }
            lastSearchTime = System.currentTimeMillis()
            doSearchQuery(s.toString());
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
    }

    override fun initView() {
        rlBaseRoot?.fitsSystemWindows = false
        // 不显示头部
        mToolBarDelegate?.hideToolBar()
        // 沉浸透明
        OtherUtils.setStatusBarTransparent(this)

        // 输入
        mBinding.etShopAddress.addTextChangedListener(textWatcherAdapter);
        // 列表滚动
        mBinding.rvAddress.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (Math.abs(dy) < 10) {
                    return;
                }
                if (dy > 0) {//向上滑
                    KeyboardUtils.hideSoftInput(recyclerView);
                }
            }
        });
        // 取消搜索
        mBinding.tvShopAddressCancel.singleClick {
            KeyboardUtils.hideSoftInput(it);
             finish();
        }
        // 确定地图选址
        mBinding.tvShopAddressConfirm.singleClick {
            KeyboardUtils.hideSoftInput(it);
            if(mMapAdapter.mCheckedPosition > -1) {
                val item = mMapAdapter.getItem(mMapAdapter.mCheckedPosition) as PoiItem;
                latlng = LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude)

                addressData = AddressData();
                addressData?.province = item?.provinceName;
                addressData?.city = item.cityName;
                addressData?.district = item.adName;
                addressData?.latLng = latlng;
                addressData?.address = item?.snippet;

                var intent = Intent();
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, addressData);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
        // 输入焦点
        mBinding.etShopAddress.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                } else {
                    KeyboardUtils.hideSoftInput(v);
                }
            };

        // 定位
        mBinding.ivLocation.singleClick {
            requestLocationPermission()
        }

        initMap()

        initRecycleView();

        initData()

    }

    fun doSearchQuery(key : String?) {
        // 进行poi搜索时清除掉地图点击事件
        aMap?.setOnMapClickListener(null);
        mPresenter?.search(city, key, latlng);
    }

    private fun initRecycleView() {
        mMapAdapter = AddressAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                KeyboardUtils.hideSoftInput(view);

                val item = adapter.data[position] as PoiItem;

                latlng = LatLng(item.latLonPoint.latitude, item.latLonPoint.longitude)

                addressData = AddressData();
                addressData?.province = item.provinceName;
                addressData?.city = item.cityName;
                addressData?.district = item.adName;
                addressData?.address = item.title;
                addressData?.latLng = latlng;

                Log.e("定位", "new latlng :$latlng")

                mapMove();

                mMapAdapter.setCheckPosition(position);
            }
        };
        mBinding.rvAddress.initAdapter(mMapAdapter);
    }

    private fun initData() {
        if (latlng == null) {
            //请求权限
            requestLocationPermission()
        } else {

            mapMove()

            addMarkersToMap()

            startJumpAnimation()

        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            val checkList = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val needRequestPermission = checkPermission(this, checkList)
            if (needRequestPermission.isEmpty()) {
                doIt()
            } else {
                requestPermissions(needRequestPermission.toTypedArray(), 32)
                doIt()
            }
        } else {
            doIt()
        }

    }

    //检查权限，返回需要申请的权限
    private fun checkPermission(context: Context, checkList: Array<String>): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        for (i in 1..checkList.size) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    context,
                    checkList[i - 1]
                )
            ) {
                list.add(checkList[i - 1])
            }
        }
        return list
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            32 -> {
                if (grantResults.isNotEmpty()) {
                    var a: Boolean = true
                    var b: Int = 0
                    for (i in grantResults) {
                        ++b
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            a = false
                        }
                    }
                    if (!a) {
                        Log.d("AddressActivityTAG", "未全部获取权限")
                    } else {
                        doIt()
                    }
                } else {
                    Log.d("AddressActivityTAG", "未获取权限")
                }
            }
        }
    }

    private fun doIt() {
        locate()
    }

    private fun locate() {
        if (mlocationClient == null) {
            mlocationClient = AMapLocationClient(context)
            mLocationOption = AMapLocationClientOption()
            //设置定位监听
            mlocationClient?.setLocationListener(mLocationListener)
            //设置为高精度定位模式
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient?.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
        }
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient?.startLocation()
    }

    private fun initMap() {
        /*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
        // Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        // MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        // 此方法必须重写
        mBinding.mvShopAddress.onCreate(savedInstanceState)

        if (aMap == null) {
            aMap = mBinding.mvShopAddress.getMap()
            //旋转手势是否可用
            aMap?.uiSettings?.isRotateGesturesEnabled = false
            //是否显示指南针
            aMap?.uiSettings?.isCompassEnabled = false
            // 是否允许显示缩放按钮
            aMap?.uiSettings?.isZoomControlsEnabled = false
            // 高德地图的 logo 默认在左下角显示，不可以移除，但支持调整到固定位置
            aMap?.uiSettings?.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_RIGHT;
            aMap?.uiSettings?.setLogoBottomMargin(-100);
            // 显示默认的定位按钮
            // aMap?.uiSettings?.isMyLocationButtonEnabled = true;
            // 可触发定位并显示当前位置
            // aMap?.setMyLocationEnabled(true);
        }

        mapPreViewListener()

        mapListener();
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
                latlng = cameraPosition.target;
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

    private fun mapPreViewListener() {
        val vto: ViewTreeObserver = mBinding.mvShopAddress.getViewTreeObserver()
        //获取地图view的宽高
        vto.addOnPreDrawListener {
            mMapHeight = mBinding.mvShopAddress.getMeasuredHeight()
            mMapWidth = mBinding.mvShopAddress.getMeasuredWidth()

            marker?.setPositionByPixels(mMapWidth / 2, mMapHeight / 2)
            true
        }
    }

    /**
     * 定位
     */
    val mLocationListener: AMapLocationListener = object : AMapLocationListener {
        override fun onLocationChanged(amapLocation: AMapLocation?) {
            if (amapLocation != null) {//mListener != null &&
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    stopLocation();
                    //省信息
                    var mProvince = amapLocation.getProvince()
                    //城市信息
                    var mCity = amapLocation.getCity()
                    //城区信息
                    var mArea = amapLocation.getDistrict()
                    //街道信息
                    val street: String = amapLocation.getStreet()
                    //街道门牌号信息
                    val streetNum: String = amapLocation.getStreetNum()
                    //详细地址
                    val mAddress = amapLocation.aoiName

                    latlng = LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    //marker?.position =  latlng;

                    mapMove();

                } else {
                    val errText =
                        "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo()
                    Log.e("AmapErr", errText)
                }
            }
        }
    };

    private fun stopLocation() {
        if (mlocationClient != null) {
            mlocationClient?.stopLocation()
            mlocationClient?.setLocationListener(null);
            mlocationClient?.onDestroy()
        }
        mlocationClient = null
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
    }

    fun startJumpAnimation() {
        if (marker == null) {
            addMarkersToMap();
        }
        if (marker != null) {
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
            animation.setDuration(600)
            //设置动画
            //设置动画
            marker!!.setAnimation(animation)
            //开始动画
            //开始动画
            marker!!.startAnimation()

            doSearchQuery("");
        }

    }

    override fun setList(list: List<PoiItem>?) {
        mMapAdapter.replaceData(list ?: listOf());
        if(list?.size?:0 > 0) {
            mMapAdapter.setCheckPosition(0);
        } else {
            mMapAdapter.setCheckPosition(-1);
        }
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mBinding.mvShopAddress.onResume()
        mapPreViewListener()
    }

    /**
     * 方法必须重写
     */
    override fun onPause(): Unit {
        super.onPause()
        mBinding.mvShopAddress.onPause()
    }

}