package com.lingmiao.shop.business.goods.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lingmiao.shop.R

class PhotoListAdapter : ListAdapter<Int, PhotoListAdapter.VH>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

        }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_photo_list_cell_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.goods_adapter_goods_info_picture, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position))
            .into(holder.imageView)
    }


}