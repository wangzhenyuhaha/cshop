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
import com.lingmiao.shop.business.main.pop.ApplyInfoPop
import com.lingmiao.shop.databinding.FragmentIdentityInfoBinding
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.dateTime2Date
import com.lingmiao.shop.util.formatString
import com.lingmiao.shop.util.getDatePicker
import java.util.*

class IdentityInfoFragment : BaseVBFragment<FragmentIdentityInfoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    //弹出的东西
    private var pop: ApplyInfoPop? = null

    //时间选择器
    private var pvCustomTime: TimePickerView? = null
    private var timeCanUse: Long? = null

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentIdentityInfoBinding.inflate(inflater, container, false)

    override fun initBundles() {
        super.initBundles()
        pop = ApplyInfoPop(requireContext())
    }

    override fun initViewsAndData(rootView: View) {

        model.setTitle("添加身份信息")

        initListener()
        initObserver()
    }

    private fun initListener() {
        //姓名
        binding.legalName.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "姓名",
                "",
                "请输入",
                model.applyShopInfo.value?.legalName,
                "取消",
                "保存",
                null
            ) {
                binding.legalNameTextView.text = it
                model.applyShopInfo.value?.legalName = it
            }
        }

        //性别
        binding.legalSex.setOnClickListener {

            pop?.apply {
                setList(listOf("女", "男"))
                setTitle("性别")
                showPopupWindow()
            }
        }

        //证件号码
        binding.legalId.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "身份证号码",
                "",
                "请输入",
                model.applyShopInfo.value?.legalId,
                "取消",
                "保存",
                null
            ) {
                binding.legalIdTextView.text = it
                model.applyShopInfo.value?.legalId = it
            }
        }

        //证件有效期
        binding.legalIDExpire.setOnClickListener {

            //选中的日期
            val selectedDate: Calendar = Calendar.getInstance()
            //开始日期
            val startDate: Calendar = Calendar.getInstance()
            //结束日期
            val endDate: Calendar = Calendar.getInstance()

            //设定结束日期，假设可以活30年
            endDate.set(
                startDate.get(Calendar.YEAR) + 30, startDate.get(Calendar.MONTH), startDate.get(
                    Calendar.DATE
                )
            )


            pvCustomTime =
                getDatePicker(requireContext(), selectedDate, startDate, endDate, { date, _ ->

                    //在界面上显示时间
                    binding.legalIDExpireTextView.text = formatString(date, DATE_FORMAT)

                    //获取当前精确时间
                    val s =
                        dateTime2Date(binding.legalIDExpireTextView.getViewText() + " 00:00:00")?.time
                            ?: 0
                    timeCanUse = s / 1000

                    model.applyShopInfo.value?.legalIDExpire = timeCanUse
                }, {
                    pvCustomTime?.returnData()
                    pvCustomTime?.dismiss()
                }, {
                    pvCustomTime?.dismiss()
                })
            pvCustomTime?.show()

        }

        //身份证照片
        binding.legalPhoto.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.ID_CARD_FRONT)
            findNavController().navigate(
                R.id.action_identityInfoFragment_to_shopIDCardFragment,
                bundle
            )
        }

        //地址
        binding.legalAddress.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "地址",
                "",
                "请输入",
                model.applyShopInfo.value?.legal_address,
                "取消",
                "保存",
                null
            ) {
                binding.legalAddressTV.text = it
                model.applyShopInfo.value?.legal_address = it
            }
        }

        //保存
        binding.tvApplyShopInfoSave.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun initObserver() {

        pop?.liveData?.observe(this, Observer {
            if (it == 0) {
                binding.legalSexTextView.text = "女"
            } else {
                binding.legalSexTextView.text = "男"
            }
            model.applyShopInfo.value?.legalSex = it
            pop?.dismiss()
        })

        model.applyShopInfo.observe(this, Observer { applyShopInfo ->

            applyShopInfo.legalName?.also {
                if (it.isNotEmpty()) binding.legalNameTextView.text = it
            }

            applyShopInfo.legalSex?.also {
                if (it == 0) {
                    binding.legalSexTextView.text = "女"
                } else {
                    binding.legalSexTextView.text = "男"
                }
            }

            applyShopInfo.legalId?.also {
                if (it.isNotEmpty()) binding.legalIdTextView.text = it
            }

            //地址
            if (!applyShopInfo.legal_address.isNullOrEmpty()) {
                binding.legalAddressTV.text = applyShopInfo.legal_address
            }

            applyShopInfo.legalIDExpire?.also {
                binding.legalIDExpireTextView.text = formatString(Date(it * 1000), DATE_FORMAT)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if (model.firstApplyShop) {
            model.applyShopInfo.value?.also {
                UserManager.setApplyShopInfo(it)
            }
        }
    }

}