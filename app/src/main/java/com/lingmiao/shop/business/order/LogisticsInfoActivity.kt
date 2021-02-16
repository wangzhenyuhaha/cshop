package com.lingmiao.shop.business.order

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.LogisticsInfo
import com.lingmiao.shop.business.order.bean.LogisticsInfoData
import com.lingmiao.shop.business.order.presenter.LogisticsInfoPresenter
import com.lingmiao.shop.business.order.presenter.impl.LogisticsInfoPresenterImpl
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.order_activity_logistics_info.*

/**
 *   物流信息
 */
class LogisticsInfoActivity : BaseActivity<LogisticsInfoPresenter>(), LogisticsInfoPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private lateinit var adapter: BaseQuickAdapter<LogisticsInfoData, BaseViewHolder>

    override fun getLayoutId(): Int {
        return R.layout.order_activity_logistics_info
    }

    override fun initView() {
//        intent.putExtra("orderId", orderBean.sn)
//        intent.putExtra("shipNo", orderBean.shipNo)
//        intent.putExtra("logiId", orderBean.logiId)
        mToolBarDelegate.setMidTitle("物流信息")
        var orderId: String? = intent.getStringExtra("orderId")
        var shipNo: String? = intent.getStringExtra("shipNo")
        var logiId: String? = intent.getStringExtra("logiId")
        var logiName: String? = intent.getStringExtra("logiName")
//        if(BuildConfig.DEBUG){
//            //todo lqx
//            shipNo = "YT2069595538689"
//            logiId = "1"
//        }


        adapter = object :
            BaseQuickAdapter<LogisticsInfoData, BaseViewHolder>(R.layout.order_adapter_logistics_info) {
            override fun convert(helper: BaseViewHolder, item: LogisticsInfoData) {
                val ivShipStatusSelected = helper.getView<ImageView>(R.id.ivShipStatusSelected)
                val ivShipStatusUnselected = helper.getView<ImageView>(R.id.ivShipStatusUnselected)
                val viShipDivideTop = helper.getView<View>(R.id.viShipDivideTop)
                val viShipDivideBottom = helper.getView<View>(R.id.viShipDivideBottom)

                if(item.position=="first"){
                    viShipDivideTop.visibility = View.INVISIBLE
                }else{
                    viShipDivideTop.visibility = View.VISIBLE
                }

                if(item.position=="last"){
                    viShipDivideBottom.visibility = View.INVISIBLE
                }else{
                    viShipDivideBottom.visibility = View.VISIBLE
                }

                if (item.sign) {
//                    ivShipStatus.setImageResource(R.mipmap.logistics_info_selected)
                    ivShipStatusSelected.visibility = View.VISIBLE
                    ivShipStatusUnselected.visibility = View.GONE

                } else {
//                    ivShipStatus.setImageResource(R.drawable.corner_logistics_info_unselected)
                    ivShipStatusSelected.visibility = View.GONE
                    ivShipStatusUnselected.visibility = View.VISIBLE
                }

                if (item.sign) {
                    helper.setTextColor(
                        R.id.tvShipInfo,
                        ContextCompat.getColor(context, R.color.color_0EA60)
                    )
                    helper.setTextColor(
                        R.id.tvShipTime,
                        ContextCompat.getColor(context, R.color.color_0EA60)
                    )
                } else {
                    helper.setTextColor(
                        R.id.tvShipInfo,
                        ContextCompat.getColor(context, R.color.color_C6C6C6)
                    )
                    helper.setTextColor(
                        R.id.tvShipTime,
                        ContextCompat.getColor(context, R.color.color_C6C6C6)
                    )
                }

                helper.setText(R.id.tvShipInfo, item.info)
                helper.setText(R.id.tvShipTime, item.time)


            }

        }

        rvLogisticsInfo.layoutManager = LinearLayoutManager(this)
        rvLogisticsInfo.adapter = adapter
        val headerView =
            LayoutInflater.from(this).inflate(R.layout.order_header_logistics_info, null)
        val tvLogisticsCompanyName = headerView.findViewById<TextView>(R.id.tvLogisticsCompanyName)
        val tvLogisticsCompanyPhone =
            headerView.findViewById<TextView>(R.id.tvLogisticsCompanyPhone)
        val tvLogisticsNumber = headerView.findViewById<TextView>(R.id.tvLogisticsNumber)
        val ivLogisticsNumberCopy = headerView.findViewById<ImageView>(R.id.ivLogisticsNumberCopy)
        adapter.addHeaderView(headerView)

        tvLogisticsCompanyName.text = logiName
//        tvLogisticsCompanyPhone.text = "95533"
        tvLogisticsNumber.text = shipNo
        ivLogisticsNumberCopy.setOnClickListener { OtherUtils.copyToClipData(shipNo)}
        tvLogisticsCompanyPhone.setOnClickListener { OtherUtils.goToDialApp(this,tvLogisticsCompanyPhone.text.toString()) }

//        val tempList = mutableListOf<LogisticsInfoData>()
//        tempList.add(LogisticsInfoData("2020-07-01 12:11:11", "已签收", true,"first"))
//        tempList.add(
//            LogisticsInfoData(
//                "2020-07-01 12:11:11",
//                "上海青浦分区公司-徐泾六区 18812345678 xxxx",
//                false
//            )
//        )
//        tempList.add(
//            LogisticsInfoData(
//                "2020-07-01 12:11:11",
//                "上海青浦分区公司-徐泾六区 18812345678 xxxx",
//                false
//            )
//        )
//        tempList.add(
//            LogisticsInfoData(
//                "2020-07-01 12:11:11",
//                "上海青浦分区公司-徐泾六区 18812345678 xxxx",
//                false
//            )
//        )
//        tempList.add(
//            LogisticsInfoData(
//                "2020-07-01 12:11:11",
//                "上海青浦分区公司-徐泾六区 18812345678 xxxx",
//                false,"last"
//            )
//        )
//
//        adapter.setNewData(tempList)
        if (shipNo != null && logiId != null) {
            showPageLoading()
            mPresenter.requestLogisticsInfoData(shipNo, logiId)
        }
    }


    override fun createPresenter(): LogisticsInfoPresenter {
        return LogisticsInfoPresenterImpl(this, this)
    }

    override fun onLogisticsInfoSuccess(bean: LogisticsInfo) {
        hidePageLoading()
        val tempList= mutableListOf<LogisticsInfoData>()
        bean.data?.let { tempList.addAll(it) }
        if(tempList.size>0){
            tempList[0].position = "first"
            tempList[0].sign = true
        }
        if(tempList.size>1){
            tempList[tempList.size-1].position = "last"
        }
        adapter.setNewData(tempList)

    }

    override fun onLogisticsInfoError(code: Int) {
        hidePageLoading()
    }

}