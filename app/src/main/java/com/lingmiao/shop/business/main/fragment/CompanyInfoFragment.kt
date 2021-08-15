package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.view.TimePickerView
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.main.pop.ApplyInfoPop
import com.lingmiao.shop.databinding.FragmentCompanyInfoBinding
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.dateTime2Date
import com.lingmiao.shop.util.formatString
import com.lingmiao.shop.util.getDatePicker
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class CompanyInfoFragment : BaseVBFragment<FragmentCompanyInfoBinding, BasePresenter>() {

    companion object {
        //设置的Pop的类型
        const val REG_MONEY = 0
        const val EMPLOYEE_NUM = 1
        const val OPERATE_LIMIT = 2
        const val INSPECT = 3
        const val THRCERT_FLAG = 4
    }


    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    //弹出的东西
    private var pop: ApplyInfoPop? = null

    //时间选择器
    private var pvCustomTime: TimePickerView? = null
    private var timeCanUse: Long? = null


    //注册资本  1  2  3  4  5
    private val regMoneyList = listOf(
        "注册资本≤10万元",
        "10万元<注册资本≤20万元",
        "20万元<注册资本≤50万元",
        "50万元<注册资本≤100万元",
        "注册资本>100万元"
    )

    //员工人数  1  2  3  4  5
    private val employeeNumList =
        listOf("员工数量≤10", "10<员工数量≤20", "20<员工数量≤50", "50<员工数量≤100", "员工数量>100")

    //经营区域  1  2  3
    private val operateLimitList = listOf("城区", "郊区", "边远地区")

    //经营地段  1  2  3
    private val inspectList = listOf("商业区", "工业区", "住宅区")

    //三证合一  0   1
    private val thrcertflagList = listOf("否", "是")


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initBundles() {
        super.initBundles()
        pop = ApplyInfoPop(requireContext())
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanyInfoBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {
        if (model.applyShopInfo.value?.shopType == 1) {
            //企业
            model.setTitle("企业资料")
        } else {
            model.setTitle("商家资料")
        }

        initListener()
        initObserver()
    }


    private fun initListener() {

        //注册资本
        binding.regMoney.setOnClickListener {

            pop?.apply {
                setList(regMoneyList)
                setTitle("注册资本")
                setType(REG_MONEY)
                showPopupWindow()
            }
        }

        //员工人数
        binding.employeeNum.setOnClickListener {
            pop?.apply {
                setList(employeeNumList)
                setTitle("员工人数")
                setType(EMPLOYEE_NUM)
                showPopupWindow()
            }
        }

        //经营区域
        binding.operateLimit.setOnClickListener {
            pop?.apply {
                setList(operateLimitList)
                setTitle("经营区域")
                setType(OPERATE_LIMIT)
                showPopupWindow()
            }
        }

        //经营地段
        binding.inspect.setOnClickListener {
            pop?.apply {
                setList(inspectList)
                setTitle("经营地段")
                setType(INSPECT)
                showPopupWindow()
            }
        }

        //三证合一
        binding.thrcertflag.setOnClickListener {
            pop?.apply {
                setList(thrcertflagList)
                setTitle("三证合一")
                setType(THRCERT_FLAG)
                showPopupWindow()
            }
        }

        //三证合一   是
        //统一社会信用代码证
        binding.creditCode.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "统一社会信用代码证",
                "",
                "请输入",
                model.applyShopInfo.value?.creditCode,
                "取消",
                "保存",
                null
            ) {
                binding.creditCodeTV.text = it
                model.applyShopInfo.value?.creditCode = it
            }
        }

        //社会信用代码证有效期
        binding.creditCodeExpire.setOnClickListener {
            //选中的日期
            val selectedDate: Calendar = Calendar.getInstance()
            //开始日期
            val startDate: Calendar = Calendar.getInstance()
            //结束日期
            val endDate: Calendar = Calendar.getInstance()

            //设定结束日期，假设可以活30年
            endDate.set(
                startDate.get(Calendar.YEAR) + 50, startDate.get(Calendar.MONTH), startDate.get(
                    Calendar.DATE
                )
            )
            pvCustomTime =
                getDatePicker(requireContext(), selectedDate, startDate, endDate, { date, _ ->

                    //在界面上显示时间
                    binding.creditCodeExpireTV.text = formatString(date, DATE_FORMAT)

                    //获取当前精确时间
                    val s =
                        dateTime2Date(binding.creditCodeExpireTV.getViewText() + " 00:00:00")?.time
                            ?: 0
                    timeCanUse = s / 1000

                    model.applyShopInfo.value?.creditCodeExpire = timeCanUse
                }, {
                    pvCustomTime?.returnData()
                    pvCustomTime?.dismiss()
                }, {
                    pvCustomTime?.dismiss()
                })
            pvCustomTime?.show()
        }

        //三证合一   否
        //税务登记号码
        binding.taxRegCode.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "税务登记号码",
                "",
                "请输入",
                model.applyShopInfo.value?.taxRegCode,
                "取消",
                "保存",
                null
            ) {
                binding.taxRegCodeTV.text = it
                model.applyShopInfo.value?.taxRegCode = it
            }
        }


        //税务登记证日期
        binding.taxCodeExpire.setOnClickListener {
            //选中的日期
            val selectedDate: Calendar = Calendar.getInstance()
            //开始日期
            val startDate: Calendar = Calendar.getInstance()
            //结束日期
            val endDate: Calendar = Calendar.getInstance()

            //设定结束日期，假设可以活30年
            endDate.set(
                startDate.get(Calendar.YEAR) + 50, startDate.get(Calendar.MONTH), startDate.get(
                    Calendar.DATE
                )
            )
            pvCustomTime =
                getDatePicker(requireContext(), selectedDate, startDate, endDate, { date, _ ->

                    //在界面上显示时间
                    binding.taxCodeExpireTV.text = formatString(date, DATE_FORMAT)

                    //获取当前精确时间
                    val s =
                        dateTime2Date(binding.taxCodeExpireTV.getViewText() + " 00:00:00")?.time
                            ?: 0
                    timeCanUse = s / 1000

                    model.applyShopInfo.value?.taxCodeExpire = timeCanUse
                }, {
                    pvCustomTime?.returnData()
                    pvCustomTime?.dismiss()
                }, {
                    pvCustomTime?.dismiss()
                })
            pvCustomTime?.show()
        }


        //税务登记证照片
        binding.taxCodePic.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.TAX_CODE_PIC,
                "上传税务登记证照片",
                model.applyShopInfo.value?.taxCodePic
            )
        }

        binding.tvApplyShopInfoNext.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun initObserver() {

        pop?.liveData?.observe(this, Observer {

            when (pop?.getType()) {
                REG_MONEY -> {
                    //注册资本
                    binding.regMoneyTextView.text = regMoneyList[it]
                    model.applyShopInfo.value?.regMoney = it + 1
                    pop?.dismiss()
                }
                EMPLOYEE_NUM -> {
                    //员工人数
                    binding.employeeNumTextView.text = employeeNumList[it]
                    model.applyShopInfo.value?.employeeNum = it + 1
                    pop?.dismiss()
                }
                OPERATE_LIMIT -> {
                    //经营区域
                    binding.operateLimitTextView.text = operateLimitList[it]
                    model.applyShopInfo.value?.operateLimit = it + 1
                    pop?.dismiss()
                }
                INSPECT -> {
                    //经营地段
                    binding.inspectTextView.text = inspectList[it]
                    model.applyShopInfo.value?.inspect = it + 1
                    pop?.dismiss()
                }
                THRCERT_FLAG -> {
                    //三证合一
                    binding.thrcertflagTextView.text = thrcertflagList[it]

                    if (it == 0) {
                        //否
                        binding.creditCode.visibility = View.GONE
                        binding.creditCodeExpire.visibility = View.GONE
                        binding.taxRegCode.visibility = View.VISIBLE
                        binding.taxCodeExpire.visibility = View.VISIBLE
                        binding.taxCodePic.visibility = View.VISIBLE

                    } else {
                        //是
                        binding.creditCode.visibility = View.VISIBLE
                        binding.creditCodeExpire.visibility = View.VISIBLE
                        binding.taxRegCode.visibility = View.GONE
                        binding.taxCodeExpire.visibility = View.GONE
                        binding.taxCodePic.visibility = View.GONE
                    }
                    model.applyShopInfo.value?.thrcertflag = it
                    pop?.dismiss()
                }
            }
        })


        model.applyShopInfo.observe(this, Observer { applyShopInfo ->

            //注册资本
            applyShopInfo.regMoney?.also {
                binding.regMoneyTextView.text = regMoneyList[it - 1]
            }
            //员工人数
            applyShopInfo.employeeNum?.also {
                binding.employeeNumTextView.text = employeeNumList[it - 1]
            }
            //经营区域
            applyShopInfo.operateLimit?.also {
                binding.operateLimitTextView.text = operateLimitList[it - 1]
            }
            //经营地段
            applyShopInfo.inspect?.also {
                binding.inspectTextView.text = inspectList[it - 1]
            }
            //三证合一
            applyShopInfo.thrcertflag?.also {
                binding.thrcertflagTextView.text = thrcertflagList[it]
                if (it == 0) {
                    //否
                    binding.creditCode.visibility = View.GONE
                    binding.creditCodeExpire.visibility = View.GONE
                    binding.taxRegCode.visibility = View.VISIBLE
                    binding.taxCodeExpire.visibility = View.VISIBLE
                    binding.taxCodePic.visibility = View.VISIBLE

                } else {
                    //是
                    binding.creditCode.visibility = View.VISIBLE
                    binding.creditCodeExpire.visibility = View.VISIBLE
                    binding.taxRegCode.visibility = View.GONE
                    binding.taxCodeExpire.visibility = View.GONE
                    binding.taxCodePic.visibility = View.GONE
                }
            }

            if (applyShopInfo.thrcertflag == null) {
                //是
                binding.creditCode.visibility = View.VISIBLE
                binding.creditCodeExpire.visibility = View.VISIBLE
                binding.taxRegCode.visibility = View.GONE
                binding.taxCodeExpire.visibility = View.GONE
                binding.taxCodePic.visibility = View.GONE
            }


            //三证合一   是
            //统一社会信用代码证
            applyShopInfo.creditCode?.also {
                if (it.isNotEmpty()) binding.creditCodeTV.text = it
            }

            //社会信用代码证有效期
            applyShopInfo.creditCodeExpire?.also {
                binding.creditCodeExpireTV.text = formatString(Date(it * 1000), DATE_FORMAT)
            }


            //三证合一   否
            //税务登记号码
            applyShopInfo.taxRegCode?.also {
                if (it.isNotEmpty()) binding.taxRegCodeTV.text = it
            }


            //税务登记证日期
            applyShopInfo.taxCodeExpire?.also {
                binding.taxCodeExpireTV.text = formatString(Date(it * 1000), DATE_FORMAT)
            }


            //税务登记证照片
            applyShopInfo.taxCodePic?.also {
                if (it.isNotEmpty()) binding.taxCodePicTV.text = "已上传"
            }

        })

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            ApplyShopInfoActivity.TAX_CODE_PIC -> {
                model.applyShopInfo.value?.taxCodePic = event.remoteUrl
                binding.taxCodePicTV.text = "已上传"
            }

        }
    }
}