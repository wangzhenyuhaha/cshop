package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.AddressData
import com.lingmiao.shop.business.main.bean.ApplyShopCategory
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import kotlinx.android.synthetic.main.goods_adapter_goods_menu.*
import java.lang.Exception

/**
 *   首页-提交资料
 */
class ApplyShopInfoActivity : BaseActivity<ApplyShopInfoPresenter>(), ApplyShopInfoPresenter.View {

    companion object {
        const val PERSONAL = 0
        const val COMPANY = 1

        const val LICENSE = 2
        const val SHOP_FRONT = 3
        const val SHOP_INSIDE = 4

        const val ID_CARD_FRONT = 5
        const val ID_CARD_BACK = 6
        const val ID_CARD_HAND = 7

        //跳转传图Activity
        fun goUploadImageActivity(
            activity: Activity,
            type: Int,
            title: String,
            imageUrl: String? = null
        ) {
            val intent = Intent(activity, ApplyShopUploadActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("title", title)
            intent.putExtra("imageUrl", imageUrl)
            activity.startActivity(intent)
        }

    }

    private val viewModel by viewModels<ApplyShopInfoViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_info
    }


    override fun createPresenter(): ApplyShopInfoPresenter {
        return ApplyShopInfoPresenterImpl(this, this)
    }

    override fun initView() {

        initObserver()

        val loginInfo = UserManager.getLoginInfo()
        //审核失败，获取信息
        loginInfo?.let {
            if (it.shopStatus != null && it.shopStatus != "UN_APPLY") mPresenter.requestShopInfoData()
        }


    }

    private fun initObserver() {

        //修改页面title
        viewModel.title.observe(this, Observer {
            mToolBarDelegate.setMidTitle(it)
        })


        viewModel.go.observe(this, Observer {
            when (it) {
                0 -> {
                    //企业对公账户结算

                }
                1 -> {
                    //企业对私账户结算

                    //暂时


                    try {
                        Log.d("ABC", "Do It")
                        val resp =
                            mPresenter?.requestApplyShopInfoData(viewModel.applyShopInfo.value!!)

                    } catch (e: Exception) {
                        Log.d("WZY", "错了")
                    }


                }
                2 -> {
                    //个体户对公账户结算

                }
                3 -> {
                    //个体户对私账户结算

                }
            }
//            if (it) {
//                try {
//
//                    mPresenter?.requestApplyShopInfoData(viewModel.applyShopInfo.value!!)
//                } catch (e: Exception) {
//                    Log.d("WZY", "错了")
//                }
//            }
        })
    }

    override fun useLightMode(): Boolean {
        return false
    }

    //申请失败后成功获取已上传数据
    override fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {

        viewModel.onShopInfoSuccess(applyShopInfo)


    }

    //申请失败后未获取已上传数据
    override fun onShopInfoError(code: Int) {

    }


    //申请店铺成功
    override fun onApplyShopInfoSuccess() {
        hideDialogLoading()
        showToast("申请成功")
        //  EventBus.getDefault().post(ApplyShopInfoEvent())
        finish()
    }

    //申请店铺失败
    override fun onApplyShopInfoError(code: Int) {
        hideDialogLoading()
    }


    class ApplyShopInfoViewModel : ViewModel() {

        //店铺经营类型
        private val _selectedCategoryListLiveData: MutableLiveData<List<ApplyShopCategory>> =
            MutableLiveData()

        val selectedCategoryListLiveData: LiveData<List<ApplyShopCategory>>
            get() = _selectedCategoryListLiveData

        //更改主营类目
        fun setCategory(list: List<ApplyShopCategory>) {
            _selectedCategoryListLiveData.value = list
        }


        //店铺信息
        private val _applyShopInfo = MutableLiveData<ApplyShopInfo>().also {
            it.value = ApplyShopInfo()
        }

        val applyShopInfo: LiveData<ApplyShopInfo>
            get() = _applyShopInfo

        //成功加载Info
        fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {
            _applyShopInfo.value = applyShopInfo
        }

        //店铺地址
        private val _adInfo: MutableLiveData<AddressData> = MutableLiveData()

        val adInfo: LiveData<AddressData>
            get() = _adInfo

        //更改店铺地址
        fun setAddress(data: AddressData) {
            _adInfo.value = data
        }

        //title
        private val _title: MutableLiveData<String> = MutableLiveData()

        val title: LiveData<String>
            get() = _title

        //更改Title
        fun setTitle(title: String) {
            _title.value = title
        }


        // 0  企业对公账户结算  ，  1  企业对私账户结算   ， 2  个体户对公账户结算     3   个体户对私账户结算
        val go: MutableLiveData<Int> = MutableLiveData()

    }


}