package com.lingmiao.shop.business.me

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.ui.PhotoListActivity
import com.lingmiao.shop.business.main.ShopAddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.me.presenter.ShopInfoPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopInfoPreImpl
import com.lingmiao.shop.business.me.presenter.impl.ShopSettingPresenterImpl
import com.lingmiao.shop.databinding.ActivityOperationSettingBinding
import com.lingmiao.shop.databinding.ActivityShopInfoBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopInfoActivity :
    BaseVBActivity<ActivityShopInfoBinding, ShopInfoPresenter>(),
    ShopInfoPresenter.View {

    override fun useLightMode() = false
   // override fun useEventBus() = true
    override fun createPresenter() = ShopInfoPreImpl(this, this)

    override fun getViewBinding() = ActivityShopInfoBinding.inflate(layoutInflater)

    private var shopManage: ApplyShopInfo? = null

    private var licenceImg: String? = null

    override fun initView() {
        mToolBarDelegate?.setMidTitle("店铺信息")
        //加载数据
        mPresenter?.loadShopInfo()

        //店铺资质
        mBinding.rlShopManageQualification.setOnClickListener {
            //店铺资质
            if (licenceImg != null) {
                val bundle = Bundle().apply {
                    putStringArrayList("list", arrayListOf<String>(licenceImg!!))
                    putInt("position", 0)
                }
                val intent = Intent(context, PhotoListActivity::class.java)
                intent.putExtra("Photo", bundle)
                context?.startActivity(intent)
            }
        }
        //店铺地址
        //店铺名字,不给修改
        mBinding.rlShopManageName.setOnClickListener {
            DialogUtils.showInputDialog(
                this,
                "店铺名称",
                "",
                "请输入",
                shopManage?.shopName,
                "取消",
                "保存",
                null
            ) {
                mBinding.tvShopManageName.text = it
                shopManage?.shopName = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopName = it
                mPresenter?.updateShopManage(request)
            }
        }
    }


    override fun onLoadedShopInfo(bean: ApplyShopInfo) {
        shopManage = bean
        //店铺名称
        mBinding.tvShopManageName.text = bean.shopName
        //店铺编号
        mBinding.tvShopManageNumber.text = String.format("%s", bean.shopId)
        //店铺类型
        mBinding.tvShopManageType.text = bean.getShopTypeStr()
        //主营类目
        mBinding.tvShopManageCategory.text = bean.categoryNames?.replace(" ", "/")
        //店铺地址
        mBinding.tvShopManageAddress.text = bean.getFullAddress()

        licenceImg = bean.licenceImg
    }

    override fun onUpdateShopSuccess(bean: ApplyShopInfo) {
        hideDialogLoading()
        bean.shopLogo?.apply {
            shopManage?.shopLogo = this
        }
        bean.shopName?.apply {
            shopManage?.shopName = this
        }
        bean.shopDesc?.apply {
            shopManage?.shopDesc = this
        }
        bean.linkName?.apply {
            shopManage?.linkName = this
        }
        bean.linkPhone?.apply {
            shopManage?.linkPhone = this
        }
        bean.linkPhone?.apply {
            shopManage?.linkPhone = this
        }
    }

    override fun onUpdateShopError(code: Int) {
        hideDialogLoading()
    }

  //  var addressLatLng: LatLng? = null

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun setAddress(event: ApplyShopPoiEvent) {
//        addressLatLng = event.adInfo?.latLng
//        shopManage?.shopLng = addressLatLng?.longitude
//        shopManage?.shopLat = addressLatLng?.latitude
//        shopManage?.shopAdd = event.adInfo?.address
//        shopManage?.shopProvince = event.adInfo?.province
//        shopManage?.shopCity = event.adInfo?.city
//        shopManage?.shopCounty = event.adInfo?.district
//        mBinding.tvShopManageAddress.text = shopManage?.getFullAddress()
//
//        showDialogLoading()
//        val request = ApplyShopInfo()
//        val loginInfo = UserManager.getLoginInfo()
//        loginInfo?.let { info -> request.shopId = info.shopId }
//        request.shopLat = addressLatLng?.latitude
//        request.shopLng = addressLatLng?.longitude
//        request.shopAdd = event.adInfo?.address
//        request.shopProvince = shopManage?.shopProvince
//        request.shopCity = shopManage?.shopCity
//        request.shopCounty = shopManage?.shopCounty
//        mPresenter?.updateShopManage(request)
//    }
}