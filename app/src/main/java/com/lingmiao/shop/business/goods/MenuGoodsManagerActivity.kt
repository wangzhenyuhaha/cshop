package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.SimpleMenuAdapter
import com.lingmiao.shop.business.goods.adapter.SimpleMenuTwoAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.fragment.GoodsMenuFragment
import com.lingmiao.shop.business.goods.presenter.MenuGoodsManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.MenuGoodsManagerPreImpl
import com.lingmiao.shop.databinding.ActivityMenuGoodsManagerBinding

@SuppressLint("NotifyDataSetChanged")
class MenuGoodsManagerActivity :
    BaseVBActivity<ActivityMenuGoodsManagerBinding, MenuGoodsManagerPre>(),
    MenuGoodsManagerPre.View {

    private val viewModel by viewModels<MenuGoodsManagerViewModel>()

    //选中的一级菜单
    private var selectedGroup: ShopGroupVO? = null

    //是否已经加载Fragment
    private var isFragmentExited: Boolean = false

    //是否处于编辑状态 true 是  false 否
    private val isEdited: MutableLiveData<Boolean> = MutableLiveData()

    //置顶菜单
    private var firstAdapter: SimpleMenuAdapter? = null
    var firstStart: Int = -1

    //常用菜单
    private var secondTop: List<ShopGroupVO>? = null
    private var secondAdapter: SimpleMenuAdapter? = null
    var secondStart: Int = -1

    //二级菜单
    private var thirdAdapter: SimpleMenuTwoAdapter? = null

    override fun createPresenter() = MenuGoodsManagerPreImpl(this, this)

    override fun useLightMode() = false

    override fun getViewBinding() = ActivityMenuGoodsManagerBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("菜单管理")

        //加载已有菜单数据
        mPresenter?.loadLv1GoodsGroup(1, true)

        //默认完成状态中
        isEdited.value = false

        isEdited.observe(this, {
            if (it) {
                mToolBarDelegate?.setRightText("完成") { isEdited.value = isEdited.value == false }
                //编辑状态中
                mBinding.menuAddOne.visiable()

                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                firstAdapter?.notifyDataSetChanged()

                //常用菜单

                //二级菜单
                if (viewModel.item.value?.children?.isNotEmpty() == true) {
                    thirdAdapter?.data?.let { list ->
                        for (i in list) {
                            i.isdeleted = true
                        }
                    }
                    thirdAdapter?.notifyDataSetChanged()
                }
            } else {
                mToolBarDelegate?.setRightText("编辑") { isEdited.value = isEdited.value == false }
                //完成状态中
                mBinding.menuAddOne.gone()

                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                firstAdapter?.notifyDataSetChanged()


                //常用菜单

                //二级菜单
                if (viewModel.item.value?.children?.isNotEmpty() == true) {
                    thirdAdapter?.data?.let { list ->
                        for (i in list) {
                            i.isdeleted = false
                        }
                    }
                    thirdAdapter?.notifyDataSetChanged()
                }
            }
        })


        //置顶菜单
        firstAdapter = SimpleMenuAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.getItem(position) as ShopGroupVO
                when (view.id) {
                    R.id.deleteIv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, View.OnClickListener {
                                mPresenter?.deleteGoodsGroup(
                                    firstAdapter?.getItem(position),
                                    position
                                )
                            })
                    }

                }
            }
        }

        //编辑置顶菜单
        firstAdapter?.setOnItemClickListener { adapter, view, position ->
            if (isEdited.value == true) {
                //编辑中
                showToast("正在编")
            } else {

                firstAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                firstAdapter?.notifyDataSetChanged()
                viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
            }
        }

        //添加置顶菜单
        mBinding.menuAddOne.singleClick {
            MenuEditActivity.openActivity(this, ShopGroupVO.LEVEL_1, null, null)
        }

        val listener: OnItemDragListener = object : OnItemDragListener {

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取初始位置
                firstStart = pos
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {
                //nothing to do
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取到达的位置
                //pos为Item到达的位置
                if (pos > 0) {

                    val pre = firstAdapter?.data?.get(pos - 1)
                    val item = firstAdapter?.data?.get(pos)
                    if (item != null && pre != null) {
                        mPresenter?.sort(1, item.shopCatId!!, pre.sort + 1)
                    }
                } else {
                    //到顶了
                    //获取第一个Item
                    val item = firstAdapter?.data?.get(pos)
                    if (item != null) {
                        mPresenter?.sort(1, item.shopCatId!!, 0)
                    }
                }
            }
        }

        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(firstAdapter)
        val mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(mBinding.rvOne)
        firstAdapter?.setOnItemDragListener(listener)
        firstAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvOne.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvOne.adapter = firstAdapter


        //常用菜单
        secondAdapter = SimpleMenuAdapter()
        mBinding.rvTwo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTwo.adapter = secondAdapter

        //二级菜单
        thirdAdapter = SimpleMenuTwoAdapter()
        mBinding.rvThree.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvThree.adapter = thirdAdapter
        //监听一级菜单的变化
        viewModel.item.observe(this, {

            if (it.children == null || (it.children?.isEmpty() == true)) {
                val list: List<ShopGroupVO> = listOf(it)
                thirdAdapter?.replaceData(list)
                thirdAdapter?.notifyDataSetChanged()
            } else {

            }
        })


// mLoadMoreRv.smoothScrollToPosition(toPosition)
        //先加载置顶菜单


    }

    //加载一级菜单成功
    override fun onLoadLv1GoodsGroupSuccess(
        list: List<ShopGroupVO>,
        isTop: Int,
        isSecond: Boolean
    ) {

        if (isTop == 1) {
            if (isEdited.value == true) {
                //编辑状态
                //置顶菜单
                list.let {
                    for (i in it) {
                        i.isdeleted = true
                    }
                }
            } else {
                list.let {
                    for (i in it) {
                        i.isdeleted = false
                    }
                }
            }

            firstAdapter?.replaceData(list)
            if (isSecond) {
                mPresenter?.loadLv1GoodsGroup(0, false)
            }

        } else {
            secondTop = list
            secondAdapter?.replaceData(list)
        }

        if (!isFragmentExited) {
            //添加Fragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<GoodsMenuFragment>(R.id.fragment)
            }
            isFragmentExited = true
            firstAdapter?.data?.get(0)?.let { viewModel.setShopGroup(it) }
        }

        hideDialogLoading()
    }

    //排序成功
    override fun onSortSuccess(isTop: Int) {
        mPresenter?.loadLv1GoodsGroup(isTop, false)
    }

    //删除一级菜单成功
    override fun onDeleteGroupSuccess(position: Int) {
        if (position < firstAdapter?.data?.size!!) {
            firstAdapter?.data?.removeAt(position)
            firstAdapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.loadLv1GoodsGroup(1, true)
    }
}