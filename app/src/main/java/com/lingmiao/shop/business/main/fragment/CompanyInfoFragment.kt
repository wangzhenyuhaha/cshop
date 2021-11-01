package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.view.TimePickerView
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.main.pop.ApplyInfoPop
import com.lingmiao.shop.databinding.FragmentCompanyInfoBinding
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.dateTime2Date
import com.lingmiao.shop.util.formatString
import com.lingmiao.shop.util.getDatePicker
import java.util.*

class CompanyInfoFragment : BaseVBFragment<FragmentCompanyInfoBinding, BasePresenter>() {

    companion object {
        //设置的Pop的类型
        const val REG_MONEY = 0
        const val EMPLOYEE_NUM = 1
        const val OPERATE_LIMIT = 2
        const val INSPECT = 3
    }


    private val model by activityViewModels<ApplyShopInfoViewModel>()

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

    private var title1: String = ""
    private var title2: String = ""


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initBundles() {
        super.initBundles()
        pop = ApplyInfoPop(requireContext())
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanyInfoBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {
        if (model.applyShopInfo.value?.shopType == 1) {
            //企业
            model.setTitle("企业信息")
        } else {
            model.setTitle("企业信息（个体户）")
        }

        if (model.applyShopInfo.value?.thrcertflag == 1) {
            //三证合一
            title1 = "统一社会信用代码证"
            title2 = "社会信用代码证有效期"
            binding.taxesModule.visibility = View.GONE
            binding.organModule.visibility = View.GONE
            binding.licenceModuleTV1.text = title1
            binding.licenceModuleTV2.text = title2

        } else {
            //三证不合一
            title1 = "营业执照编号"
            title2 = "营业执照有效期"
            binding.taxesModule.visibility = View.VISIBLE
            binding.organModule.visibility = View.VISIBLE
            binding.licenceModuleTV1.text = title1
            binding.licenceModuleTV2.text = title2

        }
        initListener()
        initObserver()
    }


    private fun initListener() {

        if (!model.shopOpenOrNot) {
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

            //营业执照名称
            binding.companyName.setOnClickListener {
                DialogUtils.showInputDialog(
                    requireActivity(),
                    "营业执照名称",
                    "",
                    "请输入",
                    model.applyShopInfo.value?.companyName,
                    "取消",
                    "保存",
                    null
                ) {
                    binding.companyNameTV.text = it
                    model.applyShopInfo.value?.companyName = it
                    model.companyAccount.value?.openAccountName = it
                }
            }

            //统一社会信用代码证(三证合一为否时传入的是营业执照的编号)（营业执照编号）
            binding.licenseNum.setOnClickListener {
                DialogUtils.showInputDialog(
                    requireActivity(),
                    title1,
                    "",
                    "请输入",
                    model.applyShopInfo.value?.licenseNum,
                    "取消",
                    "保存",
                    null
                ) {
                    binding.licenseNumTV.text = it
                    model.applyShopInfo.value?.licenseNum = it
                }
            }

            //社会信用代码证有效期（营业执照有效期）
            binding.licenceEnd.setOnClickListener {

                DialogUtils.showDialog(
                    requireActivity(),
                    "营业执照有效期",
                    "您的营业执照有效期是否为长期？",
                    "是",
                    "选择时间",
                    {
                        binding.licenceEndTV.text = "长期"
                        model.applyShopInfo.value?.licenceEnd =
                            (dateTime2Date("2099-11-01" + " 00:00:00")?.time ?: 0) / 1000
                    },
                    {
                        //选中的日期
                        val selectedDate: Calendar = Calendar.getInstance()
                        //开始日期
                        val startDate: Calendar = Calendar.getInstance()
                        //结束日期
                        val endDate: Calendar = Calendar.getInstance()

                        //设定结束日期，假设可以活30年
                        endDate.set(
                            startDate.get(Calendar.YEAR) + 50,
                            startDate.get(Calendar.MONTH),
                            startDate.get(
                                Calendar.DATE
                            )
                        )
                        pvCustomTime =
                            getDatePicker(
                                requireContext(),
                                selectedDate,
                                startDate,
                                endDate,
                                { date, _ ->

                                    //在界面上显示时间
                                    binding.licenceEndTV.text = formatString(date, DATE_FORMAT)

                                    //获取当前精确时间
                                    val s =
                                        dateTime2Date(binding.licenceEndTV.getViewText() + " 00:00:00")?.time
                                            ?: 0
                                    timeCanUse = s / 1000

                                    model.applyShopInfo.value?.licenceEnd = timeCanUse
                                },
                                {
                                    pvCustomTime?.returnData()
                                    pvCustomTime?.dismiss()
                                },
                                {
                                    pvCustomTime?.dismiss()
                                })
                        pvCustomTime?.show()
                    })


            }

            //三证合一为否时再传入以下字段
            //税务登记证号
            binding.taxesNum.setOnClickListener {
                DialogUtils.showInputDialog(
                    requireActivity(),
                    "税务登记证号",
                    "",
                    "请输入",
                    model.applyShopInfo.value?.taxes_certificate_num,
                    "取消",
                    "保存",
                    null
                ) {
                    binding.taxesNumTV.text = it
                    model.applyShopInfo.value?.taxes_certificate_num = it
                }
            }

            //税务登记证有效期
            binding.taxesExpire.setOnClickListener {

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
                        binding.taxesExpireTV.text = formatString(date, DATE_FORMAT)

                        //获取当前精确时间
                        val s =
                            dateTime2Date(binding.taxesExpireTV.getViewText() + " 00:00:00")?.time
                                ?: 0
                        timeCanUse = s / 1000

                        model.applyShopInfo.value?.taxes_distinguish_expire = timeCanUse
                    }, {
                        pvCustomTime?.returnData()
                        pvCustomTime?.dismiss()
                    }, {
                        pvCustomTime?.dismiss()
                    })
                pvCustomTime?.show()
            }

            //组织机构代码证号
            binding.organcode.setOnClickListener {
                DialogUtils.showInputDialog(
                    requireActivity(),
                    "组织机构代码证号",
                    "",
                    "请输入",
                    model.applyShopInfo.value?.organcode,
                    "取消",
                    "保存",
                    null
                ) {
                    binding.organcodeTV.text = it
                    model.applyShopInfo.value?.organcode = it
                }
            }

            //组织机构代码证有效期
            binding.organexpire.setOnClickListener {

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
                        binding.organexpireTV.text = formatString(date, DATE_FORMAT)

                        //获取当前精确时间
                        val s =
                            dateTime2Date(binding.organexpireTV.getViewText() + " 00:00:00")?.time
                                ?: 0
                        timeCanUse = s / 1000

                        model.applyShopInfo.value?.organexpire = timeCanUse
                    }, {
                        pvCustomTime?.returnData()
                        pvCustomTime?.dismiss()
                    }, {
                        pvCustomTime?.dismiss()
                    })
                pvCustomTime?.show()
            }
        }

        //营业执照电子版
        binding.licenceImg.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.LICENSE)
            findNavController().navigate(
                R.id.action_companyInfoFragment_to_shopPhotoFragment,
                bundle
            )
        }

        //税务登记证照片
        binding.taxesImg.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.TAXES)
            findNavController().navigate(
                R.id.action_companyInfoFragment_to_shopPhotoFragment,
                bundle
            )
        }

        //组织机构代码证照片
        binding.orgcodepic.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.ORGAN)
            findNavController().navigate(
                R.id.action_companyInfoFragment_to_shopPhotoFragment,
                bundle
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
            }

            if (!applyShopInfo.companyName.isNullOrEmpty()) {
                binding.companyNameTV.text = applyShopInfo.companyName
            }

            if (!applyShopInfo.licenseNum.isNullOrEmpty()) {
                binding.licenseNumTV.text = applyShopInfo.licenseNum
            }

            if (!applyShopInfo.taxes_certificate_num.isNullOrEmpty()) {
                binding.taxesNumTV.text = applyShopInfo.taxes_certificate_num
            }

            if (!applyShopInfo.organcode.isNullOrEmpty()) {
                binding.organcodeTV.text = applyShopInfo.organcode
            }

            applyShopInfo.licenceEnd?.also {
                if (formatString(Date(it * 1000), DATE_FORMAT).toString().startsWith("2099")) {
                    binding.licenceEndTV.text = "长期"
                } else {
                    binding.licenceEndTV.text = formatString(Date(it * 1000), DATE_FORMAT)
                }


            }

            applyShopInfo.taxes_distinguish_expire?.also {
                binding.taxesExpireTV.text = formatString(Date(it * 1000), DATE_FORMAT)
            }

            applyShopInfo.organexpire?.also {
                binding.organexpireTV.text = formatString(Date(it * 1000), DATE_FORMAT)
            }


        })

    }


    override fun onPause() {
        super.onPause()
        if (model.firstApplyShop) {
            model.applyShopInfo.value?.also {
                UserManager.setApplyShopInfo(it)
            }
            model.companyAccount.value?.also {
                UserManager.setCompanyAccount(it)
            }
            model.personalAccount.value?.also {
                UserManager.setPersonalAccount(it)
            }
        }
    }


}