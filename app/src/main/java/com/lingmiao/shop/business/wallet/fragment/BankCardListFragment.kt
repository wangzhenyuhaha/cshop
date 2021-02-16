package com.lingmiao.shop.business.wallet.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.AddBankCardActivity
import com.lingmiao.shop.business.wallet.BankCardListActivity
import com.lingmiao.shop.business.wallet.adapter.BankCardAdapter
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.BankCardAccountBean
import com.lingmiao.shop.business.wallet.presenter.BankCardListPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.BankCardListPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.list.BaseListFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils

/**
 * 银行卡列表
 */
class BankCardListFragment : BaseListFragment<BankCardAccountBean, BankCardListPresenter>(),
    BankCardListPresenter.View {

    companion object {
        fun newInstance(type : Int?): BankCardListFragment {
            return BankCardListFragment().apply {
                arguments = Bundle().apply {
                    putInt(WalletConstants.KEY_VIEW_TYPE, type ?: BankCardListActivity.VALUE_DEFAULT)
                }
            }
        }
    }

    private var viewType: Int? = BankCardListActivity.VALUE_DEFAULT;

    override fun initBundles() {
        viewType = arguments?.getInt(WalletConstants.KEY_VIEW_TYPE)
    }

    override fun createPresenter(): BankCardListPresenter? {
        return BankCardListPresenterImpl(this)
    }

    override fun initAdapter(): BankCardAdapter {
        return BankCardAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as BankCardAccountBean
                if (view.id == R.id.tv_bankcard_un_bind) {
                    DialogUtils.showDialog(
                        context as Activity, "解绑提示", "解绑后不可恢复，确定要解绑该银行卡吗？",
                        "取消", "确定解绑", View.OnClickListener { }, View.OnClickListener {
//                            showDialogLoading()
                            mPresenter?.unBind(bItem?.id ?: "");
                        })
                } else if (view.id == R.id.tv_bankcard_edit) {
                    val intent = Intent(activity, AddBankCardActivity::class.java)
                    intent.putExtra(WalletConstants.KEY_ITEM, bItem);
                    startActivity(intent)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                if(viewType == BankCardListActivity.VALUE_WITHDRAW) {
                    var intent = Intent();
                    intent.putExtra(WalletConstants.KEY_ITEM, mAdapter.getItem(position));
                    activity?.setResult(Activity.RESULT_OK, intent);
                    activity?.finish();
                }
            }

            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun initOthers(rootView: View) {

    }

    override fun unBind() {
        mLoadMoreDelegate?.refresh()
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data)
    }

}