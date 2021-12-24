package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
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


    //是否已经加载Fragment
    private var isFragmentExited: Boolean = false

    //是否处于编辑状态 true 是  false 否
    private val isEdited: MutableLiveData<Boolean> = MutableLiveData()

    //置顶菜单
    private var firstAdapter: SimpleMenuAdapter? = null
    var firstStart: Int = -1

    //常用菜单
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
        showDialogLoading()
        mPresenter?.loadLv1GoodsGroup(1, true)

        //默认完成状态中
        isEdited.value = false

        isEdited.observe(this, {
            if (it) {
                mToolBarDelegate?.setRightText("完成") { isEdited.value = isEdited.value == false }
                //编辑状态中
                //置顶
                mBinding.menuAddOne.visiable()
                //常用
                mBinding.menuAddTwo.visiable()


                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                firstAdapter?.notifyDataSetChanged()

                //常用菜单
                secondAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                secondAdapter?.notifyDataSetChanged()

                //二级菜单
//                if (viewModel.item.value?.children?.isNotEmpty() == true) {
//                    thirdAdapter?.data?.let { list ->
//                        for (i in list) {
//                            i.isdeleted = true
//                        }
//                    }
//                    thirdAdapter?.notifyDataSetChanged()
//                }
            } else {
                mToolBarDelegate?.setRightText("编辑") { isEdited.value = isEdited.value == false }
                //完成状态中
                //置顶
                mBinding.menuAddOne.gone()
                //常用
                mBinding.menuAddTwo.gone()


                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                firstAdapter?.notifyDataSetChanged()


                //常用菜单
                secondAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                secondAdapter?.notifyDataSetChanged()

                //二级菜单
//                if (viewModel.item.value?.children?.isNotEmpty() == true) {
//                    thirdAdapter?.data?.let { list ->
//                        for (i in list) {
//                            i.isdeleted = false
//                        }
//                    }
//                    thirdAdapter?.notifyDataSetChanged()
//                }
            }
        })

        //设置置顶菜单
        initFirstMenu()

        //设置常用菜单
        initSecondMenu()

        //设置二级菜单
        initThirdMenu()
    }

    //置顶菜单
    private fun initFirstMenu() {

        firstAdapter = SimpleMenuAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.deleteIv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, {
                                mPresenter?.deleteGoodsGroup(
                                    firstAdapter?.getItem(position),
                                    position,
                                    1
                                )
                            })
                    }
                }
            }
        }

        //添加置顶菜单
        mBinding.menuAddOne.singleClick {
            MenuEditActivity.openActivity(this, ShopGroupVO.LEVEL_1, null, null)
        }


        //知道菜单排序
        val listenerOne: OnItemDragListener = object : OnItemDragListener {

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
        firstAdapter?.setOnItemDragListener(listenerOne)
        firstAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvOne.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvOne.adapter = firstAdapter

        //查看商品
        firstAdapter?.setOnItemClickListener { adapter, _, position ->
            if (isEdited.value == true) {
                //编辑中
                MenuEditActivity.openActivity(
                    this,
                    ShopGroupVO.LEVEL_1,
                    firstAdapter?.getItem(position)?.shopCatPid,
                    firstAdapter?.getItem(position)
                )
            } else {
                firstAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                firstAdapter?.notifyDataSetChanged()
                viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
            }
        }

    }

    //常用菜单
    private fun initSecondMenu() {

        secondAdapter = SimpleMenuAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.deleteIv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, {
                                mPresenter?.deleteGoodsGroup(
                                    secondAdapter?.getItem(position),
                                    position,
                                    0
                                )
                            })
                    }
                }
            }
        }

        //添加常用菜单
        mBinding.menuAddTwo.singleClick {
            UserMenuEditActivity.openActivity(this, "0", null, 0)
        }

        //常用菜单排序
        val listenerTwo: OnItemDragListener = object : OnItemDragListener {

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取初始位置
                secondStart = pos
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

                    val pre = secondAdapter?.data?.get(pos - 1)
                    val item = secondAdapter?.data?.get(pos)
                    if (item != null && pre != null) {
                        mPresenter?.sort(0, item.shopCatId!!, pre.sort + 1)
                    }
                } else {
                    //到顶了
                    //获取第一个Item
                    val item = secondAdapter?.data?.get(pos)
                    if (item != null) {
                        mPresenter?.sort(0, item.shopCatId!!, 0)
                    }
                }
            }
        }

        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(secondAdapter)
        val mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(mBinding.rvTwo)
        secondAdapter?.setOnItemDragListener(listenerTwo)
        secondAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvTwo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTwo.adapter = secondAdapter

        //查看商品
        secondAdapter?.setOnItemClickListener { adapter, _, position ->
            if (isEdited.value == true) {
                //编辑中
                UserMenuEditActivity.openActivity(
                    this,
                    secondAdapter?.getItem(position)?.shopCatPid,
                    secondAdapter?.getItem(position),
                    0
                )
            } else {
                secondAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                secondAdapter?.notifyDataSetChanged()
                viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
            }
        }

    }

    //二级菜单
    private fun initThirdMenu() {
        //二级菜单
        thirdAdapter = SimpleMenuTwoAdapter()


        thirdAdapter?.setOnItemClickListener { adapter, _, position ->
            if (viewModel.item.value?.isTop == 0) {
                thirdAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                thirdAdapter?.notifyDataSetChanged()
                val item = adapter.data[position] as ShopGroupVO
                item.isSecondMenu = true
                viewModel.setShopGroup(item)
            }
        }


        mBinding.rvThree.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvThree.adapter = thirdAdapter


        //监听一级菜单的变化
        viewModel.item.observe(this, {

            if (it.children == null || (it.children?.isEmpty() == true)) {
                if (!it.isSecondMenu) {
                    val list: List<ShopGroupVO> = listOf(it)
                    thirdAdapter?.replaceData(list)
                    thirdAdapter?.notifyDataSetChanged()
                }

            } else {
                val list: List<ShopGroupVO> = it.children!!
                thirdAdapter?.replaceData(list)
                thirdAdapter?.notifyDataSetChanged()
            }
        })
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

            if (isEdited.value == true) {
                //编辑状态
                //常用菜单
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

            secondAdapter?.replaceData(list)
        }

        //加载Fragment
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
    override fun onDeleteGroupSuccess(position: Int, isTop: Int) {
        if (isTop == 1) {
            //置顶
            if (position < firstAdapter?.data?.size!!) {
                firstAdapter?.data?.removeAt(position)
                firstAdapter?.notifyDataSetChanged()
            }
        } else {
            //常用
            if (position < secondAdapter?.data?.size!!) {
                secondAdapter?.data?.removeAt(position)
                secondAdapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mPresenter?.loadLv1GoodsGroup(1, true)
    }

}