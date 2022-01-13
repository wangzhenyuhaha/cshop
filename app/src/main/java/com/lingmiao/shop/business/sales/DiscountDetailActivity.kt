package com.lingmiao.shop.business.sales

import android.content.Context
import android.content.Intent
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.*
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.presenter.DiscountDetailPre
import com.lingmiao.shop.business.sales.presenter.impl.DiscountDetailPreImpl
import com.lingmiao.shop.databinding.ActivityDiscountDetailBinding
import com.lingmiao.shop.util.*
import com.lingmiao.shop.util.DATE_FORMAT
import java.util.*

class DiscountDetailActivity : BaseVBActivity<ActivityDiscountDetailBinding, DiscountDetailPre>(),
    DiscountDetailPre.View {

    companion object {

        //0  查看优惠券详情   1  新增优惠券
        private const val DISCOUNT_TYPE = "DISCOUNT_TYPE"

        //显示的优惠券
        private const val COUPON = "COUPON"

        fun openActivity(context: Context, type: Int, item: Coupon = Coupon()) {
            val intent = Intent(context, DiscountDetailActivity::class.java)
            intent.putExtra(DISCOUNT_TYPE, type)
            intent.putExtra(COUPON, item)
            context.startActivity(intent)
        }

    }

    override fun createPresenter() = DiscountDetailPreImpl(this, this)

    override fun getViewBinding() = ActivityDiscountDetailBinding.inflate(layoutInflater)

    //0 查看优惠券详情 1 新增优惠券
    private var type: Int = 0

    //优惠券
    private var coupon: Coupon = Coupon()

    //时间选择器
    private var pvCustomTime1: TimePickerView? = null
    private var pvCustomTime2: TimePickerView? = null
    private var pvCustomTime3: TimePickerView? = null
    private var pvCustomTime4: TimePickerView? = null

    override fun initBundles() {
        type = intent.getIntExtra(DISCOUNT_TYPE, 0)
        coupon = intent.getSerializableExtra(COUPON) as Coupon
    }

    override fun initView() {

        if (type == 0) {
            mToolBarDelegate?.setMidTitle("优惠券")
        } else {
            mToolBarDelegate?.setMidTitle("新增优惠券")
        }

        if (type == 1) {
            addDiscount()
        } else {
            readDiscount()
        }

    }


    //这是如果新增优惠券，特有操作
    private fun addDiscount() {

        //优惠券名称(必填)
        mBinding.nameInput.singleClick {
            DialogUtils.showInputDialog(
                this,
                "优惠券名称", "", "请输入优惠券名称",
                coupon.title, "取消", "保存", null
            ) {
                mBinding.nameInput.text = it
                coupon.title = it
            }
        }

        //系统当前时间
        val selectedDate: Calendar = Calendar.getInstance()

        val startDate: Calendar = Calendar.getInstance()
        startDate.set(
            selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(
                Calendar.DATE
            )
        )

        val endDate: Calendar = Calendar.getInstance()
        endDate.set(
            selectedDate.get(Calendar.YEAR) + 5, selectedDate.get(Calendar.MONTH), selectedDate.get(
                Calendar.DATE
            )
        )

        //优惠券有效期起始
        mBinding.couponTimeStart.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime1 =
                getDatePicker(this, selectedDate, startDate, endDate, 3, { date, _ ->
                    val temp = "${formatString(date, HOUR_FORMAT)}:00"
                    mBinding.couponTimeStart.text = temp
                    coupon.couponStartTime = (dateTime2Date(
                        mBinding.couponTimeStart.getViewText() + ":00"
                    )?.time ?: 0) / 1000
                }, {
                    pvCustomTime1?.returnData()
                    pvCustomTime1?.dismiss()
                }, {
                    pvCustomTime1?.dismiss()
                })
            pvCustomTime1?.show()
        }

        //优惠券有效期结束
        mBinding.couponTimeEnd.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime2 =
                getDatePicker(this, selectedDate, startDate, endDate, 3, { date, _ ->
                    val temp = "${formatString(date, HOUR_FORMAT)}:00"
                    mBinding.couponTimeEnd.text = temp
                    coupon.couponEndTime = (dateTime2Date(
                        mBinding.couponTimeEnd.getViewText() + ":00"
                    )?.time ?: 0) / 1000
                }, {
                    pvCustomTime2?.returnData()
                    pvCustomTime2?.dismiss()
                }, {
                    pvCustomTime2?.dismiss()
                })
            pvCustomTime2?.show()
        }

        //选择使用时间方式
        mBinding.timeType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == mBinding.timeType1.id) {

                //固定时间
                coupon.useTimeType = "FIX"
                mBinding.useTimeDay.gone()
                mBinding.useTimeDetail.visiable()
            } else if (checkedId == mBinding.timeType2.id) {
                //领取后生效
                coupon.useTimeType = "PERIOD"
                mBinding.useTimeDay.visiable()
                mBinding.useTimeDetail.gone()
            }
        }

        //选择优惠券使用时间开始
        mBinding.useTimeStart.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime3 =
                getDatePicker(this, selectedDate, startDate, endDate, 3, { date, _ ->
                    val temp = "${formatString(date, HOUR_FORMAT)}:00"
                    mBinding.useTimeStart.text = temp
                    coupon.useStartTime = (dateTime2Date(
                        mBinding.useTimeStart.getViewText() + ":00"
                    )?.time ?: 0) / 1000
                }, {
                    pvCustomTime3?.returnData()
                    pvCustomTime3?.dismiss()
                }, {
                    pvCustomTime3?.dismiss()
                })
            pvCustomTime3?.show()
        }

        //选择优惠券使用时间结束
        mBinding.useTimeEnd.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime4 =
                getDatePicker(this, selectedDate, startDate, endDate, 3,{ date, _ ->
                    val temp = "${formatString(date, HOUR_FORMAT)}:00"
                    mBinding.useTimeEnd.text = temp
                    coupon.useEndTime = (dateTime2Date(
                        mBinding.useTimeEnd.getViewText() + ":00"
                    )?.time ?: 0) / 1000
                }, {
                    pvCustomTime4?.returnData()
                    pvCustomTime4?.dismiss()
                }, {
                    pvCustomTime4?.dismiss()
                })
            pvCustomTime4?.show()
        }

        //选择生效天数
        mBinding.useTimeDayNumber.singleClick {
            DialogUtils.showInputDialogNumber(
                this,
                "生效天数",
                "",
                "请输入优惠券生效天数",
                if (coupon.usePeriod == null) "" else coupon.usePeriod.toString(),
                "取消",
                "保存",
                null
            ) {
                try {
                    coupon.usePeriod = it.toInt()
                    mBinding.useTimeDayNumber.text = it
                } catch (e: Exception) {
                    coupon.usePeriod = null
                    mBinding.useTimeDayNumber.text = ""
                    showToast("请输入数字")
                }

            }
        }

        //库存
        mBinding.stockNumber.singleClick {
            DialogUtils.showInputDialogNumber(
                this,
                "库存",
                "",
                "请输入优惠券库存",
                if (coupon.createNum == null) "" else coupon.createNum.toString(),
                "取消",
                "保存",
                null
            ) {
                try {
                    coupon.createNum = it.toInt()
                    mBinding.stockNumber.text = it
                } catch (e: Exception) {
                    coupon.createNum = null
                    mBinding.stockNumber.text = ""
                    showToast("请输入数字")
                }
            }
        }

        //优惠规则  满
        mBinding.rulerNumberMan.singleClick {
            DialogUtils.showInputDialogNumber(
                this,
                "优惠券门槛", "", "请输入优惠券需满多少元",
                if (coupon.manPrice == null) "" else coupon.manPrice.toString(), "取消", "保存", null
            ) {
                try {
                    coupon.manPrice = it.toDouble()
                    mBinding.rulerNumberMan.text = it
                } catch (e: Exception) {
                    coupon.manPrice = null
                    mBinding.rulerNumberMan.text = ""
                    showToast("请输入数字")
                }
            }
        }

        //优惠规则  减
        mBinding.rulerNumberJian.singleClick {
            DialogUtils.showInputDialogNumber(
                this,
                "优惠券面额",
                "",
                "请输入优惠券可减多少元",
                if (coupon.jianPrice == null) "" else coupon.jianPrice.toString(),
                "取消",
                "保存",
                null
            ) {
                try {
                    coupon.jianPrice = it.toDouble()
                    mBinding.rulerNumberJian.text = it
                } catch (e: Exception) {
                    coupon.jianPrice = null
                    mBinding.rulerNumberJian.text = ""
                    showToast("请输入数字")
                }
            }
        }

        //每人限额
        mBinding.personalMoreNumber.singleClick {
            DialogUtils.showInputDialogNumber(
                this,
                "没人限额", "", "请输入每人限领多少张",
                if (coupon.limitNum == null) "" else coupon.limitNum.toString(), "取消", "保存", null
            ) {
                try {
                    coupon.limitNum = it.toInt()
                    mBinding.personalMoreNumber.text = it
                } catch (e: Exception) {
                    coupon.limitNum = null
                    mBinding.personalMoreNumber.text = ""
                    showToast("请输入数字")
                }
            }
        }

        mBinding.submit.singleClick {

            //检查是否数据完全
            if (coupon.title.isNullOrEmpty()) {
                showToast("请输入优惠券名称")
                return@singleClick
            }
            if (coupon.couponStartTime == null || coupon.couponEndTime == null) {
                showToast("请完整输入优惠券有效期")
                return@singleClick
            }
            if (coupon.useTimeType.isNullOrEmpty()) {
                showToast("请选择优惠券使用时间模式")
                return@singleClick
            }
            if (coupon.useTimeType == "FIX") {
                //固定时间
                if (coupon.useStartTime == null || coupon.useEndTime == null) {
                    showToast("请完整输入优惠券使用时间")
                    return@singleClick
                }
            } else {
                if (coupon.usePeriod == null) {
                    showToast("请输入优惠券领取后生效的天数")
                    return@singleClick
                }
            }

            if (coupon.createNum == null) {
                showToast("请输入优惠券库存")
                return@singleClick
            }
            if (coupon.manPrice == null) {
                showToast("请输入优惠券使用门槛")
                return@singleClick
            }
            if (coupon.jianPrice == null) {
                showToast("请输入优惠券面额")
                return@singleClick
            }
            if (coupon.limitNum == null) {
                showToast("请输入优惠券每人限额")
                return@singleClick
            }

            //调用接口，添加优惠券
            mPresenter?.submitDiscount(coupon)

        }
    }


    //这是如果查看优惠券，特有操作
    private fun readDiscount() {
        mBinding.apply {
            nameInput.text = coupon.title
            couponTimeStart.text = formatString(
                coupon.couponStartTime?.times(1000)?.let { Date(it) },
                DATE_FORMAT
            )
            couponTimeEnd.text = formatString(
                coupon.couponEndTime?.times(1000)?.let { Date(it) },
                DATE_FORMAT
            )
            if (coupon.useTimeType == "FIX") {
                //固定时间
                timeType1.isChecked = true
                mBinding.useTimeDay.gone()
                mBinding.useTimeDetail.visiable()
                useTimeStart.text = formatString(
                    coupon.useStartTime?.times(1000)?.let { Date(it) },
                    DATE_FORMAT
                )
                useTimeEnd.text = formatString(
                    coupon.useEndTime?.times(1000)?.let { Date(it) },
                    DATE_FORMAT
                )
            } else {
                //领取后生效
                timeType2.isChecked = true
                mBinding.useTimeDay.visiable()
                mBinding.useTimeDetail.gone()
                useTimeDayNumber.text = coupon.usePeriod.toString()
            }
            stockNumber.text = coupon.createNum.toString()
            rulerNumberMan.text = coupon.manPrice.toString()
            rulerNumberJian.text = coupon.jianPrice.toString()
            personalMoreNumber.text = coupon.limitNum.toString()

        }
    }

    override fun useLightMode() = false
    override fun onSubmitCoupons() {
        showToast("优惠券添加成功")
        finish()
    }

}