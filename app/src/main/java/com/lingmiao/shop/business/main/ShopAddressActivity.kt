package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.amap.api.maps.model.*
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.bean.AddressData
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.main.presenter.IShopAddressPresenter
import com.lingmiao.shop.business.main.presenter.impl.ShopAddressPresenterImpl
import com.lingmiao.shop.databinding.MainActivityShopAddressBinding
import org.greenrobot.eventbus.EventBus


/**
Create Date : 2021/7/2710:42 上午
Auther      : Fox
Desc        :
 **/
class ShopAddressActivity : BaseVBActivity<MainActivityShopAddressBinding, IShopAddressPresenter>(), IShopAddressPresenter.View  {

    private var province: String? = null
    private var city: String? = null
    private var district: String? = null
    private var address: String? = null
    // 经伟度
    private var latlng: LatLng? = null;
    private var addressData: AddressData? = null

    companion object {

        fun openActivity(context: Context, adInfo: AddressData?) {
            if (context is Activity) {
                //latLng: LatLng?, address: String?
                //LatLng(shopManage?.shopLat?: 0.0, shopManage?.shopLng?:0.0), shopManage?.shopAdd
                val intent = Intent(context, ShopAddressActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, adInfo?.latLng)
                intent.putExtra("address", adInfo?.address)
                intent.putExtra("province", adInfo?.province)
                intent.putExtra("city", adInfo?.city)
                intent.putExtra("district", adInfo?.district)
                context.startActivity(intent)
            }
        }

        fun openActivity(context: Context, shopManage: ApplyShopInfo?) {
            if (context is Activity) {
                //latLng: LatLng?, address: String?
                //LatLng(shopManage?.shopLat?: 0.0, shopManage?.shopLng?:0.0), shopManage?.shopAdd
                val intent = Intent(context, ShopAddressActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, LatLng(shopManage?.shopLat?: 0.0, shopManage?.shopLng?:0.0))
                intent.putExtra("address", shopManage?.shopAdd)
                intent.putExtra("province", shopManage?.shopProvince)
                intent.putExtra("city", shopManage?.shopCity)
                intent.putExtra("district", shopManage?.shopCounty)
                context.startActivity(intent)
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

    override fun createPresenter(): IShopAddressPresenter {
        return ShopAddressPresenterImpl(this, this);
    }

    override fun getViewBinding(): MainActivityShopAddressBinding {
        return MainActivityShopAddressBinding.inflate(layoutInflater);
    }

    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("店铺地址")

        // 选择地址
        mBinding.layoutAddress.singleClick {
            ShopMapAddressActivity.openActivity(this, 500, province, city, district, address, latlng);
        }

        // 提交
        mBinding.tvSubmit.singleClick {
            if(addressData == null || addressData?.latLng == null) {
                showToast("请先标记店铺的位置")
                return@singleClick;
            }
            if(mBinding.etDetailAddress.text.isEmpty()) {
                showToast("请输入店铺详情地址")
                return@singleClick;
            }
            addressData?.address = mBinding.etDetailAddress.text.toString();
            EventBus.getDefault().post(ApplyShopPoiEvent(addressData))
            finish();
        }

        resetLocation();
    }

    fun resetLocation() {
        mBinding.etDetailAddress.setText(address);
        mBinding.tvAddressDetail.setText(String.format("%s%s%s"?:"", province?:"", city?:"", district?:""));
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 500 && resultCode == Activity.RESULT_OK) {
            addressData = data?.getParcelableExtra(IConstant.BUNDLE_KEY_OF_ITEM) as AddressData;
            address = addressData?.address;
            province = addressData?.province;
            city = addressData?.city;
            district = addressData?.district;
            latlng = addressData?.latLng;
            resetLocation();
        }
    }
}