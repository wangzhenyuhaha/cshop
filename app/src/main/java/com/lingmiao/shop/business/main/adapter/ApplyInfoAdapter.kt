package com.lingmiao.shop.business.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R


class ApplyInfoAdapter(private val liveData: MutableLiveData<Int>) :
    ListAdapter<String, ApplyInfoAdapter.ApplyViewHolder>(DIFF) {

    companion object {
        //比较
        object DIFF : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == oldItem

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                oldItem == newItem
        }
    }

    val click: (Int) -> Unit = {
        liveData.value = it
    }

    class ApplyViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val textView: TextView =
            itemView.findViewById(R.id.main_adapter_apply_info_textView)

        private var onClick: ((Int) -> Unit)? = null

        init {
            textView.setOnClickListener {
                onClick?.invoke(adapterPosition)
            }
        }

        fun bind(item: String, click: (Int) -> Unit) {
            onClick = click
            textView.text = item
        }

        companion object {
            fun create(parent: ViewGroup): ApplyViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.main_adapter_apply_info, parent, false)
                return ApplyViewHolder(view)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplyViewHolder {
        return ApplyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ApplyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, click)
    }

}