package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.me.fragment.BannerActivity
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.EVouchersDetailPresenter
import com.lingmiao.shop.business.sales.presenter.impl.EVouchersDetailPreImpl
import com.lingmiao.shop.databinding.ActivityEvouchersDetailBinding
import com.lingmiao.shop.util.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import java.util.*

class EVouchersDetailActivity :
    BaseVBActivity<ActivityEvouchersDetailBinding, EVouchersDetailPresenter>(),
    EVouchersDetailPresenter.View {

    companion object {

        //0  查看电子券详情   1  新增电子券
        private const val ELECTRONIC_VOUCHERS_TYPE = "ELECTRONIC_VOUCHERS_TYPE"

        //显示的优惠券
        private const val ELECTRONIC_VOUCHERS = "ELECTRONIC_VOUCHERS"

        fun openActivity(
            context: Context,
            type: Int,
            item: ElectronicVoucher = ElectronicVoucher()
        ) {
            val intent = Intent(context, EVouchersDetailActivity::class.java)
            intent.putExtra(ELECTRONIC_VOUCHERS_TYPE, type)
            intent.putExtra(ELECTRONIC_VOUCHERS, item)
            context.startActivity(intent)
        }

    }

    override fun createPresenter() = EVouchersDetailPreImpl(this, this)

    override fun getViewBinding() = ActivityEvouchersDetailBinding.inflate(layoutInflater)

    //0 查看电子券详情 1 新增电子券
    private var type: Int = 0

    //电子券
    private var electronicVouchers: ElectronicVoucher = ElectronicVoucher()

    //时间选择器
    private var pvCustomTime1: TimePickerView? = null
    private var pvCustomTime2: TimePickerView? = null

    override fun initBundles() {
        type = intent.getIntExtra(ELECTRONIC_VOUCHERS_TYPE, 0)
        electronicVouchers = intent.getSerializableExtra(ELECTRONIC_VOUCHERS) as ElectronicVoucher
    }

    override fun initView() {

        if (type == 0) {
            mToolBarDelegate?.setMidTitle("电子券")
        } else {
            mToolBarDelegate?.setMidTitle("新增电子券")
        }

        if (type == 1) {
            addElectronicVoucher()
        } else {
            readElectronicVoucher()
        }

    }

    private fun addElectronicVoucher() {

        //系统当前时间
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()
        val lastDate: Calendar = Calendar.getInstance()
        startDate.set(
            selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(
                Calendar.DATE
            )
        )
        endDate.set(
            selectedDate.get(Calendar.YEAR) + 10,
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(
                Calendar.DATE
            )
        )
        lastDate.set(
            selectedDate.get(Calendar.YEAR) + 30,
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(
                Calendar.DATE
            )
        )

        //电子券名称(必填)
        mBinding.nameInput.singleClick {
            DialogUtils.showInputDialog(
                this,
                "电子券名称", "", "请输入电子券名称",
                electronicVouchers.title, "取消", "保存", null
            ) {
                mBinding.nameInput.text = it
                electronicVouchers.title = it
            }
        }


        //选择电子券有效期方式
        mBinding.timeType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == mBinding.timeType1.id) {
                //长期有效
                mBinding.timeDivide.gone()
                mBinding.time.gone()
            } else if (checkedId == mBinding.timeType2.id) {
                //固定时间
                mBinding.timeDivide.visiable()
                mBinding.time.visiable()
            }
        }
        mBinding.timeType1.isChecked = true

        //电子券有效期开始
        mBinding.useTimeStart.singleClick {
            KeyboardUtils.hideSoftInput(it)
            pvCustomTime1 = getDatePicker(this, selectedDate, startDate, endDate, 3, { date, _ ->
                val temp = "${formatString(date, HOUR_FORMAT)}:00"
                mBinding.useTimeStart.text = temp
                electronicVouchers.useStartTime = (dateTime2Date(
                    mBinding.useTimeStart.getViewText() + ":00"
                )?.time ?: 0) / 1000
            }, {
                pvCustomTime1?.returnData()
                pvCustomTime1?.dismiss()
            }, {
                pvCustomTime1?.dismiss()
            })
            pvCustomTime1?.show()
        }

        //电子券有效期结束
        mBinding.useTimeEnd.singleClick {
            KeyboardUtils.hideSoftInput(it)
            pvCustomTime2 = getDatePicker(this, selectedDate, startDate, endDate, 3, { date, _ ->
                val temp = "${formatString(date, HOUR_FORMAT)}:00"
                mBinding.useTimeEnd.text = temp
                electronicVouchers.useEndTime = (dateTime2Date(
                    mBinding.useTimeEnd.getViewText() + ":00"
                )?.time ?: 0) / 1000
            }, {
                pvCustomTime2?.returnData()
                pvCustomTime2?.dismiss()
            }, {
                pvCustomTime2?.dismiss()
            })
            pvCustomTime2?.show()
        }

        //价值金额
        mBinding.price.singleClick {
            DialogUtils.showInputDialogNumberDecimal(
                this,
                "价值金额",
                "",
                "请输入电子券价值金额",
                if (electronicVouchers.jianPrice == null) "" else electronicVouchers.jianPrice.toString(),
                "取消",
                "保存",
                null
            ) {
                try {
                    electronicVouchers.jianPrice = it.toDouble()
                    mBinding.price.text = it
                } catch (e: Exception) {
                    electronicVouchers.jianPrice = null
                    mBinding.price.text = ""
                    showToast("请输入数字")
                }
            }
        }

        //适用商品
        mBinding.useGoods.singleClick {
            val intent = Intent(this, SelectGoodsActivity::class.java)
            startActivityForResult(intent, 22)
        }

        mBinding.submit.singleClick {

            //检查是否数据完全
            if (electronicVouchers.title.isNullOrEmpty()) {
                showToast("请输入电子券名称")
                return@singleClick
            }
            if (mBinding.timeType2.isChecked) {
                //固定时间
                if (electronicVouchers.useStartTime == null || electronicVouchers.useEndTime == null) {
                    showToast("请输入电子券有效期")
                    return@singleClick
                }
            }
            if (electronicVouchers.jianPrice == null) {
                showToast("请输入电子券金额")
                return@singleClick
            }
            if (electronicVouchers.goodsID == null) {
                showToast("请选择电子券适用的商品")
                return@singleClick
            }

            //调用接口前，添加一些必要数据
            electronicVouchers.useTimeType = "FIX"
            electronicVouchers.couponStartTime = (selectedDate.time.time) / 1000
            electronicVouchers.couponEndTime = (lastDate.time.time) / 1000
            if (mBinding.timeType1.isChecked) {
                electronicVouchers.useStartTime = (selectedDate.time.time) / 1000
                electronicVouchers.useEndTime = (lastDate.time.time) / 1000
            }
            mPresenter?.submitDiscount(electronicVouchers)

        }

    }

    private fun readElectronicVoucher() {
        mBinding.apply {
            nameInput.text = electronicVouchers.title
            val start = formatString(
                electronicVouchers.useStartTime?.times(1000)?.let { Date(it) },
                MINUTES_TIME_FORMAT_OTHER
            )
            val end = formatString(
                electronicVouchers.useEndTime?.times(1000)?.let { Date(it) },
                MINUTES_TIME_FORMAT_OTHER
            )
            if (
                (
                        (end?.substring(0, 4)?.toInt() ?: 0) - (start?.substring(0, 4)?.toInt()
                            ?: 0)
                        ) == 30
            ) {
                //长期有效
                timeType1.isChecked = true
                timeType2.isChecked = false
                time.gone()
            } else {
                //固定时间
                timeType1.isChecked = false
                timeType2.isChecked = true
                time.visiable()
                useTimeStart.text = start
                useTimeEnd.text = end
            }
            price.text = electronicVouchers.jianPrice.toString()
            useGoods.text = electronicVouchers.goodsName.toString()
        }
        mBinding.submit.gone()
    }

    override fun useLightMode() = false


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {
            val goodsId = data?.getStringExtra("goods_id")
            val goodsName = data?.getStringExtra("goods_name")
            if (goodsId != null) {
                try {
                    electronicVouchers.goodsID = goodsId.toInt()
                    mBinding.useGoods.text = goodsName
                } catch (e: Exception) {

                }
            }
        }
    }


    override fun onSubmitElectronicVoucher() {
        showToast("添加电子券成功")
        finish()
    }

    override fun onSubmitElectronicVoucherFailed() {
        showToast("添加电子券失败")
        if (mBinding.timeType1.isChecked) {
            electronicVouchers.couponStartTime = null
            electronicVouchers.couponEndTime = null
            mBinding.useTimeStart.text = "请选择"
            mBinding.useTimeEnd.text = "请选择"
        }
    }
}