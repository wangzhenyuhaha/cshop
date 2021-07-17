package com.lingmiao.shop.business.common.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.R
import java.io.File

class PhotoListAdapter : ListAdapter<String, PhotoListAdapter.VH>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_photo_list_adapter_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_list_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        if (item.isNetUrl())
        { Glide.with(holder.imageView)
            .load(item)
            .into(holder.imageView)
        }else{
            Glide.with(holder.imageView)
                .load(File(item))
                .into(holder.imageView)
        }
    }


}