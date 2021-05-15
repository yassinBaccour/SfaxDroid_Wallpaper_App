package com.yassin.wallpaper.feature.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.TagView
import com.yassin.wallpaper.R

class TagAdapter(
    private var tagList: MutableList<TagView>,
    private val clickListener: (TagView) -> Unit
) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TagViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.inflate_tag,
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

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mTxtLabel: TextView = itemView.findViewById(R.id.item_content)
        val mLayout: LinearLayout = itemView.findViewById(R.id.item_tag_main)

        fun bindView(tagView: TagView) {
            mTxtLabel.text = tagView.name
        }

        fun defaultBg() {
            itemView.background =
                itemView.context.getDrawable(R.drawable.round_corner_tag_unselected)
        }

        fun selectedBg() {
            itemView.background = itemView.context.getDrawable(R.drawable.round_corner_tag_selected)
        }
    }
}
