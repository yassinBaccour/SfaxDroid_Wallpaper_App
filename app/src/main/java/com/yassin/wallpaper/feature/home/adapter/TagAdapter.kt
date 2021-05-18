package com.yassin.wallpaper.feature.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.TagView
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.InflateTagBinding

class TagAdapter(
    private var tagList: MutableList<TagView>,
    private val clickListener: (TagView) -> Unit
) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TagViewHolder(
            InflateTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(
        holder: TagViewHolder,
        position: Int
    ) {
        val item = tagList[position]
        holder.bindView(item)
        if (selectedPosition == position) {
            holder.selectedBg()
        } else {
            holder.defaultBg()
        }
        holder.mLayout.setOnClickListener {
            clickListener(item)
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = tagList.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<TagView>) {
        tagList.clear()
        tagList = list.toMutableList()
        notifyDataSetChanged()
    }

    class TagViewHolder(val binding: InflateTagBinding) : RecyclerView.ViewHolder(binding.root) {

        val mLayout: LinearLayout = binding.itemTagMain

        fun bindView(tagView: TagView) {
            binding.itemContent.text = tagView.name
        }

        fun defaultBg() {
            binding.root.background = AppCompatResources.getDrawable(
                itemView.context,
                R.drawable.round_corner_tag_unselected
            )
        }

        fun selectedBg() {
            binding.root.background =
                AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.round_corner_tag_selected
                )

        }
    }
}
