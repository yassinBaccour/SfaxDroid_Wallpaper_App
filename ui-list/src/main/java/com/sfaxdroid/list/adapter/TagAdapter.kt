package com.sfaxdroid.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.list.R
import com.sfaxdroid.list.databinding.InflateTagBinding

class TagAdapter(
    private var tagList: MutableList<TagView>,
    private val setSelectedItemPosition: (Int) -> Unit,
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
            setSelectedItemPosition(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = tagList.size

    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
    }

    fun getSelectedPosition() = selectedPosition

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<TagView>) {
        if (list.isNotEmpty()) {
            tagList.clear()
            tagList = list.toMutableList()
            notifyDataSetChanged()
        }
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
