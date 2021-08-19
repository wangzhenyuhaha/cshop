package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bigkoo.pickerview.view.TimePickerView
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.databinding.FragmentLicenseInfoBinding
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.dateTime2Date
import com.lingmiao.shop.util.formatString
import com.lingmiao.shop.util.getDatePicker
import java.util.*

class LicenseInfoFragment : BaseVBFragment<FragmentLicenseInfoBinding, BasePresenter>() {


    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    //时间选择器
    private var pvCustomTime: TimePickerView? = null
    private var timeCanUse: Long? = null


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLicenseInfoBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {

        model.setTitle("营业执照资料")

        initListener()
        initObserver()

    }

    private fun initListener() {

        binding.licenseNum.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "营业执照号",
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

        binding.licenseEnd.setOnClickListener {
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
                    binding.licenseEndTV.text = formatString(date, DATE_FORMAT)

                    //获取当前精确时间
                    val s =
                        dateTime2Date(binding.licenseEndTV.getViewText() + " 00:00:00")?.time
                            ?: 0
                    timeCanUse = s / 1000

                    model.applyShopInfo.value?.licenceEnd = timeCanUse
                }, {
                    pvCustomTime?.returnData()
                    pvCustomTime?.dismiss()
                }, {
                    pvCustomTime?.dismiss()
                })
            pvCustomTime?.show()
        }

        binding.tvApplyShopInfoNext.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun initObserver() {

        model.applyShopInfo.observe(this, androidx.lifecycle.Observer { applyShopInfo ->

            applyShopInfo.licenseNum?.also {
                if (it.isNotEmpty()) binding.licenseNumTV.text = it
            }

            //社会信用代码证有效期
            applyShopInfo.licenceEnd?.also {
                binding.licenseEndTV.text = formatString(Date(it * 1000), DATE_FORMAT)
            }

        })
    }


}